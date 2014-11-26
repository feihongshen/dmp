package cn.explink.service.validator;

import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.service.CwbOrderValidator;

@Component
public class CwbOrderTypeValidator implements CwbOrderValidator {

	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 订单类型是否符合三种类型验证
		if (cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Peisong.getValue() && cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmenhuan.getValue()
				&& cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			throw new RuntimeException("包含不符合的订单类型!");
		} else {
			/*
			 * if(cwbOrder.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue
			 * ()){ if(!((cwbOrder.getBackcargoname()
			 * ==null||cwbOrder.getBackcargoname().equals("")) &&
			 * cwbOrder.getSendcargoname() !=null &&
			 * !(cwbOrder.getSendcargoname().trim().equals("")))){ throw new
			 * RuntimeException
			 * ("“配送”的订单类型与商品状态不匹配，“配送”的订单不应该有“取回商品名称”，并且必须有“发货产品名称”，请检查数据!"); }
			 * 
			 * }
			 * if(cwbOrder.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.
			 * getValue()){ if(!(cwbOrder.getBackcargoname() !=null &&
			 * !(cwbOrder.getBackcargoname().equals("")) &&
			 * (cwbOrder.getSendcargoname() ==null ||
			 * cwbOrder.getSendcargoname().equals("")))){ throw new
			 * RuntimeException
			 * ("“上门退”的订单类型与商品状态不匹配，“上门退”的订单不应该有“发货商品名称”，并且必须有“取回产品名称”，请检查数据!");
			 * } }
			 * if(cwbOrder.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan
			 * .getValue()){ if(!(cwbOrder.getBackcargoname() !=null &&
			 * !(cwbOrder.getBackcargoname().equals("")) &&
			 * cwbOrder.getSendcargoname() !=null &&
			 * !(cwbOrder.getSendcargoname().equals("")))){ throw new
			 * RuntimeException
			 * ("“上门换”的订单类型与商品状态不匹配，“上门换”的订单必须有“发货商品名称”和“取回产品名称”，请检查数据!"); } }
			 */
		}

	}
}
