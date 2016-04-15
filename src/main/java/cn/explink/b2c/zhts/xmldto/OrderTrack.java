/**
 * 
 */
package cn.explink.b2c.zhts.xmldto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * @author Administrator
 *
 */
@XmlRootElement(name = "OrderTrack")
public class OrderTrack {
	private Order order;

	@XmlElement(name = "order")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
}

