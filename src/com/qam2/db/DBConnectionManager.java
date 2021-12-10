package com.qam2.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class providing database connection.
 * @author Alex Hanson.
 */
public final class DBConnectionManager {

    // Connection config vars
    private static final String CONNECTION_URL = "jdbc:mysql://localhost/client_schedule";
    private static final String USER = "sqlUser";
    private static final String PWD = "Passw0rd!";

    private static DBConnectionManager instance;

    private Connection con;

    private DBConnectionManager() {}

    /**
     * @return The single instance of the DBConnectionManager.
     */
    public static DBConnectionManager getInstance() {
         return instance == null ? (instance = new DBConnectionManager()) : instance;
    }

    private void openConnection() {
        try {
            con = DriverManager.getConnection(CONNECTION_URL, USER, PWD);
        } catch(SQLException e) {
            System.err.println(e);
        }
    }

    /**
     * @return The single instance of the database Connection.
     */
    public Connection getConnection() {
        if(con == null) {
            openConnection();
        }
        return con;
    }

    /**
     * Closes the database Connection.
     */
    public void close() {
        try {
            if(con != null)
                con.close();
        } catch(SQLException e) {
            System.err.println(e);
        }
    }
}
