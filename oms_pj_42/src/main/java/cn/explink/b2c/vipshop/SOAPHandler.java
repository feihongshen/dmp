package cn.explink.b2c.vipshop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SOAPHandler {

	@Autowired
	ReaderXMLHandler readXML;
	private Logger logger = LoggerFactory.getLogger(SOAPHandler.class);

	public String HTTPInvokeWs(String endpointUrl, String nameSpace, String methodName, String requestXML, String sign, String serviceCode) throws Exception {
		StringBuffer result = null;
		OutputStream out = null;
		BufferedReader in = null;
		try {
			String soapActionString = nameSpace + "/" + methodName;
			StringBuffer paramXml = new StringBuffer();
			paramXml.append("<content>" + ReaderXMLHandler.parse(requestXML) + "</content>");
			paramXml.append("<sign>" + sign + "</sign>");
			paramXml.append("<serviceCode>" + serviceCode + "</serviceCode>");

			String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><" + methodName + " xmlns=\"" + nameSpace + "\">" + paramXml + "</" + methodName
					+ "></soapenv:Body></soapenv:Envelope>";
			// logger.info("soap方式请求格式：" + soap);

			URL url = new URL(endpointUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestProperty("Content-Length", String.valueOf(soap.getBytes("UTF-8").length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			httpConn.setRequestProperty("soapActionString", soapActionString);
			// httpConn.setRequestProperty("User-Agent",
			// "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			httpConn.setRequestMethod("POST");
			httpConn.setReadTimeout(30000);
			httpConn.setConnectTimeout(30000);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			out = httpConn.getOutputStream();
			out.write(soap.getBytes("UTF-8"));
			out.flush();

			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "UTF-8");

			in = new BufferedReader(isr);
			result = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result.append(inputLine);
			}

		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception("WebService服务链路异常:" + e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}

		}
		return result.toString();
	}

	// /**
	// * 调用WEB服务
	// *
	// * @param wsdlUrl
	// * wsdl文档地址
	// * @param opName
	// * 方法名
	// * @param opArgs
	// * 参数
	// * @return 字符串
	// * @throws Exception
	// */
	// public static String invokeWs(String wsdlUrl, String opName,
	// Object... opArgs) throws Exception {
	// Object[] results = null;
	// try {
	// Client client = new Client(new URL(wsdlUrl));
	// client.setProperty(CommonsHttpMessageSender.GZIP_RESPONSE_ENABLED,
	// Boolean.TRUE);// 告诉对方支持返回GZIP内容
	// results = client.invoke(opName, opArgs);
	//
	// } catch (Throwable e) {
	// throw new Exception("WebService服务链路异常:"+e.getMessage(), e);
	// }
	//
	// return (String) results[0];
	// }

	public static void main(String[] args) throws UnsupportedEncodingException {

		String strs = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><head><version>1.0</version><request_time>2014-11-12 17:48:20</request_time><cust_code>SHKD01</cust_code></head><traces><trace><cust_data_id>1530_8</cust_data_id><order_sn>2014110400004918-T1</order_sn><order_status>31</order_status><order_status_info>从[北京K]入库</order_status_info><current_city_name>北京K</current_city_name><order_status_time>2014-11-12 16:53:34</order_status_time><sign_man></sign_man></trace></traces></request>";
		String str = "414141411142112142";
		System.out.println(Arrays.toString(strs.getBytes("UTF-8")));

	}
}
