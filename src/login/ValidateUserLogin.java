package login;

import mysql_util.AccountConnect;

import mysql_util.ConnectDB;
import util.Check;
import util.KeyException;

public class ValidateUserLogin {

	AccountConnect login =  (AccountConnect) ConnectDB.getDBConnect();
	
	public boolean validateStatus(long userId,String password) throws KeyException{ 
		Check.nullCheck(userId);
		Check.nullCheck(password);
		Check.nullCheck(login);
	return login.validateUser(userId,password,false);
	}
	public boolean validateAdminStatus(long userId,String password) throws KeyException{ 
		Check.nullCheck(login);
		return login.validateUser(userId,password,true);
		}
}
