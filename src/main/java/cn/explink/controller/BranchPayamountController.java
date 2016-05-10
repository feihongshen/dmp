package cn.explink.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import org.springframework.web.servlet.ModelAndView;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.FinanceAuditDAO;
import cn.explink.dao.FinancePayUpAuditDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PayUpDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.FinanceAudit;
import cn.explink.domain.FinancePayUpAudit;
import cn.explink.domain.NewForExportJson;
import cn.explink.domain.PayUp;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FinanceAuditTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.FundsEnum;
import cn.explink.exception.ExplinkException;
import cn.explink.service.AdvancedQueryService;
import cn.explink.service.BranchPayamountService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

@RequestMapping("/funds")
@Controller
public class BranchPayamountController {
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	BranchDAO branchDao;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	PayUpDAO payUpDAO;
	@Autowired
	BranchPayamountService branchPayamountService;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliverStateDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	AdvancedQueryService advancedQueryService;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	FinanceAuditDAO financeAuditDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	FinancePayUpAuditDAO financePayUpAuditDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger logger = LoggerFactory.getLogger(BranchPayamountController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	// //===========货款管理===================、、、、

	// 按配送结果结算
	@RequestMapping("/paymentfordeliverystate/{page}")
	public String paymentfordeliverystate(@PathVariable("page") long page, Model model, @RequestParam(value = "dateType", required = false, defaultValue = "0") int dateType,
			@RequestParam(value = "emailStartTime", required = false, defaultValue = "") String emailStartTime,
			@RequestParam(value = "eamilEndTime", required = false, defaultValue = "") String eamilEndTime,
			@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "exportType", required = false, defaultValue = "0") long exportType,
			@RequestParam(value = "deliverystate", required = false, defaultValue = "0") long deliverystate, @RequestParam(value = "isaudit", required = false, defaultValue = "-1") long isaudit,
			@RequestParam(value = "customerwarehouseid", required = false, defaultValue = "-1") long customerwarehouseid,
			@RequestParam(value = "auditState", required = false, defaultValue = "-1") long auditState, HttpServletResponse response, HttpServletRequest request) {

		if (!"1".equals(request.getParameter("isshow"))) {// 搜索的条件
			emailStartTime = (emailStartTime == null || emailStartTime.equals("")) ? (DateDayUtil.getDateBefore("", -6) + " 00:00:00") : emailStartTime;
			eamilEndTime = (eamilEndTime == null || eamilEndTime.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 23:59:59") : eamilEndTime;
		}

		List<Customer> cumstrListAll = customerDao.getCustomerByPaytype(2);
		Map<Long, String> cumstrMap = new HashMap<Long, String>();
		for (Customer customer : cumstrListAll) {
			cumstrMap.put(customer.getCustomerid(), customer.getCustomername());
		}

		// List<JSONObject> peisongchengList =
		// deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredateAll(
		// DeliveryStateEnum.PeiSongChengGong.getValue()+","+DeliveryStateEnum.ShangMenHuanChengGong.getValue(),
		// emailStartTime, eamilEndTime);
		//
		List<JSONObject> daohuojsonList = new ArrayList<JSONObject>();
		JSONObject daohuolist = new JSONObject();
		long count = 0;
		String sig = "'";
		String sig1 = "',";
		StringBuffer cwbs = new StringBuffer();
		if (customerid.length > 0) {
			String paymentfordeliverystate_diushi = systemInstallDAO.getSystemInstall("paymentfordeliverystate_diushi") == null ? "yes" : systemInstallDAO.getSystemInstall(
					"paymentfordeliverystate_diushi").getValue();
			daohuojsonList = cwbDAO.getListByCustomeridAndDeliverystateAndCredate(page, emailStartTime, eamilEndTime, getStrings(customerid), deliverystate, isaudit, customerwarehouseid, auditState,
					paymentfordeliverystate_diushi, dateType);
			daohuolist = cwbDAO.getListByCustomeridAndDeliverystateAndCredateNopage(emailStartTime, eamilEndTime, getStrings(customerid), deliverystate, isaudit, customerwarehouseid, auditState,
					paymentfordeliverystate_diushi, dateType);

			count = cwbDAO.getListByCustomeridAndDeliverystateAndCredateCount(emailStartTime, eamilEndTime, getStrings(customerid), deliverystate, isaudit, customerwarehouseid, auditState,
					paymentfordeliverystate_diushi, dateType);

			List<String> cwbList = cwbDAO.getCwbsListByCustomeridAndDeliverystateAndCredate(emailStartTime, eamilEndTime, getStrings(customerid), deliverystate, isaudit, customerwarehouseid,
					auditState, paymentfordeliverystate_diushi, dateType);
			for (String cwb : cwbList) {
				cwbs = cwbs.append(sig).append(cwb).append(sig1);
			}
		}

		model.addAttribute("cwbs", cwbs);
		model.addAttribute("dateType", dateType);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		setSetionsExpS(request, emailStartTime, eamilEndTime, 0, exportType, isaudit, daohuolist.isEmpty() ? 0 : daohuolist.getInt("cwbcount"));

		model.addAttribute("daohuolist", daohuolist);
		model.addAttribute("customerid", customerid);
		request.getSession().setAttribute("deliverystate", deliverystate);

		model.addAttribute("cumstrListAll", cumstrListAll);
		model.addAttribute("branchList", branchDao.getAllEffectBranches());

		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);

		model.addAttribute("daohuojsonList", daohuojsonList);
		// model.addAttribute("peisongchengList", peisongchengList);
		model.addAttribute("cumstrMap", cumstrMap);

		List<String> customeridList = this.getList(customerid);
		model.addAttribute("customeridStr", customeridList);

		return "funds/payment/paymentMonitor";
	}

	// 按配送结果结算导出功能
	@RequestMapping("/deliverystatesearchdetail_excel")
	public void deliverystatesearchdetail_excel(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page,
			@RequestParam(value = "emailStartTime1", required = false, defaultValue = "") String emailStartTime,
			@RequestParam(value = "eamilEndTime1", required = false, defaultValue = "") String eamilEndTime,
			@RequestParam(value = "customerid1", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "deliverystate1", required = false, defaultValue = "0") long deliverystate, @RequestParam(value = "isaudit1", required = false, defaultValue = "-1") long isaudit,
			@RequestParam(value = "customerwarehouseid1", required = false, defaultValue = "-1") long customerwarehouseid,
			@RequestParam(value = "dateType1", required = false, defaultValue = "0") int dateType, @RequestParam(value = "auditState1", required = false, defaultValue = "-1") long auditState) {

		String paymentfordeliverystate_diushi = systemInstallDAO.getSystemInstall("paymentfordeliverystate_diushi") == null ? "yes" : systemInstallDAO.getSystemInstall(
				"paymentfordeliverystate_diushi").getValue();
		List<String> cwbStrList = cwbDAO.getListByCustomeridAndDeliverystateAndCredateNoPage(page, emailStartTime, eamilEndTime, getStrings(customerid), deliverystate, isaudit, customerwarehouseid,
				auditState, paymentfordeliverystate_diushi, dateType);
		try {
			Branch nowbranch = branchDao.getBranchByBranchid(getSessionUser().getBranchid());
			logger.info("货款大数据导出数据：用户名：{},站点：{} 导出数据条数" + cwbStrList.size() + "", getSessionUser().getRealname(), nowbranch.getBranchname());
		} catch (Exception e) {
			logger.error("货款大数据导出数据：获取用户名，站点异常");
		}
		advancedQueryService.batchSelectExport(response, request, cwbStrList);
	}

