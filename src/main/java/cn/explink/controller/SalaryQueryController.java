package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.SalaryFixedDAO;
import cn.explink.dao.SalaryImportDao;
import cn.explink.dao.SalaryImportRecordDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.SalaryFixed;
import cn.explink.domain.SalaryImport;
import cn.explink.domain.SalaryImportRecord;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.ExportService;
import cn.explink.util.ExcelUtilsHandler;
import cn.explink.util.Page;

@Controller
@RequestMapping("/salaryQuery")
public class SalaryQueryController {
	@Autowired
	SalaryImportDao salaryImportDao;
	@Autowired
	ExportService exportService;
	@Autowired
	SalaryFixedDAO salaryDAO;
	@Autowired
	SalaryImportRecordDAO salaryImportRecordDAO;
	@Autowired
	BranchDAO branchDAO;
	@RequestMapping("/list/{page}")
	public String salaryQuery(@PathVariable("page") long page,
			@RequestParam(value = "batchnum", required = false, defaultValue = "") String realname,
			@RequestParam(value = "branch",required = false,defaultValue = "") String idcard,
			@RequestParam(value = "distributionmember",required = false,defaultValue = "") String isnow,
			@RequestParam(value = "idcard",required = false,defaultValue = "") String idcard1,
			@RequestParam(value = "importflag", required = false, defaultValue = "0") long importflag,
			Model model){
		List<SalaryFixed> salaryList=this.salaryDAO.getSalaryByRealnameAndIdcard(page, realname, idcard);
		int count= this.salaryDAO.getSalaryByRealnameAndIdcardCounts(realname, idcard);
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		SalaryImportRecord salaryImportRecord=new SalaryImportRecord();
		if(importflag>0)
		{

			salaryImportRecord=this.salaryImportRecordDAO.getSalaryImportRecordByImportFlag(importflag);
		}
		List<Branch> branchList=this.branchDAO.getAllBranchBySiteType(BranchEnum.ZhanDian.getValue());

		model.addAttribute("branchList", branchList);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("salaryList", salaryList);
		model.addAttribute("realname", realname);
		model.addAttribute("idcard", idcard);
		model.addAttribute("record", salaryImportRecord);
		model.addAttribute("importflag", importflag);
		return "salary/salaryQuery/list";
	}
	//按照工资条导出与全部导出
	@RequestMapping("/exportExceldata/{flag}")
	public void  exceldatallExport(@PathVariable("flag")long flag,
			@RequestParam(value = "batchnum", required = false, defaultValue = "") String realname,
			@RequestParam(value = "branch",required = false,defaultValue = "") String idcard,
			@RequestParam(value = "distributionmember",required = false,defaultValue = "") String isnow,
			@RequestParam(value = "idcard",required = false,defaultValue = "") String idcard1,
			HttpServletResponse response
			){
		final String[] cloumnName;
		final String[] cloumnName3;
		String sheetName;
		String fileName;
		if (flag==0) {
			//全部导出
			String[] cloumnName1 = new String[42]; // 导出的列名
			String[] cloumnName2 = new String[42]; // 导出的英文列名
			this.exportService.SetMisspieceFields(cloumnName1, cloumnName2);
			cloumnName = cloumnName1;
			cloumnName3 = cloumnName2;
			sheetName = "工资信息（全部导出）"; // sheet的名称
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			fileName = "SalaryAll_" + df.format(new Date()) + ".xlsx"; // 文件名
		}else {
			List<SalaryImport> salaryImports=salaryImportDao.getAllSalaryImports();
			//按照工资条导出
			String[] cloumnName1 = new String[salaryImports.size()]; // 导出的列名
			String[] cloumnName2 = new String[salaryImports.size()]; // 导出的英文列名
			this.exportService.setSalaryImportsFields(cloumnName1, cloumnName2,salaryImports);
			cloumnName = cloumnName1;
			cloumnName3 = cloumnName2;
			sheetName = "工资信息（按工资条导出）"; // sheet的名称
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			fileName = "Salary_" + df.format(new Date()) + ".xlsx"; // 文件名
		}
		//要导出的信息
		List<String> objList=new ArrayList<String>();
		ExcelUtilsHandler.exportExcelHandler(response, cloumnName, cloumnName3, sheetName, fileName, objList);
		
	}
}
