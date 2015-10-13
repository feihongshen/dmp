package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class Order {

	private String invc_id; // 运单号 =cwb
	private String ord_id; // 订单号= transcwb
	private String ord_stat;
	private String stat_time;
	private String remark;

	@XmlElement(name = "invc_id")
	public String getInvc_id() {
		return invc_id;
	}

	public void setInvc_id(String invc_id) {
		this.invc_id = invc_id;
	}

	@XmlElement(name = "ord_id")
	public String getOrd_id() {
		return ord_id;
	}

	public void setOrd_id(String ord_id) {
		this.ord_id = ord_id;
	}

	@XmlElement(name = "ord_stat")
	public String getOrd_stat() {
		return ord_stat;
	}

	public void setOrd_stat(String ord_stat) {
		this.ord_stat = ord_stat;
	}

	@XmlElement(name = "stat_time")
	public String getStat_time() {
		return stat_time;
	}

	public void setStat_time(String stat_time) {
		this.stat_time = stat_time;
	}

	@XmlElement(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
