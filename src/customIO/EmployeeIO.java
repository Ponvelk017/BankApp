package customIO;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import customLogics.AccountFunctions;
import customLogics.CustomerFunctions;
import customLogics.TransactionFunctions;
import customLogics.UserFunctions;
import details.AccountDetails;
import details.CustomerDetails;
import details.TransactionDetails;
import utility.InvalidInputException;

public class EmployeeIO {

	private static Logger logger = Bankapp.logger;

	public void employeeActions(int employeeId) throws InvalidInputException {

		TransactionDetails transactionDetails = new TransactionDetails();
		AccountDetails accountDetails = new AccountDetails();
		CustomerDetails customerDet = new CustomerDetails();
		UserFunctions userFunctions = new UserFunctions();
		AccountFunctions accountFunctions = new AccountFunctions();
		CustomerFunctions customerFunction = new CustomerFunctions();
		TransactionFunctions transactionFunctions = new TransactionFunctions();

		Scanner scanner = new Scanner(System.in);

		boolean breakCondition = true;
		while (breakCondition) {
			logger.info("-" + "-".repeat(40) + "-");
			logger.info("Select a Option to Proceed : ");
			logger.info(String.format("%10s", "1.Add a Customer"));
			logger.info(String.format("%10s", "3.Update the Details of a Customer"));
			logger.info(String.format("%10s", "4.Delete a Customer"));
			logger.info(String.format("%10s", "5.view Transactions of a User"));
			logger.info(String.format("%10s", "6.Make a User Active/Inactive"));
			logger.info(String.format("%10s", "7.Block/Unblock a Account"));
			logger.info(String.format("%8s", "8.Log Out"));
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
				int affectedRows = customerFunction.addCustomer(customerDet);
				if (affectedRows > 0) {
					logger.info("Successfully inserted");
				} else {
					logger.info("insertion was UnSuccessful");
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
				if (affectedRow > 0) {
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
			case 5: {
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
					List<TransactionDetails> record = transactionFunctions.getSingleTransactionDetails(scanner.nextLong());
					for (TransactionDetails records : record) {
						logger.severe("-" + "-".repeat(40) + "-");
						logger.severe(String.format("%-20s", "Transaction Id")
								+ String.format("%-20s", records.getId()));
						logger.severe(String.format("%-20s", "From ")
								+ String.format("%-20s", records.getAccountId()));
						logger.severe(String.format("%-20s", "To ")
								+ String.format("%-20s", records.getTransactionAccountId()));
						logger.severe(String.format("%-20s", "Transaction Time")
								+ String.format("%-20s", new Date(records.getTime()).toString()));
						logger.severe(String.format("%-20s", "Transaction type")
								+ String.format("%-20s", records.getTranactionType()));
						logger.severe(String.format("%-20s", "Transaction Status")
								+ String.format("%-20s", records.getTransactionStatus()));
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
			case 6: {
				logger.info("1. Make Customer Active \n2. Make User Inactive");
				int activeOption = scanner.nextInt();
				logger.info("Enter the User Id");
				int userId = scanner.nextInt();
				switch (activeOption) {
				case 1: {
					int affectedRows = userFunctions.coloumnUpdation("Status", "Active", userId);
					if (affectedRows > 0) {
						logger.info("User Activated");
					} else {
						logger.info("Something Went Worng , Try Again");
					}
				}
					break;
				case 2: {
					int affectedRows = userFunctions.coloumnUpdation("Status", "Inactive", userId);
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

			case 7: {
				logger.info("1. Make Account Active \n2. Make Account Inactive");
				int activeOption = scanner.nextInt();
				logger.info("Enter the Account Number");
				long account = scanner.nextLong();
				switch (activeOption) {
				case 1: {
					int affectedRows = accountFunctions.updateColoumn("Status", "Active", account);
					if (affectedRows > 0) {
						logger.info("Account Activated");
					} else {
						logger.info("Something Went Worng , Try Again");
					}
				}
					break;
				case 2: {
					int affectedRows = accountFunctions.updateColoumn("Status", "Inactive", account);
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
			case 8: {
				breakCondition = false;
			}
				break;
			}
		}
	}

}
