package customLogics;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

import dbLogics.EmployeeOperations;
import details.EmployeeDetails;
import utility.Common;
import utility.InputCheck;
import utility.InvalidInputException;

public class EmployeeFunctions {

	private EmployeeOperations employeeOperations = new EmployeeOperations();

	public static int validateDetails(EmployeeDetails employee) throws InvalidInputException {
		InputCheck.checkNull(employee);
		if (employee.getName().matches("^[A-Za-z.]+") && (employee.getMobile()+"").matches("^[0-9]{10}$")
				&& employee.getEmail().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			LocalDate currentDate = LocalDate.now();
			LocalDate dob = (new Date(employee.getDOB())).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Period period = Period.between(dob, currentDate);
			if (period.getYears() >= 18 && "MaleFemale".contains(employee.getGender()) && employee.getPassword()
					.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[a-zA-Z0-9@#$%^&+=!]).{8,}$")) {
				if (employee.getBranch().matches("^[0-9A-Z]")) {
					return 1;
				}
			}
		}
		return -1;
	}

	public int addEmployee(List<EmployeeDetails> employees) throws InvalidInputException {
		InputCheck.checkNull(employees);
		for (EmployeeDetails employee : employees) {
			if (EmployeeFunctions.validateDetails(employee) == -1) {
				return -1;
			}
		}
		List<Integer> result = employeeOperations.insertEmployee(employees);
		for (Integer singleRecord : result) {
			if (singleRecord == 0) {
				return 0;
			}
		}
		return 1;
	}

	public int updateRecord(int Id, String column, Object value) throws InvalidInputException {
		InputCheck.checkNegativeInteger(Id);
		InputCheck.checkNull(column);
		InputCheck.checkNull(value);
		if (column.equals("DOB") || column.equals("JoinDate")) {
			value = Common.dateToMilli(value.toString());
		}
		return employeeOperations.updateDetails(Id, column, value);
	}

}
