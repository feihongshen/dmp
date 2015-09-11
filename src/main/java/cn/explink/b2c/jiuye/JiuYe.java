package cn.explink.b2c.jiuye;


public class JiuYe {
	
	private String customerid; //在配送公司中的id
	private String private_key;//密钥
	private String importUrl;//订单导入Url
	private String feedbackUrl;//反馈Url
	private int warehouseid;//导入库房id
	private int maxCount; //每次查询的大小
	private String dmsCode;//承运商编码
	private int b2cenum; 
	

	public int getB2cenum() {
		return b2cenum;
	}
	public void setB2cenum(int b2cenum) {
		this.b2cenum = b2cenum;
	}
	public String getDmsCode() {
		return dmsCode;
	}
	public void setDmsCode(String dmsCode) {
		this.dmsCode = dmsCode;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
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
	
	public int getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(int warehouseid) {
		this.warehouseid = warehouseid;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	
	
}