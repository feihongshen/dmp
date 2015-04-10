package cn.explink.b2c.tmall;

import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.util.ArrayList;
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
import org.springframework.security.crypto.codec.Base64;
import org.xml.sax.InputSource;

import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.MD5.MD5Util;

/**
 * Tmall的配置信息.
 * 
 * @author Administrator
 *
 */

public class TmallConfig {

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
		return new String(Base64.encode(str.getBytes(charset)));
//		return (new sun.misc.BASE64Encoder()).encode(str.getBytes(charset));
	}

	/**
	 * 解析tmall 请求信息
	 * 
	 * @param xmlDoc
	 *            XML
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> Analyz_XmlDocByRequestXML(String xmlDoc) throws JDOMException, IOException {
		Map xmldate = new HashMap();
		StringReader red = new StringReader(xmlDoc);
		InputSource source = new InputSource(red);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(source);
		// 取得根元素
		Element root = doc.getRootElement();
		List jiedian = root.getChildren();
		Element fl = null;
		if (jiedian != null && jiedian.size() > 0) {
			for (int i = 0; i < jiedian.size(); i++) {
				fl = (Element) jiedian.get(i);
				String fname = fl.getName();
				String fvalue = fl.getValue();
				xmldate.put(fname, fvalue);
				List zijiedian = fl.getChildren();
				// 遍历节点下的每个元素
				if (zijiedian != null && zijiedian.size() > 0) {
					List orderList = new ArrayList();
					for (int j = 0; j < zijiedian.size(); j++) {
						Element xet = (Element) zijiedian.get(j);
						String sname = xet.getName();
						String svalue = xet.getValue();
						xmldate.put(sname, svalue);
					}
				}
			}
		}

		return xmldate;
	}

	/**
	 * 解析方式 JSON
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static Map<String, String> Analyz_XmlDocByRequestJSON(String xmlDoc) throws JsonParseException, JsonMappingException, IOException {
		TmallXMLNote tmallnote = jacksonmapper.readValue(xmlDoc, TmallXMLNote.class);
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_code", tmallnote.getOrder_code());
		map.put("order_flag", tmallnote.getOrder_flag());
		map.put("package_volume", tmallnote.getPackage_volume());
		map.put("package_weight", tmallnote.getPackage_weight());
		map.put("receiver_address", tmallnote.getReceiver_address());
		map.put("receiver_city", tmallnote.getReceiver_city());
		map.put("receiver_district", tmallnote.getReceiver_district());
		map.put("receiver_mobile", tmallnote.getReceiver_mobile());
		map.put("receiver_name", tmallnote.getReceiver_name());
		map.put("receiver_phone", tmallnote.getReceiver_phone());
		map.put("receiver_province", tmallnote.getReceiver_province());
		map.put("receiver_zip", tmallnote.getReceiver_zip());
		map.put("schedule_end", tmallnote.getSchedule_end());
		map.put("schedule_start", tmallnote.getSchedule_start());
		map.put("tms_service_code", tmallnote.getTms_service_code());
		map.put("wms_address", tmallnote.getWms_address());
		map.put("wms_code", tmallnote.getWms_code());
		map.put("schedule_type", String.valueOf(tmallnote.getSchedule_type()));
		map.put("total_amount", String.valueOf(tmallnote.getTotal_amount()));

		return map;
	}

}
