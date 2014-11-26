package cn.explink.b2c.tmall;

import org.springframework.stereotype.Service;

import cn.explink.domain.ExcelColumnSet;

@Service
public interface CwbColumnSet {

	// 声明b2c对接XML 转为 订单列的接口
	public ExcelColumnSet getEexcelColumnSetByB2cJoint(String b2cFlag);

}
