package cn.explink.b2c.vipshop;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SOAPHandler {
	@Autowired
	ReaderXMLHandler readXML;
	private Logger logger = LoggerFactory.getLogger(SOAPHandler.class);
	
	public String HTTPInvokeWs(String endpointUrl, String nameSpace, String methodName, String requestXML, String sign) throws Exception {
		return HTTPInvokeWs(endpointUrl, nameSpace, methodName, requestXML, sign, null);
	}
	
	public String HTTPInvokeWs(String endpointUrl, String nameSpace, String methodName, String requestXML, String sign, String serviceCode) throws Exception {
		StringBuffer result = null;
		OutputStream out = null;
		BufferedReader in = null;
		try {
			String soapActionString = nameSpace + "/" + methodName;
			StringBuffer paramXml = new StringBuffer();
			paramXml.append("<content>" + readXML.parse(requestXML) + "</content>");
			paramXml.append("<sign>" + sign.toLowerCase() + "</sign>");
			if(serviceCode != null){
				paramXml.append("<serviceCode>" + serviceCode + "</serviceCode>");
			}

			String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><" + methodName + " xmlns=\"" + nameSpace + "\">" + paramXml + "</" + methodName
					+ "></soapenv:Body></soapenv:Envelope>";
			logger.info("soap方式请求格式：" + soap);

			URL url = new URL(endpointUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestProperty("Content-Length", String.valueOf(soap.getBytes("UTF-8").length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			httpConn.setRequestProperty("soapActionString", soapActionString);
			// httpConn.setRequestProperty("User-Agent",
			// "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			httpConn.setRequestMethod("POST");
			httpConn.setReadTimeout(60000);
			httpConn.setConnectTimeout(60000);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			out = httpConn.getOutputStream();
			out.write(soap.getBytes("UTF-8"));
			out.flush();

			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "UTF-8");

			in = new BufferedReader(isr);
			result = new StringBuffer("");
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result.append(inputLine);
			}

		} catch (Throwable e) {
			logger.error("", e);
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

	/**
	 * 调用WEB服务
	 * 
	 * @param wsdlUrl
	 *            wsdl文档地址
	 * @param opName
	 *            方法名
	 * @param opArgs
	 *            参数
	 * @return 字符串
	 * @throws Exception
	 */
	// public static String invokeWs(String wsdlUrl, String opName,
	// Object... opArgs) throws Exception {
	// Object[] results = null;
	// try {
	// Client client = new Client(new URL(wsdlUrl));
	// client.setProperty(CommonsHttpMessageSender.GZIP_RESPONSE_ENABLED,Boolean.TRUE);//
	// 告诉对方支持返回GZIP内容
	// results = client.invoke(opName, opArgs);
	//
	// } catch (Throwable e) {
	// throw new Exception("WebService服务链路异常:"+e.getMessage(), e);
	// }
	//
	// return (String) results[0];
	// }

}
