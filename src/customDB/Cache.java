package customDB;

import details.AccountDetails;
import details.CustomerDetails;
import utility.InvalidInputException;

public interface Cache {

	public CustomerDetails getCustomer(int customerId) throws InvalidInputException;

	public AccountDetails getAccount(long account) throws InvalidInputException;

	public void deleteCustomer(int customerId);

	public void setCustomer(int customerId, CustomerDetails customerDetails);

	public <K, V> void setAccount(long account, AccountDetails accountDetails);

	public void deleteAccount(int customerId);
}
