package cn.explink.b2c.maikaolin.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request_body")
public class Request_Body {

	private List<Package> listPackage = null;

	@XmlElement(name = "package")
	public List<Package> getListPackage() {
		return listPackage;
	}

	public void setListPackage(List<Package> listPackage) {
		this.listPackage = listPackage;
	}

}
