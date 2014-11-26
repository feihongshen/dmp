package cn.explink.b2c.haoxgou;

import org.springframework.stereotype.Service;

import cn.explink.domain.ExcelColumnSet;

@Service
public class CwbColumnSetHXG {

	/**
	 * 根据不同的b2c标识来设置导入规则 验证参数是否合格
	 */
	public ExcelColumnSet getEexcelColumnSetByB2c(String b2cFlag) {
		ExcelColumnSet excelColumnSet = new ExcelColumnSet();

		excelColumnSet.setCwbindex(1);
		excelColumnSet.setTranscwbindex(1);
		excelColumnSet.setConsigneenameindex(1);
		excelColumnSet.setConsigneeaddressindex(1);
		excelColumnSet.setReceivablefeeindex(1);
		excelColumnSet.setPaybackfeeindex(1);
		excelColumnSet.setSendcargonameindex(1);
		excelColumnSet.setSendcargonumindex(1);
		excelColumnSet.setBackcargonameindex(1);
		excelColumnSet.setBackcargonumindex(1);
		excelColumnSet.setCwbordertypeindex(1);
		excelColumnSet.setWarehousenameindex(1);
		excelColumnSet.setCargotypeindex(1);
		excelColumnSet.setCwbremarkindex(1);
		excelColumnSet.setConsigneephoneindex(1);
		excelColumnSet.setShipcwbindex(1);
		excelColumnSet.setRemark1index(1);

		return excelColumnSet;
	}

}
