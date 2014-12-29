package cn.explink.b2c.gztl;

import cn.explink.domain.ExcelColumnSet;

public class CwbClolumSetGztl {
	/**
	 * 根据不同的b2c标识来设置导入规则 验证参数是否合格
	 */
	public ExcelColumnSet getEexcelColumnSetByB2c(String b2cFlag) {
		// TODO Auto-generated method stub
		ExcelColumnSet excelColumnSet = new ExcelColumnSet();
		excelColumnSet.setCwbordertypeidindex(1);
		excelColumnSet.setCwbindex(1);
		excelColumnSet.setTranscwbindex(1);
		excelColumnSet.setConsigneenameindex(1);
		excelColumnSet.setConsigneeaddressindex(1);
		excelColumnSet.setConsigneephoneindex(1);
		excelColumnSet.setSendcargonameindex(1);// ??sendcarname
		excelColumnSet.setBackcargonameindex(1);
		excelColumnSet.setCargoamountindex(1);// ??caramount
		excelColumnSet.setCargorealweightindex(1);
		excelColumnSet.setReceivablefeeindex(1);
		excelColumnSet.setCustomercommandindex(1);
		excelColumnSet.setSendcargonumindex(1);// sendcarnum??
		excelColumnSet.setRemark1index(1);
		excelColumnSet.setRemark2index(1);
		excelColumnSet.setRemark3index(1);
		excelColumnSet.setRemark4index(1);
		excelColumnSet.setRemark5index(1);
		excelColumnSet.setPaywayindex(1);// paywayid??
		return excelColumnSet;
	}

}
