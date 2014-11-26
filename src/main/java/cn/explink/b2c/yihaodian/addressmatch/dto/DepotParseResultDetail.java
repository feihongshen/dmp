package cn.explink.b2c.yihaodian.addressmatch.dto;

import javax.xml.bind.annotation.XmlElement;

public class DepotParseResultDetail {

	private String depotCode = "";
	private String depotName = "";
	private String doCode;
	private String printCode;

	@XmlElement(name = "depotCode")
	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	@XmlElement(name = "depotName")
	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	@XmlElement(name = "doCode")
	public String getDoCode() {
		return doCode;
	}

	public void setDoCode(String doCode) {
		this.doCode = doCode;
	}

	@XmlElement(name = "printCode")
	public String getPrintCode() {
		return printCode;
	}

	public void setPrintCode(String printCode) {
		this.printCode = printCode;
	}

}
