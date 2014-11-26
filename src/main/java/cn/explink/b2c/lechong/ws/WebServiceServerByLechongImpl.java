package cn.explink.b2c.lechong.ws;

import javax.jws.WebService;

import cn.explink.b2c.lechong.LechongService;
import cn.explink.util.ApplicationContextUtil;

@WebService(endpointInterface = "cn.explink.b2c.lechong.ws.WebServiceServerByLechong")
public class WebServiceServerByLechongImpl implements WebServiceServerByLechong {

	public String BuildOrder(String xmlstr) {

		LechongService lechongService = (LechongService) ApplicationContextUtil.getBean("lechongService");

		String result = lechongService.excutorGetOrders(xmlstr);
		return result;
	}

	@Override
	public String CancelOrder(String xmlstr) {
		LechongService lechongService = (LechongService) ApplicationContextUtil.getBean("lechongService");
		String result = lechongService.CancelOrders(xmlstr);
		return result;
	}

}
