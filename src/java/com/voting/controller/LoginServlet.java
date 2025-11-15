package com.voting.controller;

import com.voting.dao.UserDAO;
import com.voting.model.User;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // 2. Validate user via DAO
        User user = userDAO.validateLogin(email, password);

        if (user != null) {
            // Login successful
            
            // 3. Create/Get the session object
            HttpSession session = request.getSession();
            
            // 4. Store the user object in the session
            // This object will be used to check if the user is logged in and who they are
            session.setAttribute("currentUser", user);
            
            // 5. Redirect the user to the main voting page
            response.sendRedirect("vote.jsp"); 
            
        } else {
            // Login failed
            request.setAttribute("message", "Invalid email or password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}