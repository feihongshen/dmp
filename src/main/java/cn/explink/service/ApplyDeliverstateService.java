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

@Service
public class ApplyDeliverstateService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ApplyEditDeliverystateDAO applyEditDeliverystateDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;

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
		}
	}
}
