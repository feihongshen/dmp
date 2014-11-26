package cn.explink.b2c.amazon.domain.manifestDetail.shipment;

import javax.xml.bind.annotation.XmlElement;

public class ItemUnitPrice {
	private String chargeOrAllowance;
	private String monetaryAmount;

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
