package cn.explink.controller.pda;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserViewBodyPdaResponse extends PDAResponse {

	private List<UserView> body;

	public UserViewBodyPdaResponse() {
	}

	public UserViewBodyPdaResponse(String code, String error) {
		super(code, error);
	}

	@XmlElementWrapper(name = "body")
	@XmlElement(name = "user")
	public List<UserView> getBody() {
		return body;
	}

	public void setBody(List<UserView> body) {
		this.body = body;
	}

}
