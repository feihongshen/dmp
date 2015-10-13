package cn.explink.b2c.wanxiang.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.tools.ant.filters.StringInputStream;

/**
 * XML转化为bean对象的公共类
 * 
 * @author Administrator
 *
 */
public class WxUnmarchal {

	public static WxResponse Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(WxResponse.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (WxResponse) m.unmarshal(iStream);
	}
}
