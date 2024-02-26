package dbLogics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import customDB.Employee;
import details.EmployeeDetails;
import utility.Common;
import utility.DBConnection;
import utility.InputCheck;
import utility.InvalidInputException;

public class EmployeeOperations implements Employee {

	private Connection connection = DBConnection.getConnection();

	UserOperations user = new UserOperations();

	private String insertEmployee = "insert into User (Name,DOB,Mobile,Email,Gender,Password) values (?,?,?,?,?,?)";

	@Override
	public int insertEmployee(EmployeeDetails employee) throws InvalidInputException {
		InputCheck.checkNull(employee);
		int affecteedRows = 0;
		try (PreparedStatement statement = connection.prepareStatement(insertEmployee)) {
			statement.setString(1, employee.getName());
			statement.setDouble(2, Common.dateToMilli(employee.getDob()));
			statement.setString(3, employee.getMobile());
			statement.setString(4, employee.getEmail());
			statement.setString(5, employee.getGender());
			statement.setString(6, employee.getPassword());
			insertEmployee = "insert into Employee values (?,?,?,?)";
			try (PreparedStatement empStatement = connection.prepareStatement(insertEmployee,
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				empStatement.setString(2, employee.getBranch());
				empStatement.setBoolean(3, employee.getIsAdmin());
				empStatement.setDouble(4, Common.dateToMilli(employee.getJoinDate()));
				statement.executeUpdate();
				try (ResultSet record = statement.getGeneratedKeys()) {
					if (record.next()) {
						int employeeId = record.getInt(1);
						empStatement.setInt(1, employeeId);
						affecteedRows = empStatement.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affecteedRows;
	}

	@Override
	public Object getSingleRecord(String columnToGet, String column, Object value) throws InvalidInputException {
		InputCheck.checkNull(value);
		InputCheck.checkNull(column);
		InputCheck.checkNull(columnToGet);
		String query = "select " + columnToGet + " from Employee where " + column + " = ?";
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
	public int updateDetails(int Id, String column, Object value) throws InvalidInputException {
		InputCheck.checkNegativeInteger(Id);
		InputCheck.checkNull(column);
		InputCheck.checkNull(value);
		if (column.equals("DOB") || column.equals("JoinDate")) {
			value = Common.dateToMilli(value.toString());
		}
		String query = "update User join Employee on User.Id = Employee.ID set " + column + " = ? where User.Id = ?";
		int affectedRows = 0;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, value);
			statement.setInt(2, Id);
			affectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}
}
