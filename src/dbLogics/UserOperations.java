package dbLogics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import customDB.User;
import utility.DBConnection;
import utility.InputCheck;
import utility.InvalidInputException;

public class UserOperations implements User {

	private Connection connection = DBConnection.getConnection();

	private final String deleteUser = "update User set DeleteAt = ? where Id = ?";

	@Override
	public Object getSingleRecord(String columnToGet, String column, Object value) throws InvalidInputException {
		InputCheck.checkNull(value);
		InputCheck.checkNull(column);
		InputCheck.checkNull(columnToGet);
		String query = "select " + columnToGet + " from User where " + column + " = ?";
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
	public int deleteUser(int userID) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userID);
		int affectedRow = 0;
		try (PreparedStatement statement = connection.prepareStatement(deleteUser)) {
			statement.setDouble(1, System.currentTimeMillis());
			statement.setInt(2, userID);
			affectedRow = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRow;
	}

	@Override
	public int updateColumn(String coloumn, Object value, int userId) throws InvalidInputException {
		InputCheck.checkNull(coloumn);
		InputCheck.checkNull(value);
		InputCheck.checkNegativeInteger(userId);
		String query = "update User set " + coloumn + " = ? where Id = ?";
		int affectedRows = 0;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, value);
			statement.setInt(2, userId);
			affectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}

	// BlockedUser Table

	@Override
	public int addBlockedUser(int userId, int invalidAttempts, long time) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		InputCheck.checkNegativeInteger(invalidAttempts);
		InputCheck.checkNegativeInteger(time);
		int affectedRows = 0;
		String query = "insert into BlockedUser (UserId , InvalidAttempts , BlockedTime) values (?,?,?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, userId);
			statement.setInt(2, invalidAttempts);
			statement.setLong(3, time);
			affectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}

	@Override
	public int updateInvalidAttempt(int userId, int invalidAttempts, long time) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		InputCheck.checkNegativeInteger(invalidAttempts);
		InputCheck.checkNegativeInteger(time);
		int affectedRows = 0;
		String query = "update BlockedUser set InvalidAttempts =  ? ,BlockedTime = ? where UserId = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, invalidAttempts);
			statement.setLong(2, time);
			statement.setInt(3, userId);
			affectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}

	@Override
	public int deleteRecord(int userId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		String query = "delete from BlockedUser where UserId = ?";
		int affectedRows = 0;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, userId);
			affectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}

	@Override
	public Map<String, Object> getBlockedDetails(int userId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		String query = "select InvalidAttempts,BlockedTime from BlockedUser where UserId = ?";
		Map<String, Object> result = new HashMap<String, Object>();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, userId);
			try (ResultSet record = statement.executeQuery()) {
				if (record.next()) {
					result.put("InvalidAttempts", (int) record.getInt(1));
					result.put("BlockedTime", (long) record.getLong(2));
				} else {
					result.put("InvalidAttempts", 0);
					result.put("BlockedTime", 0);
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return result;
	}
}
