package edu.softwaresecurity.group5.dto;

public class BillPayDTO {
	private int id;
	private String username;
	private float amount;
	private int accountnumberfrom;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
	/**
	 * @return the accountnumberfrom
	 */
	public int getAccountnumberfrom() {
		return accountnumberfrom;
	}
	/**
	 * @param accountnumberfrom the accountnumberfrom to set
	 */
	public void setAccountnumberfrom(int accountnumberfrom) {
		this.accountnumberfrom = accountnumberfrom;
	}
}
