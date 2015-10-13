package cn.explink.vo.delivery;

import java.util.HashMap;
import java.util.Map;

public class DeliveryRateBranchOrCustomerAggregation {

	private Integer branchOrCustomerId = null;

	private Integer customerId = 0;

	private Map<String, DeliveryRateDateAggregation> dateAggMap = new HashMap<String, DeliveryRateDateAggregation>();

	private DeliveryRateDateAggregation total = new DeliveryRateDateAggregation();

	public Integer getBranchOrCustomerId() {
		return branchOrCustomerId;
	}

	public void setBranchOrCustomerId(Integer branchOrCustomerId) {
		this.branchOrCustomerId = branchOrCustomerId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Map<String, DeliveryRateDateAggregation> getDateAggMap() {
		return dateAggMap;
	}

	public void setDateAggMap(Map<String, DeliveryRateDateAggregation> deliveryRateResultList) {
		this.dateAggMap = deliveryRateResultList;
	}

	public DeliveryRateDateAggregation getTotal() {
		return total;
	}

	public void setTotal(DeliveryRateDateAggregation total) {
		this.total = total;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeliveryRateBranchOrCustomerAggregation [");
		if (branchOrCustomerId != null)
			builder.append("branchOrCustomerId=").append(branchOrCustomerId).append(", ");
		if (customerId != null) {
			builder.append("customerId=").append(customerId).append(", ");
		}
		if (dateAggMap != null)
			builder.append("dateAggMap=").append(dateAggMap).append(", ");
		if (total != null)
			builder.append("total=").append(total);
		builder.append("]");
		return builder.toString();
	}

}
