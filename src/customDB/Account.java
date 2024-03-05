package customDB;

import java.util.List;

import details.AccountDetails;
import utility.InvalidInputException;

public interface Account {

	public int createAccount(AccountDetails account) throws InvalidInputException;

	public Object getSingleRecord(String columnToGet, String column, Object value) throws InvalidInputException;

	public List<AccountDetails> getCustomAccountDetails(AccountDetails accountDetails) throws InvalidInputException;

	public List<AccountDetails> getAvailableAccount(int userId) throws InvalidInputException;

	public int updateColumn(String column, Object DepositeAmount, long accountNumber) throws InvalidInputException;
}
