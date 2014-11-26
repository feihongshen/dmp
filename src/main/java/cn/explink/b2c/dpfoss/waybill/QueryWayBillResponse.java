package cn.explink.b2c.dpfoss.waybill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class QueryWayBillResponse implements Serializable {
	// private String waybillId;
	private String waybillNo;
	private String payType;
	private String externalUserName;
	private String externalUserCode;
	private String externalOrgName;
	private String externalOrgCode;
	private String agentCompanyName;
	private String agentCompanyCode;
	private String pickupType;
	private String returnType;
	private String receiveName;
	private String receiveAddr;
	private String receiveCompany;
	private String receivePhone;
	private BigDecimal freightFee;
	private BigDecimal codAmount;
	private BigDecimal payDpFee;
	private BigDecimal declarationValue;
	private int goodsNum;
	private BigDecimal weight;
	private BigDecimal volume;
	private String notes;
	private String auditStatus;
	private Date modifyTime;
	private BigDecimal insuranceFee;
	private BigDecimal paymentCollectionFee;

	public BigDecimal getPaymentCollectionFee() {
		return paymentCollectionFee;
	}

	public void setPaymentCollectionFee(BigDecimal paymentCollectionFee) {
		this.paymentCollectionFee = paymentCollectionFee;
	}

	public BigDecimal getInsuranceFee() {
		return insuranceFee;
	}

	public void setInsuranceFee(BigDecimal insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getExternalUserName() {
		return externalUserName;
	}

	public void setExternalUserName(String externalUserName) {
		this.externalUserName = externalUserName;
	}

	public String getExternalUserCode() {
		return externalUserCode;
	}

	public void setExternalUserCode(String externalUserCode) {
		this.externalUserCode = externalUserCode;
	}

	public String getExternalOrgName() {
		return externalOrgName;
	}

	public void setExternalOrgName(String externalOrgName) {
		this.externalOrgName = externalOrgName;
	}

	public String getExternalOrgCode() {
		return externalOrgCode;
	}

	public void setExternalOrgCode(String externalOrgCode) {
		this.externalOrgCode = externalOrgCode;
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

	public String getPickupType() {
		return pickupType;
	}

	public void setPickupType(String pickupType) {
		this.pickupType = pickupType;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getReceiveAddr() {
		return receiveAddr;
	}

	public void setReceiveAddr(String receiveAddr) {
		this.receiveAddr = receiveAddr;
	}

	public String getReceiveCompany() {
		return receiveCompany;
	}

	public void setReceiveCompany(String receiveCompany) {
		this.receiveCompany = receiveCompany;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public BigDecimal getFreightFee() {
		return freightFee;
	}

	public void setFreightFee(BigDecimal freightFee) {
		this.freightFee = freightFee;
	}

	public BigDecimal getPayDpFee() {
		return payDpFee;
	}

	public void setPayDpFee(BigDecimal payDpFee) {
		this.payDpFee = payDpFee;
	}

	public BigDecimal getDeclarationValue() {
		return declarationValue;
	}

	public void setDeclarationValue(BigDecimal declarationValue) {
		this.declarationValue = declarationValue;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public BigDecimal getCodAmount() {
		return codAmount;
	}

	public void setCodAmount(BigDecimal codAmount) {
		this.codAmount = codAmount;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
}
