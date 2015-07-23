package cn.explink.domain.VO;

import java.math.BigDecimal;


public class SerachCustomerBillContractVO {
	private String cwb;
	private String cwbOrderType;
	private String cwbstate;
	private String paywayid;
	private BigDecimal deliveryMoney; //提货费
	private BigDecimal distributionMoney; //配送费
	private BigDecimal transferMoney; //中转费
	private BigDecimal refuseMoney; //拒收派费
	private BigDecimal totalCharge; //派费合计
	private String billBatches;
	private String zhongLei;
	
	public String getZhongLei() {
		return zhongLei;
	}
	public void setZhongLei(String zhongLei) {
		this.zhongLei = zhongLei;
	}
	public String getBillBatches() {
		return billBatches;
	}
	public void setBillBatches(String billBatches) {
		this.billBatches = billBatches;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public String getCwbOrderType() {
		return cwbOrderType;
	}
	public void setCwbOrderType(String cwbOrderType) {
		this.cwbOrderType = cwbOrderType;
	}
	public String getCwbstate() {
		return cwbstate;
	}
	public void setCwbstate(String cwbstate) {
		this.cwbstate = cwbstate;
	}
	public String getPaywayid() {
		return paywayid;
	}
	public void setPaywayid(String paywayid) {
		this.paywayid = paywayid;
	}
	public BigDecimal getDeliveryMoney() {
		return deliveryMoney;
	}
	public void setDeliveryMoney(BigDecimal deliveryMoney) {
		this.deliveryMoney = deliveryMoney;
	}
	public BigDecimal getDistributionMoney() {
		return distributionMoney;
	}
	public void setDistributionMoney(BigDecimal distributionMoney) {
		this.distributionMoney = distributionMoney;
	}
	public BigDecimal getTransferMoney() {
		return transferMoney;
	}
	public void setTransferMoney(BigDecimal transferMoney) {
		this.transferMoney = transferMoney;
	}
	public BigDecimal getRefuseMoney() {
		return refuseMoney;
	}
	public void setRefuseMoney(BigDecimal refuseMoney) {
		this.refuseMoney = refuseMoney;
	}
	public BigDecimal getTotalCharge() {
		return totalCharge;
	}
	public void setTotalCharge(BigDecimal totalCharge) {
		this.totalCharge = totalCharge;
	}
	
	
	
}		