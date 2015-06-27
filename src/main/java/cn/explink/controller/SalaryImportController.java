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
@RequestMapping("/salaryImport")
public class SalaryImportController {
	@Autowired
	SalaryImportDao salaryImportDao;
	/*
	 * 获取所有工资条相关信息
	 */
	@RequestMapping("/list")
	public String setSalaryImport(Model model){
		List<SalaryImport> addList = salaryImportDao.getSalaryImportAdd();
		List<SalaryImport> deductList = salaryImportDao.getSalaryImportDeduct();
		model.addAttribute("addList",addList);
		model.addAttribute("deductList",deductList);
		return "/salary/salaryImport/salaryimport";
	}	
	
	/*
	 * 保存对工资条固定值的设置
	 */
	@RequestMapping("/updateWhichvalue")
	public @ResponseBody String updateWhichvalue(HttpServletRequest request,
			@RequestParam(value= "fileNameArr[]",required = false, defaultValue="")List<String> filestrArrList,
			@RequestParam(value= "whichValueArr[]",required = false, defaultValue="")List<Integer> whichValueArrList
			){
		if(filestrArrList!=null&&whichValueArrList!=null){
			for(String str : filestrArrList){
				int index = filestrArrList.indexOf(str);
				int whichvalue = whichValueArrList.get(index);
				salaryImportDao.updateWhichvalue(whichvalue,str);
			}
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
		return "{\"errorCode\":1,\"error\":\"保存失败\"}";
	}
}
