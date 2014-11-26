package cn.explink.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.AdvancedQueryService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.OperateSelectService;
import cn.explink.util.Page;

@RequestMapping("/operateselect")
@Controller
public class OperateSelectController {

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	AdvancedQueryService advancedQueryService;
	@Autowired
	OperateSelectService operateSelectService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") String[] branchid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long flowordertype,
			@RequestParam(value = "begincredate", required = false, defaultValue = "") String begincredate, @RequestParam(value = "endcredate", required = false, defaultValue = "") String endcredate,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "autodetail", required = false, defaultValue = "1") String autodetail) {

		List<OperateSelectView> operateSelectViewList = new ArrayList<OperateSelectView>();
		Page pagevalue = new Page();
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		if (isshow != 0) {

			String branchids = "";
			if (branchid.length > 0) {
				for (String id : branchid) {
					branchids += id + ",";
				}
			}

			if (branchids.length() > 0) {
				branchids = branchids.substring(0, branchids.length() - 1);
			}
			// modi by wangych 20140721 增加“系统自动处理”查询条件
			List<OrderFlow> orderFlowList = orderFlowDAO.getOperateOrderFlowByWhere(page, branchids, flowordertype, begincredate, endcredate, autodetail);
			List<Customer> customerList = customerDAO.getAllCustomers();
			List<Branch> branchList = branchDAO.getAllBranches();
			List<User> userList = userDAO.getAllUser();
			pagevalue = new Page(orderFlowDAO.getOperateOrderFlowByWhereCount(branchids, flowordertype, begincredate, endcredate, autodetail), page, Page.ONE_PAGE_NUMBER);
			operateSelectViewList = operateSelectService.getOperateSelectViewList(orderFlowList, userList, customerList, branchList);
		}

		List<Branch> branchlist = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + ","
				+ BranchEnum.ZhongZhuan.getValue() + "," + BranchEnum.ZhanDian.getValue());
		if (branch.getSitetype() == BranchEnum.KuFang.getValue() || branch.getSitetype() == BranchEnum.TuiHuo.getValue() || branch.getSitetype() == BranchEnum.ZhongZhuan.getValue()
				|| branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			if (branchlist.size() == 0) {
				branchlist.add(branch);
			} else {
				if (!operateSelectService.checkBranchRepeat(branchlist, branch)) {
					branchlist.add(branch);
				}
			}
		}
		List<String> branchArrlist = new ArrayList<String>();
		if (branchid != null && branchid.length > 0) {
			for (String str : branchid) {
				branchArrlist.add(str);
			}
		}
		model.addAttribute("branchArrlist", branchArrlist);
		model.addAttribute("branchList", branchlist);
		model.addAttribute("operateSelectViewList", operateSelectViewList);
		model.addAttribute("page_obj", pagevalue);
		model.addAttribute("autodetailchecked", autodetail);
		model.addAttribute("page", page);
		logger.info("操作查询，当前操作人{},条数{}", getSessionUser().getRealname(), pagevalue.getTotal());
		return "operateselect/list";
	}

	@RequestMapping("/list/downloadOperateSelect")
	public String download(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "branchid", required = false, defaultValue = "0") String[] branchid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long flowordertype,
			@RequestParam(value = "begincredate", required = false, defaultValue = "") String begincredate, @RequestParam(value = "endcredate", required = false, defaultValue = "") String endcredate,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "autodetail", required = false, defaultValue = "0") String autodetail) {

		List<OperateSelectView> operateSelectViewList = new ArrayList<OperateSelectView>();
		if (isshow != 0) {

			String branchids = "";
			if (branchid.length > 0) {
				for (String id : branchid) {
					branchids += id + ",";
				}
			}

			if (branchids.length() > 0) {
				branchids = branchids.substring(0, branchids.length() - 1);
			}
			// modi by wangych 20140721 增加“系统自动处理”查询条件
			List<OrderFlow> orderFlowList = orderFlowDAO.downloadOperateOrderFlowByWhere(branchids, flowordertype, begincredate, endcredate, autodetail);
			List<Customer> customerList = customerDAO.getAllCustomers();
			List<Branch> branchList = branchDAO.getAllBranches();
			List<User> userList = userDAO.getAllUser();

			operateSelectViewList = operateSelectService.getOperateSelectViewList(orderFlowList, userList, customerList, branchList);
			SXSSFWorkbook wb = operateSelectService.download(operateSelectViewList);
			response.setContentType("application/x-msdownload");
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()); // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				fileName = fileName + otherName + lastStr;
				response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
				ServletOutputStream out = response.getOutputStream();
				wb.write(out);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("操作下载，当前操作人{}", getSessionUser().getRealname());
		return null;
	}
}
