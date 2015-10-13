package cn.explink.b2c.liantong;

/**
 * 
 * @author Administrator 联通查询接口设置
 */
public class Liantong {

	private String private_key; // 密钥
	private String customerid; // 在系统中的customerid
	private String search_url; // 提供查询url
	private long warehouseid; // 订单入库库房
	private String request_url; // 请求URL
	private String feedback_url;// 状态回传url

	// ///////物流状态回传通知//////////////////
	private String appcode; // 应用平台key
	private String appsecret; // 应用平台密钥
	private String signSecurity; // 签名密钥
	private String appkey; // 渠道key
	private String method; // 调用方法

	private long maxCount;// 每次查询大小

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

	public String getAppcode() {
		return appcode;
	}

	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getSignSecurity() {
		return signSecurity;
	}

	public void setSignSecurity(String signSecurity) {
		this.signSecurity = signSecurity;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getRequest_url() {
		return request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getSearch_url() {
		return search_url;
	}

	public void setSearch_url(String search_url) {
		this.search_url = search_url;
	}

}
