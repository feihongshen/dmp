package cn.explink.b2c.zhongliang;

public class Zhongliang {

	private String customerid;
	private String clientId;
	private String clientFlag;
	private String clientKey;
	private String clientConst;
	private String waitOrder_url;
	private String cancleOrder_url;
	private String orderStatus_url;
	private String nums;
	private long warehouseid; // 订单导入库房ID;

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientFlag() {
		return clientFlag;
	}

	public void setClientFlag(String clientFlag) {
		this.clientFlag = clientFlag;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	public String getClientConst() {
		return clientConst;
	}

	public void setClientConst(String clientConst) {
		this.clientConst = clientConst;
	}

	public String getWaitOrder_url() {
		return waitOrder_url;
	}

	public void setWaitOrder_url(String waitOrder_url) {
		this.waitOrder_url = waitOrder_url;
	}

	public String getCancleOrder_url() {
		return cancleOrder_url;
	}

	public void setCancleOrder_url(String cancleOrder_url) {
		this.cancleOrder_url = cancleOrder_url;
	}

	public String getOrderStatus_url() {
		return orderStatus_url;
	}

	public void setOrderStatus_url(String orderStatus_url) {
		this.orderStatus_url = orderStatus_url;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getNums() {
		return nums;
	}

	public void setNums(String nums) {
		this.nums = nums;
	}

}
