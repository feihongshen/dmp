package cn.explink.service.validator;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class SendCargoNumShouldAboveZero implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		try {
			if (cwbOrder.getBackcargonum() < 0) {
				throw new RuntimeException("取货商品数量不能为负数" + cwbOrder.getBackcargonum());
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("取货商品数量必须是数字类型:" + cwbOrder.getBackcargonum());
		}
	}

}
