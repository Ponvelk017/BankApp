package customLogics;

import dbLogics.BranchOpertaion;
import details.BranchDetails;
import utility.InputCheck;
import utility.InvalidInputException;

public class BranchFunction {

	BranchOpertaion branchOperation = new BranchOpertaion();

	public int addBranch(BranchDetails branch) throws InvalidInputException {
		InputCheck.checkNull(branch);
		return branchOperation.insertBranch(branch);
	}

	public int updateRecord(String column, Object value, int id) throws InvalidInputException {
		InputCheck.checkNull(value);
		InputCheck.checkNull(column);
		InputCheck.checkNegativeInteger(id);
		return branchOperation.updateBranch(column, value, id);
	}

	public BranchDetails getBranchDetails(Object value) throws InvalidInputException {
		InputCheck.checkNull(value);
		return branchOperation.getDetails(value);
	}
}
