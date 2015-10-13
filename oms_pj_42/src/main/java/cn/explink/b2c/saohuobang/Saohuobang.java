package cn.explink.b2c.saohuobang;

/**
 * @author Administrator
 *
 */
public class Saohuobang {

	private String clientID; // 用户代码，用来标识哪个承运商
	private String ProviderID; // 密钥信息
	private String customerId;// 客户编号
	private String trackLog_URL; // 跟踪日志反馈接口URL
	private long callBackCount; // 每次回调数量
	private int Isopendownload;// 下载订单接口开启与否
	private long warehouseid;
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTrackLog_URL() {
		return trackLog_URL;
	}

	public void setTrackLog_URL(String trackLog_URL) {
		this.trackLog_URL = trackLog_URL;
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

	public String getProviderID() {
		return ProviderID;
	}

	public void setProviderID(String providerID) {
		ProviderID = providerID;
	}

}