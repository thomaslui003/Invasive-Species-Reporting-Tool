package simple;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.tinylog.Logger;

/**
 * performFunctions class mainly responsible for processing the functionality of
 * the user's interaction with buttons. It performs a range of operations
 * including reporting, delete, update, and search reported cases. All of
 * functionality's further operations are done in the JDBCpostgresconn class.
 * 
 * @since last revision: June 4 2023
 * @author wlui
 *
 */
public class performFunctions {

	/**
	 * reportCaseButtonTask method to take the current username along with the
	 * current request and response to set the username in the reportPage.jsp
	 * 
	 * @param request  current HTTP servlet request
	 * @param response current HTTP servlet response
	 * @param conn     initialized JDBCpostgresconn object to call the app's query
	 *                 function
	 * @throws ServletException
	 * @throws IOException
	 */
	public void reportCaseButtonTask(HttpServletRequest request, HttpServletResponse response, JDBCpostgresconn conn,
			String currentUser) throws ServletException, IOException {

		// setting the current user's username as global value
		// String user_Name = loginServlet.current_user;
		String userName = currentUser;

		// sending the current login username to the reportPage.jsp page
		request.setAttribute("currentUserName_insert", userName);
		// forwarding all request to reportPage.jsp (the reporting a Case page)
		request.getRequestDispatcher("reportPage.jsp").forward(request, response);

	}

	/**
	 * deleteCaseButtonTask get the current user and the case id of the reported
	 * case from listCase.jsp and call the deleteCase method from JDBCpostgresconn
	 * to delete the specific reported case
	 * 
	 * @param request  		current HTTP servlet request
	 * @param response 		current HTTP servlet response
	 * @param conn     		initialized JDBCpostgresconn object to call the app's query
	 * @return currentUser  the current user who requested the delete operation
	 */
	public String deleteCaseButtonTask(HttpServletRequest request, HttpServletResponse response,
			JDBCpostgresconn conn) {

		String currentUser = null;
		try {

			// getting the current_user and case_id variable from the listCase.jsp with the
			// delete button hidden input form
			currentUser = (String) request.getParameter("username_ForDelete");
			String caseIdString = request.getParameter("case_id_ForDelete");
			int caseid = Integer.parseInt(caseIdString);
			// using the current user and caseID to identify the reported case for deletion
			conn.deleteCase(caseid, currentUser, 0);

		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error(
					"deleteCase of JDBCpostgresconn failed in the delteCaseButtonTask method in performFunction class");
		}

		return currentUser;
	}

	/**
	 * updateCaseButtonGetInfo gets all of the parameters variables from
	 * listCase.jsp including current user, case id, and date to be display on the
	 * update form page.
	 * 
	 * @param request  current HTTP servlet request
	 * @param response current HTTP servlet response
	 * @param conn     initialized JDBCpostgresconn object to call the app's query
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateCaseButtonGetInfo(HttpServletRequest request, HttpServletResponse response, JDBCpostgresconn conn)
			throws ServletException, IOException {

		// getting the current_user, case_id, date variable from the listCase.jsp with
		// the update button hidden input form
		String currentUser = (String) request.getParameter("username_ForUpdate");
		String caseId = request.getParameter("case_id_ForUpdate");
		String date = request.getParameter("date_ForUpdate");

		// sending the values to the update.jsp page
		request.setAttribute("currentUserName_update", currentUser);
		request.setAttribute("caseid_update", caseId);
		request.setAttribute("reportedDate_update", date);

		// forwarding current user, case id, date to update.jsp
		request.getRequestDispatcher("update.jsp").forward(request, response);

	}

	/**
	 * updateCaseButtonTask method get all of the user wanted updating variables
	 * from update.jsp and if the input met the input validation it will update the
	 * specific reported case via updateReportedCase else it will redirect to the
	 * error page
	 * 
	 * @param request  current HTTP servlet request
	 * @param response current HTTP servlet response
	 * @param conn     initialized JDBCpostgresconn object to call the app's query
	 * @return currentUser the current user that is wanting to update their own
	 *         reported case
	 * @throws IOException
	 */
	public String updateCaseButtonTask(HttpServletRequest request, HttpServletResponse response, JDBCpostgresconn conn)
			throws IOException {

		String currentUser = null;
		try {

			// getting the variables below from the update.jsp with the "submit update"
			// button from user input form
			String speciesNameUpdate = request.getParameter("newSpecies_updateP2");
			String reporterNameUpdate = request.getParameter("reporterName_updateP2");
			String provinceUpdate = request.getParameter("province_updateP2");
			String coordinatesUpdate = request.getParameter("coordinates_updateP2");
			String caseIdUpdate = request.getParameter("caseId_updateP2");
			currentUser = request.getParameter("currentUser_updateP2");
			int caseIdUpdateInt = Integer.parseInt(caseIdUpdate);

			// if no user input for a column, assign null to the specific variable and use
			// the COALESCE update function to select the original value instead of
			// assigning an empty value to it
			if (speciesNameUpdate == "") {
				speciesNameUpdate = null;
			}
			if (reporterNameUpdate == "") {
				reporterNameUpdate = null;
			}
			if (provinceUpdate == "") {
				provinceUpdate = null;
			}
			if (coordinatesUpdate == "") {
				coordinatesUpdate = null;
			}

			try {
				conn.inputValidationUpdate(speciesNameUpdate, reporterNameUpdate, provinceUpdate,
						coordinatesUpdate);
				// using the speciesName_update, reporterName_update, province_update,
				// coordinates_update,caseId_update_int to do an update query for the specific
				// reported case
				conn.updateReportedCase(speciesNameUpdate, reporterNameUpdate, provinceUpdate, coordinatesUpdate,
						caseIdUpdateInt, 0);

			} catch (IllegalArgumentException e) {

				// catching the exception for input that violated each of the attribute
				// requirement
				Logger.error("User input violates the attribute input requirement for updating a case");
				response.sendRedirect("errorPage.jsp"); // add back the restriction on the jsp

			}

		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error("updateReportedCase query failed at updateCaseButtonTask in performFunctions class");
		}

		return currentUser;

	}

