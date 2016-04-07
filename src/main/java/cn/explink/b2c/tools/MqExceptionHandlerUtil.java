package cn.explink.b2c.tools;


import java.util.Date;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.MqException;


@Component
public class MqExceptionHandlerUtil {
	
	private Logger logger = LoggerFactory.getLogger(MqExceptionHandlerUtil.class);
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO; 

	// 通用的mq重推  ProducerTemplate
	@Produce()
	ProducerTemplate mqExceptionTemplate;
		
	/**
	 * 执行重临时表获取tmall订单的定时器
	 */
	public void execute() {
		this.logger.info("MqExceptionHandlerUtil执行开始");
		List<MqException> mqExceptionList = this.mqExceptionDAO.listMqException();
		if(null != mqExceptionList && mqExceptionList.size() > 0){
			for(MqException mqException : mqExceptionList){
				this.executeSingle(mqException);
			}
		}else{
			this.logger.info("MqExceptionHandlerUtil 无可执行异常列表");
		}
		this.logger.info("MqExceptionHandlerUtil执行结束");
	}
	
	/**
	 * 重发单条MQ异常
	 * @param mqException
	 */
	@Transactional
	public void executeSingle(MqException mqException){

		try {
			String uri = mqException.getTopic();
			if(null != uri && !"".equals(uri)){
				this.mqExceptionTemplate.setDefaultEndpointUri(uri);
				String messageBody = mqException.getMessageBody();
				String messageHeaderName = mqException.getMessageHeaderName();
				String messageHeader = mqException.getMessageHeader();
				
				this.mqExceptionTemplate.sendBodyAndHeader(messageBody, messageHeaderName, messageHeader);
				mqException.setUpdatedByUser("system");
				mqException.setHandleTime(new Date());
				mqException.setHandleCount(-1);//成功置为负数
			}

		} catch (Exception e) {
			mqException.setHandleCount(mqException.getHandleCount() + 1);//失败次数+1
			this.logger.error("mq重发异常，topic=" + mqException.getTopic(), e);
		} finally {
			this.mqExceptionDAO.update(mqException);
		}
	}
}
