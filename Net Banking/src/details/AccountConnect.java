package details;

import java.sql.Timestamp;
import java.util.Map;

import statemets.StatementInfo;

public interface AccountConnect {

	public long getBalance(long accNo);
	public long[] getAccountNumber(String id);
	public AccountInfo getAccountInfo(long accNo);
	public boolean updateBalance(long accountNumber,int amount, String id);
	public boolean updateBalance(long accountNumber,int amount);
	public boolean setStatement(long accNo , Timestamp time,String type, int ammount) ;
	public Map<Object,StatementInfo> getStatement(long accNo);
	public Map<Object,StatementInfo> getStatements();
	public Map<Object,AccountInfo> getAccounts();
	public ContactInfo getContactDetails(String id );
	boolean validateUser(String userId, String password);
	 boolean validateUser(String userId, String password,String Key);
	 public boolean updateBalanceWithdraw(long accountNumber,int amount);
}
