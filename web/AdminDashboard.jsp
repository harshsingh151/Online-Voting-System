<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
    // --- BASIC SECURITY CHECK (Filter should do this, but this is a quick check) ---
    if (session.getAttribute("isAdmin") == null || !(Boolean)session.getAttribute("isAdmin")) {
        response.sendRedirect("admin_login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin Dashboard</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #f0f4ff; margin: 0; padding: 0; }
    .header { background-color: #36a3f7; color: white; padding: 15px; text-align: center; }
    .container { max-width: 1000px; margin: 20px auto; padding: 20px; background-color: white; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
    .card-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; }
    .card { background-color: #f7f7f7; padding: 20px; border-radius: 6px; text-align: center; border-left: 5px solid #36a3f7; }
    .card h3 { color: #36a3f7; margin-top: 0; }
    .card a { text-decoration: none; color: #333; font-weight: bold; display: block; padding: 10px; background-color: #e0e9ff; margin-top: 10px; border-radius: 4px; }
    .card a:hover { background-color: #c9d8ff; }
</style>
</head>
<body>

<div class="header">
    <h1>Admin Panel - Dashboard</h1>
    <p>Welcome, <%= session.getAttribute("adminUsername") %> | <a href="LogoutServlet?role=admin" style="color: white;">Logout</a></p>
</div>

<div class="container">
    <h2>System Overview</h2>
    
    <%-- Note: These numbers will be populated by a Servlet in a real scenario --%>
    <div class="card-grid">
        <div class="card">
    <h3>Total Voters</h3>
    <p style="font-size: 2em; color: #555;">${totalVoters}</p> 
    <a href="ManageVotersServlet">Manage Voters</a>
</div>
        <div class="card">
    <h3>Active Candidates</h3>
    <p style="font-size: 2em; color: #555;">${totalCandidates}</p>
    <a href="ManageCandidateServlet">Manage Candidates</a>
</div>
       <div class="card">
    <h3>Votes Cast</h3>
    <p style="font-size: 2em; color: #555;">${totalVotesCast}</p>
    <a href="AdminResultServlet">View Live Results</a>
</div>
    </div>
    
    <h2 style="margin-top: 40px;">Election Management</h2>
    <div class="card" style="border-left-color: ${isElectionActive ? '#dc3545' : '#28a745'};">
    <h3>Election Control</h3>

    <c:choose>
        <c:when test="${isElectionActive}">
            <p>Current Status: <strong style="color: green;">ACTIVE</strong></p>
            <a href="ElectionControlServlet?action=stop" style="background-color: #dc3545; color: white;">STOP ELECTION</a>
        </c:when>
        <c:otherwise>
            <p>Current Status: <strong style="color: red;">STOPPED</strong></p>
            <a href="ElectionControlServlet?action=start" style="background-color: #28a745; color: white;">START ELECTION</a>
        </c:otherwise>
    </c:choose>
</div>
    
</div>

</body>
</html>