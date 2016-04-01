package cn.explink.b2c.gzabc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/abctest")
public class GuangZhouABCTestController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	GuangZhouABCService gzabcService;
	@Autowired
	JointService jointService;

	@RequestMapping("/page")
	public String postToDangDangPage(HttpServletRequest request, HttpServletResponse response) {

		return "b2cdj/abctest";
	}

	/**
	 * 爱彼西数据导入接口 请求接口 2013-06-22
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/")
	public String postToDangDang(HttpServletRequest request, HttpServletResponse response) {
		GuangZhouABC gzabc = gzabcService.getGuangZhougABC(B2cEnum.GuangZhouABC.getKey());
		String action = request.getParameter("action");
		if (action.equals("gzabc")) {
			gzabc = gzabcService.getGuangZhougABC(B2cEnum.GuangZhouABC.getKey());
		} else {
			gzabc = gzabcService.getGuangZhougABC(B2cEnum.HangZhouABC.getKey());
		}

		String url = gzabc.getRequst_url();

		String shippedcode = gzabc.getShippedCode();

		String logicdata = request.getParameter("logicdata");

		String checkdata = MD5Util.md5(logicdata + gzabc.getPrivate_key());

		String str = "";
		try {
			str = getPostMethodResult(url, shippedcode, logicdata, checkdata);
		} catch (Exception e) {
			logger.error("请求爱彼西返回未知异常", e);
		}
		logger.info("response ABC test service={}", str);
		request.setAttribute("values", str);
		return "b2cdj/abctest";
	}

	private String getPostMethodResult(String url, String shippedcode, String logicdata, String checkdata) throws IOException, HttpException {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		// 填入各个表单域的值
		NameValuePair[] data = {

		new NameValuePair("sShippedCode", shippedcode), // 写死
				new NameValuePair("logicdata", logicdata), new NameValuePair("checkdata", checkdata),

		}; // 原始信息

		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		httpClient.executeMethod(postMethod); // post数据
		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();
		return str;
	}

}
