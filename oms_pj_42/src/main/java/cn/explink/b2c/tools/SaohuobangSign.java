package cn.explink.b2c.tools;

import java.security.MessageDigest;

import org.codehaus.jackson.map.ObjectMapper;

public class SaohuobangSign {
	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();
	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 字符编码格式 目前支持 UTF-8 或gbk
	public static final String input_charset = "UTF-8";
	// 签名方式 不需修改
	public static String sign_type = "MD5";
	// 消息通知类型
	public static String notify_type = "tms_order_notify";

	public static String encryptSign_Method(String content, String keyValue) throws Exception {
		if (keyValue != null) {
			return base64(MD5(content + keyValue, input_charset), input_charset);
		}
		return base64(MD5(content, input_charset), input_charset);
	}

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

}
