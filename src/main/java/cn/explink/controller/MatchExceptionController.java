package cn.explink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;

@Controller
@RequestMapping("/matchexceptionhandle")
public class MatchExceptionController {

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private BranchDAO branchDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private SystemInstallDAO systemInstallDAO;

	@RequestMapping("/")
	public String matchExpectionHandle(Model model) {
		return "meh/matchexceptionhandle";
	}

}
