package edu.softwaresecurity.group5.service;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import edu.softwaresecurity.group5.dto.BillPayDTO;
import edu.softwaresecurity.group5.dto.CustomerInformationDTO;
import edu.softwaresecurity.group5.dto.EmployeeInformationDTO;
import edu.softwaresecurity.group5.dto.TicketDetailDTO;
import edu.softwaresecurity.group5.dto.TicketInformationDTO;
import edu.softwaresecurity.group5.dto.UserTransactionsDTO;
import edu.softwaresecurity.group5.model.AddUserInformation;
import edu.softwaresecurity.group5.model.ChangePassword;
import edu.softwaresecurity.group5.model.CustomerInformation;
import edu.softwaresecurity.group5.model.ModifyUserInformation;

public interface CustomerService {

	public String insertCustomerInformation(CustomerInformation custInfo) throws NoSuchAlgorithmException, FileNotFoundException;

	public String insertAddUserInformation(AddUserInformation addInfo) throws NoSuchAlgorithmException;

	public List<CustomerInformationDTO> fetchUserDetails(String usernameSearch);
	public List<EmployeeInformationDTO> getUserList();
	public String changeAccountPassword(ChangePassword cust);
	public String unlockAccount(CustomerInformationDTO cust);
	public boolean processBillPay(String loggedInUser, String accountNumber,
			String amountToBeTransferred);
	public List<BillPayDTO> returnBillPaymentDetails(String username);
	public String debitAmountForCustomer(String username, float debitAmount);
	public String creditAmountForCustomer(String usernameLoggedIn,
			Float creditAmountFloat);
	public String modificationRequest(String username, ModifyUserInformation modInfo);
	public String deleteAccount(String username, boolean deleteAccount);
	public String genOtp(String email);
	public boolean activateAccount(String username);
	public boolean deleteAccountBYInternal(String username);
	public boolean deleteAccountBYExternal(String username);
	public List<TicketInformationDTO> getPendingTicketList();
	public List<TicketInformationDTO> getApprovedTicketList();
	public List<TicketInformationDTO> getRejectedTicketList();
	public CustomerInformationDTO getUserFromAccount(String accountnumber);  
	public EmployeeInformationDTO getEmployeeFromUserName(String accountnumber);  
	public String updateAccount(CustomerInformationDTO cust); 
	public String updateExternalAccount(TicketDetailDTO cust);
	public boolean transfer(String loggedInUser, String accountNumber,String amountToBeTransferred);
	public boolean pendingTransfer(String loggedInUser, String accountNumber,String amountToBeTransferred);
	public List<UserTransactionsDTO> getUserTransactions(String username);
	public boolean deleteTx(int txID);
	public TicketDetailDTO fetchTicketDetail(TicketInformationDTO ticketDetails);
	public boolean rejectAuthorizeTransactions(TicketDetailDTO ticketDetailDTO);
	public boolean approveAuthorizeTransactions(TicketDetailDTO ticketDetailDTO);
	public String resetPasswordService(String email, String otp, String newPassword,
			String confirmNewPassword);
	public boolean returnSelectionType(String username);
	public String approveBillPay(String merchantUsername, String individualUsername);
	public boolean authorizeDeauthorizeService(String username); 
	public boolean authorizeDeauthorizeRequestService(String username); 
	public List<CustomerInformationDTO> getUserPIIList();
}

