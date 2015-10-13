package cn.explink.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestHttpServiceHanlder {
	private static Logger logger = LoggerFactory.getLogger(RestHttpServiceHanlder.class);

	/**
	 * 请求URL
	 * 
	 * @param params
	 * @param URL
	 * @return
	 * @throws Exception
	 */
	public static String sendHttptoServer(Map<String, String> params, String URL) {

		StringBuffer sub = new StringBuffer();
		String params_str = null;
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				sub.append(entry.getKey());
				sub.append("=");
				sub.append(entry.getValue());
				sub.append("&");
			}
			params_str = sub.substring(0, sub.length() - 1);
		}

		logger.info("send url={},params={}", URL, params_str);

		StringBuilder buffer = new StringBuilder();
		try {
			URL url = new URL(URL);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(30000); // 设置延迟为30秒
			httpURLConnection.setReadTimeout(30000);
			httpURLConnection.connect();
			OutputStreamWriter reqOut = null;
			if (params_str != null) {
				reqOut = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
			}
			BufferedWriter out = new BufferedWriter(reqOut);
			out.write(params_str.toString());
			out.flush();
			out.close();
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
			// 接收服务器的返回：
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));

			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			logger.error("请求外部url=" + URL + "发生未知异常", e);
		}
		return buffer.toString();
	}

	public static String sendHttptoServer(String content, String URL) throws Exception {

		URL url = new URL(URL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		// httpURLConnection.setRequestProperty("content-type",
		// "text/xml;charset=utf-8");
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		// httpURLConnection.setRequestProperty("contentType", "utf-8");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setConnectTimeout(40000); // 设置延迟为40秒
		httpURLConnection.setReadTimeout(40000);
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

	public static String sendHttptoServerHomego(String content, String URL) throws Exception {

		URL url = new URL(URL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("content-type", "text/xml;charset=utf-8");
		// httpURLConnection.setRequestProperty("Content-Type",
		// "application/x-www-form-urlencoded;charset=utf-8");

		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("contentType", "utf-8");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setConnectTimeout(40000); // 设置延迟为40秒
		httpURLConnection.setReadTimeout(40000);
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

	// public static void main(String[] args) throws Exception {
	// String
	// json="{\"key\":\"qtwlmmb1759\",\"status\":\"5\",\"time\":\"2014-07-31 10:32:29\",\"message\":\"华邦物流(飞狐)配送员李春明已经出发投递，电话: 15032737504\",\"nu\":\"D140727926045\"}";
	//
	// String sss=sendHttptoServer(json, "http://edi.mmb.cn/edi/gen.jsp");
	// System.out.println("sssss:"+sss);
	// }

}