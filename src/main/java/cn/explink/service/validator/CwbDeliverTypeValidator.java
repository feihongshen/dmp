package cn.explink.service.validator;

import org.springframework.stereotype.Component;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class CwbDeliverTypeValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 订单类型是否符合两种类型验证
		if (cwbOrder.getCwbdelivertypeid() != 1 && cwbOrder.getCwbdelivertypeid() != 2) {
			throw new RuntimeException("包含不匹配的配送类型!");
		}
	}

}
