package cn.explink.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.service.CwbOrderValidator;

@Component
public class ExcelBranchShouldExists implements CwbOrderValidator {

	@Autowired
	BranchDAO branchDAO;

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 验证有没有这个指定站站点
		if (branchDAO.getBranchByBranchnameCheck(cwbOrder.getExcelbranch()).size() == 0) {
			throw new RuntimeException("没有找到指定站点：" + cwbOrder.getExcelbranch());
		}
	}

}
