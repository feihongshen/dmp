package cn.explink.pos.tonglianpos;

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
import cn.explink.pos.tonglianpos.xmldto.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class TlmposService_toExptFeedBack extends TlmposService {
	private Logger logger = LoggerFactory.getLogger(TlmposService_toExptFeedBack.class);

	/**
	 * 异常反馈通知接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toExceptionFeedBack(Transaction rootnote, Tlmpos tlmpos) {
		TlmposRespNote respNote = new TlmposRespNote();
		try {
			respNote = super.buildtlmposRespClass(rootnote, respNote);
			long deliverystate = getDeliveryByReasonType(rootnote.getTransaction_Body().getEx_code());

			if (respNote.getCwbOrder() == null) {
				respNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
				respNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());
				logger.error("tlmpos运单异常反馈,没有检索到数据" + respNote.getOrder_no() + ",小件员：" + respNote.getDelivery_man());
			} else if (respNote.getDeliverstate() != null && respNote.getDeliverstate().getDeliverystate() != 0) {
				respNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
				respNote.setResp_msg(TlmposExptMsgEnum.QiTaShiBai.getResp_msg() + ",不可重复反馈");
				logger.error("tlmpos运单异常反馈,订单反馈过，不可重复反馈,单号：" + respNote.getOrder_no() + ",小件员：" + respNote.getDelivery_man());
			} else if (deliverystate == -1) {
				respNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
				respNote.setResp_msg(TlmposExptMsgEnum.QiTaShiBai.getResp_msg() + "无法识别此异常码");
				logger.error("tlmpos异常反馈失败,无法识别此编码:[" + rootnote.getTransaction_Body().getEx_code() + "]，单号：" + respNote.getOrder_no() + ",小件员：" + respNote.getDelivery_man());
			} else {
				respNote.setEx_code(rootnote.getTransaction_Body().getEx_code());
				respNote.setEx_desc(rootnote.getTransaction_Body().getEx_desc());
				respNote = ExcuteCwbExptFeedBackHandler(respNote, deliverystate);
			}

		} catch (Exception e) {
			respNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
			respNote.setResp_msg(TlmposExptMsgEnum.QiTaShiBai.getResp_msg());
			logger.error("异常反馈-系统遇到不可预知的异常!异常原因:", e);

		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_toExptFeedBack(respNote, tlmpos);
		String responseXml = TlmposXMLHandler.createXMLMessage_toExptFeedBack(respMap);
		logger.info("返回tlmpos数据成功。业务编码={},返回XML={}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);

		return responseXml;
	}

	private TlmposRespNote ExcuteCwbExptFeedBackHandler(TlmposRespNote tlmposRespNote, long delivery_state) {
		long deliverystate = delivery_state;
		long backreasonid = 0;
		long leavedreasonid = 0;
		long firstlevelreasonid=0;
		ExptCodeJoint exptCodeJoint = exptcodeJointDAO.getExpMatchListByPosCode(tlmposRespNote.getEx_code(), PosEnum.TongLianPos.getKey());
		if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {

			if (exptCodeJoint != null && exptCodeJoint.getReasonid() != 0) {
				leavedreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(tlmposRespNote.getEx_code(), PosEnum.TongLianPos.getKey())).getReasonid();
			} else {
				leavedreasonid = Long.parseLong(tlmposRespNote.getEx_code());
			}
			
			try {
				firstlevelreasonid=this.reasonDao.getReasonByReasonid(leavedreasonid).getParentid();
			} catch (Exception e) {}
			
		} else if (deliverystate == DeliveryStateEnum.JuShou.getValue()) {
			if (exptCodeJoint != null && exptCodeJoint.getReasonid() != 0) {
				backreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(tlmposRespNote.getEx_code(), PosEnum.TongLianPos.getKey())).getReasonid();
			} else {
				backreasonid = Long.parseLong(tlmposRespNote.getEx_code());
			}
		}

		try {
			tlmposRespNote.setBranchid(userDAO.getUserByUsername(tlmposRespNote.getDelivery_man()).getBranchid());

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", tlmposRespNote.getDeliverid());
			parameters.put("podresultid", deliverystate);
			parameters.put("backreasonid", backreasonid);
			parameters.put("leavedreasonid", leavedreasonid);
			parameters.put("firstlevelreasonid", firstlevelreasonid);
			parameters.put("receivedfeecash", BigDecimal.ZERO);
			parameters.put("receivedfeepos", BigDecimal.ZERO);
			parameters.put("receivedfeecheque", BigDecimal.ZERO);
			parameters.put("receivedfeeother", BigDecimal.ZERO);
			parameters.put("paybackedfee", BigDecimal.ZERO);
			parameters.put("podremarkid", 0l);
			parameters.put("posremark", "");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", tlmposRespNote.getEx_code());
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", tlmposRespNote.getBranchid());
			parameters.put("sessionuserid", tlmposRespNote.getDeliverid());
			parameters.put("sign_typeid", 0);
			parameters.put("sign_man", "");
			parameters.put("sign_time", "");
			cwbOrderService.deliverStatePod(getUser(tlmposRespNote.getDeliverid()), tlmposRespNote.getOrder_no(), tlmposRespNote.getOrder_no(), parameters);
			tlmposRespNote.setResp_code(TlmposExptMsgEnum.Success.getResp_code());
			tlmposRespNote.setResp_msg(TlmposExptMsgEnum.Success.getResp_msg());
		} catch (CwbException e1) {
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(tlmposRespNote.getOrder_no());
			User user = getUser(tlmposRespNote.getDeliverid());
			exceptionCwbDAO.createExceptionCwbScan(tlmposRespNote.getOrder_no(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "",tlmposRespNote.getOrder_no());

			if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
				logger.error("通联异常反馈异常,没有检索到数据" + tlmposRespNote.getOrder_no() + ",小件员：" + tlmposRespNote.getDelivery_man(), e1);
				tlmposRespNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
				tlmposRespNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());

			} else if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()) {
				tlmposRespNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
				tlmposRespNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());
				logger.error("通联异常反馈异常,不是此小件员的货" + tlmposRespNote.getOrder_no() + ",当前小件员：" + tlmposRespNote.getDelivery_man() + e1);
			}
			e1.printStackTrace();
		}

		return tlmposRespNote;
	}

	private Map<String, String> convertMapType_toExptFeedBack(TlmposRespNote tlmposRespNote, Tlmpos tlmpos) {
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("transaction_id", tlmposRespNote.getTransaction_id());
		retMap.put("resp_code", tlmposRespNote.getResp_code());
		retMap.put("resp_msg", tlmposRespNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		retMap.put("requester", tlmpos.getRequester());
		retMap.put("target", tlmpos.getTargeter());
		retMap.put("order_no", tlmposRespNote.getOrder_no());

		// 生成待加密的字符串
		String str = TlmposXMLHandler.createMACXML_cwbSign(retMap);
		String r = CreateRespSign(tlmpos, str);
		retMap.put("MAC", r);
		return retMap;
	}

	private int getDeliveryByReasonType(String code) {
		int deliverystate = -1;

		ExptReason exptreason = exptReasonDAO.getExptReasonCodeByPos(code, PosEnum.TongLianPos.getKey());
		if (exptreason != null) {
			int expt_type = exptreason.getExpt_type();
			if (expt_type == ReasonTypeEnum.BeHelpUp.getValue()) {
				deliverystate = DeliveryStateEnum.FenZhanZhiLiu.getValue();
			} else if (expt_type == ReasonTypeEnum.ReturnGoods.getValue()) {
				deliverystate = DeliveryStateEnum.JuShou.getValue();
			}
		}

		return deliverystate;
	}

}
