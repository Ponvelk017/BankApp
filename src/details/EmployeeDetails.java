package details;

public class EmployeeDetails extends UserDetails {
	private static final long serialVersionUID = 1L;
	private String branch;
	private Boolean admin;

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean isAdmin) {
		this.admin = isAdmin;
	}

}
