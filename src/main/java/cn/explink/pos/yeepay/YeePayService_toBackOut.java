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
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.pos.yeepay.xml.YeepayRequest;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

@Service
public class YeePayService_toBackOut extends YeePayService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Md5PasswordEncoder md5 = new Md5PasswordEncoder();

	/**
	 * 签收通知接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	private String toSignCwb(String service_code, JSONObject jobject, YeePay yeePay) {
		String TransactionID = ""; // 交易流水号
		String Employee_ID = ""; // 收派员工号
		String Order_No = ""; // 运单号
		String result_code = ""; // 响应结果编码
		String result_msg = ""; // 响应结果信息
		String podremark = ""; // 配送结果备注
		String Sign_Self_Flag = "0"; // 签收标记（本人签，他人签）
		String Sign_Name = "";
		String Sign_Remark = "";
		CwbOrder cwbOrder = null;

		Employee_ID = jobject.getString("Employee_ID"); // 用户名
		Order_No = jobject.getString("Order_No"); // 订单号
		User user = userDAO.getUserByUsername(Employee_ID);
		cwbOrder = cwbDAO.getCwbByCwb(Order_No);
		if (cwbOrder == null) {
			result_code = "3";
			result_msg = "接收失败，查无此单";
			logger.error("yeepay运单签收,查无此单：" + Order_No + ",小件员：" + Employee_ID);
		} else {
			DeliveryState ds = deliveryStateDAO.getActiveDeliveryStateByCwb(cwbOrder.getCwb()); // 支付时已经更新各种款项备注了，签收时需要查询出来
			// 交易流水号
			TransactionID = StringUtil.nullConvertToEmptyString(jobject.getString("TransactionID"));
			Employee_ID = StringUtil.nullConvertToEmptyString(jobject.getString("Employee_ID"));
			Sign_Self_Flag = StringUtil.nullConvertToEmptyString(jobject.getString("Sign_Self_Flag"));
			// 签收人
			Sign_Name = StringUtil.nullConvertToEmptyString(jobject.getString("Sign_Name"));
			Sign_Remark = "交易流水号:" + TransactionID;
			if ("1".equals(Sign_Self_Flag)) {
				Sign_Name = cwbOrder.getConsigneename();
				Sign_Remark = "本人签收";
			} else if ("2".equals(Sign_Self_Flag)) {
				Sign_Remark = "他人代签收";
			}
			if (cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) == 0) {
				Sign_Remark += ";零款签收";
			} else {
				Sign_Remark += ";非零款签收";
			}
			try {

				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("deliverid", user.getUserid());
				parameters.put("podresultid", getPodResultIdByCwb(cwbOrder.getCwbordertypeid()));
				parameters.put("backreasonid", (long) 0);
				parameters.put("leavedreasonid", (long) 0);
				parameters.put("receivedfeecash", ds.getCash());
				parameters.put("receivedfeepos", ds.getPos());
				parameters.put("receivedfeecheque", ds.getCheckfee());
				parameters.put("receivedfeeother", BigDecimal.ZERO);
				parameters.put("paybackedfee", BigDecimal.ZERO);
				parameters.put("podremarkid", (long) 0);
				parameters.put("posremark", "");
				parameters.put("checkremark", "");
				parameters.put("deliverstateremark", "");
				parameters.put("owgid", 0);
				parameters.put("sessionbranchid", user.getBranchid());
				parameters.put("sessionuserid", user.getUserid());
				parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
				parameters.put("sign_man", Sign_Name);
				parameters.put("sign_time", DateTimeUtil.getNowTime());

				cwbOrderService.deliverStatePod(user, cwbOrder.getCwb(), cwbOrder.getCwb(), parameters);
				logger.info("yeepay运单签收成功,单号：" + Order_No + ",小件员：" + Employee_ID);
			} catch (Exception e) {
				logger.error("yeepay运单签收失败,数据更新失败,单号：" + Order_No + ",小件员：" + Employee_ID + "异常信息=" + e);
				e.printStackTrace();
			}
		}

		posPayDAO.save_PosTradeDetailRecord(Order_No, podremark, Double.valueOf(cwbOrder.getReceivablefee() + ""), 0, 0, "", Sign_Name, Integer.parseInt(Sign_Self_Flag), Sign_Remark, 2, 1, "",
				PosEnum.YeePay.getMethod(), 0, "");

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_cwbSign(service_code, jobject, yeePay, result_code, result_msg, cwbOrder, md5, Employee_ID);
		String responseXml = YeePayXMLHandler.createResponseXML_cwbSign(respMap);
		logger.info("返回yeepay数据成功。业务编码={},XML={}", service_code, responseXml);
		return responseXml;
	}

	private Map<String, String> convertMapType_cwbSign(String service_code, JSONObject jobject, YeePay yeePay, String result_code, String result_msg, CwbOrder cwbOrder, Md5PasswordEncoder md5,
			String employee_id) {
		Map<String, String> respMap = new HashMap<String, String>();
		String result_time = DateTimeUtil.getNowTime("yyyyMMddHHmmss");
		if (cwbOrder == null) {
			cwbOrder = new CwbOrder();
		}

		respMap.put("version", yeePay.getVersion()); // 版本号
		respMap.put("servicecode", service_code); // 请求的业务编号
		respMap.put("transactionid", jobject.getString("TransactionID")); // 交易流水号
		respMap.put("srcsysid", yeePay.getRequester()); // 请求方
		respMap.put("dstsysid", yeePay.getTargeter()); // 响应方
		respMap.put("result_code", result_code); // 响应结果编码
		respMap.put("result_msg", result_msg); // 响应结果消息
		respMap.put("result_time", result_time); // 响应时间
		respMap.put("order_no", cwbOrder.getCwb());

		// 生成待加密的字符串
		String HMacXml = YeePayXMLHandler.createResponseHMAC_cwbSign(respMap);
		// 加密后的字符串
		String hmac = md5.encodePassword(HMacXml + yeePay.getPrivatekey(), null);
		// 把签名后的数据放在map中用来返回最后的数据
		respMap.put("hmac", hmac);

		return respMap;
	}

	/**
	 * 交易撤回接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toBackOut(String service_code, YeepayRequest rootnote, YeePay yeePay) {

		YeePayRespNote respNote = new YeePayRespNote();
		respNote = super.BuildYeePayRespClass(rootnote, respNote);
		double backOutAmount = rootnote.getSessionBody().getExtendAtt().getAMT(); // 撤销金额

		respNote = ValidateOrderNoBackOut(respNote, backOutAmount);
		if (respNote.getResult_code() == null) {
			// 撤销操作
			String deliverstateremark = "撤销交易";
			try {
				cwbOrderService.deliverStatePodCancel(respNote.getOrder_No(), respNote.getBranchid(), respNote.getDeliverid(), deliverstateremark, 0);
				respNote.setResult_code("2");
				respNote.setResult_msg("操作成功");
				logger.info("yeepay运单撤销成功,单号={},小件员={}", respNote.getOrder_No(), respNote.getDeliverid());
			} catch (Exception e) {
				logger.error("yeepay运单撤销失败,更新数据失败,单号：" + respNote.getOrder_No() + ",小件员：" + respNote.getDeliverid() + "异常原因=", e);
			}
			posPayDAO.save_PosTradeDetailRecord(respNote.getOrder_No(), deliverstateremark, 0, respNote.getDeliverid(), 1, deliverstateremark, "", 0, "", 1, 4, "single", PosEnum.YeePay.getMethod(),
					0, "");

		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_backOut(service_code, rootnote, respNote, yeePay);
		String responseXml = YeePayXMLHandler.createResponseXML_backOut(respMap);
		logger.info("返回yeepay数据成功。业务编码={},XML={}", service_code, responseXml);

		return responseXml;
	}

	private YeePayRespNote ValidateOrderNoBackOut(YeePayRespNote respNote, double backOutAmount) {
		if (respNote.getCwbOrder() == null) {
			respNote.setResult_code("3");
			respNote.setResult_msg("查无此单");

		} else if (respNote.getDeliverstate().getReceivedfee().doubleValue() == 0) {
			respNote.setResult_code("3");
			respNote.setResult_msg("非POS刷卡不能撤销");

		}
		// else if(respNote.getDeliverstate().getSign_typeid()==1){
		// respNote.setResult_code("3");
		// respNote.setResult_msg("订单已签收，不可撤销");
		//
		// }

		else if (backOutAmount != respNote.getDeliverstate().getReceivedfee().doubleValue()) {
			respNote.setResult_code("3");
			respNote.setResult_msg("撤销金额与原金额不同");
		}
		return respNote;
	}

	private Map<String, String> convertMapType_backOut(String service_code, YeepayRequest rootnote, YeePayRespNote respNote, YeePay yeePay) {
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
		respMap.put("order_no", respNote.getOrder_No());

		// 生成待加密的字符串
		String HMacXml = YeePayXMLHandler.createResponseHMAC_backOut(respMap);
		// 加密后的字符串
		String hmac = md5.encodePassword(HMacXml + yeePay.getPrivatekey(), null);
		// 把签名后的数据放在map中用来返回最后的数据
		respMap.put("hmac", hmac);

		return respMap;
	}

}
