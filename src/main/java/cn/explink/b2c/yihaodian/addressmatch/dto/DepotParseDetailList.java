package cn.explink.b2c.yihaodian.addressmatch.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class DepotParseDetailList {

	private List<DepotParseDetail> depotParseDetailList;

	@XmlElement(name = "depotParseDetail")
	public List<DepotParseDetail> getDepotParseDetailList() {
		return depotParseDetailList;
	}

	public void setDepotParseDetailList(List<DepotParseDetail> depotParseDetailList) {
		this.depotParseDetailList = depotParseDetailList;
	}
}
