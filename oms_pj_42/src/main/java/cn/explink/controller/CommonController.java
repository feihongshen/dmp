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

import cn.explink.dao.CommonDAO;
import cn.explink.domain.Common;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@RequestMapping("/common")
@Controller
public class CommonController {

	@Autowired
	CommonDAO commonDAO;

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "commonname", required = false, defaultValue = "") String commonname,
			@RequestParam(value = "commonnumber", required = false, defaultValue = "") String commonnumber) {
		List<Common> commonlist = commonDAO.getCommonByPage(page, commonname, commonnumber);
		model.addAttribute("commonList", commonlist);
		model.addAttribute("page_obj", new Page(commonDAO.getCommonCount(commonname, commonnumber), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/common/list";
	}

	@RequestMapping("/add")
	public String add(Model model) {
		return "/common/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request) {
		String commonname = StringUtil.nullConvertToEmptyString(request.getParameter("commonname"));
		String commonnumber = StringUtil.nullConvertToEmptyString(request.getParameter("commonnumber")).toUpperCase();
		String orderprefix = StringUtil.nullConvertToEmptyString(request.getParameter("orderprefix"));
		List<Common> listCommon = commonDAO.getCommonByCommonname(commonname);
		List<Common> commonList = commonDAO.getCommonByCommonnumber(commonnumber);
		if (listCommon.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该承运商已存在\"}";
		} else if (commonList.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"承运商编码不可重复\"}";
		} else {
			commonDAO.CreateCommon(commonname, commonnumber, orderprefix);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";

		}
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") long id) {
		model.addAttribute("common", commonDAO.getCommonById(id));
		return "/common/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @PathVariable("id") long id) {
		String commonname = StringUtil.nullConvertToEmptyString(request.getParameter("commonname"));
		String commonnumber = StringUtil.nullConvertToEmptyString(request.getParameter("commonnumber")).toUpperCase();
		String orderprefix = StringUtil.nullConvertToEmptyString(request.getParameter("orderprefix"));
		List<Common> listCommon = commonDAO.getCommonByCommonname(commonname);
		List<Common> commonList = commonDAO.getCommonByCommonnumber(commonnumber);

		if (listCommon.size() > 0 && listCommon.get(0).getId() != id) {
			return "{\"errorCode\":1,\"error\":\"该承运商已存在\"}";
		} else if (commonList.size() > 0 && commonList.get(0).getId() != id) {
			return "{\"errorCode\":1,\"error\":\"承运商编码不可重复\"}";
		} else {
			commonDAO.saveCommon(commonname, commonnumber, orderprefix, id);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(Model model, @PathVariable("id") long id) {
		commonDAO.delCommon(id);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

}
