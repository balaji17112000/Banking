package transaction;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import details.AccountConnect;
import mysql_util.MysqlAccountConnect;
import util.KeyException;

public class Transaction {
	AccountConnect account = new MysqlAccountConnect();

	public boolean sendMoney(String id,long fromAccount, long toAccount, int amount) throws KeyException {
		long balance = account.getBalance(fromAccount);
		//System.out.println(account.getAccountNumber(id));
		if(balance < amount){
			throw new KeyException("---Transaction Failed... Insuficient Balance---");
		} else if(amount > 20000) {
			throw new KeyException("---Transaction Failed... Limit 20000 only ---");
		}else if(account.getAccountInfo(toAccount)==null) {
			throw new KeyException("---Transaction Failed...reciever Account not found---");
		} 
		else if(amount > 20000) {
			throw new KeyException("---Transaction Failed... Limit 20000 only ---");
		}
		else if(amount <=0) {
			throw new KeyException("Minimum Transaction Rs.1");
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		boolean status = account.updateBalance(toAccount,amount,  id);
		account.setStatement(fromAccount,timestamp,"Debit",amount);
		account.setStatement(toAccount,timestamp,"Credit",amount);
		return status;
	}	
	public boolean depositModey(String id, long toAccount, int amount) throws KeyException {
		boolean status = account.updateBalance(toAccount,amount);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		final SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(currentTime.format(timestamp));
		account.setStatement(toAccount,timestamp,"Credit",amount);
		return status;
	}
	public boolean withdrawModey(long fromAccount,int amount) throws KeyException {
		boolean status = account.updateBalanceWithdraw(fromAccount,amount);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		final SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(currentTime.format(timestamp));
		account.setStatement(fromAccount,timestamp,"debit",amount);
		return status;
	}
}
