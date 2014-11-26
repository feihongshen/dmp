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

import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.UserService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/abnormalType")
public class AbnormalTypeController {

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	AbnormalTypeDAO abnormalTypeDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		return "/abnormaltype/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(@RequestParam(value = "name", defaultValue = "", required = false) String name) throws Exception {
		List<AbnormalType> at = abnormalTypeDAO.getAbnormalTypeByName(name);
		if (at.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该问题件类型已存在\"}";
		} else {
			abnormalTypeDAO.creAbnormalType(name);
			logger.info("operatorUser={},问题件类型->create", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		}
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "name", defaultValue = "", required = false) String name) {
		model.addAttribute("abnormalTypeList", abnormalTypeDAO.getAbnormalTypeByWhere(page, name));
		model.addAttribute("page_obj", new Page(abnormalTypeDAO.getAbnormalTypeCount(name), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/abnormaltype/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("abnormalType", abnormalTypeDAO.getAbnormalTypeById(id));
		return "/abnormaltype/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int id, Model model, @RequestParam("name") String name) throws Exception {

		List<AbnormalType> at = abnormalTypeDAO.getAbnormalTypeByName(name);
		if (at.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该问题件类型已存在\"}";
		} else {
			abnormalTypeDAO.saveAbnormalType(name, id);
			logger.info("operatorUser={},问题件类型设置->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") int id, Model model) throws Exception {
		abnormalTypeDAO.delAbnormalType(id);
		logger.info("operatorUser={},问题件类型设置->del", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

}