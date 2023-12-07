<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #c5d4c9;
            color: #333333;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 100%;
            max-height: 100%;
            margin: 0 auto;
            padding: 20px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: space-between;
            background-color: #c5d4c9;
        }
        .header {
            display: flex;
            padding-bottom: 20px;
            margin-bottom: 30px;
            text-align:center;
            width:100%;
            background-color: #fff;
            
        }
        .logo {
        	
        	float:left;
        	padding-top:15px;
        	padding-left:5px;
            width: 200px;
            
        }
        .header img{
        	float: left;
        	
        	
        }
        .title-container {
            flex-grow: 1;
            display: flex;
            align-items: left;
            justify-content: left;
            padding-left:35px;
        }
        .title {
        	padding-top: 20px;
            margin: 0;
        }
        .body-content {
            flex-grow: 1;
            margin-bottom: 20px;
            background-color: #ffffff;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
            padding: 20px;
            border-radius: 13px;
            max-width: 600px;
            display: inline-block;
            
        }
        .footer {
            text-align: center;
            color: #777777;
        }
    </style>
</head>
<body>
	<div class="header">
            <img class="logo" src="images/nrcanlogo.png" alt="Logo">
            <div class="title-container">
                <h1 class="title">New Invasive Species Reporting App</h1>
            </div>
        </div>
    <div class="container">
        
        <div class="body-content">
            <h2>Oops! Something went wrong.</h2>
            <p>&nbsp We're sorry, but an error occurred while processing your request.</p>
            <p>&nbsp Please try again later or contact the support team if the problem persists.</p>
            <div class="code">
                <p><strong>Error details:</strong></p>
                <p>&nbsp System Error!</p>
                
                
            </div>
        </div>
        <div class="footer">
            <p><b>&copy; 2023 New Invasive Species Reporting Web App (Canada). All rights reserved.</b></p>
        </div>
    </div>
</body>
</html>