package cn.explink.b2c.bjUnion.login;

import javax.xml.bind.annotation.XmlElement;

public class Bodydata {
	private String passwd;
	@XmlElement(name = "passwd")
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
}
