package cn.explink.service.validator;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class EmaildateValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		try {
			/*
			 * if(!JMath.checkdate(cwbOrder.getEmaildate())){ throw new
			 * RuntimeException("发货时间格式不正确!"); }
			 */
			if (cwbOrder.getEmaildate() == null || cwbOrder.getEmaildate().length() == 0) {
				throw new RuntimeException("发货时间格式不正确!");
			}
			if (cwbOrder.getEmaildate() != null && cwbOrder.getEmaildate().split("-")[0].length() > 4) {
				throw new RuntimeException("发货时间格式不正确!");
			}
		} catch (Exception e) {
			throw new RuntimeException("发货时间格式不正确!");
		}
	}

}
