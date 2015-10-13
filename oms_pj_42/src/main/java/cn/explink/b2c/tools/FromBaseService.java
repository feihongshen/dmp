package cn.explink.b2c.tools;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.Headers;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class FromBaseService implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	List<CacheBaseListener> cacheBaseListeners;

	@Autowired
	private CamelContext camelContext;

	public void init() {
		logger.info("init base info camel routes");
		try {
			logger.info("enable base info install change listen routes");
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("jms:queue:VirtualTopicConsumers.cachebase_user.courierUpdate?concurrentConsumers=1").to("bean:fromBaseService?method=notifyChange").routeId("cachebase_user");
					from("jms:queue:VirtualTopicConsumers.cachebase_branch.savezhandian?concurrentConsumers=1").to("bean:fromBaseService?method=notifyChange").routeId("cachebase_branch");
					from("jms:queue:VirtualTopicConsumers.cachebase_customer.courierUpdate?concurrentConsumers=1").to("bean:fromBaseService?method=notifyChange").routeId("cachebase_customer");
				}
			});
		} catch (Exception e) {
			logger.error("init base change listen routs start fail", e);
		}

	}

	public void notifyChange(@Headers() Map<String, String> parameters) {
		for (CacheBaseListener cacheBaseListener : cacheBaseListeners) {
			cacheBaseListener.onChange(parameters);
		}
	}

	// public void notifyChange1(@Headers()Map<String, String> parameters){
	// for(CacheBaseListener cacheBaseListener:cacheBaseListeners){
	// cacheBaseListener.onChange(parameters);
	// }
	// }
	// public void notifyChange2(@Headers()Map<String, String> parameters){
	// for(CacheBaseListener cacheBaseListener:cacheBaseListeners){
	// cacheBaseListener.onChange(parameters);
	// }
	// }

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		init();
	}
}
