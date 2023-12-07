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
<title>List Case</title>
</head>
<style>
			
			
			#bodyformat {
				background-image: url('images/forest.jpg');
				background-color: #f0f0f0;
				font-family: Arial, sans-serif;
			}
			
			
			h1 {
				text-align: center;
				color: #000;
			}
			
			.container {
				background-color: #c6f5cc;
				display: inline-block;
				width: 28%;
				margin: 5px;
				box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
				padding: 10px;
				border: 5px solid black;
				border-radius: 20px;
				border-color: #fff;
				max-height: 500px; 
			}
			
			.container img {
				height: 40%;
				width: 40%;
			}
			
			.container .text {
				height: 60%;
				width: 100%;
				/* restricting the height of each card */
			}
			
			.container1 {
				background-color: #c6f5cc;
				display: inline-block;
				width: 28%;
				margin: 5px;
				box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
				padding: 10px;
				border: 5px solid black;
				border-radius: 20px;
				border-color: #fff;
				max-height: 500px; 

			}
			
			.container1 img {
				
				height: 40%;
				width: 40%;
			}
			
			.container1 .text {
				height: 60%;
				width: 100%;
				
				/* Add more styles as needed */
			}
			
			#top {
				
			}
			
			#reportCaseButton {
				width: 200px;
				margin: 0px;
				display: inline;
			}
			#reportbuttonCSS{
				background-color: #F0F0F0;
				border: 2px solid #929493;
				border-radius: 22px;
				color: #000;
				cursor: pointer;
				padding: 5px;
			
			}
			#refreshbuttonCSS{
				background-color: #F0F0F0;
				border: 2px solid #929493;
				border-radius: 22px;
				color: #000;
				cursor: pointer;
				padding: 5px;
			
			}
			#logoutbuttonCSS{
				background-color: #f1828d;
				border: 2px solid #929493;
				border-radius: 22px;
				color: #fff;
				cursor: pointer;
				padding: 5px;
			}
			
			#refreshButton {
				width: 200px;
				margin: 0px;
				display: inline;
			}
			#logoutButton{
				width: 200px;
				margin: 0px;
				display: inline;
			}
			
			#buttonlayout {
				margin-left: auto;
				margin-right: 15px;
				float: right;
			}
			
			#deletebutton{
				background-color: #f1828d;
				border: 1px solid lighten(gray, 24%);
				border-radius: 30px;
				color: #fff;
				cursor: pointer;
				padding: 5px;
				width: 27%;
			}
			
			#updatebutton{
				background-color: #2ecc71;
	            border-radius: 20px;
	            color: #fff;
	            cursor: pointer;
	            padding: 5px;
	            border: 1px solid lighten(gray, 24%);
	            width: 27%;
	            
			}
			
			#searchButton{
			
				background-color: #F0F0F0;
				border: 2px solid #929493;
				border-radius: 22px;
				color: #000;
				cursor: pointer;
				padding: 5px;
			
			}
			
		
			
			#searchform {
				float: right;
			}
			
			#noResultMessage {
			
			  position: fixed;
			  background-color: #f1f1f1;
			  padding: 20px;
			  border-radius: 5px;
			  display: none;
			  text-align: center;
			  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
			  max-width: 300px;
			  
			}
			
			#noResultMessage p {
			  margin: 0;
			  padding: 10px;
			  color: #FF0000;
			  font-weight: bold;
			}
			
			.box{
				background-color: #c6f5cc;
				display: inline-block;
				width: 28%;
				margin: auto;
				box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
				padding: 10px;
				border: 5px solid black;
				border-radius: 20px;
				border-color: #fff;
				height: 140px;
				
			}
			.box p{
			
		      	margin: auto;
      			padding: 50px;
      			color: #FF0000;
      			font-weight: bold;
      			text-align: center;
			}

</style>

