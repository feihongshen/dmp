package cn.explink.pos.alipayapp;

import java.util.List;
import java.util.Map;

import cn.explink.b2c.tools.ExptReason;
import cn.explink.domain.Reason;

public class AlipayappXMLHandler {

	/**
	 * 创建运单查询的MAC字符串
	 * 
	 * @param searchcwbmap
	 * @return String
	 */
	public static String createXML_SearchCwb(Map<String, String> map, String sign, String sign_type) {
		StringBuffer xmlstr = new StringBuffer("");

		xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xmlstr.append("<alipay>");
		xmlstr.append("<response>");
		xmlstr.append("<is_success>T</is_success>");
		xmlstr.append("<amount>" + map.get("amount") + "</amount>");
		xmlstr.append("<deliver_mobile>" + map.get("deliver_mobile") + "</deliver_mobile>");
		xmlstr.append("<payable>" + map.get("payable") + "</payable>");
		xmlstr.append("<amt_can_modify>" + map.get("amt_can_modify") + "</amt_can_modify>");
		xmlstr.append("<goods_name>" + map.get("goods_name") + "</goods_name>");
		if (map.get("out_merchant_code") != null) {
			xmlstr.append("<out_merchant_code>" + map.get("out_merchant_code") + "</out_merchant_code>");
			xmlstr.append("<out_order_no>" + map.get("out_order_no") + "</out_order_no>");
		}

		xmlstr.append("<sub_account_no>" + map.get("sub_account_no") + "</sub_account_no>");

		xmlstr.append("</response>");
		xmlstr.append("<sign>" + sign + "</sign>");
		xmlstr.append("<sign_type>" + sign_type + "</sign_type>");
		xmlstr.append("</alipay>");

		return xmlstr.toString();
	}

	/**
	 * 创建 异常状态返回的xml
	 */
	public static String createXMLException(String error, String sign, String sign_type) {
		StringBuffer xmlstr = new StringBuffer("");

		xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xmlstr.append("<alipay>");
		xmlstr.append("<response>");
		xmlstr.append("<is_success>F</is_success>");
		xmlstr.append("<error>" + error + "</error>");
		xmlstr.append("</response>");
		xmlstr.append("<sign>" + sign + "</sign>");
		xmlstr.append("<sign_type>" + sign_type + "</sign_type>");
		xmlstr.append("</alipay>");

		return xmlstr.toString();

	}

	/**
	 * 创建正常返回的支付XML
	 */
	public static String createXMLPayAmount(String sign, String sign_type) {
		StringBuffer xmlstr = new StringBuffer("");

		xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xmlstr.append("<alipay>");
		xmlstr.append("<response>");
		xmlstr.append("<is_success>T</is_success>");
		xmlstr.append("</response>");
		xmlstr.append("<sign>" + sign + "</sign>");
		xmlstr.append("<sign_type>" + sign_type + "</sign_type>");
		xmlstr.append("</alipay>");

		return xmlstr.toString();

	}

}
