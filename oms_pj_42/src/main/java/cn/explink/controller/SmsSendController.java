package cn.explink.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.service.SmsSendService;

@Controller
@RequestMapping("/sms")
public class SmsSendController {

	@Autowired
	SmsSendService smsSendService;
	@Autowired
	CwbDAO cwbDAO;

	@RequestMapping("/sendForMobiles")
	public @ResponseBody String smsSendForMobiles(@RequestParam(value = "mobiles") String mobiles, @RequestParam(value = "smsRemack") String smsRemack) {
		try {
			if (mobiles.length() > 0) {
				smsSendService.sendSms(mobiles, smsRemack, mobiles.split(",").length);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "1";

		}
		return "0";
	}

	@RequestMapping("/view")
	public String view(Model model) {
		model.addAttribute("msg", "");
		return "smsSend/sms";
	}

}
