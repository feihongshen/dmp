package cn.explink.controller.pda;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginResponse extends PDAResponse {

	private LoginResponseBody body;

	public LoginResponse() {
	}

	public LoginResponse(String code, String error) {
		super(code, error);
	}

	public LoginResponseBody getBody() {
		return body;
	}

	public void setBody(LoginResponseBody body) {
		this.body = body;
	}
}
