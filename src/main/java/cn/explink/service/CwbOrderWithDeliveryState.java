package cn.explink.service;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;

public class CwbOrderWithDeliveryState {
	private CwbOrder cwbOrder;
	private DeliveryState deliveryState;
	private String error;

	public CwbOrder getCwbOrder() {
		return cwbOrder;
	}

	public void setCwbOrder(CwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

	public DeliveryState getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(DeliveryState deliveryState) {
		this.deliveryState = deliveryState;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
