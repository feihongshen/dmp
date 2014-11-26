package cn.explink.controller;

import java.math.BigDecimal;

import cn.explink.enumutil.CwbOrderTypeIdEnum;

public class GotoClassAndDeliveryDTO {

	private String auditingtime; // 审核时间
	private BigDecimal payupamount; // 需上交款的金额（站点交给公司）
	private String cwb; // 订单号
	private BigDecimal receivedfee; // 收到总金额
	private BigDecimal returnedfee; // 退还金额
	private BigDecimal businessfee; // 应处理金额
	private BigDecimal cash; // 现金实收
	private BigDecimal pos; // POS实收
	private String posremark; // POS备注
	private String mobilepodtime; // pos反馈时间
	private BigDecimal checkfee; // 支票实收
	private String checkremark; // 支票号备注
	private BigDecimal otherfee; // 其他金额
	private long podremarkid; // 配送结果备注id
	private String deliverstateremark; // 反馈的备注输入内容
	private long deliverealuser; // 归班人id
	private long cwbordertypeid; // 订单类型
	private long deliverystate; // 配送状态
	private String consigneename; // 收件人
	private String consigneephone; // 收件人电话

	private String updateTime;
	private BigDecimal codpos; // codpos实收

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getAuditingtime() {
		return auditingtime;
	}

	public void setAuditingtime(String auditingtime) {
		this.auditingtime = auditingtime;
	}

	public BigDecimal getPayupamount() {
		return payupamount;
	}

	public void setPayupamount(BigDecimal payupamount) {
		this.payupamount = payupamount;
	}

	public long getDeliverealuser() {
		return deliverealuser;
	}

	public void setDeliverealuser(long deliverealuser) {
		this.deliverealuser = deliverealuser;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public BigDecimal getReceivedfee() {
		return receivedfee;
	}

	public void setReceivedfee(BigDecimal receivedfee) {
		this.receivedfee = receivedfee;
	}

	public BigDecimal getReturnedfee() {
		return returnedfee;
	}

	public void setReturnedfee(BigDecimal returnedfee) {
		this.returnedfee = returnedfee;
	}

	public BigDecimal getBusinessfee() {
		return businessfee;
	}

	public void setBusinessfee(BigDecimal businessfee) {
		this.businessfee = businessfee;
	}

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public BigDecimal getPos() {
		return pos;
	}

	public void setPos(BigDecimal pos) {
		this.pos = pos;
	}

	public String getPosremark() {
		return posremark;
	}

	public void setPosremark(String posremark) {
		this.posremark = posremark;
	}

	public String getMobilepodtime() {
		return mobilepodtime;
	}

	public void setMobilepodtime(String mobilepodtime) {
		this.mobilepodtime = mobilepodtime;
	}

	public BigDecimal getCheckfee() {
		return checkfee;
	}

	public void setCheckfee(BigDecimal checkfee) {
		this.checkfee = checkfee;
	}

	public String getCheckremark() {
		return checkremark;
	}

	public void setCheckremark(String checkremark) {
		this.checkremark = checkremark;
	}

	public BigDecimal getOtherfee() {
		return otherfee;
	}

	public void setOtherfee(BigDecimal otherfee) {
		this.otherfee = otherfee;
	}

	public long getPodremarkid() {
		return podremarkid;
	}

	public void setPodremarkid(long podremarkid) {
		this.podremarkid = podremarkid;
	}

	public String getDeliverstateremark() {
		return deliverstateremark;
	}

	public void setDeliverstateremark(String deliverstateremark) {
		this.deliverstateremark = deliverstateremark;
	}

	public String getCwbordertypeName() {
		for (CwbOrderTypeIdEnum cotie : CwbOrderTypeIdEnum.values()) {
			if (cotie.getValue() == cwbordertypeid)
				return cotie.getText();
		}
		return "";
	}

	public String getDeliveryTypeName() {
		for (CwbOrderTypeIdEnum cot : CwbOrderTypeIdEnum.values()) {
			if (cot.getValue() == deliverystate)
				return cot.getText();
		}
		return "";
	}

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneephone() {
		return consigneephone;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public BigDecimal getCodpos() {
		return codpos;
	}

	public void setCodpos(BigDecimal codpos) {
		this.codpos = codpos;
	}

}
