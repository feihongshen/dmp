package cn.explink.b2c.zhemeng;

import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.domain.ExcelColumnSet;

@Service
public class CwbColumnSetZhemeng {

	/**
	 * 根据不同的b2c标识来设置导入规则 验证参数是否合格
	 */
	public ExcelColumnSet getEexcelColumnSetByB2c(String b2cFlag) {
		// TODO Auto-generated method stub
		ExcelColumnSet excelColumnSet = new ExcelColumnSet();

		excelColumnSet.setCwbindex(1);
		excelColumnSet.setTranscwbindex(1);
		excelColumnSet.setConsigneenameindex(1);
		excelColumnSet.setConsigneeaddressindex(1);
		excelColumnSet.setCwbprovinceindex(1);
		excelColumnSet.setCwbcityindex(1);
		excelColumnSet.setConsigneemobileindex(1);
		excelColumnSet.setConsigneephoneindex(1);
		excelColumnSet.setCargoamountindex(1);
		excelColumnSet.setReceivablefeeindex(1);
		excelColumnSet.setSendcargonumindex(1);
		excelColumnSet.setCwbordertypeindex(1);
		excelColumnSet.setCargorealweightindex(1);
		excelColumnSet.setCargovolumeindex(1);
		excelColumnSet.setCustomercommandindex(1);

		return excelColumnSet;
	}

}
