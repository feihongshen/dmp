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
		SystemInstall siteDayLogTime = this.systemInstallDAO.getSystemInstallByName("printcwbTLF");
		String[] printcwbTLF = siteDayLogTime.getValue().split(",");
		model.addAttribute("printcwbTLF", printcwbTLF);
		return "printcwb";
	}

	@RequestMapping("printCwbnew")
	public String printCwbnew(Model model, @RequestParam(value = "scancwb", required = false, defaultValue = "") String scancwb) {
		model.addAttribute("scancwb", scancwb);
		return "printcwbnew";
	}

	// 打印入库 控制器
	@RequestMapping("printCwbruku")
	public String printCwbruku(Model model, @RequestParam(value = "scancwb", required = false, defaultValue = "") String scancwb) {
		model.addAttribute("scancwb", scancwb);
		return "printcwbruku";
	}

	@RequestMapping("/list")
	public String list(Model model) {
		return "intowarehouseprint/list";
	}

	@RequestMapping("style1")
	public String style1(Model model, @RequestParam(value = "scancwb", required = false, defaultValue = "") String scancwb) {
		SystemInstall siteDayLogTime = this.systemInstallDAO.getSystemInstallByName("printcwbTLF");
		String[] printcwbTLF = siteDayLogTime.getValue().split(",");
		model.addAttribute("scancwb", scancwb);
		model.addAttribute("printcwbTLF", printcwbTLF);
		return "intowarehouseprint/printcwb";
	}

	@RequestMapping("style2")
	public String style2(Model model, @RequestParam(value = "scancwb", required = false, defaultValue = "") String scancwb) {
		model.addAttribute("scancwb", scancwb);
		return "intowarehouseprint/printcwbnew";
	}

	@RequestMapping("style3")
	public String style3(Model model, @RequestParam(value = "scancwb", required = false, defaultValue = "") String scancwb) {
		model.addAttribute("scancwb", scancwb);
		return "intowarehouseprint/printcwbruku";
	}
}
