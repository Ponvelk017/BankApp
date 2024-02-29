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

import customDB.Transaction;
import details.TransactionDetails;
import utility.Common;
import utility.DBConnection;
import utility.InputCheck;
import utility.InvalidInputException;

public class TransactionOperations implements Transaction {

	private Connection connection = DBConnection.getConnection();
	private TransactionDetails transactionDetail;

	private final String setTransferTransaction = "insert into Transaction(Id,AccountId,TransactionAccountId,UserId,TransactionTime,TransactionType,Description,Amount,ClosingBalance) "
			+ "values(?,?,?,?,?,?,?,?,?)";
	private final String getTransferTransaction = "select * from Transaction where Id = ?";
	private final String getStatement = "select * from Transaction where  AccountId = ? || TransactionAccountId = ? and TransactionTime between ? and ?";
	private Map<String, String> mappingRecords = new HashMap<String, String>();

	public void getMappingDetails() throws InvalidInputException {
		try (PreparedStatement statement = connection.prepareStatement("select * from Transaction");
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

	private List<TransactionDetails> setDetails(ResultSet record) throws InvalidInputException {
		getMappingDetails();
		List<TransactionDetails> records = new ArrayList<TransactionDetails>();
		ResultSetMetaData metadata;
		try {
			metadata = record.getMetaData();
			int columns = metadata.getColumnCount();
			Method[] userMethods = TransactionDetails.class.getMethods();
			List<String> transactionMethodsList = new ArrayList<String>();
			for (Method temp : userMethods) {
				transactionMethodsList.add(temp.toString());
			}
			TransactionDetails transactionDetails = (TransactionDetails) TransactionDetails.class
					.getDeclaredConstructor().newInstance();
			while (record.next()) {
				for (int i = 1; i <= columns; i++) {
					String columnName = metadata.getColumnName(i);
					String dataType = metadata.getColumnTypeName(i);
					Method method;
					if (dataType.equals("INT")) {
						method = TransactionDetails.class.getMethod(mappingRecords.get(columnName), int.class);
						method.invoke(transactionDetails, record.getInt(i));
					} else if (dataType.equals("VARCHAR") || dataType.equals("ENUM") || dataType.equals("CHAR")) {
						method = TransactionDetails.class.getMethod(mappingRecords.get(columnName), String.class);
						method.invoke(transactionDetails, record.getString(i));
					} else if (!columnName.equals("DeleteAt") && dataType.equals("MEDIUMTEXT")
							|| dataType.equals("BIGINT")) {
						method = TransactionDetails.class.getMethod(mappingRecords.get(columnName), long.class);
						method.invoke(transactionDetails, record.getLong(i));
					}
				}
				System.out.println("bye "+transactionDetails.getAccountId()+" "+transactionDetails.getAmount());
				records.add(transactionDetails);
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
	public int setTransferTransaction(TransactionDetails transactionDetails) throws InvalidInputException {
		InputCheck.checkNull(transactionDetails);
		int affectedRows = 0;
		transactionDetail = new TransactionDetails();
		try (PreparedStatement statement = connection.prepareStatement(setTransferTransaction)) {
			statement.setLong(1, transactionDetails.getId());
			statement.setLong(2, transactionDetails.getAccountId());
			statement.setLong(3, transactionDetails.getTransactionAccountId());
			statement.setInt(4, transactionDetails.getUserId());
			statement.setLong(5, transactionDetails.getTime());
			statement.setString(6, transactionDetails.getTransactionType());
			statement.setString(7, transactionDetails.getDescription() + " ");
			statement.setLong(8, transactionDetails.getAmount());
			statement.setLong(9, transactionDetails.getClosingBalance());
			affectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}

	@Override
	public List<TransactionDetails> getTransferTransaction(long transactionId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(transactionId);
		List<TransactionDetails> records = new ArrayList<TransactionDetails>();
		try (PreparedStatement statement = connection.prepareStatement(getTransferTransaction)) {
			statement.setLong(1, transactionId);
			try (ResultSet record = statement.executeQuery()) {
				records = setDetails(record);
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return records;
	}

	@Override
	public List<TransactionDetails> getStatement(int duration, long account) throws InvalidInputException {
		InputCheck.checkNegativeInteger(duration);
		InputCheck.checkNegativeInteger(account);
		long from, to;
		from = Common.berforNDate(duration);
		to = Common.currentDate();
		List<TransactionDetails> transactionRecords = new ArrayList<TransactionDetails>();
		try (PreparedStatement statement = connection.prepareStatement(getStatement)) {
			statement.setLong(1, account);
			statement.setLong(2, account);
			statement.setLong(3, to);
			statement.setLong(4, from);
			try (ResultSet record = statement.executeQuery()) {
				transactionRecords = setDetails(record);
				for(TransactionDetails temp : transactionRecords) {
					System.out.println("hello "+temp.getAccountId()+" "+temp.getAmount());
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return transactionRecords;
	}

	public void getCustomData(TransactionDetails transactionDetails, List<String> columnToGet)
			throws InvalidInputException {
		InputCheck.checkNull(transactionDetails);
		InputCheck.checkNull(columnToGet);
		StringBuilder query = new StringBuilder("select ");
		try {
			PreparedStatement statement = connection.prepareStatement(query.toString());
			for (String columns : columnToGet) {
				query.append(columns + " ");
			}
			query.append("from Transaction where ");
			int count = 1;
			if (transactionDetails.getAccountId() != 0) {
				query.append("AccountId = ? ");
				count++;
			}
			if (transactionDetails.getTransactionAccountId() != 0) {
				if (count > 1) {
					query.append("AND ");
				}
				query.append("TransactionAccountId = ? ");
				count++;
			}
			if (transactionDetails.getUserId() != 0) {
				if (count > 1) {
					query.append("AND ");
				}
				query.append("UserId = ?");
			}
			if (transactionDetails.getTransactionType() != null) {
				if (count > 1) {
					query.append("AND ");
				}
				query.append("TransactionType = ?");
			}

			statement = connection.prepareStatement(query.toString());

			count = 1;
			if (transactionDetails.getAccountId() != 0) {
				statement.setLong(count++, transactionDetails.getAccountId());
			}
			if (transactionDetails.getTransactionAccountId() != 0) {
				statement.setLong(count++, transactionDetails.getTransactionAccountId());
			}
			if (transactionDetails.getUserId() != 0) {
				statement.setInt(count++, transactionDetails.getUserId());
			}
			if (transactionDetails.getTransactionType() != null) {
				statement.setString(count++, transactionDetails.getTransactionType());
			}
			System.out.println(query.toString());
//			ResultSet record = statement.executeQuery();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
	}

	@Override
	public long getId() throws InvalidInputException {
		String query = "select max(Id) from Transaction";
		long id = 0;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			try (ResultSet record = statement.executeQuery()) {
				if (record.next()) {
					id = record.getLong("max(Id)");
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience");
		}
		return id;
	}
}
