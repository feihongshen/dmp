package cn.explink.controller.express;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.controller.CwbOrderView;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbKuaiDiDAO;
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
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbKuaiDi;
import cn.explink.domain.CwbKuaiDiView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.express.ExpressSettleWayEnum;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

@RequestMapping("/datastatistics4express")
@Controller
public class ExpressDataStatisticsController {

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

	@Autowired
	CwbKuaiDiDAO cwbKuaiDiDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/tuotousearch/{page}")
	public String tuotousearch(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isaudit", required = false, defaultValue = "-1") long isaudit,
			@RequestParam(value = "isauditTime", required = false, defaultValue = "0") long isauditTime, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "6") String[] cwbordertypeid,
			@RequestParam(value = "paytype", required = false, defaultValue = "-1") long paywayid,
			@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchid,
			@RequestParam(value = "deliverid", required = false, defaultValue = "-1") long deliverid,
			@RequestParam(value = "operationOrderResultType", required = false, defaultValue = "") String[] operationOrderResultTypes,
			@RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName,
			@RequestParam(value = "", required = false, defaultValue = "0") int firstlevelreasonid, @RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@RequestParam(value = "paybackfeeIsZero", required = false, defaultValue = "-1") Integer paybackfeeIsZero, @PathVariable(value = "page") long page, HttpServletResponse response,
			HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbOrder sum = new CwbOrder();
		List<CwbOrderView> cwbOrderView = new ArrayList<CwbOrderView>();
		List<User> deliverlist = new ArrayList<User>();
		// 滞留
		if (isshow != 0) {
			this.logger.info(
					"妥投订单汇总，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",isaudit:" + isaudit + ",isauditTime:" + isauditTime + ",customerid:"
							+ this.dataStatisticsService.getStrings(customerid) + ",paywayid:" + paywayid + ",cwbordertypeid:" + this.dataStatisticsService.getStrings(cwbordertypeid)
							+ ",dispatchbranchid:" + this.dataStatisticsService.getStrings(dispatchbranchid) + ",deliverid:" + deliverid + ",operationOrderResultTypes:"
							+ this.dataStatisticsService.getStrings(operationOrderResultTypes) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:" + page,
					this.getSessionUser().getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;

			Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
			List<Branch> branchnameList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue()
					+ "," + BranchEnum.ZhongZhuan.getValue());

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
				operationOrderResultTypes = new String[] { DeliveryStateEnum.PeiSongChengGong.getValue() + "", DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "",
						DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "" };
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
			List<String> orderFlowLastList = this.deliveryStateDAO.getDeliveryStateByCredateAndFlowordertype(begindate, enddate, isauditTime, isaudit, operationOrderResultTypes, dispatchbranchid,
					deliverid, 1, customerids, firstlevelreasonid);
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

	@RequestMapping("/query")
	public String querypage(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "0") String[] customeridStr,
			@RequestParam(value = "currentBranchid", required = false, defaultValue = "0") String[] currentBranchid,
			@RequestParam(value = "orderResultType", required = false, defaultValue = "") String[] orderResultTypes,
			@RequestParam(value = "operationOrderResultTypes", required = false, defaultValue = "") String[] operationOrderResultTypes,
			@RequestParam(value = "datatype", required = false, defaultValue = "0") long datatype,
			@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchidStr,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeidStr,
			@RequestParam(value = "kufangid", required = false, defaultValue = "") String[] kufangidStr,
			@RequestParam(value = "nextbranchid", required = false, defaultValue = "") String[] nextbranchidStr,
			@RequestParam(value = "startbranchid", required = false, defaultValue = "") String[] startbranchidStr) {

		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		List<Branch> branchnameList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));// bug546licx
		List<Branch> kufangList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + ","
				+ BranchEnum.ZhongZhuan.getValue());

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

		model.addAttribute("notuihuobranchList",
				this.branchDAO.getBanchByBranchidForStock(BranchEnum.KuFang.getValue() + "," + BranchEnum.ZhanDian.getValue() + "," + BranchEnum.ZhongZhuan.getValue()));
		model.addAttribute("loginUserType", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getSitetype());// 存储当前登录用户所在机构类型
		if (!model.containsAttribute("page_obj")) {
			model.addAttribute("page_obj", new Page());
		}
		return "express/dataSummary/tuotoulist";
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page,
			@RequestParam(value = "sign", required = false, defaultValue = "0") long sign) {
		this.dataStatisticsService.DataStatisticsExportExcelMethod(response, request, page, sign);
	}

	/**
	 * 快递订单查询
	 *
	 * @param model
	 * @param page
	 * @param timeType
	 * @param begindate
	 * @param enddate
	 * @param lanshoubranchids
	 * @param lanshouuserid
	 * @param paisongbranchids
	 * @param paisonguserid
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/kuaidilist/{page}")
	public String kuaidilist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "timeType", required = false, defaultValue = "1") long timeType,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "lanshoubranchid", required = false, defaultValue = "") String[] lanshoubranchids,
			@RequestParam(value = "lanshouuserid", required = false, defaultValue = "0") long lanshouuserid,
			@RequestParam(value = "paisongbranchid", required = false, defaultValue = "") String[] paisongbranchids,
			@RequestParam(value = "paisonguserid", required = false, defaultValue = "0") long paisonguserid, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			HttpServletResponse response, HttpServletRequest request) {
		Page pageparm = new Page();
		List<CwbKuaiDi> cwbKuaiDilist = new ArrayList<CwbKuaiDi>();
		List<CwbKuaiDiView> cwbViewList = new ArrayList<CwbKuaiDiView>();
		// 保存揽收站点的选择
		List<String> lanshoubranchidList = new ArrayList<String>();
		// 保存配送站点的选择
		List<String> paisongbranchidList = new ArrayList<String>();

		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();

		List<Branch> branchList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
		List<User> lanshouuserlist = new ArrayList<User>();
		List<User> paisonguserlist = new ArrayList<User>();

		if (isshow != 0) {
			lanshoubranchidList = this.dataStatisticsService.getList(lanshoubranchids);
			paisongbranchidList = this.dataStatisticsService.getList(paisongbranchids);

			String lanshoubranchidStr = this.dataStatisticsService.getStrings(lanshoubranchids);
			String paisongbranchidStr = this.dataStatisticsService.getStrings(paisongbranchids);

			cwbKuaiDilist = this.cwbKuaiDiDAO.getCwbKuaiDiListPage(page, timeType, begindate, enddate, lanshoubranchidStr, lanshouuserid, paisongbranchidStr, paisonguserid);
			pageparm = new Page(this.cwbKuaiDiDAO.getCwbKuaiDiListCount(timeType, begindate, enddate, lanshoubranchidStr, lanshouuserid, paisongbranchidStr, paisonguserid), page, Page.ONE_PAGE_NUMBER);

			if ((cwbKuaiDilist != null) && (cwbKuaiDilist.size() > 0)) {
				String cwbs = "";
				for (CwbKuaiDi cwbKuaiDi : cwbKuaiDilist) {
					cwbs += "'" + cwbKuaiDi.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					List<Branch> branchAllList = this.branchDAO.getAllBranches();
					List<User> userList = this.userDAO.getAllUser();
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					cwbViewList = this.getCwbKuaiDiViewCount10(orderlist, cwbKuaiDilist, branchAllList, userList);
				}
			}
			String lanshouusers = this.dataStatisticsService.getStrings(lanshoubranchids);
			if (lanshouusers.length() > 0) {
				lanshouuserlist = this.userDAO.getAllUserByBranchIds(lanshouusers);
			}
			String paisongs = this.dataStatisticsService.getStrings(paisongbranchids);
			if (paisongs.length() > 0) {
				paisonguserlist = this.userDAO.getAllUserByBranchIds(paisongs);
			}
		}
		model.addAttribute("cwbViewList", cwbViewList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("lanshoubranchidList", lanshoubranchidList);
		model.addAttribute("paisongbranchidList", paisongbranchidList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("check", 1);
		model.addAttribute("lanshouuserlist", lanshouuserlist);
		model.addAttribute("paisonguserlist", paisonguserlist);

		return "express/datasummary/kuaidilist";
	}

	public List<CwbKuaiDiView> getCwbKuaiDiViewCount10(List<CwbOrder> orderlist, List<CwbKuaiDi> cwbKuaiDilist, List<Branch> branchList, List<User> userList) {
		List<CwbKuaiDiView> cwbOrderViewList = new ArrayList<CwbKuaiDiView>();
		if ((cwbKuaiDilist.size() > 0) && (orderlist.size() > 0)) {
			for (CwbKuaiDi ck : cwbKuaiDilist) {
				for (CwbOrder c : orderlist) {
					if (ck.getCwb().equals(c.getCwb())) {
						CwbKuaiDiView cwbKuaiDiView = new CwbKuaiDiView();

						cwbKuaiDiView.setCwb(c.getCwb());
						cwbKuaiDiView.setLanshouusername(this.dataStatisticsService.getQueryUserName(userList, ck.getLanshouuserid()));
						cwbKuaiDiView.setLanshoubranchname(this.dataStatisticsService.getQueryBranchName(branchList, ck.getLanshoubranchid()));
						cwbKuaiDiView.setLanshoutime(ck.getLanshoutime());
						cwbKuaiDiView.setConsigneename(c.getConsigneename());
						cwbKuaiDiView.setConsigneemobile(c.getConsigneemobile());
						cwbKuaiDiView.setConsigneeaddress(c.getConsigneeaddress());
						cwbKuaiDiView.setAllfee(ck.getAllfee());
						cwbKuaiDiView.setFlowordertype(c.getFlowordertype());
						cwbKuaiDiView.setRemark(ck.getRemark());

						cwbKuaiDiView.setShouldfare(c.getShouldfare());
						cwbKuaiDiView.setRealweight(new BigDecimal(c.getRealweight()));
						cwbKuaiDiView.setPaymethod(ExpressSettleWayEnum.getByValue(c.getPaymethod()).getText());
						cwbKuaiDiView.setCustomername(c.getSendername());
						cwbKuaiDiView.setCustomercode(c.getSendercustomcode());

						cwbOrderViewList.add(cwbKuaiDiView);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	@RequestMapping("/exportExcle4express")
	@DataSource(DatabaseType.REPLICA)
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {
		String[] cloumnName1 = new String[17]; // 导出的列名
		String[] cloumnName2 = new String[17]; // 导出的英文列名

		this.exportService.SetCwbKuaiDiFields1(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "快递单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "CwbKuaiDi_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			long timeType = request.getParameter("timeType1") == null ? -1 : Long.parseLong(request.getParameter("timeType1").toString());
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] lanshoubranchids = request.getParameterValues("lanshoubranchid1") == null ? new String[] {} : request.getParameterValues("lanshoubranchid1");
			String[] paisongbranchids = request.getParameterValues("paisongbranchid1") == null ? new String[] {} : request.getParameterValues("paisongbranchid1");
			long lanshouuserid = request.getParameter("lanshouuserid1") == null ? -1 : Long.parseLong(request.getParameter("lanshouuserid1").toString());
			long paisonguserid = request.getParameter("timeType1") == null ? -1 : Long.parseLong(request.getParameter("paisonguserid1").toString());

			List<CwbKuaiDi> cwbKuaiDilist = this.cwbKuaiDiDAO.getCwbKuaiDiListNoPage(timeType, begindate, enddate, this.dataStatisticsService.getStrings(lanshoubranchids), lanshouuserid,
					this.dataStatisticsService.getStrings(paisongbranchids), paisonguserid);

			List<CwbKuaiDiView> cwbViewList = new ArrayList<CwbKuaiDiView>();
			if ((cwbKuaiDilist != null) && (cwbKuaiDilist.size() > 0)) {
				String cwbs = "";
				for (CwbKuaiDi cwbKuaiDi : cwbKuaiDilist) {
					cwbs += "'" + cwbKuaiDi.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					List<Branch> branchAllList = this.branchDAO.getAllBranches();
					List<User> userList = this.userDAO.getAllUser();
					List<CwbOrder> orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					cwbViewList = this.getCwbKuaiDiViewCount10(orderlist, cwbKuaiDilist, branchAllList, userList);
				}
			}

			final List<CwbKuaiDiView> cwbOrderViewList = cwbViewList;
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < cwbOrderViewList.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = ExpressDataStatisticsController.this.exportService.setCwbKuaiDiObject(cloumnName3, request1, cwbOrderViewList, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
