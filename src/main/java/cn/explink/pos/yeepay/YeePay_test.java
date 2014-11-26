package cn.explink.pos.yeepay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.util.Dom4jParseUtil;

@Controller
@RequestMapping("/yeepay_test")
public class YeePay_test {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {

		String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<COD-MS><SessionHead><Version>V1.0</Version>" + "<ServiceCode>COD204</ServiceCode>"
				+ "<TransactionID>explinkCOD204201208291338601772</TransactionID>" + "<SrcSysID>yeepay</SrcSysID><DstSysID>explink</DstSysID>" + "<ReqTime>20120829141804</ReqTime><ExtendAtt>"
				+ "<Employee_ID>11111</Employee_ID><Employee_Name>测试员001</Employee_Name>" + "<Company_Code>yeepay</Company_Code><Company_Name>易宝支付</Company_Name>" + "</ExtendAtt>"
				+ "<HMAC>6b4333395d5773f4f679906d67c68942</HMAC>" + "</SessionHead>" + "<SessionBody><ExtendAtt><OrderNo>100001</OrderNo><BankCardNo>5240470031960952</BankCardNo>"
				+ "<BankOrderNo>987329479237492927392</BankOrderNo>" + "<AMT>70.00</AMT><Pay_Type>1</Pay_Type>" + "<Bank_OrderId>06051711934882164944213879605509</Bank_OrderId>"
				+ "<Yeepay_OrderId>114243214166081I</Yeepay_OrderId>" + "<ChequeNo></ChequeNo>" + "</ExtendAtt>" + "</SessionBody></COD-MS>";
		String xmltrimStr = xmlstr.replaceAll(" ", "");
		String xmltrim = xmltrimStr.substring(xmltrimStr.indexOf("<SessionHead>"));
		String xmlsB = xmltrim.substring(0, xmltrim.indexOf("<HMAC>"));
		String xmlsE = xmltrim.substring(xmltrim.indexOf("</HMAC>") + ("</HMAC>".length()), xmltrim.indexOf("</COD-MS>"));
		String checkHMAC = xmlsB + xmlsE;
		System.out.println(checkHMAC);

		JSONObject jobject = Dom4jParseUtil.parserXmlToJSONObjectBy3Layer(xmlstr);

		String data = "<SessionHead>" + "<Version>" + jobject.get("Version") + "</Version>" + "<ServiceCode>" + jobject.get("ServiceCode") + "</ServiceCode>" + "<TransactionID>"
				+ jobject.get("TransactionID") + "</TransactionID>" + "<SrcSysID>" + jobject.get("SrcSysID") + "</SrcSysID>" + "<DstSysID>" + jobject.get("DstSysID") + "</DstSysID>" + "<ReqTime>"
				+ jobject.get("ReqTime") + "</ReqTime>" + "<ExtendAtt>" + "<Employee_ID>" + jobject.get("Employee_ID") + "</Employee_ID>" + "<Employee_Name>" + jobject.get("Employee_Name")
				+ "</Employee_Name>" + "<Company_Code>" + jobject.get("Company_Code") + "</Company_Code>" + "<Company_Name>" + jobject.get("Company_Name") + "</Company_Name>" + "</ExtendAtt>"
				+ "</SessionHead>" + "<SessionBody>" + "<ExtendAtt>" + "<OrderNo>" + jobject.get("OrderNo") + "</OrderNo>" + "<BankCardNo>" + jobject.get("BankCardNo") + "</BankCardNo>"
				+ "<BankOrderNo>" + jobject.get("BankOrderNo") + "</BankOrderNo>" + "<AMT>" + jobject.get("AMT") + "</AMT>" + "<Pay_Type>" + jobject.get("Pay_Type") + "</Pay_Type>" + "<Bank_OrderId>"
				+ jobject.get("Bank_OrderId") + "</Bank_OrderId>" + "<Yeepay_OrderId>" + jobject.get("Yeepay_OrderId") + "</Yeepay_OrderId>" + "<ChequeNo>" + jobject.get("ChequeNo") + "</ChequeNo>"
				+ "</ExtendAtt>" + "</SessionBody>";
		System.out.println(data);

	}

