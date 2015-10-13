package cn.explink.b2c.letv;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import cn.explink.b2c.letv.bean.LetvResponse;

/**
 * bean转化为XML对象的公共类
 * 
 * @author Administrator
 *
 */
public class LetvUnmarchal {

	public static LetvResponse Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(LetvResponse.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (LetvResponse) m.unmarshal(iStream);
	}

}
