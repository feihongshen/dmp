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
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "reasontype", required = false, defaultValue = "0") long reasontype) {
		model.addAttribute("reasonList", reasonDao.getReasonByPage(page, reasontype));
		model.addAttribute("page_obj", new Page(reasonDao.getReasonCount(reasontype), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "reason/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long reasonid, Model model) {
		model.addAttribute("reason", reasonDao.getReasonByReasonid(reasonid));
		return "reason/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @PathVariable("id") long reasonid, @RequestParam("reasoncontent") String reasoncontent,
			@RequestParam("reasontype") long reasontype) {

		List<Reason> list = reasonDao.getReasonByReasoncontent(reasoncontent);
		if (list.size() > 0 && reasontype == list.get(0).getReasontype()) {
			return "{\"errorCode\":1,\"error\":\"该文字已存在\"}";
		}
		Reason reason1 = new Reason();
		reason1.setReasoncontent(request.getParameter("reasoncontent"));
		reason1.setReasontype(Integer.parseInt(request.getParameter("reasontype")));
		reason1.setReasonid(reasonid);
		reasonDao.saveReason(reason1);
		logger.info("operatorUser={},常用语管理->save", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request, @RequestParam("reasoncontent") String reasoncontent, @RequestParam("reasontype") long reasontype) {

		List<Reason> list = reasonDao.getReasonByReasoncontent(reasoncontent);
		if (list.size() > 0 && reasontype == list.get(0).getReasontype()) {
			return "{\"errorCode\":1,\"error\":\"该文字已存在\"}";
		}
		Reason reason = new Reason();
		reason.setReasoncontent(request.getParameter("reasoncontent"));
		reason.setReasontype(Long.parseLong(request.getParameter("reasontype")));
		reasonDao.creReason(reason);
		logger.info("operatorUser={},常用语管理->create", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"新建成功\"}";
	}

}
