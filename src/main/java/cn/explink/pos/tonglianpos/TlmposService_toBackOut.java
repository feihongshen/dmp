package cn.explink.pos.tonglianpos;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.pos.tonglianpos.xmldto.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class TlmposService_toBackOut extends TlmposService {
	private Logger logger = LoggerFactory.getLogger(TlmposService_toBackOut.class);

	/**
	 * 撤销交易接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toBackOut(Transaction rootnote, Tlmpos tlmpos) {
		TlmposRespNote tlmposRespnote = new TlmposRespNote();
		try {
			tlmposRespnote = super.buildtlmposRespClass(rootnote, tlmposRespnote);

			tlmposRespnote = ValidateRequestBusiness_toBackOut(tlmposRespnote, rootnote); // 验证是否符合撤销的条件
			if (tlmposRespnote.getResp_code() == null) { // 返回null表示符合条件
				tlmposRespnote = ExcuteCwbBackOutHandler(tlmposRespnote, rootnote); // 执行撤销方法
			}

		} catch (Exception e) {
			tlmposRespnote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
			tlmposRespnote.setResp_msg(TlmposExptMsgEnum.QiTaShiBai.getResp_msg());
			logger.error("运单撤销未知异常", e);

		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_toBackOut(tlmposRespnote, tlmpos, rootnote);
		String responseXml = TlmposXMLHandler.createXMLMessage_toBackOut(respMap);
		logger.info("返回tlmpos数据成功-业务编码={},返回XML={}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);

		return responseXml;
	}

	/**
	 * 撤销操作
	 * 
	 * @param alipayRespNote
	 * @param rootnote
	 * @return
	 */
	private TlmposRespNote ExcuteCwbBackOutHandler(TlmposRespNote alipayRespNote, Transaction rootnote) {

		String deliverstateremark = "撤销交易";

		try {
			alipayRespNote.setAmount_after(alipayRespNote.getDeliverstate().getReceivedfee().doubleValue() - rootnote.getTransaction_Body().getVoid_amt());
			// 执行公共撤销方法
			cwbOrderService.deliverStatePodCancel(alipayRespNote.getOrder_no(), alipayRespNote.getBranchid(), alipayRespNote.getDeliverid(), deliverstateremark, alipayRespNote.getAmount_after());

			posPayDAO.save_PosTradeDetailRecord(alipayRespNote.getOrder_no(), deliverstateremark, 0, alipayRespNote.getDeliverid(), 1, deliverstateremark, "", 0, "", 1, 4, rootnote
					.getTransaction_Body().getAcq_type(), PosEnum.TongLianPos.getMethod(), 0, "");
			alipayRespNote.setResp_code(TlmposExptMsgEnum.Success.getResp_code());
			alipayRespNote.setResp_msg(TlmposExptMsgEnum.Success.getResp_msg());
		} catch (Exception e) {
			logger.error("tlmpos撤销更新数据库异常！小件员：" + alipayRespNote.getDelivery_man() + ",订单号：" + alipayRespNote.getOrder_no(), e);
			alipayRespNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
			alipayRespNote.setResp_msg(e.getMessage());
		}
		return alipayRespNote;
	}

	private TlmposRespNote ValidateRequestBusiness_toBackOut(TlmposRespNote tlmposRespnote, Transaction rootnote) {
		if (tlmposRespnote.getCwbOrder() == null) {
			tlmposRespnote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
			tlmposRespnote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());
			logger.error("tlmpos运单撤销,没有检索到数据" + tlmposRespnote.getOrder_no() + ",小件员：" + tlmposRespnote.getDelivery_man());
			return tlmposRespnote;
		}

		if (tlmposRespnote.getDeliverstate().getReceivedfee().doubleValue() != rootnote.getTransaction_Body().getVoid_amt()) {
			tlmposRespnote.setResp_code(TlmposExptMsgEnum.YingShouJinEYiChang.getResp_code());
			tlmposRespnote.setResp_msg(TlmposExptMsgEnum.YingShouJinEYiChang.getResp_msg());
			logger.error("tlmpos运单撤销,撤销金额与原金额不一致,单号：" + tlmposRespnote.getOrder_no() + ",小件员：" + tlmposRespnote.getDelivery_man());
			return tlmposRespnote;
		}
		if (tlmposRespnote.getDeliverstate().getPos().doubleValue() == 0) {
			tlmposRespnote.setResp_code(TlmposExptMsgEnum.BuNengCheXiao.getResp_code());
			tlmposRespnote.setResp_msg(TlmposExptMsgEnum.BuNengCheXiao.getResp_msg());
			logger.error("tlmpos运单撤销,非POS刷卡支付不能撤销,单号：" + tlmposRespnote.getOrder_no() + ",小件员：" + tlmposRespnote.getDelivery_man());
			return tlmposRespnote;
		}
		return tlmposRespnote;
	}

	private Map<String, String> convertMapType_toBackOut(TlmposRespNote alipayRespNote, Tlmpos tlmpos, Transaction rootnote) {
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("transaction_id", alipayRespNote.getTransaction_id());
		retMap.put("resp_code", alipayRespNote.getResp_code());
		retMap.put("resp_msg", alipayRespNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		retMap.put("requester", tlmpos.getRequester());
		retMap.put("target", tlmpos.getTargeter());
		retMap.put("order_no", alipayRespNote.getOrder_no());

		// 生成待加密的字符串
		String str = TlmposXMLHandler.createMACXML_cwbSign(retMap);
		String r = CreateRespSign(tlmpos, str);
		retMap.put("MAC", r);
		return retMap;
	}

}
