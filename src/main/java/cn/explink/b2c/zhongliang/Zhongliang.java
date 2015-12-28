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
	private String backOrder_url;
	private String backOrderStatus_url;
	private String backCancel_url;
	private String nums;
	private long warehouseid; // 订单导入库房ID;

	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientFlag() {
		return this.clientFlag;
	}

	public void setClientFlag(String clientFlag) {
		this.clientFlag = clientFlag;
	}

	public String getClientKey() {
		return this.clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	public String getClientConst() {
		return this.clientConst;
	}

	public void setClientConst(String clientConst) {
		this.clientConst = clientConst;
	}

	public String getWaitOrder_url() {
		return this.waitOrder_url;
	}

	public void setWaitOrder_url(String waitOrder_url) {
		this.waitOrder_url = waitOrder_url;
	}

	public String getCancleOrder_url() {
		return this.cancleOrder_url;
	}

	public void setCancleOrder_url(String cancleOrder_url) {
		this.cancleOrder_url = cancleOrder_url;
	}

	public String getOrderStatus_url() {
		return this.orderStatus_url;
	}

	public void setOrderStatus_url(String orderStatus_url) {
		this.orderStatus_url = orderStatus_url;
	}

	public String getBackOrder_url() {
		return this.backOrder_url;
	}

	public void setBackOrder_url(String backOrder_url) {
		this.backOrder_url = backOrder_url;
	}

	public String getBackOrderStatus_url() {
		return this.backOrderStatus_url;
	}

	public void setBackOrderStatus_url(String backOrderStatus_url) {
		this.backOrderStatus_url = backOrderStatus_url;
	}

	public String getBackCancel_url() {
		return this.backCancel_url;
	}

	public void setBackCancel_url(String backCancel_url) {
		this.backCancel_url = backCancel_url;
	}

	public long getWarehouseid() {
		return this.warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getNums() {
		return this.nums;
	}

	public void setNums(String nums) {
		this.nums = nums;
	}

}
