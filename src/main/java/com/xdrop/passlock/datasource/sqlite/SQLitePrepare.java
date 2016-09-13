package com.xdrop.passlock.datasource.sqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLitePrepare {

    private final static Logger LOG = LoggerFactory.getLogger(SQLitePrepare.class);

    /**
     * Creates the SQLite passwords table
     */
    public static void createPassTable() {

        Connection sqLiteConnection = SQLiteConnection.connect();

        String sql =
                "CREATE TABLE IF NOT EXISTS passwords (id integer PRIMARY KEY ,\n" +
                "ref TEXT UNIQUE NOT NULL, description TEXT, payload TEXT NOT NULL, salt TEXT NOT NULL,\n" +
                "algo TEXT, iv TEXT NOT NULL, date_created INTEGER, last_updated INTEGER)";

        performSingleTransaction(sqLiteConnection, sql);

    }

    /**
     * Resets (deletes and recreates) the passwords table
     */
    public static void resetTable() {

        Connection sqLiteConnection = SQLiteConnection.connect();

        String sql = "DROP TABLE IF EXISTS passwords";

        performSingleTransaction(sqLiteConnection, sql);

        createPassTable();

    }

    private static void performSingleTransaction(Connection sqLiteConnection, String sql) {
        Statement statement = null;

        try {

            statement = sqLiteConnection.createStatement();
            statement.executeUpdate(sql);
            statement.close();

        } catch (SQLException e) {

            LOG.debug("SQL exception occurred", e);

        } finally {
           /* try {
                sqLiteConnection.close();
            } catch (SQLException e) {
                LOG.debug("Failed to close con", e);
            }*/
        }
    }


}
