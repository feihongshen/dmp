package cn.explink.pos.yeepay.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "COD-MS")
public class YeepayRequest {

	SessionHead sessionHead;

	SessionBody sessionBody;

	@XmlElement(name = "SessionHead")
	public SessionHead getSessionHead() {
		return sessionHead;
	}

	public void setSessionHead(SessionHead sessionHead) {
		this.sessionHead = sessionHead;
	}

	@XmlElement(name = "SessionBody")
	public SessionBody getSessionBody() {
		return sessionBody;
	}

	public void setSessionBody(SessionBody sessionBody) {
		this.sessionBody = sessionBody;
	}
}
