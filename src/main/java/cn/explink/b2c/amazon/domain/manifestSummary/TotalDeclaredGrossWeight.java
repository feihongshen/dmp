package cn.explink.b2c.amazon.domain.manifestSummary;

import javax.xml.bind.annotation.XmlElement;

public class TotalDeclaredGrossWeight {
	private String weightValue;

	@XmlElement(name = "weightValue")
	public String getWeightValue() {
		return weightValue;
	}

	public void setWeightValue(String weightValue) {
		this.weightValue = weightValue;
	}

}
