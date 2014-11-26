package cn.explink.b2c.amazon.domain.manifestDetail.shipment;

import javax.xml.bind.annotation.XmlElement;

public class ShipmentPackageDimensions {
	private String lengthValue;// 长
	private String heightValue;// 高
	private String widthValue;// 宽

	@XmlElement(name = "lengthValue")
	public String getLengthValue() {
		return lengthValue + "cm";
	}

	public void setLengthValue(String lengthValue) {
		this.lengthValue = lengthValue;
	}

	@XmlElement(name = "heightValue")
	public String getHeightValue() {
		return heightValue + "cm";
	}

	public void setHeightValue(String heightValue) {
		this.heightValue = heightValue;
	}

	@XmlElement(name = "widthValue")
	public String getWidthValue() {
		return widthValue + "cm";
	}

	public void setWidthValue(String widthValue) {
		this.widthValue = widthValue;
	}

}
