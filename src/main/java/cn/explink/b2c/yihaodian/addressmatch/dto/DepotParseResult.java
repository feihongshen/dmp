package cn.explink.b2c.yihaodian.addressmatch.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "depotParseResult")
public class DepotParseResult {
	private String errCode;
	private String errMsg;

	@XmlElement(name = "errMsg")
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	private DepotParseResultList depotParseResultList;

	@XmlElement(name = "errCode")
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	@XmlElement(name = "depotParseResultList")
	public DepotParseResultList getDepotParseResultList() {
		return depotParseResultList;
	}

	public void setDepotParseResultList(DepotParseResultList depotParseResultList) {
		this.depotParseResultList = depotParseResultList;
	}

}
