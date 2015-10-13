package cn.explink.b2c.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author wanghuajun
 * 
 */
@Component
public class HttpClienCommon {
	private static Logger log = LoggerFactory.getLogger(HttpClienCommon.class);

	public static Map<String, String> headMapUtf8 = new HashMap<String, String>();
	public static Map<String, String> headMapGBK = new HashMap<String, String>();
	public static String UTF = "UTF-8";
	public static String GBK = "GBK";
	private static String APPLICATION_JSON = "application/json";

	static {
		headMapUtf8.put("Content", "text/html,charset=utf-8");
		headMapUtf8.put("user-agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)");
		headMapUtf8.put("Cache-Control", "no-cache");
		headMapUtf8.put("If-Modified-Since", "Thu, 29 Jul 2004 02:24:49 GMT");
	}

	static {
		headMapGBK.put("Content", "text/html,charset=GBK");
		headMapGBK.put("user-agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)");
		headMapGBK.put("Cache-Control", "no-cache");
		headMapGBK.put("If-Modified-Since", "Thu, 29 Jul 2004 02:24:49 GMT");
	}

	public String doPost(Map<String, Object> paramMap, Map<String, String> headMap, String url, int connTimeOut, int soTimeOut, String charset) {
		if (connTimeOut == 0) {
			connTimeOut = 10 * 3000;
		}

		if (soTimeOut == 0) {
			soTimeOut = 20 * 3000;
		}

		HttpClient httpClient = new HttpClient();
		PostMethod method = new PostMethod(url);
		HttpConnectionManager httpManager = new SimpleHttpConnectionManager();
		HttpConnectionManagerParams httpParam = new HttpConnectionManagerParams();

		// 设置链接超时时间
		httpParam.setConnectionTimeout(connTimeOut);
		// 设置访问超时时间
		httpParam.setSoTimeout(soTimeOut);
		httpManager.setParams(httpParam);
		// 设置http访问管理机制
		httpClient.setHttpConnectionManager(httpManager);

		// 设置post请求的请求头
		method = (PostMethod) HttpClienCommon.setPostHead(method, headMap);
		// 设置请求的参数
		method = (PostMethod) this.setQueryPair(method, paramMap, charset);
		// 设置http的重复访问机制
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
		String backString = "";

		try {
			int status = httpClient.executeMethod(method);
			if (status == 200) {
				log.info("[请求接口" + url + "返回状态]status=" + status + "[status=200请求成功]");

				backString = method.getResponseBodyAsString();
			} else if (status == 500) {
				JSONObject json = new JSONObject();
				json.put("status", 500);
				return json.toString();
			}

			log.info("backString=" + backString);

		} catch (Exception e) {
			log.info("[请求接口" + url + "异常");
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("status", 500);
			return json.toString();
		} finally {
			method.releaseConnection();
		}

		return backString;
	}

