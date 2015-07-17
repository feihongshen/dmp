package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.SalaryCountDAO;
import cn.explink.dao.SalaryFixedDAO;
import cn.explink.dao.SalaryGatherDao;
import cn.explink.dao.SalaryImportDao;
import cn.explink.dao.SalaryImportRecordDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.SalaryGather;
import cn.explink.domain.SalaryGatherExport;
import cn.explink.domain.SalaryImport;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.ExportService;
import cn.explink.service.SalaryQueryService;
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
	@Autowired
	SalaryGatherDao salaryGatherDao;
	@Autowired
	SalaryQueryService salaryQueryService;
	@Autowired
	SalaryCountDAO salaryCountDAO;
	@RequestMapping("/list/{page}")
	public String salaryQuery(@PathVariable("page") long page,
			@RequestParam(value = "batchnum", required = false, defaultValue = "") String batchnum,
			@RequestParam(value = "branch",required = false,defaultValue = "0") long branchid,
			@RequestParam(value = "distributionmember",required = false,defaultValue = "") String distributionmember,
			@RequestParam(value = "idcard",required = false,defaultValue = "") String idcard,
			@RequestParam(value="isshow",defaultValue="0",required=false)long isshow,
			Model model){
		long count=0;
		List<SalaryGather> salaryGathers=new ArrayList<SalaryGather>();
		if (isshow>0) {
			salaryGathers=this.salaryGatherDao.findSalaryGathersWithQuery(page
					,salaryQueryService.checkParameterisNull(batchnum)
					,salaryQueryService.checkParameterisNull(distributionmember)
					,salaryQueryService.checkParameterisNull(idcard),branchid);
			count=this.salaryGatherDao.findSalaryGathersWithQuery(-9
					,salaryQueryService.checkParameterisNull(batchnum)
					,salaryQueryService.checkParameterisNull(distributionmember)
					,salaryQueryService.checkParameterisNull(idcard),branchid).size();
		}
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		List<Branch> branchList=this.branchDAO.getBranchsByContractflagAndSiteType(BranchEnum.ZhanDian.getValue(),"0");
		model.addAttribute("branchList", branchList);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("salaryGathers", salaryGathers);
		model.addAttribute("distributionmember", distributionmember);
		model.addAttribute("idcard", idcard);
		model.addAttribute("branchid", branchid);
		model.addAttribute("batchnum", batchnum);
		return "salary/salaryQuery/list";
	}
	//按照工资条导出与全部导出
	@RequestMapping("/exportExceldata/{flag}")
	public void  exceldatallExport(@PathVariable("flag")long flag,
			@RequestParam(value = "batchnum1", required = false, defaultValue = "") String batchnum,
			@RequestParam(value = "branch1",required = false,defaultValue = "0") long branchid,
			@RequestParam(value = "distributionmember1",required = false,defaultValue = "") String distributionmember,
			@RequestParam(value = "idcard1",required = false,defaultValue = "") String idcard,
			HttpServletResponse response
			){
		final String[] cloumnName;
		final String[] cloumnName3;
		String sheetName;
		String fileName;
		if (flag==0) {
			Integer length=SalaryGatherExport.getAllNames().split("_").length;
			//全部导出
			String[] cloumnName1 = new String[length]; // 导出的列名
			String[] cloumnName2 = new String[length]; // 导出的英文列名
			//SalaryGatherExport为导出需要的字段的类
			this.exportService.setSalaryAllExport(cloumnName1, cloumnName2,new SalaryGatherExport(),"SalaryGather");
			cloumnName = cloumnName1;
			cloumnName3 = cloumnName2;
			sheetName = "工资信息（全部导出）"; // sheet的名称
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			fileName = "SalaryAll_" + df.format(new Date()) + ".xlsx"; // 文件名
		}else {
			List<SalaryImport> salaryImports=salaryImportDao.getAllSalaryExports();
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
		List<SalaryGather> objList=this.salaryGatherDao.findSalaryGathersWithQuery(-9,salaryQueryService.checkParameterisNull(batchnum),salaryQueryService.checkParameterisNull(distributionmember),salaryQueryService.checkParameterisNull(idcard),branchid);;
		ExcelUtilsHandler.exportExcelHandler(response, cloumnName, cloumnName3, sheetName, fileName, objList);
	}
}
