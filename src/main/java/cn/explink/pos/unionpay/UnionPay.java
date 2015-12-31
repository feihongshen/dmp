package cn.explink.pos.unionpay;

public class UnionPay {

	private String private_key;
	private String request_url; // 请求URL
	private int isotherdeliveroper; // 他人刷卡限制 0 关闭 (可刷) 1 开启（不可刷）
	private String requestPosUrl; // 请求POS url
	private String resultCustomerid;//拒绝查询的电商

	public String getResultCustomerid() {
		return resultCustomerid;
	}

	public void setResultCustomerid(String resultCustomerid) {
		this.resultCustomerid = resultCustomerid;
	}

	public String getRequestPosUrl() {
		return requestPosUrl;
	}

	public void setRequestPosUrl(String requestPosUrl) {
		this.requestPosUrl = requestPosUrl;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public int getIsotherdeliveroper() {
		return isotherdeliveroper;
	}

	public void setIsotherdeliveroper(int isotherdeliveroper) {
		this.isotherdeliveroper = isotherdeliveroper;
	}

	public String getRequest_url() {
		return request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

}
