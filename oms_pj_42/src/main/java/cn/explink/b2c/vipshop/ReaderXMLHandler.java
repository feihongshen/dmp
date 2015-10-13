package cn.explink.b2c.vipshop;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

@Service
public class ReaderXMLHandler {

	public String subStringSOAP(String xml) {
		if (xml == null || "".equals(xml)) {
			return null;
		}
		if (xml.contains("<ns1:out>")) {
			return xml.substring(xml.indexOf("<ns1:out>") + 9, xml.indexOf("</ns1:out>"));
		}
		return null;
	}

	public static String parse(String xml) {
		xml = xml.replaceAll("&", "&amp;");
		xml = xml.replaceAll("<", "&lt;");
		xml = xml.replaceAll(">", "&gt;");
		xml = xml.replaceAll("'", "&apos;");
		xml = xml.replaceAll("\"", "&quot;");
		return xml;
	}

	public static String parseBack(String xml) {
		xml = xml.replaceAll("&amp;", "&");
		xml = xml.replaceAll("&lt;", "<");
		xml = xml.replaceAll("&gt;", ">");
		xml = xml.replaceAll("&apos;", "'");
		xml = xml.replaceAll("&quot;", "\"");
		return xml;
	}

	/**
	 * 解析唯品会订单数据
	 * 
	 * @throws IOException
	 * @throws JDOMException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> parseGetCwbInfo_VipShopXml(String xmlDoc) throws Exception {
		xmlDoc = subStringSOAP(parseBack(xmlDoc));
		Map<String, Object> xmldate = new HashMap<String, Object>();
		StringReader red = new StringReader(xmlDoc);
		InputSource source = new InputSource(red);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(source);
		// 取得根元素
		Element root = doc.getRootElement();
		// System.out.println("根元素：" + root.getName());
		List jiedian = root.getChildren();

		Element fl = null;
		if (jiedian != null && jiedian.size() > 0) {
			for (int i = 0; i < jiedian.size(); i++) {
				fl = (Element) jiedian.get(i);
				List zijiedian = fl.getChildren();
				// 遍历节点下的每个元素
				if (zijiedian != null && zijiedian.size() > 0) {
					List orderList = new ArrayList();
					for (int j = 0; j < zijiedian.size(); j++) {
						Element xet = (Element) zijiedian.get(j);
						String sname = xet.getName();
						String svalue = xet.getValue();
						xmldate.put(sname, svalue);
						// System.out.println("三级节点:" + sname + "==值：" +
						// svalue);

						List thirdList = xet.getChildren();
						if (thirdList != null && thirdList.size() > 0) {

							Map orderMap = new HashMap();
							for (int k = 0; k < thirdList.size(); k++) {
								Element tet = (Element) thirdList.get(k);
								String tname = tet.getName();
								String tvalue = tet.getValue();
								// System.out.println("四级节点:" + tname + "==值：" +
								// tvalue);

								orderMap.put(tname, tvalue);
							}
							orderList.add(orderMap);
							xmldate.put("orderlist", orderList);
						}

					}
				}
			}
		}

		return xmldate;
	}

}
