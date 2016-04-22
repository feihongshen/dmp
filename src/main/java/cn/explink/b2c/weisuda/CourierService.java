package cn.explink.b2c.weisuda;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;
import cn.explink.util.JsonUtil;

@Service
public class CourierService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CamelContext camelContext;

	@Produce(uri = "jms:topic:courierUpdate")
	ProducerTemplate courierUpdate;

	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	public void courierUpdate(User user) {
		try {
			this.logger.info("=========修改派送员唯速达" + user.getRealname() + "=================");
			String jsonUser = JsonUtil.translateToJson(user);
			this.logger.info("消息发送端：courierUpdate, body={},header={user:update}", jsonUser);
			this.courierUpdate.sendBodyAndHeader(jsonUser, "user", "update");
		} catch (Exception e) {
			logger.error("", e);
			//写MQ异常表
			try {
				this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".courierUpdate")
						.buildExceptionInfo(e.toString()).buildTopic(this.courierUpdate.getDefaultEndpoint().getEndpointUri())
						.buildMessageBody(JsonUtil.translateToJson(user)).buildMessageHeader("user", "update").getMqException());
			} catch (IOException e1) {
				logger.error("转换异常", e1);
			}
		}

	}

	public void carrierDel(User user) {
		try {
			this.logger.info("=========删除派送员伟速达" + user.getRealname() + "=================");
			String jsonUser = JsonUtil.translateToJson(user);
			this.courierUpdate.sendBodyAndHeader(jsonUser, "user", "del");
		} catch (Exception e) {
			logger.error("", e);
			
			try {
				this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".carrierDel")
						.buildExceptionInfo(e.toString()).buildTopic(this.courierUpdate.getDefaultEndpoint().getEndpointUri())
						.buildMessageBody(JsonUtil.translateToJson(user)).buildMessageHeader("user", "del").getMqException());
			} catch (IOException e1) {
				logger.error("转换异常", e1);
			}
		}

	}

	public void customerUpdate() {
		try {
			this.courierUpdate.sendBodyAndHeader(null, "customer", "update");
		} catch (Exception e) {
			this.logger.error("供货商通知异常JMS", e);
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".customerUpdate")
					.buildExceptionInfo(e.toString()).buildTopic(this.courierUpdate.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("customer", "update").getMqException());
		}

	}

	public void platformCustomerUpdate() {
		try {
			this.courierUpdate.sendBodyAndHeader(null, "platformcustomer", "update");
		} catch (Exception e) {
			this.logger.error("(对接平台)供货商通知异常JMS", e);
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".platformCustomerUpdate")
					.buildExceptionInfo(e.toString()).buildTopic(this.courierUpdate.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("platformcustomer", "update").getMqException());

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
	// 	   logger.error("", e);
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
