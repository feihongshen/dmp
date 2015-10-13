package cn.explink.b2c.weisuda.xml;

import javax.xml.bind.annotation.XmlElement;

public class PushOrders_Item {
	private String courier_code;
	private String order_id;
	private String bound_time;

	@XmlElement(name = "courier_code")
	public String getCourier_code() {
		return courier_code;
	}

	public void setCourier_code(String courier_code) {
		this.courier_code = courier_code;
	}

	@XmlElement(name = "order_id")
	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	@XmlElement(name = "bound_time")
	public String getBound_time() {
		return bound_time;
	}

	public void setBound_time(String bound_time) {
		this.bound_time = bound_time;
	}

}
