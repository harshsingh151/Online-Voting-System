package com.voting.controller;

import com.voting.dao.CandidateDAO;
import com.voting.model.Candidate;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AdminResultServlet")
public class AdminResultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CandidateDAO candidateDAO;

    public void init() {
        candidateDAO = new CandidateDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // --- Security Check (Admin must be logged in) ---
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAdmin") == null || !(Boolean)session.getAttribute("isAdmin")) {
            response.sendRedirect("admin_login.jsp");
            return;
        }
        // ------------------------------------------------

        // 1. Fetch the ordered election results
        List<Candidate> results = candidateDAO.getElectionResults();
        
        // 2. Calculate total votes
        int totalVotes = 0;
        for (Candidate c : results) {
            totalVotes += c.getVoteCount();
        }

        // 3. Set attributes for the JSP
        request.setAttribute("results", results);
        request.setAttribute("totalVotes", totalVotes);
        
        // 4. Forward to the admin results page
        request.getRequestDispatcher("/admin/admin_results.jsp").forward(request, response);
    }
}