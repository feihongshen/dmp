package cn.explink.b2c.huanqiugou.reqDto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="orderlist")
public class ReqDto {
	private List<Order> orderlist;
	@XmlElement(name="order")
	public List<Order> getOrderlist() {
		return orderlist;
	}
	public void setOrderlist(List<Order> orderlist) {
		this.orderlist = orderlist;
	}
	
}
