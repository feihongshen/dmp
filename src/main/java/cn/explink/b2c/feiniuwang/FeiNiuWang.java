package cn.explink.b2c.feiniuwang;

public class FeiNiuWang {
	private String customerid; //在配送公司中的id
	private String requestKey;//请求密钥
	private String responseKey;//响应秘钥
	private String feedbackUrl;//反馈Url
	private int warehouseid;//导入库房id
	private String maxCount; //每次查询的大小
	private String dmsCode;//承运商编码
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	public String getRequestKey() {
		return requestKey;
	}
	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}
	public String getResponseKey() {
		return responseKey;
	}
	public void setResponseKey(String responseKey) {
		this.responseKey = responseKey;
	}
	public String getFeedbackUrl() {
		return feedbackUrl;
	}
	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}
	public int getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(int warehouseid) {
		this.warehouseid = warehouseid;
	}
	public String getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(String maxCount) {
		this.maxCount = maxCount;
	}
	public String getDmsCode() {
		return dmsCode;
	}
	public void setDmsCode(String dmsCode) {
		this.dmsCode = dmsCode;
	}
	
}
