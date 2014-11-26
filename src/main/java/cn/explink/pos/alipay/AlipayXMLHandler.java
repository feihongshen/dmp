package cn.explink.pos.alipay;

import java.util.List;
import java.util.Map;

import cn.explink.b2c.tools.ExptReason;
import cn.explink.domain.Reason;

public class AlipayXMLHandler {

	/**
	 * 验证请求登录的信息
	 * 
	 * @param loginmap
	 * @return
	 */
	public static String checkCreateMACXML_Login(Map loginmap) {
		StringBuffer str = new StringBuffer("");
		str.append("<Transaction_Header>" + "<transaction_id>" + loginmap.get("transaction_id") + "</transaction_id>" + "<requester>" + loginmap.get("requester") + "</requester>" + "<target>"
				+ loginmap.get("target") + "</target>" + "<request_time>" + loginmap.get("request_time") + "</request_time>" + "<version>" + loginmap.get("version") + "</version>"
				+ "</Transaction_Header>" + "<Transaction_Body>" + "<delivery_man>" + loginmap.get("delivery_man") + "</delivery_man>" + "<password>" + loginmap.get("password") + "</password>"
				+ "</Transaction_Body>");
		return str.toString().replace("null", "");
	}

	/**
	 * 创建登陆时要进行加密的字符串 (MAC)
	 * 
	 * @param loginmap
	 * @return String
	 */
	public static String createMACXML_Login(Map loginmap) {
		StringBuffer str = new StringBuffer("");
		str.append("<Transaction_Header>" + "<transaction_id>").append(loginmap.get("transaction_id")).append("</transaction_id>" + "<requester>").append(loginmap.get("requester"))
				.append("</requester>" + "<target>").append(loginmap.get("target")).append("</target>" + "<resp_time>").append(loginmap.get("resp_time")).append("</resp_time>" + "<resp_code>")
				.append(loginmap.get("resp_code")).append("</resp_code>" + "<resp_msg>").append(loginmap.get("resp_msg")).append("</resp_msg>" + "<ext_attributes>" + "<delivery_dept>")
				.append(loginmap.get("delivery_dept")).append("</delivery_dept>" + "<delivery_dept_no>").append(loginmap.get("delivery_dept_no"))
				.append("</delivery_dept_no>" + "</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<delivery_man>").append(loginmap.get("delivery_man"))
				.append("</delivery_man>" + "<delivery_name>").append(loginmap.get("delivery_name")).append("</delivery_name>" + "<delivery_zone>").append(loginmap.get("delivery_zone"))
				.append("</delivery_zone>" + "</Transaction_Body>");
		return str.toString().replace("null", "");
	}

	/**
	 * 创建响应登陆XML文件
	 * 
	 * @param paymentmap
	 * @return String
	 */
	public static String createXMLMessage_Login(Map loginmap) {
		StringBuffer str = new StringBuffer("");
		str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<transaction_id>").append(loginmap.get("transaction_id"))
				.append("</transaction_id>" + "<requester>").append(loginmap.get("requester")).append("</requester>" + "<target>").append(loginmap.get("target")).append("</target>" + "<resp_time>")
				.append(loginmap.get("resp_time")).append("</resp_time>" + "<resp_code>").append(loginmap.get("resp_code")).append("</resp_code>" + "<resp_msg>").append(loginmap.get("resp_msg"))
				.append("</resp_msg>" + "<MAC>").append(loginmap.get("MAC")).append("</MAC>" + "<ext_attributes>" + "<delivery_dept>").append(loginmap.get("delivery_dept"))
				.append("</delivery_dept>" + "<delivery_dept_no>").append(loginmap.get("delivery_dept_no"))
				.append("</delivery_dept_no>" + "</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<delivery_man>").append(loginmap.get("delivery_man"))
				.append("</delivery_man>" + "<delivery_name>").append(loginmap.get("delivery_name")).append("</delivery_name>" + "<delivery_zone>").append(loginmap.get("delivery_zone"))
				.append("</delivery_zone>" + "</Transaction_Body>" + "</Transaction>");
		return str.toString().replace("null", "");
	}

