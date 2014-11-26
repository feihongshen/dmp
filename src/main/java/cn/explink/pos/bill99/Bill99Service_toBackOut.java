package cn.explink.pos.bill99;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.pos.bill99.xml.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.pos.CertificateCoderUtil;

@Service
public class Bill99Service_toBackOut extends Bill99Service {
	private Logger logger = LoggerFactory.getLogger(Bill99Service_toBackOut.class);

	/**
	 * 撤销交易接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toBackOut(Transaction rootnote, Bill99 bill99) {
		Bill99RespNote billrespNote = new Bill99RespNote();
		try {
			billrespNote = super.BuildBill99RespClass(rootnote, billrespNote);

			billrespNote = ValidateRequestBusiness_toReversal(billrespNote, rootnote); // 验证是否合格
			if (billrespNote.getResp_code() == null) { // 验证成功
				billrespNote = BExcuteReversalHandler(billrespNote, rootnote);
			}

		} catch (Exception e) {
			logger.error("Bill99运单撤销接口发生不可预知的异常:", e);
			billrespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_toBackOut(billrespNote, bill99, rootnote);
		String responseXml = Bill99XMLHandler.createXMLMessage_toBackOut(respMap);
		logger.info("返回bill99数据成功。业务编码={},返回XML={}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);

		return responseXml;
	}

	private Bill99RespNote BExcuteReversalHandler(Bill99RespNote billrespNote, Transaction rootnote) {
		try {
			String trackinfo = "运单撤销:单号=" + billrespNote.getOrder_no() + ",撤销金额=" + rootnote.getTransaction_Body().getVoid_amt() + ",撤销类型=" + rootnote.getTransaction_Body().getPay_type() + ";系统参考编码="
					+ rootnote.getTransaction_Body().getIdTxn() + ",终端号=" + rootnote.getTransaction_Body().getTerminal_id() + ",原凭证号=" + rootnote.getTransaction_Body().getVoid_trace_no();
			billrespNote.setTrackinfo(trackinfo);
			// 撤销
			cwbOrderService.deliverStatePodCancel(billrespNote.getOrder_no(), billrespNote.getBranchid(), billrespNote.getDeliverid(), trackinfo, 0);
			posPayDAO.save_PosTradeDetailRecord(billrespNote.getOrder_no(), trackinfo, 0, billrespNote.getDeliverid(), 1, trackinfo, "", 0, "", 1, 4, "single", PosEnum.Bill99.getMethod(), 0, "");

			billrespNote.setResp_code(Bill99ExptMessageEnum.Success.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.Success.getResp_msg());

			logger.info("bill99运单撤销成功,单号={},撤销金额={},小件员：" + billrespNote.getDelivery_man(), billrespNote.getOrder_no(), rootnote.getTransaction_Body().getVoid_amt());

		} catch (Exception e) {
			logger.error("bill99撤销更新数据库异常！小件员：" + billrespNote.getDelivery_man() + ",当前订单号：" + billrespNote.getOrder_no() + e);
			billrespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
		}
		return billrespNote;
	}

	private Bill99RespNote ValidateRequestBusiness_toReversal(Bill99RespNote billrespNote, Transaction rootnote) {
		if (billrespNote.getCwbOrder() == null) {
			billrespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());
			logger.error("bill99运单撤销失败,没有检索到数据" + billrespNote.getOrder_no() + ",小件员：" + billrespNote.getDelivery_man());
			return billrespNote;
		}
		if (billrespNote.getCwbOrder().getReceivablefee().doubleValue() != rootnote.getTransaction_Body().getVoid_amt()) {
			billrespNote.setResp_code(Bill99ExptMessageEnum.BuNengCheXiao.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.BuNengCheXiao.getResp_msg());
			logger.error("bill99运单撤销失败,撤销金额与原金额不一致,单号：" + billrespNote.getOrder_no() + ",小件员：" + billrespNote.getDelivery_man());
			return billrespNote;
		}
		if (billrespNote.getDeliverstate().getReceivedfee().doubleValue() == 0) {
			billrespNote.setResp_code(Bill99ExptMessageEnum.BuNengCheXiao.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.BuNengCheXiao.getResp_msg());
			logger.error("bill99运单撤销失败,该单子未支付不能撤销,单号：" + billrespNote.getOrder_no() + ",小件员：" + billrespNote.getDelivery_man());
			return billrespNote;
		}
		if (billrespNote.getDeliverstate().getPos().doubleValue() == 0) {
			billrespNote.setResp_code(Bill99ExptMessageEnum.BuNengCheXiao.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.BuNengCheXiao.getResp_msg());
			logger.error("bill99运单撤销失败,非Pos刷卡不能撤销,单号：" + billrespNote.getOrder_no() + ",小件员：" + billrespNote.getDelivery_man());
			return billrespNote;
		}
		return billrespNote;
	}

	private Map<String, String> convertMapType_toBackOut(Bill99RespNote billrespNote, Bill99 bill99, Transaction rootnote) {
		CertificateCoderUtil cfcu = new CertificateCoderUtil(bill99);

		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("transaction_sn", rootnote.getTransaction_Header().getTransaction_sn());
		retMap.put("transaction_id", rootnote.getTransaction_Header().getTransaction_id());
		retMap.put("resp_code", billrespNote.getResp_code());
		retMap.put("resp_msg", billrespNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		retMap.put("requester", bill99.getRequester());
		retMap.put("target", bill99.getTargeter());
		retMap.put("order_no", billrespNote.getOrder_no());
		retMap.put("version", bill99.getVersion());

		// 生成待加密的字符串
		String str = Bill99XMLHandler.createMACXML_payAmount(retMap);
		String MAC = Bill99RespSign(cfcu, str);
		retMap.put("MAC", MAC);
		return retMap;
	}

}
