package cn.explink.b2c.vipshop;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jdom.JDOMException;
import org.springframework.stereotype.Service;

@Service
public class ReaderXMLHandler {

	public String subStringSOAP(String xml) {
		String subStr = null;
		if (xml != null && !"".equals(xml)) {
			subStr = xml.substring(xml.indexOf("<ns1:out>") + 9, xml.indexOf("</ns1:out>"));
		}
		return subStr;
	}
	
	public String parseOXORspSOAP(String xml){
		String subStr = null;
		if (xml != null && !"".equals(xml)) {
			subStr = xml.substring(xml.indexOf("<return>") + 8, xml.indexOf("</return>"));
		}
		return subStr;
	}

	private static SOAPMessage formatSoapString(String soapString) {
		MessageFactory msgFactory;
		try {
			msgFactory = MessageFactory.newInstance();
			SOAPMessage reqMsg = msgFactory.createMessage(new MimeHeaders(),
					new ByteArrayInputStream(soapString.getBytes("UTF-8")));
			reqMsg.saveChanges();
			return reqMsg;
		} catch (Exception e) {
			return null;
		}
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
	public Map<String, Object> parserXmlToJSONObjectByArray(String fileName) throws Exception {
		// File inputXml = new File(fileName);
		fileName = subStringSOAP(parseBack(fileName));

		InputStream iStream = new ByteArrayInputStream(fileName.getBytes("UTF-8"));
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "UTF-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		Map<String, Object> jsontotal = new HashMap<String, Object>();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			List<Map<String, Object>> jarry = new ArrayList<Map<String, Object>>();
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				returnMap.put(node.getName(), node.getText());
				Map<String, Object> jsondetail = new HashMap<String, Object>();
				for (Iterator<Element> k = node.elementIterator(); k.hasNext();) {
					Element node_child = (Element) k.next();
					jsondetail.put(node_child.getName(), node_child.getText());

					// ////////////////////////商品列表///////////////////////////////////////
					List<Map<String, Object>> goodslist = new ArrayList<Map<String, Object>>(); // 商品列表
					// 解析details里面的商品列表
					for (Iterator<Element> m = node_child.elementIterator(); m.hasNext();) {
						Element node_m = (Element) m.next();
						Map<String, Object> goodsMap = new HashMap<String, Object>();
						// 解析details里面的商品列表
						for (Iterator<Element> n = node_m.elementIterator(); n.hasNext();) {
							Element node_n = (Element) n.next();
							goodsMap.put(node_n.getName(), node_n.getText());
						}
						goodslist.add(goodsMap);
					}

					jsondetail.put("goods", goodslist);

					// ///////////////////////商品列表///////////////////////////////////////

				}
				jarry.add(jsondetail);
			}

			if (jarry != null && jarry.size() > 0) {
				returnMap.put("orderlist", jarry);
			}

		}
		return returnMap;
	}

}
