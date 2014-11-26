package cn.explink.b2c.amazon.domain.header;

import javax.xml.bind.annotation.XmlElement;

public class StateChoice1 {
	private String stateProvince;

	@XmlElement(name = "stateProvince")
	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

}
