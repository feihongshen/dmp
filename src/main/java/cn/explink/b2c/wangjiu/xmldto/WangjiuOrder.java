package cn.explink.b2c.wangjiu.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 网酒网订单实体
 * 
 * @author Administrator
 *
 */
@XmlRootElement(name = "RequestOrder")
public class WangjiuOrder {

	private String secretKey;
	private String customerCode;
	private String mailNo;
	private int orderType;
	private Sender send; //
	private Receiver receiver;

	private String goodsValue;
	private String totalValue;

	private OrderItems orderItems;
	private String remark;
	private String totalServiceFee;
	private String codSplitFee;

	@XmlElement(name = "receiver")
	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	@XmlElement(name = "sender")
	public Sender getSend() {
		return send;
	}

	public void setSend(Sender send) {
		this.send = send;
	}

	@XmlElement(name = "secretKey")
	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@XmlElement(name = "customerCode")
	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	@XmlElement(name = "mailNo")
	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	@XmlElement(name = "orderType")
	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	@XmlElement(name = "goodsValue")
	public String getGoodsValue() {
		return goodsValue;
	}

	public void setGoodsValue(String goodsValue) {
		this.goodsValue = goodsValue;
	}

	@XmlElement(name = "totalValue")
	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	@XmlElement(name = "items")
	public OrderItems getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(OrderItems orderItems) {
		this.orderItems = orderItems;
	}

	@XmlElement(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlElement(name = "totalServiceFee")
	public String getTotalServiceFee() {
		return totalServiceFee;
	}

	public void setTotalServiceFee(String totalServiceFee) {
		this.totalServiceFee = totalServiceFee;
	}

	@XmlElement(name = "codSplitFee")
	public String getCodSplitFee() {
		return codSplitFee;
	}

	public void setCodSplitFee(String codSplitFee) {
		this.codSplitFee = codSplitFee;
	}

}
