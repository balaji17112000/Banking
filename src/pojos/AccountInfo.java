package pojos;

public class AccountInfo {
	private String branch,accountType;
	long userId;
	private long accountNum;

	public void setUserId(long userId) {
		this.userId= userId;
	}
	public void setAccountNumber(long accNo) {
		this.accountNum= accNo;
	}
	public void setBranch(String branch) {
		this.branch= branch;
	}
	public void setAccType(String type) {
		this.accountType= type;
	}
	public long getUserId() {
		return userId;
	}
	public long getAccoutNumber() {
		return accountNum;
	}
	public String getBranch() {
		return branch;
	}
	public String getAccountType() {
		return accountType;
	}
}
