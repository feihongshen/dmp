package cn.explink.b2c.gxdx.xmldto.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import cn.explink.b2c.gxdx.xmldto.RequestDto;

public class XmlToBean {
	public static Object toBeanRequest(String xmlstr) throws Exception{
		JAXBContext jc = JAXBContext.newInstance(RequestDto.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return m.unmarshal(iStream);
	}
	
}
