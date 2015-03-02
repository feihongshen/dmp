package cn.explink.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.AbnormalWriteBackDAO;
import cn.explink.dao.AppearWindowDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.AbnormalService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.UserService;

@Controller
@RequestMapping("/monitorlog")
public class MonitorLogController {

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	AbnormalTypeDAO abnormalTypeDAO;
	@Autowired
	AbnormalOrderDAO abnormalOrderDAO;
	@Autowired
	AbnormalWriteBackDAO abnormalWriteBackDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	RoleDAO roleDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 查看订单生命周期监控
	 *
	 * @param model
	 * @param cwb
	 * @param abnormaltypeid
	 * @return
	 */
	@RequestMapping("/monitorloglist")
	public String monitorloglist(Model model,@RequestParam(value = "customerid", required = false, defaultValue = "0") String[] customeridStr) {
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		List<String> customeridList = this.dataStatisticsService.getList(customeridStr);
		model.addAttribute("customeridStr", customeridList);
		return "/monitor/monitorlog";
	}
	@RequestMapping("/monitorcunhuolist")
	public String monitorcunhuolist(Model model,@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchidStr) {
		
		List<Branch> branchList = this.branchDAO.getBranchToUser(this.getSessionUser().getUserid());
		List<String> dispatchbranchidlist = this.dataStatisticsService.getList(dispatchbranchidStr);
		model.addAttribute("dispatchbranchidStr", dispatchbranchidlist);
		model.addAttribute("branchList", branchList);
		return "/monitor/monitorcunhuo";
	}

	
	public String getStrings(List<String> strArr) {
		String strs = "0,";
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

	public String getString(List<Long> list) {
		String str = "0,";
		if (list.size() > 0) {
			for (Long num : list) {
				str += num + ",";
			}

		}
		if (str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}
		return str;

	}
}