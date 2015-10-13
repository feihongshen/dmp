package cn.explink.b2c.hzabc;

/**
 * 对接 属性 设置
 * 
 * @author Administrator
 *
 */
public class HangZhouABC {

	private String customerids; // 在系统中的ids
	private String requst_url; // 提供给 杭州abc的url
	private String private_key; // 密钥
	private String shippedCode; // 快递公司标识 ABC下单
	private String logisticProviderID; // 反馈唯一标识

	public String getLogisticProviderID() {
		return logisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}

	private long exportbranchid; // 订单导入库房ID

	public String getShippedCode() {
		return shippedCode;
	}

	public void setShippedCode(String shippedCode) {
		this.shippedCode = shippedCode;
	}

	private String feedback_url; // 反馈给abc的url
	private int maxCount; // 每次查询大小

	public String getCustomerids() {
		return customerids;
	}

	public long getExportbranchid() {
		return exportbranchid;
	}

	public void setExportbranchid(long exportbranchid) {
		this.exportbranchid = exportbranchid;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getRequst_url() {
		return requst_url;
	}

	public void setRequst_url(String requst_url) {
		this.requst_url = requst_url;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

}
