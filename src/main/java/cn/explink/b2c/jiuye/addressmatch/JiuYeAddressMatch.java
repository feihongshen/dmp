package cn.explink.b2c.jiuye.addressmatch;

public class JiuYeAddressMatch {
	private String receiver_url;//匹配url
	private String customerid; //在配送公司中的id
	private String private_key;//密钥
	private int maxCount; //每次查询的大小
	private String dmsCode;//承运商编码
	private int b2cenum; 
	
	public int getB2cenum() {
		return b2cenum;
	}
	public void setB2cenum(int b2cenum) {
		this.b2cenum = b2cenum;
	}
	public String getDmsCode() {
		return dmsCode;
	}
	public void setDmsCode(String dmsCode) {
		this.dmsCode = dmsCode;
	}
	public String getCustomerid() {
		return customerid;
	}
	
	public String getReceiver_url() {
		return receiver_url;
	}
	public void setReceiver_url(String receiver_url) {
		this.receiver_url = receiver_url;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
}
