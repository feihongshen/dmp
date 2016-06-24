import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import cn.explink.b2c.benlaishenghuo.BenLaiShengHuoService;
import cn.explink.b2c.ems.EMS;
import cn.explink.b2c.ems.EMSDAO;
import cn.explink.b2c.ems.EMSOrderResultBack;
import cn.explink.b2c.ems.EMSService;
import cn.explink.b2c.ems.EMSTimmer;
import cn.explink.b2c.ems.EMSTranscwb;
import cn.explink.b2c.ems.EMSUnmarchal;
import cn.explink.b2c.ems.EmsAndDmpTranscwb;
import cn.explink.b2c.ems.ErrorDetail;
import cn.explink.b2c.ems.FlowFromJMSToEMSOrderService;
import cn.explink.b2c.ems.SendToEMSOrder;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import daoTest.BaseTest;

public class EncodeAndDecodeTest{	
	@Test
	public void testEncodeBase64(){
		String sendstr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ "<XMLInfo>"
				+ "<sysAccount>A1234567890Z</sysAccount>"
				+ "<passWord>e10adc3949ba59abbe56e057f20f883e</passWord>"
				+ "<appKey>SA7125144B3a6BB00</appKey>"
				+ "<printKind>2</printKind>"
				+ "<printDatas>"
				+ "<printData>"
				+ "<bigAccountDataId>2015120917274020</bigAccountDataId>"
				+ "<businessType>4</businessType>"
				+ "<billno>KDBG100618221</billno>"
				+ "<scontactor>aaa</scontactor>"
				+ "<scustMobile>13333333333</scustMobile>"
				+ "<scustComp>bbb</scustComp>"
				+ "<tcontactor>ccc</tcontactor>"
				+ "<tcustMobile>15856598606</tcustMobile>"
				+ "<tcustAddr>eeeeeeeeeee</tcustAddr>"
				+ "<tcustProvince>安徽省</tcustProvince>"
				+ "<tcustCity>安庆市</tcustCity>"
				+ "<tcustCounty>宿松县</tcustCounty>"
				+ "</printData>"
				+ "</printDatas>"
				+ "</XMLInfo>";
		System.out.println(sendstr);
		String expectedEncodeStr= "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiID8+CjxYTUxJbmZvPgo8c3lzQWNjb3VudD5BMTIzNDU2Nzg5MFo8L3N5c0FjY291bnQ+CjxwYXNzV29yZD5lMTBhZGMzOTQ5YmE1OWFiYmU1NmUwNTdmMjBmODgzZTwvcGFzc1dvcmQ+CjxhcHBLZXk+U0E3MTI1MTQ0QjNhNkJCMDA8L2FwcEtleT4KPHByaW50S2luZD4yPC9wcmludEtpbmQ+CjxwcmludERhdGFzPgo8cHJpbnREYXRhPgo8YmlnQWNjb3VudERhdGFJZD4yMDE1MTIwOTE3Mjc0MDIwPC9iaWdBY2NvdW50RGF0YUlkPgo8YnVzaW5lc3NUeXBlPjQ8L2J1c2luZXNzVHlwZT4KPGJpbGxubz5LREJHMTAwNjE4MjIxPC9iaWxsbm8+CjxzY29udGFjdG9yPmFhYTwvc2NvbnRhY3Rvcj4KPHNjdXN0TW9iaWxlPjEzMzMzMzMzMzMzPC9zY3VzdE1vYmlsZT4KPHNjdXN0Q29tcD5iYmI8L3NjdXN0Q29tcD4KPHRjb250YWN0b3I+Y2NjPC90Y29udGFjdG9yPgo8dGN1c3RNb2JpbGU+MTU4NTY1OTg2MDY8L3RjdXN0TW9iaWxlPgo8dGN1c3RBZGRyPmVlZWVlZWVlZWVlPC90Y3VzdEFkZHI+Cjx0Y3VzdFByb3ZpbmNlPuWuieW+veecgTwvdGN1c3RQcm92aW5jZT4KPHRjdXN0Q2l0eT7lronluobluII8L3RjdXN0Q2l0eT4KPHRjdXN0Q291bnR5PuWuv+advuWOvzwvdGN1c3RDb3VudHk+CjwvcHJpbnREYXRhPgo8L3ByaW50RGF0YXM+CjwvWE1MSW5mbz4K";
		String actualEncodeStr = new BASE64Encoder().encode(sendstr.getBytes());
		System.out.println(expectedEncodeStr);
		System.out.println(actualEncodeStr);
		Assert.assertEquals(expectedEncodeStr, actualEncodeStr);
	}
	
