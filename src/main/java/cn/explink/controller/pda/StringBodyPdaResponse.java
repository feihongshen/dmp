package cn.explink.controller.pda;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringBodyPdaResponse extends PDAResponse {

	private String body;

	public StringBodyPdaResponse() {
	}

	public StringBodyPdaResponse(String code, String error) {
		super(code, error);
	}

	public StringBodyPdaResponse(String statuscode, String errorinfo, long requestbatchno, String errorinfovediurl, StringBuffer body2) {
		this.setStatuscode(statuscode);
		this.setErrorinfo(errorinfo);
		this.setRequestbatchno(requestbatchno);
		this.setWavPath(errorinfovediurl);
		this.setBody(body2.toString());
	}

	public StringBodyPdaResponse(String statuscode, String errorinfo, String body2) {
		this.setStatuscode(statuscode);
		this.setErrorinfo(errorinfo);
		this.setBody(body2.toString());
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
