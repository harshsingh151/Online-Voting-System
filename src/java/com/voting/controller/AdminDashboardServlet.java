package com.voting.controller;

import com.voting.dao.AdminDAO;
import com.voting.dao.UserDAO; // <-- 1. ADD IMPORT
import com.voting.dao.CandidateDAO; // <-- 2. ADD IMPORT
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private AdminDAO adminDAO;
    private UserDAO userDAO; // <-- 3. ADD FIELD
    private CandidateDAO candidateDAO; // <-- 4. ADD FIELD

    public void init() {
        adminDAO = new AdminDAO();
        userDAO = new UserDAO(); // <-- 5. INITIALIZE
        candidateDAO = new CandidateDAO(); // <-- 6. INITIALIZE
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // --- Security Check ---
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAdmin") == null || !(Boolean)session.getAttribute("isAdmin")) {
            response.sendRedirect("admin_login.jsp");
            return;
        }

        // 1. Get statuses
        boolean isElectionActive = adminDAO.isElectionActive();
        boolean isPublished = adminDAO.isResultsPublished();

        // 2. GET DASHBOARD COUNTS (NEW)
        int totalVoters = userDAO.countTotalVoters();
        int totalCandidates = candidateDAO.countTotalCandidates();
        int totalVotesCast = candidateDAO.countTotalVotesCast();

        // 3. Set attributes
        request.setAttribute("isElectionActive", isElectionActive);
        request.setAttribute("isPublished", isPublished);
        
        // --- PASS COUNTS TO JSP (NEW) ---
        request.setAttribute("totalVoters", totalVoters);
        request.setAttribute("totalCandidates", totalCandidates);
        request.setAttribute("totalVotesCast", totalVotesCast);
        
        // 4. Forward to the JSP
        request.getRequestDispatcher("/AdminDashboard.jsp").forward(request, response);
    }
}