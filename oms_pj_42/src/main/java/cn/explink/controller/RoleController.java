package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.MenuDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.domain.Role;
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

	@RequestMapping("/list")
	public String getList(Model model, @RequestParam(value = "rolename", defaultValue = "", required = false) String rolename) throws Exception {
		if (rolename.length() > 0) {
			model.addAttribute("roles", roleDAO.getRolesByRolename(rolename));
		} else {
			model.addAttribute("roles", roleDAO.getRoles());
		}
		return "role/list";
	}

	@RequestMapping("/rolecheck")
	public @ResponseBody boolean rolecheck(@RequestParam("rolename") String rolename) throws Exception {
		rolename = new String(rolename.getBytes("ISO8859-1"), "utf-8");
		List<Role> list = roleDAO.getRolesByRolename(rolename);
		return list.size() == 0;
	}

	@RequestMapping("/create")
	public @ResponseBody String setRole(Model model, HttpServletRequest request) throws Exception {

		Role role = roleService.loadFormForRole(request);
		List<Role> list = roleDAO.getRolesByRolename(role.getRolename());
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该角色已存在\"}";
		} else {
			roleDAO.creRole(role);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}
	}

	@RequestMapping("/edit/{roleid}")
	public String edit(@PathVariable("roleid") long roleid, Model model) {
		model.addAttribute("role", roleDAO.getRolesByRoleid(roleid));
		return "role/edit";
	}

	@RequestMapping("/save/{roleid}")
	public @ResponseBody String save(@PathVariable("roleid") long roleid, @RequestParam("rolename") String rolename, Model model) {
		List<Role> list = roleDAO.getRolesByRolename(rolename);
		if (list.size() > 0 && list.get(0).getRoleid() != roleid) {
			return "{\"errorCode\":1,\"error\":\"该角已存在\"}";
		}
		roleDAO.save(roleid, rolename);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/editRoleAndMenu/{roleid}")
	public String editRoleAndMenu(@PathVariable("roleid") long roleid, Model model) {
		model.addAttribute("menus", menuDAO.getMenus());
		model.addAttribute("role", roleDAO.getRolesByRoleid(roleid));
		model.addAttribute("role_menu", roleDAO.getRoleAndMenuByRoleid(roleid));
		model.addAttribute("PDAmenu", menuDAO.getPDAMenus());
		return "role/editRoleAndMenu";
	}

	@RequestMapping("/saveRoleAndMenu/{roleid}")
	public @ResponseBody String saveRoleAndMenu(@PathVariable("roleid") long roleid, Model model, @RequestParam(value = "menu", required = false) List<Long> menu) {
		roleDAO.delRoleAndMenu(roleid);
		roleDAO.saveRoleAndMenu(menu, roleid);
		return "{\"errorCode\":0,\"error\":\"设置成功\"}";
	}
}