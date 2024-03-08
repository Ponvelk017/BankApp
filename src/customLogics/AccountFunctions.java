package customLogics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dbLogics.AccountOperations;
import details.AccountDetails;
import utility.InputCheck;
import utility.InvalidInputException;

public class AccountFunctions {

	private AccountOperations accountOperation = new AccountOperations();

	public int addAccount(AccountDetails account) throws InvalidInputException {
		InputCheck.checkNull(account);
		return accountOperation.createAccount(account);
	}

	public Map<Long, AccountDetails> accountDetails(AccountDetails accountDet) throws InvalidInputException {
		InputCheck.checkNull(accountDet);
		Map<Long, AccountDetails> records = accountOperation.getCustomAccountDetails(accountDet);
		return records;
	}

	public List<Long> getAllAccount(int customerId) throws InvalidInputException {
		List<Long> result = new ArrayList<Long>();
		Map<Long, AccountDetails> records = accountOperation.getAvailableAccount(customerId);
		for (Entry individualAccount : records.entrySet()) {
			result.add((Long) individualAccount.getKey());
		}
		return result;
	}

	public long getBalance(long accountNumber) throws InvalidInputException {
		InputCheck.checkNegativeInteger(accountNumber);
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
}
