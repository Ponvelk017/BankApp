package customLogics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbLogics.AccountOperations;
import dbLogics.TransactionOperations;
import details.AccountDetails;
import details.TransactionDetails;
import utility.InputCheck;
import utility.InvalidInputException;

public class AccountFunctions {

	private AccountOperations accountOperation = new AccountOperations();
	private TransactionOperations transaction = new TransactionOperations();
	private TransactionFunctions transactionFunctions = new TransactionFunctions();
	private TransactionDetails transactionDetails;

	public int addAccount(AccountDetails account) throws InvalidInputException {
		InputCheck.checkNull(account);
		return accountOperation.createAccount(account);
	}

	public List<AccountDetails> accountDetails(int customerId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(customerId);
		List<AccountDetails> records = accountOperation.getAccountDetails(customerId);
		return records;
	}

	public List<Long> getAllAccount(int customerId) throws InvalidInputException {
		List<Long> records = accountOperation.getAvailableAccount(customerId);
		return records;
	}

	public long getBalance(long accountNumber) throws InvalidInputException {
		return (long) accountOperation.getSingleRecord("Balance", "AccountNumber", accountNumber);
	}

	public String getStatus(long accountNumber) throws InvalidInputException {
		InputCheck.checkNegativeInteger(accountNumber);
		return (String) accountOperation.getSingleRecord("Status", "AccountNumber", accountNumber);
	}

	public int updateColoumn(String coloumn, Object value, long accountId) throws InvalidInputException {
		InputCheck.checkNull(coloumn);
		InputCheck.checkNull(value);
		InputCheck.checkNegativeInteger(accountId);
		return accountOperation.updateColumn(coloumn, value, accountId);
	}

	public long deposite(long accountNumber, long depositeAmount) throws InvalidInputException {
		InputCheck.checkNegativeInteger(accountNumber);
		InputCheck.checkNegativeInteger(depositeAmount);
		long transactionId = transactionFunctions.getLastId() + 1;
		long balance = (long) accountOperation.getSingleRecord("Balance", "AccountNumber", accountNumber);
		int affectedRows = accountOperation.updateColumn("Balance", depositeAmount + balance, accountNumber);
		transactionDetails = new TransactionDetails();
		if (affectedRows > 0) {
			transactionDetails.setId(transactionId);
			transactionDetails.setAccountId(accountNumber);
			transactionDetails
					.setUserId((int) accountOperation.getSingleRecord("UserId", "AccountNumber", accountNumber));
			transactionDetails.setTime(System.currentTimeMillis());
			transactionDetails.setTranactionType("Deposite");
			transactionDetails.setAmount(depositeAmount);
			transactionDetails.setClosingBalance(
					(long) accountOperation.getSingleRecord("Balance", "AccountNumber", accountNumber));
			affectedRows = transaction.setTransferTransaction(transactionDetails);
		}
		return transactionId;
	}

	public long withdraw(long accountNumber, long withdrawAmount, String description) throws InvalidInputException {
		InputCheck.checkNegativeInteger(accountNumber);
		InputCheck.checkNegativeInteger(withdrawAmount);
		InputCheck.checkNull(description);
		long transactionId = transactionFunctions.getLastId() + 1;
		long balance = (long) accountOperation.getSingleRecord("Balance", "AccountNumber", accountNumber);
		int affectedRows = 0;
		transactionDetails = new TransactionDetails();
		if (balance > withdrawAmount) {
			affectedRows = accountOperation.updateColumn("Balance", balance - withdrawAmount, accountNumber);
			if (affectedRows > 0) {
				transactionDetails.setId(transactionId);
				transactionDetails.setAccountId(accountNumber);
				transactionDetails
						.setUserId((int) accountOperation.getSingleRecord("UserId", "AccountNumber", accountNumber));
				transactionDetails.setTime(System.currentTimeMillis());
				transactionDetails.setTranactionType("Withdraw");
				transactionDetails.setDescription(description);
				transactionDetails.setAmount(withdrawAmount);
				transactionDetails.setClosingBalance(
						(long) accountOperation.getSingleRecord("Balance", "AccountNumber", accountNumber));
				affectedRows = transaction.setTransferTransaction(transactionDetails);
			}
		}
		return transactionId;
	}

