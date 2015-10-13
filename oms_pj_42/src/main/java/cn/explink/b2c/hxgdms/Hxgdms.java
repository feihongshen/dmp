package cn.explink.b2c.hxgdms;

/**
 * 对接 属性 设置
 * 
 * @author Administrator
 *
 */
public class Hxgdms {

	private String customerids; // 在系统中的id
	private String secretKey; // 加密字符串 双方约定
	private String feedbackUrl; // 反馈给供方的地址
	private String importUrl; // 订单导入url
	private String deleServerMobile; // 承运商客服电话

	public String getDeleServerMobile() {
		return deleServerMobile;
	}

	public void setDeleServerMobile(String deleServerMobile) {
		this.deleServerMobile = deleServerMobile;
	}

	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

	private long maxCount; // 每次请求查询数量

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
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

	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	private String dcode; // 配送公司标识
	private long warehouseid; // 订单导入库房ID

}
