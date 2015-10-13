package cn.explink.jms.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CwbOrderWithDeliveryState {
	private DmpCwbOrder cwbOrder;
	private DmpDeliveryState deliveryState;

	public DmpCwbOrder getCwbOrder() {
		return cwbOrder;
	}

	public void setCwbOrder(DmpCwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

	public DmpDeliveryState getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(DmpDeliveryState deliveryState) {
		this.deliveryState = deliveryState;
	}
}
