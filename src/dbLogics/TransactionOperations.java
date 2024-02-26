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

	Connection connection = DBConnection.getConnection();
	TransactionDetails transactionDetail;

	private final String setTransferTransaction = "insert into Transaction(AccountId,TransactionAccountId,UserId,TransactionTime,TransactionType,Description,Amount,ClosingBalance) "
			+ "values(?,?,?,?,?,?,?,?)";
	private final String getTransferTransaction = "select * from Transaction where Id = ?";
	private final String getStatement = "select * from Transaction where  AccountId = ? || TransactionAccountId = ? and TransactionTime <= ? and "
			+ "TransactionTime > ?";

	@Override
	public int setTransferTransaction(TransactionDetails transactionDetails) throws InvalidInputException {
		InputCheck.checkNull(transactionDetails);
		int transactionId = 0, affectedRows = 0;
		transactionDetail = new TransactionDetails();
		try (PreparedStatement statement = connection.prepareStatement(setTransferTransaction,
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setLong(1, transactionDetails.getAccountId());
			statement.setLong(2, transactionDetails.getTransactionAccountId());
			statement.setInt(3, transactionDetails.getUserId());
			statement.setLong(4, transactionDetails.getTime());
			statement.setString(5, transactionDetails.getTranactionType());
			statement.setString(6, transactionDetails.getDescription());
			statement.setLong(7, transactionDetails.getAmount());
			statement.setLong(8, transactionDetails.getClosingBalance());
			affectedRows = statement.executeUpdate();
			if (affectedRows > 0) {
				try (ResultSet record = statement.getGeneratedKeys()) {
					if (record.next()) {
						transactionId = record.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return transactionId;
	}

	@Override
	public TransactionDetails getTransferTransaction(long transactionId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(transactionId);
		transactionDetail = new TransactionDetails();
		try (PreparedStatement statement = connection.prepareStatement(getTransferTransaction)) {
			statement.setLong(1, transactionId);
			try (ResultSet record = statement.executeQuery()) {
				if (record.next()) {
					transactionDetail.setId(transactionId);
					transactionDetail.setAccountId(record.getLong("AccountId"));
					transactionDetail.setTransactionAccountId(record.getLong("TransactionAccountId"));
					transactionDetail.setTime(record.getLong("TransactionTime"));
					transactionDetail.setTranactionType(record.getString("TransactionType"));
					transactionDetail.setTransactionStatus(record.getString("Status"));
					transactionDetail.setAmount(record.getLong("Amount"));
					transactionDetail.setClosingBalance(record.getLong("ClosingBalance"));
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return transactionDetail;
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
}
