package details;

public class AccountInfo {
	private String branch,accountType;
	private long accountNum;
	public void setAccountNumber(long accNo) {
		this.accountNum= accNo;
	}
	public void setBranch(String branch) {
		this.branch= branch;
	}
	public void setAccType(String type) {
		this.accountType= type;
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
