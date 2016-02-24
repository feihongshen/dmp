package cn.explink.pos.bill99;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.bill99.xml.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.pos.CertificateCoderUtil;

@Service
public class Bill99Service_toPayAmount extends Bill99Service {
	private Logger logger = LoggerFactory.getLogger(Bill99Service_toPayAmount.class);

	@Autowired
	CwbOrderService cwbOrderService;

	/**
	 * 付款接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toPayAmountForPos(Transaction rootnote, Bill99 bill99) {
		Bill99RespNote billRespNote = new Bill99RespNote();
		try {
			billRespNote = super.BuildBill99RespClass(rootnote, billRespNote);
			billRespNote = BExcutePosPayHandler(billRespNote, rootnote, bill99);
		} catch (Exception e) {
			logger.error("Bill99支付接口发生不可预知的异常:", e);
			billRespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			billRespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_PayAmount(billRespNote, bill99, rootnote);
		String responseXml = Bill99XMLHandler.createXMLMessage_payAmount(respMap);

		logger.info("返回Bill99[" + rootnote.getTransaction_Header().getTransaction_id() + "]数据成功,返回XML：" + responseXml);
		return responseXml;
	}

	/**
	 * 验证支付前基本逻辑并返回参数
	 * 
	 * @param billRespNote
	 * @return
	 */
	private Bill99RespNote ValidateRequestBusiness_PosPay(Bill99RespNote billRespNote, Transaction rootnote) {

		if (billRespNote.getCwbOrder() == null) {
			billRespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
			billRespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());
			logger.error("bill99运单支付,没有检索到数据" + billRespNote.getOrder_no() + ",小件员：" + billRespNote.getDelivery_man());
			return billRespNote;
		}
		if (rootnote.getTransaction_Body().getOrder_payable_amt() == billRespNote.getDeliverstate().getReceivedfee().doubleValue()) {
			billRespNote.setResp_code(Bill99ExptMessageEnum.JiaoYiChongFu.getResp_code());
			billRespNote.setResp_msg(Bill99ExptMessageEnum.JiaoYiChongFu.getResp_msg());
			logger.error("[运单支付]该订单已支付,当前订单号：" + billRespNote.getOrder_no() + ",小件员：" + billRespNote.getDelivery_man());
			return billRespNote;
		}
		if (rootnote.getTransaction_Body().getOrder_payable_amt() != billRespNote.getCwbOrder().getReceivablefee().doubleValue()) {
			billRespNote.setResp_code(Bill99ExptMessageEnum.YingShouJinEYiChang.getResp_code());
			billRespNote.setResp_msg(Bill99ExptMessageEnum.YingShouJinEYiChang.getResp_msg());
			logger.error("[运单支付]支付金额有误,当前订单号：" + billRespNote.getOrder_no() + ",小件员：" + billRespNote.getDelivery_man());
			return billRespNote;
		}
		return billRespNote;

	}

	/**
	 * 处理支付的方法
	 * 
	 * @param billRespNote
	 * @return
	 * @throws Exception
	 */
	private Bill99RespNote BExcutePosPayHandler(Bill99RespNote billRespNote, Transaction rootnote, Bill99 bill99) throws Exception {
		switch (rootnote.getTransaction_Body().getPay_type()) {
		case 01:
			billRespNote.setSystem_pay_type(PaytypeEnum.Xianjin.getValue());
			billRespNote.setPodremark(PaytypeEnum.Xianjin.getText());
			break;
		case 02:
			billRespNote.setSystem_pay_type(PaytypeEnum.Pos.getValue());
			billRespNote.setPodremark(PaytypeEnum.Pos.getText());
			break;
		}
		try {

			long deliverid = billRespNote.getDeliverstate().getDeliveryid();
			if (bill99.getIsupdateDeliverid() == 1) { // 需要更新派送员
				deliverid = billRespNote.getDeliverid(); // 根据请求接口的登录工号
			}

			String trackinfo = "bill99刷卡支付,支付金额:" + rootnote.getTransaction_Body().getOrder_payable_amt() + "支付方式:" + billRespNote.getSystem_pay_type() + ",终端号:"
					+ rootnote.getTransaction_Body().getTerminal_id() + ";凭证号:" + rootnote.getTransaction_Body().getTrace_no();
			cwbOrderService.posPay(billRespNote.getOrder_no(), BigDecimal.valueOf(rootnote.getTransaction_Body().getOrder_payable_amt()), billRespNote.getCwbOrder().getReceivablefee(),
					billRespNote.getSystem_pay_type(), billRespNote.getPodremark(), trackinfo, deliverid, billRespNote.getDeliverstate(), "single", 0);
			
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
					deliveryStateDAO.updateDeliverybranchid(billRespNote.getOrder_no(), deliverybranchid);
				}
			}catch(Exception ex){
				logger.error("Bill99Service_toPayAmount deliverid={} Exception={}", deliverid, ex.getMessage());
				ex.printStackTrace(System.out);
			}
			//leoliao 2016-02-03 end
			
			posPayDAO.save_PosTradeDetailRecord(billRespNote.getOrder_no(), billRespNote.getPodremark(), rootnote.getTransaction_Body().getOrder_payable_amt(), deliverid,
					billRespNote.getSystem_pay_type(), billRespNote.getTrackinfo(), "", 0, "", 1, 1, "single", PosEnum.Bill99.getMethod(), 0, "");
			trackinfo += ";系统参考编号：" + rootnote.getTransaction_Body().getIdTxn() + ",[派件支付]更新数据成功,当前订单号：" + billRespNote.getOrder_no() + ",小件员：" + billRespNote.getDelivery_man();
			billRespNote.setResp_code(Bill99ExptMessageEnum.Success.getResp_code());
			billRespNote.setResp_msg(Bill99ExptMessageEnum.Success.getResp_msg());
			logger.info(trackinfo);
			return billRespNote;
		} catch (CwbException e1) {
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(billRespNote.getOrder_no());
			long deliverid = billRespNote.getDeliverstate().getDeliveryid();
			if (bill99.getIsupdateDeliverid() == 1) { // 需要更新派送员
				deliverid = billRespNote.getDeliverid(); // 根据请求接口的登录工号
			}

			User user = getUser(deliverid);
			exceptionCwbDAO.createExceptionCwbScan(billRespNote.getOrder_no(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "",billRespNote.getOrder_no());

			logger.error("bill99支付处理业务逻辑异常！小件员=" + billRespNote.getDelivery_man() + ",订单号=" + billRespNote.getOrder_no() + ",异常原因=" + e1.getMessage(), e1);
			return DealWithCatchCwbException(billRespNote, e1);

		}

	}

	/**
	 * 处理快钱的异常业务逻辑,并转化为对象
	 * 
	 * @param billRespNote
	 * @param e1
	 * @return
	 */
	private Bill99RespNote DealWithCatchCwbException(Bill99RespNote billRespNote, CwbException e1) {
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
			billRespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
			billRespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());
			return billRespNote;
		}
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.DingDanYiZhiFu.getValue()) {
			billRespNote.setResp_code(Bill99ExptMessageEnum.YiShouKuanWeiQianShou.getResp_code());
			billRespNote.setResp_msg(Bill99ExptMessageEnum.YiShouKuanWeiQianShou.getResp_msg());
			return billRespNote;
		}
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.ZhiFuAmountExceiton.getValue()) {
			billRespNote.setResp_code(Bill99ExptMessageEnum.YingShouJinEYiChang.getResp_code());
			billRespNote.setResp_msg(Bill99ExptMessageEnum.YingShouJinEYiChang.getResp_msg());
			return billRespNote;
		}
		billRespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
		billRespNote.setResp_msg(Bill99ExptMessageEnum.QiTaShiBai.getResp_msg());
		return billRespNote;
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
