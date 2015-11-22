package cn.explink.b2c.bjUnion.query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.b2c.bjUnion.HeaderdataReq;

@XmlRootElement(name = "transaction")
public class QueryReq {
	private HeaderdataReq header;
	private BodydataQuery body;
	@XmlElement(name = "transaction_header")
	public HeaderdataReq getHeader() {
		return header;
	}
	public void setHeader(HeaderdataReq header) {
		this.header = header;
	}
	@XmlElement(name = "transaction_body")
	public BodydataQuery getBody() {
		return body;
	}
	public void setBody(BodydataQuery body) {
		this.body = body;
	}
	
}
