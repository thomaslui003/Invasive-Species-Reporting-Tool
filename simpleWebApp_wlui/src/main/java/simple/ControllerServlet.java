package simple;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.tinylog.Logger;
import java.security.SecureRandom;
import java.sql.SQLException;

public class ControllerServlet extends HttpServlet {
	public static final String OPERATION_PARAM = "operation";
	public static final String FILTER_PARAM = "filter";

	public ControllerServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Logger.info("doGet Method for the ControllerServlet");
		String operation = (String) request.getParameter(OPERATION_PARAM);
		String filter = (String) request.getParameter(FILTER_PARAM);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		// send response with register result
		response.getWriter().write("operation = " + operation);
		response.getWriter().write("<br>filter = " + filter);
		//request.getRequestDispatcher("/myFile.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost (HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Logger.info("doPost Method for the ControllerServlet");
		
//		SecureRandom rand = new SecureRandom();
//		int upperbound = 1000;
//		int int_randNumber = rand.nextInt(upperbound);//range is 0 to 999
//		int caseid = int_randNumber;
		
		String species = request.getParameter("newSpecies");
		String dateReport = request.getParameter("dateReport");
		Date date=Date.valueOf(dateReport);
		String reporterName = request.getParameter("reporterName");
		String username = request.getParameter("userName");
		String province = request.getParameter("province");
		String coordinates = request.getParameter("coordinates");
		
		JDBCpostgresconn conn = new JDBCpostgresconn();
		
		//conn.insertReportedCase(species, date, reporterName, username, province, coordinates);
		
		request.getRequestDispatcher("/reportPage.jsp").forward(request, response);
//		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("myFile.jsp");
//		dispatcher.forward(request, response);
//		HttpSession session = request.getSession(true);
//		
//		response.sendRedirect(request.getContextPath() + "myFile.jsp");
		
		
	}

}
