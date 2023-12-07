package simple;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.security.MessageDigest;
import org.nfis.db.PostgresConnectionManager;
import org.tinylog.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.sql.DataSource;

/**
 * This JDBpostgresconn class consists of all the core function of the web app
 * such as insert, delete, search, update, and login validation. It also have functions to
 * initialize a tomcat connection via jdbc to the postgres database as well as
 * function to hash password for the user login.
 * 
 * @since last revision: June 2 2023
 * @author Wen Luo (Thomas) Lui
 *
 */
public class JDBCpostgresconn {

	// SQL Query for normal functions
	private static final String INSERT_REPORT_SQL = "INSERT INTO reportedcases (species_name, date_reported, reporter_name, user_name, province, coordinates)VALUES (?, ?, ?, ?, ?,?);";
	private static final String LOGIN_CHECK_SQL = "SELECT * FROM userlist WHERE user_name=? AND password=?";
	private static final String DISPLAY_REPORT_SQL = "SELECT * FROM reportedcases WHERE user_name = ?;";
	private static final String DISPLAY_ALL_OTHER_REPORT_SQL = "SELECT * FROM reportedcases WHERE NOT user_name = ?;";
	private static final String UPDATE_CASE_SQL = "UPDATE reportedcases SET species_name = COALESCE(?,species_name), reporter_name = COALESCE(?,reporter_name), province = COALESCE(?,province),coordinates = COALESCE(?,coordinates) WHERE case_id = ?";
	private static final String DELETE_CASE_SQL = "DELETE FROM reportedcases WHERE case_id = ? AND user_name = ?";
	private static final String SEARCH_SQL = "SELECT * FROM reportedcases WHERE UPPER(species_name) LIKE UPPER(?) AND UPPER(reporter_name) LIKE UPPER(?) AND UPPER(province) LIKE UPPER(?)";

	// SQL Query for testing functions
	private static final String INSERT_REPORT_SQL_TEST = "INSERT INTO testreportedcases (species_name, date_reported, reporter_name, user_name, province, coordinates)VALUES (?, ?, ?, ?, ?,?);";
	private static final String LOGIN_CHECK_SQL_TEST = "SELECT * FROM testuserlist WHERE user_name=? AND password=?";
	private static final String DISPLAY_REPORT_SQL_TEST = "SELECT * FROM testreportedcases WHERE user_name = ?;";
	private static final String DISPLAY_ALL_OTHER_REPORT_SQL_TEST = "SELECT * FROM testreportedcases WHERE NOT user_name = ?;";
	private static final String UPDATE_CASE_SQL_TEST = "UPDATE testreportedcases SET species_name = COALESCE(?,species_name), reporter_name = COALESCE(?,reporter_name), province = COALESCE(?,province),coordinates = COALESCE(?,coordinates) WHERE case_id = ?";
	private static final String DELETE_CASE_SQL_TEST = "DELETE FROM testreportedcases WHERE case_id = ? AND user_name = ?";
	private static final String DELETE_ROWS_SQL_TEST = "DELETE FROM testreportedcases";
	private static final String SEARCH_SQL_TEST = "SELECT * FROM testreportedcases WHERE UPPER(species_name) LIKE UPPER(?) AND UPPER(reporter_name) LIKE UPPER(?) AND UPPER(province) LIKE UPPER(?)";

