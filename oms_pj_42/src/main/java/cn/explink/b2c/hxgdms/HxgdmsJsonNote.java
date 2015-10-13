package cn.explink.b2c.hxgdms;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author Administrator
 *
 */
public class HxgdmsJsonNote {
	@JsonProperty(value = "WorkCode")
	private String workCode; // 订单号cwb
	@JsonProperty(value = "WorkState")
	private int workState; // 订单状态 0 ,1 ,2
	@JsonProperty(value = "WorkStateTime")
	private String workStateTime; // 状态产生时间
	@JsonProperty(value = "WorkDesc")
	private String workDesc; // 订单状态备注
	@JsonProperty(value = "DelveryCode")
	private String delveryCode; // 宅配承运商公司编码
	@JsonProperty(value = "DelveryName")
	private String delveryName; // 配送员名称
	@JsonProperty(value = "DelveryMobile")
	private String delveryMobile; // 配送员联系电话
	@JsonProperty(value = "DeleServerMobile")
	private String deleServerMobile; // 宅配承运商客服电话
	@JsonProperty(value = "PayType")
	private int payType; // 支付方式 签收时传递
	@JsonProperty(value = "CashMoney")
	private BigDecimal cashMoney; // 现金
	@JsonProperty(value = "PosMoney")
	private BigDecimal posMoney; // POS

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public int getWorkState() {
		return workState;
	}

	public void setWorkState(int workState) {
		this.workState = workState;
	}

	public String getWorkStateTime() {
		return workStateTime;
	}

	public void setWorkStateTime(String workStateTime) {
		this.workStateTime = workStateTime;
	}

	public String getWorkDesc() {
		return workDesc;
	}

	public void setWorkDesc(String workDesc) {
		this.workDesc = workDesc;
	}

	public String getDelveryCode() {
		return delveryCode;
	}

	public void setDelveryCode(String delveryCode) {
		this.delveryCode = delveryCode;
	}

	public String getDelveryName() {
		return delveryName;
	}

	public void setDelveryName(String delveryName) {
		this.delveryName = delveryName;
	}

	public String getDelveryMobile() {
		return delveryMobile;
	}

	public void setDelveryMobile(String delveryMobile) {
		this.delveryMobile = delveryMobile;
	}

	public String getDeleServerMobile() {
		return deleServerMobile;
	}

	public void setDeleServerMobile(String deleServerMobile) {
		this.deleServerMobile = deleServerMobile;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public BigDecimal getCashMoney() {
		return cashMoney;
	}

	public void setCashMoney(BigDecimal cashMoney) {
		this.cashMoney = cashMoney;
	}

	public BigDecimal getPosMoney() {
		return posMoney;
	}

	public void setPosMoney(BigDecimal posMoney) {
		this.posMoney = posMoney;
	}

}
