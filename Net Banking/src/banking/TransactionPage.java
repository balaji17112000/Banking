package banking;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

import transaction.Transaction;
import users.Customer;
import util.KeyException;

public class TransactionPage {
	private static final Logger LOG = Logger.getLogger(NetBanking.class.getName());
	private static Scanner sc = new Scanner(System.in);
	private static int getInt() {
		int operation = 0,intFlag=1;
		do {
			try {
				operation= sc.nextInt();sc.nextLine();
				intFlag=0;
			} catch(InputMismatchException e) {
				sc.nextLine();
				System.out.println("Invalid input format");
			}
		}while(intFlag==1);
		return operation;
	}
	private static long getLong() {
		long operation = 0,intFlag=1;
		do {
		try {
			operation= sc.nextLong();sc.nextLine();
			intFlag=0;
		} catch(InputMismatchException e) {
			sc.nextLine();
			System.out.println("Invalid input format");
		}
		}while(intFlag==1);
		return operation;
		}
public void displayTransactionPage(Customer customer, String id,long accNo) {
	String line ="___________________________________________________________________________";
	LOG.info(line+"\n1. Send Money\n 2. Deposit \n 3. Withdraw \n 4. Back");
	Transaction trans = new Transaction();
	switch(getInt()) {
	case 1:
		try {
			System.out.println("Enter Reciptent Account Number");
			long toAcc = getLong(),fromAcc = accNo;
			System.out.println("Paying Amount :");
			int amount = getInt();
			if(amount ==0) {
				System.out.println("Minimum Transaction Rs.1");
			}else {
				boolean status = trans.sendMoney(id,fromAcc,toAcc,amount );
				try {
					System.out.println("Processing Transaction.... Please Wait");
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(status) {
					System.out.println("Transaction completed");
				}
			}
		} catch (KeyException e) {
			LOG.info(e.getMessage());
		}
		break;
		
	case 2:
		try {
			System.out.println("Deposit Amount :");
			int amount = getInt();
			boolean status = trans.depositModey(id,accNo, amount);
			try {
				System.out.println("Processing Transaction.... Please Wait");
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(status) {
				System.out.println("Transaction completed");
			}
		} catch (KeyException e) {
			LOG.info(e.getMessage());
		}
		break;

	case 3:
		try {
			System.out.println("Withdraw Amount :");
			int amount = getInt();
			boolean status = trans.withdrawModey(accNo, amount);
			try {
				System.out.println("Withdrawing Money.... Please Wait");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(status) {
				System.out.println("Withdraw completed");
			}
		} catch (KeyException e) {
			LOG.info(e.getMessage());
		}
		break;
	case 4:
		break;
	default:
		System.out.println(" Enter valid number to operate");
		break;
	}
}
}
