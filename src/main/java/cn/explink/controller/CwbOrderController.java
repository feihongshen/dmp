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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
import cn.explink.dao.OrderArriveTimeDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.ShangMenTuiCwbDetailDAO;
import cn.explink.dao.ShiXiaoDAO;
import cn.explink.dao.SystemInstallDAO;
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
import cn.explink.domain.OrderGoods;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.ShangMenTuiCwbDetail;
import cn.explink.domain.ShiXiao;
import cn.explink.domain.SystemInstall;
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
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbRouteService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.LogToDayService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@RequestMapping("/cwborder")
@Controller
public class CwbOrderController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
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

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate, @RequestParam(value = "ordercwb", required = false, defaultValue = "") String ordercwb,
			@RequestParam(value = "servicearea", required = false, defaultValue = "0") long servicearea,
			@RequestParam(value = "emailfinishflag", required = false, defaultValue = "") String emailfinishflag,
			@RequestParam(value = "emaildateid", required = false, defaultValue = "0") long emaildateid, @RequestParam(value = "showMess", required = false, defaultValue = "") String showMess) {
		model.addAttribute("cwborderList", cwbDao.getcwbOrderByPage(page, customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, "", emaildateid));
		model.addAttribute("page_obj", new Page(cwbDao.getcwborderCount(customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, "", emaildateid), page,
				Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("customerList", customerDao.getAllCustomers());
		model.addAttribute("branchList", branchDAO.getAllEffectBranches());
		model.addAttribute("accountareaList", accountareaDao.getAllAccountArea());
		model.addAttribute("emaildateList", emaildateDao.getAllEmailDate());
		if (showMess.equals("1")) {
			model.addAttribute("showMess", "1");
		}
		return "/cwborder/list";
	}

	@RequestMapping("/delandedit/{page}")
	public String delandedit(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate, @RequestParam(value = "ordercwb", required = false, defaultValue = "") String ordercwb,
			@RequestParam(value = "servicearea", required = false, defaultValue = "0") long servicearea,
			@RequestParam(value = "emailfinishflag", required = false, defaultValue = "") String emailfinishflag,
			@RequestParam(value = "emaildateid", required = false, defaultValue = "0") long emaildateid) {
		model.addAttribute("cwborderList", cwbDao.getcwbOrderByPage(page, customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, "", emaildateid));
		model.addAttribute("page_obj", new Page(cwbDao.getcwborderCount(customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, "", emaildateid), page,
				Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/cwborder/delandedit";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long cwborderid, Model model, HttpServletRequest request) {
		model.addAttribute("cwborder", cwbDao.getCwbOrderByOpscwbid(cwborderid));
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
		cwbDao.saveCwbOrder(cwborder1);
		model.addAttribute("saveremand", "修改成功！");
		return "/cwborder/delandedit";
	}

	@RequestMapping("/selectforpda/{cwb}")
	public String selectforpda(Model model, HttpServletRequest request, @PathVariable("cwb") String cwb) {

		model.addAttribute("cwb", cwb);
		model.addAttribute("clist", customerDao.getAllCustomers());
		model.addAttribute("driverlist", userDAO.getUserByRole(3));
		model.addAttribute("deliverlist", userDAO.getUserByRole(2));
		model.addAttribute("tlist", truckDAO.getAllTruck());
		model.addAttribute("blist", branchDAO.getAllEffectBranches());
		model.addAttribute("owglist", outWarehouseGroupDAO.getAllOutWarehouse());
		model.addAttribute("reasonlist", reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ChangeTrains.getValue()));
		return "/cwborder/selectforpda";
	}

	@RequestMapping("/selectforsmt/{page}")
	public String selectforsmt(Model model, @PathVariable("page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate) {

		List<CwbOrder> clist = cwbDao.getCwbOrderByCwbordertypeidAndBranchid(page, CwbOrderTypeIdEnum.Shangmentui.getValue(), getSessionUser().getBranchid(), "", -1, begindate, enddate);

		model.addAttribute("cwbList", clist);
		model.addAttribute("page_obj", new Page(cwbDao.getCwbOrderCount(CwbOrderTypeIdEnum.Shangmentui.getValue(), getSessionUser().getBranchid(), "", -1, begindate, enddate), page,
				Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/cwborder/selectforsmt";
	}

	@RequestMapping("/selectforkfsmt/{page}")
	public String selectforkfsmt(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "printType", required = false, defaultValue = "3") long printType, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "orders", required = false, defaultValue = "") String orders,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@RequestParam(value = "selectype", required = false, defaultValue = "") String selectype,
			@RequestParam(value = "copyorders", required = false, defaultValue = "") String copyorders
			) {
		List<Branch> bList = branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		Branch nowbranch = branchDAO.getBranchById(getSessionUser().getBranchid());
		if (branchid == -1 || branchid == 0) {
			if (nowbranch != null && nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {// 站点角色
				branchid = getSessionUser().getBranchid();
				bList = new ArrayList<Branch>();
				bList.add(nowbranch);
			}
		} else if (nowbranch != null && nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {// 站点角色
			branchid = getSessionUser().getBranchid();
			bList = new ArrayList<Branch>();
			bList.add(nowbranch);
		}
		model.addAttribute("branchlist", bList);
		model.addAttribute("customerlist", customerDao.getAllCustomers());
		List<String> customeridList = dataStatisticsService.getList(customerid);
		model.addAttribute("customeridStr", customeridList);
		if (isshow != 0) {
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			String customerids = dataStatisticsService.getStrings(customerid);
			StringBuffer buffer=new StringBuffer();
			if(selectype.equals("1")){
				if (!orders.isEmpty()&&!orders.equals("每次输入的订单不超过500个")) {
					String[] orderStrings=orders.split("\\r\\n");
					for (int i = 0; i < orderStrings.length; i++) {
						buffer.append("'").append(orderStrings[i]+"',");
					}
					if (buffer.length()>0) {
						orders=buffer.substring(0,buffer.length()-1).toString();
					}
				}else if((orders.isEmpty()||orders.equals("每次输入的订单不超过500个"))&&!copyorders.isEmpty()&&selectype.equals("1")){
					orders=copyorders;
				}else {
					orders="";
				}
				model.addAttribute("orders",orders);
			}
			
			List<String> smtcwbsList = shangMenTuiCwbDetailDAO.getShangMenTuiCwbDetailByCustomerid(customerids, printType, begindate, enddate, branchid,orders,selectype);
			String cwbs = "";
			if (smtcwbsList.size() > 0) {
				cwbs = dataStatisticsService.getOrderFlowCwbs(smtcwbsList);
			} else {
				cwbs = "'--'";
			}
			List<CwbOrder> clist = cwbDao.getCwbByCwbsPage(page, cwbs);
			model.addAttribute("cwbList", clist);
			model.addAttribute("page_obj", new Page(cwbDao.getCwbOrderCwbsCount(cwbs), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
			
		}

		return "/cwborder/selectforkfsmt";
	}

	@RequestMapping("/selectforsmtprint")
	public String selectforsmtprint(Model model, @RequestParam(value = "nextbranchid", defaultValue = "-1", required = false) long nextbranchid,
			@RequestParam(value = "customeridstr", required = false, defaultValue = "") String customerids, @RequestParam(value = "modal", defaultValue = "0", required = false) long modal) {
		if (modal == 1) {
			return selectforsmtprintgome(model, nextbranchid, customerids);
		}
		customerids = customerids.length() > 1 ? customerids.substring(0, customerids.length() - 1) : "";
		if (nextbranchid == -1) {
			nextbranchid = getSessionUser().getBranchid();
		}
		List<CwbOrder> clist = cwbDao.getCwbByPrinttime(getSessionUser().getBranchid(), nextbranchid, customerids);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		for (CwbOrder c : clist) {
			cwbDao.saveCwbForPrinttime(c.getCwb(), printtime);
			shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(c.getCwb(), printtime);
			
		}
		List<Customer> customerlist = customerDao.getAllCustomers();

		model.addAttribute("clist", clist);
		model.addAttribute("customerlist", customerlist);
		return "/cwborder/selectforsmtprint";
	}

	@RequestMapping("/selectforsmtprintgome")
	public String selectforsmtprintgome(Model model, @RequestParam(value = "nextbranchid", defaultValue = "-1", required = false) long nextbranchid,
			@RequestParam(value = "customerid", required = false, defaultValue = "") String customerids) {

		if (nextbranchid == -1) {
			nextbranchid = getSessionUser().getBranchid();
		}
		customerids = customerids.length() > 1 ? customerids.substring(0, customerids.length() - 1) : "";
		List<CwbOrder> clist = cwbDao.getCwbByPrinttime(getSessionUser().getBranchid(), nextbranchid, customerids);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		for (CwbOrder c : clist) {
			cwbDao.saveCwbForPrinttime(c.getCwb(), printtime);
			shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(c.getCwb(), printtime);
		}
		List<Customer> customerlist = customerDao.getAllCustomers();
		List<Branch> branchList = branchDAO.getAllEffectBranches();

		model.addAttribute("clist", clist);
		model.addAttribute("branchList", branchList);
		model.addAttribute("customerlist", customerlist);
		return "/cwborder/selectforgomeprint";
	}

	@RequestMapping("/selectforsmtbdprint")
	
	public String selectforsmtbdprint(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint,
			@RequestParam(value = "modal", defaultValue = "0", required = false) long modal) {
		/*SystemInstall systemInstall=systemInstallDAO.getSystemInstallByName("是否默认模板为VIP模板","isdefaultmodel");
		if(systemInstall.getValue().equals("yes")&&systemInstall.getValue()!=""){
			if (modal==0) {
				modal=3;
			}
			
		}*/
		if (modal == 1) {
			return selectforgomeprint(model, isprint);
		}
		if (modal == 2) {
			return selectforjiayougouwu(model, isprint);
		}
		if (modal == 3) {
			return selectforvip(model, isprint);
		}
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}

		List<ShangMenTuiCwbDetail> smtlist = shangMenTuiCwbDetailDAO.getShangMenTuiCwbDetailByCwbs(cwbs.substring(0, cwbs.length() - 1));

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		updatePrinttimeState(smtlist, printtime);
		List<Customer> customerlist = customerDao.getAllCustomers();

		SystemInstall companyName = systemInstallDAO.getSystemInstallByName("CompanyName");
		if (companyName != null) {
			model.addAttribute("companyName", companyName.getValue());
		} else {
			model.addAttribute("companyName", "易普联科");
		}
		model.addAttribute("smtlist", smtlist);
		model.addAttribute("customerlist", customerlist);
		return "cwborder/selectforsmtprint";
	}

	@Transactional
	private void updatePrinttimeState(List<ShangMenTuiCwbDetail> smtlist,String printtime) {
		for (ShangMenTuiCwbDetail smtcd : smtlist) {
			logger.info("上门退订单打印记录cwb={}",smtcd.getCwb());
			cwbDao.saveCwbForPrinttime(smtcd.getCwb(), printtime);
			shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(smtcd.getCwb(), printtime);
		}
	}

	// 家有购物模板
	@RequestMapping("/selectforjiayougouwu")
	private String selectforjiayougouwu(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}
		List<ShangMenTuiCwbDetail> smtlist = shangMenTuiCwbDetailDAO.getShangMenTuiCwbDetailByCwbs(cwbs.substring(0, cwbs.length() - 1));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		for (ShangMenTuiCwbDetail smtcd : smtlist) {
			cwbDao.saveCwbForPrinttime(smtcd.getCwb(), printtime);
			shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(smtcd.getCwb(), printtime);
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
		List<CwbOrder> clist = cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		List<Customer> list = customerDao.getAllCustomers();
		List<User> userlist = userDAO.getAllUser();
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
		List<CwbOrder> clist = cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));
		List<Branch> branchList = branchDAO.getAllEffectBranches();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		for (CwbOrder c : clist) {
			cwbDao.saveCwbForPrinttime(c.getCwb(), printtime);
			shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(c.getCwb(), printtime);
		}
		List<Customer> customerlist = customerDao.getAllCustomers();

		SystemInstall companyName = systemInstallDAO.getSystemInstallByName("CompanyName");
		if (companyName != null) {
			model.addAttribute("companyName", companyName.getValue());
		} else {
			model.addAttribute("companyName", "易普联科");
		}
		model.addAttribute("clist", clist);
		model.addAttribute("branchList", branchList);
		model.addAttribute("customerlist", customerlist);
		SystemInstall isShowRemark = systemInstallDAO.getSystemInstall("isShowRemark");
		if (isShowRemark != null && !isShowRemark.getValue().equals("no")) {
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
		List<CwbOrder> clist = cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));
		List<Branch> branchList = branchDAO.getAllEffectBranches();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String printtime = df.format(date);
		List<OrderGoods> orderGoods = null;
		Map<String, List<OrderGoods>> mapOrderGoods = new HashMap<String, List<OrderGoods>>();
		for (CwbOrder c : clist) {
			orderGoods = orderGoodsDAO.getOrderGoodsList(c.getCwb());
			mapOrderGoods.put(c.getCwb(), orderGoods);
			cwbDao.saveCwbForPrinttime(c.getCwb(), printtime);
			shangMenTuiCwbDetailDAO.saveShangMenTuiCwbDetailForPrinttime(c.getCwb(), printtime);
		}
		List<Customer> customerlist = customerDao.getAllCustomers();

		SystemInstall companyName = systemInstallDAO.getSystemInstallByName("CompanyName");
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
	public String editBatchBranch(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "excelbranch", required = false, defaultValue = "") String excelbranch, @RequestParam(value = "branchcode", required = false, defaultValue = "") String branchcode,
			@RequestParam(value = "onePageNumber", defaultValue = "10", required = false) long onePageNumber, // 每页记录数
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 是否显示
	) throws Exception {
		int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		List<CwbOrder> cwborderList = new ArrayList<CwbOrder>();
		Page pageobj = new Page();
		long count = 0;
		if (isshow != 0) {
			List<Branch> lb = new ArrayList<Branch>();
			List<Branch> branchnamelist = branchDAO.getBranchByBranchnameCheck(branchcode.length() > 0 ? branchcode : excelbranch);
			if (branchnamelist.size() > 0) {
				lb = branchnamelist;
			} else {
				lb = branchDAO.getBranchByBranchcode(branchcode.length() > 0 ? branchcode : excelbranch);
			}
			if (lb.size() > 0) {
				String[] cwbstr = cwbs.trim().split("\r\n");
				List<String> cwbStrList = new ArrayList<String>();
				for (int i = 0; i < cwbstr.length; i++) {
					if (cwbstr[i].trim().length() == 0) {
						continue;
					}
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
				List<CwbOrder> oList = cwbDao.getCwbByCwbs(cwbstrs);
				if (oList != null && oList.size() > 0) {
					for (CwbOrder cwbOrder : oList) {
						CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
						if (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()
								|| cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue()) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
																															// 都将匹配状态变更为修改
							addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
						} else if (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()
								|| cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue()) {// 如果修改的数据原来是为匹配的
																																// 或者是人工匹配的
																																// 都将匹配状态变更为人工修改
							addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
						}
						try {
							cwborderService.updateDeliveryBranch(getSessionUser(), cwbOrder, lb.get(0), addressCodeEditType);
							count += 1;
						} catch (CwbException ce) {
							model.addAttribute("error", "匹配失败，" + ce.getMessage() + "!");
						}
					}
				}
				cwborderList = cwbDao.getCwbByCwbs(cwbstrs);
				pageobj = new Page(count, page, onePageNumber);
			} else {
				model.addAttribute("branchs", branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));
				model.addAttribute("Order", cwborderList);
				model.addAttribute("page_obj", pageobj);
				model.addAttribute("page", page);
				model.addAttribute("msg", "1");
				model.addAttribute("count", count);
				return "cwborder/editBranch";
			}

		}
		model.addAttribute("branchs", branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));
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
		model.addAttribute("cwborderList", cwbDao.getcwbOrderByWherePage(page, branchid));
		model.addAttribute("page_obj", new Page(cwbDao.getcwborderByWherePageCount(branchid), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		List<Branch> bList = getNextPossibleBranches();
		List<Branch> branchAllList = branchDAO.getAllEffectBranches();
		model.addAttribute("branchAllList", branchAllList);
		model.addAttribute("branchList", bList);
		return "/cwborder/daocuohuolist";
	}

	private List<Branch> getNextPossibleBranches() {
		List<Branch> bList = new ArrayList<Branch>();
		for (long i : cwbRouteService.getNextPossibleBranch(getSessionUser().getBranchid())) {
			bList.add(branchDAO.getBranchByBranchid(i));
		}
		return bList;
	}

	@RequestMapping("/daocuohuobeizhupage/{cwb}")
	public String daocuohuobeizhupage(Model model, @PathVariable("cwb") String cwb) {
		model.addAttribute("cwborder", cwbDao.getCwbByCwb(cwb));
		return "/cwborder/daocuohuobeizhu";
	}

	@RequestMapping("/daocuohuobeizhu/{cwb}")
	public @ResponseBody String daocuohuobeizhu(@PathVariable("cwb") String cwb, @RequestParam(value = "comment", required = false, defaultValue = "") String comment) {
		try {
			String scancwb = cwb;
			cwb = cwborderService.translateCwb(cwb);
			cwborderService.handleDaocuohuo(getSessionUser(), cwb, scancwb, comment);
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
	public String detailList(Model model, @PathVariable("listType") long listType, @PathVariable("page") long page,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid) {
		if (listType == CwbOrderListTypeEnum.ZHAN_DIAN_JIAN_KONG_ZAI_TU.getValue()) {

			model.addAttribute(
					"cwborderList",
					cwbDao.getCwbOrderByNextBranchidAndFlowordertypeToPage(page, branchid, FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + ","
							+ FlowOrderTypeEnum.TuiHuoChuZhan.getValue()));
			model.addAttribute(
					"page_obj",
					new Page(cwbDao.getCwbOrderByNextBranchidAndFlowordertypeCount(branchid, FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + ","
							+ FlowOrderTypeEnum.TuiHuoChuZhan.getValue()), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);

		} else if (listType == CwbOrderListTypeEnum.ZHAN_DIAN_JIAN_KONG_KU_CUN.getValue()) {
			model.addAttribute("cwborderList", cwbDao.getCwbOrderByCurrentbranchidAndFlowordertypeToPage(page, branchid));
			model.addAttribute("page_obj", new Page(cwbDao.getCwbOrderByCurrentbranchidAndFlowordertypeCount(branchid), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);

		} else if (listType == CwbOrderListTypeEnum.ZHAN_DIAN_JIAN_KONG_PAI_SONG_ZHONG.getValue()) {

			model.addAttribute("cwborderList", cwbDao.getCwbOrderByDeliverybranchidAndDeliverystateToPage(page, branchid, DeliveryStateEnum.WeiFanKui.getValue()));
			model.addAttribute("page_obj", new Page(cwbDao.getCwbOrderByDeliverybranchidAndDeliverystateCount(branchid, DeliveryStateEnum.WeiFanKui.getValue()), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		}

		model.addAttribute("customerMap", customerDao.getAllCustomersToMap());

		Map<Long, CustomWareHouse> customerWarehouseMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse customWareHouse : customWareHouseDAO.getAllCustomWareHouse()) {
			customerWarehouseMap.put(customWareHouse.getWarehouseid(), customWareHouse);
		}
		model.addAttribute("customerWarehouseMap", customerWarehouseMap);

		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : branchDAO.getAllBranches()) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("branchMap", branchMap);

		return "/cwborder/detaillist";
	}

	// ---------------------修改订单状态-------------------

	@RequestMapping("/editCwbstateBatch/{page}")
	public String editCwbstateBatch(Model model, HttpServletRequest request, @PathVariable("page") long page, @RequestParam(value = "cwbs", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "cwbstate", defaultValue = "-1", required = false) int cwbstate // 订单状态类型
	) {
		if (cwbs.trim().length() > 0) {
			String[] cwb = cwbs.split("\r\n");

			StringBuffer cwbsBuffer = new StringBuffer();
			for (int i = 0; i < cwb.length; i++) {
				if (cwb[i].trim().length() == 0) {
					continue;
				}
				cwbsBuffer.append("'").append(cwb[i]).append("'");
				if (i < cwb.length - 1) {
					cwbsBuffer.append(",");
				}
			}

			if (cwbstate > 0) {// 批量修改订单状态
				for (String cwbStr : cwb) {
					cwborderService.updateCwbState(cwbStr, CwbStateEnum.getByValue(cwbstate));
				}
				model.addAttribute("msg", MessageFormat.format("成功修改了{0}单的订单状态修改为{1}", cwb.length, CwbStateEnum.getByValue(cwbstate).getText()));
			}

			List<CwbOrder> cwbOrderList = cwbDao.getCwbOrderByCwbs(page, cwbsBuffer.toString());// deliveryStateDAO.getDeliverByCwbs(page,cwbsBuffer.toString());

			List<QuickSelectView> qsvList = new ArrayList<QuickSelectView>();
			for (CwbOrder cwborder : cwbOrderList) {
				QuickSelectView qsv = new QuickSelectView();
				BeanUtils.copyProperties(deliveryStateDAO.getDeliveryByCwb(cwborder.getCwb()), qsv);
				BeanUtils.copyProperties(cwborder, qsv);

				qsvList.add(qsv);
			}

			model.addAttribute("qsvList", qsvList);
			model.addAttribute("page_obj", new Page(deliveryStateDAO.getDeliverByCwbsCount(cwbsBuffer.toString()), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);

			model.addAttribute("customerMap", customerDao.getAllCustomersToMap());
		}

		return "cwborder/editCwbstateBatch";
	}

	@RequestMapping("/geteditCwbstate")
	public String geteditCwbstate(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "cwbstate", defaultValue = "0", required = false) long cwbstate // 订单状态类型
	) {
		if (cwb.length() == 0) {
			model.addAttribute("Order", null);
		} else {
			model.addAttribute("Order", cwbDao.getCwbByCwb(cwb));
		}

		return "cwborder/editCwbstate";
	}

	@RequestMapping("/editCwbstate/{cwb}")
	public @ResponseBody String editCwbstate(@PathVariable("cwb") String cwb, @RequestParam(value = "cwbstate", required = false) int cwbstate) {
		cwborderService.updateCwbState(cwb, CwbStateEnum.getByValue(cwbstate));
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
		String quot = "'", quotAndComma = "',";
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));

		if (cwb.length() > 0) {
			List<String> scancwblist = new ArrayList<String>();
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				String lastcwb = cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);
				}
			}
			request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			List<Customer> customerList = customerDao.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = branchDAO.getAllEffectBranches();
			List<User> userList = userDAO.getAllUser();
			List<Reason> reasonList = reasonDAO.getAllReason();
			List<Remark> remarkList = remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("cwbList", getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList));
			model.addAttribute("branchList", branchList);
			model.addAttribute("customerList", customerList);
			model.addAttribute("reasonList", reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue()));
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
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));

		if (cwb.length() > 0) {
			List<String> scancwblist = new ArrayList<String>();
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				String lastcwb = cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);
				}
			}
			request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			List<Customer> customerList = customerDao.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = branchDAO.getAllEffectBranches();
			List<User> userList = userDAO.getAllUser();
			List<Reason> reasonList = reasonDAO.getAllReason();
			List<Remark> remarkList = remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("cwbList", getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList));
			model.addAttribute("branchList", branchList);
			model.addAttribute("customerList", customerList);
			model.addAttribute("reasonList", reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue()));
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
	@RequestMapping("/toTuiHuoZaiTou")
	public String toTuiHuoZaiTou(Model model,HttpServletRequest request,
			@RequestParam(value = "exportmould", defaultValue = "", required = false) String exportmould,
			@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "cwbtypeid", defaultValue = "", required = false) String cwbtypeid,
			@RequestParam(value = "customerid",defaultValue = "", required = false) String customerid,
			@RequestParam(value = "branchid", defaultValue = "", required = false) String branchid,
			@RequestParam(value = "begindate", defaultValue = "", required = false) String begindate,
			@RequestParam(value = "enddate", defaultValue = "", required = false) String enddate
			) {
		String quot = "'", quotAndComma = "',";
		/*int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);*/
		String isUseAuditTuiHuo = systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		
		List<Customer> customerList = customerDao.getAllCustomers();
		List<Branch> branchList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));// bug546licx
		List<CwbOrderView> cwbList = new ArrayList<CwbOrderView>();
		List<TuihuoRecord> tuihuocwbList = new ArrayList<TuihuoRecord>();
		
		model.addAttribute("branchList", branchList);
		model.addAttribute("customerList", customerList);	
		//站点
		Map<Long , String > mapbranch = new HashMap<Long, String>();
		for(int i=0;i<branchList.size();i++){
			mapbranch.put(branchList.get(i).getBranchid(), branchList.get(i).getBranchname());
		}
		//供货商
		Map<Long , String > mapcustomer = new HashMap<Long, String>();
		for(int i=0;i<customerList.size();i++){
			mapcustomer.put(customerList.get(i).getCustomerid(), customerList.get(i).getCustomername());
		}
		//订单类型
		Map<Long , String > mapcwbordertype = new HashMap<Long, String>();
		mapcwbordertype.put(Long.valueOf(CwbOrderTypeIdEnum.Peisong.getValue()), CwbOrderTypeIdEnum.Peisong.getText());
		mapcwbordertype.put(Long.valueOf(CwbOrderTypeIdEnum.Shangmenhuan.getValue()), CwbOrderTypeIdEnum.Shangmenhuan.getText());
		mapcwbordertype.put(Long.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()), CwbOrderTypeIdEnum.Shangmentui.getText());
		
		model.addAttribute("mapbranch", mapbranch);
		model.addAttribute("mapcustomer", mapcustomer);
		model.addAttribute("mapcwbordertype",mapcwbordertype);
		//当选择条件为订单时
		if (cwb.length() > 0&&!cwb.equals("查询多个订单用回车隔开")) {
			List<String> scancwblist = new ArrayList<String>();
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();
			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				String lastcwb = cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr.trim());
					cwborderlist.add(co);
				}
			}
			request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			
			List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();
			List<User> userList = userDAO.getAllUser();
			List<Reason> reasonList = reasonDAO.getAllReason();
			List<Remark> remarkList = remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			cwbList = getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList);
			model.addAttribute("reasonList", reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.TuiHuoZaiTou.getValue()));
		}else{
			//根据选择条件查询订单
			//(cwbtypeid==null||cwbtypeid.equals("0"))&&(customerid==null||customerid.equals("0"))&&(branchid==null||branchid.equals("0"))&&(begindate==null||begindate.equals("0"))&&(enddate==null||enddate.equals("0"))
			if(cwbtypeid.equals("")&&customerid.equals("")&&branchid.equals("")&&begindate.equals("")&&enddate.equals("")){
				return "auditorderstate/toTuiHuoZaiTou";
			}
			tuihuocwbList = tuihuoRecordDAO.getTuihuoRecordByTuihuozhanruku(begindate, enddate,String.valueOf(branchid),String.valueOf(customerid), String.valueOf(cwbtypeid));
			StringBuffer cwbss = new StringBuffer("");
 			if(tuihuocwbList!=null&&tuihuocwbList.size()>0){
				for(TuihuoRecord tr:tuihuocwbList){
					cwbss.append("'"+tr.getCwb()+"',");
				}
				List<CwbOrder> cwborderList = cwbDao.getcwborderList(cwbss.substring(0, cwbss.lastIndexOf(",")));
				for(TuihuoRecord tr:tuihuocwbList){
					for(CwbOrder co:cwborderList){
						if(tr.getCwb().equals(co.getCwb())){
							CwbOrderView cov = new CwbOrderView();
							cov.setCwb(co.getCwb());
							cov.setCwbordertypeid(String.valueOf(co.getCwbordertypeid()));
							cov.setCustomerid(co.getCustomerid());//供货商id
							cov.setConsigneename(co.getConsigneename());
							cov.setConsigneeaddress(co.getConsigneeaddress());
							cov.setTuihuozhaninstoreroomtime(tr.getTuihuozhanrukutime());//退货入库时间
							cov.setAuditEganstate(0);//退货再投审核 默认0
							cov.setTuihuoid(co.getTuihuoid());//退货站点id
							cwbList.add(cov);
						}
					}
				}
				
			}
		}
		model.addAttribute("cwbList", cwbList);
		return "auditorderstate/toTuiHuoZaiTou";
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
		logger.info("--审为退货再投 开始--");
		String cwbremarks = request.getParameter("cwbremarks");
		if (cwbremarks == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbremarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || reason.indexOf("_s_") == -1) {
				continue;
			}
			String[] cwb_reasonid = reason.split("_s_");
			if (cwb_reasonid.length == 2) {
				// TODO 所有订单号均向订单所在负责人发送短信
				try {
					if (!cwb_reasonid[1].equals("0") && !cwb_reasonid[1].equals("")) {
						String scancwb = cwb_reasonid[0];
						cwborderService.auditToZaiTou(getSessionUser(), cwb_reasonid[0], scancwb, FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue(), Long.valueOf(cwb_reasonid[1]));
						successCount++;
						failureCount--;
					}
					logger.info("{} 成功", reason);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("{} 失败", reason);
				}
			} else {
				logger.info("{} 失败，格式不正确", reason);
			}
		}

		return successCount + "_s_" + failureCount;
	}
	
	/**
	 * 审为退货 针对所在机构是库房的不是退货状态的订单
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/auditTuiHuo")
	public @ResponseBody String auditTuiHuo(Model model, HttpServletRequest request) {
		logger.info("--审为退货 开始--");
		String reasons = request.getParameter("reasons");
		if (reasons == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(reasons);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || reason.indexOf("_s_") == -1) {
				continue;
			}
			String[] cwb_reasonid = reason.split("_s_");
			if (cwb_reasonid.length == 2) {
				try {
					if (!cwb_reasonid[1].equals("0")) {
						String scancwb = cwb_reasonid[0];
						cwborderService.auditToTuihuo(getSessionUser(), cwb_reasonid[0], scancwb, FlowOrderTypeEnum.DingDanLanJie.getValue(), Long.valueOf(cwb_reasonid[1]));
						successCount++;
						failureCount--;
					}
					logger.info("{} 成功", reason);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("{} 失败", reason);
				}
			} else {
				logger.info("{} 失败，格式不正确", reason);
			}
		}

		return successCount + "_s_" + failureCount;
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
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));

		if (cwb.length() > 0) {
			List<String> scancwblist = new ArrayList<String>();
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				String lastcwb = cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);
				}
			}
			request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			List<Customer> customerList = customerDao.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = branchDAO.getAllEffectBranches();
			List<User> userList = userDAO.getAllUser();
			List<Reason> reasonList = reasonDAO.getAllReason();
			List<Remark> remarkList = remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("cwbList", getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList));
			model.addAttribute("branchList", branchList);
			model.addAttribute("customerList", customerDao.getAllCustomers());
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
	@RequestMapping("/toTuiGongHuoShangSuccess")
	public String toTuiGongHuoShangSuccess(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "cwbtypeid", defaultValue = "", required = false) String cwbtypeid,
			@RequestParam(value = "customerid",defaultValue = "", required = false) String customerid,
			@RequestParam(value = "begindate", defaultValue = "", required = false) String begindate,
			@RequestParam(value = "enddate", defaultValue = "", required = false) String enddate
			) {
		String quot = "'", quotAndComma = "',";
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		List<Branch> branchList = branchDAO.getAllEffectBranches();
		List<Customer> customerList = customerDao.getAllCustomers();
		model.addAttribute("branchList", branchList);
		model.addAttribute("customerList", customerDao.getAllCustomers());
		List<CwbOrderView> covlist = new ArrayList<CwbOrderView>();
		
		if (cwb.length() > 0) {
			List<String> scancwblist = new ArrayList<String>();
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				String lastcwb = cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);
				}
			}
			request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			
			List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();
			List<User> userList = userDAO.getAllUser();
			List<Reason> reasonList = reasonDAO.getAllReason();
			List<Remark> remarkList = remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			covlist = getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList);
		}else if(cwb==null||("".equals(cwb.trim()))){
			List<OrderFlow> oflist = orderFlowDAO.getOrderByCredate(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(),begindate,enddate);
			if(oflist!=null&&oflist.size()>0){
				String cwbs = "";
				for(OrderFlow of:oflist){
					cwbs+="'"+of.getCwb()+"',";
				}
				List<CwbOrder> colist = cwbDao.getcwborderList(cwbs.substring(0,cwbs.lastIndexOf(",")));
				Map<Long, String> map = new HashMap<Long, String>();
				for(Customer co:customerList){
					map.put(co.getCustomerid(), co.getCustomername());
				}
				CwbOrderView cov = new CwbOrderView();
				for(OrderFlow odf:oflist){
					for(CwbOrder co:colist){
						if(odf.equals(co)){
							cov.setCwb(co.getCwb());
							cov.setCwbordertypename(getEnumtextByEnumvalue(co.getCwbordertypeid()));
							cov.setCustomername(map.get(co.getCustomerid()));
							cov.setCaramount(co.getCaramount());
							cov.setEmaildate(co.getEmaildate());
							cov.setTuigonghuoshangchukutime(String.valueOf(odf.getCredate()));
							covlist.add(cov);
						}
					}
				}
			}
		}
		model.addAttribute("covlist",covlist);
		return "auditorderstate/toTuiGongHuoShangSuccess";
	}

	public String  getEnumtextByEnumvalue(int value){
		for(CwbOrderTypeIdEnum cotie:CwbOrderTypeIdEnum.values()){
			if(cotie.getValue()==value){
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
		String isUseAuditTuiHuo = systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
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
			List<Customer> cList = customerDao.getAllCustomers();
			for (String cwbStr : cwb.split("\r\n")) {
				CwbOrder co = cwbDao.getCwbByCwb(cwbStr);
				if (co != null) {
					Customer customer = getCustomer(co.getCustomerid(), cList);
					if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Amazon.getKey()))) {
						OrderFlow of = orderFlowDAO.getOrderFlowByIsnow(cwbStr);
						of.setFlowordertype(FlowOrderTypeEnum.BaoGuoweiDao.getValue());
						of.setCredate(new Date());
						String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
						String csremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + getSessionUser().getRealname() + ":标记[包裹未到]";
						try {
							cwbDao.updateCwbRemark(cwbStr, csremark);
							cwborderService.send(of);
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
			List<Customer> cList = customerDao.getAllCustomers();
			for (String cwbStr : cwb.split("\r\n")) {
				CwbOrder co = cwbDao.getCwbByCwb(cwbStr);
				if (co != null) {
					Customer customer = getCustomer(co.getCustomerid(), cList);
					if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Amazon.getKey()))) {
						OrderFlow of = orderFlowDAO.getOrderFlowByIsnow(cwbStr);
						of.setFlowordertype(FlowOrderTypeEnum.ZhongZhuanyanwu.getValue());
						of.setCredate(new Date());
						String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
						String csremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + getSessionUser().getRealname() + ":标记[中转延误]";
						try {
							cwbDao.updateCwbRemark(cwbStr, csremark);
							cwborderService.send(of);
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
			List<Customer> cList = customerDao.getAllCustomers();
			for (String cwbStr : cwb.split("\r\n")) {
				CwbOrder co = cwbDao.getCwbByCwb(cwbStr);
				if (co != null) {
					Customer customer = getCustomer(co.getCustomerid(), cList);
					if (customer.getB2cEnum().equals(String.valueOf(B2cEnum.Amazon.getKey()))) {
						OrderFlow of = orderFlowDAO.getOrderFlowByIsnow(cwbStr);
						of.setFlowordertype(FlowOrderTypeEnum.ShouGongdiushi.getValue());
						of.setCredate(new Date());
						String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
						String csremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + getSessionUser().getRealname() + ":标记[货物丢失]";
						try {
							cwbDao.updateCwbRemark(cwbStr, csremark);
							cwborderService.send(of);
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
		if (cList != null && cList.size() > 0) {
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

		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));

		if (cwb.length() > 0) {
			List<String> scancwblist = new ArrayList<String>();
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				String lastcwb = cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);
				}
			}
			request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			List<Customer> customerList = customerDao.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = branchDAO.getAllEffectBranches();
			List<User> userList = userDAO.getAllUser();
			List<Reason> reasonList = reasonDAO.getAllReason();
			List<Remark> remarkList = remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("cwbList", getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList));
			model.addAttribute("branchList", branchList);
			model.addAttribute("customerList", customerDao.getAllCustomers());
			model.addAttribute("userallList", userDAO.getAllUser());
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
		logger.info("--审为退供货商 开始--");
		String cwbremarks = request.getParameter("cwbs");
		if (cwbremarks == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbremarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || reason.indexOf("_s_") == -1) {
				continue;
			}
			String[] cwb_reasonid = reason.split("_s_");
			if (cwb_reasonid.length == 2) {
				// TODO 所有订单号均向订单所在负责人发送短信
				try {
					if (!cwb_reasonid[1].equals("0")) {
						String cwb = cwborderService.translateCwb(cwb_reasonid[0]);
						cwborderService.updateCwbState(cwb, CwbStateEnum.TuiGongYingShang);
						successCount++;
						failureCount--;
					}
					logger.info("{} 成功", reason);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("{} 失败", reason);
				}
			} else {
				logger.info("{} 失败，格式不正确", reason);
			}
		}

		return successCount + "_s_" + failureCount;
	}

	/**
	 * 审核为中转
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	/*
	 * @RequestMapping("/auditZhongZhuan") public @ResponseBody String
	 * auditZhongZhuan(Model model,HttpServletRequest request,
	 * 
	 * @RequestParam(value = "deliverybranchid", required = false, defaultValue
	 * = "0") long deliverybranchid){ logger.info("--审核为中转 开始--"); String
	 * cwbremarks = request.getParameter("cwbs"); if(cwbremarks==null){ return
	 * 0+"_s_"+0; } JSONArray rJson = JSONArray.fromObject(cwbremarks); long
	 * successCount = 0; long failureCount = rJson.size(); for(int i=0 ;i <
	 * rJson.size() ; i ++ ){ String reason = rJson.getString(i);
	 * if(reason.equals("")||reason.indexOf("_s_")==-1){ continue; } String []
	 * cwb_reasonid = reason.split("_s_"); if(cwb_reasonid.length==2){ //TODO
	 * 所有订单号均向订单所在负责人发送短信 try{ if(!cwb_reasonid[1].equals("0")){
	 * cwborderService.auditToZhongZhuan(getSessionUser(),cwb_reasonid[0],
	 * deliverybranchid); successCount++; failureCount--; }
	 * logger.info("{} 成功",reason); }catch (Exception e) { e.printStackTrace();
	 * logger.error("{} 失败",reason); } }else{ logger.info("{} 失败，格式不正确",reason);
	 * } }
	 * 
	 * return successCount+"_s_"+failureCount; }
	 */

	/**
	 * 审为供货商拒收返库
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/auditGongHuoShangJuTui")
	public @ResponseBody String auditGongHuoShangJuTui(Model model, HttpServletRequest request) {
		logger.info("--审为供货商拒收返库开始--");
		String cwbremarks = request.getParameter("cwbs");
		if (cwbremarks == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbremarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || reason.indexOf("_s_") == -1) {
				continue;
			}
			String[] cwb_reasonid = reason.split("_s_");
			if (cwb_reasonid.length == 2) {
				// TODO 所有订单号均向订单所在负责人发送短信
				try {
					if (!cwb_reasonid[1].equals("0")) {
						String scancwb = cwb_reasonid[0];
						cwborderService.customrefuseback(getSessionUser(), cwb_reasonid[0], scancwb, 0, "");
						successCount++;
						failureCount--;
					}
					logger.info("{} 成功", reason);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("{} 失败", reason);
				}
			} else {
				logger.info("{} 失败，格式不正确", reason);
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
		logger.info("--审为供货商确认退货 开始--");
		String cwbremarks = request.getParameter("cwbs");
		if (cwbremarks == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbremarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || reason.indexOf("_s_") == -1) {
				continue;
			}
			String[] cwb_reasonid = reason.split("_s_");
			if (cwb_reasonid.length == 2) {
				// TODO 所有订单号均向订单所在负责人发送短信
				try {
					if (!cwb_reasonid[1].equals("0")) {
						String scancwb = cwb_reasonid[0];
						cwborderService.supplierBackSuccess(getSessionUser(), cwb_reasonid[0], scancwb, 0);
						successCount++;
						failureCount--;
					}
					logger.info("{} 成功", reason);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("{} 失败", reason);
				}
			} else {
				logger.info("{} 失败，格式不正确", reason);
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
		logger.info("--异常订单处理功能 开始--");
		String cwbdetails = request.getParameter("cwbdetails");
		if (cwbdetails == null) {
			return 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(cwbdetails);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || reason.indexOf("_s_") == -1) {
				continue;
			}
			String[] cwb_reasonid = reason.split("_s_");
			if (cwb_reasonid.length == 4) {
				// TODO 所有订单号均向订单所在负责人发送短信
				try {
					if (!cwb_reasonid[1].equals("-1") && !cwb_reasonid[2].equals("-1") && !cwb_reasonid[3].replaceAll("@", "").equals("")) {
						String scancwb = cwb_reasonid[0];
						cwborderService.SpecialCwbHandle(getSessionUser(), cwb_reasonid[0], scancwb, Long.parseLong(cwb_reasonid[1]), Long.parseLong(cwb_reasonid[2]),
								cwb_reasonid[3].replaceAll("@", ""), FlowOrderTypeEnum.YiChangDingDanChuLi.getValue());
						successCount++;
						failureCount--;
					}
					logger.info("{} 成功", reason);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("{} 失败", reason);
				}
			} else {
				logger.info("{} 失败，格式不正确", reason);
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
	public String losecwbBatch(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value="loseeffect",required=false,defaultValue="-1")long loseeffect) {
		List<Customer> cList = customerDao.getAllCustomers();// 获取供货商列表
		List<JSONObject> objList = new ArrayList<JSONObject>();
		long successCount = 0;
		long failCount=0;
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			JSONObject obj = new JSONObject();
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder co = cwbDao.getCwbByCwbLock(cwb);
				if (co == null) {
					throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
				}
				SystemInstall  systemInstall=systemInstallDAO.getSystemInstall("daohuoiseffect");
				if (systemInstall!=null) {
					if (systemInstall.getValue().equals("1")) {
						if (co.getFlowordertype()!=FlowOrderTypeEnum.DaoRuShuJu.getValue()&&co.getFlowordertype()!=FlowOrderTypeEnum.RuKu.getValue()) {
							throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.FeiDaorushujuandrukunotallowshixiao);
						}
					}else {
						if (co.getFlowordertype()!=FlowOrderTypeEnum.DaoRuShuJu.getValue()){
							throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.FeiDaorushujunotallowshixiao);

						}
					}
				}else {
					if (co.getFlowordertype()!=FlowOrderTypeEnum.DaoRuShuJu.getValue()){
						throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.FeiDaorushujunotallowshixiao);

					}
				}
			/*	if (orderFlowDAO.getOrderFlowByCwbAndFlowordertype(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), cwb).size() > 0) {
					throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.Fen_Zhan_Dao_Huo_Ding_Dan_Bu_Yun_Xu_Shi_Xiao);
				}*/
				cwbDao.dataLoseByCwb(cwb);
				exportwarhousesummaryDAO.dataLoseByCwb(cwb);
				exportwarhousesummaryDAO.LoseintowarhouseByCwb(cwb);
				transCwbDao.deleteTranscwb(cwb);
				// 失效订单删除
				cwborderService.deletecwb(cwb);
				// 删除倒车时间表的订单
				orderArriveTimeDAO.deleteOrderArriveTimeByCwb(cwb);
				// 删除审核为退货再投的订单
				orderBackCheckDAO.deleteOrderBackCheckByCwb(cwb);

				if (emaildateDao.getEmailDateById(co.getEmaildateid()) != null) {
					long cwbcount = emaildateDao.getEmailDateById(co.getEmaildateid()).getCwbcount() - 1;
					emaildateDao.editEditEmaildateForCwbcount(cwbcount, co.getEmaildateid());
				}
				shiXiaoDAO.creAbnormalOrdernew(co.getOpscwbid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), co.getCurrentbranchid(), co.getCustomerid(), cwb,
						co.getDeliverybranchid(), co.getFlowordertype(), co.getNextbranchid(), co.getStartbranchid(), getSessionUser().getUserid(),loseeffect,co.getCwbstate(),co.getEmaildate());
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
				CwbOrder cwbOrder = cwbDao.getCwbByCwb(cwb);
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
		List<Reason> shixiaoReasons=this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ShiXiaoReason.getValue());
		model.addAttribute("shixiaoreasons",shixiaoReasons);
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
	public String selectlosecwb(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "cwbstate", required = false, defaultValue = "-1") long cwbstate,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "-1") long flowordertype,
			@RequestParam(value = "userid", required = false, defaultValue = "-1") long userid,
			@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "isnow", required = false, defaultValue = "0") long isnow
			) {
		String customerids="";
		Page pageparm = new Page();
		List<ShiXiao> shixiaoList = new ArrayList<ShiXiao>();
		if (isnow==1) {

			String lastcwbs = "";
			StringBuffer str = new StringBuffer();
			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				} else {
					str.append("'" + cwb + "',");
				}
			}
			if (str.length() > 0) {
				lastcwbs = str.substring(0, str.length() - 1).toString();
			}
			customerids=cwborderService.getStrings(customerid);
			shixiaoList = shiXiaoDAO.getShiXiaoByCwbsAndCretime(page, lastcwbs, begindate, enddate,customerids,cwbstate,flowordertype,userid);
			pageparm = new Page(shiXiaoDAO.getShiXiaoByCwbsAndCretimeCount(lastcwbs, begindate, enddate,customerids,cwbstate,flowordertype,userid), page, Page.ONE_PAGE_NUMBER);
		
		}
		//if (begindate.length() > 0 || enddate.length() > 0 || cwbs.length() > 0) {}
		List<String> customeridlist=cwborderService.getList(customerid);
		model.addAttribute("customeridlist",customeridlist);
		model.addAttribute("shixiaoList", shixiaoList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("customerList", customerDao.getAllCustomers());
		model.addAttribute("userList", userDAO.getAllUser());
		model.addAttribute("page", page);
		return "/cwborder/selectlosecwb";
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {
		ExportExcelMethod(response, request);
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void ExportExcelMethod(HttpServletResponse response, HttpServletRequest request) {
		String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if (mouldfieldids2 != null && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs("0");
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

			final String sql = cwbDao.getSQLExportKeFu(exportcwbs);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = userDAO.getAllUser();
					final Map<Long, Customer> cMap = customerDao.getAllCustomersToMap();
					final List<Branch> bList = branchDAO.getAllBranches();
					final List<Common> commonList = commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = remarkDAO.getRemarkByCwbs(exportcwbs);
					final Map<String, Map<String, String>> remarkMap = exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = reasonDAO.getAllReason();
					jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = columnMapRowMapper.mapRow(rs, count);
							recordbatch.add(mapRow);
							count++;
							if (count % 100 == 0) {
								writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints((float) 15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList, cwbspayupidMap,
										complaintMap);
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
							writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = recordbatch.get(i).get("cwb").toString();
									writeSingle(recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), count - size + i, cwbspayupMsp, complaintMap);
								}
								recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
			e.printStackTrace();
		}
	}

	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	// 给CwbOrderView赋值
	public List<CwbOrderView> getCwbOrderView(List<String> scancwblist, List<CwbOrder> clist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList, List<Branch> branchList,
			List<User> userList, List<Reason> reasonList, List<Remark> remarkList) {
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
				cwbOrderView.setInstoreroomtime(ruku != null ? sdf.format(ruku) : "");// 入库时间
				cwbOrderView.setOutstoreroomtime(chukusaomiao != null ? sdf.format(chukusaomiao) : "");// 出库时间
				cwbOrderView.setInSitetime(daohuosaomiao != null ? sdf.format(daohuosaomiao) : "");// 到站时间
				cwbOrderView.setPickGoodstime(fenzhanlinghuo != null ? sdf.format(fenzhanlinghuo) : "");// 小件员领货时间
				cwbOrderView.setGobacktime(yifankui != null ? sdf.format(yifankui) : "");// 反馈时间
				cwbOrderView.setGoclasstime(yishenhe == null ? "" : sdf.format(yishenhe));// 归班时间
				cwbOrderView.setNowtime(zuixinxiugai != null ? sdf.format(zuixinxiugai) : "");// 最新修改时间
				cwbOrderView.setTuigonghuoshangchukutime(tuigonghuoshangchuku != null ? sdf.format(tuigonghuoshangchuku) : "");// 退供货商拒收返库时间
				cwbOrderView.setBackreason(c.getBackreason());
				cwbOrderView.setLeavedreasonStr(this.getQueryReason(reasonList, c.getLeavedreasonid()));// 滞留原因
				// cwbOrderView.setExpt_code(); //异常编码
				cwbOrderView.setOrderResultType(c.getDeliverid());
				cwbOrderView.setPodremarkStr(this.getQueryReason(reasonList, this.getDeliveryStateByCwb(c.getCwb()).getPodremarkid()));// 配送结果备注
				//新加----lx
				cwbOrderView.setBackreasonid(c.getBackreasonid());
				
				cwbOrderView.setCartype(c.getCartype());
				cwbOrderView.setCwbdelivertypeid(c.getCwbdelivertypeid());
				cwbOrderView.setInwarhouseremark(exportService.getInwarhouseRemarks(remarkList).get(c.getCwb()) == null ? "" : exportService.getInwarhouseRemarks(remarkList).get(c.getCwb())
						.get(ReasonTypeEnum.RuKuBeiZhu.getText()));
				cwbOrderView.setCwbordertypeid(c.getCwbordertypeid() + "");// 订单类型
				cwbOrderView.setHandleperson(c.getHandleperson());
				cwbOrderView.setHandlereason(c.getHandlereason());
				cwbOrderView.setHandleresult(c.getHandleresult());

				if (deliverystate != null) {
					cwbOrderView.setSigninman(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? c.getConsigneename() : "");
					cwbOrderView.setSignintime(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? (yifankui != null ? sdf.format(yifankui) : "") : "");
					cwbOrderView.setPosremark(deliverystate.getPosremark());
					cwbOrderView.setCheckremark(deliverystate.getCheckremark());
					cwbOrderView.setDeliverstateremark(deliverystate.getDeliverstateremark());
					cwbOrderView.setCustomerbrackhouseremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), "", "").getComment());
					cwbOrderView.setDeliverystate(deliverystate.getDeliverystate());
					if (deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() && yifankui != null) {
						cwbOrderView.setSendSuccesstime(sdf.format(yifankui));// 配送成功时间
					} else if ((deliverystate.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate.getDeliverystate() == DeliveryStateEnum.JuShou.getValue())
							&& yifankui != null) {
						cwbOrderView.setJushoutime(sdf.format(yifankui));// 拒收时间
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
		orderflowList = orderFlowDAO.getAdvanceOrderFlowByCwb(cwb);
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
		deliveryStateList = deliveryStateDAO.getDeliveryStateByCwb(cwb);
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
		orderflowList = orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, flowordertype, begindate, enddate);
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
	}

	// ---------------------修改订单状态-----END--------------
	// ========================乐峰上门换订单打印面单==============================
	@RequestMapping("/selectforkfsmh/{page}")
	public String selectforkfsmh(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "printType", required = false, defaultValue = "-1") long printType, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "leixing", required = false, defaultValue = "0") long leixing) {
		List<Branch> bList = branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		Branch nowbranch = branchDAO.getBranchById(getSessionUser().getBranchid());

		if (nowbranch != null && nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {// 站点角色
			branchid = getSessionUser().getBranchid();
			bList = new ArrayList<Branch>();
			bList.add(nowbranch);
		}
		model.addAttribute("branchlist", bList);
		model.addAttribute("customerlist", customerDao.getAllCustomers());
		List<String> customeridList = dataStatisticsService.getList(customerid);
		model.addAttribute("customeridStr", customeridList);
		if (isshow != 0) {
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			String customerids = dataStatisticsService.getStrings(customerid);

			List<CwbOrder> quhuolist = cwbDao.getCwbOrderByCwbordertypeidAndBranchid(page, leixing, branchid, customerids, printType, begindate, enddate);
			model.addAttribute("cwbList", quhuolist);
			model.addAttribute("page_obj", new Page(cwbDao.getCwbOrderCount(leixing, branchid, customerids, printType, begindate, enddate), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
			model.addAttribute("userlist", userDAO.getAllUser());
		}

		return "/cwborder/selectforkfsmh";
	}

	@RequestMapping("/selectforsmhbdprint")
	public String selectforsmhbdprint(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint,
			@RequestParam(value = "modal", defaultValue = "0", required = false) long modal) {
		if (modal == 3) {
			return selectforlefengwang(model, isprint);
		}
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}
		List<CwbOrder> clist = cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));
		List<Customer> customerlist = customerDao.getAllCustomers();

		SystemInstall companyName = systemInstallDAO.getSystemInstallByName("CompanyName");
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
		logger.info("修改订单功能 [" + type + "][{}] cwb: {}", getSessionUser().getRealname(), cwbs);
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
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
		List<CwbOrder> cwbList = cwbDao.getCwbByCwbs(cwbsSql);

		// 做重置审核状态更改的操作 start
		List<CwbOrder> allowCwb = new ArrayList<CwbOrder>();// 允许更改订单
		List<CwbOrder> prohibitedCwb = new ArrayList<CwbOrder>(); // 禁止更改的订单
		for (CwbOrder co : cwbList) {
			// 判断订单当前状态为36 已审核状态的订单才能重置审核状态
			if (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				// 判断订单号是否为POS刷卡 posremark=POS刷卡 POS刷卡的订单不允许重置审核状态
				DeliveryState ds = deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).get(0);
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
			@RequestParam(value = "cwbtypeid", defaultValue = "0", required = false) String cwbtypeid,
			@RequestParam(value = "customerid", defaultValue = "0", required = false) String customerid,
			@RequestParam(value = "branchid", defaultValue = "0", required = false) String branchid,
			@RequestParam(value = "auditstate", defaultValue = "0", required = false) String auditstate,
			@RequestParam(value = "begindate", defaultValue = "0", required = false) String begindate,
			@RequestParam(value = "enddate", defaultValue = "0", required = false) String enddate
			) {
		String quot = "'", quotAndComma = "',";
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
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
				String lastcwb = cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr);
					cwborderlist.add(co);
				}
			}

			cwblist = cwbDao.getCwbByCwbs(cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "--");

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
			cwbDao.updateCwbStateByIds(ids, CwbStateEnum.ZhongZhuan);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String datetime = df.format(date);
			
			cwbApplyZhongZhuanDAO.updateCwbApplyZhongZhuanForIds(datetime, getSessionUser().getUserid(),  1, ids);
			logger.info("订单id{}审核为中转件", ids);

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
			cwbDao.updateFlowordertypeByIds(ids, CwbStateEnum.ZhongZhuan);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String datetime = df.format(date);
			
			cwbApplyZhongZhuanDAO.updateCwbApplyZhongZhuanForIds(datetime, getSessionUser().getUserid(),  1, ids);
			logger.info("订单id{}审核为中转件", ids);

			return "{\"errorCode\":0,\"error\":\"审核成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}

	}

}