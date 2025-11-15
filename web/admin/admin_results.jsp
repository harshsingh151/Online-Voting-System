<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<title>Live Election Results</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #f0f4ff; margin: 0; padding: 0; }
    .header { background-color: #36a3f7; color: white; padding: 15px; text-align: center; }
    .container { max-width: 1000px; margin: 20px auto; padding: 20px; background-color: white; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
    h2 { text-align: center; color: #333; }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
    th { background-color: #f2f2f2; color: #333; }
    .winner { background-color: #d4edda; font-weight: bold; }
    .progress-bar-container { background-color: #f0f0f0; border-radius: 4px; overflow: hidden; height: 25px; width: 100%; }
    .progress-bar { background-color: #28a745; height: 100%; text-align: center; color: white; line-height: 25px; transition: width 0.5s; }
</style>
</head>
<body>

<div class="header">
    <h1>Live Election Results</h1>
    <p><a href="AdminDashboard.jsp" style="color: white;">&larr; Back to Dashboard</a> | <a href="../LogoutServlet?role=admin" style="color: white;">Logout</a></p>
</div>

<div class="container">
    <h2>Current Standings</h2>

    <c:choose>
        <c:when test="${totalVotes == 0}">
            <p style="text-align: center; color: gray;">No votes have been cast yet.</p>
        </c:when>
        <c:otherwise>
            <p style="text-align: center; font-size: 1.2em;">Total Votes Cast: <strong>${totalVotes}</strong></p>
            <p style="text-align: center; font-size: 1.1em; color: green;">
                Current Leader: <strong>${results[0].name} (${results[0].party})</strong>
            </p>
        </c:otherwise>
    </c:choose>

    <table>
        <thead>
            <tr>
                <th>Rank</th>
                <th>Candidate Name</th>
                <th>Party</th>
                <th>Votes</th>
                <th>Percentage</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="candidate" items="${results}" varStatus="loop">
                <c:set var="percentage" value="0"/>
                <c:if test="${totalVotes > 0}">
                     <c:set var="percentage" value="${(candidate.voteCount / totalVotes) * 100}"/>
                </c:if>
                <tr class="${loop.index == 0 && totalVotes > 0 ? 'winner' : ''}">
                    <td>${loop.index + 1}</td>
                    <td>${candidate.name}</td>
                    <td>${candidate.party}</td>
                    <td>${candidate.voteCount}</td>
                    <td>
                        <div class="progress-bar-container">
                            <c:if test="${percentage > 0}">
                                <div class="progress-bar" style="width: ${percentage}%;">
                                    <fmt:formatNumber value="${percentage}" pattern="0.0"/>%
                                </div>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>