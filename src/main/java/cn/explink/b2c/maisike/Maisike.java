package cn.explink.b2c.maisike;

public class Maisike {

	private String appname; // 用户名
	private String app_key; // 授权码 密钥
	private String send_url;// 请求迈思可URL
	private String search_key; // 订单查询接口的密钥
	private String feedback_url; // 开发给迈思可状态回传url
	private long maxCount; // 每次发送数量
	private long loopcount; // 重发次数

	public long getLoopcount() {
		return loopcount;
	}

	public void setLoopcount(long loopcount) {
		this.loopcount = loopcount;
	}

	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public String getSend_url() {
		return send_url;
	}

	public void setSend_url(String send_url) {
		this.send_url = send_url;
	}

	public String getSearch_key() {
		return search_key;
	}

	public void setSearch_key(String search_key) {
		this.search_key = search_key;
	}

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

}
