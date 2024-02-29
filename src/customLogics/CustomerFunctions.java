package customLogics;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

import dbLogics.CustomerOperations;
import details.CustomerDetails;
import utility.Common;
import utility.InputCheck;
import utility.InvalidInputException;

public class CustomerFunctions {

	private CustomerOperations customerOpertaion = new CustomerOperations();
	private CustomerDetails customerDet;

	public static int validateDetails(CustomerDetails customer) throws InvalidInputException {
		InputCheck.checkNull(customer);
		if (customer.getName().matches("^[A-Za-z.]+") && customer.getMobile().matches("^[0-9]{10}$")
				&& customer.getEmail().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			LocalDate currentDate = LocalDate.now();
			LocalDate dob =(new Date(customer.getDOB())).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Period period = Period.between(dob, currentDate);
			if (period.getYears() >= 18 && "MaleFemale".contains(customer.getGender()) && customer.getPassword()
					.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[a-zA-Z0-9@#$%^&+=!]).{8,}$")) {
				if (customer.getAadhar().length() == 12 && customer.getPan().length() == 10
						&& customer.getPan().matches("^[A_Z0-9]$")) {
					return 1;
				}
			}
		}
		return -1;
	}

	public int addCustomer(List<CustomerDetails> customers) throws InvalidInputException {
		InputCheck.checkNull(customers);
		for (CustomerDetails customer : customers) {
			if (CustomerFunctions.validateDetails(customer) == -1) {
				return 0;
			}
		}
		List<Integer> result = customerOpertaion.insertCustomer(customers);
		for (Integer singleRecord : result) {
			if (singleRecord == 0) {
				return 0;
			}
		}
		return 1;
	}

	public int updateCustomer(int Id, String column, Object value) throws InvalidInputException {
		InputCheck.checkNegativeInteger(Id);
		InputCheck.checkNull(column);
		InputCheck.checkNull(value);
		if (column.equals("DOB")) {
			value = Common.dateToMilli(value.toString());
		}
		return customerOpertaion.updateDetails(Id, column, value);
	}

	public CustomerDetails getCustomerProfile(int customerId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(customerId);
		customerDet = new CustomerDetails();
		List<CustomerDetails> customerDet = customerOpertaion.getProfile(customerId);
		return customerDet.get(0);
	}

}
