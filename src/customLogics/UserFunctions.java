 package customLogics;

import java.util.Map;

import dbLogics.EmployeeOperations;
import dbLogics.UserOperations;
import details.CustomerDetails;
import utility.InputCheck;
import utility.InvalidInputException;

public class UserFunctions {

	private UserOperations userOpertaion = new UserOperations();
	private EmployeeOperations employee = new EmployeeOperations();

	public boolean login(int userId, String password) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		InputCheck.checkNull(password);
		boolean result = false;
		String dbPassword = (String) userOpertaion.getSingleRecord("Password", "Id", userId);
		if (dbPassword != null && dbPassword.equals(password)) {
			result = true;
		}
		return result;
	}
	public int isUser(int userId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		if(userOpertaion.getSingleRecord("Id", "Id", userId) == null) {
			return -1;
		}
		return 1;
	}

	public String getDesignation(int userId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		String isUser = (String) userOpertaion.getSingleRecord("Type", "Id", userId);
		if (!isUser.equals("Customer")) {
			boolean isAdmin = ((int) employee.getSingleRecord("IsAdmin", "Id", userId) == 1) ? true : false;
			if (isAdmin) {
				return "Manager";
			} else {
				return "Employee";
			}
		}
		return "Customer";
	}

	public String getPassword(int userId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		return (String) userOpertaion.getSingleRecord("Password", "Id", userId);
	}

	public String getType(int userId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		return (String) userOpertaion.getSingleRecord("Type", "Id", userId);
	}

	public int coloumnUpdation(String column, Object value, int userId) throws InvalidInputException {
		InputCheck.checkNull(column);
		InputCheck.checkNull(value);
		InputCheck.checkNegativeInteger(userId);
		return userOpertaion.updateColumn(column, value, userId);
	}

	public int deleteUser(int userId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		return userOpertaion.deleteUser(userId);
	}
	//
	//
	//
	//BlockedUser Table
	//
	// 
	//

	public int addBlockedUser(int userId, int InvalidAttempts, long timeOfBlocked) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		InputCheck.checkNegativeInteger(InvalidAttempts);
		InputCheck.checkNegativeInteger(timeOfBlocked);
		if (InvalidAttempts > 1) {
			return userOpertaion.updateInvalidAttempt(userId , InvalidAttempts , timeOfBlocked);
		} else {
			return userOpertaion.addBlockedUser(userId, InvalidAttempts, timeOfBlocked);
		}
	}

	public Map<String, Object> blockedDetails(int userId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		return userOpertaion.getBlockedDetails(userId);

	}
	
	public int deleteBlockedUser(int userId)  throws InvalidInputException {
		InputCheck.checkNegativeInteger(userId);
		return userOpertaion.deleteRecord(userId);
	}
}
