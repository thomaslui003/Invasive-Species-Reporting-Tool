package simple;

import org.tinylog.Logger;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.mockito.Mock;
import simple.JDBCpostgresconn;

public class ControllerServletTest {

	private Connection conn;
	private Statement st;
	private ResultSet rs;
	public static JDBCpostgresconn jdbcConnection = new JDBCpostgresconn();
	private ArrayList<newReportedCase> listOfCase = new ArrayList<newReportedCase>();
	private ArrayList<newReportedCase> otherListOfCase = new ArrayList<newReportedCase>();
	private Map<String, ArrayList<newReportedCase>> mapList = new HashMap<String, ArrayList<newReportedCase>>();

	@BeforeEach
	void connectionTest() throws SQLException {

		rs = mock(ResultSet.class);
		when(rs.next()).thenReturn(true).thenReturn(false);
		when(rs.getInt(1)).thenReturn(23);
		when(rs.getString(2)).thenReturn("Red Beetle");
		when(rs.getDate(3)).thenReturn(Date.valueOf("2023-05-15"));
		when(rs.getString(4)).thenReturn("Tom Lui");
		when(rs.getString(5)).thenReturn("tomlui");
		when(rs.getString(6)).thenReturn("Nunavut");
		when(rs.getString(7)).thenReturn("222,111");

		st = mock(Statement.class);
		when(st.executeQuery("SELECT * from reportedcases;")).thenReturn(rs);

		conn = mock(Connection.class);
		when(conn.createStatement()).thenReturn(st);

	}

	@Test
	@Order(1)
	void getIndividualReportedDataTest() throws SQLException {

		try {

			// inserting two new reported case
			jdbcConnection.insertReportedCase("Mountain Pine Beetle", Date.valueOf("2023-05-15"), "Jimmy Doe",
					"jimmy123", "Alberta", "555,222", 1);
			jdbcConnection.insertReportedCase("brown spruce longhorn beetle", Date.valueOf("2023-05-15"), "Thomas Lui",
					"thomas123", "Manitoba", "123,123", 1);

		} catch (Exception e) {

			e.printStackTrace();
		}

		// displaying jimmy123's inserted cases and 1 indicates it will execute the test
		// section of the displayInsertedCases method
		listOfCase = jdbcConnection.displayInsertedCases("jimmy123", 1);
		assertEquals("Mountain Pine Beetle", listOfCase.get(0).getSpeciesName());
		assertEquals(Date.valueOf("2023-05-15"), listOfCase.get(0).getDateReported());
		assertEquals("Jimmy Doe", listOfCase.get(0).getReporterName());
		assertEquals("jimmy123", listOfCase.get(0).getUsername());
		assertEquals("Alberta", listOfCase.get(0).getProvince());
		assertEquals("555,222", listOfCase.get(0).getCoordinates());

		// displayInsertedCases should only query the specific user's cases and in this
		// case only user "thomas123"
		listOfCase = jdbcConnection.displayInsertedCases("thomas123", 1);
		assertEquals("brown spruce longhorn beetle", listOfCase.get(0).getSpeciesName());
		assertEquals(Date.valueOf("2023-05-15"), listOfCase.get(0).getDateReported());
		assertEquals("Thomas Lui", listOfCase.get(0).getReporterName());
		assertEquals("thomas123", listOfCase.get(0).getUsername());
		assertEquals("Manitoba", listOfCase.get(0).getProvince());
		assertEquals("123,123", listOfCase.get(0).getCoordinates());

		// clearing out the test reportedcases table rows before executing other test
		jdbcConnection.deleteTestRow();

		Logger.info(
				"getIndividualReportedDataTest test passed: checked whether the inserted data from insertTest matches with the intended data");

	}

