package dbLogics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import customDB.Branch;
import details.BranchDetails;
import utility.DBConnection;
import utility.InputCheck;
import utility.InvalidInputException;

public class BranchOpertaion implements Branch {

	private Connection connection = DBConnection.getConnection();

	@Override
	public int insertBranch(BranchDetails branchDetails) throws InvalidInputException {
		InputCheck.checkNull(branchDetails);
		int affectedrows = 0;
		String query = "insert into Branch(IFSCCode,Address,ManagerId,Contact) values(?,?,?,?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, branchDetails.getIfscCode());
			statement.setString(2, branchDetails.getAddress());
			statement.setInt(3, branchDetails.getManagerId());
			statement.setLong(4, branchDetails.getPhoneNumber());
			affectedrows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedrows;
	}

	@Override
	public int updateBranch(String column, Object value, int id) throws InvalidInputException {
		InputCheck.checkNull(value);
		InputCheck.checkNull(column);
		String query = "update Branch set " + column + " = ? where Id =?";
		int affectedRows = 0;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, value);
			statement.setInt(2, id);
			affectedRows = statement.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return affectedRows;
	}

	@Override
	public BranchDetails getDetails(Object value) throws InvalidInputException {
		InputCheck.checkNull(value);
		BranchDetails branchDetails = new BranchDetails();
		String query = "select * from Branch where Id = ? or IFSCCode = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, value);
			statement.setObject(2, value);
			try (ResultSet record = statement.executeQuery()) {
				if (record.next()) {
					branchDetails.setId(record.getInt("Id"));
					branchDetails.setIfscCode(record.getString("IFSCCode"));
					branchDetails.setAddress(record.getString("Address"));
					branchDetails.setManagerId(record.getInt("ManagerId"));
					branchDetails.setPhoneNumber(record.getLong("Contact"));
				}
			}
		} catch (SQLException e) {
			throw new InvalidInputException("An Error Occured , Sorry for the Inconvenience", e);
		}
		return branchDetails;
	}
}
