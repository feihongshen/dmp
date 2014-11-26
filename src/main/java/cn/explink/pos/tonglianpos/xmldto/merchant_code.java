package cn.explink.pos.tonglianpos.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class merchant_code {
	private exception exception;

	@XmlElement(name = "exception")
	public exception getException() {
		return exception;
	}

	public void setException(exception exception) {
		this.exception = exception;
	}
}