	@Test
	public void testEnAndDecodeBase64() throws IOException{
		String sendstr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ "<XMLInfo>"
				+ "<sysAccount>A1234567890Z</sysAccount>"
				+ "<passWord>e10adc3949ba59abbe56e057f20f883e</passWord>"
				+ "<appKey>SA7125144B3a6BB00</appKey>"
				+ "<printKind>2</printKind>"
				+ "<printDatas>"
				+ "<printData>"
				+ "<bigAccountDataId>2015120917274020</bigAccountDataId>"
				+ "<businessType>4</businessType>"
				+ "<billno>KDBG100618221</billno>"
				+ "<scontactor>aaa</scontactor>"
				+ "<scustMobile>13333333333</scustMobile>"
				+ "<scustComp>bbb</scustComp>"
				+ "<tcontactor>ccc</tcontactor>"
				+ "<tcustMobile>15856598606</tcustMobile>"
				+ "<tcustAddr>eeeeeeeeeee</tcustAddr>"
				+ "<tcustProvince>安徽省</tcustProvince>"
				+ "<tcustCity>安庆市</tcustCity>"
				+ "<tcustCounty>宿松县</tcustCounty>"
				+ "</printData>"
				+ "</printDatas>"
				+ "</XMLInfo>";
		
		String actualEncodeStr = new String(new BASE64Decoder().decodeBuffer(new BASE64Encoder().encode(sendstr.getBytes())));
		Assert.assertEquals(sendstr, actualEncodeStr);
	}
	
	@Test
	public void testAndDecodeBase64() throws IOException{
		String expectedDecodeStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ "<XMLInfo>"
				+ "<sysAccount>A1234567890Z</sysAccount>"
				+ "<passWord>e10adc3949ba59abbe56e057f20f883e</passWord>"
				+ "<appKey>SA7125144B3a6BB00</appKey>"
				+ "<printKind>2</printKind>"
				+ "<printDatas>"
				+ "<printData>"
				+ "<bigAccountDataId>2015120917274020</bigAccountDataId>"
				+ "<businessType>4</businessType>"
				+ "<billno>KDBG100618221</billno>"
				+ "<scontactor>aaa</scontactor>"
				+ "<scustMobile>13333333333</scustMobile>"
				+ "<scustComp>bbb</scustComp>"
				+ "<tcontactor>ccc</tcontactor>"
				+ "<tcustMobile>15856598606</tcustMobile>"
				+ "<tcustAddr>eeeeeeeeeee</tcustAddr>"
				+ "<tcustProvince>安徽省</tcustProvince>"
				+ "<tcustCity>安庆市</tcustCity>"
				+ "<tcustCounty>宿松县</tcustCounty>"
				+ "</printData>"
				+ "</printDatas>"
				+ "</XMLInfo>";
		
		String encodeStr = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiID8+CjxYTUxJbmZvPgo8c3lzQWNjb3VudD5BMTIzNDU2Nzg5MFo8L3N5c0FjY291bnQ+CjxwYXNzV29yZD5lMTBhZGMzOTQ5YmE1OWFiYmU1NmUwNTdmMjBmODgzZTwvcGFzc1dvcmQ+CjxhcHBLZXk+U0E3MTI1MTQ0QjNhNkJCMDA8L2FwcEtleT4KPHByaW50S2luZD4yPC9wcmludEtpbmQ+CjxwcmludERhdGFzPgo8cHJpbnREYXRhPgo8YmlnQWNjb3VudERhdGFJZD4yMDE1MTIwOTE3Mjc0MDIwPC9iaWdBY2NvdW50RGF0YUlkPgo8YnVzaW5lc3NUeXBlPjQ8L2J1c2luZXNzVHlwZT4KPGJpbGxubz5LREJHMTAwNjE4MjIxPC9iaWxsbm8+CjxzY29udGFjdG9yPmFhYTwvc2NvbnRhY3Rvcj4KPHNjdXN0TW9iaWxlPjEzMzMzMzMzMzMzPC9zY3VzdE1vYmlsZT4KPHNjdXN0Q29tcD5iYmI8L3NjdXN0Q29tcD4KPHRjb250YWN0b3I+Y2NjPC90Y29udGFjdG9yPgo8dGN1c3RNb2JpbGU+MTU4NTY1OTg2MDY8L3RjdXN0TW9iaWxlPgo8dGN1c3RBZGRyPmVlZWVlZWVlZWVlPC90Y3VzdEFkZHI+Cjx0Y3VzdFByb3ZpbmNlPuWuieW+veecgTwvdGN1c3RQcm92aW5jZT4KPHRjdXN0Q2l0eT7lronluobluII8L3RjdXN0Q2l0eT4KPHRjdXN0Q291bnR5PuWuv+advuWOvzwvdGN1c3RDb3VudHk+CjwvcHJpbnREYXRhPgo8L3ByaW50RGF0YXM+CjwvWE1MSW5mbz4K";
		
		String actualDecodeStr = new String(new BASE64Decoder().decodeBuffer(encodeStr));
		System.out.println(expectedDecodeStr);
		System.out.println(actualDecodeStr);
		Assert.assertEquals(expectedDecodeStr, actualDecodeStr);
		
	}
	
