package cn.explink.b2c.yihaodian.addressmatch.dto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cn.explink.b2c.explink.xmldto.OrderExportConditionDto;

/**
 * XML转化为bean对象的公共类
 * 
 * @author Administrator
 *
 */
public class YhdAddmatchUnmarchal {

	/**
	 * XML转化bean
	 * 
	 * @param xmlstr
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static DepotParse Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(DepotParse.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (DepotParse) m.unmarshal(iStream);
	}

	public static Object UnmarchalObj(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(Object.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return m.unmarshal(iStream);
	}

	public static Object MarchalObj(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(Object.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return m.unmarshal(iStream);
	}

	public static String Marchal(DepotParseResult depotParseResult) throws Exception {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(DepotParseResult.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）
		marshaller.marshal(depotParseResult, writer);
		return writer.toString();
	}

}
