package banking;

import java.util.regex.Pattern;

import util.KeyException;

public class UpdateDetailsValidator {
	
	private boolean patternCheck(String str, String expression) {
		return Pattern.matches(expression, str);
	}
	private boolean emailCheck(String email) throws KeyException {
		return patternCheck(email,"^\\w+@{1}?.\\w+\\.{1}+.\\w+"); 
	}
	public boolean numberCheck(String str) throws KeyException{	
		int n= str.length();
		if( n!=10) {
			throw new KeyException("Sorry can't able to Update phone number Enter 10 digit to update");
		}
		return patternCheck(str,"^[6-9]\\d{9}");
	}
	private boolean isPassword(String str) throws KeyException {
		return patternCheck(str,"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+]).{8,30}$");
	}
	private boolean isAddress(String str) throws KeyException {
		return patternCheck(str,"^(?=.*[0-9])(?=.*\\w).{15,50}$");
	}
	public boolean displayPage(String word,int index) throws KeyException {	
		boolean flag = true;
		
		if(index ==1) {
			if(!emailCheck(word)) {
				flag = false;
				throw new KeyException("Invalid Email Id");
			}
		}
		else if(index ==2) {
			if(!numberCheck(word)) {
				flag = false;
				throw new KeyException("Invalid phone Number, Number should start from 6-9");
			}
		}
		else if(index==3) {
			if(!isAddress(word)) {
				flag = false;
				throw new KeyException("Invalid Address!  \nAddress should be in AlphaNumeric" );
			}
		}
		else if(index==4) {
			if(!isPassword(word)) {
				flag = false;
				throw new KeyException("Invalid Password!  \nPassword should be 8-30 charecter and sholud have atleast one AlphaNumeric, Symbol and Capital letter" );
			}
		}
		return flag;
	}
}
