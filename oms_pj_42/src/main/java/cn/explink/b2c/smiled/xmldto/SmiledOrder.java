package cn.explink.b2c.smiled.xmldto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
public class SmiledOrder {

	private String workCode; // 运单号
	private String subCode; // 子单号
	private BigDecimal replCost; // 代收款
	private BigDecimal freight; // 运费(到付款)
	private int goodsNum; // 件数
	private String goodsSize; // 体积
	private BigDecimal goodsWeight; // 重量（计费重量）
	private String getPerson; // 收货人
	private String getPhone; // 收件人手机
	private String payType; // 支付方式
	private String settlementway; // 结算方式

	private String getAddress; // 收货地址
	private String getCity; // 收货城市
	private String cilienStorage; // / 仓库
	private String clientPerson; // 发货人
	private String clientAddress; // 发件人地址
	private String clientPhone; // 发件人手机
	private String clientCity; // 发件城市
	private String clientTel; // 发件人电话
	private String clientZipcode; // 发件人邮编

	private BigDecimal refundableAmount; // 应退金额
	private String deliveryReuir; // 配送要求
	private String remark; // 备注
	private String cilienName; // 寄件单位（客户名称）

	@XmlElement(name = "WorkCode")
	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	@XmlElement(name = "SubCode")
	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	@XmlElement(name = "ReplCost")
	public BigDecimal getReplCost() {
		return replCost;
	}

	public void setReplCost(BigDecimal replCost) {
		this.replCost = replCost;
	}

	@XmlElement(name = "Freight")
	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	@XmlElement(name = "GoodsNum")
	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	@XmlElement(name = "GoodsSize")
	public String getGoodsSize() {
		return goodsSize;
	}

	public void setGoodsSize(String goodsSize) {
		this.goodsSize = goodsSize;
	}

	@XmlElement(name = "GoodsWeight")
	public BigDecimal getGoodsWeight() {
		return goodsWeight;
	}

	public void setGoodsWeight(BigDecimal goodsWeight) {
		this.goodsWeight = goodsWeight;
	}

	@XmlElement(name = "GetPerson")
	public String getGetPerson() {
		return getPerson;
	}

	public void setGetPerson(String getPerson) {
		this.getPerson = getPerson;
	}

	@XmlElement(name = "GetPhone")
	public String getGetPhone() {
		return getPhone;
	}

	public void setGetPhone(String getPhone) {
		this.getPhone = getPhone;
	}

	@XmlElement(name = "PayType")
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@XmlElement(name = "Settlementway")
	public String getSettlementway() {
		return settlementway;
	}

	public void setSettlementway(String settlementway) {
		this.settlementway = settlementway;
	}

	@XmlElement(name = "GetAddress")
	public String getGetAddress() {
		return getAddress;
	}

	public void setGetAddress(String getAddress) {
		this.getAddress = getAddress;
	}

	@XmlElement(name = "GetCity")
	public String getGetCity() {
		return getCity;
	}

	public void setGetCity(String getCity) {
		this.getCity = getCity;
	}

	@XmlElement(name = "CilienName")
	public String getCilienName() {
		return cilienName;
	}

	public void setCilienName(String cilienName) {
		this.cilienName = cilienName;
	}

	@XmlElement(name = "CilienStorage")
	public String getCilienStorage() {
		return cilienStorage;
	}

	public void setCilienStorage(String cilienStorage) {
		this.cilienStorage = cilienStorage;
	}

	@XmlElement(name = "ClientPerson")
	public String getClientPerson() {
		return clientPerson;
	}

	public void setClientPerson(String clientPerson) {
		this.clientPerson = clientPerson;
	}

	@XmlElement(name = "ClientAddress")
	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	@XmlElement(name = "ClientPhone")
	public String getClientPhone() {
		return clientPhone;
	}

	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone;
	}

	@XmlElement(name = "ClientCity")
	public String getClientCity() {
		return clientCity;
	}

	public void setClientCity(String clientCity) {
		this.clientCity = clientCity;
	}

	@XmlElement(name = "ClientTel")
	public String getClientTel() {
		return clientTel;
	}

	public void setClientTel(String clientTel) {
		this.clientTel = clientTel;
	}

	@XmlElement(name = "ClientZipcode")
	public String getClientZipcode() {
		return clientZipcode;
	}

	public void setClientZipcode(String clientZipcode) {
		this.clientZipcode = clientZipcode;
	}

	@XmlElement(name = "RefundableAmount")
	public BigDecimal getRefundableAmount() {
		return refundableAmount;
	}

	public void setRefundableAmount(BigDecimal refundableAmount) {
		this.refundableAmount = refundableAmount;
	}

	@XmlElement(name = "DeliveryReuir")
	public String getDeliveryReuir() {
		return deliveryReuir;
	}

	public void setDeliveryReuir(String deliveryReuir) {
		this.deliveryReuir = deliveryReuir;
	}

	@XmlElement(name = "Remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
