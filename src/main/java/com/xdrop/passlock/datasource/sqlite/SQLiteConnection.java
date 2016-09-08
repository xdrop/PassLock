package com.xdrop.passlock.datasource.sqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private final static Logger LOG = LoggerFactory.getLogger(SQLiteConnection.class);

    private final static String jdbcUrl = "jdbc:sqlite:store.db";


    public static Connection connect() {

        Connection connection = null;

        try {

            // create a connection to the database
            connection = DriverManager.getConnection(jdbcUrl);

            LOG.info("Connection to SQLite has been established.");

        } catch (SQLException e) {

            LOG.info("Failure to connect to database", e);

        }

        return connection;
    }



}
