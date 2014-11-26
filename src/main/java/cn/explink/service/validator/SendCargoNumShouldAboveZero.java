package cn.explink.service.validator;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class SendCargoNumShouldAboveZero implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		try {
			// 发货数量非负数验证
			if (cwbOrder.getSendcargonum() < 0) {
				throw new RuntimeException("发货商品数量不能为负数" + cwbOrder.getSendcargonum());
			}
			// 发货数量不能超过2000件验证，2013.11.19，鞠sir
			if (cwbOrder.getSendcargonum() > 2000) {
				throw new RuntimeException("发货商品数量不能超过2000件" + cwbOrder.getSendcargonum());
			}
		} catch (NumberFormatException e) {
			// 取货数量非负数的数字类型验证
			throw new RuntimeException("发货商品数量必须是数字类型:" + cwbOrder.getSendcargonum());
		}
	}

}
