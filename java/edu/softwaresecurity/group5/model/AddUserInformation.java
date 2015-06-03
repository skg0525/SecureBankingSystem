package edu.softwaresecurity.group5.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class AddUserInformation {
	 @NotEmpty(message="Please enter your firstname.")
    private String firstName;
    @NotEmpty(message="Please enter your lastname.")
    private String lastName;
    private String sex;
    @NotEmpty(message="")
    private String contactNumber;
    @NotEmpty(message="Please enter your address.")
    private String address;
    @Email(message="Your email is invalid.")
    @NotEmpty(message="Please enter your email.")
    private String emailAddress_addUser;
    @NotEmpty(message="")
    private String userName;
    @NotEmpty(message = "")
    private String password;
    @NotEmpty(message = "Please enter your password again to confirm.")
    private String confirmPassword;
    @Size(min = 9, max = 9, message = "")
    private String socialSecurityNumber;
    private String selection;
	private int userExpired;
	private int userLocked;
	private int userDetailsExpired;
    private int enabled;
    
    
    public int getUserExpired() {
		return userExpired;
	}
	public void setUserExpired(int userExpired) {
		this.userExpired = userExpired;
	}
	public int getUserLocked() {
		return userLocked;
	}
	public void setUserLocked(int userLocked) {
		this.userLocked = userLocked;
	}
	public int getUserDetailsExpired() {
		return userDetailsExpired;
	}
	public void setUserDetailsExpired(int userDetailsExpired) {
		this.userDetailsExpired = userDetailsExpired;
	}
    public int getEnabled() {
        return enabled;
    }
    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getSelection() {
        return selection;
    }
    public void setSelection(String selection) {
        this.selection = selection;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public String getEmailAddress_addUser() {
        return emailAddress_addUser;
    }
    public void setEmailAddress_addUser(String emailAddress_addUser) {
        this.emailAddress_addUser = emailAddress_addUser;
    }
    
    public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}
	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
