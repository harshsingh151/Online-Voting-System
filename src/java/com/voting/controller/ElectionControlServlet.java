package com.voting.controller;

import com.voting.dao.AdminDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ElectionControlServlet")
public class ElectionControlServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private AdminDAO adminDAO;

    public void init() {
        adminDAO = new AdminDAO();
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
        
        // 1. Get the requested action (start or stop)
        String action = request.getParameter("action");
        
        if ("start".equals(action)) {
            adminDAO.setElectionStatus(true);
        } else if ("stop".equals(action)) {
            adminDAO.setElectionStatus(false);
        }
        
        // 2. Redirect back to the dashboard
        response.sendRedirect("AdminDashboardServlet"); // We will create this servlet next
    }
}