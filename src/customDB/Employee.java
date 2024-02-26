package customDB;

import details.EmployeeDetails;
import utility.InvalidInputException;

public interface Employee {

	public int insertEmployee(EmployeeDetails employee) throws InvalidInputException;

	public Object getSingleRecord(String columnToGet, String column, Object value) throws InvalidInputException;

	public int updateDetails(int Id, String column, Object value) throws InvalidInputException;
}
