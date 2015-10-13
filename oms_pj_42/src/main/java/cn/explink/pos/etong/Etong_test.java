package cn.explink.pos.etong;

import com.hyyt.elog.dmp.ObjectFactory;

import com.hyyt.elog.dmp.OrderAttemperRequestType;
import com.hyyt.elog.dmp.OrderAttemperResponseType;
import com.hyyt.elog.dmp.OrderAttemperType;

public class Etong_test {
	private static ObjectFactory etFactory = EtongFactory.getInstance(); // 初始化推送物流E通手机端接口

	public static void main(String[] args) {

		OrderAttemperRequestType requestType = etFactory.createOrderAttemperRequestType();

		EtongServiceClient client = new EtongServiceClient("http://113.108.186.155:60606/LecManager/ws/dmp?wsdl", 30000);
		OrderAttemperType order = buildOrderAttemper(); // 构建发送e通 的数据
		requestType.getOrderAttempers().add(order);

		OrderAttemperResponseType responseType = client.orderAttemper(requestType);

		System.out.println(responseType.getErrorCount());
		System.out.println(responseType.getResultBody());

	}

	private static OrderAttemperType buildOrderAttemper() {
		OrderAttemperType order = etFactory.createOrderAttemperType();
		order.setCwb("123456");
		order.setConsigneeAddress("中国人民");
		order.setConsigneeName("张三");
		order.setReceivableFee("25");
		order.setConsigneeMobile("18915452212");
		order.setSendCargoName("手机");
		order.setCwbRemark("");
		order.setCustomerCommand("");
		order.setDeliver("13426480782"); // 派送员手机号码
		order.setDomain(""); // 电信分配的企业标识
		return order;
	}
}
