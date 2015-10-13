package cn.explink.b2c.maisike.senddata_json;

import java.util.List;

/**
 * 出库给迈思可订单数据列表
 * 
 * @author Administrator
 *
 */
public class OrderArrival {

	private List<Order> orders;

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

}
