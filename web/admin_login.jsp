<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin Login</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #f0f4ff; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
    .container { background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 15px rgba(0, 0, 0, 0.2); width: 350px; border-top: 5px solid #36a3f7; }
    h2 { text-align: center; color: #36a3f7; }
    input[type="text"], input[type="password"] {
        width: 100%; padding: 10px; margin: 8px 0 15px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;
    }
    input[type="submit"] {
        background-color: #36a3f7; color: white; padding: 14px 20px; margin: 8px 0; border: none; border-radius: 4px; cursor: pointer; width: 100%;
    }
    input[type="submit"]:hover { background-color: #2b7ecf; }
    .message { color: red; text-align: center; margin-bottom: 15px; }
    .voter-link { text-align: center; margin-top: 15px; }
</style>
</head>
<body>

<div class="container">
    <h2>🔒 Admin Login</h2>
    
    <% 
        String message = (String) request.getAttribute("message");
        if (message != null && !message.isEmpty()) { 
    %>
        <p class="message"><%= message %></p>
    <% 
        } 
    %>

    <form action="AdminLoginServlet" method="post">
        
        <label for="username"><b>Username</b></label>
        <input type="text" placeholder="Enter Username" name="username" required>
        
        <label for="password"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>

        <input type="submit" value="Log In">
    </form>
    
    <div class="voter-link">
        <a href="login.jsp">Go to Voter Login</a>
    </div>
</div>

</body>
</html>