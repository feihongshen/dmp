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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.SalaryFixedDAO;
import cn.explink.domain.SalaryFixed;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
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

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page,
			@RequestParam(value = "realname", required = false, defaultValue = "") String realname,
			@RequestParam(value = "idcard",required = false,defaultValue = "") String idcard,
			@RequestParam(value = "isnow",required = false,defaultValue = "0") int isnow,
			Model model) {
		List<SalaryFixed> salaryList=this.salaryDAO.getSalaryByRealnameAndIdcard(page, realname, idcard);
		int count= this.salaryDAO.getSalaryByRealnameAndIdcardCounts(realname, idcard);
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("salaryList", salaryList);
		model.addAttribute("realname", realname);
		model.addAttribute("idcard", idcard);
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
		if (excelExtractor != null) {
			this.processFile(excelExtractor, inputStream);

		} else {
			return "redirect:list/1";
		}

		return "redirect:list/1";
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

	protected void processFile(ExcelExtractor excelExtractor, InputStream inputStream) {
		excelExtractor.extractSalary(inputStream);

	}
	@Test
	public void test() {
		Class class1 = SalaryFixed.class;
		Field[] fields=class1.getDeclaredFields();
		for (int i = 1; i < fields.length; i++) {
			System.out.println("ps.setBigDecimal("+(i)+",salary.get"+fields[i].getName().substring(0,1).toUpperCase()+fields[i].getName().substring(1)+"());");
		}
	}
}
