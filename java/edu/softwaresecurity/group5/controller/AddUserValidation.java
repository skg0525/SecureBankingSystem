package edu.softwaresecurity.group5.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import edu.softwaresecurity.group5.model.AddUserInformation;

@Component("addUserValidator")
public class AddUserValidation {
  public boolean supports(Class<?> c) {
    return AddUserInformation.class.isAssignableFrom(c);
  }

  public static void validateForm(Object info, Errors errors) {
      
	  int count = 0;
	  int number = 0;
	  int ssnNum = 0;
	  int phNum = 0;
	  
	  AddUserInformation addinfo = (AddUserInformation) info;
    
	  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "socialSecurityNumber",
		"NotEmpty.AddUserInformation.socialSecurityNumber",
		"SSN must not be Empty.");
	  
	  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactNumber",
				"NotEmpty.AddUserInformation.contactNumber",
				"Contact must not be Empty.");
	  
	  String userName = addinfo.getUserName();
	  
	  String firstName = addinfo.getFirstName();
	  Pattern p1 = Pattern.compile("[!@#${},%^&*+_.-]");
	  Matcher match_fn = p1.matcher(firstName.subSequence(0, firstName.length()));
	  if ((firstName.length()) > 10 || match_fn.find() == true) {
      errors.rejectValue("firstName",
          "lengthOfFirst.AddUserInformation.firstName",
          "Invalid first name!");
      }
	  
	  String lastName = addinfo.getLastName();
	  Matcher match_ln = p1.matcher(lastName.subSequence(0, lastName.length()));
	  if ((lastName.length()) > 10 || match_ln.find() == true) {
      errors.rejectValue("lastName",
          "lengthOfLast.AddUserInformation.lastName",
          "Invalid last name!");
      }
	  
	  String add = addinfo.getAddress();
	  Matcher match_add = p1.matcher(add.subSequence(0, add.length()));
	  if ((add.length()) > 50 || match_add.find()==true) {
      errors.rejectValue("address",
          "lengthOfAddress.AddUserInformation.address",
          "Invalid address!");
      }
	  
	  String ssn = addinfo.getSocialSecurityNumber();
	  
	  for (char c: ssn.toCharArray()) {
		  if(Character.isDigit(c)) {
			  ssnNum++;
		  }
	  }
	  if(ssnNum != 9 || ssn.length() != 9) {
		  errors.rejectValue("socialSecurityNumber", 
				  "lengthOfSocialSecurityNumber.AddUserInformation.socialSecurityNumber",
				  "Invalid SSN!");
	  }
	  
	  
	  String pNumber = addinfo.getContactNumber();
	  for (char c: pNumber.toCharArray()) {
		  if(Character.isDigit(c)) {
			  phNum++;
		  }
	  }
	  if(phNum != 10 || pNumber.length() != 10) {
		  errors.rejectValue("contactNumber", "lengthOfPhoneNumber.AddUserInformation.contactNumber",
				  "Invalid phone number!");
	  }
	  
	  
	  String pass = addinfo.getPassword();
	  for (char c: pass.toCharArray()) {
		  if (Character.isDigit(c)) {
			  number ++;
		  }
	  }

	  for (char c: pass.toCharArray()) {
		  if (Character.isUpperCase(c)) {
			  count++;
		  }
	  }
	  
	  Pattern p = Pattern.compile("[!@#${},%^&*+_.-]");
	  Matcher match1 = p.matcher(userName.subSequence(0, userName.length()));
	  Matcher match = p.matcher(pass.subSequence(0, pass.length()));
	  
	  if (pass.length()<6 || pass.length()>15 ||
				 number<=0 || count<=0 || match.find()==false || userName.length() > 10 || userName.length()==0 || match1.find()==true) {
		  errors.rejectValue("userName",
			  "matchingPassword.AddUserInformation.userName",
			  "User Name or Password is invalid!");
	  }
	  
	  if (!(addinfo.getPassword()).equals(addinfo.getConfirmPassword())) {
      errors.rejectValue("password",
          "matchingPassword.AddUserInformation.password",
          "Password and Confirm Password Not match.");
      }
   } 
}