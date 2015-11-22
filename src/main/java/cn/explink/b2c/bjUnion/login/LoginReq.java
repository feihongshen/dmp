package cn.explink.b2c.bjUnion.login;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.b2c.bjUnion.HeaderdataReq;

@XmlRootElement(name = "transaction")
public class LoginReq {
	private HeaderdataReq header;
	private Bodydata body;
	@XmlElement(name = "transaction_header")
	public HeaderdataReq getHeader() {
		return header;
	}
	public void setHeader(HeaderdataReq header) {
		this.header = header;
	}
	@XmlElement(name = "transaction_body")
	public Bodydata getBody() {
		return body;
	}
	public void setBody(Bodydata body) {
		this.body = body;
	}
	

}
