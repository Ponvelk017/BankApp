package customIO;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Logger;

import customLogics.AccountFunctions;
import customLogics.BranchFunction;
import customLogics.CustomerFunctions;
import customLogics.TransactionFunctions;
import customLogics.UserFunctions;
import details.AccountDetails;
import details.BranchDetails;
import details.CustomerDetails;
import details.TransactionDetails;
import utility.Common;
import utility.InvalidInputException;

public class EmployeeIO {

	private static Logger logger = Bankapp.logger;

	public void employeeActions() throws InvalidInputException {

		AccountDetails accountDetails = new AccountDetails();
		CustomerDetails customerDet = new CustomerDetails();
		TransactionDetails transactionDetails = new TransactionDetails();
		UserFunctions userFunctions = new UserFunctions();
		AccountFunctions accountFunctions = new AccountFunctions();
		CustomerFunctions customerFunction = new CustomerFunctions();
		TransactionFunctions transactionFunctions = new TransactionFunctions();
		BranchFunction branchFunction = new BranchFunction();

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

//		int employeeId = Bankapp.userIdThread.get();
		boolean breakCondition = true;
		while (breakCondition) {
			logger.info("-" + "-".repeat(40) + "-");
			logger.info("Select a Option to Proceed : ");
			logger.info(String.format("%10s", "1.Add a Customer"));
			logger.info(String.format("%10s", "2.Add a Account to a User"));
			logger.info(String.format("%10s", "3.Update the Details of a Customer"));
			logger.info(String.format("%10s", "4.Deposit for a Account"));
			logger.info(String.format("%10s", "5.Withdraw for a Account"));
			logger.info(String.format("%10s", "6.Delete a Customer"));
			logger.info(String.format("%10s", "7.view Transactions of a User"));
			logger.info(String.format("%10s", "8.Make a User Active/Inactive"));
			logger.info(String.format("%10s", "9.Block/Unblock a Account"));
			logger.info(String.format("%10s", "10.View all (Active/Inactive) User"));
			logger.info(String.format("%10s", "11.View all (Block/Unblock) Account"));
			logger.info(String.format("%10s", "12.Viwe a Branch Details"));
			logger.info(String.format("%8s", "13.Log Out"));
			logger.info("-" + "-".repeat(40) + "-");
			String tempint = scanner.next();
			if (!tempint.matches("\\d+")) {
				logger.warning("Invalid Input");
				continue;
			}
			int option = Integer.parseInt(tempint);
			switch (option) {
			case 1: {
				logger.info("How many Customer are you going to add");
				int customerCount = scanner.nextInt();
				int loop = 0;
				List<CustomerDetails> customer = new ArrayList<CustomerDetails>();
				while (loop < customerCount) {
					logger.info("Enter the Customer \nName");
					customerDet.setName(scanner.next());
					logger.info("DOB");
					customerDet.setDOB(Common.dateToMilli(scanner.next()));
					logger.info("Mobile");
					customerDet.setMobile(scanner.next());
					logger.info("Email");
					customerDet.setEmail(scanner.next());
					logger.info("Gender");
					customerDet.setGender(scanner.next());
					logger.info("Password");
					customerDet.setPassword(scanner.next());
					logger.info("Address");
					scanner.nextLine();
					customerDet.setAddress(scanner.nextLine());
					logger.info("Aadhar Number");
					customerDet.setAadhar(scanner.next());
					logger.info("PAN Number");
					customerDet.setPan(scanner.next());
					customer.add(customerDet);
				}
				int affectedRows = customerFunction.addCustomer(customer);
				if (affectedRows == -1) {
					logger.warning("Invalid Input");
					break;
				} else if (affectedRows > 0) {
					logger.info("Successfully inserted");
				} else {
					logger.info("insertion was UnSuccessful");
				}
				if (customerCount != 1) {
					break;
				}
			}
			case 2: {
				logger.info("Enter the details to create a account to Customer \nUserId");
				scanner.nextLine();
				accountDetails.setUserId(scanner.nextInt());
				logger.info("Branch Id");
				accountDetails.setBranchId(scanner.next());
				logger.info("Account Type \n 1.Saving \n 2.Current \n 3.Salary");
				int accTypeOption = scanner.nextInt();
				if (accTypeOption == 1) {
					accountDetails.setAccountType("Saving");
				} else if (accTypeOption == 2) {
					accountDetails.setAccountType("Current");
				} else {
					accountDetails.setAccountType("Salary");
				}
				int affectedRow = accountFunctions.addAccount(accountDetails);
				if (affectedRow == -1) {
					logger.warning("A Account with the same AccountType and BranchId preaent");
				} else if (affectedRow > 0) {
					logger.info("Account created");
				} else {
					logger.info("Account creation was UnSuccessful");
				}
			}
				break;
			case 3: {
				logger.info("Enter the details to update the details of the Customer \nUserId");
				scanner.nextLine();
				int customerId = scanner.nextInt();
				logger.info("Enter which detail you need to change");
				String columntoChange = scanner.next();
				logger.info("Enter the value");
				scanner.nextLine();
				String value = scanner.nextLine();
				int affectedRow = customerFunction.updateCustomer(customerId, columntoChange, value);
				if (affectedRow > 0) {
					logger.info("Updated Successfully");
				} else {
					logger.info("Update was UnSuccessful");
				}
			}
				break;
			case 4: {
				logger.info("Enter the Account Number: ");
				long primaryAccount = scanner.nextLong();
				logger.info("Enter the Amount to Deposit : ");
				long depositeAmount = scanner.nextLong();
				long transactionId = transactionFunctions.newDeposite(primaryAccount, depositeAmount);
				transactionDetails = transactionFunctions.getTransactionDetails(transactionId, "Id");
				if (transactionDetails != null) {
					logger.info("Successfully deposited :)\nYour Transaction statement is");
					logger.severe("-" + "-".repeat(40) + "-");
					logger.severe(String.format("%-20s", "Transaction Id")
							+ String.format("%-20s", transactionDetails.getId()));
					logger.severe(String.format("%-20s", "To ")
							+ String.format("%-20s", transactionDetails.getTransactionAccountId()));
					logger.severe(String.format("%-20s", "Transaction Time")
							+ String.format("%-20s", new Date(transactionDetails.getTransactionTime()).toString()));
					logger.severe(String.format("%-20s", "Transaction type")
							+ String.format("%-20s", transactionDetails.getTransactionType()));
					logger.severe(String.format("%-20s", "Transaction Status")
							+ String.format("%-20s", transactionDetails.getStatus()));
					logger.severe(String.format("%-20s", "Transaction Amount")
							+ String.format("%-20s", transactionDetails.getAmount()));
					logger.severe(String.format("%-20s", "Closing Balance")
							+ String.format("%-20s", transactionDetails.getClosingBalance()));
				} else {
					logger.warning("Sorry , Something went wrong");
				}
			}
				break;
			case 5: {
				logger.info("Enter the Account Number: ");
				long primaryAccount = scanner.nextLong();
				logger.info("Enter the Amount to withdraw :");
				long withdrawAmount = scanner.nextLong();
				logger.info("Enter Description");
				scanner.nextLine();
				String description = scanner.nextLine();
				long transactionId = transactionFunctions.newWithdraw(primaryAccount, withdrawAmount, description);
				transactionDetails = transactionFunctions.getTransactionDetails(transactionId, "Id");
				if (transactionDetails != null) {
					logger.info("Successfully Withdrawed :)\nYour Transaction statement is");
					logger.severe("-" + "-".repeat(40) + "-");
					logger.severe(String.format("%-20s", "Transaction Id")
							+ String.format("%-20s", transactionDetails.getId()));
					logger.severe(String.format("%-20s", "From ")
							+ String.format("%-20s", transactionDetails.getAccountId()));
					logger.severe(String.format("%-20s", "Transaction Time")
							+ String.format("%-20s", new Date(transactionDetails.getTransactionTime()).toString()));
					logger.severe(String.format("%-20s", "Transaction type")
							+ String.format("%-20s", transactionDetails.getTransactionType()));
					logger.severe(String.format("%-20s", "Description")
							+ String.format("%-20s", transactionDetails.getDescription()));
					logger.severe(String.format("%-20s", "Transaction Status")
							+ String.format("%-20s", transactionDetails.getStatus()));
					logger.severe(String.format("%-20s", "Transaction Amount")
							+ String.format("%-20s", transactionDetails.getAmount()));
					logger.severe(String.format("%-20s", "Closing Balance")
							+ String.format("%-20s", transactionDetails.getClosingBalance()));
				} else {
					logger.warning("Sorry , Something went wrong");
				}
			}
				break;
			case 6: {
				logger.info("Enter the Customer Id to Delete(make User account Inactive) : ");
				scanner.nextLine();
				int userIdToDelete = scanner.nextInt();
				String isCustomer = userFunctions.getType(userIdToDelete);
				if (isCustomer.equals("Customer")) {
					int affectedRows = userFunctions.deleteUser(userIdToDelete);
					if (affectedRows > 0) {
						logger.info("Deleted Successfully");
					} else {
						logger.info("Deleted was Unsuccessful");
					}
				} else {
					logger.warning("The Entered Id is not a Customer");
				}
			}
				break;
			case 7: {
				logger.info("1. View all Transacions Of the User");
				logger.info("2. View Single Transacions");
				logger.info("3. View transaction of a Account");
				logger.info("4. View (Deposit/Withdraw/Both)transaction of a Account over a Period");
				int transactionOption = scanner.nextInt();
				switch (transactionOption) {
				case 1: {
					logger.info("Enter the User Id to Proceed");
					List<TransactionDetails> records = transactionFunctions.accountStatement(30, scanner.nextInt(), 0);
					for (TransactionDetails singleTransaction : records) {
						logger.severe("-" + "-".repeat(40) + "-");
						if (singleTransaction.getAccountId() > 0) {
							logger.severe(String.format("%-20s", "From ")
									+ String.format("%-20s", singleTransaction.getAccountId()));
						} else if (singleTransaction.getTransactionAccountId() > 0) {
							logger.severe(String.format("%-20s", "To ")
									+ String.format("%-20s", singleTransaction.getTransactionAccountId()));
						} else {
							logger.severe(String.format("%-20s", "From ")
									+ String.format("%-20s", singleTransaction.getAccountId()));
							logger.severe(String.format("%-20s", "To ")
									+ String.format("%-20s", singleTransaction.getTransactionAccountId()));
						}
						logger.severe(String.format("%-20s", "Transaction Time")
								+ String.format("%-20s", new Date(singleTransaction.getTransactionTime()).toString()));
						logger.severe(String.format("%-20s", "Transaction type")
								+ String.format("%-20s", singleTransaction.getTransactionType()));
						logger.severe(String.format("%-20s", "Transaction Status")
								+ String.format("%-20s", singleTransaction.getStatus()));
						logger.severe(String.format("%-20s", "Transaction Amount")
								+ String.format("%-20s", singleTransaction.getAmount()));
						logger.severe(String.format("%-20s", "Closing Balance")
								+ String.format("%-20s", singleTransaction.getClosingBalance()));
					}
				}
					break;
				case 2: {
					logger.info("Enter the Transaction Id to Proceed ");
					List<TransactionDetails> record = transactionFunctions
							.getSingleTransactionDetails(scanner.nextLong(), "Id");
					for (TransactionDetails records : record) {
						logger.severe("-" + "-".repeat(40) + "-");
						logger.severe(
								String.format("%-20s", "Transaction Id") + String.format("%-20s", records.getId()));
						logger.severe(String.format("%-20s", "From ") + String.format("%-20s", records.getAccountId()));
						logger.severe(String.format("%-20s", "To ")
								+ String.format("%-20s", records.getTransactionAccountId()));
						logger.severe(String.format("%-20s", "Transaction Time")
								+ String.format("%-20s", new Date(records.getTransactionTime()).toString()));
						logger.severe(String.format("%-20s", "Transaction type")
								+ String.format("%-20s", records.getTransactionType()));
						logger.severe(String.format("%-20s", "Transaction Status")
								+ String.format("%-20s", records.getStatus()));
						logger.severe(String.format("%-20s", "Transaction Amount")
								+ String.format("%-20s", records.getAmount()));
						logger.severe(String.format("%-20s", "Closing Balance")
								+ String.format("%-20s", records.getClosingBalance()));
					}
				}
					break;
				case 3: {
					logger.info("Enter the Accoutn Number to Proceed ");
					List<TransactionDetails> record = transactionFunctions
							.getSingleTransactionDetails(scanner.nextLong(), "AccountId");
					for (TransactionDetails records : record) {
						logger.severe("-" + "-".repeat(40) + "-");
						logger.severe(
								String.format("%-20s", "Transaction Id") + String.format("%-20s", records.getId()));
						logger.severe(String.format("%-20s", "From ") + String.format("%-20s", records.getAccountId()));
						logger.severe(String.format("%-20s", "To ")
								+ String.format("%-20s", records.getTransactionAccountId()));
						logger.severe(String.format("%-20s", "Transaction Time")
								+ String.format("%-20s", new Date(records.getTransactionTime()).toString()));
						logger.severe(String.format("%-20s", "Transaction type")
								+ String.format("%-20s", records.getTransactionType()));
						logger.severe(String.format("%-20s", "Transaction Status")
								+ String.format("%-20s", records.getStatus()));
						logger.severe(String.format("%-20s", "Transaction Amount")
								+ String.format("%-20s", records.getAmount()));
						logger.severe(String.format("%-20s", "Closing Balance")
								+ String.format("%-20s", records.getClosingBalance()));
					}
				}
					break;
				case 4: {
					logger.info("Enter the Account Number");
					long accountNumber = scanner.nextLong();
					logger.info("Enter the Time Period\nFrom");
					long from = Common.dateToMilli(scanner.next());
					logger.info("To");
					String tempTo = scanner.next();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
					LocalDate inputDate = LocalDate.parse(tempTo, formatter);
					LocalDate currentDate = LocalDate.now();
					int daysDifference = (int) ChronoUnit.DAYS.between(inputDate, currentDate);
					long to = Common.beforeNDate(daysDifference);
					logger.info("Enter the transaction type : \n1.Deposit\n2.Withdraw\n3.Both");
					int transactionChoice = scanner.nextInt();
					logger.info("1.Give Latest\n2.Give Earilest");
					String sortOption = scanner.next();
					if (!sortOption.matches("\\d+") && sortOption.contains("1 2")) {
						logger.warning("Invalid Input");
						continue;
					}
					Map<String, Object> conditions = new HashMap<String, Object>();
					conditions.put("From", from);
					conditions.put("To", to);
					conditions.put("Sort", (sortOption.equals("1")) ? "desc" : "asce");
					List<String> columnToGet = new ArrayList<>();
					columnToGet.add("Id");
					columnToGet.add("AccountId");
					columnToGet.add("TransactionAccountId");
					columnToGet.add("TransactionTime");
					columnToGet.add("TransactionType");
					columnToGet.add("UserId");
					columnToGet.add("Description");
					columnToGet.add("Status");
					columnToGet.add("Amount");
					columnToGet.add("ClosingBalance");
					transactionDetails.setAccountId(accountNumber);
					transactionDetails.setTransactionType((transactionChoice == 1) ? "Deposit" : "Withdraw");
					List<TransactionDetails> record = transactionFunctions.getCustomDetails(transactionDetails,
							columnToGet, conditions);
					for (TransactionDetails records : record) {
						logger.severe("-" + "-".repeat(40) + "-");
						logger.severe(
								String.format("%-20s", "Transaction Id") + String.format("%-20s", records.getId()));
						logger.severe(String.format("%-20s", "From ") + String.format("%-20s", records.getAccountId()));
						logger.severe(String.format("%-20s", "To ")
								+ String.format("%-20s", records.getTransactionAccountId()));
						logger.severe(String.format("%-20s", "Transaction Time")
								+ String.format("%-20s", new Date(records.getTransactionTime()).toString()));
						logger.severe(String.format("%-20s", "Transaction type")
								+ String.format("%-20s", records.getTransactionType()));
						logger.severe(String.format("%-20s", "Transaction Status")
								+ String.format("%-20s", records.getStatus()));
						logger.severe(String.format("%-20s", "Transaction Amount")
								+ String.format("%-20s", records.getAmount()));
						logger.severe(String.format("%-20s", "Closing Balance")
								+ String.format("%-20s", records.getClosingBalance()));
					}
				}
					break;
				}
			}
				break;
			case 8: {
				logger.info("1. Make Customer Active \n2. Make User Inactive");
				int activeOption = scanner.nextInt();
				logger.info("Enter the User Id");
				int userId = scanner.nextInt();
				switch (activeOption) {
				case 1: {
					int affectedRows = userFunctions.coloumnUpdation("Status", "1", userId);
					if (affectedRows > 0) {
						logger.info("User Activated");
					} else {
						logger.info("Something Went Worng , Try Again");
					}
				}
					break;
				case 2: {
					int affectedRows = userFunctions.coloumnUpdation("Status", "0", userId);
					if (affectedRows > 0) {
						logger.info("User Inactivated");
					} else {
						logger.info("Something Went Worng , Try Again");
					}
				}
					break;
				}
			}
				break;

			case 9: {
				logger.info("1. Make Account Active \n2. Make Account Inactive");
				int activeOption = scanner.nextInt();
				logger.info("Enter the Account Number");
				long account = scanner.nextLong();
				switch (activeOption) {
				case 1: {
					int affectedRows = accountFunctions.updateColoumn("Status", "1", account);
					if (affectedRows > 0) {
						logger.info("Account Activated");
					} else {
						logger.info("Something Went Worng , Try Again");
					}
				}
					break;
				case 2: {
					int affectedRows = accountFunctions.updateColoumn("Status", "0", account);
					if (affectedRows > 0) {
						logger.info("Account Inactivated");
					} else {
						logger.info("Something Went Worng , Try Again");
					}
				}
					break;
				}
			}
				break;
			case 10: {
				logger.info("Which User Details do you need ?\n1.Active\n2.Inactive");
				String statusOption = scanner.next();
				CustomerDetails customerStatus = new CustomerDetails();
				Map<Integer, CustomerDetails> activeUsers;
				if (statusOption.equals("1")) {
					customerStatus.setStatus("1");
					activeUsers = customerFunction.getCustomerProfile(customerStatus);
				} else if (statusOption.equals("2")) {
					customerStatus.setStatus("0");
					activeUsers = customerFunction.getCustomerProfile(customerStatus);
				} else {
					logger.warning("Invalid Option");
					continue;
				}
				for (Entry<?, ?> individualRecord : activeUsers.entrySet()) {
					customerDet = (CustomerDetails) individualRecord.getValue();
					logger.severe("-" + "-".repeat(40) + "-");
					logger.severe(String.format("%-15s", "Name") + String.format("%-15s", customerDet.getName()));
					logger.severe(String.format("%-15s", "DOB") + String.format("%-15s", customerDet.getDOB()));
					logger.severe(
							String.format("%-15s", "Mobile Number") + String.format("%-15s", customerDet.getMobile()));
					logger.severe(String.format("%-15s", "Email") + String.format("%-15s", customerDet.getEmail()));
					logger.severe(String.format("%-15s", "Gender") + String.format("%-15s", customerDet.getGender()));
					logger.severe(String.format("%-15s", "Status") + String.format("%-15s", customerDet.getStatus()));
					logger.severe(String.format("%-15s", "Aadhar") + String.format("%-15s", customerDet.getAadhar()));
					logger.severe(String.format("%-15s", "PAN Number") + String.format("%-15s", customerDet.getPan()));
					logger.severe(String.format("%-15s", "Address") + String.format("%-15s", customerDet.getAddress()));
					logger.severe("-" + "-".repeat(40) + "-");
				}
			}
				break;
			case 11: {
				logger.info("Which Account Details do you need ?\n1.Active\n2.Inactive");
				String statusOption = scanner.next();
				AccountDetails accountDet = new AccountDetails();
				Map<Long, AccountDetails> accountData;
				if (statusOption.equals("1")) {
					accountDet.setStatus("1");
					accountData = accountFunctions.accountDetails(accountDet);
				} else if (statusOption.equals("2")) {
					accountDet.setStatus("0");
					accountData = accountFunctions.accountDetails(accountDet);
				} else {
					logger.warning("Invalid Option");
					continue;
				}
				for (Entry<?, ?> tempIndividualRecord : accountData.entrySet()) {
					AccountDetails individualAccount = (AccountDetails) tempIndividualRecord.getValue();
					logger.severe("-" + "-".repeat(40) + "-");
					logger.severe(String.format("%-15s", "Account Number")
							+ String.format("%-15s", individualAccount.getAccountNumber()));
					logger.severe(
							String.format("%-15s", "Balance") + String.format("%-15s", individualAccount.getBalance()));
					logger.severe(String.format("%-15s", "Account Type")
							+ String.format("%-15s", individualAccount.getAccountType()));
					logger.severe(
							String.format("%-15s", "Status") + String.format("%-15s", individualAccount.getStatus()));
					logger.severe(String.format("%-15s", "Branch IFSC code")
							+ String.format("%-15s", individualAccount.getBranchId()));
					logger.severe("-" + "-".repeat(40) + "-");
				}
			}
				break;
			case 12: {
				logger.info("Enter the Id or IFSC code to get the Details ");
				Object value = scanner.next();
				BranchDetails branchDetail = branchFunction.getBranchDetails(value);
				logger.severe("-" + "-".repeat(40) + "-");
				logger.severe(String.format("%-15s", "Id") + String.format("%-15s", branchDetail.getId()));
				logger.severe(String.format("%-15s", "IFSC Code") + String.format("%-15s", branchDetail.getIfscCode()));
				logger.severe(String.format("%-15s", "Address") + String.format("%-15s", branchDetail.getAddress()));
				logger.severe(
						String.format("%-15s", "ManagerId") + String.format("%-15s", branchDetail.getManagerId()));
				logger.severe(
						String.format("%-15s", "Phone Number") + String.format("%-15s", branchDetail.getPhoneNumber()));
				logger.severe("-" + "-".repeat(40) + "-");
			}
				break;
			case 13: {
				breakCondition = false;
			}
				break;
			}
		}
	}

}
