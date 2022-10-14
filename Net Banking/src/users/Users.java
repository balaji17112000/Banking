package users;

import details.AccountConnect;
import details.AccountInfo;
import details.ContactInfo;
import mysql_util.MysqlAccountConnect;

public class Users {
	AccountConnect account = new MysqlAccountConnect();
	public ContactInfo getContactDetails(String id) {
		ContactInfo contactInfo = account.getContactDetails(id);
		return contactInfo;
	}
	public long[] getAccountNum(String id) {
		return account.getAccountNumber(id);
	}
	public AccountInfo userAccountDetails(long accNo) {
		AccountInfo value = account.getAccountInfo(accNo);
		return value;
	}
	public long CheckBalance(long accNo){
		return account.getBalance(accNo);
	}
}
