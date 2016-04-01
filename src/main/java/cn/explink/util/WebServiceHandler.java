package cn.explink.util;

import java.net.URL;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.b2c.gome.SOAPHandle;

public class WebServiceHandler {
	
	private static Logger logger = LoggerFactory.getLogger(WebServiceHandler.class);
	
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
	public static Object invokeWs(String wsdlUrl, String opName, Object... opArgs) throws Exception {
		Object[] results = null;
		try {
			Client client = new Client(new URL(wsdlUrl));
			client.setProperty(CommonsHttpMessageSender.GZIP_RESPONSE_ENABLED, Boolean.TRUE);// 告诉对方支持返回GZIP内容
			client.setTimeout(40000);
			results = client.invoke(opName, opArgs);
		} catch (Throwable e) {
			throw new Exception("WebService服务链路异常:" + e.getMessage(), e);
		}
		return results[0];
	}

	public static Object invokeWsByNameAndPassWord(String url, String method, String pram, String userName, String passWord) throws Exception {
		Object[] params = new Object[] { pram };
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(url);
		call.setTimeout(30000);
		logger.info(url + "===" + userName + "====" + passWord);
		call.setClientHandlers(new SOAPHandle(userName, passWord), null);
		Object returnValue = (Object) call.invoke(method, params);
		return returnValue;
	}

	public static Object invokeWsByNameAndPassWord1(String url, String method, String pram) throws Exception {
		Object[] params = new Object[] { pram };
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(url);
		call.setTimeout(30000);
		call.setClientHandlers(new SOAPHandle("", ""), null);
		Object returnValue = (Object) call.invoke(method, params);
		return returnValue;
	}

	public static void main(String args[]) throws Exception {
		String ws_url = "http://bsp-ExecelImportAndValidateUtilTest.sf-express.com:9090/bsp-ois/ws/expressService?wsdl";
		String opName = "sfexpressService";
		String opArgs = "111";
		String request = (String) invokeWsByNameAndPassWord1(ws_url, opName, opArgs);
		logger.info(request);
	}

	private static String getMessage() {
		String ws_url = "http://bsp-ExecelImportAndValidateUtilTest.sf-express.com:9090/bsp-ois/ws/expressService?wsdl";
		String opName = "example";
		Object opArgs[] = { "1111" };

		String request = null;
		try {
			request = (String) invokeWs(ws_url, opName, opArgs);
		} catch (Exception e) {
			logger.error("", e);
		}

		return request;

	}
}
