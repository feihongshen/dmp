package cn.explink.b2c.zhemeng.xmldto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class ResponseAddress {


	private List<ResponseItem> items;
	@XmlElement(name = "items")
	public List<ResponseItem> getItems() {
		return items;
	}
	public void setItems(List<ResponseItem> items) {
		this.items = items;
	}
	

	
}
