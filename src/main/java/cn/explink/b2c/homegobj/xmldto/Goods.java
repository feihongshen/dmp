package cn.explink.b2c.homegobj.xmldto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Goods {

	private List<Good> good;

	@XmlElement(name = "good")
	public List<Good> getGood() {
		return good;
	}

	public void setGood(List<Good> good) {
		this.good = good;
	}

}
