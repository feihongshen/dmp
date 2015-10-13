package cn.explink.b2c.letv;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import cn.explink.b2c.letv.bean.LetvContent;

/**
 * bean转化为XML对象的公共类
 * 
 * @author Administrator
 *
 */
public class LetvMarchal {

	/**
	 * bean 序列化XML
	 * 
	 * @param letvContent
	 * @return
	 * @throws Exception
	 */
	public static String Marchal(LetvContent letvContent) throws Exception {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(LetvContent.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）
		marshaller.marshal(letvContent, writer);
		return writer.toString();
	}

	public static void main(String[] args) throws Exception {
		LetvContent letvcontent = new LetvContent();
		letvcontent.setBiz_no("111");
		letvcontent.setContent("22222");
		letvcontent.setWaybill_no("33333");

		String content = Marchal(letvcontent); // 请求xml报文体
		System.out.println(content);

	}

}
