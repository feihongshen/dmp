package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.dao.CustomWarHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.CustomWarHouse;
import cn.explink.util.Page;

@RequestMapping("/customerwarhouses")
@Controller
public class CustomerWarHouseController {

	@Autowired
	CustomWarHouseDAO customWarHouseDAO;
	@Autowired
	JdbcTemplate jdbctemplate;
	@Autowired
	CustomerDAO customerDAO;

	@RequestMapping("/")
	public @ResponseBody List<CustomWarHouse> getCustomerWarhouses(@RequestParam("customerid") long customerid) {
		return customWarHouseDAO.getCustomWarHouseByCustomerid(customerid);
	}

	@RequestMapping("/customerwarehousecheck")
	public @ResponseBody boolean customerwarehousecheck(@RequestParam("customerwarehouse") String customerwarehouse, @RequestParam("customerid") long customerid) throws Exception {
		customerwarehouse = new String(customerwarehouse.getBytes("ISO8859-1"), "utf-8");
		List<CustomWarHouse> list = customWarHouseDAO.getWarehouseByCustomerid(customerwarehouse, customerid);
		return list.size() == 0;
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid) {
		model.addAttribute("warehouses", customWarHouseDAO.getWarehouseByPage(page, customerid));
		model.addAttribute("customers", customerDAO.getAllCustomers());
		model.addAttribute("page_obj", new Page(customWarHouseDAO.getWarehouseCount(customerid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/customerwarhouses/list";
	}

	@RequestMapping("/add")
	public String add(Model model, HttpServletRequest request) {
		model.addAttribute("customers", customerDAO.getAllCustomers());
		return "/customerwarhouses/add";
	}

	@RequestMapping("/creat")
	public String creat(Model model, @RequestParam("customerid") long customerid, @RequestParam("customerwarehouse") String customerwarehouse, @RequestParam("warehouseremark") String warehouseremark,
			HttpServletRequest request) {
		CustomWarHouse w = new CustomWarHouse();
		w.setCustomerid(customerid);
		w.setCustomerwarehouse(customerwarehouse);
		w.setWarehouseremark(warehouseremark);
		w.setIfeffectflag(true);
		List<CustomWarHouse> list = customWarHouseDAO.getCustomerWarehouse(customerwarehouse);
		if (list.size() > 0) {
			model.addAttribute("errorState", "供货商仓库已存在");
			model.addAttribute("customerwarehouse", w);
		} else {
			customWarHouseDAO.creCustomer(w);
			model.addAttribute("warehouse", "保存成功");

		}
		model.addAttribute("customers", customerDAO.getAllCustomers());
		return "/customerwarhouses/add";
	}

	@RequestMapping("/edit/{warehouseid}")
	public String edit(Model model, @PathVariable("warehouseid") int warehouseid) {
		model.addAttribute("customerwarehouse", customWarHouseDAO.getWarehouseId(warehouseid));
		model.addAttribute("name", customerDAO.getCustomer(customWarHouseDAO.getWarehouseId(warehouseid).getCustomerid()));
		return "/customerwarhouses/edit";
	}

	@RequestMapping("/save/{warehouseid}")
	public String save(Model model, @PathVariable("warehouseid") long warehouseid, @RequestParam("customerwarehouse") String customerwarehouse, @RequestParam("warehouseremark") String warehouseremark) {
		CustomWarHouse warehouses = customWarHouseDAO.getWarehouseId(warehouseid);
		long customerid = warehouses.getCustomerid();
		CustomWarHouse w = new CustomWarHouse();
		w.setWarhouseid(warehouseid);
		w.setCustomerid(customerid);
		w.setCustomerwarehouse(customerwarehouse);
		w.setWarehouseremark(warehouseremark);
		List<CustomWarHouse> list = customWarHouseDAO.getCustomerWarehouse(customerwarehouse);
		if (list.size() > 0) {
			model.addAttribute("errorState", "供货商仓库名称已存在");
			model.addAttribute("customerwarehouse", w);
			model.addAttribute("name", customerDAO.getCustomer(customWarHouseDAO.getWarehouseId(warehouseid).getCustomerid()));
			return "/customerwarhouses/edit";
		} else {
			customWarHouseDAO.getUpdateWarehouse(w);
			model.addAttribute("customerwarehouse", customWarHouseDAO.getWarehouseId(warehouseid));
			model.addAttribute("name", customerDAO.getCustomer(customWarHouseDAO.getWarehouseId(warehouseid).getCustomerid()));
			model.addAttribute("editsave", "修改成功");
			return "/customerwarhouses/edit";
		}
	}

	@RequestMapping("del/{warehouseid}")
	public ModelAndView del(@PathVariable("warehouseid") long warehouseid) {
		customWarHouseDAO.getDelCustomerWarehouse(warehouseid);
		return new ModelAndView("redirect:/customerwarhouses/list/1");
	}
}
