package customIO;

import java.sql.Date;
import java.util.List;
import java.util.Map;
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

public class CustomerIO {

	private static Logger logger = Bankapp.logger;
	Scanner scanner = new Scanner(System.in);

	public void customerAction(int customerId) throws InvalidInputException {

		CustomerIO customerIo = new CustomerIO();
		CustomerFunctions customerFunctions = new CustomerFunctions();
		AccountFunctions accountFunctions = new AccountFunctions();
		TransactionFunctions transactionFunctions = new TransactionFunctions();
		UserFunctions userFunctions = new UserFunctions();
		TransactionDetails transactionDetails = new TransactionDetails();
		
		long primaryAccount= customerIo.getPrimaryAccount(customerId,
				accountFunctions.getAllAccount(customerId));
		logger.info("To change the Account log out and login");
		boolean condition = true;
		while (condition) {
			String status = accountFunctions.getStatus(primaryAccount);
			if(status.equals("inactive")) {
				logger.warning("Your account has been Blocked, you cannot Proceed now :(");
				break;
			}
			logger.info("-" + "-".repeat(40) + "-");
			logger.info("Select a Option to Proceed : ");
			logger.info(String.format("%10s", "1.My Profile"));
			logger.info(String.format("%10s", "2.Account Details"));
			logger.info(String.format("%10s", "3.View Balance"));
			logger.info(String.format("%10s", "4.Deposite "));
			logger.info(String.format("%10s", "5.Withdraw"));
			logger.info(String.format("%10s", "6.Transfer within Bank"));
			logger.info(String.format("%10s", "7.Transfer with other Bank"));
			logger.info(String.format("%10s", "8.Take Statement"));
			logger.info(String.format("%10s", "9.Change Password"));
			logger.info(String.format("%10s", "10.Log Out"));
			logger.info("-" + "-".repeat(40) + "-");

			int option = scanner.nextInt();
			switch (option) {
			case 1: {
				logger.info("Your Details");
				CustomerDetails customerDet = customerFunctions.getCustomerProfile(customerId);
				logger.severe("-" + "-".repeat(40) + "-");
				logger.severe(String.format("%-15s", "Name") + String.format("%-15s", customerDet.getName()));
				logger.severe(String.format("%-15s", "DOB") + String.format("%-15s", customerDet.getDob()));
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
				break;
			case 2: {
				logger.info("Account Details");
				List<AccountDetails> accounts = accountFunctions.accountDetails(customerId);
				for (AccountDetails individualAccount : accounts) {
					logger.severe("-" + "-".repeat(40) + "-");
					logger.severe(String.format("%-15s", "Account Number")
							+ String.format("%-15s", individualAccount.getAccountNo()));
					logger.severe(
							String.format("%-15s", "Balance") + String.format("%-15s", individualAccount.getBalance()));
					logger.severe(String.format("%-15s", "Account Type")
							+ String.format("%-15s", individualAccount.getAccountType()));
					logger.severe(String.format("%-15s", "Status")
							+ String.format("%-15s", individualAccount.getAccountStatus()));
					logger.severe(String.format("%-15s", "Branch IFSC code")
							+ String.format("%-15s", individualAccount.getBranchId()));
					logger.severe("-" + "-".repeat(40) + "-");
				}
			}
				break;
			case 3: {
				long balance;
				logger.info("You have Rs :");
				balance = accountFunctions.getBalance(primaryAccount);
				logger.info(balance + "");
			}
				break;
			case 4: {
				logger.info("Enter the Amount to Deposite : ");
				long depositeAmount = scanner.nextLong();
				long transactionId = accountFunctions.deposite(primaryAccount, depositeAmount);
				transactionDetails = transactionFunctions.getTransactionDetails(transactionId);
				if (transactionDetails != null) {
					logger.info("Successfully deposited :)\nYour Transaction statement is");
					logger.severe("-" + "-".repeat(40) + "-");
					logger.severe(String.format("%-20s", "Transaction Id")
							+ String.format("%-20s", transactionDetails.getId()));
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
				} else {
					logger.warning("Sorry , Something went wrong");
				}
			}
				break;
			case 5: {
				logger.info("Enter the Amount to withdraw :");
				long withdrawAmount = scanner.nextLong();
				long transactionId = accountFunctions.withdraw(primaryAccount, withdrawAmount);
				transactionDetails = transactionFunctions.getTransactionDetails(transactionId);
				if (transactionDetails != null) {
					logger.info("Successfully Withdrawed :)\nYour Transaction statement is");
					logger.severe("-" + "-".repeat(40) + "-");
					logger.severe(String.format("%-20s", "Transaction Id")
							+ String.format("%-20s", transactionDetails.getId()));
					logger.severe(String.format("%-20s", "From ")
							+ String.format("%-20s", transactionDetails.getAccountId()));
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
				} else {
					logger.warning("Sorry , Something went wrong");
				}
			}
				break;
			case 6: {
				logger.info("Enter the Reciver Account Number : ");
				long receiverAccountNumber = scanner.nextLong();
				logger.info("Enter the Amount to send : ");
				long amount = scanner.nextLong();
				Map<String, Integer> result = accountFunctions.transferWithinBank(primaryAccount, receiverAccountNumber,
						amount);
				if (result.get("SuffientBalance") == 0) {
					logger.warning("Insuffient Balance");
				} else if (result.get("UpdateSenderBalance") == 0 || result.get("UpdateReceiverBalance") == 0) {
					logger.warning("Your Transaction is't completed .\nSorry for the inconvenience :(");
				} else {
					logger.info("Your Transaction Details");
					transactionDetails = transactionFunctions.getTransactionDetails(result.get("SenderTransactionid"));
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
			}
				break;
			case 7: {
				logger.info("Enter the Reciver Account Number : ");
				long receiverAccountNumber = scanner.nextLong();
				logger.info("Enter the Amount to send : ");
				long amount = scanner.nextLong();
				long transactionId = accountFunctions.transferOtherBank(primaryAccount, receiverAccountNumber, amount);
				if (transactionId > 0) {
					logger.info("Your Transaction Details");
					transactionDetails = transactionFunctions.getTransactionDetails(transactionId);
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
				} else {
					logger.warning("Your Transaction is't completed .\nSorry for the inconvenience :(");
				}
			}
				break;
			case 8: {
				logger.info("Take Statement for \n1. 1 Month\n2. 2 Months\n3. 3 Months");
				int statementoption = scanner.nextInt();
				statementoption = (statementoption == 1 ? 30 : (statementoption == 2) ? 60 : 90);
				List<TransactionDetails> records = transactionFunctions.accountStatement(statementoption,
						primaryAccount);
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
			case 9: {
				logger.info("Enter the old Password");
				String oldPassword = scanner.next();
				if (oldPassword.equals(userFunctions.getPassword(customerId))) {
					logger.info("Enter the New Password");
					int affectedRows = userFunctions.coloumnUpdation("Password", scanner.next(), customerId);
					if (affectedRows > 0) {
						logger.info("Successfully Updated");
					} else {
						logger.info("Update was Unsuccessful");
					}
				} else {
					logger.warning("invalid Password");
				}
			}
				break;
			case 10: {
				condition = false;
			}
				break;
			}
		}
	}

	public long getPrimaryAccount(int customerId, List<Long> allAccounts) throws InvalidInputException {
		long accountNumber = -1;
		if (allAccounts.size() > 1) {
			logger.severe("-" + "-".repeat(40) + "-");
			logger.info("Choose a Account to proceed : ");
			int loop = 1;
			for (long inidivualAccountNumber : allAccounts) {
				logger.severe(String.format("%-15s", (loop++) + " : " + inidivualAccountNumber));
			}
			logger.severe("-" + "-".repeat(40) + "-");
			int accountOption = scanner.nextInt();
			if (accountOption > allAccounts.size()) {
				logger.warning("Invalid Account option :(");
			} else {
				accountNumber = allAccounts.get(accountOption - 1);
			}
		} else {
			accountNumber = allAccounts.get(0);
		}
		return accountNumber;
	}
}
