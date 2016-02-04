package cn.explink.b2c.zhongliang.backxml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class GoodDetail {

	private List<Goods> goods = new ArrayList<Goods>();

	@XmlElement(name = "Goods")
	public List<Goods> getGoods() {
		return this.goods;
	}

	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}

}