	/**
	 * insertCaseButtonTask method gets the info parameters of the reported case by
	 * the current user in the reportPage.jsp then validate the input and insert the
	 * reported case to the database via an insert query
	 * 
	 * @param request  current HTTP servlet request
	 * @param response current HTTP servlet response
	 * @param conn     initialized JDBCpostgresconn object to call the app's query
	 * @return currentUser the current user who wanted to report a case
	 * @throws IOException
	 * @throws ServletException
	 */
	public String insertCaseButtonTask(HttpServletRequest request, HttpServletResponse response, JDBCpostgresconn conn)
			throws IOException, ServletException {

		String currentUser = null;

		try {

			// getting the variables below from the reportPage.jsp with the "submit" button
			// from user input form
			String species = request.getParameter("newSpecies");
			String dateReport = request.getParameter("dateReport");
			Date date = Date.valueOf(dateReport);
			String reporterName = request.getParameter("reporterName");
			String username = request.getParameter("userName");
			currentUser = username;
			String province = request.getParameter("province");
			String coordinates = request.getParameter("coordinates");
			int testOrNot = 0;

			try {
				// validating the user input for insertion
				conn.inputValidation(species, date, reporterName, province, coordinates);
				// taking all user input and perform a insert query to report a case
				conn.insertReportedCase(species, date, reporterName, username, province, coordinates, testOrNot);

				// redirect to and refresh the listCase jsp page
				listOfCaseServlet temp = new listOfCaseServlet();
				temp.doGet(request, response);

			} catch (IllegalArgumentException e) {
				// catching the exception for input that violated each of the attribute
				// requirement
				Logger.error("User input violates the attribute input requirement for submitting a new case");
				response.sendRedirect("errorPage.jsp"); // add back the restriction on the jsp
			}

		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error("Submission of the new reporting case was not successful in listOfCaseServlet");
		}
		return currentUser;

	}

	/**
	 * searchCaseButtonTask method take input parameter form the search input in the
	 * listCase.jsp and validate the search input and execute the searchReportedCase
	 * method to query the result based on the input. If no result found, then it
	 * will show a no result found message else it will show the queried result. If
	 * the input failed the input validation, it will redirect to an error page.
	 * 
	 * @param request  current HTTP servlet request
	 * @param response current HTTP servlet response
	 * @param conn     initialized JDBCpostgresconn object to call the
	 *                 jdbcpostgresconn's query
	 * @throws IOException
	 * @throws ServletException
	 */
	public void searchCaseButtonTask(HttpServletRequest request, HttpServletResponse response, JDBCpostgresconn conn)
			throws IOException, ServletException {

		ArrayList<newReportedCase> reportedCasesData = null;
		ArrayList<newReportedCase> reportedCasesDataAllExceptCurrent = null;

		// when the search button is triggered in the listCase jsp page, it takes all of
		// the input data from user and look through relevant reported case data
		try {

			// getting the variables below from the listcase.jsp with the "search" button
			// from the user input form
			String username = request.getParameter("currentUserSearchForm");
			String species = request.getParameter("newSpecies_search");
			String reporterName = request.getParameter("reporterName_search");
			String province = request.getParameter("province_search");
			int testOrNot = 0;

			// this is executing
			Logger.info("Current searching the following: species: " + species + " , reporter name: " + reporterName
					+ " ,province: " + province);

			try {

				// taking all user input and perform a search query to report a case
				// conn.insertReportedCase(species, date, reporterName, username, province,
				// coordinates, testOrNot);
				Map<String, ArrayList<newReportedCase>> mapArray = new HashMap<String, ArrayList<newReportedCase>>();

				conn.inputValidationSearch(species, reporterName, province);
				mapArray = conn.searchReportedCase(species, reporterName, province, username, testOrNot);

				Logger.info("the size of hashmap: " + mapArray.size());
				reportedCasesData = mapArray.get("user_caseArray");
				// arraylist of reported cases for not the current user
				reportedCasesDataAllExceptCurrent = mapArray.get("other_caseArray");

				// check if both reportedCasesData and reportedCasesDataAllExceptCurrent is
				// empty (no search result found)

				if (reportedCasesData == null && reportedCasesDataAllExceptCurrent == null) {
					Logger.info("both the arraylist is empty - no result from search found");
					String noResultFound = "1";
					request.setAttribute("currentUserName", username);
					request.setAttribute("noResultFoundValue", noResultFound);

				} else {

					String noResultFound = "0";

					// forwarding Arraylist data to listCase.jsp
					request.setAttribute("noResultFoundValue", noResultFound);
					request.setAttribute("currentUserName", username);
					request.setAttribute("cases", reportedCasesData);
					request.setAttribute("otherCases", reportedCasesDataAllExceptCurrent);

				}

				request.getRequestDispatcher("listCase.jsp").forward(request, response);

			} catch (IllegalArgumentException e) {
				// catching the exception for input that violated each of the attribute
				// requirement
				Logger.error("User input violates the attribute input requirement for searching");
				// redirect to error page
				response.sendRedirect("errorPage.jsp");

			}

		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error("searchReportedCase operation failed in doPost of the performFunction");
		}

		Logger.info("searchButton in update.jsp is triggered and refresh result with listcase page");

	}

}
