package cn.explink.b2c.zhemeng.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
public class RequestAddressShell {
	
    private RequestAddressItems requestAddressItems;
    @XmlElement(name = "items")
	public RequestAddressItems getRequestAddressItems() {
		return requestAddressItems;
	}
	public void setRequestAddressItems(RequestAddressItems requestAddressItems) {
		this.requestAddressItems = requestAddressItems;
	}
	
	
	
}
