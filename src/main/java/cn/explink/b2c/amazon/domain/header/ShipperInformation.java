package cn.explink.b2c.amazon.domain.header;

import javax.xml.bind.annotation.XmlElement;

public class ShipperInformation {
	private String amazonTaxID;

	@XmlElement(name = "amazonTaxID")
	public String getAmazonTaxID() {
		return amazonTaxID;
	}

	public void setAmazonTaxID(String amazonTaxID) {
		this.amazonTaxID = amazonTaxID;
	}

}
