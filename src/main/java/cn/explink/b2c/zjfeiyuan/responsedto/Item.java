package cn.explink.b2c.zjfeiyuan.responsedto;

import javax.xml.bind.annotation.XmlElement;

public class Item {
	private String itemno;
	private String netid;
	private String netpoint;
	private String yworder;
	private String siteno;
	private String sitename;
	private String remark;
	
	@XmlElement(name="itemno")
	public String getItemno() {
		return itemno;
	}
	public void setItemno(String itemno) {
		this.itemno = itemno;
	}
	@XmlElement(name="netid")
	public String getNetid() {
		return netid;
	}
	public void setNetid(String netid) {
		this.netid = netid;
	}
	@XmlElement(name="netpoint")
	public String getNetpoint() {
		return netpoint;
	}
	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
	}
	@XmlElement(name="yworder")
	public String getYworder() {
		return yworder;
	}
	public void setYworder(String yworder) {
		this.yworder = yworder;
	}
	@XmlElement(name="siteno")
	public String getSiteno() {
		return siteno;
	}
	public void setSiteno(String siteno) {
		this.siteno = siteno;
	}
	@XmlElement(name="sitename")
	public String getSitename() {
		return sitename;
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	@XmlElement(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
