package cn.explink.b2c.mss.json;

import java.math.BigDecimal;

public class Good {
	private String partner_goods_id;// 商户商品ID
	private String name;// 商品名称
	private String specs;// 商品规格
	private int quantity;// 商品数量
	private BigDecimal price;// 商品单价(单位：分)

	public String getPartner_goods_id() {
		return this.partner_goods_id;
	}

	public void setPartner_goods_id(String partner_goods_id) {
		this.partner_goods_id = partner_goods_id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
