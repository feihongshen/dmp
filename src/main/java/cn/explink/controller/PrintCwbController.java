package cn.explink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;

@RequestMapping("/printcwb")
@Controller
public class PrintCwbController {

	@Autowired
	SystemInstallDAO systemInstallDAO;

	@RequestMapping("")
	public String printCwb(Model model) {
		SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("printcwbTLF");
		String[] printcwbTLF = siteDayLogTime.getValue().split(",");
		model.addAttribute("printcwbTLF", printcwbTLF);
		return "printcwb";
	}

	@RequestMapping("printCwbnew")
	public String printCwbnew(Model model, @RequestParam(value = "scancwb", required = false, defaultValue = "") String scancwb) {
		model.addAttribute("scancwb", scancwb);
		return "printcwbnew";
	}
}
