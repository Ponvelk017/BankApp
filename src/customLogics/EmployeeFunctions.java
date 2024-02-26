package customLogics;

import dbLogics.EmployeeOperations;
import details.EmployeeDetails;
import utility.InputCheck;
import utility.InvalidInputException;

public class EmployeeFunctions {

	EmployeeOperations employeeOperations = new EmployeeOperations();

	public int addEmployee(EmployeeDetails employee) throws InvalidInputException {
		InputCheck.checkNull(employee);
		return employeeOperations.insertEmployee(employee);
	}
	
	public int updateRecord(int Id , String column , Object value) throws InvalidInputException {
		InputCheck.checkNegativeInteger(Id);
		InputCheck.checkNull(column);
		InputCheck.checkNull(value);
		return employeeOperations.updateDetails(Id, column, value);
	}
	
	
}
