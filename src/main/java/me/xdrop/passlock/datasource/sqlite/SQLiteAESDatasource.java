package me.xdrop.passlock.datasource.sqlite;

import me.xdrop.passlock.crypto.aes.AESEncryptionData;
import me.xdrop.passlock.datasource.Datasource;
import me.xdrop.passlock.exceptions.AlreadyExistsException;
import me.xdrop.passlock.exceptions.InvalidDataException;
import me.xdrop.passlock.exceptions.RefNotFoundException;
import me.xdrop.passlock.model.BufferedProcessor;
import me.xdrop.passlock.model.PasswordEntry;
import me.xdrop.passlock.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteAESDatasource implements Datasource<AESEncryptionData> {

    private final static Logger LOG = LoggerFactory.getLogger(SQLiteAESDatasource.class);

    private Connection con;

    public SQLiteAESDatasource(String path) {

        String jdbcUrl = "jdbc:sqlite:" + path;
        LOG.debug("JDBC: " + jdbcUrl);
        con = SQLiteConnection.connect(jdbcUrl);

    }

    public PasswordEntry<AESEncryptionData> getPass(String ref) throws RefNotFoundException {

        String sql = "SELECT * FROM passwords WHERE ref = ? LIMIT 1";

        PasswordEntry<AESEncryptionData> passwordEntry = new PasswordEntry<>();
        AESEncryptionData encryptionData = new AESEncryptionData();

        try {

            PreparedStatement statement = con.prepareStatement(sql);

            statement.setString(1, ref);
            statement.execute();

            ResultSet rs = statement.getResultSet();

            if (rs.next()) {

                /* TODO: Add rest of fields eg. date */
                passwordEntry.setId(rs.getString("id"));
                passwordEntry.setDescription(rs.getString("description"));
                passwordEntry.setRef(rs.getString("ref"));

                encryptionData.setInitilizationVector(ByteUtils.fromBase64(rs.getString("iv")));
                encryptionData.setSalt(ByteUtils.fromBase64(rs.getString("salt")));
                encryptionData.setEncryptedPayload(ByteUtils.fromBase64(rs.getString("payload")));

                passwordEntry.setEncryptionData(encryptionData);

                assert (passwordEntry.getRef().equals(ref));
                statement.close();

                return passwordEntry;

            } else {

                statement.close();
                throw new RefNotFoundException();

            }

        } catch (SQLException e) {
            LOG.debug("SQL failed to get", e);
        }

        return null;

    }

    @Override
    public List<String> getPassList() {

        String sql = "SELECT ref FROM passwords";
        List<String> results = new ArrayList<>();

        try {

            PreparedStatement statement = con.prepareStatement(sql);

            statement.execute();

            ResultSet rs = statement.getResultSet();

            while (rs.next()) {

                String ref = rs.getString("ref");
                results.add(ref);

            }

            LOG.debug("SQL retrieved " + results.size() + " results");

            statement.close();

            return results;

        } catch (SQLException e) {
            LOG.debug("SQL failed to retrieve list", e);
        }

        return null;
    }

    @Override
    public int getSize() {

        String sql = "SELECT COUNT(*),'test' FROM passwords";
        ResultSet rs;
        PreparedStatement statement;

        try {

            statement = con.prepareStatement(sql);
            statement.execute();
            rs = statement.getResultSet();
            int result= 0;

            if (rs.next()) {

                /* return the count */
                result = rs.getInt(1);
            }

            rs.close();
            statement.close();

            return result;

        } catch (SQLException e) {
            LOG.info("Failed to get size", e);
        }

        return 0;
    }

    @Override
    public int bufferedUpdate(BufferedProcessor<PasswordEntry<AESEncryptionData>> bufferedProcessor) throws Exception {

        int bufferSize = bufferedProcessor.getBufferSize();
        int pages = (int) Math.ceil((double) getSize() / bufferSize);
        int recordsProcessed = 0;

        for (int offset = 1; offset <= pages; offset++) {

            List<PasswordEntry<AESEncryptionData>> batch;
            List<PasswordEntry<AESEncryptionData>> processed;

            batch = batchSelect(bufferSize, offset);

            if(batch == null)
                return 0;

            recordsProcessed += batch.size();
            bufferedProcessor.receive(batch);

            try {

                processed = bufferedProcessor.process();

            } catch (Exception e) {
                LOG.debug("Buffered update error", e);
                throw new Exception();
            }

            bufferedProcessor.send(processed);

        }

        return recordsProcessed;
    }

    private List<PasswordEntry<AESEncryptionData>> batchSelect(int batchSize, int offset) {

        String sql = "SELECT * FROM passwords LIMIT ? OFFSET ?";

        try{

            PreparedStatement statement = con.prepareStatement(sql);

            /* limit the number of rows returned according to the
             * buffer size */
            statement.setInt(1, batchSize);

            /* offset's the batch select by BatchSize * (Offset - 1) */
            statement.setInt(2, batchSize * (offset - 1));

            statement.execute();

            ResultSet rs = statement.getResultSet();

            List<PasswordEntry<AESEncryptionData>> batch = new ArrayList<>();

            while (rs.next()) {

                PasswordEntry<AESEncryptionData> passwordEntry = new PasswordEntry<>();
                AESEncryptionData encryptionData = new AESEncryptionData();

                /* TODO: Add rest of fields eg. date */
                passwordEntry.setId(rs.getString("id"));
                passwordEntry.setDescription(rs.getString("description"));
                passwordEntry.setRef(rs.getString("ref"));

                encryptionData.setInitilizationVector(ByteUtils.fromBase64(rs.getString("iv")));
                encryptionData.setSalt(ByteUtils.fromBase64(rs.getString("salt")));
                encryptionData.setEncryptedPayload(ByteUtils.fromBase64(rs.getString("payload")));

                passwordEntry.setEncryptionData(encryptionData);

                batch.add(passwordEntry);

            }

            statement.close();

            return batch;

        } catch (SQLException e) {
            LOG.info("Failed to batch select", e);
        }

        return null;

    }

    public void delPass(String ref) throws RefNotFoundException {

        String sql = "DELETE FROM passwords WHERE ref=?";

        try {

            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, ref);

            int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (affectedRows < 1) {
                throw new RefNotFoundException();
            }

        } catch (SQLException e) {
            LOG.info("Failed to delete", e);
        }

    }

    public void updatePass(String ref, PasswordEntry<AESEncryptionData> newPasswordEntry) throws RefNotFoundException {

        if (!validate(newPasswordEntry)) throw new InvalidDataException();

        String sql = "UPDATE passwords SET ref=?, description=?, payload=?, salt=?, iv=?, algo=? " +
                "WHERE ref=?";
        AESEncryptionData aesEncryptionData = newPasswordEntry.getEncryptionData();

        try {

            PreparedStatement preparedStatement = bindPreparedStatement(newPasswordEntry, sql, aesEncryptionData);

            preparedStatement.setString(7, ref);

            int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (affectedRows < 1) {
                throw new RefNotFoundException();
            }

        } catch (SQLException e) {
            LOG.info("Failed to update", e);
        }

    }

    public void addPass(String ref, PasswordEntry<AESEncryptionData> passwordEntry) throws AlreadyExistsException {

        if (!validate(passwordEntry)) throw new InvalidDataException();

        String sql = "INSERT INTO passwords (ref, description, payload, salt, iv, algo) VALUES (?,?,?,?,?,?)";
        AESEncryptionData aesEncryptionData = passwordEntry.getEncryptionData();

        try {

            PreparedStatement preparedStatement = bindPreparedStatement(passwordEntry, sql, aesEncryptionData);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {

            if (e.getErrorCode() == 19) {

                throw new AlreadyExistsException();

            }

            LOG.info("SQL add exception", e);

        }

    }

    private PreparedStatement bindPreparedStatement(PasswordEntry<AESEncryptionData> passwordEntry, String sql, AESEncryptionData aesEncryptionData) throws SQLException {

        PreparedStatement preparedStatement = con.prepareStatement(sql);

        preparedStatement.setString(1, passwordEntry.getRef());
        preparedStatement.setString(2, passwordEntry.getDescription());
        preparedStatement.setString(3, ByteUtils.toBase64(aesEncryptionData.getEncryptedPayload()));
        preparedStatement.setString(4, ByteUtils.toBase64(aesEncryptionData.getSalt()));
        preparedStatement.setString(5, ByteUtils.toBase64(aesEncryptionData.getInitilizationVector()));
        preparedStatement.setString(6, "AESwPBKDF2");

        return preparedStatement;

    }

    @Override
    public boolean isCreated() {

        // for now, we are lazy

        try {
            con.createStatement().execute("SELECT * FROM passwords");
        }  catch (SQLException e) {
            return false;
        }

        return true;

    }

    @Override
    public void initialize() {

        SQLitePrepare.createPassTable(con);

    }

    @Override
    public void reset() {

        SQLitePrepare.resetTable(con);

    }

    private boolean validate(PasswordEntry<AESEncryptionData> passwordEntry) {

        if (passwordEntry == null) return false;

        AESEncryptionData aesEncryptionData;

        if (passwordEntry.getEncryptionData() == null) return false;
        else aesEncryptionData = passwordEntry.getEncryptionData();

        return passwordEntry.getRef() != null &&
                aesEncryptionData.getInitilizationVector() != null &&
                aesEncryptionData.getEncryptedPayload() != null &&
                aesEncryptionData.getSalt() != null;

    }

}
