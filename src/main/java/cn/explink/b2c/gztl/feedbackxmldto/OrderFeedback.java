package cn.explink.b2c.gztl.feedbackxmldto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrderFeedback {

	private String id;// ID，惟一值。用于返回是否接收成功
	private String logisticProviderId;// 配送公司编号(由飞远提供)
	private String waybillNo;// 运单编号
	private String status;// 状态(由飞远提供)
	private String stateTime;// 状态产生时间
	private String operatorName;// 操作人
	private String operatorTime;// 操作时间
	private String deliveryman;// 配送员 可以为空
	private String deliverymanMobile;// 配送员电话 可以为空
	private String reason;// 原因(由飞远提供) 可以为空
	private String scanId;// 当前站点
	private String preSiteName;// 上一站名称 可以为空
	private String nextSiteName;// 下一站名称 可以为空
	private String remark;// 备注 可以为空
	private String payMethod;// POS机标识 可以为空
	private String receiveTime;// 接收时间

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogisticProviderId() {
		return this.logisticProviderId;
	}

	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

	public String getWaybillNo() {
		return this.waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStateTime() {
		return this.stateTime;
	}

	public void setStateTime(String stateTime) {
		this.stateTime = stateTime;
	}

	public String getOperatorName() {
		return this.operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorTime() {
		return this.operatorTime;
	}

	public void setOperatorTime(String operatorTime) {
		this.operatorTime = operatorTime;
	}

	public String getDeliveryman() {
		return this.deliveryman;
	}

	public void setDeliveryman(String deliveryman) {
		this.deliveryman = deliveryman;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getScanId() {
		return this.scanId;
	}

	public void setScanId(String scanId) {
		this.scanId = scanId;
	}

	public String getPreSiteName() {
		return this.preSiteName;
	}

	public void setPreSiteName(String preSiteName) {
		this.preSiteName = preSiteName;
	}

	public String getNextSiteName() {
		return this.nextSiteName;
	}

	public void setNextSiteName(String nextSiteName) {
		this.nextSiteName = nextSiteName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPayMethod() {
		return this.payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getReceiveTime() {
		return this.receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getDeliverymanMobile() {
		return this.deliverymanMobile;
	}

	public void setDeliverymanMobile(String deliverymanMobile) {
		this.deliverymanMobile = deliverymanMobile;
	}

}
