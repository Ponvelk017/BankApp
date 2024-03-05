package customLogics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbLogics.TransactionOperations;
import details.TransactionDetails;
import utility.InputCheck;
import utility.InvalidInputException;

public class TransactionFunctions {

	private TransactionOperations transactionOpertaion = new TransactionOperations();

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
		List<TransactionDetails> records = transactionOpertaion.getCustomData(transactionDetails, columnToGet,
				duration);
		Map<Integer, TransactionDetails> result = new HashMap<Integer, TransactionDetails>();
		long temp=0,accountNumber = 0;
		for (TransactionDetails record : records) {
			temp = record.getAccountId();
			if(temp != accountNumber) {
				
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
			int duration) throws InvalidInputException {
		InputCheck.checkNull(transaction);
		InputCheck.checkNull(columnToGet);
		InputCheck.checkNegativeInteger(duration);
		List<TransactionDetails> record = transactionOpertaion.getCustomData(transaction, columnToGet, duration);
		return record;
	}
}
