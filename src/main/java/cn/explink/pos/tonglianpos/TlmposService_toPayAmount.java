package cn.explink.pos.tonglianpos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.exception.CwbException;

import cn.explink.pos.tonglianpos.xmldto.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class TlmposService_toPayAmount extends TlmposService {
	private Logger logger = LoggerFactory.getLogger(TlmposService_toPayAmount.class);

	/**
	 * 付款接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	@SuppressWarnings("finally")
	public String toPayAmountForPos(Transaction rootnote, Tlmpos tlmpos) {
		TlmposRespNote respNote = new TlmposRespNote();
		try {
			respNote = buildtlmposRespClass(rootnote, respNote); // 加载公用查询类
			respNote = ExtraParamsforPay(respNote, rootnote);
			if (respNote.getResp_code() == null) {
				respNote = ExcutePosPayMethod(respNote.getCwbOrder(), respNote, rootnote, tlmpos); // 更新支付交易数据
			}

		} catch (Exception e) {
			logger.error("tlmpos-POS支付发生不可预知的异常", e);
		} finally {
			// 生成返回的xml字符串
			Map<String, String> respMap = convertMapType_PayAmount(respNote, tlmpos, rootnote);
			String responseXml = TlmposXMLHandler.createXMLMessage_payAmount(respMap);
			logger.info("返回tlmpos数据成功![" + rootnote.getTransaction_Header().getTransaction_id() + "]返回XML:" + responseXml);
			return responseXml;
		}

	}

	/**
	 * 执行更新POS支付数据的方法
	 * 
	 * @return
	 * @throws Exception
	 */
	private TlmposRespNote ExcutePosPayMethod(CwbOrder cwbOrder, TlmposRespNote respNote, Transaction rootnote, Tlmpos tlmpos) throws Exception {

		try {
			double receivablefee_add = respNote.getDeliverstate().getReceivedfee().doubleValue() + rootnote.getTransaction_Body().getOrder_payable_amt(); // 追加
			if (respNote.getPay_type() == 0) {
				respNote.setResp_code(TlmposExptMsgEnum.Success.getResp_code());
				respNote.setResp_msg(TlmposExptMsgEnum.Success.getResp_msg());
				return respNote;
			} else {
				long deliverid = respNote.getDeliverstate().getDeliveryid();
				if (tlmpos.getIsotherdeliverupdate() == 1) { // 需要更新派送员
					deliverid = respNote.getDeliverid(); // 根据请求接口的登录工号
				}

				cwbOrderService.posPay(cwbOrder.getCwb(), BigDecimal.valueOf(receivablefee_add), cwbOrder.getReceivablefee(), respNote.getPay_type(), respNote.getPodremark(), respNote.getTrackinfo(),
						deliverid, respNote.getDeliverstate(), "single", 0);
				
				//leoliao 2016-02-03
				try{
					//获取派送员所属机构(站点)-leoliao 2016-02-03
					long deliverybranchid = 0;
					User deliverUser = getUser(deliverid);
					if(deliverUser != null){
						deliverybranchid = deliverUser.getBranchid();
					}
					
					//更新反馈表deliverybranchid字段值为派送员所属机构id
					if(deliverid > 0 && deliverybranchid > 0){
						deliveryStateDAO.updateDeliverybranchid(cwbOrder.getCwb(), deliverybranchid);
					}
				}catch(Exception ex){
					logger.error("TlmposService_toPayAmount deliverid={} Exception={}", deliverid, ex.getMessage());
					ex.printStackTrace(System.out);
				}
				//leoliao 2016-02-03 end
				
				posPayDAO.save_PosTradeDetailRecord(cwbOrder.getCwb(), respNote.getPodremark(), receivablefee_add, deliverid, respNote.getPay_type(), respNote.getTrackinfo(), "", 0, "", 1, 1,
						rootnote.getTransaction_Body().getAcq_type(), PosEnum.TongLianPos.getMethod(), 0, "");

				respNote.setResp_code(TlmposExptMsgEnum.Success.getResp_code());
				respNote.setResp_msg(TlmposExptMsgEnum.Success.getResp_msg());
				return respNote;
			}

		} catch (CwbException e1) {
			if (respNote.getPay_type() != 0) {
				long deliverid = respNote.getDeliverstate().getDeliveryid();
				if (tlmpos.getIsotherdeliverupdate() == 1) { // 需要更新派送员
					deliverid = respNote.getDeliverid(); // 根据请求接口的登录工号
				}
				User user = getUser(deliverid);
				exceptionCwbDAO.createExceptionCwbScan(cwbOrder.getCwb(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(), cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0,
						0, 0, "",cwbOrder.getCwb());
			}
			logger.error("tlmpos支付处理业务逻辑异常！小件员=" + respNote.getDelivery_man() + ",订单号=" + respNote.getOrder_no() + ",异常原因=" + e1.getMessage());
			return DealWithCatchCwbException(respNote, e1);

		}

	}

	/**
	 * 处理支付宝的异常业务逻辑,并转化为对象
	 * 
	 * @param billRespNote
	 * @param e1
	 * @return
	 */
	private TlmposRespNote DealWithCatchCwbException(TlmposRespNote respNote, CwbException e1) {
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
			respNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
			respNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());
			return respNote;
		}
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.DingDanYiZhiFu.getValue()) {
			respNote.setResp_code(TlmposExptMsgEnum.YiShouKuanWeiQianShou.getResp_code());
			respNote.setResp_msg(TlmposExptMsgEnum.YiShouKuanWeiQianShou.getResp_msg());
			return respNote;
		}
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.ZhiFuAmountExceiton.getValue()) {
			respNote.setResp_code(TlmposExptMsgEnum.YingShouJinEYiChang.getResp_code());
			respNote.setResp_msg(TlmposExptMsgEnum.YingShouJinEYiChang.getResp_msg());
			return respNote;
		}
		respNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
		respNote.setResp_msg(e1.getMessage());
		return respNote;
	}

	private TlmposRespNote ExtraParamsforPay(TlmposRespNote respNote, Transaction rootnote) {
		String terminal_id = rootnote.getTransaction_Body().getTerminal_id();
		String trace_no = rootnote.getTransaction_Body().getTrace_no();
		int pay_type = rootnote.getTransaction_Body().getPay_type();
		respNote.setTerminal_id(terminal_id);// 终端号
		respNote.setTrace_no(trace_no); // 凭证号
		respNote.setPay_type(pay_type); // 付款方式
		switch (respNote.getPay_type()) {
		case 1:
			respNote.setPay_type(PaytypeEnum.Xianjin.getValue());
			respNote.setPodremark(PaytypeEnum.Xianjin.getText());
			break;
		case 2:
			respNote.setPay_type(PaytypeEnum.Pos.getValue());
			respNote.setPodremark(PaytypeEnum.Pos.getText());
			break;
		case 3:
			respNote.setPay_type(PaytypeEnum.Pos.getValue());
			respNote.setPodremark(PaytypeEnum.Pos.getText());
			break;
		default:
			respNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
			respNote.setResp_msg("支付类型不匹配");
			break;

		}
		String podremark = respNote.getPodremark();
		String trackinfo = "tlmpos运单支付,订单号:" + respNote.getOrder_no() + ",支付方式:" + podremark + ",终端号：" + respNote.getTerminal_id() + ",凭证号：" + rootnote.getTransaction_Body().getTrace_no();

		respNote.setOrder_amt(rootnote.getTransaction_Body().getOrder_payable_amt());

		respNote.setPodremark(podremark);
		respNote.setTrackinfo(trackinfo);
		logger.info(trackinfo);
		return respNote;
	}

	private Map<String, String> convertMapType_PayAmount(TlmposRespNote respNote, Tlmpos tlmpos, Transaction rootnote) {
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("transaction_id", respNote.getTransaction_id());
		retMap.put("resp_code", respNote.getResp_code());
		retMap.put("resp_msg", respNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		retMap.put("requester", tlmpos.getRequester());
		retMap.put("target", tlmpos.getTargeter());
		retMap.put("order_no", respNote.getOrder_no());
		retMap.put("e_order_no", rootnote.getTransaction_Body().getTranscwb() == null ? "" : rootnote.getTransaction_Body().getTranscwb());
		// 生成待加密的字符串
		String str = TlmposXMLHandler.createMACXML_payAmount(retMap);
		String r = CreateRespSign(tlmpos, str);
		retMap.put("MAC", r);
		return retMap;
	}

}
