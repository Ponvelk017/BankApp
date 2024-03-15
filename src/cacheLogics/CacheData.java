package cacheLogics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dbLogics.AccountOperations;
import dbLogics.CustomerOperations;
import details.AccountDetails;
import details.CustomerDetails;
import utility.InvalidInputException;

public class CacheData{

	public static CacheData cache;
	CustomerOperations customerOperations = new CustomerOperations();
	AccountOperations accountOperations = new AccountOperations();

	int capacity = 50;
	private Map<Integer, CustomerDetails> customers;
	private Map<Integer, Map<Long, AccountDetails>> accounts;

	private CacheData() {
		this.customers = new LinkedHashMap<Integer, CustomerDetails>(capacity, 1.0f, true) {
			private static final long serialVersionUID = 1L;

			
			protected boolean removeEldestEntry(Map.Entry<Integer, CustomerDetails> eldest) {
				return size() > capacity;
			}
		};
		this.accounts = new LinkedHashMap<Integer, Map<Long, AccountDetails>>(capacity, 1.0f, true) {
			private static final long serialVersionUID = 1L;

			
			protected boolean removeEldestEntry(Map.Entry<Integer, Map<Long, AccountDetails>> eldest) {
				return size() > capacity;
			}
		};
	}

	public static CacheData getInstance() {
		if (cache == null) {
			cache = new CacheData();
		}
		return cache;
	}

	public void setCustomer(int customerId, CustomerDetails customerDetails) {
		customers.put(customerId, customerDetails);
	}

	public CustomerDetails getCustomer(int customerId) throws InvalidInputException {
		System.out.println(customers + " Overall");
		if (customers.containsKey(customerId)) {
			return customers.get(customerId);
		} else {
			CustomerDetails customerDetails = new CustomerDetails();
			customerDetails.setId(customerId);
			List<String> columnToGet = new ArrayList<String>();
			columnToGet.add("User.*");
			columnToGet.add("Customer.Address");
			columnToGet.add("Customer.Aadhar");
			columnToGet.add("Customer.Pan");
			List<CustomerDetails> customerDet = customerOperations.getCustomCustomer(customerDetails, columnToGet);
			Map<Integer, CustomerDetails> result = new HashMap<Integer, CustomerDetails>();
			for (CustomerDetails singleRecord : customerDet) {
				System.out.println(singleRecord.getId() + " " + singleRecord.getName());
				result.put(singleRecord.getId(), singleRecord);
			}
			setCustomer(customerId, result.get(customerId));

			return customers.get(customerId);
		}
	}

	public void deleteCustomer(int customerId) {
		customers.remove(customerId);
	}

	@SuppressWarnings("unchecked")
	public <K, V> void setAccount(int customerId, Map<K, V> account) {
		accounts.put(customerId, (Map<Long, AccountDetails>) account);
	}


	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getAccount(int customerId) throws InvalidInputException {
		System.out.println(accounts + " Overall Accounts");
		if (accounts.containsKey(customerId)) {
			return (Map<K, V>) accounts.get(customerId);
		} else {
			AccountDetails accountDetails = new AccountDetails();
			accountDetails.setUserId(customerId);
			Map<K, V> records = (Map<K, V>) accountOperations.getCustomAccountDetails(accountDetails);
			setAccount(customerId, records);
			return (Map<K, V>) accounts.get(customerId);
		}
	}

	
	public void deleteAccount(int customerId) {
		accounts.remove(customerId);
	}

}
