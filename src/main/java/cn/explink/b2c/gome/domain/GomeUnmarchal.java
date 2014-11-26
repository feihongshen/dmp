package cn.explink.b2c.gome.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * XML转化为bean对象的公共类
 * 
 * @author Administrator
 *
 */
public class GomeUnmarchal {

	public static OrderVO Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(OrderVO.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (OrderVO) m.unmarshal(iStream);
	}
}
