package cn.explink.pos.alipay;

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
import cn.explink.pos.alipay.xml.Transaction;
import cn.explink.pos.bill99.Bill99ExptMessageEnum;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class AlipayService_toCwbSign extends AlipayService {
	private Logger logger = LoggerFactory.getLogger(AlipayService_toCwbSign.class);

	/**
	 * 签收通知接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toCwbSign(Transaction rootnote, AliPay alipay) {
		AlipayRespNote alipayRespNote = new AlipayRespNote();
		try {
			alipayRespNote = super.BuildAlipayRespClass(rootnote, alipayRespNote);
			alipayRespNote = ValidateRequestBusiness_cwbSign(alipayRespNote); // 验证签收前提条件
			if (alipayRespNote.getResp_code() == null) { // 验证通过，resp_code没有被赋值。

				String Consignee_sign_flag = rootnote.getTransaction_Body().getConsignee_sign_flag();
				if (Consignee_sign_flag.equalsIgnoreCase("N")) {
					alipayRespNote.setSign_type(SignTypeEnum.TaRenDaiQianShou.getValue());
					alipayRespNote.setSign_remark(SignTypeEnum.TaRenDaiQianShou.getSign_text());
				} else {
					alipayRespNote.setSign_type(SignTypeEnum.BenRenQianShou.getValue());
					alipayRespNote.setSign_remark(SignTypeEnum.BenRenQianShou.getSign_text());
				}

				alipayRespNote = ExcuteCwbSignHandler(alipayRespNote, rootnote, alipay);
			}

		} catch (Exception e) {
			alipayRespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			alipayRespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
			logger.error("系统遇到不可预知的异常!异常原因:", e);
			e.printStackTrace();
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_cwbSign(alipayRespNote, alipay);
		String responseXml = AlipayXMLHandler.createXMLMessage_cwbSign(respMap);
		logger.info("返回alipay数据成功。业务编码：{},返回XML:{}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);

		return responseXml;
	}

	/**
	 * 验证签收的前提条件
	 * 
	 * @param alipayRespNote
	 * @return
	 */
	private AlipayRespNote ValidateRequestBusiness_cwbSign(AlipayRespNote alipayRespNote) {
		if (alipayRespNote.getCwbOrder() == null) {
			alipayRespNote.setResp_code(AliPayExptMessageEnum.ChaXunYiChang.getResp_code());
			alipayRespNote.setResp_msg(AliPayExptMessageEnum.ChaXunYiChang.getResp_msg());
			logger.error("alipay运单签收,没有检索到数据" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man());
			return alipayRespNote;
		}

		if (alipayRespNote.getDeliverstate().getSign_typeid() == 1) {
			alipayRespNote.setResp_code(AliPayExptMessageEnum.DingDanYiQianShou.getResp_code());
			alipayRespNote.setResp_msg(AliPayExptMessageEnum.DingDanYiQianShou.getResp_msg());
			logger.error("alipay运单签收,订单已签收，不可重复签收,单号：" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man());
			return alipayRespNote;
		}

		if (alipayRespNote.getCwbOrder().getReceivablefee().compareTo(BigDecimal.ZERO) > 0 && alipayRespNote.getDeliverstate().getReceivedfee().compareTo(BigDecimal.ZERO) == 0) {
			alipayRespNote.setResp_code(AliPayExptMessageEnum.QiTaShiBai.getResp_code());
			alipayRespNote.setResp_msg("订单未支付不可签收");
			logger.error("alipay运单签收,订单未支付，不可签收,单号：" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man());
			return alipayRespNote;
		}
		return alipayRespNote;
	}

	/**
	 * 处理签收的方法
	 * 
	 * @param alipayRespNote
	 * @return
	 */
	private AlipayRespNote ExcuteCwbSignHandler(AlipayRespNote alipayRespNote, Transaction rootnote, AliPay alipay) {
		try {

			BigDecimal totalAmount = alipayRespNote.getDeliverstate().getPos().add(alipayRespNote.getDeliverstate().getCash()).add(alipayRespNote.getDeliverstate().getCheckfee())
					.add(alipayRespNote.getDeliverstate().getOtherfee());
			BigDecimal pos = alipayRespNote.getDeliverstate().getPos();
			BigDecimal Cash = alipayRespNote.getDeliverstate().getCash();
			BigDecimal paybackedfee = alipayRespNote.getDeliverstate().getReturnedfee();
			BigDecimal Businessfee = alipayRespNote.getDeliverstate().getBusinessfee();
			if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {

				if (alipayRespNote.getDeliverstate().getIsout() == 1) { // 应退款
					pos = BigDecimal.ZERO;
					Cash = BigDecimal.ZERO;
					paybackedfee = alipayRespNote.getDeliverstate().getBusinessfee();
				} else if (Businessfee.compareTo(BigDecimal.ZERO) > 0) { // 说明应收款大于0，且支付接口没有执行完毕，返回其他异常，等待支付处理完毕，pos重发签收接口。
					alipayRespNote.setResp_code(AliPayExptMessageEnum.QiTaShiBai.getResp_code());
					alipayRespNote.setResp_msg("未支付订单不可签收");
					logger.info("未支付订单不可签收cwb={}", alipayRespNote.getOrder_no());
					return alipayRespNote;
				}

			}

			long deliverid = alipayRespNote.getDeliverstate().getDeliveryid();
			if (alipay.getIsotherdeliverupdate() == 1) { // 需要更新派送员
				deliverid = alipayRespNote.getDeliverid(); // 根据请求接口的登录工号
			}
			DeliveryState deryState = alipayRespNote.getDeliverstate();

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", deliverid);
			parameters.put("podresultid", getPodResultIdByCwb(alipayRespNote.getCwbOrder().getCwbordertypeid()));
			parameters.put("backreasonid", (long) 0);
			parameters.put("leavedreasonid", (long) 0);
			parameters.put("receivedfeecash", Cash);
			parameters.put("receivedfeepos", pos);
			parameters.put("receivedfeecheque", alipayRespNote.getDeliverstate().getCheckfee());
			parameters.put("receivedfeeother", BigDecimal.ZERO);
			parameters.put("paybackedfee", paybackedfee);
			parameters.put("podremarkid", (long) 0);
			parameters.put("posremark", deryState.getPosremark() + "POS反馈");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", "POS签收成功");
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", alipayRespNote.getBranchid());
			parameters.put("sessionuserid", deliverid);
			parameters.put("sign_typeid", 1); // 是否 已签收 （0，未签收，1已签收）
			parameters.put("sign_man", rootnote.getTransaction_Body().getSignee());
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			parameters.put("nosysyemflag", "1");//
			// cwbOrderService.deliverStatePod(getUser(deliverid),alipayRespNote.getOrder_no(),parameters);
			cwbOrderService.deliverStatePod(getUser(alipayRespNote.getDeliverid()), alipayRespNote.getOrder_no(), alipayRespNote.getOrder_no(), parameters);

			deliveryStateDAO.updateOperatorIdByCwb(deliverid, alipayRespNote.getOrder_no());

			String sign_remark = "Alipay运单签收，单号" + alipayRespNote.getOrder_no() + ",签收类型：" + alipayRespNote.getSign_remark() + ",小件员:" + alipayRespNote.getDelivery_man();
			alipayRespNote.setSign_remark(sign_remark);
			posPayDAO.save_PosTradeDetailRecord(alipayRespNote.getOrder_no(), "", alipayRespNote.getCwbOrder().getReceivablefee().doubleValue(), 0, 0, "", alipayRespNote.getCwbOrder()
					.getConsigneename(), alipayRespNote.getSign_type(), alipayRespNote.getSign_remark(), 2, 1, "", PosEnum.AliPay.getMethod(), 0, "");
			logger.info(sign_remark);
			alipayRespNote.setResp_code(AliPayExptMessageEnum.Success.getResp_code());
			alipayRespNote.setResp_msg(AliPayExptMessageEnum.Success.getResp_msg());
		} catch (CwbException e1) {
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(alipayRespNote.getOrder_no());
			User user = getUser(alipayRespNote.getDeliverid());
			exceptionCwbDAO.createExceptionCwbScan(alipayRespNote.getOrder_no(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "",alipayRespNote.getOrder_no());

			if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
				logger.error("Alipay运单签收,没有检索到数据" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man(), e1);
				alipayRespNote.setResp_code(AliPayExptMessageEnum.ChaXunYiChang.getResp_code());
				alipayRespNote.setResp_msg(AliPayExptMessageEnum.ChaXunYiChang.getResp_msg());

			} else if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()) {
				alipayRespNote.setResp_code(AliPayExptMessageEnum.ChaXunYiChang.getResp_code());
				alipayRespNote.setResp_msg(AliPayExptMessageEnum.ChaXunYiChang.getResp_msg());
				logger.error("Alipay运单签收,不是此小件员的货" + alipayRespNote.getOrder_no() + ",当前小件员：" + alipayRespNote.getDelivery_man() + e1);
			}
		}
		return alipayRespNote;
	}

	private Map<String, String> convertMapType_cwbSign(AlipayRespNote alipayRespNote, AliPay alipay) {
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

	public long getPodResultIdByCwb(int cwbordertypeid) {
		if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
			return DeliveryStateEnum.ShangMenHuanChengGong.getValue();
		} else if (cwbordertypeid == (CwbOrderTypeIdEnum.Shangmentui.getValue())) {
			return DeliveryStateEnum.ShangMenTuiChengGong.getValue();
		} else {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}
	}

	public void ValidateBusinessLogic_Alipay(String cwb, CwbOrder cwbOrder, DeliveryState ds, String delivery_man, BigDecimal order_amt, String acq_type) {
		if (cwbOrder.getReceivablefee() == ds.getPos()) {
			logger.error("[运单支付]该订单已支付,当前订单号：" + cwb + ",小件员：" + delivery_man);
			throw new AlipayException(AliPayExptMessageEnum.JiaoYiChongFu);
		} else if (cwbOrder.getReceivablefee().compareTo(order_amt) > -1) {
			logger.error("[运单支付]支付金额有误,当前订单号：" + cwb + "异常金额:" + order_amt + ",小件员：" + delivery_man);
			throw new AlipayException(AliPayExptMessageEnum.YingShouJinEYiChang);
		} else if (!"split".equals(acq_type) && cwbOrder.getReceivablefee() != order_amt) {
			logger.error("[运单支付]支付金额有误,单笔支付,当前订单号：" + cwb + ",小件员：" + delivery_man);
			throw new AlipayException(AliPayExptMessageEnum.YingShouJinEYiChang);
		}
	}

}