	/**
	 * loginvalidationTest method takes a user_name and password for a test user and
	 * run a query LOGIN_CHECK_SQL_TEST to check if it exists in the testuserlist
	 * table and returns a boolean value to verify
	 * 
	 * @throws SQLException
	 */
	@Test
	@Order(2)
	void loginvalidationTest() throws SQLException {

		try {

			// test user 1 (existing user)
			String user_name1 = "jimmy123";
			String password1 = "135790";
			String hashedPassword1 = "dec58ab7d7f9fb6bd366cea633274ef3632f8eaa823bf811c14bed255d60e339"; // 135790
			// test user 2 (existing user)
			String user_name2 = "thomas123";
			String password2 = "246810";
			String hashedPassword2 = "7c2523c985881fb2c2b4cfbe917eb12c4c4b61e898ad4e7160cfca487ca3c4f3"; // 246810
			// test user 3 (not exist)
			String user_name3 = "josh123";
			String password3 = "888999";
			String hashedPassword3 = "d619e89bdaae0de8760ea721fa1ba8d9a819870b1ba82d720e7ac802270fce92"; // 888999

			JDBCpostgresconn conn = new JDBCpostgresconn();

			// testing the getSecurePwd function
			String resultHashedPassword1 = conn.getSecurePwd(password1);
			String resultHashedPassword2 = conn.getSecurePwd(password2);
			String resultHashedPassword3 = conn.getSecurePwd(password3);
			assertEquals(hashedPassword1, resultHashedPassword1);
			assertEquals(hashedPassword2, resultHashedPassword2);
			assertEquals(hashedPassword3, resultHashedPassword3);

			// using the getSecurePwd method result to get the login validation result
			Boolean result1 = jdbcConnection.validateLogin(user_name1, resultHashedPassword1, 1);
			Boolean result2 = jdbcConnection.validateLogin(user_name2, resultHashedPassword2, 1);
			Boolean result3 = jdbcConnection.validateLogin(user_name3, resultHashedPassword3, 1);

			// verifying whether the validateLogin method result matches the wanted result
			assertEquals(true, result1);
			assertEquals(true, result2);
			assertEquals(false, result3);

			Logger.info("login Validation & getSecurePwd function test passed");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * getOthersReportedDataTest tests if the current logged-in user can have others
	 * inserted cases displayed on their listing page. It checks by querying all
	 * others data except itself and in this case only if the queried data matches
	 * then it is verified correct M
	 * 
	 * @throws SQLException
	 */
	@Test
	@Order(3)
	void getOthersReportedDataTest() throws SQLException {

		// inserting two reported case by different user
		jdbcConnection.insertReportedCase("Mountain Pine Beetle", Date.valueOf("2023-05-15"), "Jimmy Doe", "jimmy123",
				"Alberta", "555,222", 1);
		jdbcConnection.insertReportedCase("brown spruce longhorn beetle", Date.valueOf("2023-05-15"), "Thomas Lui",
				"thomas123", "Manitoba", "123,123", 1);

		// with user name: jimmy123 (logged in)
		// displayOthersInsertedCases should only query all cases except current user's
		// data
		listOfCase = jdbcConnection.displayOthersInsertedCases("jimmy123", 1);
		assertEquals("brown spruce longhorn beetle", listOfCase.get(0).getSpeciesName());
		assertEquals(Date.valueOf("2023-05-15"), listOfCase.get(0).getDateReported());
		assertEquals("Thomas Lui", listOfCase.get(0).getReporterName());
		assertEquals("thomas123", listOfCase.get(0).getUsername());
		assertEquals("Manitoba", listOfCase.get(0).getProvince());
		assertEquals("123,123", listOfCase.get(0).getCoordinates());

		// with user name: thomas123 (logged in)
		// displayOthersInsertedCases should only query all cases except current user's
		// data
		listOfCase = jdbcConnection.displayOthersInsertedCases("thomas123", 1);

		assertEquals("Mountain Pine Beetle", listOfCase.get(0).getSpeciesName());
		assertEquals(Date.valueOf("2023-05-15"), listOfCase.get(0).getDateReported());
		assertEquals("Jimmy Doe", listOfCase.get(0).getReporterName());
		assertEquals("jimmy123", listOfCase.get(0).getUsername());
		assertEquals("Alberta", listOfCase.get(0).getProvince());
		assertEquals("555,222", listOfCase.get(0).getCoordinates());

		// clearing out the test reportedcases table rows before executing other test
		jdbcConnection.deleteTestRow();

		Logger.info(
				"getOthersReportedDataTest function passed: checked if the inserted data from insertTest matches with the intended data");

	}

	/**
	 * This deleteCaseTest function performs a test on the deleteCase function by
	 * first perform a insertion of a new reported case by user jimmy123 then locate
	 * the case id of that inserted case and perform a delete query. It later verify
	 * the deletion by checking the array list size by getting all of jimmy123
	 * user's data.
	 * 
	 * @throws SQLException
	 */
	@Test
	@Order(4)
	void deleteCaseTest() throws SQLException {

		try {

			jdbcConnection.insertReportedCase("Black Beetle", Date.valueOf("2023-05-14"), "Jimmy Johnson", "jimmy123",
					"Nova Scotia", "888,999", 1);
			jdbcConnection.insertReportedCase("brown spruce longhorn beetle", Date.valueOf("2023-05-15"), "Thomas Lui",
					"jimmy123", "Manitoba", "123,123", 1);

			listOfCase = jdbcConnection.displayInsertedCases("jimmy123", 1);

			Logger.info("case id is: " + listOfCase.get(1).getCase_id());

			// listOfCase.get(1) indicates second row of the jimmy123 user's data
			// after inserting the new row, there should be two item in the arraylist
			int caseid = listOfCase.get(1).getCase_id();
			assertEquals(2, listOfCase.size());
			jdbcConnection.deleteCase(caseid, "jimmy123", 1);
			listOfCase = jdbcConnection.displayInsertedCases("jimmy123", 1);
			int firstRowCaseId = listOfCase.get(0).getCase_id();
			// arraylist size of user jimmy123 should only have one row left after deletion
			assertEquals(1, listOfCase.size());
			assertNotEquals(caseid, firstRowCaseId);

			// clearing out the test reportedcases table rows before executing other test
			jdbcConnection.deleteTestRow();

			Logger.info("deleteCaseTest function passed");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * updateCaseTest checks whether updateReportedCase method in JDBCpostgresconn
	 * can successfully update a newly inserted reported case. It first insert a new
	 * reported case, then update the specific case and verify whether the updated
	 * and intended data matches
	 * 
	 * @throws SQLException
	 */
	@Test
	@Order(5)
	void updateCaseTest() throws SQLException {

		try {

			// inserting two new reported case
			jdbcConnection.insertReportedCase("Red Beetle", Date.valueOf("2023-04-15"), "Jimmy Doe", "thomas123",
					"Nova Scotia", "111,111", 1);
			jdbcConnection.insertReportedCase("Mountain Pine Beetle", Date.valueOf("2023-05-15"), "Thomas Lui",
					"thomas123", "Alberta", "123,456", 1);
			listOfCase = jdbcConnection.displayInsertedCases("thomas123", 1);
			// verifying the size of listCase
			assertEquals(2, listOfCase.size());
			int caseid = listOfCase.get(1).getCase_id();
			// using the same case id to update the specific reported case
			jdbcConnection.updateReportedCase("Brown Beetle", "Tommy Lui", "Manitoba", "888,888", caseid, 1);

			// fetching the list again after executing the update function
			listOfCase = jdbcConnection.displayInsertedCases("thomas123", 1);
			// check if updated and intended are equal
			assertEquals("Brown Beetle", listOfCase.get(1).getSpeciesName());
			assertEquals("Tommy Lui", listOfCase.get(1).getReporterName());
			assertEquals("Manitoba", listOfCase.get(1).getProvince());
			assertEquals("888,888", listOfCase.get(1).getCoordinates());

			// clearing out the test reportedcases table rows before executing other test
			jdbcConnection.deleteTestRow();

			Logger.info("updateCaseTest function passed");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * searchTest verify whether the search function performs correctly with a user
	 * inputting different parameters in the three fields (species name, reporter
	 * name, province) It first insert 4 new reported cases, and search for the two
	 * case where it meets the requirement of (Beetle, Thomas, Alberta, user:
	 * thomas123). It will then verify the result by comparing if the fetched result
	 * and wanted result matches. The test also checks when a user only input 1,2,
	 * or all 3 fields for the search and verify that it will produce the wanted
	 * result when search in any combination
	 * 
	 * @throws SQLException
	 */
	@Test
	@Order(6)
	void searchTest() throws SQLException {

		try {

			// inserting four new reported case
			jdbcConnection.insertReportedCase("Emerald Ash Borer", Date.valueOf("2023-05-11"), "Tom Lui", "thomas123",
					"Nova Scotia", "123,456", 1);
			jdbcConnection.insertReportedCase("Mountain Pine Beetle", Date.valueOf("2023-05-12"), "Thomas Lui",
					"thomas123", "Alberta", "999,888", 1);
			jdbcConnection.insertReportedCase("Green Beetle", Date.valueOf("2023-05-13"), "Thomas Lui", "thomas123",
					"Alberta", "345,678", 1);
			jdbcConnection.insertReportedCase("Brown Spruce Longhorn Beetle", Date.valueOf("2023-05-14"), "Thomas Lui",
					"thomas123", "Ontario", "666,666", 1);

			// searching results with parameters like Beetle, Thomas, and Alberta
			mapList = jdbcConnection.searchReportedCase("Beetle", "Thomas", "Alberta", "thomas123", 1);
			// arraylist of reported cases for the current user
			listOfCase = mapList.get("user_caseArray");
			// arraylist of reported cases for the non-current user
			otherListOfCase = mapList.get("other_caseArray");

			// verifying the size of listCase (with the search parameter, there should be
			// two results)
			assertEquals(2, listOfCase.size());
			String coordinate1 = listOfCase.get(0).getCoordinates();
			String secondSpeciesName = listOfCase.get(1).getSpeciesName();
			// verify the fetched coordinate and species name equals
			assertEquals("999,888", coordinate1);
			assertEquals("Green Beetle", secondSpeciesName);

			// another search with only longhorn as parameter in species name
			mapList = jdbcConnection.searchReportedCase("longhorn", "", "", "thomas123", 1);
			listOfCase = mapList.get("user_caseArray");
			otherListOfCase = mapList.get("other_caseArray");

			assertEquals("Brown Spruce Longhorn Beetle", listOfCase.get(0).getSpeciesName());
			assertEquals(Date.valueOf("2023-05-14"), listOfCase.get(0).getDateReported());
			assertEquals("Ontario", listOfCase.get(0).getProvince());
			assertEquals("666,666", listOfCase.get(0).getCoordinates());

			// clearing out the test reportedcases table rows before executing other test
			jdbcConnection.deleteTestRow();

			Logger.info("Search Test function passed");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * injectionTest checks whether SQL injection is possible under the use of prepared statement in Java.
	 * 
	 * @throws SQLException
	 */
	@Test
	@Order(7)
	void injectionTest() throws SQLException {

		try {

			// inserting four new reported cases
			jdbcConnection.insertReportedCase("Emerald Ash Borer", Date.valueOf("2023-05-11"), "Tom Lui", "thomas123",
					"Nova Scotia", "123,456", 1);
			jdbcConnection.insertReportedCase("Mountain Pine Beetle", Date.valueOf("2023-05-12"), "Thomas Lui",
					"thomas123", "Alberta", "999,888", 1);
			jdbcConnection.insertReportedCase("Green Beetle", Date.valueOf("2023-05-13"), "Thomas Lui", "thomas123",
					"Alberta", "345,678", 1);
			jdbcConnection.insertReportedCase("Brown Spruce Longhorn Beetle", Date.valueOf("2023-05-14"), "Thomas Lui",
					"thomas123", "Ontario", "666,666", 1);

			// searching results with parameters like Beetle, Thomas, and Alberta (with the Alberta parameter including 1=1)
			mapList = jdbcConnection.searchReportedCase("Beetle", "Thomas", "Alberta or 1=1", "thomas123", 1);
			// arraylist of reported cases for the current user
			listOfCase = mapList.get("user_caseArray");
			// arraylist of reported cases for the non-current user
			otherListOfCase = mapList.get("other_caseArray");

			String coordinate1 = listOfCase.get(0).getCoordinates();
			String secondSpeciesName = listOfCase.get(1).getSpeciesName();
			// verify the fetched coordinate and species name equals
			assertEquals("999,888", coordinate1);
			assertEquals("Green Beetle", secondSpeciesName);

			// verifying the size of listCase (with the search parameter, there should be
			// two results) and if sql injection was possbile it will return all rows
			assertEquals(2, listOfCase.size());	

			//clearing out the test reportedcases table rows before executing other test
			jdbcConnection.deleteTestRow();

			Logger.info("Sql Injection prevention Test function passed");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * An example of testing the Servlet
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
//	@Test
//	@Order(8)
//	void exampleDoGetTest() throws ServletException, IOException {
//
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		final String OPERATION_VALUE = "query";
//		final String FILTER_VALUE = "query";
//		when(request.getParameter(ControllerServlet.OPERATION_PARAM)).thenReturn(OPERATION_VALUE);
//		when(request.getParameter(ControllerServlet.FILTER_PARAM)).thenReturn(FILTER_VALUE);
//
//		StringWriter stringWriter = new StringWriter();
//		PrintWriter writer = new PrintWriter(stringWriter);
//		when(response.getWriter()).thenReturn(writer);
//
//		new ControllerServlet().doGet(request, response);
//
//		verify(request, atLeast(1)).getParameter(ControllerServlet.OPERATION_PARAM);
//		writer.flush(); // it may not have been flushed yet...
//		assertTrue(stringWriter.toString().contains(FILTER_VALUE));
//		Logger.info("ControllerServlet test passed.");
//
//	}

}
