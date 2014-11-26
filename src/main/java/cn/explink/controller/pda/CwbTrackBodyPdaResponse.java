package cn.explink.controller.pda;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CwbTrackBodyPdaResponse extends PDAResponse {

	private CwbTrackBody body;

	private List<String> orderFlows;

	public CwbTrackBodyPdaResponse() {
	}

	public CwbTrackBodyPdaResponse(String code, String error) {
		super(code, error);
	}

	public CwbTrackBodyPdaResponse(String statuscode, String errorinfo, long requestbatchno, String errorinfovediurl, CwbTrackBody body) {
		this.setStatuscode(statuscode);
		this.setErrorinfo(errorinfo);
		this.setRequestbatchno(requestbatchno);
		this.setWavPath(errorinfovediurl);
		this.setBody(body);
	}

	public CwbTrackBodyPdaResponse(String statuscode, String errorinfo, CwbTrackBody body) {
		this.setStatuscode(statuscode);
		this.setErrorinfo(errorinfo);
		this.setBody(body);
	}

	public CwbTrackBody getBody() {
		return body;
	}

	public void setBody(CwbTrackBody body) {
		this.body = body;
	}

	@XmlElementWrapper(name = "orderFlows")
	@XmlElement(name = "orderFlow")
	public List<String> getOrderFlows() {
		return orderFlows;
	}

	public void setOrderFlows(List<String> orderFlows) {
		this.orderFlows = orderFlows;
	}

}
