package login;

import details.AccountConnect;
import mysql_util.ConnectDB;

public class ValidateUserLogin {
	ConnectDB connectDb =  new ConnectDB();
	public Object getConnectDb() {
		return connectDb.getDBConnect();
	}
	AccountConnect login =  (AccountConnect) getConnectDb();
	
	public boolean validateStatus(String userId,String password) {
	return login.validateUser(userId,password);
	}
	public boolean validateAdminStatus(String userId,String password) {
		return login.validateUser(userId,password,"Admin");
		}
}
