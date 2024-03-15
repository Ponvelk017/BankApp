package dbLogics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import customDB.Employee;
import details.EmployeeDetails;
import utility.DBConnection;
import utility.InputCheck;
import utility.InvalidInputException;

public class EmployeeOperations implements Employee {

	private Connection connection = DBConnection.getConnection();

	private String insertUser = "insert into User (Name,DOB,Mobile,Email,Gender,Password) values (?,?,?,?,?,?)";
	private String insertEmployee = "insert into Employee values (?,?,?,?)";

	@Override
	public List<Integer> insertEmployee(List<EmployeeDetails> employees) throws InvalidInputException {
		InputCheck.checkNull(employees);
		int affectedRows = 0, employeeId = 0;
		List<Integer> result = new ArrayList<Integer>();
		for (EmployeeDetails employee : employees) {
			try (PreparedStatement statement = connection.prepareStatement(insertUser,
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				connection.setAutoCommit(false);
				statement.setString(1, employee.getName());
				statement.setLong(2, employee.getDOB());
				statement.setString(3, employee.getMobile());
				statement.setString(4, employee.getEmail());
				statement.setString(5, employee.getGender());
				statement.setString(6, employee.getPassword());
				statement.executeUpdate();
				try (ResultSet record = statement.getGeneratedKeys()) {
					if (record.next()) {
						employeeId = record.getInt(1);
					}
				}
				try (PreparedStatement empStatement = connection.prepareStatement(insertEmployee)) {
					empStatement.setInt(1, employeeId);
					empStatement.setString(2, employee.getBranch());
					empStatement.setBoolean(3, employee.getAdmin());
					affectedRows = empStatement.executeUpdate();
					result.add(affectedRows);
					connection.commit();
				}
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e1);
				}
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
			}
		}
		return result;
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
