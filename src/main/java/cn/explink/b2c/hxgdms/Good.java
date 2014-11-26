package cn.explink.b2c.hxgdms;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Good {

	@JsonProperty(value = "GoodsName")
	private String goodsName; // 商品名称
	@JsonProperty(value = "GoodsSku")
	private String goodsSku; // 商品SKU号
	@JsonProperty(value = "GoodsAmt")
	private BigDecimal goodsAmt; // 商品单价
	@JsonProperty(value = "GoodsCount")
	private int goodsCount; // 商品数量
	@JsonProperty(value = "GoodsHav")
	private BigDecimal goodsHav; // 商品单个重量
	@JsonProperty(value = "GoodsSize")
	private BigDecimal goodsSize; // 商品单个体积
	@JsonProperty(value = "GoodsTypeItem")
	private int goodsTypeItem;// 商品类型 0：贵品 1普通品

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsSku() {
		return goodsSku;
	}

	public void setGoodsSku(String goodsSku) {
		this.goodsSku = goodsSku;
	}

	public BigDecimal getGoodsAmt() {
		return goodsAmt;
	}

	public void setGoodsAmt(BigDecimal goodsAmt) {
		this.goodsAmt = goodsAmt;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	public BigDecimal getGoodsHav() {
		return goodsHav;
	}

	public void setGoodsHav(BigDecimal goodsHav) {
		this.goodsHav = goodsHav;
	}

	public BigDecimal getGoodsSize() {
		return goodsSize;
	}

	public void setGoodsSize(BigDecimal goodsSize) {
		this.goodsSize = goodsSize;
	}

	public int getGoodsTypeItem() {
		return goodsTypeItem;
	}

	public void setGoodsTypeItem(int goodsTypeItem) {
		this.goodsTypeItem = goodsTypeItem;
	}

}
