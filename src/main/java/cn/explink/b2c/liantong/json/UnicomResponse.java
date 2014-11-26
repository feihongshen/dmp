package cn.explink.b2c.liantong.json;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class UnicomResponse {

	@JsonProperty(value = "OrderNo")
	@JsonIgnore
	private String orderNo;
	@JsonProperty(value = "RespCode")
	private String respCode;
	@JsonProperty(value = "RespDesc")
	private String respDesc;
	@JsonProperty(value = "MailNo")
	private String mailNo;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

}
