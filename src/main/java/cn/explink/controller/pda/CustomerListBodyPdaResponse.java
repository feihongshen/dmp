package cn.explink.controller.pda;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.domain.Customer;

@XmlRootElement
public class CustomerListBodyPdaResponse extends PDAResponse {

	private List<Customer> body;

	public CustomerListBodyPdaResponse() {
	}

	public CustomerListBodyPdaResponse(String code, String error) {
		super(code, error);
	}

	@XmlElementWrapper(name = "body")
	@XmlElement(name = "customer")
	public List<Customer> getBody() {
		return body;
	}

	public void setBody(List<Customer> body) {
		this.body = body;
	}

}
