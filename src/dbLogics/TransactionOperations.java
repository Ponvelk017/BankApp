package dbLogics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	private final String getStatement = "select * from Transaction where  AccountId = ? || TransactionAccountId = ? and TransactionTime <= ? and "
			+ "TransactionTime > ?";

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
			statement.setString(6, transactionDetails.getTranactionType());
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
				while (record.next()) {
					transactionDetail = new TransactionDetails();
					transactionDetail.setId(transactionId);
					transactionDetail.setAccountId(record.getLong("AccountId"));
					transactionDetail.setTransactionAccountId(record.getLong("TransactionAccountId"));
					transactionDetail.setTime(record.getLong("TransactionTime"));
					transactionDetail.setTranactionType(record.getString("TransactionType"));
					transactionDetail.setTransactionStatus(record.getString("Status"));
					transactionDetail.setDescription(record.getString("Description"));
					transactionDetail.setAmount(record.getLong("Amount"));
					transactionDetail.setClosingBalance(record.getLong("ClosingBalance"));
					records.add(transactionDetail);
				}
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
				while (record.next()) {
					transactionDetail = new TransactionDetails();
					long senderAccount = record.getLong("AccountId");
					long receiverAccount = record.getLong("TransactionAccountId");
					if (senderAccount > 0) {
						transactionDetail.setAccountId(senderAccount);
					}
					if (receiverAccount > 0) {
						transactionDetail.setTransactionAccountId(receiverAccount);
					}
					transactionDetail.setTime(record.getLong("TransactionTime"));
					transactionDetail.setTranactionType(record.getString("TransactionType"));
					transactionDetail.setTransactionStatus(record.getString("Status"));
					transactionDetail.setAmount(record.getLong("Amount"));
					transactionDetail.setClosingBalance(record.getLong("ClosingBalance"));
					transactionRecords.add(transactionDetail);
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return transactionRecords;
	}

	@Override
	public long getId() {
		String query = "select max(Id) from Transaction";
		long id = 0;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			try (ResultSet record = statement.executeQuery()) {
				if (record.next()) {
					id = record.getLong("max(Id)");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
}
