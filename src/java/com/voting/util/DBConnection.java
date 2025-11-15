package com.voting.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // IMPORTANT: UPDATE THESE VARIABLES WITH YOUR MySQL DETAILS
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/online_voting_system?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root"; // Your MySQL username
    private static final String JDBC_PASSWORD = ""; // Your MySQL password

    // Method to get a database connection
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // 1. Load the JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Establish the connection
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed.");
            e.printStackTrace();
        }
        return connection;
    }

    // Method to safely close a database connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection.");
                e.printStackTrace();
            }
        }
    }
}