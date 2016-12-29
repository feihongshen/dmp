package cn.explink.b2c.mss;

import org.springframework.stereotype.Service;

import cn.explink.domain.ExcelColumnSet;

@Service
public class CwbColumnSetMSS {
	/**
	 * 根据不同的b2c标识来设置导入规则 验证参数是否合格 美食送
	 */
	public ExcelColumnSet getEexcelColumnSetByB2c(String b2cFlag) {

		ExcelColumnSet excelColumnSet = new ExcelColumnSet();
		excelColumnSet.setCwbindex(1);//
		excelColumnSet.setCwbcityindex(1);
		excelColumnSet.setSendcargonameindex(1);
		excelColumnSet.setCargosizeindex(1);
		excelColumnSet.setConsigneenameindex(1);
		excelColumnSet.setConsigneeaddressindex(1);
		excelColumnSet.setConsigneephoneindex(1);
		excelColumnSet.setConsigneemobileindex(1);
		excelColumnSet.setReceivablefeeindex(1);
		excelColumnSet.setCwbremarkindex(1);
		excelColumnSet.setRemark1index(1);
		return excelColumnSet;
	}
}
