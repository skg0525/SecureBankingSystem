package edu.softwaresecurity.group5.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class CustomerInformation {
	@NotEmpty(message="")
	private String username;
	@NotEmpty(message = "")
	private String password;
	@NotEmpty(message = "Please enter your password again to confirm.")
	private String confirmPassword;
	@NotEmpty(message="Please enter your firstname.")
	private String firstname;
	@NotEmpty(message="Please enter your lastname.")
	private String lastname;
	private String sex;
	private String selection;
	@NotEmpty(message="")
	private String phonenumber;
	@Email(message="Your email is invalid.")
	@NotEmpty(message="Please enter your email.")
	private String email;
	@Size(min = 9, max = 9, message = "")
	private String socialSecurityNumber;
	@NotEmpty(message="Please enter your address.")
	private String address;
	private int enabled;
	private int userExpired;
	private int userLocked;
	private int userDetailsExpired;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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
	 * @return the sSN
	 */
	
	
	/**
	 * @return the phonenumber
	 */
	public String getPhonenumber() {
		return phonenumber;
	}
	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}
	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}
	/**
	 * @param phonenumber the phonenumber to set
	 */
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
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
	/**
	 * @return the enabled
	 */
	public int getEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(int enabled) {
		this.enabled = enabled;
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
	 * @return the userExpired
	 */
	public int getUserExpired() {
		return userExpired;
	}
	/**
	 * @param userExpired the userExpired to set
	 */
	public void setUserExpired(int userExpired) {
		this.userExpired = userExpired;
	}
	/**
	 * @return the userLocked
	 */
	public int getUserLocked() {
		return userLocked;
	}
	/**
	 * @param userLocked the userLocked to set
	 */
	public void setUserLocked(int userLocked) {
		this.userLocked = userLocked;
	}
	/**
	 * @return the userDetailsExpired
	 */
	public int getUserDetailsExpired() {
		return userDetailsExpired;
	}
	/**
	 * @param userDetailsExpired the userDetailsExpired to set
	 */
	public void setUserDetailsExpired(int userDetailsExpired) {
		this.userDetailsExpired = userDetailsExpired;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
}