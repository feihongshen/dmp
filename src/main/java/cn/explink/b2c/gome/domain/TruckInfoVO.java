package cn.explink.b2c.gome.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class TruckInfoVO {
	private List<TruckInfo> truckInfo;

	@XmlElement(name = "truckInfo")
	public List<TruckInfo> getTruckInfo() {
		return truckInfo;
	}

	public void setTruckInfo(List<TruckInfo> truckInfo) {
		this.truckInfo = truckInfo;
	}

}
