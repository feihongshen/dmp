package cn.explink.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class XmlToBean {
	public static Object toBeanRequest(String xmlstr,Object obj) throws Exception{
		JAXBContext jc = JAXBContext.newInstance(obj.getClass());
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return m.unmarshal(iStream);
	}
	
}
