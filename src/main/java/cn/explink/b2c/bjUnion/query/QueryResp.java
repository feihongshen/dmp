package cn.explink.b2c.bjUnion.query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.b2c.bjUnion.HeaderdataResp;
@XmlRootElement(name = "transaction")
public class QueryResp {
	private HeaderdataResp header;
	private BodydataQueryResp body;
	@XmlElement(name = "transaction_header")
	public HeaderdataResp getHeader() {
		return header;
	}
	public void setHeader(HeaderdataResp header) {
		this.header = header;
	}
	@XmlElement(name = "transaction_body")
	public BodydataQueryResp getBody() {
		return body;
	}
	public void setBody(BodydataQueryResp body) {
		this.body = body;
	}
	
}
