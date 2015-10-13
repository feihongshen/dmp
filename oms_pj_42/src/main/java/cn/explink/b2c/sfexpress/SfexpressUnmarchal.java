package cn.explink.b2c.sfexpress;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import cn.explink.b2c.sfexpress.xmldtoSearch.ResponseSearch;
import cn.explink.b2c.sfexpress.xmldtoSendOrder.ResponseSend;

/**
 * XML转化为bean对象的公共类
 * 
 * @author Administrator
 *
 */
public class SfexpressUnmarchal {

	// 推送接口xml转化为bean
	public static ResponseSend UnmarchalSend(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(ResponseSend.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (ResponseSend) m.unmarshal(iStream);
	}

	// 订单状态查询接口xml转化为bean
	public static ResponseSearch UnmarchalSearch(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(ResponseSearch.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (ResponseSearch) m.unmarshal(iStream);
	}
}
