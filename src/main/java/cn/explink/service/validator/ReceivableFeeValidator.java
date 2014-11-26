package cn.explink.service.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.service.CwbOrderValidator;

@Component
public class ReceivableFeeValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 应收款的非负数验证
		if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue() && cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("配送订单应收款不能为负数!");
		}
		if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue() && cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) > 0) {
			throw new RuntimeException("上门退订单不允许有应收款!");
		}

		// 应收款超大验证
		/*
		 * if(cwbOrder.getReceivablefee().compareTo(new BigDecimal(999999))>0){
		 * throw new RuntimeException("应收款异常,过大"+cwbOrder.getReceivablefee()); }
		 */
		/*
		 * else{ if(cwbOrder.getPaybackfee().compareTo(BigDecimal.ZERO)>0){
		 * throw new RuntimeException("不能同时导入应收款和应退款!"); } }
		 */
	}

}
