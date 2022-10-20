package pojos;

import java.sql.Timestamp;

public class StatementInfo {
	private String transactionType;
	private long fromAcc,toAcc,transId,userId;
	private double amount;
	private Timestamp time;
	public void setFromAcc(long accNo) {
		this.fromAcc= accNo;
	}
	public void setToAcc(long toAcc) {
		this.toAcc= toAcc;
	}
	public void setTransId(long transId) {
		this.transId= transId;
	}
	public void setAmount(double amount) {
		this.amount= amount;
	}
	public void setTransType(String type) {
		this.transactionType= type;
	}
	public void setUserId(long id) {
		this.userId = id;
	}
	public void setTime(Timestamp time) {
		this.time= time;
	}
	public long getFromAcc() {
		return fromAcc;
	}
	public long getToAcc() {
		return toAcc;
	}
	public double getAmount() {
		return amount;
	}
	public long getTransId() {
		return transId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public Timestamp getTime() {
		return time;
	}
	public long getUserId() {
		return userId;
	}
}
