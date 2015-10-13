package cn.explink.b2c.smiled.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
public class SmiledOrderStatus {

	private String workCode; // 运单号
	private String operationTime; // 操作时间
	private String operationName; // 操作人

	private String operationUnitname; // 操作站点
	private String operationType; // 状态类型
	private String remark; // 备注
	private String isState; // 0分单失败，1 分单成功 看具体情况 操作类型=异常录入( 0：其他异常 1：拒收 2：改派 )

	@XmlElement(name = "WorkCode")
	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	@XmlElement(name = "OperationTime")
	public String getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}

	@XmlElement(name = "OperationName")
	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	@XmlElement(name = "OperationUnitname")
	public String getOperationUnitname() {
		return operationUnitname;
	}

	public void setOperationUnitname(String operationUnitname) {
		this.operationUnitname = operationUnitname;
	}

	@XmlElement(name = "OperationType")
	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	@XmlElement(name = "Remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlElement(name = "IsState")
	public String getIsState() {
		return isState;
	}

	public void setIsState(String isState) {
		this.isState = isState;
	}

}
