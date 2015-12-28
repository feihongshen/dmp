package cn.explink.b2c.huanqiugou;

/**
 * 对接 属性 设置
 * @author Administrator
 *
 */
public class Huanqiugou {

	
	private String customerids;  //在系统中的id
	private String privateKey;  //加密字符串  双方约定
	private String feedbackUrl; //反馈给供方的地址
	private String receivedUrl; // 订单导入url
	private String expressid;
	private int maxcount;
	private long warehouseid; //订单导入的库房Id
	public String getCustomerids() {
		return customerids;
	}
	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getFeedbackUrl() {
		return feedbackUrl;
	}
	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}
	public String getReceivedUrl() {
		return receivedUrl;
	}
	public void setReceivedUrl(String receivedUrl) {
		this.receivedUrl = receivedUrl;
	}
	public String getExpressid() {
		return expressid;
	}
	public void setExpressid(String expressid) {
		this.expressid = expressid;
	}
	public int getMaxcount() {
		return maxcount;
	}
	public void setMaxcount(int maxcount) {
		this.maxcount = maxcount;
	}
	public long getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}
	
	

	
	
	
}
