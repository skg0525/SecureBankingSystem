package edu.softwaresecurity.group5.dto;

public class TicketDetailDTO {
	private String username;
	private String firstname;
	private String lastname;
	private String sex;
	private String selection;
	private String phonenumber;
	private String email;
	private String address;
	private int id;
	private int pendingid;
	public int getPendingid() {
		return pendingid;
	}
	public void setPendingid(int pendingid) {
		this.pendingid = pendingid;
	}
	private boolean requestcompleted;
	private boolean requestapproved;
	private boolean requestrejected;
	private boolean deleteaccount;
	private int accountNumber;
	private String accountNumberString;
	public String getAccountNumberString() {
		return accountNumberString;
	}
	public void setAccountNumberString(String accountNumberString) {
		this.accountNumberString = accountNumberString;
	}
	private float transactionamountInfloat;
	public float getTransactionamountInfloat() {
		return transactionamountInfloat;
	}
	public void setTransactionamountInfloat(float transactionamountInfloat) {
		this.transactionamountInfloat = transactionamountInfloat;
	}
	private float accountBalance;
	private String requesttype;
	private String transactionAmount;
	private String toAccountNumber;
	private boolean billpay;
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getToAccountNumber() {
		return toAccountNumber;
	}
	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}
	public boolean isBillpay() {
		return billpay;
	}
	public void setBillpay(boolean billpay) {
		this.billpay = billpay;
	}
	public boolean isDeleteaccount() {
		return deleteaccount;
	}
	public void setDeleteaccount(boolean deleteaccount) {
		this.deleteaccount = deleteaccount;
	}
	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	public float getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(float accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	public boolean isRequestrejected() {
		return requestrejected;
	}
	public void setRequestrejected(boolean requestrejected) {
		this.requestrejected = requestrejected;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isRequestcompleted() {
		return requestcompleted;
	}
	public void setRequestcompleted(boolean requestcompleted) {
		this.requestcompleted = requestcompleted;
	}
	public boolean isRequestapproved() {
		return requestapproved;
	}
	public void setRequestapproved(boolean requestapproved) {
		this.requestapproved = requestapproved;
	}
	public String getRequesttype() {
		return requesttype;
	}
	public void setRequesttype(String requesttype) {
		this.requesttype = requesttype;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/**
	 * @return the selection
	 */
	public String getSelection() {
		return selection;
	}
	/**
	 * @param selection the selection to set
	 */
	public void setSelection(String selection) {
		this.selection = selection;
	}
	/**
	 * @return the phonenumber
	 */
	public String getPhonenumber() {
		return phonenumber;
	}
	/**
	 * @param phonenumber the phonenumber to set
	 */
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}

