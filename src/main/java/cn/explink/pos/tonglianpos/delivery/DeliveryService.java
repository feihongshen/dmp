package cn.explink.pos.tonglianpos.delivery;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.pos.tonglianpos.Tlmpos;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.util.MD5.MD5Util;

@Service
public class DeliveryService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	UserDAO userDAO;
	
	
	public String delivery(HttpServletRequest request){
		
		String xmldata = request.getParameter("xmldata");
		String sign = request.getParameter("sign");
		String requestTime = request.getParameter("requestTime");
		logger.info("pos领货请求参数：xmldata={},sign={},requestTime={}",new Object[]{xmldata,sign,requestTime});
		try {
			
			Delivery delivery = DeliveryService.getDelivery(xmldata);
			Tlmpos tongl=getTongLian(PosEnum.TongLianPos.getKey());
			
			String localSign = MD5Util.md5(requestTime+tongl.getPrivate_key());
			
//				if(!localSign.equalsIgnoreCase(sign)){
//				logger.info("签名验证失败！");
//				return 	responseXML(DeliveryEnum.SignError.getResult_code(),DeliveryEnum.SignError.getResult_msg());
//			}
			
			User user=userDAO.getUserByUsername(delivery.getDelivery_man());
			cwbOrderService.receiveGoods(user, user, delivery.getOrder_no(), delivery.getOrder_no());
			
			return  responseXML(DeliveryEnum.Success.getResult_code(),DeliveryEnum.Success.getResult_msg());
			
		} catch (Exception e) {
			logger.error("POS机领货未知异常",e);
			return  responseXML(DeliveryEnum.SystemError.getResult_code(),e.getMessage());
		}
			
	}

	private String responseXML(String code,String msg) {
		String strer="<response>"
				     +"<result_code>"+code+"</result_code>"
				     +"<result_msg>"+msg+"</result_msg>"
				     +"</response>";
		return strer;
	}
	
	 public static Delivery getDelivery(String xmlstr) throws JAXBException, UnsupportedEncodingException{
    	JAXBContext jc = JAXBContext.newInstance(Delivery.class);
        Unmarshaller u = jc.createUnmarshaller();
        InputStream iStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
        return (Delivery)u.unmarshal(iStream);
     }
	 
//	 public String getXML(Delivery delivery) throws JAXBException, UnsupportedEncodingException{
//	 	JAXBContext jc = JAXBContext.newInstance(Delivery.class);
//	 	Marshaller m = jc.createMarshaller();
//	 	m.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");//设置编码格式
//	 	m.setProperty(Marshaller.JAXB_FRAGMENT, false);//不省略头信息
//	 	Delivery d = new Delivery();
//	 	m.marshal(d, System.out);
//	 	return "";
// 	 }
	 
	 public Tlmpos getTongLian(int key) {
			Tlmpos tlmpos = new Tlmpos();
			if (!"".equals(getObjectMethod(key))) {
				JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
				tlmpos = (Tlmpos) JSONObject.toBean(jsonObj, Tlmpos.class);
			} else {
				tlmpos = new Tlmpos();
			}

			return tlmpos==null?new Tlmpos():tlmpos;
		}

	 private String getObjectMethod(int key) {
			JointEntity obj = null;
			String posValue = "";
			try {
				obj = jiontDAO.getJointEntity(key);
				posValue = obj.getJoint_property();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return posValue;
		}
	 
//	 public String responseXML(String code,String msg){
//		 Delivery_response resp = new Delivery_response();
//		 resp.setResult_code(code);
//		 resp.setResult_msg(msg);
//		 String str = null;
//		 
//		 
//		 
//		 return "";
//	 }
//	 
	 
	 
	 
	 
}
