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

import cn.explink.dao.BranchDAO;
import cn.explink.dao.WarehouseKeyDAO;
import cn.explink.domain.WarehouseKey;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/warehousekey")
public class WarehouseKeyController {
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	WarehouseKeyDAO warehouseKeyDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "targetcarwarehouseid", required = false, defaultValue = "0") int targetcarwarehouseid) {
		model.addAttribute("kflist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		model.addAttribute("warehousekeys", warehouseKeyDAO.getWarehouseKeyByPage(page, targetcarwarehouseid));
		model.addAttribute("page_obj", new Page(warehouseKeyDAO.getWarehouseKeyConut(targetcarwarehouseid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousekey/list";
	}

	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("kflist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		return "warehousekey/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String cre(Model model, HttpServletRequest request) {
		long targetcarwarehouseid = Long.parseLong(request.getParameter("targetcarwarehouseid"));
		String keyname = StringUtil.nullConvertToEmptyString(request.getParameter("keyname"));
		List<WarehouseKey> list = warehouseKeyDAO.getWarehouseKeyByKeyname(keyname);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"对应关键字已存在\"}";
		} else {
			warehouseKeyDAO.creWarehouseKey(targetcarwarehouseid, keyname);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String delete(Model model, HttpServletRequest request, @PathVariable("id") long id) {
		warehouseKeyDAO.delWarehouseKey(id);
		return "{\"errorCode\":0}";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long id, Model model, HttpServletRequest request) {
		model.addAttribute("kflist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("warehouseKey", warehouseKeyDAO.getWarehouseKeyByid(id));
		return "warehousekey/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @PathVariable("id") long id) {
		long targetcarwarehouseid = Long.parseLong(request.getParameter("targetcarwarehouseid"));
		String keyname = StringUtil.nullConvertToEmptyString(request.getParameter("keyname"));

		List<WarehouseKey> list = warehouseKeyDAO.getWarehouseKeyByKeyname(keyname);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"对应关键字已存在\"}";
		} else {
			warehouseKeyDAO.saveWarehouseKeyByid(id, targetcarwarehouseid, keyname);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}

	}
}
