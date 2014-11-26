package cn.explink.b2c.yihaodian;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.explink.b2c.yihaodian.xmldto.ReturnDto;
import cn.explink.b2c.yihaodian.xmldto.OrderDeliveryResultDto;
import cn.explink.b2c.yihaodian.xmldto.OrderExportCallbackDto;
import cn.explink.b2c.yihaodian.xmldto.OrderExportConditionDto;
import cn.explink.b2c.yihaodian.xmldto.OrderExportResultDto;

@Service
public class RestTemplateClient {
	private Logger logger = LoggerFactory.getLogger(RestTemplateClient.class);

	/**
	 * 导出订单数据
	 * 
	 * @param URL
	 * @param conditionDto
	 * @return
	 */
	public OrderExportResultDto exportOrder(String URL, OrderExportConditionDto conditionDto) {
		OrderExportResultDto result = new OrderExportResultDto();
		try {
			RestTemplate restTemplate = new RestTemplate();
			result = restTemplate.postForObject(URL, conditionDto, OrderExportResultDto.class);
			logger.info("Invoke exportOrder method Success!response OrderExportResultDto={}", JSONObject.fromObject(result));
		} catch (Exception e) {
			logger.error("error info while excute exportOrder's restTemplate.PostForObject method!", e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 订单信息导出成功回调（配送单、取件单）
	 * 
	 * @param URL
	 * @param conditionDto
	 * @return
	 */
	public ReturnDto exportCallBack(String URL, OrderExportCallbackDto conditionDto) {
		ReturnDto result = new ReturnDto();
		try {
			RestTemplate restTemplate = new RestTemplate();
			result = restTemplate.postForObject(URL, conditionDto, ReturnDto.class);
			logger.info("Invoke exportCallBack method Success!response OrderExportResultDto={}", JSONObject.fromObject(result));
		} catch (Exception e) {
			logger.error("error info while excute exportCallBack's restTemplate.PostForObject method!", e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 订单投递结果反馈（配送单、取件单）
	 * 
	 * @param URL
	 * @param conditionDto
	 * @return
	 */
	public ReturnDto DeliveryResult(String URL, OrderDeliveryResultDto conditionDto) {
		ReturnDto result = new ReturnDto();
		try {
			RestTemplate restTemplate = new RestTemplate();
			result = restTemplate.postForObject(URL, conditionDto, ReturnDto.class);
			logger.info("Invoke DeliveryResult method Success!response OrderDeliveryResultDto={}", JSONObject.fromObject(result));
		} catch (Exception e) {
			logger.error("error info while excute exportCallBack's restTemplate.PostForObject method!", e);
			e.printStackTrace();
		}
		return result;
	}

}