package transaction;

import java.sql.Timestamp;

import mysql_util.AccountConnect;
import mysql_util.ConnectDB;
import pojos.AccountInfo;
import pojos.StatementInfo;
import util.Check;
import util.KeyException;

public class Transaction {

	private AccountConnect account =  (AccountConnect) ConnectDB.getDBConnect();
	public boolean sendMoney(long id,long fromAccount, long toAccount, int amount) throws KeyException {
		Check.nullCheck(account);
		Check.nullCheck(id);
		long balance = account.getBalance(fromAccount);
		//System.out.println(account.getAccountNumber(id));
		AccountInfo userInfo = account.getAccountInfo(toAccount);
		if(balance < amount){
			throw new KeyException("---Transaction Failed... Insuficient Balance---");
		} else if(amount > 20000) {
			throw new KeyException("---Transaction Failed... Limit 20000 only ---");
		}else if(userInfo==null) {
			throw new KeyException("---Transaction Failed...reciever Account not found---");
		}else if(account.getAccountInfo(fromAccount)==null) {
			throw new KeyException("---Transaction Failed...reciever Account not found---");
		}else if(amount <=0) {
			throw new KeyException("Minimum Transaction Rs.1");
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		boolean status = account.updateBalance(toAccount,amount);
		status = account.updateBalance(fromAccount,-amount);
		StatementInfo statement = new StatementInfo();
		statement.setAmount(amount);
		statement.setToAcc(fromAccount);
		statement.setFromAcc(toAccount);
		statement.setUserId(userInfo.getUserId());
		statement.setTime(timestamp);
		statement.setTransType("Credit");
		account.setStatement(statement);
		statement.setUserId(id);
		statement.setTransType("Debit");
		statement.setToAcc(toAccount);
		statement.setFromAcc(fromAccount);
		account.setStatement(statement);
		return status;
	}	
	public boolean depositModey(long id, long toAccount, int amount) throws KeyException {
		Check.nullCheck(account);
		Check.nullCheck(id);
		boolean status;
		if(amount <=0) {
			throw new KeyException("Minimum Transaction Rs.1");
		}else if(account.getAccountInfo(toAccount)==null) {
			throw new KeyException("---Transaction Failed...reciever Account not found---");
		}
		status = account.updateBalance(toAccount,amount);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		StatementInfo statement = new StatementInfo();
		statement.setAmount(amount);
		statement.setToAcc(toAccount);
		statement.setUserId(id);
		statement.setTime(timestamp);
		statement.setTransType("Deposit");
		account.setStatement(statement);
		return status;
	}
	public boolean withdrawModey(long id, long fromAccount,int amount) throws KeyException {
		Check.nullCheck(account);
		long balance = account.getBalance(fromAccount);
		if(balance < amount){
			throw new KeyException("---Transaction Failed... Insuficient Balance---");
		}else if(amount <=0) {
			throw new KeyException("Minimum Transaction Rs.1");
		}else if(account.getAccountInfo(fromAccount)==null) {
			throw new KeyException("---Transaction Failed...reciever Account not found---");
		}
		account.submitRequest(id,fromAccount,amount);
		return true;
	}
}
