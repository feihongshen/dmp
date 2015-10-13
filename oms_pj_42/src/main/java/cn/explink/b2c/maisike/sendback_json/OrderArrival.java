package cn.explink.b2c.maisike.sendback_json;

import java.util.List;

/**
 * 出库给迈思可订单数据列表
 * 
 * @author Administrator
 *
 */
public class OrderArrival {

	private List<OrderReturn> orders;

	public List<OrderReturn> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderReturn> orders) {
		this.orders = orders;
	}

}
