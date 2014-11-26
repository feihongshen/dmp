package cn.explink.b2c.happyGo;

import org.springframework.stereotype.Service;

import cn.explink.domain.ExcelColumnSet;

@Service
public class CwbColumnSetHappy {

	/**
	 * 根据不同的b2c标识来设置导入规则 验证参数是否合格
	 */
	public ExcelColumnSet getEexcelColumnSetByB2c(String b2cFlag) {
		// TODO Auto-generated method stub
		ExcelColumnSet excelColumnSet = new ExcelColumnSet();
		excelColumnSet.setExcelbranchindex(1);
		excelColumnSet.setWarehousenameindex(1);
		excelColumnSet.setConsigneenameindex(1);
		excelColumnSet.setConsigneeaddressindex(1);
		excelColumnSet.setConsigneemobileindex(1);
		excelColumnSet.setPaywayindex(1);
		excelColumnSet.setConsigneephoneindex(1);
		excelColumnSet.setCargorealweightindex(1);
		excelColumnSet.setTranscwbindex(1);
		excelColumnSet.setReceivablefeeindex(1);
		excelColumnSet.setSendcargonameindex(1);
		excelColumnSet.setSendcargonumindex(1);
		excelColumnSet.setWarehousenameindex(1);
		excelColumnSet.setCwbordertypeindex(1);
		excelColumnSet.setCwbremarkindex(1);
		excelColumnSet.setCwbindex(1);
		excelColumnSet.setConsigneenameindex(1);
		excelColumnSet.setConsigneeaddressindex(1);
		excelColumnSet.setRemark1index(1);
		excelColumnSet.setRemark3index(1);
		excelColumnSet.setRemark2index(1);
		excelColumnSet.setIsaudit(1);
		excelColumnSet.setCommoncwb(1);
		return excelColumnSet;
	}

}
