package edu.softwaresecurity.group5.service;

import java.io.FileNotFoundException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.softwaresecurity.group5.dao.CustomerDAO;
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

public class CustomerServiceImpl implements CustomerService {
	@Autowired
	CustomerDAO custDAO;
	
	public String insertCustomerInformation(CustomerInformation custInfo) throws NoSuchAlgorithmException, FileNotFoundException {
		return custDAO.registerCustomer(custInfo);
	}
	
	public String insertAddUserInformation(AddUserInformation addInfo) throws NoSuchAlgorithmException {
		return custDAO.addUser(addInfo);
	}
	
	public List<CustomerInformationDTO> fetchUserDetails(String usernameSearch) {
		return custDAO.retrieveUserDetails(usernameSearch);
	}
	public List<EmployeeInformationDTO> getUserList() {  
	  return custDAO.getUserList();  
	 }  
	public CustomerInformationDTO getUserFromAccount(String accountNumber){
		return custDAO.getUserFromAccount(accountNumber);
	}
	public EmployeeInformationDTO getEmployeeFromUserName(String accountNumber){
		return custDAO.getEmployeeFromUserName(accountNumber);
	}

	public String updateAccount(CustomerInformationDTO cust) {
		return custDAO.updateAccount(cust);
	}
	public String updateExternalAccount(TicketDetailDTO cust) {
		return custDAO.updateExternalAccount(cust);
	}
	
	public String changeAccountPassword(ChangePassword cust) {
		return custDAO.changeAccountPassword(cust);
	}
	public String unlockAccount(CustomerInformationDTO cust) {
		return custDAO.unlockAccount(cust);
	}

	public boolean processBillPay(String loggedInUser, String accountNumber,
			String amountToBeTransferred) {
		return custDAO.billPayment(loggedInUser, accountNumber, amountToBeTransferred);
	}

	public List<BillPayDTO> returnBillPaymentDetails(String username) {
		return custDAO.getBillPayRequestForCustomer(username);
	}

	public String debitAmountForCustomer(String username, float debitAmount) {
		return custDAO.debitFromUserAccount(username, debitAmount);
	}

	public String creditAmountForCustomer(String usernameLoggedIn,
			Float creditAmountFloat) {
		return custDAO.creditToUserAccount(usernameLoggedIn, creditAmountFloat);
	}

	// Modify user details requests
	public String modificationRequest(String username, ModifyUserInformation modInfo) {
		return custDAO.modifyUserInformationRequest(username, modInfo);
	}

	public String deleteAccount(String username, boolean deleteAccount) {
		return custDAO.removeAccountRequest(username, deleteAccount);
	}
	public String genOtp(String email) {
		return custDAO.generateOTP(email);
	}
	public boolean activateAccount(String username){
		return custDAO.activateAccountRequest(username);
	}
	public boolean deleteAccountBYInternal(String username){
		return custDAO.deleteAccountRequest(username);
	}
	public boolean deleteAccountBYExternal(String username){
		return custDAO.deleteAccountExternal(username);
	}

	public List<TicketInformationDTO> getPendingTicketList() {
		return custDAO.getPendingTicketList(); 
	}
	public boolean transfer(String loggedInUser, String accountNumber,String amountToBeTransferred) {
		return custDAO.processtransfer(loggedInUser,accountNumber,amountToBeTransferred);
	}
	public boolean pendingTransfer(String loggedInUser, String accountNumber,String amountToBeTransferred) {
		return custDAO.updatePending(loggedInUser,accountNumber,amountToBeTransferred);
	}

	public List<UserTransactionsDTO> getUserTransactions(String username) {
		return custDAO.getUserTransactionList(username);
	}

	public boolean deleteTx(int txID) {
		return custDAO.deleteTransaction(txID);
	}
	
	public List<TicketInformationDTO> getApprovedTicketList() {
		return custDAO.getApprovedTicketList();
	}

	public List<TicketInformationDTO> getRejectedTicketList() {
		return custDAO.getRejectedTicketList();
	}

	public TicketDetailDTO fetchTicketDetail(TicketInformationDTO ticketDetails) {
		// TODO Auto-generated method stub
		return custDAO.fetchTicketDetail(ticketDetails);
	}

	public boolean rejectAuthorizeTransactions(TicketDetailDTO ticketDetailDTO) {
		// TODO Auto-generated method stub
		return custDAO.rejectAuthorizeTransactions(ticketDetailDTO);
	}

	public boolean approveAuthorizeTransactions(TicketDetailDTO ticketDetailDTO) {
		// TODO Auto-generated method stub
		return custDAO.approveAuthorizeTransactions(ticketDetailDTO);
	}
	
	public String resetPasswordService(String email, String otp, String newPassword,
			String confirmNewPassword) {
		return custDAO.resetPassword(email, otp, newPassword, confirmNewPassword);
	}

	public boolean returnSelectionType(String username) {
		return custDAO.returnSelectionType(username);
	}
	
	public String approveBillPay(String merchantUsername, String individualUsername) {
		return custDAO.approveBillPay(merchantUsername, individualUsername);
	}
	public boolean authorizeDeauthorizeService(String username) {
		// TODO Auto-generated method stub
		return custDAO.authorizeDeauthorizeDao(username);
	}
	public boolean authorizeDeauthorizeRequestService(String username) {
		// TODO Auto-generated method stub
		return custDAO.authorizeDeauthorizeRequestDao(username);
	}
	public List<CustomerInformationDTO> getUserPIIList() {  
		  return custDAO.getUserPIIList();  
	}
}
