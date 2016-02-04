package cn.explink.b2c.meilinkai;

import org.springframework.stereotype.Service;

import cn.explink.domain.ExcelColumnSet;
@Service
public class CwbColumnSetMLK{

	/**
	 * 
	 * 根据不同的b2c标识来设置导入规则
	 *  验证参数是否合格
	 *  如风达 
	 */
	public ExcelColumnSet getEexcelColumnSetByB2c() {
	    
		ExcelColumnSet excelColumnSet=new ExcelColumnSet();
			excelColumnSet.setCwbindex(1);
			excelColumnSet.setTranscwbindex(1);
			excelColumnSet.setCwbordertypeindex(1);//
			excelColumnSet.setSendcargonumindex(1);
			excelColumnSet.setReceivablefeeindex(1);//
			excelColumnSet.setConsigneenameindex(1);//
			excelColumnSet.setConsigneepostcodeindex(1);
			excelColumnSet.setCwbprovinceindex(1);
			excelColumnSet.setCwbcityindex(1);
			excelColumnSet.setCwbcountyindex(1);
			excelColumnSet.setConsigneeaddressindex(1);
			excelColumnSet.setConsigneemobileindex(1);
			excelColumnSet.setCargorealweightindex(1);
			excelColumnSet.setRemark1index(1);
			excelColumnSet.setRemark2index(1);
			
			
			
		return excelColumnSet;
	}

}
