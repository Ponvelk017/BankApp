package customDB;

import java.util.List;

import details.CustomerDetails;
import utility.InvalidInputException;

public interface Customer {

	public List<Integer> insertCustomer(List<CustomerDetails> customers) throws InvalidInputException;

	public int updateDetails(int Id, String column, Object value) throws InvalidInputException;

	public List<CustomerDetails> getCustomCustomer(CustomerDetails customer, List<String> columnToGet)
			throws InvalidInputException;

}
