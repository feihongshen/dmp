package cn.explink.service.validator;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.CwbOrderValidator;

@Component
public class PayWayValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 支付方式是否符合五种支付方式（添加一种 CodPos）=======LX
		if (cwbOrder.getPaywayid() != PaytypeEnum.Xianjin.getValue() && cwbOrder.getPaywayid() != PaytypeEnum.Pos.getValue() && cwbOrder.getPaywayid() != PaytypeEnum.Zhipiao.getValue()
				&& cwbOrder.getPaywayid() != PaytypeEnum.Qita.getValue() && cwbOrder.getPaywayid() != PaytypeEnum.CodPos.getValue()) {
			throw new RuntimeException("包含不可识别的支付方式!");
		}
	}
}
