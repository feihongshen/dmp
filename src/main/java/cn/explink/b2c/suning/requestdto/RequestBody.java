package cn.explink.b2c.suning.requestdto;

import java.util.List;

public class RequestBody {
	private List<Body> packages;
	private List<Order> deliveryOrders;

	public List<Order> getDeliveryOrders() {
		return deliveryOrders;
	}

	public void setDeliveryOrders(List<Order> deliveryOrders) {
		this.deliveryOrders = deliveryOrders;
	}

	public List<Body> getPackages() {
		return packages;
	}

	public void setPackages(List<Body> packages) {
		this.packages = packages;
	}
	
}
