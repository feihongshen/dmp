package cn.explink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.service.XiazaiService;

@Controller
@RequestMapping("/xiazai")
public class XiazaiContronller {

	@Autowired
	XiazaiService xiazaiService;

	@RequestMapping("/test")
	public String add(Model model) {
		return "xiazai/test";
	}

	@RequestMapping("/test1")
	public String add1(Model model) {
		return "xiazai/test1";
	}

	@RequestMapping("/xiazai1")
	public String xiazai(Model model) {
		xiazaiService.test();
		return "xiazai/test";
	}

	@RequestMapping("/tingzhi")
	public String tingzhi(Model model) {
		xiazaiService.zhongduanTest();
		return "xiazai/test1";
	}
}
