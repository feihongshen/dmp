package cn.explink.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;
import cn.explink.util.JMath;

@Component
public class ShipCwbValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		if (!StringUtils.hasLength(cwbOrder.getShipcwb())) {
			throw new RuntimeException("配送单号为空");
		}

		if (!JMath.checknumletter(cwbOrder.getShipcwb())) {
			throw new RuntimeException("配送单号含有非数字或非字母字符");
		}
	}

}
