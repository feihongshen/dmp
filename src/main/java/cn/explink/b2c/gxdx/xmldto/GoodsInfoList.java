/**
 * 
 */
package cn.explink.b2c.gxdx.xmldto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @ClassName: GoodsInfoList
 * @Description: TODO
 * @Author: 王强
 * @Date: 2015年11月12日上午11:12:47
 */
public class GoodsInfoList {
	private List<Good> good;

	@XmlElement(name = "Good")
	public List<Good> getGood() {
		return good;
	}

	public void setGood(List<Good> good) {
		this.good = good;
	}

}