	/**
	 * getSecurePassword method performs a hashing process for incoming password and
	 * salt from the user input. It uses the SHA 256 encrypting algorithm to provide
	 * hashing for passwords.
	 * 
	 * @param base password variable from user input to do hashing
	 * @return hashed password
	 */
	public String getSecurePwd(final String base) {
		try {
			final MessageDigest digest = MessageDigest.getInstance("SHA-256");
			final byte[] hash = digest.digest(base.getBytes("UTF-8"));
			final StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < hash.length; i++) {
				final String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * getTomcatConnection method uses the context.xml file to locate the specific
	 * database to be connected to and returns a tomcat connection. The file
	 * ultimately hides and holds the username and password for the postgres
	 * database.
	 * 
	 * @return tomcatConnection - the tomcat connection to postgres database
	 * @throws SQLException
	 */
	public Connection getTomcatConnection() throws SQLException {

		Connection tomcatConnection = null;

		try {
			// using the context.xml file to get the tomcat connection
			Context ctx = new InitialContext();
			Context initCtx = (Context) ctx.lookup("java:/comp/env");
			DataSource ds = (DataSource) initCtx.lookup("jdbc/newinvasivespeciesdb");
			tomcatConnection = ds.getConnection();

		} catch (NamingException e) {

			e.printStackTrace();
			Logger.error("Tomcat Connection failed in the getTomcatConnection method");

		}

		return tomcatConnection;
	}

	/**
	 * insertReportedCase method performs an insert query to the
	 * newinvasivespeciesdb (reportedcases table) with the user input data by the
	 * current user
	 * 
	 * @param caseid      unique identifier for the reported case
	 * @param species     the name of the new species associated with the reported
	 *                    case
	 * @param username    the name of the user associated with the reported case
	 * @param date        the date when the case was reported
	 * @param province    the province where the case was reported
	 * @param coordinates the coordinates associated with the reported case
	 * @throws SQLException
	 */
	public void insertReportedCase(String speciesName, Date dateReported, String reporterName, String username,
			String province, String coordinates, int testOrNot) throws SQLException {

		PreparedStatement prepStmt = null;
		Connection tomcatConn = null;
		PostgresConnectionManager postConnManger = null;
		Connection postConn = null;

		try {

			tomcatConn = getTomcatConnection();

			// testOrNot int variable: 0 = running normal query, 1 = running test query
			if ((tomcatConn != null) && testOrNot == 0) {
				// inserting query to table reportedcases in newinvasivespeciesdb using
				// prepare statement with reported case data
				prepStmt = tomcatConn.prepareStatement(INSERT_REPORT_SQL);
				{
					prepStmt.setString(1, speciesName);
					prepStmt.setDate(2, dateReported);
					prepStmt.setString(3, reporterName);
					prepStmt.setString(4, username);
					prepStmt.setString(5, province);
					prepStmt.setString(6, coordinates);

					// executing the insertion query
					prepStmt.executeUpdate();

				}

				// if testOrNot value equals 1, run test insert query and use
			} else if (testOrNot == 1) {

				postConnManger = new PostgresConnectionManager("localhost", "newinvasivespeciesdb", 5432, "postgres",
						"123456");
				postConn = postConnManger.getConnection();

				// for inserting into test db via prepare statement
				prepStmt = postConn.prepareStatement(INSERT_REPORT_SQL_TEST);
				{
					prepStmt.setString(1, speciesName);
					prepStmt.setDate(2, dateReported);
					prepStmt.setString(3, reporterName);
					prepStmt.setString(4, username);
					prepStmt.setString(5, province);
					prepStmt.setString(6, coordinates);

					prepStmt.executeUpdate();
				}

			} else {

				Logger.error("Insert query in insertReportedCase method of JDBCpostgresconn was not successful");

			}
		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error("Insert query in insertReportedCase method was not successful");

		} finally {

			// closing the prepare statement and connection to db
			try {

				prepStmt.close();

				// close the specific connection according to the testOrNot value
				if (postConnManger != null && testOrNot == 1) {
					postConnManger.closeConnection(postConn);
				} else if (tomcatConn != null && testOrNot == 0) {
					tomcatConn.close();
				} else {
					Logger.error("Closing connection failed in insertReportedCase, testOrNot value must other values");
				}

			} catch (Exception e) {
				Logger.error("Closing connection failed in insertReportedCase method of JDBCpostgresconn");
			}
		}

	}

	/**
	 * validateLogin method takes in username and password from the login page form
	 * and runs the LOGIN_CHECK_SQL query to check whether the database have a
	 * matching username with password row
	 * 
	 * @param username  the name of the current user trying to login
	 * @param password  the hashed password of the current user input for the login
	 * @param testOrNot int value indicating using test query or normal query (0 =
	 *                  normal, 1 = test)
	 * @return boolean value where true is valid login and false is non-valid login
	 * @throws SQLException
	 */
	public boolean validateLogin(String username, String password, int testOrNot) throws SQLException {

		PreparedStatement prepStmt = null;
		// default false boolean value for not valid login
		Boolean status = false;
		Connection tomcatConn = null;
		PostgresConnectionManager postConnManger = null;
		Connection postConn = null;

		try {

			tomcatConn = getTomcatConnection();

			// if connection is not null and is running the normal query
			if ((tomcatConn != null) && testOrNot == 0) {

				prepStmt = tomcatConn.prepareStatement(LOGIN_CHECK_SQL);
				{
					prepStmt.setString(1, username);
					prepStmt.setString(2, password);

					ResultSet rs = prepStmt.executeQuery();
					// status is the returning value from the query indicating valid or not valid
					// user login
					status = rs.next();

				}
				// if testOrNot variable equals to 1, then it is running the test query
			} else if (testOrNot == 1) {

				postConnManger = new PostgresConnectionManager("localhost", "newinvasivespeciesdb", 5432, "postgres",
						"123456");
				postConn = postConnManger.getConnection();

				prepStmt = postConn.prepareStatement(LOGIN_CHECK_SQL_TEST);
				{
					prepStmt.setString(1, username);
					prepStmt.setString(2, password);

					ResultSet rs = prepStmt.executeQuery();
					// returned value from the query indicating valid or not valid user login
					status = rs.next();

				}

			} else {

				Logger.error("Login validation in validateLogin was not successful");

			}
		} catch (SQLException e) {

			Logger.error("Login validation query in validateLogin was not successful");
			e.printStackTrace();

		} finally {
			// closing the prepare statement and connection
			try {

				prepStmt.close();

				// close the specific connection according to the testOrNot value
				if (postConnManger != null && testOrNot == 1) {
					postConnManger.closeConnection(postConn);
				} else if (tomcatConn != null && testOrNot == 0) {
					tomcatConn.close();
				} else {
					Logger.error("Closing connection failed in validateLogin, testOrNot value must other values");
				}

			} catch (Exception e) {
				Logger.error("Closing connection failed in validateLogin method of JDBCpostgresconn");
			}
		}
		// returning the login valid or not valid boolean value
		return status;

	}

	/**
	 * displayInsertedCases method performs a select query to fetch all of the
	 * reported cases that's belong to the parameter username variable (current
	 * user)
	 * 
	 * @param username  username of the current logged in user
	 * @param testOrNot int value indicating to run test query or normal query
	 * @return an arraylist of newReportedCase object that's only relevant to the
	 *         current user's reported cases
	 * 
	 * @throws SQLException
	 */
	public ArrayList<newReportedCase> displayInsertedCases(String username, int testOrNot) throws SQLException {

		PreparedStatement prepStmt = null;
		ArrayList<newReportedCase> caseArray = new ArrayList<newReportedCase>();
		Connection tomcatConn = null;
		PostgresConnectionManager postConnManger = null;
		Connection postConn = null;

		try {

			tomcatConn = getTomcatConnection();

			// if connection is not null and is running the normal query
			if ((tomcatConn != null) && testOrNot == 0) {

				prepStmt = tomcatConn.prepareStatement(DISPLAY_REPORT_SQL);
				prepStmt.setString(1, username);

				// resultSet for querying all result data of the user associated with the
				// username
				ResultSet rs = prepStmt.executeQuery();

				while (rs.next()) {

					// for each row of data, parsing the data accordingly based on the column index
					int caseId = Integer.parseInt(rs.getString(1).trim());
					String speciesName = rs.getString(2).trim();
					Date dateReported = rs.getDate(3);
					String reporterName = rs.getString(4).trim();
					String userName = rs.getString(5).trim();
					String province = rs.getString(6).trim();
					String coordinates = rs.getString(7);

					// creating a newReportedCase object to be added to the Arraylist
					newReportedCase newCase = new newReportedCase();
					// since setter method of newReportedCase class validates the input data, the
					// object will use the setter method for setting its attributes
					newCase.setCaseId(caseId);
					newCase.setSpeciesName(speciesName);
					newCase.setDateReported(dateReported);
					newCase.setReporterName(reporterName);
					newCase.setUsername(userName);
					newCase.setProvince(province);
					newCase.setCoordinates(coordinates);

					// adding each new case and existing ones in db to the Arraylist to be pass to
					// the listCase jsp
					caseArray.add(newCase);

				}

				// resultSet for querying all result data of the user associated to the
				// username
				// if testOrNot is 1, then run the test query
			} else if (testOrNot == 1) {

				postConnManger = new PostgresConnectionManager("localhost", "newinvasivespeciesdb", 5432, "postgres",
						"123456");
				postConn = postConnManger.getConnection();

				prepStmt = postConn.prepareStatement(DISPLAY_REPORT_SQL_TEST);
				prepStmt.setString(1, username);

				// resultSet for querying all result data of the user associated to the
				// username
				ResultSet rs = prepStmt.executeQuery();

				while (rs.next()) {

					// for each row of data, parsing the data accordingly based on the column index
					int caseId = Integer.parseInt(rs.getString(1).trim());
					String speciesName = rs.getString(2).trim();
					Date dateReported = rs.getDate(3);
					String reporterName = rs.getString(4).trim();
					String userName = rs.getString(5).trim();
					String province = rs.getString(6).trim();
					String coordinates = rs.getString(7);

					// creating a newReportedCase object to be added to the Arraylist
					newReportedCase newCase = new newReportedCase();
					// validating the user input in the setter method of newReportedCase class in
					// backend
					newCase.setCaseId(caseId);
					newCase.setSpeciesName(speciesName);
					newCase.setDateReported(dateReported);
					newCase.setReporterName(reporterName);
					newCase.setUsername(userName);
					newCase.setProvince(province);
					newCase.setCoordinates(coordinates);

					// adding each new case and existing ones in db to the Arraylist to be pass to
					// the listCase jsp
					caseArray.add(newCase);

				}

			} else {

				Logger.error("Connection failed in displayInsertedCases");

			}
		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error("Connection failed in displayInsertedCases");

		} finally {

			// closing prepare statement and the specific connection
			try {
				prepStmt.close();

				// close the specific connection according to the testOrNot value
				if (postConnManger != null && testOrNot == 1) {
					postConnManger.closeConnection(postConn);
				} else if (tomcatConn != null && testOrNot == 0) {
					tomcatConn.close();
				} else {
					Logger.error(
							"Closing connection failed in displayInsertedCases, testOrNot value must other values");
				}

			} catch (Exception e) {

				Logger.error("Closing connection failed in displayInsertedCases");
			}
		}
		return caseArray;

	}

	/**
	 * This function queries all of the reported case data except for the current
	 * user's inserted data. First, it tries to connect to the database. And, with
	 * the prepare statement, it executes the DISPLAY_ALL_OTHER_REPORT_SQL query and
	 * returns a resultSet that have all of the associated data. Then, with the
	 * parsed ResultSet data, it creates a new reportedcase object to be added to
	 * the arraylist
	 * 
	 * @param username current username that is used to fetch all data not related
	 *                 to this user
	 * @return Arraylist of newReportedCase objects
	 * @throws SQLException
	 */
	public ArrayList<newReportedCase> displayOthersInsertedCases(String username, int testOrNot) throws SQLException {

		PreparedStatement prepStmt = null;
		ArrayList<newReportedCase> caseArray = new ArrayList<newReportedCase>();
		Connection tomcatConn = null;
		PostgresConnectionManager postConnManger = null;
		Connection postConn = null;

		try {

			tomcatConn = getTomcatConnection();

			// if connection is not null and is running the normal query
			if ((tomcatConn != null) && testOrNot == 0) {

				prepStmt = tomcatConn.prepareStatement(DISPLAY_ALL_OTHER_REPORT_SQL);
				prepStmt.setString(1, username);

				// resultSet for the querying all result data of the user with the associated
				// username
				ResultSet rs = prepStmt.executeQuery();

				while (rs.next()) {
					// creating newReportedCase Object for each row of input

					int caseId = Integer.parseInt(rs.getString(1).trim());
					String speciesName = rs.getString(2).trim();
					Date dateReported = rs.getDate(3);
					String reporterName = rs.getString(4).trim();
					String userName = rs.getString(5).trim();
					String province = rs.getString(6).trim();
					String coordinates = rs.getString(7);

					// creating a newReportedCase object to be added to the Arraylist
					newReportedCase newCase = new newReportedCase();
					// validating the user input in the setter method of newReportedCase class in
					// backend
					newCase.setCaseId(caseId);
					newCase.setSpeciesName(speciesName);
					newCase.setDateReported(dateReported);
					newCase.setReporterName(reporterName);
					newCase.setUsername(userName);
					newCase.setProvince(province);
					newCase.setCoordinates(coordinates);

					// adding each new case to the Arraylist to be pass to the listCase jsp
					caseArray.add(newCase);

				}
				// if testOrNot is 1, then run the test query
			} else if (testOrNot == 1) {
				// for testing

				postConnManger = new PostgresConnectionManager("localhost", "newinvasivespeciesdb", 5432, "postgres",
						"123456");
				postConn = postConnManger.getConnection();

				prepStmt = postConn.prepareStatement(DISPLAY_ALL_OTHER_REPORT_SQL_TEST);
				prepStmt.setString(1, username);

				// resultSet for the querying all result data of one user with username
				ResultSet rs = prepStmt.executeQuery();

				while (rs.next()) {

					// parsing each row of data from the query result
					int caseId = Integer.parseInt(rs.getString(1).trim());
					String speciesName = rs.getString(2).trim();
					Date dateReported = rs.getDate(3);
					String reporterName = rs.getString(4).trim();
					String userName = rs.getString(5).trim();
					String province = rs.getString(6).trim();
					String coordinates = rs.getString(7);

					// creating a newReportedCase object to be added to the Arraylist
					newReportedCase newCase = new newReportedCase();
					// validating the user input in the setter method of newReportedCase class in
					// backend
					newCase.setCaseId(caseId);
					newCase.setSpeciesName(speciesName);
					newCase.setDateReported(dateReported);
					newCase.setReporterName(reporterName);
					newCase.setUsername(userName);
					newCase.setProvince(province);
					newCase.setCoordinates(coordinates);

					// adding each new case to the Arraylist to be pass to the listCase jsp
					caseArray.add(newCase);

				}

			} else {

				Logger.error("Connection failed in displayOtherInsertedCases and testOrNot value is neither 0 or 1");

			}
		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error(
					"Fail to fetch all other user data in displayOtherInsertedCases method and testOrNot value is neither 0 or 1");

		} finally {

			// closing prepare statement and the specific connection
			try {
				prepStmt.close();

				// close the specific connection according to the testOrNot value
				if (postConnManger != null && testOrNot == 1) {
					postConnManger.closeConnection(postConn);
				} else if (tomcatConn != null && testOrNot == 0) {
					tomcatConn.close();
				} else {
					Logger.error(
							"Closing connection failed in displayOthersInsertedCases, testOrNot value must other values");
				}

			} catch (Exception e) {

				Logger.error("Closing connection failed in displayOthersInsertedCases");
			}
		}
		return caseArray;

	}

	/**
	 * This delete function deletes a specific reporting case created by the current
	 * login user through a delete query. The function first try to initialize
	 * connection with the Postgres database via PostgresConnectionManager and then
	 * delete the reporting case with the parameters below along with the prepared
	 * statements.
	 * 
	 * @param caseId   case id associated to the reported case that the current user
	 *                 wanted to delete
	 * @param username username of the current logged in user
	 * @throws SQLException
	 */
	public void deleteCase(int caseId, String username, int testOrNot) throws SQLException {

		PreparedStatement prepStmt = null;
		Connection tomcatConn = null;
		PostgresConnectionManager postConnManger = null;
		Connection postConn = null;

		try {

			tomcatConn = getTomcatConnection();

			// if connection is not null and is running the normal query
			if ((tomcatConn != null) && testOrNot == 0) {

				prepStmt = tomcatConn.prepareStatement(DELETE_CASE_SQL);
				prepStmt.setInt(1, caseId);
				prepStmt.setString(2, username);

				prepStmt.executeUpdate();

				Logger.info("Delete query is completed");

				// if testOrNot is 1, then run the test query
			} else if (testOrNot == 1) {
				// testing section

				postConnManger = new PostgresConnectionManager("localhost", "newinvasivespeciesdb", 5432, "postgres",
						"123456");
				postConn = postConnManger.getConnection();

				prepStmt = postConn.prepareStatement(DELETE_CASE_SQL_TEST);
				prepStmt.setInt(1, caseId);
				prepStmt.setString(2, username);

				prepStmt.executeUpdate();
				Logger.info("Delete query is completed in test");

			} else {

				Logger.error("Error: connection or delete failed in deleteCase function");

			}

		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error("Deletion query failed in the deleteCase function");

		} finally {

			// closing prepare statement and the specific connection
			try {
				prepStmt.close();

				// close the specific connection according to the testOrNot value
				if (postConnManger != null && testOrNot == 1) {
					postConnManger.closeConnection(postConn);
				} else if (tomcatConn != null && testOrNot == 0) {
					tomcatConn.close();
				} else {
					Logger.error("Closing connection failed in deleteCase, testOrNot value must other values");
				}

			} catch (Exception e) {

				Logger.error("Closing connection failed in deleteCase");
			}
		}

	}

	/**
	 * This update function updates a specific caseId reporting case through an
	 * UPDATE query. The function first try to initialize connection with the
	 * Postgres database via PostgresConnectionManager and then update only the
	 * parameters below with the prepared statements.
	 * 
	 * @param speciesName  the updated species name that the user entered to the
	 *                     update form
	 * @param reporterName the updated reporter name that the user entered to the
	 *                     update form
	 * @param province     the updated province location that the user entered to
	 *                     the update form
	 * @param coordinates  the updated coordinates that the user entered to the
	 *                     update form
	 */
	public void updateReportedCase(String speciesName, String reporterName, String province, String coordinates,
			int caseId, int testOrNot) throws SQLException {

		PreparedStatement prepStmt = null;
		Connection tomcatConn = null;
		PostgresConnectionManager postConnManger = null;
		Connection postConn = null;

		try {

			// tomcat connection
			tomcatConn = getTomcatConnection();

			// if connection is not null and is running the normal query
			if ((tomcatConn != null) && testOrNot == 0) {
				prepStmt = tomcatConn.prepareStatement(UPDATE_CASE_SQL);
				{
					prepStmt.setString(1, speciesName);
					prepStmt.setString(2, reporterName);
					prepStmt.setString(3, province);
					prepStmt.setString(4, coordinates);
					prepStmt.setInt(5, caseId);

					prepStmt.executeUpdate();

					Logger.info("Update query is completed");

				}
				// if testOrNot is 1, then run the test query
			} else if (testOrNot == 1) {
				// testing section

				postConnManger = new PostgresConnectionManager("localhost", "newinvasivespeciesdb", 5432, "postgres",
						"123456");
				postConn = postConnManger.getConnection();

				prepStmt = postConn.prepareStatement(UPDATE_CASE_SQL_TEST);
				{
					// setting the prepare statement variables for the sql query
					// UPDATE_CASE_SQL_TEST
					prepStmt.setString(1, speciesName);
					prepStmt.setString(2, reporterName);
					prepStmt.setString(3, province);
					prepStmt.setString(4, coordinates);
					prepStmt.setInt(5, caseId);

					prepStmt.executeUpdate();
					Logger.info("Update query is completed in test");

				}

			} else {
				Logger.error("Update query or connection failed in updateReportedCase function");

			}
		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error("Update query failed in updateReportedCase function of JDBCpostgresconn");

		} finally {

			// closing prepare statement and the specific connection
			try {
				prepStmt.close();

				// close the specific connection according to the testOrNot value
				if (postConnManger != null && testOrNot == 1) {
					postConnManger.closeConnection(postConn);
				} else if (tomcatConn != null && testOrNot == 0) {
					tomcatConn.close();
				} else {
					Logger.error("Closing connection failed in deleteCase, testOrNot value must other values");
				}

			} catch (Exception e) {

				Logger.error("Closing connection failed in deleteCase");
			}
		}

	}

	/**
	 * searchReportedCase method performs a search in the reportedcases table with
	 * three parameters speciesName, reporterName, and province. The user can search
	 * by either one of the three parameter and it will return relevant rows of
	 * reported case to the user. The method will first establish a connection and
	 * execute the query with prepared statement to look for relevant data.
	 * 
	 * @param speciesName  the species name that the user entered to the search
	 *                     field
	 * @param reporterName the reporterName that the user entered to the search
	 *                     field
	 * @param province     the province that the user entered to the search field
	 * @param testOrNot    int value to indicate whether it is executing normal or
	 *                     test query
	 * @throws SQLException
	 */
	public Map<String, ArrayList<newReportedCase>> searchReportedCase(String speciesTemp, String reporterTemp,
			String provinceTemp, String username, int testOrNot) throws SQLException {

		ArrayList<newReportedCase> userCaseArray = new ArrayList<newReportedCase>();
		ArrayList<newReportedCase> otherCaseArray = new ArrayList<newReportedCase>();
		Map<String, ArrayList<newReportedCase>> mapArray = new HashMap<String, ArrayList<newReportedCase>>();
		PreparedStatement prepStmt = null;
		Connection tomcatConn = null;
		PostgresConnectionManager postConnManger = null;
		Connection postConn = null;

		
		try {

			tomcatConn = getTomcatConnection();

			// if connection is not null and is running the normal query
			if ((tomcatConn != null) && testOrNot == 0) {

				prepStmt = tomcatConn.prepareStatement(SEARCH_SQL);

				prepStmt.setString(1, "%" + speciesTemp + "%");
				prepStmt.setString(2, "%" + reporterTemp + "%");
				prepStmt.setString(3, "%" + provinceTemp + "%");

				ResultSet rs = prepStmt.executeQuery();

				while (rs.next()) {

					int caseId = Integer.parseInt(rs.getString(1).trim());
					String speciesName = rs.getString(2).trim();
					Date dateReported = rs.getDate(3);
					String reporterName = rs.getString(4).trim();
					String userName = rs.getString(5).trim();
					String province = rs.getString(6).trim();
					String coordinates = rs.getString(7);

					// creating a newReportedCase object to be added to the Arraylist
					newReportedCase newCase = new newReportedCase();
					// validating the user input in the setter method of newReportedCase class in
					// backend
					newCase.setCaseId(caseId);
					newCase.setSpeciesName(speciesName);
					newCase.setDateReported(dateReported);
					newCase.setReporterName(reporterName);
					newCase.setUsername(userName);
					newCase.setProvince(province);
					newCase.setCoordinates(coordinates);

					// when current user equal to the row's username then add to current user's
					// userCaseArray arraylist
					if (username.equals(userName)) {

						// adding each new case to the Arraylist to be pass to the listCase jsp (only
						// current user cases)
						userCaseArray.add(newCase);

					} else {

						// adding non-current user's new cases to the otherCaseArray arraylist
						otherCaseArray.add(newCase);
					}

					// adding each the Arraylist to HashMap to be pass to the listCase jsp
					mapArray.put("user_caseArray", userCaseArray);
					mapArray.put("other_caseArray", otherCaseArray);

				}
				// if testOrNot is 1, then run the test query
			} else if (testOrNot == 1) {
				// testing section

				postConnManger = new PostgresConnectionManager("localhost", "newinvasivespeciesdb", 5432, "postgres",
						"123456");
				postConn = postConnManger.getConnection();

				prepStmt = postConn.prepareStatement(SEARCH_SQL_TEST);

				// setting the prepare statement variables for the sql query
				prepStmt.setString(1, "%" + speciesTemp + "%");
				prepStmt.setString(2, "%" + reporterTemp + "%");
				prepStmt.setString(3, "%" + provinceTemp + "%");

				ResultSet rs = prepStmt.executeQuery();

				while (rs.next()) {
					// creating newReportedCase Object for each row of input

					int caseId = Integer.parseInt(rs.getString(1).trim());
					String speciesName = rs.getString(2).trim();
					Date dateReported = rs.getDate(3);
					String reporterName = rs.getString(4).trim();
					String userName = rs.getString(5).trim();
					String province = rs.getString(6).trim();
					String coordinates = rs.getString(7);

					// creating a newReportedCase object to be added to the Arraylist
					newReportedCase newCase = new newReportedCase();
					// validating the user input in the setter method of newReportedCase class in
					// backend
					newCase.setCaseId(caseId);
					newCase.setSpeciesName(speciesName);
					newCase.setDateReported(dateReported);
					newCase.setReporterName(reporterName);
					newCase.setUsername(userName);
					newCase.setProvince(province);
					newCase.setCoordinates(coordinates);

					// when current user equal to the row's userName then add to current user's
					// userCaseArray arraylist
					if (username.equals(userName)) {

						// adding each new case to the Arraylist to be pass to the listCase jsp (only
						// current user cases)
						userCaseArray.add(newCase);

					} else {

						// adding non-current user's new cases to the otherCaseArray arraylist
						otherCaseArray.add(newCase);
					}

					// adding each the Arraylist to HashMap to be pass to the listCase jsp
					mapArray.put("user_caseArray", userCaseArray);
					mapArray.put("other_caseArray", otherCaseArray);

				}

				Logger.info("Search query is completed in test");

			} else {

				Logger.error("Error: search query or connection failed in searchReportedCase function");

			}
		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error("Search query or connection failed in searchReportedCase function");

		} finally {

			// closing prepare statement and the specific connection
			try {
				prepStmt.close();

				// close the specific connection according to the testOrNot value
				if (postConnManger != null && testOrNot == 1) {
					postConnManger.closeConnection(postConn);
				} else if (tomcatConn != null && testOrNot == 0) {
					tomcatConn.close();
				} else {
					Logger.error("Closing connection failed in searchReportedCase, testOrNot value must other values");
				}

			} catch (Exception e) {

				Logger.error("Closing connection failed in searchReportedCase");
			}
		}
		return mapArray;

	}

	/**
	 * deleteTestRows method deletes all rows in the testreportedcases table in the
	 * newinvasivespeciesdb database such that previous runs of the JUnit test
	 * result will be removed
	 * 
	 * @throws SQLException
	 */
	public void deleteTestRow() throws SQLException {

		PreparedStatement prepStmt = null;
		PostgresConnectionManager postConnManger = null;
		Connection postConn = null;

		try {

			// get a new connection to the newinvasivespeciesdb for the prepare statement
			postConnManger = new PostgresConnectionManager("localhost", "newinvasivespeciesdb", 5432, "postgres",
					"123456");
			postConn = postConnManger.getConnection();

			// if the connection is not null
			if (postConn != null) {

				// running this DELETE_ROWS_SQL_TEST sql query to delete test rows
				prepStmt = postConn.prepareStatement(DELETE_ROWS_SQL_TEST);
				{

					prepStmt.executeUpdate();

				}

			} else {
				Logger.error("Connection failed in deleteTestRow");
			}
		} catch (SQLException e) {

			e.printStackTrace();
			Logger.error("Delete test row query failed or connection failed in deleteTestRow");

		} finally {

			// closing prepare statement and db connection
			try {
				prepStmt.close();
				if (postConnManger != null) {
					postConnManger.closeConnection(postConn);
				}

			} catch (Exception e) {

				Logger.error("Closing connection failed in deleteTestRow");

			}
		}
		Logger.info("Delete test rows query is completed");

	}

	/**
	 * inputValidation method ensures that all user input data for the reporting a
	 * case page meets the input requirement. If any of the setter method failed, it
	 * will throw an IllegalArgumentException exception with message.
	 * 
	 * @param species      species is the name of the species reported by the user
	 *                     (the input of this field can only have alphabets and less
	 *                     than 100 characters)
	 * @param date         reported date of the case by the user (the data type Date
	 *                     protects the user input validation)
	 * @param reporterName the name of the reporter of the case (the input can only
	 *                     have alphabets and less than 50 characters)
	 * @param province     the reported province location by the user (this input
	 *                     can only have less than 100 characters and all alphabets)
	 * @param coordinates  the coordinates location reported by the user of the case
	 *                     (this input can only have less than 25 characters)
	 */
	public void inputValidation(String species, Date date, String reporterName, String province, String coordinates) {
		newReportedCase newCase = new newReportedCase();
		newCase.setSpeciesName(species);
		newCase.setDateReported(date);
		newCase.setReporterName(reporterName);
		newCase.setProvince(province);
		newCase.setCoordinates(coordinates);

	}
	
	public void inputValidationUpdate(String species, String reporterName, String province, String coordinates) {
		newReportedCase newCase = new newReportedCase();
		newCase.setSpeciesName(species);
		newCase.setReporterName(reporterName);
		newCase.setProvince(province);
		newCase.setCoordinates(coordinates);

	}
	public void inputValidationSearch(String species, String reporterName,String province) {
		newReportedCase newCase = new newReportedCase();
		newCase.setSpeciesName(species);
		newCase.setReporterName(reporterName);
		newCase.setProvince(province);
	}
	

}
