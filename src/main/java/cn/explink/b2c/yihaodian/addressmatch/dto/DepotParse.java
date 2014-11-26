package cn.explink.b2c.yihaodian.addressmatch.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 请求XML实体mapping
 * 
 * @author Administrator
 *
 */
@XmlRootElement(name = "depotParse")
public class DepotParse {
	private String userCode;
	private String requestTime;
	private String sign;

	private DepotParseDetailList depotParseDetailList;

	@XmlElement(name = "userCode")
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@XmlElement(name = "requestTime")
	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	@XmlElement(name = "sign")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@XmlElement(name = "depotParseDetailList")
	public DepotParseDetailList getDepotParseDetailList() {
		return depotParseDetailList;
	}

	public void setDepotParseDetailList(DepotParseDetailList depotParseDetailList) {
		this.depotParseDetailList = depotParseDetailList;
	}

}
