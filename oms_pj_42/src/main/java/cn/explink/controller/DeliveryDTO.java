package cn.explink.controller;

public class DeliveryDTO {
	private long branchid;
	private long customerid;
	private long deliverycount;// 数量
	private long month;
	private long percentage;// 百分比

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getDeliverycount() {
		return deliverycount;
	}

	public void setDeliverycount(long deliverycount) {
		this.deliverycount = deliverycount;
	}

	public long getMonth() {
		return month;
	}

	public void setMonth(long month) {
		this.month = month;
	}

	public long getPercentage() {
		return percentage;
	}

	public void setPercentage(long percentage) {
		this.percentage = percentage;
	}

}
