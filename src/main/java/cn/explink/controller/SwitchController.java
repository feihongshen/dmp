package cn.explink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.explink.dao.SwitchDAO;

@Controller
@RequestMapping("/switchcontroller")
public class SwitchController {

	@Autowired
	SwitchDAO switchDAO;

	@RequestMapping("/updateswitch")
	public @ResponseBody String updateswitch(Model model, @RequestParam(value = "switchstate", required = true) String switchstate) {
		try {
			switchDAO.delSwitch(switchstate, "入库打印标签");
			return "{\"errorCode\":0,\"error\":\"打印标签开关设置成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"打印标签开关设置失败\"}";
		}
	}

}
