package cn.explink.b2c.zhemeng.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
public class ResponseItem {
	private String itemno;
	private String netid;
	private String netpoint;
	
	@XmlElement(name = "itemno")
	public String getItemno() {
		return itemno;
	}
	public void setItemno(String itemno) {
		this.itemno = itemno;
	}
	@XmlElement(name = "netid")
	public String getNetid() {
		return netid;
	}
	public void setNetid(String netid) {
		this.netid = netid;
	}
	@XmlElement(name = "netpoint")
	public String getNetpoint() {
		return netpoint;
	}
	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
	}
	
	

	

	

	
}
