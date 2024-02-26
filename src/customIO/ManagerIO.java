package customIO;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;
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
			logger.info(String.format("%10s", "2.Add a Employee"));
			logger.info(String.format("%10s", "3.Create an Account to User"));
			logger.info(String.format("%10s", "4.Update the Details of a Customer"));
			logger.info(String.format("%10s", "5.Update the Details of a Employee"));
			logger.info(String.format("%10s", "6.Delete a User"));
			logger.info(String.format("%10s", "7.view Transactions"));
			logger.info(String.format("%10s", "8.Make a User Active/Inactive"));
			logger.info(String.format("%10s", "9.Block/Unblock a Account"));
			logger.info(String.format("%10s", "10.Log Out"));
			logger.info("-" + "-".repeat(40) + "-");
			int option = scanner.nextInt();
			switch (option) {
			case 1: {
				logger.info("Enter the Customer \nName");
				customerDet.setName(scanner.next());
				logger.info("DOB");
				customerDet.setDob(scanner.next());
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
				int affectedRows = customerFunctions.addCustomer(customerDet);
				if (affectedRows > 0) {
					logger.info("Successfully inserted");
				} else {
					logger.info("insertion was UnSuccessful");
				}
			}
				break;
			case 2: {
				logger.info("Enter the Employee \nName");
				employeeDet.setName(scanner.next());
				logger.info("DOB");
				employeeDet.setDob(scanner.next());
				logger.info("Mobile");
				employeeDet.setMobile(scanner.next());
				logger.info("Email");
				employeeDet.setEmail(scanner.next());
				logger.info("Gender");
				employeeDet.setGender(scanner.next());
				logger.info("Password");
				employeeDet.setPassword(scanner.next());
				logger.info("BranchID");
				employeeDet.setBranch(scanner.next());
				logger.info("Join Date");
				employeeDet.setJoinDate(scanner.next());
				logger.info("Admin");
				employeeDet.setIsAdmin(scanner.nextBoolean());
				employeeDet.setType("Employee");
				int affectedRows = employeeFunctions.addEmployee(employeeDet);
				if (affectedRows > 0) {
					logger.info("Successfully inserted");
				} else {
					logger.info("insertion was UnSuccessful");
				}
			}
				break;
			case 3: {
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
				if (affectedRow > 0) {
					logger.info("Account created");
				} else {
					logger.info("Account creation was UnSuccessful");
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
				int transactionOption = scanner.nextInt();
				switch (transactionOption) {
				case 1: {
					logger.info("Enter the Account Number to Proceed");
					List<TransactionDetails> records = transactionFunctions.accountStatement(90, scanner.nextLong());
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
								+ String.format("%-20s", new Date(singleTransaction.getTime()).toString()));
						logger.severe(String.format("%-20s", "Transaction type")
								+ String.format("%-20s", singleTransaction.getTranactionType()));
						logger.severe(String.format("%-20s", "Transaction Status")
								+ String.format("%-20s", singleTransaction.getTransactionStatus()));
						logger.severe(String.format("%-20s", "Transaction Amount")
								+ String.format("%-20s", singleTransaction.getAmount()));
						logger.severe(String.format("%-20s", "Closing Balance")
								+ String.format("%-20s", singleTransaction.getClosingBalance()));
					}
				}
					break;
				case 2: {
					logger.info("Enter the Transaction Id to Proceed ");
					transactionDetails = transactionFunctions.getTransactionDetails(scanner.nextLong());
					if (transactionDetails != null) {
						logger.severe("-" + "-".repeat(40) + "-");
						logger.severe(String.format("%-20s", "Transaction Id")
								+ String.format("%-20s", transactionDetails.getId()));
						logger.severe(String.format("%-20s", "From ")
								+ String.format("%-20s", transactionDetails.getAccountId()));
						logger.severe(String.format("%-20s", "To ")
								+ String.format("%-20s", transactionDetails.getTransactionAccountId()));
						logger.severe(String.format("%-20s", "Transaction Time")
								+ String.format("%-20s", new Date(transactionDetails.getTime()).toString()));
						logger.severe(String.format("%-20s", "Transaction type")
								+ String.format("%-20s", transactionDetails.getTranactionType()));
						logger.severe(String.format("%-20s", "Transaction Status")
								+ String.format("%-20s", transactionDetails.getTransactionStatus()));
						logger.severe(String.format("%-20s", "Transaction Amount")
								+ String.format("%-20s", transactionDetails.getAmount()));
						logger.severe(String.format("%-20s", "Closing Balance")
								+ String.format("%-20s", transactionDetails.getClosingBalance()));
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
				breakCondition = false;
			}
				break;
			}
		}
	}
}
