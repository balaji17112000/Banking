package mysql_util;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import pojos.AccountInfo;
import pojos.ApprovalInfo;
import pojos.ContactInfo;
import pojos.StatementInfo;
import util.KeyException;

public interface AccountConnect {
	public long getBalance(long accNo) throws KeyException ;
	public String getUserName(long id) throws KeyException ;
	public List<Long> getAccountNumber(long id) throws KeyException ;
	public AccountInfo getAccountInfo(long accNo) throws KeyException ;
	public boolean updateBalance(long accountNumber,int amount) throws KeyException ;
	public boolean setStatement(long fromAcc ,long toAcc, Timestamp time,String type, int ammount) throws KeyException ;
	public Map<Object,StatementInfo> getStatement(long fromAcc,long toAcc) throws KeyException ;
	public Map<Object,StatementInfo> getStatements() throws KeyException ;
	public Map<Object,AccountInfo> getAccounts() throws KeyException ;
	public ContactInfo getContactDetails(long id ) throws KeyException ;
	public boolean validateUser(long userId, String password,boolean Key) throws KeyException ;
	public boolean updatePassword(long id,  String newPassword, String oldPassword) throws KeyException ;
	public void updateUserDetails(long id, int colName, String value) throws KeyException ;
	public boolean changeAccountStatus(long accNo,String status) throws KeyException;
	public boolean createAccount(String UserName,String accPassword,String panCard, long aadhar,long mobileNum, String emailId, String address, String branch,long intitalBalance,String accType) throws KeyException;
	public boolean customerAccountsChangeStatus(long userID, String status) throws KeyException ;
	public boolean submitRequest(long id, long fromAccount, int amount) throws KeyException ;
	public Map<Object, ApprovalInfo> getApprovalList()throws KeyException ;
	public boolean approveRequest(long approvalId,String status)throws KeyException;
}
