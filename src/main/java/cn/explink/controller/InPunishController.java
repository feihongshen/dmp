package cn.explink.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inpunish")
public class InPunishController {
	@RequestMapping("/inpunishdetail")
	public String inpunishdetail(Model model,HttpServletRequest request,HttpServletResponse response){
		return "/inpunish/inpunishsumdetail";
	}
}
