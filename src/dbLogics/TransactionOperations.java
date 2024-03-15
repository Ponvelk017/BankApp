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
import utility.DBConnection;
import utility.InputCheck;
import utility.InvalidInputException;

public class TransactionOperations implements Transaction {

	private Connection connection = DBConnection.getConnection();
	private AccountOperations accountOperation = new AccountOperations();

	private final String setTransferTransaction = "insert into Transaction(AccountId,TransactionAccountId,UserId,TransactionTime,TransactionType,Description,Amount,ClosingBalance,IFSCCode) "
			+ "values(?,?,?,?,?,?,?,?,?)";
	private final String setTransferTransactionWithId = "insert into Transaction(Id,AccountId,TransactionAccountId,UserId,TransactionTime,TransactionType,Description,Amount,ClosingBalance) "
			+ "values(?,?,?,?,?,?,?,?,?)";

	private Map<String, String> mappingRecords = new HashMap<String, String>();

	private void getMappingDetails() throws InvalidInputException {
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
			while (record.next()) {
				TransactionDetails transactionDetails = (TransactionDetails) TransactionDetails.class
						.getDeclaredConstructor().newInstance();
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
		try (PreparedStatement statement = connection.prepareStatement(setTransferTransaction)) {
			statement.setLong(1, transactionDetails.getAccountId());
			statement.setLong(2, transactionDetails.getTransactionAccountId());
			statement.setInt(3, transactionDetails.getUserId());
			statement.setLong(4, transactionDetails.getTransactionTime());
			statement.setString(5, transactionDetails.getTransactionType());
			statement.setString(6, transactionDetails.getDescription() + " ");
			statement.setLong(7, transactionDetails.getAmount());
			statement.setLong(8, transactionDetails.getClosingBalance());
			statement.setString(9, transactionDetails.getIFSCCode());
			affectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}

	@Override
	public int setTransferTransactionWithId(TransactionDetails transactionDetails) throws InvalidInputException {
		InputCheck.checkNull(transactionDetails);
		int affectedRows = 0;
		try (PreparedStatement statement = connection.prepareStatement(setTransferTransactionWithId)) {
			statement.setLong(1, transactionDetails.getAccountId());
			statement.setLong(2, transactionDetails.getTransactionAccountId());
			statement.setInt(3, transactionDetails.getUserId());
			statement.setLong(4, transactionDetails.getTransactionTime());
			statement.setString(5, transactionDetails.getTransactionType());
			statement.setString(6, transactionDetails.getDescription() + " ");
			statement.setLong(7, transactionDetails.getAmount());
			statement.setLong(8, transactionDetails.getClosingBalance());
			affectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}

	@Override
	public List<TransactionDetails> getTransferTransaction(long transactionId, String ConditionColumn)
			throws InvalidInputException {
		InputCheck.checkNegativeInteger(transactionId);
		InputCheck.checkNull(ConditionColumn);
		List<TransactionDetails> records = new ArrayList<TransactionDetails>();
		String query = "select * from Transaction where " + ConditionColumn + "= ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
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
	public List<TransactionDetails> getCustomData(TransactionDetails transactionDetails, List<String> columnToGet,
			Map<String, Object> condition) throws InvalidInputException {
		InputCheck.checkNull(transactionDetails);
		InputCheck.checkNull(columnToGet);
		InputCheck.checkNull(condition);
		List<TransactionDetails> result = new ArrayList<TransactionDetails>();
		StringBuilder query = new StringBuilder("select ");
		for (String columns : columnToGet) {
			query.append(columns + " ,");
		}
		try {
			PreparedStatement statement = connection.prepareStatement(query.toString());
			query = new StringBuilder(query.subSequence(0, query.length() - 1));
			query.append("from Transaction where ");
			int count = 1;
			if (transactionDetails.getAccountId() != 0) {
				query.append("AccountId = ? ");
				count++;
			}
			if (transactionDetails.getUserId() != 0) {
				if (count > 1) {
					query.append("AND ");
				}
				query.append("UserId = ? ");
				count++;
			}
			if (transactionDetails.getTransactionAccountId() != 0) {
				if (count > 1) {
					query.append("AND ");
				}
				query.append("TransactionAccountId = ? ");
				count++;
			}
			if (transactionDetails.getTransactionType() != null) {
				if (count > 1) {
					query.append("AND ");
				}
				query.append("TransactionType = ?");
			}
			if ((long) condition.get("From") != 0 && (long) condition.get("To") != 0) {
				if (count > 1) {
					query.append(" AND ");
				}
				query.append("TransactionTime between ? and ?");
				count++;
			}
			if (condition.get("Sort") != null) {
				query.append(" order by TransactionTime " + condition.get("Sort"));
			}
			query.append(" limit 150");
			statement = connection.prepareStatement(query.toString());

			count = 1;
			if (transactionDetails.getUserId() != 0) {
				statement.setLong(count++, transactionDetails.getUserId());
			}
			if (transactionDetails.getAccountId() != 0) {
				statement.setLong(count++, transactionDetails.getAccountId());
			}
			if (transactionDetails.getTransactionAccountId() != 0) {
				statement.setLong(count++, transactionDetails.getTransactionAccountId());
			}
			if (transactionDetails.getTransactionType() != null) {
				statement.setString(count++, transactionDetails.getTransactionType());
			}
			if ((long) condition.get("From") != 0 && (long) condition.get("To") != 0) {
				statement.setLong(count++, (long) condition.get("From"));
				statement.setLong(count++, (long) condition.get("To"));
			}
			ResultSet record = statement.executeQuery();
			result = setDetails(record);
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return result;
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

	@Override
	public long deposite(TransactionDetails transactionDetails, boolean autoCommitFlag) throws InvalidInputException {
		InputCheck.checkNull(transactionDetails);
		InputCheck.checkNull(autoCommitFlag);
		try {
			if (autoCommitFlag) {
				connection.setAutoCommit(false);
			}
			long balance = (long) accountOperation.getSingleRecord("Balance", "AccountNumber",
					transactionDetails.getAccountId());
			int affectedRows = accountOperation.updateColumn("Balance", transactionDetails.getAmount() + balance,
					transactionDetails.getAccountId());
			TransactionDetails transactionDet = new TransactionDetails();
			if (affectedRows > 0) {
				transactionDet.setAccountId(transactionDetails.getAccountId());
				transactionDet.setTransactionAccountId(transactionDetails.getTransactionAccountId());
				transactionDet.setUserId((int) accountOperation.getSingleRecord("UserId", "AccountNumber",
						transactionDetails.getAccountId()));
				transactionDet.setTransactionTime(System.currentTimeMillis());
				transactionDet.setTransactionType("Deposit");
				transactionDet.setAmount(transactionDetails.getAmount());
				transactionDet.setClosingBalance((long) accountOperation.getSingleRecord("Balance", "AccountNumber",
						transactionDetails.getAccountId()));
				if (transactionDetails.getId() > 0) {
					transactionDet.setId(transactionDetails.getId());
					affectedRows = setTransferTransactionWithId(transactionDetails);
				} else {
					affectedRows = setTransferTransaction(transactionDet);
				}
			}
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e1);
			}
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		} finally {
			try {
				if (autoCommitFlag) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
			}
		}
		return getId();
	}

	@Override
	public long withdraw(TransactionDetails transactionDetails, boolean autoCommitFlag) throws InvalidInputException {
		InputCheck.checkNull(transactionDetails);
		InputCheck.checkNull(autoCommitFlag);
		try {
			if (autoCommitFlag) {
				connection.setAutoCommit(false);
			}
			long balance = (long) accountOperation.getSingleRecord("Balance", "AccountNumber",
					transactionDetails.getAccountId());
			int affectedRows = 0;
			TransactionDetails transactionDet = new TransactionDetails();
			if (balance > transactionDetails.getAmount()) {
				affectedRows = accountOperation.updateColumn("Balance", balance - transactionDetails.getAmount(),
						transactionDetails.getAccountId());
				if (affectedRows > 0) {
					transactionDet.setAccountId(transactionDetails.getAccountId());
					transactionDet.setTransactionAccountId(transactionDetails.getTransactionAccountId());
					transactionDet.setUserId((int) accountOperation.getSingleRecord("UserId", "AccountNumber",
							transactionDetails.getAccountId()));
					transactionDet.setTransactionTime(System.currentTimeMillis());
					transactionDet.setTransactionType("Withdraw");
					transactionDet.setDescription(transactionDetails.getDescription());
					transactionDet.setAmount(transactionDetails.getAmount());
					transactionDet.setClosingBalance((long) accountOperation.getSingleRecord("Balance", "AccountNumber",
							transactionDetails.getAccountId()));
					affectedRows = setTransferTransaction(transactionDet);
				}
			}
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e1);
			}
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		} finally {
			try {
				if (autoCommitFlag) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
			}
		}
		return getId();
	}

	@Override
	public Map<String, Integer> transferWithinBank(TransactionDetails transactionDetails) throws InvalidInputException {
		InputCheck.checkNull(transactionDetails);
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {
			connection.setAutoCommit(false);

			TransactionDetails receiverDet = new TransactionDetails();
			receiverDet.setAccountId(transactionDetails.getTransactionAccountId());
			receiverDet.setTransactionAccountId(transactionDetails.getAccountId());
			receiverDet.setAmount(transactionDetails.getAmount());
			long receiverUpdation = deposite(receiverDet, false);

			TransactionDetails senderDet = new TransactionDetails();
			senderDet.setId(getId());
			senderDet.setAccountId(transactionDetails.getAccountId());
			senderDet.setTransactionAccountId(transactionDetails.getTransactionAccountId());
			senderDet.setDescription(transactionDetails.getDescription());
			senderDet.setAmount(transactionDetails.getAmount());
			long senderUpdation = withdraw(senderDet, false);

			long senderBalance = (long) accountOperation.getSingleRecord("Balance", "AccountNumber",
					transactionDetails.getAccountId());
			result.put("SuffientBalance", (senderBalance >= transactionDetails.getAmount()) ? 1 : 0);
			result.put("SenderTransactionid", (int) senderUpdation);
			result.put("ReciverTransactionid", (int) receiverUpdation);
			connection.commit();
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e1);
			}
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
			}
		}
		return result;
	}

	@Override
	public long transferOtherBank(long senderAcc, long receiverAcc, long amount, String description , String ifsc)
			throws InvalidInputException {
		InputCheck.checkNegativeInteger(senderAcc);
		InputCheck.checkNegativeInteger(receiverAcc);
		InputCheck.checkNegativeInteger(amount);
		InputCheck.checkNull(description);
		InputCheck.checkNull(ifsc);
		try {
			connection.setAutoCommit(false);
			long senderBalance = (long) accountOperation.getSingleRecord("Balance", "AccountNumber", senderAcc);
			int senderBalanceUpdation = 0;
			TransactionDetails transactionDetails = new TransactionDetails();
			if (senderBalance >= amount) {
				senderBalanceUpdation = accountOperation.updateColumn("Balance", senderBalance - amount, senderAcc);
				if (senderBalanceUpdation > 0) {
					transactionDetails.setAccountId(senderAcc);
					transactionDetails.setTransactionAccountId(receiverAcc);
					transactionDetails
							.setUserId((int) accountOperation.getSingleRecord("UserId", "AccountNumber", senderAcc));
					transactionDetails.setTransactionTime(System.currentTimeMillis());
					transactionDetails.setTransactionType("Withdraw");
					transactionDetails.setDescription(description);
					transactionDetails.setAmount(amount);
					transactionDetails.setClosingBalance(senderBalance - amount);
					transactionDetails.setIFSCCode(ifsc);
					setTransferTransaction(transactionDetails);
				}
			}
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e1);
			}
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
			}
		}
		return getId();
	}
}
