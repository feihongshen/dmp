package cn.explink.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.dao.SalaryImportDao;
import cn.explink.domain.SalaryImport;

@Controller
@RequestMapping("/salarySheet")
public class SalarySheetController {
	@Autowired
	SalaryImportDao salaryImportDao;
	/*
	 * 获取所有工资条相关信息
	 */
	@RequestMapping("/list")
	public String setSalaryImport(Model model){
		List<SalaryImport> simportList = salaryImportDao.getAllSalaryImports();
		model.addAttribute("simportList",simportList);
		return "/salary/salarySheet/salarysheet";
	}	
}
