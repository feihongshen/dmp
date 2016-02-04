package cn.explink.b2c.feiniuwang;

import org.springframework.stereotype.Service;

import cn.explink.domain.ExcelColumnSet;
@Service
public class CwbColumnSetFNW{

	/**
	 * 根据不同的b2c标识来设置导入规则
	 *  验证参数是否合格
	 *  如风达 
	 */
	public ExcelColumnSet getEexcelColumnSetByB2c(String b2cFlag) {
	    
		ExcelColumnSet excelColumnSet=new ExcelColumnSet();
		excelColumnSet.setTranscwbindex(1);//运单
		excelColumnSet.setCwbindex(1);//订单
		excelColumnSet.setCwbordertypeindex(1);//配送方式
		excelColumnSet.setConsigneenameindex(1);//收件人姓名
		excelColumnSet.setConsigneepostcodeindex(1);//邮编
		excelColumnSet.setConsigneephoneindex(1);
		excelColumnSet.setConsigneemobileindex(1);
		excelColumnSet.setCwbprovinceindex(1);//省
		excelColumnSet.setCwbcityindex(1);//市
		excelColumnSet.setConsigneeaddressindex(1);
		excelColumnSet.setCargoamountindex(1);//货物金额
		excelColumnSet.setReceivablefeeindex(1);//代收货款
		excelColumnSet.setSendcargonameindex(1);//发出商品名称
		excelColumnSet.setSendcargonumindex(1);//发出商品数量
		excelColumnSet.setPaywayindex(1);//支付方式
		excelColumnSet.setCustomercommandindex(1);//往飞牛D3系统下单的发件客户
		excelColumnSet.setRemark1index(1);//电商标识
		excelColumnSet.setRemark2index(1);//物流公司代码
		excelColumnSet.setRemark3index(1);//返单号
		excelColumnSet.setRemark4index(1);//商品单价
		excelColumnSet.setRemark5index(1);//服务类型
		excelColumnSet.setCwbremarkindex(1);//订单备注
		excelColumnSet.setCargorealweightindex(1);//重量
			
			
		return excelColumnSet;
	}

}
