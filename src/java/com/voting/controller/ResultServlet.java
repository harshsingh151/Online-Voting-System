package com.voting.controller;

import com.voting.dao.AdminDAO; // <-- 1. IMPORT
import com.voting.dao.CandidateDAO;
import com.voting.model.Candidate;
import com.voting.model.User;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ResultServlet")
public class ResultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CandidateDAO candidateDAO;
    private AdminDAO adminDAO; // <-- 2. ADD THIS FIELD

    public void init() {
        candidateDAO = new CandidateDAO();
        adminDAO = new AdminDAO(); // <-- 3. INITIALIZE IT
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Security Check
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp"); 
            return;
        }
        
        // 2. Fetch election results
        List<Candidate> results = candidateDAO.getElectionResults();
        
        // 3. Calculate total votes
        int totalVotes = 0;
        for (Candidate c : results) {
            totalVotes += c.getVoteCount();
        }

        // 4. GET ELECTION STATUS (THE NEW PART)
        boolean isElectionActive = adminDAO.isElectionActive();
        
        // 5. Set attributes for the JSP
        request.setAttribute("results", results);
        request.setAttribute("totalVotes", totalVotes);
        request.setAttribute("isElectionActive", isElectionActive); // <-- 4. PASS STATUS TO JSP
        
        // 6. Forward to the results page
        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }
}