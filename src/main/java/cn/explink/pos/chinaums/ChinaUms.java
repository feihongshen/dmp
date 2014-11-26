package cn.explink.pos.chinaums;

public class ChinaUms {

	private String private_key;
	private String request_url; // 请求URL
	private int isotherdeliveroper; // 他人刷卡限制 0 关闭 (可刷) 1 开启（不可刷）
	private String mer_id; // 商户号

	public String getMer_id() {
		return mer_id;
	}

	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
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
