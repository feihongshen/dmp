package cn.explink.b2c.maikaolin.xml;

import javax.xml.bind.annotation.XmlElement;

public class Item {
	String product_id;// varchar 20 Y 商品号
	String product_name;// varchar 255 Y 商品名称
	int quantity; // smallint Y 商品数量
	String lntype;

	@XmlElement(name = "product_id")
	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	@XmlElement(name = "product_name")
	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	@XmlElement(name = "quantity")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@XmlElement(name = "lntype")
	public String getLntype() {
		return lntype;
	}

	public void setLntype(String lntype) {
		this.lntype = lntype;
	}

}
