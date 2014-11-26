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
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.AccountAreaDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Customer;
import cn.explink.schedule.Constants;
import cn.explink.service.CustomerService;
import cn.explink.service.ScheduledTaskService;
import cn.explink.service.SystemInstallService;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@RequestMapping("/customer")
@Controller
public class CustomerController {

	@Autowired
	CustomerDAO customerDao;
	@Autowired
	CustomerService customerService;
	@Autowired
	AccountAreaDAO accountAreaDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	ScheduledTaskService scheduledTaskService;
	@Autowired
	SystemInstallService systemInstallService;

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
		String code = StringUtil.nullConvertToEmptyString(request.getParameter("customercode"));
		List<Customer> list = customerDao.getCustomerByCustomername(customername, code);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"供货商已存在或编码重复\"}";
		} else {
			Customer customer = customerService.loadFormForCustomer(request, null);
			customerDao.creCustomer(customer);
			list = customerDao.getCustomerByCustomername(customername, code);
			customer = list.get(0);
			/* add by wangych address syn */
			// TODO 增加同步代码
			String adressenabled = systemInstallService.getParameter("newaddressenabled");
			if (adressenabled != null && adressenabled.equals("1")) {
				scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_CUSTOMER_CREATE, Constants.REFERENCE_TYPE_CUSTOMER_ID, String.valueOf(customer.getCustomerid()), true);
			}
			customerService.initCustomerList();
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}
	}

	@RequestMapping("/createFile")
	public @ResponseBody String createFile(@RequestParam(value = "Filedata", required = false) MultipartFile file, Model model, HttpServletRequest request) {
		String customername = StringUtil.nullConvertToEmptyString(request.getParameter("customername"));
		String code = StringUtil.nullConvertToEmptyString(request.getParameter("customercode"));
		List<Customer> list = customerDao.getCustomerByCustomername(customername, code);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"供货商已存在或编码重复\"}";
		} else {
			Customer customer = customerService.loadFormForCustomer(request, file);
			customerDao.creCustomer(customer);
			list = customerDao.getCustomerByCustomername(customername, code);
			customer = list.get(0);
			/* add by wangych address syn */
			// TODO 增加同步代码
			String adressenabled = systemInstallService.getParameter("newaddressenabled");
			if (adressenabled != null && adressenabled.equals("1")) {
				scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_CUSTOMER_CREATE, Constants.REFERENCE_TYPE_CUSTOMER_ID, String.valueOf(customer.getCustomerid()), true);
			}
			customerService.initCustomerList();
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
		String code = StringUtil.nullConvertToEmptyString(request.getParameter("customercode"));
		List<Customer> list = customerDao.getCustomerByCustomername(customername, code, customerid);
		String oldcustomername = customerDao.getCustomerById(customerid).getCustomername();
		if (list.size() > 0 && list.get(0).getCustomerid() != customerid) {
			return "{\"errorCode\":1,\"error\":\"供货商已存在或编码重复\"}";
		} else {
			Customer customer = customerService.loadFormForCustomer(request, null, customerid);
			customerDao.save(customer);
			/* add by wangych address syn */
			// TODO 增加同步代码
			String adressenabled = systemInstallService.getParameter("newaddressenabled");
			if (adressenabled != null && adressenabled.equals("1")) {
				if (!oldcustomername.equals(customer.getCustomername())) {
					scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_CUSTOMER_MODIFY, Constants.REFERENCE_TYPE_CUSTOMER_ID, String.valueOf(customerid), true);
				}
			}
			customerService.initCustomerList();
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}
	}

	@RequestMapping("/saveFile/{id}")
	public @ResponseBody String saveFile(@RequestParam(value = "Filedata", required = false) MultipartFile file, Model model, HttpServletRequest request, @PathVariable("id") long customerid) {
		String customername = StringUtil.nullConvertToEmptyString(request.getParameter("customername"));
		String code = StringUtil.nullConvertToEmptyString(request.getParameter("customercode"));
		List<Customer> list = customerDao.getCustomerByCustomername(customername, code, customerid);
		String oldcustomername = customerDao.getCustomerById(customerid).getCustomername();
		if (list.size() > 0 && list.get(0).getCustomerid() != customerid) {
			return "{\"errorCode\":1,\"error\":\"供货商已存在或编码重复\"}";
		} else {
			Customer customer = customerService.loadFormForCustomer(request, file, customerid);
			customerDao.save(customer);
			/* add by wangych address syn */
			// TODO 增加同步代码
			String adressenabled = systemInstallService.getParameter("newaddressenabled");
			if (adressenabled != null && adressenabled.equals("1")) {
				if (!oldcustomername.equals(customer.getCustomername())) {
					scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_CUSTOMER_MODIFY, Constants.REFERENCE_TYPE_CUSTOMER_ID, String.valueOf(customerid), true);
				}
			}
			customerService.initCustomerList();
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") long customerid) {
		customerDao.delCustomer(customerid);
		/* add by wangych address syn */
		// TODO 增加同步代码
		String adressenabled = systemInstallService.getParameter("newaddressenabled");
		if (adressenabled != null && adressenabled.equals("1")) {
			long ifeffectflag = customerDao.getCustomerById(customerid).getIfeffectflag();
			if (ifeffectflag == 0) {
				scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_CUSTOMER_DELETE, Constants.REFERENCE_TYPE_CUSTOMER_ID, String.valueOf(customerid), true);
			} else {
				scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_CUSTOMER_CREATE, Constants.REFERENCE_TYPE_CUSTOMER_ID, String.valueOf(customerid), true);
			}
		}
		accountAreaDAO.delAccountArea(customerid);
		customWareHouseDAO.delWarehouse(customerid);
		customerService.initCustomerList();
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

}
