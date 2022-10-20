package util;

public class EncryptDecrypt {
	private static  String KEY="Access_Token";
    public static final String WORDS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*_";
	public String encrypt(String password) {
	    String encrypedPassword="";
	    int keyLen=KEY.length();
	    int passwordLen=password.length();
	    int randomIndex= passwordLen%keyLen;
	    KEY=KEY+KEY.substring(0,randomIndex);
	    for (int index=0; index<passwordLen; index++ ){
	        encrypedPassword=encrypedPassword + WORDS.charAt((WORDS.indexOf(KEY.charAt(index)) + WORDS.indexOf(password.charAt(index)))%71);
	    }
	    return encrypedPassword;
	}
	public String decrypt(String encrypedPassword) {
	   String decryptedPassword = "";
	   int passwordLen=encrypedPassword.length();
	   for (int index=0; index<passwordLen; index++ ){
	     decryptedPassword=decryptedPassword + WORDS.charAt(((WORDS.indexOf(encrypedPassword.charAt(index)) - WORDS.indexOf(KEY.charAt(index))) +71 )%71);
	    }  
	return decryptedPassword;
	}
}
