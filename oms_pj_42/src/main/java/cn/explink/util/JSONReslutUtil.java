package cn.explink.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONReslutUtil {
	private static Logger logger = LoggerFactory.getLogger(JSONReslutUtil.class);

	/**
	 * @param url
	 *            请求的新接口路径
	 * @param method
	 *            请求方式（post）
	 * @return
	 * @throws IOException
	 */
	public static String getResultMessage(String url, String params, String method) throws IOException {
		StringBuffer re = new StringBuffer();// http.getViaHttpConnection(url);
		InputStream input = null;
		BufferedReader in = null;
		try {
			URL sendurl = new URL(url);
			HttpURLConnection httpUrl = (HttpURLConnection) sendurl.openConnection();
			httpUrl.setDoInput(true);
			httpUrl.setDoOutput(true);
			httpUrl.setUseCaches(false);
			httpUrl.setInstanceFollowRedirects(true);

			httpUrl.setRequestMethod(method);
			httpUrl.connect();
			httpUrl.getOutputStream().write(params.getBytes("UTF-8"));

			input = httpUrl.getInputStream();
			in = new BufferedReader(new InputStreamReader(input, "UTF-8"));

			while (in.ready()) {
				re.append(in.readLine());
			}

			httpUrl.disconnect();
		} catch (Exception e) {
			logger.error("调用DMP异常url=" + url, e);
		} finally {
			if (input != null) {
				input.close();
				input = null;
			}
			if (in != null) {
				in.close();
				in = null;
			}

		}

		logger.info("RE : " + method + " : " + url + "?" + params + " ");
		return re.toString();
	}

	/**
	 * 客服查询 短时间请求
	 * 
	 * @param url
	 *            请求的新接口路径
	 * @param method
	 *            请求方式（post）
	 * @return
	 * @throws IOException
	 */
	public static String getResultMessageShort(SocketAddress socketAddress, String url, String params, String method) throws IOException {
		String re = "";
		// Socket socket1 = new Socket("192.168.180.2", 8888);
		// SocketAddress socketAddress = socket1.getRemoteSocketAddress();
		// socket1.close();
		Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
		URL sendurl = new URL(url);
		HttpURLConnection httpUrl = (HttpURLConnection) sendurl.openConnection(proxy);
		httpUrl.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=UTF-8");
		httpUrl.setDoInput(true);
		httpUrl.setDoOutput(true);
		httpUrl.setUseCaches(false);
		httpUrl.setConnectTimeout(5000);
		httpUrl.setReadTimeout(5000);
		httpUrl.setInstanceFollowRedirects(true);
		httpUrl.setRequestMethod("GET");// POST||GET
		httpUrl.connect();
		httpUrl.getOutputStream().write(params.getBytes("UTF-8"));
		BufferedReader in = new BufferedReader(new InputStreamReader(httpUrl.getInputStream(), "UTF-8"));
		while (in.ready()) {
			re = re + in.readLine();
		}
		httpUrl.disconnect();
		logger.info("RE : " + method + " : " + url + "?" + params + "");
		return re;
	}

	/**
	 * 定时器调用 长时间请求
	 * 
	 * @param url
	 *            请求的新接口路径
	 * @param method
	 *            请求方式（post）
	 * @return
	 * @throws IOException
	 */
	public static String getResultMessagelongJob(SocketAddress socketAddress, String url, String params, String method) throws IOException {
		String re = "";
		Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
		URL sendurl = new URL(url);
		HttpURLConnection httpUrl = (HttpURLConnection) sendurl.openConnection(proxy);
		httpUrl.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=UTF-8");
		httpUrl.setDoInput(true);
		httpUrl.setDoOutput(true);
		httpUrl.setUseCaches(false);
		httpUrl.setConnectTimeout(20000);
		httpUrl.setReadTimeout(20000);
		httpUrl.setInstanceFollowRedirects(true);
		httpUrl.setRequestMethod("GET");// POST||GET
		httpUrl.connect();
		httpUrl.getOutputStream().write(params.getBytes("UTF-8"));
		BufferedReader in = new BufferedReader(new InputStreamReader(httpUrl.getInputStream(), "UTF-8"));
		while (in.ready()) {
			re = re + in.readLine();
		}
		httpUrl.disconnect();
		logger.info("RE : " + method + " : " + url + "?" + params + " ");
		return re;
	}

	/**
	 * 定时器调用 长时间请求 统一使用
	 * 
	 * @param url
	 *            请求的新接口路径
	 * @param method
	 *            请求方式（post）
	 * @return
	 * @throws IOException
	 */
	public static String getResultMessageByProxy(SocketAddress socketAddress, String url, String params, String method) throws IOException {
		String re = "";
		Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
		URL sendurl = new URL(url);
		HttpURLConnection httpUrl = (HttpURLConnection) sendurl.openConnection(proxy);
		httpUrl.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=UTF-8");
		httpUrl.setDoInput(true);
		httpUrl.setDoOutput(true);
		httpUrl.setUseCaches(false);
		httpUrl.setConnectTimeout(20000);
		httpUrl.setReadTimeout(20000);
		httpUrl.setInstanceFollowRedirects(true);
		httpUrl.setRequestMethod("GET");// POST||GET
		httpUrl.connect();
		httpUrl.getOutputStream().write(params.getBytes("UTF-8"));
		BufferedReader in = new BufferedReader(new InputStreamReader(httpUrl.getInputStream(), "UTF-8"));
		while (in.ready()) {
			re = re + in.readLine();
		}
		httpUrl.disconnect();
		logger.info("RE : " + method + " : " + url + "?" + params + "");
		return re;
	}

	/**
	 * 客服查询 山东和天津腾讯达 短时间请求
	 * 
	 * @param url
	 *            请求的新接口路径
	 * @param method
	 *            请求方式（post）
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static String getResultMessageShortByGBK(String url, String params, String method) throws IOException, DocumentException {
		URL sendurl = new URL(url);
		HttpURLConnection httpUrl = (HttpURLConnection) sendurl.openConnection();
		httpUrl.setDoInput(true);
		httpUrl.setDoOutput(true);
		httpUrl.setUseCaches(false);
		httpUrl.setConnectTimeout(5000);
		httpUrl.setReadTimeout(5000);
		httpUrl.setInstanceFollowRedirects(true);
		httpUrl.setRequestMethod(method);// POST||GET
		httpUrl.connect();
		httpUrl.getOutputStream().write(params.getBytes("UTF-8"));
		new InputStreamReader(httpUrl.getInputStream(), "gbk");
		SAXReader reader = new SAXReader();
		Document document = reader.read(new InputStreamReader(httpUrl.getInputStream(), "gbk"));
		Element rootElm = document.getRootElement();
		List nodes = rootElm.elements("row");
		for (int i = 0; i < nodes.size(); i++) {
			Element elm = (Element) nodes.get(i);
			logger.info("RE : cwb:" + elm.elementText("cwb") + " branchname:" + new String(elm.elementText("branchname").getBytes(), "utf-8") + " trackdatetime:" + elm.elementText("trackdatetime")
					+ " trackevent:" + elm.elementText("trackevent") + " podresultname:" + elm.elementText("podresultname"));
		}
		httpUrl.disconnect();
		logger.info("RE : " + method + " : " + url + "?" + params);
		return "";
	}

	public static void sendUrl(String param) {

		try {
			URL url = new URL(param);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
			// 设定请求的方法为"POST"，默认是GET
			httpUrlConnection.setRequestMethod("GET");
			// 设置为3 毫秒超时失效
			httpUrlConnection.setConnectTimeout(3000);
			httpUrlConnection.setReadTimeout(1);
			httpUrlConnection.connect();// 发送请求
		} catch (Exception e) {
		}

	}

	public static String getResultMessageTest(String url, String params, String method) throws IOException {
		/*
		 * String re ="";// http.getViaHttpConnection(url); URL sendurl = new
		 * URL(url); HttpURLConnection
		 * httpUrl=(HttpURLConnection)sendurl.openConnection();
		 * httpUrl.setDoInput(true); httpUrl.setDoOutput(true);
		 * httpUrl.setUseCaches(false);
		 * httpUrl.setInstanceFollowRedirects(true);
		 * httpUrl.setRequestMethod(method);//POST||GET httpUrl.connect();
		 * httpUrl.getOutputStream().write(params.getBytes("UTF-8"));
		 * BufferedReader in = new BufferedReader(new
		 * InputStreamReader(httpUrl.getInputStream(), "UTF-8"));
		 * while(in.ready()) re = re+in.readLine(); httpUrl.disconnect();
		 * logger.info("RE : "+method+" : "+url+"?"+params+" : "+re); return re;
		 */

		String responseXml = "";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		String[] paramStr = params.split("&");// 按“&”拆分
		if (paramStr != null && paramStr.length > 0) {
			for (int i = 0; i < paramStr.length; i++) {
				postMethod.addParameter(paramStr[i].split("=")[0], paramStr[i].split("=")[1]);// 按“=”拆分，第一个做为参数名，第二个作为参数值
			}

		}
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(postMethod); // post数据
			responseXml = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return responseXml;

	}

	public static String ExplinkHTTPClient_Send(String URL, String para_xml, String status_type, String MD5Data) {
		String responseXml = "";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(URL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		NameValuePair[] data = { new NameValuePair("para_xml", para_xml), new NameValuePair("sign_type", "MD5"), new NameValuePair("sign_str", MD5Data), new NameValuePair("flow_type", status_type), };

		postMethod.setRequestBody(data);
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(postMethod); // post数据
			responseXml = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return responseXml;

	}

	public static String SendHttptoServer(String content, String URL) throws Exception {

		URL url = new URL(URL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("content-type", "text/xml");
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("contentType", "utf-8");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setConnectTimeout(30000); // 设置延迟为40秒
		httpURLConnection.setReadTimeout(30000);
		httpURLConnection.connect();
		OutputStreamWriter reqOut = null;
		if (content != null) {
			reqOut = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
		}
		BufferedWriter out = new BufferedWriter(reqOut);
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
	 * 支持参数传递 map类型参数
	 * 
	 * @param parmsMap
	 * @param post_url
	 * @return
	 * @throws Exception
	 */
	public static String sendHTTPServerByParms(Map<String, String> parmsMap, String post_url) throws Exception {

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(post_url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000);
		postMethod.getParams().setSoTimeout(30 * 1000);

		List<String> keys = new ArrayList<String>(parmsMap.keySet());

		String loggerinfo = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = parmsMap.get(key);
			postMethod.addParameter(new NameValuePair(key, value));

			loggerinfo += (key + "=" + value + "&");

		}
		logger.info("send parms:{},url:{}", loggerinfo, post_url);

		// 将表单的值放入postMethod中
		httpClient.executeMethod(postMethod); // post数据

		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();

		return str;
	}

}
