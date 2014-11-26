package cn.explink.b2c.amazon.domain.manifestDetail;

import javax.xml.bind.annotation.XmlElement;

public class StateChoice {
	private String stateProvince;// 省

	@XmlElement(name = "stateProvince")
	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

}
