package cn.explink.b2c.tmall;

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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
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

public class TmallConfig {

	private static Logger logger = LoggerFactory.getLogger(TmallConfig.class);
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

	public static void main(String[] args) throws Exception {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><cpCode>NJCB-0010</cpCode><logisticsId>LBX0018029310018</logisticsId><mailNo>0018029310018</mailNo><exceptionCode>1001</exceptionCode><exceptionTime>2014-06-20 09:23:32</exceptionTime><actionCode>P00</actionCode><nextDispatchTime></nextDispatchTime><remark>客户电话错误</remark><operatorMobile>186121213121</operatorMobile><operatorPhone></operatorPhone><extendHandlingContent>null</extendHandlingContent></request>";

		String testxml = "abcde12345";

		System.out.println(encryptSign_Method(testxml, "bmccmail"));
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

	public static String posthttpdata_tmall(String POST_URL, String private_key, String partner, String content, String TmallStateCode, boolean isfactExists) throws Exception {

		String contentmd5data = encryptSign_Method(content, private_key); // 64位MD5加密
		// HttpClient httpClient = new HttpClient();
		HttpClient httpClient = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
		PostMethod postMethod = new PostMethod(POST_URL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// postMethod.getParams().setParameter("content-type",
		// "application/x-www-form-urlencoded;charset=UTF-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("service", "wlb_order_info_sync"), // 写死
				new NameValuePair("sign_type", "MD5"), // 写死
				new NameValuePair("partner", partner), new NameValuePair("sign", contentmd5data), // 签名后的信息
				new NameValuePair("content", content), }; // 原始信息

		logger.info("send tmall isfactExists=" + isfactExists + ",TmallStateCode=" + TmallStateCode + ",service=wlb_order_info_sync,partner=" + partner + ",sign=" + contentmd5data + ",content="
				+ content);
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		httpClient.executeMethod(postMethod); // post数据
		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();
		return str;
	}

	/**
	 * 推送tmall异常信息
	 * 
	 * @param POST_URL
	 * @param private_key
	 * @param partner
	 * @param content
	 * @param TmallStateCode
	 * @param isfactExists
	 * @return
	 * @throws Exception
	 */
	public static String postHttpdataTotmallExpt(String POST_URL, String private_key, String content, String service_code) throws Exception {

		String contentmd5data = encryptSign_Method(content, private_key); // 64位MD5加密
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(POST_URL);
		// postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
		// "UTF-8");
		// postMethod.getParams().setParameter("content-type",
		// "application/x-www-form-urlencoded;charset=UTF-8");
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("logistics_interface", content), // XML
				new NameValuePair("logistic_provider_id", service_code), // cp编号
				new NameValuePair("msg_type", "EXCEPTION_ORDER_DEAL_CALLBACK"), // 写死
				new NameValuePair("data_digest", contentmd5data), // 签名后的信息
		};

		logger.info("推送天猫异常件信息logistics_interface={},logistic_provider_id={},msg_type=EXCEPTION_ORDER_DEAL_CALLBACK,data_digest=" + contentmd5data, content, service_code);
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		httpClient.executeMethod(postMethod); // post数据
		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();
		return str;
	}

	/**
	 * 可解析list
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> Analyz_XmlDocByTmall(String fileName) throws Exception {
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

}
