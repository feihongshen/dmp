package cn.explink.service.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class BackCargoAmountShouldAboveZero implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		try {
			if (cwbOrder.getBackcargoamount().compareTo(BigDecimal.ZERO) < 0) {
				throw new RuntimeException("发货商品数量不能为负数" + cwbOrder.getBackcargoamount());
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("发货商品数量必须是数字类型:" + cwbOrder.getBackcargoamount());
		}
	}

}
