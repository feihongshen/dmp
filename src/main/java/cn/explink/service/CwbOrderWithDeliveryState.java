package cn.explink.service;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.TransCwbDetail;

public class CwbOrderWithDeliveryState {
	private CwbOrder cwbOrder;
	private DeliveryState deliveryState;
	private String error;
	private TransCwbDetail transCwbDetail; //运单号主表信息
	
	public TransCwbDetail getTransCwbDetail() {
		return transCwbDetail;
	}

	public void setTransCwbDetail(TransCwbDetail transCwbDetail) {
		this.transCwbDetail = transCwbDetail;
	}

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