	@Test
	public void testURLCode(){
		String sendstr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ "<XMLInfo>"
				+ "<sysAccount>A1234567890Z</sysAccount>"
				+ "<passWord>e10adc3949ba59abbe56e057f20f883e</passWord>"
				+ "<appKey>SA7125144B3a6BB00</appKey>"
				+ "<printKind>2</printKind>"
				+ "<printDatas>"
				+ "<printData>"
				+ "<bigAccountDataId>2015120917274020</bigAccountDataId>"
				+ "<businessType>4</businessType>"
				+ "<billno>KDBG100618221</billno>"
				+ "<scontactor>aaa</scontactor>"
				+ "<scustMobile>13333333333</scustMobile>"
				+ "<scustComp>bbb</scustComp>"
				+ "<tcontactor>ccc</tcontactor>"
				+ "<tcustMobile>15856598606</tcustMobile>"
				+ "<tcustAddr>eeeeeeeeeee</tcustAddr>"
				+ "<tcustProvince>安徽省</tcustProvince>"
				+ "<tcustCity>安庆市</tcustCity>"
				+ "<tcustCounty>宿松县</tcustCounty>"
				+ "</printData>"
				+ "</printDatas>"
				+ "</XMLInfo>";
		System.out.println(sendstr);
		String expectedEncodeStr= "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiID8+CjxYTUxJbmZvPgo8c3lzQWNjb3VudD5BMTIzNDU2Nzg5MFo8L3N5c0FjY291bnQ+CjxwYXNzV29yZD5lMTBhZGMzOTQ5YmE1OWFiYmU1NmUwNTdmMjBmODgzZTwvcGFzc1dvcmQ+CjxhcHBLZXk+U0E3MTI1MTQ0QjNhNkJCMDA8L2FwcEtleT4KPHByaW50S2luZD4yPC9wcmludEtpbmQ+CjxwcmludERhdGFzPgo8cHJpbnREYXRhPgo8YmlnQWNjb3VudERhdGFJZD4yMDE1MTIwOTE3Mjc0MDIwPC9iaWdBY2NvdW50RGF0YUlkPgo8YnVzaW5lc3NUeXBlPjQ8L2J1c2luZXNzVHlwZT4KPGJpbGxubz5LREJHMTAwNjE4MjIxPC9iaWxsbm8+CjxzY29udGFjdG9yPmFhYTwvc2NvbnRhY3Rvcj4KPHNjdXN0TW9iaWxlPjEzMzMzMzMzMzMzPC9zY3VzdE1vYmlsZT4KPHNjdXN0Q29tcD5iYmI8L3NjdXN0Q29tcD4KPHRjb250YWN0b3I+Y2NjPC90Y29udGFjdG9yPgo8dGN1c3RNb2JpbGU+MTU4NTY1OTg2MDY8L3RjdXN0TW9iaWxlPgo8dGN1c3RBZGRyPmVlZWVlZWVlZWVlPC90Y3VzdEFkZHI+Cjx0Y3VzdFByb3ZpbmNlPuWuieW+veecgTwvdGN1c3RQcm92aW5jZT4KPHRjdXN0Q2l0eT7lronluobluII8L3RjdXN0Q2l0eT4KPHRjdXN0Q291bnR5PuWuv+advuWOvzwvdGN1c3RDb3VudHk+CjwvcHJpbnREYXRhPgo8L3ByaW50RGF0YXM+CjwvWE1MSW5mbz4K";
		String actualEncodeStr = new BASE64Encoder().encode(sendstr.getBytes());
		System.out.println(expectedEncodeStr);
		System.out.println(actualEncodeStr);
		Assert.assertEquals(expectedEncodeStr, actualEncodeStr);
		String beforeStr = URLEncoder.encode(actualEncodeStr);
	}
	
