package cn.explink.b2c.bjUnion.revoke;

import javax.xml.bind.annotation.XmlElement;

public class BodydataRevoke {
	private String order_no;
	private String amt;
	private String banktrace;
	private String postrace;
	private String cardid;
	@XmlElement(name = "order_no")
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	@XmlElement(name = "amt")
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	@XmlElement(name = "banktrace")
	public String getBanktrace() {
		return banktrace;
	}
	public void setBanktrace(String banktrace) {
		this.banktrace = banktrace;
	}
	@XmlElement(name = "postrace")
	public String getPostrace() {
		return postrace;
	}
	public void setPostrace(String postrace) {
		this.postrace = postrace;
	}
	@XmlElement(name = "cardid")
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	
}
