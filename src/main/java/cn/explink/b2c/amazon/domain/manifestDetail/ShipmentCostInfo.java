package cn.explink.b2c.amazon.domain.manifestDetail;

import javax.xml.bind.annotation.XmlElement;

public class ShipmentCostInfo {
	private String paymentMethod;// 支付方式, Non-COD/非货到付款, Cash/货到付款
	private String termsOfSale;// 销售方式DDU/FCA, 参考specification
	private AmazonFreightCost amazonFreightCost;// 运费信息(仅供参考)
	private ValueOfGoods valueOfGoods;// 货品金额信息
	private ConsigneeFreightCharge consigneeFreightCharge;// 收取收货人运费(仅供参考)
	private CashOnDeliveryCharge cashOnDeliveryCharge;// 应收金额,货到付款金额 ----重要

	@XmlElement(name = "paymentMethod")
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@XmlElement(name = "termsOfSale")
	public String getTermsOfSale() {
		return termsOfSale;
	}

	public void setTermsOfSale(String termsOfSale) {
		this.termsOfSale = termsOfSale;
	}

	@XmlElement(name = "amazonFreightCost")
	public AmazonFreightCost getAmazonFreightCost() {
		return amazonFreightCost;
	}

	public void setAmazonFreightCost(AmazonFreightCost amazonFreightCost) {
		this.amazonFreightCost = amazonFreightCost;
	}

	@XmlElement(name = "valueOfGoods")
	public ValueOfGoods getValueOfGoods() {
		return valueOfGoods;
	}

	public void setValueOfGoods(ValueOfGoods valueOfGoods) {
		this.valueOfGoods = valueOfGoods;
	}

	@XmlElement(name = "consigneeFreightCharge")
	public ConsigneeFreightCharge getConsigneeFreightCharge() {
		return consigneeFreightCharge;
	}

	public void setConsigneeFreightCharge(ConsigneeFreightCharge consigneeFreightCharge) {
		this.consigneeFreightCharge = consigneeFreightCharge;
	}

	@XmlElement(name = "CashOnDeliveryCharge")
	public CashOnDeliveryCharge getCashOnDeliveryCharge() {
		return cashOnDeliveryCharge;
	}

	public void setCashOnDeliveryCharge(CashOnDeliveryCharge cashOnDeliveryCharge) {
		this.cashOnDeliveryCharge = cashOnDeliveryCharge;
	}

}
