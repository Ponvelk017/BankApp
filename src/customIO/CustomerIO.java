package customIO;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Logger;

import cacheLogics.RedisCache;
import customDB.Cache;
import customLogics.AccountFunctions;
import customLogics.TransactionFunctions;
import customLogics.UserFunctions;
import details.AccountDetails;
import details.CustomerDetails;
import details.TransactionDetails;
import utility.Common;
import utility.InvalidInputException;

public class CustomerIO {

	private static Logger logger = Bankapp.logger;
	Scanner scanner = new Scanner(System.in);

	public void customerAction() throws InvalidInputException {

		CustomerIO customerIo = new CustomerIO();
		AccountFunctions accountFunctions = new AccountFunctions();
		TransactionFunctions transactionFunctions = new TransactionFunctions();
		UserFunctions userFunctions = new UserFunctions();
		TransactionDetails transactionDetails = new TransactionDetails();

		Cache cache = RedisCache.getInstance();

		int customerId = Bankapp.userIdThread.get();
		long primaryAccount = customerIo.getPrimaryAccount(customerId, accountFunctions.getAllAccount(customerId));
		logger.info("To change the Account log out and login");
		boolean condition = true;
		while (condition) {
			String status = accountFunctions.getStatus(primaryAccount);
			if (status.equals("0")) {
				logger.warning("Your account has been Blocked, you cannot Proceed now :(");
				break;
			}
			logger.info("-" + "-".repeat(40) + "-");
			logger.info("Select a Option to Proceed : ");
			logger.info(String.format("%10s", "1.My Profile"));
			logger.info(String.format("%10s", "2.Account Details"));
			logger.info(String.format("%10s", "3.View Balance"));
			logger.info(String.format("%10s", "4.Deposit "));
			logger.info(String.format("%10s", "5.Withdraw"));
			logger.info(String.format("%10s", "6.Transfer within Bank"));
			logger.info(String.format("%10s", "7.Transfer with other Bank"));
			logger.info(String.format("%10s", "8.Take Statement"));
			logger.info(String.format("%10s", "9.Change Password"));
			logger.info(String.format("%10s", "10.Log Out"));
			logger.info("-" + "-".repeat(40) + "-");

			String option = scanner.next();
			switch (option) {
			case "1": {
				CustomerDetails customerDet = cache.getCustomer(customerId);
				logger.severe("-" + "-".repeat(40) + "-");
				logger.severe(String.format("%-15s", "Name") + String.format("%-15s", customerDet.getName()));
				logger.severe(String.format("%-15s", "DOB")
						+ String.format("%-15s", Common.milliToDate(customerDet.getDOB())));
				logger.severe(
						String.format("%-15s", "Mobile Number") + String.format("%-15s", customerDet.getMobile()));
				logger.severe(String.format("%-15s", "Email") + String.format("%-15s", customerDet.getEmail()));
				logger.severe(String.format("%-15s", "Gender") + String.format("%-15s", customerDet.getGender()));
				logger.severe(String.format("%-15s", "Status") + String.format("%-15s", customerDet.getStatus()));
				logger.severe(String.format("%-15s", "Aadhar") + String.format("%-15s", customerDet.getAadhar()));
				logger.severe(String.format("%-15s", "PAN Number") + String.format("%-15s", customerDet.getPan()));
				logger.severe(String.format("%-15s", "Address") + String.format("%-15s", customerDet.getAddress()));
				logger.severe("-" + "-".repeat(40) + "-");
				((RedisCache) cache).displayFrequency();
			}
				break;
			case "2": {
				logger.info("Account Details");
				AccountDetails accountDetails = new AccountDetails();
				accountDetails.setUserId(customerId);
				Map<Long, AccountDetails> accounts = accountFunctions.accountDetails(accountDetails);
				for (Entry<?, ?> tempIndividualAccount : accounts.entrySet()) {
					AccountDetails individualAccount = (AccountDetails) tempIndividualAccount.getValue();
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
					logger.severe(String.format("%-15s", "Account Type")
							+ String.format("%-15s", individualAccount.getAccountType()));
					logger.severe("-" + "-".repeat(40) + "-");
				}
			}
				break;
			case "3": {
				logger.info("You have Rs :");
				AccountDetails accountDetails = cache.getAccount(primaryAccount);
				logger.info(accountDetails.getBalance() + "");
			}
				break;
			case "4": {
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
				((RedisCache) cache).displayFrequency();
			}
				break;
			case "5": {
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
			case "6": {
				logger.info("Enter the Reciver Account Number : ");
				long receiverAccountNumber = scanner.nextLong();
				logger.info("Enter the Amount to send : ");
				long amount = scanner.nextLong();
				logger.info("Enter Description");
				scanner.nextLine();
				String description = scanner.nextLine();
				Map<String, Integer> result = transactionFunctions.newTransferWithinBank(primaryAccount,
						receiverAccountNumber, amount, description);
				if (result.get("SuffientBalance") == 0) {
					logger.warning("Insuffient Balance");
				} else if (result.get("SenderTransactionid") == 0 || result.get("ReciverTransactionid") == 0) {
					logger.warning("Your Transaction is't completed .\nSorry for the inconvenience :(");
				} else {
					logger.info("Your Transaction Details");
					transactionDetails = transactionFunctions.getTransactionDetails(result.get("SenderTransactionid"),
							"Id");
					if (transactionDetails != null) {
						logger.severe("-" + "-".repeat(40) + "-");
						logger.severe(String.format("%-20s", "Transaction Id")
								+ String.format("%-20s", transactionDetails.getId()));
						logger.severe(String.format("%-20s", "From ")
								+ String.format("%-20s", transactionDetails.getAccountId()));
						logger.severe(String.format("%-20s", "To ")
								+ String.format("%-20s", transactionDetails.getTransactionAccountId()));
						logger.severe(String.format("%-20s", "Transaction Time")
								+ String.format("%-20s", new Date(transactionDetails.getTransactionTime()).toString()));
						logger.severe(String.format("%-20s", "Transaction type")
								+ String.format("%-20s", transactionDetails.getTransactionType()));
						logger.severe(String.format("%-20s", "Description")
								+ String.format("%-20s", transactionDetails.getDescription()));
						logger.severe(String.format("%-20s", "Transaction Status") + String.format("%-20s",
								(transactionDetails.getStatus().equals("1")) ? "Success" : "Failed"));
						logger.severe(String.format("%-20s", "Transaction Amount")
								+ String.format("%-20s", transactionDetails.getAmount()));
						logger.severe(String.format("%-20s", "Closing Balance")
								+ String.format("%-20s", transactionDetails.getClosingBalance()));
					}
				}
			}
				break;
			case "7": {
				logger.info("Enter the Reciver Account Number : ");
				long receiverAccountNumber = scanner.nextLong();
				logger.info("Enter the IFSC Code Of the Sender's Bank : ");
				String ifsc = scanner.next();
				logger.info("Enter the Amount to send : ");
				long amount = scanner.nextLong();
				logger.info("Enter Description");
				scanner.nextLine();
				String description = scanner.nextLine();
				long transactionId = transactionFunctions.newTransferOtherBank(primaryAccount, receiverAccountNumber,
						amount, description, ifsc);
				if (transactionId > 0) {
					logger.info("Your Transaction Details");
					transactionDetails = transactionFunctions.getTransactionDetails(transactionId, "Id");
					if (transactionDetails != null) {
						logger.severe("-" + "-".repeat(40) + "-");
						logger.severe(String.format("%-20s", "Transaction Id")
								+ String.format("%-20s", transactionDetails.getId()));
						logger.severe(String.format("%-20s", "From ")
								+ String.format("%-20s", transactionDetails.getAccountId()));
						logger.severe(String.format("%-20s", "To ")
								+ String.format("%-20s", transactionDetails.getTransactionAccountId()));
						logger.severe(String.format("%-20s", "Transaction Time")
								+ String.format("%-20s", new Date(transactionDetails.getTransactionTime()).toString()));
						logger.severe(String.format("%-20s", "Transaction type")
								+ String.format("%-20s", transactionDetails.getTransactionType()));
						logger.severe(String.format("%-20s", "Description")
								+ String.format("%-20s", transactionDetails.getDescription()));
						logger.severe(String.format("%-20s", "Transaction Status") + String.format("%-20s",
								(transactionDetails.getStatus().equals("1")) ? "Success" : "Failed"));
						logger.severe(String.format("%-20s", "Transaction Amount")
								+ String.format("%-20s", transactionDetails.getAmount()));
						logger.severe(String.format("%-20s", "Closing Balance")
								+ String.format("%-20s", transactionDetails.getClosingBalance()));
						logger.severe(String.format("%-20s", "IFSC Code Of Receiver")
								+ String.format("%-20s", transactionDetails.getIFSCCode()));
					}
				} else {
					logger.warning("Your Transaction is't completed .\nSorry for the inconvenience :(");
				}
			}
				break;
			case "8": {
				logger.info("Take Statement for \n1. 1 Month\n2. 2 Months\n3. 3 Months");
				int statementoption = scanner.nextInt();
				statementoption = (statementoption == 1 ? 30 : (statementoption == 2) ? 60 : 90);
				List<TransactionDetails> records = transactionFunctions.accountStatement(statementoption, 0,
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
							+ String.format("%-20s", Common.milliToDate(singleTransaction.getTransactionTime())
									.format(DateTimeFormatter.ISO_LOCAL_DATE)));
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
			case "9": {
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
			case "10": {
				condition = false;
			}
				break;
			default: {
				logger.warning("Invalid Choice :(");
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
