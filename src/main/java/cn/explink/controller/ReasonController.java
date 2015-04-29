package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.ReasonDao;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

@Controller
@RequestMapping("/reason")
public class ReasonController {

	@Autowired
	ReasonDao reasonDao;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(
			Model model,
			@PathVariable("page") long page,
			@RequestParam(value = "reasontype", required = false, defaultValue = "0") long reasontype) {
		model.addAttribute("reasonList",
				reasonDao.getReasonByPage(page, reasontype));
		model.addAttribute("page_obj",
				new Page(reasonDao.getReasonCount(reasontype), page,
						Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "reason/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long reasonid, Model model) {
		model.addAttribute("reason", reasonDao.getReasonByReasonid(reasonid));
		return "reason/edit";
	}

	@RequestMapping("/add")
	public String add(HttpServletRequest request, Model model) {
		request.setAttribute("reasonList", reasonDao.add());
		return "reason/add";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody
	String save(
			Model model,
			HttpServletRequest request,
			@PathVariable("id") long reasonid,
			@RequestParam(value = "reasoncontent", defaultValue = "", required = false) String reasoncontent) {

		List<Reason> list = reasonDao.getReasonByReasoncontent(reasoncontent);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该文字已存在\"}";
		}
		Reason reason = new Reason();
		reason.setReasoncontent(reasoncontent);
		reason.setReasonid(reasonid);
		reasonDao.saveReason(reason);
		logger.info("operatorUser={},常用语管理->save", getSessionUser()
				.getUsername());
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/create")
	public @ResponseBody
	String create(
			Model model,
			HttpServletRequest request,
			@RequestParam(value = "reasoncontent", defaultValue = "", required = false) String reasoncontent,
			@RequestParam(value = "reasontype", defaultValue = "0", required = false) long reasontype,
			@RequestParam(value = "whichreason", defaultValue = "0", required = false) int whichreason,
			@RequestParam(value = "parentid", defaultValue = "0", required = false) int parentid,
			@RequestParam(value = "changealowflag", defaultValue = "0", required = false) int changealowflag) {

		List<Reason> list = reasonDao.getReasonByReasoncontentAndParentid(reasoncontent,parentid);
		if (list.size() > 0&& reasontype == list.get(0).getReasontype()) {
			return "{\"errorCode\":1,\"error\":\"该文字已存在\"}";
		}
		Reason reason = new Reason();
		reason.setReasoncontent(reasoncontent);
		reason.setReasontype(reasontype);
		reason.setWhichreason(0);
		reason.setChangealowflag(changealowflag);
		if (reasontype == 1) {
			reason.setWhichreason(1);
			if (whichreason == 2) {
				reason.setParentid(parentid);
				reason.setWhichreason(2);
			}
		}

		reasonDao.creReason(reason);

		logger.info("operatorUser={},常用语管理->create", getSessionUser()
				.getUsername());
		return "{\"errorCode\":0,\"error\":\"新建成功\"}";
	}

}
