package cacheLogics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import customDB.Cache;
import dbLogics.AccountOperations;
import dbLogics.CustomerOperations;
import details.AccountDetails;
import details.CustomerDetails;
import redis.clients.jedis.Jedis;
import utility.InvalidInputException;

public class RedisCache implements Cache {

	CustomerOperations customerOperations = new CustomerOperations();
	AccountOperations accountOperations = new AccountOperations();
	public static RedisCache redis;

	private final String customerCachePrefix = "Customer";
	private final String accountCachePrefix = "Account";
	private final String frequencySet = "frequency";
	private int expirationTime = 3600;

	public static RedisCache cache;
	public final Jedis jedis;
	int capacity;

	private RedisCache(int capacity) {
		this.capacity = capacity;
		this.jedis = new Jedis("localhost", 6379);
	}

	public static synchronized RedisCache getInstance() {
		if (cache == null) {
			cache = new RedisCache(50);
		}
		return cache;
	}

	@Override
	public synchronized void setCustomer(int customerId, CustomerDetails customerDetails) {
		try {
			try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
					ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);) {
				outputStream.writeObject(customerDetails);
				jedis.set((customerCachePrefix + customerId).getBytes(), byteStream.toByteArray());
				jedis.expire(customerCachePrefix + customerId, expirationTime);
				jedis.zincrby(frequencySet, 1, (customerId + ""));
				if (jedis.zcard(frequencySet) > capacity) {
					jedis.del((jedis.zrange(frequencySet, capacity - 1, capacity).get(0)));
					jedis.zremrangeByRank(frequencySet, capacity - 1, capacity);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized CustomerDetails getCustomer(int customerId) throws InvalidInputException {
		CustomerDetails customerDetails = new CustomerDetails();
		try {
			byte[] customerData = jedis.get((customerCachePrefix + customerId).getBytes());
			if (customerData != null) {
				jedis.zincrby(frequencySet, 1, (customerCachePrefix + customerId));
				try (ByteArrayInputStream byteStream = new ByteArrayInputStream(customerData);
						ObjectInputStream outputStream = new ObjectInputStream(byteStream);) {
					customerDetails = (CustomerDetails) outputStream.readObject();
				}
			} else {
				CustomerDetails tempCustomerDetails = new CustomerDetails();
				tempCustomerDetails.setId(customerId);
				List<String> columnToGet = new ArrayList<String>();
				columnToGet.add("User.*");
				columnToGet.add("Customer.Address");
				columnToGet.add("Customer.Aadhar");
				columnToGet.add("Customer.Pan");
				List<CustomerDetails> customerDet = customerOperations.getCustomCustomer(tempCustomerDetails,
						columnToGet);
				customerDetails = customerDet.get(0);
				setCustomer(customerId, customerDetails);
			}

		} catch (

		IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return customerDetails;
	}

	@Override
	public synchronized void deleteCustomer(int customerId) {
		jedis.del(customerCachePrefix + customerId);
		jedis.zrem(frequencySet, customerCachePrefix + customerId);
	}

	@Override
	public synchronized void setAccount(long account, AccountDetails accountDetails) {
		try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);) {
			outputStream.writeObject(accountDetails);
			jedis.set((accountCachePrefix + account).getBytes(), byteStream.toByteArray());
			jedis.expire(accountCachePrefix + account, expirationTime);
			jedis.zincrby(frequencySet, 1, (accountCachePrefix + account));
			if (jedis.zcard(frequencySet) > capacity) {
				jedis.del((jedis.zrange(frequencySet, capacity - 1, capacity).get(0)));
				jedis.zremrangeByRank(frequencySet, capacity - 1, capacity);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized AccountDetails getAccount(long account) throws InvalidInputException {
		AccountDetails accounts = new AccountDetails();
		try {
			byte[] accountData = jedis.get((accountCachePrefix + account).getBytes());
			if (accountData != null) {
				jedis.zincrby(frequencySet, 1, (accountCachePrefix + account));
				try (ByteArrayInputStream byteStream = new ByteArrayInputStream(accountData);
						ObjectInputStream outputStream = new ObjectInputStream(byteStream);) {
					accounts = (AccountDetails) outputStream.readObject();
					if (accounts != null) {
						return accounts;
					}
				}
			} else {
				AccountDetails accountDetails = new AccountDetails();
				accountDetails.setAccountNumber(account);
				Map<Long, AccountDetails> usersAccount = accountOperations.getCustomAccountDetails(accountDetails);
				for (Map.Entry<Long, AccountDetails> individualAccount : usersAccount.entrySet()) {
					setAccount(individualAccount.getKey(),individualAccount.getValue());
					if (account == individualAccount.getKey()) {
						accounts = individualAccount.getValue();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public synchronized void deleteAccount(int customerId) {
		jedis.del(accountCachePrefix + customerId);
		jedis.zrem(frequencySet, accountCachePrefix + customerId);
	}

	public void displayFrequency() {
		List<String> frequency = (List<String>) jedis.zrange(frequencySet, 0, -1);
		for (String member : frequency) {
			Double score = jedis.zscore(frequencySet, member);
			System.out.println("CustomerId: " + member + ", Frequency: " + score);
		}
	}

}
