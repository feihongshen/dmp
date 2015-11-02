package cn.explink.b2c.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
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

	public static void main(String[] args) throws Exception {
		Map<String, String> maps = new HashMap<String, String>();
		String value = "%7B%22province%22%3A%22%E5%9B%9B%E5%B7%9D%E7%9C%81%22%2C%22city%22%3A%22%E9%98%BF%E5%9D%9D%E8%97%8F%E6%97%8F%E7%BE%8C%E6%97%8F%E8%87%AA%E6%B2%BB%E5%B7%9E%22%2C%22district%22%3A%22%E4%B9%9D%E5%AF%A8%E6%B2%9F%E5%8E%BF%22%2C%22address%22%3A%22%E6%BC%B3%E6%89%8E%E9%95%87%E7%81%AB%E5%9C%B0%E5%9D%9D%E6%B0%91%E6%97%8F%E9%A5%AD%E5%BA%97%E6%95%B0%E7%A0%81%E5%BD%A9%E6%89%A9%22%7D";
		maps.put("param", value);

		System.out.println(sendHttptoServer("param", "http://58.83.193.121/cc/jumei/addressmatch"));
	}

	public static String sendHttptoServer(String content, String URL) throws Exception {

		URL url = new URL(URL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("content-type", "text/xml");
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

	// public static String sendHttptoServer(String content, String URL) throws
	// IOException {
	//
	// StringBuilder buffer=null;
	// BufferedWriter out=null;
	// OutputStreamWriter reqOut=null;
	// try {
	// URL url = new URL(URL);
	// HttpURLConnection httpURLConnection = (HttpURLConnection)
	// url.openConnection();
	// httpURLConnection.setRequestProperty("content-type", "text/xml");
	// httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
	// httpURLConnection.setRequestProperty("contentType", "utf-8");
	// httpURLConnection.setDoOutput(true);
	// httpURLConnection.setDoInput(true);
	// httpURLConnection.setRequestMethod("POST");
	// httpURLConnection.setConnectTimeout(40000); //设置延迟为40秒
	// httpURLConnection.setReadTimeout(40000);
	// httpURLConnection.connect();
	//
	// if(content!=null){
	// reqOut=new
	// OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8");
	// }
	// out = new BufferedWriter(reqOut);
	// out.write(content);
	//
	// // 接收服务器的返回：
	// BufferedReader reader = new BufferedReader(new
	// InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
	// buffer = new StringBuilder();
	// String line = null;
	// while ((line = reader.readLine()) != null) {
	// buffer.append(line);
	// }
	// } catch (Exception e) {
	// logger.error("请求外部接口异常url="+URL,e);
	// } finally{
	// if(out!=null){
	// out.flush();
	// out.close();
	//
	// }
	//
	//
	// }
	//
	//
	// return buffer.toString();
	// }

	public static String sendHttptoServer(String content, String requestMethod, String URL) {
		StringBuffer buffer = new StringBuffer();
		URL url = null;
		try {
			url = new URL(URL);

			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("content-type", "json");
			httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpURLConnection.setRequestProperty("contentType", "utf-8");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod(requestMethod);
			httpURLConnection.setConnectTimeout(40000); // 设置延迟为40秒
			httpURLConnection.setReadTimeout(40000);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpURLConnection.connect();
			}

			// 当有数据需要提交时
			if (null != content) {
				OutputStream outputStream = httpURLConnection.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(content.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpURLConnection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpURLConnection.disconnect();

		} catch (Exception e) {
			logger.error("请求API异常,url=" + URL + ",content=" + content, e);
		}
		return buffer.toString();
	}
	
public static String sendHttptoServer_Json(String content, String URL) throws Exception {
		
		URL url = new URL(URL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("content-type", "application/json");
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("contentType", "utf-8");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setConnectTimeout(40000);   //设置延迟为40秒
		httpURLConnection.setReadTimeout(40000);
		httpURLConnection.connect();
		OutputStreamWriter reqOut=null;
		if(content!=null){
			reqOut=new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8");
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

}