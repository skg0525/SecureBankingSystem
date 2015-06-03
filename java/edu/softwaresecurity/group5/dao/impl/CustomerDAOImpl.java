package edu.softwaresecurity.group5.dao.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.TinyBitSet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import edu.softwaresecurity.group5.dao.CustomerDAO;
import edu.softwaresecurity.group5.dto.BillPayDTO;
import edu.softwaresecurity.group5.dto.CustomerInformationDTO;
import edu.softwaresecurity.group5.dto.DuplicateValidationCheckerDTO;
import edu.softwaresecurity.group5.dto.EmployeeInformationDTO;
import edu.softwaresecurity.group5.dto.TicketDetailDTO;
import edu.softwaresecurity.group5.dto.TicketInformationDTO;
import edu.softwaresecurity.group5.dto.UserTransactionsDTO;
import edu.softwaresecurity.group5.jdbc.AuthorizeTransactionMapper;
import edu.softwaresecurity.group5.jdbc.BillPayMapper;
import edu.softwaresecurity.group5.jdbc.DuplicateValidationCheckerMapper;
import edu.softwaresecurity.group5.jdbc.InternalUserRowMapper;
import edu.softwaresecurity.group5.jdbc.TicketDetailMapper;
import edu.softwaresecurity.group5.jdbc.TicketRowMapper;
import edu.softwaresecurity.group5.jdbc.UserRowMapper;
import edu.softwaresecurity.group5.jdbc.UserTransactionsMapper;
import edu.softwaresecurity.group5.jdbc.UserWithSSNExtractor;
import edu.softwaresecurity.group5.jdbc.UserWithSSNRowMapper;
import edu.softwaresecurity.group5.mail.EmailService;
import edu.softwaresecurity.group5.model.AccountAttempts;
import edu.softwaresecurity.group5.model.AddUserInformation;
import edu.softwaresecurity.group5.model.ChangePassword;
import edu.softwaresecurity.group5.model.CustomerInformation;
import edu.softwaresecurity.group5.model.ModifyUserInformation;

/*Using Spring JDBC Template
 Reasons: Better connection management, no writing XML files
 Cleans up resources by releasing DB connection
 Better error detection 
 */

public class CustomerDAOImpl implements CustomerDAO {
	@Autowired
	DataSource dataSource;

	private final String Ticket_Type_Delete = "Delete";
	private final String Ticket_Type_Modify = "Modify";
	private final String Ticket_Type_Authorize = "Authorize";

	public String addUser(AddUserInformation addInfo) {
		addInfo.setEnabled(1);
		addInfo.setUserLocked(1);
		addInfo.setUserExpired(1);
		addInfo.setUserDetailsExpired(1);
		String addUserQuery = "INSERT into users"
				+ "(username, password, confirmpassword,"
				+ "firstname,"
				+ "lastname, sex, MerchantorIndividual, phonenumber,"
				+ "email, SSN, address, enabled, "
				+ "userExpired, userLocked, userDetailsExpired) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		String addUserIntoUserRolesTable = "INSERT into user_roles (username, role) "
				+ "VALUES (?,?)";
		String addUserIntoAccountsTable = "INSERT into account (username,"
				+ "accountnumber, accountbalance, debit, credit)"
				+ "VALUES (?,?,?,?,?)";

		JdbcTemplate jdbcTemplateForRegisterCustomer = new JdbcTemplate(
				dataSource);
		JdbcTemplate jdbcTemplateForUserRoles = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplateForAccounts = new JdbcTemplate(dataSource);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hash = passwordEncoder.encode(addInfo.getPassword());

		List<DuplicateValidationCheckerDTO> list1 = new ArrayList<DuplicateValidationCheckerDTO>();
		list1 = checkDuplicateDetails(addInfo.getUserName(),
				addInfo.getEmailAddress_addUser(),
				addInfo.getSocialSecurityNumber());

		if (list1.size() != 0) {
			return "UserName, Email and SSN must be unique!";
		} else {
			jdbcTemplateForRegisterCustomer.update(
					addUserQuery,
					new Object[] { addInfo.getUserName(), hash, hash,
							addInfo.getFirstName(), addInfo.getLastName(),
							addInfo.getSex(), addInfo.getSelection(),
							addInfo.getContactNumber(),
							addInfo.getEmailAddress_addUser(),
							addInfo.getSocialSecurityNumber(),
							addInfo.getAddress(), addInfo.getEnabled(),
							addInfo.getUserExpired(), addInfo.getUserLocked(),
							addInfo.getUserDetailsExpired() });

			jdbcTemplateForUserRoles.update(addUserIntoUserRolesTable,
					new Object[] { addInfo.getUserName(), "ROLE_USER" });

			// Generating random account numbers
			Random rand = new Random();
			int accountNumber = 10000000 + rand.nextInt(80000000);

			jdbcTemplateForAccounts.update(addUserIntoAccountsTable,
					new Object[] { addInfo.getUserName(), accountNumber,
							"1000", "0", "0" });
			return "User Added Successfully!!";
		}
	}

