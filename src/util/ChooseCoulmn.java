package util;

public enum ChooseCoulmn{
	MAIL("Mail_Id"),ADDRESS("Mobile_Number"),NUMBER("Mobile_Number");
	String value;
	 ChooseCoulmn(String value) {
		this.value = value;
	}
	 public String getColumn(){
		 return value;
	 }
}