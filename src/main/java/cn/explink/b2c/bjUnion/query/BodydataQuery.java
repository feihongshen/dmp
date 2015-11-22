package cn.explink.b2c.bjUnion.query;

import javax.xml.bind.annotation.XmlElement;

public class BodydataQuery {
	private String order_no;
	
	@XmlElement(name = "order_no")
	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	
}
