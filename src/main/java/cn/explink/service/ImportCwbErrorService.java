package cn.explink.service;

import net.sf.json.JSONObject;

import org.apache.camel.Consume;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbErrorDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;

@Service
public class ImportCwbErrorService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CwbErrorDAO CwbErrorDAO;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_CWBORDERINSERT = "jms:queue:cwborderinsert";
	private static final String MQ_HEADER_NAME_CWBORDERINSERT = "errorOrder";

	@Consume(uri = "jms:queue:cwborderinsert")
	public void saveError(@Header("errorOrder") String errorOrder) {
		JSONObject errorJson = JSONObject.fromObject(errorOrder);
		try {
			String cwb = errorJson.getJSONObject("cwbOrderDTO").getString("cwb");
			logger.info("import excel error order cwb:{}", cwb);
			String emaildate = errorJson.getJSONObject("cwbOrderDTO").getString("emaildate");
			long emaildateid = errorJson.getLong("emaildateid");
			String message = errorJson.getString("message");
			CwbErrorDAO.creCwbError(cwb, errorJson.toString(), emaildateid, emaildate, message);
		} catch (Exception e) {
			logger.error("", e);
			logger.error("error while doing import excel error order cwb:{}", errorJson.getJSONObject("cwbOrderDTO").getString("cwb"));
			logger.error(errorOrder);
			
			// 把未完成MQ插入到数据库中, start
			String functionName = "saveError";
			String fromUri = MQ_FROM_URI_CWBORDERINSERT;
			String body = null;
			String headerName = MQ_HEADER_NAME_CWBORDERINSERT;
			String headerValue = errorOrder;
			String exceptionMessage = e.getMessage();
			
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(functionName)
					.buildExceptionInfo(exceptionMessage).buildTopic(fromUri)
					.buildMessageHeader(headerName, headerValue).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}

	public void saveOrderError(JSONObject errorJson) {
		try {
			String cwb = errorJson.getJSONObject("cwbOrderDTO").getString("cwb");
			logger.info("import excel error order cwb:{}", cwb);
			String emaildate = errorJson.getJSONObject("cwbOrderDTO").getString("emaildate");
			long emaildateid = errorJson.getLong("emaildateid");
			String message = errorJson.getString("message");
			CwbErrorDAO.creCwbError(cwb, errorJson.toString(), emaildateid, emaildate, message);
		} catch (Exception e) {
			logger.error("", e);
			logger.error("error while doing import excel error order cwb:{}", errorJson.getJSONObject("cwbOrderDTO").getString("cwb"));
			logger.error(errorJson.toString());
		}
	}

}
