package statemets;

import java.sql.Timestamp;

public class StatementInfo {
	private String transactionType;
	private long accountNum,amount;
	private Timestamp time;
	private int transId;
	public void setAccNumber(long accNo) {
		this.accountNum= accNo;
	}
	public void setTransId(int transId) {
		this.transId= transId;
	}
	public void setAmount(long amount) {
		this.amount= amount;
	}
	public void setTransType(String type) {
		this.transactionType= type;
	}
	public void setTime(Timestamp time) {
		this.time= time;
	}
	public long getAccoutNumber() {
		return accountNum;
	}
	public long getAmount() {
		return amount;
	}
	public int getTransId() {
		return transId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public Timestamp getTime() {
		return time;
	}
}
