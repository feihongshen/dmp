package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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

import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.PrintcwbDetailDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.GroupDetail;
import cn.explink.domain.OutWarehouseGroup;
import cn.explink.domain.PrintView;
import cn.explink.domain.PrintcwbDetail;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.OutWarehouseGroupEnum;
import cn.explink.enumutil.OutwarehousegroupOperateEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.PrintTemplateOpertatetypeEnum;
import cn.explink.print.template.PrintTemplateDAO;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbRouteService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.WarehouseGroupDetailService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@Controller
@RequestMapping("/warehousegroup")
public class WarehouseGroupController {

	@Autowired
	OutWarehouseGroupDAO outwarehousegroupDao;
	@Autowired
	BranchDAO branchDao;
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	EmailDateDAO emailDateDao;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	PrintcwbDetailDAO printcwbDetailDAO;
	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	GroupDetailDao groupDetailDao;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	PrintTemplateDAO printTemplateDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	WarehouseGroupDetailService warehouseGroupDetailService;
	@Autowired
	ComplaintDAO complaintDAO;

	@Autowired
	PayWayDao payWayDao;
	
	@Autowired
	BaleDao baleDAO;

	private Logger logger = LoggerFactory.getLogger(WarehouseGroupController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 查询入库信息
	 *
	 * @param page
	 * @param model
	 * @param userid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/inlist/{page}")
	public String inlist(@PathVariable("page") long page, Model model, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<OutWarehouseGroup> owgAllList = null;
		owgAllList = this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, this.getSessionUser().getBranchid(), beginemaildate, endemaildate, userid, OutwarehousegroupOperateEnum.RuKu.getValue(), 0,
				this.getSessionUser().getBranchid());

		model.addAttribute("owgList", owgAllList);
		model.addAttribute("driverList", this.userDAO.getUserByRole(3));
		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(this.getSessionUser().getBranchid(), beginemaildate, endemaildate, userid, OutwarehousegroupOperateEnum.RuKu.getValue(), 0,
						this.getSessionUser().getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/inlist";
	}

	/**
	 * 入库交接单信息打印和查询
	 *
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/inlistprint/{outwarehousegroupid}")
	public String inlistprint(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		OutWarehouseGroup owg = this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		if (owg.getPrinttime().equals("")) {
			this.outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
			this.outwarehousegroupDao.saveOutWarehouseById(OutWarehouseGroupEnum.FengBao.getValue(), outwarehousegroupid);
		}
		model.addAttribute("cwborderlist", this.cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		return "warehousegroup/inbillprinting_default";
	}

	private List<Branch> getNextPossibleBranches() {
		List<Branch> bList = new ArrayList<Branch>();
		for (long i : this.cwbRouteService.getNextPossibleBranch(this.getSessionUser().getBranchid())) {
			bList.add(this.branchDAO.getBranchByBranchid(i));
		}
		return bList;
	}

	/**
	 * 分站到货信息查询
	 *
	 * @param model
	 * @param page
	 * @param userid
	 * @return
	 */
	@RequestMapping("/inboxlist/{page}")
	public String inboxPrint(Model model, @PathVariable("page") long page, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid) {
		List<OutWarehouseGroup> owgAllList = null;
		model.addAttribute("driverList", this.userDAO.getUserByRole(3));
		// if(userid!=0){
		owgAllList = this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, this.getSessionUser().getBranchid(), "", "", userid, OutwarehousegroupOperateEnum.FenZhanDaoHuo.getValue(), 0, this.getSessionUser()
				.getBranchid());
		// }

		model.addAttribute("owgAllList", owgAllList);
		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(this.getSessionUser().getBranchid(), "", "", userid, OutwarehousegroupOperateEnum.FenZhanDaoHuo.getValue(), 0, this.getSessionUser()
						.getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/inboxlist";
	}

	/**
	 * 分站到货交接单信息打印和查询
	 *
	 * @param outwarehousegroupid
	 * @param model
	 * @return
	 */
	@RequestMapping("/inboxsearchAndPrint/{outwarehousegroupid}")
	public String searchDetail(@PathVariable("outwarehousegroupid") long outwarehousegroupid, Model model) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		OutWarehouseGroup owg = this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		if (owg.getPrinttime().equals("")) {
			this.outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
			this.outwarehousegroupDao.saveOutWarehouseById(OutWarehouseGroupEnum.FengBao.getValue(), outwarehousegroupid);
		}

		model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("cwbAllDetail", this.cwbDao.getCwbByGroupid(outwarehousegroupid));
		return "warehousegroup/inboxbillprinting_default";
	}

