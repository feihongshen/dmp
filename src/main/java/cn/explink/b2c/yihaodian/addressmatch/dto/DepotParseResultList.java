package cn.explink.b2c.yihaodian.addressmatch.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class DepotParseResultList {

	private List<DepotParseResultDetail> depotParseResultDetail;

	@XmlElement(name = "depotParseResultDetail")
	public List<DepotParseResultDetail> getDepotParseResultDetail() {
		return depotParseResultDetail;
	}

	public void setDepotParseResultDetail(List<DepotParseResultDetail> depotParseResultDetail) {
		this.depotParseResultDetail = depotParseResultDetail;
	}

}
