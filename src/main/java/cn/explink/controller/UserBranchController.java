package cn.explink.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserBranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.domain.UserBranch;
import cn.explink.service.CwbRouteService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.UserBranchService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/userBranchControl")
public class UserBranchController {

	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserBranchDAO userBranchDAO;
	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	UserBranchService userBranchService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		model.addAttribute("userlist", userDAO.getAllUserOrderByBranchid());
		return "userbranch/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(@RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchids,
			@RequestParam(value = "userid", required = false, defaultValue = "") String[] userids, HttpServletRequest request) throws Exception {
		return userBranchService.create(branchids, userids);
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "userid", required = false, defaultValue = "0") long userid) {

		model.addAttribute("userlist", userDAO.getAllUserOrderByBranchid());
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());

		List<UserBranch> useridList = userBranchDAO.getUserBranchGroupByUserId(page, branchid, userid);
		Map<String, List<UserBranch>> mapList = new HashMap<String, List<UserBranch>>();

		for (UserBranch ft : useridList) {
			mapList.put(ft.getUserid() + "List", userBranchDAO.getUserBranchByUserId(ft.getUserid()));
		}
		model.addAttribute("mapList", mapList);
		model.addAttribute("userbranchList", useridList);
		model.addAttribute("page_obj", new Page(userBranchDAO.getUserBranchCountGroupByUserid(branchid, userid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "userbranch/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("userlist", userDAO.getAllUserOrderByBranchid());
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		model.addAttribute("userBranchList", userBranchDAO.getUserBranchByUserId(id));
		model.addAttribute("userid", id);
		return "userbranch/edit";
	}

	@RequestMapping("/save/{userid}")
	public @ResponseBody String save(@PathVariable("userid") int userid, @RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchids) throws Exception {

		userBranchDAO.deleteUserBranchByUserId(userid);// 执行删除原有的
		if (branchids != null && branchids.length > 0) {
			for (String branchid : branchids) {
				List<UserBranch> ublist = userBranchDAO.getUserBranchByWheresql(Long.parseLong(branchid), userid);
				if (ublist.size() > 0) {
					continue;
				} else {// 新建新设置的
					UserBranch userBranch = new UserBranch();
					userBranch.setBranchid(Long.parseLong(branchid));
					userBranch.setUserid(userid);
					userBranchDAO.creUserBranch(userBranch);
					logger.info("operatorUser={},用户区域权限设置->save", getSessionUser().getUsername());
				}
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"请选择区域站点\"}";
		}
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") int id) throws Exception {
		userBranchDAO.deleteUserBranchByUserId(id);
		logger.info("operatorUser={},用户区域权限设置->del", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/addbybranch")
	public String addbybranch(Model model) throws Exception {
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		model.addAttribute("userlist", userDAO.getAllUserOrderByBranchid());
		return "userbranch/addbybranch";
	}

	@RequestMapping("/createbybranch")
	public @ResponseBody String createbybranch(@RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchids,
			@RequestParam(value = "userid", required = false, defaultValue = "") String[] userids, HttpServletRequest request) throws Exception {
		return userBranchService.createbybranch(branchids, userids);
	}

	@RequestMapping("/listbybranch/{page}")
	public String listbybranch(@PathVariable("page") long page, Model model, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "userid", required = false, defaultValue = "0") long userid) {
		model.addAttribute("userlist", userDAO.getAllUserOrderByBranchid());
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());

		List<UserBranch> branchidList = userBranchDAO.getUserBranchGroupByBranchid(page, branchid, userid);
		Map<String, List<UserBranch>> mapList = new HashMap<String, List<UserBranch>>();

		for (UserBranch ub : branchidList) {
			mapList.put(ub.getBranchid() + "List", userBranchDAO.getUserBranchByBranchid(ub.getBranchid()));
		}
		model.addAttribute("mapList", mapList);
		model.addAttribute("userbranchList", branchidList);
		model.addAttribute("page_obj", new Page(userBranchDAO.getUserBranchCountGroupByBranchid(branchid, userid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "userbranch/listbybranch";
	}

	@RequestMapping("/editbybranch/{branchid}")
	public String editbybranch(Model model, @PathVariable("branchid") int branchid) {
		model.addAttribute("userlist", userDAO.getAllUserOrderByBranchid());
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		model.addAttribute("userBranchList", userBranchDAO.getUserBranchByBranchid(branchid));
		model.addAttribute("branchid", branchid);
		return "userbranch/editbybranch";
	}

	@RequestMapping("/savebybranch/{branchid}")
	public @ResponseBody String savebybranch(@PathVariable("branchid") int branchid, @RequestParam(value = "userid", required = false, defaultValue = "") String[] userids) throws Exception {

		userBranchDAO.deleteUserBranchByBranchid(branchid);// 执行删除原有的
		if (userids != null && userids.length > 0) {
			for (String userid : userids) {
				List<UserBranch> ublist = userBranchDAO.getUserBranchByWheresql(branchid, Long.parseLong(userid));
				if (ublist.size() > 0) {
					continue;
				} else {// 新建新设置的
					UserBranch userBranch = new UserBranch();
					userBranch.setBranchid(branchid);
					userBranch.setUserid(Long.parseLong(userid));
					userBranchDAO.creUserBranch(userBranch);
					logger.info("operatorUser={},用户区域权限设置->按机构设置save", getSessionUser().getUsername());
				}
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"请选择用户\"}";
		}
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

	@RequestMapping("/delbybranch/{branchid}")
	public @ResponseBody String delbybranch(@PathVariable("branchid") int branchid) throws Exception {
		userBranchDAO.deleteUserBranchByBranchid(branchid);
		logger.info("operatorUser={},用户区域权限设置->按机构设置del", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

}