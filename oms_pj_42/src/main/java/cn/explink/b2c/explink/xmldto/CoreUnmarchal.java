package cn.explink.b2c.explink.xmldto;

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
public class CoreUnmarchal {

	public static OrderExportResultDto Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(OrderExportResultDto.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (OrderExportResultDto) m.unmarshal(iStream);
	}

	/**
	 * 上游部分======================================================
	 */

	/***
	 * 获取订单详情dto 请求
	 * 
	 * @param xmlstr
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static OrderExportConditionDto Unmarchal_req_getOrder(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(OrderExportConditionDto.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (OrderExportConditionDto) m.unmarshal(iStream);
	}

	/**
	 * 下游部分=================================================
	 */

	/***
	 * 获取订单详情dto 返回
	 * 
	 * @param xmlstr
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static OrderExportResultDto Unmarchal_rep_getOrder(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(OrderExportResultDto.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (OrderExportResultDto) m.unmarshal(iStream);
	}

	/***
	 * 下载数据成功后回传 通知已经拿到数据 -下游
	 * 
	 * @param xmlstr
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static ReturnDto Unmarchal_ExportCallBack(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(ReturnDto.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (ReturnDto) m.unmarshal(iStream);

	}

	/***
	 * 下载数据成功后回传 通知已经拿到数据
	 * 
	 * @param xmlstr
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static OrderExportCallbackDto Unmarchal_rep_ExportCallBack(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(OrderExportCallbackDto.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (OrderExportCallbackDto) m.unmarshal(iStream);

	}

	/***
	 * 上游oms-接收下游状态反馈
	 * 
	 * @param xmlstr
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static OrderFlowDto Unmarchal_rep_feedback(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(OrderFlowDto.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (OrderFlowDto) m.unmarshal(iStream);

	}

	/***
	 * 状态回传
	 * 
	 * @param xmlstr
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static ReturnDto Unmarchal_feedback(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(ReturnDto.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (ReturnDto) m.unmarshal(iStream);

	}
}
