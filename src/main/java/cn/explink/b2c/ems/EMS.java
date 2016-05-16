package cn.explink.b2c.ems;

/**
 * EMS设置配置属性
 * @author huan.zhou
 */
public class EMS {

	private String orderSendUrl;//订单推送url
	private String encodeKey;//订单接口秘钥
	private int sendOrderCount;//订单每次推送数量
	private String emsTranscwbUrl;//ems运单获取url
	private String emsStateUrl;//状态回传url
	private long supportKey;//异常码提供方
	private String appKey;//对接授权码
	private String sysAccount;//账号（大客户号）
	private long emsDiliveryid;//ems小件员编号
	private long emsBranchid;//ems站点编号
	public String getOrderSendUrl() {
		return orderSendUrl;
	}
	public void setOrderSendUrl(String orderSendUrl) {
		this.orderSendUrl = orderSendUrl;
	}
	public String getEncodeKey() {
		return encodeKey;
	}
	public void setEncodeKey(String encodeKey) {
		this.encodeKey = encodeKey;
	}
	public int getSendOrderCount() {
		return sendOrderCount;
	}
	public void setSendOrderCount(int sendOrderCount) {
		this.sendOrderCount = sendOrderCount;
	}
	public String getEmsTranscwbUrl() {
		return emsTranscwbUrl;
	}
	public void setEmsTranscwbUrl(String emsTranscwbUrl) {
		this.emsTranscwbUrl = emsTranscwbUrl;
	}
	public String getEmsStateUrl() {
		return emsStateUrl;
	}
	public void setEmsStateUrl(String emsStateUrl) {
		this.emsStateUrl = emsStateUrl;
	}
	public long getSupportKey() {
		return supportKey;
	}
	public void setSupportKey(long supportKey) {
		this.supportKey = supportKey;
	}
	public String getSysAccount() {
		return sysAccount;
	}
	public void setSysAccount(String sysAccount) {
		this.sysAccount = sysAccount;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public long getEmsDiliveryid() {
		return emsDiliveryid;
	}
	public void setEmsDiliveryid(long emsDiliveryid) {
		this.emsDiliveryid = emsDiliveryid;
	}
	public long getEmsBranchid() {
		return emsBranchid;
	}
	public void setEmsBranchid(long emsBranchid) {
		this.emsBranchid = emsBranchid;
	}
}
