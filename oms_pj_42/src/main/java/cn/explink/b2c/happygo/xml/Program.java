package cn.explink.b2c.happygo.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Program")
public class Program {
	List<Parameters> list;

	@XmlElement(name = "parameters")
	public List<Parameters> getList() {
		return list;
	}

	public void setList(List<Parameters> list) {
		this.list = list;
	}

}
