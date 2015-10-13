package cn.explink.b2c.wangjiu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tmall的配置信息.
 * 
 * @author Administrator
 *
 */

public class WangjiuConfig {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 字符编码格式 目前支持 UTF-8 或gbk
	public static final String input_charset = "UTF-8";
	// 签名方式 不需修改
	public static String sign_type = "MD5";
	// 消息通知类型
	public static String notify_type = "tms_order_notify";

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
	 * 可解析list
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> analyzXmlWangjiu(String fileName) throws Exception {
		// File inputXml = new File(fileName);
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes());
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "UTF-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			returnMap.put(employee.getName(), employee.getText());
			List<Map<String, Object>> jarry = new ArrayList<Map<String, Object>>();
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				returnMap.put(node.getName(), node.getText());

			}
		}
		return returnMap;
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
