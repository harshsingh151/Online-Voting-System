package com.voting.controller;

import com.voting.dao.CandidateDAO;
import com.voting.model.Candidate;

import java.io.IOException;
import java.net.URLEncoder; // Import this for safety in redirect URLs
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File; // For file operations
import java.io.InputStream; // For reading the file
import java.nio.file.Files; // For saving the file
import java.nio.file.Paths; // For path manipulation
import java.nio.file.StandardCopyOption; // For overwriting files
import javax.servlet.annotation.MultipartConfig; // To enable file uploads
import javax.servlet.http.Part; // To get the file part

@MultipartConfig
@WebServlet("/ManageCandidateServlet")
public class ManageCandidateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CandidateDAO candidateDAO;

    public void init() {
        candidateDAO = new CandidateDAO();
    }
    
    // Handles GET requests (displaying list AND deleting)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // --- Security Check (Admin must be logged in) ---
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAdmin") == null || !(Boolean)session.getAttribute("isAdmin")) {
            response.sendRedirect("admin_login.jsp");
            return;
        }
        // ------------------------------------------------
        
        String action = request.getParameter("action");
        
        if ("delete".equals(action)) {
            // Call the delete method
            deleteCandidate(request, response);
        } else {
            // Default action: Display the list of candidates
            listCandidates(request, response);
        }
    }

    // Handles POST requests (primarily for adding a new candidate)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // --- Security Check (Admin must be logged in) ---
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAdmin") == null || !(Boolean)session.getAttribute("isAdmin")) {
            response.sendRedirect("admin_login.jsp");
            return;
        }
        // ------------------------------------------------
        
        addCandidate(request, response);
    }
    
    private void listCandidates(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Fetch all candidates from the database
        List<Candidate> candidateList = candidateDAO.getAllCandidates();
        
        // Set the list as an attribute to be accessed by the JSP
        request.setAttribute("candidateList", candidateList);
        
        // Forward to the JSP page for display
        request.getRequestDispatcher("/admin/manage_candidates.jsp").forward(request, response);
    }

    private void addCandidate(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

    // 1. Get text parameters
    String name = request.getParameter("name");
    String party = request.getParameter("party");

    // 2. Get the file part
    Part filePart = request.getPart("symbol");
    String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

    // 3. Simple validation
    if (name == null || name.isEmpty() || party == null || party.isEmpty() || originalFileName == null || originalFileName.isEmpty()) {
        request.setAttribute("message", "All fields, including the image, are required.");
        listCandidates(request, response); 
        return;
    }

    // 4. Create a unique file name and define save path
    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
    // Create a unique name (e.g., 1678886543210.png)
    String uniqueFileName = System.currentTimeMillis() + fileExtension;

    // Get the absolute path to the 'uploads' folder
    String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";

    // Create the directory if it doesn't exist
    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) {
        uploadDir.mkdir();
    }

    String savePath = uploadPath + File.separator + uniqueFileName;

    // 5. Save the file to the server
    try (InputStream fileContent = filePart.getInputStream()) {
        Files.copy(fileContent, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("message", "Error saving file upload.");
        listCandidates(request, response);
        return;
    }

    // 6. Create Candidate object with the NEW file name and call DAO
    // We store only the file name (e.g., "1678886543210.png") in the DB
    Candidate newCandidate = new Candidate(name, party, uniqueFileName); 
    boolean success = candidateDAO.addCandidate(newCandidate);
    String message;

    if (success) {
        message = "Candidate added successfully!";
    } else {
        message = "Failed to add candidate. Check logs.";
    }

    // 7. Redirect to doGet to refresh the list
    response.sendRedirect("ManageCandidateServlet?message=" + URLEncoder.encode(message, "UTF-8"));
}

    private void deleteCandidate(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String message;
        try {
            // 1. Get the ID from the URL parameter
            int candidateId = Integer.parseInt(request.getParameter("id"));
            
            // 2. Call the DAO to delete the candidate
            // (Assumes you've added deleteCandidate(id) to CandidateDAO.java)
            boolean success = candidateDAO.deleteCandidate(candidateId);
            
            if (success) {
                message = "Candidate deleted successfully!";
            } else {
                message = "Failed to delete candidate. They might have associated votes or do not exist.";
            }
        } catch (NumberFormatException e) {
            message = "Invalid candidate ID.";
        }
        
        // 3. Redirect back to the list page with a status message
        // We URLEncode the message to handle spaces and special characters
        response.sendRedirect("ManageCandidateServlet?message=" + URLEncoder.encode(message, "UTF-8"));
    }
}