package cn.explink.pos.alipayCodApp;

import java.util.List;
import java.util.Map;

import cn.explink.b2c.tools.ExptReason;
import cn.explink.domain.Reason;

public class AlipayCodAppXMLHandler {

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
		xmlstr.append("<logistics_bill_no>" + map.get("logistics_bill_no") + "</logistics_bill_no>");
		xmlstr.append("<delivery_name>" + map.get("delivery_name") + "</delivery_name>");
		xmlstr.append("<consignee_contact>" + map.get("consignee_contact") + "</consignee_contact>");
		xmlstr.append("<consigee>" + map.get("consigee") + "</consigee>");
		xmlstr.append("<consigee_address>" + map.get("consigee_address") + "</consigee_address>");
		xmlstr.append("<delivery_mobile>" + map.get("delivery_mobile") + "</delivery_mobile>");
		xmlstr.append("<steps>" + map.get("steps") + "</steps>");
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
		xmlstr.append("<is_success>T</is_success>");
		xmlstr.append("<sign>" + sign + "</sign>");
		xmlstr.append("<sign_type>" + sign_type + "</sign_type>");
		xmlstr.append("</alipay>");

		return xmlstr.toString();

	}

}
