package cn.explink.b2c.liantong.json;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonProperty;

public class Goods {
	@JsonProperty(value = "GoodsName")
	private String goodsName;
	@JsonProperty(value = "GoodsNum")
	private int goodsNum;
	@JsonProperty(value = "GoodsPrice")
	private double goodsPrice;
	@JsonProperty(value = "GoodsWeight")
	private double goodsWeight;
	@JsonProperty(value = "GoodsVolume")
	private double goodsVolume;

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public double getGoodsWeight() {
		return goodsWeight;
	}

	public void setGoodsWeight(double goodsWeight) {
		this.goodsWeight = goodsWeight;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public double getGoodsVolume() {
		return goodsVolume;
	}

	public void setGoodsVolume(double goodsVolume) {
		this.goodsVolume = goodsVolume;
	}

}
