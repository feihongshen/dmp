package cn.explink.b2c.liantong.json;

import java.util.List;

/**
 * 返回 联通实体类
 * 
 * @author Administrator
 *
 */
public class LiantongResponse {

	private List<Order> orders;
	private String error; // 异常原因

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

}
