package cn.explink.service.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.service.CwbOrderValidator;

@Component
public class ReceivableFeeShouldBeZeroForShangementui implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		if (CwbOrderTypeIdEnum.Peisong.getValue() == cwbOrder.getCwbordertypeid()
				&& (cwbOrder.getPaybackfee().doubleValue() > BigDecimal.ZERO.doubleValue() || cwbOrder.getPaybackfee().doubleValue() < BigDecimal.ZERO.doubleValue())) {
			throw new RuntimeException("配送类型订单的应退款应该为0!");
		}
	}

}
