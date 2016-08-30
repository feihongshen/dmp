package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import cn.explink.b2c.auto.order.service.OrderPayChangeService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.AccountAreaDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbApplyZhongZhuanDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OperationTimeDAO;
import cn.explink.dao.OrderArriveTimeDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.OrderBackRukuRecordDao;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.OrderbackRecordDao;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.ShangMenTuiCwbDetailDAO;
import cn.explink.dao.ShiXiaoDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.dao.TruckDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.OrderBackRuku;
import cn.explink.domain.OrderGoods;
import cn.explink.domain.OrderbackRecord;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.ShangMenTuiCwbDetail;
import cn.explink.domain.ShiXiao;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.lefengVo;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderListTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.AdjustmentRecordService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbRouteService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.DfFeeService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.LogToDayService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.ExcelUtilsHandler;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;
import cn.explink.util.TuiGongHuoShangPage;

@RequestMapping("/cwborder")
@Controller
public class CwbOrderController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("orderPayChangeService")
	private OrderPayChangeService orderPayChangeService ;
	
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	EmailDateDAO emaildateDao;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	AccountAreaDAO accountareaDao;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	TruckDAO truckDAO;
	@Autowired
	OutWarehouseGroupDAO outWarehouseGroupDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	LogToDayService logToDayService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	ExportwarhousesummaryDAO exportwarhousesummaryDAO;
	@Autowired
	CwbApplyZhongZhuanDAO cwbApplyZhongZhuanDAO;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ShiXiaoDAO shiXiaoDAO;

	@Autowired
	ExportService exportService;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	JointService jointService;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	ShangMenTuiCwbDetailDAO shangMenTuiCwbDetailDAO;
	@Autowired
	OrderArriveTimeDAO orderArriveTimeDAO;
	@Autowired
	OrderBackCheckDAO orderBackCheckDAO;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;
	@Autowired
	OperationTimeDAO operationTimeDAO;
	@Autowired
	OrderbackRecordDao orderbackRecordDao;
	@Autowired
	OrderBackRukuRecordDao orderBackRukuRecordDao;
	@Autowired
	AdjustmentRecordService adjustmentRecordService;
	
    @Autowired
    DfFeeService dfFeeService;

	@Autowired
	TransCwbDetailDAO transCwbDetailDAO;
	@Autowired
	TranscwbOrderFlowDAO transcwbOrderFlowDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate, @RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate, @RequestParam(value = "ordercwb", required = false, defaultValue = "") String ordercwb, @RequestParam(value = "servicearea", required = false, defaultValue = "0") long servicearea, @RequestParam(value = "emailfinishflag", required = false, defaultValue = "") String emailfinishflag, @RequestParam(value = "emaildateid", required = false, defaultValue = "0") long emaildateid, @RequestParam(value = "showMess", required = false, defaultValue = "") String showMess) {
		model.addAttribute("cwborderList", this.cwbDao.getcwbOrderByPage(page, customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, "", emaildateid));
		model.addAttribute("page_obj", new Page(this.cwbDao.getcwborderCount(customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, "", emaildateid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("customerList", this.customerDao.getAllCustomers());
		model.addAttribute("branchList", this.branchDAO.getAllEffectBranches());
		model.addAttribute("accountareaList", this.accountareaDao.getAllAccountArea());
		model.addAttribute("emaildateList", this.emaildateDao.getAllEmailDate());
		if (showMess.equals("1")) {
			model.addAttribute("showMess", "1");
		}
		return "/cwborder/list";
	}

	@RequestMapping("/delandedit/{page}")
	public String delandedit(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate, @RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate, @RequestParam(value = "ordercwb", required = false, defaultValue = "") String ordercwb, @RequestParam(value = "servicearea", required = false, defaultValue = "0") long servicearea, @RequestParam(value = "emailfinishflag", required = false, defaultValue = "") String emailfinishflag, @RequestParam(value = "emaildateid", required = false, defaultValue = "0") long emaildateid) {
		model.addAttribute("cwborderList", this.cwbDao.getcwbOrderByPage(page, customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, "", emaildateid));
		model.addAttribute("page_obj", new Page(this.cwbDao.getcwborderCount(customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, "", emaildateid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/cwborder/delandedit";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long cwborderid, Model model, HttpServletRequest request) {
		model.addAttribute("cwborder", this.cwbDao.getCwbOrderByOpscwbid(cwborderid));
		return "/cwborder/edit";
	}

	@RequestMapping("/save/{id}")
	public String save(Model model, HttpServletRequest request, @PathVariable("id") long cwborderid) {
		CwbOrder cwborder1 = new CwbOrder();
		cwborder1.setConsigneeno(request.getParameter("consigneeno"));
		cwborder1.setConsigneename(request.getParameter("consigneename"));
		cwborder1.setConsigneeaddress(request.getParameter("consigneeaddress"));
		cwborder1.setConsigneepostcode(request.getParameter("consigneepostcode"));
		cwborder1.setConsigneephone(request.getParameter("consigneephone"));
		cwborder1.setOpscwbid(cwborderid);
		this.cwbDao.saveCwbOrder(cwborder1);
		model.addAttribute("saveremand", "修改成功！");
		return "/cwborder/delandedit";
	}

	@RequestMapping("/selectforpda/{cwb}")
	public String selectforpda(Model model, HttpServletRequest request, @PathVariable("cwb") String cwb) {

		model.addAttribute("cwb", cwb);
		model.addAttribute("clist", this.customerDao.getAllCustomers());
		model.addAttribute("driverlist", this.userDAO.getUserByRole(3));
		model.addAttribute("deliverlist", this.userDAO.getUserByRole(2));
		model.addAttribute("tlist", this.truckDAO.getAllTruck());
		model.addAttribute("blist", this.branchDAO.getAllEffectBranches());
		model.addAttribute("owglist", this.outWarehouseGroupDAO.getAllOutWarehouse());
		model.addAttribute("reasonlist", this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ChangeTrains.getValue()));
		return "/cwborder/selectforpda";
	}

	@RequestMapping("/selectforsmt/{page}")
	public String selectforsmt(Model model, @PathVariable("page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate) {

		List<CwbOrder> clist = this.cwbDao.getCwbOrderByCwbordertypeidAndBranchid(page, CwbOrderTypeIdEnum.Shangmentui.getValue(), this.getSessionUser().getBranchid(), "", -1, begindate, enddate);

		model.addAttribute("cwbList", clist);
		model.addAttribute("page_obj", new Page(this.cwbDao.getCwbOrderCount(CwbOrderTypeIdEnum.Shangmentui.getValue(), this.getSessionUser().getBranchid(), "", -1, begindate, enddate), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/cwborder/selectforsmt";
	}

	@RequestMapping("/selectforkfsmt/{page}")
	public String selectforkfsmt(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid, @RequestParam(value = "printType", required = false, defaultValue = "3") long printType, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "orders", required = false, defaultValue = "") String orders, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "selectype", required = false, defaultValue = "") String selectype, @RequestParam(value = "copyorders", required = false, defaultValue = "") String copyorders) {
		List<Branch> bList = this.branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		Branch nowbranch = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		if ((branchid == -1) || (branchid == 0)) {
			if ((nowbranch != null) && (nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {// 站点角色
				branchid = this.getSessionUser().getBranchid();
				bList = new ArrayList<Branch>();
				bList.add(nowbranch);
			}
		} else if ((nowbranch != null) && (nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {// 站点角色
			branchid = this.getSessionUser().getBranchid();
			bList = new ArrayList<Branch>();
			bList.add(nowbranch);
		}
		model.addAttribute("branchlist", bList);
		model.addAttribute("customerlist", this.customerDao.getAllCustomers());
		List<String> customeridList = this.dataStatisticsService.getList(customerid);
		model.addAttribute("customeridStr", customeridList);
		if (isshow != 0) {
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			String customerids = this.dataStatisticsService.getStrings(customerid);
			StringBuffer buffer = new StringBuffer();
			if (selectype.equals("1")) {
				if (!orders.isEmpty() && !orders.equals("每次输入的订单不超过500个")) {
					String[] orderStrings = orders.split("\\r\\n");
					for (int i = 0; i < orderStrings.length; i++) {
						buffer.append("'").append(orderStrings[i] + "',");
					}
					if (buffer.length() > 0) {
						orders = buffer.substring(0, buffer.length() - 1).toString();
					}
				} else if ((orders.isEmpty() || orders.equals("每次输入的订单不超过500个")) && !copyorders.isEmpty() && selectype.equals("1")) {
					orders = copyorders;
				} else {
					orders = "";
				}
				model.addAttribute("orders", orders);
			}

			List<String> smtcwbsList = this.shangMenTuiCwbDetailDAO.getShangMenTuiCwbDetailByCustomerid(customerids, printType, begindate, enddate, branchid, orders, selectype);
			String cwbs = "";
			if (smtcwbsList.size() > 0) {
				cwbs = this.dataStatisticsService.getOrderFlowCwbs(smtcwbsList);
			} else {
				cwbs = "'--'";
			}
			List<CwbOrder> clist = this.cwbDao.getCwbByCwbsPage(page, cwbs);
			model.addAttribute("cwbList", clist);
			model.addAttribute("page_obj", new Page(this.cwbDao.getCwbOrderCwbsCount(cwbs), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);

		}

		return "/cwborder/selectforkfsmt";
	}

	@RequestMapping("/selectforsmtprint")
	public String selectforsmtprint(Model model, @RequestParam(value = "nextbranchid", defaultValue = "-1", required = false) long nextbranchid, @RequestParam(value = "customeridstr", required = false, defaultValue = "") String customerids, @RequestParam(value = "modal", defaultValue = "0", required = false) long modal) {
		if (modal == 1) {
			return this.selectforsmtprintgome(model, nextbranchid, customerids);
		}
		customerids = customerids.length() > 1 ? customerids.substring(0, customerids.length() - 1) : "";
		if (nextbranchid == -1) {
			nextbranchid = this.getSessionUser().getBranchid();
		}
		List<CwbOrder> clist = this.cwbDao.getCwbByPrinttime(this.getSessionUser().getBranchid(), nextbranchid, customerids);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		for (CwbOrder c : clist) {
			/*this.cwbDao.saveCwbForPrinttime(c.getCwb(), printtime);
			this.shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(c.getCwb(), printtime);*/
			cwborderService.updatePrinttimeState(c,printtime);
		}
		List<Customer> customerlist = this.customerDao.getAllCustomers();

		model.addAttribute("clist", clist);
		model.addAttribute("customerlist", customerlist);
		return "/cwborder/selectforsmtprint";
	}

	@RequestMapping("/selectforsmtprintgome")
	public String selectforsmtprintgome(Model model, @RequestParam(value = "nextbranchid", defaultValue = "-1", required = false) long nextbranchid, @RequestParam(value = "customerid", required = false, defaultValue = "") String customerids) {

		if (nextbranchid == -1) {
			nextbranchid = this.getSessionUser().getBranchid();
		}
		customerids = customerids.length() > 1 ? customerids.substring(0, customerids.length() - 1) : "";
		List<CwbOrder> clist = this.cwbDao.getCwbByPrinttime(this.getSessionUser().getBranchid(), nextbranchid, customerids);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		for (CwbOrder c : clist) {
			cwborderService.updatePrinttimeState(c,printtime);
			/*this.cwbDao.saveCwbForPrinttime(c.getCwb(), printtime);
			this.shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(c.getCwb(), printtime);*/
		}
		List<Customer> customerlist = this.customerDao.getAllCustomers();
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();

		model.addAttribute("clist", clist);
		model.addAttribute("branchList", branchList);
		model.addAttribute("customerlist", customerlist);
		return "/cwborder/selectforgomeprint";
	}

@RequestMapping("/selectforsmtbdprint")
	public String selectforsmtbdprint(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint, @RequestParam(value = "modal", defaultValue = "0", required = false) long modal) {
		/*SystemInstall systemInstall=systemInstallDAO.getSystemInstallByName("是否默认模板为VIP模板","isdefaultmodel");
		if(systemInstall.getValue().equals("yes")&&systemInstall.getValue()!=""){
			if (modal==0) {
				modal=3;
			}

		}*/
	    String printCwb = "";
	    for (String cwb : isprint) {
	    	printCwb += cwb + ",";
	    }
	    String now = DateTimeUtil.getNowTime();
	    String realname = getSessionUser().getRealname();
	    logger.info("上门退订单打印，打印人：{},打印时间：{},打印订单号：{}",realname,now,printCwb.substring(0, printCwb.length() - 1));
		if (modal == 1) {
			return this.selectforgomeprint(model, isprint);
		}
		if (modal == 2) {
			return this.selectforjiayougouwu(model, isprint);
		}
		if (modal == 3) {
			return this.selectforvip(model, isprint);
		}
		String cwbs = "";
	    for (int i = 0; i < isprint.length; i++) {
	     	cwbs += "'" + isprint[i] + "',";
	    }
		List<ShangMenTuiCwbDetail> smtlist = this.shangMenTuiCwbDetailDAO.getShangMenTuiCwbDetailByCwbs(cwbs.substring(0, cwbs.length() - 1));
		List<CwbOrder> clist = cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		for (CwbOrder smtcd : clist) {
			cwborderService.updatePrinttimeState(smtcd, printtime);
		}
		List<Customer> customerlist = customerDao.getAllCustomers();
		SystemInstall companyName = this.systemInstallDAO.getSystemInstallByName("CompanyName");
		if (companyName != null) {
			model.addAttribute("companyName", companyName.getValue());
		} else {
			model.addAttribute("companyName", "易普联科");
		}
		model.addAttribute("smtlist", smtlist);
		model.addAttribute("customerlist", customerlist);
		return "cwborder/selectforsmtprint";
	}

	// 家有购物模板
	@RequestMapping("/selectforjiayougouwu")
	private String selectforjiayougouwu(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}
		List<ShangMenTuiCwbDetail> smtlist = this.shangMenTuiCwbDetailDAO.getShangMenTuiCwbDetailByCwbs(cwbs.substring(0, cwbs.length() - 1));
		List<CwbOrder> clist = cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		for (CwbOrder smtcd : clist) {
			cwborderService.updatePrinttimeState(smtcd,printtime);
			/*cwbDao.saveCwbForPrinttime(smtcd.getCwb(), printtime);
			shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(smtcd.getCwb(), printtime);*/
		}
		model.addAttribute("smtlist", smtlist);
		return "/cwborder/selectForJiaYouGouWuPrint";
	}

	// 乐蜂网模板
	@RequestMapping("/selectforlefengwang")
	private String selectforlefengwang(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}
		List<CwbOrder> clist = this.cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		List<Customer> list = this.customerDao.getAllCustomers();
		List<User> userlist = this.userDAO.getAllUser();
		// 保存打印时间
		List<lefengVo> alist = new ArrayList<lefengVo>();
		for (CwbOrder c : clist) {
			for (User u : userlist) {
				if (c.getDeliverid() == u.getUserid()) {
					lefengVo vo = new lefengVo();
					vo.setCwb(c.getCwb());
					vo.setDeliveryname(u.getRealname());
					alist.add(vo);
				}
			}
		}
		model.addAttribute("Vo", alist);
		model.addAttribute("printtime", printtime);
		model.addAttribute("clist", clist);
		model.addAttribute("customer", list);
		return "/cwborder/selectforlefengwangPrint";
	}

	@RequestMapping("/selectforgomeprint")
	public String selectforgomeprint(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}
		List<CwbOrder> clist = this.cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		for (CwbOrder c : clist) {
			cwborderService.updatePrinttimeState(c,printtime);
			/*cwbDao.saveCwbForPrinttime(c.getCwb(), printtime);
			shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(c.getCwb(), printtime);*/
		}
		List<Customer> customerlist = this.customerDao.getAllCustomers();

		SystemInstall companyName = this.systemInstallDAO.getSystemInstallByName("CompanyName");
		if (companyName != null) {
			model.addAttribute("companyName", companyName.getValue());
		} else {
			model.addAttribute("companyName", "易普联科");
		}
		model.addAttribute("clist", clist);
		model.addAttribute("branchList", branchList);
		model.addAttribute("customerlist", customerlist);
		SystemInstall isShowRemark = this.systemInstallDAO.getSystemInstall("isShowRemark");
		if ((isShowRemark != null) && !isShowRemark.getValue().equals("no")) {
			return "cwborder/selectforgomeprintincluderemark";
		} else {
			return "cwborder/selectforgomeprint";
		}
	}

	@RequestMapping("/selectforvipprint")
	public String selectforvip(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}
		List<CwbOrder> clist = this.cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		List<OrderGoods> orderGoods = null;
		Map<String, List<OrderGoods>> mapOrderGoods = new HashMap<String, List<OrderGoods>>();
		for (CwbOrder c : clist) {
			orderGoods = this.orderGoodsDAO.getOrderGoodsList(c.getCwb());
			mapOrderGoods.put(c.getCwb(), orderGoods);
			cwborderService.updatePrinttimeState(c,printtime);
			/*cwbDao.saveCwbForPrinttime(c.getCwb(), printtime);
			shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(c.getCwb(), printtime);*/
		}
		List<Customer> customerlist = this.customerDao.getAllCustomers();

		SystemInstall companyName = this.systemInstallDAO.getSystemInstallByName("CompanyName");
		if (companyName != null) {
			model.addAttribute("companyName", companyName.getValue());
		} else {
			model.addAttribute("companyName", "易普联科");
		}
		model.addAttribute("mapOrderGoods", mapOrderGoods);
		model.addAttribute("clist", clist);
		model.addAttribute("branchList", branchList);
		model.addAttribute("customerlist", customerlist);

		return "cwborder/selectforvipprint";

	}

	@RequestMapping("/geteditBranch")
	public String editBatchBranch(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs, @RequestParam(value = "excelbranch", required = false, defaultValue = "") String excelbranch, @RequestParam(value = "branchcode", required = false, defaultValue = "") String branchcode, @RequestParam(value = "onePageNumber", defaultValue = "10", required = false) long onePageNumber, // 每页记录数
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 是否显示
	) throws Exception {
		int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		List<CwbOrder> cwborderList = new ArrayList<CwbOrder>();
		Page pageobj = new Page();
		long count = 0;
		if (isshow != 0) {
			List<Branch> lb = new ArrayList<Branch>();
			List<Branch> branchnamelist = this.branchDAO.getBranchByBranchnameCheck(branchcode.length() > 0 ? branchcode : excelbranch);
			if (branchnamelist.size() > 0) {
				lb = branchnamelist;
			} else {
				lb = this.branchDAO.getBranchByBranchcode(branchcode.length() > 0 ? branchcode : excelbranch);
			}
			if (lb.size() > 0) {
				String[] cwbstr = cwbs.trim().split("\r\n");
				List<String> cwbStrList = new ArrayList<String>();
				for (int i = 0; i < cwbstr.length; i++) {
					if (cwbstr[i].trim().length() == 0) {
						continue;
					}
					cwbstr[i] = cwbstr[i].trim();
					if (!cwbStrList.contains(cwbstr[i])) {
						cwbStrList.add(cwbstr[i]);
					}
				}
				StringBuffer cwbBuffer = new StringBuffer();
				String cwbstrs = "";
				for (int i = 0; i < cwbStrList.size(); i++) {
					cwbBuffer = cwbBuffer.append("'" + cwbStrList.get(i) + "',");
				}
				if (cwbBuffer.length() > 0) {
					cwbstrs = cwbBuffer.toString().substring(0, cwbBuffer.length() - 1);
				}
				List<CwbOrder> oList = this.cwbDao.getCwbByCwbs(cwbstrs);
				if ((oList != null) && (oList.size() > 0)) {
					for (CwbOrder cwbOrder : oList) {
						CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
						if ((cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()) || (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai
								.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
												// 都将匹配状态变更为修改
							addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
						} else if ((cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()) || (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong
								.getValue())) {// 如果修改的数据原来是为匹配的
												// 或者是人工匹配的
												// 都将匹配状态变更为人工修改
							addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
						}
						try {
							this.cwborderService.updateDeliveryBranch(this.getSessionUser(), cwbOrder, lb.get(0), addressCodeEditType);
							count += 1;
						} catch (CwbException ce) {
							model.addAttribute("error", "匹配失败，" + ce.getMessage() + "!");
						}
					}
				}
				cwborderList = this.cwbDao.getCwbByCwbs(cwbstrs);
				pageobj = new Page(count, page, onePageNumber);
			} else {
				model.addAttribute("branchs", this.branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));
				model.addAttribute("Order", cwborderList);
				model.addAttribute("page_obj", pageobj);
				model.addAttribute("page", page);
				model.addAttribute("msg", "1");
				model.addAttribute("count", count);
				return "cwborder/editBranch";
			}

		}
		model.addAttribute("branchs", this.branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));
		model.addAttribute("Order", cwborderList);
		model.addAttribute("page_obj", pageobj);
		model.addAttribute("page", page);
		model.addAttribute("count", count);
		return "cwborder/editBranch";
	}

	/*
	 * @RequestMapping("/editBranch/{cwb}") public @ResponseBody String
	 * editBranch(@PathVariable("cwb") String
	 * cwb,@RequestParam(value="branchid",defaultValue="0") long branchid){
	 * Branch branch = branchDAO.getBranchById(branchid); if (branch==null) {
	 * return "{\"errorCode\":1,\"error\":\"没有找到指定站点\"}"; }
	 *
	 * try { CwbOrder co = cwbDao.getCwbByCwb(cwb);
	 * CwbOrderAddressCodeEditTypeEnum addressCodeEditType =
	 * CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
	 * if(co.getAddresscodeedittype()==
	 * CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()
	 * ||co.getAddresscodeedittype
	 * ()==CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue
	 * ()){//如果修改的数据原来是地址库匹配的或者是后来修改的 都将匹配状态变更为修改 addressCodeEditType =
	 * CwbOrderAddressCodeEditTypeEnum.XiuGai; }else
	 * if(co.getAddresscodeedittype
	 * ()==CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()
	 * ||co.getAddresscodeedittype
	 * ()==CwbOrderAddressCodeEditTypeEnum.RenGong.getValue()){//如果修改的数据原来是为匹配的
	 * 或者是人工匹配的 都将匹配状态变更为人工修改 addressCodeEditType =
	 * CwbOrderAddressCodeEditTypeEnum.RenGong; } try{
	 * cwborderService.updateDeliveryBranch(getSessionUser(),co,
	 * branch,addressCodeEditType); }catch (CwbException ce) { return
	 * "{\"errorCode\":"
	 * +ce.getError().getValue()+",\"error\":\"匹配失败，"+ce.getMessage()+"!\"}"; }
	 * } catch (Exception e) { return "{\"errorCode\":1,\"error\":\"匹配失败!\"}"; }
	 * return
	 * "{\"errorCode\":0,\"error\":\"操作成功\",\"branchid\":\""+branch.getBranchid
	 * ()+"\",\"excelbranch\":\""+branch.getBranchname()+"\"}"; }
	 */
	@RequestMapping("/daocuohuolist/{page}")
	public String daocuohuolist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid) {
		model.addAttribute("cwborderList", this.cwbDao.getcwbOrderByWherePage(page, branchid));
		model.addAttribute("page_obj", new Page(this.cwbDao.getcwborderByWherePageCount(branchid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		List<Branch> bList = this.getNextPossibleBranches();
		List<Branch> branchAllList = this.branchDAO.getAllEffectBranches();
		model.addAttribute("branchAllList", branchAllList);
		model.addAttribute("branchList", bList);
		return "/cwborder/daocuohuolist";
	}

	private List<Branch> getNextPossibleBranches() {
		List<Branch> bList = new ArrayList<Branch>();
		for (long i : this.cwbRouteService.getNextPossibleBranch(this.getSessionUser().getBranchid())) {
			bList.add(this.branchDAO.getBranchByBranchid(i));
		}
		return bList;
	}

	@RequestMapping("/daocuohuobeizhupage/{cwb}")
	public String daocuohuobeizhupage(Model model, @PathVariable("cwb") String cwb) {
		model.addAttribute("cwborder", this.cwbDao.getCwbByCwb(cwb));
		return "/cwborder/daocuohuobeizhu";
	}

	@RequestMapping("/daocuohuobeizhu/{cwb}")
	public @ResponseBody String daocuohuobeizhu(@PathVariable("cwb") String cwb, @RequestParam(value = "comment", required = false, defaultValue = "") String comment) {
		try {
			String scancwb = cwb;
			cwb = this.cwborderService.translateCwb(cwb);
			this.cwborderService.handleDaocuohuo(this.getSessionUser(), cwb, scancwb, comment);
			return "{\"errorCode\":0,\"error\":\"操作成功\",\"cwb\":\"" + cwb + "\",\"comment\":\"" + comment + "\"}";
		} catch (Exception e) {
			return "{\"errorCode\":0,\"error\":\"" + e.getMessage() + "\",\"cwb\":\"" + cwb + "\",\"comment\":\"" + comment + "\"}";
		}

	}

	/**
	 * 查询当前
	 *
	 * @param model
	 * @param listType
	 *            查询类型 对应CwbOrderListTypeEnum
	 * @param page
	 *            当前页
	 * @param branchid
	 *            操作的机构
	 * @return
	 */
	@RequestMapping("/detaillist/{listType}/{page}")
	public String detailList(Model model, @PathVariable("listType") long listType, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid) {
		if (listType == CwbOrderListTypeEnum.ZHAN_DIAN_JIAN_KONG_ZAI_TU.getValue()) {

			model.addAttribute("cwborderList", this.cwbDao.getCwbOrderByNextBranchidAndFlowordertypeToPage(page, branchid, FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.ChuKuSaoMiao
					.getValue() + "," + FlowOrderTypeEnum.TuiHuoChuZhan.getValue()));
			model.addAttribute("page_obj", new Page(this.cwbDao.getCwbOrderByNextBranchidAndFlowordertypeCount(branchid, FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.ChuKuSaoMiao
					.getValue() + "," + FlowOrderTypeEnum.TuiHuoChuZhan.getValue()), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);

		} else if (listType == CwbOrderListTypeEnum.ZHAN_DIAN_JIAN_KONG_KU_CUN.getValue()) {
			model.addAttribute("cwborderList", this.cwbDao.getCwbOrderByCurrentbranchidAndFlowordertypeToPage(page, branchid));
			model.addAttribute("page_obj", new Page(this.cwbDao.getCwbOrderByCurrentbranchidAndFlowordertypeCount(branchid), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);

		} else if (listType == CwbOrderListTypeEnum.ZHAN_DIAN_JIAN_KONG_PAI_SONG_ZHONG.getValue()) {

			model.addAttribute("cwborderList", this.cwbDao.getCwbOrderByDeliverybranchidAndDeliverystateToPage(page, branchid, DeliveryStateEnum.WeiFanKui.getValue()));
			model.addAttribute("page_obj", new Page(this.cwbDao.getCwbOrderByDeliverybranchidAndDeliverystateCount(branchid, DeliveryStateEnum.WeiFanKui.getValue()), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		}

		model.addAttribute("customerMap", this.customerDao.getAllCustomersToMap());

		Map<Long, CustomWareHouse> customerWarehouseMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse customWareHouse : this.customWareHouseDAO.getAllCustomWareHouse()) {
			customerWarehouseMap.put(customWareHouse.getWarehouseid(), customWareHouse);
		}
		model.addAttribute("customerWarehouseMap", customerWarehouseMap);

		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : this.branchDAO.getAllBranches()) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("branchMap", branchMap);

		return "/cwborder/detaillist";
	}

	// ---------------------修改订单状态-------------------

	@RequestMapping("/editCwbstateBatch/{page}")
	public String editCwbstateBatch(Model model, HttpServletRequest request, @PathVariable("page") long page, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs, @RequestParam(value = "cwbstate", defaultValue = "-1", required = false) int cwbstate // 订单状态类型
	) {
		if (cwbs.trim().length() > 0) {
			String[] cwb = cwbs.split("\r\n");

			StringBuffer cwbsBuffer = new StringBuffer();
			for (int i = 0; i < cwb.length; i++) {
				if (cwb[i].trim().length() == 0) {
					continue;
				}
				cwb[i] = cwb[i].trim();
				cwbsBuffer.append("'").append(cwb[i]).append("'");
				if (i < (cwb.length - 1)) {
					cwbsBuffer.append(",");
				}
			}

			if (cwbstate > 0) {// 批量修改订单状态
				for (String cwbStr : cwb) {
					this.cwborderService.updateCwbState(cwbStr, CwbStateEnum.getByValue(cwbstate));
				}
				model.addAttribute("msg", MessageFormat.format("成功修改了{0}单的订单状态修改为{1}", cwb.length, CwbStateEnum.getByValue(cwbstate).getText()));
			}

			List<CwbOrder> cwbOrderList = this.cwbDao.getCwbOrderByCwbs(page, cwbsBuffer.toString());// deliveryStateDAO.getDeliverByCwbs(page,cwbsBuffer.toString());

			List<QuickSelectView> qsvList = new ArrayList<QuickSelectView>();
			for (CwbOrder cwborder : cwbOrderList) {
				QuickSelectView qsv = new QuickSelectView();
				BeanUtils.copyProperties(this.deliveryStateDAO.getDeliveryByCwb(cwborder.getCwb()), qsv);
				BeanUtils.copyProperties(cwborder, qsv);

				qsvList.add(qsv);
			}

			model.addAttribute("qsvList", qsvList);
			model.addAttribute("page_obj", new Page(this.deliveryStateDAO.getDeliverByCwbsCount(cwbsBuffer.toString()), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);

			model.addAttribute("customerMap", this.customerDao.getAllCustomersToMap());
		}

		return "cwborder/editCwbstateBatch";
	}

	@RequestMapping("/geteditCwbstate")
	public String geteditCwbstate(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb, @RequestParam(value = "cwbstate", defaultValue = "0", required = false) long cwbstate // 订单状态类型
	) {
		if (cwb.length() == 0) {
			model.addAttribute("Order", null);
		} else {
			model.addAttribute("Order", this.cwbDao.getCwbByCwb(cwb));
		}

		return "cwborder/editCwbstate";
	}

	@RequestMapping("/editCwbstate/{cwb}")
	public @ResponseBody String editCwbstate(@PathVariable("cwb") String cwb, @RequestParam(value = "cwbstate", required = false) int cwbstate) {
		this.cwborderService.updateCwbState(cwb, CwbStateEnum.getByValue(cwbstate));
		return "{\"errorCode\":0,\"error\":\"操作成功\",\"cwb\":\"" + cwb + "\",\"cwbstate\":\"" + cwbstate + "\"}";
	}

	/**
	 * 审为退货 针对所在机构是库房的不是退货状态的订单
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toTuiHuo")
	public String toTuiHuo(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb// 订单状态类型
	) {
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		String quot = "'", quotAndComma = "',";

		if (cwb.length() > 0) {
			List<String> scancwblist = new ArrayList<String>();

			//拦截新增--刘武强
			List<String> allScanList = new ArrayList<String>();//扫描进入系统的单号集合
			List<String> transCwbList = new ArrayList<String>();//运单号集合
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();//订单集合
			List<TransCwbDetail> transOrderList = new ArrayList<TransCwbDetail>();//运单集合

			StringBuffer cwbs = new StringBuffer();
			for (String temp : cwb.split("\r\n")) {
				String cwbStr = temp.trim();
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbStr = cwbStr.trim();
				allScanList.add(cwbStr);
				String lastcwb = this.cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = this.cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);//找出所有的订单详细信息
				}
			}

			List<TranscwbView> TranscwbViewList = this.cwborderService.getTransCwbsListByOrderNos(allScanList);//根据输入的单号集合，获取订单-运单对应表中的信息
			for (TranscwbView temp : TranscwbViewList) {//把所有的子单号找出来
				if (!transCwbList.contains(temp.getTranscwb())) {
					transCwbList.add(temp.getTranscwb());
				}
			}
			transOrderList = this.cwborderService.getCwbOrderListByCwb(transCwbList);//找出所有子单对应的详细信息
			request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			List<Customer> customerList = this.customerDao.getAllCustomers();
			List<Branch> branchList = this.branchDAO.getAllEffectBranches();
			List<Reason> reasonList = this.reasonDAO.getAllReason();
			//model.addAttribute("cwbList", this.getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList));
			/*
			 * 1、判断是否是子单？主单？普通件？
			 * 2、如果是子单，就去运单详情表查询出子单的信息，并去订单详情表查询主单信息，综合得到子单完整的显示信息；
			 * 	    如果是主单，则查询主单信息，并查询出所有的子单信息，综合主单信息，得到所有子单完整的显示信息
			 * 	    如果是普通件，直接去订单详情表查询订单详情
			 */
			model.addAttribute("cwbList", this.cwborderService.getCwbOrderViewOfIntercept(scancwblist, cwborderlist, customerList, reasonList, transOrderList));
			model.addAttribute("branchList", branchList);
			//model.addAttribute("reasonList", this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue()));
			model.addAttribute("reasonList", this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.Intercept.getValue()));//只显示拦截原因
			model.addAttribute("scanCwbs", cwb.trim());
		}

		return "auditorderstate/toTuiHuo";
	}

	/**
	 * 审为退货 针对所在机构是库房的不是退货状态的订单
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toTuiHuo1")
	public String toTuiHuo1(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb// 订单状态类型
	) {
		String quot = "'", quotAndComma = "',";
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));

		if (cwb.length() > 0) {
			List<String> scancwblist = new ArrayList<String>();
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbStr = cwbStr.trim();
				String lastcwb = this.cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = this.cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);
				}
			}
			request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			List<Customer> customerList = this.customerDao.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllEffectBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDAO.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("cwbList", this.getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList));
			model.addAttribute("branchList", branchList);
			model.addAttribute("customerList", customerList);
			model.addAttribute("reasonList", this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue()));
		}

		return "auditorderstate/toTuiHuo1";
	}

	/**
	 * 审为退货再投
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toTuiHuoZaiTou/{page}")
	public String toTuiHuoZaiTou(Model model, HttpServletRequest request, @PathVariable(value = "page") long page, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs, @RequestParam(value = "cwbtypeid", defaultValue = "0", required = false) int cwbordertype, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, @RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid, @RequestParam(value = "begindate", defaultValue = "", required = false) String begintime, @RequestParam(value = "enddate", defaultValue = "", required = false) String endtime, @RequestParam(value = "auditstate", defaultValue = "-1", required = false) int auditstate, @RequestParam(value = "isnow", defaultValue = "0", required = false) int isnow) {
		Page pag = new Page();
		List<Branch> branchList = this.branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		List<Branch> branchLists = this.branchDAO.getAllBranches();
		List<Customer> customerList = new ArrayList<Customer>();
		List<Reason> reasonList = new ArrayList<Reason>();
		List<OrderBackRuku> obrsList = new ArrayList<OrderBackRuku>();
		if (isnow > 0) {
			String cwbsStr = "";
			if (cwbs.length() > 0) {
				cwbsStr = this.cwborderService.getCwbs(cwbs);
			}

			customerList = this.customerDao.getAllCustomers();
			reasonList = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.TuiHuoZaiTou.getValue());
			List<OrderBackRuku> obrList = this.orderBackRukuRecordDao.getOrderbackRukus(page, cwbsStr, cwbordertype, customerid, branchid, begintime, endtime, auditstate);
			obrsList = this.cwborderService.getOrderBackRukuRecord(obrList, branchLists, customerList);
			long count = this.orderBackRukuRecordDao.getOrderbackRukusCount(page, cwbsStr, cwbordertype, customerid, branchid, begintime, endtime, auditstate);
			pag = new Page(count, page, Page.ONE_PAGE_NUMBER);
		}
		//List<Branch> branchList = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());
		model.addAttribute("page_obj", pag);
		model.addAttribute("page", page);
		model.addAttribute("obrsList", obrsList);
		model.addAttribute("reasonList", reasonList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("customerList", customerList);
		return "auditorderstate/toTuiHuoZaiTou";
	}

	public List<String> getScancwbList(String cwb) {
		List<String> scancwblist = new ArrayList<String>();
		StringBuffer cwbs = new StringBuffer();
		for (String cwbStr : cwb.split("\r\n")) {
			if (cwbStr.trim().length() == 0) {
				continue;
			}
			cwbStr = cwbStr.trim();
			String lastcwb = this.cwborderService.translateCwb(cwbStr);
			cwbs = cwbs.append("'").append(lastcwb).append("',");
			CwbOrder co = this.cwbDao.getCwbByCwb(lastcwb);
			if (co != null) {
				scancwblist.add(cwbStr.trim());
			}
		}
		return scancwblist;
	}

	/**
	 * 审为退货再投
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/auditTuiHuoZaiTou")
	public @ResponseBody String auditTuiHuoZaiTou(Model model, HttpServletRequest request) {
		this.logger.info("--审为退货再投 开始--");
		String cwbremarks = request.getParameter("cwbremarks");
		if (cwbremarks == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbremarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || (reason.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_reasonid = reason.split("_s_");
			if (cwb_reasonid.length == 2) {
				// TODO 所有订单号均向订单所在负责人发送短信
				try {
					if (!cwb_reasonid[1].equals("0") && !cwb_reasonid[1].equals("")) {
						String scancwb = cwb_reasonid[0];
						this.cwborderService.auditToZaiTou(this.getSessionUser(), cwb_reasonid[0], scancwb, FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue(), Long.valueOf(cwb_reasonid[1]));
						//更改退货站入库记录表审核为退货再投的状态
						Reason rs = this.reasonDAO.getRcontentByReasonid(Integer.parseInt(cwb_reasonid[1]));
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String createtime = sdf.format(new Date());
						this.orderBackRukuRecordDao.updateOrderBackRukuRecort(cwb_reasonid[0], rs.getReasoncontent(), this.getSessionUser().getRealname(), createtime);
						successCount++;
						failureCount--;
					}
					this.logger.info("{} 成功", reason);
				} catch (Exception e) {
					this.logger.error("", e);
					this.logger.error("{} 失败", reason);
				}
			} else {
				this.logger.info("{} 失败，格式不正确", reason);
			}
		}

		return successCount + "_s_" + failureCount;
	}

	/**
	 * @throws Exception
	 *
	 * @Title: auditTuiHuo
	 * @description 拦截保存方法，  1、如果是普通件，那么走原来的逻辑，（订单=主单，运单=子单）
	 * 						 2、如果是一票多件，其子单中有部分为丢失，订单变化走原来逻辑，但是订单状态变为部分丢失，再根据货物流向配置里面下一站确定是哪一个退货组，如果没有配置，则拦截失败；子单中丢失的状态变为丢失，破损的状态变为破损，正常的变为退货
	 * 						 3、如果是一票多件，其子单中有部分为破损，其他都是正常，订单变化走原来逻辑，但是订单状态变为部分破损，再根据货物流向配置里面下一站确定是哪一个退货组，如果没有配置，则拦截失败；子单中破损的状态变为破损，正常的变为退货
	 * 						 4、如果是一票多件，所有子单位丢失/破损，订单变化走原来逻辑，但是订单状态变为部分丢失/破损，再根据货物流向配置里面下一站确定是哪一个退货组，如果没有配置，则拦截失败；所有子单状态变为丢失/破损
	 * @author 刘武强
	 * @date  2016年1月9日下午4:05:44
	 * @param  @param model
	 * @param  @param request
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/auditTuiHuo")
	public @ResponseBody String auditTuiHuo(Model model, HttpServletRequest request) {
		this.logger.info("--审为退货 开始--");
		String reasons = request.getParameter("reasons");
		if (reasons == null) {
			return 0 + "_s_" + 0;
		}

		List<Map<String, String>> cwbsList = new ArrayList<Map<String, String>>();//主单map集合
		List<Map<String, Object>> transList = new ArrayList<Map<String, Object>>();//子单map集合
		List<String> transNoList = new ArrayList<String>();//子单号集合
		List<String> cwbNoList = new ArrayList<String>();//所有子单对应的主单号集合、
		String errMsg = "";
		JSONArray rJson = JSONArray.fromObject(reasons);

		this.cwborderService.dealInterceptForegroundDate(rJson, cwbsList, transList, cwbNoList);//将前台的数据处理一下，便于接下来操作
		List<TranscwbView> TranscwbViewList = this.cwborderService.getTransCwbsListByCwbNos(cwbNoList);//根据输入的主单号集合，获取订单-运单对应表中的信息
		for (TranscwbView temp : TranscwbViewList) {//把所有的子单号找出来
			if (!transNoList.contains(temp.getTranscwb())) {
				transNoList.add(temp.getTranscwb());
			}
		}
		List<TransCwbDetail> transOrderList = this.cwborderService.getCwbOrderListByCwb(transNoList);//找出所有子单对应的详细信息
		List<CwbOrder> cwbOrderList = this.cwborderService.getCwbOrderListByCwbs(cwbNoList);//找出所有子单对应的主单信息

		long successCount = 0;
		long failureCount = cwbsList.size() + cwbOrderList.size();
		for (int i = 0; i < cwbsList.size(); i++) {//普通件，按原有逻辑走，不用修改
			Map<String, String> map = cwbsList.get(i);
			try {
				String scancwb = map.get("cwb");
				this.cwborderService.auditToTuihuo(this.getSessionUser(), scancwb, scancwb, FlowOrderTypeEnum.DingDanLanJie.getValue(), Long.valueOf(map.get("reasonid")));
				successCount++;
				failureCount--;
			} catch (Exception e) {
				this.logger.error("", e);
				errMsg = "订单号：" + map.get("cwb") + ",失败原因：" + e.getMessage() + "\n";
			}
		}

		//处理一票多件
		//修改子单的运单状态、操作状态、下一站、拦截原因，修改主单的订单状态、操作状态、下一站
		for (CwbOrder cwbTemp : cwbOrderList) {
			try {
				this.cwborderService.dealMpsTrans(cwbTemp, transOrderList, transList, cwbOrderList);
				successCount++;
				failureCount--;
			} catch (Exception e) {
				this.logger.error("", e);
				errMsg = "订单号：" + cwbTemp.getCwb() + ",失败原因：" + e.getMessage() + "\n";
			}
		}

		return successCount + "_s_" + failureCount + "_s_" + errMsg;
	}

	/**
	 * 审为退供货商
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toTuiGongHuoShang")
	public String toTuiGongHuoShang(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		String quot = "'", quotAndComma = "',";
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));

		if (cwb.length() > 0) {
			List<String> scancwblist = new ArrayList<String>();
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbStr = cwbStr.trim();
				String lastcwb = this.cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = this.cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);
				}
			}
			request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			List<Customer> customerList = this.customerDao.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllEffectBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDAO.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("cwbList", this.getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList));
			model.addAttribute("branchList", branchList);
			model.addAttribute("customerList", this.customerDao.getAllCustomers());
		}

		return "auditorderstate/toTuiGongHuoShang";
	}

	/**
	 * 审为供货商确认退货
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toTuiGongHuoShangSuccess/{page}")
	public String toTuiGongHuoShangSuccess(Model model, HttpServletRequest request, @PathVariable(value = "page") long page, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb, @RequestParam(value = "cwbtypeid", defaultValue = "0", required = false) int cwbtypeid, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, @RequestParam(value = "shenhestate", defaultValue = "-1", required = false) long shenhestate, @RequestParam(value = "begindate", defaultValue = "", required = false) String begindate, @RequestParam(value = "enddate", defaultValue = "", required = false) String enddate, @RequestParam(value = "isnow", defaultValue = "0", required = false) int isnow) {

		TuiGongHuoShangPage pag = new TuiGongHuoShangPage();
		//List<Branch> branchList = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());
		List<Customer> customerList = this.customerDao.getAllCustomers();
		//model.addAttribute("branchList", branchList);

		String cwbss = "";
		if (cwb.length() > 0) {
			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbStr = cwbStr.trim();
				cwbs = cwbs.append("'").append(cwbStr).append("',");
			}
			cwbss = cwbs.substring(0, cwbs.length() - 1);
		}
		List<OrderbackRecord> orList = new ArrayList<OrderbackRecord>();
		List<CwbOrderView> covList = new ArrayList<CwbOrderView>();
		if (isnow > 0) {
			if (!(cwb.equals("") && begindate.equals(""))) {
				orList = this.orderbackRecordDao.getCwbOrdersByCwbspage(page, cwbss, cwbtypeid, customerid, shenhestate, begindate, enddate);
				long count = this.orderbackRecordDao.getCwbOrdersByCwbsCount(cwbss, cwbtypeid, customerid, shenhestate, begindate, enddate);
//				pag = new Page(count, page, Page.ONE_PAGE_NUMBER);//原来pagesize为10
				pag = new TuiGongHuoShangPage(count, page, TuiGongHuoShangPage.ONE_PAGE_NUMBER);//新需求修改为100

				StringBuffer sb = new StringBuffer();
				if (orList.size() > 0) {
					for (OrderbackRecord ot : orList) {
						sb.append("'").append(ot.getCwb()).append("',");
					}
				}
				String strs = "";
				List<CwbOrder> coList = new ArrayList<CwbOrder>();
				if (sb.length() > 0) {
					strs = sb.substring(0, sb.length() - 1);
					coList = this.cwbDao.getListbyCwbs(strs);
				}

				covList = this.cwborderService.getTuigongSuccessCwbOrderView(coList, orList, customerList);//获取分页查询的view
			}
		}
		pag.setCurrentPageRows(covList.size());
		model.addAttribute("page", page);
		model.addAttribute("page_obj", pag);
		model.addAttribute("customerList", customerList);
		model.addAttribute("cwbList", covList);
		return "auditorderstate/toTuiGongHuoShangSuccess";
	}

	//根据查询条件~~~~lx
	public Map<List<String>, String> getScanCwbs(List<CwbOrder> oflist) {
		Map<List<String>, String> map = new HashMap<List<String>, String>();
		StringBuffer sb = new StringBuffer("");
		StringBuffer sb2 = new StringBuffer();
		for (CwbOrder co : oflist) {
			sb.append("'").append(co.getCwb()).append("',");
		}
		List<String> scancwblist = new ArrayList<String>();
		for (String cwbStr : sb.substring(0, sb.length() - 1).split(",")) {
			if (cwbStr.trim().length() == 0) {
				continue;
			}
			String lastcwb = this.cwborderService.translateCwb(cwbStr);
			sb2 = sb2.append("'").append(lastcwb).append("',");
			CwbOrder co = this.cwbDao.getCwbByCwb(lastcwb);
			if (co != null) {
				scancwblist.add(cwbStr);
			}
		}

		String str = sb2.toString().substring(0, sb2.length() - 1);
		map.put(scancwblist, str);

		return map;
	}

	public String getEnumtextByEnumvalue(int value) {
		for (CwbOrderTypeIdEnum cotie : CwbOrderTypeIdEnum.values()) {
			if (cotie.getValue() == value) {
				return cotie.getText();
			}
		}
		return null;
	}

	/**
	 * 进入包裹未到页面
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toBaoGuoWeiDao")
	public String toBaoGuoWeiDao(Model model) {
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		return "auditorderstate/toBaoGuoWeiDao";
	}

	@RequestMapping("/toBaoGuoWeiDaoCommit")
	public @ResponseBody String toBaoGuoWeiDaoCommit(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {

		JSONObject o = new JSONObject();
		String msg = "";
		int succ = 0;
		int error = 0;
		if (cwb.length() > 0) {
			List<Customer> cList = this.customerDao.getAllCustomers();
			for (String cwbStr : cwb.split("\r\n")) {
				cwbStr = cwbStr.trim();
				CwbOrder co = this.cwbDao.getCwbByCwb(cwbStr);
				if (co != null) {
					Customer customer = this.getCustomer(co.getCustomerid(), cList);
					if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Amazon.getKey()))) {
						OrderFlow of = this.orderFlowDAO.getOrderFlowByIsnow(cwbStr);
						of.setFlowordertype(FlowOrderTypeEnum.BaoGuoweiDao.getValue());
						of.setCredate(new Date());
						String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
						String csremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + this.getSessionUser().getRealname() + ":标记[包裹未到]";
						try {
							this.cwbDao.updateCwbRemark(cwbStr, csremark);
							this.cwborderService.send(of);
							succ++;
						} catch (Exception e) {
							error++;
							String msg1 = "备注太长";
							msg += "<p>订单号：[" + cwbStr.trim() + "]:<font color='red'>" + msg1 + "</font></p>";
						}
					} else {
						error++;
						String msg1 = "不是亚马逊（EDI）的订单";
						msg += "<p>订单号：[" + cwbStr.trim() + "]:<font color='red'>" + msg1 + "</font></p>";
					}
				} else {
					String msg1 = "单号不存在！";
					msg += "<p>订单号：[" + cwbStr.trim() + "]:<font color='red'>" + msg1 + "</font></p>";
				}
			}
		}
		o.put("sussesCount", succ);
		o.put("errorCount", error);
		o.put("errorMsg", msg);
		return o.toString();
	}

	@RequestMapping("/toZhongzhuanYanwuCommit")
	public @ResponseBody String toZhongzhuanYanwuCommit(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {

		JSONObject o = new JSONObject();
		String msg = "";
		int succ = 0;
		int error = 0;
		if (cwb.length() > 0) {
			List<Customer> cList = this.customerDao.getAllCustomers();
			for (String cwbStr : cwb.split("\r\n")) {
				cwbStr = cwbStr.trim();
				CwbOrder co = this.cwbDao.getCwbByCwb(cwbStr);
				if (co != null) {
					Customer customer = this.getCustomer(co.getCustomerid(), cList);
					if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Amazon.getKey()))) {
						OrderFlow of = this.orderFlowDAO.getOrderFlowByIsnow(cwbStr);
						of.setFlowordertype(FlowOrderTypeEnum.ZhongZhuanyanwu.getValue());
						of.setCredate(new Date());
						String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
						String csremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + this.getSessionUser().getRealname() + ":标记[中转延误]";
						try {
							this.cwbDao.updateCwbRemark(cwbStr, csremark);
							this.cwborderService.send(of);
							succ++;
						} catch (Exception e) {
							error++;
							String msg1 = "备注太长";
							msg += "<p>订单号：[" + cwbStr.trim() + "]:<font color='red'>" + msg1 + "</font></p>";
						}
					} else {
						error++;
						String msg1 = "不是亚马逊（EDI）的订单";
						msg += "<p>订单号：[" + cwbStr.trim() + "]:<font color='red'>" + msg1 + "</font></p>";
					}
				} else {
					String msg1 = "单号不存在！";
					msg += "<p>订单号：[" + cwbStr.trim() + "]:<font color='red'>" + msg1 + "</font></p>";
				}
			}
		}
		o.put("sussesCount", succ);
		o.put("errorCount", error);
		o.put("errorMsg", msg);
		return o.toString();
	}

	@RequestMapping("/toHuowuDiushiCommit")
	public @ResponseBody String toHuowuDiushiCommit(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {

		JSONObject o = new JSONObject();
		String msg = "";
		int succ = 0;
		int error = 0;
		if (cwb.length() > 0) {
			List<Customer> cList = this.customerDao.getAllCustomers();
			for (String cwbStr : cwb.split("\r\n")) {
				cwbStr = cwbStr.trim();
				CwbOrder co = this.cwbDao.getCwbByCwb(cwbStr);
				if (co != null) {
					Customer customer = this.getCustomer(co.getCustomerid(), cList);
					if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Amazon.getKey()))) {
						OrderFlow of = this.orderFlowDAO.getOrderFlowByIsnow(cwbStr);
						of.setFlowordertype(FlowOrderTypeEnum.ShouGongdiushi.getValue());
						of.setCredate(new Date());
						String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
						String csremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + this.getSessionUser().getRealname() + ":标记[货物丢失]";
						try {
							this.cwbDao.updateCwbRemark(cwbStr, csremark);
							this.cwborderService.send(of);
							succ++;
						} catch (Exception e) {
							error++;
							String msg1 = "备注太长";
							msg += "<p>订单号：[" + cwbStr.trim() + "]:<font color='red'>" + msg1 + "</font></p>";
						}
					} else {
						error++;
						String msg1 = "不是亚马逊（EDI）的订单";
						msg += "<p>订单号：[" + cwbStr.trim() + "]:<font color='red'>" + msg1 + "</font></p>";
					}
				} else {
					String msg1 = "单号不存在！";
					msg += "<p>订单号：[" + cwbStr.trim() + "]:<font color='red'>" + msg1 + "</font></p>";
				}
			}
		}
		o.put("sussesCount", succ);
		o.put("errorCount", error);
		o.put("errorMsg", msg);
		return o.toString();
	}

	private Customer getCustomer(long customerid, List<Customer> cList) {
		if ((cList != null) && (cList.size() > 0)) {
			for (Customer customer : cList) {
				if (customer.getCustomerid() == customerid) {
					return customer;
				}

			}
		}
		return null;
	}

	/**
	 * 订单处理
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toEnd")
	public String toEnd(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		String quot = "'", quotAndComma = "',";

		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));

		if (cwb.length() > 0) {
			List<String> scancwblist = new ArrayList<String>();
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbStr = cwbStr.trim();
				String lastcwb = this.cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = this.cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);
				}
			}
			request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			List<Customer> customerList = this.customerDao.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllEffectBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDAO.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("cwbList", this.getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList));
			model.addAttribute("branchList", branchList);
			model.addAttribute("customerList", this.customerDao.getAllCustomers());
			model.addAttribute("userallList", this.userDAO.getAllUser());
		}

		return "auditorderstate/toEnd";
	}

	/**
	 * 审为退供货商
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/auditTuiGongHuoShang")
	public @ResponseBody String auditTuiGongHuoShang(Model model, HttpServletRequest request) {
		this.logger.info("--审为退供货商 开始--");
		String cwbremarks = request.getParameter("cwbs");
		if (cwbremarks == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbremarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || (reason.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_reasonid = reason.split("_s_");
			if (cwb_reasonid.length == 2) {
				// TODO 所有订单号均向订单所在负责人发送短信
				try {
					if (!cwb_reasonid[1].equals("0")) {
						String cwb = this.cwborderService.translateCwb(cwb_reasonid[0]);
						this.cwborderService.updateCwbState(cwb, CwbStateEnum.TuiGongYingShang);
						successCount++;
						failureCount--;
					}
					this.logger.info("{} 成功", reason);
				} catch (Exception e) {
					this.logger.error("", e);
					this.logger.error("{} 失败", reason);
				}
			} else {
				this.logger.info("{} 失败，格式不正确", reason);
			}
		}

		return successCount + "_s_" + failureCount;
	}

	/**
	 * 审为供货商拒收退货
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/auditGongHuoShangJuTui")
	public @ResponseBody String auditGongHuoShangJuTui(Model model, HttpServletRequest request) {
		this.logger.info("--审为审为供货商拒收退货开始--");
		String cwbremarks = request.getParameter("cwbs");
		if (cwbremarks == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbremarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || (reason.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_reasonid = reason.split("_s_");
			if (cwb_reasonid.length == 2) {
				// TODO 所有订单号均向订单所在负责人发送短信
				try {
					if (!cwb_reasonid[1].equals("0")) {
						String scancwb = cwb_reasonid[0];
						this.cwborderService.customrefuseback(this.getSessionUser(), cwb_reasonid[0], scancwb, 0, "");
						successCount++;
						failureCount--;
					}
					this.logger.info("{} 成功", reason);
				} catch (Exception e) {
					this.logger.error("", e);
					this.logger.error("{} 失败", reason);
				}
			} else {
				this.logger.info("{} 失败，格式不正确", reason);
			}
		}

		return successCount + "_s_" + failureCount;
	}

	/**
	 * 审为供货商确认退货
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/auditTuiGongHuoShangSuccess")
	public @ResponseBody String auditTuiGongHuoShangSuccess(Model model, HttpServletRequest request) {
		this.logger.info("--审为供货商确认退货 开始--");
		String cwbremarks = request.getParameter("cwbs");
		if (cwbremarks == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbremarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("")) {
				continue;
			}
			if (reason.length() > 0) {
				// TODO 所有订单号均向订单所在负责人发送短信
				String scancwb = reason;
				this.cwborderService.supplierBackSuccess(this.getSessionUser(), reason, scancwb, 0);
				String auditname = this.getSessionUser().getRealname();//确认人
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String audittime = sdf.format(new Date());
				this.orderbackRecordDao.updateShenheState(1, reason, auditname, audittime);//修改成退供货商成功shenhestate为1
				successCount++;
				failureCount--;
				this.logger.info("{} 成功", reason);
			}
		}

		return successCount + "_s_" + failureCount;
	}

	/**
	 * 审为退客户拒收
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/auditTuiGongHuoShangFailure")
	public @ResponseBody String auditTuiGongHuoShangFailure(Model model, HttpServletRequest request) {
		this.logger.info("--审为供货商退货拒收 开始--");
		String cwbremarks = request.getParameter("cwbs");
		if (cwbremarks == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbremarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("")) {
				continue;
			}
			if (reason.length()>0) {
				String scancwb = reason;
//				cwbDao.updateFlowordertype(FlowOrderTypeEnum.GongYingShangJuShouTuiHuo.getValue(),reason);
//				cwborderService.customrefuseback(user, cwb, scancwb, requestbatchno, comment);
//				requestbatchno参数在customrefuseback方法中没有任何操作,为保证兼容性不修改此方法签名。
//				comment是记录内容,对应表express_ops_order_flow中的commont字段。
				cwborderService.customrefuseback(getSessionUser(), reason, scancwb, 0, "已审核为供货商退货拒收");
				String auditname = getSessionUser().getRealname();//确认人
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String audittime = sdf.format(new Date());
				this.orderbackRecordDao.updateShenheState(2, reason, auditname, audittime);//退货拒收修改shenhestate为2
				successCount++;
				failureCount--;
				this.logger.info("{} 成功", reason);
			}
		}
		return successCount + "_s_" + failureCount;
	}

	/**
	 * 异常订单处理功能
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/auditEnd")
	public @ResponseBody String auditEnd(Model model, HttpServletRequest request) {
		this.logger.info("--异常订单处理功能 开始--");
		String cwbdetails = request.getParameter("cwbdetails");
		if (cwbdetails == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbdetails);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || (reason.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_reasonid = reason.split("_s_");
			if (cwb_reasonid.length == 4) {
				// TODO 所有订单号均向订单所在负责人发送短信
				try {
					if (!cwb_reasonid[1].equals("-1") && !cwb_reasonid[2].equals("-1") && !cwb_reasonid[3].replaceAll("@", "").equals("")) {
						String scancwb = cwb_reasonid[0];
						this.cwborderService.SpecialCwbHandle(this.getSessionUser(), cwb_reasonid[0], scancwb, Long.parseLong(cwb_reasonid[1]), Long.parseLong(cwb_reasonid[2]), cwb_reasonid[3]
								.replaceAll("@", ""), FlowOrderTypeEnum.YiChangDingDanChuLi.getValue());
						successCount++;
						failureCount--;
					}
					this.logger.info("{} 成功", reason);
				} catch (Exception e) {
					this.logger.error("", e);
					this.logger.error("{} 失败", reason);
				}
			} else {
				this.logger.info("{} 失败，格式不正确", reason);
			}
		}

		return successCount + "_s_" + failureCount;
	}

	/**
	 * 订单批量失效
	 *
	 * @param model
	 * @param request
	 * @param cwbs
	 * @return
	 */
	@RequestMapping("/losecwbBatch")
	public String losecwbBatch(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs, @RequestParam(value = "loseeffect", required = false, defaultValue = "-1") long loseeffect) {
		List<Customer> cList = this.customerDao.getAllCustomers();// 获取供货商列表
		List<JSONObject> objList = new ArrayList<JSONObject>();
		long successCount = 0;
		long failCount = 0;
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			JSONObject obj = new JSONObject();
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder co = this.cwbDao.getCwbByCwbLock(cwb);
				if (co == null) {
					throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
				}
				SystemInstall systemInstall = this.systemInstallDAO.getSystemInstall("daohuoiseffect");
				if (systemInstall != null) {
					if (systemInstall.getValue().equals("1")) {
						if (co.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
							List<OrderFlow> orderFlows = this.orderFlowDAO.getOrderFlowByCwbAndFlowordertype(FlowOrderTypeEnum.RuKu.getValue(), cwb);
							if (orderFlows.size() > 1) {
								throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.FeiDiYiCiRuKuBuNengShiXiao);
							}
						}
						if ((co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (co.getFlowordertype() != FlowOrderTypeEnum.RuKu.getValue())) {
							throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.FeiDaorushujuandrukunotallowshixiao);
						}
					} else {
						if (co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
							throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.FeiDaorushujunotallowshixiao);

						}
					}
				} else {
					if (co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
						throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.FeiDaorushujunotallowshixiao);

					}
				}
				/*	if (orderFlowDAO.getOrderFlowByCwbAndFlowordertype(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), cwb).size() > 0) {
						throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.Fen_Zhan_Dao_Huo_Ding_Dan_Bu_Yun_Xu_Shi_Xiao);
					}*/
				this.cwbDao.dataLoseByCwb(cwb);
				this.exportwarhousesummaryDAO.dataLoseByCwb(cwb);
				this.exportwarhousesummaryDAO.LoseintowarhouseByCwb(cwb);
				this.transCwbDao.deleteTranscwb(cwb);
				
				//Added by leoliao at 2016-07-21 订单失效时删除运单明细和运单轨迹
				this.transCwbDetailDAO.deleteByCwb(cwb);
				this.transcwbOrderFlowDAO.deleteByCwb(cwb);
				//Added end
				
				// 失效订单删除
				this.cwborderService.deletecwb(cwb);
				// 删除倒车时间表的订单
				this.orderArriveTimeDAO.deleteOrderArriveTimeByCwb(cwb);
				// 删除审核为退货再投的订单
				this.orderBackCheckDAO.deleteOrderBackCheckByCwb(cwb);

				if (this.emaildateDao.getEmailDateById(co.getEmaildateid()) != null) {
					long cwbcount = this.emaildateDao.getEmailDateById(co.getEmaildateid()).getCwbcount() - 1;
					this.emaildateDao.editEditEmaildateForCwbcount(cwbcount, co.getEmaildateid());
				}
				Date disableDate = new Date() ;
				// add by bruce shangguan 20160412 订单失效，添加应付甲方调整记录
				this.orderPayChangeService.disabledOrder(co, disableDate);
				// end 20160412 
				this.shiXiaoDAO.creAbnormalOrdernew(co.getOpscwbid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(disableDate), co.getCurrentbranchid(), co.getCustomerid(), cwb, co
						.getDeliverybranchid(), co.getFlowordertype(), co.getNextbranchid(), co.getStartbranchid(), this.getSessionUser().getUserid(), loseeffect, co.getCwbstate(), co.getEmaildate());

				//买单结算的客户订单失效需要判断是否已经生成客户账单，如果生成了客户账单，要生成客户调整账单
				this.adjustmentRecordService.createAdjustmentForLosecwbBatch(co);

                //added by Steve PENG. 失效订单需要进行派费相关操作。 start
                //注释掉因为手动失效订单不需要执行相关的派费的操作。所有失效操作只在接口完成。
//                dfFeeService.saveFeeRelativeAfterOrderResetOrDisabled(co, getSessionUser(), true);
                //added by Steve PENG. 失效订单需要进行派费相关操作。 end

				successCount++;
				obj.put("cwbOrder", JSONObject.fromObject(co));
				obj.put("errorcode", "000000");
				for (Customer c : cList) {
					if (c.getCustomerid() == co.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
			} catch (CwbException ce) {// 出现验证错误
				CwbOrder cwbOrder = this.cwbDao.getCwbByCwb(cwb);
				obj.put("cwbOrder", cwbOrder);
				obj.put("errorcode", ce.getError().getValue());
				obj.put("errorinfo", ce.getMessage());
				if (cwbOrder == null) {// 如果无此订单
					obj.put("customername", "");
				} else {
					for (Customer c : cList) {
						if (c.getCustomerid() == cwbOrder.getCustomerid()) {
							obj.put("customername", c.getCustomername());
							break;
						}
					}
				}
				failCount++;

			}
			objList.add(obj);
		}
		model.addAttribute("cwbList", objList);
		model.addAttribute("customerlist", cList);
		model.addAttribute("successCount", successCount);
		model.addAttribute("failCount", failCount);
		List<Reason> shixiaoReasons = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ShiXiaoReason.getValue());
		model.addAttribute("shixiaoreasons", shixiaoReasons);
		return "/cwborder/losecwbBatch";
	}

	/**
	 * 订单失效查询
	 *
	 * @param model
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param cwbs
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectlosecwb/{page}")
	public String selectlosecwb(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbstate", required = false, defaultValue = "-1") long cwbstate, @RequestParam(value = "flowordertype", required = false, defaultValue = "-1") long flowordertype, @RequestParam(value = "userid", required = false, defaultValue = "-1") long userid, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "isnow", required = false, defaultValue = "0") long isnow) {
		String customerids = "";
		Page pageparm = new Page();
		List<ShiXiao> shixiaoList = new ArrayList<ShiXiao>();
		if (isnow == 1) {

			String lastcwbs = "";
			StringBuffer str = new StringBuffer();
			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				} else {
					cwb = cwb.trim();
					str.append("'" + cwb + "',");
				}
			}
			if (str.length() > 0) {
				lastcwbs = str.substring(0, str.length() - 1).toString();
			}
			customerids = this.cwborderService.getStrings(customerid);
			shixiaoList = this.shiXiaoDAO.getShiXiaoByCwbsAndCretime(page, lastcwbs, begindate, enddate, customerids, cwbstate, flowordertype, userid);
			pageparm = new Page(this.shiXiaoDAO.getShiXiaoByCwbsAndCretimeCount(lastcwbs, begindate, enddate, customerids, cwbstate, flowordertype, userid), page, Page.ONE_PAGE_NUMBER);

		}
		//if (begindate.length() > 0 || enddate.length() > 0 || cwbs.length() > 0) {}
		List<String> customeridlist = this.cwborderService.getList(customerid);
		model.addAttribute("customeridlist", customeridlist);
		model.addAttribute("shixiaoList", shixiaoList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("customerList", this.customerDao.getAllCustomers());
		model.addAttribute("userList", this.userDAO.getAllUser());
		model.addAttribute("page", page);
		return "/cwborder/selectlosecwb";
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {
		this.ExportExcelMethod(response, request);
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void ExportExcelMethod(HttpServletResponse response, HttpServletRequest request) {
		String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			final String exportcwbs = request.getSession().getAttribute("exportcwbs").toString();

			final String sql = this.cwbDao.getSQLExportKeFu(exportcwbs);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = CwbOrderController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = CwbOrderController.this.customerDao.getAllCustomersToMap();
					final List<Branch> bList = CwbOrderController.this.branchDAO.getAllBranches();
					final List<Common> commonList = CwbOrderController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = CwbOrderController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = CwbOrderController.this.remarkDAO.getRemarkByCwbs(exportcwbs);
					final Map<String, Map<String, String>> remarkMap = CwbOrderController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = CwbOrderController.this.reasonDAO.getAllReason();
					CwbOrderController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								Object a = CwbOrderController.this.exportService
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
								Map<String, Map<String, String>> orderflowList = CwbOrderController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : CwbOrderController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : CwbOrderController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : CwbOrderController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
					/*
					 * jdbcTemplate.query(new StreamingStatementCreator(sql),
					 * new RowCallbackHandler(){ private int count=0;
					 *
					 * @Override public void processRow(ResultSet rs) throws
					 * SQLException { Row row = sheet.createRow(count + 1);
					 * row.setHeightInPoints((float) 15);
					 *
					 * DeliveryState ds = getDeliveryByCwb(rs.getString("cwb"));
					 * Map<String,String> allTime =
					 * getOrderFlowByCredateForDetailAndExportAllTime
					 * (rs.getString("cwb"));
					 *
					 * for (int i = 0; i < cloumnName4.length; i++) { Cell cell
					 * = row.createCell((short) i); cell.setCellStyle(style);
					 * //sheet.setColumnWidth(i, (short) (5000)); //设置列宽 Object
					 * a = exportService.setObjectA(cloumnName5, rs, i ,
					 * uList,cMap
					 * ,bList,commonList,ds,allTime,cWList,remarkMap,reasonList
					 * ); if(cloumnName6[i].equals("double")){
					 * cell.setCellValue(a == null ?
					 * BigDecimal.ZERO.doubleValue() :
					 * a.equals("")?BigDecimal.ZERO
					 * .doubleValue():Double.parseDouble(a.toString())); }else{
					 * cell.setCellValue(a == null ? "" : a.toString()); } }
					 * count++;
					 *
					 * }});
					 */

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = this.deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	// 给CwbOrderView赋值
	public List<CwbOrderView> getCwbOrderView(List<String> scancwblist, List<CwbOrder> clist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList, List<Branch> branchList, List<User> userList, List<Reason> reasonList, List<Remark> remarkList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if (clist.size() > 0) {
			for (CwbOrder c : clist) {
				int index = clist.indexOf(c);
				CwbOrderView cwbOrderView = new CwbOrderView();

				cwbOrderView.setCarwarehouse(c.getCarwarehouse());
				cwbOrderView.setScancwb(scancwblist.get(index));
				cwbOrderView.setCwb(c.getCwb());
				cwbOrderView.setTranscwb(c.getTranscwb());
				cwbOrderView.setEmaildate(c.getEmaildate());
				cwbOrderView.setCarrealweight(c.getCarrealweight());
				cwbOrderView.setCarsize(c.getCarsize());
				cwbOrderView.setSendcarnum(c.getSendcarnum());
				cwbOrderView.setCwbprovince(c.getCwbprovince());
				cwbOrderView.setCwbcity(c.getCwbcity());
				cwbOrderView.setCwbcounty(c.getCwbcounty());
				cwbOrderView.setConsigneeaddress(c.getConsigneeaddress());
				cwbOrderView.setConsigneename(c.getConsigneename());
				cwbOrderView.setConsigneemobile(c.getConsigneemobile());
				cwbOrderView.setConsigneephone(c.getConsigneephone());
				cwbOrderView.setConsigneepostcode(c.getConsigneepostcode());

				cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
				String customwarehouse = this.getQueryCustomWareHouse(customerWareHouseList, Long.parseLong(c.getCustomerwarehouseid()));
				cwbOrderView.setCustomerwarehousename(customwarehouse);
				cwbOrderView.setInhouse(this.getQueryBranchName(branchList, Integer.parseInt(c.getCarwarehouse() == "" ? "0" : c.getCarwarehouse())));// 入库仓库
				cwbOrderView.setCurrentbranchname(this.getQueryBranchName(branchList, c.getCurrentbranchid()));// 当前所在机构名称
				cwbOrderView.setStartbranchname(this.getQueryBranchName(branchList, c.getStartbranchid()));// 上一站机构名称
				cwbOrderView.setNextbranchname(this.getQueryBranchName(branchList, c.getNextbranchid()));// 下一站机构名称
				cwbOrderView.setDeliverybranch(this.getQueryBranchName(branchList, c.getDeliverybranchid()));// 配送站点
				cwbOrderView.setDelivername(this.getQueryUserName(userList, c.getDeliverid()));
				cwbOrderView.setRealweight(c.getCarrealweight());
				cwbOrderView.setCwbremark(c.getCwbremark());
				cwbOrderView.setReceivablefee(c.getReceivablefee());
				cwbOrderView.setCaramount(c.getCaramount());
				cwbOrderView.setPaybackfee(c.getPaybackfee());

				DeliveryState deliverystate = this.getDeliveryByCwb(c.getCwb());
				cwbOrderView.setPaytype(this.getPayWayType(c.getCwb(), deliverystate));// 支付方式
				cwbOrderView.setRemark1(c.getRemark1());
				cwbOrderView.setRemark2(c.getRemark2());
				cwbOrderView.setRemark3(c.getRemark3());
				cwbOrderView.setRemark4(c.getRemark4());
				cwbOrderView.setRemark5(c.getRemark5());
				cwbOrderView.setFlowordertype(c.getFlowordertype());
				cwbOrderView.setReturngoodsremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), "", "").getComment());
				String currentBranch = this.getQueryBranchName(branchList, c.getCurrentbranchid());
				cwbOrderView.setCurrentbranchname(currentBranch);
				cwbOrderView.setCwbstate(c.getCwbstate());
				if (c.getCurrentbranchid() == 0) {
					cwbOrderView.setCurrentbranchid(c.getStartbranchid());
				} else {
					cwbOrderView.setCurrentbranchid(c.getCurrentbranchid());
				}
				Date ruku = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.RuKu.getValue(), "", "").getCredate();
				Date chukusaomiao = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "", "").getCredate();
				Date daohuosaomiao = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "", "").getCredate();
				daohuosaomiao = daohuosaomiao == null ? this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), "", "").getCredate() : daohuosaomiao;
				Date fenzhanlinghuo = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "", "").getCredate();
				Date yifankui = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), "", "").getCredate();
				Date tuigonghuoshangchuku = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), "", "").getCredate();
				Date zuixinxiugai = this.getOrderFlowByCwb(c.getCwb()).getCredate();
				Date yishenhe = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.YiShenHe.getValue(), "", "").getCredate();
				cwbOrderView.setAuditstate(yishenhe == null ? 0 : 1);// 审核状态
				cwbOrderView.setInstoreroomtime(ruku != null ? this.sdf.format(ruku) : "");// 入库时间
				cwbOrderView.setOutstoreroomtime(chukusaomiao != null ? this.sdf.format(chukusaomiao) : "");// 出库时间
				cwbOrderView.setInSitetime(daohuosaomiao != null ? this.sdf.format(daohuosaomiao) : "");// 到站时间
				cwbOrderView.setPickGoodstime(fenzhanlinghuo != null ? this.sdf.format(fenzhanlinghuo) : "");// 小件员领货时间
				cwbOrderView.setGobacktime(yifankui != null ? this.sdf.format(yifankui) : "");// 反馈时间
				cwbOrderView.setGoclasstime(yishenhe == null ? "" : this.sdf.format(yishenhe));// 归班时间
				cwbOrderView.setNowtime(zuixinxiugai != null ? this.sdf.format(zuixinxiugai) : "");// 最新修改时间
				cwbOrderView.setTuigonghuoshangchukutime(tuigonghuoshangchuku != null ? this.sdf.format(tuigonghuoshangchuku) : "");// 退供货商拒收返库时间
				cwbOrderView.setBackreason(c.getBackreason());
				cwbOrderView.setLeavedreasonStr(this.getQueryReason(reasonList, c.getLeavedreasonid()));// 滞留原因
				// cwbOrderView.setExpt_code(); //异常编码
				cwbOrderView.setOrderResultType(c.getDeliverid());
				cwbOrderView.setPodremarkStr(this.getQueryReason(reasonList, this.getDeliveryStateByCwb(c.getCwb()).getPodremarkid()));// 配送结果备注
				//新加----lx
				cwbOrderView.setBackreasonid(c.getBackreasonid());

				cwbOrderView.setCartype(c.getCartype());
				cwbOrderView.setCwbdelivertypeid(c.getCwbdelivertypeid());
				cwbOrderView.setInwarhouseremark(this.exportService.getInwarhouseRemarks(remarkList).get(c.getCwb()) == null ? "" : this.exportService.getInwarhouseRemarks(remarkList).get(c.getCwb())
						.get(ReasonTypeEnum.RuKuBeiZhu.getText()));
				cwbOrderView.setCwbordertypeid(c.getCwbordertypeid() + "");// 订单类型
				cwbOrderView.setHandleperson(c.getHandleperson());
				cwbOrderView.setHandlereason(c.getHandlereason());
				cwbOrderView.setHandleresult(c.getHandleresult());
				cwbOrderView.setIsmpsflag(c.getIsmpsflag());

				if (deliverystate != null) {
					cwbOrderView.setSigninman(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? c.getConsigneename() : "");
					cwbOrderView.setSignintime(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? (yifankui != null ? this.sdf.format(yifankui) : "") : "");
					cwbOrderView.setPosremark(deliverystate.getPosremark());
					cwbOrderView.setCheckremark(deliverystate.getCheckremark());
					cwbOrderView.setDeliverstateremark(deliverystate.getDeliverstateremark());
					cwbOrderView.setCustomerbrackhouseremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), "", "").getComment());
					cwbOrderView.setDeliverystate(deliverystate.getDeliverystate());
					if ((deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) && (yifankui != null)) {
						cwbOrderView.setSendSuccesstime(this.sdf.format(yifankui));// 配送成功时间
					} else if (((deliverystate.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue()) || (deliverystate.getDeliverystate() == DeliveryStateEnum.JuShou.getValue())) && (yifankui != null)) {
						cwbOrderView.setJushoutime(this.sdf.format(yifankui));// 拒收时间
					}
				}

				//针对每个订单的供应商，查看他的集单开关是否打开，用于前台显示拦截原因是否过滤破损和丢失---刘武强16.01.07
				for (Customer customer : customerList) {
					if (customer.getCustomerid() == c.getCustomerid()) {
						cwbOrderView.setMpsswitch(customer.getMpsswitch());
					}
				}
				cwbOrderViewList.add(cwbOrderView);
			}
		}
		return cwbOrderViewList;
	}

	public String getQueryUserName(List<User> userList, long userid) {
		String username = "";
		for (User u : userList) {
			if (u.getUserid() == userid) {
				username = u.getRealname();
				break;
			}
		}
		return username;
	}

	public String getQueryCustomWareHouse(List<CustomWareHouse> customerWareHouseList, long customerwarehouseid) {
		String customerwarehouse = "";
		for (CustomWareHouse ch : customerWareHouseList) {
			if (ch.getWarehouseid() == customerwarehouseid) {
				customerwarehouse = ch.getCustomerwarehouse();
				break;
			}
		}
		return customerwarehouse;
	}

	public String getPayWayType(String cwb, DeliveryState ds) {
		StringBuffer str = new StringBuffer();
		String paywaytype = "";
		if (ds.getCash().compareTo(BigDecimal.ZERO) == 1) {
			str.append("现金,");
		}
		if (ds.getPos().compareTo(BigDecimal.ZERO) == 1) {
			str.append("POS,");
		}
		if (ds.getCheckfee().compareTo(BigDecimal.ZERO) == 1) {
			str.append("支票,");
		}
		if (ds.getOtherfee().compareTo(BigDecimal.ZERO) == 1) {
			str.append("其它,");
		}
		if (str.length() > 0) {
			paywaytype = str.substring(0, str.length() - 1);
		} else {
			paywaytype = "现金";
		}
		return paywaytype;
	}

	public String getQueryBranchName(List<Branch> branchList, long branchid) {
		String branchname = "";
		for (Branch b : branchList) {
			if (b.getBranchid() == branchid) {
				branchname = b.getBranchname();
				break;
			}
		}
		return branchname;
	}

	public OrderFlow getOrderFlowByCwb(String cwb) {
		List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
		orderflowList = this.orderFlowDAO.getAdvanceOrderFlowByCwb(cwb);
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
	}

	public String getQueryReason(List<Reason> reasonList, long reasonid) {
		String reasoncontent = "";
		for (Reason r : reasonList) {
			if (r.getReasonid() == reasonid) {
				reasoncontent = r.getReasoncontent();
				break;
			}
		}
		return reasoncontent;
	}

	public DeliveryState getDeliveryStateByCwb(String cwb) {
		List<DeliveryState> deliveryStateList = new ArrayList<DeliveryState>();
		deliveryStateList = this.deliveryStateDAO.getDeliveryStateByCwb(cwb);
		DeliveryState deliverState = deliveryStateList.size() > 0 ? deliveryStateList.get(deliveryStateList.size() - 1) : new DeliveryState();
		return deliverState;
	}

	public String getQueryCustomerName(List<Customer> customerList, long customerid) {
		String customername = "";
		for (Customer c : customerList) {
			if (c.getCustomerid() == customerid) {
				customername = c.getCustomername();
				break;
			}
		}
		return customername;
	}

	public OrderFlow getOrderFlowByCwbAndType(String cwb, long flowordertype, String begindate, String enddate) {
		List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
		orderflowList = this.orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, flowordertype, begindate, enddate);
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
	}

	// ---------------------修改订单状态-----END--------------
	// ========================乐峰上门换订单打印面单==============================
	@RequestMapping("/selectforkfsmh/{page}")
	public String selectforkfsmh(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid, @RequestParam(value = "printType", required = false, defaultValue = "-1") long printType, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "leixing", required = false, defaultValue = "0") long leixing) {
		List<Branch> bList = this.branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		Branch nowbranch = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());

		if ((nowbranch != null) && (nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {// 站点角色
			branchid = this.getSessionUser().getBranchid();
			bList = new ArrayList<Branch>();
			bList.add(nowbranch);
		}
		model.addAttribute("branchlist", bList);
		model.addAttribute("customerlist", this.customerDao.getAllCustomers());
		List<String> customeridList = this.dataStatisticsService.getList(customerid);
		model.addAttribute("customeridStr", customeridList);
		if (isshow != 0) {
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			String customerids = this.dataStatisticsService.getStrings(customerid);

			List<CwbOrder> quhuolist = this.cwbDao.getCwbOrderByCwbordertypeidAndBranchid(page, leixing, branchid, customerids, printType, begindate, enddate);
			model.addAttribute("cwbList", quhuolist);
			model.addAttribute("page_obj", new Page(this.cwbDao.getCwbOrderCount(leixing, branchid, customerids, printType, begindate, enddate), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
			model.addAttribute("userlist", this.userDAO.getAllUser());
		}

		return "/cwborder/selectforkfsmh";
	}

	@RequestMapping("/selectforsmhbdprint")
	public String selectforsmhbdprint(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint, @RequestParam(value = "modal", defaultValue = "0", required = false) long modal) {
		if (modal == 3) {
			return this.selectforlefengwang(model, isprint);
		}
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}
		List<CwbOrder> clist = this.cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));
		List<Customer> customerlist = this.customerDao.getAllCustomers();

		SystemInstall companyName = this.systemInstallDAO.getSystemInstallByName("CompanyName");
		if (companyName != null) {
			model.addAttribute("companyName", companyName.getValue());
		} else {
			model.addAttribute("companyName", "易普联科");
		}
		model.addAttribute("smtlist", clist);
		model.addAttribute("customerlist", customerlist);
		return "cwborder/selectforsmh";
	}

	/**
	 * 重置审核状态
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toChongZhiStatus")
	public String toChongZhi(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs, @RequestParam(value = "type", required = false, defaultValue = "0") int type) {
		this.logger.info("修改订单功能 [" + type + "][{}] cwb: {}", this.getSessionUser().getRealname(), cwbs);
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		// 整理sql要读取的cwb start
		String[] cwbArray = cwbs.trim().split("\r\n");
		String s = "'";
		String s1 = "',";
		StringBuffer cwbsSqlBuffer = new StringBuffer();
		for (String cwb : cwbArray) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			if (cwb.length() > 0) {
				cwbsSqlBuffer = cwbsSqlBuffer.append(s).append(cwb).append(s1);
			}
		}
		if (cwbsSqlBuffer.length() == 0) {
			return "auditorderstate/toChongZhi";
		}
		// 整理sql要读取的cwb end
		model.addAttribute("cwbArray", cwbArray);
		String cwbsSql = cwbsSqlBuffer.substring(0, cwbsSqlBuffer.length() - 1);
		List<CwbOrder> cwbList = this.cwbDao.getCwbByCwbs(cwbsSql);

		// 做重置审核状态更改的操作 start
		List<CwbOrder> allowCwb = new ArrayList<CwbOrder>();// 允许更改订单
		List<CwbOrder> prohibitedCwb = new ArrayList<CwbOrder>(); // 禁止更改的订单
		for (CwbOrder co : cwbList) {
			// 判断订单当前状态为36 已审核状态的订单才能重置审核状态
			if (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				// 判断订单号是否为POS刷卡 posremark=POS刷卡 POS刷卡的订单不允许重置审核状态
				DeliveryState ds = this.deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).get(0);
				if (ds.getPosremark().indexOf("POS刷卡") == -1) {
					allowCwb.add(co);
				} else {
					// 暂借对象中的备注1字段输出一些提示语
					co.setRemark1("POS刷卡签收的订单审核后不允许重置审核状态");
					prohibitedCwb.add(co);
				}
			} else {
				// 暂借对象中的备注1字段输出一些提示语
				co.setRemark1("当前订单状态为[" + FlowOrderTypeEnum.getText(co.getFlowordertype()).getText() + "],不允许重置审核状态");
				prohibitedCwb.add(co);
			}

		}
		model.addAttribute("allowCwb", allowCwb);
		model.addAttribute("prohibitedCwb", prohibitedCwb);
		return "auditorderstate/chongxin";
	}

	/**
	 * 审核为中转件
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toChangeZhongZhuan")
	public String toChangeZhongZhuan(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwb,// 订单状态类型
			@RequestParam(value = "cwbtypeid", defaultValue = "0", required = false) String cwbtypeid, @RequestParam(value = "customerid", defaultValue = "0", required = false) String customerid, @RequestParam(value = "branchid", defaultValue = "0", required = false) String branchid, @RequestParam(value = "auditstate", defaultValue = "0", required = false) String auditstate, @RequestParam(value = "begindate", defaultValue = "0", required = false) String begindate, @RequestParam(value = "enddate", defaultValue = "0", required = false) String enddate) {
		String quot = "'", quotAndComma = "',";
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		List<Branch> branchList = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());
		List<Customer> customerList = this.customerDao.getAllCustomers();
		model.addAttribute("branchList", branchList);
		model.addAttribute("customerList", customerList);
		Map<Long, Customer> mapcustomerlist = new HashMap<Long, Customer>();
		for (Customer cu : customerList) {
			mapcustomerlist.put(cu.getCustomerid(), cu);
		}
		model.addAttribute("mapcustomerlist", mapcustomerlist);
		List<CwbOrder> cwblist = null;

		if (cwb.length() > 0) {
			List<String> scancwblist = new ArrayList<String>();
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbStr = cwbStr.trim();
				String lastcwb = this.cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = this.cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);
				}
			}

			cwblist = this.cwbDao.getCwbByCwbs(cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "--");

			model.addAttribute("cwbList", cwblist);
		}

		return "auditorderstate/toChangeZhongZhuan";
	}

	/**
	 * 审核为中转件-通过
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/saveChangeZhongZhuan")
	public @ResponseBody String saveChangeZhongZhuan(Model model, HttpServletRequest request, @RequestParam(value = "ids", defaultValue = "", required = false) String ids) {

		try {
			this.cwbDao.updateCwbStateByIds(ids, CwbStateEnum.ZhongZhuan);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String datetime = df.format(date);

			this.cwbApplyZhongZhuanDAO.updateCwbApplyZhongZhuanForIds(datetime, this.getSessionUser().getUserid(), 1, ids, 1);
			this.logger.info("订单id{}审核为中转件", ids);

			return "{\"errorCode\":0,\"error\":\"审核成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}

	}

	/**
	 * 审核为中转件-不通过
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/saveChangeZhongZhuanNopass")
	public @ResponseBody String saveChangeZhongZhuanNopass(Model model, HttpServletRequest request, @RequestParam(value = "ids", defaultValue = "", required = false) String ids) {

		try {
			this.cwbDao.updateFlowordertypeByIds(ids, CwbStateEnum.ZhongZhuan);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String datetime = df.format(date);

			this.cwbApplyZhongZhuanDAO.updateCwbApplyZhongZhuanForIds(datetime, this.getSessionUser().getUserid(), 1, ids, 0);
			this.logger.info("订单id{}审核为中转件", ids);

			return "{\"errorCode\":0,\"error\":\"审核成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}

	}

	@RequestMapping("/tuihuozaitouexport")
	public void tuihuozaitouexport(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs, @RequestParam(value = "cwbtypeid", defaultValue = "0", required = false) int cwbordertype, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, @RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid, @RequestParam(value = "begindate", defaultValue = "", required = false) String begintime, @RequestParam(value = "enddate", defaultValue = "", required = false) String endtime, @RequestParam(value = "auditstate", defaultValue = "-1", required = false) int auditstate, @RequestParam(value = "isnow", defaultValue = "0", required = false) int isnow) {

		List<Branch> branchList = new ArrayList<Branch>();
		List<Customer> customerList = new ArrayList<Customer>();
		List<OrderBackRuku> obrsList = new ArrayList<OrderBackRuku>();
		if (isnow > 0) {
			String cwbsStr = "";
			if (cwbs.length() > 0) {
				cwbsStr = this.cwborderService.getCwbs(cwbs);
			}
			branchList = this.branchDAO.getAllEffectBranches();
			customerList = this.customerDao.getAllCustomers();
			List<OrderBackRuku> obrList = this.orderBackRukuRecordDao.getOrderbackRukus(-1, cwbsStr, cwbordertype, customerid, branchid, begintime, endtime, auditstate);
			obrsList = this.cwborderService.getOrderBackRukuRecord(obrList, branchList, customerList);
		}
		String[] cloumnName1 = new String[11]; // 导出的列名
		String[] cloumnName2 = new String[11]; // 导出的英文列名

		this.exportService.SetTuiHuoZaiTouFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "退货再投"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "tuihuozaitou_" + df.format(new Date()) + ".xlsx"; // 文件名
		ExcelUtilsHandler.exportExcelHandler(response, cloumnName, cloumnName3, sheetName, fileName, obrsList);

	}

	@RequestMapping("/toGonghuoshangExport")
	public void toGonghuoshangExport(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb, @RequestParam(value = "cwbtypeid", defaultValue = "0", required = false) int cwbtypeid, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, @RequestParam(value = "shenhestate", defaultValue = "0", required = false) long shenhestate, @RequestParam(value = "begindate", defaultValue = "", required = false) String begindate, @RequestParam(value = "enddate", defaultValue = "", required = false) String enddate

	) {
		List<Customer> customerList = this.customerDao.getAllCustomers();
		String cwbss = "";
		if (cwb.length() > 0) {
			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbStr = cwbStr.trim();
				cwbs = cwbs.append("'").append(cwbStr).append("',");
			}
			cwbss = cwbs.substring(0, cwbs.length() - 1);
		}
		List<OrderbackRecord> orList = new ArrayList<OrderbackRecord>();
		List<CwbOrderView> covList = new ArrayList<CwbOrderView>();
		if (!(cwb.equals("") && begindate.equals(""))) {
			orList = this.orderbackRecordDao.getCwbOrdersByCwbspage(-9, cwbss, cwbtypeid, customerid, shenhestate, begindate, enddate);

			StringBuffer sb = new StringBuffer();
			if (orList.size() > 0) {
				for (OrderbackRecord ot : orList) {
					sb.append("'").append(ot.getCwb()).append("',");
				}
			}
			String strs = "";
			List<CwbOrder> coList = new ArrayList<CwbOrder>();
			if (sb.length() > 0) {
				strs = sb.substring(0, sb.length() - 1);
				coList = this.cwbDao.getListbyCwbs(strs);
			}

			covList = this.cwborderService.getTuigongSuccessCwbOrderView(coList, orList, customerList);//获取分页查询的view
		}
		String[] cloumnName1 = new String[9]; // 导出的列名
		String[] cloumnName2 = new String[9]; // 导出的英文列名

		this.exportService.SetKehuShoutuihuoFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "客户收退货"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "kehushoutuihuo_" + df.format(new Date()) + ".xlsx"; // 文件名
		ExcelUtilsHandler.exportExcelHandler(response, cloumnName, cloumnName3, sheetName, fileName, covList);

	}

	/**
	 *
	 * @Title: cancelIntercept
	 * @description 撤销拦截的方法
	 * @author 刘武强
	 * @date  2016年1月13日下午8:49:41
	 * @param  @param model
	 * @param  @param transCwb
	 * @param  @param request
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/cancelIntercept")
	@ResponseBody
	public String cancelIntercept(Model model, String transCwb, HttpServletRequest request) {
		boolean flag = this.cwborderService.dealCancelIntercept(transCwb);
		return flag == true ? "1" : "0";//如果成功返回1，失败返回0
	}

}