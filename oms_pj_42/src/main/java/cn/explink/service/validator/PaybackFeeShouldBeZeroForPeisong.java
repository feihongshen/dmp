package cn.explink.service.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class PaybackFeeShouldBeZeroForPeisong implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		try {
			if ("1".equals(cwbOrder.getCwbordertypeid())
					&& (cwbOrder.getPaybackfee().doubleValue() > BigDecimal.ZERO.doubleValue() || cwbOrder.getPaybackfee().doubleValue() < BigDecimal.ZERO.doubleValue())) {
				throw new RuntimeException("配送类型的应退款应该为0!");
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("配送类型的应退款必须为数字");
		}
	}

}
