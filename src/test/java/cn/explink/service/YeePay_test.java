package cn.explink.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import cn.explink.util.Dom4jParseUtil;

public class YeePay_test {
	public static void main(String[] args) {

		String xmlstr="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<COD-MS><SessionHead><Version>V1.0</Version>" +
				"<ServiceCode>COD204</ServiceCode>" +
				"<TransactionID>explinkCOD204201208291338601772</TransactionID>" +
				"<SrcSysID>yeepay</SrcSysID><DstSysID>explink</DstSysID>" +
				"<ReqTime>20120829141804</ReqTime><ExtendAtt>" +
				"<Employee_ID>11111</Employee_ID><Employee_Name>测试员001</Employee_Name>" +
				"<Company_Code>yeepay</Company_Code><Company_Name>易宝支付</Company_Name>" +
				"</ExtendAtt>" +
				"<HMAC>6b4333395d5773f4f679906d67c68942</HMAC>" +
				"</SessionHead>" +
				"<SessionBody><ExtendAtt><OrderNo>100001</OrderNo><BankCardNo>5240470031960952</BankCardNo>" +
				"<BankOrderNo>987329479237492927392</BankOrderNo>" +
				"<AMT>70.00</AMT><Pay_Type>1</Pay_Type>" +
				"<Bank_OrderId>06051711934882164944213879605509</Bank_OrderId>" +
				"<Yeepay_OrderId>114243214166081I</Yeepay_OrderId>" +
				"<ChequeNo></ChequeNo>" +
				"</ExtendAtt>" +
				"</SessionBody></COD-MS>";
		JSONObject jobject =Dom4jParseUtil.parserXmlToJSONObjectBy3Layer(xmlstr);
		
		 String data =  "<SessionHead>"+
					"<Version>"+jobject.get("Version")+"</Version>"+
					"<ServiceCode>"+jobject.get("ServiceCode")+"</ServiceCode>"+
					"<TransactionID>"+jobject.get("TransactionID")+"</TransactionID>"+
					"<SrcSysID>"+jobject.get("SrcSysID")+"</SrcSysID>"+
					"<DstSysID>"+jobject.get("DstSysID")+"</DstSysID>"+
					"<ReqTime>"+jobject.get("ReqTime")+"</ReqTime>"+
					"<ExtendAtt>"+
						"<Employee_ID>"+jobject.get("Employee_ID")+"</Employee_ID>"+
						"<Employee_Name>"+jobject.get("Employee_Name")+"</Employee_Name>"+
						"<Company_Code>"+jobject.get("Company_Code")+"</Company_Code>"+
						"<Company_Name>"+jobject.get("Company_Name")+"</Company_Name>"+
					"</ExtendAtt>"+
				"</SessionHead>"+
				"<SessionBody>"+
					"<ExtendAtt>"+
						"<OrderNo>"+jobject.get("OrderNo")+"</OrderNo>"+
						"<BankCardNo>"+jobject.get("BankCardNo")+"</BankCardNo>"+
						"<BankOrderNo>"+jobject.get("BankOrderNo")+"</BankOrderNo>"+
						"<AMT>"+jobject.get("AMT")+"</AMT>"+
						"<Pay_Type>"+jobject.get("Pay_Type")+"</Pay_Type>"+
						"<Bank_OrderId>"+jobject.get("Bank_OrderId")+"</Bank_OrderId>"+
						"<Yeepay_OrderId>"+jobject.get("Yeepay_OrderId")+"</Yeepay_OrderId>"+
						"<ChequeNo>"+jobject.get("ChequeNo")+"</ChequeNo>"+
					"</ExtendAtt>"+
				"</SessionBody>";
		System.out.println(data);
		
	
	    
	}
	
	public String submit1(String requestUrl) throws Exception {
		
		String content="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<COD-MS>" +
				"<SessionHead>" +
					"<Version>V1.0</Version><ServiceCode>COD201</ServiceCode>" +
					"<TransactionID>NEPCOD201201106211108869768</TransactionID>" +
					"<SrcSysID>yeepay</SrcSysID>" +
					"<DstSysID>explink</DstSysID>" +
					"<ReqTime>20110621162649</ReqTime>" +
					"<ExtendAtt>" +
					"</ExtendAtt>" +
					"<HMAC>bed282348248a9b34fa9853935b2d213</HMAC>" +
				"</SessionHead>" +
				"<SessionBody>" +
				"<Employee_ID>11111</Employee_ID>" +
				"<Password>b0baee9d279d34fa1dfd71aadb908c3f</Password>" +
				"</SessionBody>" +
				"</COD-MS>";
		 //content = URLEncoder.encode(content, "utf-8");
		URL url = new URL("http://localhost:8080/dmp/yeepay/");
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
		out.write(content);
		out.flush();
		// 接收服务器的返回：
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
		StringBuilder buffer = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();

	}
	
	
	/**
	 * 检查异常订单 XML 
	 *
	 * @return String
	 */
	public static String checkRequestXML_Exception(Map m) {
		 String data =  "<SessionHead>"+
							   "<Version>"+m.get("Version")+"</Version>"+
							   "<ServiceCode>"+m.get("ServiceCode")+"</ServiceCode>"+
							   "<TransactionID>"+m.get("TransactionID")+"</TransactionID>"+
							   "<SrcSysID>"+m.get("SrcSysID")+"</SrcSysID>"+
							   "<DstSysID>"+m.get("DstSysID")+"</DstSysID>"+
							   "<ReqTime>"+m.get("ReqTime")+"</ReqTime>"+
							   "<ExtendAtt>"+
									"<Employee_ID>"+m.get("Employee_ID")+"</Employee_ID>"+
									"<Employee_Name>"+m.get("Employee_Name")+"</Employee_Name>"+
									"<Company_Code>"+m.get("Company_Code")+"</Company_Code>"+
									"<Company_Name>"+m.get("Company_Name")+"</Company_Name>"+
							   "</ExtendAtt>"+
						 "</SessionHead>"+
						 "<SessionBody>"+
							   "<ExtendAtt>"+
							   		"<OrderNo>"+m.get("OrderNo")+"</OrderNo>"+
							   		"<Except_Type>"+m.get("Except_Type")+"</Except_Type>"+
								    "<Except_Code>"+m.get("Except_Code")+"</Except_Code>"+
								    "<Except_Msg>"+m.get("Except_Msg")+"</Except_Msg>"+
							   "</ExtendAtt>"+
						 "</SessionBody>";
		 
		
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
