package cn.explink.pos.alipay;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.poscodeMapp.PoscodeMapp;
import cn.explink.pos.alipay.xml.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class AlipayService_toCwbSearch extends AlipayService {
	private Logger logger = LoggerFactory.getLogger(AlipayService_toCwbSearch.class);

	/**
	 * 运单查询
	 * 
	 * @return
	 */
	public String toCwbSearch(Transaction rootnote, AliPay alipay) {
		AlipayRespNote alipayRespNote = new AlipayRespNote();
		try {
			alipayRespNote = super.BuildAlipayRespClass(rootnote, alipayRespNote);
			if (alipayRespNote.getCwbOrder() == null) {
				alipayRespNote.setResp_code(AliPayExptMessageEnum.ChaXunYiChang.getResp_code());
				alipayRespNote.setResp_msg(AliPayExptMessageEnum.ChaXunYiChang.getResp_msg());
				logger.error("alipay运单查询没有检索到数据，当前小件员：", alipayRespNote.getDelivery_man());
			} else {

				if (alipayRespNote.getDeliverstate().getSign_typeid() == 1) {
					alipayRespNote.setResp_code(AliPayExptMessageEnum.DingDanYiQianShou.getResp_code());
					alipayRespNote.setResp_msg(AliPayExptMessageEnum.DingDanYiQianShou.getResp_msg());
					logger.info("alipay运单查询:订单已签收,当前小件员:" + alipayRespNote.getDelivery_man());
				} else if ((alipayRespNote.getDeliverstate().getReceivedfee().doubleValue() > 0) && alipayRespNote.getDeliverstate().getSign_typeid() == 0) {
					alipayRespNote.setResp_code(AliPayExptMessageEnum.YiShouKuanWeiQianShou.getResp_code());
					alipayRespNote.setResp_msg(AliPayExptMessageEnum.YiShouKuanWeiQianShou.getResp_msg());
					logger.info("alipay运单查询:已收款,未签收,当前小件员:" + alipayRespNote.getDelivery_man());
				} else {
					alipayRespNote.setResp_code(AliPayExptMessageEnum.Success.getResp_code());
					alipayRespNote.setResp_msg(AliPayExptMessageEnum.Success.getResp_msg());
					logger.info("alipay运单查询:未收款,未签收,当前小件员:" + alipayRespNote.getDelivery_man());
				}
				alipayRespNote = SearchCwbDetailByAlipay(alipayRespNote); // 查询其他信息
			}
		} catch (Exception e) {
			logger.error("Alipay运单查询未知异常!", e);
			alipayRespNote.setResp_code(AliPayExptMessageEnum.QiTaShiBai.getResp_code());
			alipayRespNote.setResp_code(AliPayExptMessageEnum.QiTaShiBai.getResp_code());
		}

		Map<String, String> retMap = convertMapType_cwbSearch(alipayRespNote, alipay, rootnote);
		// 生成响应报文
		String responseXml = AlipayXMLHandler.createXMLMessage_SearchCwb(retMap);
		logger.info("[" + rootnote.getTransaction_Header().getTransaction_id() + "]返回XML:" + responseXml);
		return responseXml;
	}

	/**
	 * 查询其他信息
	 * 
	 * @param alipayRespNote
	 * @return
	 */
	private AlipayRespNote SearchCwbDetailByAlipay(AlipayRespNote alipayRespNote) {
		String consignee_contact = alipayRespNote.getCwbOrder().getConsigneemobile() + "," + alipayRespNote.getCwbOrder().getConsigneephone();

		if (consignee_contact.length() > 30) {
			alipayRespNote.setConsignee_contact(consignee_contact.substring(0, 30));
		}
		String consignee = alipayRespNote.getCwbOrder().getConsigneename();
		if (consignee.length() > 32) {
			alipayRespNote.setConsignee(consignee.substring(0, 32));
		} else {
			alipayRespNote.setConsignee(consignee);
		}

		PoscodeMapp customer = poscodeMappDAO.getPosCodeByKey(alipayRespNote.getCwbOrder().getCustomerid(), PosEnum.AliPay.getKey());
		String customercode = "";
		if (customer != null) {
			customercode = customer.getCustomercode();
		}
		alipayRespNote.setMerchant_code(customercode); // 支付方判断.

		// TBSC 淘宝客户
		if (alipayRespNote.getMerchant_code().equals("TBSC") || alipayRespNote.getMerchant_code().equals("TBWL")) {
			if (alipayRespNote.getCwbOrder().getReceivablefee().doubleValue() > 0) { // 是淘宝的订单
				alipayRespNote.setMerchant_biz_type("03");// 淘宝COD订单
			} else {
				alipayRespNote.setMerchant_biz_type("04");// 淘宝非COD订单
			}
		} else {// 不是淘宝的订单
			if (alipayRespNote.getCwbOrder().getReceivablefee().doubleValue() > 0) { // 是淘宝的订单
				alipayRespNote.setMerchant_biz_type("01");// 非淘宝COD订单
			} else {
				alipayRespNote.setMerchant_biz_type("02");// 非淘宝非COD订单
			}
		}
		return alipayRespNote;
	}

	private Map<String, String> convertMapType_cwbSearch(AlipayRespNote alipayRespNote, AliPay alipay, Transaction rootnote) {

		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map

		retMap.put("transaction_id", alipayRespNote.getTransaction_id());
		retMap.put("resp_code", alipayRespNote.getResp_code());
		retMap.put("resp_msg", alipayRespNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime());
		retMap.put("requester", alipay.getRequester());
		retMap.put("target", alipay.getTargeter());
		retMap.put("consignee", alipayRespNote.getCwbOrder() == null ? "" : alipayRespNote.getConsignee());
		retMap.put("consignee_address", alipayRespNote.getCwbOrder() == null ? "" : alipayRespNote.getCwbOrder().getConsigneeaddress());
		retMap.put("consignee_contact", alipayRespNote.getCwbOrder() == null ? "" : alipayRespNote.getConsignee_contact());
		retMap.put("amt", alipayRespNote.getCwbOrder() == null ? "0" : alipayRespNote.getCwbOrder().getReceivablefee() + "");
		retMap.put("order_no", alipayRespNote.getCwbOrder() == null ? rootnote.getTransaction_Body().getOrder_no() : alipayRespNote.getCwbOrder().getCwb());
		retMap.put("account_keyword", "");// 分账标识符,暂为空
		retMap.put("merchant_code", alipayRespNote.getMerchant_code());// 配送商户编号
		retMap.put("merchant_biz_no", alipayRespNote.getCwbOrder() == null ? rootnote.getTransaction_Body().getOrder_no() : alipayRespNote.getCwbOrder().getCwb());// B2C商户订单号
		retMap.put("merchant_biz_type", alipayRespNote.getMerchant_biz_type());// B2C商户订单类型
		// 生成待加密的字符串
		String str = AlipayXMLHandler.createMACXML_SearchCwb(retMap);
		String r = CreateRespSign(alipay, str);
		retMap.put("MAC", r);
		return retMap;
	}

}
