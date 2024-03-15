package customLogics;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cacheLogics.RedisCache;
import customDB.Cache;
import dbLogics.CustomerOperations;
import details.CustomerDetails;
import utility.Common;
import utility.InputCheck;
import utility.InvalidInputException;

public class CustomerFunctions {

	private Cache customerCache = RedisCache.getInstance();
	private CustomerOperations customerOpertaion = new CustomerOperations();

	public static int validateDetails(CustomerDetails customer) throws InvalidInputException {
		InputCheck.checkNull(customer);
		if (customer.getName().matches("^[A-Za-z.]+") && (customer.getMobile() + "").matches("^[0-9]{10}$")
				&& customer.getEmail().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			LocalDate currentDate = LocalDate.now();
			LocalDate dob = LocalDate.ofInstant(Instant.ofEpochMilli(customer.getDOB()), ZoneId.systemDefault());
			Period period = Period.between(dob, currentDate);
			if (period.getYears() >= 18 && "MaleFemale".contains(customer.getGender()) && customer.getPassword()
					.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[a-zA-Z0-9@#$%^&+=!]).{8,}$")) {
				if (customer.getAadhar().length() == 12 && customer.getPan().length() == 10) {
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
		int affectedColumns = 0;
		CustomerDetails customerDetails = customerCache.getCustomer(Id);
		synchronized (customerDetails) {
			if (column.equals("DOB")) {
				value = Common.dateToMilli(value.toString());
			}
			affectedColumns = customerOpertaion.updateDetails(Id, column, value);
			if (affectedColumns > 0) {
				customerCache.deleteCustomer(Id);
			}
			return affectedColumns;
		}
	}

	public Map<Integer, CustomerDetails> getCustomerProfile(CustomerDetails customerDetails)
			throws InvalidInputException {
		InputCheck.checkNull(customerDetails);
		new CustomerDetails();
		List<String> columnToGet = new ArrayList<String>();
		columnToGet.add("User.*");
		columnToGet.add("Customer.Address");
		columnToGet.add("Customer.Aadhar");
		columnToGet.add("Customer.Pan");
		List<CustomerDetails> customerDet = customerOpertaion.getCustomCustomer(customerDetails, columnToGet);
		Map<Integer, CustomerDetails> result = new HashMap<Integer, CustomerDetails>();
		for (CustomerDetails singleRecord : customerDet) {
			result.put(singleRecord.getId(), singleRecord);
		}
		return result;
	}

}
