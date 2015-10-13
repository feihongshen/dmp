package cn.explink.b2c.letv;

public class Letv {

	private String expressid; // 快递公司的唯一标识
	private String customerid; // 供货商Id
	private String send_url; // 也买酒URL
	private String private_key; // 密钥信息
	private String service_code; // 乐视提供

	public String getService_code() {
		return service_code;
	}

	public void setService_code(String service_code) {
		this.service_code = service_code;
	}

	public String getExpressid() {
		return expressid;
	}

	public void setExpressid(String expressid) {
		this.expressid = expressid;
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

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	private int maxCount; // 每次反馈数量，不能超过50条一次性

}
