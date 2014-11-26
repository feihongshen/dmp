package cn.explink.service.validator;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class ModelNameValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 验证有没有这个指定站站点
		if (cwbOrder.getModelname().length() == 0) {
			throw new RuntimeException("模版名称不能为空");
		}
	}

}
