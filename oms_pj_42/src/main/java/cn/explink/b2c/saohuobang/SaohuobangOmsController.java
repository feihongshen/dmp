package cn.explink.b2c.saohuobang;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/S_huobang")
public class SaohuobangOmsController {
	@Autowired
	SaohuobangomsService saohuobangomsService;
	private Logger logger = LoggerFactory.getLogger(Saohuobang.class);

	@RequestMapping("/Search")
	public @ResponseBody String getSearch(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/xml;charset=utf-8");
		/*
		 * String retrun_xml=
		 * "<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><clientID>JYHT</clientID><orders><order>"
		 * + "<mailNo>0000001</mailNo></order><order>"+
		 * "<mailNo>0000002</mailNo></order>"+ "</orders></BatchQueryRequest>";
		 */
		String retrun_xml = request.getParameter("logistics_interface");
		String sign = request.getParameter("data_digest");
		logger.info("[Search]Controller接收到的信息sign={},retrun={}", sign, retrun_xml);
		String xml = saohuobangomsService.feedback_status(retrun_xml, sign);
		return xml;
	}

}