<body id="bodyformat">
<div class="backgroundImage"></div>
	<div id="top">
		<%
		String currentuser = (String) request.getAttribute("currentUserName");
		String noSearchResult = (String) request.getAttribute("noResultFoundValue");
		%>

		<h1>New Invasive Species Reporting List</h1>
		<form method="post" action="listOfCaseServlet" id="searchform">
				Filter: 
				<input type="text" id="speciesName_search" name="newSpecies_search" placeholder = "Species Name" 
				pattern = "[A-Za-z\s]*$" title="No numbers or special characters" maxlength="100"> 
				<input type="text" id="reporterName_search" name="reporterName_search" placeholder="Reporter Name" pattern="[A-Za-z\s]*$" title="No numbers or special characters" maxlength="50"> 
				<input type="text" id="provinceName_search" name="province_search" placeholder = "Province" pattern="[A-Za-z\s]*$" title="No numbers or special characters" maxlength="100">
				<input type = "hidden" value=<%=currentuser%> name = "currentUserSearchForm">
				<input type="hidden" value="8" name="searchCaseButtonValue">
				<input type="submit" value="Search" id="searchButton">
		</form>
		<br /> <br />

		<div id="buttonlayout">


			<label> Current User: ${currentUserName} </label>

			<form method="post" action="listOfCaseServlet" id="reportCaseButton">
				<input type="hidden" value="1" name="reportCaseButtonValue">
				<input type="hidden" value=<%=currentuser%> name = "currentUserReportButton">
				<input type="submit" value="Report a Case" id = "reportbuttonCSS">
			</form>

			<form method="get" action="listOfCaseServlet" id="refreshButton">
				<input  type="hidden" value=${currentUserName } name="crUser">
				<input type="submit" value="Refresh List" id = "refreshbuttonCSS"> 
			</form>
		
			<form method="post" action="listOfCaseServlet" id="logoutButton">
				<input type="hidden" value="9" name="logoutButtonValue">
				<input type="submit" value="Logout" id = "logoutbuttonCSS"> 
			</form>
			
		</div>
	</div>

	<br />
	<br />
	<%if (noSearchResult.equals("0")){ %>
		
	<%
	List<newReportedCase> caselist = (ArrayList<newReportedCase>) request.getAttribute("cases");
	List<newReportedCase> otherCaseList = (ArrayList<newReportedCase>) request.getAttribute("otherCases");
	
	for (newReportedCase reportedcase : caselist) {
	%>
	<div class="container">
		<h2>
			<%out.print(reportedcase.getSpeciesName()); %>
		</h2>

		<%if(reportedcase.getSpeciesName().equalsIgnoreCase("Mountain Pine Beetle")){
			%>

		<img src="images/mountainPineBeetle.jpeg" alt="Image description">
		<% 
		}else if(reportedcase.getSpeciesName().equalsIgnoreCase("Emerald Ash Borer")){
			%>
		<img src="images/emeraldAshBorer.jpeg" alt="Image description">
		<%
		}else if(reportedcase.getSpeciesName().equalsIgnoreCase("Brown Spruce Longhorn Beetle")){
			%>
		<img src="images/brownSpruceLonghornBeetle.jpg"
			alt="Image description">

		<%
		}else{
			%>
		<img src="images/unknown.png" alt="Image description">

		<%
		}
		%>

		<br />
		<div class="text">
			<%
			out.print("<b>Id:</b>  " + reportedcase.getCase_id());
			out.print("<br/>");
			out.print("<b>Species Name:</b>  " + reportedcase.getSpeciesName());
			out.print("<br/>");
			out.print("<b>Date Reported:</b>  " + reportedcase.getDateReported());
			out.print("<br/>");
			out.print("<b>Reporter Name:</b>  " + reportedcase.getReporterName());
			out.print("<br/>");
			out.print("<b>User Name:</b>  " + reportedcase.getUsername());
			out.print("<br/>");
			out.print("<b>Province:</b>  " + reportedcase.getProvince());
			out.print("<br/>");
			out.print("<b>Coordinates:</b>  " + reportedcase.getCoordinates());
			out.print("<br/>");
		%>
		</div>

		<form method="post" action="listOfCaseServlet" id="DeleteButton">
			<input type="hidden"
				value=<%out.print(Integer.toString(reportedcase.getCase_id()));%>
				name="case_id_ForDelete"> <input type="hidden"
				value=<%out.print(reportedcase.getUsername());%>
				name="username_ForDelete"> 
				<input type="hidden" value=<%out.print("2");%> name="deleteCaseButtonValue"> 
				<input type="submit" value="Delete" class="Deleting" id = "deletebutton">

		</form>
		<form method="post" action="listOfCaseServlet" id="UpdateButton">
			<input type="hidden" value=<%out.print(Integer.toString(reportedcase.getCase_id()));%> name="case_id_ForUpdate"> 
				<input type="hidden" value=<%out.print(reportedcase.getUsername());%> name="username_ForUpdate"> 
				<input type="hidden" value=<%out.print(reportedcase.getDateReported());%> name="date_ForUpdate"> 
				<input type="hidden" value=<%out.print("3");%> name="updateCaseButtonValue"> 
				<input type="submit" value="Update" class="Updating" id="updatebutton">

		</form>
	</div>
	<%
	}
	
	for (newReportedCase reportedcase : otherCaseList) {
	%>

	<div class="container1">
		<h2>
			<%out.print(reportedcase.getSpeciesName()); %>
		</h2>

		<%
		if (reportedcase.getSpeciesName().equalsIgnoreCase("Mountain Pine Beetle")) {
		%>

		<img src="images/mountainPineBeetle.jpeg" alt="Image description">
		<%
		} else if (reportedcase.getSpeciesName().equalsIgnoreCase("Emerald Ash Borer")) {
		%>
		<img src="images/emeraldAshBorer.jpeg" alt="Image description">
		<%
		} else if (reportedcase.getSpeciesName().equalsIgnoreCase("Brown Spruce Longhorn Beetle")) {
		%>
		<img src="images/brownSpruceLonghornBeetle.jpg"
			alt="Image description">

		<%
		} else {
		%>
		<img src="images/unknown.png" alt="Image description">

		<%
		}
		%>

		<br />

		<div class="text">
			<%
				out.print("<b>Id:</b>  " + reportedcase.getCase_id());
				out.print("<br/>");
				out.print("<b>Species Name:</b>  " + reportedcase.getSpeciesName());
				out.print("<br/>");
				out.print("<b>Date Reported:</b>  " + reportedcase.getDateReported());
				out.print("<br/>");
				out.print("<b>Reporter Name:</b>  " + reportedcase.getReporterName());
				out.print("<br/>");
				out.print("<b>User Name:</b>  " + reportedcase.getUsername());
				out.print("<br/>");
				out.print("<b>Province:</b>  " + reportedcase.getProvince());
				out.print("<br/>");
				out.print("<b>Coordinates:</b>  " + reportedcase.getCoordinates());
				out.print("<br/>");
				out.print("<br/>");
		%>
		</div>

	</div>
	<%} %>
	<%} 
	else{ %>

	<div class="box" >
		<p>Sorry, No Result Found!</p>
	</div>

	<%
	}
	%>

</body>
</html>



