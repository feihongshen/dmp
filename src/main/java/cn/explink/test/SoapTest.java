package cn.explink.test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
public class SoapTest {
	/**
	 * @param args
	 * @throws IOException
	 * @throws HttpException
	 */
	public static void main(String[] args) throws HttpException, IOException {
		// TODO Auto-generated method stub

		String soapRequestData = 
				  "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap12:Envelope xmlns:xsi="
				+ "\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
				+ "xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
				+ "<soap12:Body>"
				+ "<CheckUser xmlns=\"http://tempuri.org/\">"
				+ "<strUserID>HDMENLO</strUserID> "
				+ "<strPwd>admin</strPwd> "
				+ "</CheckUser> "
				+ "</soap12:Body>"
				+ "</soap12:Envelope>";

		System.out.println(soapRequestData);

		PostMethod postMethod = new PostMethod(
				"http://www.mk-lips.com/HDToLIPS.asmx?op=CheckUser");

		// 然后把Soap请求数据添加到PostMethod中
		byte[] b = soapRequestData.getBytes("utf-8");
		InputStream is = new ByteArrayInputStream(b, 0, b.length);
		RequestEntity re = new InputStreamRequestEntity(is, b.length,
				"application/soap+xml; charset=utf-8");
		postMethod.setRequestEntity(re);

		// 最后生成一个HttpClient对象，并发出postMethod请求
		HttpClient httpClient = new HttpClient();
		/*HttpState state =new HttpState();
		state.addCookie(cookie);
		httpClient.setState(state);*/
		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode == 200) {
			System.out.println("调用成功！");
			String soapResponseData = postMethod.getResponseBodyAsString();
			//String responseBodyString = postMethod.getResponseBodyAsString();
			Cookie[] cookies = httpClient.getState().getCookies();
			for (int i = 0; i < cookies.length; i++) {
				System.out.println("cookiename==" + cookies[i].getName());
				System.out.println("cookieValue==" + cookies[i].getValue());
				System.out.println("Domain==" + cookies[i].getDomain());
				System.out.println("Path==" + cookies[i].getPath());
				System.out.println("Version==" + cookies[i].getVersion());
			}
			System.out.println(soapResponseData);
		} else {
			System.out.println("调用失败！错误码：" + statusCode);
		}

	}
		
}
