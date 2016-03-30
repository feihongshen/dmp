package cn.explink.pos.unionpay.test;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.UserDAO;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.PosPayService;
import cn.explink.pos.unionpay.UnionPay;
import cn.explink.pos.unionpay.UnionPayEnum;
import cn.explink.pos.unionpay.UnionPayService;
import cn.explink.service.CwbOrderService;
import cn.explink.util.MD5.Base64Utils;
import cn.explink.util.MD5.MD5Util;

@Service
public class UnionPayTestService {
	private Logger logger = LoggerFactory.getLogger(UnionPayTestService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	UserDAO userDAO;

	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	PosPayDAO posPayDAO;
	@Autowired
	PosPayService posPayService;
	@Autowired
	JointService jointService;
	@Autowired
	UnionPayService unionPayService;

	public String requestUnionPayInterface_test(String command, String username, String password, String cwb, String payamount, String signtype, String signname, String pay_type, String expt_code,
			Model model) throws Exception {
		UnionPay unionpay = unionPayService.getUnionPaySettingMethod(PosEnum.UnionPay.getKey());
		String data = "";
		if (command.equals(UnionPayEnum.Login.getCommand())) {
			data = buildJson_login(username, password);
		}
		if (command.equals(UnionPayEnum.LoginOut.getCommand())) {
			data = buildJson_loginOut(username);
		}
		if (command.equals(UnionPayEnum.Search.getCommand())) {
			data = buildJson_search(username, cwb);
		}
		if (command.equals(UnionPayEnum.Delivery.getCommand())) {
			data = buildJson_delivery(username, cwb, payamount, signtype, signname, pay_type, expt_code);
		}

		model.addAttribute("requestjson", data);
		data = Base64Utils.encode(data.getBytes());
		String Md5str = (MD5Util.md5(data + unionpay.getPrivate_key()).substring(8, 24)).toLowerCase().trim(); // 16位的md5加密

		return HTTPClient_Send(unionpay.getRequest_url(), command, data, Md5str);

	}

	private String buildJson_login(String username, String password) {
		return "{\"TerminalNo\":\"05084724\",\"LoginName\":\"" + username + "\",\"Password\":\"" + password + "\"}";
	}

	private String buildJson_loginOut(String username) {
		return "{\"TerminalNo\":\"05084724\",\"LoginName\":\"" + username + "\"}";
	}

	private String buildJson_search(String username, String cwb) {
		return "{\"TerminalNo\":\"05084724\",\"LoginName\":\"" + username + "\",\"BarCode\":\"" + cwb + "\"}";
	}

	private String buildJson_delivery(String username, String cwb, String payamount, String signtype, String signname, String pay_type, String expt_code) {
		String jsoncontent = "{'TerminalNo':'05084724','LoginName':'" + username + "','BarCode':'" + cwb + "','CardNo':'445098711123456789','BusinessTime':'20130105155600',"
				+ "'TerminalDealID':'676767','UnionpayDealID':'121212121222','TotalPayment':" + (payamount.equals("") ? 0 : payamount) + ",'SignFlag':'" + signtype + "','SignName':'" + signname
				+ "'," + "'State':'" + pay_type + "', 'UntreadReasonCode':'" + expt_code + "','Commoditys':'测试商品'}";

		return jsoncontent;
	}

	public String HTTPClient_Send(String URL, String command, String content, String Md5str) {

		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		PostMethod postMethod = new PostMethod(URL); // URL为要请求的地址
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8"); // 设置编码方式

		NameValuePair[] data = { new NameValuePair("Command", command), new NameValuePair("Data", content), new NameValuePair("Md5", Md5str)

		};

		postMethod.setRequestBody(data); // 将表单的值放入postMethod中
		try {
			httpClient.executeMethod(postMethod); // 执行数据传输的方法。
			return postMethod.getResponseBodyAsString(); // 返回一个响应结果
		} catch (HttpException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			postMethod.releaseConnection();
		}
		return "";

	}

}
