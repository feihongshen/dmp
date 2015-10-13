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

import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.util.Page;

@RequestMapping("/customerwarehouses")
@Controller
public class CustomerWareHouseController {

	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	JdbcTemplate jdbctemplate;
	@Autowired
	CustomerDAO customerDAO;

	@RequestMapping("/")
	public @ResponseBody List<CustomWareHouse> getCustomerWarehouses(@RequestParam("customerid") long customerid) {
		return customWareHouseDAO.getCustomWareHouseByCustomerid(customerid);
	}

	@RequestMapping("/customerwarehousecheck")
	public @ResponseBody boolean customerwarehousecheck(@RequestParam("customerwarehouse") String customerwarehouse, @RequestParam("customerid") long customerid) throws Exception {
		customerwarehouse = new String(customerwarehouse.getBytes("ISO8859-1"), "utf-8");
		List<CustomWareHouse> list = customWareHouseDAO.getWarehouseByCustomerid(customerwarehouse, customerid);
		return list.size() == 0;
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid) {
		model.addAttribute("warehouses", customWareHouseDAO.getWarehouseByPage(page, customerid));
		model.addAttribute("customers", customerDAO.getAllCustomers());
		model.addAttribute("page_obj", new Page(customWareHouseDAO.getWarehouseCount(customerid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/customerwarehouses/list";
	}

	@RequestMapping("/add")
	public String add(Model model, HttpServletRequest request) {
		model.addAttribute("customers", customerDAO.getAllCustomers());
		return "/customerwarehouses/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam("customerid") long customerid, @RequestParam("customerwarehouse") String customerwarehouse,
			@RequestParam("warehouseremark") String warehouseremark, HttpServletRequest request) {
		CustomWareHouse w = new CustomWareHouse();
		w.setCustomerid(customerid);
		w.setCustomerwarehouse(customerwarehouse);
		w.setWarehouseremark(warehouseremark);
		w.setIfeffectflag(1);
		List<CustomWareHouse> list = customWareHouseDAO.getCustomerWarehouse(customerwarehouse);
		for (int i = 0; i < list.size(); i++) {
			if (list.size() > 0 && customerid == list.get(i).getCustomerid()) {
				return "{\"errorCode\":1,\"error\":\"供货商仓库名称已存在\"}";
			}
		}
		customWareHouseDAO.creCustomer(w);
		return "{\"errorCode\":0,\"error\":\"新建成功\"}";
	}

	@RequestMapping("/edit/{warehouseid}")
	public String edit(Model model, @PathVariable("warehouseid") int warehouseid) {
		model.addAttribute("customerwarehouse", customWareHouseDAO.getWarehouseId(warehouseid));
		model.addAttribute("name", customerDAO.getCustomer(customWareHouseDAO.getWarehouseId(warehouseid).getCustomerid()));
		return "/customerwarehouses/edit";
	}

	@RequestMapping("/save/{warehouseid}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @PathVariable("warehouseid") long warehouseid, @RequestParam("customerwarehouse") String customerwarehouse,
			@RequestParam("warehouseremark") String warehouseremark) {
		CustomWareHouse warehouses = customWareHouseDAO.getWarehouseId(warehouseid);
		long customerid = warehouses.getCustomerid();
		CustomWareHouse w = new CustomWareHouse();
		w.setWarehouseid(warehouseid);
		w.setCustomerid(customerid);
		w.setCustomerwarehouse(customerwarehouse);
		w.setWarehouseremark(warehouseremark);
		List<CustomWareHouse> list = customWareHouseDAO.getCustomerWarehouse(customerwarehouse);
		if (list.size() > 0 && warehouseid != list.get(0).getWarehouseid()) {
			return "{\"errorCode\":1,\"error\":\"供货商仓库名称已存在\"}";
		} else {
			customWareHouseDAO.getUpdateWarehouse(w);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}
	}

	@RequestMapping("del/{warehouseid}")
	public @ResponseBody String del(@PathVariable("warehouseid") long warehouseid) {
		customWareHouseDAO.getDelCustomerWarehouse(warehouseid);
		return "{\"errorCode\":0}";
	}
}
