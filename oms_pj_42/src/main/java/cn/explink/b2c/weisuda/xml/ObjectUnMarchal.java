package cn.explink.b2c.weisuda.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

public class ObjectUnMarchal {

	public static String POJOtoXml(Object obj) throws JAXBException, PropertyException {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）
		marshaller.marshal(obj, writer);
		return writer.toString();
	}

	public static Object XmltoPOJO(String xmlstr, Object object) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(object.getClass());
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		Object obj = m.unmarshal(iStream);
		return obj;
	}
}
