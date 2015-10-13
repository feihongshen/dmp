package cn.explink.b2c.happygo;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.util.Dom4jParseUtil;

@Controller
@RequestMapping("/happygo_oms")
public class HappyGoController {
	@Autowired
	HappyGoService happyGoService;

	@RequestMapping("/getGoods")
	public @ResponseBody String daohuoConnection() {
		happyGoService.feed_backstate();
		return "oms快乐购执行完毕";
	}

	@RequestMapping("/searchGoods")
	public @ResponseBody String searchGoods(HttpServletResponse response, HttpServletRequest request) {
		InputStream input;
		String rexml = "";
		try {
			input = new BufferedInputStream(request.getInputStream());
			String XMLDOC = Dom4jParseUtil.getStringByInputStream(input);
			// logger.info("亚联登陆得到的xml={}",XMLDOC);
			rexml = happyGoService.search_backstate(XMLDOC);
		} catch (Exception e) {

		}
		return rexml;

	}

}
