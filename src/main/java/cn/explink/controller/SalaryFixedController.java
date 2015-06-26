/**
 *
 */
package cn.explink.controller;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.SalaryErrorDAO;
import cn.explink.dao.SalaryFixedDAO;
import cn.explink.dao.SalaryImportRecordDAO;
import cn.explink.domain.SalaryError;
import cn.explink.domain.SalaryFixed;
import cn.explink.domain.SalaryImportRecord;
import cn.explink.domain.User;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ResultCollectorManager;
import cn.explink.util.Page;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/salaryFixed")
public class SalaryFixedController {
	@Autowired
	Excel2007Extractor excel2007Extractor;
	@Autowired
	Excel2003Extractor excel2003Extractor;
	@Autowired
	ResultCollectorManager resultCollectorManager;
	@Autowired
	SalaryFixedDAO salaryDAO;
	@Autowired
	SalaryErrorDAO salaryErrorDAO;
	@Autowired
	SalaryImportRecordDAO salaryImportRecordDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page,
			@RequestParam(value = "realname", required = false, defaultValue = "") String realname,
			@RequestParam(value = "idcard",required = false,defaultValue = "") String idcard,
			@RequestParam(value = "isnow",required = false,defaultValue = "0") int isnow,
			@RequestParam(value = "importflag", required = false, defaultValue = "0") long importflag,
			Model model) {
		List<SalaryFixed> salaryList=this.salaryDAO.getSalaryByRealnameAndIdcard(page, realname, idcard);
		int count= this.salaryDAO.getSalaryByRealnameAndIdcardCounts(realname, idcard);
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		SalaryImportRecord salaryImportRecord=new SalaryImportRecord();
		if(importflag>0)
		{

			salaryImportRecord=this.salaryImportRecordDAO.getSalaryImportRecordByImportFlag(importflag);
		}

		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("salaryList", salaryList);
		model.addAttribute("realname", realname);
		model.addAttribute("idcard", idcard);
		model.addAttribute("record", salaryImportRecord);
		model.addAttribute("importflag", importflag);
		return "salary/salaryFixed/list";
	}
	@RequestMapping("/delete")
	public  @ResponseBody
	Map<String, Long> delete(@RequestParam(value = "ids",required = false,defaultValue = "") String ids) throws Exception {
		long counts=0;
		if((ids!=null)&&(ids.length()>0)){
		counts=this.salaryDAO.deleteSalaryByids(ids);
		}
		Map<String, Long> map=new HashMap<String, Long>();
		map.put("counts", counts);
		return map;
	}
	@RequestMapping("/importData")
	public String importData(Model model, final HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "Filedata", required = false) final MultipartFile file)
			throws Exception {
		final ExcelExtractor excelExtractor = this.getExcelExtractor(file);
		final InputStream inputStream = file.getInputStream();
		final User user=this.getSessionUser();
		final long importflag=System.currentTimeMillis();
		if (excelExtractor != null) {

			this.processFile(excelExtractor, inputStream,importflag,user);

		} else {
			return "redirect:list/1";
		}

		return "redirect:list/1?importflag=" + importflag;
	}
	private ExcelExtractor getExcelExtractor(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename.endsWith("xlsx")) {
			return this.excel2007Extractor;
		} else if (originalFilename.endsWith(".xls")) {
			return this.excel2003Extractor;
		}
		return null;
	}

	protected void processFile(ExcelExtractor excelExtractor, InputStream inputStream, long importflag,User user) {
		excelExtractor.extractSalary(inputStream,importflag,user);

	}
	@RequestMapping("/importFlagError/{id}")
	public String importFlagError(@PathVariable("id") long importflag, Model model) throws Exception {

		List<SalaryError> salaryErrorList= this.salaryErrorDAO.getSalaryErrorByImportflag(importflag);
		model.addAttribute("salaryError", salaryErrorList);
		return "/salary/salaryFixed/errorRecords";
	}
	@Test
	public void test() {
		Class class1 = SalaryFixed.class;
		Field[] fields=class1.getDeclaredFields();
		for (int i = 1; i < fields.length; i++) {
			System.out.println("salary.set"+fields[i].getName().substring(0,1).toUpperCase()+fields[i].getName().substring(1)+"(new BigDecimal(this.getXRowCellData(row, "+(i-1)+")));");
		}
	}
}
