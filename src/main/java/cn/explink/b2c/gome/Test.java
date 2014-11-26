package cn.explink.b2c.gome;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class Test {
	public static void main(String[] args) throws ServiceException {
		String url = "http://219.238.68.169/Web/webservices/TntWebService";
		String method = "getTransactionIdByAction";
		String queryStr = "SO";

		Object[] params = new Object[] { queryStr };
		Service service = new Service();

		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(url);
		call.setTimeout(30000);
		call.setClientHandlers(new SOAPHandle("HMJLN", "Hmjln12!"), null);

		Object result = null;
		try {
			result = (Object) call.invoke(method, params);
			System.out.println(result == null ? "" : result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			// System.out.println(result.getClass());
			// System.out.println("====  国美信息编号:  "+result);
			System.out.println("=====================国美webservice调用返回:  " + result);
		}
		System.out.println(result == null ? "" : result);

	}
}
