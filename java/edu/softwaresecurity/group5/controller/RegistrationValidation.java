package edu.softwaresecurity.group5.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import edu.softwaresecurity.group5.model.CustomerInformation;

@Component("registrationValidator")
public class RegistrationValidation {
  public boolean supports(Class<?> c) {
    return CustomerInformation.class.isAssignableFrom(c);
  }

  public static void validateForm(Object info, Errors errors) {
	  
	  int count = 0;
	  int number = 0;
	  int ssnNum = 0;
	  int phNum = 0;
	  
	  CustomerInformation cinfo = (CustomerInformation) info;
//	  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username",
//        "NotEmpty.CustomerInformation.username",
//        "");
    
	  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "socialSecurityNumber",
		"NotEmpty.CustomerInformation.socialSecurityNumber",
		"SSN must not be Empty.");
	  
	  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phonenumber",
				"NotEmpty.CustomerInformation.phonenumber",
				"Contact must not be Empty.");
	  
	  String userName = cinfo.getUsername();
//	  if ((userName.length()) > 10 || userName.length()==0) {
//      errors.rejectValue("username",
//          "lengthOfUser.CustomerInformation.username",
//          "Invalid User Name! Please Enter Again!");
//      }
	  
	  String firstName = cinfo.getFirstname();
	  Pattern p1 = Pattern.compile("[!@#${},%^&*+_.-]");
	  Matcher match_fn = p1.matcher(firstName.subSequence(0, firstName.length()));
	  if ((firstName.length()) > 10 || match_fn.find() == true) {
      errors.rejectValue("firstname",
          "lengthOfFirst.CustomerInformation.firstname",
          "First name is invalid");
      }
	  
	  String lastName = cinfo.getLastname();
	  Matcher match_ln = p1.matcher(lastName.subSequence(0, lastName.length()));
	  if ((lastName.length()) > 10 || match_ln.find() == true) {
      errors.rejectValue("lastname",
          "lengthOfLast.CustomerInformation.lastname",
          "Last name is invalid");
      }
	  
	  String add = cinfo.getAddress();
	  Matcher match_add = p1.matcher(add.subSequence(0, add.length()));
	  if ((add.length()) > 50 || match_add.find() == true) {
      errors.rejectValue("address",
          "lengthOfAddress.CustomerInformation.address",
          "Address is invalid");
      }
	  
	  String ssn = cinfo.getSocialSecurityNumber();
	  System.out.println("SSN CHECK: "+ssn);
	  
	  for (char c: ssn.toCharArray()) {
		  if(Character.isDigit(c)) {
			  ssnNum++;
		  }
	  }
	  System.out.println(ssnNum);
	  
	  if(ssnNum != 9 || ssn.length() != 9) {
		  errors.rejectValue("socialSecurityNumber", 
				  "lengthOfSocialSecurityNumber.CustomerInformation.socialSecurityNumber",
				  "SSN is invalid");
	  }
	  
	  String pNumber = cinfo.getPhonenumber();
	  for (char c: pNumber.toCharArray()) {
		  if(Character.isDigit(c)) {
			  phNum++;
		  }
	  }
	  if(phNum != 10 || pNumber.length() != 10) {
		  errors.rejectValue("phonenumber", "lengthOfPhoneNumber.CustomerInformation.phonenumber",
				  "Phone number is invalid");
	  }
	  
	  
	  String pass = cinfo.getPassword();
//	  if(pass.length()<6) { 
//		  errors.rejectValue("password",
//	          "matchingPassword.CustomerInformation.password",
//	          "Password too small");
//		  }
//	  
//	  if(pass.length()>15) { 
//		  errors.rejectValue("password",
//	          "matchingPassword.CustomerInformation.password",
//	          "Password too big");
//	  }
	  for (char c: pass.toCharArray()) {
		  if (Character.isDigit(c)) {
			  number ++;
		  }
	  }
//	  if(number<=0) {
//		  errors.rejectValue("password",
//	          "matchingPassword.CustomerInformation.password",
//	          "Password has no digit"); 
//	  }
	  for (char c: pass.toCharArray()) {
		  if (Character.isUpperCase(c)) {
			  count++;
		  }
	  }
//	  if(count <= 0) {
//		  errors.rejectValue("password",
//		      "matchingPassword.CustomerInformation.password",
//		      "Password has no capital letter");
//	  }
	  
	  Pattern p = Pattern.compile("[!@#${},%^&*+_.-]");
	  Matcher match = p.matcher(pass.subSequence(0, pass.length()));
	  Matcher match_1 = p.matcher(userName.subSequence(0, userName.length()));
	  if (pass.length()<6 || pass.length()>15 ||
				 number<=0 || count<=0 || match.find()==false || userName.length() > 10 || userName.length()==0 || match_1.find() == true) {
		  errors.rejectValue("username",
			  "matchingPassword.CustomerInformation.username",
			  "User Name or Password is invalid!");
	  }
	  
	  if (!(cinfo.getPassword()).equals(cinfo.getConfirmPassword())) {
      errors.rejectValue("password",
          "matchingPassword.CustomerInformation.password",
          "Password and Confirm Password Not match.");
      }
   } 
}