	/**
	 * 小件员领货信息查询
	 *
	 * @param model
	 * @param page
	 * @param userid
	 * @return
	 */
	@RequestMapping("/deliverlist/{page}")
	public String deliverList(Model model, @PathVariable("page") long page, @RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "begintime", required = false, defaultValue = "") String begintime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		String roleids = "2,4";
		List<User> deliverList = this.userDAO.getDeliveryUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());
		List<PrintView> printList = new ArrayList<PrintView>();
		model.addAttribute("deliverList", deliverList);
		if (isshow > 0) {
			begintime = begintime.length() == 0 ? DateTimeUtil.getNowDate() + " 00:00:00" : begintime;
			endtime = endtime.length() == 0 ? DateTimeUtil.getNowDate() + " 23:59:59" : endtime;
			List<GroupDetail> gdList = this.groupDetailDao.getCwbForLingHuoPrint(deliverid, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), begintime, endtime);
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			String cwbs = "";
			for (GroupDetail deliveryZhiLiu : gdList) {
				cwbs += "'" + deliveryZhiLiu.getCwb() + "',";
			}
			cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
			if (cwbs.length() > 0) {
				orderlist = this.cwbDao.getCwbOrderByCwbs(cwbs);
			}
			List<Customer> customerList = this.customerDAO.getAllCustomers();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			printList = this.warehouseGroupDetailService.getChuKuView(orderlist, gdList, customerList, branchList);
		}
		model.addAttribute("begintime", begintime);
		model.addAttribute("endtime", endtime);
		model.addAttribute("printList", printList);

		model.addAttribute("page", page);

		model.addAttribute("printtemplateList",
				this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.LingHuoAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.LingHuoHuiZong.getValue()));
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));

		return "warehousegroup/deliverlist";

	}

	/**
	 * 小件员领货交接单信息打印和查询
	 *
	 * @param outwarehousegroupid
	 * @param model
	 * @return
	 */
	@RequestMapping("/searchAndPrintFordeliver")
	public String searchDetailToDeliver(@RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint, Model model) {
		String cwbs = "", cwbstr = "";
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbstr += isprint[i] + ",";
		}
		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}

		List<CwbOrder> cwbList = this.cwbDao.getCwbByCwbs(cwbs);

		this.printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, this.getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.FenZhanLingHuo.getValue()));

		model.addAttribute("cwbList", cwbList);
		model.addAttribute("exceldeliver", this.userDAO.getUserByUserid(cwbList.get(0).getDeliverid()).getRealname());
		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());

		model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		String roleids = "2,4";
		List<User> deliverList = this.userDAO.getDeliveryUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());
		model.addAttribute("deliverList", deliverList);
		return "warehousegroup/deliverbillprinting_xhm";
	}

	/**
	 * 历史小件员领货信息查询
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/historydeliverlist/{page}")
	public String historydeliverlist(Model model, @PathVariable("page") long page, @RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		model.addAttribute("outwarehousegroupList", this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, 0, beginemaildate, endemaildate, deliverid,
				OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue(), 0, this.getSessionUser().getBranchid()));
		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(0, beginemaildate, endemaildate, deliverid, OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue(), 0, this.getSessionUser()
						.getBranchid()), page, Page.ONE_PAGE_NUMBER));

		model.addAttribute("page", page);
		String roleids = "2,4";
		List<User> deliverList = this.userDAO.getDeliveryUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());

		model.addAttribute("deliverList", deliverList);
		model.addAttribute("printtemplateList",
				this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.LingHuoAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.LingHuoHuiZong.getValue()));
		return "warehousegroup/historydeliverlist";
	}

	/**
	 * 中转出站查询
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/changelist/{page}")
	public String changelist(Model model, @PathVariable("page") long page, @RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		long branchid = this.getSessionUser().getBranchid();
		model.addAttribute("outwarehousegroupList", this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0,
				OutwarehousegroupOperateEnum.ZhongZhuanChuKu.getValue(), 0, this.getSessionUser().getBranchid()));
		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ZhongZhuanChuKu.getValue(), 0, this.getSessionUser()
						.getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/changelist";
	}

	/**
	 * 中转出站交接单打印和查询
	 *
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/changebillprinting_default/{outwarehousegroupid}")
	public String changebillprinting_default(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		/*
		 * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * Date date = new Date(); String datetime = df.format(date);
		 */

		OutWarehouseGroup owg = this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		/*
		 * if(owg.getPrinttime().equals("")){
		 * outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		 * outwarehousegroupDao
		 * .saveOutWarehouseById(OutWarehouseGroupEnum.FengBao
		 * .getValue(),outwarehousegroupid); }
		 */

		List<JSONObject> cwbJson = this.cwbDao.getDetailForPrint(outwarehousegroupid);

		model.addAttribute("owg", owg);
		model.addAttribute("cwbJson", cwbJson);
		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());

		model.addAttribute("cwborderlist", this.cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/changebillprinting_hmj";
	}

	/**
	 * 退货出站信息查询
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/returnlist/{page}")
	public String returnlist(Model model, @PathVariable("page") long page, @RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		long branchid = this.getSessionUser().getBranchid();
		model.addAttribute("outwarehousegroupList", this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0,
				OutwarehousegroupOperateEnum.TuiHuoChuZhan.getValue(), 0, this.getSessionUser().getBranchid()));
		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.TuiHuoChuZhan.getValue(), 0, this.getSessionUser()
						.getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/returnlist";
	}

	/**
	 * 退货出站交接单打印和查询
	 *
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/returnbillprinting_default/{outwarehousegroupid}")
	public String returnbillprinting_default(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		/*
		 * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * Date date = new Date(); String datetime = df.format(date);
		 */
		OutWarehouseGroup owg = this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		/*
		 * if(owg.getPrinttime().equals("")){
		 * outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		 * outwarehousegroupDao
		 * .saveOutWarehouseById(OutWarehouseGroupEnum.FengBao
		 * .getValue(),outwarehousegroupid); }
		 */

		List<JSONObject> cwbJson = this.cwbDao.getDetailForPrint(outwarehousegroupid);

		model.addAttribute("owg", owg);
		model.addAttribute("cwbJson", cwbJson);
		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());

		model.addAttribute("cwborderlist", this.cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/returnbillprinting_hmj";
	}

	/**
	 * 中转站入库查询
	 *
	 * @param page
	 * @param model
	 * @param userid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/changeinlist/{page}")
	public String changeinlist(@PathVariable("page") long page, Model model, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<OutWarehouseGroup> owgAllList = null;
		// if(userid!=0){
		owgAllList = this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, this.getSessionUser().getBranchid(), beginemaildate, endemaildate, userid,
				OutwarehousegroupOperateEnum.ZhongZhuanZhanRuKu.getValue(), 0, this.getSessionUser().getBranchid());
		// }
		model.addAttribute("owgList", owgAllList);
		model.addAttribute("driverList", this.userDAO.getUserByRole(3));
		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(this.getSessionUser().getBranchid(), beginemaildate, endemaildate, userid,
						OutwarehousegroupOperateEnum.ZhongZhuanZhanRuKu.getValue(), 0, this.getSessionUser().getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/changeinlist";
	}

	/**
	 * 中转站入库交接单打印和查询
	 *
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/changeinlistprint/{outwarehousegroupid}")
	public String changeinlistprint(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		OutWarehouseGroup owg = this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		if (owg.getPrinttime().equals("")) {
			this.outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
			this.outwarehousegroupDao.saveOutWarehouseById(OutWarehouseGroupEnum.FengBao.getValue(), outwarehousegroupid);
		}
		model.addAttribute("cwborderlist", this.cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		return "warehousegroup/changeinbillprinting_default";
	}

	/**
	 * 退货站入库查询
	 *
	 * @param page
	 * @param model
	 * @param userid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/backinlist/{page}")
	public String backinlist(@PathVariable("page") long page, Model model, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<OutWarehouseGroup> owgAllList = null;
		// if(userid!=0){
		owgAllList = this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, this.getSessionUser().getBranchid(), beginemaildate, endemaildate, userid,
				OutwarehousegroupOperateEnum.TuiHuoZhanRuku.getValue(), 0, this.getSessionUser().getBranchid());
		// }
		model.addAttribute("owgList", owgAllList);
		model.addAttribute("driverList", this.userDAO.getUserByRole(3));
		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(this.getSessionUser().getBranchid(), beginemaildate, endemaildate, userid, OutwarehousegroupOperateEnum.TuiHuoZhanRuku.getValue(),
						0, this.getSessionUser().getBranchid()), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/backinlist";
	}

	/**
	 * 退货站入库交接单打印和查询
	 *
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/backinlistprint/{outwarehousegroupid}")
	public String backinlistprint(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		OutWarehouseGroup owg = this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		if (owg.getPrinttime().equals("")) {
			this.outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
			this.outwarehousegroupDao.saveOutWarehouseById(OutWarehouseGroupEnum.FengBao.getValue(), outwarehousegroupid);
		}
		model.addAttribute("cwborderlist", this.cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		return "warehousegroup/backinbillprinting_default";
	}

	/**
	 * 退供货商出库查询
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/backtocustomerlist/{page}")
	public String backtocustomerlist(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<PrintView> printList = new ArrayList<PrintView>();
		if (isshow > 0) {
			List<GroupDetail> gdList = this.groupDetailDao.getCwbForTuiGongYingShangPrint(customerid, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			String cwbs = "";
			for (GroupDetail deliveryZhiLiu : gdList) {
				cwbs += "'" + deliveryZhiLiu.getCwb() + "',";
			}
			cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
			if (cwbs.length() > 0) {
				orderlist = this.cwbDao.getCwbOrderByCwbs(cwbs);
			}

			List<Branch> branchList = this.branchDAO.getAllBranches();
			printList = this.warehouseGroupDetailService.getChuKuView(orderlist, gdList, customerList, branchList);
		}

		model.addAttribute("printList", printList);
		model.addAttribute(
				"printtemplateList",
				this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.TuiGongYingShangChuKuAnDan.getValue() + ","
						+ PrintTemplateOpertatetypeEnum.TuiGongYingShangChuKuHuiZong.getValue()+","+PrintTemplateOpertatetypeEnum.TongLuTuiHuoShangChuKu.getValue()));
		model.addAttribute("customerlist", customerList);
		return "warehousegroup/backtocustomerlist";
	}

	/**
	 * 退供货商出库信息交接单打印和查询
	 *
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/backtocustomerbillprinting_default")
	public String backtocustomerbillprinting_default(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint,
			@RequestParam(value = "isback", defaultValue = "", required = true) String isback, @RequestParam(value = "iscustomer", required = false, defaultValue = "0") long iscustomer,
			@RequestParam(value = "printtemplateid", defaultValue = "", required = false) long printtemplateid) {
		iscustomer = 1;//设置基础值
		String cwbs = "", cwbstr = "",customerids="";
		CwbOrder co = null;
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbstr += isprint[i] + ",";
			co = this.cwbDao.getCwbByCwb(isprint[i]);//
		}
		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}

		List<CwbOrder> cwbList = this.cwbDao.getCwbByCwbs(cwbs);
		//查询退货站入库时间
		//得到list 的String
		List<String> cwbStrings=new ArrayList<String>();

		for (CwbOrder cwbOrder : cwbList) {
			cwbStrings.add(cwbOrder.getCwb());
			customerids+="'" + cwbOrder.getCustomerid() + "',";
		}
		//如果长度大于0 表示有值 进行截取
		if (customerids.length() > 0) {
			customerids = customerids.substring(0, customerids.length() - 1);
		}


		//查询退货信息
		List<TuihuoRecord> tuihuoRecordList=this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbStrings);
		List<Customer> customerList=this.customerDAO.getCustomerByIds(customerids);
		//查询支付方式
		//List<PayWay> paywayList=payWayDao.getpaywayby
		List<OrderFlow> flowList = this.orderFlowDAO.getCwbByFlowordertypeAndCwbs(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), cwbs);
		Map<String, String> mapForOperatorName = new HashMap<String, String>();
		for (OrderFlow of : flowList) {
			mapForOperatorName.put(of.getCwb(), this.userDAO.getUserByUserid(of.getUserid()).getRealname());
		}
		model.addAttribute("map", mapForOperatorName);
		this.printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, this.getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()));

		model.addAttribute("isback", isback);
		model.addAttribute("iscustomer", iscustomer);
		model.addAttribute("branch",this.branchDao.getBranchById(this.getSessionUser().getBranchid()));
		model.addAttribute("template", this.printTemplateDAO.getPrintTemplate(printtemplateid));

		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("branchlist", this.branchDAO.getAllEffectBranches());
		model.addAttribute("nextbranchid", co.getNextbranchid());

		if (this.printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 1) {

			model.addAttribute("cwbList", cwbList);
			return "warehousegroup/outbillprinting_template";
		} else if (this.printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 2) {

			List<JSONObject> cwbJson = this.cwbDao.getDetailForChuKuPrint(cwbs);

			model.addAttribute("cwbs", cwbs);
			model.addAttribute("cwbList", cwbJson);
			return "warehousegroup/outbillhuizongprinting_template";
		}else if(this.printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 5){
			List<JSONObject> cwbJson = this.cwbDao.getDetailForChuKuPrint(cwbs);
			List<WarehouseGroupPrintDto> printDtos=new ArrayList<WarehouseGroupPrintDto>();

			for (int i = 0; i < cwbStrings.size(); i++) {
				WarehouseGroupPrintDto warehouseGroupPrintDto=new WarehouseGroupPrintDto();
				warehouseGroupPrintDto.setBackcarnum(cwbList.get(i).getBackcarnum());
				warehouseGroupPrintDto.setCaramount(cwbList.get(i).getCaramount());
				warehouseGroupPrintDto.setCarrealweight(cwbList.get(i).getCarrealweight());
				warehouseGroupPrintDto.setCarsize(cwbList.get(i).getCarsize());
				warehouseGroupPrintDto.setCarwarehouse(this.branchDao.getBranchById(Long.parseLong(cwbList.get(i).getCarwarehouse())).getBranchname());
				warehouseGroupPrintDto.setConsigneeaddress(cwbList.get(i).getConsigneeaddress());
				warehouseGroupPrintDto.setConsigneemobile(cwbList.get(i).getConsigneemobile());
				warehouseGroupPrintDto.setConsigneename(cwbList.get(i).getConsigneename());
				warehouseGroupPrintDto.setConsigneephone(cwbList.get(i).getConsigneephone());
				warehouseGroupPrintDto.setConsigneepostcode(cwbList.get(i).getConsigneepostcode());
				warehouseGroupPrintDto.setCustomername(this.customerDAO.getCustomerById(cwbList.get(i).getCustomerid()).getCustomername());
				warehouseGroupPrintDto.setCwb(cwbStrings.get(i));
				warehouseGroupPrintDto.setCwbordertypeid(CwbOrderTypeIdEnum.getByValue(cwbList.get(i).getCwbordertypeid()).getText());
				if(cwbList.get(i).getCwbremark().length()>=30){
					warehouseGroupPrintDto.setCwbremark(cwbList.get(i).getCwbremark().substring(0, 30));
				}else {
					warehouseGroupPrintDto.setCwbremark(cwbList.get(i).getCwbremark());
				}
				warehouseGroupPrintDto.setEmaildate(cwbList.get(i).getEmaildate());
				warehouseGroupPrintDto.setPaybackfee(cwbList.get(i).getPaybackfee());
				warehouseGroupPrintDto.setTuihuozhanrukutime(this.tuihuoRecordDAO.getTuihuoRecordByCwb(cwbList.get(i).getCwb()).get(0).getTuihuozhanrukutime());
				warehouseGroupPrintDto.setTranscwb(cwbList.get(i).getTranscwb());
				warehouseGroupPrintDto.setPaywayid(PaytypeEnum.getByValue(Integer.parseInt(cwbList.get(i).getPaywayid()+"")).getText());
				if(cwbList.get(i).getBackreasonid()==0){
					warehouseGroupPrintDto.setReasoncontent("无");
				}else{
					if(this.reasonDAO.getReasonByReasonid(cwbList.get(i).getBackreasonid()).getReasoncontent().length()>=30){
						warehouseGroupPrintDto.setReasoncontent(this.reasonDAO.getReasonByReasonid(cwbList.get(i).getBackreasonid()).getReasoncontent().substring(0, 30));
					}else {
						warehouseGroupPrintDto.setReasoncontent(this.reasonDAO.getReasonByReasonid(cwbList.get(i).getBackreasonid()).getReasoncontent());
					}
				}
				warehouseGroupPrintDto.setCredate(this.orderFlowDAO.getOrderCurrentFlowByCwb(cwbList.get(i).getCwb()).getCredate().toString());
				warehouseGroupPrintDto.setReceivablefee(cwbList.get(i).getReceivablefee());
				warehouseGroupPrintDto.setSendcarname(cwbList.get(i).getSendcarname());
				warehouseGroupPrintDto.setSendcarnum(cwbList.get(i).getScannum());
				warehouseGroupPrintDto.setStartbranch(this.branchDao.getBranchByBranchid(cwbList.get(i).getStartbranchid()).getBranchname());
				warehouseGroupPrintDto.setBackcarname(cwbList.get(i).getBackcarname());
				printDtos.add(warehouseGroupPrintDto);
			}
			model.addAttribute("dto",printDtos);
			model.addAttribute("cwbs", cwbs);
			model.addAttribute("cwbList", cwbJson);
			model.addAttribute("tuihuoRecordList",tuihuoRecordList);
			model.addAttribute("iscustomer",2L);
			model.addAttribute("cwbOrderList",cwbList);
			return "warehousegroup/outtongluprintint_template";
		}
		return null;
	}

	/**
	 * 历史退供货商出库信息查询
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/historybacktocustomerlist/{page}")
	public String historybacktocustomerlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		model.addAttribute("outwarehousegroupList", this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0,
				OutwarehousegroupOperateEnum.TuiGongYingShangChuKu.getValue(), 0, this.getSessionUser().getBranchid()));
		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.TuiGongYingShangChuKu.getValue(), 0, this.getSessionUser()
						.getBranchid()), page, Page.ONE_PAGE_NUMBER));

		List<Customer> cList = this.customerDAO.getAllCustomers();

		model.addAttribute(
				"printtemplateList",
				this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.TuiGongYingShangChuKuAnDan.getValue() + ","
						+ PrintTemplateOpertatetypeEnum.TuiGongYingShangChuKuHuiZong.getValue()+","+PrintTemplateOpertatetypeEnum.TongLuTuiHuoShangChuKu.getValue()));
		model.addAttribute("customerlist", cList);
		model.addAttribute("page", page);
		return "warehousegroup/historybacktocustomerlist";
	}

	@RequestMapping("/owgtofengbao/{outwarehousegroupid}")
	public void owgtofengbao(@PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		OutWarehouseGroup owg = this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		if (owg.getPrinttime().equals("")) {
			if (owg.getOperatetype() == OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()) {
				this.outwarehousegroupDao.savePrinttimeAndState(datetime, OutWarehouseGroupEnum.FengBao.getValue(), outwarehousegroupid);
			} else {
				this.outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
				this.outwarehousegroupDao.saveOutWarehouseById(OutWarehouseGroupEnum.FengBao.getValue(), outwarehousegroupid);
			}
		}
	}

	@RequestMapping("/creowg/{branchid}")
	public @ResponseBody String creowg(@PathVariable("branchid") long branchid, @RequestParam(value = "operatetype", required = false, defaultValue = "0") long operatetype,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs) {
		try {
			if (cwbs.trim().length() > 0) {
				if (branchid == 0) {
					branchid = this.getSessionUser().getBranchid();
				}
				String[] strs=cwbs.split(",");
				String cwbsString="";
				for (int i = 0; i < strs.length; i++) {
					String string =strs[i].replaceAll("'","");
					cwbsString=cwbsString+"-H-"+string;
				}
				cwbsString=cwbsString.substring(3);


				this.cwbOrderService.checkResponseBatchno(this.getSessionUser(), 0, branchid, driverid, 0, OutWarehouseGroupEnum.FengBao.getValue(), operatetype, cwbsString, customerid, "");
				return "{\"errorCode\":0,\"error\":\"成功\"}";
			} else {
				return "{\"errorCode\":1,\"error\":\"错误,没有订单号\"}";
			}
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"错误" + e.getMessage() + "\"}";
		}

	}

	// /出库打印 站点出站打印 中转出站 打印
	@RequestMapping("/creowgnew")
	public @ResponseBody String creowgnew(@RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "operatetype", required = false, defaultValue = "0") long operatetype,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid,
			@RequestParam(value = "baleno", required = false, defaultValue = "") String baleno,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid,
			@RequestParam(value = "baleid", required = false, defaultValue = "0") long baleid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		try {
			if (cwbs.trim().length() > 0) {
				String[] cwbsList = cwbs.split("-HH-");
				List<JSONObject> jsonList = new ArrayList<JSONObject>();
				List<Long> branchList = new ArrayList<Long>();
				// cwb-H-nextbranchid-HH-
				// 把每一个cwb数据封装成一个json对象
				for (String s : cwbsList) {
					JSONObject json = new JSONObject();
					String[] jStrings = s.split("-H-");
					json.put("cwb", jStrings[0]);
					json.put("nextbranchid", Long.parseLong(jStrings[1]));
					jsonList.add(json);
					if (!branchList.contains(Long.parseLong(jStrings[1]))) {
						branchList.add(Long.parseLong(jStrings[1]));
					}
				}

				// 按照下一站 分成不同的 cwbs
				Map<Long, String> branchAndCwbs = new HashMap<Long, String>();
				for (Long branchid : branchList) {
					StringBuffer sbf = new StringBuffer();
					for (JSONObject jsonObject : jsonList) {
						if (jsonObject.getLong("nextbranchid") == branchid) {
							sbf.append(jsonObject.getString("cwb")).append("-H-");
						}
					}
					branchAndCwbs.put(branchid, sbf.toString());
				}
				// 记录入库起始时间 add by chunlei05.li 2016/8/12
				String outstockTime = "";
				if (StringUtils.isNotBlank(strtime) && StringUtils.isNotBlank(endtime)) {
					Map<String, String> timeMap = new HashMap<String, String>();
					timeMap.put("strtime", strtime);
					timeMap.put("endtime", endtime);
					JSONObject jsonObject = JSONObject.fromObject(timeMap);
					outstockTime = jsonObject.toString();
				}
				for (Long branchid : branchList) {
					if((null!=baleno)&&(baleno.length()>0)){
						Bale bale=null;
						if(baleid>0){
							bale=this.baleDAO.getBaleById(baleid);
						}
						if(bale!=null){
							long outwarehousegroupid=this.cwbOrderService.checkResponseBatchnoForBale(this.getSessionUser(), 0, branchid, driverid, truckid, OutWarehouseGroupEnum.FengBao.getValue(), operatetype, branchAndCwbs.get(branchid), 0 ,bale.getId(), outstockTime);
							this.outwarehousegroupDao.updateOutwarehousegroupBalenoByID(baleno,bale.getId(), outwarehousegroupid);
						}else{
							logger.info("此包号已失效，不能插入到express_ops_outwarehousegroup表，baleno="+baleno+",baleid="+baleid);
						}
					}else{
						long outwarehousegroupid=this.cwbOrderService.checkResponseBatchno(this.getSessionUser(), 0, branchid, driverid, truckid, OutWarehouseGroupEnum.FengBao.getValue(), operatetype, branchAndCwbs.get(branchid), 0, outstockTime);
					}
//					if((null!=baleno)&&(baleno.length()>0)){
//					}
				}
				return "{\"errorCode\":0,\"error\":\"成功\"}";
			} else {
				return "{\"errorCode\":1,\"error\":\"错误,没有订单号\"}";
			}
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"错误" + e.getMessage() + "\"}";
		}

	}

	/**
	 * 小件员领货打印导出功能
	 *
	 * @param model
	 * @param response
	 * @param request
	 * @param deliverid
	 * @param mouldfieldids2
	 */
	@RequestMapping("/exportExcleForDeliver")
	public void exportExcleForDeliver(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint,
			@RequestParam(value = "exportmould2", required = false, defaultValue = "") final String mouldfieldids2) {

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2) && !"".equals(mouldfieldids2)) { // 选择模板
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
		String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
		String otherName = "";
		String lastStr = ".xlsx";// 文件名后缀

		fileName = fileName + otherName + lastStr;
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
		try {
			final String sql = this.cwbDao.getSqlByCwb(cwbs);
			this.logger.info(sql);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = WarehouseGroupController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = WarehouseGroupController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = WarehouseGroupController.this.branchDAO.getAllBranches();
					final List<Common> commonList = WarehouseGroupController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = WarehouseGroupController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = WarehouseGroupController.this.remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = WarehouseGroupController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = WarehouseGroupController.this.reasonDAO.getAllReason();
					WarehouseGroupController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = WarehouseGroupController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList, cwbspayupidMap,
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
								Map<String, Map<String, String>> orderflowList = WarehouseGroupController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : WarehouseGroupController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : WarehouseGroupController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : WarehouseGroupController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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

}
