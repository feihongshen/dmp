package cn.explink.domain;

import java.math.BigDecimal;

public class CustomerBillContract {
	
		private long id;
		private String billBatches;//账单批次
		private long billState;//账单状态
		private long customerId;//客户名称
		private String dateRange;//日期范围
		private long correspondingCwbNum;//对应订单数
		private BigDecimal deliveryMoney; //提货费
		private BigDecimal transferMoney; //中转费
		private BigDecimal distributionMoney; //配送费
		private BigDecimal refuseMoney; //拒收派费
		private BigDecimal totalCharge; //派费合计
		private String remark; //备注
		private String dateCreateBill;//创建日期
		private String dateVerificationBill; //核销日期
		private long cwbOrderType; //订单类型
		private long dateState;
		private String cwbs;
		
		
		
		
		public String getCwbs() {
			return cwbs;
		}
		public void setCwbs(String cwbs) {
			this.cwbs = cwbs;
		}
		public long getDateState() {
			return dateState;
		}
		public void setDateState(long dateState) {
			this.dateState = dateState;
		}
		public long getCwbOrderType() {
			return cwbOrderType;
		}
		public void setCwbOrderType(long cwbOrderType) {
			this.cwbOrderType = cwbOrderType;
		}
	
		public String getDateCreateBill() {
			return dateCreateBill;
		}
		public void setDateCreateBill(String dateCreateBill) {
			this.dateCreateBill = dateCreateBill;
		}
		public String getDateVerificationBill() {
			return dateVerificationBill;
		}
		public void setDateVerificationBill(String dateVerificationBill) {
			this.dateVerificationBill = dateVerificationBill;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}

		public String getBillBatches() {
			return billBatches;
		}
		public void setBillBatches(String billBatches) {
			this.billBatches = billBatches;
		}
		public long getBillState() {
			return billState;
		}
		public void setBillState(long billState) {
			this.billState = billState;
		}

		public String getDateRange() {
			return dateRange;
		}
		public void setDateRange(String dateRange) {
			this.dateRange = dateRange;
		}

		public long getCustomerId() {
			return customerId;
		}
		public void setCustomerId(long customerId) {
			this.customerId = customerId;
		}
		public long getCorrespondingCwbNum() {
			return correspondingCwbNum;
		}
		public void setCorrespondingCwbNum(long correspondingCwbNum) {
			this.correspondingCwbNum = correspondingCwbNum;
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
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		
		
		
		
}
