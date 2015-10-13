package cn.explink.b2c.happygo.xml;

import javax.xml.bind.annotation.XmlElement;

public class Parameters {
	String wb_no;

	@XmlElement(name = "wb_no")
	public String getWb_no() {
		return wb_no;
	}

	public void setWb_no(String wb_no) {
		this.wb_no = wb_no;
	}

}
