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
public class EpaiAPIUnmarchal {

	public static OrderExportResultDto Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(OrderExportResultDto.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (OrderExportResultDto) m.unmarshal(iStream);
	}

	/***
	 * 获取订单详情dto
	 * 
	 * @param xmlstr
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static OrderExportResultDto Unmarchal_getOrder(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(OrderExportResultDto.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (OrderExportResultDto) m.unmarshal(iStream);
	}

	/***
	 * 下载数据成功后回传 通知已经拿到数据
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
}
