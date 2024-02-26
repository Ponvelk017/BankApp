package customDB;

import details.CustomerDetails;
import utility.InvalidInputException;

public interface Customer {

	public int insertCustomer(CustomerDetails customer) throws InvalidInputException;

	public int updateDetails(int Id, String column, Object value) throws InvalidInputException;

	public CustomerDetails getProfile(int customerId) throws InvalidInputException;
}
