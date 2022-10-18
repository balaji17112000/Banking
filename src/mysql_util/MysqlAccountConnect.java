package mysql_util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pojos.AccountInfo;
import pojos.ApprovalInfo;
import pojos.ContactInfo;
import pojos.StatementInfo;
import util.Check;
import util.KeyException;

public class MysqlAccountConnect implements AccountConnect{
	
	public Connection getConnection() throws KeyException {
		try {
			 Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root","Root@123");
			return connect;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...DB Connection not establshed...");
		}
	}
	public PreparedStatement getPreStatement(String query,Connection connect) throws KeyException {
		try {
			PreparedStatement preparedStmt = connect.prepareStatement(query);
			return preparedStmt;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...DB Connection not establshed...");
		}
	}
	public long getBalance(long accNo) throws KeyException {
		String query = "Select balance from User_Accounts where Account_No = ?";
		try(Connection connect = getConnection();
				PreparedStatement stmt = getPreStatement(query,connect);) {
			stmt.setLong(1, accNo);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getLong(1);
			}
			throw new KeyException("...Account Not found for this Acount Number..");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured in DB layer! Cant able to get Balance for the account...");
		}
	}
	public List<Long> getAccountNumber(long id) throws KeyException { //
		String query = "Select Account_No from User_Accounts where User_Id = ? and Status = 'ACTIVE'";
		try(Connection connect = getConnection();
				PreparedStatement stmt = getPreStatement(query,connect);) {
			stmt.setLong(1, id);
			ResultSet result = stmt.executeQuery();
			List<Long>arr = new ArrayList<Long>(4);
			while(result.next()) {
				arr.add(result.getLong(1));
			}
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("... Account Number not exist...");
		}
	}
	public  AccountInfo getAccountInfo(long accNo) throws KeyException{
		String query = "Select Account_No,Branch,Account_Type from User_Accounts where Account_No = ?";
		ResultSet result= null;
		try (Connection connect = getConnection();
				PreparedStatement preparedStmt = getPreStatement(query,connect);) {
			preparedStmt.setLong(1, accNo);
			AccountInfo userInfo = new AccountInfo(); 
			result =preparedStmt.executeQuery();
			if (result.next()) {
				userInfo.setAccountNumber(result.getLong(1));
				userInfo.setAccType(result.getString(2));
				userInfo.setBranch(result.getString(3));
				return userInfo;
			} 
			throw new KeyException("...Account Not found..");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error in DB layer! Account Not found..");
		} finally {
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean updateBalance(long accountNumber,int amount) throws KeyException {
		String query = "update User_Accounts set BALANCE = BALANCE+? WHERE  Account_No = ?";
		try (Connection connect = getConnection();
				PreparedStatement preparedStmt = getPreStatement(query,connect);){
			preparedStmt.setInt(1, amount);
			preparedStmt.setLong(2, accountNumber);
			preparedStmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured in DB layer! Cant able Update Balance...");
		}
	}
	public boolean setStatement(long fromAcc ,long toAcc, Timestamp time,String type, int amount) throws KeyException {
		String query = "insert into Statement_Table(From_Account,To_Account,Time_Stamp,Transaction_Type,Amount) values(?,?,?,?,?)";
		try (Connection connect = getConnection();
				PreparedStatement preparedStmt = getPreStatement(query,connect);){
			preparedStmt.setLong(1, fromAcc);
			preparedStmt.setLong(2, toAcc);
			preparedStmt.setTimestamp(3, time);
			preparedStmt.setString(4, type);
			preparedStmt.setInt(5, amount);
			preparedStmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured in creating Statement");
		}
	}
	public Map<Object,StatementInfo> getStatement(long fromAcc,long toAcc) throws KeyException{
		String query = "Select * from Statement_Table where From_Account= ? or To_Account= ?";
		Connection connect = getConnection();
		PreparedStatement stmt = getPreStatement(query,connect);
		try {
			stmt.setLong(1, fromAcc);
			stmt.setLong(2, toAcc);
			ResultSet result = stmt.executeQuery();
			return setToMapObject(result);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured in DB layer! Cant able get Statememt...");
		}finally {
			try {
				stmt.close();
				try {
				connect.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public Map<Object, StatementInfo> setToMapObject(ResultSet result)  {
		Map<Object,StatementInfo> map2 = new HashMap<Object,StatementInfo>();
		try {
			ResultSetMetaData rsmd = (ResultSetMetaData) result.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (result.next()) { // row wise iterate 
				Object[] arr= new Object[columnsNumber]; //
				StatementInfo record=null;
			    for (int i = 1; i <= columnsNumber; i++) {
			        Object columnValue = result.getObject(i);
			         arr[i-1]=columnValue;  
			    }
			    record = new StatementInfo();
			    record.setFromAcc((long)arr[0]);
			    record.setToAcc((long)arr[1]);
			    record.setTransId((long)arr[2]);
			    record.setTime((Timestamp)arr[3]);
			    record.setTransType((String)arr[4]);
			    record.setAmount((double)arr[5]);
			    map2.put(record.getTransId(),record); // map of list for avoiding key duplication
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map2;
	}
	public Map<Object, AccountInfo> setToMap(ResultSet result)  {
		Map<Object,AccountInfo> map2 = new HashMap<Object,AccountInfo>();
		try {
			ResultSetMetaData rsmd = (ResultSetMetaData) result.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (result.next()) {
				Object[] arr= new Object[5];
				AccountInfo record=null;
			    for (int i = 1; i <= columnsNumber; i++) {
			        Object columnValue = result.getObject(i);
			         arr[i-1]=columnValue;  
			    }
			    record = new AccountInfo();
			    record.setAccountNumber((long) arr[0]);;
			    record.setBranch(((String)arr[1]));;
			    record.setAccType((String) arr[2]);
			    map2.put(record.getAccoutNumber(),record); 
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try { //
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map2;
	}
	public Map<Object,StatementInfo> getStatements() throws KeyException {
		String query = "Select * from Statement_Table ";
		Connection connect = getConnection();
		PreparedStatement stmt = getPreStatement(query,connect);
		try {
			ResultSet result = stmt.executeQuery();
			return setToMapObject(result);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured in DB! Cant get Statement...");
		}finally {
			try {
				stmt.close();
				try {
				connect.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public Map<Object,AccountInfo> getAccounts() throws KeyException {
		String query = "Select Account_No,BRANCH,Account_Type from User_Accounts where Status = 'ACTIVE'";
		Connection connect = getConnection();
		PreparedStatement stmt = getPreStatement(query,connect);
		try {
			ResultSet result = stmt.executeQuery();
			return setToMap(result);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured in DB layer! Cant able to get Account information...");
		}finally {
			try {
				stmt.close();
				try {
				connect.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public ContactInfo getContactDetails(long id ) throws KeyException {
		String query = "Select Mobile_Number, Mail_Id, Residential_Address from Contact_Details where User_Id = ?";
		try(Connection connect = getConnection();
		PreparedStatement stmt = getPreStatement(query,connect);){
		stmt.setLong(1, id);
		ResultSet result = stmt.executeQuery();
		if(result.next()){
			ContactInfo contact = new ContactInfo();
			contact.setMobile(result.getLong(1));
			contact.setMail(result.getString(2));
			contact.setAddress(result.getString(3));
			contact.getAddress();
			contact.getmail();
			contact.getMobile();
			return contact;
		}
		throw new KeyException("...No contact details for this user ID...");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured in DB! Cant able to get Contact Detail...");
		}
	}
	public boolean blockedCheck(long userId) throws KeyException {
		String query = "Select Status from Login_details where User_Id =? and Status = 'Blocked'";
		try(Connection connect = getConnection();
				PreparedStatement stmnt = getPreStatement(query,connect);) {
			stmnt.setLong(1, userId);
			ResultSet result = stmnt.executeQuery();
			if (result.next()) {
				throw new KeyException("...All Accounts Blocked for this user...");
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured in DB layer! Cant validate user login");
		}
	}
	public boolean validateUser(long userId, String password,boolean Key) throws KeyException {
		String query = "Select Role from Login_details where User_Id =? and password = ? and Status = 'ACTIVE'";
		blockedCheck(userId);
		if(Key) {
			query = "Select Role from Login_details where User_Id =? and password = ? and Role= 'Admin'";
		}
			try(Connection connect = getConnection();
					PreparedStatement stmt = getPreStatement(query,connect);) {
				stmt.setLong(1, userId);
				stmt.setString(2, password);
				ResultSet result = stmt.executeQuery();
				boolean flag = false;
				if (result.next()) {
					flag = true;
					return flag;
				}
				throw new KeyException("...Invalid user login...");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new KeyException("...Error occured in DB layer! Cant validate user login");
			}
	}
	public void updateUserDetails(long id, int colName, String value) throws KeyException {
		ChooseCoulmn column = null;
		 if(colName==1) {
		 column = ChooseCoulmn.MAIL;
		}
		 else if(colName==2) {
		 column = ChooseCoulmn.NUMBER;
		 }
		 else if(colName==3) {
			 column = ChooseCoulmn.ADDRESS;
		}else {
			throw new KeyException("Invalid input from user!! check drop down");
		}
		Check.nullCheck(column);
		String query = "update Contact_Details set "+column.getColumn()+" = ? where User_Id= ?"; //
			try (Connection connect = getConnection();
					PreparedStatement stmt = getPreStatement(query,connect);){
				if(colName==2) {
					long i=Long.parseLong(value);
					stmt.setLong(1, i);
				}else {
				stmt.setString(1, value);
				}
				stmt.setLong(2, id);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new KeyException("...Error occured in DB layer! Cant able to get Update user Detail...");
			}
	}
	public boolean updatePassword(long id,  String newPassword,String oldPassword) throws KeyException {
		String query = "update Login_details set Password = ? where User_Id= ? and Password = ?";
			try (Connection connect = getConnection();
					PreparedStatement stmt = getPreStatement(query,connect);){
				stmt.setString(1, newPassword);
				stmt.setLong(2, id);
				stmt.setString(3, oldPassword);
				 int count = stmt.executeUpdate();
				if(count>0) {
					return true;
				}
				throw new KeyException("...Wrong Password! Cant change password...");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new KeyException("...Error occured while changing password! Cant change password...");
			}
	}
	public String getUserName(long id) throws KeyException {
		String query = "Select User_Name from Login_details where User_Id = ?";
		try (Connection connect = getConnection();
				PreparedStatement stmt = getPreStatement(query,connect);){
			stmt.setLong(1, id);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getString(1);
			}
			throw new KeyException("...User ID not found...");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured ! Cant able get User Name...");
		}
	}
	public boolean customerAccountsChangeStatus(long userId,String status) throws KeyException {
		String query = "update Login_details set Status = ? where User_Id= ? and Role = 'Admin'";
		try (Connection connect = getConnection();
				PreparedStatement stmt = getPreStatement(query,connect);){
			stmt.setLong(2, userId);
			stmt.setString(1, status);
			 int count = stmt.executeUpdate();
			if(count>0) {
				return true;
			}
			throw new KeyException("User Id does not exist");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured while changing Status of accounts...");
		}
	}
	public boolean changeAccountStatus(long accNo,String status) throws KeyException {
		String query = "update User_Accounts set Status = ? where Account_No= ?";
		try (Connection connect = getConnection();
				PreparedStatement stmt = getPreStatement(query,connect);){
			stmt.setString(1, status);
			stmt.setLong(2, accNo);
			 int count = stmt.executeUpdate();
			if(count>0) {
				return true;
			}
			throw new KeyException("Account does not exist");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured while changing password! Cant change password...");
		}
	}
	public boolean createAccount(String UserName,String accPassword,String panCardNo, long aadharNo,long mobileNum, String emailId, String address, String branch,long intitalBalance,String accType) throws KeyException{
		String query = "insert into Login_details(User_Name,Password,Role,Status,Email_Id) values(?,?,'Customer','ACTIVE',?)";
		Long userId = 0l;
		try (Connection connect = getConnection();
			PreparedStatement stmt = connect.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);){
			stmt.setString(1, UserName);
			stmt.setString(2, accPassword);
			stmt.setString(3, emailId);
			 stmt.execute();
			 ResultSet result =  stmt.getGeneratedKeys();
			 if(result.next()) {
				userId = result.getLong(1);
			 }
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured creating Customer account...");
		}
		query = "insert into Contact_Details values(?,?,?,?)";
		try (Connection connect = getConnection();
				PreparedStatement stmt = getPreStatement(query,connect);){
				stmt.setLong(1,userId);
				stmt.setLong(2, mobileNum);
				stmt.setString(3, emailId);
				stmt.setString(4, address);
				 stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new KeyException("...Error occured while creating contact details for user ID..."+userId);
			}
		query = "insert into Customer_Details values(?,?,?)";
		try (Connection connect = getConnection();
				PreparedStatement stmt = getPreStatement(query,connect);){
				stmt.setLong(1,userId);
				stmt.setString(2, panCardNo);
				stmt.setLong(3, aadharNo);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new KeyException("...Error occured while creating customer details...");
			}
		query = "insert into User_Accounts(User_Id,BRANCH,BALANCE,Account_Type,Status) values(?,?,?,?,'ACTIVE')";
		try (Connection connect = getConnection();
				PreparedStatement stmt = getPreStatement(query,connect);){
				stmt.setLong(1,userId);
				stmt.setString(2, branch);
				stmt.setLong(3, intitalBalance);
				stmt.setString(4, accType);
				 stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new KeyException("...Error occured while changing password! Cant change password...");
			}
		return true;
	}
	public boolean submitRequest(long id, long fromAccount, int amount) throws KeyException {
		String query = "insert into Admin_approval(User_id,Account_No,Amount,Status) values(?,?,?,'Pending')";
		try(Connection connect = getConnection();
			PreparedStatement prepstmt = getPreStatement(query,connect);){
			prepstmt.setLong(1,id);
			prepstmt.setLong(2,fromAccount);
			prepstmt.setLong(3,amount);
			prepstmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KeyException("...Error occured while submitting the Request...");
		}
	}
	public Map<Object, ApprovalInfo> getApprovalList() throws KeyException {
		String query = "Select * from Admin_approval where Status = 'pending'";
		try(Connection connect = getConnection();
				PreparedStatement prepstmt = getPreStatement(query,connect);){
			ResultSet result = prepstmt.executeQuery();	
			return setToMapForApproval(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Map<Object, ApprovalInfo> setToMapForApproval(ResultSet result)  {
		Map<Object,ApprovalInfo> map2 = new HashMap<Object,ApprovalInfo>();
		try {
			ResultSetMetaData rsmd = (ResultSetMetaData) result.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (result.next()) {
				Object[] arr= new Object[5];
				ApprovalInfo record=null;
			    for (int i = 1; i <= columnsNumber; i++) {
			        Object columnValue = result.getObject(i);
			         arr[i-1]=columnValue;  
			    }
			    record = new ApprovalInfo();
			    record.setApprovalId((long)arr[0]);
			    record.setUserId((long)arr[1]);
			    record.setAccountNumber((long) arr[2]);;
			    record.setAmount(((double)arr[3]));;
			    record.setStatus((String) arr[4]);
			    map2.put(record.getApprovalId(),record); 
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map2;
	}
	public boolean approveRequest(long approvalId,String status) throws KeyException {
		String query = "Update Admin_approval set Status = ? where approval_Id = ?";
		try(Connection connect = getConnection();PreparedStatement prpstmt = getPreStatement(query, connect);){
			prpstmt.setString(1, status);
			prpstmt.setLong(2, approvalId);
			int count = prpstmt.executeUpdate();
			if(count >0) {
				return true;
			}
			throw new KeyException("Invalid approval_Id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
enum ChooseCoulmn{
	MAIL("Mail_Id"),ADDRESS("Mobile_Number"),NUMBER("Mobile_Number");
	String value;
	 ChooseCoulmn(String value) {
		this.value = value;
	}
	 public String getColumn(){
		 return value;
	 }
}