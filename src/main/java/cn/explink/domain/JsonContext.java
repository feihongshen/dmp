package cn.explink.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class JsonContext {
	// {"customerid":"0","remark":"remark1"}
	@JsonProperty(value = "customerid")
	String customerid;// 供货商
	@JsonProperty(value = "remark")
	String remark;// 订单描述

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
