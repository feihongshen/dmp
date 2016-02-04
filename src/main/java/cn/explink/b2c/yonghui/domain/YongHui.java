package cn.explink.b2c.yonghui.domain;

public class YongHui {
	private String userCode;
	private String private_key;
	private String client_id;
	private String secret;
	private long customerid;
	private long warehouserid;
	private String orderState_url;
	private long orderStateCount;

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPrivate_key() {
		return this.private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public long getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getWarehouserid() {
		return this.warehouserid;
	}

	public void setWarehouserid(long warehouserid) {
		this.warehouserid = warehouserid;
	}

	public String getOrderState_url() {
		return this.orderState_url;
	}

	public void setOrderState_url(String orderState_url) {
		this.orderState_url = orderState_url;
	}

	public long getOrderStateCount() {
		return this.orderStateCount;
	}

	public void setOrderStateCount(long orderStateCount) {
		this.orderStateCount = orderStateCount;
	}

	public String getClient_id() {
		return this.client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
