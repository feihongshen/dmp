package cn.explink.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.MonitorDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.DeliveryService;
import cn.explink.service.MonitorService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.Page;

@Controller
@RequestMapping("/monitor")
public class MonitorController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	MonitorService monitorService;
	@Autowired
	DeliveryService deliveryService;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	BranchDAO branchDao;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	MonitorDAO monitorDAO;

	// 第一页
	@RequestMapping("/date/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "crateStartdate", required = false, defaultValue = "") String crateStartdate,
			@RequestParam(value = "crateEnddate", required = false, defaultValue = "") String crateEnddate,
			@RequestParam(value = "shipStartTime", required = false, defaultValue = "") String shipStartTime,
			@RequestParam(value = "shipEndTime", required = false, defaultValue = "") String shipEndTime,
			@RequestParam(value = "emailStartTime", required = false, defaultValue = "") String emailStartTime,
			@RequestParam(value = "eamilEndTime", required = false, defaultValue = "") String eamilEndTime,
			@RequestParam(value = "controlStrEmail", required = false, defaultValue = "") String controlStrEmail,
			@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "controlStrCustomer", required = false, defaultValue = "") String controlStrCustomer, HttpServletResponse response, HttpServletRequest request) {

		if (page != 6) {
			emailStartTime = (emailStartTime == null || emailStartTime.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 00:00:00") : emailStartTime;
			eamilEndTime = (eamilEndTime == null || eamilEndTime.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 23:59:59") : eamilEndTime;
			model.addAttribute("emailStartTime", emailStartTime);
			model.addAttribute("eamilEndTime", eamilEndTime);
		} else {
			crateStartdate = (crateStartdate == null || crateStartdate.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 00:00:00") : crateStartdate;
			eamilEndTime = (crateEnddate == null || crateEnddate.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 23:59:59") : crateEnddate;
			model.addAttribute("crateStartdate", crateStartdate);
			model.addAttribute("crateEnddate", crateEnddate);
		}
		List<EmaildateTDO> emailListByEmailTime = deliveryService.getEmaildateAndBrandidList(emailStartTime, eamilEndTime, -1);
		model.addAttribute("emailList", emailListByEmailTime);
		model.addAttribute("emailListByShiptime", emailListByEmailTime);
		model.addAttribute("emailListByEmailTime", emailListByEmailTime);
		// 查询所有的站点
		List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
		model.addAttribute("branchnameList", branchnameList);
		List<Branch> kufangList = getDmpDAO.getBranchByKufang();
		model.addAttribute("kufangList", kufangList);
		// 查询当前站点
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		long branchidPram = branchid;
		model.addAttribute("branchidSession", branchidPram);
		String emaildateid = "".equals(controlStrEmail) ? "" : controlStrEmail.substring(0, controlStrEmail.length() - 1);
		String customerid = "".equals(controlStrCustomer) ? "" : controlStrCustomer.substring(0, controlStrCustomer.length() - 1);
		// 将条件保存session中
		setSetions(request, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchidPram, branchidSession);
		if (page == 2 || page == 3 || page == 4 || page == 5 || page == 7) {
			setCustomerlist(model, customerid, branchid, dmpid);
		}
		if (page == 1) {// 数据监控
			setDateModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			return "/monitor/date/dateMonitor";
		} else if (page == 2) {// 数据监控（数据组）
			setDateGroupModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			if (emaildateid.equals("")) {
				for (EmaildateTDO emaildateTDO : emailListByEmailTime) {
					emaildateid += "'" + emaildateTDO.getEmaildateid() + "',";
				}
				request.getSession().setAttribute("emaildateYoudanwuhuo", emaildateid.length() > 0 ? emaildateid.substring(0, emaildateid.length() - 1) : "");
			}
			setYoudanwuhuo(model, emaildateid, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			return "/monitor/dateGroup/dateGroupMonitor";
		} else if (page == 3) {// 库房信息监控
			setHouseModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			if (emaildateid.equals("")) {
				for (EmaildateTDO emaildateTDO : emailListByEmailTime) {
					emaildateid += "'" + emaildateTDO.getEmaildateid() + "',";
				}
				request.getSession().setAttribute("emaildateYoudanwuhuo", emaildateid.length() > 0 ? emaildateid.substring(0, emaildateid.length() - 1) : "");
			}
			setYoudanwuhuoHouse(model, emaildateid, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			setWeirukuHouse(model, emaildateid, branchid, "", customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			return "/monitor/house/houseMonitor";
		} else if (page == 4) {// 站点信息监控
			setSiteModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchidPram);
			if (emaildateid.equals("")) {
				for (EmaildateTDO emaildateTDO : emailListByEmailTime) {
					emaildateid += "'" + emaildateTDO.getEmaildateid() + "',";
				}
				request.getSession().setAttribute("emaildateYoudanwuhuo", emaildateid.length() > 0 ? emaildateid.substring(0, emaildateid.length() - 1) : "");
			}
			setYoudanwuhuo(model, emaildateid, -1, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			return "/monitor/site/siteMonitor";
		} else if (page == 7) {// 异常信息监控
			String emaildateYoudanwuhuo = emaildateid;
			if (emaildateid.equals("")) {
				for (EmaildateTDO emaildateTDO : emailListByEmailTime) {
					emaildateYoudanwuhuo += emaildateTDO.getEmaildateid() + ",";
				}
				request.getSession().setAttribute("emaildateYoudanwuhuo", emaildateYoudanwuhuo.length() > 0 ? emaildateYoudanwuhuo.substring(0, emaildateYoudanwuhuo.length() - 1) : "");
			}
			request.getSession().setAttribute("branchidPram", branchid);
			setExpModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid, emaildateYoudanwuhuo);
			return "/monitor/exption/exptionMonitor";
		} else if (page == 8) {// 财务信息监控
			String emaildateYoudanwuhuo = emaildateid;
			if (emaildateid.equals("")) {
				for (EmaildateTDO emaildateTDO : emailListByEmailTime) {
					emaildateYoudanwuhuo += "'" + emaildateTDO.getEmaildateid() + "',";
				}
				request.getSession().setAttribute("emaildateYoudanwuhuo", emaildateYoudanwuhuo.length() > 0 ? emaildateYoudanwuhuo.substring(0, emaildateYoudanwuhuo.length() - 1) : "");
			}
			request.getSession().setAttribute("branchidPram", branchid);
			setMoneyModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid, emaildateYoudanwuhuo);
			return "/monitor/money/moneyMonitor";
		} else {
			setDateModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchidPram);
			return "/monitor/date/dateMonitor";
		}
	}

	// 第一页导出
	@RequestMapping("/dateExport/{page}")
	public String dateExp(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {
		String crateStartdate = request.getSession().getAttribute("crateStartdate").toString();
		String crateEnddate = request.getSession().getAttribute("crateEnddate").toString();
		String shipStartTime = request.getSession().getAttribute("shipStartTime").toString();
		String shipEndTime = request.getSession().getAttribute("shipEndTime").toString();
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		String emaildateid = request.getSession().getAttribute("emaildateid").toString();
		String emaildateYoudanwuhuo = request.getSession().getAttribute("emaildateYoudanwuhuo") == null ? "" : request.getSession().getAttribute("emaildateYoudanwuhuo").toString();
		String customerid = request.getSession().getAttribute("customerid").toString();
		long branchid = new Long(request.getSession().getAttribute("branchid").toString());
		Branch branch = getDmpDAO.getNowBranch(branchid);
		model.addAttribute("nowbranchName", branch == null ? "全部" : branch.getBranchname());
		if (page == 1) {
			setDateModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			return "/monitor/date/dateMonitorExpo";
		} else if (page == 2) {
			setDateGroupModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			setYoudanwuhuo(model, emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			return "/monitor/dateGroup/dateGroupMonitorExpo";
		} else if (page == 3) {
			setHouseModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			setYoudanwuhuo(model, emaildateYoudanwuhuo, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			setWeiruku(model, emaildateYoudanwuhuo, branchid, FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
					customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			return "/monitor/house/houseExpo";
		} else if (page == 4) {
			setSiteModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			setYoudanwuhuo(model, emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			return "/monitor/site/siteExpo";
		} else if (page == 7) {
			long branchidPram = new Long(request.getSession().getAttribute("branchidPram").toString());
			setExpModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchidPram, emaildateYoudanwuhuo);
			return "/monitor/exption/exptionExpo";
		} else {
			setDateModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			return "/monitor/date/dateMonitorExpo";
		}

	}

	// 查看详细页
	@RequestMapping("/dateshow/{type}/{page}")
	public String dateShow(Model model, @PathVariable("type") long type, @PathVariable("page") long page, @RequestParam(value = "flowType", required = false, defaultValue = "") String flowType,
			@RequestParam(value = "branchPramid", required = false, defaultValue = "-1") long branchPramid, HttpServletResponse response, HttpServletRequest request) {
		String crateStartdate = request.getSession().getAttribute("crateStartdate").toString();
		String crateEnddate = request.getSession().getAttribute("crateEnddate").toString();
		String shipStartTime = request.getSession().getAttribute("shipStartTime").toString();
		String shipEndTime = request.getSession().getAttribute("shipEndTime").toString();
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		String emaildateid = request.getSession().getAttribute("emaildateid").toString();
		String emaildateYoudanwuhuo = request.getSession().getAttribute("emaildateYoudanwuhuo") == null ? "" : request.getSession().getAttribute("emaildateYoudanwuhuo").toString();
		String customerid = request.getSession().getAttribute("customerid").toString();
		long branchid = new Long(request.getSession().getAttribute("branchid").toString());

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		if ("".equals(flowType)) {
			flowType = (String) request.getSession().getAttribute("flowType");
		} else {
			request.getSession().setAttribute("flowType", flowType);
		}
		if (type == 1) {
			showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchid, page);
			return "/monitor/date/dateShow";
		} else if (type == 2) {
			if (flowType.equals("ydwh")) {
				showDateYouhuowudan(model, emaildateYoudanwuhuo, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, page);
			} else {
				showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchid, page);
			}
			return "/monitor/dateGroup/dateGroupShow";
		} else if (type == 3) {
			if (flowType.equals("ydwh")) {
				showDateYouhuowudan(model, emaildateYoudanwuhuo, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, page);
			} else if (flowType.equals("wrk")) {
				showDateWeiruku(model, emaildateYoudanwuhuo, branchid,
						FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), customerid, crateStartdate,
						crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, page);
			} else if (flowType.equals("kc")) {
				showDateKucun(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, FlowOrderTypeEnum.TiHuo.getValue() + ","
						+ FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), customerid, branchid, page);
			} else {
				showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchid, page);
			}
			return "/monitor/house/houseShow";
		} else if (type == 4) {
			if (flowType.equals("ydwh")) {
				showDateYouhuowudan(model, emaildateYoudanwuhuo, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, page);
			} else {
				showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchid, page);
			}
			return "/monitor/site/siteShow";
		} else if (type == 7) {
			if (flowType.equals("ydwh")) {
				showDateYouhuowudan(model, emaildateYoudanwuhuo, branchPramid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, page);
			} else {
				showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchPramid, page);
			}
			request.getSession().setAttribute("branchPramid", branchPramid);
			return "/monitor/exption/exptionShow";
		} else {
			showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchid, page);
			return "/monitor/date/dateShow";
		}

	}

	// 详细页导出
	@RequestMapping("/dateShowExport/{page}")
	public String dateShowExp(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {
		String crateStartdate = request.getSession().getAttribute("crateStartdate").toString();
		String crateEnddate = request.getSession().getAttribute("crateEnddate").toString();
		String shipStartTime = request.getSession().getAttribute("shipStartTime").toString();
		String shipEndTime = request.getSession().getAttribute("shipEndTime").toString();
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		String emaildateid = request.getSession().getAttribute("emaildateid").toString();
		String emaildateYoudanwuhuo = request.getSession().getAttribute("emaildateYoudanwuhuo") == null ? "" : request.getSession().getAttribute("emaildateYoudanwuhuo").toString();
		String customerid = request.getSession().getAttribute("customerid").toString();
		String flowType = request.getSession().getAttribute("flowType").toString();
		long branchid = new Long(request.getSession().getAttribute("branchid").toString());
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		if (page == 1) {
			showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchid, -1);
			return "/monitor/date/dateMonitorShowExpo";
		} else if (page == 2) {
			showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchid, -1);
			return "/monitor/dateGroup/dateGroupMonitorShowExpo";
		} else if (page == 3) {
			if (flowType.equals("ydwh")) {
				showDateYouhuowudan(model, emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, -1);
			} else if (flowType.equals("wrk")) {
				showDateWeiruku(model, emaildateYoudanwuhuo, -1,
						FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), customerid, crateStartdate,
						crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, -1);
			} else if (flowType.equals("kc")) {
				showDateKucun(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateYoudanwuhuo, "2,4,7", customerid, branchid, -1);
			} else {
				showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchid, -1);
			}
			return "/monitor/house/houseShowExpo";
		} else if (page == 4) {
			if (flowType.equals("ydwh")) {
				showDateYouhuowudan(model, emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, -1);
			} else {
				showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchid, -1);
			}
			return "/monitor/site/siteShowExpo";
		} else if (page == 7) {
			long branchPramid = new Long(request.getSession().getAttribute("branchPramid").toString());
			if (flowType.equals("ydwh")) {
				showDateYouhuowudan(model, emaildateYoudanwuhuo, branchPramid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, -1);
			} else {
				showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchPramid, -1);
			}
			return "/monitor/exption/exptionShowExpo";
		} else {
			showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowType, customerid, branchid, -1);
			return "/monitor/date/dateMonitorShowExpo";
		}

	}

	// 返回第一页面
	@RequestMapping("/back/{page}")
	public String goback(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {
		String crateStartdate = request.getSession().getAttribute("crateStartdate").toString();
		String crateEnddate = request.getSession().getAttribute("crateEnddate").toString();
		String shipStartTime = request.getSession().getAttribute("shipStartTime").toString();
		String shipEndTime = request.getSession().getAttribute("shipEndTime").toString();
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		String emaildateid = request.getSession().getAttribute("emaildateid").toString();
		String emaildateYoudanwuhuo = request.getSession().getAttribute("emaildateYoudanwuhuo") == null ? "" : request.getSession().getAttribute("emaildateYoudanwuhuo").toString();
		String customerid = request.getSession().getAttribute("customerid").toString();
		long branchid = new Long(request.getSession().getAttribute("branchid").toString());
		List<EmaildateTDO> emailListByShiptime = deliveryService.getEmaildateAndBrandidList(shipStartTime, shipEndTime, -1);
		List<EmaildateTDO> emailListByEmailTime = deliveryService.getEmaildateAndBrandidList(emailStartTime, eamilEndTime, -1);
		model.addAttribute("emailList", emailListByShiptime);
		model.addAttribute("emailListByShiptime", emailListByShiptime);
		model.addAttribute("emailListByEmailTime", emailListByEmailTime);
		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);
		// 查询所有的站点
		List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
		model.addAttribute("branchnameList", branchnameList);
		List<Branch> kufangList = getDmpDAO.getBranchByKufang();
		model.addAttribute("kufangList", kufangList);
		model.addAttribute("branchid", branchid);

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		if (page == 1) {
			setDateModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			return "/monitor/date/dateMonitor";
		} else if (page == 2) {
			setCustomerlist(model, customerid, branchid, dmpid);
			setDateGroupModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			setYoudanwuhuo(model, emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			return "/monitor/dateGroup/dateGroupMonitor";
		} else if (page == 3) {
			setCustomerlist(model, customerid, branchid, dmpid);
			setHouseModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			setYoudanwuhuo(model, emaildateYoudanwuhuo, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			setWeiruku(model, emaildateYoudanwuhuo, branchid, FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
					customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			return "/monitor/house/houseMonitor";
		} else if (page == 4) {
			setCustomerlist(model, customerid, branchid, dmpid);
			setSiteModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			setYoudanwuhuo(model, emaildateYoudanwuhuo, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
			return "/monitor/site/siteMonitor";
		} else if (page == 7) {
			long branchidPram = new Long(request.getSession().getAttribute("branchidPram").toString());
			setCustomerlist(model, customerid, branchidPram, dmpid);
			setExpModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchidPram, emaildateYoudanwuhuo);
			return "/monitor/exption/exptionMonitor";
		} else {
			setDateModels(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, customerid, branchid);
			return "/monitor/date/dateMonitor";
		}
	}

	// ======以下都是封装方法=============/////
	// 存储数据监控数据
	private void setDateModels(Model model, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String customerid, long branchid) {
		// 导入数据
		model.addAttribute("daoruDate", monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
				FlowOrderTypeEnum.DaoRuShuJu.getValue() + "", customerid, -1, branchid));
		model.addAttribute("daoruDateType", "");
		model.addAttribute("daoruDateTypeDelivery", "");
		// 库房
		model.addAttribute("kufangDate", monitorService.getMonitorDate(
				crateStartdate,
				crateEnddate,
				shipStartTime,
				shipEndTime,
				emailStartTime,
				eamilEndTime,
				emaildateid,
				FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue() + ","
						+ FlowOrderTypeEnum.YouHuoWuDan.YouHuoWuDan.getValue(), customerid, 1, branchid));
		model.addAttribute("kufangDateType", FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue() + ","
				+ FlowOrderTypeEnum.YouHuoWuDan.YouHuoWuDan.getValue());
		model.addAttribute("kufangDateTypeDelivery", "");
		// 在途
		model.addAttribute(
				"zaituDate",
				monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + ","
						+ FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue()
						+ "," + FlowOrderTypeEnum.TuiHuoZhanZaiTouSaoMiao.getValue() + "," + FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), customerid, 1, branchid));
		model.addAttribute(
				"zaituDateType",
				FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue() + ","
						+ FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.TuiHuoZhanZaiTouSaoMiao.getValue() + ","
						+ FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
		model.addAttribute("zaituDateTypeDelivery", "");
		// 站
		model.addAttribute(
				"zhanDate",
				monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()
						+ "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), customerid, 1, branchid));
		model.addAttribute("zhanDateType", FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		model.addAttribute("zhanDateTypeDelivery", "");
		// 人
		model.addAttribute("renDate", monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
				FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "", customerid, 1, branchid));
		model.addAttribute("renDateType", FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		model.addAttribute("renDateTypeDelivery", "");
		// 退货站
		model.addAttribute(
				"tuihuoDate",
				monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + ","
						+ FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getValue(), customerid, 1, branchid));
		model.addAttribute("tuihuoDateType", FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "," + FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getValue());
		model.addAttribute("tuihuoDateTypeDelivery", "");
		// 中转站
		model.addAttribute(
				"zhongzhuanDate",
				monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()
						+ "," + FlowOrderTypeEnum.ZhongZhuanZhanYouHuoWuDanRuKu.getValue(), customerid, 1, branchid));
		model.addAttribute("zhongzhuanDateType", FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanYouHuoWuDanRuKu.getValue());
		model.addAttribute("zhongzhuanDateTypeDelivery", "");
		// 成功
		model.addAttribute("chenggongDate", monitorService.getMonitorDeliveryDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
				DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue(), "", customerid, 1,
				branchid));
		model.addAttribute("chenggongDateType",
				DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue());
		model.addAttribute("chenggongDateTypeDelivery", "");
		// 丢失
		model.addAttribute(
				"diushiDate",
				monitorService.getMonitorDeliveryDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, DeliveryStateEnum.HuoWuDiuShi.getValue()
						+ "", "", customerid, 1, branchid));
		model.addAttribute("diushiDateType", DeliveryStateEnum.HuoWuDiuShi.getValue());
		model.addAttribute("diushiDateTypeDelivery", "");
		// 异常
		model.addAttribute("yichangDate", monitorService.getMonitorDeliveryDate(
				crateStartdate,
				crateEnddate,
				shipStartTime,
				shipEndTime,
				emailStartTime,
				eamilEndTime,
				emaildateid,
				DeliveryStateEnum.QuanBuTuiHuo.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.FenZhanZhiLiu.getValue() + ","
						+ DeliveryStateEnum.ShangMenJuTui.getValue(), FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + "", customerid, 1, branchid));
		model.addAttribute("yichangDateType", DeliveryStateEnum.QuanBuTuiHuo.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.FenZhanZhiLiu.getValue() + ","
				+ DeliveryStateEnum.ShangMenJuTui.getValue());
		model.addAttribute("yichangDateTypeDelivery", FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + "");
		// 差
		model.addAttribute("chaDate", monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, "35", customerid, 1, branchid));
		model.addAttribute("chaDateType", "35");
		model.addAttribute("chaDateTypeDelivery", "");
	}

	// 存储数据组数据
	private void setDateGroupModels(Model model, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String customerid, long branchid) {
		// 导入数据
		model.addAttribute("daoruDate", monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
				FlowOrderTypeEnum.DaoRuShuJu.getValue() + "", customerid, -1, branchid));
		model.addAttribute("daoruDateType", "");
		// 有单无货

		// 有货无单
		model.addAttribute(
				"youhuowudanDate",
				monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue() + ","
						+ FlowOrderTypeEnum.YouHuoWuDan.getValue(), customerid, 1, branchid));
		model.addAttribute("youhuowudanDateType", FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue() + "," + FlowOrderTypeEnum.YouHuoWuDan.getValue());

	}

	// 存储库房信息
	private void setHouseModels(Model model, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String customerid, long branchid) {
		// 导入数据
		model.addAttribute("daoruDate", monitorService.getHouseDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, "", customerid, -1, branchid));
		model.addAttribute("daoruDateType", "");
		// 未入库

		// 已入库
		model.addAttribute(
				"yirukuDate",
				monitorService.getHouseDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, FlowOrderTypeEnum.TiHuo.getValue() + ","
						+ FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.YouHuoWuDan.getValue(), customerid, 1, branchid));
		model.addAttribute("yirukuDateType", FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		// 有单无货
		// 有货无单
		model.addAttribute(
				"youhuowudanDate",
				monitorService.getHouseDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue() + ","
						+ FlowOrderTypeEnum.YouHuoWuDan.getValue(), customerid, 1, branchid));
		model.addAttribute("youhuowudanDateType", FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue() + "," + FlowOrderTypeEnum.YouHuoWuDan.getValue());
		// 出库在途
		model.addAttribute("chukuzaituDate", monitorService.getHouseDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
				FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "", customerid, 1, branchid));
		model.addAttribute("chukuzaituDateType", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		// 库存
		model.addAttribute(
				"kucunDate",
				monitorService.getHouseDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, FlowOrderTypeEnum.TiHuo.getValue() + ","
						+ FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.YouHuoWuDan.getValue(), customerid, 1, branchid));
		model.addAttribute("kucunDateType", "kc");
		//
		// model.addAttribute("youdanwuhuoDate",
		// monitorService.getMonitorDateYoudanwuhuoHouse(emaildateid, branchid,
		// customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
		// emailStartTime, eamilEndTime));
		// model.addAttribute("youdanwuhuoDateType", "ydwh");
		//
		// model.addAttribute("weirukuDate",
		// monitorService.getMonitorDateWeirukuHouse(emaildateid, branchid, "",
		// customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
		// emailStartTime, eamilEndTime));
		// model.addAttribute("weirukuDateType", "wrk");

	}

	// 站点信息
	private void setSiteModels(Model model, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String customerid, long branchid) {
		// 导入数据
		model.addAttribute("daoruDate", monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, "", customerid, -1, -1));
		model.addAttribute("daoruDateType", "");
		model.addAttribute("daoruDateTypeDelivery", "");
		// 未到货
		model.addAttribute("weidaohuoDate", monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
				FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "", customerid, 1, branchid));
		model.addAttribute("weidaohuoDateType", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("weidaohuoDateTypeDelivery", "");
		// 入库未领
		model.addAttribute(
				"rukuweilingDate",
				monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()
						+ "", customerid, 1, branchid));
		model.addAttribute("rukuweilingDateType", FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		model.addAttribute("rukuweilingDateTypeDelivery", "");
		// 有货无单
		model.addAttribute(
				"youhuowudanDate",
				monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
						FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "", customerid, 1, branchid));
		model.addAttribute("youhuowudanDateType", FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		model.addAttribute("youhuowudanDateTypeDelivery", "");
		// 异常单
		model.addAttribute("yichangdanDate",
				monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, "29", customerid, 1, branchid));
		model.addAttribute("yichangdanDateType", "29");
		model.addAttribute("yichangdanDateTypeDelivery", "");
		// 已领货
		model.addAttribute("yilinghuoDate", monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
				FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "", customerid, 1, branchid));
		model.addAttribute("yilinghuoDateType", FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		model.addAttribute("yilinghuoDateTypeDelivery", "");
		// 库存退货
		model.addAttribute(
				"kucuntuihuoDate",
				monitorService.getMonitorDeliveryDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
						DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.QuanBuTuiHuo.getValue() + ","
								+ DeliveryStateEnum.BuFenTuiHuo.getValue(), FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + "", customerid, 1, branchid));
		model.addAttribute("kucuntuihuoDateType",
				DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.QuanBuTuiHuo.getValue() + ","
						+ DeliveryStateEnum.BuFenTuiHuo.getValue());
		model.addAttribute("kucuntuihuoDateTypeDelivery", FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + "");
		// 库存滞留
		model.addAttribute(
				"kucunzhiliuDate",
				monitorService.getMonitorDeliveryDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, DeliveryStateEnum.FenZhanZhiLiu.getValue()
						+ "", "", customerid, 1, branchid));
		model.addAttribute("kucunzhiliuDateType", DeliveryStateEnum.FenZhanZhiLiu.getValue());
		model.addAttribute("kucunzhiliuDateTypeDelivery", "");
		// 在途中转
		// model.addAttribute("zaituzhongzhuanDate",
		// monitorService.getMonitorDate(crateStartdate, crateEnddate,
		// shipStartTime, shipEndTime, emailStartTime, eamilEndTime,
		// emaildateid, FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getValue()+"",
		// customerid,1,branchid));
		// model.addAttribute("zaituzhongzhuanDateType",
		// FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getValue());
		// model.addAttribute("zaituzhongzhuanDateTypeDelivery", "");
		// 在途退货
		// model.addAttribute("zaitutuihuoDate",
		// monitorService.getMonitorDate(crateStartdate, crateEnddate,
		// shipStartTime, shipEndTime, emailStartTime, eamilEndTime,
		// emaildateid, FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue()+"",
		// customerid,1,branchid));
		// model.addAttribute("zaitutuihuoDateType",
		// FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue());
		// model.addAttribute("zaitutuihuoDateTypeDelivery", "");
		// 未交款
		model.addAttribute(
				"weijiaokuanDate",
				monitorService.getMonitorDeliveryDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
						DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + ","
								+ DeliveryStateEnum.BuFenTuiHuo.getValue(), "", customerid, 1, branchid));
		model.addAttribute("weijiaokuanDateType", DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ","
				+ DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue());
		model.addAttribute("weijiaokuanDateTypeDelivery", "");
		// 遗留单 超过一天
		model.addAttribute("yiliudanDate", monitorService.getMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid,
				FlowOrderTypeEnum.RuKu.getValue() + "", customerid, 1, branchid));
		model.addAttribute("yiliudanDateType", FlowOrderTypeEnum.RuKu.getValue());
		model.addAttribute("yiliudanDateTypeDelivery", "");
		// 欠款
		model.addAttribute(
				"qiankuanDate",
				monitorService.getMonitorDeliveryDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, DeliveryStateEnum.HuoWuDiuShi.getValue()
						+ "", "", customerid, 1, branchid));
		model.addAttribute("qiankuanDateType", DeliveryStateEnum.HuoWuDiuShi.getValue() + "");
		model.addAttribute("qiankuanDateTypeDelivery", "");

		// 已领货：9 （所有已领货未反馈 按人）
		// -遗留单：（入库未派 超过一天）
		// 库存 -退货：（19、20、21、22、28）
		// -滞留：23

		// 欠款：25、30 -31
	}

	// 有单无货 统计数量
	private void setYoudanwuhuo(Model model, String emaildateid, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {
		model.addAttribute("youdanwuhuoDate",
				monitorService.getMonitorDateYoudanwuhuo(emaildateid, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime));
		model.addAttribute("youdanwuhuoDateType", "ydwh");
	}

	// 未入库 统计数量
	private void setWeiruku(Model model, String emaildateid, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate, String shipStartTime,
			String shipEndTime, String emailStartTime, String eamilEndTime) {
		model.addAttribute("weirukuDate",
				monitorService.getMonitorDateWeiruku(emaildateid, branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime));
		model.addAttribute("weirukuDateType", "wrk");
	}

	// 有单无货 统计数量
	private void setYoudanwuhuoHouse(Model model, String emaildateid, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {
		model.addAttribute("youdanwuhuoDate",
				monitorService.getMonitorDateYoudanwuhuoHouse(emaildateid, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime));
		model.addAttribute("youdanwuhuoDateType", "ydwh");
	}

	// 未入库 统计数量
	private void setWeirukuHouse(Model model, String emaildateid, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate, String shipStartTime,
			String shipEndTime, String emailStartTime, String eamilEndTime) {
		model.addAttribute("weirukuDate",
				monitorService.getMonitorDateWeirukuHouse(emaildateid, branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime));
		model.addAttribute("weirukuDateType", "wrk");
	}

	// 查询具体订单数据
	private void showDate(Model model, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String flowordertype, String customerid, long branchid, long page) {
		model.addAttribute("showDateList",
				monitorService.getMonitorOrderList(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowordertype, customerid, 1, branchid, page));
		model.addAttribute(
				"page_obj",
				new Page(monitorService.getMonitorOrderListCount(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowordertype, customerid, 1,
						branchid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
	}

	// 有单无货 查看
	private void showDateYouhuowudan(Model model, String emaildateid, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime, long page) {
		model.addAttribute("showDateList",
				monitorService.getMonitorOrderYoudanwuhuoList(emaildateid, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, page));
		model.addAttribute("page_obj",
				new Page(monitorService.getMonitorOrderYoudanwuhuoListCount(emaildateid, branchid, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime),
						page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);

	}

	// 未入库 查看
	private void showDateWeiruku(Model model, String emaildateid, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate, String shipStartTime,
			String shipEndTime, String emailStartTime, String eamilEndTime, long page) {
		model.addAttribute("showDateList", monitorService.getMonitorOrderWeirukuList(emaildateid, branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
				emailStartTime, eamilEndTime, page));
		model.addAttribute(
				"page_obj",
				new Page(monitorService.getMonitorOrderWeirukuListCount(emaildateid, branchid, flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
						eamilEndTime), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);

	}

	// 查询库存
	private void showDateKucun(Model model, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String flowordertype, String customerid, long branchid, long page) {
		model.addAttribute("showDateList",
				monitorService.getMonitorOrderList(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowordertype, customerid, -1, branchid, page));
		model.addAttribute(
				"page_obj",
				new Page(monitorService.getMonitorOrderListCount(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, flowordertype, customerid, -1,
						branchid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
	}

	// 异常信息监控
	private void setExpModels(Model model, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String customerid, long branchid, String emaildateYoudanwuhuo) {
		List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
		Map map = new HashMap();
		if (branchnameList != null && branchnameList.size() > 0 && branchid < 0) {
			List<MonitorDTO> youhuowudanList = monitorDAO.getExptForBranch_youhuowudan(emaildateYoudanwuhuo, branchid, emailStartTime, eamilEndTime);
			List<MonitorDTO> yichangdanList = monitorService.getExpMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, "29", customerid,
					1, branchid);
			List<MonitorDTO> youdanwuhuoList = monitorDAO.getExptForBranch_youdanwuhuo(emaildateYoudanwuhuo, branchid, emailStartTime, eamilEndTime);
			for (int i = 0; i < branchnameList.size(); i++) {
				Map map1 = new HashMap();
				if (youhuowudanList != null && youhuowudanList.size() > 0) {
					for (int j = 0; j < youhuowudanList.size(); j++) {
						if (youhuowudanList.get(j).getBranchid() == branchnameList.get(i).getBranchid()) {
							map1.put("youhuowudan_" + branchnameList.get(i).getBranchid(), youhuowudanList.get(j).getCountsum());
							break;
						}
					}
				}
				if (yichangdanList != null && yichangdanList.size() > 0) {
					for (int j = 0; j < yichangdanList.size(); j++) {
						if (yichangdanList.get(j).getBranchid() == branchnameList.get(i).getBranchid()) {
							map1.put("yichangdan_" + branchnameList.get(i).getBranchid(), yichangdanList.get(j).getCountsum());
							break;
						}
					}
				}
				if (youdanwuhuoList != null && youdanwuhuoList.size() > 0) {
					for (int j = 0; j < youdanwuhuoList.size(); j++) {
						if (youdanwuhuoList.get(j).getBranchid() == branchnameList.get(i).getBranchid()) {
							map1.put("youdanwuhuo_" + branchnameList.get(i).getBranchid(), youdanwuhuoList.get(j).getCountsum());
							break;
						}
					}
				}
				map.put(branchnameList.get(i).getBranchid(), map1);
			}

		} else if (branchid > 0) {
			List<MonitorDTO> youhuowudanList = monitorDAO.getExptForBranch_youhuowudan(emaildateYoudanwuhuo, branchid, emailStartTime, eamilEndTime);
			List<MonitorDTO> yichangdanList = monitorService.getExpMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, "29", customerid,
					1, branchid);
			List<MonitorDTO> youdanwuhuoList = monitorDAO.getExptForBranch_youdanwuhuo(emaildateYoudanwuhuo, branchid, emailStartTime, eamilEndTime);

			Map map1 = new HashMap();
			if (youhuowudanList != null && youhuowudanList.size() > 0) {
				for (int j = 0; j < youhuowudanList.size(); j++) {
					if (youhuowudanList.get(j).getBranchid() == branchid) {
						map1.put("youhuowudan_" + branchid, youhuowudanList.get(j).getCountsum());
						break;
					}
				}
			}
			if (yichangdanList != null && yichangdanList.size() > 0) {
				for (int j = 0; j < yichangdanList.size(); j++) {
					if (yichangdanList.get(j).getBranchid() == branchid) {
						map1.put("yichangdan_" + branchid, yichangdanList.get(j).getCountsum());
						break;
					}
				}
			}
			if (youdanwuhuoList != null && youdanwuhuoList.size() > 0) {
				for (int j = 0; j < youdanwuhuoList.size(); j++) {
					if (youdanwuhuoList.get(j).getBranchid() == branchid) {
						map1.put("youdanwuhuo_" + branchid, youdanwuhuoList.get(j).getCountsum());
						break;
					}
				}
			}
			map.put(branchid, map1);
		}
		model.addAttribute("branchObjectNow", branchid > 0 ? getDmpDAO.getNowBranch(branchid) : null);
		model.addAttribute("branchidNow", branchid);
		model.addAttribute("exptionMap", map);
		model.addAttribute("youhuowudanDateType", FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		model.addAttribute("yichangdanDateType", "29");
		model.addAttribute("youdanwuhuoDateType", "ydwh");
		// 异常单

	}

	// 财务信息监控
	private void setMoneyModels(Model model, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildateid,
			String customerid, long branchid, String emaildateYoudanwuhuo) {
		List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
		Map map = new HashMap();
		if (branchnameList != null && branchnameList.size() > 0 && branchid < 0) {
			List<MonitorDTO> yirukuList = monitorService.getExpMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, "2,4,7", customerid,
					-1, branchid);
			List<MonitorDTO> yijiaokuanList = monitorService.getExpMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, "30", customerid,
					1, branchid);
			List<MonitorDTO> qiankuanList = monitorService.getExpMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, "30", customerid, 1,
					branchid);
			List<MonitorDTO> kucunList = monitorService.getExpMonitorDate(crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildateid, "2,4,7", customerid, 1,
					branchid);
			for (int i = 0; i < branchnameList.size(); i++) {
				Map map1 = new HashMap();
				if (yirukuList != null && yirukuList.size() > 0) {
					for (int j = 0; j < yirukuList.size(); j++) {
						if (yirukuList.get(j).getBranchid() == branchnameList.get(i).getBranchid()) {
							map1.put("yiruku_" + branchnameList.get(i).getBranchid(), yirukuList.get(j).getCountsum());
							break;
						}
					}
				}
				if (yijiaokuanList != null && yijiaokuanList.size() > 0) {
					for (int j = 0; j < yijiaokuanList.size(); j++) {
						if (yijiaokuanList.get(j).getBranchid() == branchnameList.get(i).getBranchid()) {
							map1.put("yijiaokuan_" + branchnameList.get(i).getBranchid(), yijiaokuanList.get(j).getCaramountsum());
							break;
						}
					}
				}
				if (qiankuanList != null && qiankuanList.size() > 0) {
					for (int j = 0; j < qiankuanList.size(); j++) {
						if (qiankuanList.get(j).getBranchid() == branchnameList.get(i).getBranchid()) {
							map1.put("qiankuan_" + branchnameList.get(i).getBranchid(), qiankuanList.get(j).getCaramountsum());
							break;
						}
					}
				}
				if (kucunList != null && kucunList.size() > 0) {
					for (int j = 0; j < kucunList.size(); j++) {
						if (kucunList.get(j).getBranchid() == branchnameList.get(i).getBranchid()) {
							map1.put("kucun_" + branchnameList.get(i).getBranchid(), kucunList.get(j).getCountsum());
							break;
						}
					}
				}

				map.put(branchnameList.get(i).getBranchid(), map1);
			}

		}
		model.addAttribute("moneyMap", map);
		model.addAttribute("yirukuDateType", FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		model.addAttribute("yijiaokuanDateType", "30");
		model.addAttribute("qiankuanDateType", "qiank");
		model.addAttribute("kucunDateType", "kc");
		// 异常单

	}

	// 保存基本查询条件的session
	private void setSetions(HttpServletRequest request, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime,
			String emaildateid, String customerid, long branchid, long branchidSession) {
		request.getSession().setAttribute("crateStartdate", crateStartdate);
		request.getSession().setAttribute("crateEnddate", crateEnddate);
		request.getSession().setAttribute("shipStartTime", shipStartTime);
		request.getSession().setAttribute("shipEndTime", shipEndTime);
		request.getSession().setAttribute("emailStartTime", emailStartTime);
		request.getSession().setAttribute("eamilEndTime", eamilEndTime);
		request.getSession().setAttribute("emaildateid", emaildateid);
		request.getSession().setAttribute("customerid", customerid);
		request.getSession().setAttribute("branchid", branchid);
		request.getSession().setAttribute("branchid_Session", branchidSession);
	}

	// 保存前台传递过来的供货商解析后存储到列表中
	private void setCustomerlist(Model model, String customerid, long branchid, String dmpid) {
		List<Customer> cumstrListAll = getDmpDAO.getAllCustomers();
		// 保存前台传递过来的供货商解析后存储到列表中
		List cumstrList1 = new ArrayList();
		if (customerid != null && customerid.length() > 0) {
			String[] cStr = customerid.split(",");
			for (int i = 0; i < cStr.length; i++) {
				cumstrList1.add(cStr[i]);
			}
		}
		model.addAttribute("cumstrList1", cumstrList1);
		model.addAttribute("cumstrListAll", cumstrListAll);
		// 查询所有的站点
		List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
		model.addAttribute("branchnameList", branchnameList);
		// 查询当前站点

		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		long branchidPram = branchid == -1 ? branchidSession : branchid;
		model.addAttribute("branchidSession", branchidPram);

	}

}