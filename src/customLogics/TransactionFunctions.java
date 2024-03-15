package customLogics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cacheLogics.RedisCache;
import customDB.Cache;
import dbLogics.TransactionOperations;
import details.AccountDetails;
import details.TransactionDetails;
import utility.Common;
import utility.InputCheck;
import utility.InvalidInputException;

public class TransactionFunctions {

	private TransactionOperations transactionOpertaion = new TransactionOperations();
	private Cache accountCache = RedisCache.getInstance();

	public TransactionDetails getTransactionDetails(long transactionId, String conditionColumn)
			throws InvalidInputException {
		InputCheck.checkNegativeInteger(transactionId);
		List<TransactionDetails> record = transactionOpertaion.getTransferTransaction(transactionId, conditionColumn);
		return record.get(0);
	}

	public List<TransactionDetails> accountStatement(int duration, int userid, long accountId)
			throws InvalidInputException {
		InputCheck.checkNegativeInteger(duration);
		InputCheck.checkNegativeInteger(userid);
		InputCheck.checkNegativeInteger(accountId);
		List<String> columnToGet = new ArrayList<String>();
		columnToGet.add("*");
		TransactionDetails transactionDetails = new TransactionDetails();
		if (userid > 0) {
			transactionDetails.setUserId(userid);
		} else if (accountId > 0) {
			transactionDetails.setAccountId(accountId);
		}
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("From", Common.beforeNDate(duration));
		conditions.put("To", System.currentTimeMillis());
		conditions.put("Sort", "desc");
		List<TransactionDetails> records = transactionOpertaion.getCustomData(transactionDetails, columnToGet,
				conditions);
		long temp = 0, accountNumber = 0;
		for (TransactionDetails record : records) {
			temp = record.getAccountId();
			if (temp != accountNumber) {

			}
		}
		return records;
	}

	public long getLastId() throws InvalidInputException {
		return transactionOpertaion.getId();
	}

	public List<TransactionDetails> getSingleTransactionDetails(long transactionId, String conditionColumn)
			throws InvalidInputException {
		InputCheck.checkNegativeInteger(transactionId);
		InputCheck.checkNull(conditionColumn);
		List<TransactionDetails> records = new ArrayList<TransactionDetails>();
		records = transactionOpertaion.getTransferTransaction(transactionId, conditionColumn);
		return records;
	}

	public List<TransactionDetails> getCustomDetails(TransactionDetails transaction, List<String> columnToGet,
			Map<String, Object> condition) throws InvalidInputException {
		InputCheck.checkNull(transaction);
		InputCheck.checkNull(columnToGet);
		InputCheck.checkNull(condition);
		List<TransactionDetails> record = transactionOpertaion.getCustomData(transaction, columnToGet, condition);
		return record;
	}

	public long newDeposite(long accountNumber, long depositeAmount) throws InvalidInputException {
		InputCheck.checkNegativeInteger(accountNumber);
		InputCheck.checkNegativeInteger(depositeAmount);
		AccountDetails accountDetailsLock = (AccountDetails) accountCache.getAccount(accountNumber);
		synchronized (accountDetailsLock) {
			TransactionDetails transactionDetails = new TransactionDetails();
			transactionDetails.setAccountId(accountNumber);
			transactionDetails.setAmount(depositeAmount);
			return transactionOpertaion.deposite(transactionDetails, true);
		}
	}

	public long newWithdraw(long accountNumber, long withdrawAmount, String description) throws InvalidInputException {
		InputCheck.checkNegativeInteger(accountNumber);
		InputCheck.checkNegativeInteger(withdrawAmount);
		InputCheck.checkNull(description);
		AccountDetails accountDetailsLock = (AccountDetails) accountCache.getAccount(accountNumber);
		synchronized (accountDetailsLock) {
			TransactionDetails transactionDetails = new TransactionDetails();
			transactionDetails.setAccountId(accountNumber);
			transactionDetails.setAmount(withdrawAmount);
			transactionDetails.setDescription(description);
			return transactionOpertaion.withdraw(transactionDetails, true);
		}
	}

	public Map<String, Integer> newTransferWithinBank(long senderAcc, long receiverAcc, long amount, String description)
			throws InvalidInputException {
		InputCheck.checkNegativeInteger(senderAcc);
		InputCheck.checkNegativeInteger(receiverAcc);
		InputCheck.checkNegativeInteger(amount);
		InputCheck.checkNull(description);
		AccountDetails accountDetailsLock = (AccountDetails) accountCache.getAccount(senderAcc);
		synchronized (accountDetailsLock) {
			TransactionDetails transactionDetails = new TransactionDetails();
			transactionDetails.setAccountId(senderAcc);
			transactionDetails.setTransactionAccountId(receiverAcc);
			transactionDetails.setAmount(amount);
			transactionDetails.setDescription(description);
			return transactionOpertaion.transferWithinBank(transactionDetails);
		}
	}

	public long newTransferOtherBank(long senderAcc, long receiverAcc, long amount, String description, String ifsc)
			throws InvalidInputException {
		InputCheck.checkNegativeInteger(senderAcc);
		InputCheck.checkNegativeInteger(receiverAcc);
		InputCheck.checkNegativeInteger(amount);
		InputCheck.checkNull(description);
		InputCheck.checkNull(ifsc);
		AccountDetails accountDetailsLock = (AccountDetails) accountCache.getAccount(senderAcc);
		synchronized (accountDetailsLock) {
			return transactionOpertaion.transferOtherBank(senderAcc, receiverAcc, amount, description, ifsc);
		}
	}
}
