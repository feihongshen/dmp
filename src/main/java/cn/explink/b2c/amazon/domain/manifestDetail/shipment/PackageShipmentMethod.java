package cn.explink.b2c.amazon.domain.manifestDetail.shipment;

import javax.xml.bind.annotation.XmlElement;

public class PackageShipmentMethod {
	private String amazonTechnicalName;
	private String packageShipOption;

	@XmlElement(name = "amazonTechnicalName")
	public String getAmazonTechnicalName() {
		return amazonTechnicalName;
	}

	public void setAmazonTechnicalName(String amazonTechnicalName) {
		this.amazonTechnicalName = amazonTechnicalName;
	}

	@XmlElement(name = "packageShipOption")
	public String getPackageShipOption() {
		return packageShipOption;
	}

	public void setPackageShipOption(String packageShipOption) {
		this.packageShipOption = packageShipOption;
	}

}
