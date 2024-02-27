package customDB;

import java.util.Map;

import utility.InvalidInputException;

public interface User {

	public Object getSingleRecord(String columnToGet, String column, Object value) throws InvalidInputException;

	public int deleteUser(int userId) throws InvalidInputException;

	public int updateColumn(String coloumn, Object value, int userId) throws InvalidInputException;

	// BlockedUser

	public Map<String, Object> getBlockedDetails(int userId) throws InvalidInputException;

	public int addBlockedUser(int userId, int invalidAttempts, long time) throws InvalidInputException;

	public int updateInvalidAttempt(int userId, int invalidAttempts, long time) throws InvalidInputException;

	public int deleteRecord(int userId) throws InvalidInputException;
}
