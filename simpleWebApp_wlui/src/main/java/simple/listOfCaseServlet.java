package simple;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.nfis.db.PostgresConnectionManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.tinylog.Logger;
import java.util.Map;
import java.util.HashMap;
import simple.newReportedCase;
import simple.loginServlet;

/**
 * listofCaseServlet class mainly process the functionality of all user's button
 * request. The class has a doGet that refresh and re-query all of the reported
 * case listing for the listCase.jsp. The doPost method is responsible for
 * processing the functionality such as search, update, insert, and delete
 * requested by the user's interaction with the frontend button. Each button
 * will contain a hidden value in the form and trigger the according
 * functionality for the web app
 * 
 * @author Wen Luo (Thomas) Lui
 * @since last revision: June 2 2023
 *
 */
@WebServlet("/listOfCaseServlet")
public class listOfCaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public listOfCaseServlet() {
		super();
	}

	/**
	 * doGet function performs a refresh page for the listCase jsp page after
	 * clicking on the refresh button
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Logger.info("doGet Method for the listOfCaseServlet");

		ArrayList<newReportedCase> reportedCasesData = null;
		ArrayList<newReportedCase> reportedCasesDataAllExceptCurrent = null;
		String currentUser = null;

		try {

			// get the session to access the current user variable after delete button
			// calling doGet to refresh page
			HttpSession session = request.getSession();

			// Refresh page:
			// getting the current_user variable from the listCase.jsp with the refresh
			// button hidden input form or get from the doPost deleteButton method
			if (currentUser == null) {
				currentUser = (String) request.getParameter("crUser");
				if (currentUser == null) {
					currentUser = (String) session.getAttribute("currentUserGeneralValue");
				}
			}

			// sending the current login username and noSearchResult to the listCase.jsp
			// page
			request.setAttribute("currentUserName", currentUser);
			String noSearchResult = "0";
			request.setAttribute("noResultFoundValue", noSearchResult);

			JDBCpostgresconn conn = new JDBCpostgresconn();

			// refreshes the listCase jsp page by requerying on the data based on the
			// current logged-in user.
			reportedCasesData = conn.displayInsertedCases(currentUser, 0);
			reportedCasesDataAllExceptCurrent = conn.displayOthersInsertedCases(currentUser, 0);

			// forwarding arraylist data to listCase.jsp
			request.setAttribute("cases", reportedCasesData);
			request.setAttribute("otherCases", reportedCasesDataAllExceptCurrent);
			request.getRequestDispatcher("listCase.jsp").forward(request, response);

		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error(
					"displayInsertedCases and displayOthersInsertedCases failed at toGet method of listOfCaseServlet");
		}

	}

	/**
	 * doPost processes all of the user interaction's button triggered functionality
	 * and perform the according query and functionality such as search, insert,
	 * delete, and update with the button trigger value.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Logger.info("doPost Method for the listOfServlet with deletion query");
		// Trigger value for the different button in the listCase jsp such that when a
		// specific button is triggered the form will assign a value to the specific
		// variable
		String reportCaseButtonTriggerValue = "0";
		String deleteCaseButtonTriggerValue = "0";
		String updateCaseButtonTriggerValue = "0";
		String updateCaseButtonTriggerValueP2 = "0";
		String updateCaseBackButtonTriggerValue = "0";
		String insertCaseButtonTriggerValue = "0";
		String insertCaseBackButtonTriggerValue = "0";
		String searchCaseButtonTriggerValue = "0";
		String logoutButtonTriggerValue = "0";
		JDBCpostgresconn conn = new JDBCpostgresconn();
		performFunctions task = new performFunctions();

		// The following get button trigger parameter from the listCase.jsp
		reportCaseButtonTriggerValue = request.getParameter("reportCaseButtonValue");
		deleteCaseButtonTriggerValue = request.getParameter("deleteCaseButtonValue");
		updateCaseButtonTriggerValue = request.getParameter("updateCaseButtonValue");
		searchCaseButtonTriggerValue = request.getParameter("searchCaseButtonValue");
		logoutButtonTriggerValue = request.getParameter("logoutButtonValue");

		// The following get button trigger parameter from the update.jsp
		updateCaseButtonTriggerValueP2 = request.getParameter("updateCaseButtonValuePart2");
		updateCaseBackButtonTriggerValue = request.getParameter("updateCaseBackButtonValue");

		// The following get button trigger parameter from the reportPage.jsp
		insertCaseButtonTriggerValue = request.getParameter("insertCaseButtonValue");
		insertCaseBackButtonTriggerValue = request.getParameter("insertBackCaseButtonValue");

		// if the reportCaseButtonTriggerValue is not null and equals to "1", it take
		// the current user's username and send it to the reportPage jsp (the reporting
		// case form) such that the reported case have the current user's username
		// attached to the case in the reportCaseButtonTask method.
		if (!(reportCaseButtonTriggerValue == null) && reportCaseButtonTriggerValue.equals("1")) {

			String currentUser = request.getParameter("currentUserReportButton");
			Logger.info("ReportCase button triggered for the listOfCaseServlet");
			task.reportCaseButtonTask(request, response, conn, currentUser);

		} else if (!(deleteCaseButtonTriggerValue == null) && deleteCaseButtonTriggerValue.equals("2")) {
			// when deleteCase Button is triggered, it performs the deletes function on the
			// specific reported case with caseid and current username.

			String currentUser = task.deleteCaseButtonTask(request, response, conn);
			Logger.info("deleteCaseButton triggered and the doGet method is called to refresh the page");
			// pass the currentUser to the doPost via Session
			HttpSession session = request.getSession();
			session.setAttribute("currentUserGeneralValue", currentUser);
			// triggering the doGet method to refresh the page after deletion
			doGet(request, response);

		} else if (!(updateCaseButtonTriggerValue == null) && updateCaseButtonTriggerValue.equals("3")) {
			// when "report a case" button is triggered on the specific reported case on the
			// list, it redirects to the update jsp page for the user to fill the update
			// information with username, caseid, and inital reported date field can't be
			// modify.

			// It forwards existing data to the update page in the
			// updateCaseButtonGetInfo method.
			task.updateCaseButtonGetInfo(request, response, conn);

		} else if (!(updateCaseButtonTriggerValueP2 == null) && updateCaseButtonTriggerValueP2.equals("4")) {
			// when the updateCase button is triggered in update jsp page, it takes all
			// updated data and performs the update query and refreshes the reported case
			// list by calling doGet method

			String currentUser = task.updateCaseButtonTask(request, response, conn);
			// sending the current user variable to the doGet method above
			HttpSession session = request.getSession();
			session.setAttribute("currentUserGeneralValue", currentUser);

			// take the current user and refresh the list of cases page after submitting the
			// update
			doGet(request, response);

		} else if (!(updateCaseBackButtonTriggerValue == null) && updateCaseBackButtonTriggerValue.equals("5")) {
			// when the back button in update jsp page is triggered, it redirects to the
			// listCase jsp page to show all of the cases

			String currentUser = request.getParameter("currentUserUpdateBackButton");
			// sending the current user variable to the doGet method above
			HttpSession session = request.getSession();
			session.setAttribute("currentUserGeneralValue", currentUser);

			doGet(request, response);

		} else if (!(insertCaseButtonTriggerValue == null) && insertCaseButtonTriggerValue.equals("6")) {

			// calling insertCaseButtonTask such that when the submit button is triggered in
			// the reportPage jsp page, it takes all of the input data and insert it into
			// the reportedcases table as a new case
			String currentUser = task.insertCaseButtonTask(request, response, conn);
			// sending the current user variable to the doGet method above
			HttpSession session = request.getSession();
			session.setAttribute("currentUserGeneralValue", currentUser);

			Logger.info(
					"the insertButton inside reportPage.jsp is triggered and doGet redirect and refresh to the list case page ");

		} else if (!(insertCaseBackButtonTriggerValue == null) && insertCaseBackButtonTriggerValue.equals("7")) {
			// when the back button in reportPage jsp page is triggered, it redirects back
			// to the listCase jsp page and refresh the listing reroute back to the listing
			// case page (listCase.jsp)

			String currentUser = request.getParameter("currentUserInsertBackButton");
			// sending the current user variable to the doGet method above
			HttpSession session = request.getSession();
			session.setAttribute("currentUserGeneralValue", currentUser);

			doGet(request, response);

		} else if (!(searchCaseButtonTriggerValue == null) && searchCaseButtonTriggerValue.equals("8")) {

			// calling the searchCaseButtonTask method to perform the search task based on
			// the user input
			task.searchCaseButtonTask(request, response, conn);
			

		}else if(!(logoutButtonTriggerValue == null) && logoutButtonTriggerValue.equals("9")) {
			
			request.getSession().invalidate();
			// redirect to login page upon pressing the logout
			response.sendRedirect("Login.jsp");
			

			
		}

		else {

			// log message when a button was pressed and no action in doPost when called
			Logger.error(
					"A button was pressed on the listCase.jsp but no action was performed in doPost listOfCaseServlet");
			// redirect to error page
			response.sendRedirect("errorPage.jsp");


		}

	}

}