package cn.explink.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBUtil {
	/**
	 * object转换成xml
	 *
	 * @param element
	 * @return
	 */
	public static <T> String convertXml(T element) {
		ByteArrayOutputStream stream = null;
		String xml = "";
		try {
			stream = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(element.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(element, stream);
			xml = new String(stream.toByteArray(), "UTF-8");
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (null != stream) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return xml;
	}

	/**
	 * xml转换成object
	 *
	 * @param c
	 * @param xml
	 * @return
	 */
	public static <T> T convertObject(Class<T> c, String xml) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * xml转换成object
	 *
	 * @param c
	 * @param xml
	 * @return
	 */
	public static <T> T convertObject(Class<T> c, InputStream inputStream) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(inputStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return t;

	}
}
