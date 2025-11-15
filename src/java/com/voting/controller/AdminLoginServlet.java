package com.voting.controller;

import com.voting.dao.AdminDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private AdminDAO adminDAO;

    public void init() {
        adminDAO = new AdminDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // 2. Validate admin via DAO
        boolean isValid = adminDAO.validateAdmin(username, password);

        if (isValid) {
            // Login successful
            
            // 3. Create/Get the session object and store admin info
            HttpSession session = request.getSession();
            // Storing a simple flag or username to mark the session as 'admin'
            session.setAttribute("isAdmin", true); 
            session.setAttribute("adminUsername", username); 
            
            // 4. Redirect to the Admin Dashboard (next step)
            response.sendRedirect("AdminDashboardServlet");
            
        } else {
            // Login failed
            request.setAttribute("message", "Invalid username or password.");
            request.getRequestDispatcher("/admin_login.jsp").forward(request, response);
        }
    }
}