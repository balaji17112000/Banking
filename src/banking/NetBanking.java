package banking;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

import login.ValidateUserLogin;
import util.KeyException;


public class NetBanking {

	private static String getString(String logs) {
		System.out.println(logs);
		String string = sc.nextLine();
		return string;
	}
	private static long getLong(String logs) {
		int intFlag=1;
		long value = 0;
		do {
			System.out.println(logs);
			try {
				value= sc.nextLong();sc.nextLine();
				intFlag=0;
			} catch(InputMismatchException e) {
				sc.nextLine();
				System.out.println("Invalid input format");
			}
		}while(intFlag==1);
		return value;
	}
	private static boolean login() throws KeyException{
		ValidateUserLogin validate = new ValidateUserLogin();
		id = getLong("Enter UserId");
		String password = getString("\nEnter Password");
		return validate.validateUserStatus(id,password);
	}
	private static boolean adminLogin() throws KeyException{
		ValidateUserLogin validate = new ValidateUserLogin();
		id = getLong("Enter AdminId");
		String password = getString("\nEnter Password");
		return validate.validateAdminStatus(id,password);
	}
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
	private static int userType() {
		int userType = 0;
		System.out.println("\t____________________________________________________");
		System.out.println("\t|\t  Enter 0 for Customer Login  (OR)\t   |\n\t|\t  Enter any Number for Admin Login  \t   |");
		System.out.println("\t|__________________________________________________|");
		userType=getInt();
		return userType;
	}
	private static final Logger LOG = Logger.getLogger(NetBanking.class.getName());
	private static Scanner sc = new Scanner(System.in);
	private static long id = 0l;
	public static void main(String arg[]) {
		System.out.println("\nWelcome to Balaji's International Bank \n\n \t\tFirst Login for Net Banking");
		boolean flag = true;
		do {
			try {
			if(userType()==0) {
				if(login()) {
					CustomerPage customerPage = new CustomerPage();
					try {
						customerPage.displayCustomerPage(id);
					} catch (KeyException e) {
						LOG.info(""+e.getCause());
					}
				}else {
					LOG.info("Invalid User credentials");
				}
			}
			else {
				if(adminLogin()) {
					AdminPage adminPage= new AdminPage();
					adminPage.displayAdminPage(id);
				}
				else {
					LOG.info("Invalid Admin credentials");
				}

			}
			} catch (KeyException e) {
				LOG.info(e.getMessage());
			}
		}while(flag);
	sc.close();
	}
}
