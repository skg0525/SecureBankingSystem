package edu.softwaresecurity.group5.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.softwaresecurity.group5.dto.CustomerInformationDTO;
import edu.softwaresecurity.group5.dto.EmployeeInformationDTO;
import edu.softwaresecurity.group5.dto.TicketDetailDTO;
import edu.softwaresecurity.group5.dto.TicketInformationDTO;
import edu.softwaresecurity.group5.model.AddUserInformation;
import edu.softwaresecurity.group5.model.ChangePassword;
import edu.softwaresecurity.group5.service.CustomerService;

@Controller
public class MainController {
	@Autowired
	CustomerService custService;

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public ModelAndView indexPage(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {

			Collection<? extends GrantedAuthority> authorities = auth
					.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {
				if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
					return new ModelAndView("forward:/accountSummary");
				} else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
					return new ModelAndView("forward:/admin");
				} else if (grantedAuthority.getAuthority().equals(
						"ROLE_EMPLOYEE")) {
					return new ModelAndView("forward:/employee");

				}
			}

			return new ModelAndView("forward:/welcome");
		}

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error",
					getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("index");

		return model;
	}

	// customize the error message
	private String getErrorMessage(HttpServletRequest request, String key) {

		Exception exception = (Exception) request.getSession()
				.getAttribute(key);

		String error = "";
		if (exception instanceof BadCredentialsException) {
			error = "Invalid username and password!";
		} else if (exception instanceof LockedException) {
			error = exception.getMessage();
		} else if (exception instanceof SessionAuthenticationException) {
			error = exception.getMessage();
		} else {
			error = "Invalid username and password!";
		}

		return error;
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public ModelAndView welcomePage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "You should not be landing here");
		model.addObject("message", "This is default page!");
		model.setViewName("customer-home");
		return model;
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView adminPage() {

		ModelAndView model = new ModelAndView();
		model.setViewName("welcomeAdmin");

		return model;
	}

	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public ModelAndView employeePage() {

		ModelAndView model = new ModelAndView();
		model.setViewName("welcomeInternal");

		return model;
	}

	// for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accessDenied() {

		ModelAndView model = new ModelAndView();

		// check if user is login
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			System.out.println(userDetail);

			model.addObject("username", userDetail.getUsername());
		}
		model.setViewName("permission-denied");
		return model;
	}

	// // Displaying the removeUser.jsp page
	// @RequestMapping(value = "/removeUser", method = RequestMethod.GET)
	// public ModelAndView returnRemoveUserPage() {
	// ModelAndView modelAndView = new ModelAndView();
	// modelAndView.setViewName("removeUser");
	// return modelAndView;
	// }

	// // Getting the userdetails
	// @RequestMapping(value = "/removeUser", method = RequestMethod.POST)
	// public ModelAndView getUserDetails(
	// @RequestParam("removeUser") String usernameSearch) {
	// ModelAndView modelAndView = new ModelAndView();
	//
	// Document userInput = Jsoup.parse(usernameSearch);
	// String userName = userInput.text();
	//
	// if(userName.length()==0 || userName.length()>15) {
	// modelAndView.addObject("errorMsg", "Please enter the correct username!");
	// modelAndView.setViewName("removeUser");
	// }
	// else{
	// List<CustomerInformationDTO> customerDetails = new
	// ArrayList<CustomerInformationDTO>();
	// customerDetails = custService.fetchUserDetails(userName);
	// if(customerDetails.size()==0) {
	// modelAndView.addObject("errorMsg", "Please enter the correct username!");
	// modelAndView.setViewName("removeUser");
	// }
	// else {
	// modelAndView.addObject("customerDetails", customerDetails);
	// modelAndView.setViewName("removeUser");
	// }
	// }
	// return modelAndView;
	// }
	// Remove internal user from db, mark user detailed expired to 0.
	@RequestMapping(value = "/removeUserDB", method = RequestMethod.POST)
	public ModelAndView getremoveUserDB(
			@RequestParam("account") String usernameSearch) {
		ModelAndView modelAndView = new ModelAndView();

		
		String userName = Jsoup.clean(usernameSearch, Whitelist.basic());
		

		boolean status = custService.deleteAccountBYInternal(userName);
		if (status) {
			modelAndView.addObject("status", "User Deleted Succefully!");
			modelAndView.setViewName("userList");
		} else {
			modelAndView
					.addObject("status",
							"Please couldnot be deleted please contact Adminstrator personally!");
			modelAndView.setViewName("userList");
		}

		return modelAndView;
	}

	// Remove external user from db, mark user detailed expired to 0.
	@RequestMapping(value = "/removeUserExternal", method = RequestMethod.POST)
	public ModelAndView getremoveExternalUserDB(
			@RequestParam("account") String usernameSearch) {
		ModelAndView modelAndView = new ModelAndView();

		
		String userName = Jsoup.clean(usernameSearch, Whitelist.basic());
		
		boolean status = custService.deleteAccountBYExternal(userName);
		if (status) {
			modelAndView.addObject("status", "User Deleted Succefully!");
			modelAndView.setViewName("");
		} else {
			modelAndView
					.addObject("status",
							"Please couldnot be deleted please contact Adminstrator personally!");
			modelAndView.setViewName("ticketAuthorizedSuccess");
		}

		return modelAndView;
	}

	// Remove user from db, mark user detailed expired to 0.
	@RequestMapping(value = "/removeUserDBSingle", method = RequestMethod.POST)
	public ModelAndView getremoveUserDBSingle(
			@RequestParam("account") String usernameSearch) {
		ModelAndView modelAndView = new ModelAndView();

		
		String userInput = Jsoup.clean(usernameSearch, Whitelist.basic());
		String userName = userInput;

		boolean status = custService.deleteAccountBYInternal(userName);
		if (status) {
			modelAndView.addObject("status", "User Deleted Succefully!");
			modelAndView.setViewName("viewTicket");
		} else {
			modelAndView
					.addObject("status",
							"Please couldnot be deleted please contact Adminstrator personally!");
			modelAndView.setViewName("viewTicket");
		}

		return modelAndView;
	}

	// // Displaying the ViewUser(SearchUser).jsp page
	// @RequestMapping(value = "/viewUser", method = RequestMethod.GET)
	// public ModelAndView returnViewUserPage() {
	// ModelAndView modelAndView = new ModelAndView();
	// modelAndView.setViewName("viewUser");
	// return modelAndView;
	// }

	// // Getting the userdetails
	// @RequestMapping(value = "/viewUser", method = RequestMethod.POST)
	// public ModelAndView getUserDetail(
	// @RequestParam("viewUser") String accountNumber) {
	// ModelAndView modelAndView = new ModelAndView();
	//
	// Document userInput = Jsoup.parse(accountNumber);
	// String userAccountNumber = userInput.text();
	//
	// int chCount = 0;
	// for (char c: userAccountNumber.toCharArray()) {
	// if(Character.isLetter(c)) {
	// chCount++;
	// }
	// }
	// if(userAccountNumber.length()==0 || chCount!=0 ||
	// userAccountNumber.length()>10 || userAccountNumber.length()<8) {
	// modelAndView.addObject("errorMsg",
	// "Please enter the correct account number!");
	// modelAndView.setViewName("viewUser");
	// }
	// else {
	// CustomerInformationDTO customerDetails = new CustomerInformationDTO();
	// customerDetails = custService.getUserFromAccount(userAccountNumber);
	// modelAndView.addObject("customerDetails", customerDetails);
	// modelAndView.setViewName("viewUser");
	// }
	// return modelAndView;
	// }

	@RequestMapping("/getList")
	public ModelAndView getUserLIst() {
		List<EmployeeInformationDTO> userList = custService.getUserList();
		return new ModelAndView("userList", "userList", userList);
	}

	// Displaying the modify user(modifyUser).jsp page
	@RequestMapping(value = "/modifyUser", method = RequestMethod.GET)
	public ModelAndView returnModifyUserPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("modifyUser");
		return modelAndView;
	}

	// First fetch the details from database for admin to edit/modify these
	// details.
	@RequestMapping(value = "/modifyUser", method = RequestMethod.POST)
	public ModelAndView getmodifyUserDetail(
			@RequestParam("modifyUser") String accountNumber) {
		ModelAndView modelAndView = new ModelAndView();

		
		String userInput = Jsoup.clean(accountNumber, Whitelist.basic());
		String userAccountNumber = userInput;

		EmployeeInformationDTO employeeDetails = new EmployeeInformationDTO();
		employeeDetails = custService
				.getEmployeeFromUserName(userAccountNumber);
		modelAndView.addObject("customerDetails", employeeDetails);
		modelAndView.setViewName("modifyUser");

		return modelAndView;
	}

	// Modify Internal employee in database, this is just for admin.
	@RequestMapping(value = "/getList", method = RequestMethod.POST)
	public ModelAndView getmodifyUserDatabase(
			@ModelAttribute("customerDetails") CustomerInformationDTO customerDetail) {
		ModelAndView modelAndView = new ModelAndView();
		CustomerInformationDTO customerDetails = customerDetail;
		String status = custService.updateAccount(customerDetails);
		modelAndView.addObject("customerDetails", customerDetails);
		modelAndView.addObject("status", status);
		modelAndView.setViewName("userList");
		return modelAndView;
	}

	// Modify Internal employee in database, this is just for admin.
	@RequestMapping(value = "/modifyExternalUserByInternal", method = RequestMethod.POST)
	public ModelAndView getmodifyExternalDatabase(
			@ModelAttribute("modifyExternalUserByAdmin") TicketDetailDTO ticketDetailDTO) {
		ModelAndView modelAndView = new ModelAndView();
		TicketDetailDTO detailDTO = ticketDetailDTO;
		String status = custService.updateExternalAccount(detailDTO);
		modelAndView.addObject("customerDetails", detailDTO);
		modelAndView.addObject("status", status);
		modelAndView.setViewName("ticketAuthorizedSuccess");
		return modelAndView;
	}

	// change password.
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public ModelAndView returnchangePasswordPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("changePassword");
		return modelAndView;
	}

	// Getting the userdetails
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ModelAndView getchangePassword(
			@ModelAttribute("customerDetails") ChangePassword customerDetail) {
		ModelAndView modelAndView = new ModelAndView();
		ChangePassword customerDetails = customerDetail;

		String pass = customerDetails.getPassword();
		int number = 0;
		int count = 0;

		for (char c : pass.toCharArray()) {
			if (Character.isDigit(c)) {
				number++;
			}
		}

		for (char c : pass.toCharArray()) {
			if (Character.isUpperCase(c)) {
				count++;
			}
		}

		Pattern p = Pattern.compile("[!@#$%^&*+_.-]");
		Matcher match = p.matcher(pass.subSequence(0, pass.length()));

		if (pass.length() < 6 || pass.length() > 15 || number <= 0
				|| count <= 0 || match.find() == false) {
			modelAndView.addObject("errorMsg",
					"Entered password is not valid! Please enter again.");
			modelAndView.setViewName("changePassword");
		} else if (pass.equalsIgnoreCase(customerDetails.getConfirmPassword()) == false) {
			modelAndView.addObject("errorMsg",
					"Password and confirm password does not match.");
			modelAndView.setViewName("changePassword");
		} else {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				UserDetails userDetail = (UserDetails) auth.getPrincipal();
				customerDetail.setUsername(userDetail.getUsername());
			}
			String status = custService.changeAccountPassword(customerDetails);
			modelAndView.addObject("status", status);
			modelAndView.setViewName("changePassword");
		}
		return modelAndView;
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public ModelAndView returnAddUserPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("addUser");
		return modelAndView;
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public ModelAndView returnAddUserPage(
			@Valid @ModelAttribute("addUserForm") AddUserInformation addUserInfo,
			BindingResult result) throws NoSuchAlgorithmException {

		ModelAndView modelAndView = new ModelAndView();
		AddUserValidation.validateForm(addUserInfo, result);

		if (result.hasErrors()) {
			modelAndView.setViewName("addUser");
			return modelAndView;
		} else {
			String error = custService.insertAddUserInformation(addUserInfo);
			if (error
					.equalsIgnoreCase("UserName, Email and SSN must be unique!")) {
				modelAndView.addObject("errorMsg", error);
				modelAndView.setViewName("addUser");
			} else {
				modelAndView.addObject("successMsg", error);
				modelAndView.setViewName("addUser");
			}
			return modelAndView;
		}
	}

	@RequestMapping(value = "/unlockAccount", method = RequestMethod.GET)
	public ModelAndView returnUnlockAccountPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("unlockAccount");
		return modelAndView;
	}

	@RequestMapping(value = "/unlockAccount", method = RequestMethod.POST)
	public ModelAndView getVerfyUserForUnlock(
			@ModelAttribute("customerDetails") CustomerInformationDTO customerDetail) {
		ModelAndView modelAndView = new ModelAndView();
		
		String userInput = Jsoup.clean(customerDetail.getUsername(), Whitelist.basic());
		customerDetail.setUsername(userInput);

		Pattern p1 = Pattern.compile("[!@#$%^&*+_.-]");
		Matcher match = p1.matcher(customerDetail.getUsername().subSequence(0, customerDetail.getUsername().length()));
		
		if(customerDetail==null||customerDetail.getUsername()==null||customerDetail.getUsername().isEmpty()||customerDetail.getUsername()==null||match.find()==true||
				customerDetail.getUsername().length()>10){

			modelAndView.addObject("status", "Please Enter a valid Input.");
		} else {
			CustomerInformationDTO customerDetails = customerDetail;
			String status = custService.unlockAccount(customerDetails);
			modelAndView.addObject("status", status);
		}

		modelAndView.setViewName("unlockAccount");
		return modelAndView;
	}

	@RequestMapping(value = "/viewQueue", method = RequestMethod.GET)
	public ModelAndView returnViewQueuePage() {
		ModelAndView modelAndView = new ModelAndView();
		List<TicketInformationDTO> userList = custService
				.getPendingTicketList();
		List<TicketInformationDTO> approvedList = custService
				.getApprovedTicketList();
		List<TicketInformationDTO> rejectedList = custService
				.getRejectedTicketList();
		modelAndView.addObject("userList", userList);
		modelAndView.addObject("approvedList", approvedList);
		modelAndView.addObject("rejectedList", rejectedList);
		modelAndView.setViewName("viewQueue");
		return modelAndView;
	}

	@RequestMapping(value = "/ticketDetails", method = RequestMethod.GET)
	public ModelAndView returnTicketDetailsPage() {
		List<TicketInformationDTO> userList = custService
				.getPendingTicketList();
		return new ModelAndView("viewQueue", "userList", userList);
	}

	// Modify Internal employee in database, this is just for admin.
	@RequestMapping(value = "/viewTicket", method = RequestMethod.POST)
	public ModelAndView getviewTicketDatabase(
			@ModelAttribute("ticketDetail") TicketInformationDTO user) {
		ModelAndView modelAndView = new ModelAndView();
		TicketInformationDTO ticketDetails = user;
		TicketDetailDTO ticketDetailDTO = custService
				.fetchTicketDetail(ticketDetails);
		if (ticketDetailDTO == null) {
			modelAndView.addObject("error",
					"Some error occured while processing please try again.");
		}
		modelAndView.addObject("ticketDetailDTO", ticketDetailDTO);
		modelAndView.setViewName("ticketDetails");
		return modelAndView;
	}

	// Authhorize Critical transactions.
	@RequestMapping(value = "/authorizeTransactionsApprove", method = RequestMethod.POST)
	public ModelAndView getauthorizeTransactionsApprove(
			@ModelAttribute("authorizeTransactions") TicketDetailDTO ticketDetailDTO) {
		ModelAndView modelAndView = new ModelAndView();
		TicketDetailDTO detailDTO = ticketDetailDTO;
		boolean result = custService.approveAuthorizeTransactions(detailDTO);
		if (result) {
			modelAndView
					.addObject("status", "Success, Authorization Approved!");
		} else {
			modelAndView.addObject("status",
					"Error Occured, Please check User's account balance for sufficient amount, Please contact admin!");
		}
		modelAndView.setViewName("ticketAuthorizedSuccess");
		return modelAndView;
	}

	// Reject Critical transactions.
	@RequestMapping(value = "/authorizeTransactionsReject", method = RequestMethod.POST)
	public ModelAndView getauthorizeTransactionsReject(
			@ModelAttribute("authorizeTransactions") TicketDetailDTO ticketDetailDTO) {
		ModelAndView modelAndView = new ModelAndView();
		TicketDetailDTO detailDTO = ticketDetailDTO;
		boolean result = custService.rejectAuthorizeTransactions(detailDTO);
		if (result) {
			modelAndView
					.addObject("status", "Authorization Rejected!");
		} else {
			modelAndView.addObject("status",
					"Error Occured, Please contact admin!");
		}
		modelAndView.setViewName("ticketAuthorizedSuccess");
		return modelAndView;
	}

	// forgotPassword module
	// GET Requests
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public ModelAndView forgotPassword() {
		ModelAndView model = new ModelAndView();
		model.setViewName("forgotPassword");
		return model;
	}

	// POST Requests
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ModelAndView getOtp(@RequestParam("email") String emailOtp) {
		ModelAndView modelAndView = new ModelAndView();
		if (!emailOtp.isEmpty()) {
			
			String emailOtpDoc = Jsoup.clean(emailOtp, Whitelist.basic());
			String emailReset = "";
			emailReset = custService.genOtp(emailOtpDoc);
			modelAndView.addObject("emailReset", emailReset);
		} else {
			modelAndView.addObject("emailReset", "Text-box cannot be left empty.");
		}

		modelAndView.setViewName("forgotPassword");
		return modelAndView;
	}
	
	// Reset Password
	// GET Request
	@RequestMapping(value="/resetPassword", method=RequestMethod.GET)
	public ModelAndView returnResetPasswordPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("resetPassword");
		return modelAndView;
	}
	
	// Reset Password
	// POST Requet
	@RequestMapping(value="/resetPassword", method=RequestMethod.POST)
	public ModelAndView processResetPassword(@RequestParam("email") String email,
			@RequestParam("otp") String otp, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmNewPassword") String confirmNewPassword) {
		ModelAndView modelAndView = new ModelAndView();
		
		if (!(email.isEmpty() && otp.isEmpty() && newPassword.isEmpty() && confirmNewPassword.isEmpty())) {
			// All inputs should be sanitized
			String emailInput = Jsoup.clean(email, Whitelist.basic());
			String otpInput = Jsoup.clean(otp, Whitelist.basic());
			String newPasswordInput = Jsoup.clean(newPassword, Whitelist.basic());
			String confirmNewPasswordInput = Jsoup.clean(confirmNewPassword, Whitelist.basic());
			
			String resetInformation = custService.resetPasswordService(emailInput, otpInput, newPasswordInput, confirmNewPasswordInput);
			modelAndView.addObject("resetInformation", resetInformation);
		} else {
			modelAndView.addObject("resetInformation", "Do not leave any text-boxes empty");
		}
		modelAndView.setViewName("resetPassword");
		return modelAndView;
	}
	@RequestMapping("/viewPII")
	public ModelAndView getPIIList() {
		List<CustomerInformationDTO> userList = custService.getUserPIIList();
		return new ModelAndView("viewPII", "userList", userList);
	}
}