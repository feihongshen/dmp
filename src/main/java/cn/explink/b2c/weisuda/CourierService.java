package cn.explink.b2c.weisuda;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
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
			this.logger.info("=========修改派送员唯速达" + user.getRealname() + "=================");
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
			this.logger.info("=========删除派送员伟速达" + user.getRealname() + "=================");
			String jsonUser = JsonUtil.translateToJson(user);
			this.courierUpdate.sendBodyAndHeader(jsonUser, "user", "del");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void customerUpdate() {
		try {
			this.courierUpdate.sendBodyAndHeader(null, "customer", "update");
		} catch (Exception e) {
			this.logger.error("供货商通知异常JMS", e);
		}

	}

	public void platformCustomerUpdate() {
		try {
			this.courierUpdate.sendBodyAndHeader(null, "platformcustomer", "update");
		} catch (Exception e) {
			this.logger.error("(对接平台)供货商通知异常JMS", e);

		}
	}

	// @PostConstruct
	// public void init() {
	// try {
	// this.camelContext.addRoutes(new RouteBuilder() {
	// @Override
	// public void configure() throws Exception {
	// this.from("jms:queue:VirtualTopicConsumers.omsToWeisudaSyn.courierUpdate?concurrentConsumers=5").to("bean:courierService?method=courierUpdate").routeId("weisuda_���¿��Ա");
	// }
	// });
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// public void courierUpdate(@Headers()Map<String, String>
	// parameters,@Body() String body){
	//
	// logger.info("key={},value={}",parameters.keySet(),parameters.get("user"));
	// logger.info("body={}",body);
	// }
	//
}
