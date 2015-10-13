package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.UserService;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/usercheck")
	public @ResponseBody boolean usercheck(@RequestParam("username") String username) throws Exception {
		List<User> list = userDAO.getUsersByUsername(username);
		return list.size() == 0;
	}

	@RequestMapping("/userrealnamecheck")
	public @ResponseBody boolean userrealnameCheck(@RequestParam("realname") String realname) throws Exception {
		realname = new String(realname.getBytes("ISO8859-1"), "utf-8");
		List<User> list = userDAO.getUsersByRealname(realname);
		return list.size() == 0;
	}

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("branches", branchDAO.getAllBranches());
		model.addAttribute("roles", roleDAO.getRoles());
		return "user/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam("branchid") long branchid, @RequestParam("roleid") long roleid, HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list = userDAO.getUsersByRealname(realname);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"员工姓名已存在\"}";
		} else {
			list = userDAO.getUsersByUsername(username);
			if (list.size() > 0) {
				return "{\"errorCode\":1,\"error\":\"员工登录名已存在\"}";
			} else {
				User user = userService.loadFormForUser(request, roleid, branchid, null);
				userService.addUser(user);
				return "{\"errorCode\":0,\"error\":\"创建成功\"}";
			}
		}
	}

	@RequestMapping("/createFile")
	public @ResponseBody String createFile(Model model, @RequestParam(value = "Filedata", required = false) MultipartFile file, @RequestParam("branchid") long branchid,
			@RequestParam("roleid") long roleid, HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list = userDAO.getUsersByRealname(realname);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"员工姓名已存在\"}";
		} else {
			list = userDAO.getUsersByUsername(username);
			if (list.size() > 0) {
				return "{\"errorCode\":1,\"error\":\"员工登录名已存在\"}";
			} else {
				User user = userService.loadFormForUser(request, roleid, branchid, file);
				userService.addUser(user);
				return "{\"errorCode\":0,\"error\":\"创建成功\",\"type\":\"add\"}";
			}
		}
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "username", required = false, defaultValue = "") String username,
			@RequestParam(value = "realname", required = false, defaultValue = "") String realname) {
		model.addAttribute("userList", userDAO.getUsersByPage(page, username, realname));
		model.addAttribute("branches", branchDAO.getAllBranches());
		model.addAttribute("roles", roleDAO.getRoles());
		model.addAttribute("page_obj", new Page(userDAO.getUserCount(username, realname), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "user/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long userid, Model model) {
		model.addAttribute("branches", branchDAO.getAllBranches());
		model.addAttribute("user", userDAO.getUserByUserid(userid));
		model.addAttribute("roles", roleDAO.getRoles());
		return "user/edit";
	}

	@RequestMapping("/saveFile/{id}")
	public @ResponseBody String saveFile(@PathVariable("id") long userid, Model model, @RequestParam(value = "Filedata", required = false) MultipartFile file, @RequestParam("branchid") long branchid,
			@RequestParam("roleid") long roleid, HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list = userDAO.getUsersByRealname(realname);

		User user = userService.loadFormForUserToEdit(request, roleid, branchid, file);
		user.setUserid(userid);
		if (list.size() > 0 && list.get(0).getUserid() != userid) {
			return "{\"errorCode\":1,\"error\":\"员工姓名已存在\"}";
		} else {
			list = userDAO.getUsersByUsername(username);
			if (list.size() > 0 && list.get(0).getUserid() != userid) {
				return "{\"errorCode\":1,\"error\":\"员工的登录用户名已存在\"}";
			} else {

				userService.editUser(user);
				return "{\"errorCode\":0,\"error\":\"保存成功\",\"type\":\"edit\"}";
			}
		}

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") long userid, Model model, @RequestParam("branchid") long branchid, @RequestParam("roleid") long roleid, HttpServletRequest request)
			throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list = userDAO.getUsersByRealname(realname);

		User user = userService.loadFormForUserToEdit(request, roleid, branchid, null);
		user.setUserid(userid);
		if (list.size() > 0 && list.get(0).getUserid() != userid) {
			return "{\"errorCode\":1,\"error\":\"员工姓名已存在\"}";
		} else {
			list = userDAO.getUsersByUsername(username);
			if (list.size() > 0 && list.get(0).getUserid() != userid) {
				return "{\"errorCode\":1,\"error\":\"员工的登录用户名已存在\"}";
			} else {

				userService.editUser(user);
				return "{\"errorCode\":0,\"error\":\"保存成功\"}";
			}
		}

	}

	@RequestMapping("updatepassword")
	public String updatePassword(@RequestParam("password") String password, @RequestParam("confirmpassword") String confirmpassword, ExplinkUserDetail userDetail, Model model) {
		if (password.equals(confirmpassword)) {
			jdbcTemplate.update("update express_set_user set password=? where userid=?", password, getSessionUser().getUserid());
			model.addAttribute("message", "修改成功");
		} else {
			model.addAttribute("message", "两次输入的密码不一致");
		}
		return "/passwordupdate";
	}

}