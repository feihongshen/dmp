package cn.explink.b2c.chinamobile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * bean转化为XML对象的公共类
 * 
 * @author Administrator
 *
 */
public class ChinaUnmarchal {

	public static ChinaMobileResponse Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(ChinaMobileResponse.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (ChinaMobileResponse) m.unmarshal(iStream);
	}

}
