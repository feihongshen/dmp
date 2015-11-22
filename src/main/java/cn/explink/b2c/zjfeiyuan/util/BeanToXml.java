package cn.explink.b2c.zjfeiyuan.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.stereotype.Service;

import cn.explink.b2c.zjfeiyuan.requestdto.RequestData;
import cn.explink.b2c.zjfeiyuan.responsedto.ResponseData;

public class BeanToXml {
	public static String toXml(RequestData rd) throws Exception{
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(RequestData.class);
		Marshaller marshaller = jc.createMarshaller();   
		marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");//编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//是否格式化生成的xml串   
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);//是否省略xml头信息（<?xml version="1.0" encoding="gb2312" standalone="yes"?>）   
		marshaller.marshal(rd,writer);   
		return writer.toString();
	}
	
	public static String toXml2(ResponseData rd) throws Exception{
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(ResponseData.class);
		Marshaller marshaller = jc.createMarshaller();   
		marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");//编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//是否格式化生成的xml串   
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);//是否省略xml头信息（<?xml version="1.0" encoding="gb2312" standalone="yes"?>）   
		marshaller.marshal(rd,writer);   
		return writer.toString();
	}
}
