package cn.explink.service.validator;

import org.springframework.stereotype.Component;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class ExchangecwbValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		if(cwbOrder.getExchangeflag()==1){
			if(cwbOrder.getExchangecwb()==null||cwbOrder.getExchangecwb().trim().length()<1){
				throw new RuntimeException("业务为上门换时关联的订单号不能为空");
			}
		}
	}

}