	@Test
	public void handleSendOrderToEMS(){
		String sendOrderUrl = "http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=getPrintDatas&xml=";
		String sendstr = "";
		BufferedReader in = null;
		try {
			//logger.info("[EMS]订单下发接口,发送报文xml{}", sendstr);
			sendstr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
					+ "<XMLInfo>"
					+ "<sysAccount>A1234567890Z</sysAccount>"
					+ "<passWord>e10adc3949ba59abbe56e057f20f883e</passWord>"
					+ "<appKey>SA7125144B3a6BB00</appKey>"
					+ "<printKind>2</printKind>"
					+ "<printDatas>"
					+ "<printData>"
					+ "<bigAccountDataId>2015120917274020</bigAccountDataId>"
					+ "<businessType>4</businessType>"
					+ "<billno>KDBG100618221</billno>"
					+ "<scontactor>aaa</scontactor>"
					+ "<scustMobile>13333333333</scustMobile>"
					+ "<scustComp>bbb</scustComp>"
					+ "<tcontactor>ccc</tcontactor>"
					+ "<tcustMobile>15856598606</tcustMobile>"
					+ "<tcustAddr>eeeeeeeeeee</tcustAddr>"
					+ "<tcustProvince>安徽省</tcustProvince>"
					+ "<tcustCity>安庆市</tcustCity>"
					+ "<tcustCounty>宿松县</tcustCounty>"
					+ "</printData>"
					+ "</printDatas>"
					+ "</XMLInfo>";
			//将发送给ems的订单信息字符串进行base64加密
			String base64Sendstr = new BASE64Encoder().encode(sendstr.getBytes());
//					"http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=getPrintDatas&xml=PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiID8%2BCjxYTUxJbmZvPgo8c3lzQWNjb3VudD5BMTIzNDU2Nzg5MFo8L3N5c0FjY291bnQ%2BCjxwYXNzV29yZD5lMTBhZGMzOTQ5YmE1OWFiYmU1NmUwNTdmMjBmODgzZTwvcGFzc1dvcmQ%2BCjxhcHBLZXk%2BU0E3MTI1MTQ0QjNhNkJCMDA8L2FwcEtleT4KPHByaW50S2luZD4yPC9wcmludEtpbmQ%2BCjxwcmludERhdGFzPgo8cHJpbnREYXRhPgo8YmlnQWNjb3VudERhdGFJZD4yMDE1MTIwOTE3Mjc0MDIwPC9iaWdBY2NvdW50RGF0YUlkPgo8YnVzaW5lc3NUeXBlPjQ8L2J1c2luZXNzVHlwZT4KPGJpbGxubz5LREJHMTAwNjE4MjIxPC9iaWxsbm8%2BCjxzY29udGFjdG9yPmFhYTwvc2NvbnRhY3Rvcj4KPHNjdXN0TW9iaWxlPjEzMzMzMzMzMzMzPC9zY3VzdE1vYmlsZT4KPHNjdXN0Q29tcD5iYmI8L3NjdXN0Q29tcD4KPHRjb250YWN0b3I%2BY2NjPC90Y29udGFjdG9yPgo8dGN1c3RNb2JpbGU%2BMTU4NTY1OTg2MDY8L3RjdXN0TW9iaWxlPgo8dGN1c3RBZGRyPmVlZWVlZWVlZWVlPC90Y3VzdEFkZHI%2BCjx0Y3VzdFByb3ZpbmNlPuWuieW%2BveecgTwvdGN1c3RQcm92aW5jZT4KPHRjdXN0Q2l0eT7lronluobluII8L3RjdXN0Q2l0eT4KPHRjdXN0Q291bnR5PuWuv%2BadvuWOvzwvdGN1c3RDb3VudHk%2BCjwvcHJpbnREYXRhPgo8L3ByaW50RGF0YXM%2BCjwvWE1MSW5mbz4K";
					
			base64Sendstr = URLEncoder.encode(base64Sendstr);
//					new BASE64Encoder().encode(sendstr.getBytes());
			//String base64Sendstr="http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=getPrintDatas&xml=PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiID8%2BCjxYTUxJbmZvPgo8c3lzQWNjb3VudD5BMTIzNDU2Nzg5MFo8L3N5c0FjY291bnQ%2BCjxwYXNzV29yZD5lMTBhZGMzOTQ5YmE1OWFiYmU1NmUwNTdmMjBmODgzZTwvcGFzc1dvcmQ%2BCjxhcHBLZXk%2BU0E3MTI1MTQ0QjNhNkJCMDA8L2FwcEtleT4KPHByaW50S2luZD4yPC9wcmludEtpbmQ%2BCjxwcmludERhdGFzPgo8cHJpbnREYXRhPgo8YmlnQWNjb3VudERhdGFJZD4yMDE1MTIwOTE3Mjc0MDIwPC9iaWdBY2NvdW50RGF0YUlkPgo8YnVzaW5lc3NUeXBlPjQ8L2J1c2luZXNzVHlwZT4KPGJpbGxubz5LREJHMTAwNjE4MjIxPC9iaWxsbm8%2BCjxzY29udGFjdG9yPmFhYTwvc2NvbnRhY3Rvcj4KPHNjdXN0TW9iaWxlPjEzMzMzMzMzMzMzPC9zY3VzdE1vYmlsZT4KPHNjdXN0Q29tcD5iYmI8L3NjdXN0Q29tcD4KPHRjb250YWN0b3I%2BY2NjPC90Y29udGFjdG9yPgo8dGN1c3RNb2JpbGU%2BMTU4NTY1OTg2MDY8L3RjdXN0TW9iaWxlPgo8dGN1c3RBZGRyPmVlZWVlZWVlZWVlPC90Y3VzdEFkZHI%2BCjx0Y3VzdFByb3ZpbmNlPuWuieW%2BveecgTwvdGN1c3RQcm92aW5jZT4KPHRjdXN0Q2l0eT7lronluobluII8L3RjdXN0Q2l0eT4KPHRjdXN0Q291bnR5PuWuv%2BadvuWOvzwvdGN1c3RDb3VudHk%2BCjwvcHJpbnREYXRhPgo8L3ByaW50RGF0YXM%2BCjwvWE1MSW5mbz4K";
			// 创建HttpClient实例     
	        HttpClient httpclient = new DefaultHttpClient();  
			HttpPost httpposts = new HttpPost(sendOrderUrl + base64Sendstr);    
	        HttpResponse response = httpclient.execute(httpposts);  
	        in = new BufferedReader(new InputStreamReader(response.getEntity()  
                    .getContent()));  
            StringBuffer sb = new StringBuffer("");  
            String line = "";  
            String NL = System.getProperty("line.separator");  
            while ((line = in.readLine()) != null) {  
                sb.append(line + NL);  
            }  
            in.close();  
            String result = sb.toString();  
			
			//将返回给dmp的订单信息字符串进行base64解密
			String response_XML = new String(new BASE64Decoder().decodeBuffer(result));
			System.out.println(response_XML);
		} catch (Exception e) {
		} 
		
	}
}
