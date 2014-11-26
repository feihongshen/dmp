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
			// 取回商品金额非负数验证
			if (cwbOrder.getBackcargoamount().compareTo(BigDecimal.ZERO) < 0) {
				throw new RuntimeException("取回商品金额不能为负数" + cwbOrder.getBackcargoamount());
			}
			// 取回商品金额超大验证
			/*
			 * if(cwbOrder.getBackcargoamount().compareTo(new
			 * BigDecimal(999999))>0){ throw new
			 * RuntimeException("取回商品金额异常,过大"+cwbOrder.getBackcargoamount()); }
			 */
		} catch (NumberFormatException e) {
			// 发货商品数量数字类型验证
			throw new RuntimeException("取回商品金额必须是数字类型:" + cwbOrder.getBackcargoamount());
		}
	}

}
