package cn.explink.service.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class CargoRealWeigghtShouldAboveZero implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		try {
			if (cwbOrder.getCargorealweight().compareTo(BigDecimal.ZERO) < 0) {
				throw new RuntimeException("重量不能为负数" + cwbOrder.getCargorealweight());
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("重量必须是数字类型:" + cwbOrder.getCargorealweight());
		}
	}

}
