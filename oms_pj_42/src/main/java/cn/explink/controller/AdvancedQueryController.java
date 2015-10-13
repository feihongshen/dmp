package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Exportmould;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.AdvancedQueryService;
import cn.explink.service.ExportService;
import cn.explink.util.Page;

@RequestMapping("/advancedquery")
@Controller
public class AdvancedQueryController {

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	SetExportFieldDAO setExportFieldDAO;
	@Autowired
	SetExportFieldDAO setexportfieldDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExportDAO exportDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	AdvancedQueryService advancedQueryService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	private Logger logger = LoggerFactory.getLogger(AdvancedQueryController.class);

	@RequestMapping("/querysimple_list")
	public String query_simple(Model model, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "currentBranchid", required = false, defaultValue = "0") long currentBranchid,
			@RequestParam(value = "orderResultType", required = false, defaultValue = "") String[] orderResultTypes) {
		Branch branch = getDmpDAO.getNowBranch(currentBranchid);
		List<Branch> branchnameList = getDmpDAO.getQueryBranchByBranchsiteAndUserid(userid, BranchEnum.ZhanDian.getValue() + "," + BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue()
				+ "," + BranchEnum.ZhongZhuan.getValue());
		List<Branch> kufangList = getDmpDAO.getQueryBranchByBranchsiteAndUserid(userid, BranchEnum.KuFang.getValue() + "");

		if (branch.getSitetype() == BranchEnum.KuFang.getValue()) {
			if (kufangList.size() == 0) {
				kufangList.add(branch);
			} else {
				if (!advancedQueryService.checkBranchRepeat(kufangList, branch)) {
					kufangList.add(branch);
				}
			}
		} else if (branch.getSitetype() == BranchEnum.ZhanDian.getValue() || branch.getSitetype() == BranchEnum.TuiHuo.getValue() || branch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			if (branchnameList.size() == 0) {
				branchnameList.add(branch);
			} else {
				if (!advancedQueryService.checkBranchRepeat(branchnameList, branch)) {
					branchnameList.add(branch);
				}
			}
		}
		List<CustomWareHouse> list = getDmpDAO.getCustomWareHouseByCustomerid(customerid);
		List<User> nextBranchUserList = getDmpDAO.getAllUserbybranchid(currentBranchid);
		List<String> flowordertypelist = new ArrayList<String>();
		for (String orderResultType : orderResultTypes) {
			flowordertypelist.add(orderResultType);
		}

		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", getDmpDAO.getAllCustomers());
		model.addAttribute("commonlist", getDmpDAO.getAllCommons());
		model.addAttribute("customWareHouseList", list);
		model.addAttribute("nextBranchUserlist", nextBranchUserList);
		model.addAttribute("kufangList", kufangList);
		model.addAttribute("orderResultTypeStr", flowordertypelist);
		if (!model.containsAttribute("page_obj")) {
			model.addAttribute("page_obj", new Page());
		}
		return "/advancedquery/simplelist";
	}

	@RequestMapping("/querysimple/{page}")
	public String querysimpleList(Model model, @RequestParam(value = "datetype", required = false, defaultValue = "1") long datetype,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "commonnumber", required = false, defaultValue = "") String commonnumber,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "-1") long flowordertype,
			@RequestParam(value = "operationOrderResultType", required = false, defaultValue = "") String[] operationOrderResultTypes,
			@RequestParam(value = "orderResultType", required = false, defaultValue = "") String[] orderResultTypes,
			@RequestParam(value = "customerwarehouseid", required = false, defaultValue = "-1") long customerwarehouseid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "-1") long cwbordertypeid,
			@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "-1") long dispatchbranchid,
			@RequestParam(value = "dispatchdeliveryid", required = false, defaultValue = "-1") long dispatchdeliveryid,
			@RequestParam(value = "kufangid", required = false, defaultValue = "-1") long kufangid, @RequestParam(value = "paytype", required = false, defaultValue = "-1") long paywayid,
			@RequestParam(value = "consigneename", required = false, defaultValue = "") String consigneename,
			@RequestParam(value = "consigneemobile", required = false, defaultValue = "") String consigneemobile,
			@RequestParam(value = "beginWeight", required = false, defaultValue = "-1") String beginweight, @RequestParam(value = "endWeight", required = false, defaultValue = "-1") String endweight,
			@RequestParam(value = "beginsendcarnum", required = false, defaultValue = "-1") String beginsendcarnum,
			@RequestParam(value = "endsendcarnum", required = false, defaultValue = "-1") String endsendcarnum, @RequestParam(value = "carsize", required = false, defaultValue = "") String carsize,
			@RequestParam(value = "packagecode", required = false, defaultValue = "") String packagecode,
			@RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName,
			@RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrder> cwbOrderView = new ArrayList<CwbOrder>();

		if (isshow != 0) {
			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			String consigneeName = consigneename.replaceAll("'", "\\\\'");
			String consigneeMobile = consigneemobile.replaceAll("'", "\\\\'");
			long beginWeight = Long.parseLong(beginweight == null || "".equals(beginweight) ? "-2" : beginweight);
			long endWeight = Long.parseLong(endweight == null || "".equals(endweight) ? "-2" : endweight);
			long beginSendcarNum = Long.parseLong(beginsendcarnum == null || "".equals(beginsendcarnum) ? "-2" : beginsendcarnum);
			long endSendcarNum = Long.parseLong(endsendcarnum == null || "".equals(endsendcarnum) ? "-2" : endsendcarnum);
			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			// 根据时间类型取订单号
			// 获取值
			clist = cwbDAO.getCwborderList(page, datetype, begindate, enddate, customerid, commonnumber, orderName, customerwarehouseid, cwbordertypeid, dispatchbranchid, kufangid, paywayid,
					dispatchdeliveryid, consigneeName, consigneeMobile, beginWeight, endWeight, beginSendcarNum, endSendcarNum, carsize, flowordertype, operationOrderResultTypes, packagecode);
			count = cwbDAO.getcwborderCount(datetype, begindate, enddate, customerid, commonnumber, orderName, customerwarehouseid, cwbordertypeid, dispatchbranchid, kufangid, paywayid,
					dispatchdeliveryid, consigneeName, consigneeMobile, beginWeight, endWeight, beginSendcarNum, endSendcarNum, carsize, flowordertype, operationOrderResultTypes, packagecode);

			sum = cwbDAO.getcwborderSum(datetype, begindate, enddate, customerid, commonnumber, orderName, customerwarehouseid, cwbordertypeid, dispatchbranchid, kufangid, paywayid,
					dispatchdeliveryid, consigneeName, consigneeMobile, beginWeight, endWeight, beginSendcarNum, endSendcarNum, carsize, flowordertype, operationOrderResultTypes, packagecode);
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			List<Customer> customerList = getDmpDAO.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = getDmpDAO.getCustomWareHouse();
			List<Branch> branchList = getDmpDAO.getAllBranchs();
			List<User> userList = getDmpDAO.getAllUsers();
			List<Reason> reasonList = getDmpDAO.getAllReason();
			// 赋值显示对象
			cwbOrderView = advancedQueryService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate);
			setModel(datetype, begindate, enddate, customerid, commonnumber, orderName, customerwarehouseid, -1, -1, cwbordertypeid, "", -1, dispatchbranchid, kufangid, paywayid, dispatchdeliveryid,
					consigneename, consigneemobile, beginWeight, endWeight, beginSendcarNum, endSendcarNum, carsize, flowordertype, orderResultTypes, packagecode, model);
			logger.info("高级查询（querysimple版），当前操作人{},条数{}", user.getRealname(), count);
		}
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("exportmouldlist", getDmpDAO.getExportmoulds(user, dmpid));
		return query_simple(model, user.getUserid(), customerid, -1, orderResultTypes);
	}

	private void setModel(long datetype, String begindate, String enddate, long customerid, String commonnumber, String orderbyName, long customerwarehouseid, long startbranchid, long nextbranchid,
			long cwbordertypeid, String deliverycwbs, long currentBranchid, long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid, String consigneename, String consigneemobile,
			long beginWatht, long endWatht, long beginsendcarnum, long endsendcarnum, String carsize, long flowordertype, String[] deliverystates, String packagecode, Model model) {
		model.addAttribute("datetype", datetype);
		model.addAttribute("begindate", begindate);
		model.addAttribute("enddate", enddate);
		model.addAttribute("customerid", customerid);
		model.addAttribute("commonnumber", commonnumber);
		model.addAttribute("orderbyName", orderbyName);
		model.addAttribute("customerwarehouseid", customerwarehouseid);
		model.addAttribute("startbranchid", startbranchid);
		model.addAttribute("nextbranchid", nextbranchid);
		model.addAttribute("cwbordertypeid", cwbordertypeid);
		model.addAttribute("deliverycwbs", deliverycwbs);
		model.addAttribute("currentBranchid", currentBranchid);
		model.addAttribute("dispatchbranchid", dispatchbranchid);
		model.addAttribute("kufangid", kufangid);
		model.addAttribute("paywayid", paywayid);
		model.addAttribute("dispatchdeliveryid", dispatchdeliveryid);
		model.addAttribute("consigneename", consigneename);
		model.addAttribute("consigneemobile", consigneemobile);
		model.addAttribute("beginWatht", beginWatht);
		model.addAttribute("endWatht", endWatht);
		model.addAttribute("beginsendcarnum", beginsendcarnum);
		model.addAttribute("endsendcarnum", endsendcarnum);
		model.addAttribute("carsize", carsize);
		model.addAttribute("flowordertype", flowordertype);
		model.addAttribute("deliverystates", deliverystates);
		model.addAttribute("packagecode", packagecode);
		// 保存前台传递过来的供货商解析后存储到列表中
	}

	@RequestMapping("/updateDeliver")
	public @ResponseBody String updateDeliver(Model model, @RequestParam("branchid") long branchid) {
		if (branchid > 0) {
			List<User> list = getDmpDAO.getAllDeliver(branchid);

			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}

	}

	//
	@RequestMapping("/updateCustomerwarehouse")
	public @ResponseBody String updateCustomerwarehouse(Model model, @RequestParam("customerid") long customerid) {
		if (customerid > 0) {
			List<CustomWareHouse> list = getDmpDAO.getCustomWareHouseByCustom(customerid);
			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}

	}

	//
	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page) {
		try {
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = getDmpDAO.getLogUser(dmpid);
			Branch deliverybranch = getDmpDAO.getNowBranch(user.getBranchid());
			logger.info("大数据导出数据：用户名：{},站点：{}", user.getRealname(), deliverybranch.getBranchname());
		} catch (Exception e) {
			logger.error("大数据导出数据：获取用户名，站点异常");
		}
		advancedQueryService.AdvanceQueryExportExcelMethod(response, request, page);
	}
	//
	// private void setSesstion(String cwbandtranscwb, long datetype, String
	// begindate, String enddate, long customerid, String commonnumber, String
	// orderResultTypeStr, String orderbyName,
	// long customerwarehouseid, long cwbordertypeid, long startbranchid, long
	// nextbranchid, long kufangid, long paytype, long startdelivername, long
	// nextdelivername, String consigneename,
	// String consigneemobile, long beginWatht, long endWatht, long
	// beginsendcarnum, long endsendcarnum, String carsize, long flowordertype,
	// HttpServletRequest request) {
	// request.getSession().setAttribute("cwbandtranscwb", cwbandtranscwb);
	// request.getSession().setAttribute("datetype", datetype);
	// request.getSession().setAttribute("begindate", begindate);
	// request.getSession().setAttribute("enddate", enddate);
	// request.getSession().setAttribute("customerid", customerid);
	// request.getSession().setAttribute("commonnumber", commonnumber);
	// request.getSession().setAttribute("orderbyName", orderbyName);
	// request.getSession().setAttribute("customerwarehouseid",
	// customerwarehouseid);
	// request.getSession().setAttribute("cwbordertypeid", cwbordertypeid);
	// request.getSession().setAttribute("startbranchid", startbranchid);
	// request.getSession().setAttribute("nextbranchid", nextbranchid);
	// request.getSession().setAttribute("kufangid", kufangid);
	// request.getSession().setAttribute("paytype", paytype);
	// request.getSession().setAttribute("startdelivername", startdelivername);
	// request.getSession().setAttribute("nextdelivername", nextdelivername);
	// request.getSession().setAttribute("consigneename", consigneename);
	// request.getSession().setAttribute("consigneemobile", consigneemobile);
	// request.getSession().setAttribute("beginWatht", beginWatht);
	// request.getSession().setAttribute("endWatht", endWatht);
	// request.getSession().setAttribute("beginsendcarnum", beginsendcarnum);
	// request.getSession().setAttribute("endsendcarnum", endsendcarnum);
	// request.getSession().setAttribute("carsize", carsize);
	// request.getSession().setAttribute("orderResultTypeStr",
	// orderResultTypeStr);
	// request.getSession().setAttribute("flowordertype", flowordertype);
	// // 保存前台传递过来的供货商解析后存储到列表中
	// List flowordertypelist = new ArrayList();
	// if (orderResultTypeStr != null && orderResultTypeStr.length() > 0) {
	// String[] cStr = orderResultTypeStr.split(",");
	// for (int i = 0; i < cStr.length; i++) {
	// flowordertypelist.add(cStr[i]);
	// }
	// }
	// request.setAttribute("orderResultTypeStr", flowordertypelist);
	//
	// }

}
