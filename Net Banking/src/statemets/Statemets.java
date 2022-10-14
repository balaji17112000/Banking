package statemets;

import java.util.Map;

import details.AccountConnect;
import mysql_util.MysqlAccountConnect;

public class Statemets {
	AccountConnect account = new MysqlAccountConnect();
	public Map<Object, StatementInfo> viewStatement(long accNo) {
		return account.getStatement(accNo);
	}
}

