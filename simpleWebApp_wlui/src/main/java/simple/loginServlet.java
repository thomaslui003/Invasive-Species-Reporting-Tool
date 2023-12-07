package simple;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import javax.servlet.ServletException;
import simple.newReportedCase;

import org.tinylog.Logger;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String OPERATION_PARAM = "operation";
	public static final String FILTER_PARAM = "filter";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public loginServlet() {
		super();
	}

	/**
	 * This doGet method load the initial login.jsp page
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Logger.info("doGet Method of the LoginServlet");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String operation = (String) request.getParameter(OPERATION_PARAM);
		String filter = (String) request.getParameter(FILTER_PARAM);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		// send response with register result
		response.getWriter().write("operation = " + operation);
		response.getWriter().write("<br>filter = " + filter);

		request.getRequestDispatcher("/Login.jsp").forward(request, response);
	}

	/**
	 * doPost function triggered from login button on the login JSP page. The
	 * function validates the login by checking the username and password. If the
	 * login is valid,it creates two ArrayLists to hold the current user's reported
	 * cases and other users' reported cases. These ArrayLists are then passed to
	 * the listCase JSP file for displaying the reported cases in the frontend. By
	 * separating the cases into two arraylists, it restricts user access to delete
	 * and update functionalities for the reported cases. If the login validation
	 * fails, redirects back to the login page with a failed popup message.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Logger.info("doPost Method for the LoginServlet");

		// getting the username and password from the tag in the login page form
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		JDBCpostgresconn conn = new JDBCpostgresconn();

		// hashing the password with SHA256
		String hashedpassword = conn.getSecurePwd(password);

		Boolean validatedOrNot = false;
		// value to indicate if its executing test query or the normal query, 0 is
		// normal and 1 is test
		int testOrNot = 0;

		// validateLogin function called to check if username and password exist and
		// matches in the userlist table and returns a boolean value for valid or not
		// valid
		try {

			// with the conn object to access validateLogin function, parameters like
			// username, password, and testOrNot are input to check login validation via a
			// select query
			validatedOrNot = conn.validateLogin(username, hashedpassword, testOrNot);

			// if login is valid, it creates a Arraylist to hold newReportedCase objects.
			if (validatedOrNot) {

				// once login is valid, set the logged-in username to the current_user variable
				ArrayList<newReportedCase> reportedCasesData = null;
				ArrayList<newReportedCase> reportedCasesDataAllExceptCurrent = null;
				Logger.info("user login success for " + username + "and the valid boolean value is: " + validatedOrNot);

				try {

					// sending the current login username to the listCase.jsp page
					request.setAttribute("currentUserName", username);

					// using the current logged in user (username variable) to create an arraylist
					// of reported case associated with the user
					reportedCasesData = conn.displayInsertedCases(username, 0);
					// using the current logged in user to create an arraylist of reported case not
					// associated with the current logged in user
					reportedCasesDataAllExceptCurrent = conn.displayOthersInsertedCases(username, 0);

					// default value for indicating both arraylist are null for the frontend
					String noSearchResult = "1";

					// indicating either one or both arraylist is null (listCase.jsp will
					// validate this value whether to display any reported cases or not)
					if (reportedCasesData != null || reportedCasesDataAllExceptCurrent != null) {
						// reported cases exist
						noSearchResult = "0";
					} else {
						// no reported cases exist
						noSearchResult = "1";
					}

					// forwarding Arraylist data to listCase.jsp
					request.setAttribute("cases", reportedCasesData);
					request.setAttribute("otherCases", reportedCasesDataAllExceptCurrent);
					request.setAttribute("noResultFoundValue", noSearchResult);
					// forwarding all requests to listCase.jsp
					request.getRequestDispatcher("listCase.jsp").forward(request, response);

				} catch (SQLException e) {

					e.printStackTrace();
					Logger.error(
							"displayInsertedCases and displayOthersInsertedCases failed in LoginServlet doPost method");

				}

			} else {

				// if login is not valid,then redirect back to login page
				Logger.info("Login not valid, redirected back to the login page");

				// Login failed, set an attribute to indicate failure to signal the popup error
				// message (for the Javascript script to trigger)
				request.setAttribute("loginFailed", true);
				request.getRequestDispatcher("Login.jsp").forward(request, response);

			}

		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error("validateLogin method call failed at doPost method in loginSerlvet");
			// redirect to error page
			response.sendRedirect("errorPage.jsp");


		}

	}

}
