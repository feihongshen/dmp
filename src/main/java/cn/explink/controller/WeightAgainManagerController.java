package cn.explink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/weightAgainManager")
@Controller
public class WeightAgainManagerController {
	
	
	/**
	 * index 页面
	 */
	@RequestMapping(params = "index")
	public String index(Model model){
		return "weightAgain";
	}
}
