package details;

import java.io.Serializable;

public class AccountDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private long accountNumber;
	private int userId;
	private String branchId;
	private long balance;
	private String accountStatus;
	private String accountType;

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public String getStatus() {
		return accountStatus;
	}

	public void setStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
}
