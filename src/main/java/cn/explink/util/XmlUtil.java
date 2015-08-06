package cn.explink.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlUtil {
	
	/**
     * 
     * @param clazz xml绑定的对象的类 
     * @param xmlString 字符编码为UTF-8的xml字符串
     * @return 返回xml绑定的对象
     * @throws Exception
     */
    public static <T> T toObject(Class<T> clazz,String xmlString) throws Exception{
        ByteArrayInputStream stream = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
        Unmarshaller unmarshaller = null;
        JAXBContext context = null;
        Object returnObject = null;
        try {
            context = JAXBContext.newInstance(clazz);
            unmarshaller = context.createUnmarshaller();
            returnObject = unmarshaller.unmarshal(stream);
        } catch (Exception e) {
            throw e;
        }finally{
            if(stream!=null){
                stream.close();
            }
        }
        return (T) returnObject;
    }
    
	public static <T> String toXml(Class<T> clazz, T obj) throws Exception {
		String ret = "";
		JAXBContext context = null;
		Marshaller marshaller;
		try {
			//context = JAXBContext.newInstance(clazz);
			Map<String, Object> props = new HashMap<String, Object>(1);
			props.put(" javax.xml.bind.context.factory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
			context=org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(new Class[] {clazz}, props,Thread.currentThread().getContextClassLoader() );
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(obj, stringWriter);
			ret = stringWriter.getBuffer().toString();
		} catch (Exception e) {
			throw e;
		}
		return ret;
	}

}
