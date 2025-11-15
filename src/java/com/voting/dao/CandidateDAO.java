package com.voting.dao;

import com.voting.model.Candidate;
import com.voting.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAO {

    // Inside com.voting.dao.CandidateDAO.java
// Add these new SQL constants at the top
    private static final String COUNT_TOTAL_CANDIDATES_SQL = "SELECT COUNT(candidate_id) FROM candidates";
    private static final String COUNT_TOTAL_VOTES_SQL = "SELECT SUM(vote_count) FROM candidates";

// Add this new method
    public int countTotalCandidates() {
        int count = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(COUNT_TOTAL_CANDIDATES_SQL);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
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

// Add this second new method
    public int countTotalVotesCast() {
        int count = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(COUNT_TOTAL_VOTES_SQL);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
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
    
    private static final String INSERT_CANDIDATE_SQL = 
        "INSERT INTO candidates (name, party, symbol_url) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_CANDIDATES_SQL = 
        "SELECT candidate_id, name, party, symbol_url, vote_count FROM candidates ORDER BY name";
    private static final String DELETE_CANDIDATE_SQL = 
        "DELETE FROM candidates WHERE candidate_id = ?";
    private static final String UPDATE_VOTE_COUNT_SQL = 
        "UPDATE candidates SET vote_count = vote_count + 1 WHERE candidate_id = ?";
    private static final String SELECT_RESULTS_SQL = 
        "SELECT name, party, vote_count FROM candidates ORDER BY vote_count DESC";
    
    /**
     * Adds a new candidate to the database.
     */
    public boolean addCandidate(Candidate candidate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_CANDIDATE_SQL);
            preparedStatement.setString(1, candidate.getName());
            preparedStatement.setString(2, candidate.getParty());
            preparedStatement.setString(3, candidate.getSymbolUrl());
            
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
    // Inside CandidateDAO.java
// (Add this method along with the others)

    /**
     * Deletes a candidate from the database based on their ID.
     * @param candidateId The ID of the candidate to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteCandidate(int candidateId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        // We defined this SQL string in Step 13
        // private static final String DELETE_CANDIDATE_SQL = "DELETE FROM candidates WHERE candidate_id = ?";
        
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_CANDIDATE_SQL);
            preparedStatement.setInt(1, candidateId);

            // executeUpdate() returns the number of rows affected
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
    /**
     * Retrieves all candidates from the database.
     */
    public List<Candidate> getAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_ALL_CANDIDATES_SQL);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setCandidateId(rs.getInt("candidate_id"));
                candidate.setName(rs.getString("name"));
                candidate.setParty(rs.getString("party"));
                candidate.setSymbolUrl(rs.getString("symbol_url"));
                candidate.setVoteCount(rs.getInt("vote_count"));
                candidates.add(candidate);
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
        return candidates;
    }

    /**
     * Increments the vote count for a specific candidate.
     */
    public boolean incrementVoteCount(int candidateId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_VOTE_COUNT_SQL);
            preparedStatement.setInt(1, candidateId);
            
            return preparedStatement.executeUpdate() > 0;
            
        } finally {
            // Note: In the final voting logic, closing the connection here is tricky
            // if you need to use the SAME connection to update the user's 'has_voted' status.
            // We'll manage transactions in the Servlet later.
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection(connection);
        }
    }
    
    // You would add methods for 'deleteCandidate', 'updateCandidate', and 'getResults' here.
    // Inside com.voting.dao.CandidateDAO.java

/**
 * Retrieves the election results (name, party, and vote_count), 
 * sorted by vote count in descending order.
 */
public List<Candidate> getElectionResults() {
    List<Candidate> results = new ArrayList<>();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    
    // SQL Statement was defined in Step 13: 
    // private static final String SELECT_RESULTS_SQL = "SELECT name, party, vote_count FROM candidates ORDER BY vote_count DESC";

    try {
        connection = DBConnection.getConnection();
        // Use SELECT_RESULTS_SQL from Step 13
        preparedStatement = connection.prepareStatement(SELECT_RESULTS_SQL); 
        rs = preparedStatement.executeQuery();

        while (rs.next()) {
            Candidate candidate = new Candidate();
            // Note: We only set the fields we need for the results page
            candidate.setName(rs.getString("name"));
            candidate.setParty(rs.getString("party"));
            candidate.setVoteCount(rs.getInt("vote_count"));
            results.add(candidate);
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
    return results;
}
}