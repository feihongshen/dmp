package cn.explink.b2c.smile;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class GoodsInfo {

	private List<Good> goods;

	@XmlElement(name = "Good")
	public List<Good> getGoods() {
		return goods;
	}

	public void setGoods(List<Good> goods) {
		this.goods = goods;
	}

}
