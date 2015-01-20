package cn.explink.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.PunishTypeDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.PunishType;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.UserService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/punishType")
public class PunishTypeController {

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PunishTypeDAO punishTypeDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		return "/punishType/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(@RequestParam(value = "name", defaultValue = "", required = false) String name) throws Exception {
		List<PunishType> at = this.punishTypeDAO.getPunishTypeByName(name);
		if (at.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该扣罚类型已存在\"}";
		} else {
			this.punishTypeDAO.crePunishType(name);
			this.logger.info("operatorUser={},扣罚类型->create", this.getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		}
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "name", defaultValue = "", required = false) String name) {
		model.addAttribute("punishTypeList", this.punishTypeDAO.getPunishTypeByWhere(page, name));
		model.addAttribute("page_obj", new Page(this.punishTypeDAO.getPunishTypeCount(name), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/punishType/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("punishType", this.punishTypeDAO.getPunishTypeById(id));
		return "/punishType/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int id, Model model, @RequestParam("name") String name) throws Exception {

		List<PunishType> at = this.punishTypeDAO.getPunishTypeByName(name);
		if (at.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该扣罚类型已存在\"}";
		} else {
			this.punishTypeDAO.savePunishType(name, id);
			this.logger.info("operatorUser={},扣罚类型设置->save", this.getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") int id, Model model) throws Exception {
		this.punishTypeDAO.delPunishType(id);
		this.logger.info("operatorUser={},扣罚类型设置->del", this.getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

	@RequestMapping("/delData/{id}")
	public @ResponseBody String del(@PathVariable("id") long id) throws Exception {
		this.punishTypeDAO.delPunishTypeData(id);
		this.logger.info("operatorUser={},系统 设置->del", this.getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"删除成功\"}";
	}

}