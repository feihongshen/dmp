/**
 *
 */
package cn.explink.b2c.weisuda.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Administrator
 *
 */
public class Goods {
private List<Good> good;

/**
 * @return the good
 */
@XmlElement(name = "good")
public List<Good> getGood() {
	return this.good;
}

/**
 * @param good the good to set
 */
public void setGood(List<Good> good) {
	this.good = good;
}

}
