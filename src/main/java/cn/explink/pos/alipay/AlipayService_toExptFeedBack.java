package cn.explink.pos.alipay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.ExptCodeJoint;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.alipay.xml.Transaction;
import cn.explink.pos.bill99.Bill99ExptMessageEnum;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class AlipayService_toExptFeedBack extends AlipayService {
	private Logger logger = LoggerFactory.getLogger(AlipayService_toExptFeedBack.class);

	/**
	 * 异常反馈通知接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toExceptionFeedBack(Transaction rootnote, AliPay alipay) {
		AlipayRespNote alipayRespNote = new AlipayRespNote();
		try {
			alipayRespNote = super.BuildAlipayRespClass(rootnote, alipayRespNote);
			long deliverystate = getDeliveryByReasonType(rootnote.getTransaction_Body().getEx_code());

			if (alipayRespNote.getCwbOrder() == null) {
				alipayRespNote.setResp_code(AliPayExptMessageEnum.ChaXunYiChang.getResp_code());
				alipayRespNote.setResp_msg(AliPayExptMessageEnum.ChaXunYiChang.getResp_msg());
				logger.error("alipay运单异常反馈,没有检索到数据" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man());
			} else if (alipayRespNote.getDeliverstate() != null && alipayRespNote.getDeliverstate().getDeliverystate() != 0) {
				alipayRespNote.setResp_code(AliPayExptMessageEnum.QiTaShiBai.getResp_code());
				alipayRespNote.setResp_msg(AliPayExptMessageEnum.QiTaShiBai.getResp_msg() + ",不可重复反馈");
				logger.error("alipay运单异常反馈,订单反馈过，不可重复反馈,单号：" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man());
			} else if (deliverystate == -1) {
				alipayRespNote.setResp_code(AliPayExptMessageEnum.QiTaShiBai.getResp_code());
				alipayRespNote.setResp_msg(AliPayExptMessageEnum.QiTaShiBai.getResp_msg() + "无法识别此异常码");
				logger.error("alipay异常反馈失败,无法识别此编码:[" + rootnote.getTransaction_Body().getEx_code() + "]，单号：" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man());
			} else {
				alipayRespNote.setEx_code(rootnote.getTransaction_Body().getEx_code());
				alipayRespNote.setEx_desc(rootnote.getTransaction_Body().getEx_desc());
				alipayRespNote = ExcuteCwbExptFeedBackHandler(alipayRespNote, deliverystate);
			}

		} catch (Exception e) {
			alipayRespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			alipayRespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
			logger.error("异常反馈-系统遇到不可预知的异常!异常原因:", e);
			e.printStackTrace();
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_toExptFeedBack(alipayRespNote, alipay);
		String responseXml = AlipayXMLHandler.createXMLMessage_toExptFeedBack(respMap);
		logger.info("返回alipay数据成功。业务编码={},返回XML={}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);

		return responseXml;
	}

	private AlipayRespNote ExcuteCwbExptFeedBackHandler(AlipayRespNote alipayRespNote, long delivery_state) {
		long deliverystate = delivery_state;
		long backreasonid = 0;
		long leavedreasonid = 0;
		ExptCodeJoint exptCodeJoint = exptcodeJointDAO.getExpMatchListByPosCode(alipayRespNote.getEx_code(), PosEnum.AliPay.getKey());
		if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {

			if (exptCodeJoint != null && exptCodeJoint.getReasonid() != 0) {
				leavedreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(alipayRespNote.getEx_code(), PosEnum.AliPay.getKey())).getReasonid();
			} else {
				leavedreasonid = Long.parseLong(alipayRespNote.getEx_code());
			}
		} else if (deliverystate == DeliveryStateEnum.JuShou.getValue()) {
			if (exptCodeJoint != null && exptCodeJoint.getReasonid() != 0) {
				backreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(alipayRespNote.getEx_code(), PosEnum.AliPay.getKey())).getReasonid();
			} else {
				backreasonid = Long.parseLong(alipayRespNote.getEx_code());
			}
		}

		try {
			alipayRespNote.setBranchid(userDAO.getUserByUsername(alipayRespNote.getDelivery_man()).getBranchid());

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", alipayRespNote.getDeliverid());
			parameters.put("podresultid", deliverystate);
			parameters.put("backreasonid", backreasonid);
			parameters.put("leavedreasonid", leavedreasonid);
			parameters.put("receivedfeecash", BigDecimal.ZERO);
			parameters.put("receivedfeepos", BigDecimal.ZERO);
			parameters.put("receivedfeecheque", BigDecimal.ZERO);
			parameters.put("receivedfeeother", BigDecimal.ZERO);
			parameters.put("paybackedfee", BigDecimal.ZERO);
			parameters.put("podremarkid", 0l);
			parameters.put("posremark", "");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", alipayRespNote.getEx_code());
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", alipayRespNote.getBranchid());
			parameters.put("sessionuserid", alipayRespNote.getDeliverid());
			parameters.put("sign_typeid", 0);
			parameters.put("sign_man", "");
			parameters.put("sign_time", "");
			cwbOrderService.deliverStatePod(getUser(alipayRespNote.getDeliverid()), alipayRespNote.getOrder_no(), alipayRespNote.getOrder_no(), parameters);
			alipayRespNote.setResp_code(Bill99ExptMessageEnum.Success.getResp_code());
			alipayRespNote.setResp_msg(Bill99ExptMessageEnum.Success.getResp_msg());
		} catch (CwbException e1) {
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(alipayRespNote.getOrder_no());
			User user = getUser(alipayRespNote.getDeliverid());
			exceptionCwbDAO.createExceptionCwbScan(alipayRespNote.getOrder_no(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "",alipayRespNote.getOrder_no());

			if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
				logger.error("bill99异常反馈异常,没有检索到数据" + alipayRespNote.getOrder_no() + ",小件员：" + alipayRespNote.getDelivery_man(), e1);
				alipayRespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
				alipayRespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());

			} else if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()) {
				alipayRespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
				alipayRespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());
				logger.error("bill99异常反馈异常,不是此小件员的货" + alipayRespNote.getOrder_no() + ",当前小件员：" + alipayRespNote.getDelivery_man() + e1);
			}
			e1.printStackTrace();
		}

		return alipayRespNote;
	}

	private Map<String, String> convertMapType_toExptFeedBack(AlipayRespNote alipayRespNote, AliPay alipay) {
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

	private int getDeliveryByReasonType(String code) {
		int deliverystate = -1;
		long reasontype = reasonDao.getReasonTypeByExptCode(code);
		if (reasontype == ReasonTypeEnum.BeHelpUp.getValue()) {
			deliverystate = DeliveryStateEnum.FenZhanZhiLiu.getValue();
		} else if (reasontype == ReasonTypeEnum.ReturnGoods.getValue()) {
			deliverystate = DeliveryStateEnum.JuShou.getValue();
		} else {
			ExptReason exptreason = exptReasonDAO.getExptReasonCodeByPos(code, PosEnum.AliPay.getKey());
			if (exptreason != null) {
				int expt_type = exptreason.getExpt_type();
				if (expt_type == ReasonTypeEnum.BeHelpUp.getValue()) {
					deliverystate = DeliveryStateEnum.FenZhanZhiLiu.getValue();
				} else if (expt_type == ReasonTypeEnum.ReturnGoods.getValue()) {
					deliverystate = DeliveryStateEnum.JuShou.getValue();
				}
			}
		}

		return deliverystate;
	}

}
