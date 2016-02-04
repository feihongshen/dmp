package cn.explink.b2c.yonghui;

import org.springframework.stereotype.Service;

import cn.explink.domain.ExcelColumnSet;

@Service
public class CwbColumnSetYH {

	/**
	 * 根据不同的b2c标识来设置导入规则 验证参数是否合格
	 */
	public ExcelColumnSet getEexcelColumnSetByB2c(String b2cFlag) {
		// TODO Auto-generated method stub
		ExcelColumnSet excelColumnSet = new ExcelColumnSet();
		excelColumnSet.setCwbindex(1);
		excelColumnSet.setTranscwbindex(1); // 新增 一票多件
		excelColumnSet.setConsigneenameindex(1);
		excelColumnSet.setConsigneeaddressindex(1);
		excelColumnSet.setConsigneemobileindex(1);
		excelColumnSet.setCargorealweightindex(1);
		excelColumnSet.setCustomercommandindex(1);
		excelColumnSet.setCargoamountindex(1);
		excelColumnSet.setReceivablefeeindex(1);
		excelColumnSet.setSendcargonumindex(1);
		excelColumnSet.setCwbremarkindex(1);
		excelColumnSet.setMulti_shipcwbindex(1);// 标识一票多件oms存储多条数据用到
		excelColumnSet.setRemark1index(1);
		excelColumnSet.setRemark2index(2);
		return excelColumnSet;
	}

}
