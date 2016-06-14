package cn.explink.b2c.zhemeng.track;

/**
 * 哲盟_轨迹 接口 配置信息实体
 * @author yurong.liang 2016-05-31
 */
public class ZhemengTrack {
	private String tms_service_code; // 承运商code
	private String private_key; // 密钥
	private String send_url; // 推送轨迹url
	private String customerid; //供货商id
	
	public String getTms_service_code() {
		return tms_service_code;
	}
	public void setTms_service_code(String tms_service_code) {
		this.tms_service_code = tms_service_code;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public String getSend_url() {
		return send_url;
	}
	public void setSend_url(String send_url) {
		this.send_url = send_url;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
}
