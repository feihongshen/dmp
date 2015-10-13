package cn.explink.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.SetExcelColumnDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.ExcelColumnSet;
import cn.explink.service.ColumnService;

@RequestMapping("/excelcolumn")
@Controller
public class ExcelColumnController {

	@Autowired
	SetExcelColumnDAO setExcelColumnDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ColumnService columnService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	UserDAO userDAO;

	@RequestMapping("/list")
	public String list(Model model, HttpServletRequest request) {

		model.addAttribute("columns", setExcelColumnDAO.getExcelColumnAll());
		model.addAttribute("customers", customerDAO.getAllCustomers());

		return "/excelcolumn/list";
	}

	@RequestMapping("/view/{customerid}")
	public String view(Model model, @PathVariable("customerid") long customerid) {
		model.addAttribute("name", customerDAO.getCustomer(customerid));
		model.addAttribute("customerid", setExcelColumnDAO.getExcelColumnSetByCustomerid(customerid));
		return "/excelcolumn/view";
	}

	@RequestMapping("/add")
	public String add(Model model, HttpServletRequest request) {
		model.addAttribute("customers", customerDAO.getAllCustomers());
		model.addAttribute("customerName", setExcelColumnDAO.getExcelColumnAll());
		return "/excelcolumn/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request) throws Exception {
		ExcelColumnSet excelColumnSet = columnService.loadFormForColumn(request);
		columnService.addColumn(excelColumnSet);
		return "{\"errorCode\":0,\"error\":\"新建成功\"}";
	}

	@RequestMapping("/edit/{columnid}")
	public String edit(Model model, @PathVariable("columnid") long columnid) {
		model.addAttribute("e", setExcelColumnDAO.getColumnid(columnid));
		model.addAttribute("name", customerDAO.getCustomer(setExcelColumnDAO.getColumnid(columnid).getCustomerid()));
		return "/excelcolumn/edit";
	}

	@RequestMapping("/save/{columnid}")
	public @ResponseBody String save(Model model, @PathVariable("columnid") long columnid, HttpServletRequest request) {
		columnService.editColumn(columnService.loadFormForColumn(request, columnid));
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/del/{columnid}")
	public @ResponseBody String del(@PathVariable("columnid") long columnid) {
		setExcelColumnDAO.getDeleColumn(columnid);
		return "{\"errorCode\":0}";
	}

}
