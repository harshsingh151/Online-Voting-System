package com.voting.controller;

import com.voting.dao.UserDAO;
import com.voting.model.User;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// This annotation maps the URL pattern to this Servlet
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Instance of the DAO for database interaction
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get parameters from the registration form
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Simple validation
        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("message", "All fields are required.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 2. Create a User model object
        User newUser = new User(name, email, password);

        // 3. Call the DAO to register the user
        boolean success = userDAO.registerUser(newUser);

        if (success) {
            // Registration successful
            request.setAttribute("message", "Registration successful! Please login.");
            request.getRequestDispatcher("/login.jsp").forward(request, response); 
        } else {
            // Registration failed (likely due to duplicate email)
            request.setAttribute("message", "Registration failed. Email address may already be in use.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}