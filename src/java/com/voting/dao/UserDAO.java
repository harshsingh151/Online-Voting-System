package com.voting.dao;

import com.voting.model.User;
import com.voting.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class UserDAO {

    private static final String COUNT_TOTAL_VOTERS_SQL = "SELECT COUNT(id) FROM users";

    public int countTotalVoters() {
        int count = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(COUNT_TOTAL_VOTERS_SQL);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1); // Get the value from the first column
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
        return count;
    }
    
    // SQL Statements
    private static final String REGISTER_USER_SQL = 
        "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
    private static final String VALIDATE_USER_SQL = 
        "SELECT id, name, password, has_voted FROM users WHERE email = ?";
    
    // NOTE: For a real system, you would use a proper HASHING library (like BCrypt) 
    // for password storage and comparison. For this starter project, we will use plain text 
    // passwords, but be aware of the security risk.

    /**
     * Registers a new user into the database.
     * @param user The User object containing name, email, and password.
     * @return true if registration is successful, false otherwise (e.g., email already exists).
     */
    
    // Inside UserDAO.java
// (Add this method along with registerUser and validateLogin)

    private static final String SELECT_ALL_VOTERS_SQL = 
        "SELECT id, name, email, has_voted, registration_date FROM users ORDER BY registration_date DESC";

    /**
     * Retrieves a list of all registered users (voters).
     * @return A List of User objects.
     */
    public List<User> getAllVoters() {
        List<User> voters = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_ALL_VOTERS_SQL);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setHasVoted(rs.getBoolean("has_voted"));
                // You might need to add registration_date to your User.java model if you want to display it
                voters.add(user);
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
        return voters;
    }
    public boolean registerUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DBConnection.getConnection();
            
            // 1. Prepare the SQL statement
            preparedStatement = connection.prepareStatement(REGISTER_USER_SQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword()); // Storing plain password for simplicity

            // 2. Execute the statement
            int rowsAffected = preparedStatement.executeUpdate();
            
            // If one row was inserted, registration was successful
            return rowsAffected == 1;
            
        } catch (SQLException e) {
            // Check for duplicate entry error (specific to MySQL email unique constraint)
            if (e.getErrorCode() == 1062) { 
                System.err.println("Registration failed: Email already exists: " + user.getEmail());
                return false;
            }
            e.printStackTrace();
            return false;
        } finally {
            // 3. Close resources
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection(connection);
        }
    }

    /**
     * Validates user credentials for login.
     * @param email The user's email address.
     * @param password The user's provided password.
     * @return The User object if login is successful, null otherwise.
     */
    public User validateLogin(String email, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(VALIDATE_USER_SQL);
            preparedStatement.setString(1, email);

            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                // User found, now compare passwords (plain text for this example)
                String storedPassword = rs.getString("password");
                
                if (password.equals(storedPassword)) {
                    // Password matches, create and populate User object
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(email);
                    user.setHasVoted(rs.getBoolean("has_voted"));
                    // Do NOT set the password on the object passed to the frontend/session
                }
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
        return user;
    }
    
}