package cn.explink.service.validator;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class CommonnumberValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 验证承运商是否对应订单记录
		if (cwbOrder.getCommon() == null) {
			throw new RuntimeException("没有找到该承运商：" + cwbOrder.getCwb());
		}

		// 验证承运商是否对正常应用
		if (cwbOrder.getCommon().getCommonstate() == 0) {
			throw new RuntimeException("该承运商不存在或已经停用" + cwbOrder.getCwb());
		}
	}

}
