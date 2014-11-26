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
		// 供货商运单号非空验证
		if (!StringUtils.hasLength(cwbOrder.getShipcwb())) {
			throw new RuntimeException("供货商运单号为空");
		}
		// 供货商运单号非法字符验证
		if (!JMath.checknumletter(cwbOrder.getShipcwb())) {
			throw new RuntimeException("供货商运单号含有非数字或非字母字符");
		}
	}

}
