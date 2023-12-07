package simple;

import java.sql.Date;

/**
 * 
 * 
 * Last update: June 1 2023
 * @author Wen Luo (Thomas) Lui
 *
 */
public class newReportedCase {
	private int caseId;
	private String speciesName;
	private Date dateReported;
	private String reporterName;
	private String user_name;
	private String province;
	private String coordinates;

	/**
	 * Constructs a new reported case object. The newReportedCase object requires
	 * setter functions to set values to its attributes
	 *
	 * @param caseId       the unique identifier for the reported case
	 * @param speciesName  the name of the new species associated with the reported
	 *                     case
	 * @param dateReported the date when the case was reported
	 * @param reporterName the name of the reporter who reported the case
	 * @param user_name    the name of the user associated with the reported case
	 * @param province     the province where the case was reported
	 * @param coordinates  the coordinates associated with the reported case
	 */
	public newReportedCase() {
		this.caseId = -1;
		this.speciesName = null;
		this.dateReported = null;
		this.reporterName = null;
		this.user_name = null;
		this.province = null;
		this.coordinates = null;

	}

	/**
	 * Gets the unique identifier for the reported case.
	 *
	 * @return the case ID
	 */
	public int getCase_id() {
		return caseId;
	}

	/**
	 * Sets the unique identifier for the reported case. No validation needed since
	 * this value is set by the db when inserting a new case
	 *
	 * @param caseId 		the case ID to set
	 */
	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}

	/**
	 * Gets the name of the species associated with the reported case.
	 *
	 * @return the species name
	 */
	public String getSpeciesName() {
		return speciesName;
	}

	/**
	 * Setter method for speciesName and it checks whether it only consists of
	 * alphabets and input is less than 50 characters. After validating, sets the
	 * name of the species associated with the reported case. It then throws an
	 * illegalArgumentexception if exceeding the condition
	 *
	 * @param speciesName 		the species name to set
	 */
	public void setSpeciesName(String speciesName) throws IllegalArgumentException{

		// used to validate if input only consists of alphabets and less than 50
		// characters
		if (speciesName==null||((speciesName.matches("[A-Za-z\\s]*$")) && (speciesName.length() <= 100))) {
			this.speciesName = speciesName;
			
		} else {

			throw new IllegalArgumentException(
					"species name have exceed 100 characters or contain numbers and special characters ");
		}

	}

	/**
	 * Gets the date when the case was reported.
	 *
	 * @return the date reported
	 */
	public Date getDateReported() {
		return dateReported;
	}

	/**
	 * Sets the date when the case was reported.
	 *
	 * @param dateReported 		the date reported to set
	 */
	public void setDateReported(Date dateReported) {
		this.dateReported = dateReported;
	}

	/**
	 * Gets the name of the reporter who reported the case.
	 *
	 * @return the reporter name
	 */
	public String getReporterName() {
		return reporterName;
	}

	/**
	 * Setter method for reporterName and it checks whether it only consists of
	 * alphabets and input is less than 50 characters. After validating, sets the
	 * name of the reporter who reported the case. It then throws an
	 * illegalArgumentexception if exceeding the condition
	 *
	 * @param reporterName 		the reporter name to set
	 */
	public void setReporterName(String reporterName) throws IllegalArgumentException{

		// used to validate if input only consists of alphabets and less than 50
		// characters
		
		if (reporterName==null||(reporterName.matches("[A-Za-z\\s]*$")) && (reporterName.length() <= 50)) {
			
			this.reporterName = reporterName;
		} else {
			throw new IllegalArgumentException(
					"reporter name have exceed 50 characters or contain numbers and special characters ");
		}
	}

	/**
	 * Gets the name of the user associated with the reported case.
	 *
	 * @return the user name
	 */
	public String getUsername() {
		return user_name;
	}

	/**
	 * Sets the name of the user associated with the reported case.
	 *
	 * @param user_name the user name to set
	 */
	public void setUsername(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * Gets the province where the case was reported.
	 *
	 * @return the province name
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * Setter method for province and it checks whether it only consisgetSpeciesNamets of
	 * alphabets and input is less than 100 characters. After validating, sets the
	 * province where the case was reported. It then throws an
	 * illegalArgumentexception if exceeding the condition
	 *
	 * @param province the province name to set
	 */
	public void setProvince(String province) throws IllegalArgumentException{
		this.province = province;

		// used to validate if input only consists of alphabets and less than 100
		// characters
		if (province==null||(province.matches("[A-Za-z\\s]*$")) && (province.length() <= 100)) {
			this.province = province;
		} else {
			// throw an exception for exceeding input and special characters or numbers
			throw new IllegalArgumentException(
					"province have exceed 100 characters or contain numbers and special characters ");
		}

	}

	/**
	 * Gets the coordinates associated with the reported case.
	 *
	 * @return the coordinates
	 */
	public String getCoordinates() {
		return coordinates;
	}

	/**
	 * Sets the coordinates associated with the reported case.
	 *
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(String coordinates) throws IllegalArgumentException{

		this.coordinates = coordinates;

		if (coordinates==null||(coordinates.length() <= 25)) {
			this.coordinates = coordinates;
		} else {
			// throw an exception for exceeding input and special characters or numbers
			throw new IllegalArgumentException("Coordinates have exceed 25 characters for user input");
		}

	}

}