package cn.explink.domain;

import java.math.*;

/**
 * express_set_exceed_subsidy_apply 实体类 
 * Fri Jul 17 10:03:33 CST 2015
 * LiChongChong
 */

public class ExpressSetExceedSubsidyApply {
	private int id;
	private String applyNo;
	private int applyState;
	private int deliveryPerson;
	private String cwbOrder;
	private int cwbOrderState;
	private String receiveAddress;
	private int isExceedAreaSubsidy;
	private String exceedAreaSubsidyRemark;
	private BigDecimal exceedAreaSubsidyAmount;
	private int isBigGoodsSubsidy;
	private String bigGoodsSubsidyRemark;
	private BigDecimal bigGoodsSubsidyAmount;
	private String remark;
	private int shenHePerson;
	private String shenHeTime;
	private String shenHeIdea;
	private String createTime;
	private String updateTime;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyState(int applyState) {
		this.applyState = applyState;
	}

	public int getApplyState() {
		return applyState;
	}

	public void setDeliveryPerson(int deliveryPerson) {
		this.deliveryPerson = deliveryPerson;
	}

	public int getDeliveryPerson() {
		return deliveryPerson;
	}

	public void setCwbOrder(String cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

	public String getCwbOrder() {
		return cwbOrder;
	}

	public void setCwbOrderState(int cwbOrderState) {
		this.cwbOrderState = cwbOrderState;
	}

	public int getCwbOrderState() {
		return cwbOrderState;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setIsExceedAreaSubsidy(int isExceedAreaSubsidy) {
		this.isExceedAreaSubsidy = isExceedAreaSubsidy;
	}

	public int getIsExceedAreaSubsidy() {
		return isExceedAreaSubsidy;
	}

	public void setExceedAreaSubsidyRemark(String exceedAreaSubsidyRemark) {
		this.exceedAreaSubsidyRemark = exceedAreaSubsidyRemark;
	}

	public String getExceedAreaSubsidyRemark() {
		return exceedAreaSubsidyRemark;
	}

	public void setExceedAreaSubsidyAmount(BigDecimal exceedAreaSubsidyAmount) {
		this.exceedAreaSubsidyAmount = exceedAreaSubsidyAmount;
	}

	public BigDecimal getExceedAreaSubsidyAmount() {
		return exceedAreaSubsidyAmount;
	}

	public void setIsBigGoodsSubsidy(int isBigGoodsSubsidy) {
		this.isBigGoodsSubsidy = isBigGoodsSubsidy;
	}

	public int getIsBigGoodsSubsidy() {
		return isBigGoodsSubsidy;
	}

	public void setBigGoodsSubsidyRemark(String bigGoodsSubsidyRemark) {
		this.bigGoodsSubsidyRemark = bigGoodsSubsidyRemark;
	}

	public String getBigGoodsSubsidyRemark() {
		return bigGoodsSubsidyRemark;
	}

	public void setBigGoodsSubsidyAmount(BigDecimal bigGoodsSubsidyAmount) {
		this.bigGoodsSubsidyAmount = bigGoodsSubsidyAmount;
	}

	public BigDecimal getBigGoodsSubsidyAmount() {
		return bigGoodsSubsidyAmount;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setShenHePerson(int shenHePerson) {
		this.shenHePerson = shenHePerson;
	}

	public int getShenHePerson() {
		return shenHePerson;
	}

	public void setShenHeTime(String shenHeTime) {
		this.shenHeTime = shenHeTime;
	}

	public String getShenHeTime() {
		return shenHeTime;
	}

	public void setShenHeIdea(String shenHeIdea) {
		this.shenHeIdea = shenHeIdea;
	}

	public String getShenHeIdea() {
		return shenHeIdea;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}
}
