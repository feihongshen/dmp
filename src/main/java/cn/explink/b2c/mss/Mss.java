package cn.explink.b2c.mss;

/**
 * 电商管理美食送
 *
 * @author zhaoshb
 * @since AR1.0
 */
public class Mss {

	private String importUrl; // 请求URL
	private String feedbackUrl;// 状态回传url
	private String imgUrl;//图片上传Url
	private long warehouseid; // 订单入库库房
	private String customerid; // 在系统中的customerid
	private int maxCount; // 每次查询的大小
	private String secretKey;//私钥
	public String getImportUrl() {
		return importUrl;
	}
	public void setImportUrl(String importUrl) {
		this.importUrl = importUrl;
	}
	public String getFeedbackUrl() {
		return feedbackUrl;
	}
	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public long getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	

	

	
}
