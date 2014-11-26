package cn.explink.b2c.dangdang_dataimport;

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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/explinkdatasyn")
public class DangDangDataSynTestController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	DangDangDataSynService dangDangDataSynService;
	@Autowired
	JointService jointService;

	@RequestMapping("/page")
	public String postToDangDangPage(HttpServletRequest request, HttpServletResponse response) {

		return "b2cdj/dangdang_datasyntest";
	}

	/**
	 * 当当数据导入接口 请求接口 2013-06-22
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/test")
	public String postToDangDang(HttpServletRequest request, HttpServletResponse response) {
		DangDangDataSyn dangdang = dangDangDataSynService.getDangDang(B2cEnum.DangDang_daoru.getKey());
		String url = dangdang.getOwn_url();

		String action = request.getParameter("action");
		String express_id = dangdang.getExpress_id();
		String request_time = DateTimeUtil.getNowTime();

		String token = MD5Util.getMD5String32Bytes(request_time + express_id, dangdang.getPrivate_key());

		String order_list = request.getParameter("order_list");

		String str = "";
		try {
			str = getPostMethodResult(url, action, express_id, request_time, token, order_list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("请求当当返回未知异常", e);
		}
		logger.info("response dangdang test service={}", str);
		request.setAttribute("values", str);
		return "b2cdj/dangdang_datasyntest";
	}

	private String getPostMethodResult(String url, String action, String express_id, String request_time, String token, String order_list) throws IOException, HttpException {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("version", "1.0"), // 写死
				new NameValuePair("action", action), // 写死
				new NameValuePair("express_id", express_id), new NameValuePair("request_time", request_time), new NameValuePair("token", token), new NameValuePair("order_list", order_list), }; // 原始信息

		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		httpClient.executeMethod(postMethod); // post数据
		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();
		return str;
	}

	/**
	 * 当当数据导入接口 请求接口 2013-06-22
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/")
	public @ResponseBody String requestByDangDang(HttpServletRequest request, HttpServletResponse response) {

		try {

			DangDangDataSyn dangdangsyn = dangDangDataSynService.getDangDang(B2cEnum.DangDang_daoru.getKey());
			String charcode = dangdangsyn.getCharcode();
			// request.setCharacterEncoding(charcode);
			response.setContentType("text/xml;charset=" + charcode);
			response.setCharacterEncoding(charcode);

			String version = request.getParameter("version");// 当当 版本
			String action = request.getParameter("action");// 接收的接口 put / del
			String express_id = request.getParameter("express_id");// 快递公司id
			String request_time = request.getParameter("request_time");// 请求时间
			String token = request.getParameter("token"); // 验证码
			String order_list = request.getParameter("order_list"); // JSON 字符串

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.DangDang_daoru.getKey());
			if (isOpenFlag == 0) {
				logger.warn("未开启当当订单导入接口");
				return dangDangDataSynService.respJSONInfo("103");
			}

			logger.info("当当数据导入接口请求:version=" + version + ",action=" + action + ",express_id=" + express_id + ",request_time=" + request_time + ",token=" + token + ",order_list=" + order_list);

			return dangDangDataSynService.AnalizyDangDangRequestInfo(dangdangsyn, version, action, express_id, request_time, token, order_list);

		} catch (Exception e) {
			logger.error("当当请求数据导入接口发生未知异常", e);
			return dangDangDataSynService.respJSONInfo("103");
		}

	}

}
