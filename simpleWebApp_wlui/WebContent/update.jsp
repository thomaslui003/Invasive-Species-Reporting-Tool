<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="simple.newReportedCase"%>
<%@page import="org.nfis.db.PostgresConnectionManager"%>
<%@page import="javax.servlet.ServletException"%>
<%@page import="javax.servlet.annotation.WebServlet"%>
<%@page import="javax.servlet.http.HttpServlet"%>
<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page import="javax.servlet.http.HttpServletResponse"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="simple.listOfCaseServlet"%>
<%@page import="simple.JDBCpostgresconn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">


<% 
	String currentUser = (String)request.getAttribute("currentUserName_update");
	String caseId = (String)request.getAttribute("caseid_update");
	String reportedDate = (String)request.getAttribute("reportedDate_update");
	%>
<title>Update Form</title>
</head>

<style>
	​​
	.formContainer{
	    
	    background-color: #F0F0F0;
	    border-radius: 20px;
	    box-shadow: 0 0 10px rgba(0,0,0,0.1);
	    max-width: 400px;
	    padding: 20px;
	   
	}
	
	#updateButton{
	            background-color: #26a269;
	            border: none;
	            border-radius: 30px;
	            color: #fff;
	            cursor: pointer;
	            padding: 10px;
	            width: 33%;
	            box-shadow: 0 0 10px rgba(0,0,0,0.2);
	          	
	        }
	#updateBackButton{
	
			background-color: #f1828d;
	        border: none;
	        border-radius: 30px;
	        color: #fff;
	        cursor: pointer;
	        padding: 10px;
	        width: 33%;
	        box-shadow: 0 0 10px rgba(0,0,0,0.2);
	        }
</style>

<body style =  "background-color: #c6f5cc">

<div class = "formContainer" >
    <h1> Updating Reporting Form </h1>
    <label for ="username">Current User: <%out.print(currentUser);%></label>
    <br>
    <label for ="caseid">Editing Case Id: <%out.print(caseId);%></label>
    <br>
    <label for ="dateReport">Reported Date: <%out.print(reportedDate); %></label>
    <br>
    <br>
    
    <form method = "post" action="listOfCaseServlet" id="updateformCSS">
        <label for ="newSpecies">Update Species Name:</label>
        <input type = "text" id="newSpecies" name="newSpecies_updateP2" pattern = "[A-Za-z\s]*$" title="No numbers or special characters" maxlength="100">
        <br>
        <br>
        <label for ="reportername">Update Reporter Name:</label>
        <input type = "text" id="reporterName" name="reporterName_updateP2" pattern = "[A-Za-z\s]*$" title="No numbers or special characters" maxlength="50">        
        <br>
        <br>
        <label for="province">Province / Territories:</label>
          <select id="province" name="province_updateP2">
            <option hidden disabled selected value> ------- Select an option ------- </option>
            <option value="British Columbia">British Columbia</option>
            <option value="Alberta">Alberta</option>
            <option value="Manitoba">Manitoba</option>
            <option value="New Brunswick">New Brunswick</option>
            <option value="Newfoundland and Labrador">Newfoundland and Labrador</option>
            <option value="Northwest Territories">Northwest Territories</option>
            <option value="Nova Scotia">Nova Scotia</option>
            <option value="Nunavut">Nunavut</option>
            <option value="Ontario">Ontario</option>
            <option value="Prince Edward Island">Prince Edward Island</option>
            <option value="Quebec">Quebec</option>
            <option value="Saskatchewan">Saskatchewan</option>
            <option value="Yukon">Yukon</option>
          </select>
        <br>
        <br>
        <label for ="coordinates">Coordinates Location:</label>
        <input type = "text" id="coordinates" name="coordinates_updateP2" placeholder = " Long, Lat" maxlength="25">
        
        <input type ="hidden" value = <%out.print(caseId);%> name = "caseId_updateP2">
        <input type ="hidden" value = "4" name = "updateCaseButtonValuePart2">
        <input type = "hidden" value = <%out.print(currentUser);%> name = "currentUser_updateP2">
        <br>
        <br>
        <input type = "submit" value = "Submit Update" id = "updateButton">
    </form>
    <br/>
    <form method = "post" action="listOfCaseServlet">
    	<input type = "hidden" value = "5" name = "updateCaseBackButtonValue">
    	<input type = "hidden" value = <%out.print(currentUser);%> name = "currentUserUpdateBackButton">
    	<input type = "submit" value = "Back" id="updateBackButton">
    </form>
    </div>

</body>
</html>