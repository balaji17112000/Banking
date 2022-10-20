package users;

import java.util.List;
import java.util.Map;

import mysql_util.AccountConnect;
import mysql_util.MysqlAccountConnect;
import pojos.AccountInfo;
import pojos.ContactInfo;
import pojos.StatementInfo;
import util.Check;
import util.ChooseCoulmn;
import util.KeyException;

public class Users {
	
	protected int value=0;
	protected AccountConnect account = new MysqlAccountConnect();
	
	public ContactInfo getContactDetails(long id) throws KeyException {
		Check.nullCheck(id);
		ContactInfo contactInfo = account.getContactDetails(id);
		Check.nullCheck(contactInfo);
		return contactInfo;
	}
	public List<Long> getAccountNum(long id) throws KeyException{
		Check.nullCheck(id);
		List<Long> accNum=account.getAccountNumber(id);
		Check.nullCheck(accNum);
		return accNum;
	}
	public AccountInfo getUserAccountDetails(long accNo) throws KeyException{
		AccountInfo value = account.getAccountInfo(accNo);
		Check.nullCheck(value);
		return value;
	}
	public long CheckBalance(long accNo) throws KeyException{
		if (accNo<=0) {
			throw new KeyException("Invalid Account Number");
		}
		return account.getBalance(accNo);
	}

	public void updateDetails(long id,int colName, String value) throws KeyException{
		Check.nullCheck(id);
		Check.nullCheck(value);
		Check.emptyCheck(value);
		ChooseCoulmn column = null;
		 if(colName==1) {
		 column = ChooseCoulmn.MAIL;
		}
		 else if(colName==2) {
		 column = ChooseCoulmn.NUMBER;
		 }
		 else if(colName==3) {
			 column = ChooseCoulmn.ADDRESS;
		}else {
			throw new KeyException("Invalid input from user!! check drop down");
		}
		 account.updateUserDetails(id,column, value);
	}
	public Map<Object, StatementInfo> viewStatement(long fromAcc,long toAcc) throws KeyException{
		if (fromAcc<=0|| toAcc <=0) {
			throw new KeyException("Invalid Account Number");
		}
		return account.getStatement(fromAcc);
	}
	public String getuserName(long id) throws KeyException{
		Check.nullCheck(id);
		return account.getUserName(id);
	}
	public boolean updateUserPassword(long id,String newPassword, String oldPassword) throws KeyException {
		boolean flag = account.updatePassword(id, newPassword,oldPassword);
		if(flag==false) {
			throw new KeyException("Incorrect Password");
		}
		return flag;
	}
}
