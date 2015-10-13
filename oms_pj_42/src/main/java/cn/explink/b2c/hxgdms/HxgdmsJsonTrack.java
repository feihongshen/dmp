package cn.explink.b2c.hxgdms;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author Administrator 物流跟踪JONS-bean
 */
public class HxgdmsJsonTrack {
	@JsonProperty(value = "WorkCode")
	private String workCode; // 订单号cwb
	@JsonProperty(value = "FlowType")
	private String flowType; // 订单号cwb
	@JsonProperty(value = "OperationDesc")
	private String operationDesc; // 订单号cwb
	@JsonProperty(value = "OperationTime")
	private String operationTime; // 订单号cwb
	@JsonProperty(value = "OperationName")
	private String operationName; // 订单号cwb
	@JsonProperty(value = "DelveryCode")
	private String delveryCode; // 订单号cwb

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	public String getOperationDesc() {
		return operationDesc;
	}

	public void setOperationDesc(String operationDesc) {
		this.operationDesc = operationDesc;
	}

	public String getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getDelveryCode() {
		return delveryCode;
	}

	public void setDelveryCode(String delveryCode) {
		this.delveryCode = delveryCode;
	}

}
