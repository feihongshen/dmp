package cn.explink.pos.yalian;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.Dom4jParseUtil;

@Controller
@RequestMapping("/Yalian_go")
public class YalianController {

	@Autowired
	YalianDMPService yalianService;
	@Autowired
	JiontDAO jointDAO;

	private Logger logger = LoggerFactory.getLogger(YalianApp.class);

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("mobilecodapplist", yalianService.getyalianapp(key));
		model.addAttribute("joint_num", key);
		return "jointmanage/yalian/edit";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			yalianService.edit(request, joint_num);

			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		yalianService.update(key, state);

		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/*
	 * 亚联登陆
	 */
	@RequestMapping("/login")
	public @ResponseBody String Landed(HttpServletRequest request) {
		InputStream input;
		String responsexml = null;
		try {
			input = new BufferedInputStream(request.getInputStream());
			String XMLDOC = Dom4jParseUtil.getStringByInputStream(input);
			logger.info("亚联登陆得到的xml={}", XMLDOC);
			responsexml = yalianService.JudgmentLanded(XMLDOC);
			logger.info("亚联登陆[login接口返回xml],{}", responsexml);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responsexml;
		// xml="<request><deliverName>admin</deliverName><deliverPwd>admin</deliverPwd><operateDate>1</operateDate></request>";
	}

	/*
	 * 亚联确认接收订单接口
	 */
	@RequestMapping("/confirm")
	public @ResponseBody String Confirm(HttpServletRequest request) {
		InputStream input;
		String responsexml = null;
		try {
			input = new BufferedInputStream(request.getInputStream());
			String XMLDOC = Dom4jParseUtil.getStringByInputStream(input);
			logger.info("亚联订单确认接口得到的dmp,xml={}", XMLDOC);
			responsexml = yalianService.ComfirmReceive(XMLDOC);
			logger.info("亚联订单确认[confirm接口返回xml],{}", responsexml);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responsexml;
	}

	/*
	 * 订单反馈
	 */
	@RequestMapping("/deliverpod")
	public @ResponseBody String RebackInformation(HttpServletRequest request) {
		InputStream input;
		String responsexml = null;
		try {
			input = new BufferedInputStream(request.getInputStream());
			String XMLDOC = Dom4jParseUtil.getStringByInputStream(input);
			logger.info("亚联订单反馈接口得到的dmp,xml={}", XMLDOC);
			responsexml = yalianService.RebackXmlForYalain(PosEnum.YalianApp.getKey(), XMLDOC);
			logger.info("亚联订单反馈[deliverpod接口返回xml],{}", responsexml);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responsexml;

	}

	/*
	 * 修建收件人
	 */
	@RequestMapping("/update")
	public @ResponseBody String ModificationConsignee(HttpServletRequest request) {
		InputStream input;
		String responsexml = null;
		try {
			input = new BufferedInputStream(request.getInputStream());
			// String
			// XMLDOC="<request><cwb>109</cwb><consigneeName>张三</consigneeName><consigneMobile></consigneMobile><consigneeCerdType>身份证</consigneeCerdType><consigneeCerdNum>430682199007127011</consigneeCerdNum><operateDate>2014-01-17 10:17:44</operateDate></request>";
			String XMLDOC = Dom4jParseUtil.getStringByInputStream(input);
			logger.info("获取亚联修改收件人[update接口],xml={}", XMLDOC);
			responsexml = yalianService.ModificationXmlforConsignee(PosEnum.YalianApp.getKey(), XMLDOC);
			logger.info("亚联修改收件人[update接口返回xml],{}", responsexml);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responsexml;
	}

}
