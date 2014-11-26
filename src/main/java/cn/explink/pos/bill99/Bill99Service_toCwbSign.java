package cn.explink.pos.bill99;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.alipay.AliPayExptMessageEnum;
import cn.explink.pos.bill99.xml.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayHandler;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.pos.CertificateCoderUtil;

@Service
public class Bill99Service_toCwbSign extends Bill99Service {
	private Logger logger = LoggerFactory.getLogger(Bill99Service_toCwbSign.class);

	/**
	 * 签收通知接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toCwbSign(Transaction rootnote, Bill99 bill99) {
		Bill99RespNote billrespNote = new Bill99RespNote();
		try {
			billrespNote = super.BuildBill99RespClass(rootnote, billrespNote);

			// billrespNote=ExcuteCwbSignHandler(rootnote, billrespNote,bill99);
			// //执行签收的方法

			if (billrespNote.getDeliverstate().getSign_typeid() == 1) { // 判断是否已签收
				billrespNote.setResp_code(Bill99ExptMessageEnum.DingDanYiQianShou.getResp_code());
				billrespNote.setResp_msg(Bill99ExptMessageEnum.DingDanYiQianShou.getResp_msg());
			} else {
				billrespNote = ExcuteCwbSignHandler(rootnote, billrespNote, bill99); // 执行签收的方法
			}

		} catch (Exception e) {
			billrespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
			logger.error("运单签收-系统遇到不可预知的异常!异常原因:", e);
			e.printStackTrace();
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_cwbSign(billrespNote, bill99, rootnote);
		String responseXml = Bill99XMLHandler.createXMLMessage_cwbSign(respMap);
		logger.info("Bill99[" + rootnote.getTransaction_Header().getTransaction_id() + "]订单号：{},返回数据成功-返回XML：{}", billrespNote.getOrder_no(), responseXml);
		return responseXml;
	}

	/**
	 * 处理签收信息的方法
	 * 
	 * @param jobject
	 * @param billrespNote
	 * @return
	 */
	private Bill99RespNote ExcuteCwbSignHandler(Transaction rootnote, Bill99RespNote billrespNote, Bill99 bill99) {
		try {
			// BigDecimal
			// totalAmount=billrespNote.getDeliverstate().getPos().add(billrespNote.getDeliverstate().getCash()).add(billrespNote.getDeliverstate().getCheckfee()).add(billrespNote.getDeliverstate().getOtherfee());
			// BigDecimal pos=billrespNote.getDeliverstate().getPos();
			// if(totalAmount.compareTo(BigDecimal.ZERO)==0){
			// pos=billrespNote.getDeliverstate().getBusinessfee();
			// }
			BigDecimal totalAmount = billrespNote.getDeliverstate().getPos().add(billrespNote.getDeliverstate().getCash()).add(billrespNote.getDeliverstate().getCheckfee())
					.add(billrespNote.getDeliverstate().getOtherfee());
			BigDecimal pos = billrespNote.getDeliverstate().getPos();
			BigDecimal Cash = billrespNote.getDeliverstate().getCash();
			BigDecimal Businessfee = billrespNote.getDeliverstate().getBusinessfee();
			BigDecimal paybackedfee = billrespNote.getDeliverstate().getReturnedfee();
			if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {

				if (billrespNote.getDeliverstate().getIsout() == 1) { // 应退款
					pos = BigDecimal.ZERO;
					Cash = BigDecimal.ZERO;
					paybackedfee = billrespNote.getDeliverstate().getBusinessfee();
				} else if (Businessfee.compareTo(BigDecimal.ZERO) > 0) { // 说明应收款大于0，且支付接口没有执行完毕，返回其他异常，等待支付处理完毕，pos重发签收接口。
					billrespNote.setResp_code(AliPayExptMessageEnum.QiTaShiBai.getResp_code());
					billrespNote.setResp_msg("未支付订单不可签收");
					logger.info("未支付订单不可签收cwb={}", billrespNote.getOrder_no());
					return billrespNote;
				}

			}

			long deliverid = billrespNote.getDeliverstate().getDeliveryid();
			if (bill99.getIsupdateDeliverid() == 1) { // 需要更新派送员
				deliverid = billrespNote.getDeliverid(); // 根据请求接口的登录工号
			}

			DeliveryState deryState = billrespNote.getDeliverstate();

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", deliverid);
			parameters.put("podresultid", getPodResultIdByCwb(billrespNote.getCwbOrder().getCwbordertypeid()));
			parameters.put("backreasonid", (long) 0);
			parameters.put("leavedreasonid", (long) 0);
			parameters.put("receivedfeecash", Cash);
			parameters.put("receivedfeepos", pos);
			parameters.put("receivedfeecheque", billrespNote.getDeliverstate().getCheckfee());
			parameters.put("receivedfeeother", BigDecimal.ZERO);
			parameters.put("paybackedfee", paybackedfee);
			parameters.put("podremarkid", (long) 0);
			parameters.put("posremark", deryState.getPosremark() + "POS反馈");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", "POS签收成功");
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", billrespNote.getBranchid());
			parameters.put("sessionuserid", deliverid);
			parameters.put("sign_typeid", 1); // 是否 已签收 （0，未签收，1已签收）
			parameters.put("sign_man", rootnote.getTransaction_Body().getSignee());
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			parameters.put("nosysyemflag", "1");//
			cwbOrderService.deliverStatePod(userDAO.getUserByUsername(billrespNote.getDelivery_man()), billrespNote.getOrder_no(), billrespNote.getOrder_no(), parameters);

			deliveryStateDAO.updateOperatorIdByCwb(deliverid, billrespNote.getOrder_no());

			RecordPosPayDetail_Method(rootnote, billrespNote.getCwbOrder(), "", rootnote.getTransaction_Body().getSignee(), billrespNote.getOrder_no());
			billrespNote.setResp_code(Bill99ExptMessageEnum.Success.getResp_code());
			billrespNote.setResp_msg(Bill99ExptMessageEnum.Success.getResp_msg());
			logger.info("bill99运单签收成功,单号：{},小件员：{}", billrespNote.getOrder_no(), billrespNote.getDelivery_man());
		} catch (CwbException e1) {
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(billrespNote.getOrder_no());
			User user = userDAO.getUserByUsername(billrespNote.getDelivery_man());
			exceptionCwbDAO.createExceptionCwb(billrespNote.getOrder_no(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "");

			if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
				logger.error("bill99运单签收,没有检索到数据" + billrespNote.getOrder_no() + ",小件员：" + billrespNote.getDelivery_man(), e1);
				billrespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
				billrespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());

			} else if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()) {
				billrespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
				billrespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());
				logger.error("bill99运单签收,不是此小件员的货" + billrespNote.getOrder_no() + ",当前小件员：" + billrespNote.getDelivery_man(), e1);
			} else {
				billrespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
				billrespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
				logger.error("bill99运单签收,订单号=" + billrespNote.getOrder_no() + ",小件员=" + billrespNote.getDelivery_man(), e1);
			}
		}
		return billrespNote;
	}

	private void RecordPosPayDetail_Method(Transaction rootnote, CwbOrder cwbOrder, String podremark, String signee, String order_no) {
		String Sign_Remark;
		int Sign_Self_Flag;
		// 记录支付签收表posPayDetail
		String consignee_sign_flag = rootnote.getTransaction_Body().getConsignee_sign_flag();
		if ("Y".equalsIgnoreCase(consignee_sign_flag)) {
			Sign_Self_Flag = SignTypeEnum.BenRenQianShou.getValue();
			Sign_Remark = SignTypeEnum.BenRenQianShou.getSign_text();
		} else {
			Sign_Self_Flag = SignTypeEnum.TaRenDaiQianShou.getValue();
			Sign_Remark = SignTypeEnum.TaRenDaiQianShou.getSign_text();
		}
		Sign_Remark = PosPayHandler.getSignPayAmountType(cwbOrder.getReceivablefee(), Sign_Remark); // 签收类型
		posPayDAO.save_PosTradeDetailRecord(order_no, podremark, Double.valueOf(cwbOrder.getReceivablefee() + ""), 0, 0, "", signee, Sign_Self_Flag, Sign_Remark, 2, 1, "", PosEnum.Bill99.getMethod(),
				0, "");
		logger.info("bill99签收交易-记录posPayDetail表成功! 订单号:{}", order_no);
	}

	private Map<String, String> convertMapType_cwbSign(Bill99RespNote billrespNote, Bill99 bill99, Transaction rootnote) {
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
