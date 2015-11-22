package cn.explink.b2c.bjUnion.sign;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.b2c.bjUnion.HeaderdataResp;
@XmlRootElement(name = "transaction")
public class SignResp {
	private HeaderdataResp header;
	private BodydataSignResp body;
	@XmlElement(name = "transaction_header")
	public HeaderdataResp getHeader() {
		return header;
	}
	public void setHeader(HeaderdataResp header) {
		this.header = header;
	}
	@XmlElement(name = "transaction_body")
	public BodydataSignResp getBody() {
		return body;
	}
	public void setBody(BodydataSignResp body) {
		this.body = body;
	}
	
}
