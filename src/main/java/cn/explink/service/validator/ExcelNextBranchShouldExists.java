package cn.explink.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.service.CwbOrderValidator;

@Component
public class ExcelNextBranchShouldExists implements CwbOrderValidator {

	@Autowired
	BranchDAO branchDAO;

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 验证是否存在此派送的站点
		if (branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid()) == null) {
			throw new RuntimeException("指定派送站点 查无此站点：" + cwbOrder.getDeliverybranchid());
		}
	}

}
