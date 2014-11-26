package cn.explink.b2c.benlaishenghuo;

import org.springframework.stereotype.Service;
import cn.explink.domain.ExcelColumnSet;

@Service
public class CwbColumnSetBenlaishenghuo {

	/**
	 * 本来生活
	 */
	public ExcelColumnSet getEexcelColumnSetByB2c(String b2cFlag) {
		ExcelColumnSet excelColumnSet = new ExcelColumnSet();
		excelColumnSet.setSendcargonumindex(1);
		excelColumnSet.setCustomercommandindex(1);
		excelColumnSet.setConsigneephoneindex(1);
		excelColumnSet.setCwbindex(1);// 订单号
		excelColumnSet.setConsigneenameindex(1);// 客户姓名
		excelColumnSet.setCwbprovinceindex(1);// 省
		excelColumnSet.setCwbcityindex(1);// 市
		excelColumnSet.setCwbcountyindex(1);// 区
		excelColumnSet.setConsigneeaddressindex(1);// 详细地址
		excelColumnSet.setConsigneemobileindex(1);// 联系电话
		excelColumnSet.setCargorealweightindex(1);// 订单重量
		excelColumnSet.setCargosizeindex(1);// 大小规格
		excelColumnSet.setReceivablefeeindex(1);// 代收货款
		excelColumnSet.setCargoamountindex(1);// 订单数量
		excelColumnSet.setTranscwbindex(1);
		excelColumnSet.setWarehousenameindex(1);// 仓库
		excelColumnSet.setCwbordertypeindex(1);// 运单类型
		excelColumnSet.setCwbremarkindex(1);// 提示
		excelColumnSet.setEmaildateindex(1);
		excelColumnSet.setRemark1index(1);
		excelColumnSet.setRemark2index(1);
		excelColumnSet.setRemark3index(1);
		return excelColumnSet;
	}

}
