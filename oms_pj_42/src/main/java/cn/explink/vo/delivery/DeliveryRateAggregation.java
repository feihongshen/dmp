package cn.explink.vo.delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryRateAggregation {

	private DeliveryRateQueryType queryType;

	private DeliveryRateComputeType computeType;

	private List<DeliveryRateTimeType> timeTypes;

	private List<String> allDate;

	private Map<Integer, Map<Integer, DeliveryRateBranchOrCustomerAggregation>> branchOrCustomerAggMap = new HashMap<Integer, Map<Integer, DeliveryRateBranchOrCustomerAggregation>>();

	private Map<Integer, DeliveryRateBranchOrCustomerAggregation> total = new HashMap<Integer, DeliveryRateBranchOrCustomerAggregation>();

	public Map<Integer, DeliveryRateBranchOrCustomerAggregation> getTotal() {
		return total;
	}

	public void setTotal(Map<Integer, DeliveryRateBranchOrCustomerAggregation> total) {
		this.total = total;
	}

	private Boolean customization;

	public DeliveryRateQueryType getQueryType() {
		return queryType;
	}

	public void setQueryType(DeliveryRateQueryType queryType) {
		this.queryType = queryType;
	}

	public Map<Integer, Map<Integer, DeliveryRateBranchOrCustomerAggregation>> getBranchOrCustomerAggMap() {
		return branchOrCustomerAggMap;
	}

	public void setBranchOrCustomerAggMap(Map<Integer, Map<Integer, DeliveryRateBranchOrCustomerAggregation>> branchOrCustomerAggMap) {
		this.branchOrCustomerAggMap = branchOrCustomerAggMap;
	}

	public List<String> getAllDate() {
		return allDate;
	}

	public void setAllDate(List<String> allDate) {
		this.allDate = allDate;
	}

	public DeliveryRateComputeType getComputeType() {
		return computeType;
	}

	public void setComputeType(DeliveryRateComputeType computeType) {
		this.computeType = computeType;
	}

	public List<DeliveryRateTimeType> getTimeTypes() {
		return timeTypes;
	}

	public void setTimeTypes(List<DeliveryRateTimeType> timeType) {
		this.timeTypes = timeType;
	}

	public Boolean getCustomization() {
		return customization;
	}

	public void setCustomization(Boolean customization) {
		this.customization = customization;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeliveryRateAggregation [");
		if (queryType != null)
			builder.append("queryType=").append(queryType).append(", ");
		if (computeType != null)
			builder.append("computeType=").append(computeType).append(", ");
		if (timeTypes != null)
			builder.append("timeTypes=").append(timeTypes).append(", ");
		if (allDate != null)
			builder.append("allDate=").append(allDate).append(", ");
		if (branchOrCustomerAggMap != null)
			builder.append("branchOrCustomerAggMap=").append(branchOrCustomerAggMap).append(", ");
		if (total != null)
			builder.append("total=").append(total).append(", ");
		if (customization != null)
			builder.append("customization=").append(customization);
		builder.append("]");
		return builder.toString();
	}

}
