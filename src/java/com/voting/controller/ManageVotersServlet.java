package com.voting.controller;

import com.voting.dao.UserDAO;
import com.voting.model.User;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ManageVotersServlet")
public class ManageVotersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
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
        
        // 1. Fetch all voters from DAO
        List<User> voterList = userDAO.getAllVoters();

        // 2. Set the list as a request attribute
        request.setAttribute("voterList", voterList);

        // 3. Forward to the JSP page
        // Place this new JSP inside the 'admin' folder for consistency
        request.getRequestDispatcher("/admin/manage_voters.jsp").forward(request, response);
    }
}