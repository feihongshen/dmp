package cn.explink.pos.etong;

import org.apache.cxf.endpoint.Client;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.hyyt.elog.dmp.DmpElogService;
import com.hyyt.elog.dmp.OrderAttemperRequestType;
import com.hyyt.elog.dmp.OrderAttemperResponseType;
import com.hyyt.ws.LogicEndPoint;

public class EtongServiceClient implements LogicEndPoint {

	private long timeout = 10000L;// 单位毫秒

	private LogicEndPoint logicEndpoint;

	private DmpElogService dmpElogService;

	private static final String TARGET_NS = "http://dmp.elog.hyyt.com";

	public EtongServiceClient(String url, long timeout) {

		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		// factory.setServiceClass(LogicEndPoint.class);
		factory.setServiceClass(DmpElogService.class);
		factory.setAddress(url);
		dmpElogService = (DmpElogService) factory.create();
		Client client = ClientProxy.getClient(dmpElogService);
		if (client != null) {
			HTTPConduit conduit = (HTTPConduit) client.getConduit();
			HTTPClientPolicy policy = new HTTPClientPolicy();
			policy.setConnectionTimeout(timeout);
			policy.setReceiveTimeout(timeout);
			conduit.setClient(policy);
		}
	}

	public OrderAttemperResponseType orderAttemper(OrderAttemperRequestType orderAttemperRequestType) {
		return dmpElogService.orderAttemper(orderAttemperRequestType);
	}

}
