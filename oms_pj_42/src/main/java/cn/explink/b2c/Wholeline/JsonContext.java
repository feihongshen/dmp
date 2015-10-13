package cn.explink.b2c.Wholeline;

import org.codehaus.jackson.annotate.JsonProperty;

public class JsonContext {
	@JsonProperty(value = "contentValue")
	String contentValue;// 操作描述
	@JsonProperty(value = "disOperTm")
	String disOperTm;// 操作时间: YYYY-MM-DD HH:mm
	@JsonProperty(value = "operEmpCode")
	String operEmpCode;// 操作人
	@JsonProperty(value = "operTypeCode")
	String operTypeCode;// 操作码
	@JsonProperty(value = "stayWayCode")
	String stayWayCode;// 滞留原因代码
	@JsonProperty(value = "waybillNo")
	String waybillNo;// 运单号

	public String getContentValue() {
		return contentValue;
	}

	public void setContentValue(String contentValue) {
		this.contentValue = contentValue;
	}

	public String getDisOperTm() {
		return disOperTm;
	}

	public void setDisOperTm(String disOperTm) {
		this.disOperTm = disOperTm;
	}

	public String getOperEmpCode() {
		return operEmpCode;
	}

	public void setOperEmpCode(String operEmpCode) {
		this.operEmpCode = operEmpCode;
	}

	public String getOperTypeCode() {
		return operTypeCode;
	}

	public void setOperTypeCode(String operTypeCode) {
		this.operTypeCode = operTypeCode;
	}

	public String getStayWayCode() {
		return stayWayCode;
	}

	public void setStayWayCode(String stayWayCode) {
		this.stayWayCode = stayWayCode;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

}
