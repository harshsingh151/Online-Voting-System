package com.voting.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Get existing session, don't create new one

        if (session != null) {
            session.invalidate(); // Destroy the session data
        }

        // Redirect based on who is logging out (or just go to the main login page)
        String role = request.getParameter("role");
        if ("admin".equals(role)) {
            response.sendRedirect("admin_login.jsp");
        } else {
            response.sendRedirect("login.jsp"); // Voter login page
        }
    }
}