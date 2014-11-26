package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.AdvancedQueryService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
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
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	AdvancedQueryService advancedQueryService;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	PayWayDao payWayDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	RemarkDAO remarkDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	private Logger logger = LoggerFactory.getLogger(AdvancedQueryController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/query")
	public String querypage(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "currentBranchid", required = false, defaultValue = "0") long currentBranchid,
			@RequestParam(value = "orderResultType", required = false, defaultValue = "") String[] orderResultTypes,
			@RequestParam(value = "operationOrderResultTypes", required = false, defaultValue = "") String[] operationOrderResultTypes) {

		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.KuFang.getValue() + ","
				+ BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
		List<Branch> kufangList = branchDAO.getQueryBranchByBranchidAndUserid(getSessionUser().getUserid(), BranchEnum.KuFang.getValue());

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
		List<CustomWareHouse> list = customWareHouseDAO.getCustomWareHouseByCustomerid(customerid);
		List<User> nextBranchUserList = userDAO.getAllUserbybranchidByUserDeleteFlag(currentBranchid);
		List<String> flowordertypelist = new ArrayList<String>();
		for (String orderResultType : orderResultTypes) {
			flowordertypelist.add(orderResultType);
		}
		List<String> operationOrderResultTypeslist = new ArrayList<String>();
		for (String operationOrderResultType : operationOrderResultTypes) {
			operationOrderResultTypeslist.add(operationOrderResultType);
		}
		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("commonlist", commonDAO.getAllCommons());
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		model.addAttribute("customWareHouseList", list);
		model.addAttribute("nextBranchUserlist", nextBranchUserList);
		model.addAttribute("kufangList", kufangList);
		model.addAttribute("orderResultTypeStr", flowordertypelist);
		model.addAttribute("operationOrderResultTypeStr", operationOrderResultTypeslist);
		if (!model.containsAttribute("page_obj")) {
			model.addAttribute("page_obj", new Page());
		}
		return "/advancedquery/list";
	}

	@RequestMapping("/search/{page}")
	public String hmj_list(Model model, @RequestParam(value = "datetype", required = false, defaultValue = "1") long datetype,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "commonnumber", required = false, defaultValue = "") String commonnumber,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "-1") long flowordertype,
			@RequestParam(value = "orderResultType", required = false, defaultValue = "") String[] orderResultTypes,
			@RequestParam(value = "operationOrderResultType", required = false, defaultValue = "") String[] operationOrderResultTypes,
			@RequestParam(value = "customerwarehouseid", required = false, defaultValue = "-1") long customerwarehouseid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "-1") long cwbordertypeid,
			@RequestParam(value = "startbranchid", required = false, defaultValue = "-1") long startbranchid,
			@RequestParam(value = "nextbranchid", required = false, defaultValue = "-1") long nextbranchid,
			@RequestParam(value = "currentbranchid", required = false, defaultValue = "-1") long currentBranchid,
			@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "-1") long dispatchbranchid,
			@RequestParam(value = "dispatchdeliveryid", required = false, defaultValue = "-1") long dispatchdeliveryid,
			@RequestParam(value = "kufangid", required = false, defaultValue = "-1") long kufangid, @RequestParam(value = "paytype", required = false, defaultValue = "-1") long paywayid,
			@RequestParam(value = "consigneename", required = false, defaultValue = "") String consigneename,
			@RequestParam(value = "consigneemobile", required = false, defaultValue = "") String consigneemobile,
			@RequestParam(value = "beginWeight", required = false, defaultValue = "-1") String beginweight, @RequestParam(value = "endWeight", required = false, defaultValue = "-1") String endweight,
			@RequestParam(value = "beginsendcarnum", required = false, defaultValue = "-1") String beginsendcarnum,
			@RequestParam(value = "endsendcarnum", required = false, defaultValue = "-1") String endsendcarnum, @RequestParam(value = "carsize", required = false, defaultValue = "") String carsize,
			@RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName,
			@RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {

		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		CwbOrder paybackfeesum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();

		if (isshow != 0) {
			logger.info(
					"高级查询(search)，操作人{}，选择条件datetype:" + datetype + ",begindate:" + begindate + ",enddate:" + enddate + ",customerid:" + customerid + ",commonnumber:" + commonnumber
							+ ",flowordertype:" + flowordertype + ",orderResultTypes:" + dataStatisticsService.getStrings(orderResultTypes) + ",operationOrderResultTypes:"
							+ dataStatisticsService.getStrings(operationOrderResultTypes) + ",customerwarehouseid:" + customerwarehouseid + ",cwbordertypeid:" + cwbordertypeid + ",startbranchid:"
							+ startbranchid + ",nextbranchid:" + nextbranchid + ",currentBranchid:" + currentBranchid + ",dispatchbranchid:" + dispatchbranchid + ",dispatchdeliveryid:"
							+ dispatchdeliveryid + ",kufangid:" + kufangid + ",paywayid:" + paywayid + ",consigneename:" + consigneename + ",consigneemobile:" + consigneemobile + ",beginweight:"
							+ beginweight + ",endweight:" + endweight + ",beginsendcarnum:" + beginsendcarnum + ",endsendcarnum:" + endsendcarnum + ",carsize:" + carsize + ",orderbyName:"
							+ orderbyName + ",orderbyId:" + orderbyId + ",page:" + page, getSessionUser().getRealname());

			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			String deliverycwbs = "";
			String orderflowcwbs = "";
			String consigneeName = consigneename.replaceAll("'", "\\\\'");
			String consigneeMobile = consigneemobile.replaceAll("'", "\\\\'");
			long beginWeight = Long.parseLong(beginweight == null || "".equals(beginweight) ? "-2" : beginweight);
			long endWeight = Long.parseLong(endweight == null || "".equals(endweight) ? "-2" : endweight);
			long beginSendcarNum = Long.parseLong(beginsendcarnum == null || "".equals(beginsendcarnum) ? "-2" : beginsendcarnum);
			long endSendcarNum = Long.parseLong(endsendcarnum == null || "".equals(endsendcarnum) ? "-2" : endsendcarnum);
			List<CwbOrder> clist = new ArrayList<CwbOrder>();

			// 根据订单配送结果取订单号

			String deliverystateCwbs = "'--'";
			// 根据时间类型取订单号
			if (datetype > 1) {
				List<String> orderFlowList = new ArrayList<String>();
				orderFlowList = advancedQueryService.getOrderFlowList(datetype, begindate, enddate, operationOrderResultTypes);
				if (orderFlowList.size() > 0) {
					orderflowcwbs = advancedQueryService.getOrderFlowCwb(orderFlowList);
				} else {
					orderflowcwbs = "'--'";
				}
			}

			// 获取值
			count = cwbDAO.getcwborderCountHmjQ(datetype, begindate, enddate, customerid, commonnumber, deliverystateCwbs, customerwarehouseid, startbranchid, nextbranchid, cwbordertypeid,
					orderflowcwbs, deliverycwbs, currentBranchid, dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneeName, consigneeMobile, beginWeight, endWeight, beginSendcarNum,
					endSendcarNum, carsize, flowordertype, orderResultTypes, "");

			sum = cwbDAO.getcwborderSumHmjQ(datetype, begindate, enddate, customerid, commonnumber, deliverystateCwbs, customerwarehouseid, startbranchid, nextbranchid, cwbordertypeid, orderflowcwbs,
					deliverycwbs, currentBranchid, dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneeName, consigneeMobile, beginWeight, endWeight, beginSendcarNum, endSendcarNum,
					carsize, flowordertype, orderResultTypes, "");

			paybackfeesum = cwbDAO.getcwborderSumHmjQ(datetype, begindate, enddate, customerid, commonnumber, deliverystateCwbs, customerwarehouseid, startbranchid, nextbranchid, cwbordertypeid,
					orderflowcwbs, deliverycwbs, currentBranchid, dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneeName, consigneeMobile, beginWeight, endWeight, beginSendcarNum,
					endSendcarNum, carsize, flowordertype, orderResultTypes, "");

			clist = cwbDAO.getcwbOrderByPageHmjQ(page, datetype, begindate, enddate, customerid, commonnumber, orderName, customerwarehouseid, startbranchid, nextbranchid, cwbordertypeid,
					orderflowcwbs, deliverycwbs, currentBranchid, dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneeName, consigneeMobile, beginWeight, endWeight, beginSendcarNum,
					endSendcarNum, carsize, flowordertype, orderResultTypes, "");

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			/*
			 * setSesstion(datetype,begindate,enddate,customerid,commonnumber,
			 * orderName,customerwarehouseid,
			 * startbranchid,nextbranchid,cwbordertypeid
			 * ,orderflowcwbs,deliverycwbs,currentBranchid, dispatchbranchid,
			 * kufangid,paywayid,dispatchdeliveryid, consigneename,
			 * consigneemobile,
			 * beginWeight,endWeight,beginSendcarNum,endSendcarNum, carsize,
			 * flowordertype,orderResultTypes,"", request);
			 */
			setModel(datetype, begindate, enddate, customerid, commonnumber, orderName, customerwarehouseid, startbranchid, nextbranchid, cwbordertypeid, deliverycwbs, currentBranchid,
					dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneename, consigneemobile, beginWeight, endWeight, beginSendcarNum, endSendcarNum, carsize, flowordertype,
					new String[] {}, operationOrderResultTypes, orderResultTypes, "", model);

			List<Customer> customerList = customerDAO.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = branchDAO.getAllBranches();
			List<User> userList = userDAO.getAllUserByuserDeleteFlag();
			List<Reason> reasonList = reasonDao.getAllReason();
			List<Remark> remarkList = remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = advancedQueryService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", paybackfeesum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		logger.info("高级查询（复杂版），当前操作人{},条数{}", getSessionUser().getRealname(), count);
		return querypage(model, customerid, currentBranchid, orderResultTypes, operationOrderResultTypes);
	}

	@RequestMapping("/querysimple_list")
	public String querysimple(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "currentBranchid", required = false, defaultValue = "0") long currentBranchid,
			@RequestParam(value = "orderResultType", required = false, defaultValue = "") String[] orderResultTypes) {

		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.KuFang.getValue() + ","
				+ BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
		List<Branch> kufangList = branchDAO.getQueryBranchByBranchidAndUserid(getSessionUser().getUserid(), BranchEnum.KuFang.getValue());

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
		List<CustomWareHouse> list = customWareHouseDAO.getCustomWareHouseByCustomerid(customerid);
		List<User> nextBranchUserList = userDAO.getAllUserbybranchidByUserDeleteFlag(currentBranchid);
		List<String> flowordertypelist = new ArrayList<String>();
		for (String orderResultType : orderResultTypes) {
			flowordertypelist.add(orderResultType);
		}

		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("commonlist", commonDAO.getAllCommons());
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
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
	public String querysimple_list(Model model, @RequestParam(value = "datetype", required = false, defaultValue = "1") long datetype,
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

		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();

		if (isshow != 0) {
			logger.info(
					"高级查询(querysimple)，操作人{}，选择条件datetype:" + datetype + ",begindate:" + begindate + ",enddate:" + enddate + ",customerid:" + customerid + ",commonnumber:" + commonnumber
							+ ",flowordertype:" + flowordertype + ",operationOrderResultTypes:" + dataStatisticsService.getStrings(operationOrderResultTypes) + ",orderResultTypes:"
							+ dataStatisticsService.getStrings(orderResultTypes) + ",customerwarehouseid:" + customerwarehouseid + ",cwbordertypeid:" + cwbordertypeid + ",dispatchbranchid:"
							+ dispatchbranchid + ",dispatchdeliveryid:" + dispatchdeliveryid + ",kufangid:" + kufangid + ",paywayid:" + paywayid + ",consigneename:" + consigneename
							+ ",consigneemobile:" + consigneemobile + ",beginweight:" + beginweight + ",endweight:" + endweight + ",beginsendcarnum:" + beginsendcarnum + ",endsendcarnum:"
							+ endsendcarnum + ",carsize:" + carsize + ",packagecode:" + packagecode + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",page:" + page, getSessionUser()
							.getRealname());

			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			String deliverycwbs = "";
			String orderflowcwbs = "";
			String consigneeName = consigneename.replaceAll("'", "\\\\'");
			String consigneeMobile = consigneemobile.replaceAll("'", "\\\\'");
			long beginWeight = Long.parseLong(beginweight == null || "".equals(beginweight) ? "-2" : beginweight);
			long endWeight = Long.parseLong(endweight == null || "".equals(endweight) ? "-2" : endweight);
			long beginSendcarNum = Long.parseLong(beginsendcarnum == null || "".equals(beginsendcarnum) ? "-2" : beginsendcarnum);
			long endSendcarNum = Long.parseLong(endsendcarnum == null || "".equals(endsendcarnum) ? "-2" : endsendcarnum);
			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			// 根据订单配送结果取订单号
			String deliverystateCwbs = "'--'";
			// 根据时间类型取订单号

			if (datetype > 1) {
				List<String> orderFlowList = new ArrayList<String>();
				if (datetype == 8) {
					orderFlowList = advancedQueryService.getOrderFlowListAudit(datetype, begindate, enddate, orderResultTypes, dispatchbranchid, dispatchdeliveryid);
				} else {
					orderFlowList = advancedQueryService.getOrderFlowList(datetype, begindate, enddate, orderResultTypes);
				}
				if (orderFlowList.size() > 0) {
					orderflowcwbs = dataStatisticsService.getOrderFlowCwbs(orderFlowList);
				} else {
					orderflowcwbs = "'--'";
				}
			}
			// 获取值
			count = cwbDAO.getcwborderCountHmjQ(datetype, begindate, enddate, customerid, commonnumber, deliverystateCwbs, customerwarehouseid, -1, -1, cwbordertypeid, orderflowcwbs, deliverycwbs,
					-1, dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneeName, consigneeMobile, beginWeight, endWeight, beginSendcarNum, endSendcarNum, carsize, flowordertype,
					orderResultTypes, packagecode);

			sum = cwbDAO.getcwborderSumHmjQ(datetype, begindate, enddate, customerid, commonnumber, deliverystateCwbs, customerwarehouseid, -1, -1, cwbordertypeid, orderflowcwbs, deliverycwbs, -1,
					dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneeName, consigneeMobile, beginWeight, endWeight, beginSendcarNum, endSendcarNum, carsize, flowordertype,
					orderResultTypes, packagecode);

			clist = cwbDAO.getcwbOrderByPageHmjQ(page, datetype, begindate, enddate, customerid, commonnumber, orderName, customerwarehouseid, -1, -1, cwbordertypeid, orderflowcwbs, deliverycwbs, -1,
					dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneeName, consigneeMobile, beginWeight, endWeight, beginSendcarNum, endSendcarNum, carsize, flowordertype,
					orderResultTypes, packagecode);

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			// setSesstion(datetype,begindate,enddate,customerid,commonnumber,orderName,customerwarehouseid,
			// -1,-1,cwbordertypeid,orderflowcwbs,deliverycwbs,-1,
			// dispatchbranchid, kufangid,paywayid,dispatchdeliveryid,
			// consigneename, consigneemobile,
			// beginWeight,endWeight,beginSendcarNum,endSendcarNum, carsize,
			// flowordertype,orderResultTypes,packagecode, request);

			setModel(datetype, begindate, enddate, customerid, commonnumber, orderName, customerwarehouseid, -1, -1, cwbordertypeid, deliverycwbs, -1, dispatchbranchid, kufangid, paywayid,
					dispatchdeliveryid, consigneename, consigneemobile, beginWeight, endWeight, beginSendcarNum, endSendcarNum, carsize, flowordertype, new String[] {}, orderResultTypes,
					orderResultTypes, packagecode, model);

			List<Customer> customerList = customerDAO.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = branchDAO.getAllBranches();
			List<User> userList = userDAO.getAllUserByuserDeleteFlag();
			List<Reason> reasonList = reasonDao.getAllReason();
			List<Remark> remarkList = remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = advancedQueryService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		logger.info("高级查询（querysimple版），当前操作人{},条数{}", getSessionUser().getRealname(), count);
		return querysimple(model, customerid, -1, orderResultTypes);
	}

	@RequestMapping("/updateDeliver")
	public @ResponseBody String updateDeliver(Model model, @RequestParam("branchid") long branchid) {
		if (branchid > 0) {
			List<User> list = userDAO.getAllUserByBranchid(branchid);

			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}

	}

	@RequestMapping("/updateCustomerwarehouse")
	public @ResponseBody String updateCustomerwarehouse(Model model, @RequestParam("customerid") long customerid) {
		if (customerid > 0) {
			List<CustomWareHouse> list = customWareHouseDAO.getCustomWareHouseByCustomerid(customerid);
			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}

	}

	@RequestMapping("/exportExcleSimple")
	public void exportExcleSimple(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page) {
		try {
			User us = getSessionUser();
			Branch deliverybranch = branchDAO.getBranchByBranchid(us.getBranchid());
			logger.info("大数据导出数据：用户名：{},站点：{}", us.getRealname(), deliverybranch.getBranchname());
		} catch (Exception e) {
			logger.error("大数据导出数据：获取用户名，站点异常");
		}
		advancedQueryService.AdvanceQueryExportExcelMethod(response, request, page, 1);
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page) {
		try {
			User us = getSessionUser();
			Branch deliverybranch = branchDAO.getBranchByBranchid(us.getBranchid());
			logger.info("大数据导出数据：用户名：{},站点：{}", us.getRealname(), deliverybranch.getBranchname());
		} catch (Exception e) {
			logger.error("大数据导出数据：获取用户名，站点异常");
		}
		advancedQueryService.AdvanceQueryExportExcelMethod(response, request, page, 0);
	}

	private void setModel(long datetype, String begindate, String enddate, long customerid, String commonnumber, String orderbyName, long customerwarehouseid, long startbranchid, long nextbranchid,
			long cwbordertypeid, String deliverycwbs, long currentBranchid, long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid, String consigneename, String consigneemobile,
			long beginWatht, long endWatht, long beginsendcarnum, long endsendcarnum, String carsize, long flowordertype, String[] deliverystates, String[] operationOrderResultType,
			String[] orderResultType, String packagecode, Model model) {
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
		model.addAttribute("operationOrderResultType", operationOrderResultType);
		model.addAttribute("orderResultType", orderResultType);
		model.addAttribute("packagecode", packagecode);
		// 保存前台传递过来的供货商解析后存储到列表中
	}

}
