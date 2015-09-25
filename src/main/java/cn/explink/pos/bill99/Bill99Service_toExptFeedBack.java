package cn.explink.pos.bill99;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.bill99.xml.Transaction;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.pos.CertificateCoderUtil;

@Service
public class Bill99Service_toExptFeedBack extends Bill99Service {
	private Logger logger = LoggerFactory.getLogger(Bill99Service_toExptFeedBack.class);

	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;

	/**
	 * 异常反馈通知接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toExceptionFeedBack(Transaction rootnote, Bill99 bill99) {
		Bill99RespNote billrespNote = new Bill99RespNote();
		try {
			billrespNote = super.BuildBill99RespClass(rootnote, billrespNote);

			billrespNote = ValidateRequestBusiness_exptFeedBack(billrespNote, rootnote);
			if (billrespNote.getResp_code() == null) { // 如果验证通过了 执行反馈方法
				billrespNote = ExcuteCwbExptFeedBackHandler(rootnote, billrespNote);
			}

		} catch (Exception e) {
			billrespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
			logger.error("异常反馈-系统遇到不可预知的异常!异常原因:", e);
			e.printStackTrace();
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_toExptFeedBack(billrespNote, bill99, rootnote);
		String responseXml = Bill99XMLHandler.createXMLMessage_toExptFeedBack(respMap);
		logger.info("返回bill99数据成功。业务编码={},返回XML={}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);
		return responseXml;
	}

	private Bill99RespNote ExcuteCwbExptFeedBackHandler(Transaction rootnote, Bill99RespNote billrespNote) {
		String ex_packages = rootnote.getTransaction_Header().getExt_attributes().getEx_packages(); // 异常件数
		long backreasonid = 0;
		long leavedreasonid = 0;
		if (billrespNote.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			leavedreasonid = rootnote.getTransaction_Body().getEx_code();
		} else if (billrespNote.getDeliverystate() == DeliveryStateEnum.JuShou.getValue()) {
			backreasonid = rootnote.getTransaction_Body().getEx_code();
		}
		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", billrespNote.getDeliverid());
			parameters.put("podresultid", billrespNote.getDeliverystate());
			parameters.put("backreasonid", backreasonid);
			parameters.put("leavedreasonid", leavedreasonid);
			parameters.put("receivedfeecash", BigDecimal.ZERO);
			parameters.put("receivedfeepos", BigDecimal.ZERO);
			parameters.put("receivedfeecheque", BigDecimal.ZERO);
			parameters.put("receivedfeeother", BigDecimal.ZERO);
			parameters.put("paybackedfee", BigDecimal.ZERO);
			parameters.put("podremarkid", (long) 0);
			parameters.put("posremark", "POS反馈");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", rootnote.getTransaction_Body().getEx_desc());
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", billrespNote.getBranchid());
			parameters.put("sessionuserid", billrespNote.getDeliverid());
			parameters.put("sign_typeid", 0); // 是否 已签收 （0，未签收，1已签收）
			parameters.put("sign_man", "");
			parameters.put("sign_time", "");
			cwbOrderService.deliverStatePod(getUser(billrespNote.getDeliverid()), billrespNote.getOrder_no(), billrespNote.getOrder_no(), parameters);
			billrespNote.setResp_code(Bill99ExptMessageEnum.Success.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.Success.getResp_msg());
			logger.info("bill异常反馈成功，订单号=" + billrespNote.getOrder_no() + ",件数：" + ex_packages + ",小件员=" + billrespNote.getDelivery_man() + ",异常code=" + rootnote.getTransaction_Body().getEx_code()
					+ ",异常msg=" + rootnote.getTransaction_Body().getEx_desc());
		} catch (CwbException e1) {
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(billrespNote.getOrder_no());
			User user = getUser(billrespNote.getDeliverid());
			exceptionCwbDAO.createExceptionCwbScan(billrespNote.getOrder_no(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "",billrespNote.getOrder_no());

			if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
				logger.error("bill99异常反馈异常,没有检索到数据" + billrespNote.getOrder_no() + ",小件员：" + billrespNote.getDelivery_man(), e1);
				billrespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
				billrespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());

			} else if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()) {
				billrespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
				billrespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());
				logger.error("bill99异常反馈异常,不是此小件员的货" + billrespNote.getOrder_no() + ",当前小件员：" + billrespNote.getDelivery_man() + e1);
			}
		}
		return billrespNote;
	}

	private Bill99RespNote ValidateRequestBusiness_exptFeedBack(Bill99RespNote billrespNote, Transaction rootnote) {
		billrespNote.setDeliverystate(getDeliveryStateByReasonType(rootnote.getTransaction_Body().getEx_code()));

		if (billrespNote.getCwbOrder() == null) {
			billrespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());
			logger.error("bill99运单异常反馈,没有检索到数据" + billrespNote.getOrder_no() + ",小件员：" + billrespNote.getDelivery_man());
		} else if (billrespNote.getDeliverstate().getSign_typeid() == 1) {
			billrespNote.setResp_code(Bill99ExptMessageEnum.DingDanYiQianShou.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.DingDanYiQianShou.getResp_msg());
			logger.error("bill99运单异常反馈,订单已签收，不可重复签收,单号：" + billrespNote.getOrder_no() + ",小件员：" + billrespNote.getDelivery_man());
		} else if (billrespNote.getDeliverystate() == -1) {
			billrespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
			logger.error("bill99异常反馈失败,无法识别此编码:[" + rootnote.getTransaction_Body().getEx_code() + "]，单号：" + billrespNote.getOrder_no() + ",小件员：" + billrespNote.getDelivery_man());
		}
		return billrespNote;
	}

	private Map<String, String> convertMapType_toExptFeedBack(Bill99RespNote billrespNote, Bill99 bill99, Transaction rootnote) {
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
