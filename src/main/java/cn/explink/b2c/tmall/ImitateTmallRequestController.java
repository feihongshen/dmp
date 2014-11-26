package cn.explink.b2c.tmall;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tmall.TmallService;
import cn.explink.util.DateTimeUtil;

@Controller
@RequestMapping("/tmalltest")
public class ImitateTmallRequestController {
	private Logger logger = LoggerFactory.getLogger(ImitateTmallRequestController.class);
	@Autowired
	TmallService tmallService;

	@RequestMapping("/ImitateTmallRequest")
	public @ResponseBody String ImitateTmallRequest() {

		String content = "<request>" + "<order_code>LBX00199412050002</order_code><tms_service_code>TXD-001</tms_service_code>"
				+ "<tms_order_code>0019941205000</tms_order_code><schedule_type>104</schedule_type>"
				+ "<schedule_start>2014-08-09 10:00:00</schedule_start><schedule_end>2014-08-09 11:00:00</schedule_end>" + "<receiver_name>毛敏蝶1</receiver_name><receiver_zip>215400</receiver_zip>"
				+ "<receiver_province>江苏省</receiver_province>" + "<receiver_city>苏州市</receiver_city><receiver_district>太仓市</receiver_district>"
				+ "<receiver_address>城厢镇上海西路47号农商行城厢支行</receiver_address>" + "<receiver_mobile>18626294210</receiver_mobile><package_weight>210</package_weight>"
				+ "<wms_code>LANBO-002</wms_code><wms_address>上海上海市青浦区徐泾镇华徐公路589号E1库U2-U3仓</wms_address>" + "</request>";
		String URL = "http://localhost:8080/dmp/tmall/interface";
		String resp_content = HTTPClient_Send(URL, content);
		return resp_content;

	}

	public static String HTTPClient_Send(String URL, String content) {
		String responseXml = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		PostMethod postMethod = new PostMethod(URL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		String nowtime = DateTimeUtil.getNowTime();
		NameValuePair[] data = { new NameValuePair("content", content), new NameValuePair("partner", "201211201923452901"), new NameValuePair("sign_type", "MD5"),
				new NameValuePair("sign", "ZTMwNDAyZjI0YWNlYmQzNGI4ZjIyYTkyY2NmOGM2M2Q="), new NameValuePair("notify_id", "1211545462232s323"), new NameValuePair("notify_type", "tms_order_notify"),
				new NameValuePair("notify_time", nowtime),

		};

		postMethod.setRequestBody(data);
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(postMethod); // post数据
			responseXml = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return responseXml;

	}

}
