package cn.explink.b2c.zhemeng;

public class Zhemeng {
	private String tms_service_code; // 客户
	private String private_key; // 密钥信息
	private String receiver_url; //接收url
	private String send_url; // 推送url
	private String customerid; //供货商
	private long warehouseid; // 库房
	public String getTms_service_code() {
		return tms_service_code;
	}
	public void setTms_service_code(String tms_service_code) {
		this.tms_service_code = tms_service_code;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public String getReceiver_url() {
		return receiver_url;
	}
	public void setReceiver_url(String receiver_url) {
		this.receiver_url = receiver_url;
	}
	public String getSend_url() {
		return send_url;
	}
	public void setSend_url(String send_url) {
		this.send_url = send_url;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public long getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}
	
	
}
