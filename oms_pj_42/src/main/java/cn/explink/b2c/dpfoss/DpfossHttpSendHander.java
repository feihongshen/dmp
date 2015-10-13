package cn.explink.b2c.dpfoss;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.util.MD5.MD5Util;

public class DpfossHttpSendHander {
	private static Logger logger = LoggerFactory.getLogger(DpfossHttpSendHander.class);

	private static HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
	static {
		httpConnectionManager.getParams().setConnectionTimeout(30 * 1000);
		httpConnectionManager.getParams().setSoTimeout(60 * 1000);
	}

	public static String sendHttpServicetoDpfoss(String request, String url, String servicecode, String agent, String pwd) {

		HttpClient httpClient = new HttpClient();
		httpClient.setHttpConnectionManager(httpConnectionManager);
		PostMethod postMethod = new PostMethod(url);

		postMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		// 设置请求消息
		postMethod.setRequestBody(request);

		// 在HTTP头中添加设置ESBHeader
		postMethod.addRequestHeader("ESB-Version", "1.0");
		postMethod.addRequestHeader("ESB-ESBServiceCode", servicecode);
		postMethod.addRequestHeader("ESB-UserName", agent);
		postMethod.addRequestHeader("ESB-Password", MD5Util.md5(pwd));
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);

		try {
			httpClient.executeMethod(postMethod);

			// 获取执行结果1：成功 0：失败
			Header header = postMethod.getResponseHeader("ESB-ResultCode");
			String result = null;
			if (header != null) {
				result = header.getValue();
			}

			String response = postMethod.getResponseBodyAsString();

			// if(result!=null&&!result.equals("1")){
			// logger.error("请求德邦物流外发单接口头信息返回异常,ESB-ResultCode=0");
			// return "请求德邦物流外发单接口头信息返回异常,ESB-ResultCode=0 ";
			// }

			return response;

		} catch (Exception e) {
			logger.error("处理请求德邦物流推送接口发生未知异常", e);
			return "Connectiontimeout";

		} finally {
			postMethod.releaseConnection();
		}

	}
}
