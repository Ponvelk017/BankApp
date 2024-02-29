package customLogics;

import java.util.ArrayList;
import java.util.List;

import dbLogics.TransactionOperations;
import details.TransactionDetails;
import utility.InputCheck;
import utility.InvalidInputException;

public class TransactionFunctions {

	private TransactionOperations transactionOpertaion = new TransactionOperations();

	public TransactionDetails getTransactionDetails(long transactionId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(transactionId);
		List<TransactionDetails> record = transactionOpertaion.getTransferTransaction(transactionId);
		return record.get(0);
	}

	public List<TransactionDetails> accountStatement(int duration, long account) throws InvalidInputException {
		InputCheck.checkNegativeInteger(duration);
		InputCheck.checkNegativeInteger(account);
		List<TransactionDetails> record = transactionOpertaion.getStatement(duration, account);
		for(TransactionDetails temp : record) {
			System.out.println(temp.getAccountId()+" "+temp.getAmount());
		}
		return record;
	}
	
	public long getLastId() throws InvalidInputException {
		System.out.println("transaction function "+transactionOpertaion.getId());
		return transactionOpertaion.getId();
	}
	
	public List<TransactionDetails> getSingleTransactionDetails(long transactionId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(transactionId);
		List<TransactionDetails> records = new ArrayList<TransactionDetails>();
		records = transactionOpertaion.getTransferTransaction(transactionId);
		return records;
	}
}
