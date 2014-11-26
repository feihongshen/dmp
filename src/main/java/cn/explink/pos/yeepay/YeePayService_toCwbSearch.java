package cn.explink.pos.yeepay;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.explink.pos.yeepay.xml.YeepayRequest;
import cn.explink.util.DateTimeUtil;

@Service
public class YeePayService_toCwbSearch extends YeePayService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Md5PasswordEncoder md5 = new Md5PasswordEncoder();

	/**
	 * 运单查询接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toCwbSearch(String service_code, YeepayRequest rootnote, YeePay yeePay) {

		YeePayRespNote respNote = new YeePayRespNote();

		respNote = super.BuildYeePayRespClass(rootnote, respNote);
		if (respNote.getCwbOrder() == null) {
			respNote.setResult_code("20");
			respNote.setResult_msg("查无此单");
			respNote.setOrder_Status("20");
			respNote.setOrder_Status_Msg("查无此单");
			logger.warn("yeepay运单查询查无此人：", respNote.getEmployee_ID());

		} else {

			respNote.setResult_code("2");
			respNote.setResult_msg("成功接收");

			respNote.setOrder_Status("23");
			respNote.setOrder_Status_Msg("未支付，未签收");
			if (respNote.getDeliverstate() != null) {
				if (respNote.getDeliverstate().getSign_typeid() == 1) {
					respNote.setOrder_Status("21");
					respNote.setOrder_Status_Msg("订单已签收");
				} else if ((respNote.getDeliverstate().getReceivedfee().doubleValue() > 0) && respNote.getDeliverstate().getSign_typeid() == 0) {
					respNote.setOrder_Status("22");
					respNote.setOrder_Status_Msg("已收款,未签收");
				}
			}

			if ("".equals(respNote.getCwbOrder().getCwbremark())) {
				respNote.setSignStandard(respNote.getCwbOrder().getCustomercommand());
			} else {
				respNote.setSignStandard(respNote.getCwbOrder().getCwbremark() + "," + respNote.getCwbOrder().getCustomercommand());
			}

		}
		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_cwbSearch(service_code, rootnote, respNote, yeePay);
		String responseXml = YeePayXMLHandler.createResponseXML_cwbSearch(respMap);
		logger.info("返回yeepay数据业务编码={},XML={}", service_code, responseXml);
		return responseXml;
	}

	private Map<String, String> convertMapType_cwbSearch(String service_code, YeepayRequest rootnote, YeePayRespNote respnote, YeePay yeePay) {
		Map<String, String> respMap = new HashMap<String, String>();
		String result_time = DateTimeUtil.getNowTime("yyyyMMddHHmmss");

		respMap.put("version", rootnote.getSessionHead().getVersion()); // 版本号
		respMap.put("servicecode", service_code); // 请求的业务编号
		respMap.put("transactionid", rootnote.getSessionHead().getTransactionID()); // 交易流水号
		respMap.put("srcsysid", yeePay.getRequester()); // 请求方
		respMap.put("dstsysid", yeePay.getTargeter()); // 响应方
		respMap.put("result_code", respnote.getResult_code()); // 响应结果编码
		respMap.put("result_msg", respnote.getResult_msg()); // 响应结果消息
		respMap.put("result_time", result_time); // 响应时间
		respMap.put("employee_id", rootnote.getSessionBody().getEmployee_ID());
		respMap.put("order_no", respnote.getOrder_No());
		respMap.put("signstandard", respnote.getSignStandard());
		respMap.put("receiver_name", respnote.getCwbOrder().getConsigneename());
		respMap.put("receiver_addr", respnote.getCwbOrder().getConsigneeaddress());
		respMap.put("receiver_tel", respnote.getCwbOrder().getConsigneemobile());
		respMap.put("receiver_orderno", respnote.getCwbOrder().getCwb());
		respMap.put("order_amt", respnote.getCwbOrder().getReceivablefee() + "");
		respMap.put("biz_name", posPayService.getCustomerNameByCustomerId(respnote.getCwbOrder().getCustomerid()));
		respMap.put("amt", respnote.getCwbOrder().getReceivablefee() + "");
		respMap.put("sub_station_code", respnote.getBranchid() + "");
		respMap.put("sub_station_name", posPayService.getBranchNameById(respnote.getBranchid()));
		respMap.put("serial_no", respnote.getCwbOrder().getOpscwbid() + "");
		respMap.put("sorting_name", "");
		respMap.put("weight", respnote.getCwbOrder().getCarrealweight() + "");
		respMap.put("checked_items", respnote.getCwbOrder().getSendcarname());
		respMap.put("biz_code", "");
		respMap.put("pa_details", "");
		respMap.put("pc_autosplit", "");
		respMap.put("order_status_msg", respnote.getOrder_Status_Msg());
		respMap.put("order_status", respnote.getOrder_Status());
		// 生成待加密的字符串
		String HMacXml = YeePayXMLHandler.createResponseXMLHMAC_cwbSearch(respMap);
		// 加密后的字符串
		String hmac = md5.encodePassword(HMacXml + yeePay.getPrivatekey(), null);
		// 把签名后的数据放在map中用来返回最后的数据
		respMap.put("hmac", hmac);

		return respMap;
	}

}
