package cn.explink.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.AccountAreaDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.AccountArea;
import cn.explink.util.Page;

@RequestMapping("/accountareas")
@Controller
public class AccountAreaController {

	@Autowired
	AccountAreaDAO accountAreaDAO;
	@Autowired
	CustomerDAO customerDAO;

	@RequestMapping("/")
	public @ResponseBody List<AccountArea> getCustomerWarehouses(@RequestParam("customerid") long customerid) {
		return accountAreaDAO.getAccountAreaByCustomerid(customerid);
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid) {
		model.addAttribute("accountAreaes", accountAreaDAO.getAccountAreaByPage(page, customerid));
		model.addAttribute("customeres", customerDAO.getAllCustomers());
		model.addAttribute("page_obj", new Page(accountAreaDAO.getAccountAreaCount(customerid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "accountareas/list";
	}

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("customeres", customerDAO.getAllCustomers());
		return "accountareas/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam("customerid") long customerid, @RequestParam("areaname") String areaname,
			@RequestParam(value = "arearemark", required = false, defaultValue = "") String arearemark) {
		List<AccountArea> list = accountAreaDAO.getAccountAreaByCustomeridAnd(customerid, areaname);

		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"供货商结算区域名称已存在\"}";
		} else {
			accountAreaDAO.creAccountArea(customerid, areaname, arearemark);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long areaid, Model model) {
		model.addAttribute("customeres", customerDAO.getAllCustomers());
		model.addAttribute("accountArea", accountAreaDAO.getAccountAreaById(areaid));
		return "accountareas/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") long areaid, Model model, @RequestParam("customerid") long customerid, @RequestParam("areaname") String areaname,
			@RequestParam(value = "arearemark", required = false, defaultValue = "") String arearemark) {
		List<AccountArea> list = accountAreaDAO.getAccountAreaByCustomeridAnd(customerid, areaname);
		if (list.size() > 0 && areaid != list.get(0).getAreaid()) {
			return "{\"errorCode\":1,\"error\":\"供货商结算区域名称已存在\"}";
		}
		accountAreaDAO.saveAccountArea(customerid, areaname, arearemark, areaid);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") long areaid) {
		accountAreaDAO.editAccountAreaIsEffectFlag(areaid);
		return "{\"errorCode\":0}";
	}
}
