package cn.explink.b2c.yihaodian.xmldto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderExportCallback")
public class OrderExportCallbackDto implements Serializable {

	private static final long serialVersionUID = 9150757861511043149L;
	// 用户代码
	private String userCode;
	// 请求时间
	private String requestTime;
	// 签名
	private String sign;
	// 运单号
	private String shipmentCode;

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

	public String getShipmentCode() {
		return shipmentCode;
	}

	public void setShipmentCode(String shipmentCode) {
		this.shipmentCode = shipmentCode;
	}

}
