package cn.explink.b2c.maikolin;

public class Maikolin {
	private String userCode; // 用户代码，用来标识哪个承运商
	private String private_key; // 密钥信息
	private String pushCwb_URL;
	private String customerids; // 在系统中的id
	private long warehouseid; // 订单入库库房Id
	private long callBackCount; // 每次回调数量
	private int Isopendownload;
	private String express_id;

	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	public String getPushCwb_URL() {
		return pushCwb_URL;
	}

	public void setPushCwb_URL(String pushCwb_URL) {
		this.pushCwb_URL = pushCwb_URL;
	}

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

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public long getCallBackCount() {
		return callBackCount;
	}

	public void setCallBackCount(long callBackCount) {
		this.callBackCount = callBackCount;
	}

	public int getIsopendownload() {
		return Isopendownload;
	}

	public void setIsopendownload(int isopendownload) {
		Isopendownload = isopendownload;
	}

}
