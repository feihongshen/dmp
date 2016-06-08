package cn.explink.b2c.tools;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.MqException;
import cn.explink.domain.SystemInstall;


@Component
public class MqExceptionHandlerUtil {
	
	private Logger logger = LoggerFactory.getLogger(MqExceptionHandlerUtil.class);
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO; 

	// 通用的mq重推  ProducerTemplate
	@Produce()
	ProducerTemplate mqExceptionTemplate;
	
	@Autowired
	SystemInstallDAO systemInstallDAO;
		
	//mq异常定时器每次执行的次数 系统参数
	public static final String MqExceptionExecuteCount = "mqExceptionExecuteCount";
	
	//mq异常定时器每次执行的次数  默认值
	public static final int DefaultExecuteCount = 1000;
	
	/**
	 * 执行重临时表获取tmall订单的定时器
	 */
	public void execute() {
		this.logger.info("MqExceptionHandlerUtil执行开始");
		int executeCount = DefaultExecuteCount;
		SystemInstall systemInstall = this.systemInstallDAO.getSystemInstall(MqExceptionExecuteCount);
		if(null != systemInstall){
			if(null != systemInstall.getValue() && Integer.valueOf(systemInstall.getValue()) > 0){
				executeCount = Integer.valueOf(systemInstall.getValue());
			}
		}
		List<MqException> mqExceptionList = this.mqExceptionDAO.listMqException(executeCount);
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

		mqException.setHandleCount(mqException.getHandleCount() + 1);//失败次数+1
		try {
			String uri = mqException.getTopic();
			if(null != uri && !"".equals(uri)){
				this.mqExceptionTemplate.setDefaultEndpointUri(uri);
				String messageBody = mqException.getMessageBody();
				String messageHeader = mqException.getMessageHeader();
				Map<String,Object> headers = new HashMap<String,Object>();
				if(null != messageHeader && !"".equals(messageHeader)){
					headers = JSON.parseObject(messageHeader, new TypeReference<Map<String, Object>>() {});
				}
				//处理消息的唯一标示UUID
				String messageHeaderUUID = mqException.getMessageHeaderUUID();//消息的唯一标示UUID
				if(null != messageHeaderUUID && !"".equals(messageHeaderUUID)){
					headers.put("MessageHeaderUUID", messageHeaderUUID);
				}else{
					String newUUID = UUID.randomUUID().toString();
					headers.put("MessageHeaderUUID", newUUID);//第一次重发，生成唯一标示UUID
					mqException.setMessageHeaderUUID(newUUID);
				}
	
				this.mqExceptionTemplate.sendBodyAndHeaders(messageBody, headers);
				mqException.setUpdatedByUser("system");
				mqException.setHandleTime(new Date());
				mqException.setHandleFlag(true);//标记为成功
			}
		} catch (Exception e) {
			this.logger.error("mq重发异常，topic=" + mqException.getTopic(), e);
		} finally {
			this.mqExceptionDAO.update(mqException);
		}
	}
}
