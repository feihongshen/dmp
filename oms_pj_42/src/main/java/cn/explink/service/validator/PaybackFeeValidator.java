package cn.explink.service.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class PaybackFeeValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		if (cwbOrder.getPaybackfee().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("应退款不能为负数!");
		}
	}

}