	public Map<String, Integer> transferWithinBank(long senderAcc, long receiverAcc, long amount, String description)
			throws InvalidInputException {
		InputCheck.checkNegativeInteger(senderAcc);
		InputCheck.checkNegativeInteger(receiverAcc);
		InputCheck.checkNegativeInteger(amount);
		InputCheck.checkNull(description);
		long transactionId = transactionFunctions.getLastId() + 1;
		long senderBalance = (long) accountOperation.getSingleRecord("Balance", "AccountNumber", senderAcc);
		long receiverBalance = (long) accountOperation.getSingleRecord("Balance", "AccountNumber", receiverAcc);
		int senderBalanceUpdation = 0, receiverBalanceUpdation = 0;
		transactionDetails = new TransactionDetails();
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (senderBalance >= amount) {
			senderBalanceUpdation = accountOperation.updateColumn("Balance", senderBalance - amount, senderAcc);
			if (senderBalanceUpdation > 0) {
				receiverBalanceUpdation = accountOperation.updateColumn("Balance", receiverBalance + amount,
						receiverAcc);
				if (receiverBalanceUpdation > 0) {
					// Sender record
					transactionDetails.setId(transactionId);
					transactionDetails.setAccountId(senderAcc);
					transactionDetails.setTransactionAccountId(receiverAcc);
					transactionDetails
							.setUserId((int) accountOperation.getSingleRecord("UserId", "AccountNumber", senderAcc));
					transactionDetails.setTime(System.currentTimeMillis());
					transactionDetails.setTranactionType("Withdraw");
					transactionDetails.setDescription(description);
					transactionDetails.setAmount(amount);
					transactionDetails.setClosingBalance(senderBalance - amount);
					transaction.setTransferTransaction(transactionDetails);

					// receiver record
					transactionDetails.setId(transactionId);
					transactionDetails.setAccountId(receiverAcc);
					transactionDetails.setTransactionAccountId(senderAcc);
					transactionDetails
							.setUserId((int) accountOperation.getSingleRecord("UserId", "AccountNumber", senderAcc));
					transactionDetails.setTime(System.currentTimeMillis());
					transactionDetails.setTranactionType("Deposite");
					transactionDetails.setAmount(amount);
					transactionDetails.setClosingBalance(senderBalance - amount);
					transaction.setTransferTransaction(transactionDetails);
				} else {
					senderBalanceUpdation = accountOperation.updateColumn("Balance", senderBalance + amount, senderAcc);
				}
			}
		} else {

		}
		result.put("SuffientBalance", (senderBalance >= amount) ? 1 : 0);
		result.put("UpdateSenderBalance", senderBalanceUpdation);
		result.put("UpdateReceiverBalance", receiverBalanceUpdation);
		result.put("SenderTransactionid", (int) transactionId);
		result.put("ReciverTransactionid", (int) transactionId);
		return result;
	}

	public long transferOtherBank(long senderAcc, long receiverAcc, long amount, String description)
			throws InvalidInputException {
		InputCheck.checkNegativeInteger(senderAcc);
		InputCheck.checkNegativeInteger(receiverAcc);
		InputCheck.checkNegativeInteger(amount);
		InputCheck.checkNull(description);
		long transactionId = transactionFunctions.getLastId() + 1;
		long senderBalance = (long) accountOperation.getSingleRecord("Balance", "AccountNumber", senderAcc);
		int senderBalanceUpdation = 0;
		transactionDetails = new TransactionDetails();
		if (senderBalance >= amount) {
			senderBalanceUpdation = accountOperation.updateColumn("Balance", senderBalance - amount, senderAcc);
			if (senderBalanceUpdation > 0) {
				transactionDetails.setId(transactionId);
				transactionDetails.setAccountId(senderAcc);
				transactionDetails.setTransactionAccountId(receiverAcc);
				transactionDetails
						.setUserId((int) accountOperation.getSingleRecord("UserId", "AccountNumber", senderAcc));
				transactionDetails.setTime(System.currentTimeMillis());
				transactionDetails.setTranactionType("Withdraw");
				transactionDetails.setDescription(description);
				transactionDetails.setAmount(amount);
				transactionDetails.setClosingBalance(senderBalance - amount);
				transaction.setTransferTransaction(transactionDetails);
			}
		}
		return transactionId;
	}
}
