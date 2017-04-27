package cn.explink.domain.json;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class QueryCwbResponseDto {
	private String returnCode;// 状态
	private String remark;// 描述
	private List<QueryOrder> queryOrders;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<QueryOrder> getQueryOrders() {
		return queryOrders;
	}

	public void setQueryOrders(List<QueryOrder> queryOrders) {
		this.queryOrders = queryOrders;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	

}