	@RequestMapping("/login")
	private void toLogin() {
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<COD-MS>" + "<SessionHead>" + "<Version>V1.0</Version><ServiceCode>COD201</ServiceCode>"
				+ "<TransactionID>NEPCOD201201106211108869768</TransactionID>" + "<SrcSysID>yeepay</SrcSysID>" + "<DstSysID>explink</DstSysID>" + "<ReqTime>20110621162649</ReqTime>" + "<ExtendAtt>"
				+ "</ExtendAtt>" + "<HMAC>bed282348248a9b34fa9853935b2d213</HMAC>" + "</SessionHead>" + "<SessionBody>" + "<Employee_ID>11111</Employee_ID>"
				+ "<Password>b0baee9d279d34fa1dfd71aadb908c3f</Password>" + "</SessionBody>" + "</COD-MS>";
		String url = "http://123.178.27.74/dmp4499/yeepay/";
		logger.info(submit1(url, content));

	}

	@RequestMapping("/search")
	private void toSearch(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<COD-MS>" + "<SessionHead>"
				+ "<Version>V1.0</Version><ServiceCode>COD202</ServiceCode><TransactionID>NEPCOD202201106211726526347</TransactionID>"
				+ "<SrcSysID>yeepay</SrcSysID><DstSysID>NEP</DstSysID><ReqTime>20110621164014</ReqTime><ExtendAtt></ExtendAtt>" + "<HMAC>B5CB8310FF297639E696FEEC225D7EFB</HMAC>" + "</SessionHead>"
				+ "<SessionBody>" + "<Employee_ID>111111</Employee_ID>" + "<Order_No>" + cwb + "</Order_No>" + "</SessionBody>" + "</COD-MS>";
		String url = "http://123.178.27.74/dmp4499/yeepay/";
		logger.info(submit1(url, content));

	}

	@RequestMapping("/pay")
	private void toPayAmount(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<COD-MS>" + "<SessionHead>" + "<Version>V1.0</Version>" + "<ServiceCode>COD204</ServiceCode>"
				+ "<TransactionID>NEPCOD204201106214068358793</TransactionID>" + "<SrcSysID>yeepay</SrcSysID><DstSysID>NEP</DstSysID>" + "<ReqTime>20110621165623</ReqTime>" + "<ExtendAtt>"
				+ "<Employee_ID>111111</Employee_ID>" + "<Employee_Name>阿斯蒂芬</Employee_Name>" + "<Company_Code>yeepay</Company_Code>" + "<Company_Name>易宝支付</Company_Name>" + "</ExtendAtt>"
				+ "<HMAC>883AEAC711D9BA647BE65F0BA5CF4166</HMAC>" + "</SessionHead>" + "<SessionBody>" + "<ExtendAtt>" + "<OrderNo>" + cwb + "</OrderNo>"
				+ "<BankCardNo>6222020200091246245</BankCardNo>" + "<BankOrderNo>10000</BankOrderNo><AMT>0.01</AMT>" + "<Pay_Type>1</Pay_Type>" + "<Bank_OrderId>020202</Bank_OrderId>"
				+ "<Yeepay_OrderId>001111</Yeepay_OrderId>" + "<ChequeNo>11</ChequeNo>" + "</ExtendAtt>" + "</SessionBody>" + "</COD-MS>";
		String url = "http://123.178.27.74/dmp4499/yeepay/";
		logger.info(submit1(url, content));

	}

	@RequestMapping("/sign")
	private void toSign(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<COD-MS>" + "<SessionHead>"
				+ "<Version>V1.0</Version><ServiceCode>COD203</ServiceCode><TransactionID>rufengdaCOD203201207240114634068</TransactionID>"
				+ "<SrcSysID>yeepay</SrcSysID><DstSysID>NEP</DstSysID><ReqTime>20120724110754</ReqTime><ExtendAtt></ExtendAtt>" + "<HMAC>f5af36f94345df37bac8dfb63320a274</HMAC>" + "</SessionHead>"
				+ "<SessionBody>" + "<ExtendAtt>" + "<Employee_ID>111111</Employee_ID>" + "<Employee_Name>夏永良</Employee_Name>" + "<Company_Code>yeepay</Company_Code>"
				+ "<Company_Name>易宝支付</Company_Name>" + "<Pay_Type>1</Pay_Type>" + "<Order_No>" + cwb + "</Order_No>" + "<Sign_Self_Flag>1</Sign_Self_Flag>"
				+ "<Sign_Name>王子娟</Sign_Name><Sign_Tel></Sign_Tel>" + "</ExtendAtt>" + "</SessionBody>" + "</COD-MS>";
		String url = "http://123.178.27.74/dmp4499/yeepay/";
		logger.info(submit1(url, content));

	}

