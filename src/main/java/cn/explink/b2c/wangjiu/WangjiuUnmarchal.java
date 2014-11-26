package cn.explink.b2c.wangjiu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import cn.explink.b2c.wangjiu.xmldto.WangjiuOrder;
import cn.explink.pos.alipay.searchpos.TenPayBean;

/**
 * XML转化为bean对象的公共类
 * 
 * @author Administrator
 *
 */
public class WangjiuUnmarchal {

	public static WangjiuOrder Unmarchal(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(WangjiuOrder.class);
		Unmarshaller m = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (WangjiuOrder) m.unmarshal(iStream);
	}

}
