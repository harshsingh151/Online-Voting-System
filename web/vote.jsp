<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.voting.model.User" %>

<% 
    // === THIS IS THE FIXED LOGIC BLOCK ===
    
    // 1. Get the user from the session and cast it
    User currentUser = (User) session.getAttribute("currentUser");
    
    // 2. Check if user is logged in
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // 3. Check if user has already voted
    if (currentUser.isHasVoted()) {
        // Redirect to the SERVLET, not the JSP
        response.sendRedirect("ResultServlet"); 
        return;
    }
    
    // If user is logged in AND has not voted, the page will load.
    // The 'candidateList' is expected to be set by the VoteServlet's doGet method.
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cast Your Vote</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #e6f7ff; margin: 0; padding: 20px; text-align: center; }
    .container { max-width: 800px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }
    h1 { color: #007bff; }
    .welcome { margin-bottom: 20px; font-size: 1.1em; }
    .candidate-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-top: 30px; }
    .candidate-card { border: 2px solid #ccc; padding: 15px; border-radius: 8px; cursor: pointer; transition: all 0.3s; }
    .candidate-card:hover { border-color: #007bff; box-shadow: 0 0 10px rgba(0, 123, 255, 0.5); }
    .candidate-card input[type="radio"] { display: none; }
    .candidate-card input[type="radio"]:checked + label { border-color: #28a745; background-color: #e2f4e5; border-width: 3px; }
    .candidate-card label { display: block; cursor: pointer; padding: 10px; }
    .candidate-card img { width: 80px; height: 80px; object-fit: cover; border-radius: 50%; margin-bottom: 10px; border: 2px solid #ddd; }
    .candidate-info h3 { margin: 5px 0; color: #333; }
    .candidate-info p { color: #555; font-size: 0.9em; }
    .submit-btn { background-color: #dc3545; color: white; padding: 15px 30px; border: none; border-radius: 5px; cursor: pointer; font-size: 1.2em; margin-top: 30px; }
    .submit-btn:hover { background-color: #c82333; }
    .message { color: red; margin-top: 15px; }
</style>
</head>
<body>

<div class="container">
    <h1>General Election 2024</h1>
    <p class="welcome">Hello, <strong><%= currentUser.getName() %></strong>! Please cast your single vote below. | <a href="LogoutServlet">Logout</a></p>

    <% 
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
        <p class="message"><%= message %></p>
    <%
        }
    %>
    
    <form action="VoteServlet" method="post" id="voteForm">
        <div class="candidate-grid">
            <c:choose>
                <c:when test="${not empty candidateList}">
                    <c:forEach var="candidate" items="${candidateList}">
                        <div class="candidate-card">
                            <input type="radio" id="cand${candidate.candidateId}" name="candidateId" value="${candidate.candidateId}" required>
                            <label for="cand${candidate.candidateId}">
                                <img src="uploads/${candidate.symbolUrl}" alt="${candidate.name} symbol"> 
                                <div class="candidate-info">
                                    <h3>${candidate.name}</h3>
                                    <p><strong>Party:</strong> ${candidate.party}</p>
                                </div>
                            </label>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p style="grid-column: 1 / -1; color: gray;">No candidates are currently registered. Please contact the administrator.</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <button type="submit" class="submit-btn" onclick="return confirm('WARNING: You can only vote once. Are you sure you want to cast your vote for the selected candidate?');">CAST VOTE</button>
    </form>
</div>

</body>
</html>