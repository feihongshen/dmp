package cn.explink.b2c.huitongtx;

import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import cn.explink.pos.tools.JacksonMapper;

/**
 * Tmall的配置信息.
 * 
 * @author Administrator
 *
 */

public class HuitongtxConfig {

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();
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
		// parmsMap.put("app_secret", app_secret);

		return parmsMap;
	}

}
