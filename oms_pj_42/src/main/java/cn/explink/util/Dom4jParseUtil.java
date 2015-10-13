package cn.explink.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Dom4jParseUtil {
	@SuppressWarnings("unused")
	public static JSONObject parserXmlToJSONObject(String fileName) {
		JSONObject jsonObject = new JSONObject();
		// File inputXml = new File(fileName);
		try {
			InputStream iStream = new ByteArrayInputStream(fileName.getBytes("UTF-8"));
			SAXReader saxReader = new SAXReader();

			Reader r = new InputStreamReader(iStream, "UTF-8");
			Document document = saxReader.read(r);
			Element employees = document.getRootElement();

			for (Iterator i = employees.elementIterator(); i.hasNext();) {
				Element employee = (Element) i.next();
				jsonObject.put(employee.getName(), employee.getText());

				for (Iterator j = employee.elementIterator(); j.hasNext();) {
					Element node = (Element) j.next();
					jsonObject.put(node.getName(), node.getText());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject = JSONObject.fromObject("{}");
		}

		return jsonObject;
	}

	/**
	 * 可解析三层的xml
	 * 
	 * @param fileName
	 * @return
	 */
	public static JSONObject parserXmlToJSONObjectBy3Layer(String fileName) {
		// File inputXml = new File(fileName);
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes());
		SAXReader saxReader = new SAXReader();
		JSONObject jsonObject = new JSONObject();
		try {
			Reader r = new InputStreamReader(iStream, "UTF-8");
			Document document = saxReader.read(r);

			Element employees = document.getRootElement();

			for (Iterator i = employees.elementIterator(); i.hasNext();) {
				Element employee = (Element) i.next();
				jsonObject.put(employee.getName(), employee.getText());

				for (Iterator j = employee.elementIterator(); j.hasNext();) {
					Element node = (Element) j.next();
					jsonObject.put(node.getName(), node.getText());
					for (Iterator k = node.elementIterator(); k.hasNext();) {
						Element node_child = (Element) k.next();
						jsonObject.put(node_child.getName(), node_child.getText());
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject = JSONObject.fromObject("{}");

		}

		return jsonObject;
	}

	/**
	 * 读取文件流 把文件流转化为 xml格式字符串
	 * 
	 * @param inputstream
	 * @return XML String
	 */
	public static String getStringByInputStream(InputStream inputstream) {
		try {
			String line = "";
			BufferedReader breader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
			StringBuilder sbuilder = new StringBuilder();
			while ((line = breader.readLine()) != null) {
				sbuilder.append(line);
			}
			return sbuilder.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
