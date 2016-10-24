package cn.explink.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;
/**
 * 收货地址效验
 * @author jian.xie
 *
 */
@Component
public class ConsigneeaddressNewValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 收货地址是否为空
		if (!StringUtils.hasLength(cwbOrder.getConsigneeaddress())) {
			throw new RuntimeException("收货地址为空");
		}
	}

}
