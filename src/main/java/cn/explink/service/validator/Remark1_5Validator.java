package cn.explink.service.validator;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.CwbOrderValidator;

@Component
public class Remark1_5Validator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 验证有没有这个指定站站点
		if (cwbOrder.getRemark1() != null && cwbOrder.getRemark1().length() > 100) {
			throw new RuntimeException("自定义1对应列的数据最大只能存储100字");
		} else if (cwbOrder.getRemark2() != null && cwbOrder.getRemark2().length() > 100) {
			throw new RuntimeException("自定义2对应列的数据最大只能存储100字");
		} else if (cwbOrder.getRemark3() != null && cwbOrder.getRemark3().length() > 100) {
			throw new RuntimeException("自定义3对应列的数据最大只能存储100字");
		} else if (cwbOrder.getRemark4() != null && cwbOrder.getRemark4().length() > 500) {
			throw new RuntimeException("自定义4对应列的数据最大只能存储500字");
		} else if (cwbOrder.getRemark5() != null && cwbOrder.getRemark5().length() > 1500) {
			throw new RuntimeException("自定义5对应列的数据最大只能存储1500字");
		}
	}

}
