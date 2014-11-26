package cn.explink.pos.bill99;

import java.util.Map;

public class Bill99XMLHandler {

	/**
	 * 创建登陆时要进行加密的字符串 (MAC)
	 * 
	 * @param loginmap
	 * @return String
	 */
	public static String createMACXML_Login(Map loginmap) {
		String data = "<Transaction_Body>" + "<delivery_man>" + loginmap.get("delivery_man") + "</delivery_man>" + "<delivery_name>" + loginmap.get("delivery_name") + "</delivery_name>"
				+ "</Transaction_Body>";
		return data.replace("null", "");
	}

	/**
	 * 创建响应登陆XML文件
	 * 
	 * @param paymentmap
	 * @return String
	 */
	public static String createXMLMessage_Login(Map loginmap) {
		String data = "<?xml version='1.0' encoding='UTF-8'?>" + "<Transaction>" + "<Transaction_Header>" + "<version>" + loginmap.get("version") + "</version>" + "<transaction_sn>"
				+ loginmap.get("transaction_sn") + "</transaction_sn>" + "<transaction_id>" + loginmap.get("transaction_id") + "</transaction_id>" + "<requester>" + loginmap.get("requester")
				+ "</requester>" + "<target>" + loginmap.get("target") + "</target>" + "<resp_time>" + loginmap.get("resp_time") + "</resp_time>" + "<resp_code>" + loginmap.get("resp_code")
				+ "</resp_code>" + "<resp_msg>" + loginmap.get("resp_msg") + "</resp_msg>" + "<ext_attributes>" + "<delivery_dept_no>" + loginmap.get("delivery_dept_no") + "</delivery_dept_no>"
				+ "<delivery_dept>" + loginmap.get("delivery_dept") + "</delivery_dept>" + "</ext_attributes>" + "<MAC>" + loginmap.get("MAC") + "</MAC>" + "</Transaction_Header>"
				+ "<Transaction_Body>" + "<delivery_man>" + loginmap.get("delivery_man") + "</delivery_man>" + "<delivery_name>" + loginmap.get("delivery_name") + "</delivery_name>"
				+ "</Transaction_Body>" + "</Transaction>";
		return data.replace("null", "");
	}

	/**
	 * 创建运单查询的MAC字符串
	 *
	 * @param searchcwbmap
	 * @return String
	 */
	public static String createMACXML_SearchCwb(Map searchcwbmap) {
		StringBuffer macstr = new StringBuffer("");
		macstr.append("" +

		"<Transaction_Body>" + "<orderId>").append(searchcwbmap.get("order_no")).append("</orderId>" + "<amt>").append(searchcwbmap.get("amt")).append("</amt>" + "</Transaction_Body>");
		return macstr.toString().replace("null", "");

	}

	/**
	 * 创建运单查询xml文件
	 *
	 * @param searchcwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_SearchCwb(Map searchcwbmap) {
		StringBuffer xmlstr = new StringBuffer("");
		xmlstr.append("<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>" + searchcwbmap.get("version") + "</version>" + "<transaction_sn>")
				.append(searchcwbmap.get("transaction_sn")).append("</transaction_sn>" + "<transaction_id>").append(searchcwbmap.get("transaction_id")).append("</transaction_id>" + "<requester>")
				.append(searchcwbmap.get("requester")).append("</requester>" + "<target>").append(searchcwbmap.get("target")).append("</target>" + "<resp_time>").append(searchcwbmap.get("resp_time"))
				.append("</resp_time>" + "<resp_code>").append(searchcwbmap.get("resp_code")).append("</resp_code>" + "<resp_msg>").append(searchcwbmap.get("resp_msg"))
				.append("</resp_msg>" + "<MAC>").append(searchcwbmap.get("MAC")).append("</MAC>" + "<ext_attributes>" + "<consignee>").append(searchcwbmap.get("consignee"))
				.append("</consignee>" + "<consignee_address>").append(searchcwbmap.get("consignee_address")).append("</consignee_address>" + "<consignee_contact>")
				.append(searchcwbmap.get("consignee_contact")).append("</consignee_contact>" + "<consignee_id>").append(searchcwbmap.get("consignee_id")).append("</consignee_id>" + "<payee_id>")
				.append(searchcwbmap.get("payee_id")).append("</payee_id>" + "</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<orderId>").append(searchcwbmap.get("order_no"))
				.append("</orderId>" + "<amt>").append(searchcwbmap.get("amt")).append("</amt>" + "</Transaction_Body>" + "</Transaction>");
		return xmlstr.toString().replace("null", "");

	}

	/**
	 * 创建： 支付结果 MAC字符串
	 *
	 * @param cwbmap
	 * @return String
	 */
	public static String createMACXML_payAmount(Map cwbmap) {
		String macstr = "<Transaction_Body>" + "<order_no>" + cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>";
		return macstr.replace("null", "");
	}

