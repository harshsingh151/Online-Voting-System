<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.voting.model.User" %>
<% 
    // Get the current user (set by LoginServlet)
    User currentUser = (User) session.getAttribute("currentUser");
    
    // Redirect if not logged in
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Pass currentUser to the JSP context so JSTL tags (<c:if>) can access it
    pageContext.setAttribute("currentUser", currentUser);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Election Results</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 20px; }
    .container { max-width: 900px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 12px; box-shadow: 0 0 15px rgba(0, 0, 0, 0.1); }
    h1 { text-align: center; color: #333; margin-bottom: 30px; }
    .status-box { background-color: #e6f7ff; border: 1px solid #91d5ff; padding: 15px; border-radius: 8px; margin-bottom: 20px; text-align: center; }
    .status-box p { margin: 5px 0; }
    /* This is the new message box style */
    .message-box { background-color: #fff3cd; border: 1px solid #ffeeba; color: #856404; padding: 20px; border-radius: 8px; text-align: center; font-size: 1.2em; }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
    th { background-color: #007bff; color: white; }
    .winner { background-color: #d4edda; font-weight: bold; }
    .progress-bar-container { background-color: #f0f0f0; border-radius: 4px; overflow: hidden; height: 25px; width: 100%; }
    .progress-bar { background-color: #28a745; height: 100%; text-align: center; color: white; line-height: 25px; transition: width 0.5s; }
</style>
</head>
<body>

<div class="container">
    <h1>🗳️ Election Dashboard</h1>

    <c:choose>
        <%-- CASE 1: User has already voted. Show them the results. --%>
        <c:when test="${currentUser.isHasVoted()}">
            <div class="status-box">
                <c:choose>
                    <c:when test="${totalVotes == 0}">
                        <p>No votes have been cast yet.</p>
                    </c:when>
                    <c:otherwise>
                        <p>Total Votes Cast: <strong>${totalVotes}</strong></p>
                        <p style="color: green; font-weight: bold;">Current Leader: ${results[0].name} (${results[0].party})</p>
                    </c:otherwise>
                </c:choose>
                <p style="color:blue; font-weight:bold; margin-top:10px;">You have already cast your vote.</p>
                <p style="margin-top:15px;"><a href="LogoutServlet">Logout</a></p>
            </div>

            <h2>Live Results</h2>
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
        </c:when>
        
        <%-- CASE 2: User has NOT voted AND election is STOPPED. Show "closed" message. --%>
        <c:when test="${!isElectionActive}">
            <div class="message-box">
                <p>Voting is not open yet (or is currently closed).</p>
                <p>Please wait until the election is active to cast your vote.</p>
                <p style="font-size: 0.8em; margin-top: 20px;"><a href="LogoutServlet">Logout</a></p>
            </div>
        </c:when>
        
        <%-- CASE 3: User has NOT voted and election IS active. (Shouldn't happen, they'd be on vote.jsp) --%>
        <c:otherwise>
             <p>You have not voted. <a href="vote.jsp">Go back to Voting</a> | <a href="LogoutServlet">Logout</a></p>
        </c:otherwise>
    </c:choose>
    
</div>

</body>
</html>