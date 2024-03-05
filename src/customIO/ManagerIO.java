package customIO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.logging.Logger;

import customLogics.AccountFunctions;
import customLogics.CustomerFunctions;
import customLogics.EmployeeFunctions;
import customLogics.TransactionFunctions;
import customLogics.UserFunctions;
import details.AccountDetails;
import details.CustomerDetails;
import details.EmployeeDetails;
import details.TransactionDetails;
import utility.Common;
import utility.InvalidInputException;

public class ManagerIO {
	private static Logger logger = Bankapp.logger;

	public void managerActions() throws InvalidInputException {

		Scanner scanner = new Scanner(System.in);

		CustomerDetails customerDet = new CustomerDetails();
		EmployeeDetails employeeDet = new EmployeeDetails();
		AccountDetails accountDet = new AccountDetails();
		TransactionDetails transactionDetails = new TransactionDetails();
		CustomerFunctions customerFunctions = new CustomerFunctions();
		EmployeeFunctions employeeFunctions = new EmployeeFunctions();
		AccountFunctions accountFunction = new AccountFunctions();
		UserFunctions userFunction = new UserFunctions();
		TransactionFunctions transactionFunctions = new TransactionFunctions();

		boolean breakCondition = true;
		while (breakCondition) {
			logger.info("-" + "-".repeat(40) + "-");
			logger.info("Select a Option to Proceed : ");
			logger.info(String.format("%10s", "1.Add a Customer"));
			logger.info(String.format("%10s", "2.Create an Account to User"));
			logger.info(String.format("%10s", "3.Add a Employee"));
			logger.info(String.format("%10s", "4.Update the Details of a Customer"));
			logger.info(String.format("%10s", "5.Update the Details of a Employee"));
			logger.info(String.format("%10s", "6.Delete a User"));
			logger.info(String.format("%10s", "7.view Transactions"));
			logger.info(String.format("%10s", "8.Make a User Active/Inactive"));
			logger.info(String.format("%10s", "9.Block/Unblock a Account"));
			logger.info(String.format("%10s", "10.View all (Active/Inactive) User"));
			logger.info(String.format("%10s", "11.View all (Block/Unblock) Account"));
			logger.info(String.format("%10s", "12.Log Out"));
			logger.info("-" + "-".repeat(40) + "-");
			int option = scanner.nextInt();
			switch (option) {
			case 1: {
				logger.info("How many Customer are you going to add");
				int customerCount = scanner.nextInt();
				List<CustomerDetails> customer = new ArrayList<CustomerDetails>();
				while (customerCount-- > 0) {
					logger.info("Enter the Customer \nName");
					customerDet.setName(scanner.next());
					logger.info("DOB");
					customerDet.setDOB(Common.dateToMilli(scanner.next()));
					logger.info("Mobile");
					customerDet.setMobile(scanner.next());
					logger.info("Email");
					customerDet.setEmail(scanner.next());
					logger.info("Gender");
					customerDet.setGender(Common.encryptPassword(scanner.next()));
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
				int affectedRows = customerFunctions.addCustomer(customer);
				if (affectedRows == -1) {
					logger.warning("Invalid Input");
				} else if (affectedRows > 0) {
					logger.info("Successfully inserted");
				} else {
					logger.info("insertion was UnSuccessful");
				}
			}
			case 2: {
				logger.info("Enter the details to create a account to Customer \nUserId");
				scanner.nextLine();
				accountDet.setUserId(scanner.nextInt());
				logger.info("Branch Id");
				accountDet.setBranchId(scanner.next());
				logger.info("Account Type \n 1.Saving \n 2.Current \n 3.Salary");
				int accTypeOption = scanner.nextInt();
				if (accTypeOption == 1) {
					accountDet.setAccountType("Saving");
				} else if (accTypeOption == 2) {
					accountDet.setAccountType("Current");
				} else {
					accountDet.setAccountType("Salary");
				}
				int affectedRow = accountFunction.addAccount(accountDet);
				if (affectedRow == -1) {
					logger.warning("Invalid Input");
					break;
				} else if (affectedRow > 0) {
					logger.info("Account created");
				} else {
					logger.info("Account creation was UnSuccessful");
				}
			}
				break;
			case 3: {
				logger.info("How many Employee are you going to add");
				int employeeCount = scanner.nextInt();
				List<EmployeeDetails> employee = new ArrayList<EmployeeDetails>();
				while (employeeCount-- > 0) {
					logger.info("Enter the Employee \nName");
					employeeDet.setName(scanner.next());
					logger.info("DOB");
					employeeDet.setDOB(Common.dateToMilli(scanner.next()));
					logger.info("Mobile");
					employeeDet.setMobile(scanner.next());
					logger.info("Email");
					employeeDet.setEmail(scanner.next());
					logger.info("Gender");
					employeeDet.setGender(scanner.next());
					logger.info("Password");
					employeeDet.setPassword(Common.encryptPassword(scanner.next()));
					logger.info("BranchID");
					employeeDet.setBranch(scanner.next());
					logger.info("Join Date");
					employeeDet.setJoinDate(scanner.next());
					logger.info("Admin");
					employeeDet.setIsAdmin(scanner.nextBoolean());
					employeeDet.setType("Employee");
					employee.add(employeeDet);
				}
				int affectedRows = employeeFunctions.addEmployee(employee);
				if (affectedRows > 0) {
					logger.info("Successfully inserted");
				} else {
					logger.info("insertion was UnSuccessful");
				}
			}
				break;
			case 4: {
				logger.info("Enter the details to update the details of the Customer \nUserId");
				scanner.nextLine();
				int customerId = scanner.nextInt();
				logger.info("Enter which detail you need to change");
				String columntoChange = scanner.next();
				logger.info("Enter the value");
				scanner.nextLine();
				String value = scanner.nextLine();
				int affectedRow = customerFunctions.updateCustomer(customerId, columntoChange, value);
				if (affectedRow > 0) {
					logger.info("Updated Successfully");
				} else {
					logger.info("Update was UnSuccessful");
				}
			}
				break;
			case 5: {
				logger.info("Enter the details to update the details of the Employee \nUserId");
				scanner.nextLine();
				int cemployeeId = scanner.nextInt();
				logger.info("Enter which detail you need to change");
				String columntoChange = scanner.next();
				logger.info("Enter the value");
				scanner.nextLine();
				String value = scanner.nextLine();
				int affectedRow = employeeFunctions.updateRecord(cemployeeId, columntoChange, value);
				if (affectedRow > 0) {
					logger.info("Updated Successfully");
				} else {
					logger.info("Update was UnSuccessful");
				}
			}
				break;
			case 6: {
				logger.info("Enter the Customer Id to Delete(make User account Inactive) : ");
				scanner.nextLine();
				int userIdToDelete = scanner.nextInt();
				int affectedRows = userFunction.deleteUser(userIdToDelete);
				if (affectedRows > 0) {
					logger.info("Deleted Successfully");
				} else {
					logger.info("Deleted was Unsuccessful");
				}
			}
				break;
			case 7: {
				logger.info("1. View all Transacions Of the User");
				logger.info("2. View Single Transacions");
				logger.info("3. View transaction of a Account");
				logger.info("4. View (Deposite/Withdraw/Both)transaction of a Account over a Period");
				int transactionOption = scanner.nextInt();
				switch (transactionOption) {
				case 1: {
					logger.info("Enter the User Id to Proceed");
					List<TransactionDetails> records = transactionFunctions.accountStatement(90, scanner.nextInt(), 0);
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
					logger.info("Enter the Time Period\n1.1 month\n2.3 months\n3.6 months\n4.12 months");
					String durationOption = scanner.next();
					int duration = ((durationOption.equals("1")) ? 1
							: (durationOption.equals("2")) ? 3
									: (durationOption.equals("3")) ? 6 : (durationOption.equals("4")) ? 12 : -1);
					if (duration == -1) {
						logger.warning("Invalid Choice");
					}
					logger.info("Enter the transaction type : \n1.Deposite\n2.Withdraw\n3.Both");
					int transactionChoice = scanner.nextInt();
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
					transactionDetails.setTransactionType((transactionChoice == 1) ? "Deposite" : "Withdraw");
					List<TransactionDetails> record = transactionFunctions.getCustomDetails(transactionDetails,
							columnToGet, duration);
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
					int affectedRows = userFunction.coloumnUpdation("Status", "Active", userId);
					if (affectedRows > 0) {
						logger.info("User Activated");
					} else {
						logger.info("Something Went Worng , Try Again");
					}
				}
					break;
				case 2: {
					int affectedRows = userFunction.coloumnUpdation("Status", "Inactive", userId);
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
					int affectedRows = accountFunction.updateColoumn("Status", "Active", account);
					if (affectedRows > 0) {
						logger.info("Account Activated");
					} else {
						logger.info("Something Went Worng , Try Again");
					}
				}
					break;
				case 2: {
					int affectedRows = accountFunction.updateColoumn("Status", "Inactive", account);
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
					customerStatus.setStatus("Active");
					activeUsers = customerFunctions.getCustomerProfile(customerStatus);
				} else if (statusOption.equals("2")) {
					customerStatus.setStatus("Inactive");
					activeUsers = customerFunctions.getCustomerProfile(customerStatus);
				} else {
					logger.warning("Invalid Option");
					continue;
				}
				for (Entry individualRecord : activeUsers.entrySet()) {
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
				AccountDetails accountDetail = new AccountDetails();
				List<AccountDetails> accountData;
				if (statusOption.equals("1")) {
					accountDetail.setStatus("Active");
					accountData = accountFunction.accountDetails(accountDetail);
				} else if (statusOption.equals("2")) {
					accountDetail.setStatus("Inactive");
					accountData = accountFunction.accountDetails(accountDetail);
				} else {
					logger.warning("Invalid Option");
					continue;
				}
				for (AccountDetails individualAccount : accountData) {
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
				breakCondition = false;
			}
				break;
			}
		}
	}
}
