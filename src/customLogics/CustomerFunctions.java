package customLogics;

import dbLogics.CustomerOperations;
import details.CustomerDetails;
import utility.InputCheck;
import utility.InvalidInputException;

public class CustomerFunctions {

	private CustomerOperations customerOpertaion = new CustomerOperations();
	private CustomerDetails customerDet;

	public int addCustomer(CustomerDetails customer) throws InvalidInputException {
		InputCheck.checkNull(customer);
		return customerOpertaion.insertCustomer(customer);
	}

	public int updateCustomer(int Id, String column, Object value) throws InvalidInputException {
		InputCheck.checkNegativeInteger(Id);
		InputCheck.checkNull(column);
		InputCheck.checkNull(value);
		return customerOpertaion.updateDetails(Id, column, value);
	}

	public CustomerDetails getCustomerProfile(int customerId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(customerId);
		customerDet = new CustomerDetails();
		customerDet = customerOpertaion.getProfile(customerId);
		return customerDet;
	}

}
