package cn.explink.b2c.zhts;

public class Zhts {

	private String userCode; // 承运商编码
	private String private_key; // 下单、查询顺丰快递URL
	private String searhUrl;
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public String getSearhUrl() {
		return searhUrl;
	}
	public void setSearhUrl(String searhUrl) {
		this.searhUrl = searhUrl;
	}

	

}
