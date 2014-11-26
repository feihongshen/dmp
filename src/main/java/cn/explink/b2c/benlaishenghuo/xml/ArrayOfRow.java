package cn.explink.b2c.benlaishenghuo.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RequestOrders")
public class ArrayOfRow {
	private List<row> list = null;

	@XmlElement(name = "RequestOrder")
	public List<row> getList() {
		return list;
	}

	public void setList(List<row> list) {
		this.list = list;
	}

}
