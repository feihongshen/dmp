package cn.explink.b2c.amazon.domain.manifestDetail;

import javax.xml.bind.annotation.XmlElement;

public class AmazonFreightCost {
	private String chargeOrAllowance;// 支付方式
	private String monetaryAmount;// 金额

	@XmlElement(name = "chargeOrAllowance")
	public String getChargeOrAllowance() {
		return chargeOrAllowance;
	}

	public void setChargeOrAllowance(String chargeOrAllowance) {
		this.chargeOrAllowance = chargeOrAllowance;
	}

	@XmlElement(name = "monetaryAmount")
	public String getMonetaryAmount() {
		return monetaryAmount;
	}

	public void setMonetaryAmount(String monetaryAmount) {
		this.monetaryAmount = monetaryAmount;
	}

}
