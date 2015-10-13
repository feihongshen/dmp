package cn.explink.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.SmsConfigDAO;
import cn.explink.domain.SmsConfig;
import cn.explink.service.SmsConfigService;

@RequestMapping("/smsconfig")
@Controller
public class SmsConfigController {

	@Autowired
	SmsConfigDAO smsconfigDAO;
	@Autowired
	SmsConfigService smsConfigService;

	@RequestMapping("/setsmsview")
	public String list(Model model) {
		SmsConfig smsConfig = smsconfigDAO.getAllSmsConfig();
		int checksmsnull = 1;
		if (smsConfig == null) {
			smsConfig = new SmsConfig();
			checksmsnull = 0;
		}
		model.addAttribute("smsconfig", smsConfig);
		model.addAttribute("checksmsnull", checksmsnull);
		return "/smsconfig/setsmsview";
	}

	@RequestMapping("/alertsms")
	public String alertsms() {
		return "/smsconfig/save";
	}

	@RequestMapping("/savesmsconfig")
	public @ResponseBody String savesmsconfig(Model model, @RequestParam("pass") String pass) {
		if ("explink".equals(pass)) {
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		}
		return "{\"errorCode\":1,\"error\":\"密码有误\"}";
	}

	@RequestMapping("/createORsave")
	public String save(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		SmsConfig s = smsConfigService.loadFormForSmsConfig(request);
		String checksmsnull = (String) request.getParameter("checksmsnull");
		if (checksmsnull.equals("0")) {
			smsconfigDAO.creSmsConfig(s.getName(), s.getPassword(), s.getWarningcount(), s.getPhone(), s.getTemplet(), s.getMonitor(), s.getWarningcontent(), s.getIsOpen(), s.getTemplatecontent());
		} else if (checksmsnull.equals("1")) {
			smsconfigDAO.saveSmsConfig(s.getName(), s.getPassword(), s.getWarningcount(), s.getPhone(), s.getTemplet(), s.getMonitor(), s.getWarningcontent(), s.getIsOpen(), s.getTemplatecontent(),
					s.getId());
		}
		model.addAttribute("smsconfig", smsconfigDAO.getAllSmsConfig());
		model.addAttribute("checksmsnull", 1);
		return "/smsconfig/setsmsview";
	}

}
