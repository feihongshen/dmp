package cn.explink.b2c.bjUnion.revoke;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import cn.explink.b2c.bjUnion.HeaderdataReq;

@XmlRootElement(name = "transaction")
public class RevokeReq {
	private HeaderdataReq header;
	private BodydataRevoke body;
	@XmlElement(name = "transaction_header")
	public HeaderdataReq getHeader() {
		return header;
	}
	public void setHeader(HeaderdataReq header) {
		this.header = header;
	}
	@XmlElement(name = "transaction_body")
	public BodydataRevoke getBody() {
		return body;
	}
	public void setBody(BodydataRevoke body) {
		this.body = body;
	}
	
}
