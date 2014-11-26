package cn.explink.b2c.aa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.util.WebServiceHandler;

/**
 * 测试国美 获取订单数据的接口
 * 
 * @author lansheng
 *
 */
@Controller
@RequestMapping("/gome2")
public class GetOrders {

	private static void getSoAndAsnCwbs(String url, String usename, String password) {

		// 获取正向订单
		try {
			String method = "getTransactionIdByAction";
			String pram = "SO";
			Object o = WebServiceHandler.invokeWsByNameAndPassWord(url, method, pram, usename, password);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("/test")
	public void test() {
		getSoAndAsnCwbs("http://219.238.68.169/Web/webservices/FulfillmentWebService", "YMJ", "Ymj1234!");

	}

	public static void main(String[] args) {
		getSoAndAsnCwbs("http://219.238.68.169/Web/webservices/FulfillmentWebService", "YMJ", "Ymj1234!");
	}

}
