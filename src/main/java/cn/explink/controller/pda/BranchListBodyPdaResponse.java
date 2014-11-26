package cn.explink.controller.pda;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.domain.Branch;

@XmlRootElement
public class BranchListBodyPdaResponse extends PDAResponse {

	private List<Branch> body;

	public BranchListBodyPdaResponse() {
	}

	public BranchListBodyPdaResponse(String code, String error) {
		super(code, error);
	}

	@XmlElementWrapper(name = "body")
	@XmlElement(name = "branch")
	public List<Branch> getBody() {
		return body;
	}

	public void setBody(List<Branch> body) {
		this.body = body;
	}

}
