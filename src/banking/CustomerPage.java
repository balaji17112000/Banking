package banking;

import java.sql.Timestamp;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import pojos.AccountInfo;
import pojos.ContactInfo;
import pojos.StatementInfo;
import users.Users;
import util.Check;
import util.KeyException;

public class CustomerPage {
	private static final Logger LOG = Logger.getLogger(NetBanking.class.getName());
	private static Scanner sc = new Scanner(System.in);
	private static int getInt() {
		int operation = 0;
		do {
			try {
				operation= sc.nextInt();sc.nextLine();
			} catch(InputMismatchException e) {
				System.out.println("Invalid input format");
				sc.nextLine();
			}
		}while(operation==0);
		return operation;
	}
	public void displayCustomerPage(long id) throws KeyException{
		String line ="___________________________________________________________________________";
		int choose = 0;
		System.out.println("\n"+line+"\nWelcome User!!");
		Users customer = new Users();
		List<Long> accountNums = customer.getAccountNum(id);
		Check.nullCheck(accountNums);
		long accNo = 0;
		if (accountNums.size()==0) {
			accNo= 0;
		}else if(accountNums.size()==1) {
			accNo = accountNums.get(0);
		}else {	
			System.out.println("Choose account to do Banking");
			for(long accNum :accountNums) {
				choose++;
				System.out.println(choose+". "+accNum);
			}
			while(true) {
				int index = getInt();
				if(index>accountNums.size()) {
					System.out.println (" Invalid Account Number");
					continue;
				}
				accNo = accountNums.get(index-1);
				break;
			}
		}
		boolean innerFlag= true;
		do {
			try {
				System.out.println(line+"\n\t 1. View Balance\n\t 2. View Account Details\n\t 3. View Contact Details\n\t 4. Transaction\n\t 5. View Statement \n\t 6. Update Details\n\t 7. Logout");
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
					AccountInfo value = customer.getUserAccountDetails(accNo);
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
					Map<Object, StatementInfo> map = customer.viewStatement(accNo, accNo);
					if(map.isEmpty()) {
						System.out.println("No Statement createdfor this account.");
						break;
					}
					Iterator<Map.Entry<Object,StatementInfo>> itr = map.entrySet().iterator();
					System.out.println("\nStatement for Account: "+accNo+"\nTransactionId\tFrom Account \t To Account\tDate&Time\t TransferType \t Amount");
					while(itr.hasNext()) 
			        { 
			             Map.Entry<Object, StatementInfo> entry = itr.next(); 
			             StatementInfo vals = entry.getValue();
			             long fromAcc = vals.getFromAcc();
			             long toAcc = vals.getToAcc();
			             long trans_Id = vals.getTransId();
			             Timestamp time = vals.getTime();
			             String transType = vals.getTransactionType();
			             double amount = vals.getAmount();
			             System.out.println("\n"+trans_Id+"\t\t"+fromAcc+"\t "+toAcc+"\t\t"+time+"\t"+transType+"\t\t"+amount); 
			        } 
					break;
				case 6:
					System.out.print("\nChoose Operation\n\t1. Update Email\n\t2. Update MobileNumber \n\t3. Update Residentinal Address \n \t4. Change Password");
					int index = getInt();
					System.out.println("Enter value to update");
					String replace= sc.nextLine();
					Check.emptyCheck(replace);
					UpdateDetailsValidator validate = new UpdateDetailsValidator(); 
					try {
					validate.displayPage(replace, index);
					}catch(KeyException e) {
						LOG.info(e.getMessage());
						break;
					}if(index==4) {
						System.out.println("Enter the Old Password");
						String oldPassword = sc.nextLine();
						if(customer.updateUserPassword(id, replace, oldPassword)) {
							System.out.println("Password Changed");
						}
						break;
					}
					try {
					customer.updateDetails(id, index, replace);
					}catch(KeyException e) {
						LOG.info(e.getMessage());
					}
					System.out.println("Update Done.....");
					break;
				case 7:
					LOG.info("Looged Out");
					LOG.info(line);
					innerFlag = false;
					break;
				default:
					System.out.println("choose valid operation b/w 1-5");
					break;
			}
			}catch(KeyException e) {
				LOG.info(e.getMessage());
			}
		}while(innerFlag);
	
	}
}
