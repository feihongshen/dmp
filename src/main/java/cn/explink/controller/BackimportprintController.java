package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BackIntoprintDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Backintowarehouse_print;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.BranchService;
import cn.explink.service.ExplinkUserDetail;

@Controller
@RequestMapping("/backimportprint")
public class BackimportprintController {

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	BackIntoprintDAO backIntoprintDAO;
	@Autowired
	BranchService branchService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(
			Model model,
			@PathVariable("page") long page,
			HttpServletRequest request,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") String[] branchid,
			@RequestParam(value = "begincredate", required = false, defaultValue = "") String begincredate,
			@RequestParam(value = "endcredate", required = false, defaultValue = "") String endcredate,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		model.addAttribute("branches",
				branchDAO.getBranchesByKuFangAndZhanDian());
		model.addAttribute("userList",
				userDAO.getUserByRole(3));
		model.addAttribute("driverid",
				driverid);
		List<String> branchArrlist = new ArrayList<String>();
		if (branchid != null && branchid.length > 0) {
			for (String str : branchid) {
				branchArrlist.add(str);
			}
		}
		String branchids=getStringByBranchids(branchid);
		long flowordertype = FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue();
		List<Backintowarehouse_print> backIntoprintList = backIntoprintDAO.getBackintoPrint(begincredate, endcredate, flowordertype, branchids, driverid, 0);
		model.addAttribute("branchArrlist", branchArrlist);
		return "backimportprint/list";
	}

	private String getStringByBranchids(String[] branchid) {
		String branchids = "";
		if (branchid.length > 0) {
			for (String id : branchid) {
				branchids += id + ",";
			}
		}

		if (branchids.length() > 0) {
			branchids = branchids.substring(0, branchids.length() - 1);
		}
		return branchids;
	}

	public static String getStringsByStringList(List<String> strArr) {
		String strs = "";
		if (strArr.size() > 0) {
			for (String str : strArr) {
				strs += "'" + str + "',";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

}
