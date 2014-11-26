package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
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

import cn.explink.dao.OperationRuleDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

@Controller
@RequestMapping("/operationRule")
public class OperationRuleController {

	@Autowired
	UserDAO userDAO;
	@Autowired
	OperationRuleDao operationRuleDAO;
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
		return "/operationrule/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, @RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "expression", required = false, defaultValue = "") String expression, @RequestParam(value = "result", required = false, defaultValue = "-1") int result,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") int flowordertype, @RequestParam(value = "order", required = false, defaultValue = "0") int order,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage) throws Exception {
		/*
		 * List<OperationRule> orlist = operationRuleDAO.getOperationRule(name,
		 * expression, result, flowordertype, order, errormessage,
		 * getSessionUser().getUserid(), getSessionUser().getUserid());
		 * if(orlist.size()>0){ return "{\"errorCode\":1,\"error\":\"该设置已存在\"}";
		 * }else{
		 */
		operationRuleDAO.creOperationRule(name, expression, result, flowordertype, order, errormessage, getSessionUser().getUserid(), getSessionUser().getUserid(), new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(new Date()));
		logger.info("operatorUser={},操作设置->create", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		/* } */
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "flowordertype", required = false, defaultValue = "0") int flowordertype) {
		model.addAttribute("userList", userDAO.getAllUser());
		model.addAttribute("orList", operationRuleDAO.getOperationRuleByPage(page, flowordertype));
		model.addAttribute("page_obj", new Page(operationRuleDAO.getOperationRuleCount(page, flowordertype), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/operationrule/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") long id) {
		model.addAttribute("operationRule", operationRuleDAO.getOperationRuleById(id));
		return "/operationrule/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(HttpServletRequest request, @PathVariable("id") int id, Model model, @RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "expression", required = false, defaultValue = "") String expression, @RequestParam(value = "result", required = false, defaultValue = "0") int result,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") int flowordertype, @RequestParam(value = "order", required = false, defaultValue = "0") int order,
			@RequestParam(value = "errormessage", required = false, defaultValue = "0") String errormessage) throws Exception {
		/*
		 * List<OperationRule> orlist = operationRuleDAO.getOperationRule(name,
		 * expression, result, flowordertype, order, errormessage, 0, 0);
		 * if(orlist.size()>0){ return "{\"errorCode\":1,\"error\":\"该设置已存在\"}";
		 * }else{
		 */
		operationRuleDAO.saveOperationRule(id, name, expression, result, flowordertype, order, errormessage, getSessionUser().getUserid());
		logger.info("operatorUser={},操作设置->save", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		/* } */
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") int id) throws Exception {
		operationRuleDAO.delOperationRule(id);
		logger.info("operatorUser={},操作设置->del", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

}