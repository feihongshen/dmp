package cn.explink.b2c.wangjiu;

import java.security.MessageDigest;

import org.codehaus.jackson.map.ObjectMapper;

import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.MD5.MD5Util;
import org.apache.commons.codec.binary.Base64;

/**
 * Tmall的配置信息.
 * 
 * @author Administrator
 *
 */

public class WangjiuConfig {

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();
	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 字符编码格式 目前支持 UTF-8 或gbk
	public static final String input_charset = "UTF-8";
	// 签名方式 不需修改
	public static String sign_type = "MD5";
	// 消息通知类型
	public static String notify_type = "tms_order_notify";

	public static void main(String[] args) {
		try {
			String password = encryptSign_Method("12345", "12");
			String str = "123456789" + "explink";
			String md5str = MD5Util.md5(str).substring(8, 24);
			System.out.println("md5str=" + md5str);
			String base64str = base64(md5str, "UTF-8");

			String CheckMd5 = MD5Util.md5(str).substring(8, 24);
			System.out.println("===16位MD5===" + CheckMd5);
			System.out.println("===base64===" + base64str);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 淘宝的签名方式 md5
	public static String encryptSign_Method(String content, String keyValue) throws Exception {
		if (keyValue != null) {
			return base64(MD5(content + keyValue, input_charset), input_charset);
		}
		return base64(MD5(content, input_charset), input_charset);
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

	public static String base64Md5Result(String xml, String customerCode, String secretKey) throws Exception {
		MessageDigest messagedigest = MessageDigest.getInstance("MD5");
		messagedigest.update((xml + customerCode + secretKey).getBytes("UTF-8"));
		byte[] abyte0 = messagedigest.digest();
		String data_digest = new String(Base64.encodeBase64(abyte0));
		return data_digest;
	}

}
