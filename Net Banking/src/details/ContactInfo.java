package details;

public class ContactInfo {
	private String mail, address;
	private long mob;
	
	public void setMobile(long mob) {
		 this.mob = mob ;
	}
	public void setMail(String mail) {
		 this.mail= mail;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getMobile() {
		return mob;
	}
	public String getmail() {
		return mail;
	}
	public String getAddress() {
		return address;
	}
}
