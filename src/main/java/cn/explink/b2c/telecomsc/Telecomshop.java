package cn.explink.b2c.telecomsc;

/**
 * 
 * @author Administrator 联通查询接口设置
 */
public class Telecomshop {

	private String private_key; // 密钥
	private String customerid; // 在系统中的customerid
	private String receiver_url; // 接收电信请求的url
	private String sender_url; // 回传给电信url
	private int maxCount; // 每次查询最大
	private long warehouseid; // 订单导入库房

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getReceiver_url() {
		return receiver_url;
	}

	public void setReceiver_url(String receiver_url) {
		this.receiver_url = receiver_url;
	}

	public String getSender_url() {
		return sender_url;
	}

	public void setSender_url(String sender_url) {
		this.sender_url = sender_url;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

}
