package cn.explink.b2c.yihaodian.xmldto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderDeliveryResult")
public class OrderDeliveryResultDto {

	private static final long serialVersionUID = -7267820637752661573L;

	/** 用户代码 */
	private String userCode;

	/** 请求时间 */
	private String requestTime;

	/** 签名（md5） */
	private String sign;

	/** 运单号 */
	private String shipmentCode;

	/** 总支付额(单位:元) */
	private BigDecimal amount;

	/** 支付日期 */
	private String payTime;

	/** 支付方式 */
	private Integer payMethod;

	/** 配送状态 */
	private Integer deliveryState;

	/** 现金金额(单位:元) */
	private BigDecimal cashAmount;

	/** 刷卡金额(单位:元) */
	private BigDecimal cardAmount;

	/** 支票金额(单位:元) */
	private BigDecimal checkAmount;

	/** 预留金额A */
	private BigDecimal amountA;

	/** 预留金额B */
	private BigDecimal amountB;

	/** 预留金额C */
	private BigDecimal amountC;

	/** POS机号 */
	private String posTerminalCode;

	/** POS交易流水号 */
	private String posSerialCode;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getShipmentCode() {
		return shipmentCode;
	}

	public void setShipmentCode(String shipmentCode) {
		this.shipmentCode = shipmentCode;
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

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	public Integer getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(Integer deliveryState) {
		this.deliveryState = deliveryState;
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

	public BigDecimal getAmountA() {
		return amountA;
	}

	public void setAmountA(BigDecimal amountA) {
		this.amountA = amountA;
	}

	public BigDecimal getAmountB() {
		return amountB;
	}

	public void setAmountB(BigDecimal amountB) {
		this.amountB = amountB;
	}

	public BigDecimal getAmountC() {
		return amountC;
	}

	public void setAmountC(BigDecimal amountC) {
		this.amountC = amountC;
	}

	public String getPosTerminalCode() {
		return posTerminalCode;
	}

	public void setPosTerminalCode(String posTerminalCode) {
		this.posTerminalCode = posTerminalCode;
	}

	public String getPosSerialCode() {
		return posSerialCode;
	}

	public void setPosSerialCode(String posSerialCode) {
		this.posSerialCode = posSerialCode;
	}

}