	@RequestMapping("/expt")
	private void toExpt(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<COD-MS>" + "<SessionHead>"
				+ "<Version>V1.0</Version><ServiceCode>COD206</ServiceCode><TransactionID>NEPCOD206201106218148113870</TransactionID>"
				+ "<SrcSysID>yeepay</SrcSysID><DstSysID>NEP</DstSysID><ReqTime>20110621181558</ReqTime>" + "<ExtendAtt>" + "<Employee_ID>111111</Employee_ID><Employee_Name>阿斯蒂芬</Employee_Name>"
				+ "<Company_Code>yeepay</Company_Code><Company_Name>易宝支付</Company_Name>" + "</ExtendAtt>" + "<HMAC>8A0E6C7D2F285F6BD7FB9F884FB975DE</HMAC>" + "</SessionHead>" + "<SessionBody>"
				+ "<ExtendAtt>" + "<OrderNo>100003</OrderNo>" + "<Except_Type>1</Except_Type>" + "<Except_Code>07</Except_Code>" + "<Except_Msg>联系不上客户</Except_Msg>" + "</ExtendAtt>"
				+ "</SessionBody>" + "</COD-MS>";
		String url = "http://123.178.27.74/dmp4499/yeepay/";
		logger.info(submit1(url, content));

	}

	@RequestMapping("/backout")
	private void toBackOut(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String amt = request.getParameter("amt");
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<COD-MS>" + "<SessionHead>" + "<Version>V1.0</Version><ServiceCode>COD205</ServiceCode>"
				+ "<TransactionID>NEPCOD205201106213129556566</TransactionID>" + "<SrcSysID>yeepay</SrcSysID><DstSysID>NEP</DstSysID>" + "<ReqTime>20110621173135</ReqTime>" + "<ExtendAtt>"
				+ "<Employee_ID>111111</Employee_ID>" + "<Employee_Name>阿斯蒂芬</Employee_Name>" + "<Company_Code>yeepay</Company_Code>" + "<Company_Name>易宝支付</Company_Name>" + "</ExtendAtt>"
				+ "<HMAC>B6EC16F55BBC1041E6B63BEDF749BB46</HMAC>" + "</SessionHead>" + "<SessionBody>" + "<ExtendAtt>" + "<OrderNo>" + cwb + "</OrderNo>" + "<AMT>" + amt + "</AMT>"
				+ "<Bank_OrderId>adsfasdf</Bank_OrderId>" + "<Yeepay_OrderId>111111111</Yeepay_OrderId>" + "</ExtendAtt>" + "</SessionBody>" + "</COD-MS>";
		String url = "http://123.178.27.74/dmp4499/yeepay/";
		logger.info(submit1(url, content));

	}

	public String submit1(String requestUrl, String xmlstr) {
		StringBuilder buffer = new StringBuilder();
		URL url;
		try {
			url = new URL(requestUrl);

			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("content-type", "text/xml");
			httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpURLConnection.setRequestProperty("contentType", "utf-8");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(5000);
			httpURLConnection.setReadTimeout(5000);
			httpURLConnection.connect();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
			out.write(xmlstr);
			out.flush();
			// 接收服务器的返回：
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));

			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();

	}

	/**
	 * 检查异常订单 XML
	 *
	 * @return String
	 */
	public static String checkRequestXML_Exception(Map m) {
		String data = "<SessionHead>" + "<Version>" + m.get("Version") + "</Version>" + "<ServiceCode>" + m.get("ServiceCode") + "</ServiceCode>" + "<TransactionID>" + m.get("TransactionID")
				+ "</TransactionID>" + "<SrcSysID>" + m.get("SrcSysID") + "</SrcSysID>" + "<DstSysID>" + m.get("DstSysID") + "</DstSysID>" + "<ReqTime>" + m.get("ReqTime") + "</ReqTime>"
				+ "<ExtendAtt>" + "<Employee_ID>" + m.get("Employee_ID") + "</Employee_ID>" + "<Employee_Name>" + m.get("Employee_Name") + "</Employee_Name>" + "<Company_Code>"
				+ m.get("Company_Code") + "</Company_Code>" + "<Company_Name>" + m.get("Company_Name") + "</Company_Name>" + "</ExtendAtt>" + "</SessionHead>" + "<SessionBody>" + "<ExtendAtt>"
				+ "<OrderNo>" + m.get("OrderNo") + "</OrderNo>" + "<Except_Type>" + m.get("Except_Type") + "</Except_Type>" + "<Except_Code>" + m.get("Except_Code") + "</Except_Code>"
				+ "<Except_Msg>" + m.get("Except_Msg") + "</Except_Msg>" + "</ExtendAtt>" + "</SessionBody>";

		return filter(data);
	}

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

}
