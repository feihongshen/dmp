package cn.explink.b2c.weisuda.xml;

import javax.xml.bind.annotation.XmlElement;

public class GetUnVerifyOrders_back_Item {
	private String order_id;
	private String order_status;
	private String courier_code;
	private String consignee;
	private String opertime;
	private String reason;
	private String memo;
	private String paymethod;
	private String money;
	private String reject_map;
	private String pay_status;
	private String delay_reason;

	@XmlElement(name = "order_id")
	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	@XmlElement(name = "order_status")
	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	@XmlElement(name = "courier_code")
	public String getCourier_code() {
		return courier_code;
	}

	public void setCourier_code(String courier_code) {
		this.courier_code = courier_code;
	}

	@XmlElement(name = "consignee")
	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	@XmlElement(name = "opertime")
	public String getOpertime() {
		return opertime;
	}

	public void setOpertime(String opertime) {
		this.opertime = opertime;
	}

	@XmlElement(name = "reason")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@XmlElement(name = "memo")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@XmlElement(name = "paymethod")
	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	@XmlElement(name = "money")
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	@XmlElement(name = "reject_map")
	public String getReject_map() {
		return reject_map;
	}

	public void setReject_map(String reject_map) {
		this.reject_map = reject_map;
	}

	@XmlElement(name = "pay_status")
	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	@XmlElement(name = "delay_reason")
	public String getDelay_reason() {
		return delay_reason;
	}

	public void setDelay_reason(String delay_reason) {
		this.delay_reason = delay_reason;
	}

}
