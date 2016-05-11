package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.ImportCwbOrderType;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.util.StringUtil;

@Service
public class CwbOrderTypeService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public ImportCwbOrderType loadFormForCwbOrderTypeToEdit(HttpServletRequest request, long importid) {
		ImportCwbOrderType importCwbOrderType = loadFormForCwbOrderType(request);
		importCwbOrderType.setImportid(importid);
		return importCwbOrderType;
	}

	public ImportCwbOrderType loadFormForCwbOrderType(HttpServletRequest request) {
		ImportCwbOrderType importCwbOrderType = new ImportCwbOrderType();
		importCwbOrderType.setImporttypeid(Integer.parseInt(request.getParameter("importtypeid")));
		importCwbOrderType.setImporttype(StringUtil.nullConvertToEmptyString(request.getParameter("importtype")));
		return importCwbOrderType;
	}
	
	/**
	 * 校验上门退成功订单
	 * @param cwbOrder 订单对象
	 * @param flowOrderTypeEnum 操作类型
	 * @throws CwbException 如果不允许该项操作，则会抛出异常
	 */
	public void validateShangMenTuiSuccess(CwbOrder cwbOrder, FlowOrderTypeEnum flowOrderTypeEnum) {
		if (cwbOrder == null) {
			logger.info("validateShangMenTuiSuccess->cwbOrder is null");
			return;
		}
		if (flowOrderTypeEnum == null) {
			logger.info("validateShangMenTuiSuccess->flowOrderTypeEnum is null");
			return;
		}
		if (CwbOrderTypeIdEnum.Shangmentui.getValue() == cwbOrder.getCwbordertypeid()) { //上门退订单
			/*
			 * 上门退订单流程
  导入数据（或对接获取）——地址库匹配——站点到货——小件员领货——小件员反馈——归班——退货出站——退货库入库——退供货商——退供货商成功（失败，失败就拒收返库）
			 * 上门退成功的订单，即站长已经进行归班审核的订单，下一步只能做 “退货出站” 的操作
			 */
			if (CwbStateEnum.TuiHuo.getValue() == cwbOrder.getCwbstate() &&
					DeliveryStateEnum.ShangMenTuiChengGong.getValue() == cwbOrder.getDeliverystate() &&
					FlowOrderTypeEnum.YiShenHe.getValue() == cwbOrder.getFlowordertype()) {
				
				throw new CwbException(cwbOrder.getCwb(), flowOrderTypeEnum.getValue(), 
						ExceptionCwbErrorTypeEnum.SHANG_MEN_TUI_CHENG_GONG_TUI_HUO_CHU_ZHAN, flowOrderTypeEnum.getText());
			}
		}
	}

}
