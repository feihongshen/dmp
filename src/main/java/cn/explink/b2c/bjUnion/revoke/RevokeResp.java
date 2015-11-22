package cn.explink.b2c.bjUnion.revoke;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.b2c.bjUnion.HeaderdataResp;
@XmlRootElement(name = "transaction")
public class RevokeResp {
	private HeaderdataResp header;
	private BodydataRevokeResp body;
	@XmlElement(name = "transaction_header")
	public HeaderdataResp getHeader() {
		return header;
	}
	public void setHeader(HeaderdataResp header) {
		this.header = header;
	}
	@XmlElement(name = "transaction_body")
	public BodydataRevokeResp getBody() {
		return body;
	}
	public void setBody(BodydataRevokeResp body) {
		this.body = body;
	}
	
}
