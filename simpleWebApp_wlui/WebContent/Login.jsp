<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<style>
body {
	background-image: url('images/forest.jpg');
	background-color: #F1F1F1;
	font-family: Arial, sans-serif;
}

form {
	background-color: #fff;
	border-radius: 17px;
	box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
	max-width: 400px;
	margin: 0 auto;
	padding: 20px;
}

h1 {
	text-align: center;
}

label {
	display: block;
	font-weight: bold;
	margin-bottom: 10px;
}

#loginButton {
	background-color: #4CAF50;
	border: none;
	border-radius: 7px;
	color: #fff;
	cursor: pointer;
	padding: 10px;
	width: 33%;
}

    .popup {
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      background-color: #f1f1f1;
      padding: 20px;
      border-radius: 5px;
      display: none;
      text-align: center;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
      max-width: 300px;
    }
    .popup p {
      margin: 0;
      padding: 10px;
      color: #FF0000;
      font-weight: bold;
    }

</style>
<script>
    // JavaScript code to toggle the popup message display
    window.onload = function() {
      if (${loginFailed}) {
        document.getElementById("popupMessage").style.display = "block";
        setTimeout(function() {
            document.getElementById("popupMessage").style.display = "none";
          }, 3500);
      }
    }
  </script>
<body>
	<h1>User Login</h1>
	<form method="post" action="loginServlet">
		<label for="username"> Username:</label> 
		<input type="text"id="username" name="username" maxlength="20" required> 
		<br>
		<br> 
		<label for="password">Password:</label> 
		<input type="password" id="password" name="password" maxlength="20" required>
		<br>
		<br>
		<input type="submit" value = "Login" id="loginButton">
	</form>
	<br/>
	<br/>
	<!-- Popup message element -->
	<div id="popupMessage" class="popup" style="display: none;">
    <p>Invalid Username or Password!</p>
  	</div>


</body>
</html>