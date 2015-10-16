package cn.explink.b2c.zhemeng.xmldto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


public class RequestAddressItems {
	
    private List<RequestItems> items;
	
	@XmlElement(name = "item")
	public List<RequestItems> getItems() {
		return items;
	}
	public void setItems(List<RequestItems> items) {
		this.items = items;
	}
	
}
