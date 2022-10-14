package banking;

import java.sql.Timestamp;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import details.AccountInfo;
import details.ContactInfo;
import statemets.StatementInfo;
import statemets.Statemets;
import users.Customer;

public class CustomerPage {
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

	public void displayCustomerPage(String id){
		String line ="___________________________________________________________________________";
		int choose = 0;
		System.out.println("\n"+line+"\nWelcome User!!");
		Customer customer = new Customer();
		long[] accountNums = customer.getAccountNum(id);
		long accNo = 0;
		if (accountNums.length==0) {
			accNo= 0;
		}else if(accountNums.length==1) {
			accNo = accountNums[0];
		}else {	
			System.out.println("Choose account to do Banking");
			for(long accNum :accountNums) {
				choose++;
				System.out.println(choose+". "+accNum);
			}
			accNo = accountNums[getInt()-1];
		}
		boolean innerFlag= true;
		do {
			LOG.info(line+"\n\t 1. View Balance\n\t 2. View Account Details\n\t 3. View Contact Details\n\t 4. Transaction\n\t 5. View Statement \n\t 6. Logout");
			switch(getInt()) {
			case 1:
				//User access page
				if(accNo == 0) {
					System.out.println("No account for this User");
					break;
				}
				System.out.println("\n"+line+"\nAccount Balance :"+customer.CheckBalance(accNo));
				break;
			case 2:
				AccountInfo value = customer.userAccountDetails(accNo);
				if(value==null) {
					System.out.println("No account for this User");
					break;
				}
//				long [] accNum = customer.getAccountNum(id);
//				System.out.println(Arrays.toString(accNum));
				String accType = value.getAccountType(), accBranch = value.getBranch();
				System.out.println("\n"+line+"\nAccount Details \nAccount Number\t:"+accNo+"\nAccountType\t:"+accType+"\nBranch     \t: "+accBranch);
				break;
				
			case 3:
				ContactInfo contactInfo =customer.getContactDetails(id);
				long mobile = contactInfo.getMobile();
				String mail_Id = contactInfo.getmail();
				String address = contactInfo.getAddress();
				System.out.println(line+"\nContanct Info Details \nMobile Number\t:"+mobile+"\nMail_ID\t:"+mail_Id+"\nResidential_Address\t: "+address);
				break;
				
			case 4:
				if(accNo == 0) {
					System.out.println("No account for this User");
					break;
				}
				TransactionPage transaction =new TransactionPage();
				transaction.displayTransactionPage(customer, id,accNo);
				break;
			case 5:
				if(accNo == 0) {
					System.out.println("No account for this User");
					break;
				}
				Statemets stmt = new Statemets();
				Map<Object, StatementInfo> map = stmt.viewStatement(accNo);
				if(map.isEmpty()) {
					System.out.println("No Statement created.");
					break;
				}
				Iterator<Map.Entry<Object,StatementInfo>> itr = map.entrySet().iterator();
				System.out.println("\nStatement for Account: "+accNo+"\nTransactionId\tDate&Time\t\tCredit/Debit \t Amount");
				while(itr.hasNext()) 
		        { 
		             Map.Entry<Object, StatementInfo> entry = itr.next(); 
		             StatementInfo vals = entry.getValue();
		             long trans_Id = vals.getTransId();
		             Timestamp time = vals.getTime();
		             String transType = vals.getTransactionType();
		             long amount = vals.getAmount();
		             System.out.println("\n"+trans_Id+"\t\t"+time+"\t"+transType+"\t\t"+amount); 
		        } 
				break;
			case 6:
				LOG.info("Looged Out");
				LOG.info(line);
				innerFlag = false;
				break;
			default:
				System.out.println("choose valid operation b/w 1-5");
				break;
		}
	}while(innerFlag);
	}
}
