package cn.explink.b2c.explink.code_down;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import cn.explink.b2c.explink.xmldto.OrderExportCallbackDto;
import cn.explink.b2c.explink.xmldto.OrderExportConditionDto;
import cn.explink.b2c.explink.xmldto.OrderExportResultDto;
import cn.explink.b2c.explink.xmldto.ReturnDto;

public class RestTemplateHanlder {
	private Logger logger = LoggerFactory.getLogger(RestTemplateHanlder.class);

	/**
	 * 导出订单数据
	 * 
	 * @param URL
	 * @param conditionDto
	 * @return
	 */
	public OrderExportResultDto exportOrder(String URL, OrderExportConditionDto conditionDto) {
		OrderExportResultDto result = new OrderExportResultDto();
		try {
			RestTemplate restTemplate = new RestTemplate();
			result = restTemplate.postForObject(URL, conditionDto, OrderExportResultDto.class);
			logger.info("Invoke exportOrder method Success!response OrderExportResultDto={}", JSONObject.fromObject(result));
		} catch (Exception e) {
			logger.error("error info while excute exportOrder's restTemplate.PostForObject method!" + e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 订单信息导出成功回调（配送单、取件单）
	 * 
	 * @param URL
	 * @param conditionDto
	 * @return
	 */
	public ReturnDto exportCallBack(String URL, OrderExportCallbackDto conditionDto) {
		ReturnDto result = new ReturnDto();
		try {
			RestTemplate restTemplate = new RestTemplate();
			result = restTemplate.postForObject(URL, conditionDto, ReturnDto.class);
			logger.info("Invoke exportCallBack method Success!response OrderExportResultDto={}", JSONObject.fromObject(result));
		} catch (Exception e) {
			logger.error("error info while excute exportCallBack's restTemplate.PostForObject method!" + e);
			e.printStackTrace();
		}
		return result;
	}

	public static String SendHttptoServer(String content, String URL) throws Exception {

		URL url = new URL(URL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("content-type", "text/xml");
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("contentType", "utf-8");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setConnectTimeout(30000); // 设置延迟为40秒
		httpURLConnection.setReadTimeout(30000);
		httpURLConnection.connect();
		OutputStreamWriter reqOut = null;
		if (content != null) {
			reqOut = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
		}
		BufferedWriter out = new BufferedWriter(reqOut);
		out.write(content);
		out.flush();
		// 接收服务器的返回：
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
		StringBuilder buffer = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

}