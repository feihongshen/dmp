package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import cn.explink.dao.PaiFeiRuleDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Menu;
import cn.explink.domain.User;
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
import cn.explink.util.ExcelUtils;
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
	@Autowired
	PaiFeiRuleDAO pfFeiRuleDAO;
	@Autowired
	CwbOrderService cs;
	@Autowired
	ExportService exportService;
	@Autowired
	UserInfService userInfService;

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
				if (roleid == 2) {
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
				if (roleid == 2) {
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
		return "user/edit";
	}

	@RequestMapping("/saveFile/{id}")
	public @ResponseBody
	String saveFile(@PathVariable("id") long userid, Model model, @RequestParam(value = "Filedata", required = false) MultipartFile file, @RequestParam("branchid") long branchid,
			@RequestParam("roleid") long roleid, HttpServletRequest request) throws Exception {
		String username = StringUtil.nullConvertToEmptyString(request.getParameter("username"));
		String realname = StringUtil.nullConvertToEmptyString(request.getParameter("realname"));
		List<User> list = this.userDAO.getUsersByUsernameToUpper(username);
		String oldrealname = this.userDAO.getUserByUserid(userid).getRealname();
		int oldemployeestatus = this.userDAO.getUserByUserid(userid).getEmployeestatus();
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
				if (roleid == 2) {
					String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
					if ((adressenabled != null) && adressenabled.equals("1")) {
						if (user.getEmployeestatus() != 3) {
							if (oldemployeestatus != 3) {
								if (!realname.equals(oldrealname)) {
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
		String oldrealname = this.userDAO.getUserByUserid(userid).getRealname();
		int oldemployeestatus = this.userDAO.getUserByUserid(userid).getEmployeestatus();
		User user = this.userService.loadFormForUserToEdit(request, roleid, branchid, null, userid);
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
				this.logger.info("operatorUser={},用户管理->save", this.getSessionUser().getUsername());
				// TODO 增加同步代码
				if (roleid == 2) {
					String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
					if ((adressenabled != null) && adressenabled.equals("1")) {
						if (user.getEmployeestatus() != 3) {
							if (oldemployeestatus != 3) {
								if (!realname.equals(oldrealname)) {
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
	public String updateWebPassword(@RequestParam("webPassword") String webPassword, @RequestParam("confirmWebPassword") String confirmWebPassword, ExplinkUserDetail userDetail, Model model) {
		if (webPassword.equals(confirmWebPassword)) {
			this.jdbcTemplate.update("update express_set_user set webPassword=? where userid=?", webPassword, this.getSessionUser().getUserid());
			this.logger.info("operatorUser={},用户管理->updatewebpassword", this.getSessionUser().getUsername());
			model.addAttribute("message", "修改网页登录密码成功");
		} else {
			model.addAttribute("message", "两次输入的网页登录密码不一致");
		}
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
							if (!realname.equals(oldrealname)) {
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
	
}