package cn.explink.b2c.aa;

import java.net.URL;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;

import cn.explink.b2c.gome.SOAPHandle;

public class WebServiceHandler {
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
		call.setClientHandlers(new SOAPHandle(userName, passWord), null);
		Object returnValue = (Object) call.invoke(method, params);
		return returnValue;
	}

	public static void main(String args[]) throws Exception {
		// String
		// wsdlUrl="http://221.226.72.18:8009/default/POS_PDA_DLVERService?wsdl";
		// String opName="GetOrdWayBillInfoForD2D";
		// String begin_date="20130705120055";
		// String end_date="20130816120055";
		// Object parms[]={begin_date,end_date,"123456"};
		// Object wsresult=invokeWs(wsdlUrl,opName,parms);
		// System.out.println(wsresult);
		System.out.println(getMessage());
	}

	private static String getMessage() {
		String ws_url = "http://localhost:8080/dmp_ws/services/Hello?wsdl";
		String opName = "example";
		Object opArgs[] = { "你好啊！webservice！" };

		String request = null;
		try {
			request = (String) invokeWs(ws_url, opName, opArgs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return request;

	}

	// /**
	// * 用axis2调用远程WEB服务，推荐使用，因为不需要解析WSDL文档
	// *
	// * @param endpointUrl
	// * WEB服务末端URL
	// * @param nameSpace
	// * WEB服务命名空间
	// * @param methodName
	// * 方法名
	// * @param parms
	// * 要传的参数数组
	// * @return 对方响应内容
	// * @throws AxisFault
	// */
	// public static String invokeWs1(String endpointUrl, String nameSpace,
	// String methodName, Map<String, Object> parms) throws Exception {
	// try {
	// ServiceClient sc = new ServiceClient();
	// Options opts = sc.getOptions();
	// opts.setTo(new EndpointReference(endpointUrl));
	// opts.setAction("urn:" + methodName);
	// opts.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
	// opts.setProperty(HTTPConstants.CHUNKED, false);
	// opts.setProperty(HTTPConstants.MC_ACCEPT_GZIP, Boolean.TRUE);
	// opts.setTransportInProtocol("SOAP");
	// opts.setTimeOutInMilliSeconds(60000);
	// OMFactory fac = OMAbstractFactory.getOMFactory();
	// OMNamespace omNs = fac.createOMNamespace(nameSpace, "tns");
	// OMElement method = fac.createOMElement(methodName, omNs);
	// for (String key : parms.keySet()) {
	// OMElement param = fac.createOMElement(key, omNs);
	// param.setText(String.valueOf(parms.get(key)));
	// method.addChild(param);
	// }
	// OMElement res = sc.sendReceive(method);
	// String content = res.getFirstElement().getText();
	// sc.cleanupTransport();
	// return content;
	// } catch (Throwable e) {
	// e.printStackTrace();
	// throw new RuntimeException("WebService服务链路异常:"+e.getMessage(), e);
	// }
	// }

}
