package cn.explink.b2c.liantongordercenter.xmlDto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderConfirm")
public class OrderConfirm {

	private String orderId;// 客户订单号
	private String mailNo;// 物流公司运单号
	private String dealType;// 订单操作标识：1：确认 2：取消

	private OrderConfirmOption OrderConfirm;

	@XmlAttribute(name = "orderid")
	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@XmlAttribute(name = "mailno")
	public String getMailNo() {
		return this.mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	@XmlAttribute(name = "dealtype")
	public String getDealType() {
		return this.dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	@XmlElement(name = "OrderConfirmOption")
	public OrderConfirmOption getOrderConfirm() {
		return this.OrderConfirm;
	}

	public void setOrderConfirm(OrderConfirmOption orderConfirm) {
		this.OrderConfirm = orderConfirm;
	}

}
