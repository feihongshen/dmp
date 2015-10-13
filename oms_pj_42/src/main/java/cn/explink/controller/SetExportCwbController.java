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

import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.domain.Exportmould;
import cn.explink.domain.Role;
import cn.explink.domain.SetExportField;
import cn.explink.service.ExportmouldService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/setexportcwb")
public class SetExportCwbController {

	@Autowired
	SetExportFieldDAO setExportFieldDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	ExportmouldService exportmouldService;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page) {
		List<Exportmould> exportmouldlist = exportmouldDAO.getExportmouldByPage(page);
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("page_obj", new Page(exportmouldDAO.getExportmouldCount(), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "setexportcwb/listM";
	}

	@RequestMapping("/add")
	public String setlist(Model model) {
		List<SetExportField> listSetExportField = setExportFieldDAO.getAllSetExportField();
		List<Role> roleList = getDmpDAO.getRoleList();
		model.addAttribute("listSetExportField", listSetExportField);
		model.addAttribute("roleList", roleList);
		return "setexportcwb/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request, @RequestParam("roleid") long roleid, @RequestParam("mouldname") String mouldname,
			@RequestParam(value = "fieldid", required = false, defaultValue = "") String fieldids) {
		List<Exportmould> listmould = exportmouldDAO.getExportmouldBymouldname(mouldname);
		if (listmould.size() > 0 && exportmouldService.checkRoleid(listmould, roleid)) {
			return "{\"errorCode\":0,\"error\":\"该模版已存在\"}";
		} else {
			String fieldid[] = {};
			String mouldfieldids = "";
			if (fieldids.length() > 0) {
				fieldid = fieldids.split(",");
			}
			if (fieldid.length > 0) {
				StringBuffer str = new StringBuffer();
				for (int i = 0; i < fieldid.length; i++) {
					str.append(fieldid[i] + ",");
				}
				mouldfieldids = str.substring(0, str.length() - 1).toString();
			}
			Role role = getDmpDAO.getRoleByRoleid(roleid);
			String rolename = role.getRolename();
			exportmouldDAO.creExportmould(roleid, rolename, mouldname, mouldfieldids);
		}
		return "{\"errorCode\":0,\"error\":\"新建成功\"}";
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(Model model, @PathVariable("id") long id) {
		exportmouldDAO.delExportmould(id);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") long id) {
		Exportmould exportmould = exportmouldDAO.getExportmouldById(id);
		List<SetExportField> listSetExportField = setExportFieldDAO.getAllSetExportField();
		List<Role> roleList = getDmpDAO.getRoleList();
		String field[] = exportmould.getMouldfieldids().split(",");
		model.addAttribute("exportmould", exportmould);
		model.addAttribute("listSetExportField", listSetExportField);
		model.addAttribute("roleList", roleList);
		model.addAttribute("field", field);
		return "setexportcwb/editM";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @RequestParam("roleid") long roleid, @RequestParam("mouldname") String mouldname, @PathVariable("id") long id,
			@RequestParam(value = "fieldid", required = false, defaultValue = "") String fieldids) {
		List<Exportmould> listmould = exportmouldDAO.getExportmouldByRoleidNoId(roleid, id);
		if (listmould.size() > 0 && exportmouldService.checkMouldname(listmould, mouldname)) {
			return "{\"errorCode\":0,\"error\":\"该模版已存在\"}";
		} else {
			String fieldid[] = {};
			String mouldfieldids = "";
			if (fieldids.length() > 0) {
				fieldid = fieldids.split(",");
			}
			if (fieldid.length > 0) {
				StringBuffer str = new StringBuffer();
				for (int i = 0; i < fieldid.length; i++) {
					str.append(fieldid[i] + ",");
				}
				mouldfieldids = str.substring(0, str.length() - 1).toString();
			}
			Role role = getDmpDAO.getRoleByRoleid(roleid);
			String rolename = role.getRolename();
			exportmouldDAO.editExportmould(roleid, rolename, mouldname, mouldfieldids, id);
		}
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/view/{id}")
	public String view(Model model, @PathVariable("id") long id) {
		Exportmould exportmould = exportmouldDAO.getExportmouldById(id);
		List<SetExportField> listSetExportField = setExportFieldDAO.getAllSetExportField();
		String field[] = exportmould.getMouldfieldids().split(",");
		model.addAttribute("exportmould", exportmould);
		model.addAttribute("listSetExportField", listSetExportField);
		model.addAttribute("field", field);
		return "setexportcwb/viewM";
	}

}
