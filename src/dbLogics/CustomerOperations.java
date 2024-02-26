package dbLogics;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import customDB.Customer;
import details.CustomerDetails;
import utility.Common;
import utility.DBConnection;
import utility.InputCheck;
import utility.InvalidInputException;

public class CustomerOperations implements Customer {

	Connection connection = DBConnection.getConnection();
	UserOperations user = new UserOperations();
	CustomerDetails customerDet = new CustomerDetails();

	private String insertCustomer= "insert into User (Name,DOB,Mobile,Email,Gender,Password) values (?,?,?,?,?,?)";
	private final String getProfile = "select User.*,Customer.Address,Customer.Aadhar,Customer.Pan from User left join \"\n"
			+ "				+ \"Customer on User.Id = Customer.Id where User.Id = ?; "; 
	
	@Override
	public int insertCustomer(CustomerDetails customer) throws InvalidInputException {
		InputCheck.checkNull(customer);
		int affectedRows = 0, customerId = 0;
		try (PreparedStatement statement = connection.prepareStatement(insertCustomer,
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, customer.getName());
			statement.setDouble(2, Common.dateToMilli(customer.getDob()));
			statement.setString(3, customer.getMobile());
			statement.setString(4, customer.getEmail());
			statement.setString(5, customer.getGender());
			statement.setString(6, customer.getPassword());
			statement.executeUpdate();
			try (ResultSet record = statement.getGeneratedKeys()) {
				if (record.next()) {
					customerId = record.getInt(1);
				}
			}
			insertCustomer = "insert into Customer values (?,?,?,?)";
			try (PreparedStatement empStatement = connection.prepareStatement(insertCustomer)) {
				empStatement.setInt(1, customerId);
				empStatement.setString(2, customer.getAadhar());
				empStatement.setString(3, customer.getPan());
				empStatement.setString(4, customer.getAddress());
				affectedRows = empStatement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}

	@Override
	public int updateDetails(int Id, String column, Object value) throws InvalidInputException {
		InputCheck.checkNegativeInteger(Id);
		InputCheck.checkNull(column);
		InputCheck.checkNull(value);
		if (column.equals("DOB")) {
			value = Common.dateToMilli(value.toString());
		}
		String query = "update User join Customer on User.Id = Customer.ID set " + column + " = ? where User.Id = ?";
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

	@Override
	public CustomerDetails getProfile(int customerId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(customerId);
		try (PreparedStatement statement = connection.prepareStatement(getProfile)) {
			statement.setInt(1, customerId);
			try (ResultSet record = statement.executeQuery()) {
				if (record.next()) {
					customerDet.setName(record.getString("Name"));
					customerDet.setDob(new Date(record.getLong("DOB")).toString());
					customerDet.setMobile(record.getString("Mobile"));
					customerDet.setEmail(record.getString("Email"));
					customerDet.setGender(record.getString("Gender"));
					customerDet.setAddress(record.getString("Address"));
					customerDet.setAadhar(record.getString("Aadhar"));
					customerDet.setPan(record.getString("Pan"));
					customerDet.setType(record.getString("Type"));
					customerDet.setStatus(record.getString("Status"));
					customerDet.setDeleteAt(new Date(record.getLong("DeleteAt")).toString());
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return customerDet;
	}

}
