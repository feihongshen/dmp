package cn.explink.b2c.wangjiu.xmldto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class QueryOrders {

	private List<QueryOrder> orderlist;

	@XmlElement(name = "order")
	public List<QueryOrder> getOrderlist() {
		return orderlist;
	}

	public void setOrderlist(List<QueryOrder> orderlist) {
		this.orderlist = orderlist;
	}
}
