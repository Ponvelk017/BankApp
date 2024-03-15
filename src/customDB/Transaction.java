package customDB;

import java.util.List;
import java.util.Map;

import details.TransactionDetails;
import utility.InvalidInputException;

public interface Transaction {

	public int setTransferTransaction(TransactionDetails transactionDetails) throws InvalidInputException;

	public int setTransferTransactionWithId(TransactionDetails transactionDetails) throws InvalidInputException;

	public List<TransactionDetails> getTransferTransaction(long transactionId, String ConditionColumn)
			throws InvalidInputException;

	public List<TransactionDetails> getCustomData(TransactionDetails transactionDetails, List<String> columnToGet,
			Map<String, Object> condition) throws InvalidInputException;

	public long getId() throws InvalidInputException;

	public long deposite(TransactionDetails transactionDetails, boolean autoCommitFlag) throws InvalidInputException;

	public long withdraw(TransactionDetails transactionDetails, boolean autoCommitFlag) throws InvalidInputException;

	public Map<String, Integer> transferWithinBank(TransactionDetails transactionDetails) throws InvalidInputException;

	public long transferOtherBank(long senderAcc, long receiverAcc, long amount, String description ,String ifsc)
			throws InvalidInputException;
}
