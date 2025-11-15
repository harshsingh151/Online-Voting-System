package com.voting.dao;


import com.voting.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    private static final String VALIDATE_ADMIN_SQL = 
        "SELECT * FROM admin WHERE username = ? AND password = ?";

    /**
     * Validates admin credentials.
     * @param username The admin username.
     * @param password The admin password (plain text for now).
     * @return true if credentials match, false otherwise.
     */
    
    // Inside AdminDAO.java

// Add these new SQL constants at the top with the others
private static final String GET_ELECTION_STATUS_SQL = "SELECT is_active FROM election_status WHERE id = 1";
private static final String SET_ELECTION_STATUS_SQL = "UPDATE election_status SET is_active = ? WHERE id = 1";

/**
 * Checks if the election is currently active.
 * @return true if election is active, false otherwise.
 */
public boolean isElectionActive() {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    boolean isActive = false;
    
    try {
        connection = DBConnection.getConnection();
        preparedStatement = connection.prepareStatement(GET_ELECTION_STATUS_SQL);
        rs = preparedStatement.executeQuery();

        if (rs.next()) {
            isActive = rs.getBoolean("is_active");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Close resources
        try {
            if (rs != null) rs.close();
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnection.closeConnection(connection);
    }
    return isActive;
}

/**
 * Sets the election status.
 * @param isActive true to start the election, false to stop it.
 * @return true if the status was updated, false otherwise.
 */
public boolean setElectionStatus(boolean isActive) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
        connection = DBConnection.getConnection();
        preparedStatement = connection.prepareStatement(SET_ELECTION_STATUS_SQL);
        preparedStatement.setBoolean(1, isActive);
        
        return preparedStatement.executeUpdate() > 0;
        
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnection.closeConnection(connection);
    }
}
    public boolean validateAdmin(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        boolean isValid = false;
        
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(VALIDATE_ADMIN_SQL);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password); // Comparing plain password
            
            rs = preparedStatement.executeQuery();

            // If rs.next() is true, it means a matching row was found
            isValid = rs.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection(connection);
        }
        return isValid;
    }

    public boolean isResultsPublished() {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    boolean isPublished = false;

    try {
        connection = DBConnection.getConnection();
        // Assumes GET_PUBLISH_STATUS_SQL = "SELECT is_published FROM election_status WHERE id = 1"
        preparedStatement = connection.prepareStatement("SELECT is_published FROM election_status WHERE id = 1");
        rs = preparedStatement.executeQuery();

        if (rs.next()) {
            isPublished = rs.getBoolean("is_published");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnection.closeConnection(connection);
    }
    return isPublished;
}
}