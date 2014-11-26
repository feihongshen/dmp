package cn.explink.pos.tonglianpos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tonglianpos.xmldto.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class TlmposService_toCwbSign extends TlmposService {
	private Logger logger = LoggerFactory.getLogger(TlmposService_toCwbSign.class);

	/**
	 * 签收通知接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toCwbSign(Transaction rootnote, Tlmpos tlmpos) {
		TlmposRespNote mposRespnote = new TlmposRespNote();
		try {
			mposRespnote = super.buildtlmposRespClass(rootnote, mposRespnote);
			mposRespnote = ValidateRequestBusiness_cwbSign(mposRespnote); // 验证签收前提条件
			if (mposRespnote.getResp_code() == null) { // 验证通过，resp_code没有被赋值。

				String Consignee_sign_flag = rootnote.getTransaction_Body().getConsignee_sign_flag();
				if (Consignee_sign_flag.equalsIgnoreCase("N")) {
					mposRespnote.setSign_type(SignTypeEnum.TaRenDaiQianShou.getValue());
					mposRespnote.setSign_remark(SignTypeEnum.TaRenDaiQianShou.getSign_text());
				} else {
					mposRespnote.setSign_type(SignTypeEnum.BenRenQianShou.getValue());
					mposRespnote.setSign_remark(SignTypeEnum.BenRenQianShou.getSign_text());
				}

				mposRespnote = ExcuteCwbSignHandler(mposRespnote, rootnote, tlmpos);
			}

		} catch (Exception e) {
			mposRespnote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
			mposRespnote.setResp_msg(e.getMessage());
			logger.error("运单签收-未知异常", e);

		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_cwbSign(mposRespnote, tlmpos, rootnote);
		String responseXml = TlmposXMLHandler.createXMLMessage_cwbSign(respMap);
		logger.info("返回tlmpos数据成功。业务编码：{},返回XML:{}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);

		return responseXml;
	}

	/**
	 * 验证签收的前提条件
	 * 
	 * @param tlmposRespNote
	 * @return
	 */
	private TlmposRespNote ValidateRequestBusiness_cwbSign(TlmposRespNote tlmposRespNote) {
		if (tlmposRespNote.getCwbOrder() == null) {
			tlmposRespNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
			tlmposRespNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());
			logger.error("tlmpos运单签收,没有检索到数据" + tlmposRespNote.getOrder_no() + ",小件员：" + tlmposRespNote.getDelivery_man());
			return tlmposRespNote;
		}

		if (tlmposRespNote.getDeliverstate().getSign_typeid() == 1) {
			tlmposRespNote.setResp_code(TlmposExptMsgEnum.DingDanYiQianShou.getResp_code());
			tlmposRespNote.setResp_msg(TlmposExptMsgEnum.DingDanYiQianShou.getResp_msg());
			logger.error("tlmpos运单签收,订单已签收，不可重复签收,单号：" + tlmposRespNote.getOrder_no() + ",小件员：" + tlmposRespNote.getDelivery_man());
			return tlmposRespNote;
		}

		if (tlmposRespNote.getCwbOrder().getReceivablefee().compareTo(BigDecimal.ZERO) > 0 && tlmposRespNote.getDeliverstate().getReceivedfee().compareTo(BigDecimal.ZERO) == 0) {
			tlmposRespNote.setResp_code(TlmposExptMsgEnum.QianShouBuChuLi.getResp_code());
			tlmposRespNote.setResp_msg("订单未支付不可签收");
			logger.error("tlmpos运单签收,订单未支付，不可签收,单号：" + tlmposRespNote.getOrder_no() + ",小件员：" + tlmposRespNote.getDelivery_man());
			return tlmposRespNote;
		}
		return tlmposRespNote;
	}

	/**
	 * 处理签收的方法
	 * 
	 * @param tlmposRespNote
	 * @return
	 */
	private TlmposRespNote ExcuteCwbSignHandler(TlmposRespNote tlmposRespNote, Transaction rootnote, Tlmpos tlmpos) {
		try {

			BigDecimal totalAmount = tlmposRespNote.getDeliverstate().getPos().add(tlmposRespNote.getDeliverstate().getCash()).add(tlmposRespNote.getDeliverstate().getCheckfee())
					.add(tlmposRespNote.getDeliverstate().getOtherfee());
			BigDecimal pos = tlmposRespNote.getDeliverstate().getPos();
			BigDecimal Cash = tlmposRespNote.getDeliverstate().getCash();
			BigDecimal paybackedfee = tlmposRespNote.getDeliverstate().getReturnedfee();
			BigDecimal Businessfee = tlmposRespNote.getDeliverstate().getBusinessfee();
			if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {

				if (tlmposRespNote.getDeliverstate().getIsout() == 1) { // 应退款
					pos = BigDecimal.ZERO;
					Cash = BigDecimal.ZERO;
					paybackedfee = tlmposRespNote.getDeliverstate().getBusinessfee();
				} else if (Businessfee.compareTo(BigDecimal.ZERO) > 0) { // 说明应收款大于0，且支付接口没有执行完毕，返回其他异常，等待支付处理完毕，pos重发签收接口。
					tlmposRespNote.setResp_code(TlmposExptMsgEnum.QianShouBuChuLi.getResp_code());
					tlmposRespNote.setResp_msg("未支付订单不可签收");
					logger.info("未支付订单不可签收cwb={}", tlmposRespNote.getOrder_no());
					return tlmposRespNote;
				}

			}

			long deliverid = tlmposRespNote.getDeliverstate().getDeliveryid();
			if (tlmpos.getIsotherdeliverupdate() == 1) { // 需要更新派送员
				deliverid = tlmposRespNote.getDeliverid(); // 根据请求接口的登录工号
			}
			DeliveryState deryState = tlmposRespNote.getDeliverstate();

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", deliverid);
			parameters.put("podresultid", getPodResultIdByCwb(tlmposRespNote.getCwbOrder().getCwbordertypeid()));
			parameters.put("backreasonid", (long) 0);
			parameters.put("leavedreasonid", (long) 0);
			parameters.put("receivedfeecash", Cash);
			parameters.put("receivedfeepos", pos);
			parameters.put("receivedfeecheque", tlmposRespNote.getDeliverstate().getCheckfee());
			parameters.put("receivedfeeother", BigDecimal.ZERO);
			parameters.put("paybackedfee", paybackedfee);
			parameters.put("podremarkid", (long) 0);
			parameters.put("posremark", deryState.getPosremark() + "POS反馈");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", "POS签收成功");
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", tlmposRespNote.getBranchid());
			parameters.put("sessionuserid", deliverid);
			parameters.put("sign_typeid", 1); // 是否 已签收 （0，未签收，1已签收）
			parameters.put("sign_man", rootnote.getTransaction_Body().getSignee());
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			parameters.put("nosysyemflag", "1");//

			cwbOrderService.deliverStatePod(getUser(tlmposRespNote.getDeliverid()), tlmposRespNote.getOrder_no(), tlmposRespNote.getOrder_no(), parameters);

			deliveryStateDAO.updateOperatorIdByCwb(deliverid, tlmposRespNote.getOrder_no());

			String sign_remark = "tlmpos运单签收，单号" + tlmposRespNote.getOrder_no() + ",签收类型：" + tlmposRespNote.getSign_remark() + ",小件员:" + tlmposRespNote.getDelivery_man();
			tlmposRespNote.setSign_remark(sign_remark);
			posPayDAO.save_PosTradeDetailRecord(tlmposRespNote.getOrder_no(), "", tlmposRespNote.getCwbOrder().getReceivablefee().doubleValue(), 0, 0, "", tlmposRespNote.getCwbOrder()
					.getConsigneename(), tlmposRespNote.getSign_type(), tlmposRespNote.getSign_remark(), 2, 1, "", PosEnum.TongLianPos.getMethod(), 0, "");
			logger.info(sign_remark);
			tlmposRespNote.setResp_code(TlmposExptMsgEnum.Success.getResp_code());
			tlmposRespNote.setResp_msg(TlmposExptMsgEnum.Success.getResp_msg());
		} catch (CwbException e1) {
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(tlmposRespNote.getOrder_no());
			User user = getUser(tlmposRespNote.getDeliverid());
			exceptionCwbDAO.createExceptionCwb(tlmposRespNote.getOrder_no(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "");

			if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
				logger.error("tlmpos运单签收,没有检索到数据" + tlmposRespNote.getOrder_no() + ",小件员：" + tlmposRespNote.getDelivery_man(), e1);
				tlmposRespNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
				tlmposRespNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());

			} else if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()) {
				tlmposRespNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
				tlmposRespNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());
				logger.error("tlmpos运单签收,不是此小件员的货" + tlmposRespNote.getOrder_no() + ",当前小件员：" + tlmposRespNote.getDelivery_man() + e1);
			}
		}
		return tlmposRespNote;
	}

	private Map<String, String> convertMapType_cwbSign(TlmposRespNote tlmposRespNote, Tlmpos tlmpos, Transaction rootnote) {
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("transaction_id", tlmposRespNote.getTransaction_id());
		retMap.put("resp_code", tlmposRespNote.getResp_code());
		retMap.put("resp_msg", tlmposRespNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		retMap.put("requester", tlmpos.getRequester());
		retMap.put("target", tlmpos.getTargeter());
		retMap.put("order_no", tlmposRespNote.getOrder_no());
		retMap.put("e_order_no", rootnote.getTransaction_Body().getTranscwb() == null ? "" : rootnote.getTransaction_Body().getTranscwb());

		// 生成待加密的字符串
		String str = TlmposXMLHandler.createMACXML_cwbSign(retMap);
		String r = CreateRespSign(tlmpos, str);
		retMap.put("MAC", r);
		return retMap;
	}

	public long getPodResultIdByCwb(int cwbordertypeid) {
		if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
			return DeliveryStateEnum.ShangMenHuanChengGong.getValue();
		} else if (cwbordertypeid == (CwbOrderTypeIdEnum.Shangmentui.getValue())) {
			return DeliveryStateEnum.ShangMenTuiChengGong.getValue();
		} else {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}
	}

}
