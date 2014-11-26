package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.NotifyDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Notify;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.NotifyService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/notify")
public class NotifyController {
	@Autowired
	NotifyDao notifyDao;
	@Autowired
	UserDAO userDAO;
	@Autowired
	NotifyService notifyService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();

	}

	@RequestMapping("/list/{page}")
	public String notifyIndex(@PathVariable("page") long page, Model model) {
		List<Notify> notifyList = notifyDao.getAllNotify(page);
		long count = notifyDao.getAllNotifyCount();
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("notifylist", notifyList);
		model.addAttribute("page", page);
		return "notify/list";
	}

	@RequestMapping("/add")
	public String add() {
		return "notify/add";
	}

	@RequestMapping("/cre")
	public @ResponseBody String cre(@RequestParam(value = "title", required = false, defaultValue = "") String title, @RequestParam(value = "type", required = false, defaultValue = "0") long type,
			@RequestParam(value = "content", required = false, defaultValue = "") String content) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowtime = sdf.format(new Date());
		notifyDao.creNotify(getSessionUser().getUserid(), title, type, content, nowtime);
		return "{\"error\":\"发布成功\"}";

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long id, Model model) {
		Notify nf = notifyDao.getNotifyById(id);
		model.addAttribute("nf", nf);
		return "notify/edit";
	}

	@RequestMapping("/save")
	public @ResponseBody String save(@RequestParam(value = "id") long id, @RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "type", required = false, defaultValue = "0") long type, @RequestParam(value = "content", required = false, defaultValue = "") String content) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String edittime = sdf.format(new Date());
		notifyDao.update(id, title, type, content, edittime, getSessionUser().getUserid());
		return "{\" error\":\"保存成功\"}";
	}

	@RequestMapping("/del/{ids}")
	public @ResponseBody String del(@PathVariable("ids") String ids) {
		// 删除与这条公告相关的 附件 图片
		try {
			notifyService.delByIds(ids);
			return "{\"error\":\"删除成功\"}";
		} catch (Exception e) {
			return "{\"error\":\"删除失败\"}";
		}

	}

	@RequestMapping("/managelist/{page}")
	public String managelist(@PathVariable("page") long page, Model model) {
		List<Notify> notifyList = notifyDao.getAllNotify(page);
		Map<Long, String> mapForUser = getUserMap();
		long count = notifyDao.getAllNotifyCount();
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("usermap", mapForUser);
		model.addAttribute("notifylist", notifyList);
		model.addAttribute("page", page);
		return "notify/managelist";
	}

	private Map<Long, String> getUserMap() {
		Map<Long, String> mapForUser = new HashMap<Long, String>();
		List<User> users = userDAO.getAllUser();
		if (users != null && users.size() > 0) {
			for (User user : users) {
				mapForUser.put(user.getUserid(), user.getRealname());
			}
		}
		return mapForUser;
	}

	@RequestMapping("/totop/{id}")
	public String totop(@PathVariable("id") long id) {
		notifyService.updateToTop(id);
		return "redirect:/notify/managelist/1";
	}

	@RequestMapping("/show/{id}")
	public String showsomeone(@PathVariable("id") long id, Model model) {
		Notify nf = notifyDao.getNotifyById(id);
		User user = userDAO.getUserByUserid(nf.getCreuserid());
		model.addAttribute("username", user == null ? "" : user.getRealname());
		model.addAttribute("nf", nf);
		return "notify/show";
	}

	@RequestMapping("/showmanage/{id}")
	public String showmanage(@PathVariable("id") long id, Model model) {
		Notify nf = notifyDao.getNotifyById(id);
		User user = userDAO.getUserByUserid(nf.getCreuserid());
		model.addAttribute("username", user == null ? "" : user.getRealname());
		model.addAttribute("nf", nf);
		return "notify/showmanage";
	}

}
