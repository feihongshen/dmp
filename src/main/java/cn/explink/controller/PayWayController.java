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

import cn.explink.dao.PayWayDao;
import cn.explink.domain.PayWay;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/payway")
public class PayWayController {
	@Autowired
	PayWayDao payWayDao;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "paywayid", required = false, defaultValue = "0") int paywayid) {
		model.addAttribute("payWays", payWayDao.getPayWayByPage(page, paywayid));
		model.addAttribute("page_obj", new Page(payWayDao.getPayWayByConut(paywayid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "payway/list";
	}

	@RequestMapping("/count")
	public @ResponseBody long count() {
		return payWayDao.getPayWayByConut(0);
	}

	@RequestMapping("/add")
	public String add() {
		return "payway/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String cre(Model model, HttpServletRequest request) {
		String payway = StringUtil.nullConvertToEmptyString(request.getParameter("payway"));
		long paywayid = Integer.parseInt(request.getParameter("paywayid"));
		List<PayWay> list = payWayDao.getPayWayByPayway(payway);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"对应文字已存在\"}";
		} else {
			payWayDao.create(payway, paywayid);
			logger.info("operatorUser={},支付方式管理->create", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}
	}

	/*
	 * @RequestMapping("/importtypecheck") public @ResponseBody boolean
	 * branchnamecheck(@RequestParam("importtype") String importtype) throws
	 * Exception { importtype = new String(importtype.getBytes("ISO8859-1"),
	 * "utf-8"); List<Map<String, Object>> list = jdbcTemplate.queryForList(
	 * "SELECT * from express_set_importset where importsetflag=1 and importtype=?"
	 * , importtype); if (list.size() == 0) { return true; } else { return
	 * false; } }
	 */

	@RequestMapping("/del/{id}")
	public @ResponseBody String delete(Model model, HttpServletRequest request, @PathVariable("id/") long id) {
		payWayDao.delPayWayById(id);
		logger.info("operatorUser={},支付方式管理->del", getSessionUser().getUsername());
		return "{\"errorCode\":0}";

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long id, Model model, HttpServletRequest request) {
		model.addAttribute("payway", payWayDao.getPayWayByid(id));
		return "payway/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @PathVariable("id") long id) {
		String payway = StringUtil.nullConvertToEmptyString(request.getParameter("payway"));
		long paywayid = Long.parseLong(StringUtil.nullConvertToEmptyString(request.getParameter("paywayid").toString()));

		List<PayWay> list = payWayDao.getPayWayByParm(payway, paywayid);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"对应文字已存在\"}";
		} else {
			payWayDao.savePayWayById(id, payway, paywayid);
			logger.info("operatorUser={},支付方式管理->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}

	}

}
