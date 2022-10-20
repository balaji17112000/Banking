package users;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import pojos.AccountInfo;
import pojos.ApprovalInfo;
import pojos.CustomerInfo;
import pojos.StatementInfo;
import util.Check;
import util.KeyException;

public class Admin extends Users{
	public List<Long>  getAccountNum(long id) throws KeyException{
		Check.nullCheck(id);
		List<Long> accNum=account.getAccountNumber(id);
		Check.nullCheck(accNum);
		return accNum;
	}
	public Map<Object,StatementInfo> getLogStatements()throws KeyException{
		return account.getStatements();
	}
	public Map<Object,AccountInfo> getAccounts() throws KeyException{
		return account.getAccounts();
	}
	public void updateUserPassword(long id,long adminId, String colName, String newPassword, String oldPassword) throws KeyException {
		if(adminId!=id){
			throw new KeyException("Action Blocked");
		}
		if(!account.updatePassword(id, newPassword,oldPassword)) {
			throw new KeyException("Incorrect Password");
		}
	}
	public boolean accountCreate(AccountInfo accountDetails,CustomerInfo customerDetails) throws KeyException{
		return account.createAccount(accountDetails,customerDetails);
	}
	public boolean changeStatusCustomerAccount(long accNo,String status) throws KeyException {
		return account.changeAccountStatus(accNo,status);
	}
	public boolean changeStatusCustomerAccounts(long userID, String status) throws KeyException {
		return account.customerAccountsChangeStatus(userID,status);
	}
	public boolean approveWithdraw(ApprovalInfo Withdrawetails, long approvalId,long userId) throws KeyException {
		Check.nullCheck(Withdrawetails);
		long withdrawAcc = Withdrawetails.getAccoutNumber();
		double amount = Withdrawetails.getAmount();
		if(amount<=0) {
			throw new KeyException("Amount should be > 0");
		}
		else if(amount>account.getBalance(withdrawAcc) ) {
			throw new KeyException("Insufficient balance");
		}
		boolean status = account.updateBalance(withdrawAcc,(int)-amount);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		StatementInfo statement = new StatementInfo();
		statement.setAmount(amount);
		statement.setFromAcc(withdrawAcc);
		statement.setToAcc(withdrawAcc);
		statement.setUserId(userId);
		statement.setTime(timestamp);
		statement.setTransType("Withdraw");
		account.setStatement(statement);
		account.approveRequest(approvalId,"Approved");
		return status;
	}
	public boolean declineWithdraw(long approvalId) throws KeyException {
		return account.approveRequest(approvalId,"Declined");
	} 
	public Map<Object, ApprovalInfo> viewRequest() throws KeyException {
		return account.getApprovalList();
	}
}
