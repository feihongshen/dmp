package cn.explink.pos.bill99;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.bill99.xml.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.pos.CertificateCoderUtil;

@Service
public class Bill99Service_toReverseDept extends Bill99Service {
	private Logger logger = LoggerFactory.getLogger(Bill99Service_toReverseDept.class);

	/**
	 * 冲正接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toReverseDept(Transaction rootnote, Bill99 bill99) {

		Bill99RespNote billrespNote = new Bill99RespNote();
		try {
			billrespNote = super.BuildBill99RespClass(rootnote, billrespNote);

			billrespNote.setPodremark("冲正交易");
			billrespNote = ValidateRequestBusiness_toReversal(billrespNote, rootnote);
			if (billrespNote.getResp_code() == null) {
				billrespNote = ExcuteReversalHandler(billrespNote, rootnote);
			}

		} catch (Exception e) {
			logger.error("Bill99运单冲正接口发生不可预知的异常:", e);
			billrespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_PayAmount(billrespNote, bill99, rootnote);
		String responseXml = Bill99XMLHandler.createXMLMessage_payAmount(respMap);
		logger.info("返回bill99数据-业务编码={},返回XML={}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);

		return responseXml;
	}

	private Bill99RespNote ExcuteReversalHandler(Bill99RespNote billrespNote, Transaction rootnote) {
		switch (rootnote.getTransaction_Body().getPay_type()) {
		case 01:
			billrespNote.setSystem_pay_type(PaytypeEnum.Xianjin.getValue());
			billrespNote.setPodremark(PaytypeEnum.Xianjin.getText());
		case 02:
			billrespNote.setSystem_pay_type(PaytypeEnum.Pos.getValue());
			billrespNote.setPodremark(PaytypeEnum.Pos.getText());
		}
		String trackinfo = "bill99运单冲正,冲正支付方式=" + rootnote.getTransaction_Body().getPay_type() + ";终端号=" + rootnote.getTransaction_Body().getTerminal_id() + ";原凭证号:"
				+ rootnote.getTransaction_Body().getReverse_trace_no() + ";系统参考编号：" + rootnote.getTransaction_Body().getIdTxn() + ";冲正类型：[" + rootnote.getTransaction_Body().getReverse_tran_type()
				+ "];冲正金额：" + rootnote.getTransaction_Body().getReverse_amt();
		try {
			cwbOrderService.deliverStatePodCancel(billrespNote.getOrder_no(), billrespNote.getBranchid(), billrespNote.getDeliverid(), trackinfo, 0);
			posPayDAO.save_PosTradeDetailRecord(billrespNote.getOrder_no(), billrespNote.getPodremark(), rootnote.getTransaction_Body().getReverse_amt(), billrespNote.getDeliverid(),
					billrespNote.getSystem_pay_type(), trackinfo, "", 0, "", 1, 2, "single", PosEnum.Bill99.getMethod(), 0, "");

			billrespNote.setResp_code(Bill99ExptMessageEnum.Success.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.Success.getResp_msg());
			logger.info("运单冲正:单号={},冲正详情={}", billrespNote.getOrder_no(), trackinfo);
		} catch (Exception e) {
			logger.error("bill99冲正交易更新数据库异常！小件员：" + billrespNote.getDelivery_man() + ",当前订单号：" + billrespNote.getOrder_no() + e);
			billrespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
		}
		return billrespNote;
	}

	private Bill99RespNote ValidateRequestBusiness_toReversal(Bill99RespNote billrespNote, Transaction rootnote) {
		if (billrespNote.getCwbOrder() == null) {
			billrespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());
			logger.error("bill99[运单冲正],没有检索到数据" + billrespNote.getOrder_no() + ",小件员：" + billrespNote.getDelivery_man());
			return billrespNote;
		}
		// if(billrespNote.getDeliverstate().getSign_typeid()==1){
		// billrespNote.setResp_code(Bill99ExptMessageEnum.DingDanYiQianShou.getResp_code());
		// billrespNote.setResp_msg(Bill99ExptMessageEnum.DingDanYiQianShou.getResp_msg());
		// logger.error("[运单冲正]该订单已签收，不能冲正,当前订单号："+billrespNote.getOrder_no()+",小件员："+billrespNote.getDelivery_man());
		// return billrespNote;
		// }
		return billrespNote;
	}

	private Map<String, String> convertMapType_PayAmount(Bill99RespNote billrespNote, Bill99 bill99, Transaction rootnote) {
		CertificateCoderUtil cfcu = new CertificateCoderUtil(bill99);
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("version", bill99.getVersion());
		retMap.put("transaction_sn", rootnote.getTransaction_Header().getTransaction_sn());
		retMap.put("transaction_id", rootnote.getTransaction_Header().getTransaction_id());
		retMap.put("resp_code", billrespNote.getResp_code());
		retMap.put("resp_msg", billrespNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		retMap.put("requester", bill99.getRequester());
		retMap.put("target", bill99.getTargeter());
		retMap.put("order_no", billrespNote.getOrder_no());
		// 生成待加密的字符串
		String str = Bill99XMLHandler.createMACXML_payAmount(retMap);
		String MAC = Bill99RespSign(cfcu, str);
		retMap.put("MAC", MAC);
		return retMap;
	}

}
