package cn.explink.b2c.lechong.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Request")
public class RequestLechong {
	private String LogisticProviderID;
	private Orders orders;
	private String MD5Key;

	@XmlElement(name = "LogisticProviderID")
	public String getLogisticProviderID() {
		return LogisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		LogisticProviderID = logisticProviderID;
	}

	@XmlElement(name = "Orders")
	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	@XmlElement(name = "MD5Key")
	public String getMD5Key() {
		return MD5Key;
	}

	public void setMD5Key(String mD5Key) {
		MD5Key = mD5Key;
	}
}
