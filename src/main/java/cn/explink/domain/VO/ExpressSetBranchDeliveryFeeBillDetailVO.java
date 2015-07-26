package cn.explink.domain.VO;

import java.math.*;

/**
 * Fri Jul 24 14:03:00 CST 2015 LiChongChong
 */

public class ExpressSetBranchDeliveryFeeBillDetailVO {
	private int id;
	private int billId;
	private String cwb;
	private int flowordertype;
	private String cwbordertypeid;
	private int customerid;
	private int isReceived;
	private BigDecimal receivablefee;
	private String emaildate;
	private String newpaywayid;
	private String podtime;
	private BigDecimal sumFee;
	private BigDecimal basicFee;
	private BigDecimal collectionSubsidyFee;
	private BigDecimal areaSubsidyFee;
	private BigDecimal exceedSubsidyFee;
	private BigDecimal businessSubsidyFee;
	private BigDecimal attachSubsidyFee;
	private BigDecimal deliverySumFee;
	private BigDecimal deliveryBasicFee;
	private BigDecimal deliveryCollectionSubsidyFee;
	private BigDecimal deliveryAreaSubsidyFee;
	private BigDecimal deliveryExceedSubsidyFee;
	private BigDecimal deliveryBusinessSubsidyFee;
	private BigDecimal deliveryAttachSubsidyFee;
	private BigDecimal pickupSumFee;
	private BigDecimal pickupCollectionSubsidyFee;
	private BigDecimal pickupAreaSubsidyFee;
	private BigDecimal pickupExceedSubsidyFee;
	private BigDecimal pickupAttachSubsidyFee;
	private BigDecimal pickupBasicFee;
	private BigDecimal pickupBusinessSubsidyFee;
	private int cwbOrderCount;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public int getBillId() {
		return billId;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getCwb() {
		return cwb;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	public int getFlowordertype() {
		return flowordertype;
	}

	public void setCwbordertypeid(String cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	public int getCustomerid() {
		return customerid;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setNewpaywayid(String newpaywayid) {
		this.newpaywayid = newpaywayid;
	}

	public String getNewpaywayid() {
		return newpaywayid;
	}

	public void setPodtime(String podtime) {
		this.podtime = podtime;
	}

	public String getPodtime() {
		return podtime;
	}

	public void setSumFee(BigDecimal sumFee) {
		this.sumFee = sumFee;
	}

	public BigDecimal getSumFee() {
		return sumFee;
	}

	public void setBasicFee(BigDecimal basicFee) {
		this.basicFee = basicFee;
	}

	public BigDecimal getBasicFee() {
		return basicFee;
	}

	public void setCollectionSubsidyFee(BigDecimal collectionSubsidyFee) {
		this.collectionSubsidyFee = collectionSubsidyFee;
	}

	public BigDecimal getCollectionSubsidyFee() {
		return collectionSubsidyFee;
	}

	public void setAreaSubsidyFee(BigDecimal areaSubsidyFee) {
		this.areaSubsidyFee = areaSubsidyFee;
	}

	public BigDecimal getAreaSubsidyFee() {
		return areaSubsidyFee;
	}

	public void setExceedSubsidyFee(BigDecimal exceedSubsidyFee) {
		this.exceedSubsidyFee = exceedSubsidyFee;
	}

	public BigDecimal getExceedSubsidyFee() {
		return exceedSubsidyFee;
	}

	public void setBusinessSubsidyFee(BigDecimal businessSubsidyFee) {
		this.businessSubsidyFee = businessSubsidyFee;
	}

	public BigDecimal getBusinessSubsidyFee() {
		return businessSubsidyFee;
	}

	public void setAttachSubsidyFee(BigDecimal attachSubsidyFee) {
		this.attachSubsidyFee = attachSubsidyFee;
	}

	public BigDecimal getAttachSubsidyFee() {
		return attachSubsidyFee;
	}

	public void setDeliverySumFee(BigDecimal deliverySumFee) {
		this.deliverySumFee = deliverySumFee;
	}

	public BigDecimal getDeliverySumFee() {
		return deliverySumFee;
	}

	public void setDeliveryBasicFee(BigDecimal deliveryBasicFee) {
		this.deliveryBasicFee = deliveryBasicFee;
	}

	public BigDecimal getDeliveryBasicFee() {
		return deliveryBasicFee;
	}

	public void setDeliveryCollectionSubsidyFee(
			BigDecimal deliveryCollectionSubsidyFee) {
		this.deliveryCollectionSubsidyFee = deliveryCollectionSubsidyFee;
	}

	public BigDecimal getDeliveryCollectionSubsidyFee() {
		return deliveryCollectionSubsidyFee;
	}

	public void setDeliveryAreaSubsidyFee(BigDecimal deliveryAreaSubsidyFee) {
		this.deliveryAreaSubsidyFee = deliveryAreaSubsidyFee;
	}

	public BigDecimal getDeliveryAreaSubsidyFee() {
		return deliveryAreaSubsidyFee;
	}

	public void setDeliveryExceedSubsidyFee(BigDecimal deliveryExceedSubsidyFee) {
		this.deliveryExceedSubsidyFee = deliveryExceedSubsidyFee;
	}

	public BigDecimal getDeliveryExceedSubsidyFee() {
		return deliveryExceedSubsidyFee;
	}

	public void setDeliveryBusinessSubsidyFee(
			BigDecimal deliveryBusinessSubsidyFee) {
		this.deliveryBusinessSubsidyFee = deliveryBusinessSubsidyFee;
	}

	public BigDecimal getDeliveryBusinessSubsidyFee() {
		return deliveryBusinessSubsidyFee;
	}

	public void setDeliveryAttachSubsidyFee(BigDecimal deliveryAttachSubsidyFee) {
		this.deliveryAttachSubsidyFee = deliveryAttachSubsidyFee;
	}

	public BigDecimal getDeliveryAttachSubsidyFee() {
		return deliveryAttachSubsidyFee;
	}

	public void setPickupSumFee(BigDecimal pickupSumFee) {
		this.pickupSumFee = pickupSumFee;
	}

	public BigDecimal getPickupSumFee() {
		return pickupSumFee;
	}

	public void setPickupCollectionSubsidyFee(
			BigDecimal pickupCollectionSubsidyFee) {
		this.pickupCollectionSubsidyFee = pickupCollectionSubsidyFee;
	}

	public BigDecimal getPickupCollectionSubsidyFee() {
		return pickupCollectionSubsidyFee;
	}

	public void setPickupAreaSubsidyFee(BigDecimal pickupAreaSubsidyFee) {
		this.pickupAreaSubsidyFee = pickupAreaSubsidyFee;
	}

	public BigDecimal getPickupAreaSubsidyFee() {
		return pickupAreaSubsidyFee;
	}

	public void setPickupExceedSubsidyFee(BigDecimal pickupExceedSubsidyFee) {
		this.pickupExceedSubsidyFee = pickupExceedSubsidyFee;
	}

	public BigDecimal getPickupExceedSubsidyFee() {
		return pickupExceedSubsidyFee;
	}

	public void setPickupAttachSubsidyFee(BigDecimal pickupAttachSubsidyFee) {
		this.pickupAttachSubsidyFee = pickupAttachSubsidyFee;
	}

	public BigDecimal getPickupAttachSubsidyFee() {
		return pickupAttachSubsidyFee;
	}

	public BigDecimal getPickupBasicFee() {
		return pickupBasicFee;
	}

	public void setPickupBasicFee(BigDecimal pickupBasicFee) {
		this.pickupBasicFee = pickupBasicFee;
	}

	public BigDecimal getPickupBusinessSubsidyFee() {
		return pickupBusinessSubsidyFee;
	}

	public void setPickupBusinessSubsidyFee(BigDecimal pickupBusinessSubsidyFee) {
		this.pickupBusinessSubsidyFee = pickupBusinessSubsidyFee;
	}

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public int getIsReceived() {
		return isReceived;
	}

	public void setIsReceived(int isReceived) {
		this.isReceived = isReceived;
	}

	public int getCwbOrderCount() {
		return cwbOrderCount;
	}

	public void setCwbOrderCount(int cwbOrderCount) {
		this.cwbOrderCount = cwbOrderCount;
	}

}
