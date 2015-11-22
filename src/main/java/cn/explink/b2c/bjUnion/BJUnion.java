package cn.explink.b2c.bjUnion;

public class BJUnion {
	private String requestUrl;//银联请求路径
	private String private_key;//密钥
	private String customerid;//客户id
	private String ischecksign;//是否校验加密信息
	
	
	public String getIschecksign() {
		return ischecksign;
	}
	public void setIschecksign(String ischecksign) {
		this.ischecksign = ischecksign;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	
}
