package cn.explink.pos.yeepay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.bill99.Bill99ExptMessageEnum;
import cn.explink.pos.bill99.Bill99RespNote;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.yeepay.xml.YeepayRequest;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

@Service
public class YeePayService_toPayAmount extends YeePayService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Md5PasswordEncoder md5 = new Md5PasswordEncoder();

	/**
	 * 付款接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toPayAmountForPos(String service_code, YeepayRequest rootnote, YeePay yeePay) {

		YeePayRespNote respNote = new YeePayRespNote();
		try {

			respNote = super.BuildYeePayRespClass(rootnote, respNote);

			if (respNote.getCwbOrder() == null) {
				respNote.setResult_code("3");
				respNote.setResult_msg("接收失败，查无此单");
			} else {
				respNote = BExcuteDealWithPayAmountLogics(rootnote, respNote);
				if (respNote.getResult_code() == null) {
					respNote.setResult_code("2");
					respNote.setResult_msg("操作成功");
				}
			}

		} catch (Exception e) {
			logger.error("yeepay支付接口发生不可预知的异常:", e);
			respNote.setResult_code("3");
			respNote.setResult_msg("未知异常");
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_PayAmount(service_code, rootnote, respNote, yeePay);
		String responseXml = YeePayXMLHandler.createResponseXML_PayAmount(respMap);

		logger.info("返回yeepay数据成功。业务编码={},XML={}", service_code, responseXml);

		return responseXml;
	}

	private YeePayRespNote BExcuteDealWithPayAmountLogics(YeepayRequest rootnote, YeePayRespNote respNote) throws Exception {
		String trackinfo;
		double receivablefee = respNote.getCwbOrder().getReceivablefee().doubleValue(); // 应收款
		// double
		// receivedAmount=posPayService.getReceivedAmountByCwb(respNote.getCwbOrder().getCwb(),new
		// BigDecimal(receivablefee),respNote.getDeliverid()); //实收款
		double receivedfeePos = Double.valueOf(rootnote.getSessionBody().getExtendAtt().getAMT()); // pos查询出来的日期

		// 日志内容
		trackinfo = "(YeePay)移动POS信息反馈:派送运单支付,";
		// 配送结果备注 1 刷卡支付 2 账户支付 3 支票支付 4 现金支付

		switch (rootnote.getSessionBody().getExtendAtt().getPay_Type()) {
		case 1:
			respNote.setSystem_pay_type(PaytypeEnum.Pos.getValue());
			respNote.setPodremark(PaytypeEnum.Pos.getText());
			break;
		case 2:
			respNote.setSystem_pay_type(PaytypeEnum.Qita.getValue());
			respNote.setPodremark(PaytypeEnum.Qita.getText());
			break;
		case 3:
			respNote.setSystem_pay_type(PaytypeEnum.Zhipiao.getValue());
			respNote.setPodremark(PaytypeEnum.Zhipiao.getText());
			break;
		case 4:
			respNote.setSystem_pay_type(PaytypeEnum.Xianjin.getValue());
			respNote.setPodremark(PaytypeEnum.Xianjin.getText());
			break;

		}

		try {
			trackinfo = "YeePay刷卡支付,支付金额:" + receivedfeePos + "支付方式:" + respNote.getSystem_pay_type() + ",终端号:" + rootnote.getSessionHead().getTransactionID() + ",银行卡名称:"
					+ rootnote.getSessionBody().getExtendAtt().getBankCardNo() + ",获取银行的订单号=" + rootnote.getSessionBody().getExtendAtt().getBank_OrderId() + ",获取易宝的订单号="
					+ rootnote.getSessionBody().getExtendAtt().getYeepay_OrderId() + ",支票=" + rootnote.getSessionBody().getExtendAtt().getChequeNo();

			cwbOrderService.posPay(respNote.getCwbOrder().getCwb(), BigDecimal.valueOf(receivedfeePos), BigDecimal.valueOf(receivablefee), respNote.getSystem_pay_type(), respNote.getPodremark(),
					"POS机操作", respNote.getDeliverid(), respNote.getDeliverstate(), "single", 0);

			posPayDAO.save_PosTradeDetailRecord(respNote.getCwbOrder().getCwb(), respNote.getPodremark(), receivedfeePos, respNote.getDeliverid(), respNote.getSystem_pay_type(), trackinfo, "", 0, "",
					1, 1, "single", PosEnum.YeePay.getMethod(), 0, "");

		} catch (CwbException e1) {
			User user = userDAO.getUserByUserid(respNote.getDeliverid());
			exceptionCwbDAO.createExceptionCwbScan(respNote.getCwbOrder().getCwb(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(), respNote.getCwbOrder() == null ? 0
					: respNote.getCwbOrder().getCustomerid(), 0, 0, 0, "",respNote.getCwbOrder().getCwb());

			respNote.setResult_code("3");
			respNote.setResult_msg("处理业务逻辑异常");

			logger.error("yeepay支付更新数据库异常！小件员：" + respNote.getDeliverid() + ",当前订单号：" + respNote.getCwbOrder().getCwb() + "异常信息=" + e1.getMessage(), e1);
			return DealWithCatchCwbException(respNote, e1);
		}

		return respNote;
	}

	/**
	 * 处理易宝支付的异常业务逻辑,并转化为对象
	 * 
	 * @param respNote
	 * @param e1
	 * @return
	 */
	private YeePayRespNote DealWithCatchCwbException(YeePayRespNote respNote, CwbException e1) {
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
			respNote.setResult_code("3");
			respNote.setResult_code("查询异常");
			return respNote;
		}
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.DingDanYiZhiFu.getValue()) {
			respNote.setResult_code("3");
			respNote.setResult_msg("已收款未签收");
			return respNote;
		}
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.ZhiFuAmountExceiton.getValue()) {
			respNote.setResult_code("3");
			respNote.setResult_msg("应收金额不正确");
			return respNote;
		}
		respNote.setResult_code("3");
		respNote.setResult_msg("其他原因异常");
		return respNote;
	}

	private Map<String, String> convertMapType_PayAmount(String service_code, YeepayRequest rootnote, YeePayRespNote respNote, YeePay yeePay) {
		Map<String, String> respMap = new HashMap<String, String>();
		String result_time = DateTimeUtil.getNowTime("yyyyMMddHHmmss");

		respMap.put("version", rootnote.getSessionHead().getVersion()); // 版本号
		respMap.put("servicecode", service_code); // 请求的业务编号
		respMap.put("transactionid", rootnote.getSessionHead().getTransactionID()); // 交易流水号
		respMap.put("srcsysid", yeePay.getRequester()); // 请求方
		respMap.put("dstsysid", yeePay.getTargeter()); // 响应方
		respMap.put("result_code", respNote.getResult_code()); // 响应结果编码
		respMap.put("result_msg", respNote.getResult_msg()); // 响应结果消息
		respMap.put("result_time", result_time); // 响应时间
		respMap.put("employee_id", respNote.getUsername());
		respMap.put("order_no", respNote.getCwbOrder() == null ? "" : respNote.getCwbOrder().getCwb());

		// 生成待加密的字符串
		String HMacXml = YeePayXMLHandler.createResponseXMLHMAC_PayAmount(respMap);
		// 加密后的字符串
		String hmac = md5.encodePassword(HMacXml + yeePay.getPrivatekey(), null);
		// 把签名后的数据放在map中用来返回最后的数据
		respMap.put("hmac", hmac);

		return respMap;
	}

}
