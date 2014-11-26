package cn.explink.service.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class CargoAmountShouldAboveZeroAndBelowBillon implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 货物金额非负数验证
		if (cwbOrder.getCargoamount().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("货物金额不能为负数" + cwbOrder.getCargoamount());
		}
		// 货物金额超大验证
		/*
		 * if(cwbOrder.getCargoamount().compareTo(new BigDecimal(999999))>0){
		 * throw new RuntimeException("货物金额异常,过大"+cwbOrder.getCargoamount()); }
		 */
	}

}
