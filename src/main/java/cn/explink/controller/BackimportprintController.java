package cn.explink.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BackIntoprintDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Backintowarehouse_print;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PrintTemplateOpertatetypeEnum;
import cn.explink.print.template.PrintColumn;
import cn.explink.print.template.PrintTemplate;
import cn.explink.print.template.PrintTemplateDAO;
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
	CustomerDAO customerDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PrintTemplateDAO printTemplateDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, HttpServletRequest request, @RequestParam(value = "branchid", required = false, defaultValue = "0") String[] branchid,
			@RequestParam(value = "begincredate", required = false, defaultValue = "") String begincredate, @RequestParam(value = "endcredate", required = false, defaultValue = "") String endcredate,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid, @RequestParam(value = "flag", required = false, defaultValue = "0") long flag,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		model.addAttribute("branches", this.branchDAO.getBranchesByKuFangAndZhanDian());
		model.addAttribute("driverList", this.userDAO.getUserByRole(3));
		model.addAttribute("userList", this.userDAO.getAllUser());
		model.addAttribute("driverid", driverid);
		model.addAttribute("flag", flag);
		model.addAttribute("begincredate", begincredate);
		model.addAttribute("endcredate", endcredate);
		model.addAttribute("templete", this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.Tuihuozhanrukumingxi.getValue() + ""));
		List<String> branchArrlist = new ArrayList<String>();
		if ((branchid != null) && (branchid.length > 0)) {
			for (String str : branchid) {
				branchArrlist.add(str);
			}
		}
		String branchids = this.getStringByBranchids(branchid);
		long flowordertype = FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue();

		List<Backintowarehouse_print> backIntoprintList = new ArrayList<Backintowarehouse_print>();
		if (isshow > 0) {

			backIntoprintList = this.backIntoprintDAO.getBackintoPrint(begincredate, endcredate, flowordertype, branchids, driverid, flag, this.getSessionUser());
		}
		model.addAttribute("backIntoprintList", backIntoprintList);
		model.addAttribute("reasonList", this.reasonDao.getAllReason());
		model.addAttribute("customerList", this.customerDAO.getAllCustomers());
		model.addAttribute("branchArrlist", branchArrlist);
		model.addAttribute("branchids", branchids);
		return "backimportprint/list";
	}

	@RequestMapping("/print")
	public String print(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint,
			@RequestParam(value = "flag", defaultValue = "0", required = true) long flag, @RequestParam(value = "driverid", defaultValue = "0", required = true) long driverid,
			@RequestParam(value = "branchids", defaultValue = "0", required = true) String branchids, @RequestParam(value = "starttime", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime, @RequestParam(value = "printid", required = false, defaultValue = "0") long printid) {
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
		}
		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}
		PrintTemplate pt = this.printTemplateDAO.getPrintTemplate(printid);
		List<Backintowarehouse_print> bPrints = new ArrayList<Backintowarehouse_print>();
		bPrints = this.backIntoprintDAO.getBackintoPrint(starttime, endtime, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), branchids, driverid, flag, this.getSessionUser(), cwbs);
		this.backIntoprintDAO.updateBackintoPrint(starttime, endtime, cwbs, this.getSessionUser(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue());
		List<Reason> reasonList = this.reasonDao.getAllReason();
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<User> driverList = this.userDAO.getUserByRole(3);
		List<User> userList = this.userDAO.getAllUser();
		List<Branch> branches = this.branchDAO.getBranchesByKuFangAndZhanDian();
		model.addAttribute("branches", branches);
		model.addAttribute("driverList", driverList);
		model.addAttribute("userList", userList);
		model.addAttribute("bPrints", bPrints);
		model.addAttribute("pt", pt);
		model.addAttribute("reasonList", reasonList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("branch", this.branchDAO.getBranchById(this.getSessionUser().getBranchid()));
		List<PrintColumn> printColumns = pt.getColumns();
		printColumns = printColumns == null ? new ArrayList<PrintColumn>() : printColumns;
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		for (int i = 0; i < bPrints.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			for (PrintColumn printColumn : printColumns) {
				if (printColumn.getField().equals("cwb")) {
					map.put(printColumn.getField(), bPrints.get(i).getCwb());
				}
				if (printColumn.getField().equals("transcwb")) {
					map.put(printColumn.getField(), bPrints.get(i).getTranscwb());
				}
				if (printColumn.getField().equals("customername")) {
					for (Customer c : customerList) {
						if (c.getCustomerid() == bPrints.get(i).getCustomerid()) {
							map.put(printColumn.getField(), c.getCustomername());
						}
					}
				}
				if (printColumn.getField().equals("startbranchid")) {
					for (Branch b : branches) {
						if (b.getBranchid() == bPrints.get(i).getStartbranchid()) {
							map.put(printColumn.getField(), b.getBranchname());
						}
					}
				}
				if (printColumn.getField().equals("opreator")) {
					for (User u : userList) {
						if (u.getUserid() == bPrints.get(i).getUserid()) {
							map.put(printColumn.getField(), u.getUsername());
						}
					}
				}
				if (printColumn.getField().equals("backintotime")) {
					map.put(printColumn.getField(), bPrints.get(i).getCreatetime());
				}
				if (printColumn.getField().equals("backreason")) {
					for (Reason r : reasonList) {
						if (r.getReasonid() == bPrints.get(i).getBackreasonid()) {
							map.put(printColumn.getField(), r.getReasoncontent());
						}
					}
				}
				if (printColumn.getField().equals("driver")) {
					for (User u : driverList) {
						if (u.getUserid() == bPrints.get(i).getDriverid()) {
							map.put(printColumn.getField(), u.getUsername() == null ? "" : u.getUsername());
						}
					}
				}
			}
			listMap.add(map);
		}
		model.addAttribute("listMap", listMap);
		return "backimportprint/print";

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
