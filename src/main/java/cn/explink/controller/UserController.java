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
import org.springframework.web.multipart.MultipartFile;

import cn.explink.b2c.weisuda.CourierService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.MenuDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Menu;
import cn.explink.domain.User;
import cn.explink.schedule.Constants;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ScheduledTaskService;
import cn.explink.service.SystemInstallService;
import cn.explink.service.UserMonitorService;
import cn.explink.service.UserService;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	CourierService courierService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	MenuDAO menuDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	UserMonitorService userMonitorService;
	@Autowired
	ScheduledTaskService scheduledTaskService;
	@Autowired
	SystemInstallService systemInstallService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/")
	public @ResponseBody List<User> getUserByBranchid(@RequestParam("branchid") long branchid) {
		return userDAO.getUserByBranchid(branchid);
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

	@RequestMapping("/crossCapablePDA")
	public @ResponseBody String crossCapablePDA(@RequestParam("branchid") long branchid, @RequestParam("roleid") long roleid) throws Exception {
		String reMenu = "";

		List<Menu> ms = menuDAO.getMenusByUserRoleidToPDA(roleid);
		Branch b = branchDAO.getBranchById(branchid);
		try {
			for (Menu m : ms) {
				if (b.getFunctionids().indexOf(m.getMenuno()) > -1) {
					reMenu += m.getName() + "　";
				}
			}
		} catch (Exception e) {
		}
		return reMenu;
	}

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("branches", branchDAO.getAllEffectBranches());
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
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() != 3) {
					courierService.courierUpdate(user);
				}
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() == 3) {
					courierService.carrierDel(user);
				}
				logger.info("operatorUser={},用户管理->create", getSessionUser().getUsername());
				// TODO 增加同步代码
				if (roleid == 2) {
					String adressenabled = systemInstallService.getParameter("newaddressenabled");
					if (adressenabled != null && adressenabled.equals("1")) {
						if (user.getEmployeestatus() != 3) {
							list = userDAO.getUsersByUsername(username);
							scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(list.get(0).getUserid()), true);
						}
					}
				}
				userMonitorService.userMonitorByUsername(user.getUsername());
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
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() != 3) {
					courierService.courierUpdate(user);
				}
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() == 3) {
					courierService.carrierDel(user);
				}
				logger.info("operatorUser={},用户管理->createFile", getSessionUser().getUsername());
				// TODO 增加同步代码
				if (roleid == 2) {
					String adressenabled = systemInstallService.getParameter("newaddressenabled");
					if (adressenabled != null && adressenabled.equals("1")) {
						if (user.getEmployeestatus() != 3) {
							list = userDAO.getUsersByUsername(username);
							scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(list.get(0).getUserid()), true);
						}
					}
				}
				userMonitorService.userMonitorByUsername(user.getUsername());
				return "{\"errorCode\":0,\"error\":\"创建成功\",\"type\":\"add\"}";
			}
		}
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "username", required = false, defaultValue = "") String username,
			@RequestParam(value = "realname", required = false, defaultValue = "") String realname, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "roleid", required = false, defaultValue = "-1") long roleid) {
		model.addAttribute("userList", userDAO.getUsersByPage(page, username, realname, branchid, roleid));
		model.addAttribute("brancheEffectList", branchDAO.getAllEffectBranches());
		model.addAttribute("branches", branchDAO.getAllBranches());
		model.addAttribute("roles", roleDAO.getRoles());
		model.addAttribute("page_obj", new Page(userDAO.getUserCount(username, realname, branchid, roleid), page, Page.ONE_PAGE_NUMBER));
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
		String oldrealname = userDAO.getUserByUserid(userid).getRealname();
		int oldemployeestatus = userDAO.getUserByUserid(userid).getEmployeestatus();
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
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() != 3) {
					courierService.courierUpdate(user);
				}
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() == 3) {
					courierService.carrierDel(user);
				}
				logger.info("operatorUser={},用户管理->saveFile", getSessionUser().getUsername());
				// TODO 增加同步代码
				if (roleid == 2) {
					String adressenabled = systemInstallService.getParameter("newaddressenabled");
					if (adressenabled != null && adressenabled.equals("1")) {
						if (user.getEmployeestatus() != 3) {
							if (oldemployeestatus != 3) {
								if (!realname.equals(oldrealname)) {
									scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_MODIFY, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
								}
							} else {
								scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
							}
						} else {
							scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_DELETE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
						}
					}
				}
				userMonitorService.userMonitorById(userid);
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
		String oldrealname = userDAO.getUserByUserid(userid).getRealname();
		int oldemployeestatus = userDAO.getUserByUserid(userid).getEmployeestatus();
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
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() != 3) {
					courierService.courierUpdate(user);
				}
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() == 3) {
					courierService.carrierDel(user);
				}
				logger.info("operatorUser={},用户管理->save", getSessionUser().getUsername());
				// TODO 增加同步代码
				if (roleid == 2) {
					String adressenabled = systemInstallService.getParameter("newaddressenabled");
					if (adressenabled != null && adressenabled.equals("1")) {
						if (user.getEmployeestatus() != 3) {
							if (oldemployeestatus != 3) {
								if (!realname.equals(oldrealname)) {
									scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_MODIFY, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
								}
							} else {
								scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
							}
						} else {
							scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_DELETE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
						}
					}
				}
				userMonitorService.userMonitorById(userid);
				return "{\"errorCode\":0,\"error\":\"保存成功\"}";
			}
		}

	}

	@RequestMapping("updatepassword")
	public String updatePassword(@RequestParam("password") String password, @RequestParam("confirmpassword") String confirmpassword, ExplinkUserDetail userDetail, Model model) {
		if (password.equals(confirmpassword)) {
			jdbcTemplate.update("update express_set_user set password=? where userid=?", password, getSessionUser().getUserid());
			logger.info("operatorUser={},用户管理->updatepassword", getSessionUser().getUsername());
			model.addAttribute("message", "修改成功");
		} else {
			model.addAttribute("message", "两次输入的密码不一致");
		}
		return "/passwordupdate";
	}

	// ****************************************************小件员管理**************************************************************//

	@RequestMapping("/addBranch")
	public String addBranch(Model model) throws Exception {
		model.addAttribute("branch", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()));
		return "user/addbranch";
	}

	@RequestMapping("/createBranch")
	public @ResponseBody String createBranch(Model model, HttpServletRequest request) throws Exception {
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
				User user = userService.loadFormForUser(request, 2, getSessionUser().getBranchid(), null);
				userService.addUser(user);
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() != 3) {
					courierService.courierUpdate(user);
				}
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() == 3) {
					courierService.carrierDel(user);
				}
				logger.info("operatorUser={},用户管理->createFile", getSessionUser().getUsername());
				// TODO 增加同步代码
				String adressenabled = systemInstallService.getParameter("newaddressenabled");
				if (adressenabled != null && adressenabled.equals("1")) {
					if (user.getEmployeestatus() != 3) {
						list = userDAO.getUsersByUsername(username);
						scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(list.get(0).getUserid()), true);
					}
				}
				userMonitorService.userMonitorByUsername(username);
				return "{\"errorCode\":0,\"error\":\"创建成功\",\"type\":\"add\"}";
			}
		}
	}

	@RequestMapping("/editBranch/{id}")
	public String editBranch(@PathVariable("id") long userid, Model model) {
		model.addAttribute("branch", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()));
		model.addAttribute("user", userDAO.getUserByUserid(userid));
		return "user/editbranch";
	}

	@RequestMapping("/saveBranch/{id}")
	public @ResponseBody String saveBranch(@PathVariable("id") long userid, Model model, HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list = userDAO.getUsersByRealname(realname);
		String oldrealname = userDAO.getUserByUserid(userid).getRealname();
		int oldemployeestatus = userDAO.getUserByUserid(userid).getEmployeestatus();
		User user = userService.loadFormForUserToEdit(request, 2, getSessionUser().getBranchid(), null);
		user.setUserid(userid);
		if (list.size() > 0 && list.get(0).getUserid() != userid) {
			return "{\"errorCode\":1,\"error\":\"员工姓名已存在\"}";
		} else {
			list = userDAO.getUsersByUsername(username);
			if (list.size() > 0 && list.get(0).getUserid() != userid) {
				return "{\"errorCode\":1,\"error\":\"员工的登录用户名已存在\"}";
			} else {
				userService.editUser(user);
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() != 3) {
					courierService.courierUpdate(user);
				}
				if ((user.getRoleid() == 2 || user.getRoleid() == 4) && user.getEmployeestatus() == 3) {
					courierService.carrierDel(user);
				}
				logger.info("operatorUser={},用户管理->saveFile", getSessionUser().getUsername());
				// TODO 增加同步代码
				String adressenabled = systemInstallService.getParameter("newaddressenabled");
				if (adressenabled != null && adressenabled.equals("1")) {
					if (user.getEmployeestatus() != 3) {
						if (oldemployeestatus != 3) {
							if (!realname.equals(oldrealname)) {
								scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_MODIFY, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
							}
						} else {
							scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
						}
					} else {
						scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_DELETE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
					}
				}
				userMonitorService.userMonitorById(userid);
				return "{\"errorCode\":0,\"error\":\"保存成功\",\"type\":\"edit\"}";
			}
		}

	}

	@RequestMapping("/listbranch/{page}")
	public String listbranch(@PathVariable("page") long page, Model model, @RequestParam(value = "username", required = false, defaultValue = "") String username,
			@RequestParam(value = "realname", required = false, defaultValue = "") String realname) {
		model.addAttribute("branch", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()));
		model.addAttribute("userList", userDAO.getUsersForUserBranchByPage(page, username, realname, getSessionUser().getBranchid()));
		model.addAttribute("page_obj", new Page(userDAO.getUserForUserBranchCount(username, realname, getSessionUser().getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "user/listbranch";
	}

}