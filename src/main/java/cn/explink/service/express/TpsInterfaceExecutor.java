package cn.explink.service.express;

import java.util.Map;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.VO.express.ExtralInfo4Address;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.util.Tools;

@Component
public class TpsInterfaceExecutor {

	@Produce(uri = "jms:topic:executeTpsInterface")
	ProducerTemplate executeTpsInterfaceTemplate;

	@Produce(uri = "jms:topic:autoAddressInfo2")
	ProducerTemplate addressMatchExpressService;

	@Autowired
	private MqExceptionDAO mqExceptionDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Map<String, Object> executTpsInterface(ExpressOperationInfo paramObj) {
		//快递轨迹回传个tps 需注释
		/*try{
			String header = Tools.obj2json(paramObj);
			this.logger.info("消息发送端：executeTpsInterfaceTemplate, executeTpsInterfaceHeader={}", header);
			this.executeTpsInterfaceTemplate.sendBodyAndHeader(null, "executeTpsInterfaceHeader", Tools.obj2json(paramObj));
		}catch(Exception e){
			logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".executTpsInterface")
					.buildExceptionInfo(e.toString()).buildTopic(this.executeTpsInterfaceTemplate.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("executeTpsInterfaceHeader", Tools.obj2json(paramObj)).getMqException());
		}*/
		return null;
	}

	/**
	 *
	 * @Title: autoMatch
	 * @description 发送jms消息，匹配地址库(自动匹配站点)
	 * @author 王志宇（刘武强修改）
	 * @date 2015年9月17日上午9:48:25
	 * @param @param preOrderNo 预订单号/订单号
	 * @param @param userId 用户id
	 * @param @param address 待匹配地址
	 * @param @param addressMatcher 调用者标示
	 * @param @return
	 * @return Boolean
	 * @throws
	 */
	public Boolean autoMatch(String preOrderNo, Long userId, String address, int addressMatcher) {
		ExtralInfo4Address info = new ExtralInfo4Address(preOrderNo, userId, address, addressMatcher);
		try{
			String header = Tools.obj2json(info);
			this.logger.info("消息发送端：addressMatchExpressService, autoMatchAddressInfo={}", header);
			this.addressMatchExpressService.sendBodyAndHeader(null, "autoMatchAddressInfo", header);
		}catch(Exception e){
			logger.error("", e);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".autoMatch")
					.buildExceptionInfo(e.toString()).buildTopic(this.addressMatchExpressService.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("autoMatchAddressInfo", Tools.obj2json(info)).getMqException());
		}
		return true;
	}

}
