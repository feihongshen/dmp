package cn.explink.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;
import cn.explink.util.JMath;

@Component
public class TransCwbValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		if (!StringUtils.hasLength(cwbOrder.getTranscwb())) {
			throw new RuntimeException("发货单号为空");
		}

		if (!JMath.checknumletter(cwbOrder.getTranscwb())) {
			throw new RuntimeException("发货单号含有非数字或非字母字符");
		}
	}

}
