package cn.explink.b2c.sfexpress.xmldtoSearch;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 下单至顺丰返回的bean
 * 
 * @author Administrator
 *
 */

public class Body {

	private RouteResponse routeResponse;// 头信息

	@XmlElement(name = "RouteResponse")
	public RouteResponse getRouteResponse() {
		return routeResponse;
	}

	public void setRouteResponse(RouteResponse routeResponse) {
		this.routeResponse = routeResponse;
	}

}
