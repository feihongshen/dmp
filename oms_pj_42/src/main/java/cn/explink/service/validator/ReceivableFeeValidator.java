package cn.explink.service.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class ReceivableFeeValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		if (cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("应收款不能为负数!");
		}
	}

}
