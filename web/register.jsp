<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Voter Registration</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #f4f4f4; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
    .container { background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); width: 350px; }
    h2 { text-align: center; color: #333; }
    input[type="text"], input[type="email"], input[type="password"] {
        width: 100%; padding: 10px; margin: 8px 0 15px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;
    }
    input[type="submit"] {
        background-color: #4CAF50; color: white; padding: 14px 20px; margin: 8px 0; border: none; border-radius: 4px; cursor: pointer; width: 100%;
    }
    input[type="submit"]:hover { background-color: #45a049; }
    .message { color: red; text-align: center; margin-bottom: 15px; }
    .login-link { text-align: center; margin-top: 15px; }
</style>
</head>
<body>

<div class="container">
    <h2>🗳️ Voter Registration</h2>
    
    <% 
        // Display error/success message passed from the Servlet
        String message = (String) request.getAttribute("message");
        if (message != null && !message.isEmpty()) { 
    %>
        <p class="message"><%= message %></p>
    <% 
        } 
    %>

    <form action="RegisterServlet" method="post">
        
        <label for="name"><b>Full Name</b></label>
        <input type="text" placeholder="Enter Full Name" name="name" required>
        
        <label for="email"><b>Email Address</b></label>
        <input type="email" placeholder="Enter Email" name="email" required>

        <label for="password"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>

        <input type="submit" value="Register">
    </form>
    
    <div class="login-link">
        Already have an account? <a href="login.jsp">Login Here</a>
    </div>
</div>

</body>
</html>