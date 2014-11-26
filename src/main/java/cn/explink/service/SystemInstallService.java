package cn.explink.service;

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
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;

@Component
@DependsOn({ "systemInstallDAO" })
public class SystemInstallService implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	List<SystemConfigChangeListner> systemConfigChangeListners;

	@Autowired
	private CamelContext camelContext;

	@Autowired
	SystemInstallDAO systemInstallDAO;

	@Produce(uri = "jms:topic:systeminstall")
	ProducerTemplate systemInstallProducerTemplate;

	public void init() {
		logger.info("init addressmatch camel routes");
		try {
			logger.info("enable system install change listen routes");
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("jms:topic:systeminstall").to("bean:systemInstallService?method=notifyChange").routeId("配置改动");
				}
			});
		} catch (Exception e) {
			logger.error("system install change listen routs start fail", e);
		}

	}

	public void notifyChange(@Headers() Map<String, String> parameters) {
		for (SystemConfigChangeListner systemConfigChangeListner : systemConfigChangeListners) {
			systemConfigChangeListner.onChange(parameters);
		}
	}

	public String getParameter(String key) {
		return getParameter(key, "");
	}

	public String getParameter(String key, String defaultvalue) {
		SystemInstall systemInstallByName = systemInstallDAO.getSystemInstallByName(key);
		if (systemInstallByName == null) {
			return defaultvalue;
		}
		return systemInstallByName.getValue();
	}

	public void creSystemInstall(String chinesename, String name, String value) {
		systemInstallDAO.creSystemInstall(chinesename, name, value);
		systemInstallProducerTemplate.sendBodyAndHeader(null, name, value);

	}

	public void saveSystemInstall(String chinesename, String name, String value, long id) {
		systemInstallDAO.saveSystemInstall(chinesename, name, value, id);
		systemInstallProducerTemplate.sendBodyAndHeader(null, name, value);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		init();
	}
}
