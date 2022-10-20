package banking;

import java.sql.Timestamp;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import mysql_util.AccountConnect;
import mysql_util.ConnectDB;
import pojos.AccountInfo;
import pojos.ApprovalInfo;
import pojos.ContactInfo;
import pojos.StatementInfo;
import users.Admin;
import util.Check;
import util.KeyException;

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
	private static long getLong() {
		Long operation = 0l,intFlag=1l;
		do {
			try {
				operation= sc.nextLong();sc.nextLine();
				intFlag=0l;
			} catch(InputMismatchException e) {
				sc.nextLine();
				System.out.println("Invalid input format");
			}
		}while(intFlag==1);
		return operation;
	}
	private boolean patternCheck(String str, String expression) {
		return Pattern.matches(expression, str);
	}
	public boolean emailCheck(String email) throws KeyException {
		return patternCheck(email,"^\\w+@{1}?.\\w+\\.{1}+.\\w+"); 
	}
	public boolean isPassword(String str) throws KeyException {
		Check.emptyCheck(str);
		return patternCheck(str,"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+]).{8,30}$");
	}
	public boolean isAddress(String str) throws KeyException {
		Check.emptyCheck(str);
		return patternCheck(str,"^(?=.*[0-9])(?=.*\\w).{15,50}$");
	}
