package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

import cn.explink.core.bean.BeanConverter;
import cn.explink.core.utils.StringUtils;
import cn.explink.dao.BaleCwbDao;
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
import cn.explink.dao.PrintcwbDetailDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TruckDAO;
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
import cn.explink.domain.OutChangePrintVo;
import cn.explink.domain.OutWarehouseGroup;
import cn.explink.domain.PrintView;
import cn.explink.domain.PrintcwbDetail;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.Truck;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.WuhanTransportDetailVo;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.OutWarehouseGroupEnum;
import cn.explink.enumutil.OutwarehousegroupOperateEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.PrintTemplateOpertatetypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.print.template.PrintTemplate;
import cn.explink.print.template.PrintTemplateDAO;
import cn.explink.service.BranchService;
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
@RequestMapping("/warehousegroupdetail")
public class WarehouseGroup_detailController {

	@Autowired
	OutWarehouseGroupDAO outwarehousegroupDao;
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
	SystemInstallDAO systemInstallDAO;
	@Autowired
	PrintTemplateDAO printTemplateDAO;
	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	GroupDetailDao groupDetailDao;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	WarehouseGroupDetailService warehouseGroupDetailService;

	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	BaleDao baleDAO;
	@Autowired
	TruckDAO truckDAO;
	@Autowired
	private BaleCwbDao baleCwbDao;
	@Autowired
	CwbOrderService cwborderService;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	
	@Autowired
	private BranchService branchService;
	
	private static Logger logger = LoggerFactory.getLogger(WarehouseGroup_detailController.class);

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
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate,
			@RequestParam(value = "onePageNumber", defaultValue = "10", required = false) long onePageNumber // 每页记录数
	) {
		List<User> userList = this.userDAO.getAllUserbybranchid(this.getSessionUser().getBranchid());

		model.addAttribute("userList", userList);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long day7 = (System.currentTimeMillis() / 1000) - (60 * 60 * 24 * 5);
		if ((beginemaildate.length() == 0) && (endemaildate.length() == 0)) {
			beginemaildate = df.format(new Date(day7 * 1000));
			endemaildate = df.format(new Date());
		}

		List<OrderFlow> oflist = this.orderFlowDAO.getOrderFlowByWhere(page, this.getSessionUser().getBranchid(), userid, FlowOrderTypeEnum.RuKu.getValue(), beginemaildate, endemaildate,
				onePageNumber);

		model.addAttribute("oflist", oflist);
		model.addAttribute("page_obj", new Page(this.orderFlowDAO.getOrderFlowCount(this.getSessionUser().getBranchid(), userid, FlowOrderTypeEnum.RuKu.getValue(), beginemaildate, endemaildate),
				page, onePageNumber));
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
	@RequestMapping("/inlistprint")
	public String inlistprint(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {

		String cwbs = "", cwbstr = "";
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbstr += isprint[i] + ",";
		}

		List<CwbOrder> cwbList = this.cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));

		this.printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, this.getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.RuKu.getValue()));

		SystemInstall si = this.systemInstallDAO.getSystemInstallByName("rukujiaojiedan");
		model.addAttribute("template", this.printTemplateDAO.getPrintTemplate(Long.parseLong(si.getValue())));
		model.addAttribute("cwbList", cwbList);
		// model.addAttribute("localbranchname",branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());

		// model.addAttribute("branchname",branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		return "warehousegroup/inbillprinting_template";
	}

	private List<Branch> getNextPossibleBranches() {
		List<Branch> bList = new ArrayList<Branch>();
		for (long i : this.cwbRouteService.getNextPossibleBranch(this.getSessionUser().getBranchid())) {
			Branch branch = this.branchDAO.getBranchByBranchid(i);
			if (branch.getBranchid() != 0) {
				bList.add(this.branchDAO.getBranchByBranchid(i));
			}
		}
		return bList;
	}

	/**
	 *
	 * @param model
	 * @param request
	 * @param isprint
	 * @param isback
	 * @param iscustomer
	 * @param islinghuo
	 * @param deliverid
	 * @param nextbranchid
	 * @param printtemplateid
	 * @return
	 */
	@RequestMapping("/outbillprinting_default")
	public String outbillprinting_default(Model model, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint,
			@RequestParam(value = "isback", defaultValue = "", required = true) String isback, @RequestParam(value = "iscustomer", required = false, defaultValue = "0") long iscustomer,
			@RequestParam(value = "islinghuo", defaultValue = "0", required = true) long islinghuo, @RequestParam(value = "currentdeliverid", defaultValue = "0", required = true) long deliverid,
			@RequestParam(value = "nextbranchid", defaultValue = "", required = false) String nextbranchid,
			@RequestParam(value = "printtemplateid", defaultValue = "", required = false) long printtemplateid,
			@RequestParam(value = "starttime", defaultValue = "", required = false) String starttime,
		    @RequestParam(value = "endtime", defaultValue = "", required = false) String endtime) {
		String cwbs = "", cwbstr = "", cwbhuizongstr = "";
		String[] cwbArr = new String[isprint.length];
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbstr += isprint[i] + ",";
			cwbArr[i] = isprint[i];
			cwbhuizongstr += isprint[i] + "-H-";
		}

		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}
		List<CwbOrder> cwbList = this.cwbDao.getCwbByCwbs(cwbs);
		model.addAttribute("cwbs", cwbhuizongstr);

		if (isback.equals("1")) {
			this.printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, this.getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.TuiHuoChuZhan
					.getValue()));
		} else {
			this.printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, this.getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.ChuKuSaoMiao
					.getValue()));
		}

		model.addAttribute("isback", isback);
		model.addAttribute("iscustomer", iscustomer);
		model.addAttribute("islinghuo", islinghuo);
		model.addAttribute("strtime", starttime);
		model.addAttribute("endtime", endtime);
		
		model.addAttribute("template", this.printTemplateDAO.getPrintTemplate(printtemplateid));
		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("branchlist", this.branchDAO.getAllBranches());
		model.addAttribute("userlist", this.userDAO.getAllUser());
		model.addAttribute("nextbranchid", Long.parseLong(nextbranchid));
		model.addAttribute("deliverid", deliverid);
		PrintTemplate printtemplate = this.printTemplateDAO.getPrintTemplate(printtemplateid);
		if (printtemplate.getTemplatetype() == 1) {

			model.addAttribute("cwbList", cwbList);
			return "warehousegroup/outbillprinting_template";
		} 
		// 武汉飞远运输交接单 add by chunlei05.li 2016/8/11
		else if(printtemplate.getTemplatetype() == 4 && printtemplate.getOpertatetype() == PrintTemplateOpertatetypeEnum.WuHanLinHuoDan.getValue()) {
			User user = this.getSessionUser();
			model.addAttribute("username", user.getRealname());
			Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
			model.addAttribute("branchname", branch.getBranchname());
			model.addAttribute("printTime", new Date());
			User deliver = this.userDAO.getUserByUserid(deliverid);
			model.addAttribute("delivername", deliver.getRealname());
			List<String[]> cwbArrayList = new ArrayList<String[]>();
			final int ROW_NUM = 2; // 每行显示数
			int row = (int) Math.ceil((double) cwbList.size() / ROW_NUM);
			BigDecimal receivablefeeTotal = BigDecimal.ZERO; // 代收货款
			StringBuilder huizongcwbs = new StringBuilder(""); // 订单号汇总
			for (int i = 0; i < row; i++) {
				String[] cwbArray = new String[ROW_NUM];
				for (int j = 0; j < ROW_NUM; j++) {
					int index = i * ROW_NUM + j;
					if (index >= cwbList.size()) {
						break;
					}
					CwbOrder cwb = cwbList.get(index);
					cwbArray[j] = cwb.getCwb();
					receivablefeeTotal = receivablefeeTotal.add(cwb.getReceivablefee());
					huizongcwbs.append(cwb.getCwb()).append("-H-");
				}
				cwbArrayList.add(cwbArray);
			}
			model.addAttribute("cwbArrayList", cwbArrayList);
			model.addAttribute("ordernum", cwbList.size());
			model.addAttribute("receivablefeeTotal", receivablefeeTotal);
			if (cwbList.size() > 0) {
				model.addAttribute("customerid", cwbList.get(0).getCustomerid());
			}
			model.addAttribute("huizongcwbs", huizongcwbs);
			return "warehousegroup/wuHanPickingListPrint";	
		}
		else if (printtemplate.getTemplatetype() == 2) {

			List<JSONObject> cwbJson = this.cwbDao.getDetailForChuKuPrint(cwbs);

			model.addAttribute("cwbList", cwbJson);
			model.addAttribute("cwbArr", cwbArr);
			return "warehousegroup/outbillhuizongprinting_template";
		} else if (printtemplate.getTemplatetype() == 2) {

			List<JSONObject> cwbJson = this.cwbDao.getDetailForChuKuPrint(cwbs);

			model.addAttribute("cwbList", cwbJson);
			model.addAttribute("cwbArr", cwbArr);
			return "warehousegroup/outbillhuizongprinting_template";
		} else if (this.printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 10) {//武汉飞远		
		    OutChangePrintVo print = initPrintData(cwbList, Long.parseLong(nextbranchid), starttime, endtime, getSessionUser());
		    model.addAttribute("cwbList",cwbList);
		    model.addAttribute("print", print);
			return "warehousegroup/outbackbillprinting_wuhan";
		}
		return null;
	}

	/**
	 * 出库交接单信息打印和查询 新 按照站点分开
	 *
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/outbillprinting_defaultnew")
	public String outbillprinting_defaultnew(Model model, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint,
			@RequestParam(value = "isback", defaultValue = "", required = true) String isback, @RequestParam(value = "iscustomer", required = false, defaultValue = "0") long iscustomer,
			@RequestParam(value = "islinghuo", defaultValue = "0", required = true) long islinghuo, @RequestParam(value = "currentdeliverid", defaultValue = "0", required = true) long deliverid,
			@RequestParam(value = "nextbranchid", defaultValue = "-1", required = false) String nextbranchid,
			@RequestParam(value = "printtemplateid", defaultValue = "", required = false) long printtemplateid, @RequestParam(value = "baleno", defaultValue = "", required = false) String baleno,
			@RequestParam(value = "driverid", defaultValue = "0", required = false) long driverid, @RequestParam(value = "truckid", defaultValue = "0", required = false) long truckid,
			@RequestParam(value = "cwbOrderTypeId", defaultValue = "", required = false) String cwbOrderTypeId,
			@RequestParam(value = "starttime", defaultValue = "", required = false) String starttime,
		    @RequestParam(value = "endtime", defaultValue = "", required = false) String endtime
			) {
		model.addAttribute("mytruckid", truckid);
		model.addAttribute("flowtype", request.getParameter("type") == null ? -1 : request.getParameter("type"));
		String cwbs = "", cwbstr = "", cwbhuizongstr = "";
		long baleid=-1;
		String[] cwbArr = new String[isprint.length];
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbstr += isprint[i] + ",";
			cwbArr[i] = isprint[i];
			cwbhuizongstr += isprint[i] + "-H-";
		}

		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}

		long flowordertype = Long.parseLong(request.getParameter("flowordertype"));

		/*TODO*/
