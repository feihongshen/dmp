package cn.explink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.service.AppearWindowService;

@Controller
@RequestMapping("/showWindow")
public class AppearWindowContrller {
	@Autowired
	AppearWindowService appearWindowService;

	@RequestMapping("/showInfo")
	public @ResponseBody String showInfo() {// 查询新表中state=1（为提醒）的订单
		String newlist = appearWindowService.getAppearNewinfo();
		return newlist;
	}

	@RequestMapping("/changeState/{type}")
	public @ResponseBody String changeState(@PathVariable("type") String type) {
		String a = appearWindowService.getchangeState(type);
		return a;
	}

}
