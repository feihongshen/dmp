package cn.explink.b2c.wangjiu.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 网酒网订单实体
 * 
 * @author Administrator
 *
 */
@XmlRootElement(name = "BatchQueryRequest")
public class BatchQueryRequest {

	private String mailNo;

	// private String clientID;
	//
	// private QueryOrders queryorders;
	//
	// @XmlElement(name="orders")
	// public QueryOrders getQueryorders() {
	// return queryorders;
	// }
	//
	// public void setQueryorders(QueryOrders queryorders) {
	// this.queryorders = queryorders;
	// }
	//
	// @XmlElement(name="clientID")
	// public String getClientID() {
	// return clientID;
	// }
	//
	// public void setClientID(String clientID) {
	// this.clientID = clientID;
	// }

	@XmlElement(name = "mailno")
	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

}
