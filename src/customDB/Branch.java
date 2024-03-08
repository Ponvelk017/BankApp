package customDB;

import details.BranchDetails;
import utility.InvalidInputException;

public interface Branch {

	public int insertBranch(BranchDetails branchDetails) throws InvalidInputException;

	public int updateBranch(String column, Object value, int id) throws InvalidInputException;

	public BranchDetails getDetails(Object value) throws InvalidInputException;

}
