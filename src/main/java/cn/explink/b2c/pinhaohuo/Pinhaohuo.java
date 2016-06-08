package cn.explink.b2c.pinhaohuo;

/**
 * 对接 属性 设置
 * @author Administrator
 *
 */
public class Pinhaohuo {

	
	private String customerids;  //在系统中的id
	private String private_key;  //加密字符串  双方约定
	
	private String feedbackUrl; //反馈给供方的地址
	private String importUrl; // 订单导入url
	private String deleServerMobile; //承运商客服电话
	private long maxCount; //每次请求查询数量
	private String code; //配送公司标识
	private long warehouseid; //订单导入库房ID
	private String customerArrs; //多个customerid 集合,多个业务，根据多个customerid配置
	
	
	public String getCustomerArrs() {
		return customerArrs;
	}
	public void setCustomerArrs(String customerArrs) {
		this.customerArrs = customerArrs;
	}
	public String getCustomerids() {
		return customerids;
	}
	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public long getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}
	
	
	
}
