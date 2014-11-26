package cn.explink.b2c.liantong.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class UnicomRequest {
	@JsonProperty(value = "OrderNo")
	private String orderNo;
	@JsonProperty(value = "ApplyType")
	private String applyType;
	@JsonProperty(value = "MailNo")
	private String mailNo;
	@JsonProperty(value = "Sender")
	private Sender sender; // 发件人节点
	@JsonProperty(value = "Receiver")
	private Receiver receiver;// 收件人节点
	@JsonProperty(value = "SendStartTime")
	private String sendStartTime;
	@JsonProperty(value = "SendEndTime")
	private String sendEndTime;
	@JsonProperty(value = "Goods")
	private List<Goods> goods; // 商品名称
	@JsonProperty(value = "OrderAmount")
	private String orderAmount;

	private String custid; // 结算标示

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public String getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public String getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	public List<Goods> getGoods() {
		return goods;
	}

	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

}
