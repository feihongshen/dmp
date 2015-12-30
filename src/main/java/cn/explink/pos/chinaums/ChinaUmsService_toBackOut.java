package cn.explink.pos.chinaums;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.pos.chinaums.xml.Transaction;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class ChinaUmsService_toBackOut extends ChinaUmsService {
	private Logger logger = LoggerFactory.getLogger(ChinaUmsService_toBackOut.class);

	/**
	 * 撤销交易接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toBackOut(Transaction rootnote, ChinaUms chinaUms) {
		ChinaUmsRespNote chinaUmsRespNote = new ChinaUmsRespNote();
		try {
			EmployeeInfo employee = new EmployeeInfo();
			employee = jiontDAO.getEmployeeInfo(rootnote.getTransaction_Header().getEmployno());
			if (employee == null) {
				rootnote.getTransaction_Header().setResponse_code(ChinaUmsExptMessageEnum.NoUserName.getResp_code());
				rootnote.getTransaction_Header().setResponse_msg(ChinaUmsExptMessageEnum.NoUserName.getResp_msg());
				return chinaUmsService_public.createXML_toExptFeedBack(chinaUms, rootnote);
			}
			chinaUmsRespNote = super.BuildChinaumsRespClass(rootnote);
			chinaUmsRespNote = ValidateRequestBusiness_toBackOut(chinaUmsRespNote, rootnote, employee); // 验证是否符合撤销的条件
			if (chinaUmsRespNote.getResp_code() == null) { // 返回null表示符合条件
				chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.Success.getResp_code());
				chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.Success.getResp_msg());
				chinaUmsRespNote = ExcuteCwbBackOutHandler(chinaUmsRespNote, rootnote);
			}

		} catch (Exception e) {
			logger.error("系统遇到不可预知的异常!异常原因:", e);
			e.printStackTrace();
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_toBackOut(rootnote, chinaUmsRespNote, chinaUms);
		String responseXml = ChinaUmsXMLHandler.createXMLMessage_toBackOut(respMap);
		logger.info("返回chinaums数据成功-业务编码-撤销反馈-={},返回XML={}", rootnote.getTransaction_Header().getTranstype(), responseXml);

		return responseXml;
	}

	private ChinaUmsRespNote ExcuteCwbBackOutHandler(ChinaUmsRespNote chinaUmsRespNote, Transaction rootnote) {
		// 撤销操作
		String deliverstateremark = "撤销交易";

		try {
			chinaUmsRespNote.setAmount_after(chinaUmsRespNote.getDeliverstate().getReceivedfee().doubleValue() - Double.parseDouble(rootnote.getTransaction_Body().getCod()));
			// 修改为媛媛的类 撤销
			cwbOrderService.deliverStatePodCancel(chinaUmsRespNote.getOrder_no(), chinaUmsRespNote.getDeliverstate().getDeliverybranchid(), chinaUmsRespNote.getDeliverstate().getDeliveryid(),
					deliverstateremark, chinaUmsRespNote.getAmount_after());

			posPayDAO.save_PosTradeDetailRecord(chinaUmsRespNote.getOrder_no(), deliverstateremark, 0, chinaUmsRespNote.getDeliverstate().getDeliveryid(), 1, deliverstateremark, "", 0, "", 4, 2, "",
					PosEnum.ChinaUms.getMethod(), 0, rootnote.getTransaction_Header().getTermid());
		} catch (Exception e) {
			logger.error("chinaums撤销更新数据库异常！小件员：" + chinaUmsRespNote.getDelivery_man() + ",当前订单号：" + chinaUmsRespNote.getOrder_no() + e);
		}
		return chinaUmsRespNote;
	}

	private ChinaUmsRespNote ValidateRequestBusiness_toBackOut(ChinaUmsRespNote chinaUmsRespNote, Transaction rootnote, EmployeeInfo employee) {
		if (chinaUmsRespNote.getCwbOrder() == null) {
			chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_code());
			chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_msg());
			logger.error("chinaums运单撤销,没有检索到数据" + chinaUmsRespNote.getOrder_no() + ",小件员：" + chinaUmsRespNote.getDelivery_man());
			return chinaUmsRespNote;
		}
		if (chinaUmsRespNote.getCwbOrder().getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) {
			chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.JinEyichang.getResp_code());
			chinaUmsRespNote.setResp_msg("已撤销反馈的订单不可重复撤销");
			logger.error("chinaums运单撤销,已撤销反馈的订单不可重复撤销" + chinaUmsRespNote.getOrder_no() + ",小件员：" + chinaUmsRespNote.getDelivery_man());
			return chinaUmsRespNote;
		}

		if (employee.getUserid() != chinaUmsRespNote.getDeliverstate().getDeliveryid()) {
			chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.bushitongyigeren.getResp_code());
			chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.bushitongyigeren.getResp_msg());
			logger.error("chinaums运单撤销,不是当前小件员" + chinaUmsRespNote.getOrder_no() + ",小件员：" + chinaUmsRespNote.getDelivery_man());
			return chinaUmsRespNote;
		}
		if (employee.getUserid() != chinaUmsRespNote.getDeliverstate().getDeliveryid()) {
			chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.bushitongyigeren.getResp_code());
			chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.bushitongyigeren.getResp_msg());
			logger.error("chinaums运单撤销,不是当前小件员" + chinaUmsRespNote.getOrder_no() + ",小件员：" + chinaUmsRespNote.getDelivery_man());
			return chinaUmsRespNote;
		}
		if (chinaUmsRespNote.getDeliverstate().getPos().doubleValue() == 0) {
			chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.BuNengCheXiao.getResp_code());
			chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.BuNengCheXiao.getResp_msg());
			logger.error("chinaums运单撤销,非POS刷卡支付不能撤销,单号：" + chinaUmsRespNote.getOrder_no() + ",小件员：" + chinaUmsRespNote.getDelivery_man());
			return chinaUmsRespNote;
		}
		if (Double.parseDouble(rootnote.getTransaction_Body().getCod()) != chinaUmsRespNote.getDeliverstate().getPos().doubleValue()) {
			chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.jinebuyizhi.getResp_code());
			chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.jinebuyizhi.getResp_msg());
			logger.error("chinaums运单撤销,撤销的金额和原支付不一致，不能撤销,单号：" + chinaUmsRespNote.getOrder_no() + ",小件员：" + chinaUmsRespNote.getDelivery_man());
			return chinaUmsRespNote;
		}
		if (chinaUmsRespNote.getCwbOrder().getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
			chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.DingDanYiQianShou.getResp_code());
			chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.DingDanYiQianShou.getResp_msg());
			logger.error("chinaums运单撤销,不能撤销，订单已签收审核,单号：" + chinaUmsRespNote.getOrder_no() + ",小件员：" + chinaUmsRespNote.getDelivery_man());
			return chinaUmsRespNote;
		}
		return chinaUmsRespNote;
	}

	private Map<String, String> convertMapType_toBackOut(Transaction transaction, ChinaUmsRespNote chinaUmsRespNote, ChinaUms chinaUms) {
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("version", transaction.getTransaction_Header().getVersion());
		retMap.put("transtype", transaction.getTransaction_Header().getTranstype());
		retMap.put("employno", transaction.getTransaction_Header().getEmployno());
		retMap.put("termid", transaction.getTransaction_Header().getTermid());
		retMap.put("response_time", DateTimeUtil.getNowTimeNo());
		retMap.put("response_code", chinaUmsRespNote.getResp_code());
		retMap.put("response_msg", chinaUmsRespNote.getResp_msg());

		// 生成待加密的字符串
		String str = ChinaUmsXMLHandler.createMACXML_cwbSign(retMap);
		String r = CreateRespSign(chinaUms, str);
		retMap.put("mac", r.toUpperCase());
		return retMap;
	}

}
