package com.xdrop.passlock.datasource.sqlite;

import com.xdrop.passlock.crypto.aes.AESEncryptionData;
import com.xdrop.passlock.datasource.Datasource;
import com.xdrop.passlock.exceptions.InvalidDataException;
import com.xdrop.passlock.exceptions.RefNotFoundException;
import com.xdrop.passlock.model.PasswordEntry;
import com.xdrop.passlock.search.FuzzySearcher;
import com.xdrop.passlock.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteAESDatasource implements Datasource<AESEncryptionData> {

    private final static Logger LOG = LoggerFactory.getLogger(SQLiteAESDatasource.class);

    private Connection con = SQLiteConnection.connect();

    public PasswordEntry<AESEncryptionData> getPass(String ref) throws RefNotFoundException {

        String sql = "SELECT * FROM passwords WHERE ref = ? LIMIT 1";

        PasswordEntry<AESEncryptionData> passwordEntry = new PasswordEntry<>();
        AESEncryptionData encryptionData = new AESEncryptionData();

        try{

            PreparedStatement statement = con.prepareStatement(sql);

            statement.setString(1, ref);
            statement.execute();

            ResultSet rs = statement.getResultSet();

            if(rs.next()){
                passwordEntry.setId(rs.getString("id"));
                // passwordEntry.setDate(rs.getInt("date"));
                passwordEntry.setDescription(rs.getString("description"));
                passwordEntry.setRef(rs.getString("ref"));

                encryptionData.setInitilizationVector(ByteUtils.fromBase64(rs.getString("iv")));
                encryptionData.setSalt(ByteUtils.fromBase64(rs.getString("salt")));
                encryptionData.setEncryptedPayload(ByteUtils.fromBase64(rs.getString("payload")));

                passwordEntry.setEncryptionData(encryptionData);

                assert(passwordEntry.getRef().equals(ref));
                statement.close();

                return passwordEntry;
            } else{
                statement.close();
                throw new RefNotFoundException();
            }

        } catch (SQLException e) {
            LOG.debug("SQL failed to get", e);
        }

        return null;

    }

    public PasswordEntry<AESEncryptionData> getPass(String fuzzyRef, FuzzySearcher fuzzySearcher)
            throws RefNotFoundException {

        String sql = "SELECT ref FROM passwords";

        try{

            Statement statement = con.createStatement();
            statement.execute(sql);

            ResultSet rs = statement.getResultSet();


            List<String> candidates = new ArrayList<>();

            while(rs.next()){
                candidates.add(rs.getString("ref"));
            }

            statement.close();

            String ref = fuzzySearcher.search(fuzzyRef, candidates);

            if(ref == null){
                throw new RefNotFoundException();
            }

            return getPass(ref);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delPass(String ref) throws RefNotFoundException {

        String sql = "DELETE FROM passwords WHERE ref=?";

        try{

            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, ref);

            int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();

            if(affectedRows < 1){
                throw new RefNotFoundException();
            }

        } catch (SQLException e) {

            LOG.info("Failed to delete", e);

        }

    }

    public void updatePass(String ref, PasswordEntry<AESEncryptionData> newPasswordEntry) throws RefNotFoundException {

        if(!validate(newPasswordEntry)) throw new InvalidDataException();

        String sql = "UPDATE passwords SET ref=?, description=?, payload=?, salt=?, iv=?, algo=?" +
                "WHERE ref=?";
        AESEncryptionData aesEncryptionData = newPasswordEntry.getEncryptionData();

        try{

            PreparedStatement preparedStatement = bindPreparedStatement(ref, newPasswordEntry, sql, aesEncryptionData);

            preparedStatement.setString(7, ref);

            int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();

            if(affectedRows < 1){
                throw new RefNotFoundException();
            }


        } catch (SQLException e) {
            LOG.info("Failed to update", e);
        }


    }

    public void addPass(String ref, PasswordEntry<AESEncryptionData> passwordEntry) {

        if(!validate(passwordEntry)) throw new InvalidDataException();

        String sql = "INSERT INTO passwords (ref, description, payload, salt, iv, algo) VALUES (?,?,?,?,?,?)";
        AESEncryptionData aesEncryptionData = passwordEntry.getEncryptionData();

        try{

            PreparedStatement preparedStatement = bindPreparedStatement(ref, passwordEntry, sql, aesEncryptionData);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {

            LOG.info("SQL add exception", e);

        }

    }

    private PreparedStatement bindPreparedStatement(String ref, PasswordEntry<AESEncryptionData> passwordEntry, String sql, AESEncryptionData aesEncryptionData) throws SQLException {

        PreparedStatement preparedStatement = con.prepareStatement(sql);

        preparedStatement.setString(1, ref);
        preparedStatement.setString(2, passwordEntry.getDescription());
        preparedStatement.setString(3, ByteUtils.toBase64(aesEncryptionData.getEncryptedPayload()));
        preparedStatement.setString(4, ByteUtils.toBase64(aesEncryptionData.getSalt()));
        preparedStatement.setString(5, ByteUtils.toBase64(aesEncryptionData.getInitilizationVector()));
        preparedStatement.setString(6, "AESwPBKDF2");

        return preparedStatement;

    }


    @Override
    public void initialize() {

        SQLitePrepare.createPassTable();

    }

    @Override
    public void reset() {

        SQLitePrepare.resetTable();

    }


    private boolean validate(PasswordEntry<AESEncryptionData> passwordEntry){

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
