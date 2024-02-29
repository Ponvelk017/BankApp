package customDB;

import java.util.List;

import details.EmployeeDetails;
import utility.InvalidInputException;

public interface Employee {

	public List<Integer> insertEmployee(List<EmployeeDetails> employees) throws InvalidInputException;

	public Object getSingleRecord(String columnToGet, String column, Object value) throws InvalidInputException;

	public int updateDetails(int Id, String column, Object value) throws InvalidInputException;
}
