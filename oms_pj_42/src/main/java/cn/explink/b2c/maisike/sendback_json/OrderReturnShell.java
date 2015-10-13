package cn.explink.b2c.maisike.sendback_json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class OrderReturnShell {
	@JsonProperty(value = "returnorders")
	private List<OrderReturn> orders;

	public List<OrderReturn> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderReturn> orders) {
		this.orders = orders;
	}

}
