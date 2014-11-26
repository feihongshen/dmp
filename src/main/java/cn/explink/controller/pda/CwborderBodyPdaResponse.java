package cn.explink.controller.pda;

import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.domain.CwbOrder;

@XmlRootElement
public class CwborderBodyPdaResponse extends PDAResponse {

	private CwbOrder body;

	public CwborderBodyPdaResponse() {
	}

	public CwborderBodyPdaResponse(String code, String error) {
		super(code, error);
	}

	public CwborderBodyPdaResponse(String statuscode, String errorinfo, long requestbatchno, String errorinfovediurl, CwbOrder body) {
		this.setStatuscode(statuscode);
		this.setErrorinfo(errorinfo);
		this.setRequestbatchno(requestbatchno);
		this.setWavPath(errorinfovediurl);
		this.setBody(body);
	}

	public CwborderBodyPdaResponse(String statuscode, String errorinfo, CwbOrder body) {
		this.setStatuscode(statuscode);
		this.setErrorinfo(errorinfo);
		this.setBody(body);
	}

	public CwbOrder getBody() {
		return body;
	}

	public void setBody(CwbOrder body) {
		this.body = body;
	}

}
