package cn.explink.b2c.bjUnion.login;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import cn.explink.b2c.bjUnion.HeaderdataResp;

@XmlRootElement(name = "transaction")
public class LoginResp {
	private HeaderdataResp header;
	private BodydataResp body;
	@XmlElement(name = "transaction_header")
	public HeaderdataResp getHeader() {
		return header;
	}
	public void setHeader(HeaderdataResp header) {
		this.header = header;
	}
	@XmlElement(name = "transaction_body")
	public BodydataResp getBody() {
		return body;
	}
	public void setBody(BodydataResp body) {
		this.body = body;
	}
	
}
