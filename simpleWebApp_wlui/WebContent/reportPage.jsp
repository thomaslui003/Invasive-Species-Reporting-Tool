<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
<head>
<style>

	​
	h1{
		text-align: center;
	}
	
	.container{
		display: flex;
		height: 100vh;
	
	
	}
	
	.reportform{
	
		  flex: 1;
		  border-radius: 20px;
		  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		  padding: 60px;
		  background-color: #F0F0F0;
		  border: 8px solid white;
		  border-color: #fff;
	
	}
	.imageside{
			
		  flex: 1.5;
		  background-color: #c6f5cc;
		  padding: 10px;
		  display: flex;
		  align-items: center;
		  justify-content: center;
	
	}
	.imageside img{
	
	  	max-width: 100%;
  		max-height: 100%;
	}
	
	​
	#formSpecies{
		
	    background-color: #F0F0F0;
	    border-radius: 20px;
	    box-shadow: 0 0 10px rgba(0,0,0,0.1);
	    max-width: 400px;
	    margin: 0 auto;
	    padding: 20px;
	    border-color: #fff;
	    
	}
	
	#submitButton{
	
	            background-color: #2ecc71;
	            border: none;
	            border-radius: 30px;
	            color: #fff;
	            cursor: pointer;
	            padding: 10px;
	            width: 33%;
	            
	        }
	        
	           
	#insertBackButton{
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

<meta charset="UTF-8">

<title>New Invasive Species Reporting Form</title>

<!-- getting the current user name to be display on the reporting case form page -->
<%
	String currentUser = (String)request.getAttribute("currentUserName_insert");
	%>

<body style =  "background-color: #c6f5cc">

<div class = "container" >
	
	<div class = "reportform">
	    <h1> New Invasive Species Reporting Form (Canada) </h1>
	    <form method = "post" action="listOfCaseServlet" id="formSpecies">
	        <label for ="newSpecies">New Species:</label>
	        <input type = "text" id="newSpecies" name="newSpecies" pattern = "[A-Za-z\s]*$" title="No numbers or special characters" maxlength="100" required>
	        <br>
	        <br>
	        <label for ="dateReport">Reporting Date:</label>
	        <input type = "date" id="dateReport" name="dateReport" required>
	        <br>
	        <br>
	        <label for ="reportername">Reporter Name:</label>
	        <input type = "text" id="reporterName" name="reporterName" pattern = "[A-Za-z\s]*$" title="No numbers or special characters" maxlength="50" required>
	        <br>
	        <br>
	        <label for ="username">Current User: <%out.print(currentUser);%></label>
	        <input type = "hidden" value = <%out.print(currentUser);%> id="userName" name="userName">
	        <br>
	        <br>
	        <label for="province">Province / Territories:</label>
	          <select id="province" name="province" required>
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
	        <input type = "text" id="coordinates" name="coordinates" placeholder = " Long, Lat (Eg. 40.74,-73.98)" required>
	        <br>
	        <br>
	        <input type = "hidden" value = "6" name="insertCaseButtonValue">
	        <input type = "submit" value = "Submit Form" id = "submitButton">
	    </form>
	        <br/>
	    <form method = "post" action="listOfCaseServlet">
	    	<input type = "hidden" value = "7" name = "insertBackCaseButtonValue">
	    	<input type = "hidden" value = <%out.print(currentUser);%> name = "currentUserInsertBackButton">
	    	<input type = "submit" value = "Back" id="insertBackButton">
	    </form>
		
	</div>
	<div class = "imageside">
	
		<img src="images/forest.jpg" alt="Background Image">
	
	</div>
	
</div>

</body>
</html>