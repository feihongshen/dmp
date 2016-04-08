package cn.explink.service;

import net.sf.json.JSONObject;

import org.apache.camel.Consume;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.ApplyEditDeliverystateDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;

@Service
public class ApplyDeliverstateService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ApplyEditDeliverystateDAO applyEditDeliverystateDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_SEND_BTOC_TO_DMP = "jms:queue:sendBToCToDmp";
	private static final String MQ_HEADER_NAME_SEND_BTOC_TO_DMP = "delIds";

	@Consume(uri = "jms:queue:sendBToCToDmp")
	public void saveError(@Header("delIds") String errorOrder) {
		JSONObject delidsjson = JSONObject.fromObject(errorOrder);
		try {
			String ids = delidsjson.getString("ids");
			String pushtime = delidsjson.getString("pushtime");
			//logger.info("deliverystate ids:{}", ids);
			applyEditDeliverystateDAO.updateState(1, ids);
			deliveryStateDAO.updateStateByIds(1, pushtime, ids);
		} catch (Exception e) {
			logger.error("", e);
			
			// 把未完成MQ插入到数据库中, start
			String functionName = "saveError";
			String fromUri = MQ_FROM_URI_SEND_BTOC_TO_DMP;
			String body = null;
			String headerName = MQ_HEADER_NAME_SEND_BTOC_TO_DMP;
			String headerValue = errorOrder;
			String exceptionMessage = e.getMessage();
			
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(functionName)
					.buildExceptionInfo(exceptionMessage).buildTopic(fromUri)
					.buildMessageHeader(headerName, headerValue).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			
			// 把未完成MQ插入到数据库中, end
		}
	}
}
