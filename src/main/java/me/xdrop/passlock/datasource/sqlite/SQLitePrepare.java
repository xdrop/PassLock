package me.xdrop.passlock.datasource.sqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLitePrepare {

    private final static Logger LOG = LoggerFactory.getLogger(SQLitePrepare.class);

    /**
     * Creates the SQLite passwords table
     */
    public static void createPassTable(Connection sqLiteConnection) {

        String sql =
                "CREATE TABLE IF NOT EXISTS passwords (id integer PRIMARY KEY ,\n" +
                "ref TEXT UNIQUE NOT NULL, description TEXT, payload TEXT NOT NULL, salt TEXT NOT NULL,\n" +
                "algo TEXT, iv TEXT NOT NULL, date_created INTEGER, last_updated INTEGER)";

        performSingleTransaction(sqLiteConnection, sql);

    }

    /**
     * Resets (deletes and recreates) the passwords table
     */
    public static void resetTable(Connection sqLiteConnection) {

        String sql = "DROP TABLE IF EXISTS passwords";

        performSingleTransaction(sqLiteConnection, sql);

        createPassTable(sqLiteConnection);

    }

    private static void performSingleTransaction(Connection sqLiteConnection, String sql) {
        PreparedStatement statement = null;

        try {

            statement = sqLiteConnection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            LOG.debug("SQL exception occurred", e);
        } finally {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    LOG.debug("Failed to close statement", e);
                }
            }

        }

    }

}
