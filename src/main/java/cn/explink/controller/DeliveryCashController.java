package cn.explink.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryCashDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.GotoClassOldDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.DeliveryCash;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.AdvancedQueryService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.DeliveryCashService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/deliverycash")
public class DeliveryCashController {
	private Logger logger = LoggerFactory.getLogger(DeliveryCashController.class);

	@Autowired
	DeliveryCashDAO deliveryCashDAO;
	@Autowired
	DeliveryCashService deliveryCashService;

	@Autowired
	UserDAO userDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;

	@Autowired
	GotoClassOldDAO gotoClassOldDAO;

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	AdvancedQueryService advancedQueryService;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list")
	public String list(Model model, @RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchid,
			@RequestParam(value = "deliverid", defaultValue = "0", required = false) long deliveryid, @RequestParam(value = "flowordertype", defaultValue = "0", required = false) long flowordertype,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "deliverystate", defaultValue = "", required = false) String[] deliverystate,
			@RequestParam(value = "paybackfeeIsZero", defaultValue = "-1", required = false) Integer paybackfeeIsZero) {

		if (flowordertype > 0) {
			logger.info(
					"小件员工作量统计，操作人{}，选择条件begindate:" + begindate + ",enddate:" + enddate + ",deliveryid:" + deliveryid + ",flowordertype:" + flowordertype + ",dispatchbranchid:"
							+ dataStatisticsService.getStrings(dispatchbranchid) + ",dispatchbranchid:" + dataStatisticsService.getStrings(dispatchbranchid) + ",deliverystate:"
							+ dataStatisticsService.getStrings(deliverystate), getSessionUser().getRealname());
		}
		List<Customer> customerList = customerDAO.getAllCustomers();
		long width = 800;
		if (customerList.size() > 3) {
			width = width + (100 * (customerList.size() - 3));
		}
		model.addAttribute("width", width);
		String branchids = "-1";
		for (String branchid : dispatchbranchid) {
			branchids = branchids + "," + branchid;
		}
		List<User> deliverList = userDAO.getAllUserByRolesAndBranchids(branchids);
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));

		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			if (branchnameList.size() == 0) {
				branchnameList.add(branch);
			} else {
				if (!dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
					branchnameList.add(branch);
				}
			}
		}
		StringBuffer deliverystates = new StringBuffer();
		deliverystates = deliverystates.append(",");
		for (int i = 0; i < deliverystate.length; i++) {
			deliverystates = deliverystates.append(deliverystate[i]).append(",");
		}
		List<String> customerSorList = deliveryCashService.getCustomerListForSummary(deliverList, deliveryid, flowordertype, begindate, enddate, deliverystate, paybackfeeIsZero);
		customerList = deliveryCashService.resetCustomer(customerList, customerSorList);
		model.addAttribute("deliverystate", deliverystates.toString());
		model.addAttribute("branchList", branchnameList);
		model.addAttribute("deliverList", deliverList);
		model.addAttribute("customerList", customerList);
		List<String> dispatchbranchidlist = dataStatisticsService.getList(dispatchbranchid);
		model.addAttribute("dispatchbranchidStr", dispatchbranchidlist);
		if (dispatchbranchid.length == 0) {
			return "deliverycash/deliverycashlist";
		}
		// 最外层的是变量存储 第二层是小件员 第三层是供货商 对应金额或者数量
		Map<String, Map<Long, Map<Long, BigDecimal>>> summary = deliveryCashService.getSummary(customerList, deliverList, deliveryid, flowordertype, begindate, enddate, deliverystate,
				paybackfeeIsZero);
		model.addAttribute("count", summary.get("count"));
		model.addAttribute("amountNOPOSMap", summary.get("amountNOPOSMap"));
		model.addAttribute("amountPOSMap", summary.get("amountPOSMap"));
		model.addAttribute("amountPaybackMap", summary.get("amountPaybackMap"));

		model.addAttribute("check", 1);

		return "deliverycash/deliverycashlist";
	}

	@RequestMapping("/show/{page}")
	public String show(Model model, @RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String dispatchbranchid,
			@RequestParam(value = "deliverid", defaultValue = "0", required = false) long deliveryid, @RequestParam(value = "flowordertype", defaultValue = "0", required = false) long flowordertype,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "deliverystate", defaultValue = "", required = false) String[] deliverystate,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") Long customerId,
			@RequestParam(value = "paybackfeeIs", required = false, defaultValue = "-1") Integer paybackfeeIs,
			@RequestParam(value = "dispatchbranchidArr", required = false, defaultValue = "") String[] dispatchbranchidArr, @PathVariable("page") int page) {
		List<Customer> customerList = customerDAO.getAllCustomers();

//		List<User> deliverList = userDAO.getAllUserByRolesAndBranchids("2,4", dispatchbranchid);
		// modify by jian_xie 20160816  不显示不在职的数据
		List<User> deliverList = userDAO.getAllUserByRolesAndBranchids(dispatchbranchid);
		
		model.addAttribute("deliverList", deliverList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("branchList", branchDAO.getAllBranches());

		List<DeliveryCash> dcList = deliveryCashService.getSummaryList(customerList, deliverList, deliveryid, flowordertype, begindate, enddate, deliverystate, customerId, paybackfeeIs);
		List<DeliveryCash> viewList = new ArrayList<DeliveryCash>();
		for (int i = (page - 1) * Page.ONE_PAGE_NUMBER; i < Page.ONE_PAGE_NUMBER * page && i < dcList.size(); i++) {
			viewList.add(dcList.get(i));
		}
		model.addAttribute("dispatchbranchids", dispatchbranchid);
		model.addAttribute("paybackfeeIs", paybackfeeIs);
		model.addAttribute("dispatchbranchidArr", dispatchbranchidArr);
		model.addAttribute("list", viewList);
		model.addAttribute("page_obj", new Page(dcList.size(), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("deliverystate", deliverystate);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));

		return "deliverycash/show";
	}

	@RequestMapping("/deliverycashsearchdetail_excel")
	public void deliverycashsearchdetail_excel(Model model, HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String dispatchbranchid,
			@RequestParam(value = "deliverid", defaultValue = "0", required = false) long deliveryid, @RequestParam(value = "flowordertype", defaultValue = "0", required = false) long flowordertype,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "paybackfeeIs", required = false, defaultValue = "-1") Integer paybackfeeIs,
			@RequestParam(value = "deliverystate", defaultValue = "", required = false) String[] deliverystate,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") Long customerId) {
		List<Customer> customerList = customerDAO.getAllCustomers();

//		List<User> deliverList = userDAO.getAllUserByRolesAndBranchids("2,4", dispatchbranchid);
		// modify by jian_xie 20160816  不显示不在职的数据
		List<User> deliverList = userDAO.getAllUserByRolesAndBranchids(dispatchbranchid);
		model.addAttribute("deliverList", deliverList);
		model.addAttribute("customerList", customerList);

		List<String> dccwbList = deliveryCashService.getSummaryCwbList(customerList, deliverList, deliveryid, flowordertype, begindate, enddate, deliverystate, customerId, paybackfeeIs);

		try {
			logger.info("小件员工作量统计大数据导出数据: 导出数据条数" + dccwbList.size() + "");
		} catch (Exception e) {
			logger.error("小件员工作量统计大数据导出数据异常");
		}
		advancedQueryService.batchSelectExport(response, request, dccwbList);

	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody String onException(Exception e) {
		logger.error("", e);
		return "{\"errorCode\":1,\"error\":\"系统错误:\"" + e.getMessage() + "}";
	}

	@ExceptionHandler(CwbException.class)
	public @ResponseBody String onCwbException(CwbException e) {
		logger.error("", e);
		return "{\"errorCode\":1,\"cwb\":\"" + e.getCwb() + "\",\"error\":\"系统错误:\"" + e.getMessage() + "}";
	}

	@RequestMapping("/exportForDelivery")
	public void export(HttpServletResponse response, @RequestParam(value = "dispatchbranchid1", required = false, defaultValue = "") String[] dispatchbranchid,
			@RequestParam(value = "deliverid1", defaultValue = "0", required = false) long deliveryid,
			@RequestParam(value = "flowordertype1", defaultValue = "0", required = false) long flowordertype,
			@RequestParam(value = "begindate1", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate1", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "deliverystate1", defaultValue = "", required = false) String[] deliverystate,
			@RequestParam(value = "paybackfeeIsZero1", defaultValue = "-1", required = false) Integer paybackfeeIsZero) {
		List<Customer> customerList = customerDAO.getAllCustomers();
		String branchids = "-1";
		for (String branchid : dispatchbranchid) {
			branchids = branchids + "," + branchid;
		}
		List<User> deliverList = userDAO.getAllUserByRolesAndBranchids(branchids);
		// 最外层的是变量存储 第二层是小件员 第三层是供货商 对应金额或者数量
		Map<String, Map<Long, Map<Long, BigDecimal>>> summary = deliveryCashService.getSummary(customerList, deliverList, deliveryid, flowordertype, begindate, enddate, deliverystate,
				paybackfeeIsZero);
		if (summary.isEmpty()) {
			return;
		}
		List<String> customerSorList = deliveryCashService.getCustomerListForSummary(deliverList, deliveryid, flowordertype, begindate, enddate, deliverystate, paybackfeeIsZero);
		
		deliveryCashService.export(summary, response, customerSorList);
		
		//以下处理打印日志
		Map<Long, Map<Long, BigDecimal>> countMap = summary.get("count");
		Set<Long> userids = countMap.keySet();
		String useridsStr = getUserString(userids);
		List<User> userList = userDAO.getAllUserByUserIds(useridsStr);
		int count = userList == null?0:userList.size();
		String dataJson = this.setexportDataJson(dispatchbranchid, deliveryid, flowordertype, begindate, enddate, deliverystate, paybackfeeIsZero);
		String filename = "小件员工作量统计" + DateTimeUtil.getNowDate() + ".xlsx";
		//记录打印日志
		this.auditExportExcel(dataJson, filename, count, this.getSessionUser().getUserid());
	}

	private String setexportDataJson(String[] dispatchbranchid, long deliveryid, long flowordertype, String begindate, String enddate, String[] deliverystate, Integer paybackfeeIsZero){
		JSONObject json = new JSONObject();
		json.put("dispatchbranchid", dispatchbranchid);
		json.put("deliveryid", deliveryid);
		json.put("flowordertype", flowordertype);
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("deliverystate", deliverystate);
		json.put("paybackfeeIsZero", paybackfeeIsZero);
		return json.toString();
	}
	
	/**
	 * 记录所有导出文件的操作
	 * 
	 * @param request
	 * @param dataJson
	 * @param userid
	 */
	private void auditExportExcel(String dataJson, String fileName, long count, long userid) {
		try{
			String logStr = String.format(
					"UserId [%s] was exported the excel file:[name: %s, line-count: %d] by using conditions [%s]", userid, fileName, count, dataJson);
			logger.info(logStr);
		}catch(Exception e){
			logger.error("Fail to log exported file info");
		}
	}
	
	private String getUserString(Set<Long> userids) {
		String strs = "";
		if (userids.size() > 0) {
			for (Long str : userids) {
				strs += "'" + str + "',";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;

	}
	
	@RequestMapping("/updateDeliverByBranchids")
	public @ResponseBody String updateDeliverByBranchids(Model model, @RequestParam("branchid") String branchids) {
		if (branchids.length() > 0) {
			branchids = branchids.substring(0, branchids.length() - 1);
			List<User> list = this.userDAO.getAllUserByRolesAndBranchids(branchids);
			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}
	}
}
