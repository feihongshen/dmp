package cn.explink.b2c.liantongordercenter;

public class LianTongOrderCenter {

	private String private_key; // 密钥
	private String customerid; // 在系统中的customerid
	private long warehouseid; // 订单入库库房
	private String request_url; // 请求URL
	private String feedback_url;// 状态回传url
	private long maxCount;// 每次查询大小
	private String search_url; // 提供查询url
	private String appsecret; // 应用平台密钥
	private String appkey; // 渠道key

	public String getPrivate_key() {
		return this.private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public long getWarehouseid() {
		return this.warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getRequest_url() {
		return this.request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	public String getFeedback_url() {
		return this.feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

	public long getMaxCount() {
		return this.maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

	public String getSearch_url() {
		return this.search_url;
	}

	public void setSearch_url(String search_url) {
		this.search_url = search_url;
	}

	public String getAppsecret() {
		return this.appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getAppkey() {
		return this.appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

}
