package dbLogics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customDB.Customer;
import details.CustomerDetails;
import utility.DBConnection;
import utility.InputCheck;
import utility.InvalidInputException;

public class CustomerOperations implements Customer {

	private Connection connection = DBConnection.getConnection();

	private String insertUser = "insert into User (Name,DOB,Mobile,Email,Gender,Password) values (?,?,?,?,?,?)";
	private String insertCustomer = "insert into Customer values (?,?,?,?)";
	private final String getProfile = "select User.*,Customer.Address,Customer.Aadhar,Customer.Pan from User "
			+ "left join Customer on User.Id = Customer.Id where User.Id = ?";
	private String mappingDetails = "select * from User left join Customer on User.Id = Customer.Id";

	private Map<String, String> mappingRecords = new HashMap<String, String>();

	public void getMappingDetails() throws InvalidInputException {
		try (PreparedStatement statement = connection.prepareStatement(mappingDetails);
				ResultSet allColumns = statement.executeQuery()) {
			ResultSetMetaData metadata = allColumns.getMetaData();
			int columns = ((ResultSetMetaData) metadata).getColumnCount();
			for (int i = 1; i <= columns; i++) {
				String tempColumn = (String) metadata.getColumnName(i);
				String field = "set" + ((tempColumn).charAt(0) + "").toUpperCase()
						+ tempColumn.substring(1, tempColumn.length());
				mappingRecords.put(tempColumn, field);
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
	}

	@Override
	public List<Integer> insertCustomer(List<CustomerDetails> customers) throws InvalidInputException {
		InputCheck.checkNull(customers);
		int affectedRows = 0, customerId = 0;
		List<Integer> result = new ArrayList<Integer>();
		for (CustomerDetails customer : customers) {
			try (PreparedStatement statement = connection.prepareStatement(insertUser,
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				connection.setAutoCommit(false);
				statement.setString(1, customer.getName());
				statement.setLong(2, customer.getDOB());
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
					connection.commit();
					result.add(affectedRows);
				}
			} catch (SQLException e) {
				try {
					if (connection != null) {
						connection.rollback();
					}
				} catch (SQLException e1) {
					throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e1);
				}
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
			}

		}
		return result;
	}

	@Override
	public int updateDetails(int Id, String column, Object value) throws InvalidInputException {
		InputCheck.checkNegativeInteger(Id);
		InputCheck.checkNull(column);
		InputCheck.checkNull(value);
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
	public List<CustomerDetails> getProfile(int customerId) throws InvalidInputException {
		InputCheck.checkNegativeInteger(customerId);
		getMappingDetails();
		List<CustomerDetails> records = new ArrayList<CustomerDetails>();
		try (PreparedStatement statement = connection.prepareStatement(getProfile)) {
			statement.setInt(1, customerId);
			try (ResultSet record = statement.executeQuery()) {
				ResultSetMetaData metadata = record.getMetaData();
				int columns = metadata.getColumnCount();
				Method[] userMethods = CustomerDetails.class.getMethods();
				List<String> customerMethodsList = new ArrayList<String>();
				for (Method temp : userMethods) {
					customerMethodsList.add(temp.toString());
				}
				CustomerDetails customerDetails = (CustomerDetails) CustomerDetails.class.getDeclaredConstructor()
						.newInstance();
				while (record.next()) {
					for (int i = 1; i <= columns; i++) {
						String columnName = metadata.getColumnName(i);
						String dataType = metadata.getColumnTypeName(i);
						Method method;
						if (dataType.equals("INT")) {
							method = CustomerDetails.class.getMethod(mappingRecords.get(columnName), int.class);
							method.invoke(customerDetails, record.getInt(i));
						} else if (dataType.equals("VARCHAR") || dataType.equals("ENUM") || dataType.equals("CHAR")) {
							method = CustomerDetails.class.getMethod(mappingRecords.get(columnName), String.class);
							method.invoke(customerDetails, record.getString(i));
						} else if (!columnName.equals("DeleteAt") && dataType.equals("MEDIUMTEXT") || dataType.equals("BIGINT")) {
							method = CustomerDetails.class.getMethod(mappingRecords.get(columnName), long.class);
							method.invoke(customerDetails, record.getLong(i));
						}
					}
					records.add(customerDetails);
				}
			} catch (SecurityException | IllegalArgumentException | SQLException | InstantiationException
					| IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
			}
			return records;
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
	}
}
