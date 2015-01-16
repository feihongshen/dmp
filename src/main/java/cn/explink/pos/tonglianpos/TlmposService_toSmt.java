package cn.explink.pos.tonglianpos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
public class TlmposService_toSmt extends TlmposService {
	private Logger logger = LoggerFactory.getLogger(TlmposService_toSmt.class);

	/**
	 * 上门退业务处理
	 *
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toDealWithSmt(Transaction rootnote, Tlmpos tlmpos) {
		TlmposRespNote respNote = new TlmposRespNote();
		try {
			respNote = super.buildtlmposRespClass(rootnote, respNote);
			String return_type = rootnote.getTransaction_Body().getReturn_type();

			long returnCode = this.validatorReturnTypes(Long.valueOf(return_type));

			if (respNote.getCwbOrder() == null) {
				respNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
				respNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());
				this.logger.error("tlmpos运单上门揽退反馈,没有检索到数据" + respNote.getOrder_no() + ",小件员：" + respNote.getDelivery_man());
			} else if ((respNote.getDeliverstate() != null) && (respNote.getDeliverstate().getDeliverystate() != 0)) {
				respNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
				respNote.setResp_msg(TlmposExptMsgEnum.QiTaShiBai.getResp_msg() + ",不可重复反馈");
				this.logger.error("tlmpos运单上门揽退反馈,订单反馈过，不可重复反馈,单号：" + respNote.getOrder_no() + ",小件员：" + respNote.getDelivery_man());
			} else if (returnCode == 0) {
				respNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
				respNote.setResp_msg(TlmposExptMsgEnum.QiTaShiBai.getResp_msg() + "无法识别此异常码");
				this.logger.error("tlmpos上门揽退反馈失败,无法识别此编码:[" + returnCode + "]，单号：" + respNote.getOrder_no() + ",小件员：" + respNote.getDelivery_man());
			} else {
				respNote.setEx_code(rootnote.getTransaction_Body().getEx_code());
				respNote.setEx_desc(rootnote.getTransaction_Body().getEx_desc());
				respNote = this.excuteCwbFeedBackLantuiHandler(respNote, rootnote, returnCode);
			}

		} catch (Exception e) {
			respNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
			respNote.setResp_msg(TlmposExptMsgEnum.QiTaShiBai.getResp_msg());
			this.logger.error("上门揽退反馈-系统遇到不可预知的异常!异常原因:", e);

		}

		// 生成返回的xml字符串
		Map<String, String> respMap = this.convertMapType_toExptFeedBack(respNote, tlmpos);
		String responseXml = TlmposXMLHandler.createXMLMessage_toExptFeedBack(respMap);
		this.logger.info("返回tlmpos数据成功。业务编码={},返回XML={}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);

		return responseXml;
	}

	private int validatorReturnTypes(long return_type) {
		for (ReturnTypeEnum enums : ReturnTypeEnum.values()) {
			if (enums.getCode() == return_type) {
				return enums.getCode();
			}
		}
		return 0;
	}

	private TlmposRespNote excuteCwbFeedBackLantuiHandler(TlmposRespNote tlmposRespNote, Transaction transaction, long return_type) {
		long deliverystate = 0;
		String remark = "";
		BigDecimal infactfee = BigDecimal.valueOf(transaction.getTransaction_Body().getInfactfee());
		if (return_type == ReturnTypeEnum.QuanTui.getCode()) {
			deliverystate = DeliveryStateEnum.ShangMenTuiChengGong.getValue();
			remark = "POS反馈-上门退成功";
		} else if (return_type == ReturnTypeEnum.BufenTui.getCode()) {
			deliverystate = DeliveryStateEnum.ShangMenTuiChengGong.getValue();
			remark = "POS反馈-部分退货";
		} else if (return_type == ReturnTypeEnum.JuTui.getCode()) {
			deliverystate = DeliveryStateEnum.ShangMenJuTui.getValue();
			remark = "POS反馈-上门拒退";
			infactfee = BigDecimal.ZERO;
		}

		long backreasonid = 0;
		long leavedreasonid = 0;

		try {
			tlmposRespNote.setBranchid(this.userDAO.getUserByUsername(tlmposRespNote.getDelivery_man()).getBranchid());

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", tlmposRespNote.getDeliverid());
			parameters.put("podresultid", deliverystate);
			parameters.put("backreasonid", backreasonid);
			parameters.put("leavedreasonid", leavedreasonid);
			parameters.put("receivedfeecash", BigDecimal.ZERO);
			parameters.put("receivedfeepos", BigDecimal.ZERO);
			parameters.put("receivedfeecheque", BigDecimal.ZERO);
			parameters.put("receivedfeeother", BigDecimal.ZERO);
			parameters.put("paybackedfee", BigDecimal.ZERO);
			parameters.put("podremarkid", 0l);
			parameters.put("posremark", remark);
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", remark);
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", tlmposRespNote.getBranchid());
			parameters.put("sessionuserid", tlmposRespNote.getDeliverid());
			parameters.put("sign_typeid", 0);
			parameters.put("sign_man", "");
			parameters.put("sign_time", "");
			parameters.put("infactfare", infactfee);

			this.cwbOrderService.deliverStatePod(this.getUser(tlmposRespNote.getDeliverid()), tlmposRespNote.getOrder_no(), tlmposRespNote.getOrder_no(), parameters);
			tlmposRespNote.setResp_code(TlmposExptMsgEnum.Success.getResp_code());
			tlmposRespNote.setResp_msg(TlmposExptMsgEnum.Success.getResp_msg());
		} catch (CwbException e1) {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(tlmposRespNote.getOrder_no());
			User user = this.getUser(tlmposRespNote.getDeliverid());
			this.exceptionCwbDAO.createExceptionCwb(tlmposRespNote.getOrder_no(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "");

			if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
				this.logger.error("通联上门揽退反馈异常,没有检索到数据" + tlmposRespNote.getOrder_no() + ",小件员：" + tlmposRespNote.getDelivery_man(), e1);
				tlmposRespNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
				tlmposRespNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());

			} else if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()) {
				tlmposRespNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
				tlmposRespNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());
				this.logger.error("通联上门揽退反馈异常,不是此小件员的货" + tlmposRespNote.getOrder_no() + ",当前小件员：" + tlmposRespNote.getDelivery_man() + e1);
			}

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
		String r = this.CreateRespSign(tlmpos, str);
		retMap.put("MAC", r);
		return retMap;
	}

	private int getDeliveryByReasonType(String code) {
		int deliverystate = -1;

		ExptReason exptreason = this.exptReasonDAO.getExptReasonCodeByPos(code, PosEnum.TongLianPos.getKey());
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
