package cn.explink.domain.orderflow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
public class OrderNote {

	private String orderNo;//订单号
	private String transOrderNo;
	private String operationTime;//操作时间
	private String operatorName;//操作人
	private String operationTrack;//订单操作轨迹
	private String userCode;//用户标识
	private String status; //状态码
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the orderNo
	 */
	@XmlElement(name = "order_no")
	public String getOrderNo() {
		return orderNo;
	}
	@XmlElement(name = "transorder_no")
	public String getTransOrderNo() {
		return transOrderNo;
	}
	public void setTransOrderNo(String transOrderNo) {
		this.transOrderNo = transOrderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return the operationTime
	 */
	@XmlElement(name = "operation_time")
	public String getOperationTime() {
		return operationTime;
	}
	/**
	 * @param operationTime the operationTime to set
	 */
	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}
	/**
	 * @return the operatorName
	 */
	@XmlElement(name = "operation_name")
	public String getOperatorName() {
		return operatorName;
	}
	/**
	 * @param operatorName the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	/**
	 * @return the operationTrack
	 */
	@XmlElement(name = "operation_track")
	public String getOperationTrack() {
		return operationTrack;
	}
	/**
	 * @param operationTrack the operationTrack to set
	 */
	public void setOperationTrack(String operationTrack) {
		this.operationTrack = operationTrack;
	}
}
