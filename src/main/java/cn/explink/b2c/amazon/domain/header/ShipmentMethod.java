package cn.explink.b2c.amazon.domain.header;

import javax.xml.bind.annotation.XmlElement;

public class ShipmentMethod {
	private String amazonTechnicalName;

	@XmlElement(name = "amazonTechnicalName")
	public String getAmazonTechnicalName() {
		return amazonTechnicalName;
	}

	public void setAmazonTechnicalName(String amazonTechnicalName) {
		this.amazonTechnicalName = amazonTechnicalName;
	}

}
