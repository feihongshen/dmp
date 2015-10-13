package cn.explink.b2c.huitongtx;

import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * Tmall的配置信息.
 * 
 * @author Administrator
 *
 */

public class HuitongtxConfig {

	private static Logger logger = LoggerFactory.getLogger(HuitongtxConfig.class);

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 字符编码格式 目前支持 UTF-8 或gbk
	public static final String input_charset = "UTF-8";
	// 签名方式 不需修改
	public static String sign_type = "MD5";

	// 淘宝的签名方式 md5
	public static String encryptSign_Method(String content, String keyValue) {
		try {
			if (keyValue != null) {
				return base64(MD5(content + keyValue, input_charset), input_charset);
			}
			return base64(MD5(content, input_charset), input_charset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keyValue;
	}

	/**
	 * 对传入的字符串进行MD5加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String MD5(String plainText, String charset) throws Exception {

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes(charset));
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	/**
	 * base64编码
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String base64(String str, String charset) throws Exception {
		return (new sun.misc.BASE64Encoder()).encode(str.getBytes(charset));
	}

	/**
	 * 把数组所有元素排序，并按照“参数+参数值”的模式拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			prestr = prestr + key + value;
		}

		return prestr;
	}

	public static Map<String, String> buildParmsMap(String method, String timestamp, String app_key, String app_secret, String data) {
		Map<String, String> parmsMap = new HashMap<String, String>();
		parmsMap.put("method", method);
		parmsMap.put("timestamp", timestamp);
		parmsMap.put("app_key", app_key);

		parmsMap.put("data", data);

		return parmsMap;
	}

	public static String posthttpdata_Httx(String POST_URL, String app_key, String content, String statuscode, String private_key) {

		String method = "ips2.send.orderstatus";
		String timestamp = DateTimeUtil.getNowTime();
		// 生成sign
		Map<String, String> params = HuitongtxConfig.buildParmsMap(method, timestamp, app_key, private_key, content);
		String sign_str = private_key + HuitongtxConfig.createLinkString(params) + private_key;
		// logger.info("发送汇通天下签名串="+sign_str);
		String sign = MD5Util.md5(sign_str).toUpperCase();
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(POST_URL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("method", "ips2.send.orderstatus"), // 写死
				new NameValuePair("timestamp", DateTimeUtil.getNowTime()), new NameValuePair("app_key", app_key), new NameValuePair("sign", sign), // 签名后的信息
				new NameValuePair("data", content), }; // 原始信息

		logger.info("send 汇通天下 url=" + POST_URL + ",status_code=" + statuscode + ",method=ips2.send.orderstatus,app_key=" + app_key + ",sign=" + sign + ",data=" + content);
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		try {
			httpClient.executeMethod(postMethod);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // post数据
		String str = "";
		try {
			str = postMethod.getResponseBodyAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postMethod.releaseConnection();
		return str;
	}

}
