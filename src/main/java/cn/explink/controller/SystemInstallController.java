package cn.explink.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.SystemInstallService;
import cn.explink.service.UserService;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/systeminstall")
public class SystemInstallController {

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	SystemInstallService systemInstallService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
//	@Autowired
//	Scheduler schedulerFactory;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		return "/systeminstall/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, @RequestParam("chinesename") String chinesename, @RequestParam("name") String name, @RequestParam("value") String value)
			throws Exception {
		SystemInstall cs = systemInstallDAO.getSystemInstallByParam(chinesename, name, value);
		if (cs != null) {
			return "{\"errorCode\":1,\"error\":\"该设置已存在\"}";
		} else {
			systemInstallService.creSystemInstall(chinesename, name, value);
			logger.info("operatorUser={},系统 设置->create", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		}
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, HttpServletRequest request, @RequestParam(value = "value", required = false, defaultValue = "") String value,
			@RequestParam(value = "chinesename", required = false, defaultValue = "") String chinesename) throws Exception {
		chinesename = chinesename.replace("'", "");
		value = value.replace("'", "");
		model.addAttribute("siList", systemInstallDAO.getSystemInstallByWhere(page, chinesename, value));
		model.addAttribute("page_obj", new Page(systemInstallDAO.getSystemInstallCount(chinesename, value), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/systeminstall/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") long id) {
		model.addAttribute("systemInstall", systemInstallDAO.getSystemInstallById(id));
		return "/systeminstall/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") long id, @RequestParam("chinesename") String chinesename, @RequestParam("name") String name, @RequestParam("value") String value)
			throws Exception {
		SystemInstall cs = systemInstallDAO.getSystemInstallById(id);
		if (cs == null) {
			return "{\"errorCode\":1,\"error\":\"该设置不存在\"}";
		} else {
			systemInstallService.saveSystemInstall(chinesename, name, value, id);
			if ("siteDayLogTime".equals(name) || "wareHouseDayLogTime".equals(name) || "tuiHuoDayLogTime".equals(name)) {
				setQuartzTime(value, name);
			}
			logger.info("operatorUser={},系统 设置->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") long id) throws Exception {
		systemInstallDAO.delSystemInstallById(id);
		logger.info("operatorUser={},系统 设置->del", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

	/**
	 * 重置Quartz时间
	 * 
	 * @param timevalue
	 * @param beanId
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public void setQuartzTime(String timevalue, String beanId) throws SchedulerException, ParseException {
		/*CronTriggerBean trigger = (CronTriggerBean) schedulerFactory.getTrigger(beanId, Scheduler.DEFAULT_GROUP);
		if (trigger != null) {
			trigger.setCronExpression(StringUtil.getTimeToQuartzHHMMss(timevalue));
			schedulerFactory.rescheduleJob(beanId, Scheduler.DEFAULT_GROUP, trigger);
		}*/
	}

}