//		List<CwbOrder> cwbList = this.cwbDao.getCwbByCwbsForPrint(cwbs, nextbranchid, this.getSessionUser().getBranchid(), flowordertype);
		List<CwbOrder> cwbList = new ArrayList<CwbOrder>();
		/*按包打印，主表、关联表取数差异显示明细*/
		List<CwbOrder> cwbListForBaleView = new ArrayList<CwbOrder>();

		/**
		 * 广州通路需求，如果出库交接单打印，查询页面录入包号查询，变更查询逻辑（由关联表而不是主表取数）
		 */
		if( !StringUtils.isEmpty(baleno) ){
			Bale bale=this.baleDAO.getBaleOnway(baleno);
			if(bale==null){
				this.logger.info("the bale is not found for baleno="+baleno);	
			}
			if(bale!=null){
				baleid=bale.getId();
				Map<String, List<CwbOrder>> cwbOrderListMap = this.handleQueryForBale(bale.getId(),nextbranchid,flowordertype,cwbOrderTypeId);
				cwbList = cwbOrderListMap.get("cwbList");
				cwbListForBaleView = cwbOrderListMap.get("cwbListForBaleView");
			}
			long baleCount = (bale==null)?0:bale.getScannum();
			model.addAttribute("baleCount", baleCount);
			
//			cwbList = this.cwbDao.getCwbByCwbsForPrint(cwbs, nextbranchid, this.getSessionUser().getBranchid(), flowordertype);
		}else{
			cwbList = this.cwbDao.getCwbByCwbsAndcwbTypeForPrint(cwbs, nextbranchid, this.getSessionUser().getBranchid(), flowordertype,cwbOrderTypeId);
		}

		StringBuffer sbf = new StringBuffer();

		// 拼成cwb-H-nextbranchid-HH-

		for (CwbOrder cwbOrder : cwbList) {

			sbf.append(cwbOrder.getCwb()).append("-H-").append(cwbOrder.getNextbranchid()).append("-HH-");

		}
		sbf.toString();
		String aimcwbs = "";
		if (sbf.length() > 0) {
			aimcwbs = sbf.substring(0, sbf.length() - 4);
		}

		model.addAttribute("cwbs", aimcwbs);

		if (isback.equals("1")) {
			this.printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, this.getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.TuiHuoChuZhan
					.getValue()));
		} else {
			this.printcwbDetailDAO.crePrintcwbDetail(new PrintcwbDetail(0, this.getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.ChuKuSaoMiao
					.getValue()));
		}

		
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		model.addAttribute("isback", isback);
		model.addAttribute("iscustomer", iscustomer);
		model.addAttribute("islinghuo", islinghuo);

		model.addAttribute("template", this.printTemplateDAO.getPrintTemplate(printtemplateid));
		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("branchlist", branchList);
		model.addAttribute("userlist", this.userDAO.getAllUser());
		// model.addAttribute("nextbranchid", Long.parseLong(nextbranchid));
		model.addAttribute("deliverid", deliverid);

		String[] branchids = nextbranchid.split(",");
		Map<Long, List<CwbOrder>> map = new HashMap<Long, List<CwbOrder>>();
		Map<Long, List<CwbOrder>> mapForBaleHandler = new HashMap<Long, List<CwbOrder>>();
		Map<Long, CwbOrder> mapForCustomer = new HashMap<Long, CwbOrder>();
		/*
		 * for (String s : branchids) { if(Long.parseLong(s)>0){ List<CwbOrder>
		 * orderlist=new ArrayList<CwbOrder>(); for (CwbOrder cwb : cwbList) {
		 * mapForCustomer.put(cwb.getCustomerid(), cwb); if
		 * (cwb.getNextbranchid()==Long.parseLong(s)) { orderlist.add(cwb); } }
		 * map.put(Long.parseLong(s), orderlist); }
		 *
		 * }
		 */

		if( !StringUtils.isEmpty(baleno) ){
			if(cwbListForBaleView==null||cwbListForBaleView.isEmpty()||cwbListForBaleView.size()<1){
				this.logger.info("cwbListForBaleView is empty");
			}else{
				for (CwbOrder cwb : cwbListForBaleView) {
					if(cwb==null){
						this.logger.info("cwb1 is empty");
						continue;
					}
					mapForCustomer.put(cwb.getCustomerid(), cwb);
					for (Branch b : branchList) {
						if (cwb.getNextbranchid() == b.getBranchid()) {
							if (!map.containsKey(cwb.getNextbranchid())) {
								List<CwbOrder> orders = new ArrayList<CwbOrder>();
								map.put(cwb.getNextbranchid(), orders);
								logger.info("出库打印站点名称： " + b.getBranchname() + " 站点id： " + b.getBranchid());
							}
							map.get(cwb.getNextbranchid()).add(cwb);
							
							break;
						}
					}
				}
			}
			if(cwbList==null||cwbList.isEmpty()||cwbList.size()<1){
				this.logger.info("cwbList is empty");
			}else{
				for (CwbOrder cwb : cwbList) {
					if(cwb==null){
						this.logger.info("cwb2 is empty");
						continue;
					}
					mapForCustomer.put(cwb.getCustomerid(), cwb);
					if (!mapForBaleHandler.containsKey(cwb.getNextbranchid())) {
						List<CwbOrder> orders = new ArrayList<CwbOrder>();
						mapForBaleHandler.put(cwb.getNextbranchid(), orders);
					}
					mapForBaleHandler.get(cwb.getNextbranchid()).add(cwb);
				}
			}
		}else{
			for (CwbOrder cwb : cwbList) {
				mapForCustomer.put(cwb.getCustomerid(), cwb);
				for (Branch b : branchList) {
					if (cwb.getNextbranchid() == b.getBranchid()) { 
						if (!map.containsKey(cwb.getNextbranchid())) {
							List<CwbOrder> orders = new ArrayList<CwbOrder>();
							map.put(cwb.getNextbranchid(), orders);
							logger.info("出库打印站点名称： " + b.getBranchname() + " 站点id： " + b.getBranchid());
						}
						map.get(cwb.getNextbranchid()).add(cwb);
						break;
					} 
				}
			}
		}
		

		model.addAttribute("mapBybranchid", map);
		model.addAttribute("mapForBaleHandler", mapForBaleHandler);
		PrintTemplate printtemplate = this.printTemplateDAO.getPrintTemplate(printtemplateid);
		if (printtemplate.getTemplatetype() == 1) {
			model.addAttribute("baleno",  baleno);
			model.addAttribute("baleid",  baleid);
			return "warehousegroup/outbillprinting_templatenew";
		}
		// 武汉飞远运输交接单 add by chunlei05.li 2016/8/11
		else if(printtemplate.getTemplatetype() == 4 && printtemplate.getOpertatetype() == PrintTemplateOpertatetypeEnum.WuHanYunShuJiaoJieDan.getValue()) {
			User user = this.getSessionUser();
			Branch userBranch = this.branchService.getBranchByBranchid(user.getBranchid());
			// 转换成打印信息
			Map<Long, WuhanTransportDetailVo> voMap = new HashMap<Long, WuhanTransportDetailVo>();
			for (CwbOrder cwb : cwbList) {
				WuhanTransportDetailVo vo = voMap.get(cwb.getNextbranchid());
				if (vo == null) {
					vo = new WuhanTransportDetailVo();
					vo.setBranchname(userBranch.getBranchname());
					Branch nextBranch = this.branchService.getBranchByBranchid(cwb.getNextbranchid());
					vo.setNextBranchname(nextBranch.getBranchname());
					vo.setOutstockStartTime(starttime);
					vo.setOutstockEndTime(endtime);
					voMap.put(cwb.getNextbranchid(), vo);
				}
				vo.setOutstockOrderNum(vo.getOutstockOrderNum() + 1);
				vo.setOutstockSendNum(vo.getOutstockSendNum() + (int) cwb.getSendcarnum());
			}
			List<WuhanTransportDetailVo> voList = new ArrayList<WuhanTransportDetailVo>(voMap.values());
			model.addAttribute("printList", voList);
			model.addAttribute("baleno",  baleno);
			model.addAttribute("baleid",  baleid);
			model.addAttribute("strtime",  starttime);
			model.addAttribute("endtime",  endtime);
			return "warehousegroup/wuhanTransportDetailPrint";
		} else if (printtemplate.getTemplatetype() == 2) {
			Map<Long, List<JSONObject>> hmap = new HashMap<Long, List<JSONObject>>();
			// 按照custome
			List<Customer> customerList = this.customerDAO.getAllCustomers();
			Set<Long> customers = mapForCustomer.keySet();
			for (Long branchid : map.keySet()) {
				List<CwbOrder> listForBranchid = map.get(branchid);
				// 得到每个站点的 订单详情
				if (listForBranchid.size() > 0) {
					List<JSONObject> customerJson = new ArrayList<JSONObject>();// 封装
					Map<Long, List<CwbOrder>> mapForCustomerTemp = new HashMap<Long, List<CwbOrder>>();
					// 按照供货商 - 订单 存入map
					for (Long customerid : customers) {
						List<CwbOrder> cwborderBycustomer = new ArrayList<CwbOrder>();
						for (CwbOrder co : listForBranchid) {
							if (customerid == co.getCustomerid()) {
								cwborderBycustomer.add(co);
							}
						}
						mapForCustomerTemp.put(customerid, cwborderBycustomer);
					}
					// 根据 供货商循环 得到每个供货商的汇总信息
					for (Long customerid : customers) {
						List<CwbOrder> tempList = mapForCustomerTemp.get(customerid);
						if (tempList.size() > 0) {
							JSONObject obj = new JSONObject();
							obj.put("customername", this.getCustomerName(customerid, customerList));
							obj.put("deliverid", deliverid);
							long cwbcount = 0;
							long sendcarnum = 0;
							long backcarnum = 0;
							BigDecimal caramount = BigDecimal.ZERO;
							BigDecimal receivablefee = BigDecimal.ZERO;
							BigDecimal paybackfee = BigDecimal.ZERO;
							for (CwbOrder cwbOrder : tempList) {
								cwbcount++;
								sendcarnum += cwbOrder.getSendcarnum();
								backcarnum += cwbOrder.getBackcarnum();
								caramount = caramount.add(cwbOrder.getCaramount());
								receivablefee = receivablefee.add(cwbOrder.getReceivablefee());
								paybackfee = paybackfee.add(cwbOrder.getPaybackfee());
							}
							obj.put("cwbcount", cwbcount);
							obj.put("sendcarnum", sendcarnum);
							obj.put("backcarnum", backcarnum);
							obj.put("caramount", caramount);
							obj.put("receivablefee", receivablefee);
							obj.put("paybackfee", paybackfee);
							customerJson.add(obj);// 添加到对应站点的 供货商汇总list
						}

					}
					hmap.put(branchid, customerJson);
				}
			}
			model.addAttribute("huizongmap", hmap);
			return "warehousegroup/outbillhuizongprinting_templatenew";
		} else if (printtemplate.getTemplatetype() == 4) {
			List<WarehouseGroupPrintDto> printlist = new ArrayList<WarehouseGroupPrintDto>();
			WarehouseGroupPrintDto printDto = new WarehouseGroupPrintDto();
			int danshu = 0;
			int jianshu = 0;
			BigDecimal carryweight = BigDecimal.ZERO;
			if (!baleno.equals("")) {
				Bale bale=this.baleDAO.getBaleOnway(baleno);
				List<CwbOrder> cwbOrders = new ArrayList<CwbOrder>();
				if(bale!=null){
					cwbOrders=this.cwbDao.getCwbByBaleid(bale.getId(),cwbOrderTypeId);
				}
				for (int i = 0; i < cwbOrders.size(); i++) {
					jianshu += cwbOrders.get(i).getSendcarnum();
					carryweight = carryweight.add(cwbOrders.get(i).getCarrealweight());
				}
				printDto.setDanshu(cwbOrders.size());
				printDto.setJianshu(jianshu);
				printDto.setCarrealweight(carryweight);
				printDto.setChengzhong(BigDecimal.ZERO);
				printDto.setCwbremark("");
				printDto.setBaleno(baleno);
				printlist.add(printDto);
				model.addAttribute("printList", printlist);
				String[] nextBranchid = nextbranchid.split(",");
				String nextBranch = "";
				//DMP 4.2.8 修复分拣出库打印
//				for (int i = 1; i < nextBranchid.length; i++) {
				for (int i = 0,len = nextBranchid.length; i < len; i++) {
					nextBranch = nextBranch + this.branchDAO.getBranchByBranchid(Long.parseLong(nextBranchid[i])).getBranchname() ;
					if(i == (len -1)){
						nextBranch += ",";
					}
				}
				model.addAttribute("branchname", nextBranch);
				/*
				 * if(truckid-1){ model.addAttribute("truckid","___________");
				 * }else {
				 * model.addAttribute("truckid",truckDAO.getTruckByTruckid
				 * (truckid).getTruckno()); }
				 */

				List<GroupDetail> groupDetails = new ArrayList<GroupDetail>();
				if(bale!=null){
					groupDetails = this.groupDetailDao.getGroupDetailListByBale(bale.getId());
				}
				GroupDetail groupDetail = new GroupDetail();
				for (GroupDetail gDetail : groupDetails) {
					groupDetail = gDetail;
				}
				if (truckid > 0) {
					model.addAttribute("truckid", this.truckDAO.getTruckByTruckid(truckid).getTruckno());
				} else {
					if (groupDetail.getTruckid() > 0) {
						model.addAttribute("truckid", this.truckDAO.getTruckByTruckid(groupDetail.getTruckid()).getTruckno());
					} else {
						model.addAttribute("truckid", "________");
					}
				}

				WarehouseGroupPrintDto warehouseGroupPrintDto = new WarehouseGroupPrintDto();
				warehouseGroupPrintDto.setBaleno("1");
				warehouseGroupPrintDto.setDanshu(cwbOrders.size());
				warehouseGroupPrintDto.setJianshu(jianshu);
				warehouseGroupPrintDto.setCarrealweight(carryweight);
				warehouseGroupPrintDto.setChengzhong(BigDecimal.ZERO);
				warehouseGroupPrintDto.setCwbremark("");
				model.addAttribute("total", warehouseGroupPrintDto);

			} else {
				Set<String> baleSet = new HashSet<String>();
				List<CwbOrder> cwbOrders = new ArrayList<CwbOrder>();
				List<WarehouseGroupPrintDto> printDtos = new ArrayList<WarehouseGroupPrintDto>();// 没有合包的订单重新新建一个list保存
				for (int i = 0; i < cwbList.size(); i++) {
					cwbOrders.add(this.cwbDao.getCwbByCwb(cwbList.get(i).getCwb()));
				}
				List<GroupDetail> groupDetails = new ArrayList<GroupDetail>();
				for (int i = 0; i < cwbOrders.size(); i++) {
					groupDetails = this.groupDetailDao.getGroupDetailListByCwb(cwbOrders.get(i).getCwb());
					if (!cwbOrders.get(i).getPackagecode().equals("")) {
						if(groupDetails!=null){
							for(GroupDetail gd:groupDetails){
								if(gd.getBaleid()>0){
									Bale bale=this.baleDAO.getBaleById(gd.getBaleid());
									if(bale!=null){
										baleSet.add(bale.getBaleno());
									}
								}
							}
						}
						//baleSet.add(cwbOrders.get(i).getPackagecode());
					} else {
						WarehouseGroupPrintDto printDto2 = new WarehouseGroupPrintDto();
						printDto2.setBaleno(cwbOrders.get(i).getCwb() + "(订单)");
						printDto2.setDanshu(1);
						printDto2.setJianshu(cwbOrders.get(i).getSendcarnum());
						printDto2.setCarrealweight(cwbOrders.get(i).getCarrealweight());
						printDto2.setChengzhong(BigDecimal.ZERO);
						if (cwbOrders.get(i).getCwbremark().length() > 30) {
							printDto2.setCwbremark(cwbOrders.get(i).getCwbremark().substring(0, 30));
						} else {
							printDto2.setCwbremark(cwbOrders.get(i).getCwbremark());
						}
						printDtos.add(printDto2);// 将创建的新的打印对象添加到没有合包的list中
					}
				}
				if (printDtos.size() != 0) {
					for (int i = 0; i < printDtos.size(); i++) {
						printlist.add(printDtos.get(i));
					}
				}
				for (String string : baleSet) {
					Bale bale=this.baleDAO.getBaleOnway(string);
					List<CwbOrder> cwbOrders2 = new ArrayList<CwbOrder>();
					if(bale!=null){
						cwbOrders2=this.cwbDao.getCwbByBaleid(bale.getId(),cwbOrderTypeId);
					}		
					WarehouseGroupPrintDto printDto2 = new WarehouseGroupPrintDto();
					for (int i = 0; i < cwbOrders2.size(); i++) {
						jianshu += cwbOrders2.get(i).getSendcarnum();
						carryweight = carryweight.add(cwbOrders2.get(i).getCarrealweight());
					}
					printDto2.setDanshu(cwbOrders2.size());
					printDto2.setJianshu(jianshu);
					printDto2.setCarrealweight(carryweight);
					printDto2.setChengzhong(BigDecimal.ZERO);
					printDto2.setCwbremark("");
					printDto2.setBaleno(string);
					printlist.add(printDto2);

				}

				model.addAttribute("printList", printlist);
				String[] nextBranchid = nextbranchid.split(",");
				String nextBranch = "";
				for (int i = 1; i < nextBranchid.length; i++) {
					nextBranch = nextBranch + this.branchDAO.getBranchByBranchid(Long.parseLong(nextBranchid[i])).getBranchname() + ",";
				}
				nextBranch = nextBranch.substring(0, nextBranch.length() - 1);
				model.addAttribute("branchname", nextBranch);

				if (truckid > 0) {
					model.addAttribute("truckid", this.truckDAO.getTruckByTruckid(truckid).getTruckno());
				} else {
					model.addAttribute("truckid", "________");
				}
				// 添加统计总和信息

				WarehouseGroupPrintDto warehouseGroupPrintDto = new WarehouseGroupPrintDto();
				int jianshuTotal = 0;
				int danshuTotal = 0;
				BigDecimal carrweightTotal = BigDecimal.ZERO;
				for (int i = 0; i < printlist.size(); i++) {
					jianshuTotal += printlist.get(i).getJianshu();
					danshuTotal += printlist.get(i).getDanshu();
					carrweightTotal = carrweightTotal.add(printlist.get(i).getCarrealweight());
				}
				warehouseGroupPrintDto.setBaleno(printlist.size() + "");
				warehouseGroupPrintDto.setJianshu(jianshuTotal);
				warehouseGroupPrintDto.setDanshu(danshuTotal);
				warehouseGroupPrintDto.setCarrealweight(carrweightTotal);
				warehouseGroupPrintDto.setChengzhong(BigDecimal.ZERO);
				warehouseGroupPrintDto.setCwbremark("");

				model.addAttribute("total", warehouseGroupPrintDto);

			}
			/*
			 * Map<String , List<CwbOrder>> baleMap=new HashMap<String,
			 * List<CwbOrder>>(); map.put(baleno, value);
			 */

			return "warehousegroup/outbillanbaoprinting_templatenew";
		} else if (this.printTemplateDAO.getPrintTemplate(printtemplateid).getTemplatetype() == 11) {//武汉飞远 中转
			List<OutChangePrintVo> prints = new ArrayList<OutChangePrintVo>();
			User user = getSessionUser();
			if (map != null && !map.isEmpty()) {
				for (Long key : map.keySet()) {
					List<CwbOrder> list = map.get(key);
					OutChangePrintVo print = initPrintData(list, key, starttime, endtime, user);
				    prints.add(print);
				}
			}
		    model.addAttribute("prints", prints);
		    model.addAttribute("baleno",  baleno);
			model.addAttribute("baleid",  baleid);
			return "warehousegroup/outchangebillprinting_wuhan";
		}
		return null;
	}

	/**
	 * 通路逻辑：按包查询内容，主表、关联表数据不一致情况
	 * @param baleno 包号
	 * @param flowordertype 
	 * @param currentbranchid 
	 * @param nextbranchid 
	 * @param cwbList 实际对应订单明细
	 * @param cwbListForBaleView 用于打印页面运单维度订单明细（一票多件多条记录，订单号由运单号替换）
	 */
	private Map<String,List<CwbOrder>> handleQueryForBale(long baleid, String nextbranchid, long flowordertype,String cwbOrderTypeId) {
		
		Map<String,List<CwbOrder>> resultMap = new HashMap<String, List<CwbOrder>>();
		//所有包中关联订/运单号 Str list
		List<String> orderNoStrs = this.baleCwbDao.getCwbsByBale(""+baleid);
		//所有真实订单号 Strs
		StringBuilder cwbsSB = new StringBuilder();
		String cwbs = "";
		if( null != orderNoStrs && !orderNoStrs.isEmpty()){
			for (String tempNo : orderNoStrs) {
				String targetCwb = this.cwborderService.translateCwb(tempNo);
				cwbsSB.append("'").append(targetCwb).append("',");
			}
		}
		if( cwbsSB.length() > 0){
			cwbs = cwbsSB.substring(0,cwbsSB.length()-1);
		}
		//真实订单list(根据打印相关表获取)
//		List<CwbOrder> cwbList = this.cwbDao.getCwbListByAnyNo(orderNoStrs);
//		List<CwbOrder> cwbList = this.cwbDao.getCwbByCwbsForPrint(cwbs, nextbranchid, this.getSessionUser().getBranchid(), flowordertype,baleno);
		//dmp 4.2.8 修复分拣出库打印的bug
		List<CwbOrder> cwbList = this.cwbDao.getCwbByCwbsForPrint(cwbs,this.getSessionUser().getBranchid(),baleid,cwbOrderTypeId);
		
		//显示用订单list
		List<CwbOrder> cwbListForBaleView = new ArrayList<CwbOrder>();
		//构建显示-打印用订单list（运单维度）
		Map<String , String> cwbTransCwbMap = this.buildCwbTransCwbMap(cwbList);
		
		if( null != cwbList && null != orderNoStrs ){
			for (String tempNo : orderNoStrs) {
				CwbOrder cwbOrder = null;
				//关联为订单号
				if( cwbTransCwbMap.get(tempNo) == null  ){
					cwbOrder = this.getCwbOrderByOrderNO(cwbList,tempNo);
				//关联为运单号	
				}else{
					cwbOrder = this.getCwbOrderByTransNo(cwbList,tempNo,cwbTransCwbMap);
				}
				cwbListForBaleView.add(cwbOrder);
			}
		}
		
		resultMap.put("cwbList", cwbList);
		resultMap.put("cwbListForBaleView", cwbListForBaleView);
		return resultMap;
	}
	
	/**
	 * 按包查询内容，主表、关联表数据不一致情况 for 历史打印
	 * @param baleno
	 * @param nextbranchid
	 * @param flowordertype
	 * @return
	 */
	private Map<String,List<CwbOrder>> handleQueryForBale(long baleid) {
		
		Map<String,List<CwbOrder>> resultMap = new HashMap<String, List<CwbOrder>>();
		//所有包中关联订/运单号 Str list
		List<String> orderNoStrs = this.baleCwbDao.getCwbsByBale(""+baleid);
		//真实订单list(根据打印相关表获取)
		List<CwbOrder> cwbList = this.cwbDao.getCwbListByAnyNo(orderNoStrs);
		//显示用订单list
		List<CwbOrder> cwbListForBaleView = new ArrayList<CwbOrder>();
		//构建显示-打印用订单list（运单维度）
		Map<String , String> cwbTransCwbMap = this.buildCwbTransCwbMap(cwbList);
		if( null != cwbList && null != orderNoStrs ){
			for (String tempNo : orderNoStrs) {
				CwbOrder cwbOrder = null;
				//关联为订单号
				if( cwbTransCwbMap.get(tempNo) == null ){
					cwbOrder = this.getCwbOrderByOrderNO(cwbList,tempNo);
				//关联为运单号	
				}else{
					cwbOrder = this.getCwbOrderByTransNo(cwbList,tempNo,cwbTransCwbMap);
				}
				cwbListForBaleView.add(cwbOrder);
			}
		}
		resultMap.put("cwbList", cwbList);
		resultMap.put("cwbListForBaleView", cwbListForBaleView);
		return resultMap;
	}

	private CwbOrder getCwbOrderByTransNo(List<CwbOrder> cwbList, String tempNo, Map<String, String> cwbTransCwbMap) {
		CwbOrder tarCwbOrder = new CwbOrder();
		for (CwbOrder cwbOrder : cwbList) {
			//此处未实现按照不同客户区分大小写设置 TODO
			if(cwbOrder.getCwb().equalsIgnoreCase(cwbTransCwbMap.get(tempNo))){
				tarCwbOrder = BeanConverter.convert(tarCwbOrder, cwbOrder);
				/**
				 * 一票一件，存在运单号情况处理
				 */
				if(cwbOrder.getSendcarnum() > 1 || cwbOrder.getBackcarnum() > 1){
					tarCwbOrder.setCwb(tempNo);
				}
				return tarCwbOrder;
			}
		}
		return null;
	}

	private CwbOrder getCwbOrderByOrderNO(List<CwbOrder> cwbList, String tempNo) {
		for (CwbOrder cwbOrder : cwbList) {
			if(cwbOrder.getCwb().equalsIgnoreCase(tempNo)){
				return cwbOrder;
			}
		}
		return null;
	}

	/**
	 * 构建运单号-订单号关联Map
	 * @param cwbList
	 * @return
	 */
	private Map<String, String> buildCwbTransCwbMap(List<CwbOrder> cwbList) {
		Map<String, String> resultMap = new HashMap<String, String>();
		if( null != cwbList && !cwbList.isEmpty()){
			for (CwbOrder cwbOrder : cwbList) {
				String transCwb = cwbOrder.getTranscwb() == null? "" : cwbOrder.getTranscwb();
				if( !transCwb.equals("") && transCwb.split(",").length > 0){
					for (String tempTransCwb : transCwb.split(",")) {
						resultMap.put( tempTransCwb , cwbOrder.getCwb());
					}
				}
			}
		}
		return resultMap;
	}

	// 得到供货商的名字
	private String getCustomerName(Long customerid, List<Customer> customerList) {
		String name = "";
		if ((customerList != null) && (customerList.size() > 0)) {
			for (Customer customer : customerList) {
				if (customerid == customer.getCustomerid()) {
					name = customer.getCustomername();
					break;
				}
			}
		}
		return name;
	}
	
	

	/**
	 * 出库信息查询
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return jm's ideas 直接用cwbdetail
	 *         按照nextbranchid和当前操作flowordertype来查询到对应的订单信息 这是未打印
	 */
	@RequestMapping("/outlist/{page}")
	public String outlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") String[] branchid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid,
			@RequestParam(value = "cwbOrderTypeId", required = false, defaultValue = "") String cwbOrderTypeId,
			HttpServletRequest request) {
		// 查询的实际起始时间 add by chunlei05.li 2016-08-11
		String realStrTime = strtime;
		String realEndTime = endtime;
		List<PrintView> printList = new ArrayList<PrintView>();
		List<Branch> bList = this.getNextPossibleBranches();
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tList = this.truckDAO.getAllTruck();
		List<Branch> removeList = new ArrayList<Branch>();
		String type=request.getParameter("type");
		long typeid=1;
		if ((type!=null)&&(type!="")) {
			typeid=Long.parseLong(type);
		}
		if (typeid==2) {
			for (Branch b : bList) {// 去掉退货站
				if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
					removeList.add(b);
				}
			}
			bList.removeAll(removeList);
		}
		model.addAttribute("branchlist", bList);
		String branchids = this.getStrings(branchid);
		if (isshow > 0) {
			List<GroupDetail> gdList = new ArrayList<GroupDetail>();
			Bale bale=null;
			if (baleno.equals("")) {
				if ((driverid == -1) || (truckid == -1) || (truckid == 0)) {
					if (truckid != -1) {
						gdList = this.groupDetailDao.getCwbForChuKuPrintTimeNewByTruckid(this.getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), strtime, endtime,
								"", truckid);
					} else if (driverid != -1) {
						gdList = this.groupDetailDao.getCwbForChuKuPrintTimeNewByDriverid(this.getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), strtime, endtime,
								"", driverid);
					} else {
						gdList = this.groupDetailDao.getCwbForChuKuPrintTimeNew(this.getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), strtime, endtime, "");
					}
				} else {
					// 驾驶员 和 车牌号都有值
					gdList = this.groupDetailDao.getCwbForChuKuPrintTimeNew2(this.getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), strtime, endtime, "", driverid,
							truckid);
				}
				// 包号为空将truckid传入 目的是不让报错
				model.addAttribute("truckid", truckid);

			} else {
				bale=this.baleDAO.getBaleOnway(baleno);
				if (truckid != -1) {
					gdList = this.groupDetailDao.getCwbListByBalenoExportByTruckid(truckid);
				} else if (driverid != -1) {
					gdList = this.groupDetailDao.getCwbListByBalenoExportBydriverid(driverid);
				} else {
					if(bale!=null){
						gdList = this.groupDetailDao.getCwbListByBaleIdExport(bale.getId());
					}
				}
				// 包号不为空 查询groupDetail表将truckid查询出来

				List<GroupDetail> groupDetails = new ArrayList<GroupDetail>();
				if(bale!=null){
					groupDetails = this.groupDetailDao.getGroupDetailListByBale(bale.getId());
				}
				if (groupDetails.size() > 0) {
					model.addAttribute("truckid", groupDetails.get(0).getTruckid());
				}

			}
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			String cwbs = "";
			for (GroupDetail deliveryZhiLiu : gdList) {
				cwbs += "'" + deliveryZhiLiu.getCwb() + "',";
			}
			cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
			if (!baleno.equals("")) {
				//按包逻辑优化，查询关联表记录
				if(bale!=null){
					orderlist = this.cwbDao.getCwbByBaleid(bale.getId(),cwbOrderTypeId);
				}
			} else if (cwbs.length() > 0) {
				//getCwbOrderByCwbs
				orderlist = this.cwbDao.getCwbOrderByCwbsAndcwbOrderType(cwbs,cwbOrderTypeId);
			}
			List<Customer> customerList = this.customerDAO.getAllCustomers();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			printList = this.warehouseGroupDetailService.getChuKuView(orderlist, gdList, customerList, branchList);
			// 获取查询结果的最大时间和最小时间  add by chunlei05.li 2016-08-11
			if (org.apache.commons.lang3.StringUtils.isBlank(strtime) || org.apache.commons.lang3.StringUtils.isBlank(strtime)) {
				String maxTime = null;
				String minTime = null;
				for (GroupDetail gd : gdList) {
					String createtime = gd.getCreatetime();
					if (org.apache.commons.lang3.StringUtils.isBlank(createtime)) {
						continue;
					}
					if (maxTime == null) {
						maxTime = createtime;
					}
					if (minTime == null) {
						minTime = createtime;
					}
					if (createtime.compareTo(maxTime) == 1) {
						maxTime = createtime;
					}
					if(createtime.compareTo(minTime) == -1) {
						minTime = createtime;
					}
				}
				if (org.apache.commons.lang3.StringUtils.isBlank(realStrTime)) {
					realStrTime = minTime;
				}
				if (org.apache.commons.lang3.StringUtils.isBlank(realEndTime)) {
					realEndTime = maxTime;
				}
			}
		}
		// LKN 这段代码没用
		// cwbs = cwbs.length()>0?cwbs.substring(0, cwbs.length()-1):"";
		// if (cwbs.length()>0) {
		// orderlist = cwbDao.getCwbOrderByCwbs(cwbs);
		// }
		// List<Customer> customerList = customerDAO.getAllCustomers();
		// List<Branch> branchList = branchDAO.getAllEffectBranches();
		// List<PrintView> printList =
		// warehouseGroupDetailService.getChuKuView(orderlist, gdList,
		// customerList, branchList);
		model.addAttribute(
				"printtemplateList",
				this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ChuKuAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.ChuKuHuiZong.getValue() + ","
						+ PrintTemplateOpertatetypeEnum.ChuKuAnBao.getValue() + "," + PrintTemplateOpertatetypeEnum.WuHanYunShuJiaoJieDan.getValue()));
		model.addAttribute("printList", printList);
		model.addAttribute("type", typeid);
		model.addAttribute("branchids", branchids);
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("time", "出库时间");
		model.addAttribute("uList", uList);
		model.addAttribute("tList", tList);
		model.addAttribute("baleno", baleno);
		model.addAttribute("realStrTime", realStrTime);
		model.addAttribute("realEndTime", realEndTime);
		/*
		 * List<GroupDetail> groupDetails=new ArrayList<GroupDetail>();
		 * groupDetails=groupDetailDao.getGroupDetailListByBale(baleno);
		 * if(groupDetails.size()>0){
		 *
		 * model.addAttribute("truckid", groupDetails.get(0).getTruckid());
		 * }else { model.addAttribute("truckid", truckid); }
		 */
		model.addAttribute("driverid", driverid);
		model.addAttribute("truckid", truckid);
		model.addAttribute("cwbOrderTypeId", cwbOrderTypeId);
		return "warehousegroup/outdetaillist";
	}

	/**
	 * 库对库出库打印
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	@RequestMapping("/kdkoutlist/{page}")
	public String kdkoutlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") String[] branchid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<Branch> bList = this.getNextPossibleBranches();
		List<Branch> lastList = new ArrayList<Branch>();
		String branchids = this.getStrings(branchid);
		for (Branch b : bList) {// 去掉退货站
			if (b.getSitetype() == BranchEnum.KuFang.getValue()) {
				lastList.add(b);
			}
		}
		model.addAttribute("branchlist", lastList);
		List<PrintView> printList = new ArrayList<PrintView>();
		if (isshow > 0) {
			List<GroupDetail> gdList = this.groupDetailDao.getCwbForChuKuPrintTimeNew(this.getSessionUser().getBranchid(), this.getStrings(branchid), FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(),
					strtime, endtime, "");
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
		model.addAttribute("printtemplateList",
				this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ChuKuAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.ChuKuHuiZong.getValue()));
		model.addAttribute("printList", printList);
		model.addAttribute("type", FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue());
		model.addAttribute("time", "出库时间");
		model.addAttribute("flowordertype", FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue());
		model.addAttribute("branchids", branchids);
		return "warehousegroup/outdetaillist2";
	}

	private String getStrings(String[] branchid) {
		String str = "-1";
//		for (String s : branchid) {
//			str += s + ",";
//		}
		if(branchid != null && branchid.length > 0){
			str = org.apache.commons.lang.StringUtils.join(branchid,",");
		}
		

//		return str.substring(0, str.length() - 1);
		return str;
	}

	/**
	 * 中转出站交接单
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/branchzhongzhuanoutlist/{page}")
	public String branchzhongzhuanoutlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") String[] branchid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<Branch> bList = this.getNextPossibleBranches();
		List<Branch> removeList = new ArrayList<Branch>();
		for (Branch b : bList) {// 去掉退货站
			if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
				removeList.add(b);
			}
		}
		bList.removeAll(removeList);
		model.addAttribute("branchlist", bList);
		String branchids = this.getStrings(branchid);
		List<PrintView> printList = new ArrayList<PrintView>();
		if (isshow > 0) {
			List<GroupDetail> gdList = this.groupDetailDao.getCwbForChuKuPrintTimeNew(this.getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), strtime, endtime, "");
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
		model.addAttribute("printList", printList);
		model.addAttribute(
				"printtemplateList",
				this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanAnDan.getValue() + ","
						+ PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanHuiZong.getValue()));
		model.addAttribute("type", 2);
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("time", "出站时间");
		model.addAttribute("branchids", branchids);
		model.addAttribute("time", "出站时间");
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		return "warehousegroup/outdetaillist2";
	}

	/**
	 * 站点出站交接单
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/zhandianoutlist/{page}")
	public String zhandianoutlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") String[] branchid,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "strtime", required = false, defaultValue = "") String strtime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime

	) {
		List<Branch> bList = this.getNextPossibleBranches();
		List<Branch> lastList = new ArrayList<Branch>();

		for (Branch b : bList) {
			if (b.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				lastList.add(b);
			}
		}
		model.addAttribute("branchlist", lastList);
		String branchids = this.getStrings(branchid);
		List<PrintView> printList = new ArrayList<PrintView>();
		if (isshow > 0) {
			List<GroupDetail> gdList = this.groupDetailDao.getCwbForChuKuPrintTimeNew(this.getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "", "", "");
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

		model.addAttribute("printList", printList);

		model.addAttribute(
				"printtemplateList",
				this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ZhanDianChuZhanAnDan.getValue() + ","
						+ PrintTemplateOpertatetypeEnum.ZhanDianChuZhanHuiZong.getValue()));
		model.addAttribute("type", 3);
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("time", "出站时间");
		model.addAttribute("branchids", branchids);
		model.addAttribute("time", "出站时间");
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		return "warehousegroup/outdetaillist2";
	}

	/**
	 * 历史出库信息查询
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/historyoutlist/{page}/{type}")
	public String historyoutlist(Model model, @PathVariable("page") long page, @PathVariable("type") long type, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate, @RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid) {
		List<Branch> blist = this.getNextPossibleBranches();
		List<Truck> tList = this.truckDAO.getAllTruck();
		List<PrintTemplate> printtemplateList = this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ChuKuAnDan.getValue() + ","
				+ PrintTemplateOpertatetypeEnum.ChuKuHuiZong.getValue() + "," + PrintTemplateOpertatetypeEnum.ChuKuAnBao.getValue() + "," + PrintTemplateOpertatetypeEnum.WuHanYunShuJiaoJieDan.getValue());
		if (type == 2) {
			// 中转出站
			printtemplateList = this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanAnDan.getValue() + ","
					+ PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanHuiZong.getValue());
			List<Branch> removeList = new ArrayList<Branch>();
			for (Branch b : blist) {// 去掉退货站
				if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
					removeList.add(b);
				}
			}
			blist.removeAll(removeList);
		} else if (type == 3) {
			// 站点出站
			printtemplateList = this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.ZhanDianChuZhanAnDan.getValue() + ","
					+ PrintTemplateOpertatetypeEnum.ZhanDianChuZhanHuiZong.getValue());
			List<Branch> lastList = new ArrayList<Branch>();
			for (Branch b : blist) {
				if (b.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					lastList.add(b);
				}
			}
			blist = lastList;
		} else if (type == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) {
			List<Branch> lastList = new ArrayList<Branch>();
			for (Branch b : blist) {
				if (b.getSitetype() == BranchEnum.KuFang.getValue()) {
					lastList.add(b);
				}
			}
			blist = lastList;
		}
		if (truckid > 0) {
			model.addAttribute("outwarehousegroupList", this.outwarehousegroupDao.getOutWarehouseGroupByPage2(page, branchid, beginemaildate, endemaildate, 0, truckid,
					OutwarehousegroupOperateEnum.ChuKu.getValue(), 0, this.getSessionUser().getBranchid()));
		} else {
			model.addAttribute("outwarehousegroupList", this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0,
					OutwarehousegroupOperateEnum.ChuKu.getValue(), 0, this.getSessionUser().getBranchid()));
		}

		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue(), 0, this.getSessionUser()
						.getBranchid()), page, Page.ONE_PAGE_NUMBER));
		List<Branch> branchs = this.branchDAO.getAllBranches();
		model.addAttribute("allbranch", branchs);
		model.addAttribute("branchList", blist);
		model.addAttribute("printtemplateList", printtemplateList);
		model.addAttribute("page", page);
		model.addAttribute("type", type);
		model.addAttribute("trucks", tList);
		return "warehousegroup/historyoutlist";
	}

	/**
	 * 历史出库交接单信息打印和查询
	 *
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/outbillprinting_history/{outwarehousegroupid}")
	public String outbillprinting_history(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid,
			@RequestParam(value = "isback", defaultValue = "", required = true) String isback, @RequestParam(value = "iscustomer", required = false, defaultValue = "0") long iscustomer,
			@RequestParam(value = "islinghuo", defaultValue = "0", required = true) long islinghuo, @RequestParam(value = "printtemplateid", defaultValue = "", required = false) long printtemplateid) {

		/**
		 * 通路按包逻辑添加
		 */
		List<CwbOrder> cwbList = new ArrayList<CwbOrder>();
		List<CwbOrder> cwbListForBale = new ArrayList<CwbOrder>();
		OutWarehouseGroup owg = this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid);
		long truckid = owg.getTruckid();
		String balenoowg=owg.getBaleno();
		Bale bale=null;
		if((null!=balenoowg)&&(balenoowg.length()>0)){
			bale=this.baleDAO.getBaleById(owg.getBaleid());
			model.addAttribute("baleno", balenoowg);
			model.addAttribute("baleCount", bale==null?0:bale.getScannum());
		}
		String cwbs = "";

		if (owg.getSign() == 1) {
			if( !StringUtils.isEmpty(balenoowg)){
				cwbs = owg.getCwbs();
				Map<String, List<CwbOrder>> cwbListMap = this.handleQueryForBale(owg.getBaleid());
				cwbList = cwbListMap.get("cwbList");
				cwbListForBale = cwbListMap.get("cwbListForBaleView");
			}else{
				cwbs = owg.getCwbs();
				if(cwbs.indexOf("'") >-1){
					if(",".equals(cwbs.substring(cwbs.length()-1, cwbs.length()))){
						cwbs = cwbs.substring(0, cwbs.length()-1);
					}
				}else{
					String cwbs1 = cwbs;
					String[] cwbStr = cwbs1.split(",");
					cwbs="";
					if(cwbStr.length >0){
						for (String cwb : cwbStr) {
							cwbs += "'"+cwb+"',";
						}
					}
					if(cwbs.length()>0){
						cwbs =  cwbs.substring(0, cwbs.length()-1);
					}else{
						cwbs = "'--'";
					}
				}
				cwbList.addAll(this.cwbDao.getCwbByCwbs(cwbs));
			}
		} else {
			List<GroupDetail> gdList = this.groupDetailDao.getAllGroupDetailByGroupid(outwarehousegroupid);

			for (GroupDetail gd : gdList) {
				if (cwbs.indexOf(gd.getCwb()) == -1) {
					cwbList.add(this.cwbDao.getCwbByCwb(gd.getCwb()));
					cwbs += "'" + gd.getCwb() + "',";
				}
			}
			if (cwbs.length() > 0) {
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
		}
		List<OrderFlow> flowList = this.orderFlowDAO.getCwbByFlowordertypeAndCwbs(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), cwbs);
		Map<String, String> mapForOperatorName = new HashMap<String, String>();
		for (OrderFlow of : flowList) {
			mapForOperatorName.put(of.getCwb(), this.userDAO.getUserByUserid(of.getUserid()).getRealname());
		}
		model.addAttribute("map", mapForOperatorName);

		model.addAttribute("template", this.printTemplateDAO.getPrintTemplate(printtemplateid));
		Branch localbranch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		model.addAttribute("localbranchname", localbranch.getBranchname());
		long nextbranchid = this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid();
		Branch branch = this.branchDAO.getBranchByBranchid(nextbranchid);
		model.addAttribute("branchname",branch.getBranchname());
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("userlist", this.userDAO.getAllUser());
		model.addAttribute("iscustomer", iscustomer);
		model.addAttribute("isback", isback);
		model.addAttribute("islinghuo", islinghuo);
		model.addAttribute("branchlist", this.branchDAO.getAllEffectBranches());
		model.addAttribute("operatetype", this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getOperatetype());
		PrintTemplate printtemplate = this.printTemplateDAO.getPrintTemplate(printtemplateid);
		if (printtemplate.getTemplatetype() == 1) {
			/**
			 * 按包逻辑完善：真实订单、显示打印用订单 list
			 */
			model.addAttribute("cwbList", cwbList);
			model.addAttribute("cwbListForBale",cwbListForBale);
			return "warehousegroup/outbillprinting_history";
		}
		// 武汉飞远运输交接单 add by chunlei05.li 2016/8/11
		else if(printtemplate.getTemplatetype() == 4 && printtemplate.getOpertatetype() == PrintTemplateOpertatetypeEnum.WuHanYunShuJiaoJieDan.getValue()) {
			// 转换成打印信息
			WuhanTransportDetailVo vo = new WuhanTransportDetailVo();
			vo = new WuhanTransportDetailVo();
			vo.setBranchname(localbranch.getBranchname());
			vo.setNextBranchname(branch.getBranchname());
			for (CwbOrder cwb : cwbList) {
				vo.setOutstockOrderNum(vo.getOutstockOrderNum() + 1);
				vo.setOutstockSendNum(vo.getOutstockSendNum() + (int) cwb.getSendcarnum());
			}
			if (org.apache.commons.lang3.StringUtils.isNotBlank(owg.getOutstockTime())) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String,String> createtimeMap;
				try {
					createtimeMap = mapper.readValue(owg.getOutstockTime(), Map.class);
				} catch (Exception e) {
					createtimeMap = new HashMap<String, String>();
				}
				vo.setOutstockStartTime(createtimeMap.get("strtime"));
				vo.setOutstockEndTime(createtimeMap.get("endtime"));
			}
			List<WuhanTransportDetailVo> voList = new ArrayList<WuhanTransportDetailVo>();
			voList.add(vo);
			model.addAttribute("printList", voList);
			return "warehousegroup/wuhanTransportDetailPrint_history";
		}
		// 武汉飞远领货单 add by chunlei05.li 2016/8/11
		else if(printtemplate.getTemplatetype() == 4 && printtemplate.getOpertatetype() == PrintTemplateOpertatetypeEnum.WuHanLinHuoDan.getValue()) {
			User user = this.getSessionUser();
			model.addAttribute("username", user.getRealname());
			model.addAttribute("branchname", localbranch.getBranchname());
			model.addAttribute("printTime", new Date());
			User deliver = this.userDAO.getUserByUserid(owg.getDriverid());
			model.addAttribute("delivername", deliver.getRealname());
			List<String[]> cwbArrayList = new ArrayList<String[]>();
			final int ROW_NUM = 2; // 每行显示数
			int row = (int) Math.ceil((double) cwbList.size() / ROW_NUM);
			BigDecimal receivablefeeTotal = BigDecimal.ZERO; // 代收货款
			StringBuilder huizongcwbs = new StringBuilder(""); // 订单号汇总
			for (int i = 0; i < row; i++) {
				String[] cwbArray = new String[ROW_NUM];
				for (int j = 0; j < ROW_NUM; j++) {
					int index = i * ROW_NUM + j;
					if (index >= cwbList.size()) {
						break;
					}
					CwbOrder cwb = cwbList.get(index);
					cwbArray[j] = cwb.getCwb();
					receivablefeeTotal = receivablefeeTotal.add(cwb.getReceivablefee());
					huizongcwbs.append(cwb.getCwb()).append("-H-");
				}
				cwbArrayList.add(cwbArray);
			}
			model.addAttribute("cwbArrayList", cwbArrayList);
			model.addAttribute("ordernum", cwbList.size());
			model.addAttribute("receivablefeeTotal", receivablefeeTotal);
			if (cwbList.size() > 0) {
				model.addAttribute("customerid", cwbList.get(0).getCustomerid());
			}
			model.addAttribute("huizongcwbs", huizongcwbs);
			if (org.apache.commons.lang3.StringUtils.isNotBlank(owg.getOutstockTime())) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String,String> createtimeMap;
				try {
					createtimeMap = mapper.readValue(owg.getOutstockTime(), Map.class);
				} catch (Exception e) {
					createtimeMap = new HashMap<String, String>();
				}
				model.addAttribute("strtime", createtimeMap.get("strtime"));
				model.addAttribute("endtime", createtimeMap.get("endtime"));
			}
			return "warehousegroup/wuHanPickingListPrint_history";		
		}
		else if (printtemplate.getTemplatetype() == 2) {

			List<JSONObject> cwbJson = this.cwbDao.getDetailForChuKuPrint(cwbs);

			model.addAttribute("cwbList", cwbJson);
			return "warehousegroup/outbillhuizongprinting_history";
		} else if (printtemplate.getTemplatetype() == 5) {
			List<CwbOrder> cwbList2 = this.cwbDao.getCwbByCwbs(cwbs);

			List<WarehouseGroupPrintDto> printDtos = new ArrayList<WarehouseGroupPrintDto>();

			for (int i = 0; i < cwbList2.size(); i++) {
				WarehouseGroupPrintDto warehouseGroupPrintDto = new WarehouseGroupPrintDto();
				warehouseGroupPrintDto.setBackcarnum(cwbList2.get(i).getBackcarnum());
				warehouseGroupPrintDto.setCaramount(cwbList2.get(i).getCaramount());
				warehouseGroupPrintDto.setCarrealweight(cwbList2.get(i).getCarrealweight());
				warehouseGroupPrintDto.setCarsize(cwbList2.get(i).getCarsize());
				warehouseGroupPrintDto.setCarwarehouse(this.branchDAO.getBranchById(Long.parseLong(cwbList2.get(i).getCarwarehouse())).getBranchname());
				warehouseGroupPrintDto.setConsigneeaddress(cwbList2.get(i).getConsigneeaddress());
				warehouseGroupPrintDto.setConsigneemobile(cwbList2.get(i).getConsigneemobile());
				warehouseGroupPrintDto.setConsigneename(cwbList2.get(i).getConsigneename());
				warehouseGroupPrintDto.setConsigneephone(cwbList2.get(i).getConsigneephone());
				warehouseGroupPrintDto.setConsigneepostcode(cwbList2.get(i).getConsigneepostcode());
				warehouseGroupPrintDto.setCustomername(this.customerDAO.getCustomerById(cwbList2.get(i).getCustomerid()).getCustomername());
				warehouseGroupPrintDto.setCwb(cwbList2.get(i).getCwb());
				warehouseGroupPrintDto.setCwbordertypeid(CwbOrderTypeIdEnum.getByValue(cwbList2.get(i).getCwbordertypeid()).getText());
				if (cwbList.get(i).getCwbremark().length() >= 30) {
					warehouseGroupPrintDto.setCwbremark(cwbList2.get(i).getCwbremark().substring(0, 30));
				} else {
					warehouseGroupPrintDto.setCwbremark(cwbList2.get(i).getCwbremark());
				}
				warehouseGroupPrintDto.setEmaildate(cwbList2.get(i).getEmaildate());
				warehouseGroupPrintDto.setPaybackfee(cwbList2.get(i).getPaybackfee());
				warehouseGroupPrintDto.setTuihuozhanrukutime(this.tuihuoRecordDAO.getTuihuoRecordByCwb(cwbList2.get(i).getCwb()).get(0).getTuihuozhanrukutime());
				warehouseGroupPrintDto.setTranscwb(cwbList2.get(i).getTranscwb());
				warehouseGroupPrintDto.setPaywayid(PaytypeEnum.getByValue(Integer.parseInt(cwbList2.get(i).getPaywayid() + "")).getText());
				/*
				 * System.out.println(cwbList.get(i).getBackreasonid());
				 * System.out.println(cwbList.get(i).getBackreason());
				 * System.out
				 * .println(reasonDAO.getReasonByReasonid(cwbList.get(i
				 * ).getBackreasonid()).getReasoncontent());
				 */
				if (cwbList2.get(i).getBackreasonid() == 0) {
					warehouseGroupPrintDto.setReasoncontent("无");
				} else {
					if (this.reasonDao.getReasonByReasonid(cwbList2.get(i).getBackreasonid()).getReasoncontent().length() >= 30) {
						warehouseGroupPrintDto.setReasoncontent(this.reasonDao.getReasonByReasonid(cwbList2.get(i).getBackreasonid()).getReasoncontent().substring(0, 30));
					} else {
						warehouseGroupPrintDto.setReasoncontent(this.reasonDao.getReasonByReasonid(cwbList2.get(i).getBackreasonid()).getReasoncontent());
					}
				}
				warehouseGroupPrintDto.setCredate(this.orderFlowDAO.getOrderCurrentFlowByCwb(cwbList2.get(i).getCwb()).getCredate().toString());
				warehouseGroupPrintDto.setReceivablefee(cwbList2.get(i).getReceivablefee());
				warehouseGroupPrintDto.setSendcarname(cwbList2.get(i).getSendcarname());
				warehouseGroupPrintDto.setSendcarnum(cwbList2.get(i).getScannum());
				warehouseGroupPrintDto.setStartbranch(this.branchDAO.getBranchByBranchid(cwbList2.get(i).getStartbranchid()).getBranchname());
				warehouseGroupPrintDto.setBackcarname(cwbList2.get(i).getBackcarname());
				printDtos.add(warehouseGroupPrintDto);
			}

			model.addAttribute("cwbOrderList", cwbList2);
			model.addAttribute("printDtos", printDtos);
			return "warehousegroup/outbilltongluprinting_history";
		} else if (printtemplate.getTemplatetype() == 4) {
			// 只知道需要打印的cwb
			List<CwbOrder> cwbList2 = this.cwbDao.getCwbByCwbs(cwbs);// 根据cwbs查询出需要打印的记录
			Set<String> baleSet = new HashSet<String>();
			List<WarehouseGroupPrintDto> warehouseGroupPrintDtos = new ArrayList<WarehouseGroupPrintDto>();// 显示值需要的list
			// 得到需要打印的合包号
			for (CwbOrder cwbOrder : cwbList2) {
				if (!cwbOrder.getPackagecode().equals("")) {
					baleSet.add(cwbOrder.getPackagecode());
				}
			}
			if (baleSet.size() == 0) {
				// 表示全部是订单没有合包
				for (CwbOrder cwbOrder : cwbList2) {
					// 重新创建一个打印dto
					WarehouseGroupPrintDto printDto = new WarehouseGroupPrintDto();
					printDto.setBaleno(cwbOrder.getCwb() + "(订单)");
					printDto.setJianshu(cwbOrder.getSendcarnum());
					printDto.setDanshu(1);
					printDto.setCarrealweight(cwbOrder.getCarrealweight());
					printDto.setChengzhong(BigDecimal.ZERO);
					// 获取车牌号
					if (cwbOrder.getCwbremark().length() > 30) {
						printDto.setCwbremark(cwbOrder.getCwbremark().substring(0, 30));
					} else {
						printDto.setCwbremark(cwbOrder.getCwbremark());
					}
					warehouseGroupPrintDtos.add(printDto);
				}
				if ((truckid != 0) && (truckid != -1)) {
					model.addAttribute("truckid", this.truckDAO.getTruckByTruckid(truckid).getTruckno());
				} else {
					model.addAttribute("truckid", "________");
				}

				model.addAttribute("printList", warehouseGroupPrintDtos);
				model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
				// 设置汇总的值
				WarehouseGroupPrintDto wPrintDtoTotal = new WarehouseGroupPrintDto();
				int danshu = 0;
				int jianshu = 0;
				BigDecimal carryweight = BigDecimal.ZERO;
				for (CwbOrder cwbOrder : cwbList2) {
					jianshu += cwbOrder.getSendcarnum();
					carryweight = carryweight.add(cwbOrder.getCarrealweight());
				}
				danshu = cwbList2.size();
				wPrintDtoTotal.setBaleno(cwbList2.size() + "");
				wPrintDtoTotal.setDanshu(danshu);
				wPrintDtoTotal.setJianshu(jianshu);
				wPrintDtoTotal.setCarrealweight(carryweight);
				wPrintDtoTotal.setChengzhong(BigDecimal.ZERO);
				wPrintDtoTotal.setCwbremark("");
				model.addAttribute("total", wPrintDtoTotal);
			} else {
				// 表示有好几个打印包号,包括了输入包号打印
				List<CwbOrder> cwbOrders = new ArrayList<CwbOrder>();// 创建一个新的list用户装含有包号的cwborder
				List<CwbOrder> cwbOrders2 = new ArrayList<CwbOrder>();// 创建一个list用于装没有包号的cwborder
				for (String baleno : baleSet) {
					for (CwbOrder cwbOrder : cwbList2) {
						if (cwbOrder.getPackagecode().equals(baleno)) {
							cwbOrders.add(cwbOrder);
						}
					}
				}
				// 循环匹配没有包号订单
				for (CwbOrder cwbOrder : cwbList2) {
					if (cwbOrder.getPackagecode().equals("")) {
						cwbOrders2.add(cwbOrder);
					}
				}
				// 将每一个包号中含有的cwborder做汇总
				for (String baleno : baleSet) {
					WarehouseGroupPrintDto warehouseGroupPrintDto = new WarehouseGroupPrintDto();
					int danshu = 0;
					int jianshu = 0;
					BigDecimal carryweight = BigDecimal.ZERO;
					for (CwbOrder cwbOrder : cwbOrders) {
						if (cwbOrder.getPackagecode().equals(baleno)) {
							jianshu += cwbOrder.getSendcarnum();
							danshu++;
							carryweight = carryweight.add(cwbOrder.getCarrealweight());
						}
					}
					warehouseGroupPrintDto.setBaleno(baleno);
					warehouseGroupPrintDto.setDanshu(danshu);
					warehouseGroupPrintDto.setJianshu(jianshu);
					warehouseGroupPrintDto.setCwbremark("");
					warehouseGroupPrintDto.setCarrealweight(carryweight);
					warehouseGroupPrintDto.setChengzhong(BigDecimal.ZERO);

					warehouseGroupPrintDtos.add(warehouseGroupPrintDto);

				}
				for (CwbOrder cwbOrder : cwbOrders2) {
					WarehouseGroupPrintDto warehouseGroupPrintDto = new WarehouseGroupPrintDto();
					warehouseGroupPrintDto.setBaleno(cwbOrder.getCwb() + "(订单)");
					warehouseGroupPrintDto.setDanshu(1);
					warehouseGroupPrintDto.setJianshu(cwbOrder.getSendcarnum());
					warehouseGroupPrintDto.setCarrealweight(cwbOrder.getCarrealweight());
					warehouseGroupPrintDto.setChengzhong(BigDecimal.ZERO);
					if (cwbOrder.getCwbremark().length() > 30) {
						warehouseGroupPrintDto.setCwbremark(cwbOrder.getCwbremark().substring(0, 30));
					} else {
						warehouseGroupPrintDto.setCwbremark(cwbOrder.getCwbremark());
					}
					warehouseGroupPrintDtos.add(warehouseGroupPrintDto);
				}

				if (baleSet.size() == 1) {
					model.addAttribute("truckid", this.truckDAO.getTruckByTruckid(this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getTruckid()).getTruckno());
				} else {

					if ((truckid != 0) && (truckid != -1)) {
						model.addAttribute("truckid", this.truckDAO.getTruckByTruckid(truckid).getTruckno());
					} else {
						model.addAttribute("truckid", "________");
					}
				}

				model.addAttribute("printList", warehouseGroupPrintDtos);
				model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
				// 设置汇总值
				WarehouseGroupPrintDto warehouseGroupPrintDtoTotal = new WarehouseGroupPrintDto();
				warehouseGroupPrintDtoTotal.setBaleno(cwbOrders.size() + cwbOrders2.size() + "");
				warehouseGroupPrintDtoTotal.setDanshu(cwbList2.size());
				int jianshu2 = 0;
				BigDecimal carryweight = BigDecimal.ZERO;
				for (CwbOrder cwbOrder : cwbList2) {
					jianshu2 += cwbOrder.getSendcarnum();
					carryweight = carryweight.add(cwbOrder.getCarrealweight());
				}
				warehouseGroupPrintDtoTotal.setJianshu(jianshu2);
				warehouseGroupPrintDtoTotal.setCarrealweight(carryweight);
				warehouseGroupPrintDtoTotal.setChengzhong(BigDecimal.ZERO);
				warehouseGroupPrintDtoTotal.setCwbremark("");
				model.addAttribute("total", warehouseGroupPrintDtoTotal);
			}
			return "warehousegroup/outanbaoprinting_history";
		} else if (printtemplate.getTemplatetype() == 10) {//武汉飞远 退货出站
			OutChangePrintVo print = initPrintData(cwbList, nextbranchid,"","", getSessionUser());//TODO 出库时间
			model.addAttribute("print", print);
			return "warehousegroup/outbackbillprintinghistory_wuhan";
		} else if (printtemplate.getTemplatetype() == 11) {//武汉飞远 中转出站
			OutChangePrintVo print = initPrintData(cwbList, nextbranchid,"","", getSessionUser());//TODO 出库时间
			model.addAttribute("print", print);
			return "warehousegroup/outchangebillprintinghistory_wuhan";
		}
		
		return null;
	}
	
	
	/**
	 * 初始化打印数据print add by vic.liang@pjbest.com 2016-08-12
	 * @param cwbList 打印订单列表
	 * @param nextbranchid 打印订单下一站
	 * @param starttime 打印订单出库开始时间
	 * @param endtime 打印订单出库结算时间
	 * @param user 打印用户
	 */
	private OutChangePrintVo initPrintData (List<CwbOrder> cwbList,long nextbranchid, String starttime, String endtime, User user) {
		String userbranchname = this.branchDAO.getBranchName(user.getBranchid());
		String nextbranchname = this.branchDAO.getBranchName(nextbranchid);
	    String username = user.getUsername();
		OutChangePrintVo print = new OutChangePrintVo();
		Map<Integer,List<String>> map = new HashMap<Integer, List<String>>();
	    int count = 0;//订单数
		int sum = 0;//件数
		StringBuilder sb = new StringBuilder();
	    if (cwbList != null) {
			int index = 1;
			List<String> list = null;
			for (int i=0; i < cwbList.size(); i++){
				int temp = i % 2; //2个订单一行
				if (temp == 0) {
					list = new ArrayList<String>();
					map.put(index, list);
					index++;
				}
				String cwb = cwbList.get(i).getCwb();
				list.add(cwb);
				count++;
				sum += cwbList.get(i).getSendcarnum();
				if (sb.length() > 0) {
					sb.append(",").append("'").append(cwb).append("'");
				} else {
					sb.append("'").append(cwb).append("'");
				}
			}
		}
	    if (!StringUtils.isEmpty(starttime)) {
	    	print.setStarttime(starttime);
	    	print.setEndtime(endtime);
	    } else {//没有输入查询时间
	    	Map<String,Object> mapTime = this.groupDetailDao.getGroupDetailDateTime(sb.toString());
		    if (mapTime != null) {
		    	print.setStarttime((String)mapTime.get("starttime"));
		    	print.setEndtime((String)mapTime.get("endtime"));
		    }
	    }
	    print.setCwbmap(map);
	    print.setCount(count);
	    print.setSum(sum);
		print.setUserbranchname(userbranchname);
		print.setNextbranchname(nextbranchname);
		print.setUsername(username);
		print.setPrinttime(DateTimeUtil.getNowDataTime());
		return print;
	}

	/**
	 * 燕赵出库信息查询
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/yzoutlist/{page}")
	public String yzoutlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<Branch> blist = this.branchDAO.getBanchByBranchidForCwbtobranchid(this.getSessionUser().getBranchid());
		if (branchid == 0) {
			String branchids = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getCwbtobranchid().length() == 0 ? "0" : this.branchDAO.getBranchByBranchid(
					this.getSessionUser().getBranchid()).getCwbtobranchid();
			model.addAttribute("outwarehousegroupList",
					this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchids, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue(), 0));
			model.addAttribute("page_obj", new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(branchids, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue(), 0),
					page, Page.ONE_PAGE_NUMBER));
		} else {
			model.addAttribute("outwarehousegroupList", this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0,
					OutwarehousegroupOperateEnum.ChuKu.getValue(), 0, this.getSessionUser().getBranchid()));
			model.addAttribute(
					"page_obj",
					new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue(), 0, this.getSessionUser()
							.getBranchid()), page, Page.ONE_PAGE_NUMBER));
		}

		model.addAttribute("bList", this.branchDAO.getAllEffectBranches());

		model.addAttribute("branchList", blist);
		model.addAttribute("page", page);
		return "warehousegroup/yzoutlist";
	}

	/**
	 * 燕赵出库交接单信息打印和查询
	 *
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/outbillprinting_yz/{outwarehousegroupid}")
	public String outbillprinting_yz(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
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

		List<JSONObject> cwbJson = this.cwbDao.getSomeDetailForYZPrint(outwarehousegroupid);

		model.addAttribute("owg", owg);
		model.addAttribute("cwbJson", cwbJson);
		model.addAttribute("cwborderlist", this.cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/outbillprinting_yz";
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
		owgAllList = this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, this.getSessionUser().getBranchid(), "", "", userid, OutwarehousegroupOperateEnum.FenZhanDaoHuo.getValue(), 0, this
				.getSessionUser().getBranchid());
		// }

		model.addAttribute("owgAllList", owgAllList);
		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(this.getSessionUser().getBranchid(), "", "", userid, OutwarehousegroupOperateEnum.FenZhanDaoHuo.getValue(), 0, this
						.getSessionUser().getBranchid()), page, Page.ONE_PAGE_NUMBER));
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
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ZhongZhuanChuKu.getValue(), 0, this
						.getSessionUser().getBranchid()), page, Page.ONE_PAGE_NUMBER));
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

		List<JSONObject> cwbJson = this.cwbDao.getSomeDetailForPrint(outwarehousegroupid);

		model.addAttribute("owg", owg);
		model.addAttribute("cwbJson", cwbJson);
		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());

		model.addAttribute("cwborderlist", this.cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", this.branchDAO.getBranchByBranchid(this.outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/changebillprinting_txd";
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
	public String returnlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		//List<Branch> bList = this.getNextPossibleBranches();
		//List<Branch> lastList = new ArrayList<Branch>();
		List<PrintView> printList = new ArrayList<PrintView>();
		
		List<Branch> bList = this.cwborderService.getNextPossibleBranches(this.getSessionUser());
		List<Branch> removeList = new ArrayList<Branch>();
		for (Branch b : bList) {// 去掉中转站
			if ((b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) || (b.getSitetype() == BranchEnum.ZhanDian.getValue())) {
				removeList.add(b);
			}
		}
		bList.removeAll(removeList);
		
		/*for (Branch b : bList) {
			// 退货站
			if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
				lastList.add(b);
			}
		}*/
		model.addAttribute("branchlist", bList);

		if (isshow > 0) {
			List<GroupDetail> gdList = this.groupDetailDao.getCwbForChuKuPrintTime(this.getSessionUser().getBranchid(), branchid, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), begindate, enddate);
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

		model.addAttribute("printList", printList);
		model.addAttribute("type",1);
		model.addAttribute("printtemplateList",
				this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.TuiHuoChuZhanAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.TuiHuoChuZhanHuiZong.getValue()));
		return "warehousegroup/returnlist";
	}

	/**
	 * 退货出站交接单打印和查询
	 *
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/returnbillprinting_default")
	public String returnbillprinting_default(Model model, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		String cwbs = "", cwbstr = "";
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbstr += isprint[i] + ",";
		}

		List<CwbOrder> cwbList = this.cwbDao.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1));

		this.printcwbDetailDAO
				.crePrintcwbDetail(new PrintcwbDetail(0, this.getSessionUser().getUserid(), new Timestamp(System.currentTimeMillis()), cwbstr, FlowOrderTypeEnum.TuiHuoChuZhan.getValue()));

		SystemInstall si = this.systemInstallDAO.getSystemInstallByName("chukujiaojiedan");
		model.addAttribute("template", this.printTemplateDAO.getPrintTemplate(Long.parseLong(si.getValue())));
		model.addAttribute("cwbList", cwbList);
		model.addAttribute("isback", "1");
		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(cwbList.get(0).getNextbranchid()).getBranchname());
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());

		return "warehousegroup/outbillprinting_template";
	}

	/**
	 * 历史退货出站信息查询
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/historyreturnlist/{page}")
	public String historyreturnlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		/*List<Branch> blist = this.getNextPossibleBranches();
		List<Branch> removeList=new ArrayList<Branch>();
		for (Branch branch : blist) {
			if (branch.getSitetype()==4) {
				removeList.add(branch);
			}
		}
		blist.removeAll(removeList);*/
