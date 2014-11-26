package cn.explink.controller.pda;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.domain.Reason;

@XmlRootElement
public class ReasonListBodyPdaResponse extends PDAResponse {

	List<Reason> body;

	public ReasonListBodyPdaResponse() {
	}

	public ReasonListBodyPdaResponse(String statuscode, String errorinfo, long requestbatchno, String errorinfovediurl, List<Reason> rlist) {
		super(statuscode, errorinfo, requestbatchno, errorinfovediurl);
		this.setBody(rlist);
	}

	@XmlElementWrapper(name = "body")
	@XmlElement(name = "reason")
	public List<Reason> getBody() {
		return body;
	}

	public void setBody(List<Reason> body) {
		this.body = body;
	}

}
