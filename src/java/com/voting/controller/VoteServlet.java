package com.voting.controller;

// Import the new DAO
import com.voting.dao.AdminDAO;
import com.voting.dao.CandidateDAO;
import com.voting.dao.UserDAO;
import com.voting.model.Candidate; // Added missing import
import com.voting.model.User;
import com.voting.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement; // Added missing import
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/VoteServlet")
public class VoteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CandidateDAO candidateDAO;
    private UserDAO userDAO;
    private AdminDAO adminDAO; // <-- NEW: For checking election status

    public void init() {
        candidateDAO = new CandidateDAO();
        userDAO = new UserDAO();
        adminDAO = new AdminDAO(); // <-- NEW: Initialize the DAO
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // --- NEW ELECTION STATUS CHECK ---
        if (!adminDAO.isElectionActive()) {
            // Voting is closed. Send them to the results page.
            // The ResultServlet will handle checking if results are published.
            response.sendRedirect("ResultServlet"); // <-- FIX
            return;
        }
        // ---------------------------------
        
        // 1. Security Check (User must be logged in)
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) { // Simplified check
            response.sendRedirect("login.jsp"); 
            return;
        }
        
        // Check if user has already voted
        if (currentUser.isHasVoted()) {
            response.sendRedirect("ResultServlet"); // <-- FIX
            return;
        }

        // 2. Fetch candidates for display
        List<Candidate> candidateList = candidateDAO.getAllCandidates();
        request.setAttribute("candidateList", candidateList);
        
        // 3. Forward to the voting page
        request.getRequestDispatcher("/vote.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // --- NEW ELECTION STATUS CHECK ---
        if (!adminDAO.isElectionActive()) {
            request.setAttribute("message", "Voting is currently closed. Your vote was not counted.");
            doGet(request, response); // Re-display voting page with error
            return;
        }
        // ---------------------------------
        
        // 1. Security & Pre-Check
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.isHasVoted()) {
            response.sendRedirect("login.jsp"); 
            return;
        }

        String candidateIdStr = request.getParameter("candidateId");
        if (candidateIdStr == null || candidateIdStr.isEmpty()) {
            request.setAttribute("message", "Please select a candidate before submitting.");
            doGet(request, response); // Re-display the page with error
            return;
        }
        
        int candidateId = Integer.parseInt(candidateIdStr);
        Connection conn = null;
        boolean success = false;

        try {
            // Start Transaction Management
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Crucial: Start the transaction

            // 2. Increment Vote Count
            String updateVoteSQL = "UPDATE candidates SET vote_count = vote_count + 1 WHERE candidate_id = ?";
            try (PreparedStatement psVote = conn.prepareStatement(updateVoteSQL)) {
                psVote.setInt(1, candidateId);
                if (psVote.executeUpdate() == 0) {
                    throw new SQLException("Failed to update candidate vote count. Rolling back.");
                }
            }
            
            // 3. Mark User as Voted
            String markVotedSQL = "UPDATE users SET has_voted = 1 WHERE id = ? AND has_voted = 0";
            try (PreparedStatement psUser = conn.prepareStatement(markVotedSQL)) {
                psUser.setInt(1, currentUser.getId());
                if (psUser.executeUpdate() == 0) {
                    throw new SQLException("Failed to mark user as voted (or user already voted). Rolling back.");
                }
            }
            
            // 4. Commit Transaction
            conn.commit();
            success = true;

            // 5. Update Session Status
            currentUser.setHasVoted(true);
            session.setAttribute("currentUser", currentUser);
            
        } catch (SQLException e) {
            e.printStackTrace();
            // 6. Rollback Transaction on error
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            request.setAttribute("message", "Voting failed due to a system error. Please try again. (Ref: DB Error)");
            doGet(request, response); 
            return;
        } finally {
            // 7. Reset AutoCommit and Close Connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                DBConnection.closeConnection(conn);
            }
        }

        if (success) {
            // 8. Redirect to the results page after successful vote
            response.sendRedirect("ResultServlet"); // Redirect to servlet, not JSP
        }
    }
}