	/**
	 * 创建： 响应支付结果 的XML文件
	 *
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_payAmount(Map cwbmap) {
		String xmlstr = "<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>" + cwbmap.get("version") + "</version>" + "<transaction_sn>"
				+ cwbmap.get("transaction_sn") + "</transaction_sn>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>" + cwbmap.get("requester")
				+ "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>" + cwbmap.get("resp_code")
				+ "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>"
				+ cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return xmlstr.replace("null", "");
	}

	/**
	 * 创建： 响应 派件异常 的XML文件
	 *
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_cwbSign(Map cwbmap) {
		String xmlstr = "<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>" + cwbmap.get("version") + "</version>" + "<transaction_sn>"
				+ cwbmap.get("transaction_sn") + "</transaction_sn>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>" + cwbmap.get("requester")
				+ "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>" + cwbmap.get("resp_code")
				+ "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>"
				+ cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return xmlstr.replace("null", "");
	}

	/**
	 * 创建： 响应 派件异常 的XML文件
	 *
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_toExptFeedBack(Map cwbmap) {
		String xmlstr = "<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>" + cwbmap.get("version") + "</version>" + "<transaction_sn>"
				+ cwbmap.get("transaction_sn") + "</transaction_sn>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>" + cwbmap.get("requester")
				+ "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>" + cwbmap.get("resp_code")
				+ "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>"
				+ cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return xmlstr.replace("null", "");
	}

	/**
	 * 创建： 响应 撤销交易 的XML文件
	 *
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_toBackOut(Map cwbmap) {
		String xmlstr = "<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>" + cwbmap.get("version") + "</version>" + "<transaction_sn>"
				+ cwbmap.get("transaction_sn") + "</transaction_sn>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>" + cwbmap.get("requester")
				+ "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>" + cwbmap.get("resp_code")
				+ "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>"
				+ cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return xmlstr.replace("null", "");
	}

	public static void main(String[] args) {
		String xmlDOC = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<Transaction>" + "<Transaction_Header>" + "<transaction_sn>200908101234</transaction_sn>"
				+ "<transaction_id>MI0001</transaction_id>" + "<requester>0000000000</requester>" + "<target>0000000039</target>" + "<request_time>20090911121212</request_time>"
				+ "<MAC>L38N6uOqnVwj9bg8fb8MKqMZLhr1tcJKkM8FmwnuRDf</MAC>" + "</Transaction_Header>" + "<Transaction_Body>" + "<delivery_man>600932</delivery_man>"
				+ "<password>DE38DJFNGKER3 </password>" + "</Transaction_Body>" + "</Transaction>";
		String xmltrimStr = xmlDOC;
		String xmlsE = xmltrimStr.substring(xmltrimStr.indexOf("<Transaction_Body>"), xmltrimStr.lastIndexOf("</Transaction_Body>") + 19);
		System.out.println(xmlsE);
		System.out.println(xmlDOC);
	}
}