	/**
	 * 创建运单查询的MAC字符串
	 * 
	 * @param searchcwbmap
	 * @return String
	 */
	public static String createMACXML_SearchCwb(Map searchcwbmap) {
		StringBuffer macstr = new StringBuffer("");
		macstr.append("" + "<Transaction_Header>" + "<transaction_id>").append(searchcwbmap.get("transaction_id")).append("</transaction_id>" + "<requester>").append(searchcwbmap.get("requester"))
				.append("</requester>" + "<target>").append(searchcwbmap.get("target")).append("</target>" + "<resp_time>").append(searchcwbmap.get("resp_time"))
				.append("</resp_time>" + "<resp_code>").append(searchcwbmap.get("resp_code")).append("</resp_code>" + "<resp_msg>").append(searchcwbmap.get("resp_msg"))
				.append("</resp_msg>" + "<ext_attributes>" + "<consignee>").append(searchcwbmap.get("consignee")).append("</consignee>" + "<consignee_address>")
				.append(searchcwbmap.get("consignee_address")).append("</consignee_address>" + "<consignee_contact>").append(searchcwbmap.get("consignee_contact"))
				.append("</consignee_contact>" + "</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>").append(searchcwbmap.get("order_no"))
				.append("</order_no>" + "<amt>").append(searchcwbmap.get("amt")).append("</amt>" + "<merchant_code>").append(searchcwbmap.get("merchant_code"))
				.append("</merchant_code>" + "<account_keyword>").append(searchcwbmap.get("account_keyword")).append("</account_keyword>" + "<merchant_biz_no>")
				.append(searchcwbmap.get("merchant_biz_no")).append("</merchant_biz_no>" + "<merchant_biz_type>").append(searchcwbmap.get("merchant_biz_type"))
				.append("</merchant_biz_type>" + "</Transaction_Body>");

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
		xmlstr.append("<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<transaction_id>").append(searchcwbmap.get("transaction_id"))
				.append("</transaction_id>" + "<requester>").append(searchcwbmap.get("requester")).append("</requester>" + "<target>").append(searchcwbmap.get("target"))
				.append("</target>" + "<resp_time>").append(searchcwbmap.get("resp_time")).append("</resp_time>" + "<resp_code>").append(searchcwbmap.get("resp_code"))
				.append("</resp_code>" + "<resp_msg>").append(searchcwbmap.get("resp_msg")).append("</resp_msg>" + "<MAC>").append(searchcwbmap.get("MAC"))
				.append("</MAC>" + "<ext_attributes><consignee>").append(searchcwbmap.get("consignee")).append("</consignee>" + "<consignee_address>").append(searchcwbmap.get("consignee_address"))
				.append("</consignee_address>" + "<consignee_contact>").append(searchcwbmap.get("consignee_contact"))
				.append("</consignee_contact>" + "</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>").append(searchcwbmap.get("order_no"))
				.append("</order_no>" + "<amt>").append(searchcwbmap.get("amt")).append("</amt>" + "<merchant_code>").append(searchcwbmap.get("merchant_code"))
				.append("</merchant_code>" + "<account_keyword>").append(searchcwbmap.get("account_keyword")).append("</account_keyword>" + "<merchant_biz_no>")
				.append(searchcwbmap.get("merchant_biz_no")).append("</merchant_biz_no>" + "<merchant_biz_type>").append(searchcwbmap.get("merchant_biz_type"))
				.append("</merchant_biz_type>" + "</Transaction_Body>" + "</Transaction>");
		return xmlstr.toString().replace("null", "");

	}

	/**
	 * 创建： 支付结果 撤销交易 派件异常 签收结果 的MAC字符串
	 * 
	 * @param cwbmap
	 * @return String
	 */
	public static String createMACXML_payAmount(Map cwbmap) {
		String macstr = "<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>" + cwbmap.get("requester") + "</requester>" + "<target>"
				+ cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>" + cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>"
				+ cwbmap.get("resp_msg") + "</resp_msg>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>";
		return macstr.replace("null", "");
	}

	public static String createMACXML_cwbSign(Map cwbmap) {
		String macstr = "<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>" + cwbmap.get("requester") + "</requester>" + "<target>"
				+ cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>" + cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>"
				+ cwbmap.get("resp_msg") + "</resp_msg>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>";
		return macstr.replace("null", "");
	}

