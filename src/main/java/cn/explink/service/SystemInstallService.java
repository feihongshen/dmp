package cn.explink.service;

import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
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

import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;

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
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_SYSTEM_INSTALL = "jms:topic:systeminstall";

	public void init() {
		logger.info("init addressmatch camel routes");
		try {
			logger.info("enable system install change listen routes");
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from(MQ_FROM_URI_SYSTEM_INSTALL).to("bean:systemInstallService?method=notifyChange").routeId("配置改动");
				}
			});
		} catch (Exception e) {
			logger.error("system install change listen routs start fail", e);
		}

	}

	public void notifyChange(@Headers() Map<String, String> parameters, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		try {
			for (SystemConfigChangeListner systemConfigChangeListner : systemConfigChangeListners) {
				systemConfigChangeListner.onChange(parameters);
			}
		} catch (Exception e) {
			// 把未完成MQ插入到数据库中, start
			Map<String, String> headers = parameters;
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".notifyChange")
					.buildExceptionInfo(e.toString()).buildTopic(MQ_FROM_URI_SYSTEM_INSTALL)
					.buildMessageHeader(headers)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
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
		try{
			logger.info("消息发送端：systemInstallProducerTemplate, {}={}", name, value);
			systemInstallProducerTemplate.sendBodyAndHeader(null, name, value);
		}catch(Exception e){
			logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".creSystemInstall")
					.buildExceptionInfo(e.toString()).buildTopic(this.systemInstallProducerTemplate.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader(name, value).getMqException());
		}

	}

	public void saveSystemInstall(String chinesename, String name, String value, long id) {
		systemInstallDAO.saveSystemInstall(chinesename, name, value, id);
		try{
			logger.info("消息发送端：systemInstallProducerTemplate, {}={}", name, value);
			systemInstallProducerTemplate.sendBodyAndHeader(null, name, value);
		}catch(Exception e){
			logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".saveSystemInstall")
					.buildExceptionInfo(e.toString()).buildTopic(this.systemInstallProducerTemplate.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader(name, value).getMqException());
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		init();
	}
}
