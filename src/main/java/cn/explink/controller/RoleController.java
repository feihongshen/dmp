package cn.explink.controller;

import java.util.ArrayList;
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

import cn.explink.dao.MenuDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dmp40.Dmp40Function;
import cn.explink.dmp40.Dmp40FunctionDAO;
import cn.explink.domain.Menu;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	RoleService roleService;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	MenuDAO menuDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	UserDAO userDAO;
	@Autowired
	Dmp40FunctionDAO dmpDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list")
	public String getList(
			Model model,
			@RequestParam(value = "rolename", defaultValue = "", required = false) String rolename)
			throws Exception {
		if (rolename.length() > 0) {
			model.addAttribute("roles", roleDAO.getRolesByRolename(rolename));
		} else {
			model.addAttribute("roles", roleDAO.getRoles());
		}
		List<User> userList = userDAO.getAllUser();
		Map<Long, Long> userAndCountMap = new HashMap<Long, Long>();
		for (User user : userList) {
			if (!userAndCountMap.containsKey(user.getRoleid())) {
				userAndCountMap.put(user.getRoleid(), 0l);
			}
			userAndCountMap.put(user.getRoleid(),
					userAndCountMap.get(user.getRoleid()) + 1);
		}
		model.addAttribute("userAndCountMap", userAndCountMap);
		return "role/list";
	}

	@RequestMapping("/rolecheck")
	public @ResponseBody
	boolean rolecheck(@RequestParam("rolename") String rolename)
			throws Exception {
		rolename = new String(rolename.getBytes("ISO8859-1"), "utf-8");
		List<Role> list = roleDAO.getRolesByRolename(rolename);
		return list.size() == 0;
	}

	@RequestMapping("/create")
	public @ResponseBody
	String setRole(Model model, HttpServletRequest request) throws Exception {

		Role role = roleService.loadFormForRole(request);
		List<Role> list = roleDAO.getRolesByRolename(role.getRolename());
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该角色已存在\"}";
		} else {
			roleDAO.creRole(role);
			logger.info("operatorUser={},岗位管理->create", getSessionUser()
					.getUsername());
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}
	}

	@RequestMapping("/edit/{roleid}")
	public String edit(@PathVariable("roleid") long roleid, Model model) {
		model.addAttribute("role", roleDAO.getRolesByRoleid(roleid));
		return "role/edit";
	}

	@RequestMapping("/save/{roleid}")
	public @ResponseBody
	String save(@PathVariable("roleid") long roleid,
			@RequestParam("rolename") String rolename, Model model) {
		List<Role> list = roleDAO.getRolesByRolename(rolename);
		if (list.size() > 0 && list.get(0).getRoleid() != roleid) {
			return "{\"errorCode\":1,\"error\":\"该角已存在\"}";
		}
		roleDAO.save(roleid, rolename);
		logger.info("operatorUser={},岗位管理->save", getSessionUser()
				.getUsername());
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/editRoleAndMenu/{roleid}")
	public String editRoleAndMenu(@PathVariable("roleid") long roleid,
			Model model) {

		List<Dmp40Function> Dmp40Functionlist = dmpDao.getAllFunctionFromDB();

		List<Menu> menuList = new ArrayList<Menu>();

		for (Dmp40Function d : Dmp40Functionlist) {

			Menu m = new Menu();
			m.setName(d.getFunctionName());
			m.setId(Integer.parseInt(d.getId()));
			String strs = d.getParentFunctionId();
			if (strs.equals("null") || strs.equals("")) {
				strs = 0 + "";
			}
			m.setParentid(Long.valueOf(strs));
			m.setMenulevel(String.valueOf(d.getFunctionLevel()));
			m.setUrl(d.getFunctionUrl());
			m.setMenuno(d.getFunctionOrder());
			menuList.add(m);
		}

		model.addAttribute("menus", menuList);
		model.addAttribute("role", roleDAO.getRolesByRoleid(roleid));
		model.addAttribute("role_menu", roleDAO.getRoleAndMenuByRoleid(roleid));
		model.addAttribute("PDAmenu", menuDAO.getPDAMenus());
		return "role/editRoleAndMenu";
	}

	@RequestMapping("/saveRoleAndMenu/{roleid}")
	public @ResponseBody
	String saveRoleAndMenu(
			@PathVariable("roleid") long roleid,
			Model model,
			@RequestParam(value = "menu", required = false) List<Long> menu,
			@RequestParam(value = "isDelivery", required = false, defaultValue = "0") int isDelivery) {
		roleDAO.delRoleAndMenu(roleid);
		roleDAO.saveRoleAndMenu(menu, roleid);
		roleDAO.saveRoleIsDeliveryByRoleid(roleid, isDelivery);
		logger.info("operatorUser={},岗位管理->saveRoleAndMenu", getSessionUser()
				.getUsername());
		return "{\"errorCode\":0,\"error\":\"设置成功\"}";
	}
}