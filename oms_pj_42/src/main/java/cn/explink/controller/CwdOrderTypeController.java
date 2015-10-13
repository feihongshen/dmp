package cn.explink.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CwbOrderTypeDAO;
import cn.explink.domain.ImportCwbOrderType;
import cn.explink.service.CwbOrderTypeService;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/cwbordertype")
public class CwdOrderTypeController {
	@Autowired
	CwbOrderTypeDAO cwbOrderTypeDAO;
	@Autowired
	CwbOrderTypeService cwbOrderTypeService;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "cwbordertype", required = false, defaultValue = "0") int cwbordertype) {
		model.addAttribute("importCwbOrderTypes", cwbOrderTypeDAO.getCwbOrderTypeByPage(page, cwbordertype));
		model.addAttribute("page_obj", new Page(cwbOrderTypeDAO.getCwbOrderTypeConut(cwbordertype), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		if (cwbordertype > 0) {
			model.addAttribute("importCwbOrderTypes", cwbOrderTypeDAO.getCwbOrderTypeByPage(page, cwbordertype));
			model.addAttribute("page_obj", new Page(cwbOrderTypeDAO.getCwbOrderTypeConut(cwbordertype), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		}
		return "cwbordertype/list";
	}

	@RequestMapping("/add")
	public String add() {
		return "cwbordertype/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String cre(Model model, HttpServletRequest request) {
		String importtype = StringUtil.nullConvertToEmptyString(request.getParameter("importtype"));
		List<ImportCwbOrderType> list = cwbOrderTypeDAO.getImportType(importtype);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"对应文字已存在\"}";
		} else {
			ImportCwbOrderType importCwbOrderType = cwbOrderTypeService.loadFormForCwbOrderType(request);
			cwbOrderTypeDAO.creImportCwbOrderType(importCwbOrderType);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}
	}

	@RequestMapping("/importtypecheck")
	public @ResponseBody boolean branchnamecheck(@RequestParam("importtype") String importtype) throws Exception {
		importtype = new String(importtype.getBytes("ISO8859-1"), "utf-8");
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * from express_set_importset where importsetflag=1 and importtype=?", importtype);
		if (list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping("/del/{importid}")
	public @ResponseBody String delete(Model model, HttpServletRequest request, @PathVariable("importid") long importid) {
		cwbOrderTypeDAO.delImportCwbOrderType(importid);
		return "{\"errorCode\":0}";

	}

	@RequestMapping("/edit/{importid}")
	public String edit(@PathVariable("importid") long importid, Model model, HttpServletRequest request) {
		model.addAttribute("importCwbOrderType", cwbOrderTypeDAO.getImportCwbOrderTypeByimportid(importid));
		return "cwbordertype/edit";
	}

	@RequestMapping("/save/{importid}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @PathVariable("importid") long importid) {
		String importtype = StringUtil.nullConvertToEmptyString(request.getParameter("importtype"));
		List<ImportCwbOrderType> list = cwbOrderTypeDAO.getImportType(importtype);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"对应文字已存在\"}";
		} else {
			ImportCwbOrderType importCwbOrderType = cwbOrderTypeService.loadFormForCwbOrderTypeToEdit(request, importid);
			cwbOrderTypeDAO.saveImportCwbOrderType(importCwbOrderType);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}

	}

}
