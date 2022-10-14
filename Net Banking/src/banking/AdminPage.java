package banking;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import details.AccountInfo;
import details.ContactInfo;
import statemets.StatementInfo;
import users.Admin;

public class AdminPage {
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
public void displayAdminPage(String id) {
	String line ="___________________________________________________________________________";
	System.out.println(line+"\nWelcome Admin!!");
	Admin admin = new Admin();	
	long[] accountNums = admin.getAccountNum(id);
	//System.out.println(accountNums);
	long accNo = 0;
	if (accountNums.length==0) {
		accNo= 0;
	}else if(accountNums.length==1) {
		accNo = accountNums[0];
	}else {	
		System.out.println("Choose account to do Banking");
		int choose=0;
		for(long accNum :accountNums) {
			choose++;
			System.out.println(choose+". "+accNum);
		}
		accNo = accountNums[getInt()-1];
	}
	//Admin access page
	boolean innerFlag= true;
	do {
		LOG.info(line+"\n\t 1. View Account Details\n\t 2. View Contact Details\n\t 3. View Log Statement \n\t 4. View Accounts Table \n\t 5. Logout");
		switch(getInt()) {
		case 1:
			AccountInfo value = admin.userAccountDetails(accNo);
			if(accNo==0) {
				System.out.println("No account for this Admin");
				break;
			}
			String accType = value.getAccountType(), accBranch = value.getBranch();
			System.out.println("\n"+line+"\nAccount Details \nAccount Number\t:"+accNo+"\nAccountType\t:"+accType+"\nBranch     \t: "+accBranch);
			break;
		case 2:
			ContactInfo contactInfo = admin.getContactDetails(id);
			long mobile = contactInfo.getMobile();
			String mail_Id = contactInfo.getmail();
			String address = contactInfo.getAddress();
			System.out.println(line+"\nContanct Info Details \nMobile Number\t:"+mobile+"\nMail_ID\t:"+mail_Id+"\nResidential_Address\t: "+address);
			break;
		case 3:
			Map<Object, StatementInfo> statements =admin.getLogStatements();
			if(statements.isEmpty()) {
				System.out.println("No transaction done...");
				break;
			}
			Iterator<Map.Entry<Object,StatementInfo>> itr = statements.entrySet().iterator();
			try {
				System.out.println("Fetching Statements.... Please Wait");
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\nUsers Statements:Statement :\nTransactionId\tDate&Time\t\tCredit/Debit \t Amount");
			while(itr.hasNext()) 
	        { 
	             Map.Entry<Object, StatementInfo> entry = itr.next(); 
	             StatementInfo vals = entry.getValue();
	             System.out.println("\n"+vals.getTransId()+"\t\t"+vals.getTime()+"\t"+vals.getTransactionType()+"\t\t"+vals.getAmount()); 
	        } 
			break;
		case 4:
			Map<Object, AccountInfo> accounts =admin.getAccounts();
			if(accounts.isEmpty()) {
				System.out.println("No Accounts found");
				break;
			}
			try {
				System.out.println("Fetching User Accounts.... Please Wait");
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Iterator<Map.Entry<Object,AccountInfo>> cursor = accounts.entrySet().iterator();
			System.out.println("\nBank Accounts: \nAccount Number\tBranch\t\t Account Type");
			while(cursor.hasNext()) 
	        { 
	             Map.Entry<Object, AccountInfo> entry = cursor.next(); 
	             AccountInfo vals = entry.getValue();
	             System.out.println("\n"+vals.getAccoutNumber()+"\t"+vals.getBranch()+"\t\t"+vals.getAccountType()); 
	        } 
			break;
		case 5:
			innerFlag = false;
			break;
		default:
			System.out.println("choose valid operation b/w 1-5");
			break;
	}
}while(innerFlag);
}
}
