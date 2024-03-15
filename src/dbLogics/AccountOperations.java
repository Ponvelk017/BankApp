package dbLogics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customDB.Account;
import details.AccountDetails;
import utility.DBConnection;
import utility.InputCheck;
import utility.InvalidInputException;

public class AccountOperations implements Account {

	private Connection connection = DBConnection.getConnection();

	private final String createAccount = "insert into Account(UserId , BranchId , AccountType) values (?,?,?)";

	private Map<String, String> mappingRecords = new HashMap<String, String>();

	private void getMappingDetails() throws InvalidInputException {
		try (PreparedStatement statement = connection
				.prepareStatement("select * from Account where AccountNumber = -1");
				ResultSet allColumns = statement.executeQuery()) {
			ResultSetMetaData metadata = allColumns.getMetaData();
			int columns = ((ResultSetMetaData) metadata).getColumnCount();
			for (int i = 1; i <= columns; i++) {
				String tempColumn = (String) metadata.getColumnName(i);
				String field = "set" + ((tempColumn).charAt(0) + "").toUpperCase()
						+ tempColumn.substring(1, tempColumn.length());
				mappingRecords.put(tempColumn, field);
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
	}

	private Map<Long, AccountDetails> setDetails(ResultSet record) throws InvalidInputException {
		getMappingDetails();
		Map<Long, AccountDetails> records = new HashMap<Long, AccountDetails>();
		ResultSetMetaData metadata;
		try {
			metadata = record.getMetaData();
			int columns = metadata.getColumnCount();
			Method[] userMethods = AccountDetails.class.getMethods();
			List<String> customerMethodsList = new ArrayList<String>();
			for (Method temp : userMethods) {
				customerMethodsList.add(temp.toString());
			}
			while (record.next()) {
				AccountDetails accountDetails = (AccountDetails) AccountDetails.class.getDeclaredConstructor()
						.newInstance();
				for (int i = 1; i <= columns; i++) {
					String columnName = metadata.getColumnName(i);
					String dataType = metadata.getColumnTypeName(i);
					Method method;
					if (dataType.equals("INT")) {
						method = AccountDetails.class.getMethod(mappingRecords.get(columnName), int.class);
						method.invoke(accountDetails, record.getInt(i));
					} else if (dataType.equals("VARCHAR") || dataType.equals("ENUM") || dataType.equals("CHAR")) {
						method = AccountDetails.class.getMethod(mappingRecords.get(columnName), String.class);
						method.invoke(accountDetails, record.getString(i));
					} else if (!columnName.equals("DeleteAt") && dataType.equals("MEDIUMTEXT")
							|| dataType.equals("BIGINT")) {
						method = AccountDetails.class.getMethod(mappingRecords.get(columnName), long.class);
						method.invoke(accountDetails, record.getLong(i));
					}
				}
				records.put(record.getLong("AccountNumber"), accountDetails);
			}
		} catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		} finally {
			try {
				record.close();
			} catch (SQLException e) {
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
			}
		}
		return records;
	}

	@Override
	public int createAccount(AccountDetails account) throws InvalidInputException {
		InputCheck.checkNull(account);
		int affectedRow = 0;
		try (PreparedStatement statement = connection.prepareStatement(createAccount)) {
			statement.setInt(1, account.getUserId());
			statement.setString(2, account.getBranchId());
			statement.setString(3, account.getAccountType());
			affectedRow = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRow;
	}

	@Override
	public Object getSingleRecord(String columnToGet, String column, Object value) throws InvalidInputException {
		InputCheck.checkNull(value);
		InputCheck.checkNull(column);
		InputCheck.checkNull(columnToGet);
		String query = "select " + columnToGet + " from Account where " + column + " = ?";
		Object result = null;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, value);
			try (ResultSet record = statement.executeQuery()) {
				while (record.next()) {
					result = record.getObject(1);
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, AccountDetails> getCustomAccountDetails(AccountDetails accountDetails)
			throws InvalidInputException {
		InputCheck.checkNull(accountDetails);
		Map<Long, AccountDetails> userAccount = new HashMap<Long, AccountDetails>();
		StringBuilder query = new StringBuilder("select * from Account where ");
		int count = 1;
		if (accountDetails.getAccountNumber() != 0) {
			query.append("AccountNumber = ? ");
			count++;
		}
		if (accountDetails.getUserId() != 0) {
			query.append("UserId = ? ");
			count++;
		}
		if (accountDetails.getStatus() != null) {
			query.append("Status = ? ");
			count++;
		}
		try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
			count = 1;
			if (accountDetails.getAccountNumber() != 0) {
				statement.setLong(count++, accountDetails.getAccountNumber());
			}
			if (accountDetails.getUserId() != 0) {
				statement.setInt(count++, accountDetails.getUserId());
			}
			if (accountDetails.getStatus() != null) {
				if (accountDetails.getStatus().endsWith("Active")) {
					statement.setString(count++, "Active");
				} else {
					statement.setString(count++, "Inactive");
				}
			}
			try (ResultSet record = statement.executeQuery()) {
				userAccount = setDetails(record);
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return userAccount;
	}

	@Override
	public int updateColumn(String column, Object DepositeAmount, long accountNumber) throws InvalidInputException {
		InputCheck.checkNull(column);
		InputCheck.checkNull(DepositeAmount);
		InputCheck.checkNull(accountNumber);
		int affectedRows = 0;
		String query = "update Account set " + column + " = ? where AccountNumber = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, DepositeAmount);
			statement.setObject(2, accountNumber);
			affectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}

}
