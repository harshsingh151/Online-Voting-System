<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
    // Redirect if not admin
    if (session.getAttribute("isAdmin") == null || !(Boolean)session.getAttribute("isAdmin")) {
        response.sendRedirect("../admin_login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Manage Candidates</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #f0f4ff; margin: 0; padding: 0; }
    .header { background-color: #36a3f7; color: white; padding: 15px; text-align: center; }
    .container { max-width: 1200px; margin: 20px auto; padding: 20px; background-color: white; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
    th { background-color: #f2f2f2; color: #333; }
    .add-form { background-color: #e0e9ff; padding: 15px; border-radius: 6px; margin-bottom: 20px; }
    .add-form input[type="text"] { padding: 8px; margin: 5px; width: 200px; }
    .add-form input[type="submit"] { background-color: #28a745; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }
    .message { padding: 10px; margin-bottom: 15px; border-radius: 4px; text-align: center; }
    .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
    .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
</style>
</head>
<body>

<div class="header">
    <h1>Candidate Management</h1>
    <p><a href="AdminDashboard.jsp" style="color: white;">&larr; Back to Dashboard</a> | <a href="../LogoutServlet?role=admin" style="color: white;">Logout</a></p>
</div>

<div class="container">
    
    <% 
        // Display message after POST-Redirect-GET
        String message = request.getParameter("message");
        if (message != null && !message.isEmpty()) { 
    %>
        <p class="message ${message.contains('success') ? 'success' : 'error'}"><%= message %></p>
    <% 
        } 
    %>

    <h2>Add New Candidate</h2>
    <div class="add-form">
    <form action="ManageCandidateServlet" method="post" enctype="multipart/form-data">
        <input type="text" name="name" placeholder="Candidate Name" required>
        <input type="text" name="party" placeholder="Party Name" required>
        
        <label for="symbol">Party Symbol:</label>
        <input type="file" name="symbol" accept="image/png, image/jpeg" required>
        
        <input type="submit" value="Add Candidate">
    </form>
</div>

    <h2>Current Candidates</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Party</th>
                <th>Votes</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="candidate" items="${candidateList}">
                <tr>
                    <td>${candidate.candidateId}</td>
                    <td>${candidate.name}</td>
                    <td>${candidate.party}</td>
                    <td>${candidate.voteCount}</td>
                    <td>
                        <a href="ManageCandidateServlet?action=delete&id=${candidate.candidateId}" onclick="return confirm('Are you sure you want to delete ${candidate.name}?');">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty candidateList}">
                <tr>
                    <td colspan="5" style="text-align: center;">No candidates registered yet.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
    
</div>

</body>
</html>