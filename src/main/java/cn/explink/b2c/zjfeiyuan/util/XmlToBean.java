package cn.explink.b2c.zjfeiyuan.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import cn.explink.b2c.zjfeiyuan.requestdto.RequestData;
import cn.explink.b2c.zjfeiyuan.responsedto.ResponseData;

public class XmlToBean {
	public static Object toBean(String xmlstr) throws Exception{
		JAXBContext jc = JAXBContext.newInstance(ResponseData.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return m.unmarshal(iStream);
	}
	
	public static Object toBean2(String xmlstr) throws Exception{
		JAXBContext jc = JAXBContext.newInstance(RequestData.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return m.unmarshal(iStream);
	}
}
