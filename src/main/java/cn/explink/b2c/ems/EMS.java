package cn.explink.b2c.ems;

/**
 * EMS设置配置属性
 * @author huan.zhou
 */
public class EMS {

	private String orderSendUrl;//订单推送url
	private String orderPrivateKey;//订单接口秘钥
	private int sendOrderCount;//订单每次推送数量
	private int resendOrderCount;//订单重推次数上限
	private String emsTranscwbUrl;//ems运单获取url
	private String emsTranscwbPrivateKey;//ems运单接口秘钥
	private int emsTranscwbCount;//ems运单抓取数量
	private String emsStateUrl;//状态回传url
	private String emsStateKey;//状态回传接口秘钥
	private long supportKey;//异常码提供方
	public String getOrderSendUrl() {
		return orderSendUrl;
	}
	public void setOrderSendUrl(String orderSendUrl) {
		this.orderSendUrl = orderSendUrl;
	}
	public String getOrderPrivateKey() {
		return orderPrivateKey;
	}
	public void setOrderPrivateKey(String orderPrivateKey) {
		this.orderPrivateKey = orderPrivateKey;
	}
	public int getSendOrderCount() {
		return sendOrderCount;
	}
	public void setSendOrderCount(int sendOrderCount) {
		this.sendOrderCount = sendOrderCount;
	}
	public int getResendOrderCount() {
		return resendOrderCount;
	}
	public void setResendOrderCount(int resendOrderCount) {
		this.resendOrderCount = resendOrderCount;
	}
	public String getEmsTranscwbUrl() {
		return emsTranscwbUrl;
	}
	public void setEmsTranscwbUrl(String emsTranscwbUrl) {
		this.emsTranscwbUrl = emsTranscwbUrl;
	}
	public String getEmsTranscwbPrivateKey() {
		return emsTranscwbPrivateKey;
	}
	public void setEmsTranscwbPrivateKey(String emsTranscwbPrivateKey) {
		this.emsTranscwbPrivateKey = emsTranscwbPrivateKey;
	}
	public int getEmsTranscwbCount() {
		return emsTranscwbCount;
	}
	public void setEmsTranscwbCount(int emsTranscwbCount) {
		this.emsTranscwbCount = emsTranscwbCount;
	}
	public String getEmsStateUrl() {
		return emsStateUrl;
	}
	public void setEmsStateUrl(String emsStateUrl) {
		this.emsStateUrl = emsStateUrl;
	}
	public String getEmsStateKey() {
		return emsStateKey;
	}
	public void setEmsStateKey(String emsStateKey) {
		this.emsStateKey = emsStateKey;
	}
	public long getSupportKey() {
		return supportKey;
	}
	public void setSupportKey(long supportKey) {
		this.supportKey = supportKey;
	}
	
}
