package cn.explink.b2c.lechong.xml;

import javax.xml.bind.annotation.XmlElement;

public class Order {
	private String DoID;// 科捷物流运单号
	private String MailNO;// 快递面单号
	private String OrderType;// 物流订单类型
	private String IsCod;// 是否是COD订单，1 是；0 否
	private String PaymentType;// 支付方式，1 现金；2 POS 机；3 支票
	private String DispatchDay;// 送货时间要求，0 不限，1 工作日，2 为双休日节假日
	private String DispatchTime;// 到货时间要求：0 不限，1
								// 工作时间（早8:00-18:00），2晚间送货（18:00-21:00）
	private String DispatchNotify;// 配送前通知标记：0 无，1 通知
	private String Remark;// 备注
	private Sender sender;// 寄件方信息
	private Receiver receiver;// 收件方信息
	private String NeedFund;// 应收货款金额
	private String BackFund;// 应付货款金额
	private String Weight;// 包裹重量
	private Items items;// 包裹重量
	private String field1;
	private String field2;
	private String field3;

	@XmlElement(name = "DoID")
	public String getDoID() {
		return DoID;
	}

	public void setDoID(String doID) {
		DoID = doID;
	}

	@XmlElement(name = "MailNO")
	public String getMailNO() {
		return MailNO;
	}

	public void setMailNO(String mailNO) {
		MailNO = mailNO;
	}

	@XmlElement(name = "OrderType")
	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	@XmlElement(name = "IsCod")
	public String getIsCod() {
		return IsCod;
	}

	public void setIsCod(String isCod) {
		IsCod = isCod;
	}

	@XmlElement(name = "PaymentType")
	public String getPaymentType() {
		return PaymentType;
	}

	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}

	@XmlElement(name = "DispatchDay")
	public String getDispatchDay() {
		return DispatchDay;
	}

	public void setDispatchDay(String dispatchDay) {
		DispatchDay = dispatchDay;
	}

	@XmlElement(name = "DispatchTime")
	public String getDispatchTime() {
		return DispatchTime;
	}

	public void setDispatchTime(String dispatchTime) {
		DispatchTime = dispatchTime;
	}

	@XmlElement(name = "DispatchNotify")
	public String getDispatchNotify() {
		return DispatchNotify;
	}

	public void setDispatchNotify(String dispatchNotify) {
		DispatchNotify = dispatchNotify;
	}

	@XmlElement(name = "Remark")
	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	@XmlElement(name = "Sender")
	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	@XmlElement(name = "Receiver")
	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	@XmlElement(name = "NeedFund")
	public String getNeedFund() {
		return NeedFund;
	}

	public void setNeedFund(String needFund) {
		NeedFund = needFund;
	}

	@XmlElement(name = "BackFund")
	public String getBackFund() {
		return BackFund;
	}

	public void setBackFund(String backFund) {
		BackFund = backFund;
	}

	@XmlElement(name = "Weight")
	public String getWeight() {
		return Weight;
	}

	public void setWeight(String weight) {
		Weight = weight;
	}

	@XmlElement(name = "Items")
	public Items getItems() {
		return items;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	@XmlElement(name = "field1")
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	@XmlElement(name = "field2")
	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	@XmlElement(name = "field3")
	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}
}