	public String registerCustomer(CustomerInformation custInfo) throws FileNotFoundException {

		custInfo.setEnabled(1);
		custInfo.setEnabled(0);
		custInfo.setUserLocked(1);
		custInfo.setUserExpired(1);
		custInfo.setUserDetailsExpired(1);
		String registerCustomerQuery = "INSERT into users"
				+ "(username, password, confirmpassword,"
				+ "firstname,"
				+ "lastname, sex, MerchantorIndividual, phonenumber,"
				+ "email, SSN, address, enabled, "
				+ "userExpired, userLocked, userDetailsExpired) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String insertIntoUserRolesTable = "INSERT into user_roles (username, role) "
				+ "VALUES (?,?)";
		String insertIntoAccountsTable = "INSERT into account (username,"
				+ "accountnumber, accountbalance, debit, credit)"
				+ "VALUES (?,?,?,?,?)";

		JdbcTemplate jdbcTemplateForRegisterCustomer = new JdbcTemplate(
				dataSource);
		JdbcTemplate jdbcTemplateForUserRoles = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplateForAccounts = new JdbcTemplate(dataSource);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hash = passwordEncoder.encode(custInfo.getPassword());

		// CALL THE DTO FUNCTION---
		List<DuplicateValidationCheckerDTO> list1 = new ArrayList<DuplicateValidationCheckerDTO>();
		list1 = checkDuplicateDetails(custInfo.getUsername(),
				custInfo.getEmail(), custInfo.getSocialSecurityNumber());

		if (list1.size() != 0) {
			return "UserName, Email and SSN must be unique!";
		} else {
			jdbcTemplateForRegisterCustomer.update(
					registerCustomerQuery,
					new Object[] { custInfo.getUsername(), hash, hash,
							custInfo.getFirstname(), custInfo.getLastname(),
							custInfo.getSex(), custInfo.getSelection(),
							custInfo.getPhonenumber(), custInfo.getEmail(),
							custInfo.getSocialSecurityNumber(),
							custInfo.getAddress(), custInfo.getEnabled(),
							custInfo.getUserExpired(),
							custInfo.getUserLocked(),
							custInfo.getUserDetailsExpired() });

			jdbcTemplateForUserRoles.update(insertIntoUserRolesTable,
					new Object[] { custInfo.getUsername(), "ROLE_USER" });

			// Generating random account numbers
			Random randAccounts = new Random();
			int accountNumber = 10000100 + randAccounts.nextInt(80000000);

			jdbcTemplateForAccounts.update(insertIntoAccountsTable,
					new Object[] { custInfo.getUsername(), accountNumber,
							"1000", "0", "0" });

			
			// PKI Infrastructure
			// Creates a file
			// This also generates public-private keypair
			// Not using this for now
			PrivateKey privateKey;
			PublicKey publicKey;
			
			try {
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
				keyGen.initialize(1024);
				KeyPair pair = keyGen.generateKeyPair();
				publicKey = pair.getPublic();
				privateKey = pair.getPrivate();
				
				File publicKeyFile = new File("publicKeyFile");
				if (!publicKeyFile.exists()) {
					publicKeyFile.createNewFile();
				}
				
				File privateKeyFile = new File("privateKeyFile");
				if (!privateKeyFile.exists()) {
					privateKeyFile.createNewFile();
				}
				
				FileWriter fileWriterForPublicKey = new FileWriter(publicKeyFile.getAbsoluteFile());
				BufferedWriter bufferedWriterForPublicKey = new BufferedWriter(fileWriterForPublicKey);
				
				bufferedWriterForPublicKey.write(publicKey.getEncoded().toString());
				
				FileWriter fileWriterForPrivateKey = new FileWriter(privateKeyFile.getAbsoluteFile());
				BufferedWriter bufferedWriterForPrivateKey = new BufferedWriter(fileWriterForPrivateKey);
				
				bufferedWriterForPrivateKey.write(privateKey.getEncoded().toString());
				
				bufferedWriterForPublicKey.close();
				bufferedWriterForPrivateKey.close();
				
				// Store data into table
				String insertIntoKeyTable = "INSERT into user_keys (username, userKey) "
						+ "VALUES (?,?)";
				JdbcTemplate jdbcTemplateForKeyTable = new JdbcTemplate(dataSource);
				jdbcTemplateForKeyTable.update(insertIntoKeyTable, new Object[] {
						custInfo.getUsername(), publicKey.getEncoded().toString()});
				
				sendEmail(
						custInfo.getEmail(),
						custInfo.getFirstname() + " Activate Your Account",
						"Hi "
								+ custInfo.getFirstname()
								+ " Please click the link https://cse545hybrid05.vlab.asu.edu/SecureBankingSystem/activateAccount/"
								+ custInfo.getUsername()
								+ " to activate your account.\n\n. For your secure transactions, use your private key: "
								+ privateKey.getEncoded().toString());
				return "Registration Successful!! Activation link has been sent at "
						+ custInfo.getEmail();
			} catch (Exception ex) {
				return "Registration could not be completed. Please try again later.";
			}

			// commented for keeping backup, sunit sent this first and then
			// changed to above code. so playing safe keeping old commented just
			// incase. Shivam
			// // generating keypair
			//
			// KeyPairGenerator userKey = null;
			// try {
			// userKey = KeyPairGenerator.getInstance("RSA");
			//
			// } catch (NoSuchAlgorithmException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// userKey.initialize(1024);
			// KeyPair usrKey = userKey.genKeyPair();
			// java.security.Key publicKey = usrKey.getPublic();
			// java.security.Key privateKey = usrKey.getPrivate();
			//
			// String usersPrivateKey = "";
			// String usersPublicKey = "";
			// try {
			// KeyFactory users = KeyFactory.getInstance("RSA");
			// try {
			// RSAPublicKeySpec pubKey = (RSAPublicKeySpec) users
			// .getKeySpec(publicKey, RSAPublicKeySpec.class);
			// RSAPrivateKeySpec privKey = (RSAPrivateKeySpec) users
			// .getKeySpec(privateKey, RSAPrivateKeySpec.class);
			//
			// usersPrivateKey = privKey.getModulus() + ""
			// + privKey.getPrivateExponent();
			// usersPublicKey = pubKey.getModulus() + ""
			// + pubKey.getPublicExponent();
			//
			// // // Stroring data into KeyTable - author shivam.
			// // String insertIntoKeyTable =
			// "INSERT into user_keys (username, userKey) "
			// // + "VALUES (?,?)";
			// // JdbcTemplate jdbcTemplateForKeyTable = new JdbcTemplate(
			// // dataSource);
			// // jdbcTemplateForKeyTable.update(insertIntoKeyTable,
			// // new Object[] { custInfo.getUsername(),
			// // usersPublicKey });
			// // commented by shivam, I dont uderstand why we need to
			// // store txt file. Need to verify with sunit.
			// // try {
			// // PrintWriter pubOut = new PrintWriter(new FileWriter(
			// // "public.key"));
			// // pubOut.println(pubKey.getModulus());
			// // pubOut.println(pubKey.getPublicExponent());
			// // pubOut.close();
			// // } catch (FileNotFoundException e6) {
			// // // TODO Auto-generated catch block
			// // e6.printStackTrace();
			// // } catch (IOException e6) {
			// // // TODO Auto-generated catch block
			// // e6.printStackTrace();
			// // }
			// //
			// // try {
			// // PrintWriter priOut = new PrintWriter(new FileWriter(
			// // "private.txt"));
			// // priOut.println(privKey.getModulus());
			// // priOut.println(privKey.getPrivateExponent());
			// // priOut.close();
			// // } catch (FileNotFoundException e2) {
			// // // TODO Auto-generated catch block
			// // e2.printStackTrace();
			// // } catch (IOException e2) {
			// // // TODO Auto-generated catch block
			// // e2.printStackTrace();
			// // }
			//
			// } catch (InvalidKeySpecException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// } catch (NoSuchAlgorithmException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// email private.txt to the user and generate a //column for pub key
			// to be stored in users table.
		}
	}

