package cn.explink.test;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.explink.b2c.happyGo.HappyGo;
import cn.explink.b2c.happyGo.HappyGoService;
import cn.explink.b2c.jumei.AnalyzXMLJuMeiHandler;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/testHappy")
public class TestController {
	@Autowired
	HappyGoService happyGoService;

	private Logger logger = LoggerFactory.getLogger(TestController.class);

	@RequestMapping("save")
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String number = request.getParameter("number");
		HappyGo happy = happyGoService.getHappyGo(B2cEnum.happyGo.getKey());
		String xml = "<Program><parameters>" + "<from_date>" + start + "</from_date>" + "<to_date>" + end + "</to_date></parameters>"
				+ "<page><page_size>200</page_size><page_no>0</page_no ></page></Program>";
		StringBuffer sbSystemArgs = new StringBuffer(); // 系统参数
		String sign = getMD5GetherForSign(happy, xml, number, sbSystemArgs);
		logger.info("sign={},url={}", sign, happy.getPostUrl() + "?" + sbSystemArgs.toString() + "&sign=" + sign);
		String retrun_xml = happyGoService.doRequest(xml, happy.getPostUrl() + "?" + sbSystemArgs.toString() + "&sign=" + sign, happy);
		logger.info("获得返回快乐购" + number + "的xml{}", retrun_xml);
		Map<String, Object> map;
		try {
			map = AnalyzXMLJuMeiHandler.parserXmlToJSONObjectByArray(retrun_xml);

			happyGoService.manageShappygo(number, map);
		} catch (Exception e) {
			logger.error("快乐购异常" + e);
		}

	}

	private String getMD5GetherForSign(HappyGo happy, String xml, String strMethod, StringBuffer sbSystemArgs) {
		long ate = System.currentTimeMillis();
		String a = String.valueOf(ate);
		String b = a.substring(0, 10);

		sbSystemArgs.append("app_id=" + happy.getCode());
		sbSystemArgs.append("&charset=utf-8");
		sbSystemArgs.append("&data_type=xml");
		sbSystemArgs.append("&function_id=" + strMethod);

		sbSystemArgs.append("&sign_type=md5");
		sbSystemArgs.append("&time=" + b);
		sbSystemArgs.append("&version=1.0");
		String md5Date = sbSystemArgs.toString() + xml + happy.getKey();
		String sign = MD5Util.md5(md5Date).toUpperCase();
		return sign;
	}

}
