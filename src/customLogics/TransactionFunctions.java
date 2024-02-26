package customLogics;

import java.util.List;

import dbLogics.TransactionOperations;
import details.TransactionDetails;
import utility.InputCheck;
import utility.InvalidInputException;

public class TransactionFunctions {

	TransactionOperations transactionOpertaion = new TransactionOperations();
	TransactionDetails transactionDetails;

	public TransactionDetails getTransactionDetails(long transactionId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(transactionId);
		transactionDetails = new TransactionDetails();
		transactionDetails = transactionOpertaion.getTransferTransaction(transactionId);
		return transactionDetails;
	}

	public List<TransactionDetails> accountStatement(int duration, long account) throws InvalidInputException {
		InputCheck.checkNegativeInteger(duration);
		InputCheck.checkNegativeInteger(account);
		List<TransactionDetails> record = transactionOpertaion.getStatement(duration, account);
		return record;
	}
}
