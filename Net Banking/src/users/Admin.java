package users;

import java.util.Map;

import details.AccountInfo;
import statemets.StatementInfo;

public class Admin extends Users{
	public long[] getAccountNum(String id) {
		return account.getAccountNumber(id);
	}
	public Map<Object,StatementInfo> getLogStatements(){
		return account.getStatements();
	}
	public Map<Object,AccountInfo> getAccounts() {
		return account.getAccounts();
	}
}