public void displayAdminPage(long id) {
	String line ="___________________________________________________________________________";
	System.out.println(line+"\nWelcome Admin!!");
	Admin admin = new Admin();	
	
	//Admin access page
	boolean innerFlag= true;
	do {
			try {
			System.out.println(line+"\n\t 1. View Admin Account Details\n\t 2. View Admin Contact Details\n\t 3. View All Log Statement \n\t 4. View  All Accounts Table \n\t 5. Get particular Account details \n\t 6. Get Particluar Statement  \n\t 7. Get Particular Contact Details \n\t 8. Change Users Contact Detils \n\t 9. Create Account \n\t 10. Delete Customer Account \n\t 11. Activate Customer Account \n\t 12. Deactivate Customer Account \n\t 13. Block Customer's Accounts \n\t 14. Activate Accounts \n\t 15. Deactive Customer Accounts \n\t 16. Approve transaction \n\t 17. Logout");
			switch(getInt()) {
			case 1:
				List<Long> accountNums = null;
				try {
					accountNums = admin.getAccountNum(id);
					Check.nullCheck(accountNums);
				} catch (KeyException e1) {
					e1.printStackTrace();
				}
				long accNo = 0;
				if (accountNums.size()==0) {
					accNo= 0;
				}else if(accountNums.size()==1) {
					accNo = accountNums.get(0);
				}else {	
					System.out.println("Choose account to do Banking");
					int choose=0;
					for(long accNum :accountNums) {
						choose++;
						System.out.println(choose+". "+accNum);
					}
					accNo = accountNums.get(getInt()-1);
				}
				AccountInfo value = admin.getUserAccountDetails(accNo);
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
				System.out.println("\nUsers Statements:Statement :\nTransactionId\tFromAccount\tToAccount\tDate&Time\t\tCredit/Debit \t Amount");
				while(itr.hasNext()) 
		        { 
		             Map.Entry<Object, StatementInfo> entry = itr.next(); 
		             StatementInfo vals = entry.getValue();
		             long fromAcc =  vals.getFromAcc();
		             long toAcc =  vals.getToAcc();
		             long transId = vals.getTransId();
		             Timestamp time = vals.getTime();
		             String type =vals.getTransactionType();
		             double amount =  vals.getAmount();
		             System.out.println("\n"+transId+"\t\t"+fromAcc+"\t"+toAcc+"\t"+time+"\t"+type+"\t"+amount); 
		        } 
				break;
			case 4:
				Map<Object, AccountInfo> accounts =admin.getAccounts();
				if(accounts.isEmpty()) {
					System.out.println("No Accounts found for this account");
					break;
				}
				try {
					System.out.println("Fetching User Accounts.... Please Wait");
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Iterator<Map.Entry<Object,AccountInfo>> cursor = accounts.entrySet().iterator();
				System.out.println("\nBank Accounts: \nAccount Number\tBranch\t Account Type");
				while(cursor.hasNext()) 
		        { 
		             Map.Entry<Object, AccountInfo> entry = cursor.next(); 
		             AccountInfo vals = entry.getValue();
		             System.out.println("\n"+vals.getAccoutNumber()+"\t"+vals.getBranch()+"\t"+vals.getAccountType()); 
		        } 
				break;
			case 5:
				System.out.println("Enter Account Number to get AccountDetails");
				long userAccNo = sc.nextLong();
				AccountInfo userAcc = admin.getUserAccountDetails(userAccNo);
				if(userAcc==null) {
					System.out.println("No account for this User");
					break;
				}
				accType = userAcc.getAccountType();
				accBranch = userAcc.getBranch();
				System.out.println("\n"+line+"\nAccount Details \nAccount Number\t:"+userAccNo+"\nAccountType\t:"+accType+"\nBranch     \t: "+accBranch);
				break;
			case 6:
				System.out.println("Enter Account Number to get Statement");
				userAccNo = sc.nextLong();
				if(userAccNo <= 0 ) {
					System.out.println("No account for this User");
					break;
				}
				AccountConnect account =  (AccountConnect) ConnectDB.getDBConnect();
				Map<Object, StatementInfo> map = account.getStatement(userAccNo);
				if(map.isEmpty()) {
					System.out.println("No Statement created.");
					break;
				}
				Iterator<Map.Entry<Object,StatementInfo>> iter = map.entrySet().iterator();
				System.out.println("\nStatement for Account: "+userAccNo+"\nTransactionId\tFrom Account \t To Account\tDate&Time\t\t TransferType \t Amount");
				while(iter.hasNext()) 
		        { 
		             Map.Entry<Object, StatementInfo> entry = iter.next(); 
		             StatementInfo vals = entry.getValue();
		             long fromAcc = vals.getFromAcc();
		             long toAcc = vals.getToAcc();
		             long trans_Id = vals.getTransId();
		             Timestamp time = vals.getTime();
		             String transType = vals.getTransactionType();
		             double amount = vals.getAmount();
		             System.out.println("\n"+trans_Id+"\t\t"+fromAcc+"\t "+toAcc+"\t"+time+"\t  "+transType+"\t "+amount); 
		        } 
				break;
			case 7:
				System.out.println("Enter user ID");
				long userId = sc.nextLong();
				ContactInfo userContact = admin.getContactDetails(userId);
				if(userContact==null) {
					System.out.println("No account for this User");
					break;
				}
				mobile = userContact.getMobile();
				mail_Id = userContact.getmail();
				address = userContact.getAddress();
				System.out.println(line+"\nContanct Info Details \nMobile Number\t:"+mobile+"\nMail_ID\t:"+mail_Id+"\nResidential_Address\t: "+address);
				break;
			case 8:
				System.out.print("\nChoose Operation\n\t1. Update Email\n\t2. Update MobileNumber \n\t3. Update Residentinal Address \n \t4. Change Password");
				String update[] = {"Mail_Id","Mobile_Number","Residential_Address","Password"};
				int index = getInt()-1;
				System.out.println("Enter value to update");
				String replace= sc.nextLine();
				System.out.println("Enter User ID to modify");
				long user_id =sc.nextLong();
				Check.emptyCheck(replace);
				UpdateDetailsValidator validate = new UpdateDetailsValidator(); 
				try {
					validate.displayPage(replace, index);
					if(index==3) {
						System.out.println("Enter the Old Password");
						String oldPassword = sc.nextLine();
						admin.updateUserPassword(user_id,id,update[index], replace, oldPassword);
						break;
					}
					}catch(KeyException e) {
						LOG.info(e.getMessage());
						break;
					}
				admin.updateDetails(user_id,index, replace);
				break;
			case 9:
				validate = new UpdateDetailsValidator(); 
				CreateCustomerPage createAccount = new CreateCustomerPage(sc);
				if(createAccount.getCustomerDetails(admin, validate)) {
					System.out.println("Account Created");
				}
				break;
			case 10:
				System.out.println("Enter Account Number to Deactivate");
				long accNumber =getLong();
				if(admin.changeStatusCustomerAccount(accNumber,"INACTIVE")) {
					System.out.println("Account Inactivated");
				}
				else {
					throw new KeyException("Account Not found");
				}
				break;
			case 11:
				System.out.println("Enter Account Number to Activate");
				accNumber =getLong();
				if(admin.changeStatusCustomerAccount(accNumber,"ACTIVE")) {
					System.out.println("Account Activated");
				}
				else {
					throw new KeyException("Account Already Activated (OR)Account Not found");
				}
				break;
			case 12:
				System.out.println("Enter Customer ID to block");
				int userID =getInt();
				if(admin.changeStatusCustomerAccount(userID,"BLOCKED")) {
					System.out.println("Account Activated");
				}
				else {
					throw new KeyException("Account Already Blocked (OR)Account Not found");
				}
				break;
			case 13:
				System.out.println("Enter Customer ID to Block all accounts");
				userID =getInt();
				if(admin.changeStatusCustomerAccounts(userID,"BLOCKED")) {
					System.out.println("Account Blocked");
				}
				else {
					throw new KeyException("Account Already Blocked (OR) Account Not found");
				}
				break;
			case 14:
				System.out.println("Enter Customer ID to Activate all accounts");
				userID =getInt();
				if(admin.changeStatusCustomerAccounts(userID,"ACTIVE")) {
					System.out.println("Account Activated");
				}
				else {
					throw new KeyException("Account Already Activated (OR) Account Not found");
				}
				break;
			case 15:
				System.out.println("Enter Customer ID to DeActivate all accounts");
				userID =getInt();
				if(admin.changeStatusCustomerAccounts(userID,"INACTIVE")) {
					System.out.println("Account Deactivated");
				}
				else {
					throw new KeyException("Account Already deactivated (OR) Account Not found");
				}
				break;
			case 16:
				Map<Object, ApprovalInfo> details = admin.viewRequest();
				if(details.isEmpty()) {
					System.out.println("No transaction is Pending...");
					break;
				}
				Iterator<Map.Entry<Object,ApprovalInfo>> iterator = details.entrySet().iterator();
				try {
					System.out.println("Fetching Pending Request.... Please Wait");
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("\nApproval Pending List :\nApproval Id\tFromAccount\tUser ID\tAmount\t\tStatus");
				long approvalId = 0 ;
				while(iterator.hasNext()) 
		        { 
		             Map.Entry<Object, ApprovalInfo> entry = iterator.next(); 
		             ApprovalInfo vals = entry.getValue();
		             long withdrawAcc =  vals.getAccoutNumber();
		             long  user_Id=  vals.getUserId();
		             approvalId= vals.getApprovalId();
		             double amount = vals.getAmount();
		             String status =vals.getStatus();
		             System.out.println("\n"+approvalId+"\t\t"+withdrawAcc+"\t"+user_Id+"\t"+amount+"\t\t"+status); 
		        }
				System.out.println("Enter approval id to Approve");
				Long approveWithrawId = getLong();
				ApprovalInfo Withdrawetails = details.get(approveWithrawId);
				if(Withdrawetails==null) {
					throw new KeyException("...Invalid Approval ID...");
				}
				System.out.println("Enter 1 to approve request and 0 to decline request");
				int option = getInt();
				if(option==1) {
					admin.approveWithdraw(Withdrawetails,approvalId,id);
					System.out.println("...Request Approved...");
					break;
				}
				admin.declineWithdraw(approvalId);
				System.out.println("...Request Declined...");
				break;
			case 17:
				innerFlag = false;
				break;
			default:
				System.out.println("choose valid operation b/w 1-11");
				break;
		}
		}catch(KeyException e) {
			LOG.info(e.getMessage());
		}
	}while(innerFlag);

}
}
