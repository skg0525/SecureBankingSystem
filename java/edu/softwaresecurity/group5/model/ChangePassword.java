package edu.softwaresecurity.group5.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class ChangePassword {

	@NotEmpty(message = "Please enter your password.")
	@Size(min = 6, max = 15, message = "Your password must between 6 and 15 characters")
	private String password;
	@NotEmpty(message = "Please enter your password again to confirm.")
	@Size(min = 6, max = 15, message = "Your password must between 6 and 15 characters")
	private String confirmPassword;
	private String username;
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
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

}
