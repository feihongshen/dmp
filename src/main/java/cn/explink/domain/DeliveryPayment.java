package cn.explink.domain;

/**
 * 交款单
 * @author chunlei05.li
 * @date 2016年8月26日 下午1:57:46
 */
public class DeliveryPayment {
	/**
	 * 归班反馈-全部数据
	 */
	private DeliveryState deliveryState;
	
	/**
	 * 订单主表
	 */
	private CwbOrder cwbOrder;

	public DeliveryState getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(DeliveryState deliveryState) {
		this.deliveryState = deliveryState;
	}

	public CwbOrder getCwbOrder() {
		return cwbOrder;
	}

	public void setCwbOrder(CwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

	@Override
	public String toString() {
		return "DeliveryPayment [deliveryState=" + deliveryState + ", cwbOrder=" + cwbOrder + "]";
	}
}
