package cn.explink.b2c.smiled;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cn.explink.b2c.smiled.xmldto.SmiledOrder;
import cn.explink.b2c.smiled.xmldto.SmiledOrderStatus;
import cn.explink.b2c.smiled.xmldto.SmiledResponse;

/**
 * XML转化为bean对象的公共类
 * 
 * @author Administrator
 *
 */
public class SmiledUnmarchal {

	public static String marchal(SmiledOrder condto) throws Exception {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(SmiledOrder.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）

		marshaller.marshal(condto, writer);
		return writer.toString();
	}

	public static SmiledResponse Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(SmiledResponse.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (SmiledResponse) m.unmarshal(iStream);
	}

	public static SmiledOrderStatus UnmarchalStatus(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(SmiledOrderStatus.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (SmiledOrderStatus) m.unmarshal(iStream);
	}

}
