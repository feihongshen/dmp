package cn.explink.vo.delivery;

import java.util.Date;
import java.util.List;

public class DeliveryRateResult {

	// 站点或供应商
	private int branchOrCustomer;
	// 同时有站点和供应商中的供商商
	private int customer = 0;
	// 日期
	private Date date;

	private Integer total;

	private List<Integer> deliveryCount;

	public int getBranchOrCustomer() {
		return branchOrCustomer;
	}

	public void setBranchOrCustomer(int branchOrCustomer) {
		this.branchOrCustomer = branchOrCustomer;
	}

	public int getCustomer() {
		return customer;
	}

	public void setCustomer(int customer) {
		this.customer = customer;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<Integer> getDeliveryCount() {
		return deliveryCount;
	}

	public void setDeliveryCount(List<Integer> deliveryCount) {
		this.deliveryCount = deliveryCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeliveryRateResult [branchOrCustomer=").append(branchOrCustomer).append(", customer=").append(customer).append(", date=").append(date).append(", total=").append(total)
				.append(", deliveryCount=").append(deliveryCount).append("]");
		return builder.toString();
	}

}
