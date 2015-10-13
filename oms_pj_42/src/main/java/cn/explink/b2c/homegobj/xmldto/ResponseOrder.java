package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class ResponseOrder {

	private String invc_id; // 运单号 =cwb
	private String ord_id; // 订单号= transcwb
	private String err_code;
	private String err_desc;

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

	@XmlElement(name = "err_code")
	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	@XmlElement(name = "err_desc")
	public String getErr_desc() {
		return err_desc;
	}

	public void setErr_desc(String err_desc) {
		this.err_desc = err_desc;
	}

}
