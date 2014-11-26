package cn.explink.b2c.vipshop.addresspre;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/addresspre")
public class AddressInterController {
	/**
	 * 唯品全请求地址库匹配地址并返回
	 */
	@Autowired
	AddressInterService addressIngerService;

	private Logger logger = LoggerFactory.getLogger(AddressInterController.class);

	@RequestMapping("/testaddressvip")
	public String actiondetail() {

		try {
			return "/testaddressvip/testaddressvip";
		} catch (Exception e) {
			logger.error("唯品会请求地址库 遇见不可预知的错误！" + e);
			return null;
		}
	}

	/**
	 * 接收唯品会请求，请求地址库匹配地址前置信息并返回匹配结果
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/")
	public @ResponseBody String requestByVipshop(HttpServletRequest request, HttpServletResponse response) {

		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			// 获取参数，名称类型待定
			String xml = request.getParameter("data");
			logger.info("唯品会匹配站点: 地址：请求xml------{}", xml);
			return addressIngerService.getAddress(xml);
		} catch (Exception e) {
			logger.error("唯品会请求地址库 遇见不可预知的错误！" + e);
			return null;
		}
	}

	public static void main(String[] arg) {
		String xml = "<request>" + "<head>" + "<usercode>VIPSHOP</usercode>" + "<batchno>2013072312341034</batchno>" + "</head>" + "<items>" + "<item>" + "<itemno>1</itemno>"
				+ "<address><![CDATA[北京东城区和平里安贞苑]]></address>" + "</item>" + "<item>" + "<itemno>2</itemno>" + "<address><![CDATA[北京朝阳区小关惠新北里]]></address>" + "</item>" + "</items>" + "</request>";
		System.out.println("xml:" + xml);
	}
}
