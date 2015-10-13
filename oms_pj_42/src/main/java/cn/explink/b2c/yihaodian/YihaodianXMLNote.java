package cn.explink.b2c.yihaodian;

import java.math.BigDecimal;

/**
 * yihaodian XML的节点
 * 
 * @author Administrator
 *
 */
public class YihaodianXMLNote {

	private String cwb;
	private BigDecimal amount; // 总支付额
	private String payTime;
	private Integer deliverystate; // 配送状态
	private Integer payMethod = 1; // 支付方式默认为1现金
	private BigDecimal cashAmount; // 现金支付金额
	private BigDecimal cardAmount = BigDecimal.ZERO; // 刷卡支付，必须是一号店的POS机
	private BigDecimal checkAmount = BigDecimal.ZERO; // 支票

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public Integer getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(Integer deliverystate) {
		this.deliverystate = deliverystate;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	public BigDecimal getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}

	public BigDecimal getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(BigDecimal cardAmount) {
		this.cardAmount = cardAmount;
	}

	public BigDecimal getCheckAmount() {
		return checkAmount;
	}

	public void setCheckAmount(BigDecimal checkAmount) {
		this.checkAmount = checkAmount;
	}

}
