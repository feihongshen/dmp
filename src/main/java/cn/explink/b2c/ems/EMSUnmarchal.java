package cn.explink.b2c.ems;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


/**
 * XML转化为bean对象的公共类
 */
public class EMSUnmarchal {

	public static EMSTranscwb Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(EMSTranscwb.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (EMSTranscwb) m.unmarshal(iStream);
	}
	
	public static EMSOrderResultBack UnmarchalOrder(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(EMSOrderResultBack.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (EMSOrderResultBack) m.unmarshal(iStream);
	}
}
