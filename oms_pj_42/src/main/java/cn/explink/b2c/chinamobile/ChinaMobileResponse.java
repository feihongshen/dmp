package cn.explink.b2c.chinamobile;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "InterEB")
public class ChinaMobileResponse {

	private String returnCode;
	private String returnMsg;
	private String retdelieverId;
	private String field1;
	private String field2;
	private String field3;

	@XmlElement(name = "ReturnCode")
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	@XmlElement(name = "ReturnMsg")
	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	@XmlElement(name = "RetdelieverId")
	public String getRetdelieverId() {
		return retdelieverId;
	}

	public void setRetdelieverId(String retdelieverId) {
		this.retdelieverId = retdelieverId;
	}

	@XmlElement(name = "Field1")
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	@XmlElement(name = "Field2")
	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	@XmlElement(name = "Field3")
	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

}
