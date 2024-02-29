package customDB;

import java.util.List;

import details.TransactionDetails;
import utility.InvalidInputException;

public interface Transaction {

	public int setTransferTransaction(TransactionDetails transactionDetails) throws InvalidInputException;

	public List<TransactionDetails> getTransferTransaction(long transactionId) throws InvalidInputException;

	public List<TransactionDetails> getStatement(int duration, long account) throws InvalidInputException;

	public long getId() throws InvalidInputException;
}
