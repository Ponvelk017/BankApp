package details;

public class TransactionDetails {

	private long id;
	private long accountId;
	private long transactionAccountId;
	private int userId;
	private String tranactionType;
	private String transactionStatus;
	private String description;
	private long amount;
	private long closingBalance;
	private long time;

	public String getTranactionType() {
		return tranactionType;
	}

	public void setTranactionType(String tranactionType) {
		this.tranactionType = tranactionType;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getIFSCCode() {
		return IFSCCode;
	}

	public void setIFSCCode(String iFSCCode) {
		IFSCCode = iFSCCode;
	}

	private String IFSCCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getTransactionAccountId() {
		return transactionAccountId;
	}

	public void setTransactionAccountId(long transactionAccountId) {
		this.transactionAccountId = transactionAccountId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTransactionType() {
		return tranactionType;
	}

	public void setTransactionType(String tranactionType) {
		this.tranactionType = tranactionType;
	}

	public String getStatus() {
		return transactionStatus;
	}

	public void setStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(long closingBalance) {
		this.closingBalance = closingBalance;
	}

	public long getTransactionTime() {
		return time;
	}

	public void setTransactionTime(long time) {
		this.time = time;
	}

}
