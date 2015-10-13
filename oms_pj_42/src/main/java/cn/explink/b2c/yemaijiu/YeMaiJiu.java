package cn.explink.b2c.yemaijiu;

public class YeMaiJiu {

	private String expressCode; // 快递公司的唯一标识
	private String customerid; // 供货商Id
	private String send_url; // 也买酒URL
	private String private_key; // 密钥信息
	private int maxCount; // 每次反馈数量，不能超过50条一次性

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getSend_url() {
		return send_url;
	}

	public void setSend_url(String send_url) {
		this.send_url = send_url;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

}
