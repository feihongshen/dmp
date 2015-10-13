package cn.explink.b2c.saohuobang.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BatchQueryRequest")
public class BatchQueryRequest {
	private String logisticProviderID;
	private String clientID;
	private Orders orders;

	@XmlElement(name = "logisticProviderID")
	public String getLogisticProviderID() {
		return logisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}

	@XmlElement(name = "clientID")
	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	@XmlElement(name = "orders")
	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

}
