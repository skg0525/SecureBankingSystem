package edu.softwaresecurity.group5.dto;

import java.sql.Timestamp;

public class UserTransactionsDTO {
	private int transactionID;
	private String usernameFrom;
	private String usernameTo;
	private String usernameFromAccountNumber;
	private String usernameToAccountNumber;
	private String transactionType;
	private boolean userDelete;
	private Timestamp currentTimeStamp;
	
	/**
	 * @return the usernameFrom
	 */
	public String getUsernameFrom() {
		return usernameFrom;
	}
	/**
	 * @param usernameFrom the usernameFrom to set
	 */
	public void setUsernameFrom(String usernameFrom) {
		this.usernameFrom = usernameFrom;
	}
	/**
	 * @return the usernameTo
	 */
	public String getUsernameTo() {
		return usernameTo;
	}
	/**
	 * @param usernameTo the usernameTo to set
	 */
	public void setUsernameTo(String usernameTo) {
		this.usernameTo = usernameTo;
	}
	/**
	 * @return the usernameFromAccountNumber
	 */
	public String getUsernameFromAccountNumber() {
		return usernameFromAccountNumber;
	}
	/**
	 * @param usernameFromAccountNumber the usernameFromAccountNumber to set
	 */
	public void setUsernameFromAccountNumber(String usernameFromAccountNumber) {
		this.usernameFromAccountNumber = usernameFromAccountNumber;
	}
	/**
	 * @return the usernameToAccountNumber
	 */
	public String getUsernameToAccountNumber() {
		return usernameToAccountNumber;
	}
	/**
	 * @param usernameToAccountNumber the usernameToAccountNumber to set
	 */
	public void setUsernameToAccountNumber(String usernameToAccountNumber) {
		this.usernameToAccountNumber = usernameToAccountNumber;
	}
	/**
	 * @return the currentTimeStamp
	 */
	public Timestamp getCurrentTimeStamp() {
		return currentTimeStamp;
	}
	/**
	 * @param currentTimeStamp the currentTimeStamp to set
	 */
	public void setCurrentTimeStamp(Timestamp currentTimeStamp) {
		this.currentTimeStamp = currentTimeStamp;
	}
	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	/**
	 * @return the transactionID
	 */
	public int getTransactionID() {
		return transactionID;
	}
	/**
	 * @param transactionID the transactionID to set
	 */
	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}
	/**
	 * @return the userDelete
	 */
	public boolean isUserDelete() {
		return userDelete;
	}
	/**
	 * @param userDelete the userDelete to set
	 */
	public void setUserDelete(boolean userDelete) {
		this.userDelete = userDelete;
	}
}
