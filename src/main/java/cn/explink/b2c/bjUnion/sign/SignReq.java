package cn.explink.b2c.bjUnion.sign;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.b2c.bjUnion.HeaderdataReq;

@XmlRootElement(name = "transaction")
public class SignReq {
	private HeaderdataReq header;
	private BodydataSign body;
	@XmlElement(name = "transaction_header")
	public HeaderdataReq getHeader() {
		return header;
	}
	public void setHeader(HeaderdataReq header) {
		this.header = header;
	}
	@XmlElement(name = "transaction_body")
	public BodydataSign getBody() {
		return body;
	}
	public void setBody(BodydataSign body) {
		this.body = body;
	}
}
