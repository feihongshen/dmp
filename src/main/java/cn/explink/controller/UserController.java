package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.b2c.weisuda.CourierService;
import cn.explink.consts.ImageValidateConsts;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.MenuDAO;
import cn.explink.dao.PaiFeiRuleDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Menu;
import cn.explink.domain.Role;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.addressvo.AddressSyncServiceResult;
import cn.explink.domain.addressvo.ApplicationVo;
import cn.explink.domain.addressvo.BatchSyncAdressResultVo;
import cn.explink.domain.addressvo.DelivererVo;
import cn.explink.enumutil.PaiFeiRuleTypeEnum;
import cn.explink.enumutil.UserEmployeestatusEnum;
import cn.explink.schedule.Constants;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.ScheduledTaskService;
import cn.explink.service.SystemInstallService;
import cn.explink.service.UserInfService;
import cn.explink.service.UserMonitorService;
import cn.explink.service.UserService;
import cn.explink.service.addressmatch.AddressSyncService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.ResourceBundleUtil;
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
	@Autowired
	PaiFeiRuleDAO pfFeiRuleDAO;
	@Autowired
	CwbOrderService cs;
	@Autowired
	ExportService exportService;
	@Autowired
	UserInfService userInfService;
	@Autowired
	private SystemInstallDAO systemInstallDAO;
	
	@Autowired
	private AddressSyncService addressService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/")
	public @ResponseBody
	List<User> getUserByBranchid(@RequestParam("branchid") long branchid) {
		return this.userDAO.getUserByBranchid(branchid);

	}
	
	@RequestMapping("/getUsersByBranchids")
	public @ResponseBody
	List<User> getUsersByBranchids(@RequestParam("branchid") String[] branchid) {
		String branchids=cs.getToString(branchid);
		return this.userDAO.getUserByBranchids(branchids);

	}


	@RequestMapping("/usercheck")
	public @ResponseBody
	boolean usercheck(@RequestParam("username") String username) throws Exception {
		List<User> list = this.userDAO.getUsersByUsername(username);
		return list.size() == 0;
	}

	@RequestMapping("/userrealnamecheck")
	public @ResponseBody
	boolean userrealnameCheck(@RequestParam("realname") String realname) throws Exception {
		realname = new String(realname.getBytes("ISO8859-1"), "utf-8");
		List<User> list = this.userDAO.getUsersByRealname(realname);
		return list.size() == 0;
	}

	@RequestMapping("/crossCapablePDA")
	public @ResponseBody
	String crossCapablePDA(@RequestParam("branchid") long branchid, @RequestParam("roleid") long roleid) throws Exception {
		String reMenu = "";

		List<Menu> ms = this.menuDAO.getMenusByUserRoleidToPDA(roleid);
		Branch b = this.branchDAO.getBranchById(branchid);
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
		model.addAttribute("branches", this.branchDAO.getAllEffectBranches());
		model.addAttribute("roles", this.roleDAO.getRoles());
		model.addAttribute("pfrulelist", this.pfFeiRuleDAO.getPaiFeiRuleByType(PaiFeiRuleTypeEnum.Derlivery.getValue()));
		return "user/add";
	}

	@RequestMapping("/create")
	public @ResponseBody
	String create(Model model, @RequestParam("branchid") long branchid, @RequestParam("roleid") long roleid, HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
//		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list = this.userDAO.getUsersByUsernameToUpper(username);
			if (list.size() > 0) {
				return "{\"errorCode\":1,\"error\":\"员工登录名已存在\"}";
			} else {
				User user = this.userService.loadFormForUser(request, roleid, branchid, null);
				this.userService.addUser(user);
				if (!userInfService.isCloseOldInterface()) {
					if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() != 3)) {
						this.courierService.courierUpdate(user);
					}
					if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() == 3)) {
						this.courierService.carrierDel(user);
					}
				}
				//  新接口 add by jian_xie
				userInfService.saveUserInf(user, getSessionUser());
				this.logger.info("operatorUser={},用户管理->create", this.getSessionUser().getUsername());
				// TODO 增加同步代码
				// 同步地址库逻辑修改 2016-07-20 chunlei05.li
				if (this.userService.isDeliver(roleid)) {
					String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
					if ((adressenabled != null) && adressenabled.equals("1")) {
						if (user.getEmployeestatus() != 3) {
							list = this.userDAO.getUsersByUsername(username);
							this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(list.get(0).getUserid()), true);
						}
					}
				}
				this.userMonitorService.userMonitorByUsername(user.getUsername());
				return "{\"errorCode\":0,\"error\":\"创建成功\"}";
			}
		
	}

	@RequestMapping("/createFile")
	public @ResponseBody
	String createFile(Model model, @RequestParam(value = "Filedata", required = false) MultipartFile file, @RequestParam("branchid") long branchid, @RequestParam("roleid") long roleid,
			HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
//		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list =  this.userDAO.getUsersByUsernameToUpper(username);
			if (list.size() > 0) {
				return "{\"errorCode\":1,\"error\":\"员工登录名已存在\"}";
			} else {
				User user = this.userService.loadFormForUser(request, roleid, branchid, file);
				this.userService.addUser(user);
				if(!userInfService.isCloseOldInterface()){
					if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() != 3)) {
						this.courierService.courierUpdate(user);
					}
					if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() == 3)) {
						this.courierService.carrierDel(user);
					}
				}
				//  新接口 add by jian_xie
				userInfService.saveUserInf(user, getSessionUser());
				this.logger.info("operatorUser={},用户管理->createFile", this.getSessionUser().getUsername());
				// TODO 增加同步代码
				// 同步地址库逻辑修改 2016-07-20 chunlei05.li
				if (this.userService.isDeliver(roleid)) {
					String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
					if ((adressenabled != null) && adressenabled.equals("1")) {
						if (user.getEmployeestatus() != 3) {
							list = this.userDAO.getUsersByUsername(username);
							this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(list.get(0).getUserid()), true);
						}
					}
										
				}
				this.userMonitorService.userMonitorByUsername(user.getUsername());
				return "{\"errorCode\":0,\"error\":\"创建成功\",\"type\":\"add\"}";
			}
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "username", required = false, defaultValue = "") String username,
			@RequestParam(value = "realname", required = false, defaultValue = "") String realname, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "roleid", required = false, defaultValue = "-1") long roleid) {
		model.addAttribute("userList", this.userDAO.getUsersByPage(page, username, realname, branchid, roleid));
		model.addAttribute("brancheEffectList", this.branchDAO.getAllEffectBranches());
		model.addAttribute("branches", this.branchDAO.getAllBranches());
		model.addAttribute("roles", this.roleDAO.getRoles());
		model.addAttribute("page_obj", new Page(this.userDAO.getUserCount(username, realname, branchid, roleid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		User user=this.getSessionUser();
		String userloginname=user.getUsername();
		model.addAttribute("flag", userloginname.equals("admin") ? true : false);
		return "user/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long userid, Model model) {
		model.addAttribute("branches", this.branchDAO.getAllBranches());
		model.addAttribute("user", this.userDAO.getUserByUserid(userid));
		model.addAttribute("roles", this.roleDAO.getRoles());
		model.addAttribute("pfrulelist", this.pfFeiRuleDAO.getPaiFeiRuleByType(PaiFeiRuleTypeEnum.Derlivery.getValue()));
		model.addAttribute("loginForbiddenPleaseWaitMinutes", this.getLoginForbiddenPleaseWaitMinutes(userid));
		return "user/edit";
	}

	@RequestMapping("/saveFile/{id}")
	public @ResponseBody
	String saveFile(@PathVariable("id") long userid, Model model, @RequestParam(value = "Filedata", required = false) MultipartFile file, @RequestParam("branchid") long branchid,
			@RequestParam("roleid") long roleid, HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list = this.userDAO.getUsersByUsernameToUpper(username);
		User oldUser = this.userDAO.getUserByUserid(userid);
		String oldrealname = oldUser.getRealname();
		long oldBranchid = oldUser.getBranchid();
		String oldUsername = oldUser.getUsername();
		int oldemployeestatus = oldUser.getEmployeestatus();
		User user = this.userService.loadFormForUserToEdit(request, roleid, branchid, file, userid);
		user.setUserid(userid);
			if ((list.size() > 0) && (list.get(0).getUserid() != userid)) {
				return "{\"errorCode\":1,\"error\":\"员工的登录用户名已存在\"}";
			} else {
				this.userService.editUser(user);
				if(!userInfService.isCloseOldInterface()){
					if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() != 3)) {
						this.courierService.courierUpdate(user);
					}
					if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() == 3)) {
						this.courierService.carrierDel(user);
					}
				}
				//  新接口 add by jian_xie
				userInfService.saveUserInf(user, getSessionUser());
				this.logger.info("operatorUser={},用户管理->saveFile", this.getSessionUser().getUsername());
				// TODO 增加同步代码
				// 同步地址库逻辑修改 2016-07-20 chunlei05.li
				if (this.userService.isDeliver(roleid)) {
					String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
					if ((adressenabled != null) && adressenabled.equals("1")) {
						if (user.getEmployeestatus() != 3) {
							if (oldemployeestatus != 3) {
								// 2016-7-20 如果更改了名称、登录名或者站点，则更新地址库
								if (!StringUtils.equals(oldrealname, realname) || oldBranchid != branchid || !StringUtils.equals(oldUsername, username)) {
									this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_MODIFY, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
								}
							} else {
								this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
							}
						} else {
							this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_DELETE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
						}
					}
					
				}
				this.userMonitorService.userMonitorById(userid);
				return "{\"errorCode\":0,\"error\":\"保存成功\",\"type\":\"edit\"}";
			}

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody
	String save(@PathVariable("id") long userid, Model model, @RequestParam("branchid") long branchid, @RequestParam("roleid") long roleid, HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list = this.userDAO.getUsersByUsernameToUpper(username);
		User oldUser = this.userDAO.getUserByUserid(userid);
		String oldrealname = oldUser.getRealname();
		long oldBranchid = oldUser.getBranchid();
		String oldUsername = oldUser.getUsername();
		int oldemployeestatus = oldUser.getEmployeestatus();
		User user = this.userService.loadFormForUserToEdit(request, roleid, branchid, null, userid);
		user.setUserid(userid);
		if ((list.size() > 0) && (list.get(0).getUserid() != userid)) {
			return "{\"errorCode\":1,\"error\":\"员工的登录用户名已存在\"}";
		} else {
			this.userService.editUser(user);
			if (!userInfService.isCloseOldInterface()) {
				if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() != 3)) {
					this.courierService.courierUpdate(user);
				}
				if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() == 3)) {
					this.courierService.carrierDel(user);
				}
			}
			// 新接口 add by jian_xie
			userInfService.saveUserInf(user, getSessionUser());
			this.logger.info("operatorUser={},用户管理->save", this.getSessionUser().getUsername());
			// TODO 增加同步代码
			// 同步地址库逻辑修改 2016-07-20 chunlei05.li
			if (this.userService.isDeliver(roleid)) {
				String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((adressenabled != null) && adressenabled.equals("1")) {
					if (user.getEmployeestatus() != 3) {
						if (oldemployeestatus != 3) {
							// 2016-7-20 如果更改了名称、登录名或者站点，则更新地址库
							if (!StringUtils.equals(oldrealname, realname) || oldBranchid != branchid || !StringUtils.equals(oldUsername, username)) {
								this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_MODIFY, Constants.REFERENCE_TYPE_USER_ID,String.valueOf(userid), true);
							}
						} else {
							this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
						}
					} else {
						this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_DELETE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
					}
				}
			}
			this.userMonitorService.userMonitorById(userid);
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("updatepassword")
	public String updatePassword(@RequestParam("password") String password, @RequestParam("confirmpassword") String confirmpassword, ExplinkUserDetail userDetail, Model model) {
		if (password.equals(confirmpassword)) {
			this.jdbcTemplate.update("update express_set_user set password=? where userid=?", password, this.getSessionUser().getUserid());
			this.logger.info("operatorUser={},用户管理->updatepassword", this.getSessionUser().getUsername());
			model.addAttribute("message", "修改POS登录密码成功");
		} else {
			model.addAttribute("message", "两次输入的POS登录密码不一致");
		}
		return "/passwordupdate";
	}

	@RequestMapping("updatewebpassword")
	public String updateWebPassword(@RequestParam("webPassword") String webPassword, 
									@RequestParam("confirmWebPassword") String confirmWebPassword, 
									@RequestParam("validateWebCode") String validateWebCode, //验证码
									@RequestParam("oldWebPassword") String oldWebPassword,  //旧密码
									ExplinkUserDetail userDetail, Model model) {
		//by zhili01.liang on 2016-08-09
		//唯品支付资金归集，修改密码时需同时验证密码和验证码======START=====
		/*
		if (webPassword.equals(confirmWebPassword)) {
			this.jdbcTemplate.update("update express_set_user set webPassword=? where userid=?", webPassword, this.getSessionUser().getUserid());
			this.logger.info("operatorUser={},用户管理->updatewebpassword", this.getSessionUser().getUsername());
			model.addAttribute("message", "修改网页登录密码成功");
		} else {
			model.addAttribute("message", "两次输入的网页登录密码不一致");
		}
		return "/passwordupdate";
		*/
		User sessionUser = getSessionUser();
		cn.explink.domain.User user = userDAO.getAllUserByid(sessionUser.getUserid());
		String vCodeInSession =  getImageValidateCode(ImageValidateConsts.TYPE_UPDATE_WEB_PWD);
		if(StringUtils.isEmpty(validateWebCode) 
				||!validateWebCode.equals(vCodeInSession)){
			model.addAttribute("message", "验证码不正确，请重新输入");
			return "/passwordupdate";
		}
		//验证成功后，清空验证码
		clearImageValidateCode(ImageValidateConsts.TYPE_UPDATE_WEB_PWD);
		if(!oldWebPassword.trim().equals(user.getWebPassword())){
			model.addAttribute("message", "旧密码错误，请输入正确的密码");
			return "/passwordupdate";
		}
		if(webPassword.trim().equals(user.getWebPassword())){
			model.addAttribute("message", "新旧密码不能相同，请输入正确的密码");
			return "/passwordupdate";
		}
		if(!webPassword.equals(confirmWebPassword)){
			model.addAttribute("message", "两次输入的网页登录密码不一致");
			return "/passwordupdate";
		}
		
		
		//成功操作
		this.jdbcTemplate.update("update express_set_user set webPassword=? where userid=?", webPassword, this.getSessionUser().getUserid());
		this.logger.info("operatorUser={},用户管理->updatewebpassword", sessionUser.getUsername());
		model.addAttribute("message", "修改网页登录密码成功");
		//唯品支付资金归集，修改密码时需同时验证密码和验证码======END=====
		return "/passwordupdate";
	}

	// ****************************************************小件员管理**************************************************************//

	@RequestMapping("/addBranch")
	public String addBranch(Model model) throws Exception {
		model.addAttribute("branch", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()));
		model.addAttribute("pfrulelist", this.pfFeiRuleDAO.getPaiFeiRuleByType(PaiFeiRuleTypeEnum.Derlivery.getValue()));
		return "user/addbranch";
	}

	@RequestMapping("/createBranch")
	public @ResponseBody
	String createBranch(Model model, HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
//		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list =  this.userDAO.getUsersByUsernameToUpper(username);
			if (list.size() > 0) {
				return "{\"errorCode\":1,\"error\":\"员工登录名已存在\"}";
			} else {
				User user = this.userService.loadFormForUser(request, 2, this.getSessionUser().getBranchid(), null);
				this.userService.addUser(user);
				if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() != 3)) {
					this.courierService.courierUpdate(user);
				}
				if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() == 3)) {
					this.courierService.carrierDel(user);
				}
				this.logger.info("operatorUser={},用户管理->createFile", this.getSessionUser().getUsername());
				// TODO 增加同步代码
				String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((adressenabled != null) && adressenabled.equals("1")) {
					if (user.getEmployeestatus() != 3) {
						list = this.userDAO.getUsersByUsername(username);
						this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(list.get(0).getUserid()), true);
					}
				}
				this.userMonitorService.userMonitorByUsername(username);
				return "{\"errorCode\":0,\"error\":\"创建成功\",\"type\":\"add\"}";
			}
	}

	@RequestMapping("/editBranch/{id}")
	public String editBranch(@PathVariable("id") long userid, Model model) {
		model.addAttribute("branch", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()));
		model.addAttribute("user", this.userDAO.getUserByUserid(userid));
		model.addAttribute("pfrulelist", this.pfFeiRuleDAO.getPaiFeiRuleByType(PaiFeiRuleTypeEnum.Derlivery.getValue()));
		return "user/editbranch";
	}

	@RequestMapping("/saveBranch/{id}")
	public @ResponseBody
	String saveBranch(@PathVariable("id") long userid, Model model, HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list = this.userDAO.getUsersByUsernameToUpper(username);
		String oldrealname = this.userDAO.getUserByUserid(userid).getRealname();
		String oldUsername = this.userDAO.getUserByUserid(userid).getUsername();
		int oldemployeestatus = this.userDAO.getUserByUserid(userid).getEmployeestatus();
		User user = this.userService.loadFormForUserToEdit(request, 2, this.getSessionUser().getBranchid(), null, userid);
		user.setUserid(userid);
			if ((list.size() > 0) && (list.get(0).getUserid() != userid)) {
				return "{\"errorCode\":1,\"error\":\"员工的登录用户名已存在\"}";
			} else {
				this.userService.editUser(user);
				if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() != 3)) {
					this.courierService.courierUpdate(user);
				}
				if (((user.getRoleid() == 2) || (user.getRoleid() == 4)) && (user.getEmployeestatus() == 3)) {
					this.courierService.carrierDel(user);
				}
				this.logger.info("operatorUser={},用户管理->saveFile", this.getSessionUser().getUsername());
				// TODO 增加同步代码
				String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((adressenabled != null) && adressenabled.equals("1")) {
					if (user.getEmployeestatus() != 3) {
						if (oldemployeestatus != 3) {
							// 2016-7-20 如果更改了名称、登录名，则更新地址库
							if (!StringUtils.equals(oldrealname, realname) || !StringUtils.equals(oldUsername, username)) {
								this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_MODIFY, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
							}
						} else {
							this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_CREATE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
						}
					} else {
						this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_USER_DELETE, Constants.REFERENCE_TYPE_USER_ID, String.valueOf(userid), true);
					}
				}
				this.userMonitorService.userMonitorById(userid);
				return "{\"errorCode\":0,\"error\":\"保存成功\",\"type\":\"edit\"}";
			}

	}

	@RequestMapping("/listbranch/{page}")
	public String listbranch(@PathVariable("page") long page, Model model, @RequestParam(value = "username", required = false, defaultValue = "") String username,
			@RequestParam(value = "realname", required = false, defaultValue = "") String realname) {
		model.addAttribute("branch", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()));
		model.addAttribute("userList", this.userDAO.getUsersForUserBranchByPage(page, username, realname, this.getSessionUser().getBranchid()));
		model.addAttribute("page_obj", new Page(this.userDAO.getUserForUserBranchCount(username, realname, this.getSessionUser().getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "user/listbranch";
	}
	
	//根据工作状态动态获取结算状态
	@RequestMapping("/getjiesuanstate")
	@ResponseBody
	public String getjiesuansate(
	@RequestParam(value = "employeestatus", required = false, defaultValue = "") String employeestatus){
		int employestate = Integer.parseInt(employeestatus);
		if(employestate == UserEmployeestatusEnum.GongZuo.getValue()||employestate == UserEmployeestatusEnum.XiuJia.getValue()){
			return "{\"errorCode\":1,\"error\":\"工作或休假\"}";
		}else if(employestate ==UserEmployeestatusEnum.DaiLiZhi.getValue()){
			return "{\"errorCode\":2,\"error\":\"待离职\"}";
		}else{
			return "{\"errorCode\":3,\"error\":\"离职\"}";
		}
	}
	/**
	 * realname
	 * sex
	 * branchid
	 * roleid
	 * employeestatus
	 * jiesuanstate
	 * idcardno
	 * usermobile
	 * 
	 * Comet
	 * @return
	 */
	@RequestMapping(value="/exportuserinfo")
	public void exportuserinfo(HttpServletRequest request,HttpServletResponse response){
		String[] cloumnName1 = new String[12]; // 导出的列名
		String[] cloumnName2 = new String[12]; // 导出的英文列名
		exportService.setUserInfo(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "用户信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "UserInfo_" + df.format(new Date()) + ".xlsx"; // 文件名
		
	try{		
		
		String branchid = request.getParameter("branchid");
		String roleid = request.getParameter("roleid");
		String username = request.getParameter("username");
		String realname = request.getParameter("realname");
		
		
		final List<User> userList=this.userDAO.getExportUserInfo(username, realname, Long.valueOf(branchid), Long.valueOf(roleid));
		
		ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
			@Override
			public void fillData(Sheet sheet, CellStyle style) {
				for (int k = 0; k < userList.size(); k++) {
					Row row = sheet.createRow(k + 1);
					row.setHeightInPoints(15);
					for (int i = 0; i < cloumnName.length; i++) {
						Cell cell = row.createCell((short) i);
						cell.setCellStyle(style);
						Object a = null;
						// 给导出excel赋值
						a = exportService.setUserInfoObject(cloumnName3,userList,a, i, k);
						cell.setCellValue(a == null ? "" : a.toString());
					}
				}
			}
		};
		
		excelUtil.excel(response, cloumnName, sheetName, fileName);
	} catch (Exception e) {
		logger.error("", e);
	}
	
		
		
	}
	
	/**
	 * 同步地址库
	 * @date 2016年7月25日 下午5:49:05
	 * @return
	 */
	@RequestMapping(value="/batchSyncAdress")
	@ResponseBody
	public Map<String, Object> batchSyncAdress() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Set<Long> roleidSet = new HashSet<Long>();
		// 小件员和站长属于配送员
		roleidSet.add(2l);
		roleidSet.add(4l);
		// 其它类型
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if(roles != null) {
			for(Role role : roles) {
				roleidSet.add(role.getRoleid());
			}
		}
		// 获取站点用户
		List<User> deliverList = this.userService.getAllUserByRole(new ArrayList<Long>(roleidSet));
		// 同步地址库
		long addresscustomerid = Long.parseLong(ResourceBundleUtil.addresscustomerid);
		// 生成applicationVo
		ApplicationVo applicationVo = new ApplicationVo();
		applicationVo.setCustomerId(addresscustomerid);
		applicationVo.setId(Long.parseLong(ResourceBundleUtil.addressid));
		applicationVo.setPassword(ResourceBundleUtil.addresspassword);
		
		List<BatchSyncAdressResultVo> batchSyncAdressResultVoList = new ArrayList<BatchSyncAdressResultVo>();
		
		int success = 0; // 成功数量
		int failure = 0; // 失败数量
		for (User deliver : deliverList) {
			// 正常状态，更新地址库。
			DelivererVo delivererVo = new DelivererVo();
			delivererVo.setCustomerId(addresscustomerid);
			delivererVo.setExternalId(deliver.getUserid());
			delivererVo.setName(deliver.getRealname());
			delivererVo.setExternalStationId(deliver.getBranchid());
			delivererVo.setUserCode(deliver.getUsername());
			
			try {
				AddressSyncServiceResult addressSyncServiceResult;
				if (deliver.getUserDeleteFlag() == 1 && deliver.getEmployeestatus() != 3) { // 地址库逻辑：saveOrUpdate
					addressSyncServiceResult = this.addressService.updateDeliverer(applicationVo, delivererVo);
				} else { // 删除
					addressSyncServiceResult = this.addressService.deleteDeliverer(applicationVo, delivererVo);
	
				}
				if (addressSyncServiceResult.getResultCode().getCode() == 0) {
					logger.info("[成功]小件员批量同步地址库 {} ：{}", deliver.getUsername(), addressSyncServiceResult.toString());
					success++;
				} else {
					BatchSyncAdressResultVo batchSyncAdressResultVo = new BatchSyncAdressResultVo();
					batchSyncAdressResultVo.setUsername(deliver.getUsername());
					batchSyncAdressResultVo.setRealname(deliver.getRealname());
					batchSyncAdressResultVo.setMessage(addressSyncServiceResult.toString());
					batchSyncAdressResultVo.setResult("失败");
					batchSyncAdressResultVoList.add(batchSyncAdressResultVo);
					logger.info("[失败]小件员批量同步地址库 {} ：{}", deliver.getUsername(), addressSyncServiceResult.toString());
					failure++;
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				resultMap.put("code", 1);
				resultMap.put("errorMsg", e.getMessage());
				return resultMap;
			}
		}
		resultMap.put("code", 0);
		resultMap.put("success", success);
		resultMap.put("failure", failure);
		resultMap.put("batchSyncAdressResultVoList", batchSyncAdressResultVoList);
		return resultMap;
	}
	
	/**
	 *@author zhili01.liang
	 * 唯品支付资金归集，修改密码时需同时验证密码和验证码
	 * 跳转到image_validate.jsp页面
	 * @return
	 */
	@RequestMapping("/randomImg")
	public String randomImg(){
		return "image_validate";
	}
	
	/**
	 * @author zhili01.liang
	 * 唯品支付资金归集，修改密码时需同时验证密码和验证码
	 * 获取image_validate.jsp里的验证码
	 * @param type 对应ImageValidateConsts里的值
	 * @return
	 */
	private String getImageValidateCode(String type){
		String validateCode = "-1";
		HttpSession session = getSession();
		Object validateObject= session.getAttribute(type);
		if(validateObject!=null){
			validateCode = validateObject.toString();
			//使用一次后清空，防止用户用同一个验证码破译密码
			
		}
		return validateCode;
		
	}
	
	/**
	 * 清空验证码
	 * @param type
	 */
	private void clearImageValidateCode(String type){
		HttpSession session = getSession();
		session.setAttribute(type, null);
	}
	
	/**
	 * 获取http session
	 * @return
	 */
	private HttpSession getSession(){
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();  
	    HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();  
	    return request.getSession();
	}
	
	private long getLoginForbiddenPleaseWaitMinutes(long userid){
		long loginForbiddenPleaseWaitMinutes = 0;
		User user = userDAO.getUserByUserid(userid);
		if (user != null){
			int lastLoginState = user.getLastLoginState();	// 上次登录状态（1-成功，0-失败）
			int loginFailCount = user.getLoginFailCount();	// 累计连续登录错误次数
			String lastLoginTryTime = user.getLastLoginTryTime();	// 上次尝试登录时间
			
			int loginFailMaxTryTimeLimit = getLoginFailMaxTryTimeLimit();	// 登录失败尝试最大次数
			int loginForbiddenIntervalInMinutes = getLoginForbiddenIntervalInMinutes();	// 登录失败禁止时间长度（分钟）
			
			String nowTimeInString = DateTimeUtil.getNowTime();
			
			if(lastLoginState != 1){
				if(loginFailCount >= loginFailMaxTryTimeLimit){
					loginForbiddenPleaseWaitMinutes = loginForbiddenIntervalInMinutes - getDateDiffInMinutes(nowTimeInString, lastLoginTryTime);
				}
			}
		}
		
		return loginForbiddenPleaseWaitMinutes;
	}
	
	// 登录失败尝试最大次数
	private int getLoginFailMaxTryTimeLimit() {
		int loginFailMaxTryTimeLimit;
		SystemInstall loginFailMaxTryTimeLimitSystemInstall = systemInstallDAO
				.getSystemInstall("loginFailMaxTryTimeLimit");
		loginFailMaxTryTimeLimit = (loginFailMaxTryTimeLimitSystemInstall == null ? 0
				: Integer.valueOf(loginFailMaxTryTimeLimitSystemInstall.getValue()));
		return loginFailMaxTryTimeLimit;
	}
	
	// 登录失败禁止时间长度（分钟）
	private int getLoginForbiddenIntervalInMinutes() {
		int loginForbiddenIntervalInMinutes;
		SystemInstall loginForbiddenIntervalInMinutesSystemInstall = systemInstallDAO
				.getSystemInstall("loginForbiddenIntervalInMinutes");
		loginForbiddenIntervalInMinutes = (loginForbiddenIntervalInMinutesSystemInstall == null ? 60
				: Integer.valueOf(loginForbiddenIntervalInMinutesSystemInstall.getValue()));
		return loginForbiddenIntervalInMinutes;
	}
	
	private long getDateDiffInMinutes(String dateStr1, String dateStr2) {
		if (StringUtil.isEmpty(dateStr1)) {
			dateStr1 = "0000-00-00 00:00:00";
		}
		if (StringUtil.isEmpty(dateStr2)) {
			dateStr2 = "0000-00-00 00:00:00";
		}
		return Math.abs(DateTimeUtil.dateDiff("minute", dateStr1, dateStr2));
	}
	
	/**
	 * 解除登录封禁
	 * @param userid
	 * @return
	 */
	@RequestMapping("/liftLoginForbiddance")
	@ResponseBody
	public String liftLoginForbiddance(@RequestParam(value = "userid", required = false, defaultValue = "0") Long userid) {
		User user = this.userDAO.getUserByUserid(userid);
		user.setLoginFailCount(0);
		userDAO.saveUser(user);
		return "{\"errorCode\":0,\"error\":\"解禁成功\"}";
	}
}