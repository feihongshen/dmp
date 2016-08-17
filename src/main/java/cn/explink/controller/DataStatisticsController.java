package cn.explink.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserBranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EmailDate;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.OrderTypeEnum;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

@RequestMapping("/datastatistics")
@Controller
public class DataStatisticsController {

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
	EmailDateDAO emaildateDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	SystemInstallDAO systemInstallDAO;

	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	UserBranchDAO userBranchDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	ComplaintDAO complaintDAO;

	private ObjectMapper om = new ObjectMapper();

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/query")
	public String querypage(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "0") String[] customeridStr, @RequestParam(value = "currentBranchid", required = false, defaultValue = "0") String[] currentBranchid, @RequestParam(value = "orderResultType", required = false, defaultValue = "") String[] orderResultTypes, @RequestParam(value = "operationOrderResultTypes", required = false, defaultValue = "") String[] operationOrderResultTypes, @RequestParam(value = "datatype", required = false, defaultValue = "0") long datatype, @RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchidStr, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeidStr, @RequestParam(value = "kufangid", required = false, defaultValue = "") String[] kufangidStr, @RequestParam(value = "nextbranchid", required = false, defaultValue = "") String[] nextbranchidStr, @RequestParam(value = "startbranchid", required = false, defaultValue = "") String[] startbranchidStr) {

		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		List<Branch> branchnameList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));// bug546licx
		List<Branch> kufangList = this.branchDAO
				.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());

		if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.TuiHuo.getValue()) || (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			if (kufangList.size() == 0) {
				kufangList.add(branch);
			} else {
				if (!this.dataStatisticsService.checkBranchRepeat(kufangList, branch)) {
					kufangList.add(branch);
				}
			}
		} else if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			if (branchnameList.size() == 0) {
				branchnameList.add(branch);
			} else {
				if (!this.dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
					branchnameList.add(branch);
				}
			}
		}

		List<String> customeridList = this.dataStatisticsService.getList(customeridStr);

		String customerids = this.dataStatisticsService.getStrings(customeridStr);

		List<CustomWareHouse> list = this.customWareHouseDAO.getCustomWareHouseByCustomerids(customerids);
		List<String> flowordertypelist = this.dataStatisticsService.getList(orderResultTypes);

		List<String> operationOrderResultTypeslist = this.dataStatisticsService.getList(operationOrderResultTypes);
		List<String> dispatchbranchidlist = this.dataStatisticsService.getList(dispatchbranchidStr);
		List<String> cwbordertypeidlist = this.dataStatisticsService.getList(cwbordertypeidStr);
		List<String> kufangidlist = this.dataStatisticsService.getList(kufangidStr);
		List<String> nextbranchidlist = this.dataStatisticsService.getList(nextbranchidStr);
		List<String> startbranchidlist = this.dataStatisticsService.getList(startbranchidStr);
		List<String> currentBranchidlist = this.dataStatisticsService.getList(currentBranchid);

		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("commonlist", this.commonDAO.getAllCommons());
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		model.addAttribute("customWareHouseList", list);
		model.addAttribute("kufangList", kufangList);
		model.addAttribute("orderResultTypeStr", flowordertypelist);
		model.addAttribute("operationOrderResultTypeStr", operationOrderResultTypeslist);
		model.addAttribute("dispatchbranchidStr", dispatchbranchidlist);
		model.addAttribute("cwbordertypeidStr", cwbordertypeidlist);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("kufangidStr", kufangidlist);
		model.addAttribute("nextbranchidStr", nextbranchidlist);
		model.addAttribute("startbranchidStr", startbranchidlist);
		model.addAttribute("currentBranchidStr", currentBranchidlist);

		model.addAttribute("notuihuobranchList", this.branchDAO.getBanchByBranchidForStock(BranchEnum.KuFang.getValue() + "," + BranchEnum.ZhanDian.getValue() + "," + BranchEnum.ZhongZhuan.getValue()));
		model.addAttribute("loginUserType", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getSitetype());// 存储当前登录用户所在机构类型
		if (!model.containsAttribute("page_obj")) {
			model.addAttribute("page_obj", new Page());
		}
		if (datatype == 1) {
			return "datastatistics/zhiliulist";
		} else if (datatype == 2) {
			return "datastatistics/jushoulist";
		} else if (datatype == 3) {
			return "datastatistics/tuigonghuoshanglist";
		} else if (datatype == 4) {
			return "datastatistics/zaitulist";
		} else if (datatype == 5) {
			return "datastatistics/tuotoulist";
		} else if (datatype == 6) {
			return "datastatistics/outwarehousedatalist";
		} else if (datatype == 7) {
			return "datastatistics/fahuolist";
		} else if (datatype == 8) {
			return "datastatistics/daohuodata";
		}/*
			* else if(datatype==9){ return "datastatistics/zhongzhuandata"; }
			*/

		return null;
	}

	/**
	 * 滞留订单汇总
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param isaudit
	 * @param isauditTime
	 * @param customerid
	 * @param dispatchbranchid
	 * @param deliverid
	 * @param cwbordertypeid
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/zhiliusearch/{page}")
	public String zhiliusearch(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isaudit", required = false, defaultValue = "-1") long isaudit, @RequestParam(value = "isauditTime", required = false, defaultValue = "0") long isauditTime, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchid, @RequestParam(value = "deliverid", required = false, defaultValue = "-1") long deliverid, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName, @RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "firstlevelid", required = false, defaultValue = "0") int firstlevelreasonid, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		List<User> deliverlist = new ArrayList<User>();
		// 滞留
		String[] operationOrderResultTypes = { DeliveryStateEnum.FenZhanZhiLiu.getValue() + "" };
		if (isshow != 0) {
			this.logger
					.info("滞留订单汇总，操作人{}，选择条件begindate:" + begindate + ",enddate:" + enddate + ",isaudit:" + isaudit + ",isauditTime:" + isauditTime + ",customerid:" + this.dataStatisticsService
							.getStrings(customerid) + ",dispatchbranchid:" + this.dataStatisticsService.getStrings(dispatchbranchid) + ",deliverid:" + deliverid + ",cwbordertypeid:" + this.dataStatisticsService
							.getStrings(cwbordertypeid) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:" + page + "", this.getSessionUser().getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;

			Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
			List<Branch> branchnameList = this.branchDAO
					.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan
							.getValue());

			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if (branchnameList.size() == 0) {
					branchnameList.add(branch);
				} else {
					if (!this.dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
						branchnameList.add(branch);
					}
				}
			}
			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			String orderflowcwbs = "";
			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			if ((dispatchbranchid.length == 0) & (branchnameList.size() > 0)) {
				dispatchbranchid = new String[branchnameList.size()];
				for (Branch bc : branchnameList) {
					dispatchbranchid[branchnameList.indexOf(bc)] = bc.getBranchid() + "";

				}
			}

			deliverlist = this.userDAO.getAllUserByBranchIds(this.dataStatisticsService.getStrings(dispatchbranchid));
			SystemInstall systemInstall = this.systemInstallDAO.getSystemInstallByName("ZhiLiuTongji");
			int zhiliucheck = 0;
			if (systemInstall != null) {
				try {
					zhiliucheck = Integer.parseInt(systemInstall.getValue());
				} catch (NumberFormatException e) {
					zhiliucheck = 0;
				}
			}
			String customerids = this.dataStatisticsService.getStrings(customerid);
			String cwbordertypeids = this.dataStatisticsService.getStrings(cwbordertypeid);
			List<String> orderFlowList = this.deliveryStateDAO
					.getDeliveryStateByCredateAndFlowordertype(begindate, enddate, isauditTime, isaudit, operationOrderResultTypes, dispatchbranchid, deliverid, zhiliucheck, customerids, firstlevelreasonid);

			if (orderFlowList.size() > 0) {
				orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}

			// 获取值
			count = this.cwbDAO.getcwborderCountHuiZong("", "", customerids, "", "", cwbordertypeids, orderflowcwbs, "", "", "", 0, 0, 1, "");

			sum = this.cwbDAO.getcwborderSumHuiZong("", "", customerids, "", "", cwbordertypeids, orderflowcwbs, "", "", "", 0, 0, 1, "");

			clist = this.cwbDAO.getcwbOrderByPageHuiZong(page, "", "", orderName, customerids, "", "", cwbordertypeids, orderflowcwbs, "", "", "", 0, 0, 1, "");

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			List<Customer> customerList = this.customerDAO.getAllCustomersNew();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();

			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}
		model.addAttribute("deliverlist", deliverlist);

		List<Reason> levelreasonlist = this.reasonDao.add();
		List<User> nowUserList = new ArrayList<User>();
		nowUserList.addAll(this.userDAO.getUserByRole(2));
		nowUserList.addAll(this.userDAO.getUserByRole(4));
		model.addAttribute("userList", nowUserList);
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("check", 1);
		model.addAttribute("deliverid", deliverid);
		model.addAttribute("levelreasonlist", levelreasonlist);

		this.logger.info("滞留订单汇总，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return this.querypage(model, customerid, new String[] {}, null, operationOrderResultTypes, 1, dispatchbranchid, cwbordertypeid, new String[] {}, new String[] {}, new String[] {});
	}

	/**
	 * 拒收订单汇总
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param isaudit
	 * @param isauditTime
	 * @param customerid
	 * @param dispatchbranchid
	 * @param deliverid
	 * @param cwbordertypeid
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param operationOrderResultTypes
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/jushousearch/{page}")
	public String jushousearch(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isaudit", required = false, defaultValue = "-1") long isaudit, @RequestParam(value = "isauditTime", required = false, defaultValue = "0") long isauditTime, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchid, @RequestParam(value = "deliverid", required = false, defaultValue = "-1") long deliverid, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName, @RequestParam(value = "", required = false, defaultValue = "0") int firstlevelreasonid, @RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "operationOrderResultType", required = false, defaultValue = "") String[] operationOrderResultTypes, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		// 拒收、上门退拒退
		// String[] operationOrderResultTypes =
		// {DeliveryStateEnum.JuShou.getValue()+""};
		List<User> deliverlist = new ArrayList<User>();

		if (isshow != 0) {
			this.logger
					.info("拒收订单汇总，操作人{}，选择条件begindate:" + begindate + ",enddate:" + enddate + ",isaudit:" + isaudit + ",isauditTime:" + isauditTime + ",customerid:" + this.dataStatisticsService
							.getStrings(customerid) + ",dispatchbranchid:" + this.dataStatisticsService.getStrings(dispatchbranchid) + ",deliverid:" + deliverid + ",cwbordertypeid:" + this.dataStatisticsService
							.getStrings(cwbordertypeid) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",operationOrderResultType：" + this.dataStatisticsService
							.getStrings(operationOrderResultTypes) + ",page:" + page, this.getSessionUser().getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;

			Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
			List<Branch> branchnameList = this.branchDAO
					.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan
							.getValue());

			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if (branchnameList.size() == 0) {
					branchnameList.add(branch);
				} else {
					if (!this.dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
						branchnameList.add(branch);
					}
				}
			}
			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			String orderflowcwbs = "";
			List<CwbOrder> clist = new ArrayList<CwbOrder>();

			if ((dispatchbranchid.length == 0) & (branchnameList.size() > 0)) {
				dispatchbranchid = new String[branchnameList.size()];
				for (Branch bc : branchnameList) {
					dispatchbranchid[branchnameList.indexOf(bc)] = bc.getBranchid() + "";

				}
			}

			deliverlist = this.userDAO.getAllUserByBranchIds(this.dataStatisticsService.getStrings(dispatchbranchid));

			SystemInstall systemInstall = this.systemInstallDAO.getSystemInstallByName("JuShouTongji");
			int jushouCheck = 0;
			if (systemInstall != null) {
				try {
					jushouCheck = Integer.parseInt(systemInstall.getValue());
				} catch (NumberFormatException e) {
					jushouCheck = 0;
				}
			}
			if (operationOrderResultTypes.length == 0) {
				operationOrderResultTypes = new String[4];
				operationOrderResultTypes[0] = DeliveryStateEnum.JuShou.getValue() + "";
				operationOrderResultTypes[1] = DeliveryStateEnum.ShangMenJuTui.getValue() + "";
				operationOrderResultTypes[2] = DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "";
				operationOrderResultTypes[3] = DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "";
			}

			String customerids = this.dataStatisticsService.getStrings(customerid);
			String cwbordertypeids = this.dataStatisticsService.getStrings(cwbordertypeid);
			List<String> orderFlowList = this.deliveryStateDAO
					.getDeliveryStateByCredateAndFlowordertype(begindate, enddate, isauditTime, isaudit, operationOrderResultTypes, dispatchbranchid, deliverid, jushouCheck, customerids, firstlevelreasonid);

			if (orderFlowList.size() > 0) {

				orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}

			// 获取值
			count = this.cwbDAO.getcwborderCountHuiZong("", "", customerids, "", "", cwbordertypeids, orderflowcwbs, "", "", "", 0, 0, 2, "");

			sum = this.cwbDAO.getcwborderSumHuiZong("", "", customerids, "", "", cwbordertypeids, orderflowcwbs, "", "", "", 0, 0, 2, "");

			clist = this.cwbDAO.getcwbOrderByPageHuiZong(page, "", "", orderName, customerids, "", "", cwbordertypeids, orderflowcwbs, "", "", "", 0, 0, 2, "");

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			List<Customer> customerList = this.customerDAO.getAllCustomersNew();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}
		model.addAttribute("deliverlist", deliverlist);
		List<User> nowUserList = new ArrayList<User>();
		nowUserList.addAll(this.userDAO.getUserByRole(2));
		nowUserList.addAll(this.userDAO.getUserByRole(4));
		model.addAttribute("userList", nowUserList);
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("check", 1);
		model.addAttribute("deliverid", deliverid);
		this.logger.info("拒收订单汇总，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return this.querypage(model, customerid, new String[] {}, null, operationOrderResultTypes, 2, dispatchbranchid, cwbordertypeid, new String[] {}, new String[] {}, new String[] {});
	}

	/**
	 * 退供货商订单汇总
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param customerid
	 * @param cwbordertypeid
	 * @param flowordertype
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/tuigonghuoshangsearch/{page}")
	public String tuigonghuoshangsearch(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "flowordertype", required = false, defaultValue = "-1") long flowordertype, @RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName, @RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();

		String[] operationOrderResultTypes = {};
		if (isshow != 0) {
			this.logger
					.info("退供货商订单汇总，操作人{}，选择条件begindate:" + begindate + ",enddate:" + enddate + ",customerid:" + this.dataStatisticsService.getStrings(customerid) + ",flowordertype:" + flowordertype + ",cwbordertypeid:" + this.dataStatisticsService
							.getStrings(cwbordertypeid) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:" + page, this.getSessionUser().getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;

			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			String orderflowcwbs = "";
			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			List<String> orderFlowList = new ArrayList<String>();

			if (flowordertype == -1) {
				orderFlowList
						.addAll(this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), operationOrderResultTypes, new String[] {}, 0, 0));
				orderFlowList
						.addAll(this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), operationOrderResultTypes, new String[] {}, 0, 0));
				orderFlowList
						.addAll(this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), operationOrderResultTypes, new String[] {}, 0, 0));
			} else {
				orderFlowList = this.orderFlowDAO.getOrderFlowByCredateAndFlowordertype(begindate, enddate, flowordertype, operationOrderResultTypes, new String[] {}, 0, 0);
			}

			if (orderFlowList.size() > 0) {
				orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}
			String customerids = this.dataStatisticsService.getStrings(customerid);
			String cwbordertypeids = this.dataStatisticsService.getStrings(cwbordertypeid);
			// 获取值
			count = this.cwbDAO.getcwborderCountHuiZong("", "", customerids, "", "", cwbordertypeids, orderflowcwbs, "", "", "", flowordertype, 0, 3, "");

			sum = this.cwbDAO.getcwborderSumHuiZong("", "", customerids, "", "", cwbordertypeids, orderflowcwbs, "", "", "", flowordertype, 0, 3, "");

			clist = this.cwbDAO.getcwbOrderByPageHuiZong(page, "", "", orderName, customerids, "", "", cwbordertypeids, orderflowcwbs, "", "", "", flowordertype, 0, 3, "");

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			List<Customer> customerList = this.customerDAO.getAllCustomersNew();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();

			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		this.logger.info("退供货商汇总，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return this.querypage(model, customerid, new String[] {}, null, operationOrderResultTypes, 3, new String[] {}, cwbordertypeid, new String[] {}, new String[] {}, new String[] {});
	}

	/**
	 * 库房在途订单汇总
	 *
	 * @param model
	 * @param datetype
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param nextbranchid
	 * @param cwbordertypeid
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/zaitusearch/{page}")
	public String zaitusearch(Model model, @RequestParam(value = "datetype", required = false, defaultValue = "1") long datetype, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangid", required = false, defaultValue = "") String[] kufangid, @RequestParam(value = "nextbranchid", required = false, defaultValue = "") String[] nextbranchid, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName, @RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbOrder feeco = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		List<Branch> branchList = this.branchDAO.getAllBranches();
		// 滞留
		String[] operationOrderResultTypes = {};
		if ((isshow != 0) && (kufangid.length > 0) && (nextbranchid.length > 0)) {
			this.logger
					.info("库房在途订单汇总，操作人{}，选择条件datetype:" + datetype + "begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + this.dataStatisticsService.getStrings(kufangid) + ",nextbranchid:" + this.dataStatisticsService
							.getStrings(nextbranchid) + ",cwbordertypeid:" + this.dataStatisticsService.getStrings(cwbordertypeid) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:" + page, this
							.getSessionUser().getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			List<CwbOrder> clist = new ArrayList<CwbOrder>();

			String cwbordertypeids = this.dataStatisticsService.getStrings(cwbordertypeid);
			String nextbranchids = this.dataStatisticsService.getStrings(nextbranchid);
			String kufangids = this.dataStatisticsService.getStrings(kufangid);

			// 获取值

			if (datetype == 1) {
				clist = this.cwbDAO.getzaitucwbOrderByPage(page, orderName, begindate, enddate, kufangids, nextbranchids, cwbordertypeids, CwbFlowOrderTypeEnum.ChuKuSaoMiao.getValue());
				feeco = this.cwbDAO.getzaitucwborderSum(datetype, begindate, enddate, kufangids, nextbranchids, cwbordertypeids, CwbFlowOrderTypeEnum.ChuKuSaoMiao.getValue());
				count = this.cwbDAO.getzaitucwborderCount(begindate, enddate, kufangids, nextbranchids, cwbordertypeids, CwbFlowOrderTypeEnum.ChuKuSaoMiao.getValue());
			} else if (datetype == 2) {

				List<String> orderflowlist = this.orderFlowDAO.getZaituCwbsByDateAndFlowtype(begindate, enddate, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), kufangids);
				String orderflowcwbs = this.dataStatisticsService.getStrings(orderflowlist);
				if (orderflowcwbs.length() == 0) {
					orderflowcwbs = "'--'";
				}
				List<String> cwbStr = this.cwbDAO.getzaitucwborderCwb(kufangids, nextbranchids, cwbordertypeids, CwbFlowOrderTypeEnum.ChuKuSaoMiao.getValue(), orderflowcwbs);
				if (!cwbStr.isEmpty()) {
					String cwbs = this.dataStatisticsService.getStrings(cwbStr);
					clist = this.cwbDAO.getCwbByCwbsPage(page, cwbs);
					count = this.cwbDAO.getCwbOrderCwbsCount(cwbs);
					feeco = this.cwbDAO.getcwborderSumBycwbs(cwbs);
				}
			}

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			List<Customer> customerList = this.customerDAO.getAllCustomersNew();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();

			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}

		model.addAttribute("branchAllList", this.branchDAO.getBranchToUser(this.getSessionUser().getUserid()));
		model.addAttribute("count", count);
		model.addAttribute("sum", feeco.getReceivablefee());
		model.addAttribute("paybackfeesum", feeco.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		this.logger.info("库房在途订单汇总，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return this.querypage(model, new String[] {}, new String[] {}, null, operationOrderResultTypes, 4, new String[] {}, cwbordertypeid, kufangid, nextbranchid, new String[] {});
	}

	/**
	 * 妥投订单汇总
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param isaudit
	 * @param isauditTime
	 * @param customerid
	 * @param cwbordertypeid
	 * @param paywayid
	 * @param dispatchbranchid
	 * @param deliverid
	 * @param operationOrderResultTypes
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/tuotousearch/{page}")
	public String tuotousearch(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isaudit", required = false, defaultValue = "-1") long isaudit, @RequestParam(value = "isauditTime", required = false, defaultValue = "0") long isauditTime, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "paytype", required = false, defaultValue = "-1") long paywayid, @RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchid, @RequestParam(value = "deliverid", required = false, defaultValue = "-1") long deliverid, @RequestParam(value = "operationOrderResultType", required = false, defaultValue = "") String[] operationOrderResultTypes, @RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName, @RequestParam(value = "", required = false, defaultValue = "0") int firstlevelreasonid, @RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "paybackfeeIsZero", required = false, defaultValue = "-1") Integer paybackfeeIsZero, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		List<User> deliverlist = new ArrayList<User>();
		// 滞留
		if (isshow != 0) {
			this.logger
					.info("妥投订单汇总，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",isaudit:" + isaudit + ",isauditTime:" + isauditTime + ",customerid:" + this.dataStatisticsService
							.getStrings(customerid) + ",paywayid:" + paywayid + ",cwbordertypeid:" + this.dataStatisticsService.getStrings(cwbordertypeid) + ",dispatchbranchid:" + this.dataStatisticsService
							.getStrings(dispatchbranchid) + ",deliverid:" + deliverid + ",operationOrderResultTypes:" + this.dataStatisticsService.getStrings(operationOrderResultTypes) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:" + page, this
							.getSessionUser().getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;

			Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
			List<Branch> branchnameList = this.branchDAO
					.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan
							.getValue());

			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if (branchnameList.size() == 0) {
					branchnameList.add(branch);
				} else {
					if (!this.dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
						branchnameList.add(branch);
					}
				}
			}
			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			String orderflowcwbs = "";
			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			if (operationOrderResultTypes.length == 0) {
				operationOrderResultTypes = new String[] { DeliveryStateEnum.PeiSongChengGong.getValue() + "", DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "", DeliveryStateEnum.ShangMenTuiChengGong
						.getValue() + "" };
			}
			if ((dispatchbranchid.length == 0) & (branchnameList.size() > 0)) {
				dispatchbranchid = new String[branchnameList.size()];
				for (Branch bc : branchnameList) {
					dispatchbranchid[branchnameList.indexOf(bc)] = bc.getBranchid() + "";

				}
			}

			deliverlist = this.userDAO.getAllUserByBranchIds(this.dataStatisticsService.getStrings(dispatchbranchid));

			String customerids = this.dataStatisticsService.getStrings(customerid);
			String cwbordertypeids = this.dataStatisticsService.getStrings(cwbordertypeid);
			// add by jian_xie 2016-08-09，当选定了小件员，站点过滤不需要。兼容小件员跳槽到其它站点问题。
			if(deliverid > 0){
				dispatchbranchid = new String[0];
			}
			List<String> orderFlowLastList = this.deliveryStateDAO
					.getDeliveryStateByCredateAndFlowordertype(begindate, enddate, isauditTime, isaudit, operationOrderResultTypes, dispatchbranchid, deliverid, 1, customerids, firstlevelreasonid);
			if (orderFlowLastList.size() > 0) {
				orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(orderFlowLastList);
			} else {
				orderflowcwbs = "'--'";
			}

			// 获取值
			count = this.cwbDAO.getcwborderCountHuiZong(customerids, cwbordertypeids, orderflowcwbs, 0, paywayid, new String[] {}, paybackfeeIsZero);
			sum = this.cwbDAO.getcwborderSumHuiZong(customerids, cwbordertypeids, orderflowcwbs, 0, paywayid, new String[] {}, paybackfeeIsZero);
			clist = this.cwbDAO.getcwbOrderByPageHuiZong(page, orderName, customerids, cwbordertypeids, orderflowcwbs, 0, paywayid, new String[] {}, paybackfeeIsZero);
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			List<Customer> customerList = this.customerDAO.getAllCustomersNew();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			List<User> userList = this.userDAO.getAllUserByuserDeleteFlag();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}

		List<User> nowUserList = new ArrayList<User>();
		nowUserList.addAll(this.userDAO.getUserByRole(2));
		nowUserList.addAll(this.userDAO.getUserByRole(4));
		model.addAttribute("deliverlist", deliverlist);
		model.addAttribute("userList", nowUserList);
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("deliverid", deliverid);
		model.addAttribute("check", 1);
		this.logger.info("妥投订单汇总，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return this.querypage(model, customerid, new String[] {}, null, operationOrderResultTypes, 5, dispatchbranchid, cwbordertypeid, new String[] {}, new String[] {}, new String[] {});
	}

	/**
	 * 库房出库统计
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param customerid
	 * @param nextbranchid
	 * @param cwbordertypeid
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/outwarehousedata/{page}")
	public String outwarehousedata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangid", required = false, defaultValue = "-1") String[] kufangid, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "nextbranchid", required = false, defaultValue = "") String[] nextbranchid, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName, @RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		List<Branch> branchList = this.branchDAO.getAllBranches();

		if (isshow != 0) {
			this.logger
					.info("库房出库统计，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + kufangid + ",customerid:" + this.dataStatisticsService.getStrings(customerid) + ",cwbordertypeid:" + this.dataStatisticsService
							.getStrings(cwbordertypeid) + ",nextbranchid:" + this.dataStatisticsService.getStrings(nextbranchid) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:" + page, this
							.getSessionUser().getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			String customerids = this.dataStatisticsService.getStrings(customerid);
			String cwbordertypeids = this.dataStatisticsService.getStrings(cwbordertypeid);
			String nextbranchids = this.dataStatisticsService.getStrings(nextbranchid);
			String kufangids = this.dataStatisticsService.getStrings(kufangid);
			if ("-1".equals(kufangids)) {
				kufangids = "";
			}
			// 获取值
			count = this.cwbDAO.getcwbOrderByOutWarehouseCountNew(begindate, enddate, orderName, customerids, kufangids, nextbranchids, cwbordertypeids, 0);
			sum = this.cwbDAO.getcwbOrderByOutWarehouseSumNew(begindate, enddate, orderName, customerids, kufangids, nextbranchids, cwbordertypeids, 0);
			clist = this.cwbDAO.getcwbOrderByOutWarehouseNew(page, begindate, enddate, orderName, customerids, kufangids, nextbranchids, cwbordertypeids, 0);
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			List<Customer> customerList = this.customerDAO.getAllCustomersNew();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}
		model.addAttribute("branchAllList", this.branchDAO.getBranchToUser(this.getSessionUser().getUserid()));
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);

		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		List<Branch> branchnameList = this.branchDAO
				.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
		List<Branch> kufangList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "");
		if ((branch.getSitetype() == BranchEnum.KuFang.getValue())) {
			if (kufangList.size() == 0) {
				kufangList.add(branch);
			} else {
				if (!this.dataStatisticsService.checkBranchRepeat(kufangList, branch)) {
					kufangList.add(branch);
				}
			}
		} else if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			if (branchnameList.size() == 0) {
				branchnameList.add(branch);
			} else {
				if (!this.dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
					branchnameList.add(branch);
				}
			}
		}
		List<String> customeridList = this.dataStatisticsService.getList(customerid);
		List<String> cwbordertypeidlist = this.dataStatisticsService.getList(cwbordertypeid);
		List<String> nextbranchidlist = this.dataStatisticsService.getList(nextbranchid);
		List<String> kufangidList = this.dataStatisticsService.getList(kufangid);

		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		model.addAttribute("kufangList", kufangList);
		model.addAttribute("cwbordertypeidStr", cwbordertypeidlist);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("kufangidStr", kufangidList);
		model.addAttribute("nextbranchidStr", nextbranchidlist);
		this.logger.info("库房出库统计，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return "datastatistics/outwarehousedatalist";
		// return querypage(model,customerid,new
		// String[]{},null,operationOrderResultTypes,6,new
		// String[]{},cwbordertypeid,kufangid,nextbranchid,new String[]{});
	}

	/**
	 * 库房入库统计
	 *
	 * @param model
	 * @param page
	 * @param emaildateids
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param customerids
	 * @param cwbordertypeid
	 * @param isruku
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping("/intowarehousedata/{page}")
	public String intowarehousedata(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "emaildatebegin", required = false, defaultValue = "") String emaildatebegin, @RequestParam(value = "emaildateend", required = false, defaultValue = "") String emaildateend, @RequestParam(value = "kufangid", required = false, defaultValue = "0") long kufangid, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerids, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "-2") long cwbordertypeid, @RequestParam(value = "isruku", required = false, defaultValue = "false") String isruku, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, HttpServletResponse response, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		long count = 0;
		Page pageparm = new Page();
		if (isruku.equals("true")) {
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowDate() + " 00:00:00" : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
		} else {
			emaildatebegin = emaildatebegin.length() == 0 ? DateTimeUtil.getNowDate() + " 00:00:00" : emaildatebegin;
			emaildateend = emaildateend.length() == 0 ? DateTimeUtil.getNowTime() : emaildateend;
		}
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		List<Branch> branchList = this.branchDAO.getAllBranches();
		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		// 蓝生2014/9/26 16:04:23 @李媛媛 去掉库房中统计的中转库
		List<Branch> kufangList = this.branchDAO
				.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
		if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.TuiHuo.getValue()) || (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			if (kufangList.size() == 0) {
				kufangList.add(branch);
			} else {
				if (!this.dataStatisticsService.checkBranchRepeat(kufangList, branch)) {
					kufangList.add(branch);
				}
			}
		}
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist", customerList);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		model.addAttribute("kufangList", kufangList);
		List<String> customeridList = this.dataStatisticsService.getList(customerids);
		model.addAttribute("customeridStr", customeridList);

		if (isshow != 0) {
			this.logger
					.info("库房入库统计，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + kufangid + ",cwbordertypeid:" + cwbordertypeid + ",isruku:" + isruku + ",customerids:" + this.dataStatisticsService
							.getStrings(customerids) + ",isshow:" + isshow + ",page:" + page, this.getSessionUser().getRealname());

			String customers = "";
			if (customerids.length > 0) {
				customers = this.dataStatisticsService.getStrings(customerids);
			}

			List<CwbOrder> clist = new ArrayList<CwbOrder>();

			if (isruku.equals("false")) {
				clist = this.cwbDAO.getCwbDetailByParamAndCwbsPage(page, customers, emaildatebegin, emaildateend, cwbordertypeid, kufangid);
				count = this.cwbDAO.getCwbDetailByParamAndCwbsCount(customers, emaildatebegin, emaildateend, cwbordertypeid, kufangid);
			} else {
				List<String> orderflowList = this.orderFlowDAO.getIntoCwbDetailAndOrderFlow(begindate, enddate, FlowOrderTypeEnum.RuKu.getValue(), kufangid);
				String orderflowcwbs = "";
				if (orderflowList.size() > 0) {
					orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(orderflowList);
				} else {
					orderflowcwbs = "'--'";
				}
				clist = this.cwbDAO.getIntoCwbByCwbsPage(page, orderflowcwbs, customers, cwbordertypeid, emaildatebegin, emaildateend);
				count = this.cwbDAO.getIntoCwbByCwbsCount(orderflowcwbs, customers, cwbordertypeid, emaildatebegin, emaildateend);
			}

			/*
			 * List<OrderFlow>
			 * orderflowList=orderFlowDAO.getIntoCwbDetailAndOrderFlowPage
			 * (customers,emaildatebegin,emaildateend,begindate, enddate,
			 * FlowOrderTypeEnum.RuKu.getValue(), kufangid, cwbordertypeid);
			 * List<OrderFlow> orderflowpageList = new ArrayList<OrderFlow>();
			 * for(int i = (page-1)*Page.ONE_PAGE_NUMBER ; i <
			 * Page.ONE_PAGE_NUMBER*page&&i<orderflowList.size() ;i++){
			 * orderflowpageList.add(orderflowList.get(i)); }
			 *
			 *
			 * for(OrderFlow of : orderflowpageList){
			 * clist.add(om.readValue(of.getFloworderdetail(),
			 * CwbOrderWithDeliveryState.class).getCwbOrder()); } count =
			 * orderflowList.size();
			 *
			 * if(isruku.equals("false")){ StringBuffer str = new
			 * StringBuffer(); String cwbs=""; if(orderflowList.size()>0){
			 * for(OrderFlow of : orderflowList){
			 * str.append("'").append(of.getCwb()).append("',"); } cwbs =
			 * str.substring(0, str.length()-1); clist =
			 * cwbDAO.getCwbDetailByParamAndCwbsPage
			 * (page,emaildatebegin,emaildateend, cwbordertypeid,
			 * cwbs,kufangid); count =
			 * cwbDAO.getCwbDetailByParamAndCwbsCount(emaildatebegin
			 * ,emaildateend, cwbordertypeid, cwbs,kufangid); } }
			 */
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService
					.getCwbOrderView(clist, this.customerDAO.getAllCustomersNew(), customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}
		List<Customer> customerMarkList = new ArrayList<Customer>();
		if (customerids.length > 0) {
			customerMarkList = this.customerDAO.getCustomerByIds(this.dataStatisticsService.getStrings(customerids));
		}
		model.addAttribute("customerMarkList", customerMarkList);
		model.addAttribute("count", count);
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		this.logger.info("库房入库统计，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return "datastatistics/intowarehousedatalist";
	}

	/**
	 * 客户发货统计
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param customerid
	 * @param cwbordertypeid
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/fahuodata/{page}")
	public String fahuodata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangid", required = false, defaultValue = "") String[] kufangid, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName, @RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "flowordertype", required = false, defaultValue = "0") long flowordertype, @RequestParam(value = "servicetype", required = false, defaultValue = "全部") String servicetype, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();

		String[] operationOrderResultTypes = {};
		if (isshow != 0) {
			this.logger
					.info("客户发货统计，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + this.dataStatisticsService.getStrings(kufangid) + ",cwbordertypeid:" + cwbordertypeid + ",customerid:" + this.dataStatisticsService
							.getStrings(customerid) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:" + page, this.getSessionUser().getRealname());

			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			List<CwbOrder> clist = new ArrayList<CwbOrder>();

			String customerids = this.dataStatisticsService.getStrings(customerid);
			String cwbordertypeids = this.dataStatisticsService.getStrings(cwbordertypeid);
			String kufangids = this.dataStatisticsService.getStrings(kufangid);
			kufangids=!kufangids.isEmpty()?(kufangids+",0"):kufangids;
			// 获取值
			List<Branch> kufangList = this.branchDAO
					.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
			Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
			if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.TuiHuo.getValue()) || (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				if (kufangList.size() == 0) {
					kufangList.add(branch);
				} else {
					if (!this.checkBranchRepeat(kufangList, branch)) {
						kufangList.add(branch);
					}
				}
			}
			if ((kufangid.length == 0) && (kufangids.length() == 0)) {
				for (Branch kf : kufangList) {
					kufangids += kf.getBranchid() + ",";
				}
				if ((kufangids.length() > 0) && kufangids.contains(",")) {
					kufangids = kufangids.substring(0, kufangids.length() - 1);
				}
			}
			count = this.cwbDAO.getcwborderCountHuiZong(begindate, enddate, customerids, "", "", cwbordertypeids, "", "", "", kufangids, flowordertype, 0, 7, servicetype);

			sum = this.cwbDAO.getcwborderSumHuiZong(begindate, enddate, customerids, "", "", cwbordertypeids, "", "", "", kufangids, flowordertype, 0, 7, servicetype);

			clist = this.cwbDAO.getcwbOrderByPageHuiZong(page, begindate, enddate, orderName, customerids, "", "", cwbordertypeids, "", "", "", kufangids, flowordertype, 0, 7, servicetype);

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			List<Customer> customerList = this.customerDAO.getAllCustomersNew();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}
		model.addAttribute("servicetype", servicetype);
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		this.logger.info("客户发货统计，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return this.querypage(model, customerid, new String[] {}, null, operationOrderResultTypes, 7, new String[] {}, cwbordertypeid, kufangid, new String[] {}, new String[] {});
	}

	/**
	 * 分站到货统计功能
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param customerid
	 * @param cwbordertypeid
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/daohuodata/{page}")
	public String daohuodata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangid", required = false, defaultValue = "") String[] kufangid, @RequestParam(value = "customerid", required = false, defaultValue = "") String customerid, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "currentBranchid", required = false, defaultValue = "") String[] currentBranchid, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "isnowdata", required = false, defaultValue = "0") long isnowdata, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取全部供货商
		if (isshow != 0) {
			this.logger
					.info("分站到货统计，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + this.dataStatisticsService.getStrings(kufangid) + ",cwbordertypeid:" + cwbordertypeid + ",customerid:" + customerid + ",currentBranchid:" + this.dataStatisticsService
							.getStrings(currentBranchid) + ",isshow:" + isshow + ",page:" + page, this.getSessionUser().getRealname());

			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;

			// 定义参数
			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			String orderflowcwbs = "";

			Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
			List<Branch> branchnameList = this.branchDAO
					.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan
							.getValue());

			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if (branchnameList.size() == 0) {
					branchnameList.add(branch);
				} else {
					if (!this.dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
						branchnameList.add(branch);
					}
				}
			}

			if (currentBranchid.length == 0) {
				if ((branchnameList != null) && (branchnameList.size() > 0)) {
					currentBranchid = new String[branchnameList.size()];
					for (Branch b : branchnameList) {
						currentBranchid[branchnameList.indexOf(b)] = b.getBranchid() + "";
					}

				}

			}

			//Modified by leoliao at 2016-07-29 优化分站到货查询性能
			String flowordertypes   = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue();
			String currentBranchids = this.dataStatisticsService.getStrings(currentBranchid);
			String sqlOrderFlow      = this.orderFlowDAO.genSqlOrderFlowBySome(begindate, enddate, flowordertypes, currentBranchids, isnowdata, false);
			//String sqlOrderFlowLimit = this.orderFlowDAO.genSqlOrderFlowBySome(begindate, enddate, flowordertypes, currentBranchids, isnowdata, true);
			
			String cwbordertypeids = this.dataStatisticsService.getStrings(cwbordertypeid);
			String kufangids       = this.dataStatisticsService.getStrings(kufangid);
			
			//将发货仓库里面拼接上0，保持导出和查询一直
			if(kufangids != null && kufangids.length() > 0){
				kufangids += ",0";
			}
			
			// 获订单总数
			count = this.cwbDAO.getDaoHuoCount(customerid, cwbordertypeids, kufangids, "", sqlOrderFlow);
			// 获取订单中金额
			sum   = this.cwbDAO.getDaoHuoSum(customerid, cwbordertypeids, kufangids, "", sqlOrderFlow);
			//获取订单明细
			//clist = this.cwbDAO.getDaoHuoByPage(page, customerid, cwbordertypeids, kufangids, "", sqlOrderFlowLimit);
			clist = this.cwbDAO.getDaoHuoByPage(page, customerid, cwbordertypeids, kufangids, "", sqlOrderFlow);
			
			/*
			List<String> orderFlowList = this.orderFlowDAO
					.getOrderFlowBySome(begindate, enddate, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), this.dataStatisticsService
							.getStrings(currentBranchid), isnowdata);
			if (orderFlowList.size() > 0) {
				orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}

			String cwbordertypeids = this.dataStatisticsService.getStrings(cwbordertypeid);
			String kufangids = this.dataStatisticsService.getStrings(kufangid);
			
			//将发货仓库里面拼接上0，保持导出和查询一直
			if(kufangids != null && kufangids.length() > 0){
				kufangids += ",0";
			}
			
			// 获取值
			count = this.cwbDAO.getcwborderDaoHuoCount(customerid, cwbordertypeids, orderflowcwbs, kufangids, "");

			sum = this.cwbDAO.getcwborderDaoHuoSum(customerid, cwbordertypeids, orderflowcwbs, kufangids, "");

			clist = this.cwbDAO.getDaoHuocwbOrderByPage(page, customerid, cwbordertypeids, orderflowcwbs, kufangids, "");
			*/
			//Modified end

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService
					.getCwbOrderView(clist, this.customerDAO.getAllCustomersNew(), customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}
		model.addAttribute("count", count);
		model.addAttribute("customerlist", customerList);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		this.logger.info("分站到货统计，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return this.querypage(model, new String[] {}, currentBranchid, null, new String[] {}, 8, new String[] {}, cwbordertypeid, kufangid, new String[] {}, new String[] {});
	}

	/**
	 * 按到站时间，统计到站后，没有反馈的订单
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param nextbranchid
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/noResultsearch/{page}")
	public String noResultsearch(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangids", required = false, defaultValue = "") String[] kufangids, @RequestParam(value = "branchids", required = false, defaultValue = "") String[] branchids, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		List<Branch> branchList = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());
		List<Branch> kufangList = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.KuFang.getValue());
		model.addAttribute("branchAllList", branchList);
		model.addAttribute("kufangList", kufangList);
		model.addAttribute("begindate", begindate);
		model.addAttribute("enddate", enddate);
		List<String> kufangidList = new ArrayList<String>();
		if ((kufangids != null) && (kufangids.length > 0)) {
			for (String kufangid : kufangids) {
				kufangidList.add(kufangid);
			}
		}
		List<String> branchidList = new ArrayList<String>();
		if ((branchids != null) && (branchids.length > 0)) {
			for (String branchid : branchids) {
				branchidList.add(branchid);
			}
		}
		if (isshow == 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				branchidList.add(branch.getBranchid() + "");
			}
		}
		model.addAttribute("kufangidstr", kufangidList);
		model.addAttribute("branchidstr", branchidList);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		if (isshow != 0) {
			this.logger
					.info("无结果订单统计，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangids:" + this.dataStatisticsService.getStrings(kufangids) + ",branchids:" + this.dataStatisticsService
							.getStrings(branchids) + ",isshow:" + isshow + ",page:" + page, this.getSessionUser().getRealname());

			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			// 定义参数
			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			/*
			 * 1.先获取满足站点和库房以及操作时间范围内的所有到站订单 SELECT DISTINCT d.cwb FROM
			 * `express_ops_order_flow` AS f LEFT JOIN `express_ops_cwb_detail`
			 * AS d ON f.cwb=d.cwb WHERE f.flowordertype=7 AND
			 * f.credate>='2013-05-08 15:10:29' AND f.credate<='2013-05-09
			 * 15:10:29' AND d.startbranchid IN(192) AND d.`carwarehouse`
			 * IN(186);
			 *
			 * 2.获取查到的订单中比这个时间晚的还做到站的订单 SELECT DISTINCT cwb FROM
			 * express_ops_order_flow WHERE cwb IN(
			 * '213050437951','213053501759','113050535325','213053646051','213050380083','213050592611','11305040007643','9130111063179','13050661483039','13050664551938','13050664576638','13050662866339','13050664901335','13050663012338','33136166','33213224','33185070','6054770549','6059222246','6057476148','6055624389','6058327024','6059322728','6058648587','6055483025','6058381644','6059103204','6058637900','6056668307','6052403787','6043998364','6046242129','6046107920','6048141145')
			 * AND credate>'2013-05-09 15:10:29' AND flowordertype=7;
			 *
			 * 3.获取 把第一步查到订单加上排除掉第二步查到的订单 SELECT DISTINCT cwb FROM
			 * express_ops_order_flow WHERE cwb IN(
			 * '213050437951','213053501759','113050535325','213053646051','213050380083','213050592611','11305040007643','9130111063179','13050661483039','13050664551938','13050664576638','13050662866339','13050664901335','13050663012338','33136166','33213224','33185070','6054770549','6059222246','6057476148','6055624389','6058327024','6059322728','6058648587','6055483025','6058381644','6059103204','6058637900','6056668307','6052403787','6043998364','6046242129','6046107920','6048141145')
			 * AND cwb NOT IN('--') AND flowordertype=7;
			 *
			 * 4.把第三步查到订单在反馈中查出哪些订单已经反馈 SELECT DISTINCT cwb FROM
			 * `express_ops_delivery_state` WHERE cwb IN(
			 * '213050437951','213053501759','113050535325','213053646051','213050380083','213050592611','11305040007643','9130111063179','13050661483039','13050664551938','13050664576638','13050662866339','13050664901335','13050663012338','33136166','33213224','33185070','6054770549','6059222246','6057476148','6055624389','6058327024','6059322728','6058648587','6055483025','6058381644','6059103204','6058637900','6056668307','6052403787','6043998364','6046242129','6046107920','6048141145')
			 * AND state=1 AND `deliverystate`>0;
			 *
			 * 5.获取第三步查到订单排除掉第四步查到的订单并且分页取10个 SELECT DISTINCT cwb FROM
			 * express_ops_order_flow WHERE cwb IN(
			 * '213050437951','213053501759','113050535325','213053646051','213050380083','213050592611','11305040007643','9130111063179','13050661483039','13050664551938','13050664576638','13050662866339','13050664901335','13050663012338','33136166','33213224','33185070','6054770549','6059222246','6057476148','6055624389','6058327024','6059322728','6058648587','6055483025','6058381644','6059103204','6058637900','6056668307','6052403787','6043998364','6046242129','6046107920','6048141145')
			 * AND cwb NOT IN('213050437951') AND flowordertype=7 LIMIT 0,10;
			 *
			 * 6.把第五步查到的订单去查订单表的订单详情 SELECT * FROM express_ops_cwb_detail WHERE
			 * cwb IN(
			 * '11305040007643','113050535325','13050661483039','13050662866339','13050663012338','13050664551938','13050664576638','13050664901335','213050380083','213050592611');
			 */
			// 第一步
			String oneCwbs = this.dataStatisticsService
					.getCwbs(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), begindate, enddate, kufangids, branchids);
			String twoCbs = "";
			String threeCwbs = "";
			String fourCwbs = "";
			String fiveCwbs = "";
			String fiveCwbsAll = "";
			if ("".equals(oneCwbs)) {
				model.addAttribute("count", count);
				model.addAttribute("orderlist", cwbOrderView);
				model.addAttribute("page_obj", pageparm);
				model.addAttribute("page", page);
				return "datastatistics/noresultlist";
			}
			if (!"".equals(oneCwbs)) {// 如果第一步查不到订单后面的都不用管了
				List<String> twoCwbsList = this.orderFlowDAO
						.getTwoCwbs(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), oneCwbs, enddate);
				twoCbs = this.dataStatisticsService.getCwbs(twoCwbsList);
				twoCbs = twoCbs.equals("") ? "'--'" : twoCbs;
			}
			if (!"".equals(twoCbs)) {// 如果第二步查不到订单第三步就不用调用了
				List<String> threeCwbsList = this.orderFlowDAO
						.getThreeCwbs(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), oneCwbs, twoCbs);
				threeCwbs = this.dataStatisticsService.getCwbs(threeCwbsList);
			}
			if (!"".equals(threeCwbs)) {// 如果第三步才到订单号
				List<String> fourCwbsList = this.orderFlowDAO.getFourCwbs(threeCwbs);
				fourCwbs = this.dataStatisticsService.getCwbs(fourCwbsList);
				fourCwbs = fourCwbs.equals("") ? "'--'" : fourCwbs;
			} else if (!"".equals(twoCbs)) {
				model.addAttribute("count", count);
				model.addAttribute("orderlist", cwbOrderView);
				model.addAttribute("page_obj", pageparm);
				model.addAttribute("page", page);
				return "datastatistics/noresultlist";

			}
			if (!"".equals(fourCwbs)) {
				List<String> fiveCwbsList = this.orderFlowDAO
						.getFiveCwbs(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), threeCwbs, fourCwbs, page);
				List<String> fiveCwbsAllList = this.orderFlowDAO
						.getFiveCwbs(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), threeCwbs, fourCwbs, -1);
				fiveCwbs = this.dataStatisticsService.getCwbs(fiveCwbsList);
				fiveCwbsAll = this.dataStatisticsService.getCwbs(fiveCwbsAllList);
				count = this.orderFlowDAO.getFiveCwbsCount(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), threeCwbs, fourCwbs);
			}
			if (!"".equals(fiveCwbs)) {
				clist = this.cwbDAO.getCwbOrderByCwbs(fiveCwbs);
				sum = this.cwbDAO.getSumByCwbs(fiveCwbsAll);
			} else {
				model.addAttribute("count", count);
				model.addAttribute("orderlist", cwbOrderView);
				model.addAttribute("page_obj", pageparm);
				model.addAttribute("page", page);
				return "datastatistics/noresultlist";
			}
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			List<Customer> customerList = this.customerDAO.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchAllList = this.branchDAO.getAllBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchAllList, userList, reasonList, begindate, enddate, remarkList);

		}

		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		this.logger.info("无结果订单统计，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return "datastatistics/noresultlist";
	}

	@RequestMapping("/exportNoresult")
	public void excel(@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangids", required = false, defaultValue = "") String[] kufangids, @RequestParam(value = "branchids", required = false, defaultValue = "") String[] branchids, @RequestParam(value = "exportmould", required = false, defaultValue = "") String mouldfieldids, HttpServletResponse response) {

		String sql = "";
		begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
		enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
		String oneCwbs = this.dataStatisticsService
				.getCwbs(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), begindate, enddate, kufangids, branchids);
		String twoCbs = "";
		String threeCwbs = "";
		String fourCwbs = "";
		String fiveCwbsAll = "";
		Map<String, Object> paramsMAP = new HashMap<String, Object>();
		paramsMAP.put("begindate", begindate);
		paramsMAP.put("enddate", enddate);
		paramsMAP.put("kufangids", kufangids);
		paramsMAP.put("branchids",branchids);
		if (!"".equals(oneCwbs)) {// 如果第一步查不到订单后面的都不用管了
			List<String> twoCwbsList = this.orderFlowDAO
					.getTwoCwbs(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), oneCwbs, enddate);
			twoCbs = this.dataStatisticsService.getCwbs(twoCwbsList);
			twoCbs = twoCbs.equals("") ? "'--'" : twoCbs;
		}
		if (!"".equals(twoCbs)) {// 如果第二步查不到订单第三步就不用调用了
			List<String> threeCwbsList = this.orderFlowDAO
					.getThreeCwbs(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), oneCwbs, twoCbs);
			threeCwbs = this.dataStatisticsService.getCwbs(threeCwbsList);
		}
		if (!"".equals(threeCwbs)) {// 如果第三步才到订单号
			List<String> fourCwbsList = this.orderFlowDAO.getFourCwbs(threeCwbs);
			fourCwbs = this.dataStatisticsService.getCwbs(fourCwbsList);
			fourCwbs = fourCwbs.equals("") ? "'--'" : fourCwbs;
		}
		if (!"".equals(fourCwbs)) {
			List<String> fiveCwbsAllList = this.orderFlowDAO
					.getFiveCwbs(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), threeCwbs, fourCwbs, -1);
			fiveCwbsAll = this.dataStatisticsService.getCwbs(fiveCwbsAllList);
			sql = this.cwbDAO.getSqlByCwb(fiveCwbsAll);
		}
		if (!"".equals(sql)) {
			this.dataStatisticsService.exportExcelByNoresultMethod(response, sql, paramsMAP, mouldfieldids);
		}

	}

	/**
	 * 中转订单统计功能
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param customerid
	 * @param cwbordertypeid
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/zhongzhuandata/{page}")
	public String zhongzhuandata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid, @RequestParam(value = "type", required = false, defaultValue = "startbranchid") String type, @RequestParam(value = "branchid2", required = false, defaultValue = "") String[] branchid2, @RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName, @RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		Branch nowBranch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		String[] nextbranchids = null;
		String[] startbranchids = null;
		if (isshow != 0) {
			this.logger.info("中转订单统计，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",branchid:" + branchid + ",type:" + type + ",branchid2:" + this.dataStatisticsService
					.getStrings(branchid2) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:" + page, this.getSessionUser().getRealname());

			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			// 定义参数
			String orderName = " " + orderbyName + " " + orderbyId;
			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			String orderflowcwbs = "";
			List<String> orderFlowList = new ArrayList<String>();
			long flowordertype = FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
			if (type.equals("startbranchid")) {
				nextbranchids = branchid2;
				startbranchids = new String[] { branchid + "" };
				flowordertype = FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue();
			} else if (type.equals("nextbranchid")) {
				nextbranchids = new String[] { branchid + "" };
				startbranchids = branchid2;
			}
			orderFlowList = this.orderFlowDAO.getOrderFlowForZhongZhuan(begindate, enddate, flowordertype, nextbranchids, startbranchids);
			if (orderFlowList.size() > 0) {
				orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(orderFlowList);
			} else {
				orderflowcwbs = "'--'";
			}
			// 获取值
			count = this.cwbDAO.getcwborderCountHuiZong("", "", "", "", "", "", orderflowcwbs, "", "", "", 0, 0, 9, "");

			clist = this.cwbDAO.getcwbOrderByPageHuiZong(page, "", "", orderName, "", "", "", "", orderflowcwbs, "", "", "", 0, 0, 9, "");

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			this.setSesstion("", "", 0, orderbyName, 0, 0, 0, orderflowcwbs, 0, 0, 0, 0, 0, count, request);

			List<Customer> customerList = this.customerDAO.getAllCustomersNew();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDao.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getAllRemark();
			// 赋值显示对象
			cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		}
		if (nowBranch.getSitetype() == BranchEnum.KuFang.getValue()) {
			model.addAttribute("iskufang", "1");
		} else {
			model.addAttribute("iskufang", "0");
		}

		List<String> branchid2list = this.dataStatisticsService.getList(branchid2);
		model.addAttribute("branchid2Str", branchid2list);

		model.addAttribute("notuihuobranchList", this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.ZhanDian
				.getValue()));
		model.addAttribute("zhongzhuanbranchList", this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhongZhuan.getValue() + ""));
		model.addAttribute("count", count);
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		this.logger.info("中转订单统计，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		return "datastatistics/zhongzhuandata";
	}

	/**
	 * 站点出站统计
	 *
	 * @param model
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param startbranchid
	 * @param nextbranchid
	 * @param flowordertype
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/zhandianchuzhanlist/{page}")
	public String zhandianchuzhanlist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "startbranchid", required = false, defaultValue = "") String[] startbranchid, @RequestParam(value = "nextbranchid", required = false, defaultValue = "") String[] nextbranchid, @RequestParam(value = "flowordertype", required = false, defaultValue = "6") long flowordertype, @RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName, @RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, HttpServletResponse response, HttpServletRequest request) {
		try {
			long count = 0;
			Page pageparm = new Page();
			List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
			List<Branch> bList = this.branchDAO.getAllBranches();
			List<Branch> branchList = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());

			if ((isshow != 0) && (startbranchid.length > 0) && (nextbranchid.length > 0)) {
				this.logger
						.info("站点出站统计，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",startbranchid:" + this.dataStatisticsService.getStrings(startbranchid) + ",flowordertype:" + flowordertype + ",nextbranchid:" + this.dataStatisticsService
								.getStrings(nextbranchid) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:" + page, this.getSessionUser().getRealname());

				begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
				enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
				// 定义参数
				List<CwbOrder> clist = new ArrayList<CwbOrder>();

				List<OrderFlow> orderLastList = new ArrayList<OrderFlow>();
				String sig = "'";
				List<OrderFlow> orderFlowList = this.orderFlowDAO.getOrderFlowForZhanDianChuZhan(begindate, enddate, startbranchid, nextbranchid, flowordertype);
				if (orderFlowList.size() > 0) {
					StringBuffer cwbTemp = new StringBuffer();
					for (OrderFlow of : orderFlowList) {// 第一次循环，过滤获取入库时间符合条件的数据
						if (cwbTemp.indexOf(sig + of.getCwb() + sig) == -1) {
							cwbTemp = cwbTemp.append(sig).append(of.getCwb()).append(sig);
							orderLastList.add(of);
						}
					}
				}

				for (int i = (int) ((page - 1) * Page.ONE_PAGE_NUMBER); (i < (page * Page.ONE_PAGE_NUMBER)) && (i < orderLastList.size()); i++) {
					clist.add(this.om.readValue(JSONObject.fromObject(orderLastList.get(i).getFloworderdetail()).getString("cwbOrder"), CwbOrder.class));
				}

				// 获取值
				count = orderLastList.size();

				pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

				List<Customer> customerList = this.customerDAO.getAllCustomersNew();
				List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();

				List<User> userList = this.userDAO.getAllUser();
				List<Reason> reasonList = this.reasonDao.getAllReason();
				List<Remark> remarkList = this.remarkDAO.getAllRemark();
				// 赋值显示对象
				cwbOrderView = this.dataStatisticsService.getCwbOrderView(clist, customerList, customerWareHouseList, bList, userList, reasonList, "", "", remarkList);

			}

			List<String> nextbranchidlist = this.dataStatisticsService.getList(nextbranchid);
			List<String> startbranchidlist = this.dataStatisticsService.getList(startbranchid);
			model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
			model.addAttribute("nextbranchidStr", nextbranchidlist);
			model.addAttribute("startbranchidStr", startbranchidlist);
			model.addAttribute("branchList", branchList);
			model.addAttribute("count", count);
			model.addAttribute("orderlist", cwbOrderView);
			model.addAttribute("page_obj", pageparm);
			model.addAttribute("page", page);
			this.logger.info("站点出站统计，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		} catch (Exception e) {
			this.logger.error("站点出站统计出错", e);
		}
		return "datastatistics/zhandianchuzhanlist";
	}

	@RequestMapping("/checkKufang/{id}")
	public @ResponseBody String editlevel(Model model, @PathVariable("id") long id) {
		Branch nowBranch = this.branchDAO.getBranchByBranchid(id);
		if (nowBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			return "{\"errorCode\":0}";
		} else {
			return "{\"errorCode\":1}";
		}
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page, @RequestParam(value = "sign", required = false, defaultValue = "0") long sign) {
		this.dataStatisticsService.DataStatisticsExportExcelMethod(response, request, page, sign);
	}

	/**
	 * 库房在途订单统计导出功能
	 *
	 * @param model
	 * @param response
	 * @param request
	 * @param page
	 */
	@RequestMapping("/zaituexportExcle")
	public void zaituexportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page) {
		this.dataStatisticsService.DataStatisticsZaituExportExcelMethod(response, request, page);
	}

	@RequestMapping("/exportOutWareExcle")
	public void exportOutWareExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page, @RequestParam(value = "sign", required = false, defaultValue = "0") long sign) {
		this.dataStatisticsService.DataStatisticsExportOutWareExcelMethod(response, request, page, sign);
	}

	@RequestMapping("/exportIntoWareExcle")
	public void exportIntoWareExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page) {
		this.dataStatisticsService.DataStatisticsExportIntoWareExcelMethod(response, request, page);
	}

	private void setSesstion(String begindate, String enddate, long customerid, String orderbyName, long startbranchid, long nextbranchid, long cwbordertypeid, String orderflowcwbs, long currentBranchid, long dispatchbranchid, long kufangid, long flowordertype, long paywayid, long count, HttpServletRequest request) {
		request.getSession().setAttribute("begindate", begindate);
		request.getSession().setAttribute("enddate", enddate);
		request.getSession().setAttribute("customerid", customerid);
		request.getSession().setAttribute("orderbyName", orderbyName);
		request.getSession().setAttribute("startbranchid", startbranchid);
		request.getSession().setAttribute("nextbranchid", nextbranchid);
		request.getSession().setAttribute("cwbordertypeid", cwbordertypeid);
		request.getSession().setAttribute("orderflowcwbs", orderflowcwbs);
		request.getSession().setAttribute("currentBranchid", currentBranchid);
		request.getSession().setAttribute("dispatchbranchid", dispatchbranchid);
		request.getSession().setAttribute("kufangid", kufangid);
		request.getSession().setAttribute("flowordertype", flowordertype);
		request.getSession().setAttribute("paywayid", paywayid);
		request.getSession().setAttribute("count", count);

	}

	@RequestMapping("/updateDeliver")
	public @ResponseBody String updateDeliver(Model model, @RequestParam("branchid") long branchid) {
		if (branchid > 0) {
			List<User> list = this.userDAO.getAllUserByRolesAndBranchid("2,4", branchid);

			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}

	}

	@RequestMapping("/updateDeliverByBranchids")
	public @ResponseBody String updateDeliverByBranchids(Model model, @RequestParam("branchid") String branchids) {
		if (branchids.length() > 0) {
			branchids = branchids.substring(0, branchids.length() - 1);
			List<User> list = this.userDAO.getAllUserByRolesAndBranchids("2,4", branchids);
			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}
	}

	@RequestMapping("/updateEmaildateid")
	public @ResponseBody List<EmailDate> updateEmaildateid(Model model, @RequestParam(value = "customerid", defaultValue = "0") long customerid) {
		return this.emaildateDAO.getEmailDateByCustomerid(customerid);
	}

	/**
	 * 退货出站统计
	 *
	 * @param model
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param branchid
	 * @param customerid
	 * @param istuihuozhanruku
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/tuihuochuzhanlist/{page}")
	public String tuihuochuzhanlist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchids, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerids, @RequestParam(value = "istuihuozhanruku", required = false, defaultValue = "0") long istuihuozhanruku, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, HttpServletResponse response, HttpServletRequest request) {
		try {
			long count = 0;
			Page pageparm = new Page();
			List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
			List<Branch> branchnameList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
			model.addAttribute("branchnameList", branchnameList);
			List<Customer> customerList = this.customerDAO.getAllCustomers();

			if (isshow != 0) {
				this.logger
						.info("退货出站统计，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",branchids:" + this.dataStatisticsService.getStrings(branchids) + ",istuihuozhanruku:" + istuihuozhanruku + ",customerids:" + this.dataStatisticsService
								.getStrings(customerids) + ",isshow:" + isshow + ",page:" + page, this.getSessionUser().getRealname());

				begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
				enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
				// 定义参数
				List<CwbOrder> clist = new ArrayList<CwbOrder>();
				List<TuihuoRecord> tuihuoRecordList = this.tuihuoRecordDAO
						.getTuihuoRecordByTuihuochuzhan(begindate, enddate, this.dataStatisticsService.getStrings(branchids), this.dataStatisticsService.getStrings(customerids), istuihuozhanruku, page);
				StringBuffer cwbTemp = new StringBuffer();
				if (tuihuoRecordList.size() > 0) {
					for (TuihuoRecord tr : tuihuoRecordList) {// 第一次循环，过滤获取入库时间符合条件的数据
						cwbTemp.append("'" + tr.getCwb() + "',");
					}
				}
				if (!"".equals(cwbTemp) && (cwbTemp.length() > 0)) {
					String cwbs = cwbTemp.substring(0, cwbTemp.length() - 1);
					clist = this.cwbDAO.getCwbByCwbs(cwbs);
					// 获取值
					count = this.tuihuoRecordDAO.getCountTuihuoRecordByTuihuochuzhan(begindate, enddate, this.dataStatisticsService.getStrings(branchids), this.dataStatisticsService
							.getStrings(customerids), istuihuozhanruku);

					pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

					List<Branch> branchList = this.branchDAO.getAllBranches();

					// 赋值显示对象

					cwbOrderView = this.dataStatisticsService.getCwbOrderTuiHuoView(clist, tuihuoRecordList, this.customerDAO.getAllCustomersNew(), branchList);
				}
			}
			List<String> branchidlist = this.dataStatisticsService.getList(branchids);
			List<String> customeridList = this.dataStatisticsService.getList(customerids);

			model.addAttribute("customerList", customerList);
			model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
			model.addAttribute("branchidStr", branchidlist);
			model.addAttribute("customeridStr", customeridList);
			model.addAttribute("count", count);
			model.addAttribute("orderlist", cwbOrderView);
			model.addAttribute("page_obj", pageparm);
			model.addAttribute("page", page);
			this.logger.info("退货出站统计，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		} catch (Exception e) {
			this.logger.error("退货出站统计出错", e);
		}
		return "datastatistics/tuihuochuzhanlist";
	}

	/**
	 * 退货站入库统计
	 *
	 * @param model
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param branchids
	 * @param customerids
	 * @param cwbordertypeid
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/tuihuozhanrukulist/{page}")
	public String tuihuozhanrukulist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchids, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerids, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeids, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, HttpServletResponse response, HttpServletRequest request) {
		try {
			long count = 0;
			Page pageparm = new Page();
			List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
			List<Branch> branchnameList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
			model.addAttribute("branchnameList", branchnameList);
			List<Customer> customerList = this.customerDAO.getAllCustomers();

			if (isshow != 0) {
				this.logger
						.info("退货站入库统计，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",branchids:" + this.dataStatisticsService.getStrings(branchids) + ",cwbordertypeids:" + this.dataStatisticsService
								.getStrings(cwbordertypeids) + ",customerids:" + this.dataStatisticsService.getStrings(customerids) + ",isshow:" + isshow + ",page:" + page, this.getSessionUser()
								.getRealname());

				begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
				enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
				// 定义参数
				List<CwbOrder> clist = new ArrayList<CwbOrder>();
				List<TuihuoRecord> tuihuoRecordList = this.tuihuoRecordDAO
						.getTuihuoRecordByTuihuozhanrukuOnPage(begindate, enddate, this.dataStatisticsService.getStrings(branchids), this.dataStatisticsService.getStrings(customerids), this.dataStatisticsService
								.getStrings(cwbordertypeids), page);
				StringBuffer cwbTemp = new StringBuffer();
				if (tuihuoRecordList.size() > 0) {
					for (TuihuoRecord tr : tuihuoRecordList) {// 第一次循环，过滤获取入库时间符合条件的数据
						cwbTemp.append("'" + tr.getCwb() + "',");
					}
				}
				if (!"".equals(cwbTemp) && (cwbTemp.length() > 0)) {
					String cwbs = cwbTemp.substring(0, cwbTemp.length() - 1);
					clist = this.cwbDAO.getCwbByCwbs(cwbs);
					// 获取值
					count = this.tuihuoRecordDAO.getCountTuihuoRecordByTuihuozhanruku(begindate, enddate, this.dataStatisticsService.getStrings(branchids), this.dataStatisticsService
							.getStrings(customerids), this.dataStatisticsService.getStrings(cwbordertypeids));

					pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

					List<Branch> branchList = this.branchDAO.getAllBranches();

					// 赋值显示对象
					cwbOrderView = this.dataStatisticsService.getCwbOrderTuiHuoView(clist, tuihuoRecordList, this.customerDAO.getAllCustomersNew(), branchList);
				}
			}
			List<String> branchidlist = this.dataStatisticsService.getList(branchids);
			List<String> customeridList = this.dataStatisticsService.getList(customerids);
			List<String> cwbordertypeidList = this.dataStatisticsService.getList(cwbordertypeids);

			model.addAttribute("customerList", customerList);
			model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
			model.addAttribute("branchidStr", branchidlist);
			model.addAttribute("customeridStr", customeridList);
			model.addAttribute("cwbordertypeidStr", cwbordertypeidList);
			model.addAttribute("count", count);
			model.addAttribute("orderlist", cwbOrderView);
			model.addAttribute("page_obj", pageparm);
			model.addAttribute("page", page);
			this.logger.info("退货站入库统计，当前操作人{},条数{}", this.getSessionUser().getRealname(), count);
		} catch (Exception e) {
			this.logger.error("退货站入库统计出错", e);
		}
		return "datastatistics/tuihuozhanrukulist";
	}

	@RequestMapping("/tuihuoexportExcle")
	public void tuihuoexportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begindate1", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate1", required = false, defaultValue = "") String enddate, @RequestParam(value = "branchid1", required = false, defaultValue = "") String[] branchids, @RequestParam(value = "customerid1", required = false, defaultValue = "") String[] customerids, @RequestParam(value = "istuihuozhanruku1", required = false, defaultValue = "0") long istuihuozhanruku, @RequestParam(value = "tuihuotype", required = false, defaultValue = "1") long tuihuotype, @RequestParam(value = "cwbordertypeid1", required = false, defaultValue = "") String[] cwbordertypeids, @RequestParam(value = "sign", required = false, defaultValue = "") String sign) {

		this.dataStatisticsService
				.cwbExportExcelMethod(response, request, begindate, enddate, this.dataStatisticsService.getStrings(branchids), this.dataStatisticsService.getStrings(customerids), this.dataStatisticsService
						.getStrings(cwbordertypeids), istuihuozhanruku, tuihuotype);
	}

	@RequestMapping("/selectallnexusbranch")
	public @ResponseBody String selectallnexusbranch(@RequestParam(value = "branchname", required = false, defaultValue = "") String branchname) {
		if (branchname.length() > 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
			List<Branch> branchList = this.branchDAO
					.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan
							.getValue());

			if (branchList.size() == 0) {
				branchList.add(branch);
			} else {
				if (!this.dataStatisticsService.checkBranchRepeat(branchList, branch)) {
					branchList.add(branch);
				}
			}

			List<Branch> dispatchbranchidStr = this.branchDAO.getBranchByBranchnameMoHu(branchname);
			List<Branch> dispatchbranchidnewStr = new ArrayList<Branch>();
			if ((dispatchbranchidStr.size() > 0) && (branchList.size() > 0)) {
				for (Branch b : branchList) {
					for (Branch branchStr : dispatchbranchidStr) {
						if (branchStr.getBranchid() == b.getBranchid()) {
							dispatchbranchidnewStr.add(branchStr);
						}
					}
				}
			}
			return JSONArray.fromObject(dispatchbranchidnewStr).toString();
		} else {
			return "[]";
		}
	}

	/**
	 * 根据站点名称精确查询到匹配的站点名称
	 *
	 * @param branchname
	 * @return
	 */
	@RequestMapping("/selectnexusbranchbybranchname")
	public @ResponseBody String selectnexusbranchbybranchname(@RequestParam(value = "branchname", required = false, defaultValue = "") String branchname) {
		if (branchname.length() > 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
			List<Branch> branchList = this.branchDAO
					.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan
							.getValue());

			if (branchList.size() == 0) {
				branchList.add(branch);
			} else {
				if (!this.dataStatisticsService.checkBranchRepeat(branchList, branch)) {
					branchList.add(branch);
				}
			}

			Branch deliverybranch = this.branchDAO.getBranchByBranchname(branchname);
			Branch newdispatchbranch = new Branch();
			for (Branch b : branchList) {
				if (deliverybranch.getBranchid() == b.getBranchid()) {
					newdispatchbranch = deliverybranch;
				}
			}
			return JSONObject.fromObject(newdispatchbranch).toString();
		} else {
			return "{}";
		}
	}

	/**
	 * 根据输入名称 查询机构
	 *
	 * @param branchname
	 * @return
	 */
	@RequestMapping("/selectbranchbybranchname")
	public @ResponseBody String selectbranchbybranchname(@RequestParam(value = "branchname", required = false, defaultValue = "") String branchname) {
		if (branchname.length() > 0) {
			Branch deliverybranch = this.branchDAO.getBranchByBranchname(branchname);
			return JSONObject.fromObject(deliverybranch).toString();
		} else {
			return "{}";
		}
	}

	/**
	 * 供货商发货汇总
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param isshow
	 * @return
	 */
	@RequestMapping("/customerfahuodata")
	public String customerfahuodata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangid", required = false, defaultValue = "0") long kufangid, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<Branch> kufanglist = this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		Map<Long, Long> customMap = new HashMap<Long, Long>();
		if (isshow != 0) {
			this.logger.info("供货商发货汇总，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + kufangid + ",isshow:" + isshow, this.getSessionUser().getRealname());
			customMap = this.dataStatisticsService.getCustomerMap(customerList, kufangid, begindate, enddate);
		}
		model.addAttribute("customMap", customMap);
		model.addAttribute("customerList", customerList);
		model.addAttribute("kufanglist", kufanglist);
		this.logger.info("供货商发货汇总，当前操作人{},参数{}", this.getSessionUser().getRealname(), kufangid + "--" + begindate + "--" + enddate);
		return "datastatistics/customerfahuodatalist";
	}

	/**
	 * 供货商发货汇总详情功能
	 *
	 * @param model
	 * @param page
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @return
	 */
	@RequestMapping("/customerfahuodatashow/{page}")
	public String customerfahuodatashow(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangid", required = false, defaultValue = "0") long kufangid) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<Branch> kufanglist = this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		List<CwbOrder> clist = this.cwbDAO.getCustomerfahuodataList(page, customerid, kufangid, begindate, enddate);

		model.addAttribute("clist", clist);
		model.addAttribute("customerList", customerList);
		model.addAttribute("kufanglist", kufanglist);
		model.addAttribute("page_obj", new Page(this.cwbDAO.getCustomerfahuodataCount(customerid, kufangid, begindate, enddate), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		return "datastatistics/customerfahuodatashow";
	}

	/**
	 * 供货商发货汇总导出
	 *
	 * @param model
	 * @param response
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 */
	@RequestMapping("/customerfahuodata_excel")
	public void customerfahuodata_excel(Model model, HttpServletResponse response, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangid", required = false, defaultValue = "0") long kufangid, @RequestParam(value = "exportmould", required = false, defaultValue = "0") String exportEume) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(exportEume);
		cloumnName1 = new String[listSetExportField.size()];
		cloumnName2 = new String[listSetExportField.size()];
		cloumnName3 = new String[listSetExportField.size()];
		for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
			cloumnName1[k] = listSetExportField.get(j).getFieldname();
			cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
			cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "供货商发货汇总信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "customerfahuo_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据

			final String sql = this.cwbDAO.getCustomerfahuodataSql(customerid, kufangid, begindate, enddate);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataStatisticsController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = DataStatisticsController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = DataStatisticsController.this.branchDAO.getAllBranches();
					final List<Common> commonList = DataStatisticsController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataStatisticsController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = DataStatisticsController.this.remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = DataStatisticsController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = DataStatisticsController.this.reasonDao.getAllReason();
					DataStatisticsController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {
							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}
						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap, Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = DataStatisticsController.this.exportService
										.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList, cwbspayupidMap, complaintMap);
								if (cloumnName6[i].equals("double")) {
									cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
								} else {
									cell.setCellValue(a == null ? "" : a.toString());
								}
							}
						}

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								this.processRow(rs);
							}
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (this.recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : this.recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = this.getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = DataStatisticsController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp, complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : DataStatisticsController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : DataStatisticsController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : DataStatisticsController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
							return cwbspayupidMap;
						}
					});

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 站点到货汇总
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param isshow
	 * @return
	 */
	@RequestMapping("/zhandiandaohuodata")
	public String zhandiandaohuodata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<Branch> zhandianlist = this.branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		Map<Long, Long> branchMap = new HashMap<Long, Long>();
		if (isshow != 0) {
			this.logger.info("站点到货汇总，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",isshow:" + isshow, this.getSessionUser().getRealname());
			if (zhandianlist.size() > 0) {
				for (Branch b : zhandianlist) {
					List<String> ordercwblist = this.orderFlowDAO
							.getDaohuoOrderFlow(begindate, enddate, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), b
									.getBranchid());
					branchMap.put(b.getBranchid(), (long) ordercwblist.size());
				}
			}
		}
		model.addAttribute("branchMap", branchMap);
		model.addAttribute("zhandianlist", zhandianlist);
		this.logger.info("站点到货汇总，当前操作人{},参数{}", this.getSessionUser().getRealname(), begindate + "--" + enddate);
		return "datastatistics/zhandiandaohuodatalist";
	}

	/**
	 * 站点到货汇总详情
	 *
	 * @param model
	 * @param page
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @return
	 */
	@RequestMapping("/zhandiandaohuodatashow/{page}")
	public String zhandiandaohuodatashow(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate) {
		String orderflowcwbs = "";
		List<String> ordercwblist = this.orderFlowDAO
				.getDaohuoOrderFlow(begindate, enddate, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), branchid);

		if (ordercwblist.size() > 0) {
			orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(ordercwblist);
		} else {
			orderflowcwbs = "'--'";
		}

		List<CwbOrder> clist = this.cwbDAO.getCwbByCwbsPage(page, orderflowcwbs);

		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
		List<Branch> branchList = this.branchDAO.getAllBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<Reason> reasonList = this.reasonDao.getAllReason();
		List<Remark> remarkList = this.remarkDAO.getAllRemark();

		// 赋值显示对象
		List<CwbOrderView> cwbOrderView = this.dataStatisticsService
				.getCwbOrderView(clist, this.customerDAO.getAllCustomersNew(), customerWareHouseList, branchList, userList, reasonList, begindate, enddate, remarkList);

		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("customerList", customerList);
		model.addAttribute("page_obj", new Page(this.cwbDAO.getCwbOrderCwbsCount(orderflowcwbs), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		return "datastatistics/zhandiandaohuodatashow";
	}

	/**
	 * 站点到货汇总导出
	 *
	 * @param model
	 * @param response
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 */
	@RequestMapping("/zhandiandaohuodata_excel")
	public void zhandiandaohuodata_excel(Model model, HttpServletResponse response, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "exportmould", required = false, defaultValue = "0") String exportEume) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(exportEume);
		cloumnName1 = new String[listSetExportField.size()];
		cloumnName2 = new String[listSetExportField.size()];
		cloumnName3 = new String[listSetExportField.size()];
		for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
			cloumnName1[k] = listSetExportField.get(j).getFieldname();
			cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
			cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "站点到货汇总信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "zhandiandaohuo_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			String orderflowcwbs = "";
			List<String> ordercwblist = this.orderFlowDAO
					.getDaohuoOrderFlow(begindate, enddate, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), branchid);

			if (ordercwblist.size() > 0) {
				orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(ordercwblist);
			} else {
				orderflowcwbs = "'--'";
			}

			final String sql = this.cwbDAO.getSqlByCwb(orderflowcwbs);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataStatisticsController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = DataStatisticsController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = DataStatisticsController.this.branchDAO.getAllBranches();
					final List<Common> commonList = DataStatisticsController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataStatisticsController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = DataStatisticsController.this.remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = DataStatisticsController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = DataStatisticsController.this.reasonDao.getAllReason();
					DataStatisticsController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {
							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}
						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap, Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = DataStatisticsController.this.exportService
										.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList, cwbspayupidMap, complaintMap);
								if (cloumnName6[i].equals("double")) {
									cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
								} else {
									cell.setCellValue(a == null ? "" : a.toString());
								}
							}
						}

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								this.processRow(rs);
							}
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (this.recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : this.recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = this.getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = DataStatisticsController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp, complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : DataStatisticsController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : DataStatisticsController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : DataStatisticsController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
							return cwbspayupidMap;
						}
					});

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public boolean checkBranchRepeat(List<Branch> branchlist, Branch branch) {
		for (int i = 0; i < branchlist.size(); i++) {
			if (branch.getBranchname().equals(branchlist.get(i).getBranchname())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param cwbordertypeids
	 * @param beginupdatetime
	 * @param endupdatetime
	 * @param beginemaildate
	 * @param endemaildate
	 * @param page
	 * @return
	 */
	@RequestMapping("/obsoleteOrder/{page}")
	@DataSource(DatabaseType.REPLICA)
	public String queryObsoleteOrder(Model model, HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "cwb", required = false ) String cwb,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeids,
			@RequestParam(value = "beginupdatetime", required = false, defaultValue = "") String beginupdatetime,
			@RequestParam(value = "endupdatetime", required = false, defaultValue = "") String endupdatetime,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@PathVariable(value = "page") long page) {
		List<CwbOrder> result = null;
		long count = 0;
		Page pageparm = null;
		try {
			if(isshow!=0){
				//因为逻辑较简单，可以直接调用dao层
				count = this.cwbDAO.selectObsoleteCwbOrderListCount(cwb, StringUtils.join(cwbordertypeids,','), beginupdatetime, endupdatetime, 
						beginemaildate, endemaildate);
				result = this.cwbDAO.selectObsoleteCwbOrderList(cwb, StringUtils.join(cwbordertypeids,','), beginupdatetime, endupdatetime, 
						beginemaildate, endemaildate, page);
				Map<Long, Customer> customermap = this.customerDAO.getAllCustomersToMap();
				Map<Long, String> usermap = this.userDAO.getAllUserRealNameMap();
	
				pageparm = new Page(count, page, 10);
				model.addAttribute("cwb", cwb);
				model.addAttribute("cwbordertypeidStr", cwbordertypeids);
				model.addAttribute("beginupdatetime", beginupdatetime);
				model.addAttribute("endupdatetime", endupdatetime);
				model.addAttribute("beginemaildate", beginemaildate);
				model.addAttribute("endemaildate", endemaildate);
				model.addAttribute("customermap",customermap);
				model.addAttribute("usermap", usermap);
				model.addAttribute("pageparm", pageparm);
				model.addAttribute("result", result);
				model.addAttribute("count", count);
			}
		} catch (Exception e) {
			this.logger.error("queryObsoleteOrder failed ", e);
		}
		return "datastatistics/shixiao";
	}
	/**
	 * Excel导出
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param cwbordertypeids
	 * @param beginupdatetime
	 * @param endupdatetime
	 * @param beginemaildate
	 * @param endemaildate 
	 * @return
	 */	
	@RequestMapping("/obsoleteOrder/excel")
	@DataSource(DatabaseType.REPLICA)
	public String queryObsoleteOrder(Model model, HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "cwb", required = false ) String cwb,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeids,
			@RequestParam(value = "beginupdatetime", required = false, defaultValue = "") String beginupdatetime,
			@RequestParam(value = "endupdatetime", required = false, defaultValue = "") String endupdatetime,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate ) {
		
		long count = 0;
		Page pageparm = null;
		try {
			//因为逻辑较简单，可以直接调用dao层 
			count = this.cwbDAO.selectObsoleteCwbOrderListCount(cwb, StringUtils.join(cwbordertypeids,','), beginupdatetime, endupdatetime, 
					beginemaildate, endemaildate);
			final List<CwbOrder> result = this.cwbDAO.selectObsoleteCwbOrderList(cwb, StringUtils.join(cwbordertypeids,','), beginupdatetime, endupdatetime, 
					beginemaildate, endemaildate, 1, 65535); //Excel2003的最大行数
			String sheetName = "失效订单信息"; // sheet的名称
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String fileName = "ObsoleteOrder_" + df.format(new Date()) + ".xlsx"; // 文件名
			final String[] cloumnName = {"订单号", "运单号", "订单类型", "发货时间", "发货件数", "发货客户", "订单状态", "订单操作类型", "配送状态", "数据状态", "失效时间", "失效人" };
			final CustomerDAO _customerDAO = this.customerDAO;
			final UserDAO _userDAO = this.userDAO;
			ExcelUtils excelUtil = new ExcelUtils() {
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					Font font = sheet.getWorkbook().createFont();
					font.setFontName("宋体");
					font.setFontHeightInPoints((short) 10);
					style.setFont(font);
					//设置列的默认值
					for(int i = 0; i < cloumnName.length; i++) {
						sheet.setColumnWidth(i, 4000);
					}
					for (int i = 0; i < result.size(); i++) {
						CwbOrder vo = result.get(i);
						Row row = sheet.createRow(i + 1);
						short colIndex = 0;
						
						Cell cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue(vo.getCwb());
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue(vo.getTranscwb());
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue(OrderTypeEnum.getByValue(vo.getCwbordertypeid()));
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue(vo.getEmaildate());
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue(vo.getSendcarnum());
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style); 
						cell.setCellValue(_customerDAO.getCustomerById(vo.getCustomerid()).getCustomername());
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue(CwbStateEnum.getTextByValue((int)vo.getCwbstate()));
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue(FlowOrderTypeEnum.getByValue((int)vo.getFlowordertype()).getText());
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue(DeliveryStateEnum.getByValue(vo.getDeliverystate()).getText());
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue("失效");
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue(vo.getPrinttime());
						
						cell = row.createCell(colIndex++);
						cell.setCellStyle(style);
						cell.setCellValue(_userDAO.getUserByUserid(vo.getDeliverid()).getRealname());
						
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			this.logger.error("queryObsoleteOrder failed ", e);
		}
		return "datastatistics/shixiao";
	}

}
