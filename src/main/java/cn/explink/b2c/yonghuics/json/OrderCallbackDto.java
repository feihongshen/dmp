package cn.explink.b2c.yonghuics.json;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderExportCallback")
public class OrderCallbackDto implements Serializable {

	private static long serialVersionUID = 9150757861511043149L;
	// 用户代码
	private String userCode;
	// 请求时间
	private String requestTime;
	// 签名
	private String sign;
	// 回传包裹号，运单号
	private String cwbs;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getCwbs() {
		return cwbs;
	}

	public void setCwbs(String cwbs) {
		this.cwbs = cwbs;
	}

}
