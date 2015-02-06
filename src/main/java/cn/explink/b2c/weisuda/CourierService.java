package cn.explink.b2c.weisuda;

import javax.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.domain.User;
import cn.explink.util.JsonUtil;

@Service
public class CourierService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CamelContext camelContext;

	@Produce(uri = "jms:topic:courierUpdate")
	ProducerTemplate courierUpdate;

	public void courierUpdate(User user) {
		try {
			String jsonUser = JsonUtil.translateToJson(user);
			this.courierUpdate.sendBodyAndHeader(jsonUser, "user", "update");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Produce(uri = "jms:topic:carrierDel")
	ProducerTemplate carrierDel;

	public void carrierDel(User user) {
		try {
			String jsonUser = JsonUtil.translateToJson(user);
			this.courierUpdate.sendBodyAndHeader(jsonUser, "user", "del");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ���¹�����
	 */
	public void customerUpdate() {
		try {
			this.courierUpdate.sendBodyAndHeader(null, "customer", "update");
		} catch (Exception e) {
			this.logger.error("���¹����̷���JMS�쳣", e);
		}

	}

	@PostConstruct
	public void init() {
		try {
			this.camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					this.from("jms:queue:VirtualTopicConsumers.omsToWeisudaSyn.courierUpdate?concurrentConsumers=5").to("bean:courierService?method=courierUpdate").routeId("weisuda_���¿��Ա");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void courierUpdate(@Headers()Map<String, String>
	// parameters,@Body() String body){
	//
	// logger.info("key={},value={}",parameters.keySet(),parameters.get("user"));
	// logger.info("body={}",body);
	// }
	//
}
