package cn.explink.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.UserDAO;
import cn.explink.service.CwbOrderValidator;

@Component
public class ExcelDeliverShouldExists implements CwbOrderValidator {

	@Autowired
	UserDAO userDAO;

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		if (userDAO.getUsersByRealname(cwbOrder.getExceldeliver()).size() == 0) {
			throw new RuntimeException("指定小件员 查无此人：" + cwbOrder.getExceldeliver());
		}
	}

}
