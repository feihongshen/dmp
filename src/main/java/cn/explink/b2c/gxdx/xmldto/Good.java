/**
 * 
 */
package cn.explink.b2c.gxdx.xmldto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @ClassName: Good
 * @Description: TODO
 * @Author: 王强
 * @Date: 2015年11月12日上午11:13:50
 */
public class Good {
	private String goodsName;// 货物名称
	private BigDecimal goodsValue;// 货物金额
	private String goodsBarCode;// 店内码
	private int listType; // 0正常1换进2换出
	
	
	
	
	

	@XmlElement(name = "GoodsName")
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	@XmlElement(name = "GoodsValue")
	public BigDecimal getGoodsValue() {
		return goodsValue;
	}

	public void setGoodsValue(BigDecimal goodsValue) {
		this.goodsValue = goodsValue;
	}

	@XmlElement(name = "GoodsBarCode")
	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	@XmlElement(name = "ListType")
	public int getListType() {
		return listType;
	}

	public void setListType(int listType) {
		this.listType = listType;
	}

}
