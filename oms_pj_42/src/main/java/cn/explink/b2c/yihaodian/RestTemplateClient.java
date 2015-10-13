package cn.explink.b2c.yihaodian;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.explink.b2c.yihaodian.xmldto.OrderDeliveryLogDto;
import cn.explink.b2c.yihaodian.xmldto.OrderDeliveryResultDto;
import cn.explink.b2c.yihaodian.xmldto.ReturnDto;

@Service
public class RestTemplateClient {
	private Logger logger = LoggerFactory.getLogger(RestTemplateClient.class);

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 订单投递结果反馈（配送单、取件单）
	 * 
	 * @param URL
	 * @param conditionDto
	 * @return
	 */
	public ReturnDto DeliveryResult(String URL, OrderDeliveryResultDto conditionDto) {
		ReturnDto result = new ReturnDto();
		logger.info("Invoke DeliveryResult's method Success!request OrderDeliveryResultDto={}", JSONObject.fromObject(conditionDto));
		try {
			// RestTemplate restTemplate = new RestTemplate();
			result = restTemplate.postForObject(URL, conditionDto, ReturnDto.class);
			logger.info("Invoke DeliveryResult's method Success!response OrderDeliveryResultDto={}", JSONObject.fromObject(result));
		} catch (Exception e) {
			String exptMessage = "error info while excute DeliveryResult's restTemplate.PostForObject method!" + e;
			// exptMessage=ExceptionTrace.getExceptionTrace(e,exptMessage);
			logger.error(exptMessage, e);
			// Mail.LoadingAndSendMessage(exptMessage);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 订单跟踪日志反馈（配送单、取件单）
	 * 
	 * @param URL
	 * @param conditionDto
	 * @return
	 */
	public ReturnDto orderDeliveryLog(String URL, OrderDeliveryLogDto conditionDto) {
		ReturnDto result = new ReturnDto();
		logger.info("Invoke orderDeliveryLog's method Success!request OrderDeliveryLogDto={}", JSONObject.fromObject(conditionDto));
		try {

			// RestTemplate restTemplate = new RestTemplate();

			result = restTemplate.postForObject(URL, conditionDto, ReturnDto.class);
			logger.info("Invoke orderDeliveryLog's method Success!response OrderDeliveryLogDto={}", JSONObject.fromObject(result));
		} catch (Exception e) {
			String exptMessage = "error info while excute orderDeliveryLog's restTemplate.PostForObject method!" + e;
			// exptMessage=ExceptionTrace.getExceptionTrace(e,exptMessage);
			logger.error(exptMessage, e);
			// Mail.LoadingAndSendMessage(exptMessage);
			e.printStackTrace();
		}
		return result;
	}

}