	public String doGet(Map<String, Object> paramMap, Map<String, String> headMap, String url, int connTimeOut, int soTimeOut, String charset) {
		if (connTimeOut == 0) {
			connTimeOut = 10 * 3000;
		}

		if (soTimeOut == 0) {
			soTimeOut = 20 * 3000;
		}

		HttpClient httpClient = new HttpClient();
		GetMethod method = new GetMethod(url);
		HttpConnectionManager httpManager = new SimpleHttpConnectionManager();
		HttpConnectionManagerParams httpParam = new HttpConnectionManagerParams();

		// 设置链接超时时间
		httpParam.setConnectionTimeout(connTimeOut);
		// 设置访问超时时间
		httpParam.setSoTimeout(soTimeOut);
		httpManager.setParams(httpParam);
		// 设置http访问管理机制
		httpClient.setHttpConnectionManager(httpManager);

		// 设置post请求的请求头
		method = (GetMethod) HttpClienCommon.setPostHead(method, headMap);
		// 设置请求的参数
		method = (GetMethod) this.setQueryPair(method, paramMap, charset);
		// 设置http的重复访问机制
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, true));

		String backString = "";

		try {
			int status = httpClient.executeMethod(method);

			if (status == 200) {
				log.info("[请求接口" + url + "返回状态]status=" + status + "[status=200请求成功]");

				backString = method.getResponseBodyAsString();
			} else {
				JSONObject json = new JSONObject();
				json.put("status", 500);
				log.info("[请求接口" + url + "返回状态]status=" + status + "[status=]" + status + "请求成功]");
				return json.toString();
			}

			log.info("backString=" + backString);

		} catch (Exception e) {
			log.info("[请求接口" + url + "异常");
			e.printStackTrace();
			JSONObject j = new JSONObject();
			j.put("status", 500);
			return j.toString();
		} finally {
			method.releaseConnection();
		}

		return backString;
	}

	/**
	 * @Title:getPostParam
	 * @Description:设置请求头
	 * @param paramMap
	 * @return HttpMethodParams
	 */
	private static HttpMethodBase setPostHead(HttpMethodBase method, Map<String, String> paramMap) {
		if (paramMap != null && paramMap.size() > 0) {
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				method.setRequestHeader(entry.getKey(), entry.getValue());
			}
		}

		return method;
	}

	protected static HttpMethodBase setQueryPair(HttpMethodBase method, Map<String, Object> paramMap, String charset) {
		if (paramMap != null && paramMap.size() > 0) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();

			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				if (entry.getKey() != null) {
					NameValuePair name = new NameValuePair();
					name.setName(entry.getKey());
					try {
						name.setValue(entry.getValue() != null ? entry.getValue().toString() : "");
					} catch (Exception e) {
						e.printStackTrace();
					}
					list.add(name);
				}
			}

			if (list != null && list.size() > 0) {
				NameValuePair[] pairArr = new NameValuePair[list.size()];

				pairArr = list.toArray(pairArr);
				method.setQueryString(EncodingUtil.formUrlEncode(pairArr, charset));
			}

		}

		return method;
	}

	public static String doPostStream(String json, Map<String, String> headMap, String url, int connTimeOut, int soTimeOut, String charset) {
		if (connTimeOut == 0) {
			connTimeOut = 10 * 3000;
		}

		if (soTimeOut == 0) {
			soTimeOut = 20 * 3000;
		}

		HttpClient httpClient = new HttpClient();
		PostMethod method = new PostMethod(url);
		HttpConnectionManager httpManager = new SimpleHttpConnectionManager();
		HttpConnectionManagerParams httpParam = new HttpConnectionManagerParams();

		// 设置链接超时时间
		httpParam.setConnectionTimeout(connTimeOut);
		// 设置访问超时时间
		httpParam.setSoTimeout(soTimeOut);
		httpManager.setParams(httpParam);
		// 设置http访问管理机制
		httpClient.setHttpConnectionManager(httpManager);

		// 设置post请求的请求头
		method = (PostMethod) setPostHead(method, headMap);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
		// 设置请求的参数
		try {
			RequestEntity requestEntity = new StringRequestEntity(json, APPLICATION_JSON, charset);
			method.setRequestEntity(requestEntity);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// 设置http的重复访问机制
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));

		StringBuffer backString = new StringBuffer();

		try {
			int status = httpClient.executeMethod(method);

			if (status == 200) {
				log.info("[请求接口" + url + "返回状态]status=" + status + "[status=200请求成功]");

				InputStream backInputStream = method.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(backInputStream, charset));
				String str = null;
				while ((str = br.readLine()) != null) {
					backString.append(str);
				}
			} else if (status == 500) {
				JSONObject j = new JSONObject();
				j.put("status", 500);
				return j.toString();
			}
			log.info("status=" + status + ",backString=" + backString);

		} catch (Exception e) {
			log.info("[请求接口" + url + "异常");
			e.printStackTrace();
			JSONObject j = new JSONObject();
			j.put("status", 500);
			return j.toString();
		} finally {
			method.releaseConnection();
		}

		return backString.toString();
	}

	public static String post(Map<String, Object> paramMap, Map<String, String> headMap, String url, int connTimeOut, int soTimeOut, String charset) {
		HttpClient httpClient = new HttpClient();
		PostMethod method = new PostMethod(url);
		HttpConnectionManager httpManager = new SimpleHttpConnectionManager();
		HttpConnectionManagerParams httpParam = new HttpConnectionManagerParams();

		// 设置链接超时时间
		httpParam.setConnectionTimeout(connTimeOut);
		// 设置访问超时时间
		httpParam.setSoTimeout(soTimeOut);
		httpManager.setParams(httpParam);
		// 设置http访问管理机制
		httpClient.setHttpConnectionManager(httpManager);

		// 设置post请求的请求头
		method = (PostMethod) HttpClienCommon.setPostHead(method, headMap);
		// 设置请求的参数
		method = (PostMethod) setQueryPair(method, paramMap, charset);
		// 设置http的重复访问机制
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
		String backString = "";

		try {
			int status = httpClient.executeMethod(method);
			if (status == 200) {
				log.info("[请求接口" + url + "返回状态]status=" + status + "[status=200请求成功]");

				backString = method.getResponseBodyAsString();
			} else if (status == 500) {
				JSONObject json = new JSONObject();
				json.put("status", 500);
				return json.toString();
			}

			log.info("backString=" + backString);

		} catch (Exception e) {
			log.info("[请求接口" + url + "异常");
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("status", 500);
			return json.toString();
		} finally {
			method.releaseConnection();
		}

		return backString;
	}

	public static void main(String[] args) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		JSONObject json = new JSONObject();
		json.put("userCode", "123");
		json.put("requestTime", "20140411150025");
		json.put("sign", "25cc274b2fb68dd83ea0a338e888f93d");
		json.put("pageSize", 200);
		paramMap.put("content", json.toString());

		String s = HttpClienCommon.post(paramMap, null, "http://218.104.234.195:8883/efuture-service/rest/logistics/download", 5000, 5000, "utf-8");
		System.out.println("###" + s);
	}
}
