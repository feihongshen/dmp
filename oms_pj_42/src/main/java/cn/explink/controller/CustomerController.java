package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Customer;
import cn.explink.service.CustomerService;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@RequestMapping("/customer")
@Controller
public class CustomerController {

	@Autowired
	CustomerDAO customerDao;
	@Autowired
	CustomerService customerService;

	@RequestMapping("/customernameCheck")
	public @ResponseBody boolean customernameCheck(@RequestParam("cname") String cname) throws Exception {
		cname = new String(cname.getBytes("ISO8859-1"), "utf-8");
		List<Customer> customerList = customerDao.getCustomerByCustomernameCheck(cname);
		return customerList.size() == 0;
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "customername", required = false, defaultValue = "") String customername,
			@RequestParam(value = "showMessage", required = false, defaultValue = "") String showMessage) {

		model.addAttribute("customerList", customerDao.getCustomerByPage(page, customername));
		model.addAttribute("page_obj", new Page(customerDao.getCustomerCount(customername), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "customer/list";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request) {
		String customername = StringUtil.nullConvertToEmptyString(request.getParameter("customername"));
		List<Customer> list = customerDao.getCustomerByCustomername(customername);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"供货商已存在\"}";
		} else {
			Customer customer = customerService.loadFormForCustomer(request);
			customerDao.creCustomer(customer);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long customerid, Model model, HttpServletRequest request) {
		model.addAttribute("customer", customerDao.getCustomerById(customerid));
		return "customer/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @PathVariable("id") long customerid) {
		String customername = StringUtil.nullConvertToEmptyString(request.getParameter("customername"));
		List<Customer> list = customerDao.getCustomerByCustomername(customername);
		if (list.size() > 0 && list.get(0).getCustomerid() != customerid) {
			return "{\"errorCode\":1,\"error\":\"供货商已存在\"}";
		} else {
			Customer customer = customerService.loadFormForCustomer(request, customerid);
			customerDao.save(customer);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") long customerid) {
		customerDao.delCustomer(customerid);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

}
