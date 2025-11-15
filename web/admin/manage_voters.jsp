<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<meta charset="UTF-8">
<title>Manage Voters</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #f0f4ff; margin: 0; padding: 0; }
    .header { background-color: #36a3f7; color: white; padding: 15px; text-align: center; }
    .container { max-width: 1200px; margin: 20px auto; padding: 20px; background-color: white; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
    th { background-color: #f2f2f2; color: #333; }
    .voted-yes { color: green; font-weight: bold; }
    .voted-no { color: red; }
</style>
</head>
<body>

<div class="header">
    <h1>Voter Management</h1>
    <p><a href="AdminDashboard.jsp" style="color: white;">&larr; Back to Dashboard</a> | <a href="../LogoutServlet?role=admin" style="color: white;">Logout</a></p>
</div>

<div class="container">
    <h2>All Registered Voters</h2>
    <table>
        <thead>
            <tr>
                <th>Voter ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Has Voted?</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="voter" items="${voterList}">
                <tr>
                    <td>${voter.id}</td>
                    <td>${voter.name}</td>
                    <td>${voter.email}</td>
                    <td>
                        <c:if test="${voter.hasVoted}">
                            <span class="voted-yes">YES</span>
                        </c:if>
                        <c:if test="${!voter.hasVoted}">
                            <span class="voted-no">NO</span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty voterList}">
                <tr>
                    <td colspan="4" style="text-align: center;">No voters have registered yet.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

</body>
</html>