package cn.explink.controller;

import java.util.List;
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
import cn.explink.dao.ParameterDetailDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.ParameterDetail;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ParameterDetailService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/parameterDetail")
public class ParameterDetailController {

	@Autowired
	ParameterDetailService parameterDetailService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	ParameterDetailDAO parameterDetailDAO;
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
		return "/parameterdetail/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, @RequestParam(value = "flowordertype", required = false, defaultValue = "0") int flowordertype,
			@RequestParam(value = "filed", required = false, defaultValue = "") String filed) throws Exception {
		List<ParameterDetail> pdlist = parameterDetailDAO.getParameterDetail(flowordertype, filed);
		if (pdlist.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该设置已存在\"}";
		} else {
			ParameterDetail pd = parameterDetailService.loadFormForBranch(request);
			parameterDetailDAO.creParameterDetail(pd);
			logger.info("operatorUser={},操作设置->create", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		}
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "flowordertype", required = false, defaultValue = "0") int flowordertype,
			@RequestParam(value = "filed", required = false, defaultValue = "") String filed) {
		model.addAttribute("pdList", parameterDetailDAO.getParameterDetailByWhere(page, flowordertype, filed));
		model.addAttribute("page_obj", new Page(parameterDetailDAO.getParameterDetailCount(flowordertype, filed), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/parameterdetail/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") long id) {
		model.addAttribute("pd", parameterDetailDAO.getParameterDetailById(id));
		return "/parameterdetail/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(HttpServletRequest request, @PathVariable("id") int id, Model model,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") int flowordertype, @RequestParam(value = "filed", required = false, defaultValue = "") String filed)
			throws Exception {
		List<ParameterDetail> pdlist = parameterDetailDAO.getParameterDetail(flowordertype, filed);
		if (pdlist.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该设置已存在\"}";
		} else {
			ParameterDetail pd = parameterDetailService.loadFormForBranch(request);
			parameterDetailDAO.saveParameterDetail(id, flowordertype, filed, pd.getName());
			logger.info("operatorUser={},操作设置->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") int id) throws Exception {
		parameterDetailDAO.delParameterDetail(id);
		logger.info("operatorUser={},操作设置->del", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

}