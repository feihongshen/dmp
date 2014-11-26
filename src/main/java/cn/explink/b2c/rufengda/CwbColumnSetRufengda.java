package cn.explink.b2c.rufengda;

import org.springframework.stereotype.Service;

import cn.explink.domain.ExcelColumnSet;

@Service
public class CwbColumnSetRufengda {

	/**
	 * 根据不同的b2c标识来设置导入规则 验证参数是否合格 如风达
	 */
	public ExcelColumnSet getEexcelColumnSetByB2c(String b2cFlag) {

		ExcelColumnSet excelColumnSet = new ExcelColumnSet();
		excelColumnSet.setCwbindex(1);//
		excelColumnSet.setEmaildateindex(1); //
		excelColumnSet.setConsigneenameindex(1);//
		excelColumnSet.setConsigneepostcodeindex(1);//
		excelColumnSet.setConsigneeaddressindex(1);//
		excelColumnSet.setConsignoraddressindex(1); //
		excelColumnSet.setCwbprovinceindex(1); //
		excelColumnSet.setCwbcityindex(1); //
		excelColumnSet.setCwbcountyindex(1); //
		excelColumnSet.setConsigneemobileindex(1);//
		excelColumnSet.setConsigneephoneindex(1);//
		excelColumnSet.setCargorealweightindex(1);//
		excelColumnSet.setCustomercommandindex(1);//
		excelColumnSet.setCargoamountindex(1);//
		excelColumnSet.setReceivablefeeindex(1);//
		excelColumnSet.setSendcargonameindex(1);//
		excelColumnSet.setSendcargonumindex(1);//
		excelColumnSet.setWarehousenameindex(1);//
		excelColumnSet.setBackcargonameindex(1);
		excelColumnSet.setBackcargonumindex(1);
		excelColumnSet.setCwbordertypeindex(1);//
		excelColumnSet.setPaybackfeeindex(1);//
		excelColumnSet.setCwbdelivertypeindex(1);//
		excelColumnSet.setCustomerid(1);//
		excelColumnSet.setCwbremarkindex(1);
		excelColumnSet.setPaywayindex(1); // 支付方式
		excelColumnSet.setTranscwbindex(1); // 20130701 add 运单号
		return excelColumnSet;
	}

}
