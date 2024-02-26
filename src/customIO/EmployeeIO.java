package customIO;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import customLogics.AccountFunctions;
import customLogics.CustomerFunctions;
import customLogics.TransactionFunctions;
import customLogics.UserFunctions;
import details.CustomerDetails;
import details.TransactionDetails;
import utility.InvalidInputException;

public class EmployeeIO {

	private static Logger logger = Bankapp.logger;

	public void employeeActions(int employeeId) throws InvalidInputException {

		TransactionDetails transactionDetails = new TransactionDetails();
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
			logger.info(String.format("%10s", "2.Update the Details of a Customer"));
			logger.info(String.format("%10s", "3.Delete a Customer"));
			logger.info(String.format("%10s", "4.view Transactions of a User"));
			logger.info(String.format("%10s", "5.Make a User Active/Inactive"));
			logger.info(String.format("%10s", "6.Block/Unblock a Account"));
			logger.info(String.format("%8s", "7.Log Out"));
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
				break;
			case 2: {
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
			case 3: {
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
			case 4: {
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
			case 5: {
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

			case 6: {
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
			case 7: {
				breakCondition = false;
			}
				break;
			}
		}
	}

}
