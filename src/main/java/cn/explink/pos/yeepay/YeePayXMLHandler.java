package cn.explink.pos.yeepay;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YeePayXMLHandler {

	/**
	 * 过滤制表符
	 * 
	 * @param content
	 * @return String
	 */
	public static String filter(String content) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(content);
		return m.replaceAll("");
	}

	/**
	 * 创建响应登录加密的字符串
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseXMLHMAC_Login(Map responseloginxmlmap) {
		String data = "<SessionHead>" + "<Version>" + responseloginxmlmap.get("version") + "</Version>" + "<ServiceCode>" + responseloginxmlmap.get("servicecode") + "</ServiceCode>"
				+ "<TransactionID>" + responseloginxmlmap.get("transactionid") + "</TransactionID>" + "<SrcSysID>" + responseloginxmlmap.get("srcsysid") + "</SrcSysID>" + "<DstSysID>"
				+ responseloginxmlmap.get("dstsysid") + "</DstSysID>" + "<Result_code>" + responseloginxmlmap.get("result_code") + "</Result_code>" + "<Result_msg>"
				+ responseloginxmlmap.get("result_msg") + "</Result_msg>" + "<Resp_time>" + responseloginxmlmap.get("result_time") + "</Resp_time>" + "<ExtendAtt>" + "<Employee_ID>"
				+ responseloginxmlmap.get("employee_id") + "</Employee_ID>" + "</ExtendAtt>" + "</SessionHead>" + "<SessionBody>" + "<ExtendAtt>" + "<Employee_Name>"
				+ responseloginxmlmap.get("employee_name") + "</Employee_Name>" + "<Company_Code>" + responseloginxmlmap.get("company_code") + "</Company_Code>" + "<Company_Name>"
				+ responseloginxmlmap.get("company_name") + "</Company_Name>" + "<Company_Addr>" + responseloginxmlmap.get("company_addr") + "</Company_Addr>" + "<Company_Tel>"
				+ responseloginxmlmap.get("company_tel") + "</Company_Tel>" + "</ExtendAtt>" + "</SessionBody>";
		return filter(data);
	}

	/**
	 * 创建响应登录的 XML
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseXML_Login(Map responseloginxmlmap) {
		String data_head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String data = "<COD-MS>" + "<SessionHead>" + "<Version>" + responseloginxmlmap.get("version") + "</Version>" + "<ServiceCode>" + responseloginxmlmap.get("servicecode") + "</ServiceCode>"
				+ "<TransactionID>" + responseloginxmlmap.get("transactionid") + "</TransactionID>" + "<SrcSysID>" + responseloginxmlmap.get("srcsysid") + "</SrcSysID>" + "<DstSysID>"
				+ responseloginxmlmap.get("dstsysid") + "</DstSysID>" + "<Result_code>" + responseloginxmlmap.get("result_code") + "</Result_code>" + "<Result_msg>"
				+ responseloginxmlmap.get("result_msg") + "</Result_msg>" + "<Resp_time>" + responseloginxmlmap.get("result_time") + "</Resp_time>" + "<ExtendAtt>" + "<Employee_ID>"
				+ responseloginxmlmap.get("employee_id") + "</Employee_ID>" + "</ExtendAtt>" + "<HMAC>" + responseloginxmlmap.get("hmac") + "</HMAC>" + "</SessionHead>" + "<SessionBody>"
				+ "<ExtendAtt>" + "<Employee_Name>" + responseloginxmlmap.get("employee_name") + "</Employee_Name>" + "<Company_Code>" + responseloginxmlmap.get("company_code") + "</Company_Code>"
				+ "<Company_Name>" + responseloginxmlmap.get("company_name") + "</Company_Name>" + "<Company_Addr>" + responseloginxmlmap.get("company_addr") + "</Company_Addr>" + "<Company_Tel>"
				+ responseloginxmlmap.get("company_tel") + "</Company_Tel>" + "</ExtendAtt>" + "</SessionBody>" + "</COD-MS>";

		return data_head + filter(data);
	}

	/**
	 * 创建响应运单查询 生成签名字符串 hmac xml
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseXMLHMAC_cwbSearch(Map m) {
		String data = "<SessionHead>" + "<Version>" + m.get("version") + "</Version>" + "<ServiceCode>" + m.get("servicecode") + "</ServiceCode>" + "<TransactionID>" + m.get("transactionid")
				+ "</TransactionID>" + "<SrcSysID>" + m.get("srcsysid") + "</SrcSysID>" + "<DstSysID>" + m.get("dstsysid") + "</DstSysID>" + "<Result_code>" + m.get("result_code") + "</Result_code>"
				+ "<Result_msg>" + m.get("result_msg") + "</Result_msg>" + "<Resp_time>" + m.get("result_time") + "</Resp_time>" + "<ExtendAtt>" + "<Employee_ID>" + m.get("employee_id")
				+ "</Employee_ID>" + "<Order_No>" + m.get("order_no") + "</Order_No>" + "</ExtendAtt>" + "</SessionHead>" + "<SessionBody>" + "<ExtendAtt>" + "<SignStandard>" + m.get("signstandard")
				+ "</SignStandard>" + "<Receiver_Name>" + m.get("receiver_name") + "</Receiver_Name>" + "<Receiver_Addr>" + m.get("receiver_addr") + "</Receiver_Addr>" + "<Receiver_Tel>"
				+ m.get("receiver_tel") + "</Receiver_Tel>" + "<Receiver_OrderNo>" + m.get("receiver_orderno") + "</Receiver_OrderNo>" + "<Order_AMT>" + m.get("order_amt") + "</Order_AMT>"
				+ "<Biz_Code>" + m.get("biz_code") + "</Biz_Code>" + "<Biz_Name>" + m.get("biz_name") + "</Biz_Name>" + "<Pa_Details>" + m.get("pa_details") + "</Pa_Details>" + "<Pc_AutoSplit>"
				+ m.get("pc_autosplit") + "</Pc_AutoSplit>" + "<AMT>" + m.get("amt") + "</AMT>" + "<Sub_Station_Code>" + m.get("sub_station_code") + "</Sub_Station_Code>" + "<Sub_Station_Name>"
				+ m.get("sub_station_name") + "</Sub_Station_Name>" + "<Serial_NO>" + m.get("serial_no") + "</Serial_NO>" + "<Sorting_Name>" + m.get("sorting_name") + "</Sorting_Name>" + "<Weight>"
				+ m.get("weight") + "</Weight>" + "<Checked_Items>" + m.get("checked_items") + "</Checked_Items>" + "<Order_Status>" + m.get("order_status") + "</Order_Status>" + "<Order_Status_Msg>"
				+ m.get("order_status_msg") + "</Order_Status_Msg>" + "</ExtendAtt>" + "</SessionBody>";

		return filter(data);
	}

	/**
	 * 创建响应运单查询 xml
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseXML_cwbSearch(Map m) {
		String data_head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String data = "<COD-MS>" + "<SessionHead>" + "<Version>" + m.get("version") + "</Version>" + "<ServiceCode>" + m.get("servicecode") + "</ServiceCode>" + "<TransactionID>"
				+ m.get("transactionid") + "</TransactionID>" + "<SrcSysID>" + m.get("srcsysid") + "</SrcSysID>" + "<DstSysID>" + m.get("dstsysid") + "</DstSysID>" + "<Result_code>"
				+ m.get("result_code") + "</Result_code>" + "<Result_msg>" + m.get("result_msg") + "</Result_msg>" + "<Resp_time>" + m.get("result_time") + "</Resp_time>" + "<ExtendAtt>"
				+ "<Employee_ID>" + m.get("employee_id") + "</Employee_ID>" + "<Order_No>" + m.get("order_no") + "</Order_No>" + "</ExtendAtt>" + "<HMAC>" + m.get("hmac") + "</HMAC>"
				+ "</SessionHead>" + "<SessionBody>" + "<ExtendAtt>" + "<SignStandard>" + m.get("signstandard") + "</SignStandard>" + "<Receiver_Name>" + m.get("receiver_name") + "</Receiver_Name>"
				+ "<Receiver_Addr>" + m.get("receiver_addr") + "</Receiver_Addr>" + "<Receiver_Tel>" + m.get("receiver_tel") + "</Receiver_Tel>" + "<Receiver_OrderNo>" + m.get("receiver_orderno")
				+ "</Receiver_OrderNo>" + "<Order_AMT>" + m.get("order_amt") + "</Order_AMT>" + "<Biz_Code>" + m.get("biz_code") + "</Biz_Code>" + "<Biz_Name>" + m.get("biz_name") + "</Biz_Name>"
				+ "<Pa_Details>" + m.get("pa_details") + "</Pa_Details>" + "<Pc_AutoSplit>" + m.get("pc_autosplit") + "</Pc_AutoSplit>" + "<AMT>" + m.get("amt") + "</AMT>" + "<Sub_Station_Code>"
				+ m.get("sub_station_code") + "</Sub_Station_Code>" + "<Sub_Station_Name>" + m.get("sub_station_name") + "</Sub_Station_Name>" + "<Serial_NO>" + m.get("serial_no") + "</Serial_NO>"
				+ "<Sorting_Name>" + m.get("sorting_name") + "</Sorting_Name>" + "<Weight>" + m.get("weight") + "</Weight>" + "<Checked_Items>" + m.get("checked_items") + "</Checked_Items>"
				+ "<Order_Status>" + m.get("order_status") + "</Order_Status>" + "<Order_Status_Msg>" + m.get("order_status_msg") + "</Order_Status_Msg>" + "</ExtendAtt>" + "</SessionBody>"
				+ "</COD-MS>";
		return data_head + filter(data);
	}

	/**
	 * 创建加密付款 XML
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseXMLHMAC_PayAmount(Map m) {
		String data = "<SessionHead>" + "<Version>" + m.get("version") + "</Version>" + "<ServiceCode>" + m.get("servicecode") + "</ServiceCode>" + "<TransactionID>" + m.get("transactionid")
				+ "</TransactionID>" + "<SrcSysID>" + m.get("srcsysid") + "</SrcSysID>" + "<DstSysID>" + m.get("dstsysid") + "</DstSysID>" + "<Result_code>" + m.get("result_code") + "</Result_code>"
				+ "<Result_msg>" + m.get("result_msg") + "</Result_msg>" + "<Resp_time>" + m.get("result_time") + "</Resp_time>" + "<ExtendAtt>" + "<Order_No>" + m.get("order_no") + "</Order_No>"
				+ "</ExtendAtt>" + "</SessionHead>";
		return filter(data);
	}

	/**
	 * 创建响应付款 XML
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseXML_PayAmount(Map m) {
		String data_head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String data = "<COD-MS>" + "<SessionHead>" + "<Version>" + m.get("version") + "</Version>" + "<ServiceCode>" + m.get("servicecode") + "</ServiceCode>" + "<TransactionID>"
				+ m.get("transactionid") + "</TransactionID>" + "<SrcSysID>" + m.get("srcsysid") + "</SrcSysID>" + "<DstSysID>" + m.get("dstsysid") + "</DstSysID>" + "<Result_code>"
				+ m.get("result_code") + "</Result_code>" + "<Result_msg>" + m.get("result_msg") + "</Result_msg>" + "<Resp_time>" + m.get("result_time") + "</Resp_time>" + "<ExtendAtt>"
				+ "<Order_No>" + m.get("order_no") + "</Order_No>" + "</ExtendAtt>" + "<HMAC>" + m.get("hmac") + "</HMAC>" + "</SessionHead>" + "</COD-MS>";
		return data_head + filter(data);
	}

	/**
	 * 创建签收 加密 XML
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseHMAC_cwbSign(Map m) {
		String data = "<SessionHead>" + "<Version>" + m.get("version") + "</Version>" + "<ServiceCode>" + m.get("servicecode") + "</ServiceCode>" + "<TransactionID>" + m.get("transactionid")
				+ "</TransactionID>" + "<SrcSysID>" + m.get("srcsysid") + "</SrcSysID>" + "<DstSysID>" + m.get("dstsysid") + "</DstSysID>" + "<Result_code>" + m.get("result_code") + "</Result_code>"
				+ "<Result_msg>" + m.get("result_msg") + "</Result_msg>" + "<Resp_time>" + m.get("result_time") + "</Resp_time>" + "<ExtendAtt>" + "<Order_No>" + m.get("order_no") + "</Order_No>"
				+ "</ExtendAtt>" + "</SessionHead>";
		return filter(data);
	}

	/**
	 * 创建响应签收 XML
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseXML_cwbSign(Map m) {
		String data_head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String data = "<COD-MS>" + "<SessionHead>" + "<Version>" + m.get("version") + "</Version>" + "<ServiceCode>" + m.get("servicecode") + "</ServiceCode>" + "<TransactionID>"
				+ m.get("transactionid") + "</TransactionID>" + "<SrcSysID>" + m.get("srcsysid") + "</SrcSysID>" + "<DstSysID>" + m.get("dstsysid") + "</DstSysID>" + "<Result_code>"
				+ m.get("result_code") + "</Result_code>" + "<Result_msg>" + m.get("result_msg") + "</Result_msg>" + "<Resp_time>" + m.get("result_time") + "</Resp_time>" + "<ExtendAtt>"
				+ "<Order_No>" + m.get("order_no") + "</Order_No>" + "</ExtendAtt>" + "<HMAC>" + m.get("hmac") + "</HMAC>" + "</SessionHead>" + "</COD-MS>";
		return data_head + filter(data);
	}

	/**
	 * 创建撤销交易 HMAC XML
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseHMAC_backOut(Map m) {
		String data = "<SessionHead>" + "<Version>" + m.get("version") + "</Version>" + "<ServiceCode>" + m.get("servicecode") + "</ServiceCode>" + "<TransactionID>" + m.get("transactionid")
				+ "</TransactionID>" + "<SrcSysID>" + m.get("srcsysid") + "</SrcSysID>" + "<DstSysID>" + m.get("dstsysid") + "</DstSysID>" + "<Result_code>" + m.get("result_code") + "</Result_code>"
				+ "<Result_msg>" + m.get("result_msg") + "</Result_msg>" + "<Resp_time>" + m.get("result_time") + "</Resp_time>" + "<ExtendAtt>" + "<Order_No>" + m.get("order_no") + "</Order_No>"
				+ "</ExtendAtt>" + "</SessionHead>";
		return filter(data);
	}

	/**
	 * 创建响应撤销交易 XML
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseXML_backOut(Map m) {
		String data_head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String data = "<COD-MS>" + "<SessionHead>" + "<Version>" + m.get("version") + "</Version>" + "<ServiceCode>" + m.get("servicecode") + "</ServiceCode>" + "<TransactionID>"
				+ m.get("transactionid") + "</TransactionID>" + "<SrcSysID>" + m.get("srcsysid") + "</SrcSysID>" + "<DstSysID>" + m.get("dstsysid") + "</DstSysID>" + "<Result_code>"
				+ m.get("result_code") + "</Result_code>" + "<Result_msg>" + m.get("result_msg") + "</Result_msg>" + "<Resp_time>" + m.get("result_time") + "</Resp_time>" + "<ExtendAtt>"
				+ "<Order_No>" + m.get("order_no") + "</Order_No>" + "</ExtendAtt>" + "<HMAC>" + m.get("hmac") + "</HMAC>" + "</SessionHead>" + "</COD-MS>";
		return data_head + filter(data);
	}

	/**
	 * 创建异常订单 HMAC XML
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseHMAC_Exception(Map m) {
		String data = "<SessionHead>" + "<Version>" + m.get("version") + "</Version>" + "<ServiceCode>" + m.get("servicecode") + "</ServiceCode>" + "<TransactionID>" + m.get("transactionid")
				+ "</TransactionID>" + "<SrcSysID>" + m.get("srcsysid") + "</SrcSysID>" + "<DstSysID>" + m.get("dstsysid") + "</DstSysID>" + "<Result_code>" + m.get("result_code") + "</Result_code>"
				+ "<Result_msg>" + m.get("result_msg") + "</Result_msg>" + "<Resp_time>" + m.get("result_time") + "</Resp_time>" + "<ExtendAtt>" + "<Order_No>" + m.get("order_no") + "</Order_No>"
				+ "</ExtendAtt>" + "</SessionHead>";
		return filter(data);
	}

	/**
	 * 创建响应异常订单 XML
	 *
	 * @param responseloginxmlmap
	 * @return String
	 */
	public static String createResponseXML_Exception(Map m) {
		String data_head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String data = "<COD-MS>" + "<SessionHead>" + "<Version>" + m.get("version") + "</Version>" + "<ServiceCode>" + m.get("servicecode") + "</ServiceCode>" + "<TransactionID>"
				+ m.get("transactionid") + "</TransactionID>" + "<SrcSysID>" + m.get("srcsysid") + "</SrcSysID>" + "<DstSysID>" + m.get("dstsysid") + "</DstSysID>" + "<Result_code>"
				+ m.get("result_code") + "</Result_code>" + "<Result_msg>" + m.get("result_msg") + "</Result_msg>" + "<Resp_time>" + m.get("result_time") + "</Resp_time>" + "<ExtendAtt>"
				+ "<Order_No>" + m.get("order_no") + "</Order_No>" + "</ExtendAtt>" + "<HMAC>" + m.get("hmac") + "</HMAC>" + "</SessionHead>" + "</COD-MS>";
		return data_head + filter(data);
	}

}
