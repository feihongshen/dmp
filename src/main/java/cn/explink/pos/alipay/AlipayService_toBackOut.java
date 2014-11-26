package cn.explink.pos.alipay;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.DeliveryState;
import cn.explink.pos.alipay.xml.Transaction;
import cn.explink.pos.bill99.Bill99ExptMessageEnum;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class AlipayService_toBackOut extends AlipayService {
	private Logger logger = LoggerFactory.getLogger(AlipayService_toBackOut.class);

	/**
	 * 撤销交易接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toBackOut(Transaction rootnote, AliPay alipay) {
		AlipayRespNote alipayRespNote = new AlipayRespNote();
		try {
			alipayRespNote = super.BuildAlipayRespClass(rootnote, alipayRespNote);

			alipayRespNote = ValidateRequestBusiness_toBackOut(alipayRespNote, rootnote); // 验证是否符合撤销的条件
			if (alipayRespNote.getResp_code() == null) { // 返回null表示符合条件
				alipayRespNote = ExcuteCwbBackOutHandler(alipayRespNote, rootnote);
			}

		} catch (Exception e) {
			alipayRespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			alipayRespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
			logger.error("系统遇到不可预知的异常!异常原因:", e);
			e.printStackTrace();
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_toBackOut(alipayRespNote, alipay);
		String responseXml = AlipayXMLHandler.createXMLMessage_toBackOut(respMap);
		logger.info("返回alipay数据成功-业务编码={},返回XML={}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);

		return responseXml;
	}

	private AlipayRespNote ExcuteCwbBackOutHandler(AlipayRespNote alipayRespNote, Transaction rootnote) {
		// 撤销操作
		String deliverstateremark = "撤销交易";
		if ("split".equals(rootnote.getTransaction_Body().getAcq_type())) {
			deliverstateremark += ",分单撤销";
		}
		try {
			alipayRespNote.setAmount_after(alipayRespNote.getDeliverstate().getReceivedfee().doubleValue() - rootnote.getTransaction_Body().getVoid_amt());
			// 修改为媛媛的类 撤销
			cwbOrderService.deliverStatePodCancel(alipayRespNote.getOrder_no(), alipayRespNote.getBranchid(), alipayRespNote.getDeliverid(), deliverstateremark, alipayRespNote.getAmount_after());

			posPayDAO.save_PosTradeDetailRecord(alipayRespNote.getOrder_no(), deliverstateremark, 0, alipayRespNote.getDeliverid(), 1, deliverstateremark, "", 0, "", 1, 4, rootnote
					.getTransaction_Body().getAcq_type(), PosEnum.AliPay.getMethod(), 0, "");
			alipayRespNote.setResp_code(Bill99ExptMessageEnum.Success.getResp_code());
			alipayRespNote.setResp_msg(Bill99ExptMessageEnum.Success.getResp_msg());
		} catch (Exception e) {
			logger.error("Alipay撤销更新数据库异常！小件员：" + alipayRespNote.getDelivery_man() + ",当前订单号：" + alipayRespNote.getOrder_no() + e);
			alipayRespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			alipayRespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
		}
		return alipayRespNote;
	}

	private AlipayRespNote ValidateRequestBusiness_toBackOut(AlipayRespNote alipayRespNote, Transaction rootnote) {
		if (alipayRespNote.getCwbOrder() == null) {
			alipayRespNote.setResp_code(AliPayExptMessageEnum.ChaXunYiChang.getResp_code());
			alipayRespNote.setResp_msg(AliPayExptMessageEnum.ChaXunYiChang.getResp_msg());
			logger.error("alipay运单撤销,没有检索到数据" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man());
			return alipayRespNote;
		}
		// if(alipayRespNote.getDeliverstate().getSign_typeid()==1){
		// alipayRespNote.setResp_code(AliPayExptMessageEnum.BuNengCheXiao.getResp_code());
		// alipayRespNote.setResp_msg(AliPayExptMessageEnum.BuNengCheXiao.getResp_msg()+",订单已签收");
		// logger.error("alipay运单撤销,订单已签收不能撤销,单号："+alipayRespNote.getOrder_no()+",小件员："+alipayRespNote.getDelivery_man());
		// return alipayRespNote;
		// }
		if (!"split".equals(rootnote.getTransaction_Body().getAcq_type()) && alipayRespNote.getDeliverstate().getReceivedfee().doubleValue() != rootnote.getTransaction_Body().getVoid_amt()) {
			alipayRespNote.setResp_code(AliPayExptMessageEnum.YingShouJinEYiChang.getResp_code());
			alipayRespNote.setResp_msg(AliPayExptMessageEnum.YingShouJinEYiChang.getResp_msg());
			logger.error("alipay运单撤销,撤销金额与原金额不一致,单号：" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man());
			return alipayRespNote;
		}
		if (alipayRespNote.getDeliverstate().getPos().doubleValue() == 0) {
			alipayRespNote.setResp_code(AliPayExptMessageEnum.BuNengCheXiao.getResp_code());
			alipayRespNote.setResp_msg(AliPayExptMessageEnum.BuNengCheXiao.getResp_msg());
			logger.error("alipay运单撤销,非POS刷卡支付不能撤销,单号：" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man());
			return alipayRespNote;
		}
		return alipayRespNote;
	}

	private Map<String, String> convertMapType_toBackOut(AlipayRespNote alipayRespNote, AliPay alipay) {
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("transaction_id", alipayRespNote.getTransaction_id());
		retMap.put("resp_code", alipayRespNote.getResp_code());
		retMap.put("resp_msg", alipayRespNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime());
		retMap.put("requester", alipay.getRequester());
		retMap.put("target", alipay.getTargeter());
		retMap.put("order_no", alipayRespNote.getOrder_no());

		// 生成待加密的字符串
		String str = AlipayXMLHandler.createMACXML_cwbSign(retMap);
		String r = CreateRespSign(alipay, str);
		retMap.put("MAC", r);
		return retMap;
	}

}
