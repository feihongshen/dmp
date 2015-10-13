package cn.explink.b2c.sfexpress.xmldtoSearch;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * 查询顺丰状态返回的bean
 * 
 * @author Administrator
 *
 */

public class RouteResponse {

	private List<Route> routes; // 跟踪流程

	private String mailno;// 运单号
	private String orderid;// 订单号

	@XmlElement(name = "Route")
	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	@XmlElement(name = "mailno")
	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	@XmlElement(name = "orderid")
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

}
