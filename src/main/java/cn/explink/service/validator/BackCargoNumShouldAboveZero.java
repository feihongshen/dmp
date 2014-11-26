package cn.explink.service.validator;

import org.springframework.stereotype.Component;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class BackCargoNumShouldAboveZero implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		try {
			if (cwbOrder.getBackcargonum() < 0) {
				// 取回商品数量非负数验证
				throw new RuntimeException("取回商品数量不能为负数" + cwbOrder.getBackcargonum());
			}
		} catch (NumberFormatException e) {
			// 取回商品数量数字类型的验证
			throw new RuntimeException("取回商品数量必须是数字类型:" + cwbOrder.getBackcargonum());
		}
	}

}
