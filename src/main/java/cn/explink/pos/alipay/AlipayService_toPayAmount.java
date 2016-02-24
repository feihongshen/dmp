package cn.explink.pos.alipay;

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
import cn.explink.pos.alipay.xml.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class AlipayService_toPayAmount extends AlipayService {
	private Logger logger = LoggerFactory.getLogger(AlipayService_toPayAmount.class);

	/**
	 * 付款接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toPayAmountForPos(Transaction rootnote, AliPay alipay) {
		AlipayRespNote respNote = new AlipayRespNote();
		try {
			respNote = BuildAlipayRespClass(rootnote, respNote);
			respNote = ExtraParamsforPay(respNote, rootnote);
			if (respNote.getResp_code() == null) {
				respNote = ExcutePosPayMethod(respNote.getCwbOrder(), respNote, rootnote, alipay); // 更新支付交易数据
			}

		} catch (Exception e) {
			logger.error("alipay-POS支付发生不可预知的异常", e);
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_PayAmount(respNote, alipay);
		String responseXml = AlipayXMLHandler.createXMLMessage_payAmount(respMap);
		logger.info("返回alipay数据成功![" + rootnote.getTransaction_Header().getTransaction_id() + "]返回XML:" + responseXml);
		return responseXml;
	}

	/**
	 * 执行更新POS支付数据的方法
	 * 
	 * @return
	 * @throws Exception
	 */
	private AlipayRespNote ExcutePosPayMethod(CwbOrder cwbOrder, AlipayRespNote respNote, Transaction rootnote, AliPay alipay) throws Exception {

		try {
			double receivablefee_add = respNote.getDeliverstate().getReceivedfee().doubleValue() + rootnote.getTransaction_Body().getOrder_payable_amt(); // 追加
			if (respNote.getPay_type() == 0) {
				respNote.setResp_code(AliPayExptMessageEnum.Success.getResp_code());
				respNote.setResp_msg(AliPayExptMessageEnum.Success.getResp_msg());
				return respNote;
			} else {
				long deliverid = respNote.getDeliverstate().getDeliveryid();
				if (alipay.getIsotherdeliverupdate() == 1) { // 需要更新派送员
					deliverid = respNote.getDeliverid(); // 根据请求接口的登录工号
				}

				String acq_type = rootnote.getTransaction_Body().getAcq_type(); // 是否分单支付
																				// sigle,splie
				cwbOrderService.posPay(cwbOrder.getCwb(), BigDecimal.valueOf(receivablefee_add), cwbOrder.getReceivablefee(), rootnote.getTransaction_Body().getPay_type(), respNote.getPodremark(),
						respNote.getTrackinfo(), deliverid, respNote.getDeliverstate(), acq_type, 0);
				
				//leoliao 2016-02-03
				try{
					//获取派送员所属机构(站点)
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
					logger.error("AlipayService_toPayAmount deliverid={} Exception={}", deliverid, ex.getMessage());
					ex.printStackTrace(System.out);
				}
				//leoliao 2016-02-03 end
				
				posPayDAO.save_PosTradeDetailRecord(cwbOrder.getCwb(), respNote.getPodremark(), receivablefee_add, deliverid, respNote.getPay_type(), respNote.getTrackinfo(), "", 0, "", 1, 1,
						rootnote.getTransaction_Body().getAcq_type(), PosEnum.AliPay.getMethod(), 0, "");
				logger.info(respNote.getTrackinfo());
				respNote.setResp_code(AliPayExptMessageEnum.Success.getResp_code());
				respNote.setResp_msg(AliPayExptMessageEnum.Success.getResp_msg());
				return respNote;
			}

		} catch (CwbException e1) {
			if (respNote.getPay_type() != 0) {
				long deliverid = respNote.getDeliverstate().getDeliveryid();
				if (alipay.getIsotherdeliverupdate() == 1) { // 需要更新派送员
					deliverid = respNote.getDeliverid(); // 根据请求接口的登录工号
				}
				User user = getUser(deliverid);
				exceptionCwbDAO.createExceptionCwbScan(cwbOrder.getCwb(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(), cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0,
						0, 0, "",cwbOrder.getCwb());
			}
			logger.error("Alipay支付处理业务逻辑异常！小件员=" + respNote.getDelivery_man() + ",订单号=" + respNote.getOrder_no() + ",异常原因=" + e1.getMessage());
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
	private AlipayRespNote DealWithCatchCwbException(AlipayRespNote respNote, CwbException e1) {
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
			respNote.setResp_code(AliPayExptMessageEnum.ChaXunYiChang.getResp_code());
			respNote.setResp_msg(AliPayExptMessageEnum.ChaXunYiChang.getResp_msg());
			return respNote;
		}
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.DingDanYiZhiFu.getValue()) {
			respNote.setResp_code(AliPayExptMessageEnum.YiShouKuanWeiQianShou.getResp_code());
			respNote.setResp_msg(AliPayExptMessageEnum.YiShouKuanWeiQianShou.getResp_msg());
			return respNote;
		}
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.ZhiFuAmountExceiton.getValue()) {
			respNote.setResp_code(AliPayExptMessageEnum.YingShouJinEYiChang.getResp_code());
			respNote.setResp_msg(AliPayExptMessageEnum.YingShouJinEYiChang.getResp_msg());
			return respNote;
		}
		respNote.setResp_code(AliPayExptMessageEnum.QiTaShiBai.getResp_code());
		respNote.setResp_msg(AliPayExptMessageEnum.QiTaShiBai.getResp_msg());
		return respNote;
	}

	private AlipayRespNote ExtraParamsforPay(AlipayRespNote respNote, Transaction rootnote) {
		String terminal_id = rootnote.getTransaction_Body().getTerminal_id();
		String trace_no = rootnote.getTransaction_Body().getTrace_no();
		int pay_type = rootnote.getTransaction_Body().getPay_type();
		respNote.setTerminal_id(terminal_id);// 终端号
		respNote.setTrace_no(trace_no); // 凭证号
		respNote.setAlipay_Pay_type(pay_type); // 付款方式
		switch (respNote.getAlipay_Pay_type()) {
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
		case 4:
			respNote.setPay_type(0); // 无货款
			respNote.setPodremark("无货款");
			break;
		case 5:
			respNote.setPay_type(PaytypeEnum.Zhipiao.getValue());
			respNote.setPodremark(PaytypeEnum.Zhipiao.getText());
			break;
		default:
			respNote.setResp_code(AliPayExptMessageEnum.QiTaShiBai.getResp_code());
			respNote.setResp_msg("支付类型不匹配");
			break;

		}
		String podremark = respNote.getPodremark();
		String trackinfo = "alipay运单支付,订单号:" + respNote.getOrder_no() + ",支付方式:" + podremark + ",终端号：" + respNote.getTerminal_id() + ",凭证号：" + rootnote.getTransaction_Body().getTerminal_id();
		if ("split".equals(rootnote.getTransaction_Body().getAcq_type()) && rootnote.getTransaction_Body().getOrder_payable_amt() != 0) {
			trackinfo += ",收单模式：分单支付，当前分单金额" + rootnote.getTransaction_Body().getOrder_payable_amt();
			podremark += ",收单模式：分单支付。";
		} else {
			respNote.setOrder_amt(rootnote.getTransaction_Body().getOrder_payable_amt());
		}
		respNote.setPodremark(podremark);
		respNote.setTrackinfo(trackinfo);
		logger.info(trackinfo);
		return respNote;
	}

	/**
	 * 处理alipay支付的复杂逻辑,返回一个对象
	 * 
	 * @param jobject
	 * @param cwbOrder
	 * @return
	 */
	private AlipayRespNote ValidateRequestBusiness_PosPay(AlipayRespNote respNote, Transaction rootnote, CwbOrder cwbOrder) {
		if (cwbOrder == null) {
			respNote.setResp_code(AliPayExptMessageEnum.ChaXunYiChang.getResp_code());
			respNote.setResp_msg(AliPayExptMessageEnum.ChaXunYiChang.getResp_msg());
			logger.error("alipay运单支付,没有检索到数据" + respNote.getOrder_no() + ",小件员：" + respNote.getDelivery_man());
			return respNote;
		}
		if (respNote.getDeliverstate().getReceivedfee().compareTo(respNote.getCwbOrder().getReceivablefee()) == 0 && rootnote.getTransaction_Body().getOrder_payable_amt() > 0) {
			respNote.setResp_code(AliPayExptMessageEnum.JiaoYiChongFu.getResp_code());
			respNote.setResp_msg(AliPayExptMessageEnum.JiaoYiChongFu.getResp_msg());
			logger.error("[运单支付]该订单已支付,当前订单号：" + cwbOrder.getCwb() + ",小件员：" + respNote.getDelivery_man());
			return respNote;
		}
		if (respNote.getDeliverstate().getBusinessfee().doubleValue() < rootnote.getTransaction_Body().getOrder_payable_amt()) {
			respNote.setResp_code(AliPayExptMessageEnum.YingShouJinEYiChang.getResp_code());
			respNote.setResp_msg(AliPayExptMessageEnum.YingShouJinEYiChang.getResp_msg());
			logger.error("[运单支付]支付金额有误,当前订单号：" + respNote.getOrder_no() + ",小件员：" + respNote.getDelivery_man());
			return respNote;
		}
		if (!"split".equals(rootnote.getTransaction_Body().getAcq_type()) && respNote.getCwbOrder().getReceivablefee().doubleValue() != rootnote.getTransaction_Body().getOrder_payable_amt()) {
			respNote.setResp_code(AliPayExptMessageEnum.YingShouJinEYiChang.getResp_code());
			respNote.setResp_msg(AliPayExptMessageEnum.YingShouJinEYiChang.getResp_msg());
			logger.error("[运单支付]支付金额有误,当前订单号：" + respNote.getOrder_no() + ",小件员：" + respNote.getDelivery_man());
			return respNote;
		}

		return respNote;
	}

	private Map<String, String> convertMapType_PayAmount(AlipayRespNote respNote, AliPay alipay) {
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("transaction_id", respNote.getTransaction_id());
		retMap.put("resp_code", respNote.getResp_code());
		retMap.put("resp_msg", respNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime());
		retMap.put("requester", alipay.getRequester());
		retMap.put("target", alipay.getTargeter());
		retMap.put("order_no", respNote.getOrder_no());
		// 生成待加密的字符串
		String str = AlipayXMLHandler.createMACXML_payAmount(retMap);
		String r = CreateRespSign(alipay, str);
		retMap.put("MAC", r);
		return retMap;
	}

}