	/**
	 * 创建： 响应支付结果 撤销交易 派件异常 签收结果 的XML文件
	 * 
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_payAmount(Map cwbmap) {
		String xmlstr = "<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>"
				+ cwbmap.get("requester") + "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>"
				+ cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>"
				+ "<Transaction_Body>" + "<order_no>" + cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return xmlstr.replace("null", "");
	}

	/**
	 * 创建： 响应 分单支付结果反馈 的XML文件
	 * 
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_PayFinish(Map cwbmap) {
		String xmlstr = "<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>"
				+ cwbmap.get("requester") + "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>"
				+ cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>"
				+ "<Transaction_Body>" + "<order_no>" + cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return xmlstr.replace("null", "");
	}

	/**
	 * 创建： 响应 分单撤销结果反馈 的XML文件
	 * 
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_BackOutFinish(Map cwbmap) {
		String xmlstr = "<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>"
				+ cwbmap.get("requester") + "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>"
				+ cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>"
				+ "<Transaction_Body>" + "<order_no>" + cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return xmlstr.replace("null", "");
	}

	/**
	 * 创建： 响应支付结果 撤销交易 派件异常 签收结果 的XML文件
	 * 
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_cwbSign(Map cwbmap) {
		String xmlstr = "<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>"
				+ cwbmap.get("requester") + "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>"
				+ cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>"
				+ "<Transaction_Body>" + "<order_no>" + cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return xmlstr.replace("null", "");
	}

	/**
	 * 创建： 分单支付完成结果反馈 的MAC字符串
	 * 
	 * @param cwbmap
	 * @return String
	 */
	public static String createMACXML_PayFinish(Map cwbmap) {
		String macstr = "<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>" + cwbmap.get("requester") + "</requester>" + "<target>"
				+ cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>" + cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>"
				+ cwbmap.get("resp_msg") + "</resp_msg>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>";
		return macstr.replace("null", "");
	}

	// 验证查单的信息
	public static String checkCreateMACXML_cwbSearch(Map searchmap) {
		StringBuffer str = new StringBuffer("");
		String logistics_alias = "";
		if (searchmap.get("logistics_alias") != null) {
			logistics_alias = "<logistics_alias>" + searchmap.get("logistics_alias") + "</logistics_alias>";
		}

		String merchant_biz_no = "";
		if (searchmap.get("merchant_biz_no") != null) {
			merchant_biz_no = "<merchant_biz_no>" + searchmap.get("merchant_biz_no") + "</merchant_biz_no>";
		}
		str.append("<Transaction_Header>" + "<transaction_id>" + searchmap.get("transaction_id") + "</transaction_id>" + "<requester>" + searchmap.get("requester") + "</requester>" + "<target>"
				+ searchmap.get("target") + "</target>" + "<request_time>" + searchmap.get("request_time") + "</request_time>" + "<version>" + searchmap.get("version") + "</version>"
				+ "<ext_attributes>" + "<delivery_man>" + searchmap.get("delivery_man") + "</delivery_man>" + logistics_alias + "</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>"
				+ "<order_no>" + searchmap.get("order_no") + "</order_no>" + merchant_biz_no + "</Transaction_Body>");
		return str.toString().replace("null", "");
	}

	// 验证派件支付
	public static String checkCreateMACXML_toPayAmount(Map payresultmap) {
		StringBuffer str = new StringBuffer("");

		String logistics_alias = "";
		if (payresultmap.get("logistics_alias") != null) {
			logistics_alias = payresultmap.get("logistics_alias").toString();
		}

		str.append("<Transaction_Header>" + "<transaction_id>" + payresultmap.get("transaction_id") + "</transaction_id>" + "<requester>" + payresultmap.get("requester")
				+ "</requester>"
				+ "<target>"
				+ payresultmap.get("target")
				+ "</target>"
				// +"<serial_no>"+payresultmap.get("serial_no")+"</serial_no>"
				// //20120116

				+ "<request_time>" + payresultmap.get("request_time") + "</request_time>" + "<version>" + payresultmap.get("version") + "</version>" + "<ext_attributes>" + "<delivery_dept_no>"
				+ payresultmap.get("delivery_dept_no") + "</delivery_dept_no>" + "<delivery_dept>" + payresultmap.get("delivery_dept") + "</delivery_dept>" + "<delivery_man>"
				+ payresultmap.get("delivery_man") + "</delivery_man>" + "<delivery_name>" + payresultmap.get("delivery_name") + "</delivery_name>");
		if (logistics_alias != null && !"".equals(logistics_alias)) {
			str.append("<logistics_alias>" + logistics_alias + "</logistics_alias>");
		}
		str.append("</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + payresultmap.get("order_no") + "</order_no>" + "<order_payable_amt>"
				+ payresultmap.get("order_payable_amt") + "</order_payable_amt>" + "<pay_type>" + payresultmap.get("pay_type") + "</pay_type>" + "<terminal_id>" + payresultmap.get("terminal_id")
				+ "</terminal_id>" + "<trace_no>" + payresultmap.get("trace_no") + "</trace_no>" + "</Transaction_Body>");
		return str.toString().replace("null", "");
	}