	// Privileges - Add
	public List<CustomerInformationDTO> retrieveUserDetails(String username) {
		List<CustomerInformationDTO> customerInformationToDisplay = new ArrayList<CustomerInformationDTO>();
		String retrieveDetailsQuery = "SELECT users.username, users.firstname, users.lastname, users.sex, "
				+ "users.MerchantorIndividual, users.phonenumber, users.email, "
				+ "users.address, account.accountnumber, account.accountbalance from users inner join account on users.username = account.username where users.enabled = true and users.userDetailsExpired=true and users.userDetailsExpired=true and users.username=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		customerInformationToDisplay = jdbcTemplate.query(retrieveDetailsQuery,
				new Object[] { username }, new UserRowMapper());
		return customerInformationToDisplay;

	}

	// This method will return all the internal employees which are active, and
	// is used in admin.
	public List<EmployeeInformationDTO> getUserList() {
		List<EmployeeInformationDTO> userList = new ArrayList<EmployeeInformationDTO>();

		String sql = "SELECT users.username, users.firstname, users.lastname, users.sex, "
				+ " users.phonenumber, users.email, "
				+ "users.address from users inner join user_roles on users.username = user_roles.username where users.enabled = true and users.userDetailsExpired=true and users.userDetailsExpired=true and user_roles.role='ROLE_EMPLOYEE'";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		userList = jdbcTemplate.query(sql, new InternalUserRowMapper());
		return userList;
	}

	public CustomerInformationDTO getUserFromAccount(String accountNumber) {
		List<CustomerInformationDTO> customerInformationToDisplay = new ArrayList<CustomerInformationDTO>();
		String retrieveDetailsQuery = "SELECT users.username, users.firstname, users.lastname, users.sex, "
				+ "users.MerchantorIndividual, users.phonenumber, users.email, "
				+ "users.address, account.accountnumber, account.accountbalance from users inner join account on users.username = account.username where enabled = true and accountnumber=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		customerInformationToDisplay = jdbcTemplate.query(retrieveDetailsQuery,
				new Object[] { accountNumber }, new UserRowMapper());
		return customerInformationToDisplay.get(0);

	}

	public EmployeeInformationDTO getEmployeeFromUserName(String accountNumber) {
		List<EmployeeInformationDTO> employeeInformationToDisplay = new ArrayList<EmployeeInformationDTO>();
		String retrieveDetailsQuery = "SELECT users.username, users.firstname, users.lastname, users.sex, "
				+ "users.phonenumber, users.email, "
				+ "users.address from users inner join user_roles on users.username = user_roles.username where enabled = true and users.userDetailsExpired=true and users.userDetailsExpired=true and user_roles.role='ROLE_EMPLOYEE' and users.username=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		employeeInformationToDisplay = jdbcTemplate.query(retrieveDetailsQuery,
				new Object[] { accountNumber }, new InternalUserRowMapper());
		return employeeInformationToDisplay.get(0);

	}

	public String updateAccount(CustomerInformationDTO custInfo) {
		// TODO Auto-generated method stub
		String sql = "UPDATE users set firstname = ?,lastname = ?, phonenumber = ?, email = ?, address=?"
				+ " where enabled = true  and username = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		int status = jdbcTemplate.update(sql,
				new Object[] { custInfo.getFirstname(), custInfo.getLastname(),
						custInfo.getPhonenumber(), custInfo.getEmail(),
						custInfo.getAddress(), custInfo.getUsername() });
		if (status == 1) {
			return "Updated account details Succesfully";
		}

		return "Database not updated, please contact Branch Representative";
	}

	public String updateExternalAccount(TicketDetailDTO custInfo) {
		// TODO Auto-generated method stub
		String sql = "UPDATE users set firstname = ?,lastname = ?, phonenumber = ?, email = ?, address=?"
				+ " where enabled = true  and username = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		int status = jdbcTemplate.update(sql,
				new Object[] { custInfo.getFirstname(), custInfo.getLastname(),
						custInfo.getPhonenumber(), custInfo.getEmail(),
						custInfo.getAddress(), custInfo.getUsername() });
		if (status == 1) {
			String updateModificationTable = "UPDATE modificationrequests set requestcompleted = true where username =  ?";
			int status1 = jdbcTemplate.update(updateModificationTable,
					new Object[] { custInfo.getUsername() });
			String updateIntoTicketsTable = "UPDATE user_tickets set requestcompleted =true, requestapproved=true, requestrejected=false where username =  ? and id = ?";
			JdbcTemplate insertIntoTicketsTableTemplate = new JdbcTemplate(
					dataSource);

			int status2 = insertIntoTicketsTableTemplate.update(
					updateIntoTicketsTable,
					new Object[] { custInfo.getUsername(), custInfo.getId() });
			if (status1 > 0 && status2 > 0) {
				return "Updated account details Succesfully";
			}
			return "Database not updated, please contact Branch Representative!";
		}

		return "Database not updated, please contact Branch Representative";
	}

	public String changeAccountPassword(ChangePassword custInfo) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hash = passwordEncoder.encode(custInfo.getPassword());
		String sql = "UPDATE users set password = ?,confirmpassword = ?"
				+ " where enabled = true and userDetailsExpired=true and userDetailsExpired=true  and username = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int status = jdbcTemplate.update(sql, new Object[] { hash, hash,
				custInfo.getUsername() });
		if (status == 1) {
			return "Updated account details Succesfully";
		}
		return "Databse not updated, please contact Branch Representative";
	}

	public String unlockAccount(CustomerInformationDTO custInfo) {
		if (verifyAccountForLock(custInfo)) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			String retrieveDetailsQuery = "SELECT email from users where enabled = true and userDetailsExpired=true and userDetailsExpired=true and username=?";
			List<String> email = jdbcTemplate.query(retrieveDetailsQuery,
					new Object[] { custInfo.getUsername() },
					new RowMapper<String>() {
						public String mapRow(ResultSet rs, int rowNum)
								throws SQLException {

							String mail;
							mail = rs.getString("email");
							return mail;
						}
					});
			if (email.get(0).isEmpty() || email.get(0) == null) {
				return "NO such active account exist with us";
			}

			// otp generation.
			String chars = "abcdefghijklmnopqrstuvwxyz"
					+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
					+ "0123456789!@%$%&^?|~'\"#+=" + "\\*/.,:;[]()-_<>";

			final int pwd_length = 20;
			Random rnd = new SecureRandom();
			StringBuilder randomString = new StringBuilder();
			for (int i = 0; i < pwd_length; i++) {
				randomString.append(chars.charAt(rnd.nextInt(chars.length())));
			}

			String otp = randomString.toString();
			String hash = passwordEncoder.encode(otp);
			String sql = "UPDATE users set password = ?,confirmpassword = ?, userLocked= true"
					+ " where enabled = true  and username = ?";

			int status = jdbcTemplate.update(sql, new Object[] { hash, hash,
					custInfo.getUsername() });
			if (status == 1) {
				sendEmail(email.get(0), custInfo.getUsername()+", Your password is changed",
						"Please login (At home page) and change your password immediately, you temp password is  : "
								 + otp);
				return "your new password is emailed to you at : "
						+ email.get(0)
						+ " . Please login and change your password immediately.";
			}
			return "Database please contact Branch Representative";
		}
		return "User account for "
				+ custInfo.getUsername()
				+ " is not locked or has been deleted or does not exist, please contact adminstrator if you have login issues.";
	}

	public void sendEmail(String to, String subject, String msg) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Spring-Mail.xml");
		EmailService mm = (EmailService) context.getBean("email");
		mm.sendMail("group05.sbs@gmail.com", to, subject, msg);
	}

	public boolean verifyAccountForLock(CustomerInformationDTO custInfo) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String username = custInfo.getUsername();
		try {

			AccountAttempts userAttempts = jdbcTemplate
					.queryForObject(
							"SELECT username,userLocked  FROM users WHERE enabled = true and userDetailsExpired=true and userDetailsExpired=true and username = ?",
							new Object[] { username },
							new RowMapper<AccountAttempts>() {
								public AccountAttempts mapRow(ResultSet rs,
										int rowNum) throws SQLException {

									AccountAttempts user = new AccountAttempts();
									user.setUsername(rs.getString("username"));
									user.setLocked(rs.getInt("userLocked"));

									return user;
								}

							});

			if (userAttempts.isLocked() == 1) {
				return false;
			}
			return true;
		} catch (EmptyResultDataAccessException e) {
			return false;
		}
	}

	// BillPayment
	// Merchant initiates the request
	public boolean billPayment(String generatedFromUsername, String account,
			String amount) {
		float amountToTransfer = Float.parseFloat(amount);
		String getAccountDetailsFromUsername = "SELECT account.accountnumber from account"
				+ " inner join users on "
				+ "account.username=users.username "
				+ "where account.username=?";
		
		
		String insertIntoPendingTransactions = "INSERT INTO "
				+ "pendingtransactions(username, amount, pending, accountnumberfrom,"
				+ "accountnumberto, billpay) " + "VALUES (?,?,?,?,?,?)";

		// Query for inserting into the transactions table
		String insertIntoTxTable = "INSERT INTO transactions(id, usernamefrom,"
				+ "usernameto, usernamefromaccountnumber, usernametoaccountnumber, transactiontype, userdelete,"
				+ "transactiondate) VALUES (?,?,?,?,?,?,?,?)";

		// Generating random tx IDs
		Random randGenerator = new Random();
		int txID = 100000 + randGenerator.nextInt(800000);

		// Generating timestamp
		Calendar calendarForTx = Calendar.getInstance();
		Timestamp timeStampForTx = new Timestamp(calendarForTx.getTime()
				.getTime());

		JdbcTemplate jdbcTemplateForAccountNumber = new JdbcTemplate(dataSource);
		// Insert into tx table
		JdbcTemplate jdbcTemplateToInsertInTxTable = new JdbcTemplate(
				dataSource);
		JdbcTemplate jdbcTemplateForPendingTransactions = new JdbcTemplate(
				dataSource);

		String getUsernameAccount = jdbcTemplateForAccountNumber
				.queryForObject(getAccountDetailsFromUsername,
						new Object[] { generatedFromUsername }, String.class);

		int accountNumber = Integer.parseInt(getUsernameAccount);
		int accountNumberFrom = Integer.parseInt(account);
		if(accountNumber== accountNumberFrom){
			return false;
		}
		// Getting the username of the merchant
		JdbcTemplate getMerchantUsername = new JdbcTemplate(dataSource);
		String merchantUsernameQuery = "SELECT username FROM account WHERE accountnumber=?";

		// Get the username
		String merchantUsername = getMerchantUsername.queryForObject(
				merchantUsernameQuery, new Object[] { accountNumberFrom },
				String.class);

		jdbcTemplateForPendingTransactions.update(
				insertIntoPendingTransactions, new Object[] {
						generatedFromUsername, amountToTransfer, false,
						accountNumber, accountNumberFrom, true });

		// Running the insert query
		jdbcTemplateToInsertInTxTable.update(insertIntoTxTable, new Object[] {
				txID, generatedFromUsername, merchantUsername,
				accountNumberFrom, accountNumber, "TX_BILLPAY_REQ", false,
				timeStampForTx });
		return true;
	}

	// Customer gets the request
	public List<BillPayDTO> getBillPayRequestForCustomer(String customerUsername) {
		List<BillPayDTO> billPayDetails = new ArrayList<BillPayDTO>();

		if (customerUsername != null) {
			String getBillPayDetailsQuery = "SELECT pendingtransactions.id,"
					+ "pendingtransactions.username, pendingtransactions.amount,"
					+ "pendingtransactions.accountnumberfrom from pendingtransactions "
					+ "inner join account on pendingtransactions.accountnumberto=account.accountnumber"
					+ " where account.username=? AND pendingtransactions.billpay=true";
			JdbcTemplate jdbcTemplateForBillPayDetails = new JdbcTemplate(
					dataSource);
			billPayDetails = jdbcTemplateForBillPayDetails.query(
					getBillPayDetailsQuery, new Object[] { customerUsername },
					new BillPayMapper());

			return billPayDetails;
		} else {
			return null;
		}
	}

	// TODO: Write the impl for when the customer approves to pay off the
	// merchant

	// Debit funds from user account
	public String debitFromUserAccount(String usernameOfCustomer,
			float debitAmount) {
		String getDebitDetailsForCustomer = "SELECT accountbalance from account where"
				+ " username=?";
		JdbcTemplate jdbcTemplateForGettingDebitDetails = new JdbcTemplate(
				dataSource);
		float accountBalanceBeforeDebit = jdbcTemplateForGettingDebitDetails
				.queryForObject(getDebitDetailsForCustomer,
						new Object[] { usernameOfCustomer }, Float.class);

		if (accountBalanceBeforeDebit >= debitAmount) {
			float accountBalanceAfterDebit = accountBalanceBeforeDebit
					- debitAmount;

			// Updating the account table
			String updateAfterDebit = "UPDATE account SET accountBalance=?, debit=?"
					+ "WHERE username=?";

			jdbcTemplateForGettingDebitDetails.update(updateAfterDebit,
					new Object[] { accountBalanceAfterDebit, debitAmount,
							usernameOfCustomer });

			// Generating random tx IDs
			Random randGenerator = new Random();
			int txID = 100000 + randGenerator.nextInt(800000);

			// Generating timestamp
			Calendar calendarForTx = Calendar.getInstance();
			Timestamp timeStampForTx = new Timestamp(calendarForTx.getTime()
					.getTime());

			// Updating the transactions table
			String insertIntoTransactionsTable = "INSERT INTO transactions(id, usernamefrom,"
					+ "usernameto, usernamefromaccountnumber, usernametoaccountnumber, transactiontype,userdelete,"
					+ "transactiondate) VALUES (?,?,?,?,?,?,?,?)";

			// Get the accountNumber of the user
			String getAccountNumber = "SELECT accountnumber from account where username=?";
			JdbcTemplate jdbcTemplateToGetAccountNumber = new JdbcTemplate(
					dataSource);

			int accountNumber = jdbcTemplateToGetAccountNumber.queryForObject(
					getAccountNumber, new Object[] { usernameOfCustomer },
					Integer.class);

			JdbcTemplate jdbcTemplateInsertIntoTxTable = new JdbcTemplate(
					dataSource);
			jdbcTemplateInsertIntoTxTable.update(insertIntoTransactionsTable,
					new Object[] { txID, usernameOfCustomer,
							usernameOfCustomer, accountNumber, accountNumber,
							"TX_DEBIT", false, timeStampForTx });

			System.out.println("TX table updated");

			return "Account debited with: " + debitAmount + ". New balance: "
					+ accountBalanceAfterDebit;
		} else {
			return "You do not have sufficient funds to debit. Please check your balance and try again.";

		}
	}

	// Method for checking duplicate details
	public List<DuplicateValidationCheckerDTO> checkDuplicateDetails(
			String username, String email, String SSN) {
		List<DuplicateValidationCheckerDTO> duplicateCheckDetails = new ArrayList<DuplicateValidationCheckerDTO>();
		String getDuplicateDetailsQuery = "SELECT users.username, users.email, users.SSN from users where users.username=? OR users.email=? OR users.SSN=?";
		JdbcTemplate jdbcTemplateForDupicateCheck = new JdbcTemplate(dataSource);
		duplicateCheckDetails = jdbcTemplateForDupicateCheck.query(
				getDuplicateDetailsQuery,
				new Object[] { username, email, SSN },
				new DuplicateValidationCheckerMapper());

		return duplicateCheckDetails;
	}

	public String creditToUserAccount(String usernameLoggedIn,
			Float creditAmountFloat) {
		String getCreditDetailsForCustomer = "SELECT accountbalance from account where"
				+ " username=?";

		JdbcTemplate jdbcTemplateForGettingCreditDetails = new JdbcTemplate(
				dataSource);
		float accountBalanceBeforeCredit = jdbcTemplateForGettingCreditDetails
				.queryForObject(getCreditDetailsForCustomer,
						new Object[] { usernameLoggedIn }, Float.class);
		float accountBalanceAfterCredit = accountBalanceBeforeCredit
				+ creditAmountFloat;

		// Generating random tx IDs
		Random randGenerator = new Random();
		int txID = 100000 + randGenerator.nextInt(800000);

		// Generating timestamp
		Calendar calendarForTx = Calendar.getInstance();
		Timestamp timeStampForTx = new Timestamp(calendarForTx.getTime()
				.getTime());

		// Updating the account table
		String updateAfterCredit = "UPDATE account SET accountBalance=?, credit=?"
				+ "WHERE username=?";

		// Get the accountNumber of the user
		String getAccountNumber = "SELECT accountnumber from account where username=?";
		JdbcTemplate jdbcTemplateToGetAccountNumber = new JdbcTemplate(
				dataSource);

		int accountNumber = jdbcTemplateToGetAccountNumber.queryForObject(
				getAccountNumber, new Object[] { usernameLoggedIn },
				Integer.class);

		// Insert into tx table
		String insertIntoTxTable = "INSERT INTO transactions(id, usernamefrom,"
				+ "usernameto, usernamefromaccountnumber, usernametoaccountnumber, transactiontype, userdelete,"
				+ "transactiondate) VALUES (?,?,?,?,?,?,?,?)";

		// Inserting
		JdbcTemplate insertIntoTxTableTemplate = new JdbcTemplate(dataSource);
		insertIntoTxTableTemplate.update(insertIntoTxTable, new Object[] {
				txID, usernameLoggedIn, usernameLoggedIn, accountNumber,
				accountNumber, "TX_CREDIT", false, timeStampForTx });

		jdbcTemplateForGettingCreditDetails.update(updateAfterCredit,
				new Object[] { accountBalanceAfterCredit, creditAmountFloat,
						usernameLoggedIn });
		return "Amount credited with: " + creditAmountFloat
				+ ". New balance is: " + accountBalanceAfterCredit;
	}

	public String modifyUserInformationRequest(String username,
			ModifyUserInformation modInfo) {
		try {
			String checkWhetherUserHasSubmittedRequest = "SELECT username from modificationrequests "
					+ "WHERE username=?";
			JdbcTemplate checkUsernameTemplate = new JdbcTemplate(dataSource);

			// This will throw an exception if the rows are NULL
			String userName = checkUsernameTemplate.queryForObject(
					checkWhetherUserHasSubmittedRequest,
					new Object[] { username }, String.class);

			if (!userName.isEmpty() && userName != null) {
				// Update the user
				String updateModificationRequests = "UPDATE modificationrequests SET "
						+ "firstname=?, lastname=?, sex=?, MerchantorIndividual=?, phonenumber=?,"
						+ "email=?, address=?, requestcompleted=? where username=?";

				JdbcTemplate jdbcTemplateToUpdate = new JdbcTemplate(dataSource);
				jdbcTemplateToUpdate.update(
						updateModificationRequests,
						new Object[] { modInfo.getFirstname(),
								modInfo.getLastname(), modInfo.getSex(),
								modInfo.getSelection(),
								modInfo.getPhonenumber(), modInfo.getEmail(),
								modInfo.getAddress(), false, username });

				return "Your request has been updated with this new information. It will be approved by the administrator or employee soon.";
			} else {
				// If the user isn't present, an exception would be raised
				// anyway
				// So ignoring this block
				return "This block will not get called.";
			}
		} catch (IncorrectResultSizeDataAccessException dataAccessException) {
			// Insert the user
			String insertIntoModifyRequestsTable = "INSERT INTO modificationrequests(username,"
					+ "firstname, lastname, sex, MerchantorIndividual, phonenumber, "
					+ "email,"
					+ "address, requestcompleted)	 VALUES (?,?,?,?,?,?,?,?,?)";
			JdbcTemplate insertIntoModifyRequestsTableTemplate = new JdbcTemplate(
					dataSource);
			insertIntoModifyRequestsTableTemplate.update(
					insertIntoModifyRequestsTable,
					new Object[] { username, modInfo.getFirstname(),
							modInfo.getLastname(), modInfo.getSex(),
							modInfo.getSelection(), modInfo.getPhonenumber(),
							modInfo.getEmail(), modInfo.getAddress(), false });
			String insertIntoTicketsTable = "INSERT into user_tickets(username, requestcompleted, requestapproved, requestrejected,requesttype)"
					+ " VALUES (?,?,?,?,?)";
			JdbcTemplate insertIntoTicketsTableTemplate = new JdbcTemplate(
					dataSource);

			insertIntoTicketsTableTemplate.update(insertIntoTicketsTable,
					new Object[] { username, false, false, false,
							Ticket_Type_Modify });

			return "Request submitted. The internal user or admin will approve your request.";
		}
	}

	public String removeAccountRequest(String username,
			boolean deleteAccountOrNot) {
		try {
			String getDeleteAccountColumnValueQuery = "SELECT deleteaccount FROM deleteaccount WHERE username=?";
			JdbcTemplate jdbcTemplateForDeleteAccountValue = new JdbcTemplate(
					dataSource);
			boolean deleteAccountValueInDB = jdbcTemplateForDeleteAccountValue
					.queryForObject(getDeleteAccountColumnValueQuery,
							new Object[] { username }, Boolean.class);

			if (!deleteAccountValueInDB) {
				if (deleteAccountOrNot) {
					String updateModificationRequests = "INSERT INTO deleteaccount (username, deleteaccount) VALUES (?,?)";
					JdbcTemplate updateTemplate = new JdbcTemplate(dataSource);
					updateTemplate.update(updateModificationRequests,
							new Object[] { username, deleteAccountOrNot });
					String insertIntoTicketsTable = "INSERT into user_tickets(username, requestcompleted, requestapproved, requestrejected, requesttype)"
							+ " VALUES (?,?,?,?,?)";
					JdbcTemplate insertIntoTicketsTableTemplate = new JdbcTemplate(
							dataSource);

					insertIntoTicketsTableTemplate.update(
							insertIntoTicketsTable, new Object[] { username,
									false, false, false, Ticket_Type_Delete });

					return "Request submitted. The internal user of admin will approve your request.";
				} else {
					return "You have selected No. Your account request has not been submitted for internal review.";
				}
			} else {
				return "You have already submitted a request for account deletion.";
			}
		} catch (EmptyResultDataAccessException dataAccessException) {
			// Meaning, the user has not asked for a deleteAccount request
			// Hence, they are not present in the deleteaccount table
			// So, insert them into the table
			if (deleteAccountOrNot) {
				String updateModificationRequests = "INSERT INTO deleteaccount (username, deleteaccount) VALUES (?,?)";
				JdbcTemplate updateTemplate = new JdbcTemplate(dataSource);
				updateTemplate.update(updateModificationRequests, new Object[] {
						username, deleteAccountOrNot });
				String insertIntoTicketsTable = "INSERT into user_tickets(username, requestcompleted, requestapproved, requestrejected, requesttype)"
						+ " VALUES (?,?,?,?,?)";
				JdbcTemplate insertIntoTicketsTableTemplate = new JdbcTemplate(
						dataSource);

				insertIntoTicketsTableTemplate.update(insertIntoTicketsTable,
						new Object[] { username, false, false, false,
								Ticket_Type_Delete });

				return "Request submitted. The internal user of admin will approve your request.";
			} else {
				return "You have selected No. Your account request has not been submitted for internal review.";
			}
		}
	}

	public String generateOTP(String emailUserInput) {
		try {
			// Check whether the emailUserInput is actually present in our table
			String checkUserEmail = "SELECT email from users where email=?";
			JdbcTemplate jdbcTemplateToCheckIfEmailExists = new JdbcTemplate(
					dataSource);

			String emailExistsOrNot = jdbcTemplateToCheckIfEmailExists
					.queryForObject(checkUserEmail,
							new Object[] { emailUserInput }, String.class);

			if (!emailExistsOrNot.isEmpty()) {
				// generate otp
				String sample = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,;:[]{}+-";
				String otp = "";
				Random rand = new Random();
				int randomNum = 0;
				for (int i = 0; i < 8; i++) {
					randomNum = rand.nextInt((71 - 0) + 1) + 0;
					otp += sample.substring(randomNum, randomNum + 1);
				}

				// Creating a timestamp object
				Calendar calendar = Calendar.getInstance();
				Timestamp timestampForOTP = new Timestamp(calendar.getTime()
						.getTime());

				String insertIntoOTPTable = "INSERT INTO onetimepasswords VALUES (?,?,?)";

				JdbcTemplate jdbcTemplateForInsertIntoOTPTable = new JdbcTemplate(
						dataSource);

				int status = jdbcTemplateForInsertIntoOTPTable.update(
						insertIntoOTPTable, new Object[] { emailUserInput, otp,
								timestampForOTP });
				String passwordResetLink = "https://cse545hybrid05.vlab.asu.edu/SecureBankingSystem/resetPassword";

				// Send the email to the user
				String emailSubject = "Fogot Password Instructions";
				String emailContent = "Your OTP is: " + otp + ". \n\n"
						+ "Please click this link - " + passwordResetLink
						+ " and enter the details to reset your password.";
				sendEmail(emailUserInput, emailSubject, emailContent);

				if (status == 1) {
					return "OTP has been sent to your email!";
				} else {
					return "An OTP cannot be sent to you at the time. Please try again later.";
				}
			} else {
				// Email does not exists
				return "This email account does not exists.";
			}
		} catch (IncorrectResultSizeDataAccessException dataAccessException) {
			// The user is not present in the onetimepasswords table
			// So, insert the user
			// generate otp
			String sample = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,;:[]{}+-";
			String otp = "";
			Random rand = new Random();
			int randomNum = 0;
			for (int i = 0; i < 8; i++) {
				randomNum = rand.nextInt((71 - 0) + 1) + 0;
				otp += sample.substring(randomNum, randomNum + 1);
			}

			String insertIntoOTPTable = "INSERT INTO onetimepasswords VALUES (?,?,?)";
			JdbcTemplate jdbcTemplateForInsertIntoOTPTable = new JdbcTemplate(
					dataSource);

			// Creating a timestamp object
			Calendar calendar = Calendar.getInstance();
			Timestamp timestampForOTP = new Timestamp(calendar.getTime()
					.getTime());

			int status = jdbcTemplateForInsertIntoOTPTable.update(
					insertIntoOTPTable, new Object[] { emailUserInput, otp,
							timestampForOTP });
			String passwordResetLink = "https://cse545hybrid05.vlab.asu.edu/SecureBankingSystem/resetPassword";

			// Send the email to the user
			String emailSubject = "Fogot Password Instructions";
			String emailContent = "Your OTP is: " + otp + ". \n\n"
					+ "Please click this link - " + passwordResetLink
					+ " and enter the details to reset your password.";
			sendEmail(emailUserInput, emailSubject, emailContent);

			if (status == 1) {
				return "OTP has been sent to your email!";
			} else {
				return "An OTP cannot be sent to you at the time. Please try again later.";
			}
		}
	}

	public boolean activateAccountRequest(String username) {
		String sql = "UPDATE users set enabled = true where enabled = false and userDetailsExpired=true and userDetailsExpired=true and username =  ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int status = jdbcTemplate.update(sql, new Object[] { username });
		if (status == 1) {
			return true;
		}
		return false;
	}

	public boolean deleteAccountRequest(String username) {

		String sql = "UPDATE users set enabled = false, userExpired=false, userLocked=false, userDetailsExpired=false where enabled = true and userExpired=true  and username =  ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int status = jdbcTemplate.update(sql, new Object[] { username });
		if (status == 1) {
			return true;
		}
		return false;
	}

	public boolean deleteAccountExternal(String username) {

		if (deleteAccountRequest(username)) {
			String sql = "UPDATE deleteaccount set deleteaccount = true where username =  ?";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			int status = jdbcTemplate.update(sql, new Object[] { username });
			String updateIntoTicketsTable = "UPDATE user_tickets set requestcompleted =true, requestapproved=true, requestrejected=false where username =  ?";
			JdbcTemplate insertIntoTicketsTableTemplate = new JdbcTemplate(
					dataSource);

			int status1 = insertIntoTicketsTableTemplate.update(
					updateIntoTicketsTable, new Object[] { username });
			if (status == 1 && status1 == 1) {
				return true;
			}
			return false;
		}
		return false;
	}

	// TODO make this list only return tickets for active users not the deleted
	// ones.
	public List<TicketInformationDTO> getPendingTicketList() {
		List<TicketInformationDTO> userList = new ArrayList<TicketInformationDTO>();

		String sql = "SELECT user_tickets.id, user_tickets.username, user_tickets.requestcompleted, user_tickets.requestapproved, user_tickets.requestrejected, user_tickets.requesttype "
				+ " from user_tickets inner join users on "
				+ "user_tickets.username=users.username where user_tickets.requestcompleted=false and user_tickets.requestrejected=false and"
				+ " user_tickets.requestapproved=false and users.enabled = true and users.userDetailsExpired=true and users.userDetailsExpired=true";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		userList = jdbcTemplate.query(sql, new TicketRowMapper());
		return userList;
	}

	// TODO make this list only return tickets for active users not the deleted
	// ones.
	public List<TicketInformationDTO> getApprovedTicketList() {
		List<TicketInformationDTO> userList = new ArrayList<TicketInformationDTO>();

		String sql = "SELECT user_tickets.id, user_tickets.username, user_tickets.requestcompleted, user_tickets.requestapproved, user_tickets.requestrejected, user_tickets.requesttype "
				+ " from user_tickets inner join users on "
				+ "user_tickets.username=users.username where user_tickets.requestcompleted=true and user_tickets.requestrejected=false and"
				+ " user_tickets.requestapproved=true";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		userList = jdbcTemplate.query(sql, new TicketRowMapper());
		return userList;
	}

	// TODO make this list only return tickets for active users not the deleted
	// ones.
	public List<TicketInformationDTO> getRejectedTicketList() {
		List<TicketInformationDTO> userList = new ArrayList<TicketInformationDTO>();

		String sql = "SELECT user_tickets.id, user_tickets.username, user_tickets.requestcompleted, user_tickets.requestapproved, user_tickets.requestrejected, user_tickets.requesttype "
				+ " from user_tickets inner join users on "
				+ "user_tickets.username=users.username where user_tickets.requestcompleted=true and user_tickets.requestrejected=true and"
				+ " user_tickets.requestapproved=false";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		userList = jdbcTemplate.query(sql, new TicketRowMapper());
		return userList;
	}

	// Transfer money module
	public boolean processtransfer(String generatedFromUsernameFrom,
			String account, String amount) {
		float amountToTransfer = Float.parseFloat(amount);
		CustomerInformationDTO custinfo = getUserFromAccount(account);
		String generatedFromUsernameTo = custinfo.getUsername();
		String balanceFromS = "SELECT account.accountbalance from account "
				+ " inner join users on " + "account.username=users.username "
				+ "WHERE account.username=?";
		String balanceToS = "SELECT account.accountbalance from account "
				+ " inner join users on " + "account.username=users.username "
				+ "WHERE account.username=?";
		String getAccountDetailsFromUsernameFrom = "SELECT account.accountnumber from account"
				+ " inner join users on "
				+ "account.username=users.username "
				+ "where account.username=?";
		String debitS = "SELECT account.debit from account "
				+ " inner join users on " + "account.username=users.username "
				+ "WHERE account.username=?";
		String creditS = "SELECT account.credit from account "
				+ " inner join users on " + "account.username=users.username "
				+ "WHERE account.username=?";
		String insertIntoAccountFrom = "UPDATE "
				+ "account SET account.accountbalance=?, account.debit=?"
				+ "WHERE account.username= ? ";
		String insertIntoAccountTo = "UPDATE "
				+ "account SET account.accountbalance=?, account.credit=?"
				+ "WHERE account.username= ? ";
		
		// Query for inserting into the transactions table
		String insertIntoTxTable = "INSERT INTO transactions(id, usernamefrom,"
				+ "usernameto, usernamefromaccountnumber, usernametoaccountnumber, transactiontype, userdelete,"
				+ "transactiondate) VALUES (?,?,?,?,?,?,?,?)";

		JdbcTemplate jdbcTemplateForAccountNumber = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplateForAccount = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplateToInsertIntoTxTable = new JdbcTemplate(dataSource);

		String getUsernameAccount = jdbcTemplateForAccountNumber
				.queryForObject(getAccountDetailsFromUsernameFrom,
						new Object[] { generatedFromUsernameFrom },
						String.class);

		String balanceFromString = jdbcTemplateForAccountNumber.queryForObject(
				balanceFromS, new Object[] { generatedFromUsernameFrom },
				String.class);

		String balanceToString = jdbcTemplateForAccountNumber.queryForObject(
				balanceToS, new Object[] { generatedFromUsernameTo },
				String.class);

		String debitString = jdbcTemplateForAccountNumber.queryForObject(
				debitS, new Object[] { generatedFromUsernameFrom },
				String.class);

		String creditString = jdbcTemplateForAccountNumber
				.queryForObject(creditS,
						new Object[] { generatedFromUsernameTo }, String.class);
		
		// Generating random tx IDs
		Random randGenerator = new Random();
		int txID = 100000 + randGenerator.nextInt(800000);
		
		// Get the current timestamp
		Calendar calendar = Calendar.getInstance();
		Timestamp currentTimestamp = new Timestamp(calendar.getTime()
				.getTime());
		
		// Inserting into tx table
		jdbcTemplateToInsertIntoTxTable.update(insertIntoTxTable, new Object[] {
		txID, generatedFromUsernameFrom, generatedFromUsernameTo,
		getUsernameAccount, account, "TX_DEBIT_CREDIT", false,
		currentTimestamp });

		float balanceFrom = Float.parseFloat(balanceFromString);
		float balanceTo = Float.parseFloat(balanceToString);
		float credit = Float.parseFloat(creditString);
		float debit = Float.parseFloat(debitString);
		balanceFrom = balanceFrom - amountToTransfer;
		balanceTo = balanceTo + amountToTransfer;
		if (balanceFrom < 0) {
			return false;
		}
		credit = credit + amountToTransfer;
		debit = debit + amountToTransfer;
		int accountNumber = Integer.parseInt(getUsernameAccount);
		int accountNumberFrom = Integer.parseInt(account);
		if (accountNumber == accountNumberFrom) {
			return false;
		}
		jdbcTemplateForAccount.update(insertIntoAccountFrom, new Object[] {
				balanceFrom, debit, generatedFromUsernameFrom });
		jdbcTemplateForAccount.update(insertIntoAccountTo, new Object[] {
				balanceTo, credit, generatedFromUsernameTo });
		return true;
	}

	public boolean updatePending(String generatedFromUsernameFrom,
			String account, String amount) {
		float amountToTransfer = Float.parseFloat(amount);
		CustomerInformationDTO custinfo = getUserFromAccount(account);
		String generatedFromUsernameTo = custinfo.getUsername();
		
		String balanceFromS = "SELECT account.accountbalance from account "
				+ " inner join users on " + "account.username=users.username "
				+ "WHERE account.username=?";
		String getAccountDetailsFromUsernameFrom = "SELECT account.accountnumber from account"
				+ " inner join users on "
				+ "account.username=users.username "
				+ "where account.username=?";
		String debitS = "SELECT account.debit from account "
				+ " inner join users on " + "account.username=users.username "
				+ "WHERE account.username=?";
		String insertIntoPendingFrom = "INSERT INTO pendingtransactions (username,amount,pending,accountnumberfrom,accountnumberto)"
				+ "VALUES((SELECT username from account where accountnumber=?),?,?,?,?)";
		String insertIntoAccountFrom = "UPDATE "
				+ "account SET account.accountbalance=?, account.debit=?"
				+ "WHERE account.username= ? ";
		// Query to insert into user_tickets table
		String insertIntoTicketsTable = "INSERT into user_tickets(username, requestcompleted, requestapproved, requestrejected,requesttype)"
				+ " VALUES (?,?,?,?,?)";
		
		// Query for inserting into the transactions table
		String insertIntoTxTable = "INSERT INTO transactions(id, usernamefrom,"
				+ "usernameto, usernamefromaccountnumber, usernametoaccountnumber, transactiontype, userdelete,"
				+ "transactiondate) VALUES (?,?,?,?,?,?,?,?)";		

		JdbcTemplate jdbcTemplateForAccountNumber = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplateForAccount = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplateForPending = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplateForUserTickets = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplateForTransactionsTable = new JdbcTemplate(dataSource);

		String getUsernameAccount = jdbcTemplateForAccountNumber
				.queryForObject(getAccountDetailsFromUsernameFrom,
						new Object[] { generatedFromUsernameFrom },
						String.class);
		String balanceFromString = jdbcTemplateForAccountNumber.queryForObject(
				balanceFromS, new Object[] { generatedFromUsernameFrom },
				String.class);
		String debitString = jdbcTemplateForAccountNumber.queryForObject(
				debitS, new Object[] { generatedFromUsernameFrom },
				String.class);
		
		// Generating random tx IDs
		Random randGenerator = new Random();
		int txID = 100000 + randGenerator.nextInt(800000);
		
		// Get the current timestamp
		Calendar calendar = Calendar.getInstance();
		Timestamp currentTimestamp = new Timestamp(calendar.getTime()
				.getTime());
		
		jdbcTemplateForTransactionsTable.update(insertIntoTxTable, new Object[] {
				txID, generatedFromUsernameFrom, generatedFromUsernameTo,
				getUsernameAccount, account, "TX_DEBIT_CREDIT", false,
				currentTimestamp });
				
		float balanceFrom = Float.parseFloat(balanceFromString);
		float debit = Float.parseFloat(debitString);
		balanceFrom = balanceFrom - amountToTransfer;
		debit = debit + amountToTransfer;
		if (balanceFrom < 0) {
			return false;
		}
		int accountNumber = Integer.parseInt(getUsernameAccount);
		int accountNumberTo = Integer.parseInt(account);
		if (accountNumber == accountNumberTo) {
			return false;
		}
		jdbcTemplateForPending.update(insertIntoPendingFrom, new Object[] {
				accountNumber, amountToTransfer, " 1", accountNumber,
				accountNumberTo });
		jdbcTemplateForAccount.update(insertIntoAccountFrom, new Object[] {
				balanceFrom, debit, generatedFromUsernameFrom });

		// Inserting into user_tickets
		jdbcTemplateForUserTickets.update(insertIntoTicketsTable, new Object[] {
				generatedFromUsernameFrom, false, false, false,
				Ticket_Type_Authorize });

		return true;
	}

	public List<UserTransactionsDTO> getUserTransactionList(String username) {
		String getTransactionsQuery = "SELECT * from transactions where usernamefrom=? AND userdelete=?";
		List<UserTransactionsDTO> getTransactionsList = new ArrayList<UserTransactionsDTO>();

		JdbcTemplate jdbcTemplateForTransactionsTable = new JdbcTemplate(
				dataSource);
		getTransactionsList = jdbcTemplateForTransactionsTable.query(
				getTransactionsQuery, new Object[] { username, false },
				new UserTransactionsMapper());
		return getTransactionsList;
	}

	// update the Tx table field to NULL
	public boolean deleteTransaction(int txID) {
		String updateInTxTable = "UPDATE transactions SET userdelete=? where id=?";
		JdbcTemplate updateTxTable = new JdbcTemplate(dataSource);
		int status = updateTxTable.update(updateInTxTable, new Object[] { true,
				txID });
		if (status == 1) {
			return true;
		} else {
			return false;
		}
	}

	public TicketDetailDTO fetchTicketDetail(TicketInformationDTO ticketDetails) {
		// TODO Auto-generated method stub
		List<TicketDetailDTO> customerInformationToDisplay = new ArrayList<TicketDetailDTO>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		if (ticketDetails.getRequesttype().equalsIgnoreCase(Ticket_Type_Modify)) {
			String retrieveDetailsQuery = "SELECT user_tickets.id, user_tickets.username, user_tickets.requestcompleted, user_tickets.requestapproved, user_tickets.requestrejected, user_tickets.requesttype , modificationrequests.firstname, modificationrequests.lastname, modificationrequests.sex, "
					+ "modificationrequests.MerchantorIndividual, modificationrequests.phonenumber, modificationrequests.email, "
					+ "modificationrequests.address, account.accountnumber, account.accountbalance, false  from user_tickets inner join modificationrequests on user_tickets.username=modificationrequests.username"
					+ " inner join account on modificationrequests.username = account.username inner join users on users.username = modificationrequests.username "
					+ " where user_tickets.requestcompleted=false and user_tickets.requestrejected=false and"
					+ " user_tickets.requestapproved=false and users.enabled = true and users.userDetailsExpired=true and users.userDetailsExpired=true and user_tickets.id=? and user_tickets.username=?";

			customerInformationToDisplay = jdbcTemplate.query(
					retrieveDetailsQuery, new Object[] { ticketDetails.getId(),
							ticketDetails.getUsername() },
					new TicketDetailMapper());
			return customerInformationToDisplay.get(0);
		} else if (ticketDetails.getRequesttype().equalsIgnoreCase(
				Ticket_Type_Delete)) {
			String retrieveDetailsQuery = "SELECT user_tickets.id, user_tickets.username, user_tickets.requestcompleted, user_tickets.requestapproved, user_tickets.requestrejected, user_tickets.requesttype , users.firstname, users.lastname, users.sex, "
					+ "users.MerchantorIndividual, users.phonenumber, users.email, "
					+ "users.address, account.accountnumber, account.accountbalance, deleteaccount.deleteaccount  from user_tickets inner join deleteaccount on user_tickets.username=deleteaccount.username"
					+ " inner join account on deleteaccount.username = account.username inner join users on users.username = deleteaccount.username "
					+ " where user_tickets.requestcompleted=false and user_tickets.requestrejected=false and"
					+ " user_tickets.requestapproved=false and users.enabled = true and users.userDetailsExpired=true and users.userDetailsExpired=true and user_tickets.id=? and user_tickets.username=?";

			customerInformationToDisplay = jdbcTemplate.query(
					retrieveDetailsQuery, new Object[] { ticketDetails.getId(),
							ticketDetails.getUsername() },
					new TicketDetailMapper());
			return customerInformationToDisplay.get(0);
		} else if (ticketDetails.getRequesttype().equalsIgnoreCase(
				Ticket_Type_Authorize)) {
			String retrieveDetailsQuery = "SELECT user_tickets.id, user_tickets.username, user_tickets.requestcompleted, user_tickets.requestapproved, user_tickets.requestrejected, user_tickets.requesttype , users.firstname, users.lastname, "
					+ " account.accountnumber, account.accountbalance, pendingtransactions.amount, pendingtransactions.accountnumberto, pendingtransactions.billpay,pendingtransactions.id  from user_tickets inner join pendingtransactions on user_tickets.username=pendingtransactions.username"
					+ " inner join account on pendingtransactions.username = account.username inner join users on users.username = pendingtransactions.username "
					+ " where user_tickets.requestcompleted=false and user_tickets.requestrejected=false and"
					+ " user_tickets.requestapproved=false and users.enabled = true and users.userDetailsExpired=true and users.userDetailsExpired=true and user_tickets.id=? and user_tickets.username=?";

			customerInformationToDisplay = jdbcTemplate.query(
					retrieveDetailsQuery, new Object[] { ticketDetails.getId(),
							ticketDetails.getUsername() },
					new AuthorizeTransactionMapper());
			return customerInformationToDisplay.get(0);
		}
		return null;
	}

	public boolean rejectAuthorizeTransactions(TicketDetailDTO ticketDetailDTO) {
		// TODO Auto-generated method stub

		String updatePendingtable = "UPDATE pendingtransactions  SET pending = false where username=? and pending=true and accountnumberfrom =? and accountnumberto= ? and id = ? ";
		JdbcTemplate updatePendingTableJDBC = new JdbcTemplate(dataSource);
		int status = updatePendingTableJDBC.update(
				updatePendingtable,
				new Object[] { ticketDetailDTO.getUsername(),
						ticketDetailDTO.getAccountNumber(),
						ticketDetailDTO.getToAccountNumber(),
						ticketDetailDTO.getPendingid() });
			String updateIntoTicketsTable = "UPDATE user_tickets set requestcompleted =true, requestapproved=false, requestrejected=true where username =  ? and id = ?";
			JdbcTemplate insertIntoTicketsTableTemplate = new JdbcTemplate(
					dataSource);

			int status1 = insertIntoTicketsTableTemplate.update(
					updateIntoTicketsTable,
					new Object[] { ticketDetailDTO.getUsername(),ticketDetailDTO.getId() });
			if (status1 == 1) {
				float newBalance = ticketDetailDTO.getAccountBalance() + ticketDetailDTO.getTransactionamountInfloat();
				// Get the accountNumber of the user
				String getAccountNumber = "SELECT debit from account where username=?";
				JdbcTemplate jdbcTemplateToGetAccountNumber = new JdbcTemplate(
						dataSource);

				float debit = jdbcTemplateToGetAccountNumber.queryForObject(
						getAccountNumber, new Object[] { ticketDetailDTO.getUsername() },
						Float.class);
				float newDebit= debit - ticketDetailDTO.getTransactionamountInfloat();
				 
				String update = "UPDATE account set accountbalance= ? and debit = ? where username = ?";
				JdbcTemplate updateaccountTableJDBC = new JdbcTemplate(dataSource);
				int status2 = updateaccountTableJDBC.update(
						update,
						new Object[] { newBalance, newDebit,ticketDetailDTO.getUsername()});
				if(status2>0){
					return true;
				}
				else{
					return false;
				}
			
		}
			return false;
	}

	public boolean approveAuthorizeTransactions(TicketDetailDTO ticketDetailDTO) {
		// TODO Auto-generated method stub
		String updatePendingtable = "UPDATE pendingtransactions  SET pending = false where username=? and pending=true and accountnumberfrom =? and accountnumberto= ? and id = ? ";
		JdbcTemplate updatePendingTableJDBC = new JdbcTemplate(dataSource);
		int status = updatePendingTableJDBC.update(
				updatePendingtable,
				new Object[] { ticketDetailDTO.getUsername(),
						ticketDetailDTO.getAccountNumber(),
						ticketDetailDTO.getToAccountNumber(),
						ticketDetailDTO.getPendingid() });
		
		JdbcTemplate jdbcTemplateForAccountNumber1 = new JdbcTemplate(dataSource);
		String accountSQL = "SELECT account.accountnumber from account "
				+ " inner join users on " + "account.username=users.username "
				+ "WHERE account.username=?";
		String account = jdbcTemplateForAccountNumber1.queryForObject(
				accountSQL, new Object[] { ticketDetailDTO.getUsername() },
				String.class);
		
		float amountToTransfer = Float.parseFloat(ticketDetailDTO.getTransactionAmount());
		CustomerInformationDTO custinfo = getUserFromAccount(account);
		String generatedFromUsernameTo = custinfo.getUsername();
		
		
		
		String getAccountDetailsFromUsernameFrom = "SELECT account.accountnumber from account"
				+ " inner join users on "
				+ "account.username=users.username "
				+ "where account.username=?";
		String debitS = "SELECT account.debit from account "
				+ " inner join users on " + "account.username=users.username "
				+ "WHERE account.username=?";
		String insertIntoAccountFrom = "UPDATE "
				+ "account SET account.accountbalance=?, account.debit=?"
				+ "WHERE account.username= ? ";
		
		String balanceFromS = "SELECT account.accountbalance from account "
				+ " inner join users on " + "account.username=users.username "
				+ "WHERE account.username=?";
			

		JdbcTemplate jdbcTemplateForAccountNumber = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplateForAccount = new JdbcTemplate(dataSource);

		String getUsernameAccount = jdbcTemplateForAccountNumber
				.queryForObject(getAccountDetailsFromUsernameFrom,
						new Object[] { ticketDetailDTO.getUsername() },
						String.class);
		String balanceFromString = jdbcTemplateForAccountNumber.queryForObject(
				balanceFromS, new Object[] { ticketDetailDTO.getUsername() },
				String.class);
		String debitString = jdbcTemplateForAccountNumber.queryForObject(
				debitS, new Object[] { ticketDetailDTO.getUsername() },
				String.class);
		
		
				
		float balanceFrom = Float.parseFloat(balanceFromString);
		float debit = Float.parseFloat(debitString);
		balanceFrom = balanceFrom + amountToTransfer;
		debit = debit - amountToTransfer;
		if (balanceFrom < 0) {
			return false;
		}
		
		jdbcTemplateForAccount.update(insertIntoAccountFrom, new Object[] {
				balanceFrom, debit, ticketDetailDTO.getUsername() });
		
		if (processtransfer(ticketDetailDTO.getUsername(),
				ticketDetailDTO.getToAccountNumber(),
				ticketDetailDTO.getTransactionAmount())) {
			String updateIntoTicketsTable = "UPDATE user_tickets set requestcompleted =true, requestapproved=true, requestrejected=false where username =  ? and id = ?";
			JdbcTemplate insertIntoTicketsTableTemplate = new JdbcTemplate(
					dataSource);

			int status1 = insertIntoTicketsTableTemplate.update(
					updateIntoTicketsTable,
					new Object[] { ticketDetailDTO.getUsername(),
							ticketDetailDTO.getId() });
			return true;
		}

		else
			return false;

	}

	// Reset Passwords using OTP
	public String resetPassword(String email, String otp, String newPassword,
			String confirmNewPassword) {

		// To hash passwords
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String checkForEmail = "SELECT email FROM users WHERE email=?";
		JdbcTemplate jdbcTemplateForCheckingEmail = new JdbcTemplate(dataSource);

		String checkEmail = jdbcTemplateForCheckingEmail.queryForObject(
				checkForEmail, new Object[] { email }, String.class);

		if (!checkEmail.isEmpty()) {
			// User exists
			String checkOTPAssociatedWithEmail = "SELECT otp from onetimepasswords where email=?";
			JdbcTemplate checkOTP = new JdbcTemplate(dataSource);

			// Execute query
			String OTP = checkOTP.queryForObject(checkOTPAssociatedWithEmail,
					new Object[] { email }, String.class);

			// Get the current timestamp
			Calendar calendar = Calendar.getInstance();
			Timestamp currentTimestamp = new Timestamp(calendar.getTime()
					.getTime());

			// Get the timestamp stored in the table
			String getTimeStampFromDB = "SELECT dateandtime from onetimepasswords WHERE email=?";
			JdbcTemplate templateToGetTimeStamp = new JdbcTemplate(dataSource);

			Timestamp storedTimestamp = templateToGetTimeStamp
					.queryForObject(getTimeStampFromDB, new Object[] { email },
							Timestamp.class);

			// Get the times
			Long currentTimeStampInMilliSeconds = currentTimestamp.getTime();
			Long storedTimeStampInMillSeconds = storedTimestamp.getTime();

			Long differenceBetweenTimeStamps = currentTimeStampInMilliSeconds
					- storedTimeStampInMillSeconds;

			// Difference
			Long differenceInHours = differenceBetweenTimeStamps
					/ (60 * 60 * 1000);
			System.out.println(differenceInHours);

			// Check OTP
			if ((differenceInHours > 1)) {
				String removeFromOTPTable = "DELETE FROM onetimepasswords where email=?";

				JdbcTemplate jdbcTemplateToRemove = new JdbcTemplate(dataSource);

				jdbcTemplateToRemove.update(removeFromOTPTable,
						new Object[] { email });
				return "The OTP has expired. Please request another OTP.";
			} else {
				if (otp.equals(OTP) && newPassword.equals(confirmNewPassword)) {
					// Update the password in users table
					String updatePasswordsQuery = "UPDATE users SET password=?,confirmpassword=? WHERE email=?";
					String deleteFromOTPTable = "DELETE FROM onetimepasswords where email=?";

					JdbcTemplate updatePasswordTemplate = new JdbcTemplate(
							dataSource);
					JdbcTemplate deleteOTPTemplate = new JdbcTemplate(
							dataSource);

					// Call the hash function
					String newPasswordHash = passwordEncoder
							.encode(newPassword);
					String confirmNewPasswordHash = passwordEncoder
							.encode(confirmNewPassword);

					updatePasswordTemplate.update(updatePasswordsQuery,
							new Object[] { newPasswordHash,
									confirmNewPasswordHash, email });
					deleteOTPTemplate.update(deleteFromOTPTable,
							new Object[] { email });

					return "Your password has been updated.";
				} else {
					return "Please enter the correct OTP.";
				}
			}
		} else {
			return "Your password could not be updated. OTP expired or you have entered incorrect OTP.";
		}
	}
	
	// Check whether a username is a merchant or individual
	public boolean returnSelectionType(String username) {
		String getSelectionType = "SELECT MerchantorIndividual from users WHERE username=?";
		JdbcTemplate templateForSelectionType = new JdbcTemplate(dataSource);
		
		String getSelection = templateForSelectionType.queryForObject(getSelectionType, 
				new Object[] {username}, String.class);
		
		if (getSelection.equalsIgnoreCase("Merchant")) {
			return true;
		} else {
			return false;
		}
	}

	public String approveBillPay(String merchantUsername, String individualUsername) {
		// Set the billpay column as 1 
		String checkPendingTransactionsTableForBillPay = "UPDATE pendingtransactions "
				+ "SET billpay=false WHERE username=?";
		JdbcTemplate jdbcTemplateForApproveBillPay = new JdbcTemplate(dataSource);
		jdbcTemplateForApproveBillPay.update(checkPendingTransactionsTableForBillPay, new Object[] {merchantUsername});
		
		// Call the processtransfer and updatependingmethods
		// Get the other persons' account information
		String checkMerchants = "SELECT accountnumber from account where username=?";
		String checkAmount = "SELECT amount from pendingtransactions where username=?";
		
		JdbcTemplate getAccountNumberFromDB = new JdbcTemplate(dataSource);
		JdbcTemplate getAmountFromDB = new JdbcTemplate(dataSource);
		
		String accountNumberOfMerchant = getAccountNumberFromDB.queryForObject(checkMerchants, 
				new Object[] {merchantUsername}, String.class);
		List<String> amountToBePaid = getAmountFromDB.query(checkAmount,
				new Object[] {merchantUsername}, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
			}});
		
		for (String amount : amountToBePaid) {
			processtransfer(individualUsername, accountNumberOfMerchant, 
					amount);
			
			Float amountInFloat = Float.parseFloat(amount);
			if (amountInFloat >= 10000) {
				updatePending(individualUsername, accountNumberOfMerchant,
						amount);
			} else {
				// Nothing
			}
		}
		return "Processed Bill Pay.";
	}

	public boolean authorizeDeauthorizeDao(String userName) {
		try{
			String authorizedQuery = "SELECT authorized from usersPII where username=?";
		    JdbcTemplate jdbc = new JdbcTemplate(
					dataSource);
		    byte  status = jdbc.queryForObject(authorizedQuery, new Object[] { userName },Byte.class);
			if (status==1) {
				return true;
			} else {
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	public boolean authorizeDeauthorizeRequestDao(String userName) {
	    JdbcTemplate jdbcRequest = new JdbcTemplate(
				dataSource);
	    
		try{
			String authorizedQuery = "SELECT authorized from usersPII where username=?";
		    JdbcTemplate jdbc = new JdbcTemplate(
					dataSource);
		    boolean  status = jdbc.queryForObject(authorizedQuery, new Object[] { userName },Boolean.class);
			if (status) {
				String authorizedRequestQuery = "UPDATE usersPII SET authorized=false where username= ?";
			    int  status1 = jdbcRequest.update(authorizedRequestQuery, new Object[] { userName });
				if (status1>0) {
					return false;
				}
			} else {
				String authorizedRequestQuery = "UPDATE usersPII SET authorized=true where username= ?";
				int  status1 = jdbcRequest.update(authorizedRequestQuery, new Object[] { userName });
				if (status1>0) {
					return true;
				}
			}
		}catch(Exception e){
			String authorizedRequestQuery = "INSERT into usersPII (username,authorized) Values (?, ?)";
			int  status1 = jdbcRequest.update(authorizedRequestQuery, new Object[] { userName,true });
			if (status1>0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	// This method will return all the external users which are active, and have opted PII
		// is used in admin.
	public List<CustomerInformationDTO> getUserPIIList() {
		List<CustomerInformationDTO> userList = new ArrayList<CustomerInformationDTO>();

		String sql = "SELECT users.username, users.firstname, users.lastname, users.sex, users.MerchantorIndividual, "
				+ " users.phonenumber, users.email, users.address, users.SSN from users inner join "
				+ "user_roles on users.username = user_roles.username inner join usersPII on users.username=usersPII.username where users.enabled = true and users.userDetailsExpired=true and users.userDetailsExpired=true and user_roles.role='ROLE_USER' and usersPII.authorized=true";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		userList = jdbcTemplate.query(sql, new UserWithSSNRowMapper());
		return userList;
	}
}