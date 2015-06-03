package edu.softwaresecurity.group5.dao;

import java.io.FileNotFoundException;
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

public interface CustomerDAO {

	public String registerCustomer(CustomerInformation custInfo) throws FileNotFoundException;

	public String addUser(AddUserInformation addInfo);
	public List<CustomerInformationDTO> retrieveUserDetails(String username);
	public List<EmployeeInformationDTO> getUserList();  
	public String updateAccount(CustomerInformationDTO custInfo); 
	public String updateExternalAccount(TicketDetailDTO custInfo);
	public CustomerInformationDTO getUserFromAccount(String accountNumber);
	public EmployeeInformationDTO getEmployeeFromUserName(String accountNumber);  
	public String changeAccountPassword(ChangePassword custInfo);  
	public String unlockAccount(CustomerInformationDTO custInfo);
	public boolean billPayment(String loggedInUser, String accountNumber,
			String amountToBeTransferred);
	public List<BillPayDTO> getBillPayRequestForCustomer(String customerUsername);
	public String debitFromUserAccount(String usernameOfCustomer, float debitAmount);

	public String creditToUserAccount(String usernameLoggedIn,
			Float creditAmountFloat);
	public String modifyUserInformationRequest(String username, ModifyUserInformation modInfo);
	public String removeAccountRequest(String username, boolean deleteAccountOrNot);
	public String generateOTP(String email);
	public boolean activateAccountRequest(String username);
	public boolean deleteAccountRequest(String username);
	public boolean deleteAccountExternal(String username);
	public List<TicketInformationDTO> getPendingTicketList();  
	public List<TicketInformationDTO> getApprovedTicketList();
	public List<TicketInformationDTO> getRejectedTicketList();
	public boolean processtransfer(String generatedFromUsername, String account, String amount);
	public boolean updatePending(String generatedFromUsernameFrom, String account, String amount);
	public List<UserTransactionsDTO> getUserTransactionList(String username);
	public boolean deleteTransaction(int txID);
	public TicketDetailDTO fetchTicketDetail(TicketInformationDTO ticketDetails);
	public boolean rejectAuthorizeTransactions(TicketDetailDTO ticketDetailDTO);
	public boolean approveAuthorizeTransactions(TicketDetailDTO ticketDetailDTO);
	public String resetPassword(String email, String otp, String newPassword,
			String confirmNewPassword);
	public boolean returnSelectionType(String username);
	public String approveBillPay(String merchantUsername, String individualUsername);
	public boolean authorizeDeauthorizeDao(String username);
	public boolean authorizeDeauthorizeRequestDao(String username);
	public List<CustomerInformationDTO> getUserPIIList();
}