//		model.addAttribute("branchList", blist);
		List<Branch> bList = this.getNextPossibleBranches();
		List<Branch> lastList = new ArrayList<Branch>();
		for (Branch b : bList) {
			// 退货站
			if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
				lastList.add(b);
			}
		}
		model.addAttribute("branchList", lastList);

		model.addAttribute("outwarehousegroupList", this.outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0,
				OutwarehousegroupOperateEnum.TuiHuoChuZhan.getValue(), 0, this.getSessionUser().getBranchid()));
		model.addAttribute(
				"page_obj",
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.TuiHuoChuZhan.getValue(), 0, this.getSessionUser()
						.getBranchid()), page, Page.ONE_PAGE_NUMBER));

		model.addAttribute("printtemplateList",
				this.printTemplateDAO.getPrintTemplateByOpreatetype(PrintTemplateOpertatetypeEnum.TuiHuoChuZhanAnDan.getValue() + "," + PrintTemplateOpertatetypeEnum.TuiHuoChuZhanHuiZong.getValue()));
		model.addAttribute("page", page);
		return "warehousegroup/historyreturnlist";
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
				new Page(this.outwarehousegroupDao.getOutWarehouseGroupCount(this.getSessionUser().getBranchid(), beginemaildate, endemaildate, userid,
						OutwarehousegroupOperateEnum.TuiHuoZhanRuku.getValue(), 0, this.getSessionUser().getBranchid()), page, Page.ONE_PAGE_NUMBER));
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

	@RequestMapping("/exportforoutwarehouse")
	public void exportforoutwarehouse(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "isprint", required = false, defaultValue = "") String[] isprint) {
		String mouldfieldids2 = request.getParameter("exportmould2");
		String cwbs = "";
		String[] cwbArr = new String[isprint.length];
		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i].trim().length() == 0) {
				continue;
			}
			cwbs += "'" + isprint[i] + "',";
			cwbArr[i] = isprint[i];
		}

		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}
		final String exportcwbs = cwbs;
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
			final String sql = this.cwbDao.getSQLExportKeFu(exportcwbs);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = WarehouseGroup_detailController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = WarehouseGroup_detailController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = WarehouseGroup_detailController.this.branchDAO.getAllBranches();
					final List<Common> commonList = WarehouseGroup_detailController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = WarehouseGroup_detailController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = WarehouseGroup_detailController.this.remarkDAO.getRemarkByCwbs(exportcwbs);
					final Map<String, Map<String, String>> remarkMap = WarehouseGroup_detailController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = WarehouseGroup_detailController.this.reasonDao.getAllReason();
					WarehouseGroup_detailController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								Object a = WarehouseGroup_detailController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList,
										remarkMap, reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = WarehouseGroup_detailController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
											complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : WarehouseGroup_detailController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : WarehouseGroup_detailController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : WarehouseGroup_detailController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
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

	/*
	 * public Map<String,Map<String,String>>
	 * getOrderFlowByCredateForDetailAndExportAllTime(List<String>
	 * cwbs,List<Branch> branchlist){
	 *
	 * Map<String,Map<String,String>> map=new HashMap<String,
	 * Map<String,String>>(); try{ List<OrderFlow> ofList =
	 * orderFlowDAO.getOrderFlowByCwbs(cwbs); for(OrderFlow of : ofList){
	 * if(of.getFlowordertype()==
	 * FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()){ getCwbRow(map,
	 * of).put("customerbrackhouseremark", of.getComment()); }else
	 * if(of.getFlowordertype()== FlowOrderTypeEnum.RuKu.getValue()){
	 * getCwbRow(map, of).put("Instoreroomtime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
	 * if(getsitetype
	 * (branchlist,of.getBranchid())==BranchEnum.ZhongZhuan.getValue()){
	 * getCwbRow(map, of).put("zhongzhuanzhanIntime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); } }else
	 * if(of.getFlowordertype()==
	 * FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue
	 * ()||of.getFlowordertype()==
	 * FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()){
	 * getCwbRow(map, of).put("InSitetime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); }else
	 * if(of.getFlowordertype()== FlowOrderTypeEnum.FenZhanLingHuo.getValue()){
	 * getCwbRow(map, of).put("PickGoodstime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); }else
	 * if(of.getFlowordertype()== FlowOrderTypeEnum.ChuKuSaoMiao.getValue()){
	 * getCwbRow(map, of).put("Outstoreroomtime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
	 * if(getsitetype
	 * (branchlist,of.getBranchid())==BranchEnum.ZhongZhuan.getValue()){
	 * getCwbRow(map, of).put("zhongzhuanzhanOuttime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); } }else
	 * if(of.getFlowordertype()== FlowOrderTypeEnum.YiShenHe.getValue()){
	 * getCwbRow(map, of).put("Goclasstime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); }else
	 * if(of.getFlowordertype()== FlowOrderTypeEnum.YiFanKui.getValue()){
	 * getCwbRow(map, of).put("Gobacktime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); }else
	 * if
	 * (of.getFlowordertype()==FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue
	 * ()) { getCwbRow(map, of).put("tuigonghuoshangchukutime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); }
	 * if(getCwbRow(map,
	 * of).get("Newchangetime")==null||of.getCredate().getTime()> new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getCwbRow(map,
	 * of).get("Newchangetime")).getTime()){ getCwbRow(map,
	 * of).put("Newchangetime", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate())); } }
	 * }catch (Exception e) {} return map; }
	 */
	private Map<String, String> getCwbRow(Map<String, Map<String, String>> map, OrderFlow of) {
		if (map.get(of.getCwb()) == null) {
			map.put(of.getCwb(), new HashMap<String, String>());
		}
		Map<String, String> map2 = map.get(of.getCwb());
		return map2;
	}

	private long getsitetype(List<Branch> branchlist, long branchid) {
		long sitetype = 0;
		if (branchlist.size() > 0) {
			for (Branch b : branchlist) {
				if (branchid == b.getBranchid()) {
					sitetype = b.getSitetype();
					break;
				}
			}
		}
		return sitetype;
	}

	/**
	 * 出库信息查询(按包)
	 *
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return jm's ideas 直接用cwbdetail
	 *         按照nextbranchid和当前操作flowordertype来查询到对应的订单信息 这是未打印
	 */
	@RequestMapping("/outbalelist/{page}")
	public String outbalelist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-1") String[] branchid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno) {
		List<PrintView> printList = new ArrayList<PrintView>();
		List<Branch> bList = this.getNextPossibleBranches();
		model.addAttribute("branchlist", bList);
		String branchids = this.getStrings(branchid);
		if (isshow > 0) {
			// 过滤订单打印表
			List<GroupDetail> gdList = this.groupDetailDao.getCwbForChuKuPrintTimeNewForBale(this.getSessionUser().getBranchid(), branchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "", "", baleno);
			// 过滤包号表
			List<Bale> baleList = this.baleDAO.getBaleByBalePrint(this.getSessionUser().getBranchid(), baleno, strtime, endtime);

			List<Branch> branchList = this.branchDAO.getAllBranches();
			printList = this.warehouseGroupDetailService.getChuKuBaleView(gdList, baleList, branchList);
		}
		model.addAttribute("printList", printList);
		model.addAttribute("type", 1);
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("branchids", branchids);
		return "warehousegroup/outbalelist";
	}

	/**
	 * 出库交接单打印(按包号)
	 *
	 * @param model
	 * @param request
	 * @param isprint
	 * @param nextbranchid
	 * @return
	 */
	@RequestMapping("/outbalelist_print")
	public String outbalelist_print(Model model, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		List<Branch> branchList = this.branchDAO.getAllBranches();

		String baleids = "";

		for (int i = 0; i < isprint.length; i++) {
			if (isprint[i]==null||isprint[i].trim().length() == 0||isprint[i].trim().equals("0")) {
				continue;
			}
			baleids +=  isprint[i] + ",";
		}
		baleids = baleids.length()>0?baleids.substring(0, baleids.length()-1):"-1";

		List<Long> nextbranchids = this.groupDetailDao.getBranchIdsGroupBYbranchid(baleids);

		Map<Long, List<Map<String,Object>>> branchmap = new HashMap<Long, List<Map<String,Object>>>();

		if((nextbranchids != null) && (nextbranchids.size()>0)){
			// 循环包号
			for (int i = 0; i < nextbranchids.size(); i++) {

				List<Long> banchidsbaleids = this.groupDetailDao.getBalesBybranchid(baleids, nextbranchids.get(i) );
				List<Map<String,Object>> bList = new ArrayList<Map<String,Object>>();
				for(long baleid : banchidsbaleids){
					Map<String,Object> map = new HashMap<String,Object>();
					// 根据包号查找订单打印表数据
					List<GroupDetail> groupDetailList = this.groupDetailDao.getGroupDetailListByBale(baleid);
					if ((groupDetailList != null) && !groupDetailList.isEmpty()) {
						// 获得包号、发货地、目的地
						map.put("baleid", groupDetailList.get(0).getBaleid());
						map.put("baleno", groupDetailList.get(0).getBaleno());
						map.put("nextbranchname", this.warehouseGroupDetailService.getQueryBranchName(branchList, groupDetailList.get(0).getNextbranchid()));

						String cwbs = "";
						for (int j = 0; j < groupDetailList.size(); j++) {
							cwbs += "'" + groupDetailList.get(j).getCwb() + "',";
						}
						// 获取订单数量、代收总金额
						List<Map<String, Object>> cwbList = this.cwbDao.getCwbByPrintCwbs(cwbs.substring(0, cwbs.lastIndexOf(",")));
						
						Map<String, Object> cwbListView = this.warehouseGroupDetailService.getChuKuBaleCwbView(cwbList, groupDetailList.get(0).getBaleno(),baleid);
						map.put("cwbListView", cwbListView);
						map.put("drivername", groupDetailList.get(0).getDriverid()>0? this.userDAO.getAllUserByid(groupDetailList.get(0).getDriverid()).getRealname():"");
						bList.add(map);
					}
				}
				branchmap.put(nextbranchids.get(i),bList);
			}
		}
		Map<Long ,String> branchMap = new HashMap<Long,String>();

			List<Branch> branchList2 = this.branchDAO.getAllBranches();
			for (Branch branch : branchList2) {
				branchMap.put(branch.getBranchid(), branch.getBranchname());
			}

		model.addAttribute("branchMap", branchMap);
		// 获得名称：XXX出仓交接单
		SystemInstall sy = this.systemInstallDAO.getSystemInstallByName("chukujiaojiedananbao");

		model.addAttribute("nextbranchids", nextbranchids);
		model.addAttribute("branchname", this.warehouseGroupDetailService.getQueryBranchName(branchList, this.getSessionUser().getBranchid()));
		model.addAttribute("username",  this.getSessionUser().getRealname());
		model.addAttribute("templateName", sy.getValue() == null ? "" : sy.getValue());

		model.addAttribute("baleids", baleids);//todo jsp
		model.addAttribute("branchmap", branchmap);
		PrintTemplate printTemplate = this.printTemplateDAO.getPrintTemplateByType(4);
		Map<String,String> tmap = new HashMap<String,String>();
		if(printTemplate == null){
			tmap.put("danshu", "danshu");
			tmap.put("jianshu", "jianshu");
			tmap.put("zhongliang", "zhongliang");
		}else{
			JSONArray tjson =  null;
			try {
				tjson = JSONArray.fromObject(printTemplate.getDetail()) ;
			} catch (Exception e) {
				logger.error("", e);
			}
			if(tjson != null){
				for (Object object : tjson) {
					JSONObject o = JSONObject.fromObject(object);
					if("danshu".equals(o.getString("field")) ){
						tmap.put("danshu", "danshu");
					}
					if("jianshu".equals(o.getString("field")) ){
						tmap.put("jianshu", "jianshu");
					}
					if("carrealweight".equals(o.getString("field")) ){
						tmap.put("carrealweight", "carrealweight");
					}
				}
			}

		}
		model.addAttribute("tmap", tmap);

		return "warehousegroup/outbalelist_print";
	}

	/**
	 * 更新包号状态为已打印
	 *
	 * @param model
	 * @param request
	 * @param balenos
	 * @return
	 */
	@RequestMapping("/outbalelist_update")
	public @ResponseBody
	String outbalelist_update(Model model, HttpServletRequest request, @RequestParam(value = "baleids", defaultValue = "", required = true) String baleids) {
		String[] baleidsStr =baleids.split(",");
		try {
			for (int i = 0; i < baleidsStr.length; i++) {
				if (baleidsStr[i]==null||baleidsStr[i].trim().length() == 0||baleidsStr[i].trim().equals("0")) {
					continue;
				}
				long baleid=Long.parseLong(baleidsStr[i]);
				if(baleid<1){
					continue;
				}
				this.groupDetailDao.updateGroupDetailListByBale(baleid);
			}
			return "{\"errorCode\":0,\"error\":\"成功\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 出库交接单打印(按包) 历史打印记录
	 *
	 * @param model
	 * @param page
	 * @param type
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/outbalelist_history/{page}")
	public String outbalelist_history(Model model, @PathVariable("page") long page, @RequestParam(value = "nextbranchid", required = false, defaultValue = "0") long nextbranchid,
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		try {
			List<Branch> branchList = this.branchDAO.getAllBranches();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long startTime = 0, endTime = 0;
			if (!"".equals(starttime)) {
				startTime = ft.parse(starttime).getTime();
			}
			if (!"".equals(endtime)) {
				endTime = ft.parse(endtime).getTime();
			}
			model.addAttribute("list", this.groupDetailDao.getGroupDetailhistoryByBale(page, this.getSessionUser().getBranchid(), startTime, endTime, nextbranchid));
			model.addAttribute("page_obj", new Page(this.groupDetailDao.getGroupDetailhistoryByBaleCount(this.getSessionUser().getBranchid(), startTime, endTime, nextbranchid), page,
					Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
			model.addAttribute("branchList", branchList);
		} catch (ParseException e) {
			logger.error("", e);
		}

		return "warehousegroup/outbalelist_history";
	}
}
