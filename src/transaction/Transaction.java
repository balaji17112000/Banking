package transaction;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import mysql_util.AccountConnect;
import mysql_util.ConnectDB;
import util.Check;
import util.KeyException;

public class Transaction {

	private AccountConnect account =  (AccountConnect) ConnectDB.getDBConnect();
	public boolean sendMoney(long id,long fromAccount, long toAccount, int amount) throws KeyException {
		Check.nullCheck(account);
		Check.nullCheck(id);
		long balance = account.getBalance(fromAccount);
		//System.out.println(account.getAccountNumber(id));
		if(balance < amount){
			throw new KeyException("---Transaction Failed... Insuficient Balance---");
		} else if(amount > 20000) {
			throw new KeyException("---Transaction Failed... Limit 20000 only ---");
		}else if(account.getAccountInfo(toAccount)==null) {
			throw new KeyException("---Transaction Failed...reciever Account not found---");
		}else if(account.getAccountInfo(fromAccount)==null) {
			throw new KeyException("---Transaction Failed...reciever Account not found---");
		}else if(amount <=0) {
			throw new KeyException("Minimum Transaction Rs.1");
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		boolean status = account.updateBalance(toAccount,amount);
		status = account.updateBalance(fromAccount,-amount);
		account.setStatement(fromAccount,toAccount,timestamp,"Transfer",amount);
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
		final SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(currentTime.format(timestamp));
		account.setStatement(toAccount,toAccount,timestamp,"Deposit",amount);
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
