package cn.explink.vo.delivery;

import java.util.Date;
import java.util.List;

/**
 * 妥投率查询请求
 */
public class DeliveryRateRequest {

	/**
	 * 请求提交时间
	 */
	private Date submitTime;

	/**
	 * 请求执行时间
	 */
	private Date executeTime;

	/**
	 * 查询类型
	 */
	private DeliveryRateQueryType queryType;

	/**
	 * 站点id
	 */
	private List<Long> branchIds;

	/**
	 * 供应商id
	 */
	private List<Long> customerIds;

	private Date startDate;

	private Date endDate;

	/**
	 * 妥投时间类型
	 */
	private DeliveryRateComputeType computeType;

	/**
	 * 时效
	 */
	private List<DeliveryRateTimeType> timeTypes;

	private Boolean customization;

	private String startTime;

	private String endTime;

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	public DeliveryRateQueryType getQueryType() {
		return queryType;
	}

	public void setQueryType(DeliveryRateQueryType queryType) {
		this.queryType = queryType;
	}

	public List<Long> getBranchIds() {
		return branchIds;
	}

	public void setBranchIds(List<Long> branchIds) {
		this.branchIds = branchIds;
	}

	public List<Long> getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(List<Long> vendorIds) {
		this.customerIds = vendorIds;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public void setTimeTypes(List<DeliveryRateTimeType> timeTypes) {
		this.timeTypes = timeTypes;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Boolean getCustomization() {
		return customization;
	}

	public void setCustomization(Boolean customization) {
		this.customization = customization;
	}

}
