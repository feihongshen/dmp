/**
 * 
 */
package cn.explink.domain.orderflow;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * @author Administrator
 *
 */
@XmlRootElement(name = "OrderTrack")
public class OrderTrack {
	private OrderNote order;

	@XmlElement(name = "order")
	public OrderNote getOrder() {
		return order;
	}

	public void setOrder(OrderNote order) {
		this.order = order;
	}
	
}

