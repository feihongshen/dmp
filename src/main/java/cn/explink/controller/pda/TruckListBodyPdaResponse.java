package cn.explink.controller.pda;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.domain.Truck;

@XmlRootElement
public class TruckListBodyPdaResponse extends PDAResponse {

	private List<Truck> body;

	public TruckListBodyPdaResponse() {
	}

	public TruckListBodyPdaResponse(String statuscode, String errorinfo) {
		super(statuscode, errorinfo);
	}

	@XmlElementWrapper(name = "body")
	@XmlElement(name = "truck")
	public List<Truck> getBody() {
		return body;
	}

	public void setBody(List<Truck> body) {
		this.body = body;
	}

}
