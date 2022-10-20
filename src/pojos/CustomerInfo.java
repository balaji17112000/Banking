package pojos;

public class CustomerInfo {
	private String name,password, panNo;
	private long aadharNo;
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public void setAadharNo(long aadharNo) {
		this.aadharNo = aadharNo;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public String getPanNo() {
		return panNo;
	}
	public long getAadharNo() {
		return aadharNo;
	}
}
