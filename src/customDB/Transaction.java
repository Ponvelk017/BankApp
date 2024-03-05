package customDB;

import java.util.List;

import details.TransactionDetails;
import utility.InvalidInputException;

public interface Transaction {

	public int setTransferTransaction(TransactionDetails transactionDetails) throws InvalidInputException;

	public List<TransactionDetails> getTransferTransaction(long transactionId, String ConditionColumn)
			throws InvalidInputException;

	public List<TransactionDetails> getCustomData(TransactionDetails transactionDetails, List<String> columnToGet,
			int duration) throws InvalidInputException;

	public long getId() throws InvalidInputException;
}
