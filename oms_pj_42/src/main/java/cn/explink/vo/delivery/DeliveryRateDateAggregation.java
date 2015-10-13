package cn.explink.vo.delivery;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

public class DeliveryRateDateAggregation {

	private Date date = null;

	private Integer customerid = 0;

	public Integer getCustomerid() {
		return customerid;
	}

	public void setCustomerid(Integer customerid) {
		this.customerid = customerid;
	}

	private Integer total = 0;

	@JsonDeserialize(keyUsing = DeliveryRateTimeTypeDeSerializer.class)
	private Map<DeliveryRateTimeType, DeliveryRate> timeTypeMap = new HashMap<DeliveryRateTimeType, DeliveryRate>();

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Map<DeliveryRateTimeType, DeliveryRate> getTimeTypeMap() {
		return timeTypeMap;
	}

	public void setTimeTypeMap(Map<DeliveryRateTimeType, DeliveryRate> timeTypeMap) {
		this.timeTypeMap = timeTypeMap;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeliveryRateDateAggregation [");
		if (date != null)
			builder.append("date=").append(date).append(", ");
		if (customerid != null)
			builder.append("customerid=").append(customerid).append(", ");
		if (total != null)
			builder.append("total=").append(total).append(", ");
		if (timeTypeMap != null)
			builder.append("timeTypeMap=").append(timeTypeMap).append(", ");
		builder.append("]");
		return builder.toString();
	}

}
