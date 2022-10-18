package pojos;

public class ApprovalInfo {
	long userId,accountNum,approvalId;
	double amount;
	String status;
	public void setUserId(long userId) {
		this.userId= userId;
	}
	public void setApprovalId(long approvalId) {
		this.approvalId= approvalId;
	}
	public void setAccountNumber(long accNo) {
		this.accountNum= accNo;
	}
	public void setAmount(double amount) {
		this.amount= amount;
	}
	public void setStatus(String status) {
		this.status= status;
	}
	public long getUserId() {
		return userId;
	}
	public long getApprovalId() {
		return approvalId;
	}
	public long getAccoutNumber() {
		return accountNum;
	}
	public double getAmount() {
		return amount;
	}
	public String getStatus() {
		return status;
	}
}
