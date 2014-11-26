package cn.explink.controller.pda;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BatchDeliverBodyPdaResponse extends PDAResponse {

	private BatchDeliverBody body;

	public BatchDeliverBodyPdaResponse() {
	}

	public BatchDeliverBodyPdaResponse(String code, String error) {
		super(code, error);
	}

	public BatchDeliverBody getBody() {
		return body;
	}

	public void setBody(BatchDeliverBody body) {
		this.body = body;
	}
}
