package cn.explink.b2c.liantongordercenter.xmlDto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderConfirmOption")
public class OrderConfirmOption {
	private String expressType;// 快件产品类别

	@XmlAttribute(name = "express_type")
	public String getExpressType() {
		return this.expressType;
	}

	public void setExpressType(String expressType) {
		this.expressType = expressType;
	}

}
