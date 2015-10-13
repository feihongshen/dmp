package cn.explink.b2c.dpfoss.track;

import java.io.Serializable;
import java.util.Date;

public class UploadTrackRequest implements Serializable {

	private String traceId;
	private String waybillNo;
	private String agentCompanyName;
	private String agentCompanyCode;
	private String agentOrgName;
	private String agentOrgCode;
	private Date operationTime;
	private String operationUserName;
	private String operationDescribe;
	private String operationcity;
	private String operationtype;
	private String dispatchname;
	private String dispatchphoneno;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getAgentCompanyName() {
		return agentCompanyName;
	}

	public void setAgentCompanyName(String agentCompanyName) {
		this.agentCompanyName = agentCompanyName;
	}

	public String getAgentCompanyCode() {
		return agentCompanyCode;
	}

	public void setAgentCompanyCode(String agentCompanyCode) {
		this.agentCompanyCode = agentCompanyCode;
	}

	public String getAgentOrgName() {
		return agentOrgName;
	}

	public void setAgentOrgName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}

	public String getAgentOrgCode() {
		return agentOrgCode;
	}

	public void setAgentOrgCode(String agentOrgCode) {
		this.agentOrgCode = agentOrgCode;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public String getOperationUserName() {
		return operationUserName;
	}

	public void setOperationUserName(String operationUserName) {
		this.operationUserName = operationUserName;
	}

	public String getOperationDescribe() {
		return operationDescribe;
	}

	public void setOperationDescribe(String operationDescribe) {
		this.operationDescribe = operationDescribe;
	}

	public String getOperationcity() {
		return operationcity;
	}

	public void setOperationcity(String operationcity) {
		this.operationcity = operationcity;
	}

	public String getOperationtype() {
		return operationtype;
	}

	public void setOperationtype(String operationtype) {
		this.operationtype = operationtype;
	}

	public String getDispatchname() {
		return dispatchname;
	}

	public void setDispatchname(String dispatchname) {
		this.dispatchname = dispatchname;
	}

	public String getDispatchphoneno() {
		return dispatchphoneno;
	}

	public void setDispatchphoneno(String dispatchphoneno) {
		this.dispatchphoneno = dispatchphoneno;
	}

}
