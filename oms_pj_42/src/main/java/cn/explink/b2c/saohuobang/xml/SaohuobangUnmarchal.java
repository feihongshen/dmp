package cn.explink.b2c.saohuobang.xml;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.activemq.util.ByteArrayInputStream;

/**
 * XML转化为bean对象的公共类
 * 
 * @author Administrator
 *
 */
public class SaohuobangUnmarchal {

	public static BatchQueryRequest Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(BatchQueryRequest.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (BatchQueryRequest) m.unmarshal(iStream);
	}

}
