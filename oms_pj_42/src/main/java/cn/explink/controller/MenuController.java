package cn.explink.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.dao.MenuDAO;
import cn.explink.domain.Menu;
import cn.explink.service.ExplinkUserDetail;

@Controller
@RequestMapping("/")
public class MenuController {

	@Autowired
	MenuDAO menuDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@RequestMapping("")
	public String usercheck(Model model) throws Exception {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		List<Menu> topLvelMenuByUserid = menuDAO.getTopLvelMenuByUserRoleid(userDetail.getUser().getRoleid());
		model.addAttribute("MENUPARENTLIST", topLvelMenuByUserid);
		model.addAttribute("menus2", menuDAO.getSecondLvelMenuByUserRoleid(userDetail.getUser().getRoleid(), "2"));
		model.addAttribute("menus3", menuDAO.getSecondLvelMenuByUserRoleid(userDetail.getUser().getRoleid(), "3"));
		return "index";
	}
}