	// 验证签收
	public static String checkCreateMACXML_toCwbSign(Map signmap) {
		StringBuffer str = new StringBuffer("");
		String logistics_alias = "";
		if (signmap.get("logistics_alias") != null) {
			logistics_alias = signmap.get("logistics_alias").toString();
		}
		str.append("<Transaction_Header>" + "<transaction_id>" + signmap.get("transaction_id") + "</transaction_id>" + "<requester>" + signmap.get("requester") + "</requester>" + "<target>"
				+ signmap.get("target") + "</target>" + "<serial_no>"
				+ signmap.get("serial_no")
				+ "</serial_no>" // 20120116
				+ "<request_time>" + signmap.get("request_time") + "</request_time>" + "<version>" + signmap.get("version") + "</version>" + "<ext_attributes>" + "<delivery_dept_no>"
				+ signmap.get("delivery_dept_no") + "</delivery_dept_no>" + "<delivery_dept>" + signmap.get("delivery_dept") + "</delivery_dept>" + "<delivery_man>" + signmap.get("delivery_man")
				+ "</delivery_man>" + "<delivery_name>" + signmap.get("delivery_name") + "</delivery_name>");
		if (logistics_alias != null && !"".equals(logistics_alias)) {
			str.append("<logistics_alias>" + logistics_alias + "</logistics_alias>");
		}

		str.append("</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + signmap.get("order_no") + "</order_no>" + "<signee>" + signmap.get("signee") + "</signee>"
				+ "<consignee_sign_flag>" + signmap.get("consignee_sign_flag") + "</consignee_sign_flag>" + "</Transaction_Body>");
		return str.toString().replace("null", "");
	}

	// 验证异常反馈
	public static String checkCreateMACXML_toException(Map exceptionmap) {
		StringBuffer str = new StringBuffer("");
		String logistics_alias = "";
		if (exceptionmap.get("logistics_alias") != null) {
			logistics_alias = exceptionmap.get("logistics_alias").toString();
		}
		str.append("<Transaction_Header>" + "<transaction_id>" + exceptionmap.get("transaction_id") + "</transaction_id>" + "<requester>" + exceptionmap.get("requester") + "</requester>" + "<target>"
				+ exceptionmap.get("target") + "</target>"
				+ "<serial_no>"
				+ exceptionmap.get("serial_no")
				+ "</serial_no>" // 20120116
				+ "<request_time>" + exceptionmap.get("request_time") + "</request_time>" + "<version>" + exceptionmap.get("version") + "</version>" + "<ext_attributes>" + "<delivery_dept_no>"
				+ exceptionmap.get("delivery_dept_no") + "</delivery_dept_no>" + "<delivery_dept>" + exceptionmap.get("delivery_dept") + "</delivery_dept>" + "<delivery_man>"
				+ exceptionmap.get("delivery_man") + "</delivery_man>" + "<delivery_name>" + exceptionmap.get("delivery_name") + "</delivery_name>" + "<ex_packages>" + exceptionmap.get("ex_packages")
				+ "</ex_packages>");
		if (logistics_alias != null && !"".equals(logistics_alias)) {
			str.append("<logistics_alias>" + logistics_alias + "</logistics_alias>");
		}
		str.append("</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + exceptionmap.get("order_no") + "</order_no>" + "<ex_code>" + exceptionmap.get("ex_code")
				+ "</ex_code>" + "<ex_desc>" + exceptionmap.get("ex_desc") + "</ex_desc>" + "</Transaction_Body>");
		return str.toString().replace("null", "");
	}

