package cn.explink.controller.pda;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CwborderViewBodyPdaResponse extends PDAResponse {

	private PdaCwbOrderView body;

	public CwborderViewBodyPdaResponse() {
	}

	public CwborderViewBodyPdaResponse(String code, String error) {
		super(code, error);
	}

	public CwborderViewBodyPdaResponse(String statuscode, String errorinfo, long requestbatchno, String errorinfovediurl, PdaCwbOrderView body) {
		this.setStatuscode(statuscode);
		this.setErrorinfo(errorinfo);
		this.setRequestbatchno(requestbatchno);
		this.setWavPath(errorinfovediurl);
		this.setBody(body);
	}

	public CwborderViewBodyPdaResponse(String statuscode, String errorinfo, PdaCwbOrderView body) {
		this.setStatuscode(statuscode);
		this.setErrorinfo(errorinfo);
		this.setBody(body);
	}

	public PdaCwbOrderView getBody() {
		return body;
	}

	public void setBody(PdaCwbOrderView body) {
		this.body = body;
	}

}
