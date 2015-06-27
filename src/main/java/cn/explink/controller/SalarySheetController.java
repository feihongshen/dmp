package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
		List<SalaryImport> addList = salaryImportDao.getSalarySheetAdd();
		List<SalaryImport> deductList = salaryImportDao.getSalarySheetDeduct();
		model.addAttribute("addList",addList);
		model.addAttribute("deductList",deductList);
		return "/salary/salarySheet/salarysheet";
	}	
	
	@RequestMapping("/updateischecked")
	public @ResponseBody String updateischecked(HttpServletRequest request,
			@RequestParam(value= "fileNameArr[]",required = false, defaultValue="")List<String> filestrArrList,
			@RequestParam(value= "ischeckedArr[]",required = false, defaultValue="")List<Integer> ischeckedArrList
			){
		for(String filename : filestrArrList){
			int index = filestrArrList.indexOf(filename);
			int ischecked = ischeckedArrList.get(index);
			salaryImportDao.updateIscheced(filename,ischecked);
		}
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}
}
