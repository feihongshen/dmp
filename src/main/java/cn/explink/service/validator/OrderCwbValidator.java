package cn.explink.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;
import cn.explink.util.JMath;

@Component
public class OrderCwbValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 订单号非空验证
		if (!StringUtils.hasLength(cwbOrder.getCwb())) {
			throw new RuntimeException("订单号为空");
		}
		// 订单号非法字符验证
		if (!JMath.checknumletter(cwbOrder.getCwb())) {
			throw new RuntimeException("订单号含有非数字或非字母字符");
		}
	}

}
