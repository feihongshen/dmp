package cn.explink.b2c.feiniuwang;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
public class HttpHelper {
	public static void main(String[] args) throws Exception {
		String url = "http://106.2.195.186:8088/dmp40818/feiniuwang/dms/";
		String key = "test";
		String charset = "UTF-8";
		String logistics_interface = "";
		String data_digest = MD5Util.doSign(logistics_interface, key, charset);
		String logistic_provider_id = "";
		String msg_type = "ORDERCREATE";
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("logistics_interface", logistics_interface);
		parameters.put("data_digest", data_digest);
		parameters.put("logistic_provider_id", logistic_provider_id);
		parameters.put("msg_type", msg_type);
		
		//post请求
		String resultXml = postRequest_ResponseBodyAsString(url,parameters);
		
		System.out.println("post请求结果===>"+ resultXml);
	}
	
	public static String postRequest_ResponseBodyAsString(String url, Map<String, String> parameters) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod method = new PostMethod(url);
		int statusCode;
		String responseCharSet = "";
		String responseString = "";
		try {
			for (String key : parameters.keySet()) {
				method.addParameter(key, parameters.get(key));
			}
			method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			statusCode = httpClient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
				System.err.println("Http服务链路异常:服务器状态码为" + statusCode);
			}
			responseCharSet = method.getResponseCharSet();
			responseString = method.getResponseBodyAsString();
			if (responseCharSet.equals("ISO-8859-1")) {
				responseString = new String(responseString.getBytes(responseCharSet), "UTF-8");
			}
			
		} catch (Throwable e) {
			System.err.println("Http服务链路异常:" + e.getMessage() + e);
		} finally {
			method.releaseConnection();
		}
		return responseString;
	}
}