	/**
	 * 创建： 响应 派件异常 的XML文件
	 * 
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_toExptFeedBack(Map cwbmap) {
		String xmlstr = "<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>"
				+ cwbmap.get("requester") + "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>"
				+ cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>"
				+ "<Transaction_Body>" + "<order_no>" + cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return xmlstr.replace("null", "");
	}

	/**
	 * 创建： 响应 撤销交易 的XML文件
	 * 
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_toBackOut(Map cwbmap) {
		String xmlstr = "<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>"
				+ cwbmap.get("requester") + "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<resp_code>"
				+ cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>"
				+ "<Transaction_Body>" + "<order_no>" + cwbmap.get("order_no") + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return xmlstr.replace("null", "");
	}

	/**
	 * 创建： 响应 异常编码查询 的XML文件
	 * 
	 * @param cwbmap
	 * @return String Object
	 */
	public static String createXMLMessage_searchExptCode(Map cwbmap, List<Reason> reasonlist, List<ExptReason> dangdangList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='utf-8' ?>" + "<Transaction>" + "<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>"
				+ cwbmap.get("requester") + "</requester>" + "<target>" + cwbmap.get("target") + "</target>" + "<resp_code>" + cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>"
				+ cwbmap.get("resp_msg") + "</resp_msg>" + "<resp_time>" + cwbmap.get("resp_time") + "</resp_time>" + "<MAC>" + cwbmap.get("MAC") + "</MAC>" + "</Transaction_Header>"
				+ "<Transaction_Body>");

		if (reasonlist != null && reasonlist.size() > 0) {
			sb.append("<item merchant_code=''>");
			for (Reason r : reasonlist) {
				sb.append("<exception code='" + (r.getReasonid() < 10 ? ("0" + r.getReasonid()) : (r.getReasonid() + "")) + "' msg='" + r.getReasoncontent() + "'/>");
			}
			sb.append("</item>");
		}
		// 当当的异常码
		if (dangdangList != null && dangdangList.size() > 0) {
			sb.append("<item merchant_code='" + dangdangList.get(0).getCustomercode() + "'>");
			for (ExptReason e : dangdangList) {
				sb.append("<exception code='" + e.getExpt_code() + "' msg='" + e.getExpt_msg() + "' />");
			}
			sb.append("</item>");
		}

