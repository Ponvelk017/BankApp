package dbLogics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import customDB.Account;
import details.AccountDetails;
import utility.DBConnection;
import utility.InputCheck;
import utility.InvalidInputException;

public class AccountOperations implements Account {

	Connection connection = DBConnection.getConnection();

	private final String createAccount = "insert into Account(UserId , BranchId , AccountType) values (?,?,?)";
	private final String getAccountDetails = "select * from Account where UserId = ? ";
	private final String getAvailableAccount = "select AccountNumber from Account where UserId = ? ";

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

	@Override
	public List<AccountDetails> getAccountDetails(int userId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		List<AccountDetails> userAccount = new ArrayList<AccountDetails>();
		try (PreparedStatement statement = connection.prepareStatement(getAccountDetails)) {
			statement.setInt(1, userId);
			try (ResultSet record = statement.executeQuery()) {
				while (record.next()) {
					AccountDetails tempAccount = new AccountDetails();
					tempAccount.setAccountNo(record.getLong("AccountNumber"));
					tempAccount.setUserId(record.getInt("UserId"));
					tempAccount.setBranchId(record.getString("BranchId"));
					tempAccount.setAccountStatus(record.getString("Status"));
					tempAccount.setBalance(record.getLong("Balance"));
					tempAccount.setAccountType(record.getString("AccountType"));
					userAccount.add(tempAccount);
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return userAccount;
	}

	@Override
	public List<Long> getAvailableAccount(int userId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		List<Long> availableAccounts = new ArrayList<Long>();
		try (PreparedStatement statement = connection.prepareStatement(getAvailableAccount)) {
			statement.setInt(1, userId);
			try (ResultSet record = statement.executeQuery()) {
				while (record.next()) {
					availableAccounts.add(record.getLong("AccountNumber"));
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return availableAccounts;
	}

	@Override
	public int updateColumn(String column ,Object DepositeAmount , long accountNumber) throws InvalidInputException {
		InputCheck.checkNull(column);
		InputCheck.checkNull(DepositeAmount);
		InputCheck.checkNull(accountNumber);
		int affectedRows = 0;
		String query = "update Account set "+column+" = ? where AccountNumber = ?";
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
