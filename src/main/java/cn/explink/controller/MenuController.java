package cn.explink.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.MenuDAO;
import cn.explink.dao.NotifyDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Menu;
import cn.explink.domain.Notify;
import cn.explink.domain.SystemInstall;
import cn.explink.service.ExplinkUserDetail;

@Controller
@RequestMapping("/")
public class MenuController {

	@Autowired
	MenuDAO menuDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}");

	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	NotifyDao notifyDao;
	@Autowired
	JointService jointService;

	@RequestMapping("")
	public String usercheck(Model model, HttpServletRequest request) throws Exception {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		Properties properties = getSystemInstallProperties();
		List<Menu> topLvelMenuByUserid = menuDAO.getTopLvelMenuByUserRoleid(userDetail.getUser().getRoleid());
		model.addAttribute("MENUPARENTLIST", topLvelMenuByUserid);
		List<Menu> menu2 = menuDAO.getSecondLvelMenuByUserRoleid(userDetail.getUser().getRoleid(), "2");
		replacePropertyHolder(properties, menu2, request);
		model.addAttribute("menus2", menu2);
		List<Menu> menu3 = menuDAO.getSecondLvelMenuByUserRoleid(userDetail.getUser().getRoleid(), "3");
		replacePropertyHolder(properties, menu3, request);
		model.addAttribute("menus3", menu3);
		model.addAttribute("omsUrl", properties.get("omsUrl"));
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("isOpenFlag", isOpenFlag);
		return "index";
	}

	private void replacePropertyHolder(Properties properties, List<Menu> secondLvelMenu, HttpServletRequest request) {
		for (Menu menu : secondLvelMenu) {
			String menuurl = placeholderHelper.replacePlaceholders(menu.getUrl(), properties);
			if (!menuurl.startsWith("/") && !menu.getUrl().startsWith("http")) {
				menuurl = request.getContextPath() + "/" + menuurl;
			}
			menu.setUrl(menuurl);
		}
	}

	private Properties getSystemInstallProperties() {
		List<SystemInstall> systemInstalls = systemInstallDAO.getAllProperties();
		Properties properties = new Properties();
		for (SystemInstall systemInstall : systemInstalls) {
			properties.put(systemInstall.getName(), systemInstall.getValue());
		}
		return properties;
	}

	@RequestMapping("welcome")
	public String welcome(Model model, HttpServletRequest request) throws Exception {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		Properties properties = getSystemInstallProperties();
		List<Menu> topLvelMenuByUserid = menuDAO.getTopLvelMenuByUserRoleid(userDetail.getUser().getRoleid());
		Notify nf = notifyDao.getIstopNotify();
		List<Notify> notifies = notifyDao.getIndexNotify();

		model.addAttribute("MENUPARENTLIST", topLvelMenuByUserid);
		model.addAttribute("menus2", menuDAO.getSecondLvelMenuByUserRoleid(userDetail.getUser().getRoleid(), "2"));
		List<Menu> menu3 = menuDAO.getSecondLvelMenuByUserRoleidToWelcome(userDetail.getUser().getRoleid(), "3");
		replacePropertyHolder(properties, menu3, request);
		model.addAttribute("topnf", nf);
		model.addAttribute("notifies", notifies);
		model.addAttribute("menus3", menu3);
		return "iframe_welcome";
	}
}