package cn.explink.controller.pda;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringBodyXMLPdaResponse extends PDAResponse {

	private PDAXMLBody body;

	public StringBodyXMLPdaResponse() {
	}

	public StringBodyXMLPdaResponse(String code, String error) {
		super(code, error);
	}

	public StringBodyXMLPdaResponse(String statuscode, String errorinfo, long requestbatchno, String errorinfovediurl, PDAXMLBody body2) {
		this.setStatuscode(statuscode);
		this.setErrorinfo(errorinfo);
		this.setRequestbatchno(requestbatchno);
		this.setWavPath(errorinfovediurl);
		this.setBody(body2);
	}

	public StringBodyXMLPdaResponse(String statuscode, String errorinfo, PDAXMLBody body2) {
		this.setStatuscode(statuscode);
		this.setErrorinfo(errorinfo);
		this.setBody(body2);
	}

	public PDAXMLBody getBody() {
		return body;
	}

	public void setBody(PDAXMLBody body) {
		this.body = body;
	}

}
