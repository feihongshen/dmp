package cn.explink.b2c.explink.xmldto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * bean转化为XML对象的公共类
 * 
 * @author Administrator
 *
 */
public class CoreMarchal {

	public static String Marchal(OrderExportConditionDto condto) throws Exception {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(OrderExportConditionDto.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）
		marshaller.marshal(condto, writer);
		return writer.toString();
	}

	/**
	 * 获取订单-请求-下游
	 * 
	 * @param condto
	 * @return
	 * @throws Exception
	 */
	public static String Marchal_getOrder(OrderExportConditionDto condto) throws Exception {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(OrderExportConditionDto.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）

		marshaller.marshal(condto, writer);
		return writer.toString();
	}

	/**
	 * 获取订单-返回-上游
	 * 
	 * @param condto
	 * @return
	 * @throws JAXBException
	 * @throws Exception
	 */
	public static String Marchal_setOrders(OrderExportResultDto condto) throws JAXBException {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(OrderExportResultDto.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）

		marshaller.marshal(condto, writer);
		return writer.toString();
	}

	/**
	 * 下载数据成功后回传 通知已经拿到数据
	 * 
	 * @param condto
	 * @return ReturnDto returnDto
	 * @throws Exception
	 */
	public static String Marchal_ExportCallBack(OrderExportCallbackDto condto) throws Exception {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(OrderExportCallbackDto.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）
		marshaller.marshal(condto, writer);
		return writer.toString();
	}

	/**
	 * 状态流转反馈
	 * 
	 * @param resultDto
	 * @return
	 * @throws Exception
	 */
	public static String Marchal_OrderFlow(OrderFlowDto resultDto) throws Exception {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(OrderFlowDto.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）
		marshaller.marshal(resultDto, writer);
		return writer.toString();
	}

	/***
	 * 下载数据成功后回传 通知已经拿到数据 -上游返回
	 * 
	 * @param xmlstr
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static String Marchal_rep_ExportCallBack(ReturnDto returnDto) throws JAXBException, UnsupportedEncodingException {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(ReturnDto.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）
		marshaller.marshal(returnDto, writer);
		return writer.toString();

	}

	/***
	 * 下载数据成功后回传 通知已经拿到数据 -上游返回
	 * 
	 * @param xmlstr
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static String Marchal_rep_feedback(ReturnDto returnDto) throws JAXBException, UnsupportedEncodingException {
		StringWriter writer = new StringWriter();
		JAXBContext jc = JAXBContext.newInstance(ReturnDto.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息（<?xml
																// version="1.0"
																// encoding="gb2312"
																// standalone="yes"?>）
		marshaller.marshal(returnDto, writer);
		return writer.toString();

	}
}
