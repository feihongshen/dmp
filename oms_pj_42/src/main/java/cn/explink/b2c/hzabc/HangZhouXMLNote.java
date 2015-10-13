package cn.explink.b2c.hzabc;

/**
 * ABC XML的节点
 * 
 * @author Administrator
 *
 */
public class HangZhouXMLNote {

	private String logisticProviderID; // 配送公司编号
	private String waybillNo; // 运单号 ->cwb
	private String status; // 状态 字典
	private String stateTime; // 状态产生时间
	private String operName; // 操作人
	private String operateTime; // 操作时间
	private String operatorUnit;// 操作站点
	private String deliveryMan; // 配送员
	private String reason; // 原因编号
	private String deliveryMobile; // 配送员电话

	private String scanId; // 唯一id，可设为order_flow
	private String preSiteName; // 上一站名称 存在时可用
	private String nextSiteName; // 下一站名称 存在时可用

	private String remark;
	private String actualMny; // 实收金额
	private String payMethod; // 现金、刷卡、预付、快捷支付、支票、其他
	private String posId; // POS机终端号

	public String getLogisticProviderID() {
		return logisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStateTime() {
		return stateTime;
	}

	public void setStateTime(String stateTime) {
		this.stateTime = stateTime;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperatorUnit() {
		return operatorUnit;
	}

	public void setOperatorUnit(String operatorUnit) {
		this.operatorUnit = operatorUnit;
	}

	public String getDeliveryMan() {
		return deliveryMan;
	}

	public void setDeliveryMan(String deliveryMan) {
		this.deliveryMan = deliveryMan;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDeliveryMobile() {
		return deliveryMobile;
	}

	public void setDeliveryMobile(String deliveryMobile) {
		this.deliveryMobile = deliveryMobile;
	}

	public String getScanId() {
		return scanId;
	}

	public void setScanId(String scanId) {
		this.scanId = scanId;
	}

	public String getPreSiteName() {
		return preSiteName;
	}

	public void setPreSiteName(String preSiteName) {
		this.preSiteName = preSiteName;
	}

	public String getNextSiteName() {
		return nextSiteName;
	}

	public void setNextSiteName(String nextSiteName) {
		this.nextSiteName = nextSiteName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getActualMny() {
		return actualMny;
	}

	public void setActualMny(String actualMny) {
		this.actualMny = actualMny;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

}
