package cn.explink.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.MonitorLogService;

@Controller
@RequestMapping("/monitorlog")
public class MonitorLogController {

	@Autowired
	MonitorLogService monitorLogService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	DataStatisticsService dataStatisticsService;
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
	public String monitorloglist(Model model,@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customeridStr,
			HttpServletRequest request) {
		
		List<Customer> clist =  this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist",clist);
		Map<Long,String> cmap =new HashMap<Long , String>();
		for (Customer cut : clist) {
			cmap.put(cut.getCustomerid(), cut.getCustomername());
		}
		model.addAttribute("customerMap",cmap);
		List<String> customeridList = this.dataStatisticsService.getList(customeridStr);
		model.addAttribute("customeridStr", customeridList);
		List<Branch>  blist = branchDAO.getBranchAllzhandian(BranchEnum.KuFang.getValue()+"");
		String branchids ="-1";
		if(blist != null && blist.size()>0){
			for (Branch branch : blist) {
				branchids += ","+branch.getBranchid();
			}
		}
		String customerids =getStrings(customeridStr);
		List<MonitorLogDTO> monitorList =  new ArrayList<MonitorLogDTO>();
		if(request.getParameter("isnow") != null && request.getParameter("isnow").equals("1") ){
			monitorList = monitorLogService.getMonitorLogByBranchid(branchids,customerids);
		}		
		
		model.addAttribute("monitorList", monitorList);
		return "/monitor/monitorlog";
	}
	private String getStrings(String[] strArr) {
		String strs = "";
		if (strArr.length > 0) {
			for (String str : strArr) {
				strs += str + ",";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}
	@RequestMapping("/monitorcunhuolist")
	public String monitorcunhuolist(Model model,@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchidStr) {
		
		List<Branch> branchList = this.branchDAO.getBranchToUser(this.getSessionUser().getUserid());
		List<String> dispatchbranchidlist = this.dataStatisticsService.getList(dispatchbranchidStr);
		model.addAttribute("dispatchbranchidStr", dispatchbranchidlist);
		model.addAttribute("branchList", branchList);
		return "/monitor/monitorcunhuo";
	}
	
	
}