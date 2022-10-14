package banking;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

import login.ValidateUserLogin;


public class NetBanking {

	private static String getString(String logs) {
		System.out.println(logs);
		String string = sc.nextLine();
		return string;
	}
	private static boolean login() {
		ValidateUserLogin validate = new ValidateUserLogin();
		id = getString("Enter UserId");
		String password = getString("\nEnter Password");
		return validate.validateStatus(id,password);
	}
	private static boolean adminLogin() {
		ValidateUserLogin validate = new ValidateUserLogin();
		id = getString("Enter AdminId");
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
		System.out.println("________________________________________________________");
		System.out.println("Enter 0 for Customer Login  (OR)\nEnter any Number for Admin Login");
		System.out.println("________________________________________________________");
		userType=getInt();
		return userType;
	}
	private static final Logger LOG = Logger.getLogger(NetBanking.class.getName());
	private static Scanner sc = new Scanner(System.in);
	private static String id = "";
	public static void main(String arg[]) {
		System.out.println("\nWelcome to Balaji's International Bank \n\n \t\tFirst Login for Net Banking");
		boolean flag = true;
		do {
			if(userType()==0) {
				if(login()) {
					CustomerPage customerPage = new CustomerPage();
					customerPage.displayCustomerPage(id);
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
		}while(flag);
	sc.close();
	}
}
