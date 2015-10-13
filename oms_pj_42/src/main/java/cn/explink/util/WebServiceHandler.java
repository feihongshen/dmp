package cn.explink.util;

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
			client.setTimeout(30000); // 设置超时时间为30秒
			results = client.invoke(opName, opArgs);
		} catch (Throwable e) {
			e.printStackTrace();
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

	public static Object LandWsByNameAndPassWord(String url, String method, String pram) throws Exception {
		Object[] params = new Object[] { pram };
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(url);
		call.setTimeout(30000);
		Object returnValue = (Object) call.invoke(method, params);
		return returnValue;
	}

	public static Object invokeWsByObjectParms(String url, String method, Object[] pram) throws Exception {
		Object[] params = new Object[] { pram };
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(url);
		call.setTimeout(30000);
		Object returnValue = (Object) call.invoke(method, params);
		return returnValue;
	}

	public static void main(String args[]) throws Exception {

		// Object
		// parms[]={rtn_id,ord_id,dlver_cd,payee,disburse_amt,disburse_date,hxg.getPassword()};

		String ws_url = "http://bsp-test.sf-express.com:9090/bsp-ois/ws/expressService?wsdl";
		String opName = "sfexpressService";
		String xml = "<Request service='OrderService' lang='zh-CN'><Head>BJXXHKZX,682AtACqIz1dCzXS</Head><Body><Order><orderid>LBXTST00260050</orderid><express_type>1</express_type><j_province>北京</j_province><j_city>北京市</j_city><j_company>北京迅祥</j_company><j_contact>张三</j_contact><j_tel>010-54544</j_tel><j_address>北京市朝阳区建国路88号</j_address><d_province></d_province><d_city></d_city><d_company></d_company><d_contact>张三</d_contact><d_tel> 13426480484</d_tel><d_address>北京是朝阳区建国路111号</d_address><parcel_quantity>1</parcel_quantity><pay_method>1</pay_method><OrderOption><custid>1023125456</custid><cargo></cargo><cargo_count>1</cargo_count><cargo_unit/><cargo_weight>0.000</cargo_weight><cargo_amount>0.50</cargo_amount><cargo_amount>0.50</cargo_amount><cargo_total_weight>0.000</cargo_total_weight><sendstarttime>2014-05-05 20:22:02</sendstarttime></OrderOption><AddedService><name>COD</name><value>0.50</value><value1>1023125456</value1></AddedService></Order></Body>";
		Object parm = new Object[] { xml };
		Object wsresult = invokeWs(ws_url, opName, parm);
		System.out.println(wsresult);

	}

	// public static String invokeService(){
	// org.codehaus.xfire.service.Service serviceModel = new
	// ObjectServiceFactory().create(CommonServiceService.class);
	// try {
	// orderMngService = (OrderMngServiceWs) new
	// XFireProxyFactory().create(serviceModel, oms_order_ws_URL);
	// } catch (Exception e) {
	// logger.error("exception happened when notify oms order finished. ", e);
	// }
	// Client client = Client.getInstance(orderMngService);
	// client.addOutHandler(omsAuthenticationHandler);
	//
	// }

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