		sb.append("</Transaction_Body>" + "</Transaction>");
		return sb.toString().replace("null", "");
	}

	// 验证分单支付完成
	public static String checkCreateMACXML_toPayFinish(Map comebackmap) {
		StringBuffer str = new StringBuffer("");
		String logistics_alias = "";
		if (comebackmap.get("logistics_alias") != null) {
			logistics_alias = comebackmap.get("logistics_alias").toString();
		}

		str.append("<Transaction_Header>" + "<transaction_id>" + comebackmap.get("transaction_id") + "</transaction_id>" + "<requester>" + comebackmap.get("requester") + "</requester>" + "<target>"
				+ comebackmap.get("target") + "</target>" + "<serial_no>"
				+ comebackmap.get("serial_no")
				+ "</serial_no>" // 20120116
				+ "<request_time>" + comebackmap.get("request_time") + "</request_time>" + "<version>" + comebackmap.get("version") + "</version>" + "<ext_attributes>" + "<delivery_dept_no>"
				+ comebackmap.get("delivery_dept_no") + "</delivery_dept_no>" + "<delivery_dept>" + comebackmap.get("delivery_dept") + "</delivery_dept>" + "<delivery_man>"
				+ comebackmap.get("delivery_man") + "</delivery_man>" + "<delivery_name>" + comebackmap.get("delivery_name") + "</delivery_name>");
		if (logistics_alias != null && !"".equals(logistics_alias)) {
			str.append("<logistics_alias>" + logistics_alias + "</logistics_alias>");
		}
		str.append("</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + comebackmap.get("order_no") + "</order_no>" + "<order_amt>" + comebackmap.get("order_amt")
				+ "</order_amt>");
		str.append("</Transaction_Body>");
		return str.toString().replace("null", "");
	}

	// 验证撤销完成
	public static String checkCreateMACXML_toBackOutFinish(Map comebackmap) {
		StringBuffer str = new StringBuffer("");
		String logistics_alias = "";
		if (comebackmap.get("logistics_alias") != null) {
			logistics_alias = comebackmap.get("logistics_alias").toString();
		}

		str.append("<Transaction_Header>" + "<transaction_id>" + comebackmap.get("transaction_id") + "</transaction_id>" + "<requester>" + comebackmap.get("requester") + "</requester>" + "<target>"
				+ comebackmap.get("target") + "</target>" + "<serial_no>"
				+ comebackmap.get("serial_no")
				+ "</serial_no>" // 20120116
				+ "<request_time>" + comebackmap.get("request_time") + "</request_time>" + "<version>" + comebackmap.get("version") + "</version>" + "<ext_attributes>" + "<delivery_dept_no>"
				+ comebackmap.get("delivery_dept_no") + "</delivery_dept_no>" + "<delivery_dept>" + comebackmap.get("delivery_dept") + "</delivery_dept>" + "<delivery_man>"
				+ comebackmap.get("delivery_man") + "</delivery_man>" + "<delivery_name>" + comebackmap.get("delivery_name") + "</delivery_name>");
		if (logistics_alias != null && !"".equals(logistics_alias)) {
			str.append("<logistics_alias>" + logistics_alias + "</logistics_alias>");
		}
		str.append("</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + comebackmap.get("order_no") + "</order_no>" + "<void_order_amt>"
				+ comebackmap.get("void_order_amt") + "</void_order_amt>");
		str.append("</Transaction_Body>");
		return str.toString().replace("null", "");
	}

	// 验证异常码查询
	public static String checkCreateMACXML_toSearchExptCode(Map comebackmap) {
		StringBuffer str = new StringBuffer("");

		str.append("<Transaction_Header>" + "<transaction_id>" + comebackmap.get("transaction_id") + "</transaction_id>" + "<requester>" + comebackmap.get("requester") + "</requester>" + "<target>"
				+ comebackmap.get("target") + "</target>" + "<request_time>" + comebackmap.get("request_time") + "</request_time>" + "<version>" + comebackmap.get("version") + "</version>"
				+ "</Transaction_Header>");

		return str.toString().replace("null", "");
	}

	/**
	 * 创建：异常单查询 的MAC字符串
	 * 
	 * @param cwbmap
	 * @return String
	 */
	public static String createMACXML_searchExptCode(Map cwbmap, List<Reason> reasonlist, List<ExptReason> dangdangList) {
		StringBuffer sb = new StringBuffer();

		sb.append("<Transaction_Header>" + "<transaction_id>" + cwbmap.get("transaction_id") + "</transaction_id>" + "<requester>" + cwbmap.get("requester") + "</requester>" + "<target>"
				+ cwbmap.get("target") + "</target>" + "<resp_code>" + cwbmap.get("resp_code") + "</resp_code>" + "<resp_msg>" + cwbmap.get("resp_msg") + "</resp_msg>" + "<resp_time>"
				+ cwbmap.get("resp_time") + "</resp_time>" + "</Transaction_Header>" + "<Transaction_Body>");

		// 物流公司内部异常码(滞留和退货)
		if (reasonlist != null && reasonlist.size() > 0) {
			sb.append("<item merchant_code=''>");
			for (Reason r : reasonlist) {
				sb.append("<exception code='" + (r.getReasonid() < 10 ? ("0" + r.getReasonid()) : (r.getReasonid() + "")) + "' msg='" + r.getReasoncontent() + "'/>");
			}
			sb.append("</item>");
		}

		// 当当的异常码
		if (dangdangList != null && dangdangList.size() > 0) {
			sb.append("<item merchant_code='" + dangdangList.get(0).getCustomercode() + "'>");
			for (ExptReason e : dangdangList) {
				sb.append("<exception code='" + e.getExpt_code() + "' msg='" + e.getExpt_msg() + "' />");
			}
			sb.append("</item>");
		}

		sb.append("</Transaction_Body>");
		return sb.toString().replace("null", "");
	}

}
