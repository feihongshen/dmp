package cn.explink.b2c.wangjiu;

/**
 * 对接 属性 设置
 * 
 * @author Administrator
 *
 */
public class Wangjiu {

	private String customerid; // 在系统中的id
	private String parternID; // 配送公司标识，也作为 签名用
	private String feedbackUrl; // 反馈给供方的地址
	private String importUrl; // 订单导入url

	private long warehouseid; // 订单导入库房ID

	private int maxCount; // 每次查询大小推送
	private String private_key;

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getParternID() {
		return parternID;
	}

	public void setParternID(String parternID) {
		this.parternID = parternID;
	}

	public String getFeedbackUrl() {
		return feedbackUrl;
	}

	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}

	public String getImportUrl() {
		return importUrl;
	}

	public void setImportUrl(String importUrl) {
		this.importUrl = importUrl;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

}
