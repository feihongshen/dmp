package cn.explink.pos.tonglianpos.delivery;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.ObjectUnMarchal;
import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.pos.tonglianpos.Tlmpos;
import cn.explink.pos.tonglianpos.delivery.respDto.ErrorInfo;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.util.MD5.MD5Util;

@Service
public class DeliveryService {
	private static Logger logger = LoggerFactory.getLogger(DeliveryService.class);
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	UserDAO userDAO;

	public String delivery(HttpServletRequest request, String XMLDOC) {

		try {

			Delivery delivery = DeliveryService.getDelivery(XMLDOC);
			Tlmpos tongl = this.getTongLian(PosEnum.TongLianPos.getKey());

			String localSign = MD5Util.md5(delivery.getRequestTime() + tongl.getPrivate_key());

			if (!localSign.equalsIgnoreCase(delivery.getSign())) {
				logger.info("签名验证失败！cwb={}", delivery.getOrder_no());
				return this.responseXML(DeliveryEnum.SignError.getResult_code(), DeliveryEnum.SignError.getResult_msg());
			}

			User user = this.userDAO.getUserByUsername(delivery.getDelivery_man());
			
			List<ErrorInfo> errorList = new ArrayList<ErrorInfo>();
			for(String orderNo:delivery.getOrder_no().split(",")){
				orderNo = cwbOrderService.translateCwb(orderNo);
				try {
					this.cwbOrderService.receiveGoods(user, user, orderNo, orderNo);
				} catch (Exception e) {
					ErrorInfo errorInfo = new ErrorInfo();
					errorInfo.setCwb(orderNo);
					errorInfo.setMsg(e.getMessage());
					errorList.add(errorInfo);
					logger.error("领货扫描异常",e);
					
				}
			}
			if(errorList.size()>0){
				Delivery_response response=new Delivery_response();
				response.setResult_code(DeliveryEnum.Success.getResult_code());
				response.setResult_msg(DeliveryEnum.Success.getResult_msg());
				response.setErrorList(errorList);
				return  ObjectUnMarchal.POJOtoXml(response);
			}

			return this.responseXML(DeliveryEnum.Success.getResult_code(), DeliveryEnum.Success.getResult_msg());

		} catch (Exception e) {
			logger.error("POS机领货未知异常", e);
			return this.responseXML(DeliveryEnum.SystemError.getResult_code(), e.getMessage());
		}

	}

	private String responseXML(String code, String msg) {
		String strer = "<response>" + "<result_code>" + code + "</result_code>" + "<result_msg>" + msg + "</result_msg>" + "</response>";
		return strer;
	}

	public static Delivery getDelivery(String xmlstr) throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(Delivery.class);
		Unmarshaller u = jc.createUnmarshaller();
		InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		return (Delivery) u.unmarshal(iStream);
	}

	public static void main(String[] args) throws UnsupportedEncodingException, JAXBException {
		String xmldata = "<request><sign>24F4375FF2F985C0EA45AF959D03A0A5<requestTime>2015-01-06 15:57:52</requestTime><delivery_man>111111    </delivery_man><delivery_dept_no>45110</delivery_dept_no><order_no>811898</order_no></request>";
		Delivery sss = DeliveryService.getDelivery(xmldata);
		logger.info(sss.getOrder_no());
	}

	// public String getXML(Delivery delivery) throws JAXBException,
	// UnsupportedEncodingException{
	// JAXBContext jc = JAXBContext.newInstance(Delivery.class);
	// Marshaller m = jc.createMarshaller();
	// m.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");//设置编码格式
	// m.setProperty(Marshaller.JAXB_FRAGMENT, false);//不省略头信息
	// Delivery d = new Delivery();
	// m.marshal(d, System.out);
	// return "";
	// }

	public Tlmpos getTongLian(int key) {
		Tlmpos tlmpos = new Tlmpos();
		if (!"".equals(this.getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
			tlmpos = (Tlmpos) JSONObject.toBean(jsonObj, Tlmpos.class);
		} else {
			tlmpos = new Tlmpos();
		}

		return tlmpos == null ? new Tlmpos() : tlmpos;
	}

	private String getObjectMethod(int key) {
		JointEntity obj = null;
		String posValue = "";
		try {
			obj = this.jiontDAO.getJointEntity(key);
			posValue = obj.getJoint_property();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return posValue;
	}

	// public String responseXML(String code,String msg){
	// Delivery_response resp = new Delivery_response();
	// resp.setResult_code(code);
	// resp.setResult_msg(msg);
	// String str = null;
	//
	//
	//
	// return "";
	// }
	//

}
