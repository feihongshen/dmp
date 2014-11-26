package cn.explink.b2c.explink.xmldto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 成功回调返回dto
 * 
 * @author Administrator
 *
 */
@XmlRootElement(name = "IFReturn")
public class ReturnDto {

	private String errCode;
	private String errMsg;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
