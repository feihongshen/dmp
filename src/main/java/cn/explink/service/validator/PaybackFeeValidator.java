package cn.explink.service.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.service.CwbOrderValidator;

@Component
public class PaybackFeeValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 应退款金额的非负验证
		if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue() && cwbOrder.getPaybackfee().compareTo(BigDecimal.ZERO) > 0) {
			throw new RuntimeException("配送订单不允许有应退款!");
		}

		if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue() && cwbOrder.getPaybackfee().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("上门退订单应退款不能为负数!");
		}

		if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue() && cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) < 0) {
			cwbOrder.setPaybackfee(new BigDecimal(0).divide(cwbOrder.getPaybackfee()));
		}

		// 应收款超大验证
		/*
		 * if(cwbOrder.getPaybackfee().compareTo(new BigDecimal(999999))>0){
		 * throw new RuntimeException("应退款异常,过大"+cwbOrder.getPaybackfee()); }
		 */
		/*
		 * else{ if(cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO)<0){
		 * throw new RuntimeException("不能同时导入应收款和应退款!"); } }
		 */
	}

}
