package cn.explink.service.express;

import java.util.Map;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import cn.explink.domain.VO.express.ExtralInfo4Address;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.util.Tools;

@Component
public class TpsInterfaceExecutor {

	@Produce(uri = "jms:topic:executeTpsInterface")
	ProducerTemplate executeTpsInterfaceTemplate;

	@Produce(uri = "jms:topic:autoAddressInfo2")
	ProducerTemplate addressMatchExpressService;

	public Map<String, Object> executTpsInterface(ExpressOperationInfo paramObj) {
		this.executeTpsInterfaceTemplate.sendBodyAndHeader(null, "executeTpsInterfaceHeader", Tools.obj2json(paramObj));
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
		this.addressMatchExpressService.sendBodyAndHeader(null, "autoMatchAddressInfo", Tools.obj2json(info));
		return true;
	}

}
