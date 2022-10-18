package banking;

import java.util.InputMismatchException;
import java.util.Scanner;

import users.Admin;
import util.KeyException;

public class CreateCustomerPage {
	private static Scanner sc;
	public CreateCustomerPage(Scanner sc) {
		CreateCustomerPage.sc = sc;
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
	public boolean getCustomerDetails(Admin admin,UpdateDetailsValidator validate) throws KeyException {
		System.out.println("Enter UserName");
		String UserName= sc.nextLine();
		System.out.println("Enter Password");
		String accPassword = sc.nextLine();
		validate.displayPage(accPassword,4);
		System.out.println("Enter pancard Number");
		String panCard = sc.nextLine();
		System.out.println("Enter EmailID");
		String emailId = sc.nextLine();
		validate.displayPage(emailId,1);
		System.out.println("Enter Address");
		String cusAddress=sc.nextLine();
		validate.displayPage(cusAddress,3);
		System.out.println("Enter branch");
		String branch = sc.nextLine();
		System.out.println("Enter Account Type");
		String cusAccType = sc.nextLine();
		System.out.println("Enter aadhar Number");
		long aadhar = getLong();
		System.out.println("Enter Mobile Number");
 		long mobileNum = getLong();
 		Long num= (Long)mobileNum;
		validate.displayPage(num.toString(),2);
 		System.out.println("Enter Intial Balance amount");
			long intitalBalance = getLong();
		admin.accountCreate( UserName, accPassword, panCard,  aadhar, mobileNum,  emailId,  cusAddress,  branch, intitalBalance, cusAccType);
		System.out.println("Account created");
		return false;
	}
}