	/**
	 * 按发货时间结算
	 * 
	 * @param page
	 *            分页
	 * @param model
	 * @param emaildateid
	 *            批次id
	 * @param rukuStartTime
	 *            入库开始时间
	 * @param rukuEndTime
	 *            入库结束时间
	 * @param customerid
	 *            供货商
	 * @param customerwarehouseid
	 *            供货商发货仓库
	 * @param exportType
	 *            导出类型
	 * @param audittype
	 *            财务人员审核状态
	 * @param cwbOrderType
	 *            订单类型 配送 上门退 上门换
	 * @return
	 */
	@RequestMapping("/paymentMonitorbyemailtime/{page}")
	public String paymentMonitorbyemailtime(@PathVariable("page") long page, Model model, @RequestParam(value = "emaildateid", required = false, defaultValue = "") String[] emaildateid,
			@RequestParam(value = "rukuStartTime", required = false, defaultValue = "") String rukuStartTime,
			@RequestParam(value = "rukuEndTime", required = false, defaultValue = "") String rukuEndTime, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "customerwarehouseid", required = false, defaultValue = "-1") long customerwarehouseid,
			@RequestParam(value = "exportType", required = false, defaultValue = "0") long exportType, @RequestParam(value = "auditState", required = false, defaultValue = "-1") long auditState,
			@RequestParam(value = "cwbOrderType", required = false, defaultValue = "-1") int cwbOrderType, HttpServletRequest request) {

		List<Customer> cumstrListAll = customerDao.getCustomerByPaytype(1);
		Map<Long, String> cumstrMap = new HashMap<Long, String>();
		for (Customer customer : cumstrListAll) {
			cumstrMap.put(customer.getCustomerid(), customer.getCustomername());
		}

		List<CwbOrder> daohuojsonList = new ArrayList<CwbOrder>();
		List<CwbOrder> daohuoList = new ArrayList<CwbOrder>();
		BigDecimal countfee = BigDecimal.ZERO;
		long count = 0;
		StringBuffer cwbs = new StringBuffer();
		if (customerid.length > 0) { // 获取对应条件的订单数据，
			daohuoList = cwbDAO.getListByEmaildateId(getStrings(emaildateid), getStrings(customerid), customerwarehouseid, auditState, cwbOrderType);
			// 处理入库时间条件筛选 start

			String sig = "'";
			String sig1 = "',";
			for (CwbOrder co : daohuoList) {
				cwbs = cwbs.append(sig).append(co.getCwb()).append(sig1);
			}
			Date rukuStart = null;// 获取要比较的入库时间
			Date rukuEnd = null;
			try {
				rukuStart = df.parse(rukuStartTime);
				rukuEnd = df.parse(rukuEndTime);
				List<OrderFlow> ofList_temp = orderFlowDAO.getCwbsByFlowordertype(FlowOrderTypeEnum.RuKu, cwbs.substring(0, cwbs.length() - 1));
				if (ofList_temp.size() > 0) {
					// List<OrderFlow> ofList = new ArrayList<OrderFlow>();
					StringBuffer cwbTemp = new StringBuffer();
					cwbs = new StringBuffer();
					for (OrderFlow of : ofList_temp) {// 第一次循环，过滤获取入库时间符合条件的数据
						if (cwbTemp.indexOf(of.getCwb() + sig) == -1) {
							cwbTemp = cwbTemp.append(of.getCwb()).append(sig);
							if (of.getCredate().getTime() > rukuStart.getTime() && of.getCredate().getTime() < rukuEnd.getTime()) {
								cwbs.append(sig).append(of.getCwb()).append(sig1);
							}
						}
					}
					if (cwbs.length() == 0) {// 如果根据入库时间没有筛选到结果的话，标识没有符合条件的记录
						daohuoList = new ArrayList<CwbOrder>();
					} else {
						daohuoList = cwbDAO.getCwbOrderByCwbs(cwbs.substring(0, cwbs.length() - 1));// 得到最终的数据
					}
				} else {
					daohuoList = new ArrayList<CwbOrder>();
				}
				// 处理入库时间条件筛选 end
			} catch (Exception e) {
			}

			for (int i = (int) ((page - 1) * Page.ONE_PAGE_NUMBER); i < (page * Page.ONE_PAGE_NUMBER) && i < daohuoList.size(); i++) {
				daohuojsonList.add(daohuoList.get(i));
			}
			count = daohuoList.size();
		}

		for (CwbOrder co : daohuoList) {
			countfee = countfee.add(co.getReceivablefee()).subtract(co.getPaybackfee());
		}
		if (customerid.length > 0) {
			model.addAttribute("emaildateList", emailDateDAO.getEmailDateByCustomerids(getStrings(customerid)));
		}
		model.addAttribute("countfee", countfee);
		model.addAttribute("all", daohuoList.size());
		model.addAttribute("cumstrListAll", cumstrListAll);
		model.addAttribute("branchList", branchDao.getAllEffectBranches());
		model.addAttribute("customWareHose", customWareHouseDAO.getAllCustomWareHouse());
		model.addAttribute("rukuStartTime", rukuStartTime);
		model.addAttribute("rukuEndTime", rukuEndTime);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("daohuojsonList", daohuojsonList);
		model.addAttribute("cumstrMap", cumstrMap);
		List<String> customeridList = this.getList(customerid);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("emaildateidStr", this.getList(emaildateid));
		model.addAttribute("cwbs", cwbs.toString());
		return "funds/payment/paymentMonitorbyemailtime";
	}

	@RequestMapping("/newsearchdetail_excel")
	public void newpaymentDetail_excel(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page,
			@RequestParam(value = "emaildateid1", required = false, defaultValue = "") String[] emaildateid,
			@RequestParam(value = "rukuStartTime1", required = false, defaultValue = "") String rukuStartTime,
			@RequestParam(value = "rukuEndTime1", required = false, defaultValue = "") String rukuEndTime,
			@RequestParam(value = "customerid1", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "customerwarehouseid1", required = false, defaultValue = "-1") long customerwarehouseid,
			@RequestParam(value = "exportType1", required = false, defaultValue = "0") long exportType, @RequestParam(value = "auditState1", required = false, defaultValue = "-1") long auditState,
			@RequestParam(value = "cwbOrderType1", required = false, defaultValue = "-1") int cwbOrderType) {
		List<CwbOrder> daohuoList = new ArrayList<CwbOrder>();
		List<String> cwbList = new ArrayList<String>();
		if (customerid.length > 0) { // 获取对应条件的订单数据，
			daohuoList = cwbDAO.getListByEmaildateId(getStrings(emaildateid), getStrings(customerid), customerwarehouseid, auditState, cwbOrderType);
			// 处理入库时间条件筛选 start
			StringBuffer cwbs = new StringBuffer();
			String sig = "'";
			String sig1 = "',";
			for (CwbOrder co : daohuoList) {
				cwbs = cwbs.append(sig).append(co.getCwb()).append(sig1);
			}
			Date rukuStart = null;// 获取要比较的入库时间
			Date rukuEnd = null;
			try {
				rukuStart = df.parse(rukuStartTime);
				rukuEnd = df.parse(rukuEndTime);
				List<OrderFlow> ofList_temp = orderFlowDAO.getCwbsByFlowordertype(FlowOrderTypeEnum.RuKu, cwbs.substring(0, cwbs.length() - 1));
				if (ofList_temp.size() > 0) {
					// List<OrderFlow> ofList = new ArrayList<OrderFlow>();
					StringBuffer cwbTemp = new StringBuffer();
					cwbs = new StringBuffer();
					for (OrderFlow of : ofList_temp) {// 第一次循环，过滤获取入库时间符合条件的数据
						if (cwbTemp.indexOf(of.getCwb() + sig) == -1) {
							cwbTemp = cwbTemp.append(of.getCwb()).append(sig);
							if (of.getCredate().getTime() > rukuStart.getTime() && of.getCredate().getTime() < rukuEnd.getTime()) {
								cwbs.append(sig).append(of.getCwb()).append(sig1);
							}
						}
					}
					daohuoList = cwbDAO.getCwbOrderByCwbs(cwbs.substring(0, cwbs.length() - 1));// 得到最终的数据
				} else {
					daohuoList = new ArrayList<CwbOrder>();
				}
				// 处理入库时间条件筛选 end
			} catch (Exception e) {
			}

			for (int i = (int) page; i < (page + Page.EXCEL_PAGE_NUMBER) && i < daohuoList.size(); i++) {
				cwbList.add(daohuoList.get(i).getCwb());
			}
		}

		try {
			Branch nowbranch = branchDao.getBranchByBranchid(getSessionUser().getBranchid());
			logger.info("货款大数据导出数据：用户名：{},站点：{} 导出数据条数" + cwbList.size() + "", getSessionUser().getRealname(), nowbranch.getBranchname());
		} catch (Exception e) {
			logger.error("货款大数据导出数据：获取用户名，站点异常");
		}
		advancedQueryService.batchSelectExport(response, request, cwbList);

	}

	@RequestMapping("/financeauditconfirm")
	public String financeAuditConfirm(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "auditCustomerid", required = false, defaultValue = "0") long auditCustomerid,
			@RequestParam(value = "auditCustomerwarehouseid", required = false, defaultValue = "") long auditCustomerwarehouseid,
			@RequestParam(value = "auditCwbOrderType", required = false, defaultValue = "1") int auditCwbOrderType) {

		cwbs = cwbs.substring(0, cwbs.length() - 1);
		List<CwbOrder> coList = cwbDAO.getCwbOrderByCwbs(cwbs);// 得到最终的数据
		BigDecimal countfee = BigDecimal.ZERO;
		for (CwbOrder co : coList) {
			countfee = countfee.add(co.getReceivablefee()).subtract(co.getPaybackfee());
		}

		model.addAttribute("customer", customerDao.getCustomerById(auditCustomerid));
		model.addAttribute("customerwarehouse", customWareHouseDAO.getWarehouseId(auditCustomerwarehouseid));
		model.addAttribute("cwbOrderType", CwbOrderTypeIdEnum.getByValue(auditCwbOrderType));
		model.addAttribute("coList", coList);
		model.addAttribute("countfee", countfee);
		return "funds/payment/financeAuditConfirm";
	}

	@RequestMapping("/financeauditBackconfirm")
	public String financeauditBackconfirm(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "auditCustomerid", required = false, defaultValue = "0") long auditCustomerid,
			@RequestParam(value = "auditCustomerwarehouseid", required = false, defaultValue = "-1") long auditCustomerwarehouseid,
			@RequestParam(value = "isout", required = false, defaultValue = "1") int isout) {

		List<JSONObject> coList = cwbDAO.getListByCustomeridAndDeliverystateAndCredateByBack(-1, cwbs, auditCustomerid + "", isout, auditCustomerwarehouseid, 0);// 得到最终的数据

		JSONObject feeJson = cwbDAO.getListByCustomeridAndDeliverystateAndCredateNopageByBack(cwbs, auditCustomerid + "", isout, auditCustomerwarehouseid, 0);

		model.addAttribute("customer", customerDao.getCustomerById(auditCustomerid));
		model.addAttribute("customerwarehouse", customWareHouseDAO.getWarehouseId(auditCustomerwarehouseid));
		model.addAttribute("isout", isout);
		model.addAttribute("feeJson", feeJson);
		model.addAttribute("coList", coList);
		StringBuffer cwbss = new StringBuffer();
		if (coList != null && coList.size() > 0) {
			for (JSONObject json : coList) {
				cwbss.append("'" + json.getString("cwb") + "',");
			}
		}
		if (isout == 1) {
			model.addAttribute("type", FinanceAuditTypeEnum.TuiHuoKuanJieSuan_RuZhang.getValue());
		} else {
			model.addAttribute("type", FinanceAuditTypeEnum.TuiHuoKuanJieSuan_ChuZhang.getValue());
		}
		model.addAttribute("cwbs", cwbss);

		return "funds/payment/financeAuditBackConfirm";
	}

	/**
	 * 财务审核结算提交
	 * 
	 * @param cwbs
	 * @param auditCustomerid
	 *            审核供货商id
	 * @param auditCustomerwarehouseid
	 *            审核供货商发货仓库id
	 * @param auditCwbOrderType
	 *            审核的订单类型
	 * @param type
	 *            审核类型 对应FinanceAuditTypeEnum
	 * @param auditdatetime
	 *            审核时间
	 * @param paydatetime
	 *            付款时间
	 * @param paytype
	 *            付款方式 1现金 2 POS
	 * @param payamount
	 *            付款金额
	 * @param shouldpayamount
	 *            应付金额
	 * @param paynumber
	 *            付款流水号
	 * @param payremark
	 *            付款备注
	 * @return
	 */
	@RequestMapping("/financeaudit")
	public String financeAudit(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "auditCustomerid", required = false, defaultValue = "0") long auditCustomerid,
			@RequestParam(value = "auditCustomerwarehouseid", required = false, defaultValue = "-1") long auditCustomerwarehouseid,
			@RequestParam(value = "auditCwbOrderType", required = false, defaultValue = "1") int auditCwbOrderType, @RequestParam(value = "type") int type,
			@RequestParam(value = "auditdatetime") String auditdatetime, @RequestParam(value = "paydatetime", required = false, defaultValue = "") String paydatetime,
			@RequestParam(value = "paytype", required = false, defaultValue = "1") int paytype, @RequestParam(value = "payamount", required = false, defaultValue = "0") BigDecimal payamount,
			@RequestParam(value = "shouldpayamount", required = false, defaultValue = "0") BigDecimal shouldpayamount,
			@RequestParam(value = "paynumber", required = false, defaultValue = "") String paynumber, @RequestParam(value = "payremark", required = false, defaultValue = "") String payremark

	) {
		try {
			FinanceAudit fa = branchPayamountService.saveFinanceAuditDetail(cwbs, auditCustomerid, auditCustomerwarehouseid, auditCwbOrderType, type, auditdatetime, paydatetime, paytype, payamount,
					shouldpayamount, paynumber, payremark);
			logger.info("财务结算交款：{}", fa.toString());
			model.addAttribute("fa", fa);
		} catch (ExplinkException e) {
			model.addAttribute("error", e.getMessage());
		}

		model.addAttribute("customer", customerDao.getCustomerById(auditCustomerid));
		model.addAttribute("customerwarehouse", customWareHouseDAO.getWarehouseId(auditCustomerwarehouseid));
		model.addAttribute("cwbOrderType", CwbOrderTypeIdEnum.getByValue(auditCwbOrderType));
		model.addAttribute("type", type);

		return "funds/payment/financeAudit";
	}

	/**
	 * 财务审核结算提交
	 * 
	 * @param cwbs
	 * @param auditCustomerid
	 *            审核供货商id
	 * @param auditCustomerwarehouseid
	 *            审核供货商发货仓库id
	 * @param auditCwbOrderType
	 *            审核的订单类型
	 * @param type
	 *            审核类型 对应FinanceAuditTypeEnum
	 * @param auditdatetime
	 *            审核时间
	 * @param paydatetime
	 *            付款时间
	 * @param paytype
	 *            付款方式 1现金 2 POS
	 * @param payamount
	 *            付款金额
	 * @param shouldpayamount
	 *            应付金额
	 * @param paynumber
	 *            付款流水号
	 * @param payremark
	 *            付款备注
	 * @return
	 */
	@RequestMapping("/financeauditBack")
	public String financeauditBack(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "auditCustomerid", required = false, defaultValue = "0") long auditCustomerid,
			@RequestParam(value = "auditCustomerwarehouseid", required = false, defaultValue = "-1") long auditCustomerwarehouseid,
			@RequestParam(value = "auditCwbOrderType", required = false, defaultValue = "1") int auditCwbOrderType, @RequestParam(value = "type") int type,
			@RequestParam(value = "auditdatetime") String auditdatetime, @RequestParam(value = "paydatetime", required = false, defaultValue = "") String paydatetime,
			@RequestParam(value = "paytype", required = false, defaultValue = "1") int paytype, @RequestParam(value = "payamount", required = false, defaultValue = "0") BigDecimal payamount,
			@RequestParam(value = "shouldpayamount", required = false, defaultValue = "0") BigDecimal shouldpayamount,
			@RequestParam(value = "paynumber", required = false, defaultValue = "") String paynumber, @RequestParam(value = "payremark", required = false, defaultValue = "") String payremark

	) {
		try {
			FinanceAudit fa = branchPayamountService.saveFinanceAuditBackDetail(cwbs, auditCustomerid, auditCustomerwarehouseid, auditCwbOrderType, type, auditdatetime, paydatetime, paytype,
					payamount, shouldpayamount, paynumber, payremark);
			model.addAttribute("fa", fa);
		} catch (ExplinkException e) {
			model.addAttribute("error", e.getMessage());
		}

		model.addAttribute("customer", customerDao.getCustomerById(auditCustomerid));
		model.addAttribute("customerwarehouse", customWareHouseDAO.getWarehouseId(auditCustomerwarehouseid));
		model.addAttribute("cwbOrderType", CwbOrderTypeIdEnum.getByValue(auditCwbOrderType));
		model.addAttribute("type", type);
		return "funds/payment/financeAudit";
	}

	@RequestMapping("/paymentExp/{page}")
	public String paymentExp(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {

		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		long customerid = new Long(request.getSession().getAttribute("customerid").toString());

		List<Customer> cumstrListAll = customerDao.getAllCustomers();
		Map<Long, String> cumstrMap = new HashMap<Long, String>();
		for (Customer customer : cumstrListAll) {
			cumstrMap.put(customer.getCustomerid(), customer.getCustomername());
		}
		Map<Integer, Map<Integer, List<JSONObject>>> map = new HashMap<Integer, Map<Integer, List<JSONObject>>>();
		if (customerid == -1) {
			map = branchPayamountService.getPayamountMap(cumstrListAll, FundsEnum.PeiSongChengGong.getValue(), emailStartTime, eamilEndTime);
		} else {
			List<Customer> cumstrList = customerDao.getCustomerByIds(customerid + "");
			map = branchPayamountService.getPayamountMap(cumstrList, FundsEnum.PeiSongChengGong.getValue(), emailStartTime, eamilEndTime);
		}
		model.addAttribute("customerid", customerid);
		model.addAttribute("cumstrListAll", cumstrListAll);

		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);

		model.addAttribute("dataMap", map);
		model.addAttribute("cumstrMap", cumstrMap);

		return "funds/payment/paymentExpo";
	}

	// //===========退货款管理===================、、、、
	@RequestMapping("/paymentBack/{page}")
	public String paymentBack(@PathVariable("page") long page, Model model, @RequestParam(value = "emailStartTime", required = false, defaultValue = "") String emailStartTime,
			@RequestParam(value = "eamilEndTime", required = false, defaultValue = "") String eamilEndTime,
			@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "isout", required = false, defaultValue = "0") long isout,
			@RequestParam(value = "customerwarehouseid", required = false, defaultValue = "-1") long customerwarehouseid,
			@RequestParam(value = "auditState", required = false, defaultValue = "-1") long auditState, HttpServletResponse response, HttpServletRequest request) {

		if (!"1".equals(request.getParameter("isshow"))) {// 搜索的条件
			emailStartTime = (emailStartTime == null || emailStartTime.equals("")) ? (DateDayUtil.getDateBefore("", -6) + " 00:00:00") : emailStartTime;
			eamilEndTime = (eamilEndTime == null || eamilEndTime.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 23:59:59") : eamilEndTime;
		}

		List<Customer> cumstrListAll = customerDao.getAllCustomers();
		Map<Long, String> cumstrMap = new HashMap<Long, String>();
		for (Customer customer : cumstrListAll) {
			cumstrMap.put(customer.getCustomerid(), customer.getCustomername());
		}

		// List<JSONObject> peisongchengList =
		// deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredateAll(
		// DeliveryStateEnum.PeiSongChengGong.getValue()+","+DeliveryStateEnum.ShangMenHuanChengGong.getValue(),
		// emailStartTime, eamilEndTime);
		//
		List<JSONObject> daohuojsonList = new ArrayList<JSONObject>();
		JSONObject daohuolist = new JSONObject();
		long count = 0;
		Map cwbsDateMap = new HashMap();
		StringBuffer cwbss = new StringBuffer();
		String cwbStr = "";
		if ("1".equals(request.getParameter("isshow"))) {
			String cwbs = "'--'";
			List<OrderFlow> orderFlowList = new ArrayList<OrderFlow>();
			orderFlowList = orderFlowDAO.getCwbsByDateAndFlowtype(emailStartTime, eamilEndTime, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue());
			if (orderFlowList.size() > 0) {
				cwbs = "";
				for (OrderFlow orderFlow : orderFlowList) {
					cwbsDateMap.put(orderFlow.getCwb(), orderFlow.getCredate());
					cwbs += "'" + orderFlow.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "'--'";
			} else {
				cwbs = "'--'";
			}

			daohuojsonList = cwbDAO.getListByCustomeridAndDeliverystateAndCredateByBack(page, cwbs, getStrings(customerid), isout, customerwarehouseid, auditState);
			daohuolist = cwbDAO.getListByCustomeridAndDeliverystateAndCredateNopageByBack(cwbs, getStrings(customerid), isout, customerwarehouseid, auditState);

			count = cwbDAO.getListByCustomeridAndDeliverystateAndCredateCountByBack(cwbs, getStrings(customerid), isout, customerwarehouseid, auditState);
			if (daohuojsonList != null && daohuojsonList.size() > 0) {
				for (JSONObject json : daohuojsonList) {
					cwbss.append("'" + json.getString("cwb") + "',");
				}
				cwbStr = cwbss.toString().substring(0, cwbss.toString().length() - 1);
			}

		}
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));

		model.addAttribute("cwbsDateMap", cwbsDateMap);
		model.addAttribute("daohuolist", daohuolist);
		model.addAttribute("customerid", customerid);

		model.addAttribute("cumstrListAll", cumstrListAll);
		model.addAttribute("branchList", branchDao.getAllEffectBranches());

		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);

		model.addAttribute("daohuojsonList", daohuojsonList);
		// model.addAttribute("peisongchengList", peisongchengList);
		model.addAttribute("cumstrMap", cumstrMap);

		List<String> customeridList = this.getList(customerid);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("cwbs", cwbStr);

		return "funds/payment/paymentBackMonitor";
	}

	@RequestMapping("/deliverystatesearchdetailback_excel")
	@DataSource(DatabaseType.REPLICA)
	public void deliverystatesearchdetailBack_excel(Model model, HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "begin", required = false, defaultValue = "0") long page, @RequestParam(value = "emailStartTime1", required = false, defaultValue = "") String emailStartTime,
			@RequestParam(value = "eamilEndTime1", required = false, defaultValue = "") String eamilEndTime,
			@RequestParam(value = "customerid1", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "isout1", required = false, defaultValue = "-1") long isout,
			@RequestParam(value = "customerwarehouseid1", required = false, defaultValue = "-1") long customerwarehouseid,
			@RequestParam(value = "auditState1", required = false, defaultValue = "-1") long auditState) {

		String cwbs = "'--'";
		List<OrderFlow> orderFlowList = new ArrayList<OrderFlow>();
		orderFlowList = orderFlowDAO.getCwbsByDateAndFlowtype(emailStartTime, eamilEndTime, FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue());
		if (orderFlowList.size() > 0) {
			cwbs = "";
			for (OrderFlow orderFlow : orderFlowList) {
				cwbs += "'" + orderFlow.getCwb() + "',";
			}
			cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "'--'";
		} else {
			cwbs = "'--'";
		}

		List<String> cwbStrList = cwbDAO.getListByCustomeridAndDeliverystateAndCredateNoPageByBack(page, cwbs, getStrings(customerid), isout, customerwarehouseid, auditState, isout);

		try {
			Branch nowbranch = branchDao.getBranchByBranchid(getSessionUser().getBranchid());
			logger.info("货款大数据导出数据：用户名：{},站点：{} 导出数据条数" + cwbStrList.size() + "", getSessionUser().getRealname(), nowbranch.getBranchname());
		} catch (Exception e) {
			logger.error("货款大数据导出数据：获取用户名，站点异常");
		}
		advancedQueryService.batchSelectExport(response, request, cwbStrList);

	}

	@RequestMapping("/searchdetail/{type}/{customerid}/{page}")
	public String searchdetail(Model model, @PathVariable("type") long type, @PathVariable("customerid") long customerid, @PathVariable("page") long page, HttpServletRequest request) {
		int showphoneflag = 1;// .getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);

		String emailStartTime = (String) request.getSession().getAttribute("emailStartTime");
		String eamilEndTime = (String) request.getSession().getAttribute("eamilEndTime");
		if (type == FundsEnum.Fahuo.getValue()) {
			List<CwbOrder> orderList = branchPayamountService.searchDetailByFlowType(type, customerid, page, emailStartTime, eamilEndTime);
			long count = branchPayamountService.searchDetailByFlowTypeCount(type, customerid, page, emailStartTime, eamilEndTime);
			model.addAttribute("cwbdetaillist", orderList);
			model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		} else {
			Map<Integer, List<String>> map = branchPayamountService.getCwbList(customerid, type, emailStartTime, eamilEndTime);
			List<String> cwbStr = map.get((int) page);
			List<CwbOrder> orderList = new ArrayList<CwbOrder>();
			long orderCount = 0;
			if (cwbStr != null) {
				for (String cwb : cwbStr) {
					orderList.add(cwbDAO.getCwbByCwb(cwb));
				}
			}
			Set<Integer> key = map.keySet();
			for (Iterator it = key.iterator(); it.hasNext();) {
				Integer s = (Integer) it.next();
				orderCount += map.get(s).size();
			}
			model.addAttribute("cwbdetaillist", orderList);
			model.addAttribute("page_obj", new Page(orderCount, page, Page.ONE_PAGE_NUMBER));
		}
		List<Customer> cumstrListAll = customerDao.getAllCustomers();
		Map<Long, String> cumstrMap = new HashMap<Long, String>();
		for (Customer customer : cumstrListAll) {
			cumstrMap.put(customer.getCustomerid(), customer.getCustomername());
		}
		model.addAttribute("cumstrMap", cumstrMap);
		request.getSession().setAttribute("customershowid", customerid);
		request.getSession().setAttribute("flowordertype", type);
		return "funds/payment/paymentSearchDetail";
	}

	@RequestMapping("/searchdetail_excel")
	@DataSource(DatabaseType.REPLICA)
	public void paymentDetail_excel(Model model, HttpServletResponse response, HttpServletRequest request) {
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		long customerid = new Long(request.getSession().getAttribute("customershowid").toString());
		long type = new Long(request.getSession().getAttribute("flowordertype").toString());

		List<String> cwbStrList = new ArrayList<String>();
		if (type == FundsEnum.Fahuo.getValue()) {
			cwbStrList = branchPayamountService.searchDetailByFlowTypeExp(type, customerid, emailStartTime, eamilEndTime, -1);
		} else {
			cwbStrList = branchPayamountService.getCwbListAll(customerid, type, emailStartTime, eamilEndTime);

		}
		try {
			Branch nowbranch = branchDao.getBranchByBranchid(getSessionUser().getBranchid());
			logger.info("货款大数据导出数据：用户名：{},站点：{} 导出数据条数" + cwbStrList.size() + "", getSessionUser().getRealname(), nowbranch.getBranchname());
		} catch (Exception e) {
			logger.error("货款大数据导出数据：获取用户名，站点异常");
		}
		advancedQueryService.batchSelectExport(response, request, cwbStrList);

	}

	@RequestMapping("/searchdetail_back_excel")
	@DataSource(DatabaseType.REPLICA)
	public void paymentDetail_back_excel(Model model, HttpServletResponse response, HttpServletRequest request) {
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		long customerid = new Long(request.getSession().getAttribute("customershowid").toString());
		long type = new Long(request.getSession().getAttribute("flowordertype").toString());

		List<String> cwbStrList = new ArrayList<String>();
		if (type == FundsEnum.Fahuo.getValue()) {
			cwbStrList = branchPayamountService.searchDetailByFlowTypeExp(type, customerid, emailStartTime, eamilEndTime, -1);
		} else {
			cwbStrList = branchPayamountService.getCwbListAll(customerid, type, emailStartTime, eamilEndTime);

		}
		try {
			Branch nowbranch = branchDao.getBranchByBranchid(getSessionUser().getBranchid());
			logger.info("退货款大数据导出数据：用户名：{},站点：{} 导出数据条数" + cwbStrList.size() + "", getSessionUser().getRealname(), nowbranch.getBranchname());
		} catch (Exception e) {
			logger.error("退货款大数据导出数据：获取用户名，站点异常");
		}
		advancedQueryService.batchSelectExport(response, request, cwbStrList);
	}

	@RequestMapping("/payment_return")
	public String payment_return(HttpServletRequest request, Model model) {
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		long customerid = new Long(request.getSession().getAttribute("customerid").toString());

		List<Customer> cumstrListAll = customerDao.getAllCustomers();
		Map<Long, String> cumstrMap = new HashMap<Long, String>();
		for (Customer customer : cumstrListAll) {
			cumstrMap.put(customer.getCustomerid(), customer.getCustomername());
		}
		model.addAttribute("cumstrMap", cumstrMap);

		List<JSONObject> peisongchengList = deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredateAll(DeliveryStateEnum.PeiSongChengGong.getValue() + ","
				+ DeliveryStateEnum.ShangMenHuanChengGong.getValue(), emailStartTime, eamilEndTime);

		List<JSONObject> daohuojsonList = deliverStateDAO.getListByCustomeridAndFloworderTypeowAndCredate(FlowOrderTypeEnum.DaoRuShuJu.getValue(), emailStartTime, eamilEndTime, customerid);

		model.addAttribute("customerid", customerid);
		model.addAttribute("cumstrListAll", cumstrListAll);

		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);

		model.addAttribute("daohuojsonList", daohuojsonList);
		model.addAttribute("peisongchengList", peisongchengList);

		return "funds/payment/paymentMonitor";
	}

	@RequestMapping("/searchdetail_back/{type}/{customerid}/{page}")
	public String searchdetail_back(Model model, @PathVariable("type") long type, @PathVariable("customerid") long customerid, @PathVariable("page") long page, HttpServletRequest request) {
		int showphoneflag = 1;// .getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);

		String emailStartTime = (String) request.getSession().getAttribute("emailStartTime");
		String eamilEndTime = (String) request.getSession().getAttribute("eamilEndTime");
		if (type == FundsEnum.Fahuo.getValue()) {
			List<CwbOrder> orderList = branchPayamountService.searchDetailByFlowType(type, customerid, page, emailStartTime, eamilEndTime);
			long count = branchPayamountService.searchDetailByFlowTypeCount(type, customerid, page, emailStartTime, eamilEndTime);
			model.addAttribute("cwbdetaillist", orderList);
			model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		} else {
			Map<Integer, List<String>> map = branchPayamountService.getCwbList(customerid, type, emailStartTime, eamilEndTime);

			List<String> cwbStr = map.get((int) page);
			List<CwbOrder> orderList = new ArrayList<CwbOrder>();
			long orderCount = 0;
			if (cwbStr != null) {
				for (String cwb : cwbStr) {
					orderList.add(cwbDAO.getCwbByCwb(cwb));
				}
			}
			Set<Integer> key = map.keySet();
			for (Iterator it = key.iterator(); it.hasNext();) {
				Integer s = (Integer) it.next();
				orderCount += map.get(s).size();
			}
			model.addAttribute("cwbdetaillist", orderList);
			model.addAttribute("page_obj", new Page(orderCount, page, Page.ONE_PAGE_NUMBER));
		}
		List<Customer> cumstrListAll = customerDao.getAllCustomers();
		Map<Long, String> cumstrMap = new HashMap<Long, String>();
		for (Customer customer : cumstrListAll) {
			cumstrMap.put(customer.getCustomerid(), customer.getCustomername());
		}
		model.addAttribute("cumstrMap", cumstrMap);
		request.getSession().setAttribute("customershowid", customerid);
		request.getSession().setAttribute("flowordertype", type);
		return "funds/payment/paymentSearchDetail_back";
	}

	@RequestMapping("/payment_back_return")
	public String payment_back_return(HttpServletRequest request, Model model) {
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		long customerid = new Long(request.getSession().getAttribute("customerid").toString());
		long exportType = new Long(request.getSession().getAttribute("exportType").toString());
		List<Customer> cumstrListAll = customerDao.getAllCustomers();
		Map<Long, String> cumstrMap = new HashMap<Long, String>();
		for (Customer customer : cumstrListAll) {
			cumstrMap.put(customer.getCustomerid(), customer.getCustomername());
		}
		List<JSONObject> peisongchengList = deliverStateDAO.getCwbByCustomeridAndDeliverystateAndCredateAll(DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ","
				+ DeliveryStateEnum.ShangMenHuanChengGong.getValue(), emailStartTime, eamilEndTime);

		List<JSONObject> daohuojsonList = deliverStateDAO.getListByCustomeridAndFloworderTypeowAndCredate(FlowOrderTypeEnum.DaoRuShuJu.getValue(), emailStartTime, eamilEndTime, customerid);

		setSetionsExp(request, emailStartTime, eamilEndTime, customerid, exportType, 0);
		model.addAttribute("customerid", customerid);
		model.addAttribute("cumstrListAll", cumstrListAll);

		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);

		model.addAttribute("daohuojsonList", daohuojsonList);
		model.addAttribute("peisongchengList", peisongchengList);

		model.addAttribute("cumstrMap", cumstrMap);
		return "funds/payment/paymentBackMonitor";
	}

	// 保存基本查询条件的session
	private void setSetionsExp(HttpServletRequest request, String emailStartTime, String eamilEndTime, long customerid, long exportType, long count) {
		request.getSession().setAttribute("emailStartTime", emailStartTime);
		request.getSession().setAttribute("eamilEndTime", eamilEndTime);
		request.getSession().setAttribute("customerid", customerid);
		request.getSession().setAttribute("exportType", exportType);
		request.getSession().setAttribute("count", count);
	}

	private void setSetionsExpS(HttpServletRequest request, String emailStartTime, String eamilEndTime, long customerid, long exportType, long isaudit, int count) {
		request.getSession().setAttribute("emailStartTime", emailStartTime);
		request.getSession().setAttribute("eamilEndTime", eamilEndTime);
		request.getSession().setAttribute("customerid", customerid);
		request.getSession().setAttribute("exportType", exportType);
		request.getSession().setAttribute("isaudit", isaudit);
		request.getSession().setAttribute("count", count);
	}

	// --------------------------站点交款审核------------------------------
	@RequestMapping("/payamount")
	public String payamount(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,// 站点上交款开始时间
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,// 站点上交款结束时间
			@RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate, HttpServletResponse response, HttpServletRequest request) {
		List<String> sercjidlist = new ArrayList<String>();
		SystemInstall systemInstall = systemInstallDAO.getSystemInstallByName("isUsePayAudit");
		if (systemInstall != null) {
			if ("yes".equals(systemInstall.getValue())) {
				return payamountAudit(model, branchid, strateBranchpaydatetime, endBranchpaydatetime, upstate, response, request);
			}
		}
		List<PayUp> payupList = null;
		Map<Long, JSONObject> payCountMap = new HashMap<Long, JSONObject>();
		if (strateBranchpaydatetime.length() != 0 && endBranchpaydatetime.length() != 0) {
			payupList = payUpDAO.getPayUpByCredatetimeAndUpstateAndBranchid(upstate, strateBranchpaydatetime, endBranchpaydatetime, branchid, getSessionUser().getBranchid());

			for (PayUp p : payupList) {
				List<JSONObject> jsonList = payUpDAO.getPayUpByIdForCount(p.getId());
				if (jsonList.size() > 0) {
					payCountMap.put(p.getId(), jsonList.get(0));
				}
			}
			sercjidlist = payUpDAO.getpayupAllForexport(upstate, strateBranchpaydatetime, endBranchpaydatetime, branchid);
		}
		List<Branch> branchList = branchDao.getBranchAllzhandian(String.valueOf(BranchEnum.ZhanDian.getValue()));
		/*
		 * Map<Long,BigDecimal> branchTotalFee = new HashMap<Long,BigDecimal>();
		 * for(PayUp payUp : payupList){ for(Branch b :branchList){
		 * if(payUp.getBranchid()==b.getBranchid()){
		 * branchTotalFee.put(payUp.getBranchid(),
		 * (b.getArrearagepayupaudit()==null
		 * ?BigDecimal.ZERO:b.getArrearagepayupaudit
		 * ()).add(b.getPosarrearagepayupaudit
		 * ()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit())); } } }
		 * BigDecimal totaldebtfee = BigDecimal.ZERO; List<Long> keys = new
		 * ArrayList<Long>(branchTotalFee.keySet()); for (int i = 0; i <
		 * keys.size(); i++) { //总欠款
		 * totaldebtfee=totaldebtfee.add(branchTotalFee.get(keys.get(i))); }
		 * model.addAttribute("totaldebtfee",totaldebtfee);
		 */
		model.addAttribute("serch", sercjidlist);
		model.addAttribute("payupList", payupList);
		model.addAttribute("groupByPayupidCount", payCountMap);
		model.addAttribute("branchList", branchList);

		return "funds/payamount/view";
	}

	@RequestMapping("/payamountAudit")
	public String payamountAudit(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,// 站点上交款开始时间
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,// 站点上交款结束时间
			@RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate, HttpServletResponse response, HttpServletRequest request) {
		List<PayUpDTO> payupList = null;
		List<PayUpDTO> payupListShow = new ArrayList<PayUpDTO>();
		if (strateBranchpaydatetime.length() != 0 && endBranchpaydatetime.length() != 0) {
			payupList = payUpDAO.getPayUpByCredatetimeAndUpstateAndBranchidAudit(upstate, strateBranchpaydatetime, endBranchpaydatetime, branchid, getSessionUser().getBranchid());
			for (PayUpDTO p : payupList) {
				String auditIds = p.getAuditids();
				String ids = doInsertSort2(p.getIds());
				List<FinancePayUpAudit> list = financePayUpAuditDAO.getList(auditIds);
				List<JSONObject> jsonList = payUpDAO.getPayUpByIdForCountAudit(ids);
				p.setIds(ids);
				if (jsonList.size() > 0) {
					p.setAduitJson(jsonList.get(0));
					if (list != null && list.size() > 0) {
						BigDecimal payamount = BigDecimal.ZERO;
						BigDecimal payamountPos = BigDecimal.ZERO;
						for (FinancePayUpAudit financePayUpAudit : list) {
							payamount = payamount.add(financePayUpAudit.getPayamount());
							payamountPos = payamountPos.add(financePayUpAudit.getPayamountpos());
						}
						p.setRamount(payamount);
						p.setRamountPos(payamountPos);
						if (list.get(0).getUpdateTime() != null && !list.get(0).getUpdateTime().equals("")) {
							p.setUpdateTime(list.get(0).getUpdateTime());
						}
					} else {
						p.setRamount(new BigDecimal(jsonList.get(0).getString("sumCash")));
						p.setRamountPos(new BigDecimal(jsonList.get(0).getString("sumPos")));
					}
				}
				payupListShow.add(p);
			}
		}
		List<Branch> branchList = branchDao.getBranchAllzhandian(String.valueOf(BranchEnum.ZhanDian.getValue()));
		/*
		 * Map<Long,BigDecimal> branchCashTotalFee = new
		 * HashMap<Long,BigDecimal>(); Map<Long,BigDecimal> branchPosTotalFee =
		 * new HashMap<Long,BigDecimal>(); Map<Long,BigDecimal> branchTotalFee =
		 * new HashMap<Long,BigDecimal>(); for(PayUpDTO payUp : payupListShow){
		 * for(Branch b :branchList){ if(payUp.getBranchid()==b.getBranchid()){
		 * branchCashTotalFee.put(payUp.getBranchid(),
		 * b.getArrearagepayupaudit()
		 * ==null?BigDecimal.ZERO:b.getArrearagepayupaudit());
		 * branchPosTotalFee.put(payUp.getBranchid(),
		 * b.getPosarrearagepayupaudit
		 * ()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit());
		 * branchTotalFee.put(payUp.getBranchid(),
		 * (b.getArrearagepayupaudit()==null
		 * ?BigDecimal.ZERO:b.getArrearagepayupaudit
		 * ()).add(b.getPosarrearagepayupaudit
		 * ()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit())); } } }
		 * BigDecimal totalcashdebtfee = BigDecimal.ZERO; BigDecimal
		 * totalposdebtfee = BigDecimal.ZERO; BigDecimal totaldebtfee =
		 * BigDecimal.ZERO; List<Long> cashkeys = new
		 * ArrayList<Long>(branchCashTotalFee.keySet()); for (int i = 0; i <
		 * cashkeys.size(); i++) { //现金欠款
		 * totalcashdebtfee=totalcashdebtfee.add(branchCashTotalFee
		 * .get(cashkeys.get(i))); } List<Long> poskeys = new
		 * ArrayList<Long>(branchPosTotalFee.keySet()); for (int i = 0; i <
		 * poskeys.size(); i++) { //pos欠款
		 * totalposdebtfee=totalposdebtfee.add(branchPosTotalFee
		 * .get(poskeys.get(i))); } List<Long> keys = new
		 * ArrayList<Long>(branchTotalFee.keySet()); for (int i = 0; i <
		 * keys.size(); i++) { //总欠款
		 * totaldebtfee=totaldebtfee.add(branchTotalFee.get(keys.get(i))); }
		 * model.addAttribute("totalcashdebtfee",totalcashdebtfee);
		 * model.addAttribute("totalposdebtfee",totalposdebtfee);
		 * model.addAttribute("totaldebtfee",totaldebtfee);
		 */
		model.addAttribute("payupList", payupListShow);
		model.addAttribute("branchList", branchList);
		return "funds/payamount/viewAudit";
	}

	@RequestMapping("/viewall")
	public String viewallList(Model model, @RequestParam(value = "beginday", required = false, defaultValue = "") String beginday,
			@RequestParam(value = "endday", required = false, defaultValue = "") String endday, @RequestParam(value = "hours", required = false, defaultValue = "0") int hours,
			HttpServletRequest request) {

		List<Branch> bList = branchDao.getAllBranchBySiteType(BranchEnum.ZhanDian.getValue());
		Date toDay = new Date();
		String secondDay = "";
		try {
			toDay = sdf.parse(beginday);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(toDay);
			rightNow.add(Calendar.HOUR, hours);// 日期加小时
			beginday = df.format(rightNow.getTime());
			rightNow.add(Calendar.DAY_OF_YEAR, 1);// 日期加一天
			secondDay = df.format(rightNow.getTime());
			if (endday.length() > 0) {
				Date tomorrow = new Date();
				tomorrow = sdf.parse(endday);
				Calendar tomorCalendar = Calendar.getInstance();
				tomorCalendar.setTime(tomorrow);
				tomorCalendar.add(Calendar.HOUR, hours);
				secondDay = df.format(tomorCalendar.getTime());
			}
		} catch (ParseException e) {
			beginday = sdf.format(toDay);
			secondDay = beginday + " 23:59:59";
			if (endday.length() > 0) {
				secondDay = endday + " 23:59:59";
			}
		}

		List<JSONObject> deliveryAmountList = deliverStateDAO.getDeliveryByDayGroupByDeliverybranchid(beginday, secondDay);
		List<JSONObject> auditingAmountList = gotoClassAuditingDAO.getAuditingByDayGroupByBranchid(beginday, secondDay);
		List<JSONObject> payupAmountList = payUpDAO.getPayupByDayGroupByBranchid(beginday, secondDay);

		List<JSONObject> viewAllList = new ArrayList<JSONObject>();
		for (Branch b : bList) {
			JSONObject viewAll = new JSONObject();
			viewAll.put("branchname", b.getBranchname());
			for (JSONObject deliveryAmount : deliveryAmountList) { // 反馈未归班的总金额
				if (b.getBranchid() == deliveryAmount.getLong("deliverybranchid")) {
					viewAll.putAll(deliveryAmount);
					viewAll.put("deliveryamount", deliveryAmount.getDouble("amount"));
					viewAll.put("deliverypos", deliveryAmount.getDouble("pos"));
				}
			}
			for (JSONObject auditingAmount : auditingAmountList) {// 归班未缴款的总金额
				if (b.getBranchid() == auditingAmount.getLong("branchid")) {
					viewAll.putAll(auditingAmount);
					viewAll.put("auditingamount", auditingAmount.getDouble("amount"));
					viewAll.put("auditingpos", auditingAmount.getDouble("pos"));
				}
			}

			for (JSONObject payupAmount : payupAmountList) {// 归班未缴款的总金额
				if (b.getBranchid() == payupAmount.getLong("branchid")) {
					viewAll.putAll(payupAmount);
					viewAll.put("payupamount", payupAmount.getDouble("amount"));
					viewAll.put("payuppos", payupAmount.getDouble("pos"));
				}
			}
			viewAllList.add(viewAll);

		}

		model.addAttribute("list", viewAllList);

		return "funds/payamount/viewAll";
	}

	/**
	 * 查询各款项 未审核的
	 * 
	 * @param model
	 * @param controlStr
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/deliveryStateTypeShow/{id}/{type}/{page}")
	public String deliveryStateTypeShow(Model model, @PathVariable("id") long id, @PathVariable("type") long type, @PathVariable("page") long page,
			@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,// 站点上交款开始时间
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,// 站点上交款结束时间
			@RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate, HttpServletRequest request) {
		request.getSession().setAttribute("type", type);
		model.addAttribute("branchid", branchid);
		model.addAttribute("strateBranchpaydatetime", strateBranchpaydatetime);
		model.addAttribute("endBranchpaydatetime", endBranchpaydatetime);
		model.addAttribute("upstate", upstate);
		model.addAttribute("payupid", id);
		model.addAttribute("userList", userDAO.getAllUser());
		model.addAttribute("deliveryStatelist", payUpDAO.getPayUpByIdForDetail(id, type, page));
		model.addAttribute("page_obj", new Page(payUpDAO.getPayUpByIdForDetailCount(id, type), page, Page.ONE_PAGE_NUMBER));
		return "funds/payamount/DeliveryView";
	}

	@RequestMapping("/deliveryStateTypeAuditShow/{ids}/{type}/{page}")
	public String deliveryStateTypeAuditShow(Model model, @PathVariable("ids") String ids, @PathVariable("type") long type, @PathVariable("page") long page,
			@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,// 站点上交款开始时间
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,// 站点上交款结束时间
			@RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate, HttpServletRequest request) {
		request.getSession().setAttribute("type", type);
		model.addAttribute("branchid", branchid);
		model.addAttribute("strateBranchpaydatetime", strateBranchpaydatetime);
		model.addAttribute("endBranchpaydatetime", endBranchpaydatetime);
		model.addAttribute("upstate", upstate);
		model.addAttribute("payupid", ids);
		model.addAttribute("userList", userDAO.getAllUser());
		model.addAttribute("deliveryStatelist", payUpDAO.getPayUpByIdForDetailAudit(ids, type, page));
		model.addAttribute("page_obj", new Page(payUpDAO.getPayUpByIdForDetailAuditCount(ids, type), page, Page.ONE_PAGE_NUMBER));
		return "funds/payamount/DeliveryViewAudit";
	}

	/**
	 * 合计的明细功能
	 * 
	 * @param model
	 * @param id
	 * @param type
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/deliveryStateTypeAllShow/{type}/{page}")
	public String deliveryStateTypeAllShow(Model model, @PathVariable("type") long type, @PathVariable("page") long page,
			@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,// 站点上交款开始时间
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,// 站点上交款结束时间
			@RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate, HttpServletRequest request) {
		request.getSession().setAttribute("type", type);
		model.addAttribute("branchid", branchid);
		model.addAttribute("strateBranchpaydatetime", strateBranchpaydatetime);
		model.addAttribute("endBranchpaydatetime", endBranchpaydatetime);
		model.addAttribute("upstate", upstate);
		model.addAttribute("payupid", 0);
		model.addAttribute("userList", userDAO.getAllUser());
		model.addAttribute("deliveryStatelist",
				payUpDAO.getAllPayUpByCredatetimeAndUpstateAndBranchid(page, upstate, strateBranchpaydatetime, endBranchpaydatetime, branchid, getSessionUser().getBranchid(), type));
		model.addAttribute("page_obj",
				new Page(payUpDAO.getAllPayUpByCredatetimeAndUpstateAndBranchidCount(upstate, strateBranchpaydatetime, endBranchpaydatetime, branchid, getSessionUser().getBranchid(), type), page,
						Page.ONE_PAGE_NUMBER));
		return "funds/payamount/DeliveryView";
	}

	@RequestMapping("/showExp/{id}")
	public void showDeliveryStateExp(Model model, @PathVariable("id") long id, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,// 站点上交款开始时间
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,// 站点上交款结束时间
			@RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate, HttpServletResponse response, HttpServletRequest request) {
		long type = request.getSession().getAttribute("type") == null ? -1 : Long.parseLong(request.getSession().getAttribute("type").toString());
		List<JSONObject> obj = payUpDAO.getPayUpByIdForDetailNopage(id, type);
		if (id == 0) {
			obj = payUpDAO.getAllPayUpByCredatetimeAndUpstateAndBranchidNoPage(upstate, strateBranchpaydatetime, endBranchpaydatetime, branchid, getSessionUser().getBranchid(), type);
		}

		for (JSONObject singleobj : obj) {
			CwbOrder co = cwbDAO.getCwbByCwb(singleobj.getString("cwb"));
			String deliverybranch = branchDao.getBranchById(co.getDeliverybranchid()).getBranchid() == 0 ? "" : branchDao.getBranchById(co.getDeliverybranchid()).getBranchname();
			String customername = customerDao.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? "" : customerDao.getCustomerById(co.getCustomerid()).getCustomername();
			singleobj.put("deliverybranch", deliverybranch);
			singleobj.put("customername", customername);
		}
		branchPayamountService.getCwbDetailByFlowOrder_payment(response, obj, userDAO.getAllUser());
	}

	@RequestMapping("/showExpAudit/{ids}")
	public void showDeliveryStateAuditExp(Model model, @PathVariable("ids") String ids, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,// 站点上交款开始时间
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,// 站点上交款结束时间
			@RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate, HttpServletResponse response, HttpServletRequest request) {
		long type = request.getSession().getAttribute("type") == null ? -1 : Long.parseLong(request.getSession().getAttribute("type").toString());
		List<JSONObject> obj = payUpDAO.getPayUpByIdForDetailAuditNopage(ids, type);
		if (ids.length() == 0) {
			obj = payUpDAO.getAllPayUpByCredatetimeAndUpstateAndBranchidNoPage(upstate, strateBranchpaydatetime, endBranchpaydatetime, branchid, getSessionUser().getBranchid(), type);
		}

		for (JSONObject singleobj : obj) {
			CwbOrder co = cwbDAO.getCwbByCwb(singleobj.getString("cwb"));
			String deliverybranch = branchDao.getBranchById(co.getDeliverybranchid()).getBranchid() == 0 ? "" : branchDao.getBranchById(co.getDeliverybranchid()).getBranchname();
			String customername = customerDao.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? "" : customerDao.getCustomerById(co.getCustomerid()).getCustomername();
			singleobj.put("deliverybranch", deliverybranch);
			singleobj.put("customername", customername);
		}
		branchPayamountService.getCwbDetailByFlowOrder_payment(response, obj, userDAO.getAllUser());
	}

	@RequestMapping("/update")
	public ModelAndView update(Model model, @RequestParam(value = "controlStr", required = false, defaultValue = "") String controlStr,
			@RequestParam(value = "mackStr", required = false, defaultValue = "") String mackStr, @RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate,
			@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,// 站点上交款开始时间
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,// 站点上交款结束时间
			HttpServletRequest request) {

		String[] mStr = null;
		if (controlStr.length() > 0) {
			String[] conStr = controlStr.split(";");
			if (mackStr != null && mackStr.length() > 0 && mackStr.indexOf("P:P") > -1) {
				mStr = mackStr.split("P:P");
			}

			for (int i = 0; i < conStr.length; i++) {
				if (!"-1".equals(conStr[i])) {
					String auditingremark = "";
					if (mStr != null && mStr.length != 0) {
						if (mStr[i] != null && mStr[i].length() > 0) {
							auditingremark = mStr[i];
						}
					}
					payUpDAO.savePayUpForAudting(Long.parseLong(conStr[i]), getSessionUser().getRealname(), auditingremark, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				}

			}

		}
		model.addAttribute("page_obj", new Page());

		return new ModelAndView("redirect:/funds/payamount?branchid=" + branchid + "&strateBranchpaydatetime=" + strateBranchpaydatetime + "&endBranchpaydatetime=" + endBranchpaydatetime
				+ "&upstate=" + upstate);
	}

	@RequestMapping("/updateAudit")
	public ModelAndView updateAudit(Model model, @RequestParam(value = "controlStr", required = false, defaultValue = "") String controlStr,
			@RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,// 站点上交款开始时间
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,// 站点上交款结束时间
			HttpServletRequest request) {
		logger.info("站点交款审核提交的内容：{}", controlStr);
		if (controlStr.length() > 0) {
			JSONArray jsonList = JSONArray.fromObject(controlStr);

			branchPayamountService.updateAudit(jsonList, getSessionUser());
		}
		model.addAttribute("page_obj", new Page());
		return new ModelAndView("redirect:/funds/payamountAudit?branchid=" + branchid + "&strateBranchpaydatetime=" + strateBranchpaydatetime + "&endBranchpaydatetime=" + endBranchpaydatetime
				+ "&upstate=" + upstate);
	}

	@RequestMapping("/financeChargeList/{page}")
	public String financeChargeList(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "type", required = false, defaultValue = "0") long type, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, HttpServletRequest request) {

		if (begindate.length() == 0 || enddate.length() == 0) {
			begindate = DateTimeUtil.getCurrentDayZeroTime();
			enddate = DateTimeUtil.getNowTime();
		}
		List<FinanceAudit> financeAuditList = financeAuditDAO.getFinanceAuditByParams(page, customerid, type, begindate, enddate);

		model.addAttribute("financeAuditList", financeAuditList);
		model.addAttribute("customerList", customerDao.getAllCustomers());
		model.addAttribute("page_obj", new Page(financeAuditDAO.getFinanceAuditByParamsCount(customerid, type, begindate, enddate), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/funds/payment/financeChargeList";
	}

	/**
	 * 财务记账查询功能
	 * 
	 * @param model
	 * @param response
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/financeChargeconfirm/{id}")
	public String financeChargeconfirm(Model model, HttpServletResponse response, HttpServletRequest request, @PathVariable("id") long id) {
		FinanceAudit financeAudit = financeAuditDAO.getFinanceAuditById(id);

		model.addAttribute("customer", customerDao.getCustomerById(financeAudit.getCustomerid()));
		model.addAttribute("customerwarehouse", customWareHouseDAO.getWarehouseId(financeAudit.getCustomerwarehouseid()));
		model.addAttribute("financeChargeDetail", financeAudit);
		return "funds/payment/financeChargeConfirm";
	}

	/**
	 * 财务记账查询功能中的付款功能
	 * 
	 * @param model
	 * @param response
	 * @param request
	 * @param id
	 * @param payqiankuandatetime
	 * @param paytype
	 * @param payqiankuanamount
	 * @param paynumber
	 * @param payremark
	 * @return
	 */
	@RequestMapping("/financecharge/{id}")
	public String financecharge(Model model, HttpServletResponse response, HttpServletRequest request, @PathVariable("id") long id, @RequestParam("payqiankuandatetime") String payqiankuandatetime,
			@RequestParam(value = "paytype", required = false, defaultValue = "1") int paytype, @RequestParam("payqiankuanamount") BigDecimal payqiankuanamount,
			@RequestParam(value = "paynumber", required = false, defaultValue = "") String paynumber, @RequestParam(value = "payremark", required = false, defaultValue = "") String payremark) {
		FinanceAudit fa = financeAuditDAO.getFinanceAuditById(id);
		try {
			if (payqiankuanamount == null || payqiankuanamount.compareTo(fa.getQiankuanamount()) > 0) {
				throw new ExplinkException("上交欠款金额不能大于欠款或者为空，请核实！");
			}
			BigDecimal payamount = fa.getPayamount().add(payqiankuanamount);
			fa.setPayamount(payamount);
			BigDecimal qiankuanamount = fa.getShouldpayamount().subtract(fa.getPayamount());
			fa.setQiankuanamount(qiankuanamount);
			fa.setPayqiankuanamount(payqiankuanamount);
			fa.setPaynumber(paynumber);
			fa.setPayremark(payremark);
			fa.setPaytype(paytype);
			financeAuditDAO.saveFinanceAuditById(id, payqiankuandatetime, BigDecimal.ZERO, qiankuanamount, payamount, payremark, paynumber, paytype);
			logger.info("财务结算交欠款：{}", fa.toString());
			model.addAttribute("fa", fa);
		} catch (ExplinkException e) {
			model.addAttribute("error", e.getMessage());
		}

		model.addAttribute("customer", customerDao.getCustomerById(fa.getCustomerid()));
		model.addAttribute("customerwarehouse", customWareHouseDAO.getWarehouseId(fa.getCustomerwarehouseid()));

		return "funds/payment/financeCharge";
	}

	/**
	 * 财务记账查询功能导出功能
	 * 
	 * @param model
	 * @param response
	 * @param request
	 * @param auditid
	 */
	@RequestMapping("/financeCharge_excel")
	public void financeCharge_excel(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam("auditid") long auditid) {
		List<String> cwbStrList = financeAuditDAO.getFinanceAuditTempByAuditid(auditid);
		try {
			Branch nowbranch = branchDao.getBranchByBranchid(getSessionUser().getBranchid());
			logger.info("财务记账查询功能导出数据：用户名：{},站点：{} 导出数据条数" + cwbStrList.size() + "", getSessionUser().getRealname(), nowbranch.getBranchname());
		} catch (Exception e) {
			logger.error("财务记账查询功能导出数据：获取用户名，站点异常");
		}
		advancedQueryService.batchSelectExport(response, request, cwbStrList);
	}

	public String getStrings(String[] strArr) {
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

	public List<String> getList(String[] strArr) {
		List<String> strList = new ArrayList<String>();
		if (strArr != null && strArr.length > 0) {
			for (String str : strArr) {
				strList.add(str);
			}
		}
		return strList;
	}

	/*
	 * 导出excel
	 */
	@RequestMapping("/dateExport")
	public String dateExp(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,// 站点上交款开始时间
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,// 站点上交款结束时间
			@RequestParam(value = "upstate", required = false, defaultValue = "-1") int upstate, HttpServletResponse response, HttpServletRequest request) {
		List<PayUpDTO> payupList = null;
		List<PayUpDTO> payupListShow = new ArrayList<PayUpDTO>();
		if (strateBranchpaydatetime.length() != 0 && endBranchpaydatetime.length() != 0) {
			payupList = payUpDAO.getPayUpByCredatetimeAndUpstateAndBranchidAudit(upstate, strateBranchpaydatetime, endBranchpaydatetime, branchid, getSessionUser().getBranchid());
			for (PayUpDTO p : payupList) {
				String auditIds = p.getAuditids();
				String ids = doInsertSort2(p.getIds());
				List<FinancePayUpAudit> list = financePayUpAuditDAO.getList(auditIds);
				List<JSONObject> jsonList = payUpDAO.getPayUpByIdForCountAudit(ids);
				p.setIds(ids);
				if (jsonList.size() > 0) {
					p.setAduitJson(jsonList.get(0));
					if (list != null && list.size() > 0) {
						BigDecimal payamount = BigDecimal.ZERO;
						BigDecimal payamountPos = BigDecimal.ZERO;
						for (FinancePayUpAudit financePayUpAudit : list) {
							payamount = payamount.add(financePayUpAudit.getPayamount());
							payamountPos = payamountPos.add(financePayUpAudit.getPayamountpos());
						}
						p.setRamount(payamount);
						p.setRamountPos(payamountPos);
						if (list.get(0).getUpdateTime() != null && !list.get(0).getUpdateTime().equals("")) {
							p.setUpdateTime(list.get(0).getUpdateTime());
						}
					} else {
						p.setRamount(new BigDecimal(jsonList.get(0).getString("sumCash")));
						p.setRamountPos(new BigDecimal(jsonList.get(0).getString("sumPos")));
					}
				}
				payupListShow.add(p);
			}
		}

		model.addAttribute("payupList", payupListShow);
		model.addAttribute("branchList", branchDao.getBranchAllzhandian(String.valueOf(BranchEnum.ZhanDian.getValue())));
		return "/funds/payamount/viewAuditXp";
	}

	@RequestMapping("/exprotTestYB")
	public void exportExcle(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "id", required = false, defaultValue = "-1") final String id) {

		String[] cloumnName1 = new String[19]; // 导出的列名
		String[] cloumnName2 = new String[19]; // 导出的英文列名
		exportService.SetDeliverPayupFiel(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "站点导出"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "BranchExport_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			String ids = request.getParameter("id") == null ? "" : request.getParameter("id").toString();

			final List<PayUp> paylist = payUpDAO.getAllForExportbyid(ids);
			String[] i = ids.split(",");
			List<NewForExportJson> slist = new ArrayList<NewForExportJson>();
			for (int j = 0; j < i.length; j++) {
				NewForExportJson nfe = payUpDAO.getPayUpByIdForExport(i[j]);
				slist.add(nfe);
			}

			final List<NewForExportJson> listshow = new ArrayList<NewForExportJson>();
			final List<User> userList = userDAO.getAllUser();
			final List<Branch> branchList = branchDao.getAllBranches();
			for (NewForExportJson p : slist) {
				List<FinancePayUpAudit> list = financePayUpAuditDAO.getList(String.valueOf(p.getId()));
				List<JSONObject> jsonList = payUpDAO.getPayUpByIdForCountAudit(String.valueOf(p.getId()));
				if (jsonList.size() > 0) {
					p.setAduitJson(jsonList.get(0));
					if (list != null && list.size() > 0) {
						p.setRamount(list.get(0).getPayamount());
						p.setRamountPos(list.get(0).getPayamountpos());
					} else {
						p.setRamount(new BigDecimal(jsonList.get(0).getString("sumCash")));
						p.setRamountPos(new BigDecimal(jsonList.get(0).getString("sumPos")));
					}
				}
				listshow.add(p);

			}
			ExcelUtils excelUtil = new ExcelUtils() {// 生成工具类实例，并实现填充数据的抽象方法//生成工具类实例，并实现填充数据的抽象方法

				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < listshow.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setexportC(cloumnName3, request1, listshow, paylist, a, i, k, branchList, userList);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}

			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private String doInsertSort2(String payupIds) {
		String[] ids = payupIds.split(",");
		String result = "";
		int[] src = new int[ids.length];
		for (int i = 0; i < ids.length; i++) {
			src[i] = Integer.parseInt(ids[i]);
		}
		int len = src.length;
		for (int i = 1; i < len; i++) {
			int j;
			int temp = src[i];
			for (j = i; j > 0; j--) {
				if (src[j - 1] > temp) {
					src[j] = src[j - 1];
				} else
					// 如果当前的数，不小前面的数，那就说明不小于前面所有的数，
					// 因为前面已经是排好了序的，所以直接通出当前一轮的比较
					break;
			}
			src[j] = temp;
		}
		for (int i : src) {
			result += "" + i + ",";
		}
		return result.length() > 0 ? result.substring(0, result.length() - 1) : "0";
	}

}
