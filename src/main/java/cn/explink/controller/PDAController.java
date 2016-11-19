package cn.explink.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.JsonGenerationException;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tps.TpsCwbFlowService;
import cn.explink.core.utils.StringUtils;
import cn.explink.dao.BaleCwbDao;
import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchRouteDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbALLStateControlDAO;
import cn.explink.dao.CwbApplyZhongZhuanDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbKuaiDiDAO;
import cn.explink.dao.CwbStateControlDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.EntranceDAO;
import cn.explink.dao.ExceedFeeDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.MenuDAO;
import cn.explink.dao.OperationTimeDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.StockResultDAO;
import cn.explink.dao.SwitchDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TruckDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.YpdjHandleRecordDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.Branch;
import cn.explink.domain.ChangeGoodsTypeResult;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbDetailView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbStateControl;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EmailDate;
import cn.explink.domain.Entrance;
import cn.explink.domain.GroupDetail;
import cn.explink.domain.JsonContext;
import cn.explink.domain.Menu;
import cn.explink.domain.OperationTime;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.OrderGoods;
import cn.explink.domain.PrintStyle;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.Role;
import cn.explink.domain.SetExportField;
import cn.explink.domain.Smtcount;
import cn.explink.domain.StockResult;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.Truck;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.WavFileName;
import cn.explink.domain.VO.express.BatchCount;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.BranchTypeEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderPDAEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.switchs.SwitchEnum;
import cn.explink.exception.CwbException;
import cn.explink.param.AutoAllocationParam;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.schedule.Constants;
import cn.explink.service.BaleService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderTypeService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.CwbRouteService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.EmailDateService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.KfzdOrderService;
import cn.explink.service.OneToMoreService;
import cn.explink.service.UserService;
import cn.explink.service.OrderBackCheckService;
import cn.explink.service.docking.AutoAllocationService;
import cn.explink.service.express.ExpressOutStationService;
import cn.explink.service.mps.CwbOrderBranchInfoModificationService;
import cn.explink.service.mps.MPSCommonService;
import cn.explink.service.mps.MPSOptStateService;
import cn.explink.service.mps.OrderInterceptService;
import cn.explink.service.mps.release.OutWarehouseMPSReleaseService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StreamingStatementCreator;
import cn.explink.util.StringUtil;
import cn.explink.util.Tongxing.SocketClient;

@RequestMapping("/PDA")
@Controller
public class PDAController {

	private static final String PLAY_GP_SOUND = "playGPSound";

	private static final String PLAY_YPDJ_SOUND = "RUKUPCandPDAaboutYJDPWAV";
	private static final int PORT = 8008;

	private Logger logger = LoggerFactory.getLogger(PDAController.class);
	@Autowired
	CwbALLStateControlDAO cwbALLStateControlDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	OutWarehouseGroupDAO outWarehouseGroupDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	EntranceDAO entranceDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	StockResultDAO stockResultDAO;
	@Autowired
	TruckDAO truckDAO;
	@Autowired
	GroupDetailDao groupdetailDAO;
	@Autowired
	MenuDAO menuDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	GroupDetailDao groupDetailDAO;
	@Autowired
	BaleDao baleDAO;
	@Autowired
	SwitchDAO switchDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	BranchRouteDAO branchRouteDAO;
	@Autowired
	ExceedFeeDAO exceedFeeDAO;
	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	YpdjHandleRecordDAO ypdjHandleRecordDAO;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	OperationTimeDAO operationTimeDAO;

	@Autowired
	DataStatisticsService dataStatisticsService;

	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	CwbKuaiDiDAO cwbKuaiDiDAO;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;
	@Autowired
	EmailDateService emailDateService;
	@Autowired
	ExportwarhousesummaryDAO exportwarhousesummaryDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	OneToMoreService otmservice;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	OrderBackCheckDAO orderBackCheckDAO;
	@Autowired
	CwbApplyZhongZhuanDAO cwbApplyZhongZhuanDAO;
	@Autowired
	AutoAllocationService autoAllocationService;
	@Autowired
	private BaleCwbDao baleCwbDao;

	@Autowired
	CwbStateControlDAO cwbStateControlDAO;

	@Autowired
	private CwbOrderService cwbOrderService;

	@Autowired
	ExpressOutStationService expressOutStationService;

	@Autowired
	private KfzdOrderService kfzdOrderService;

	@Autowired
	private OutWarehouseMPSReleaseService outWarehouseMPSReleaseService;

	@Autowired
	private OrderInterceptService orderInterceptService;

	@Autowired
	private CwbOrderBranchInfoModificationService cwbOrderBranchInfoModificationService;
	@Autowired
	private MPSCommonService mpsCommonService;
	
	@Autowired
	private TpsCwbFlowService tpsCwbFlowService;
	
	@Autowired
	private BaleService baleService;
	
	@Autowired
	private CwbOrderTypeService cwbOrderTypeService;
	
	@Autowired
	private MPSOptStateService mpsOptStateService ;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderBackCheckService orderBackCheckService;

	private ObjectMapper om = new ObjectMapper();

	private boolean playGPSound = true;

	private boolean playYPDJSound = true;
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/test")
	public String test(Model model) {

		return "readWav";
	}

	@RequestMapping("/changeGoodsType")
	public String changeGoodsType() {
		return "pda/changeGoodsType";
	}

	@RequestMapping("/submitGoodsTypeChange")
	public @ResponseBody ChangeGoodsTypeResult submitGoodsTypeChange(String orderNos, int goodsType) {
		ChangeGoodsTypeResult result = this.cwbOrderService.changeGoodsType(orderNos, goodsType);

		return result;
	}

	@RequestMapping("/getpdaMenu")
	public @ResponseBody String getpdaMenu(Model model) {
		List<Menu> mList = this.menuDAO.getMenusByUserRoleidToPDA(this.getSessionUser().getRoleid());
		String functionids = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getFunctionids();
		String pdaMenu = "";
		for (Menu m : mList) {
			if (functionids.indexOf(m.getMenuno()) != -1) {
				pdaMenu += m.getMenuno() + ",";
			}
		}
		if (pdaMenu.length() > 0) {
			pdaMenu = pdaMenu.substring(0, pdaMenu.length() - 1);
		}
		return pdaMenu;
	}

	/**
	 * 提货
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/intoWarehousForGetGoods")
	public String intoWarehousForGetGoods(Model model) {
		List<Customer> cList = this.customerDAO.getAllCustomers();
		List<User> uList = this.userDAO.getUserByRole(3);
		List<CwbOrder> weiTiHuolist = this.cwbDAO.getDaoRuByBranchidForList(this.getSessionUser().getBranchid());
		List<CwbOrder> yiTiHuolist = this.cwbDAO.getYiTiByBranchidForList(this.getSessionUser().getBranchid());
		long weiTiHuoCount = this.cwbDAO.getDaoRubyBranchid(this.getSessionUser().getBranchid(), -1l).getOpscwbid();
		long yiTiHuoCount = this.cwbDAO.getTiHuobyBranchid(this.getSessionUser().getBranchid(), -1l).getOpscwbid();

		model.addAttribute("customerlist", cList);
		model.addAttribute("userList", uList);
		model.addAttribute("weiTiHuolist", weiTiHuolist);
		model.addAttribute("yiTiHuolist", yiTiHuolist);
		model.addAttribute("weiTiHuoCount", weiTiHuoCount);
		model.addAttribute("yiTiHuoCount", yiTiHuoCount);
		return "pda/intoWarehousForGetGoodsBatch";
	}

	/**
	 * 进入入库的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/intowarhousenodetail")
	public String intowarhousenodetail(Model model) {
		List<Customer> cList = this.customerDAO.getAllCustomers();
		List<Entrance> eList=this.entranceDAO.getAllEnableEntrances();
		List<User> uList = this.userDAO.getUserByRole(3);
		model.addAttribute("customerlist", cList);
		model.addAttribute("entrancelist", eList);
		model.addAttribute("userList", uList);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.RuKuDaYinBiaoQian.getText()));
		model.addAttribute("RUKUPCandPDAaboutYJDPWAV",
				this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV") == null ? "yes" : this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV").getValue());

		model.addAttribute("isprintnew", this.systemInstallDAO.getSystemInstall("isprintnew").getValue());

		return "pda/intowarhouse_nodetail";
	}

	@RequestMapping("/onetomore")
	public String getmore() {

		return "pda/onetomore";
	}

	@RequestMapping("/oneTOmore")
	public String getMoreByOne(Model model, HttpServletRequest request) {
		String cwb = request.getParameter("cwb") == null ? "" : request.getParameter("cwb");
		String transcwb = request.getParameter("transcwb") == null ? "" : request.getParameter("transcwb");
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		if (cwbOrder == null) {
			request.setAttribute("message", "订单号不存在！");
			return "pda/onetomore";
		}
		if (!((cwbOrder.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) || (cwbOrder.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()))) {
			request.setAttribute("message", "已入库订单,运单号无法补入!");
			return "pda/onetomore";
		}
		String transcwbs = this.otmservice.replaceTranscwb(cwb, transcwb);
		this.cwbDAO.updateTranscwb(cwb, transcwbs);// 更新数据库一票多件订单补入

		List<TranscwbView> translist = this.transCwbDao.getTransCwbByCwb(cwb);
		StringBuffer buffer = new StringBuffer();
		for (Iterator iterator = translist.iterator(); iterator.hasNext();) {
			TranscwbView transcwbView = (TranscwbView) iterator.next();
			buffer.append(transcwbView.getTranscwb() + ",");
		}
		String trancwbs = buffer.substring(0, buffer.length() - 1).toString();
		for (String str : transcwbs.split(",")) {
			if (!trancwbs.contains(str)) {
				this.transCwbDao.saveTranscwb(str, cwb);
			}
		}
		request.setAttribute("message", "补入成功！");
		return "pda/onetomore";
	}

	/**
	 * 进入入库的功能页面（明细）
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/intowarhouse")
	public String intowarhouse(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "isscanbaleTag", defaultValue = "0") long isscanbaleTag, @RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		long startTime = System.currentTimeMillis();
		List<Customer> cList = this.customerDAO.getAllCustomers();
		List<Entrance> eList=this.entranceDAO.getAllEnableEntrances();//分拨入口查询
		//是否开启自动分拣设置
		String autoAllocatingSwitch = this.systemInstallDAO.getSystemInstall("AutoAllocating").getValue();
		List<User> uList = this.userDAO.getUserByRole(3);
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		// TODO 按批次查询
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未入库
		List<CwbOrder> weirukulist = this.cwbDAO.getRukuByBranchidForList(b.getBranchid(), b.getSitetype(), 1, customerid, emaildate);
		List<CwbDetailView> weirukuViewlist = this.getcwbDetail(weirukulist, cList, showCustomerjSONArray, null, 0);

		// 已入库
		List<CwbOrder> yirukulist = this.cwbDAO.getYiRukubyBranchidList(b.getBranchid(), customerid, 1, emaildate);
		List<CwbDetailView> yirukuViewlist = this.getcwbDetail(yirukulist, cList, showCustomerjSONArray, null, 0);
		String isOpenDialog = this.systemInstallDAO.getSystemInstallByName("Dialog").getValue();
		model.addAttribute("isOpenDialog", isOpenDialog);
		model.addAttribute("isscanbaleTag", isscanbaleTag);
		model.addAttribute("weirukulist", weirukuViewlist);
		model.addAttribute("yirukulist", yirukuViewlist);
		model.addAttribute("sitetype", b.getSitetype());
		model.addAttribute("customerlist", cList);
		model.addAttribute("userList", uList);
		model.addAttribute("entrancelist", eList);
		model.addAttribute("auto_allocat", autoAllocatingSwitch);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.RuKuDaYinBiaoQian.getText()));
		model.addAttribute("RUKUPCandPDAaboutYJDPWAV",
				this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV") == null ? "yes" : this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV").getValue());
		model.addAttribute("isprintnew", this.systemInstallDAO.getSystemInstall("isprintnew").getValue());
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("ifshowtag", this.systemInstallDAO.getSystemInstall("ifshowbudatag") == null ? null : this.systemInstallDAO.getSystemInstall("ifshowbudatag").getValue());
		
//		/**
//		 * 对接自动分拨的中间件,放在进入页面的时机是因为在系统启动时很有可能没有打开中间件客户端
//		 */
//		if(autoAllocatingSwitch.equals("1")){
//			this.createSocketConnectMap(eList);
//		}
		this.logger.info("进入分拣库入库页面的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return "pda/intowarhouse";
	}
	

	/**
	 * 进入中转站入库的功能页面（明细）
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/changeintowarhouse")
	public String changeintowarhouse(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "isscanbaleTag", defaultValue = "0") long isscanbaleTag) {

		List<Customer> cList = this.customerDAO.getAllCustomers();
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Entrance> eList=this.entranceDAO.getAllEnableEntrances();//分拨入口查询
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		//是否开启自动分拣设置
		String autoAllocatingSwitch = this.systemInstallDAO.getSystemInstall("AutoAllocating").getValue();
		// TODO 按批次查询
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未入库
		List<CwbOrder> weirukulist = this.cwbDAO.getZhongZhuanZhanRukuByBranchidForList(b.getBranchid(), b.getSitetype(), 1, customerid);
		List<CwbDetailView> weirukuViewlist = this.getcwbDetail(weirukulist, cList, showCustomerjSONArray, null, 0);

		// 已入库
		List<CwbOrder> yirukulist = this.cwbDAO.getZhongZhuanZhanYiRukubyBranchidList(b.getBranchid(), customerid, 1);
		List<CwbDetailView> yirukuViewlist = this.getcwbDetail(yirukulist, cList, showCustomerjSONArray, null, 0);
		model.addAttribute("isscanbaleTag", isscanbaleTag);
		model.addAttribute("weirukulist", weirukuViewlist);
		model.addAttribute("yirukulist", yirukuViewlist);
		model.addAttribute("sitetype", b.getSitetype());
		model.addAttribute("customerlist", cList);
		model.addAttribute("userList", uList);
		model.addAttribute("entrancelist", eList);
		model.addAttribute("auto_allocat", autoAllocatingSwitch);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.RuKuDaYinBiaoQian.getText()));
		model.addAttribute("RUKUPCandPDAaboutYJDPWAV",
				this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV") == null ? "yes" : this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV").getValue());
		model.addAttribute("showCustomerSign", showCustomerSign);
//		/**
//		 * 对接自动分拨的中间件,放在进入页面的时机是因为在系统启动时很有可能没有打开中间件客户端
//		 */
//		if(autoAllocatingSwitch.equals("1")){
//			this.createSocketConnectMap(eList);
//		}
		return "pda/changeintowarhouse";
	}

	/**
	 * 入库扫描 未入库 list
	 *
	 * @return
	 */
	@RequestMapping("/getimportweirukulist")
	public @ResponseBody List<CwbDetailView> getimportweirukulist(@RequestParam(value = "page", defaultValue = "1") long page, @RequestParam(value = "customerid", defaultValue = "0") long customerid,
			@RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 供货商
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<CwbOrder> weirukulist = this.cwbDAO.getRukuByBranchidForList(b.getBranchid(), b.getSitetype(), page, customerid, emaildate);
		List<CwbDetailView> weirukuVeiwList = this.getcwbDetail(weirukulist, customerList, showCustomerjSONArray, null, 0);

		return weirukuVeiwList;

	}

	/**
	 * 入库扫描 已入库 list
	 *
	 * @return
	 */
	@RequestMapping("/getimportyiruku")
	public @ResponseBody List<CwbDetailView> getimportyiruku(@RequestParam(value = "page", defaultValue = "1") long page, @RequestParam(value = "customerid", defaultValue = "0") long customerid,
			@RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 供货商
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<CwbOrder> yiruku = this.cwbDAO.getYiRukubyBranchidList(this.getSessionUser().getBranchid(), customerid, page, emaildate);
		List<CwbDetailView> yirukuVeiwList = this.getcwbDetail(yiruku, customerList, showCustomerjSONArray, null, 0);
		return yirukuVeiwList;

	}

	/**
	 * 进入入库的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/branchimort")
	public String branchimort(Model model) {
		List<User> uList = this.userDAO.getUserByRole(3);

		model.addAttribute("userList", uList);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.RuKuDaYinBiaoQian.getText()));
		return "pda/branchimport";
	}

	/**
	 * 进入到货的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/branchimortdetail")
	public String branchimortdetail(Model model) {
		// 根据角色获取yonn
		List<User> uList = this.userDAO.getUserByRole(3);
		// 获取供应商集合
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		// 获取站点集合
		List<Branch> branchList = this.branchDAO.getAllBranches();
		String showintowarehousedata = "no";
		try {
			showintowarehousedata = this.systemInstallDAO.getSystemInstallByName("showintowarehousedata").getValue();
		} catch (Exception e) {
			this.logger.error("分站到货时，”分站到货未到货数据是否显示库房入库的数据“系统配置获取失败");
		}

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;

		//历史未到货用 2016-3-12
		//String flowordertypesHis = FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue();
		String flowordertypes = FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue() + "," + FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue() + ","
				+ FlowOrderTypeEnum.DingDanLanJie.getValue()
                // add by 王志宇 在今日未到货中添加揽件出站判断条件
				+ "," + FlowOrderTypeEnum.LanJianChuZhan.getValue();
		if (showintowarehousedata.equals("yes")) {
			flowordertypes += "," + FlowOrderTypeEnum.RuKu.getValue();
		}
		// 今日出库(未到货)订单数
		List<String> jinriweidaohuocwbslist = this.operationTimeDAO.getOrderFlowJinRiChuKuORRuKuListAll(this.getSessionUser().getBranchid(), flowordertypes, DateTimeUtil.getCurrentDayZeroTime());
		// orderFlowDAO.getOrderFlowJinRiChuKuORRuKuList(getSessionUser().getBranchid(),
		// flowordertypes, DateTimeUtil.getCurrentDayZeroTime());

		String jinriweidaohuocwbs = "";
		List<CwbOrder> jinriweidaohuolist = new ArrayList<CwbOrder>();
		if (jinriweidaohuocwbslist.size() > 0) {
			jinriweidaohuocwbs = this.getStrings(jinriweidaohuocwbslist);
			jinriweidaohuolist = this.cwbDAO.getJinRiDaoHuoByBranchidForList(flowordertypes, 1, jinriweidaohuocwbs);
		}

		// 今日未到货list
		List<CwbDetailView> jinriweidaohuoViewlist = this.getcwbDetail(jinriweidaohuolist, customerList, showCustomerjSONArray, branchList, 1);
		// 历史未到货list
		List<String> lishiweidaohuocwbslist = this.operationTimeDAO.getlishiweidaohuoAll(this.getSessionUser().getBranchid(), flowordertypes, DateTimeUtil.getCurrentDayZeroTime());
		String lishiweidaocwbs = "";
		List<CwbOrder> historyweidaohuolist = new ArrayList<CwbOrder>();
		if (lishiweidaohuocwbslist.size() > 0) {
			// 历史未到货订单String
			lishiweidaocwbs = this.getStrings(lishiweidaohuocwbslist);
			// 历史未到货集合 状态为 出库 中转 入库
			historyweidaohuolist = this.cwbDAO.getHistoryDaoHuoByBranchidForList(flowordertypes, 1, lishiweidaocwbs);
		}
		// 页面显示历史未到货
		List<CwbDetailView> historyweidaohuoViewlist = this.getcwbDetail(historyweidaohuolist, customerList, showCustomerjSONArray, branchList, 1);
		// 已到货list
		List<String> yidaohuocwbs = this.operationTimeDAO.getyidaohuoByBranchid(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		String yidaohuo = "";
		List<CwbOrder> yidaohuolist = new ArrayList<CwbOrder>();
		if (yidaohuocwbs.size() > 0) {
			yidaohuo = this.getStrings(yidaohuocwbs);

			yidaohuolist = this.cwbDAO.getHistoryDaoHuoByBranchidForList(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.LanJianRuZhan.getValue(), this.getSessionUser().getBranchid(), 1, yidaohuo);

		}
		List<CwbDetailView> yidaohuoViewlist = this.getcwbDetail(yidaohuolist, customerList, showCustomerjSONArray, branchList, 2);
		// 揽件未到货list
		List<CwbOrder> lanjianweidaohuolist = this.cwbDAO.getLanJianWeiDaoZhanByBranchidForList(this.getSessionUser().getBranchid(), 1);
		List<CwbDetailView> lanjianweidaohuoViewlist = this.getcwbDetail(lanjianweidaohuolist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, branchList, 1);

		model.addAttribute("lanjianweidaohuolist", lanjianweidaohuoViewlist);
		model.addAttribute("jinriweidaohuolist", jinriweidaohuoViewlist);
		model.addAttribute("historyweidaohuolist", historyweidaohuoViewlist);

		model.addAttribute("yidaohuoViewlist", yidaohuoViewlist);
		model.addAttribute("customerlist", customerList);
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("userList", uList);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.RuKuDaYinBiaoQian.getText()));
		return "pda/branchimortdetail";
	}

	/**
	 * 站点入站 已到站 list
	 *
	 * @return
	 */
	@RequestMapping("/getbranchimportyidaolist")
	public @ResponseBody List<CwbDetailView> getbranchimportyidaolist(@RequestParam(value = "page", defaultValue = "1") long page) {
		List<Branch> branchList = this.branchDAO.getAllBranches();
		List<String> yidaohuocwbs = this.operationTimeDAO.getyidaohuoByBranchid(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		String yidaohuo = "";
		List<CwbOrder> cList = new ArrayList<CwbOrder>();
		if (yidaohuocwbs.size() > 0) {
			yidaohuo = this.getStrings(yidaohuocwbs);
			cList = this.cwbDAO.getYiDaohuobyCwbsForFenzhandaohuo(yidaohuo, page);
		}
		// List<CwbOrder>cList
		// =cwbDAO.getYiDaohuobyBranchidList(getSessionUser().getBranchid(),page);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		List<CwbDetailView> yidaozhanViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, branchList, 2);
		return yidaozhanViewlist;
	}

	/**
	 * 分站到货今日到货 list
	 *
	 * @return
	 */
	@RequestMapping("/getbranchimportjinriweidaolist")
	public @ResponseBody List<CwbDetailView> getbranchimportjinriweidaolist(@RequestParam(value = "page", defaultValue = "1") long page) {
		List<Branch> branchList = this.branchDAO.getAllBranches();
		String showintowarehousedata = "no";
		try {
			showintowarehousedata = this.systemInstallDAO.getSystemInstallByName("showintowarehousedata").getValue();
		} catch (Exception e) {
			this.logger.error("分站到货时，”分站到货未到货数据是否显示库房入库的数据“系统配置获取失败");
		}

		String flowordertypes = FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue();
		if (showintowarehousedata.equals("yes")) {
			flowordertypes += "," + FlowOrderTypeEnum.RuKu.getValue();
		}
		// 今日出库(未到货)订单数
		List<String> jinriweidaohuocwbslist = this.operationTimeDAO.getOrderFlowJinRiChuKuORRuKuListAll(this.getSessionUser().getBranchid(), flowordertypes, DateTimeUtil.getCurrentDayZeroTime());

		String jinriweidaohuocwbs = "";
		List<CwbOrder> jinriweidaohuolist = new ArrayList<CwbOrder>();
		if (jinriweidaohuocwbslist.size() > 0) {
			jinriweidaohuocwbs = this.getStrings(jinriweidaohuocwbslist);
			jinriweidaohuolist = this.cwbDAO.getJinRiDaoHuoByBranchidForList(this.getSessionUser().getBranchid(), flowordertypes, page, jinriweidaohuocwbs);
		}

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		List<CwbDetailView> jinriweidaohuoViewlist = this.getcwbDetail(jinriweidaohuolist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, branchList, 1);

		return jinriweidaohuoViewlist;
	}

	/**
	 * 分站到货历史未到货list
	 *
	 * @param page
	 * @return
	 */
	@RequestMapping("/getbranchimporthistoryweidaolist")
	public @ResponseBody List<CwbDetailView> getbranchimporthistoryweidaolist(@RequestParam(value = "page", defaultValue = "1") long page) {
		List<Branch> branchList = this.branchDAO.getAllBranches();
		String showintowarehousedata = "no";
		try {
			showintowarehousedata = this.systemInstallDAO.getSystemInstallByName("showintowarehousedata").getValue();
		} catch (Exception e) {
			this.logger.error("分站到货时，”分站到货未到货数据是否显示库房入库的数据“系统配置获取失败");
		}

		String flowordertypes = FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue();
		if (showintowarehousedata.equals("yes")) {
			flowordertypes += "," + FlowOrderTypeEnum.RuKu.getValue();
		}
		List<String> lishiweidaohuocwbslist = this.operationTimeDAO.getlishiweidaohuoAll(this.getSessionUser().getBranchid(), flowordertypes, DateTimeUtil.getCurrentDayZeroTime());
		String lishiweidaocwbs = "";
		List<CwbOrder> historyweidaohuolist = new ArrayList<CwbOrder>();
		if (lishiweidaohuocwbslist.size() > 0) {
			lishiweidaocwbs = this.getStrings(lishiweidaohuocwbslist);
			historyweidaohuolist = this.cwbDAO.getHistoryDaoHuoByBranchidForList(flowordertypes, this.getSessionUser().getBranchid(), 1, lishiweidaocwbs);
		}
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");

		// 历史未到货
		List<CwbDetailView> historyweidaohuoViewlist = this.getcwbDetail(historyweidaohuolist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, branchList, 1);

		return historyweidaohuoViewlist;

	}

	/**
	 * 到货扫描（批量）
	 *
	 * @param model
	 * @param cwbs
	 * @return
	 */
	@RequestMapping("/cwbbranchintowarhouseBatch")
	public String cwbbranchintowarhouseBatch(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid) {
		long allcwbnum = 0;
		long SuccessCount = 0;
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;

		Branch b = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());

		List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取供货商列表
		List<Branch> branchList = this.branchDAO.getAllBranches();

		List<JSONObject> objList = new ArrayList<JSONObject>();
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			allcwbnum++;
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.substationGoods(this.getSessionUser(), cwb, scancwb, -1, 0, "", "", false);
				this.baleService.disableBale(scancwb);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				SuccessCount++;
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
				obj.put("cwbOrder", cwbOrder);
				obj.put("errorcode", ce.getError().getValue());
				obj.put("errorinfo", ce.getMessage());
				if (cwbOrder == null) {// 如果无此订单
					obj.put("customername", "");
					obj.put("outstoreroomtime", "");// 出库时间
				} else {
					OrderFlow of = this.orderFlowDAO.getOrderFlowByCwbAndFlowtype(cwbOrder.getCwb(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "");
					for (Customer c : customerList) {
						if (c.getCustomerid() == cwbOrder.getCustomerid()) {
							obj.put("customername", c.getCustomername());
							break;
						}
					}
					obj.put("outstoreroomtime", of != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()) : "");// 出库时间
				}
			}
			objList.add(obj);
		}
		model.addAttribute("objList", objList);

		model.addAttribute("customerlist", customerList);

		String showintowarehousedata = "no";
		try {
			showintowarehousedata = this.systemInstallDAO.getSystemInstallByName("showintowarehousedata").getValue();
		} catch (Exception e) {
			this.logger.error("分站到货时，”分站到货未到货数据是否显示库房入库的数据“系统配置获取失败");
		}

		String flowordertypes = FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue() + "," + FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue() + ","
				+ FlowOrderTypeEnum.DingDanLanJie.getValue();
		if (showintowarehousedata.equals("yes")) {
			flowordertypes += "," + FlowOrderTypeEnum.RuKu.getValue();
		}

		// 今日出库(未到货)订单数
		List<String> jinriweidaohuocwbslist = this.operationTimeDAO.getOrderFlowJinRiChuKuORRuKuListAll(this.getSessionUser().getBranchid(), flowordertypes, DateTimeUtil.getCurrentDayZeroTime());
		// lishi
		List<String> lishiweidaohuocwbslist = this.operationTimeDAO.getlishiweidaohuoAll(this.getSessionUser().getBranchid(), flowordertypes, DateTimeUtil.getCurrentDayZeroTime());
		String jinriweidaohuocwbs = "";
		long jinriweidaocount = 0;
		List<CwbOrder> jinriweidaohuolist = new ArrayList<CwbOrder>();
		if (jinriweidaohuocwbslist.size() > 0) {
			jinriweidaohuocwbs = this.getStrings(jinriweidaohuocwbslist);
			jinriweidaocount = this.cwbDAO.getJinRiWeiDaoHuoCount(flowordertypes, jinriweidaohuocwbs);
			// 历史未到货订单list
			jinriweidaohuolist = this.cwbDAO.getJinRiDaoHuoByBranchidForList(flowordertypes, 1, jinriweidaohuocwbs);
		}
		String lishiweidaohuocwbs = "";
		long historyweidaocount = 0;
		List<CwbOrder> historyweidaohuolist = new ArrayList<CwbOrder>();
		if (lishiweidaohuocwbslist.size() > 0) {
			lishiweidaohuocwbs = this.getStrings(lishiweidaohuocwbslist);
			historyweidaocount = this.cwbDAO.getJinRiWeiDaoHuoCount(flowordertypes, lishiweidaohuocwbs);
			historyweidaohuolist = this.cwbDAO.getHistoryDaoHuoByBranchidForList(flowordertypes, 1, lishiweidaohuocwbs);
		}
		// 今日未到货订单list

		// 今日未到货明细
		List<CwbDetailView> jinriweidaohuoViewlist = this.getcwbDetail(jinriweidaohuolist, customerList, showCustomerjSONArray, branchList, 1);

		List<CwbDetailView> historyweidaohuoViewlist = this.getcwbDetail(historyweidaohuolist, customerList, showCustomerjSONArray, branchList, 1);

		List<CwbOrder> yidaohuolist = this.cwbDAO.getYiDaohuobyBranchidList(b.getBranchid(), 1);

		// 揽件未到货list
		List<CwbOrder> lanjianweidaohuolist = this.cwbDAO.getLanJianWeiDaoZhanByBranchidForList(b.getBranchid(), 1);
		List<CwbDetailView> lanjianweidaohuoViewlist = this.getcwbDetail(lanjianweidaohuolist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, branchList, 1);
		long lanjianweidaohuocount = this.cwbDAO.countLanJianWeiDaoZhanByBranch(b.getBranchid());

		// 已到货明细
		List<CwbDetailView> yirukuViewlist = this.getcwbDetail(yidaohuolist, customerList, showCustomerjSONArray, branchList, 2);
		model.addAttribute("jinriweidaocount", jinriweidaocount);// 今日未到货总数
		model.addAttribute("historyweidaocount", historyweidaocount);// 历史未到货总数

		model.addAttribute("lanjianweidaohuocount", lanjianweidaohuocount);//
		model.addAttribute("lanjianweidaohuolist", lanjianweidaohuoViewlist);//

		model.addAttribute("jinriweidaohuolist", jinriweidaohuoViewlist);
		model.addAttribute("historyweidaohuolist", historyweidaohuoViewlist);
		model.addAttribute("yidaohuonum", this.cwbDAO.getYiDaohuobyBranchid(this.getSessionUser().getBranchid()).getOpscwbid());
		model.addAttribute("lesscwbnum", this.ypdjHandleRecordDAO.getDaoHuoQuejianCount(this.getSessionUser().getBranchid()));
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("yidaohuolist", yirukuViewlist);
		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + SuccessCount + "单，异常" + (allcwbnum - SuccessCount) + "单";
		}
		model.addAttribute("msg", msg);

		return "pda/branchimportBatch";
	}

	/**
	 * 站点到货 已到货list
	 */

	@RequestMapping("/getbranchimportbatchyidaolist")
	public @ResponseBody List<CwbDetailView> getbranchimportbatchyidaolist(@RequestParam(value = "page", defaultValue = "1") long page) {
		List<Branch> branchList = this.branchDAO.getAllBranches();
		List<CwbOrder> cList = this.cwbDAO.getYiDaohuobyBranchidList(this.getSessionUser().getBranchid(), page);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 已入库明细
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, branchList, 2);
		return weidaohuoViewlist;

	}

	/**
	 * 进入入库（包）的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/intowarhouseforbale")
	public String intowarhouseforbale(Model model) {
		List<Customer> cList = this.customerDAO.getAllCustomers();
		List<User> uList = this.userDAO.getUserByRole(3);

		model.addAttribute("customerlist", cList);
		model.addAttribute("userList", uList);
		return "pda/intowarhouseforbale";
	}

	/**
	 * 进入分站退货出站的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/branchbackexport")
	public String branchbackexport(Model model, @RequestParam(value = "branchid", defaultValue = "0") long branchid, @RequestParam(value = "isscanbaleTag", defaultValue = "0") long isscanbaleTag// 为包号修改
	) {
		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser(), BranchEnum.TuiHuo);

		// TODO只有做了订单拦截的订单才是待退货
		List<CwbOrder> cwbAllList = this.getAuditTuiHuo();
		List<CwbOrder> yichuzhanlist = this.cwbDAO.getCwbByFlowOrderTypeAndNextbranchidAndStartbranchidList(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), this.getSessionUser().getBranchid(), branchid);
		List<CwbOrder> ypeisong = new ArrayList<CwbOrder>();
		List<CwbOrder> yshangmenhuan = new ArrayList<CwbOrder>();
		List<CwbOrder> yshangmentui = new ArrayList<CwbOrder>();
		for (CwbOrder cwb : yichuzhanlist) {
			if (cwb.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) {
				ypeisong.add(cwb);
			} else if (cwb.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				yshangmenhuan.add(cwb);
			} else if (cwb.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				yshangmentui.add(cwb);
			}
		}
		List<CwbOrder> wpeisong = new ArrayList<CwbOrder>();
		List<CwbOrder> wshangmenhuan = new ArrayList<CwbOrder>();
		List<CwbOrder> wshangmentui = new ArrayList<CwbOrder>();
		for (CwbOrder cwb : cwbAllList) {
			// added shenhongfei 2016-1-22
			if ((cwb.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) || (cwb.getCwbstate() == CwbStateEnum.BUFENDIUSHI.getValue())
					|| (cwb.getCwbstate() == CwbStateEnum.BUFENPOSUN.getValue()) || (cwb.getCwbstate() == CwbStateEnum.WANQUANPOSUN.getValue())) {
				wpeisong.add(cwb);
			} else if (cwb.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				wshangmenhuan.add(cwb);
			} else if (cwb.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				wshangmentui.add(cwb);
			}
		}
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		List<CwbDetailView> weichukuViewlist = this.getcwbDetail(cwbAllList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		List<CwbDetailView> wpeisongViewlist = this.getcwbDetail(wpeisong, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		List<CwbDetailView> wshangmenhuanViewlist = this.getcwbDetail(wshangmenhuan, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		List<CwbDetailView> wshangmentuiViewlist = this.getcwbDetail(wshangmentui, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);

		List<CwbDetailView> yichuzhanViewlist = this.getcwbDetail(yichuzhanlist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		List<CwbDetailView> ypeisongViewlist = this.getcwbDetail(ypeisong, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		List<CwbDetailView> yshangmenhuanViewlist = this.getcwbDetail(yshangmenhuan, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		List<CwbDetailView> yshangmentuiViewlist = this.getcwbDetail(yshangmentui, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);

		List<User> uList = this.userDAO.getUserByRole(3);

		model.addAttribute("yichuzhanlist", yichuzhanViewlist);
		model.addAttribute("ypeisong", ypeisongViewlist);
		model.addAttribute("yshangmenhuan", yshangmenhuanViewlist);
		model.addAttribute("yshangmentui", yshangmentuiViewlist);

		model.addAttribute("weichukulist", weichukuViewlist);
		model.addAttribute("wpeisong", wpeisongViewlist);
		model.addAttribute("wshangmenhuan", wshangmenhuanViewlist);
		model.addAttribute("wshangmentui", wshangmentuiViewlist);

		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuBuYunXu.getText()));
		model.addAttribute("ckfb_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuFengBao.getText()));
		model.addAttribute("isscanbaleTag", isscanbaleTag);
		return "pda/branchbackexport";
	}

	private List<CwbOrder> getAuditTuiHuo() {
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		List<CwbOrder> cwbAllList = new ArrayList<CwbOrder>();
		/*
		 * if (isUseAuditTuiHuo.equals("yes")) { cwbAllList =
		 * this.cwbDAO.getCwbOrderByFlowOrderTypeAndCurrentbranchid
		 * (FlowOrderTypeEnum.DingDanLanJie.getValue(),
		 * this.getSessionUser().getBranchid()); } else {
		 */
		cwbAllList.addAll(this.cwbDAO.getCwbOrderByFlowOrderTypeAndDeliveryStateAndCurrentbranchid(FlowOrderTypeEnum.YiShenHe, DeliveryStateEnum.JuShou, this.getSessionUser().getBranchid()));
		cwbAllList.addAll(this.cwbDAO.getCwbOrderByFlowOrderTypeAndDeliveryStateAndCurrentbranchid(FlowOrderTypeEnum.YiShenHe, DeliveryStateEnum.BuFenTuiHuo, this.getSessionUser().getBranchid()));
		cwbAllList.addAll(this.cwbDAO.getCwbOrderByFlowOrderTypeAndDeliveryStateAndCurrentbranchid(FlowOrderTypeEnum.YiShenHe, DeliveryStateEnum.ShangMenHuanChengGong, this.getSessionUser()
				.getBranchid()));
		cwbAllList.addAll(this.cwbDAO.getCwbOrderByFlowOrderTypeAndDeliveryStateAndCurrentbranchid(FlowOrderTypeEnum.YiShenHe, DeliveryStateEnum.ShangMenTuiChengGong, this.getSessionUser()
				.getBranchid()));

		// add shenhongfei 2016-1-22

		String cwbStates = CwbStateEnum.BUFENDIUSHI.getValue() + "," + CwbStateEnum.BUFENPOSUN.getValue() + "," + CwbStateEnum.WANQUANPOSUN.getValue() + "," + CwbStateEnum.TuiHuo.getValue();
		String flowordertypes = FlowOrderTypeEnum.DingDanLanJie.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
		cwbAllList.addAll(this.cwbDAO.getCwbOrderByFlowOrderTypeAndDeliveryStateAndIsmpsflagAndCurrentbranchid(cwbStates, flowordertypes, this.getSessionUser().getBranchid()));

		// }
		return cwbAllList;
	}

	/**
	 * 分站 退货出站 已扫描list
	 *
	 * @return
	 */
	@RequestMapping("/getbranchbackexportyisaomiaolist")
	public @ResponseBody List<CwbDetailView> getbranchbackexportyisaomiaolist(@RequestParam(value = "branchid", defaultValue = "0") long nextbranchid) {
		List<CwbOrder> cList = this.cwbDAO.getCwbByFlowOrderTypeAndNextbranchidAndStartbranchidList(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), this.getSessionUser().getBranchid(), 0);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 已入库明细
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;

	}

	/**
	 * 分站 退货出站 待出货list
	 *
	 * @return
	 */
	@RequestMapping("/getbranchbackexportdaichuhuolist")
	public @ResponseBody List<CwbDetailView> getbranchbackexportdaichuhuolist() {
		List<CwbOrder> cwbAllList = this.getAuditTuiHuo();
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 待退货明细
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cwbAllList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;
	}

	/**
	 * 进入退货站退货出站的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/backbranchbackexport")
	public String backbranchbackexport(Model model) {
		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());
		List<Branch> lastList = new ArrayList<Branch>();
		for (Branch b : bList) {// 去掉中转站
			if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
				lastList.add(b);
			}
		}

		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();
		long branchid = this.getSessionUser().getBranchid();
		List<CwbOrder> weichukulist = this.cwbDAO.getKDKChukuForCwbOrder(branchid, lastList.size() > 0 ? lastList.get(0).getBranchid() : 0, CwbStateEnum.TuiHuo.getValue());
		model.addAttribute("weichukulist", weichukulist);
		model.addAttribute("yichukulist", this.cwbDAO.getYiChuKubyBranchidList(branchid, lastList.size() > 0 ? lastList.get(0).getBranchid() : 0, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), 1));
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("branchlist", lastList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuBuYunXu.getText()));
		model.addAttribute("ckfb_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuFengBao.getText()));
		return "pda/backbranchbackexport";
	}

	/**
	 * 退货站出站扫描 已出站 list
	 *
	 * @param branchid
	 * @return
	 */

	@RequestMapping("/getbackbranchbackexportyichuzhanlist")
	public @ResponseBody List<CwbDetailView> getbackbranchbackexportyichuzhanlist(@RequestParam(value = "branchid", defaultValue = "0") long branchid) {
		List<CwbOrder> cList = this.cwbDAO.getYiChuKubyBranchidList(this.getSessionUser().getBranchid(), branchid, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), 1);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);

		return weidaohuoViewlist;

	}

	/**
	 * 退货站出站扫描 未出站 list
	 *
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/getbackbranchbackexportweichuzhanlist")
	public @ResponseBody List<CwbDetailView> getbackbranchbackexportweichuzhanlist(@RequestParam(value = "branchid", defaultValue = "0") long branchid) {
		List<CwbOrder> cList = this.cwbDAO.getKDKChukuForCwbOrder(this.getSessionUser().getBranchid(), branchid, CwbStateEnum.TuiHuo.getValue());
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);

		return weidaohuoViewlist;

	}

	/**
	 * 进入分站中转出站的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/branchchangeexport")
	public String branchchangeexport(Model model) {
		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());
		List<Branch> removeList = new ArrayList<Branch>();
		model.addAttribute("branchalllist", bList);
		SystemInstall isZhongZhuan = this.systemInstallDAO.getSystemInstall("isZhongZhuanShow");
		int isZhong = 1;// 默认 1.显示库房 2.只显示中转 3.显示所有货物流向

		try {
			isZhong = isZhongZhuan == null ? 1 : Integer.parseInt(isZhongZhuan.getValue());
		} catch (NumberFormatException e) {
			this.logger.error("", e);
		}
		for (Branch b : bList) {// 去掉退货站
			if (isZhong == 1) {
				if ((b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) || (b.getSitetype() == BranchEnum.KuFang.getValue())) {
					removeList.add(b);
				}
			} else if (isZhong == 2) {
				if (b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
					removeList.add(b);
				}
			} else {
				removeList.add(b);
			}
		}
		List<CwbOrder> dzzlist = this.cwbDAO.getDaizhongzhuan(this.getSessionUser().getBranchid());
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<Reason> reasonlist = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ChangeTrains.getValue());
		model.addAttribute("reasonlist", reasonlist);
		model.addAttribute("branchlist", removeList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("dzzlist", dzzlist);
		model.addAttribute("customerList", customerList);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuBuYunXu.getText()));
		model.addAttribute("ckfb_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuFengBao.getText()));
		return "pda/branchchangeexport";
	}

	/**
	 * 进入出库的功能页面（明细）
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/exportwarhouse")
	public String exportwarhouse(Model model, @RequestParam(value = "branchid", defaultValue = "0") long branchid, @RequestParam(value = "isscanbaleTag", defaultValue = "0") long isscanbaleTag) {
		long startTime = System.currentTimeMillis();
		//Modified by leoliao at 2016-07-11 把已停用的站点过滤
		List<Branch> listNextBranch = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());
		List<Branch> bList = this.filterNoEffectBranch(listNextBranch);
		//Modified end
		
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		List<Customer> cList = this.customerDAO.getAllCustomers();
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		int cwbstate = 1;
		/*
		 * if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) { cwbstate = 2;
		 * }
		 */
		List<String> cwbyichukuList = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(b.getBranchid(), branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		String cwbs = "";
		if (cwbyichukuList.size() > 0) {
			cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbyichukuList);
		} else {
			cwbs = "'--'";
		}
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未出库明细
		List<CwbOrder> weichukulist = this.cwbDAO.getChukuForCwbOrderByBranchid(b.getBranchid(), cwbstate, 1, branchid);
		List<CwbDetailView> weichukuViewlist = this.getcwbDetail(weichukulist, cList, showCustomerjSONArray, null, 0);
		// 已出库明细
		List<CwbOrder> yichukulist = this.cwbDAO.getCwbByCwbsPage(1, cwbs, Page.DETAIL_PAGE_NUMBER);
		List<CwbDetailView> yichukuViewlist = this.getcwbDetail(yichukulist, cList, showCustomerjSONArray, null, 0);
		String isOpenDialog = this.systemInstallDAO.getSystemInstallByName("Dialog").getValue();
		/***************add****************/
		// modify by bruce shangguan 20160908   获取电子秤称重时长
		SystemInstall systemInstall = this.systemInstallDAO.getSystemInstall("weightTime") ;
		String weightTime = "0" ; // 电子秤称重时长默认为10秒
		if(systemInstall != null && !StringUtils.isEmpty(systemInstall.getValue()) && systemInstall.getValue().trim().matches("^0|([1-9][0-9]*)$")){
			weightTime = systemInstall.getValue() ;
		}
		model.addAttribute("weightTime", weightTime);
		// end 20160908
		/**************end****************/
		model.addAttribute("weichukulist", weichukuViewlist);
		model.addAttribute("yichukulist", yichukuViewlist);
		model.addAttribute("customerlist", cList);
		model.addAttribute("isOpenDialog", isOpenDialog);
		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuBuYunXu.getText()));
		model.addAttribute("ckfb_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuFengBao.getText()));
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("isscanbaleTag", isscanbaleTag);
		this.logger.info("进入分拣库出库页面的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return "pda/exportwarhouse";
	}

	/**
	 * 进入中转站出库的功能页面（明细）
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/changeexportwarhouse")
	public String changeexportwarhouse(Model model, @RequestParam(value = "branchid", defaultValue = "0") long branchid, @RequestParam(value = "isscanbaleTag", defaultValue = "0") long isscanbaleTag) {
		//Modified by leoliao at 2016-07-11 把已停用的站点过滤
		List<Branch> listNextBranch = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());
		List<Branch> bList = this.filterNoEffectBranch(listNextBranch);
		//Modified end
		
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		List<Customer> cList = this.customerDAO.getAllCustomers();

		List<String> cwbyichukuList = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(this.getSessionUser().getBranchid(), branchid,
				FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
		String cwbs = "";
		if (cwbyichukuList.size() > 0) {
			cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbyichukuList);
		} else {
			cwbs = "'--'";
		}
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未出库明细
		List<CwbOrder> weichukulist = this.cwbDAO.getZhongZhuanZhanChukuForCwbOrderByBranchid(this.getSessionUser().getBranchid(), 1, branchid);
		List<CwbDetailView> weichukuViewlist = this.getcwbDetail(weichukulist, cList, showCustomerjSONArray, null, 0);
		// 已出库明细
		List<CwbOrder> yichukulist = this.cwbDAO.getCwbByCwbsPage(1, cwbs, Page.DETAIL_PAGE_NUMBER);
		List<CwbDetailView> yichukuViewlist = this.getcwbDetail(yichukulist, cList, showCustomerjSONArray, null, 0);

		model.addAttribute("weichukulist", weichukuViewlist);
		model.addAttribute("yichukulist", yichukuViewlist);
		model.addAttribute("customerlist", cList);

		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuBuYunXu.getText()));
		model.addAttribute("ckfb_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuFengBao.getText()));
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("isscanbaleTag", isscanbaleTag);
		return "pda/changeexportwarhouse";
	}

	/**
	 * 得到出库明细 已出库list
	 *
	 * @return
	 */
	@RequestMapping("/getexportyichukulist")
	public @ResponseBody List<CwbDetailView> getexportyichukulist(@RequestParam(value = "branchid", defaultValue = "0") long branchid, @RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "flowordertype", defaultValue = "6") long flowordertype) {
		List<String> cwbyichukuList = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(this.getSessionUser().getBranchid(), branchid, flowordertype);

		String cwbs = "";
		if (cwbyichukuList.size() > 0) {
			cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbyichukuList);
		} else {
			cwbs = "'--'";
		}

		List<CwbOrder> cList = this.cwbDAO.getCwbByCwbsPage(page, cwbs, Page.DETAIL_PAGE_NUMBER);

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 供货商
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<CwbDetailView> yichukuVeiwList = this.getcwbDetail(cList, customerList, showCustomerjSONArray, null, 0);

		return yichukuVeiwList;

	}

	/**
	 * 得到 出库明细 未入库 list
	 *
	 * @return
	 */
	@RequestMapping("/getexportweichukulist")
	public @ResponseBody List<CwbDetailView> getexportweichukulist(@RequestParam(value = "branchid", defaultValue = "0") long branchid, @RequestParam(value = "page", defaultValue = "1") long page) {
		Branch localbranch = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		int cwbstate = CwbStateEnum.PeiShong.getValue();
		if (localbranch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
			cwbstate = CwbStateEnum.TuiHuo.getValue();
		}

		List<CwbOrder> cList = this.cwbDAO.getChukuForCwbOrderByBranchid(localbranch.getBranchid(), cwbstate, page, branchid);

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 供货商
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<CwbDetailView> weichukuVeiwList = this.getcwbDetail(cList, customerList, showCustomerjSONArray, null, 0);

		return weichukuVeiwList;

	}

	/**
	 * 进入出库的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/exportwarhousenodetail")
	public String exportwarhousenodetail(Model model) {
		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("ckfb_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuFengBao.getText()));
		return "pda/exportwarhousenodetail";
	}

	/**
	 * 加急件出库
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/jiajijianchukulist")
	public String jiajijian(Model model) {
		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("ckfb_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuFengBao.getText()));
		return "pda/jiajijianchukulist";
	}

	/**
	 * 进入站点出站的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/branchexportwarhouse")
	public String branchexportwarhouse(Model model, @RequestParam(value = "branchid", defaultValue = "0") long branchid) {
		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());
		List<Branch> lastList = new ArrayList<Branch>();

		for (Branch b : bList) {
			if (b.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				lastList.add(b);
			}
		}
		List<User> uList = this.userDAO.getUserByRole(3);

		List<Customer> cList = this.customerDAO.getAllCustomers();

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		List<CwbDetailView> weichukulistViewlist = this.getcwbDetail(this.cwbDAO.getZhanDianChuZhanbyBranchidList(this.getSessionUser().getBranchid(), branchid == 0 ? (lastList.size() == 0 ? 0
				: lastList.get(0).getBranchid()) : branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()), cList, showCustomerjSONArray, null, 1);
		List<String> cwbs = this.operationTimeDAO.getchaoqi(this.getSessionUser().getBranchid(), branchid == 0 ? (lastList.size() == 0 ? 0 : lastList.get(0).getBranchid()) : branchid,
				FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		List<CwbDetailView> yichukulistViewList = this.getcwbDetail(this.cwbDAO.getZhanDianYiChuZhanbyBranchidList(cwbs), cList, showCustomerjSONArray, null, 1);

		model.addAttribute("weichukulist", weichukulistViewlist);
		model.addAttribute("yichukulist", yichukulistViewList);
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("customerlist", cList);

		model.addAttribute("branchlist", lastList);
		model.addAttribute("userList", uList);
		return "pda/branchexportwarhouse";
	}

	/**
	 * 站点出站 已扫描list
	 *
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/getbranchexportwarehouseyisaomiao")
	public @ResponseBody List<CwbDetailView> getbranchexportwarehouseyisaomiao(@RequestParam(value = "branchid", defaultValue = "0") long branchid) {
		List<String> cwbs = this.operationTimeDAO.getchaoqi(this.getSessionUser().getBranchid(), branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		List<CwbOrder> cList = this.cwbDAO.getZhanDianYiChuZhanbyBranchidList(cwbs);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 已入库明细
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;

	}

	/**
	 * 站点出站 未出库list
	 *
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/getbranchexportwarehousedaichuku")
	public @ResponseBody List<CwbDetailView> getbranchexportwarehousedaichuku(@RequestParam(value = "branchid", defaultValue = "0") long branchid) {
		List<CwbOrder> cList = this.cwbDAO.getZhanDianChuZhanbyBranchidList(this.getSessionUser().getBranchid(), branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 已入库明细
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;

	}

	/**
	 * 进入库房对库房出库的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/kdkexportwarhouse")
	public String kdkexportwarhouse(Model model, @RequestParam(value = "branchid", defaultValue = "0") long branchid) {
		List<Branch> bList = this.cwbOrderService.getNextPossibleKuFangBranches(this.getSessionUser());
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		List<Customer> cList = this.customerDAO.getAllCustomers();
		List<CwbOrder> weichukulist = this.cwbDAO.getKDKChukuForCwbOrder(this.getSessionUser().getBranchid(), branchid == 0 ? (bList.size() > 0 ? bList.get(0).getBranchid() : 0) : branchid, -1);
		List<String> cwbyichukuList = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(this.getSessionUser().getBranchid(), branchid == 0 ? (bList.size() > 0 ? bList.get(0)
				.getBranchid() : 0) : branchid, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue());
		String cwbs = "";
		if (cwbyichukuList.size() > 0) {
			cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbyichukuList);
		} else {
			cwbs = "'--'";
		}

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未出库明细
		List<CwbDetailView> weichukuViewlist = this.getcwbDetail(weichukulist, cList, showCustomerjSONArray, null, 0);
		// 已出库明细
		List<CwbOrder> yichukulist = this.cwbDAO.getCwbByCwbsPage(1, cwbs, Page.DETAIL_PAGE_NUMBER);
		List<CwbDetailView> yichukuViewlist = this.getcwbDetail(yichukulist, cList, showCustomerjSONArray, null, 0);
		model.addAttribute("weichukulist", weichukuViewlist);
		model.addAttribute("yichukulist", yichukuViewlist);
		model.addAttribute("customerlist", cList);

		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuBuYunXu.getText()));
		model.addAttribute("ckfb_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuFengBao.getText()));
		model.addAttribute("showCustomerSign", showCustomerSign);
		return "pda/kdkexportwarhouse";
	}

	/**
	 * 库对库 扫描 已出库list
	 *
	 * @return
	 */
	@RequestMapping("/getkdkexportyichukulist")
	public @ResponseBody List<CwbDetailView> getkdkexportyichukulist(@RequestParam(value = "nextbranchid", defaultValue = "0") long nextbranchid) {
		// List<Branch> bList =
		// cwbOrderService.getNextPossibleKuFangBranches(getSessionUser());
		List<CwbOrder> cList = this.cwbDAO.getYiChuKubyBranchidList(this.getSessionUser().getBranchid(), nextbranchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), 1);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 已入库明细
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;

	}

	/**
	 * 库对库扫描 未出库 list
	 *
	 * @return
	 */

	@RequestMapping("/getkdkexporweichukutlist")
	public @ResponseBody List<CwbDetailView> getkdkexporweichukutlist() {
		List<Branch> bList = this.cwbOrderService.getNextPossibleKuFangBranches(this.getSessionUser());
		List<CwbOrder> cList = this.cwbDAO.getKDKChukuForCwbOrder(this.getSessionUser().getBranchid(), bList.size() > 0 ? bList.get(0).getBranchid() : 0, -1);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 已入库明细
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;

	}

	/**
	 * 进入加急件出库的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/urgentexportwarhouse")
	public String urgentexportwarhouse(Model model) {
		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());

		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuBuYunXu.getText()));
		return "pda/urgentexportwarhouse";
	}

	/**
	 * 进入出库的功能页面（包）
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/exportwarhouseforbale")
	public String exportwarhouseforbale(Model model) {
		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());

		List<User> uList = this.userDAO.getUserByRole(3);

		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuBuYunXu.getText()));
		return "pda/exportwarhouseforbale";
	}

	/**
	 * 进入小件员领货的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/branchdeliver")
	public String branchdeliver(Model model) {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());

		model.addAttribute("userList", uList);

		return "pda/branchdeliverid";
	}

	@RequestMapping("/getBranchDeliver")
	public @ResponseBody List<User> getBranchDeliver(@RequestParam(value = "branchid", defaultValue = "0") long branchid) {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, branchid);

		return uList;
	}

	/**
	 * 进入小件员领货（详细）的功能页面
	 *
	 * @param model
	 *            //3a
	 * @return
	 */
	@RequestMapping("/branchdeliverdetail")
	public String branchdeliverdetail(Model model, @RequestParam(value = "deliverid", defaultValue = "0") long deliverid, @RequestParam(value = "customerid", defaultValue = "-1") long customerid) {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());

		List<Customer> cList = this.customerDAO.getAllCustomers();
		List<Branch> branchList = this.branchDAO.getAllBranches();
		List<CwbOrder> todayzhiliulist = new ArrayList<CwbOrder>();// 今日滞留单
		List<CwbOrder> todayweilinghuolist = new ArrayList<CwbOrder>();// 今日待领货list
		List<CwbOrder> historyzhiliulist = new ArrayList<CwbOrder>();// 历史待领货list
		List<CwbOrder> historydaohuolist = new ArrayList<CwbOrder>();
		List<CwbDetailView> historyweilinghuolist = new ArrayList<CwbDetailView>();// 历史待领货list
		// 今日待领货
		// List<String> todaydaohuocwbs =
		// this.operationTimeDAO.getOrderFlowLingHuoList(this.getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
		// + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime());

		// OXO: 过滤JIT类型的订单统计 by jinghui.pan on 20150804
		List<String> todaydaohuocwbs = this.operationTimeDAO.getOrderFlowLingHuoList(this.getSessionUser().getBranchid(), this.getCwbOrderTypeIdsWithoutJIT(),
				FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime());

		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime(), "");
		// 今日到货订单
		List<CwbOrder> todaydaohuolist = new ArrayList<CwbOrder>();
		if (todaydaohuocwbs.size() > 0) {
			todaydaohuolist = this.cwbDAO.getTodayWeiLingDaohuobyBranchid(this.getSessionUser().getBranchid(), this.getStrings(todaydaohuocwbs));
		}
		List<String> todayzhiliucwbs = this.operationTimeDAO.getjinrizhiliu(this.getSessionUser().getBranchid(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(),
				DateTimeUtil.getCurrentDayZeroTime());

		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.YiShenHe.getValue()+"",
		// DateTimeUtil.getCurrentDayZeroTime(), "");
		// 历史到货
		// List<String> historycwbs =
		// this.operationTimeDAO.getlishidaohuo(this.getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
		// + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime());
		// OXO: 过滤JIT类型的订单统计 by jinghui.pan on 20150804
		List<String> historycwbs = this.operationTimeDAO.getlishidaohuo(this.getSessionUser().getBranchid(), this.getCwbOrderTypeIdsWithoutJIT(),
				FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime());

		// cwbDAO.getHistoryyWeiLingDaohuobyBranchid(getSessionUser().getBranchid(),getStrings(todaydaohuocwbs));

		if (historycwbs.size() > 0) {

			historydaohuolist = this.cwbDAO.getCwbOrderByFlowordertypeAndCwbs(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), this.getStrings(historycwbs));
		}

		// 今日滞留订单
		if (todayzhiliucwbs.size() > 0) {
			todayzhiliulist = this.cwbDAO.getTodayWeiLingZhiliuByWhereListformingxi(DeliveryStateEnum.FenZhanZhiLiu.getValue(), this.getSessionUser().getBranchid(), this.getStrings(todayzhiliucwbs),
					deliverid);
		}

		// 历史滞留订单
		List<String> historyzhiliucwbs = this.operationTimeDAO.getlishizhiliu(this.getSessionUser().getBranchid(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(),
				DateTimeUtil.getCurrentDayZeroTime());
		// cwbDAO.getHistoryWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(),getSessionUser().getBranchid(),getStrings(todayzhiliucwbs));
		if (historyzhiliucwbs.size() > 0) {
			historyzhiliulist = this.cwbDAO.getHistoryWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(), this.getSessionUser().getBranchid(), this.getStrings(historyzhiliucwbs),
					deliverid);
		}

		// 今日反馈待中转失败订单-20150629新增---------------------------------------------
		List<CwbOrder> historydaizhongzhuanlist = new ArrayList<CwbOrder>();// 历史待领货list
		List<CwbOrder> todaydaizhongzhuanlist = new ArrayList<CwbOrder>();

		List<String> todayAppZhongZhuanCwbs = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanList(DateTimeUtil.getCurrentDayZeroTime(), "", 2, this.getSessionUser().getBranchid());

		if (todayAppZhongZhuanCwbs.size() > 0) {
			todaydaizhongzhuanlist = this.cwbDAO.getTodayWeiLingZhiliuByWhereListformingxi(DeliveryStateEnum.DaiZhongZhuan.getValue(), this.getSessionUser().getBranchid(),
					this.getStrings(todayAppZhongZhuanCwbs), deliverid);
		}
		// 历史待中转订单
		List<String> historyAppZhongZhuanCwbs = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanList("", DateTimeUtil.getCurrentDayZeroTime(), 2, this.getSessionUser().getBranchid());

		if (historyAppZhongZhuanCwbs.size() > 0) {
			historydaizhongzhuanlist = this.cwbDAO.getHistoryWeiLingZhiliuByWhereList(DeliveryStateEnum.DaiZhongZhuan.getValue(), this.getSessionUser().getBranchid(),
					this.getStrings(historyAppZhongZhuanCwbs), deliverid);
		}

		// 今日反馈待中转失败订单----------------------------------------------

		// 今日反馈拒收审核不通过失败订单-20150629新增---------------------------------------------
		List<CwbOrder> historyjushoulist = new ArrayList<CwbOrder>();// 历史待领货list
		List<CwbOrder> todayjushoulist = new ArrayList<CwbOrder>();

		List<String> todayJuShouCwbs = this.orderBackCheckDAO.getOrderBackChecksCwbs(this.getSessionUser().getBranchid(), DateTimeUtil.getCurrentDayZeroTime(), "");
		String deliverystates = DeliveryStateEnum.JuShou.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.ShangMenJuTui.getValue();
		if (todayJuShouCwbs.size() > 0) {
			todayjushoulist = this.cwbDAO.getTodayWeiLingJuShouByWhereListformingxi(deliverystates, this.getSessionUser().getBranchid(), this.getStrings(todayJuShouCwbs), deliverid);
		}
		// 历史拒收订单
		List<String> historyJuShouCwbs = this.orderBackCheckDAO.getOrderBackChecksCwbs(this.getSessionUser().getBranchid(), "", DateTimeUtil.getCurrentDayZeroTime());

		if (historyJuShouCwbs.size() > 0) {
			historyjushoulist = this.cwbDAO.getHistoryWeiLingJuShouByWhereList(deliverystates, this.getSessionUser().getBranchid(), this.getStrings(historyJuShouCwbs), deliverid);
		}

		// 今日反馈拒收审核不通过----------------------------------------------

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;

		// 2.历史未领货==========================
		historydaohuolist.addAll(historyzhiliulist);
		historydaohuolist.addAll(historydaizhongzhuanlist);
		historydaohuolist.addAll(historyjushoulist);
		// 只显示选择小件员匹配到的件
		historydaohuolist = this.cwbOrderService.filterCwbOrderByDeliver(historydaohuolist, deliverid);
		historyweilinghuolist = this.getcwbDetail(historydaohuolist, cList, showCustomerjSONArray, branchList, 2);

		// 1.今日未领货======================================
		todayweilinghuolist.addAll(todayjushoulist);// 今日拒收不通过
		todayweilinghuolist.addAll(todaydaizhongzhuanlist);// 今日待中转
		todayweilinghuolist.addAll(todayzhiliulist);// 今日滞留
		todayweilinghuolist.addAll(todaydaohuolist);// 今日到货
		// 只显示选择小件员匹配到的件
		todayweilinghuolist = this.cwbOrderService.filterCwbOrderByDeliver(todayweilinghuolist, deliverid);
		List<CwbDetailView> todayweilinghuoViewlist = this.getcwbDetail(todayweilinghuolist, cList, showCustomerjSONArray, branchList, 2);

		// 3.已领货明细==========================
		List<String> linghuocwbs = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchid(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		String yilinghuocwbs = "";
		if (linghuocwbs.size() > 0) {
			yilinghuocwbs = this.getStrings(linghuocwbs);
		} else {
			yilinghuocwbs = "'--'";
		}
		
		List<CwbOrder> yilinghuolist = this.cwbDAO.getYiLingHuoListbyBranchidformingxi(yilinghuocwbs, this.getSessionUser().getBranchid(), deliverid, 1);
		
		List<CwbDetailView> todayweilingCwbOrders = new ArrayList<CwbDetailView>();
		for (int i = 0; (i < Page.DETAIL_PAGE_NUMBER) && (i < todayweilinghuoViewlist.size()); i++) {
			todayweilingCwbOrders.add(todayweilinghuoViewlist.get(i));
		}
		List<CwbDetailView> historylistCwbOrders = new ArrayList<CwbDetailView>();
		for (int i = 0; (i < Page.DETAIL_PAGE_NUMBER) && (i < historyweilinghuolist.size()); i++) {
			historylistCwbOrders.add(historyweilinghuolist.get(i));
		}

		List<CwbDetailView> yilinghuoViewlist = this.getcwbDetail(yilinghuolist, cList, showCustomerjSONArray, branchList, 3);
		
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("todayweilinghuoViewlist", todayweilingCwbOrders);
		model.addAttribute("historyweilinghuolist", historylistCwbOrders);
		model.addAttribute("yilinghuolist", yilinghuoViewlist);
		model.addAttribute("customerlist", cList);
		model.addAttribute("userList", uList);
		return "pda/branchdeliverdetail";
	}

	/**
	 * 领货明细 未领货 history list
	 *
	 * @return
	 */
	@RequestMapping("/getbranchideliverweilinghistorylist")
	public @ResponseBody List<CwbDetailView> getbranchideliverweilinghistorylist(@RequestParam(value = "deliverid", defaultValue = "0") long deliverid,
			@RequestParam(value = "page", defaultValue = "1") long page) {
		List<Branch> branchList = this.branchDAO.getAllBranches();
		// 今日到货订单数
		// List<String> todaydaohuocwbs =
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime(), "");
		// List<String> todaydaohuocwbs =
		// operationTimeDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime());
		// 今日滞留订单数
		// List<String> todayzhiliucwbs =
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.YiShenHe.getValue()+"",
		// DateTimeUtil.getCurrentDayZeroTime(), "");
		// List<String> todayzhiliucwbs =
		// operationTimeDAO.getjinrizhiliu(getSessionUser().getBranchid(),
		// DeliveryStateEnum.FenZhanZhiLiu.getValue(),FlowOrderTypeEnum.YiShenHe.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime());

		List<CwbOrder> cList = new ArrayList<CwbOrder>();
		List<CwbOrder> historydaohuolist = new ArrayList<CwbOrder>();
		// 历史到货订单
		// List<CwbOrder> historydaohuolist =
		// cwbDAO.getHistoryyWeiLingDaohuobyBranchid(getSessionUser().getBranchid(),getStrings(todaydaohuocwbs));
		List<String> historycwbs = this.operationTimeDAO.getlishidaohuo(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
				+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime());

		if (historycwbs.size() > 0) {

			historydaohuolist = this.cwbDAO.getCwbOrderByFlowordertypeAndCwbs(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), this.getStrings(historycwbs));
		}

		List<CwbOrder> historyzhiliulist = new ArrayList<CwbOrder>();// 历史待领货list
		// 历史滞留订单
		// List<CwbOrder> historyzhiliulist =
		// cwbDAO.getHistoryWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(),getSessionUser().getBranchid(),getStrings(todayzhiliucwbs));
		List<String> historyzhiliucwbs = this.operationTimeDAO.getlishizhiliu(this.getSessionUser().getBranchid(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(),
				DateTimeUtil.getCurrentDayZeroTime());

		if (historyzhiliucwbs.size() > 0) {
			historyzhiliulist = this.cwbDAO.getCwbOrderByDeliverystateAndCwbs(DeliveryStateEnum.FenZhanZhiLiu.getValue(), this.getStrings(historyzhiliucwbs));
		}

		cList.addAll(historydaohuolist);
		cList.addAll(historyzhiliulist);
		List<CwbOrder> historylistCwbOrders = new ArrayList<CwbOrder>();
		for (int i = 0; (i < Page.DETAIL_PAGE_NUMBER) && (i < cList.size()); i++) {
			historylistCwbOrders.add(cList.get(i));
		}

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 已入库明细
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(historylistCwbOrders, this.customerDAO.getAllCustomers(), showCustomerjSONArray, branchList, 2);
		return weidaohuoViewlist;
	}

	/**
	 * 领货明细 已领货 list
	 *
	 * @return
	 */
	@RequestMapping("/getbranchideliveryilinglist")
	public @ResponseBody List<CwbDetailView> getbranchideliveryilinglist(@RequestParam(value = "deliverid", defaultValue = "0") long deliverid,
			@RequestParam(value = "page", defaultValue = "1") long page) {
		List<Branch> branchList = this.branchDAO.getAllBranches();
		List<String> linghuocwbs = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchid(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		String yilinghuocwbs = "";
		if (linghuocwbs.size() > 0) {
			yilinghuocwbs = this.getStrings(linghuocwbs);
		} else {
			yilinghuocwbs = "'--'";
		}

		List<CwbOrder> cList = this.cwbDAO.getYiLingHuoListbyBranchidformingxi(yilinghuocwbs, this.getSessionUser().getBranchid(), deliverid, page);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");

		// 已入库明细
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, branchList, 3);
		return weidaohuoViewlist;

	}

	/**
	 * 领货明细 今日未领货 list
	 *
	 * @return
	 */
	@RequestMapping("/getbranchideliverweilinglist")
	public @ResponseBody List<CwbDetailView> getbranchideliverweilinglist(@RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "deliverid", defaultValue = "0") long deliverid, @RequestParam(value = "showCustomerSign", defaultValue = "false") Boolean showCustomerSign,
			@RequestParam(value = "clist", defaultValue = "") List<Customer> customerlist) {
		List<Branch> branchList = this.branchDAO.getAllBranches();
		// 今日到货订单数
		// List<String> todaydaohuocwbs =
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime(), "");
		List<String> todaydaohuocwbs = this.operationTimeDAO.getOrderFlowLingHuoList(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
				+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime());

		// 今日滞留订单数
		// List<String> todayzhiliucwbs =
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.YiShenHe.getValue()+"",
		// DateTimeUtil.getCurrentDayZeroTime(), "");
		List<String> todayzhiliucwbs = this.operationTimeDAO.getjinrizhiliu(this.getSessionUser().getBranchid(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(),
				DateTimeUtil.getCurrentDayZeroTime());
		// 今日到货订单
		List<CwbOrder> todaydaohuolist = new ArrayList<CwbOrder>();
		if (todaydaohuocwbs.size() > 0) {
			todaydaohuolist = this.cwbDAO.getTodayWeiLingDaohuobyBranchid(this.getSessionUser().getBranchid(), this.getStrings(todaydaohuocwbs));
		}
		// 今日滞留订单
		List<CwbOrder> todayzhiliulist = new ArrayList<CwbOrder>();
		if (todayzhiliucwbs.size() > 0) {
			todayzhiliulist = this.cwbDAO.getTodayWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(), this.getSessionUser().getBranchid(), this.getStrings(todayzhiliucwbs));
		}
		List<CwbOrder> cList = new ArrayList<CwbOrder>();
		cList.addAll(todaydaohuolist);
		cList.addAll(todayzhiliulist);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		List<CwbOrder> todaylistCwbOrders = new ArrayList<CwbOrder>();
		for (int i = 0; (i < Page.DETAIL_PAGE_NUMBER) && (i < cList.size()); i++) {
			todaylistCwbOrders.add(cList.get(i));
		}
		List<CwbDetailView> todayweilingViewlist = this.getcwbDetail(todaylistCwbOrders, customerlist, showCustomerjSONArray, branchList, 2);
		return todayweilingViewlist;

	}

	/**
	 * 进入小件员批量领货的功能页面
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwbs
	 *            回车分割订单号列表
	 * @param deliverid
	 *            小件员id
	 * @param allnum
	 *            当前页面已领货的总数
	 * @return
	 */
	@RequestMapping("/branchdeliverBatch")
	public String branchdeliverBatch(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid, boolean isChaoqu) {
		/*
		 * String roleids = "2,4"; List<User> uList =
		 * this.userDAO.getUserByRolesAndBranchid(roleids,
		 * this.getSessionUser().getBranchid());
		 */String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}

		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());
		model.addAttribute("userList", uList);// 获取用户列表

		User deliveryUser = this.userDAO.getUserByUserid(deliverid);// 获取小件员
		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表
		List<Branch> branchList = this.branchDAO.getAllBranches();
		long allnum = 0;

		long linghuoSuccessCount = 0;
		String alertErrorMsg = "";
		String alertWarnMsg = "";
		String relatedShangMenTuiCwb = ""; // 配送单关联的上门退订单

		List<JSONObject> objList = new ArrayList<JSONObject>();
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			allnum++;
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				
				//校验是否存在退货出站审核中的记录
				orderBackCheckService.validateCheckStateAuditing(cwb, FlowOrderTypeEnum.FenZhanLingHuo);
				
				//Added by leoliao at 2016-07-28 快递单未补录全则不允许领货
				CwbOrder cwbOrderTmp = cwbOrderService.getCwbByCwb(cwb);
				if(cwbOrderTmp != null && cwbOrderTmp.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue() && cwbOrderTmp.getIsadditionflag() == 0){
					logger.info("快递单(cwb={})未补录全不允许领货", cwb);
					continue;
				}
				//Added end
				
				CwbOrder cwbOrder = this.cwbOrderService.receiveGoodsByDeliver(this.getSessionUser(), deliveryUser, cwb, scancwb, isChaoqu,true);
				//*******Hps_Concerto*****2016年5月26日17:23:11
				try{
					if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype())!=null){
						if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText()=="已审核"){
							obj.put("flowordertypetext","审核为："+DeliveryStateEnum.getByValue(cwbOrder.getDeliverystate()).getText());
						 }else if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText()=="已反馈") {
							 obj.put("flowordertypetext","反馈为："+DeliveryStateEnum.getByValue(cwbOrder.getDeliverystate()).getText()); 
							/*hps*/ }else{
								obj.put("flowordertypetext",CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText());
						 }
					}else{
						obj.put("flowordertypetext","");
					}
				}catch(Exception e){
					obj.put("flowordertypetext","");
				}
				obj.put("cwbstate", cwbOrder.getCwbstate());
				OrderBackCheck oc = orderBackCheckDAO.getOrderBackCheckOnlyCwb(cwbOrder.getCwb());
				if(oc==null){ 
					obj.put("checkstateresultname", "");
				}else{
					obj.put("checkstateresultname", oc.getCheckresult()==0?"未审核":(oc.getCheckresult()==1?"确认退货":"站点配送"));
				}
				//*******************
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				linghuoSuccessCount++;
				
				if (relatedShangMenTuiCwb.isEmpty()) {
					relatedShangMenTuiCwb = cwbOrderService.haveRelatedShangMenTuiCwb(cwbOrder);
				} else {
					relatedShangMenTuiCwb += "," + cwbOrderService.haveRelatedShangMenTuiCwb(cwbOrder);
				}
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				if (ExceptionCwbErrorTypeEnum.LingHuo_ZhiFuXinxiWeiQueRen.equals(ce.getError())) { // 订单存在未确认的支付信息修改申请，终止领货，并且弹窗提示
					// 领货失败
					if ((alertErrorMsg != null) && (alertErrorMsg.length() > 0)) {
						alertErrorMsg += "," + cwb;
					} else {
						alertErrorMsg = cwb;
					}
				}
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
					//*******Hps_Concerto*****2016年5月26日17:23:11
					obj.put("cwbstate", cwbOrder.getCwbstate());
					//如果dc为空，那么增加判断，防止出现异常 ---刘武强20160612
					try{
						if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype())!=null){
							if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText()=="已审核"){
								obj.put("flowordertypetext","审核为："+DeliveryStateEnum.getByValue(cwbOrder.getDeliverystate()).getText());
							 }else if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText()=="已反馈") {
								 obj.put("flowordertypetext","反馈为："+DeliveryStateEnum.getByValue(cwbOrder.getDeliverystate()).getText()); 
								/*hps*/ }else{
									obj.put("flowordertypetext",CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText());
							 }
						}else{
							obj.put("flowordertypetext","");
						}
					}catch(Exception e){
						obj.put("flowordertypetext","");
					}
					OrderBackCheck oc = orderBackCheckDAO.getOrderBackCheckOnlyCwb(cwbOrder.getCwb());
					if(oc==null){ 
						obj.put("checkstateresultname", "");
					}else{
						obj.put("checkstateresultname", oc.getCheckresult()==0?"未审核":(oc.getCheckresult()==1?"确认退货":"站点配送"));
					}
					//*******************
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
				obj.put("cwbOrder", cwbOrder);
				obj.put("errorcode", ce.getError().getValue());
				obj.put("createtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				obj.put("errorinfo", ce.getMessage());
				if (cwbOrder == null) {// 如果无此订单
					obj.put("customername", "");
					obj.put("inSitetime", "");
				} else {
					OrderFlow of = this.orderFlowDAO.getOrderFlowByCwbAndFlowtype(cwbOrder.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
							+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
					for (Customer c : cList) {
						if (c.getCustomerid() == cwbOrder.getCustomerid()) {
							obj.put("customername", c.getCustomername());
							break;
						}
					}
					obj.put("inSitetime", of != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()) : "");// 出库时间
				}
			}
			objList.add(obj);
		}

		if ((alertErrorMsg != null) && (alertErrorMsg.length() > 0)) {
			alertErrorMsg += "有修改未审核，请联系客服人员！";
			alertWarnMsg = "";
		} else {
			StringBuilder editCwbs = new StringBuilder();
			if ((cwbs != null) && !cwbs.isEmpty()) {
				String searchCwbs = this.getCwbs(cwbs);
				Map<String, List<String>> editCwbMap = this.cwbOrderService.getEditCwb(searchCwbs);
				if ((editCwbMap != null) && !editCwbMap.isEmpty()) {
					List<String> editCwbInfoList = editCwbMap.get("cwbInfo");
					List<String> editCwbPayList = editCwbMap.get("cwbPay");

					if ((editCwbPayList != null) && !editCwbPayList.isEmpty()) {
						for (String editCwb : editCwbPayList) {
							if (editCwbs.indexOf(editCwb) == -1) {
								if (editCwbs.length() > 0) {
									editCwbs.append("," + editCwb);
								} else {
									editCwbs.append(editCwb);
								}
							}
						}
					}

					if ((editCwbInfoList != null) && !editCwbInfoList.isEmpty()) {
						for (String editCwb : editCwbInfoList) {
							if (editCwbs.indexOf(editCwb) == -1) {
								if (editCwbs.length() > 0) {
									editCwbs.append("," + editCwb);
								} else {
									editCwbs.append(editCwb);
								}
							}
						}
					}
				}
			}

			if (editCwbs.length() > 0) {
				alertWarnMsg = editCwbs.toString() + "有修改，请及时核对！";
			}
		}
		if (alertErrorMsg.isEmpty() && alertWarnMsg.isEmpty()
				&& !relatedShangMenTuiCwb.isEmpty()) {
			alertWarnMsg = relatedShangMenTuiCwb + "有关联揽退单！";
		}
		
		model.addAttribute("objList", objList);
		model.addAttribute("customerlist", cList);

		List<CwbOrder> todayweilinghuolist = new ArrayList<CwbOrder>();// 今日待领货list
		List<CwbOrder> historyweilinghuolist = new ArrayList<CwbOrder>();// 历史待领货list
		List<CwbOrder> historydaohuolist = new ArrayList<CwbOrder>();

		// 今日到货订单数
		// List<String> todaydaohuocwbs =
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime(), "");
		List<String> todaydaohuocwbs = this.operationTimeDAO.getOrderFlowLingHuoList(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
				+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime());
		// 今日滞留订单数
		// List<String> todayzhiliucwbs =
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.YiShenHe.getValue()+"",
		// DateTimeUtil.getCurrentDayZeroTime(), "");
		List<String> todayzhiliucwbs = this.operationTimeDAO.getjinrizhiliu(this.getSessionUser().getBranchid(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(),
				DateTimeUtil.getCurrentDayZeroTime());
		// 今日到货订单
		List<CwbOrder> todaydaohuolist = new ArrayList<CwbOrder>();
		if (todaydaohuocwbs.size() > 0) {
			todaydaohuolist = this.cwbDAO.getTodayWeiLingDaohuobyBranchid(this.getSessionUser().getBranchid(), this.getStrings(todaydaohuocwbs));
		}
		// 历史到货订单
		// List<CwbOrder> historydaohuolist =
		// cwbDAO.getHistoryyWeiLingDaohuobyBranchid(getSessionUser().getBranchid(),getStrings(todaydaohuocwbs));
		List<String> historycwbs = this.operationTimeDAO.getlishidaohuo(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
				+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime());

		if (historycwbs.size() > 0) {
			historydaohuolist = this.cwbDAO.getCwbOrderByFlowordertypeAndCwbs(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), this.getStrings(historycwbs));
		}

		// 今日滞留订单
		List<CwbOrder> todayzhiliulist = new ArrayList<CwbOrder>();
		if (todayzhiliucwbs.size() > 0) {
			todayzhiliulist = this.cwbDAO.getTodayWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(), this.getSessionUser().getBranchid(), this.getStrings(todayzhiliucwbs));
		}
		List<CwbOrder> historyzhiliulist = new ArrayList<CwbOrder>();// 历史滞留list
		// 历史滞留订单
		// List<CwbOrder> historyzhiliulist =
		// cwbDAO.getHistoryWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(),getSessionUser().getBranchid(),getStrings(todayzhiliucwbs));
		List<String> historyzhiliucwbs = this.operationTimeDAO.getlishizhiliu(this.getSessionUser().getBranchid(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(),
				DateTimeUtil.getCurrentDayZeroTime());

		if (historyzhiliucwbs.size() > 0) {
			historyzhiliulist = this.cwbDAO.getCwbOrderByDeliverystateAndCwbs(DeliveryStateEnum.FenZhanZhiLiu.getValue(), this.getStrings(historyzhiliucwbs));
		}

		todayweilinghuolist.addAll(todaydaohuolist);
		todayweilinghuolist.addAll(todayzhiliulist);

		historyweilinghuolist.addAll(historydaohuolist);
		historyweilinghuolist.addAll(historyzhiliulist);

		List<String> linghuocwbs = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchid(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		String yilinghuocwbs = "";
		if (linghuocwbs.size() > 0) {
			yilinghuocwbs = this.getStrings(linghuocwbs);
		} else {
			yilinghuocwbs = "'--'";
		}
		// ======================
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未领货
		todayweilinghuolist = this.cwbOrderService.filterCwbOrderByDeliver(todayweilinghuolist, deliverid);
		List<CwbDetailView> todayweilingViewlist = this.getcwbDetail(todayweilinghuolist, cList, showCustomerjSONArray, branchList, 2);

		// 已领货
		List<CwbDetailView> yilinghuoViewList = this.getcwbDetail(this.cwbDAO.getYiLingHuoListbyBranchidformingxi(yilinghuocwbs, this.getSessionUser().getBranchid(), deliverid, 1), cList,
				showCustomerjSONArray, branchList, 3);

		// 历史未领货
		historyweilinghuolist = this.cwbOrderService.filterCwbOrderByDeliver(historyweilinghuolist, deliverid);
		List<CwbDetailView> historyweilinghuoViewList = this.getcwbDetail(historyweilinghuolist, cList, showCustomerjSONArray, branchList, 2);

		List<CwbDetailView> todayweilingCwbOrders = new ArrayList<CwbDetailView>();
		for (int i = 0; (i < Page.DETAIL_PAGE_NUMBER) && (i < todayweilingViewlist.size()); i++) {
			todayweilingCwbOrders.add(todayweilingViewlist.get(i));
		}
		List<CwbDetailView> historylistCwbOrders = new ArrayList<CwbDetailView>();
		for (int i = 0; (i < Page.DETAIL_PAGE_NUMBER) && (i < historyweilinghuoViewList.size()); i++) {
			historylistCwbOrders.add(historyweilinghuoViewList.get(i));
		}
		model.addAttribute("todayweilinghuolist", todayweilingCwbOrders);
		model.addAttribute("historyweilinghuolist", historylistCwbOrders);
		model.addAttribute("todayweilinghuocount", todayweilinghuolist.size());
		model.addAttribute("historyweilinghuocount", historyweilinghuolist.size());
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("yilinghuo", this.cwbDAO.getYiLingHuoCountbyBranchid(yilinghuocwbs, this.getSessionUser().getBranchid(), deliverid));
		model.addAttribute("yilinghuolist", yilinghuoViewList);
		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + linghuoSuccessCount + "单，异常" + (allnum - linghuoSuccessCount) + "单";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("alertErrorMsg", alertErrorMsg);
		model.addAttribute("alertWarnMsg", alertWarnMsg);

		return "pda/branchdeliverBatch";
	}

	@RequestMapping("/getBatchLinghuoSum")
	public @ResponseBody JSONObject getBatchLinghuoSum(@RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid) {
		JSONObject obj = new JSONObject();
		String todaytime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		obj.put("linghuoSuccessCount",
				this.orderFlowDAO.getOrderFlowByCredateAndFlowordertypeCount(this.getSessionUser().getBranchid(), todaytime, "", FlowOrderTypeEnum.FenZhanLingHuo.getValue(), deliverid));// 本次扫描成功总数

		return obj;
	}

	/**
	 * 进入小件员批量反馈的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/deliverpod")
	public String deliverpod(Model model) {
		// 退货原因
		List<Reason> returnlist = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());
		// 滞留原因
		List<Reason> staylist = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.BeHelpUp.getValue());
		// 当前站小件员列表
		String roleids = "2,4";
		List<User> userlist = this.userDAO.getUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());

		model.addAttribute("pl_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.PiLiangFanKuiPOS.getText()));
		model.addAttribute("userlist", userlist);
		model.addAttribute("returnlist", returnlist);
		model.addAttribute("staylist", staylist);
		return "pda/deliverpod";
	}

	/**
	 * 进入退货站入库的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/backimport")
	public String backimport(Model model) {
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Customer> cList = this.customerDAO.getAllCustomers();
		List<Reason> backreasonList = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.TuiHuoZhanRuKuBeiZhu.getValue());

		List<CwbOrder> yichuzhanlist = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), 1, String.valueOf(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()));
		List<CwbOrder> ypeisong = new ArrayList<CwbOrder>();
		List<CwbOrder> yshangmenhuan = new ArrayList<CwbOrder>();
		List<CwbOrder> yshangmentui = new ArrayList<CwbOrder>();
		ypeisong = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Peisong.getValue());
		yshangmenhuan = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Shangmenhuan.getValue());
		yshangmentui = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Shangmentui.getValue());

		// 包含订单拦截后的 待退货入库===LX
		List<CwbOrder> cwbAllList = this.cwbDAO.getBackRukuByBranchidForListAll(this.getSessionUser().getBranchid(), 1);

		// ========新增（结束）===========

		List<CwbOrder> wpeisong = new ArrayList<CwbOrder>();
		List<CwbOrder> wshangmenhuan = new ArrayList<CwbOrder>();
		List<CwbOrder> wshangmentui = new ArrayList<CwbOrder>();

		// 增加订单拦截后的退货未入库统计===LX
		wpeisong = this.cwbDAO.getBackRukuByBranchidForListAll(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Peisong.getValue());
		wshangmenhuan = this.cwbDAO.getBackRukuByBranchidForList(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Shangmenhuan.getValue());
		wshangmentui = this.cwbDAO.getBackRukuByBranchidForList(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Shangmentui.getValue());

		/*
		 * //订单拦截配送单类型统计（退货未入库）====LX List<CwbOrder> cwbInterceptListPS =
		 * this.cwbDAO
		 * .getBackRukuByInterceptList(this.getSessionUser().getBranchid
		 * (),cwbstatestrs,CwbOrderTypeIdEnum.Peisong.getValue(),1);
		 * if((cwbInterceptListPS!=null)&&(!cwbInterceptListPS.isEmpty())){
		 * wpeisong.addAll(cwbInterceptListPS); }
		 */
		/*
		 * for (CwbOrder cwb : cwbAllList) { if (cwb.getCwbordertypeid() ==
		 * CwbOrderTypeIdEnum.Peisong.getValue()) { wpeisong.add(cwb); } else if
		 * (cwb.getCwbordertypeid() ==
		 * CwbOrderTypeIdEnum.Shangmenhuan.getValue()) { wshangmenhuan.add(cwb);
		 * } else if (cwb.getCwbordertypeid() ==
		 * CwbOrderTypeIdEnum.Shangmentui.getValue()) { wshangmentui.add(cwb); }
		 * }
		 */
		model.addAttribute("ypeisong", ypeisong);
		model.addAttribute("yshangmenhuan", yshangmenhuan);
		model.addAttribute("yshangmentui", yshangmentui);

		model.addAttribute("wpeisong", wpeisong);
		model.addAttribute("wshangmenhuan", wshangmenhuan);
		model.addAttribute("wshangmentui", wshangmentui);

		model.addAttribute("weituihuorukuList", cwbAllList);
		model.addAttribute("yituihuorukuList", yichuzhanlist);
		model.addAttribute("customerlist", cList);
		model.addAttribute("userList", uList);

		model.addAttribute("backreasonList", backreasonList);
		return "pda/backimport";
	}

	/**
	 * 进入退货站、中转站入库的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/backandchangeimport")
	public String backandchangeimport(Model model) {
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Customer> cList = this.customerDAO.getAllCustomers();
		// TODO 退货中转入库备注
		List<Reason> backreasonList = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.TuiHuoZhanRuKuBeiZhu.getValue());

		List<Branch> tbranchlist = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.TuiHuo.getValue());
		List<Branch> zbranchlist = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhongZhuan.getValue());
		Map allcwbstr = new HashMap();

		Map tAllcwbstr = this.getCwbsbyBranch(tbranchlist, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), 0);
		Map zAllcwbstr = this.getCwbsbyBranch(zbranchlist, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), 0);

		Map tPScwbstr = this.getCwbsbyBranch(tbranchlist, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), CwbOrderTypeIdEnum.Peisong.getValue());
		Map tSMTcwbstr = this.getCwbsbyBranch(tbranchlist, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), CwbOrderTypeIdEnum.Shangmentui.getValue());
		Map tSMHcwbstr = this.getCwbsbyBranch(tbranchlist, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), CwbOrderTypeIdEnum.Shangmenhuan.getValue());
		Map zPScwbstr = this.getCwbsbyBranch(zbranchlist, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), CwbOrderTypeIdEnum.Peisong.getValue());
		Map zSMTcwbstr = this.getCwbsbyBranch(zbranchlist, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), CwbOrderTypeIdEnum.Shangmentui.getValue());
		Map zSMHcwbstr = this.getCwbsbyBranch(zbranchlist, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), CwbOrderTypeIdEnum.Shangmenhuan.getValue());

		Map twAllcwbstr = this.getCwbsbyBranch(tbranchlist, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), 0);
		Map zwAllcwbstr = this.getCwbsbyBranch(zbranchlist, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), 0);

		Map twPScwbstr = this.getCwbsbyBranch(tbranchlist, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), CwbOrderTypeIdEnum.Peisong.getValue());
		Map twSMTcwbstr = this.getCwbsbyBranch(tbranchlist, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), CwbOrderTypeIdEnum.Shangmentui.getValue());
		Map twSMHcwbstr = this.getCwbsbyBranch(tbranchlist, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), CwbOrderTypeIdEnum.Shangmenhuan.getValue());
		Map zwPScwbstr = this.getCwbsbyBranch(zbranchlist, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), CwbOrderTypeIdEnum.Peisong.getValue());
		Map zwSMTcwbstr = this.getCwbsbyBranch(zbranchlist, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), CwbOrderTypeIdEnum.Shangmentui.getValue());
		Map zwSMHcwbstr = this.getCwbsbyBranch(zbranchlist, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), CwbOrderTypeIdEnum.Shangmenhuan.getValue());

		allcwbstr.putAll(tAllcwbstr);
		allcwbstr.putAll(zAllcwbstr);
		allcwbstr.putAll(tPScwbstr);
		allcwbstr.putAll(tSMTcwbstr);
		allcwbstr.putAll(tSMHcwbstr);
		allcwbstr.putAll(zPScwbstr);
		allcwbstr.putAll(zSMTcwbstr);
		allcwbstr.putAll(zSMHcwbstr);

		allcwbstr.putAll(twAllcwbstr);
		allcwbstr.putAll(zwAllcwbstr);
		allcwbstr.putAll(twPScwbstr);
		allcwbstr.putAll(twSMTcwbstr);
		allcwbstr.putAll(twSMHcwbstr);
		allcwbstr.putAll(zwPScwbstr);
		allcwbstr.putAll(zwSMTcwbstr);
		allcwbstr.putAll(zwSMHcwbstr);

		String allcwbs = this.getStringByMap(allcwbstr);

		List<CwbOrder> allList = new ArrayList<CwbOrder>();
		if (allcwbs.length() > 0) {

			allList = this.cwbDAO.getListbyCwbs(allcwbs);
		}

		List<CwbOrder> tyirukulist = new ArrayList<CwbOrder>();
		List<CwbOrder> tweirukulist = new ArrayList<CwbOrder>();
		List<CwbOrder> typeisong = new ArrayList<CwbOrder>();
		List<CwbOrder> tyshangmenhuan = new ArrayList<CwbOrder>();
		List<CwbOrder> tyshangmentui = new ArrayList<CwbOrder>();
		List<CwbOrder> twpeisong = new ArrayList<CwbOrder>();
		List<CwbOrder> twshangmenhuan = new ArrayList<CwbOrder>();
		List<CwbOrder> twshangmentui = new ArrayList<CwbOrder>();

		List<CwbOrder> zyirukulist = new ArrayList<CwbOrder>();
		List<CwbOrder> zweirukulist = new ArrayList<CwbOrder>();
		List<CwbOrder> zypeisong = new ArrayList<CwbOrder>();
		List<CwbOrder> zyshangmenhuan = new ArrayList<CwbOrder>();
		List<CwbOrder> zyshangmentui = new ArrayList<CwbOrder>();
		List<CwbOrder> zwpeisong = new ArrayList<CwbOrder>();
		List<CwbOrder> zwshangmenhuan = new ArrayList<CwbOrder>();
		List<CwbOrder> zwshangmentui = new ArrayList<CwbOrder>();

		if ((allList != null) && (allList.size() > 0)) {
			for (CwbOrder co : allList) {
				if (tAllcwbstr.get(co.getCwb()) != null) {
					tyirukulist.add(co);
				}
				if (twAllcwbstr.get(co.getCwb()) != null) {
					tweirukulist.add(co);
				}
				if (tPScwbstr.get(co.getCwb()) != null) {
					typeisong.add(co);
				}
				if (tSMHcwbstr.get(co.getCwb()) != null) {
					tyshangmenhuan.add(co);
				}
				if (tSMTcwbstr.get(co.getCwb()) != null) {
					tyshangmentui.add(co);
				}
				if (twPScwbstr.get(co.getCwb()) != null) {
					twpeisong.add(co);
				}
				if (twSMHcwbstr.get(co.getCwb()) != null) {
					twshangmenhuan.add(co);
				}
				if (twSMTcwbstr.get(co.getCwb()) != null) {
					twshangmentui.add(co);
				}
				if (zAllcwbstr.get(co.getCwb()) != null) {
					zyirukulist.add(co);
				}
				if (zwAllcwbstr.get(co.getCwb()) != null) {
					zweirukulist.add(co);
				}
				if (zPScwbstr.get(co.getCwb()) != null) {
					zypeisong.add(co);
				}
				if (zSMHcwbstr.get(co.getCwb()) != null) {
					zyshangmenhuan.add(co);
				}
				if (zSMTcwbstr.get(co.getCwb()) != null) {
					zyshangmentui.add(co);
				}
				if (zwPScwbstr.get(co.getCwb()) != null) {
					zwpeisong.add(co);
				}
				if (zwSMHcwbstr.get(co.getCwb()) != null) {
					zwshangmenhuan.add(co);
				}
				if (zwSMTcwbstr.get(co.getCwb()) != null) {
					zwshangmentui.add(co);
				}
			}
		}

		model.addAttribute("weituihuorukuList", tweirukulist);
		model.addAttribute("yituihuorukuList", tyirukulist);
		model.addAttribute("ypeisong", typeisong);
		model.addAttribute("yshangmenhuan", tyshangmenhuan);
		model.addAttribute("yshangmentui", tyshangmentui);
		model.addAttribute("wpeisong", twpeisong);
		model.addAttribute("wshangmenhuan", twshangmenhuan);
		model.addAttribute("wshangmentui", twshangmentui);

		model.addAttribute("zweituihuorukuList", zweirukulist);
		model.addAttribute("zyituihuorukuList", zyirukulist);
		model.addAttribute("zypeisong", zypeisong);
		model.addAttribute("zyshangmenhuan", zyshangmenhuan);
		model.addAttribute("zyshangmentui", zyshangmentui);
		model.addAttribute("zwpeisong", zwpeisong);
		model.addAttribute("zwshangmenhuan", zwshangmenhuan);
		model.addAttribute("zwshangmentui", zwshangmentui);

		model.addAttribute("customerlist", cList);
		model.addAttribute("userList", uList);
		model.addAttribute("backreasonList", backreasonList);
		return "pda/backandchangeimport";
	}

	/**
	 * 按站点list，flowtype、订单类型 查询超期异常监控表，返回订单list
	 *
	 * @param branchlist
	 * @param flowordertype
	 * @param cwbordertypeid
	 * @return
	 */
	private Map<String, String> getCwbsbyBranch(List<Branch> branchlist, int flowordertype, int cwbordertypeid) {
		Map<String, String> map = new HashMap<String, String>();
		String branchids = "-1";
		if ((branchlist != null) && (branchlist.size() > 0)) {
			for (Branch branch : branchlist) {
				branchids += "," + branch.getBranchid();
			}
		}
		List<String> tcwbs = new ArrayList<String>();
		if ((flowordertype == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) || (flowordertype == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue())) {
			tcwbs = this.operationTimeDAO.getBackandChangeYiRukuListbyBranchid(branchids, flowordertype, cwbordertypeid, 1);
		} else {
			tcwbs = this.operationTimeDAO.getBackandChangeWeiRukuListbyBranchid(branchids, flowordertype, cwbordertypeid, 1);
		}
		for (String tcwb : tcwbs) {
			map.put(tcwb, tcwb);
		}
		return map;
	}

	/**
	 *
	 * @param map
	 * @return
	 */
	private String getStringByMap(Map<String, String> map) {

		String cwbs = "";
		Set<String> key = map.keySet();
		for (Iterator it = key.iterator(); it.hasNext();) {
			String s = (String) it.next();
			cwbs += "'" + s + "',";
		}
		return cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";

	}

	private String getCwbs(String cwbs) {
		StringBuffer strs = new StringBuffer("");
		if (!cwbs.equals("")) {
			for (String str : cwbs.split("\r\n")) {
				str = str.trim();
				strs.append("'").append(str).append("',");
			}
		}
		String cwbss = "";
		if (strs.length() > 0) {
			cwbss = strs.substring(0, strs.length() - 1);
		}
		return cwbss;
	}

	/**
	 * 退货站入库 得到未入库 list
	 *
	 */

	@RequestMapping("/getbackimportweirukulist")
	public @ResponseBody List<CwbDetailView> getbackimportweiruku(@RequestParam(value = "page", defaultValue = "1") long page) {
		List<CwbOrder> cList = this.cwbDAO.getBackRukuByBranchidForList(this.getSessionUser().getBranchid(), page);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;
	}

	/**
	 * 退货站入库 得到已入库信息
	 *
	 * @return
	 */
	@RequestMapping("/getbackimportyirukulist")
	public @ResponseBody List<CwbDetailView> getbackimportyiruku(@RequestParam(value = "page") long page) {

		List<CwbOrder> cList = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), page, String.valueOf(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()));
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;
	}

	/**
	 * 临时功能，将“退货站入库”功能中“待入库”数据插入到退货记录表ops_tuihuorecord，避免退货出站统计和退货站入库统计查不到这些记录
	 *
	 * @param model
	 * @param response
	 * @param request
	 * @param cwbs
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping("/tuihuochuzhanCretuihuorecord")
	public String tuihuochuzhanCretuihuorecord(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs) {
		try {
			long count = 0;
			if (cwbs.length() > 0) {
				for (String cwb : cwbs.split("\r\n")) {
					if (cwb.trim().length() == 0) {
						continue;
					}
					cwb = cwb.trim();
					OrderFlow of = this.orderFlowDAO.getOrderFlowByParam(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), cwb);
					if (of != null) {
						count++;
						CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.om.readValue(of.getFloworderdetail(), CwbOrderWithDeliveryState.class);
						CwbOrder co = cwbOrderWithDeliveryState.getCwbOrder();

						TuihuoRecord tuihuoRecord = new TuihuoRecord();
						tuihuoRecord.setCwb(of.getCwb());
						tuihuoRecord.setBranchid(co.getStartbranchid());
						tuihuoRecord.setTuihuobranchid(co.getNextbranchid());
						tuihuoRecord.setTuihuochuzhantime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
						tuihuoRecord.setCustomerid(co.getCustomerid());
						tuihuoRecord.setCwbordertypeid(co.getCwbordertypeid());
						tuihuoRecord.setUserid(of.getUserid());

						this.tuihuoRecordDAO.creTuihuoRecord(tuihuoRecord);
					}
				}
				model.addAttribute("msg", "成功" + count + "单");
			}
		} catch (Exception e) {
			this.logger.info("临时功能，将“退货站入库”功能中“待入库”数据插入到退货记录表ops_tuihuorecord，避免退货出站统计和退货站入库统计查不到这些记录报错：" + e.getMessage());
		}

		return "pda/cretuihuorecord";
	}

	/**
	 * 进入退货站再投的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/backexport")
	public String backexport(Model model) {
		List<Branch> bList = new ArrayList<Branch>();
		for (long i : this.cwbRouteService.getNextPossibleBackBranch(this.getSessionUser().getBranchid())) {
			bList.add(this.branchDAO.getBranchByBranchid(i));
		}
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		return "pda/backexport";
	}

	/**
	 * 进入退供货商出库的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/backtocustomer")
	public String backtocustomer(Model model) {
		List<Customer> cList = this.customerDAO.getAllCustomers();
		List<Customer> customersList = this.customerDAO.getAllCustomers();
		List<CwbOrder> yitghsckList = this.cwbDAO.getTuiGongHuoShangYiChuKu(this.getSessionUser().getBranchid(), 1);
		List<CwbOrder> ypeisong = new ArrayList<CwbOrder>();
		List<CwbOrder> yshangmenhuan = new ArrayList<CwbOrder>();
		List<CwbOrder> yshangmentui = new ArrayList<CwbOrder>();

		ypeisong = this.cwbDAO.getTuiGongHuoShangYiChuKu(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Peisong.getValue());
		yshangmenhuan = this.cwbDAO.getTuiGongHuoShangYiChuKu(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Shangmenhuan.getValue());
		yshangmentui = this.cwbDAO.getTuiGongHuoShangYiChuKu(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Shangmentui.getValue());
		/*
		 * for (CwbOrder cwb : yitghsckList) { if (cwb.getCwbordertypeid() ==
		 * CwbOrderTypeIdEnum.Peisong.getValue()) { ypeisong.add(cwb); } else if
		 * (cwb.getCwbordertypeid() ==
		 * CwbOrderTypeIdEnum.Shangmenhuan.getValue()) { yshangmenhuan.add(cwb);
		 * } else if (cwb.getCwbordertypeid() ==
		 * CwbOrderTypeIdEnum.Shangmentui.getValue()) { yshangmentui.add(cwb); }
		 * }
		 */
		// 最新注释
		/*
		 * List<CwbOrder> weitghsckList =
		 * this.cwbDAO.getTGYSCKListbyBranchid(this
		 * .getSessionUser().getBranchid(), 1); List<CwbOrder> wpeisong = new
		 * ArrayList<CwbOrder>(); List<CwbOrder> wshangmenhuan = new
		 * ArrayList<CwbOrder>(); List<CwbOrder> wshangmentui = new
		 * ArrayList<CwbOrder>(); wpeisong =
		 * this.cwbDAO.getTGYSCKListbyBranchid(
		 * this.getSessionUser().getBranchid(), 1,
		 * CwbOrderTypeIdEnum.Peisong.getValue()); wshangmenhuan =
		 * this.cwbDAO.getTGYSCKListbyBranchid
		 * (this.getSessionUser().getBranchid(), 1,
		 * CwbOrderTypeIdEnum.Shangmenhuan.getValue()); wshangmentui =
		 * this.cwbDAO
		 * .getTGYSCKListbyBranchid(this.getSessionUser().getBranchid(), 1,
		 * CwbOrderTypeIdEnum.Shangmentui.getValue());
		 */
		// 与退客户入库 已出库保持
		String flowordertypes = FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "," + FlowOrderTypeEnum.DingDanLanJie.getValue();
		List<CwbOrder> weitghsckList = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), 1, flowordertypes);
		List<CwbOrder> wpeisong = new ArrayList<CwbOrder>();
		List<CwbOrder> wshangmenhuan = new ArrayList<CwbOrder>();
		List<CwbOrder> wshangmentui = new ArrayList<CwbOrder>();
		wpeisong = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Peisong.getValue());
		wshangmenhuan = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Shangmenhuan.getValue());
		wshangmentui = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), 1, CwbOrderTypeIdEnum.Shangmentui.getValue());

		/*
		 * for (CwbOrder cwb : weitghsckList) { if (cwb.getCwbordertypeid() ==
		 * CwbOrderTypeIdEnum.Peisong.getValue()) { wpeisong.add(cwb); } else if
		 * (cwb.getCwbordertypeid() ==
		 * CwbOrderTypeIdEnum.Shangmenhuan.getValue()) { wshangmenhuan.add(cwb);
		 * } else if (cwb.getCwbordertypeid() ==
		 * CwbOrderTypeIdEnum.Shangmentui.getValue()) { wshangmentui.add(cwb); }
		 * }
		 */
		model.addAttribute("weitghsckList", weitghsckList);
		model.addAttribute("yitghsckList", yitghsckList);
		model.addAttribute("customerlist", cList);

		model.addAttribute("ypeisong", ypeisong);
		model.addAttribute("yshangmenhuan", yshangmenhuan);
		model.addAttribute("yshangmentui", yshangmentui);

		model.addAttribute("wpeisong", wpeisong);
		model.addAttribute("wshangmenhuan", wshangmenhuan);
		model.addAttribute("wshangmentui", wshangmentui);

		model.addAttribute("customersList", customersList);

		return "pda/backtocustomer";
	}

	/**
	 * 得到退供货商 待出库list
	 *
	 * @return
	 */

	@RequestMapping("/getbacktocustomerdaichukulist")
	public @ResponseBody List<CwbDetailView> getbacktocustomerdaichukulist(@RequestParam(value = "page", required = true, defaultValue = "1") long page) {
		List<CwbOrder> cList = this.cwbDAO.getTGYSCKListbyBranchid(this.getSessionUser().getBranchid(), page);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;

	}

	/**
	 * 得到退供货商已出库 list
	 *
	 * @return
	 */
	@RequestMapping("/getbacktocustomeryichukulist")
	public @ResponseBody List<CwbDetailView> getbacktocustomeryichukulist(@RequestParam(value = "page", required = true, defaultValue = "1") long page) {
		List<CwbOrder> cList = this.cwbDAO.getTuiGongHuoShangYiChuKu(this.getSessionUser().getBranchid(), page);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;
	}

	/**
	 * 进入供货商拒收返库的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/customerrefuseback")
	public String customerrefuseback(Model model) {
		List<Customer> cList = this.customerDAO.getAllCustomers();

		model.addAttribute("yichukulist", this.cwbDAO.getCustomerRefusedListForScan(this.getSessionUser().getBranchid()));
		model.addAttribute("weitghsckList", this.cwbDAO.getCustomerRefusedListByBranchid(this.getSessionUser().getBranchid()));
		model.addAttribute("customerlist", cList);
		return "pda/customerrefuseback";
	}

	/**
	 * 退供货商拒收返库 已返库list
	 *
	 * @return
	 */
	@RequestMapping("/getcustomerrefusedbackyifankulist")
	public @ResponseBody List<CwbDetailView> getcustomerrefusedbackyifankulist() {
		List<CwbOrder> cList = this.cwbDAO.getCustomerRefusedListForScan(this.getSessionUser().getBranchid());
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;

	}

	/**
	 * 退供货商拒收返库 待返库list
	 *
	 * @return
	 */
	@RequestMapping("/getcustomerrefusedbackdaifankulist")
	public @ResponseBody List<CwbDetailView> getcustomerrefusedbackdaifankulist() {
		List<CwbOrder> cList = this.cwbDAO.getCustomerRefusedListByBranchid(this.getSessionUser().getBranchid());
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(cList, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;
	}

	@RequestMapping("/cwbintoWarehousForGetGoodsBatch")
	public String cwbintoWarehousForGetGoodsBatch(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid) {
		List<Customer> customerlist = this.customerDAO.getAllCustomers();

		List<JSONObject> objList = new ArrayList<JSONObject>();
		long succesCount = 0l;
		long errorCount = 0l;
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			CwbOrder cwbOrder = new CwbOrder();
			obj.put("cwb", cwb);
			try {// 成功订单
				cwbOrder = this.cwbOrderService.intoWarehousForGetGoods(this.getSessionUser(), cwb, 0, customerid);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				succesCount++;
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				errorCount++;
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
				obj.put("cwbOrder", cwbOrder);
				obj.put("errorcode", ce.getError().getValue());
				obj.put("errorinfo", ce.getMessage());
			}
			objList.add(obj);
		}
		model.addAttribute("objList", objList);

		List<User> uList = this.userDAO.getUserByRole(3);
		List<CwbOrder> weiTiHuolist = this.cwbDAO.getDaoRuByBranchidForList(this.getSessionUser().getBranchid());
		List<CwbOrder> yiTiHuolist = this.cwbDAO.getYiTiByBranchidForList(this.getSessionUser().getBranchid());
		long weiTiHuoCount = this.cwbDAO.getDaoRubyBranchid(this.getSessionUser().getBranchid(), customerid).getOpscwbid();
		long yiTiHuoCount = this.cwbDAO.getTiHuobyBranchid(this.getSessionUser().getBranchid(), customerid).getOpscwbid();
		model.addAttribute("customerlist", customerlist);
		model.addAttribute("userList", uList);
		model.addAttribute("weiTiHuolist", weiTiHuolist);
		model.addAttribute("yiTiHuolist", yiTiHuolist);
		model.addAttribute("weiTiHuoCount", weiTiHuoCount);
		model.addAttribute("yiTiHuoCount", yiTiHuoCount);
		model.addAttribute("SuccessCount", succesCount);// 本次扫描提货成功总数
		model.addAttribute("ErrorCount", errorCount);// 本次扫描提货失败总数

		return "pda/intoWarehousForGetGoodsBatch";
	}

	// private boolean isInExpressPackage(CwbOrder co, String cwb) {
	// if ((co != null) && (co.getCwbordertypeid() ==
	// CwbOrderTypeIdEnum.Express.getValue())) {
	// List<BaleCwb> baleCwbList = this.baleCwbDao.getBaleCwbByCwb(cwb);
	// if ((baleCwbList != null) && !baleCwbList.isEmpty()) {
	// for (BaleCwb baleCwb : baleCwbList) {
	// if (this.isPackageAvailable(baleCwb.getBaleno())) {
	// return true;
	// }
	// }
	// }
	// }
	// return false;
	// }

	// private boolean isPackageAvailable(String packageNo) {
	// List<Bale> baleList = this.baleDAO.getBaleByBaleno(packageNo);
	// if ((baleList == null) || baleList.isEmpty()) {
	// return false;
	// }
	// if ((baleList.size() == 1) && (baleList.get(0).getBalestate() !=
	// BaleStateEnum.BuKeYong.getValue())) {
	// return true;
	// }
	// return false;
	// }

	/**
	 * 入库扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param customerid
	 * @param driverid
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/cwbintowarhouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbintowarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid,
			@RequestParam(value = "requestbatchno", required = true, defaultValue = "0") long requestbatchno, @RequestParam(value = "comment", required = true, defaultValue = "") String comment,
			@RequestParam(value = "emaildate", defaultValue = "0") long emaildate, @RequestParam(value = "youhuowudanflag", defaultValue = "-1") String youhuowudanflag,
			@RequestParam(value = "autoallocatid", defaultValue = "-1") String entranceno) {
		if(isAutoAllocatingButDisconnected(entranceno)){
			throw new CwbException(cwb, FlowOrderTypeEnum.RuKu.getValue(), ExceptionCwbErrorTypeEnum.AUTO_ALLOCATING_BUT_DISCOUNTED_PLEASE_HANDLE);
		}
		
		long startTime = System.currentTimeMillis();
		ExplinkResponse resp = null;
		JSONArray promt = null;
		String msg = null;
		String scancwb = cwb;
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = new CwbOrder();
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		User user = this.getSessionUser();

		String branchName="";//配送站点名称
		String outputNo="";//出货口编号
		boolean isPackage = false;
		// 订单不存在，则可能是包号，按照包号进行查询
		if (null == co) {
			// 这里cwb是包号
			Bale isbale=this.baleDAO.getBaleOnway(scancwb);
			List<String> cwbList = null;
			if(isbale==null){
				msg = "无此包号:" + scancwb;
			}else{
				cwbList = this.baleCwbDao.getCwbsByBale(""+isbale.getId());
			}
			if (isbale!=null&&(cwbList != null) && !cwbList.isEmpty()) {
				isPackage = true;
				String validateSecondLevelMsg = this.validateSecondLevelStationPackage(cwbList);
				if (isbale.getBalestate() == BaleStateEnum.BuKeYong.getValue()) {
					msg = scancwb + "已被拆包";
				} else if (!StringUtil.isEmpty(validateSecondLevelMsg)) {
					msg = validateSecondLevelMsg;
				} else {
					// 更新包的下一站为0，当前站位操作人的站点
					this.baleDAO.updateBranchIdAndNextBranchId(isbale.getId(), 0, this.getSessionUser().getBranchid());

					StringBuilder cwbs = new StringBuilder();
					for (String cwbStr : cwbList) {
						cwbs.append(cwbStr).append("\r\n");
					}
					BatchCount batchCount = new BatchCount(0, 0, 0);
					boolean useEaimDate = emaildate == 0 ? false : true;
					Branch b = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());

					List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表

					List<JSONObject> objList = new ArrayList<JSONObject>();
					List<String> allEmaildate = new ArrayList<String>();
					if (useEaimDate) {
						allEmaildate = this.cwbDAO.getListByEmaildateId(b.getBranchid(), b.getSitetype(), 1, customerid, emaildate);
					}
					promt = new JSONArray();
					boolean intohouseFailed = false;
					try {
						this.cwbOrderService.intohouseForExpressPackage(this.getSessionUser(), cwbs.toString(), customerid, driverid, batchCount, useEaimDate, cList, objList, allEmaildate, promt);
					} catch (Exception e) {
						intohouseFailed = true;
						msg = e.getMessage();
					}
					if (!intohouseFailed && (cwbs.length() > 0)) {
						// 分拣库入库自动拆包 added by songkaojun 2015-11-09
						logger.info("分拣库入库自动拆包，包号：" + isbale.getBaleno());
						this.baleDAO.updateBalesate(isbale.getId(), BaleStateEnum.BuKeYong.getValue());
						msg = scancwb + " 成功扫描" + batchCount.getThissuccess() + "单";
						// 移除下一站显示信息 songkaojun 2015-11-06
						// msg += "<br>下一站：" + nextBranch.getBranchname();
					}
				}
			}
		}
		if (!isPackage) {
			if (emaildate > 0) {
				if (null == co) {
					ExplinkResponse explinkResponse = new ExplinkResponse("2", "查无此单", cwb);
					explinkResponse.addLastWav(this.getErrorWavFullPath(request, WavFileName.WCDH));
					return explinkResponse;
				}
				if (co.getEmaildateid() != emaildate) {
					EmailDate e = this.emaildateDAO.getEmailDateById(co.getEmaildateid());
					ExplinkResponse explinkResponse = new ExplinkResponse("2", "订单号不在本批次中，请选择" + e.getEmaildatetime() + "的批次", cwb);
					return explinkResponse;
				}
			}
			try {
				Branch userbranch = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
				// 不允许二级站点直接入库
				this.validateSecondLevelStation(co);

				if ((userbranch.getBranchid() != 0) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
					cwbOrder = this.cwbOrderService.substationGoods(this.getSessionUser(), cwb, scancwb, driverid, requestbatchno, comment, "", false);
				} else {
					if (youhuowudanflag.equals("0")) {
						this.checkyouhuowudan(this.getSessionUser(), cwb, customerid, this.getSessionUser().getBranchid());
						/**
						 * add by 王志宇
						 * 快递入库跨环节逻辑，如果一个订单当前站不为空，并且该订单的当前环节的下一环节包括入库环节
						 * ，则此次入库为跨环节入库 如果是跨环节入库则需补上订单出站轨迹并回传tps
						 */
						// =======================add
						// begin================================
						List<CwbStateControl> cwbStateControlList = new ArrayList<CwbStateControl>();
						if (co.getFlowordertype() > 0) {
							cwbStateControlList = this.cwbStateControlDAO.getCwbStateControlByFromstate(Integer.parseInt(co.getFlowordertype() + ""));
						}
						int kuahuanjieFlag = 0;
						// 判断是否为快递单，并且是当前站是否为0或者空
						if ((co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) && (co.getCurrentbranchid() != 0) && (co.getCurrentbranchid() != user.getBranchid())) {
							for (int a = 0; a < cwbStateControlList.size(); a++) {
								// 判断订单当前环节的下一环节是否包含入库
								if (cwbStateControlList.get(a).getTostate() == FlowOrderTypeEnum.RuKu.getValue()) {
									// 创建流程跟踪信息表
									this.cwbOrderService.createFloworder(user, co.getCurrentbranchid(), co, FlowOrderTypeEnum.LanJianChuZhan, "", System.currentTimeMillis(), scancwb, false);
									// 状态回传tps
									// this.expressOutStationService.executeTpsTransInterface4TransFeedBack(co.getCwb(),
									// user, co.getNextbranchid());
									kuahuanjieFlag++;
								}
							}
							if (kuahuanjieFlag == 0) {
								throw new CwbException(cwb, FlowOrderTypeEnum.RuKu.getValue(), ExceptionCwbErrorTypeEnum.BuNengKuaHuanJieRuKu);
							}
						}
						// =====================add
						// end=======================================

						if (co.getDeliverybranchid() == 0) {
							FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.RuKu;
							String bupipeishifouruku = "0"; // 0：不验证 1：验证 2：弹框
							bupipeishifouruku = this.systemInstallDAO.getSystemInstall("bupipeishifouruku") == null ? "0" : this.systemInstallDAO.getSystemInstall("bupipeishifouruku").getValue();
							if ("2".equals(bupipeishifouruku.trim())) {
								throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.ShangWeiPiPeiZhanDian);
							} else if ("1".equals(bupipeishifouruku.trim())) {
								throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.ShangWeiPiPeiZhanDianBuyunxuRuku);
							}
						}
					}
					cwbOrder = this.cwbOrderService.intoWarehous(this.getSessionUser(), cwb, scancwb, customerid, driverid, requestbatchno, comment, "", false);
					this.tpsCwbFlowService.save(cwbOrder,scancwb, FlowOrderTypeEnum.RuKu,this.getSessionUser().getBranchid(),null,true,null,null);
				}
				JSONObject obj = new JSONObject();
				resp = new ExplinkResponse("000000", CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText(), obj);

				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());

				if (cwbOrder.getNextbranchid() != 0) {
					Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
					obj.put("cwbbranchname", branch.getBranchname());
				} else {
					obj.put("cwbbranchname", "");
				}

				if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
					obj.put("cwbgaojia", "true");
					resp.addShortWav(this.getErrorWavFullPath(request, "gj.wav"));
				} else {
					obj.put("cwbgaojia", "");
				}
				// 添加货物类型声音.
				this.addGoodsTypeWaveJSON(request, co, resp);
				// 查询系统设置，得到name=showCustomer的express_set_system_install表中的value,加入到obj中
				String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
				List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
				String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
				String[] showcustomer = list.get(0).getCustomerid().split(",");
				for (String s : showcustomer) {
					if (s.equals(cwbcustomerid)) {
						CwbOrder order = this.cwbDAO.getCwbByCwb(cwb);
						Object a;
						try {
							a = order.getClass().getMethod("get" + list.get(0).getRemark()).invoke(order);
							obj.put("showRemark", a);
						} catch (Exception e) {
							this.logger.error("", e);
							obj.put("showRemark", "Erro");
						}
					}
				}
				/*
				 * 单票成功订单，有站点提示语音的，忽略通用提示音
				 */
				// 通用提示音
				String wavPath = null;
				// 一票多件提示音乐
				String multiTipPath = null;
				if (resp.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
					wavPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.OK.getVediourl());
				} else {
					wavPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.SYS_ERROR.getVediourl());
				}
				if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
					resp.setErrorinfo(resp.getErrorinfo() + "\n一票多件");
					if (this.isPlayYPDJSound()) {
						multiTipPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
						resp.addLongWav(multiTipPath);
					}
				}
				// 添加前台提示音播放列表(前台按照声音顺序进行播放)
				if (cwbOrder.getDeliverybranchid() != 0) {
					// 如果存在站点声音，忽略通用提示音
					Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
					branchName=branch.getBranchname();//站点中文名
					outputNo=branch.getOutputno();//站点出货口
					obj.put("cwbdeliverybranchname", branch.getBranchname());
					if (!this.isStringEmpty(branch.getBranchwavfile())) {
						String fullPath = this.getWavFullPath(request, branch.getBranchwavfile());
						resp.addLastWav(fullPath);
						obj.put("cwbdeliverybranchnamewav", fullPath);
					} else {
						// 存在站点,但未设置声音，也使用通用提示音
						resp.addLongWav(wavPath);
					}
				} else {
					// 如果不存在站点声音，使用通用提示音
					resp.addLongWav(wavPath);
					obj.put("cwbdeliverybranchname", "");
					obj.put("cwbdeliverybranchnamewav", "");
				}
				// 如果扫描的是封在某个包里面的快递单，则将该包设为不可用
				this.setExpressPackageUnable(cwbOrder);

				/**
				 * 对接自动分拨的中间件
				 */
				this.addQueue(outputNo, entranceno, cwb, branchName, scancwb, Constants.INTOWAHOUSE_TYPE_INTOWAHOUSE);		
			
				this.logger.info("分拣库入库扫描的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
				return resp;
			} catch (CwbException e) {
				this.logger.error("cwb="+cwb,e);
				cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
				if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU.getValue()) {
					Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
					ExplinkResponse explinkResponse = new ExplinkResponse(CwbOrderPDAEnum.CHONG_FU_RU_KU.getCode(), cwb + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError() + " "
							+ StringUtil.nullConvertToEmptyString(branch.getBranchname()), null);
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_RU_KU.getVediourl());
					// 添加货物类型声音.
					this.addGoodsTypeWaveJSON(request, co, explinkResponse);
					explinkResponse.addLongWav(this.getErrorWavFullPath(request, CwbOrderPDAEnum.CHONG_FU_RU_KU.getVediourl()));
					return explinkResponse;
				} else {
					ExplinkResponse explinkResponse = new ExplinkResponse(e.getError().getValue() + "", e.getMessage(), null);
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
					// 添加货物类型声音.
					this.addGoodsTypeWaveJSON(request, co, explinkResponse);
					// 单号不存在异常.
					this.addNoOrderWav(request, e, explinkResponse);
					explinkResponse.addShortWav(this.getErrorWavFullPath(request, CwbOrderPDAEnum.SYS_ERROR.getVediourl()));
					return explinkResponse;
				}
			}
		} else {
			resp = new ExplinkResponse("103", msg, promt);
			this.logger.info("分拣库入库扫描的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
			return resp;
		} 
	}

	private String validateSecondLevelStationPackage(List<String> cwbList) {
		String msg = "";
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwbList.get(0));
		try {
			this.validateSecondLevelStation(co);
		} catch (Exception e) {
			msg = e.getMessage();
		}
		return msg;
	}

	/**
	 *
	 *
	 * @param cwb
	 * @param co
	 */
	private void validateSecondLevelStation(CwbOrder co) {
		if ((co == null) || (CwbOrderTypeIdEnum.Express.getValue() != co.getCwbordertypeid())) {
			return;
		}
		// 当前站点
		Branch currentbranch = this.branchDAO.getBranchById(co.getCurrentbranchid());
		Integer branchType = Integer.parseInt(currentbranch.getContractflag() == null ? "0" : currentbranch.getContractflag());

		if (branchType == BranchTypeEnum.ErJiZhan.getValue()) {
			throw new CwbException(co.getCwb(), FlowOrderTypeEnum.RuKu.getValue(), ExceptionCwbErrorTypeEnum.Second_Level_Station_Validation);
		}
	}

	/**
	 *
	 * 如果扫描的是封在某个包里面的快递单，则将该包设为不可用
	 *
	 * @param cwbOrder
	 */
	private void setExpressPackageUnable(CwbOrder cwbOrder) {
		if ((cwbOrder == null) || (cwbOrder.getCwb() == null) || (cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Express.getValue())) {
			return;
		}
		this.baleService.disableBale(cwbOrder.getCwb());
		/*
		List<BaleCwb> baleCwbList = this.baleCwbDao.getBaleCwbByCwb(cwbOrder.getCwb());
		if ((baleCwbList == null) || baleCwbList.isEmpty()) {
			return;
		}
		for (BaleCwb baleCwb : baleCwbList) {
			List<Bale> baleList = this.baleDAO.getBaleByBaleno(baleCwb.getBaleno());
			if ((baleList == null) || baleList.isEmpty()) {
				continue;
			}
			for (Bale bale : baleList) {
				if (bale.getBalestate() == BaleStateEnum.BuKeYong.getValue()) {
					continue;
				}
				this.baleDAO.updateBalesate(bale.getBaleno(), BaleStateEnum.BuKeYong.getValue());
			}
		}
		*/
	}

	private boolean addNoOrderWav(HttpServletRequest request, CwbException e, ExplinkResponse explinkResponse) {
		if (!ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI.equals(e.getError())) {
			return false;
		}
		explinkResponse.addLongWav(this.getErrorWavFullPath(request, WavFileName.WCDH));

		return true;
	}

	private boolean isStringEmpty(String str) {
		if ((str == null) || (str.length() == 0)) {
			return true;
		}
		return false;
	}

	private String getErrorWavFullPath(HttpServletRequest request, String fillName) {
		return request.getContextPath() + ServiceUtil.waverrorPath + fillName;
	}

	private String getWavFullPath(HttpServletRequest request, String fillName) {
		return request.getContextPath() + ServiceUtil.wavPath + fillName;
	}

	private void addGoodsTypeWaveJSON(HttpServletRequest request, CwbOrder order, ExplinkResponse explinkResponse) {
		if ((order == null) || !this.isPlayGPSound()) {
			return;
		}
		int goodsType = order.getGoodsType();
		switch (goodsType) {
		case 1: {
			explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.DJ));
			break;
		}
		case 2: {
			explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GP));
			break;
		}
		case 3: {
			explinkResponse.addLongWav(this.getErrorWavFullPath(request, WavFileName.DJGP));
			break;
		}
		default: {
		}
		}
	}

	private void addCustomerWav(HttpServletRequest request, ExplinkResponse explinkResponse, CwbOrder order) {
		Customer customer = this.customerDAO.getCustomerById(order.getCustomerid());
		if (customer == null) {
			return;
		}
		explinkResponse.addLongWav(this.getWavFullPath(request, customer.getWavFilePath()));
	}

	/**
	 * 入库扫描（批量）
	 *
	 * @param model
	 * @param cwbs
	 * @param customerid
	 * @param driverid
	 * @return
	 */
	@RequestMapping("/cwbintowarhouseBatch")
	public String cwbintowarhouseBatch(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid,
			@RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		BatchCount batchCount = new BatchCount(0, 0, 0);
		boolean useEaimDate = emaildate == 0 ? false : true;
		Branch b = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());

		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();
		List<String> allEmaildate = new ArrayList<String>();
		if (useEaimDate) {
			allEmaildate = this.cwbDAO.getListByEmaildateId(b.getBranchid(), b.getSitetype(), 1, customerid, emaildate);
		}

		JSONArray promt = new JSONArray();
		this.processBatchIntohouse(cwbs, customerid, driverid, batchCount, useEaimDate, cList, objList, allEmaildate, promt);
		model.addAttribute("objList", objList);
		List<User> uList = this.userDAO.getUserByRole(3);
		// TODO 按批次查询
		List<Map<String, Object>> countobj = this.cwbDAO.getRukubyBranchid(this.getSessionUser().getBranchid(), b.getSitetype(), customerid, emaildate);
		model.addAttribute("count", countobj.get(0).get("count"));
		model.addAttribute("sum", countobj.get(0).get("sum"));
		model.addAttribute("thissuccess", this.cwbDAO.getYiRukubyBranchid(this.getSessionUser().getBranchid(), customerid, emaildate).getOpscwbid());
		model.addAttribute("lesscwbnum", this.ypdjHandleRecordDAO.getRukuQuejianbyBranchid(this.getSessionUser().getBranchid(), customerid, emaildate));
		model.addAttribute("youhuowudanCount", batchCount.getYouhuowudanCount());
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;

		// 未入库明细
		List<CwbOrder> weirukulist = this.cwbDAO.getRukuByBranchidForList(b.getBranchid(), b.getSitetype(), 1, customerid, emaildate);
		List<CwbDetailView> weirukuViewlist = this.getcwbDetail(weirukulist, cList, showCustomerjSONArray, null, 0);

		// 已入库明细
		List<CwbOrder> yirukulist = this.cwbDAO.getYiRukubyBranchidList(b.getBranchid(), customerid, 1, emaildate);
		List<CwbDetailView> yirukuViewlist = this.getcwbDetail(yirukulist, cList, showCustomerjSONArray, null, 0);
		model.addAttribute("weirukulist", weirukuViewlist);
		model.addAttribute("yirukulist", yirukuViewlist);

		model.addAttribute("userList", uList);
		model.addAttribute("customerlist", cList);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.RuKuDaYinBiaoQian.getText()));
		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + batchCount.getThissuccess() + "单，异常" + (batchCount.getAllcwbnum() - batchCount.getThissuccess()) + "单";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("promt", promt.toString());
		model.addAttribute("emaildate", emaildate);
		return "pda/intowarhouseBatch";
	}

	private void processBatchIntohouse(String cwbs, long customerid, long driverid, BatchCount batchCount, boolean useEaimDate, List<Customer> cList, List<JSONObject> objList,
			List<String> allEmaildate, JSONArray promt) {
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			if (useEaimDate && !allEmaildate.contains(cwb)) {
				CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
				JSONObject obj = new JSONObject();
				obj.put("cwb", cwb);
				if (null == co) {
					obj.put("emaildatename", "查无此单");
				} else {
					EmailDate e = this.emaildateDAO.getEmailDateById(co.getEmaildateid());
					if (null == e) {
						obj.put("emaildatename", "查无此单");
					} else {
						obj.put("emaildatename", "订单号不在本批次中，请选择" + e.getEmaildatetime() + "的批次");
					}
				}
				promt.add(obj);
				continue;
			}
			batchCount.setAllcwbnum(batchCount.getAllcwbnum() + 1);
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);

			try {// 成功订单
				//有货无单校验
				this.checkyouhuowudan(this.getSessionUser(), cwb, customerid, this.getSessionUser().getBranchid());
				CwbOrder cwbOrder = this.cwbOrderService.intoWarehous(this.getSessionUser(), cwb, scancwb, customerid, driverid, 0, "", "", false);
				this.tpsCwbFlowService.save(cwbOrder,scancwb, FlowOrderTypeEnum.RuKu,this.getSessionUser().getBranchid(),null,true,null,null);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				//****Hps_Concerto************add 2016年6月27日 17:31:39
				String cwbordertype="";
				String flowordertypetext="";
				String cwbstatetext="";
				if(cwbOrder!=null){
					for (CwbStateEnum  ce   : CwbStateEnum.values()){
						 if(ce.getValue()==cwbOrder.getCwbstate()){
							 cwbstatetext = ce.getText();
						 }
					 } 
					try{
						if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype())!=null){
							if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText()=="已审核"){
									flowordertypetext="审核为："+DeliveryStateEnum.getByValue(cwbOrder.getDeliverystate()).getText();
							 }else if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText()=="已反馈") {
									flowordertypetext="反馈为："+DeliveryStateEnum.getByValue(cwbOrder.getDeliverystate()).getText(); 
								/*hps*/ }else{
									 flowordertypetext=CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText();
							 }
						}else{
							flowordertypetext="";
						}
					}catch(Exception e){
						flowordertypetext="";
					}
					cwbstatetext=CwbOrderTypeIdEnum.getTextByValue(cwbOrder.getCwbordertypeid());
				}
				obj.put("cwbordertype", cwbordertype);
				obj.put("cwbstatetext", cwbstatetext);
				obj.put("flowordertypetext", flowordertypetext);
				//**********
				for (Customer c : cList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
				if (cwbOrder.getEmaildateid() == 0) {
					batchCount.setYouhuowudanCount(batchCount.getYouhuowudanCount() + 1);
				}
				batchCount.setThissuccess(batchCount.getThissuccess() + 1);
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				//***Hps_Concerto  add 2016年6月27日 17:30:48
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				String cwbordertype="";
				String flowordertypetext="";
				String cwbstatetext="";
				if(cwbOrder!=null){
					for (CwbStateEnum  cr   : CwbStateEnum.values()){
						 if(cr.getValue()==cwbOrder.getCwbstate()){
							 cwbstatetext = cr.getText();
						 }
					 } 
					try{
						if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype())!=null){
							if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText()=="已审核"){
									flowordertypetext="审核为："+DeliveryStateEnum.getByValue(cwbOrder.getDeliverystate()).getText();
							 }else if(CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText()=="已反馈") {
									flowordertypetext="反馈为："+DeliveryStateEnum.getByValue(cwbOrder.getDeliverystate()).getText(); 
								/*hps*/ }else{
									 flowordertypetext=CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText();
							 }
						}else{
							flowordertypetext="";
						}
					}catch(Exception e){
						flowordertypetext="";
					}
					cwbordertype=CwbOrderTypeIdEnum.getTextByValue(cwbOrder.getCwbordertypeid());
				}
				obj.put("cwbordertype", cwbordertype);
				obj.put("cwbstatetext", cwbstatetext);
				obj.put("flowordertypetext", flowordertypetext);
				//********************
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
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

			}
			objList.add(obj);
		}
	}

	/**
	 * 中转站入库扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param customerid
	 * @param driverid
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/cwbChangeintowarhouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbChangeintowarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "requestbatchno", required = true, defaultValue = "0") long requestbatchno, @RequestParam(value = "comment", required = true, defaultValue = "") String comment,
			@RequestParam(value = "autoallocatid", defaultValue = "-1") String entranceno) {
		if(isAutoAllocatingButDisconnected(entranceno)){
			throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.AUTO_ALLOCATING_BUT_DISCOUNTED_PLEASE_HANDLE);
		}
		String scancwb = cwb;
		String branchName="";//配送站点名称
		String outputNo="";//出货口编号
		cwb = this.cwbOrderService.translateCwb(cwb);

		CwbOrder cwbOrder = new CwbOrder();
		try {

			cwbOrder = this.cwbOrderService.changeintoWarehous(this.getSessionUser(), cwb, scancwb, customerid, 0, requestbatchno, comment, "", false, 0, 0);
			this.baleService.disableBale(scancwb);
			JSONObject obj = new JSONObject();
			obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
			obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
			if (cwbOrder.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				obj.put("cwbbranchname", branch.getBranchname());
				// obj.put("cwbbranchnamewav", request.getContextPath() +
				// ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? ""
				// : branch.getBranchwavfile()));
			} else {
				obj.put("cwbbranchname", "");
				obj.put("cwbbranchnamewav", "");
			}
			ExplinkResponse explinkResponse = new ExplinkResponse("000000", CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText(), obj);
			if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				obj.put("cwbgaojia", "true");
				explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GJ));
			} else {
				obj.put("cwbgaojia", "");
			}
			// 查询系统设置，得到name=showCustomer的express_set_system_install表中的value,加入到obj中
			String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
			List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
			String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
			String[] showcustomer = list.get(0).getCustomerid().split(",");
			for (String s : showcustomer) {
				if (s.equals(cwbcustomerid)) {
					CwbOrder order = this.cwbDAO.getCwbByCwb(cwb);
					Object a;
					try {
						a = order.getClass().getMethod("get" + list.get(0).getRemark()).invoke(order);
						obj.put("showRemark", a);
					} catch (Exception e) {
						this.logger.error("", e);
						obj.put("showRemark", "Erro");
					}
				}
			}
			this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
			String wavPath = null;
			if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
			} else {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
			}
			if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
				explinkResponse.setErrorinfo(explinkResponse.getErrorinfo() + "\n一票多件");
				if (this.isPlayYPDJSound()) {
					wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl();
				}
			}
			explinkResponse.addLongWav(wavPath);
			if (cwbOrder.getDeliverybranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
				branchName=branch.getBranchname();//站点中文名
				outputNo=branch.getOutputno();//站点出货口
				obj.put("cwbdeliverybranchname", branch.getBranchname());
				obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
				if (branch.getBranchwavfile() != null) {
					explinkResponse.addLastWav(request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile());
				}
			} else {
				obj.put("cwbdeliverybranchname", "");
				obj.put("cwbdeliverybranchnamewav", "");
			}
			
			/**
			 * 对接自动分拨的中间件
			 */
			this.addQueue(outputNo, entranceno, cwb, branchName, scancwb, Constants.INTOWAHOUSE_TYPE_CHANGE_INTOWAHOUSE);
			
			return explinkResponse;
		} catch (CwbException e) {
			this.logger.error("cwb="+cwb,e);
			cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
					: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
			if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU.getValue()) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				ExplinkResponse explinkResponse = new ExplinkResponse(CwbOrderPDAEnum.CHONG_FU_RU_KU.getCode(), cwb + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError() + " "
						+ StringUtil.nullConvertToEmptyString(branch.getBranchname()), null);
				this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);

				String wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_RU_KU.getVediourl();
				explinkResponse.addShortWav(wavPath);

				return explinkResponse;
			} else {
				ExplinkResponse explinkResponse = new ExplinkResponse(e.getError().getValue() + "", e.getMessage(), null);
				this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);

				String wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
				explinkResponse.addShortWav(wavPath);

				return explinkResponse;
			}

		}
	}

	/**
	 * 中转站入库扫描（批量）
	 *
	 * @param model
	 * @param cwbs
	 * @param customerid
	 * @param driverid
	 * @return
	 */
	@RequestMapping("/cwbChangeintowarhouseBatch")
	public String cwbChangeintowarhouseBatch(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid) {
		long allcwbnum = 0;
		long thissuccess = 0;
		
		Branch b = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());

		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();

		JSONArray promt = new JSONArray();
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			allcwbnum++;
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);

			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.changeintoWarehous(this.getSessionUser(), cwb, scancwb, customerid, 0, 0, "", "", false, 0, 0);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				for (Customer c : cList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
				thissuccess++;
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
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

			}
			objList.add(obj);
		}
		model.addAttribute("objList", objList);
		List<User> uList = this.userDAO.getUserByRole(3);
		// TODO 按批次查询
		List<Map<String, Object>> countobj = this.cwbDAO.getZhongZhuanZhanRukubyBranchid(this.getSessionUser().getBranchid(), b.getSitetype(), customerid);
		model.addAttribute("count", countobj.get(0).get("count"));
		model.addAttribute("sum", countobj.get(0).get("sum"));
		model.addAttribute("thissuccess", this.cwbDAO.getZhongZhuanZhanYiRukubyBranchid(this.getSessionUser().getBranchid(), customerid).getOpscwbid());
		model.addAttribute("lesscwbnum", this.ypdjHandleRecordDAO.getZhongZhuanZhanRukuQuejianbyBranchid(this.getSessionUser().getBranchid(), customerid));
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;

		// 未入库明细
		List<CwbOrder> weirukulist = this.cwbDAO.getZhongZhuanZhanRukuByBranchidForList(b.getBranchid(), b.getSitetype(), 1, customerid);
		List<CwbDetailView> weirukuViewlist = this.getcwbDetail(weirukulist, cList, showCustomerjSONArray, null, 0);

		// 已入库明细
		List<CwbOrder> yirukulist = this.cwbDAO.getZhongZhuanZhanYiRukubyBranchidList(b.getBranchid(), customerid, 1);
		List<CwbDetailView> yirukuViewlist = this.getcwbDetail(yirukulist, cList, showCustomerjSONArray, null, 0);
		model.addAttribute("weirukulist", weirukuViewlist);
		model.addAttribute("yirukulist", yirukuViewlist);

		model.addAttribute("userList", uList);
		model.addAttribute("customerlist", cList);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.RuKuDaYinBiaoQian.getText()));
		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + thissuccess + "单，异常" + (allcwbnum - thissuccess) + "单";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("promt", promt.toString());
		return "pda/changeintowarhouseBatch";
	}

	/**
	 * 得到入库扫描批量 未入库list======================= getintowarehousebacthweiruku
	 */
	@RequestMapping("/getintowarehousebacthweiruku")
	public @ResponseBody List<CwbDetailView> getintowarehousebacthweiruku(@RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "customerid", defaultValue = "0") long customerid, @RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		List<CwbOrder> weirukulist = this.cwbDAO.getRukuByBranchidForList(b.getBranchid(), b.getSitetype(), page, customerid, emaildate);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(weirukulist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;
	}

	/**
	 * 得到入库扫描 批量 已入库 =================================
	 */
	@RequestMapping("/getintowarhousebatchyiruku")
	public @ResponseBody List<CwbDetailView> getintowarhousebatchyiruku(@RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "customerid", defaultValue = "0") long customerid, @RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		List<CwbOrder> yirukulist = this.cwbDAO.getYiRukubyBranchidList(b.getBranchid(), customerid, page, emaildate);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> yirukuViewlist = this.getcwbDetail(yirukulist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return yirukuViewlist;
	}

	/**
	 * 分站到货扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param customerid
	 * @param driverid
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/cwbsubstationGoods/{cwb}")
	public @ResponseBody ExplinkResponse cwbsubstationGoods(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid, @RequestParam(value = "requestbatchno", required = true, defaultValue = "0") long requestbatchno,
			@RequestParam(value = "comment", required = true, defaultValue = "") String comment) {
		User user = this.getSessionUser();
		String scancwb = cwb;
		Bale isbale = this.baleDAO.getBaleOnway(cwb);
		ExplinkResponse resp = null;
		JSONArray promt = null;
		String msg = null;
		// this.baleDAO.updateBalesate(cwb, BaleStateEnum.BuKeYong.getValue());
		boolean isPackage = false;
		if ((isbale == null)) {
			msg = "无此包号:" + cwb;
		}
		if ((isbale != null)) {
			if (user.getBranchid() == isbale.getBranchid()) {
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.ZHANDIANHEBAOBUYUNXUBENZHANDAOHUO);
			}
			// 这里cwb是包号
			List<String> cwbList = this.baleCwbDao.getCwbsByBale(""+isbale.getId());
			if ((cwbList != null) && !cwbList.isEmpty()) {
				isPackage = true;
				 if (isbale.getBalestate() == BaleStateEnum.BuKeYong.getValue()) {
					msg = cwb + "已被拆包";
				} else {
					StringBuilder cwbs = new StringBuilder();
					for (String cwbStr : cwbList) {
						cwbs.append(cwbStr).append("\r\n");
					}
					BatchCount batchCount = new BatchCount(0, 0, 0);

					List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取供货商列表

					List<JSONObject> objList = new ArrayList<JSONObject>();
					promt = new JSONArray();

					this.doCwbBranchIntoWarehouse(cwbs.toString(), batchCount, customerList, objList);

					String errorcode = objList.get(0).get("errorcode").toString();

					if (!errorcode.equals("000000")) {
						msg = objList.get(0).get("errorinfo").toString();
					} else {
						msg = cwb + " 成功扫描" + batchCount.getThissuccess() + "单";
					}
				}
			}
			//if(openbale!=null&&openbale.equals("1")){
			logger.info("cwbsubstationGoods，分站到货扫描，包号：" + isbale.getBaleno());
			this.baleDAO.updateBalesate(isbale.getId(), BaleStateEnum.YiDaoHuo.getValue());
			//}
		}
		if (!isPackage) {
			cwb = this.cwbOrderService.translateCwb(cwb);
			CwbOrder cwbOrderOld = this.cwbDAO.getCwbByCwb(cwb);
			// User user = this.getSessionUser();
			if (cwbOrderOld == null) {
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}

			/**
			 * add by 王志宇
			 *
			 * 快递分站到货跨环节逻辑，如果一个订单当前站不为空，并且该订单的当前环节的下一环节包括入库环节，则此次分战到货为跨环节分战到货
			 * 如果是跨环节分战到货则需补上订单出站轨迹并回传tps
			 */
			// ==================add begin=================================
			List<CwbStateControl> cwbStateControlList = new ArrayList<CwbStateControl>();
			if (cwbOrderOld.getFlowordertype() > 0) {
				cwbStateControlList = this.cwbStateControlDAO.getCwbStateControlByFromstate(Integer.parseInt(cwbOrderOld.getFlowordertype() + ""));

			}
			int kuahuanjieFlag = 0;

			// 判断是否为快递单，并且是当前站是否为0或者空
			if (cwbOrderOld.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
				// 判断当前站
				if ((cwbOrderOld.getCurrentbranchid() != 0) && (cwbOrderOld.getCurrentbranchid() != user.getBranchid())) {
					for (int a = 0; a < cwbStateControlList.size(); a++) {
						// 判断订单当前环节的下一环节是否包含入分战到货 //如果到错货就不需要补这个流程信息 --刘武强 11.25
						if ((cwbStateControlList.get(a).getTostate() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())) {
							if (cwbOrderOld.getFlowordertype() != FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) {
								// 创建流程跟踪信息表
								this.cwbOrderService.createFloworder(user, cwbOrderOld.getCurrentbranchid(), cwbOrderOld, FlowOrderTypeEnum.LanJianChuZhan, "", System.currentTimeMillis(), scancwb, false);
							}
							// 状态回传tps
							// expressOutStationService.executeTpsTransInterface4TransFeedBack(cwbOrderOld.getCwb(),
							// user, cwbOrderOld.getNextbranchid());
							kuahuanjieFlag = 1;
							break;
						}
					}
					if (kuahuanjieFlag == 0) {
						throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BuNengKuaHuanJieRuZhan);
					}
				} else if ((cwbOrderOld.getCurrentbranchid() != 0) && (cwbOrderOld.getCurrentbranchid() == user.getBranchid())) {
					// new Operation_Repeat
					throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Operation_Repeat);
				}
			} else {
				Branch branchStart = this.branchDAO.getBranchByBranchid(cwbOrderOld.getStartbranchid());
				// 如果上一站是二级站
				if ((branchStart != null) && (branchStart.getContractflag() != null)) {
					if (Integer.parseInt(branchStart.getContractflag()) == BranchTypeEnum.ErJiZhan.getValue()) {
						cwbOrderOld.setFlowordertype(FlowOrderTypeEnum.LanJianRuZhan.getValue());
						cwbOrderOld.setNextbranchid(0);
					}
				}

			}
			// ===================add end==============================

			CwbOrder cwbOrder = this.cwbOrderService.substationGoods(this.getSessionUser(), cwb, scancwb, driverid, requestbatchno, comment, "", false);
			JSONObject obj = new JSONObject();
			String deliveryname = "";
			if (cwbOrder != null) {
				if (cwbOrder.getDeliverid() != 0) {
					if (this.getSessionUser().getBranchid() == cwbOrder.getDeliverid()) {
						deliveryname = this.userDAO.getAllUserByid(cwbOrder.getDeliverid()).getRealname() == null ? "" : this.userDAO.getAllUserByid(cwbOrder.getDeliverid()).getRealname();
					}
				}
			}
			if ((int) cwbOrderOld.getFlowordertype() == FlowOrderTypeEnum.DingDanLanJie.getValue()) {// 订单拦截显示在页面上
				obj.put("dingdanlanjie", "订单拦截");
			} else {
				obj.put("dingdanlanjie", "");
			}
			obj.put("deliveryname", deliveryname);
			obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
			obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
			if (cwbOrder.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				obj.put("cwbbranchname", branch.getBranchname());
				obj.put("cwbbranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
			} else {
				obj.put("cwbbranchname", "");
				obj.put("cwbbranchnamewav", "");
			}
			ExplinkResponse explinkResponse = new ExplinkResponse("000000", FlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText(), obj);
			// 站点机构声音
			if (cwbOrder.getDeliverybranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
				obj.put("cwbdeliverybranchname", branch.getBranchname());
				obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
			} else {
				obj.put("cwbdeliverybranchname", "");
				obj.put("cwbdeliverybranchnamewav", "");
			}
			// 配送员
			String deliverName = "";
			if(cwbOrder.getExceldeliverid() != 0) {
				User deliver = this.userService.getUserByUserid(cwbOrder.getExceldeliverid());
				if(deliver != null) {
					deliverName = deliver.getRealname();
				}
			}
			obj.put("deliverName", deliverName);
			// 查询系统设置，得到name=showCustomer的express_set_system_install表中的value,加入到obj中
			String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
			List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
			String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
			String[] showcustomer = list.get(0).getCustomerid().split(",");
			for (String s : showcustomer) {
				if (s.equals(cwbcustomerid)) {
					CwbOrder order = this.cwbDAO.getCwbByCwb(cwb);
					Object a;
					try {
						a = order.getClass().getMethod("get" + list.get(0).getRemark()).invoke(order);
						obj.put("showRemark", a);
					} catch (Exception e) {
						this.logger.error("", e);
						obj.put("showRemark", "Erro");
					}
				}
			}
			// 加入货物类型声音.
			this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
			// 操作成功、失败声音
			String wavPath = null;
			// 一票多件提示音乐
			String multiTipPath = null;
			if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
			} else {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
			}
			//不论成功失败都要令包设为不可用
			//if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
				//如果扫描的是封在某个包里面的快递单，则将该包设为不可用
				//this.setExpressPackageUnable(cwbOrder);
			//}
			this.baleService.disableBale(scancwb);

			explinkResponse.addLongWav(wavPath);
			if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
				explinkResponse.setErrorinfo(explinkResponse.getErrorinfo() + "\n一票多件");
				if (this.isPlayYPDJSound()) {
					multiTipPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl();
					explinkResponse.addLongWav(multiTipPath);
				}
			}
			if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				obj.put("cwbgaojia", "true");
				explinkResponse.addLongWav(this.getErrorWavFullPath(request, WavFileName.GJ));
			} else {
				obj.put("cwbgaojia", "");
			}
			return explinkResponse;
		} else {
			resp = new ExplinkResponse("000001", msg, promt);
			return resp;
		}
	}

	private void doCwbBranchIntoWarehouse(String cwbs, BatchCount batchCount, List<Customer> customerList, List<JSONObject> objList) {
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			batchCount.setAllcwbnum(batchCount.getAllcwbnum() + 1);
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.substationGoods(this.getSessionUser(), cwb, scancwb, -1, 0, "", "", false);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				batchCount.setThissuccess(batchCount.getThissuccess() + 1);
			} catch (CwbException ce) {// 出现验证错误
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
				obj.put("cwbOrder", cwbOrder);
				obj.put("errorcode", ce.getError().getValue());
				obj.put("errorinfo", ce.getMessage());
				if (cwbOrder == null) {// 如果无此订单
					obj.put("customername", "");
					obj.put("outstoreroomtime", "");// 出库时间
				} else {
					OrderFlow of = this.orderFlowDAO.getOrderFlowByCwbAndFlowtype(cwbOrder.getCwb(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "");
					for (Customer c : customerList) {
						if (c.getCustomerid() == cwbOrder.getCustomerid()) {
							obj.put("customername", c.getCustomername());
							break;
						}
					}
					obj.put("outstoreroomtime", of != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()) : "");// 出库时间
				}
			}
			objList.add(obj);
		}
	}

	/**
	 * 按包号入库扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param customerid
	 * @param driverid
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/cwbintowarhouseByPackageCode/{packageCode}")
	public @ResponseBody ExplinkResponse cwbintowarhouseByPackageCode(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("packageCode") String packageCode,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid) {
		/*
		 * List<CwbOrder> coList = cwbDAO.getCwbByPackageCode(packageCode);
		 * String cwbs = "";
		 */
		Bale isbale = this.baleDAO.getBaleOnway(packageCode);

		JSONObject obj = new JSONObject();
		obj.put("packageCode", packageCode);

		if (/* coList.size()==0|| */packageCode.equals("0") || (isbale == null)) {
			ExplinkResponse explinkResponse = new ExplinkResponse("600000", CwbFlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText(), obj);
			explinkResponse.setErrorinfo("\n按包到货入库，包号不存在，操作失败");
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
			return explinkResponse;
		} else {
			logger.info("cwbintowarhouseByPackageCode，按包号入库扫描，包号：" + isbale.getBaleno());
			this.baleDAO.updateBalesate(isbale.getId(), BaleStateEnum.YiDaoHuo.getValue());
			ExplinkResponse explinkResponse = new ExplinkResponse("000000", CwbFlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText(), obj);
			explinkResponse.setErrorinfo("\n按包到货成功，已到货");
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
			return explinkResponse;
		}
	}

	/**
	 * 入库备注提交
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param csremarkid
	 * @param multicwbnum
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/forremark/{cwb}")
	public @ResponseBody ExplinkResponse forremark(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "csremarkid", required = false, defaultValue = "0") long csremarkid, @RequestParam(value = "multicwbnum", required = false, defaultValue = "0") long multicwbnum,
			@RequestParam(value = "content", required = false, defaultValue = "") String content) {
		cwb = this.cwbOrderService.translateCwb(cwb);
		if (csremarkid == 1) {
			content = "破损";
		} else if (csremarkid == 2) {
			content = "超大";
		} else if (csremarkid == 3) {
			content = "超重";
		} else if (csremarkid == 4) {
			content = "一票多物";
		}

		this.cwbOrderService.forremark(this.getSessionUser(), content, multicwbnum, cwb);

		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", "");
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		return explinkResponse;
	}

	/**
	 * 出库扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/cwbexportwarhouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbexportwarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag,
			@RequestParam(value = "requestbatchno", required = true, defaultValue = "") String requestbatchno, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno,
			@RequestParam(value = "comment", required = false, defaultValue = "") String comment, @RequestParam(value = "reasonid", required = false, defaultValue = "0") long reasonid,
			@RequestParam(value = "carrealweight", required = false, defaultValue = "0") BigDecimal carrealweight ) {
		long startTime = System.currentTimeMillis();
		String msg = null;
		boolean isPackage = false;
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		// 订单不存在，则可能是包号，按照包号进行查询
		if (null == co) {
			// 这里cwb是包号
			Bale isbale = this.baleDAO.getBaleOnway(cwb);
			List<String> cwbList = null;
			if(isbale==null){
				msg = "（异常扫描）无此包号:" + cwb;
			}else{
				cwbList = this.baleCwbDao.getCwbsByBale(""+isbale.getId());
			}		
			if ((cwbList != null) && !cwbList.isEmpty()) {
				isPackage = true;
				if (branchid == 0) {
					msg = "（异常扫描）请先选择下一站";
				} else {
					if (isbale.getBalestate() == BaleStateEnum.BuKeYong.getValue()) {
						msg = "（异常扫描）" + cwb + "已被拆包";
					} else {
						/* ***************modify begin*********************/
						//comment by neo01.huang，2016-7-25，这种代码没加入事务控制，出异常了也不回滚代码。
						//把下面的代码抽取到一个带有事务的service方法中，逻辑不变，用于解决live问题1751，合包扫描过程中当前站为0导致不能封包出库
						
						// 更新包的下一站为用户选择的下一站，当前站为0
//						this.baleDAO.updateBranchIdAndNextBranchId(isbale.getId(), branchid, 0);
//
//						StringBuilder cwbs = new StringBuilder();
//						for (String cwbStr : cwbList) {
//							cwbs.append(cwbStr).append("\r\n");
//						}
//						BatchCount batchCount = new BatchCount(0, 0, 0);
//						List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表
//
//						List<JSONObject> objList = new ArrayList<JSONObject>();
//
//						boolean exportHouseFailed = false;
//						try {
//							this.cwbOrderService.exportHouseForExpressPackage(this.getSessionUser(),cwbs.toString(), branchid, driverid, truckid, confirmflag, batchCount, cList, objList);
//						} catch (Exception e) {
//							exportHouseFailed = true;
//							msg = "（异常扫描）" + e.getMessage();
//						}
						/* ***************modify end***********************/
						
						/* ***************add begin*********************/
						//add by neo01.huang，2016-7-25
						boolean exportHouseFailed = false;
						StringBuilder cwbs = new StringBuilder();
						BatchCount batchCount = new BatchCount(0, 0, 0);
						try {
							//出库扫描包号处理
							baleService.cwbexportwarhouseScanBaleNo(
									getSessionUser(), isbale, cwb, branchid, cwbList, driverid, truckid, confirmflag, cwbs, batchCount);
						} catch (Exception e) {
							exportHouseFailed = true;
							String errMsg = "包号：" + cwb + ",";
							if (e instanceof CwbException) {
								CwbException cwbException = (CwbException) e;
								errMsg += "单号：" + cwbException.getCwb() + ",操作：" + cwbException.getFlowordertye();
							}
							errMsg += "," + e.getMessage();
							logger.info(errMsg, e);
							msg = "（异常扫描）" + e.getMessage();
						}
						/* ***************add begin*********************/

						if (!exportHouseFailed && (cwbs.length() > 0)) {
							Branch nextBranch = this.branchDAO.getBranchByBranchid(branchid);
							msg = cwb + " 成功扫描" + batchCount.getThissuccess() + "单";
							msg += "<br>下一站：" + nextBranch.getBranchname();
						}
					}
				}
			}
		}
		if (!isPackage) {
			JSONObject obj = new JSONObject();

			ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);

			long successCount = request.getSession().getAttribute(baleno + "-successCount") == null ? 0 : Long.parseLong(request.getSession().getAttribute(baleno + "-successCount").toString());
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			CwbOrder cwbOrder = this.cwbOrderService.outWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid,
					requestbatchno == null ? 0 : requestbatchno.length() == 0 ? 0 : Long.parseLong(requestbatchno), confirmflag == 1, comment, baleno, reasonid, false, false);
			Customer customer = this.customerDAO.getCustomerById(cwbOrder.getCustomerid());
			this.tpsCwbFlowService.save(cwbOrder, scancwb, FlowOrderTypeEnum.ChuKuSaoMiao,this.getSessionUser().getBranchid(),null,true,null,null);
			obj.put("packageCode", baleno);
			obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
			obj.put("cwbcustomername", customer.getCustomername());
			// 出库报配送站声音.
			if (cwbOrder.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				obj.put("cwbbranchname", branch.getBranchname());
			} else {
				obj.put("cwbbranchname", "");
			}
			if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				obj.put("cwbgaojia", "true");
				explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GJ));
			} else {
				obj.put("cwbgaojia", "");
			}
			// 查询系统设置，得到name=showCustomer的express_set_system_install表中的value,加入到obj中
			String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
			List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
			String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
			String[] showcustomer = list.get(0).getCustomerid().split(",");
			for (String s : showcustomer) {
				if (s.equals(cwbcustomerid)) {
					CwbOrder order = this.cwbDAO.getCwbByCwb(cwb);
					Object a;
					try {
						a = order.getClass().getMethod("get" + list.get(0).getRemark()).invoke(order);
						obj.put("showRemark", a);
					} catch (Exception e) {
						this.logger.error("", e);
						obj.put("showRemark", "Erro");
					}
				}
			}
			// 添加货物类型音频文件.
			this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
			String wavPath = null;
			String multiple = null;
			if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
				if (this.isPlayYPDJSound()) {
					multiple = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl();
					explinkResponse.addLongWav(multiple);
				}
			}

			if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
				if (!baleno.equals("") && !baleno.equals("0")) {
					successCount++;
					request.getSession().setAttribute(baleno + "-successCount", successCount);
					explinkResponse.setErrorinfo("\n按包出库成功，已出库" + successCount + "件");
					obj.put("successCount", successCount);
				}
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
			} else {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
			}

			if (cwbOrder.getDeliverybranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
				obj.put("cwbdeliverybranchname", branch.getBranchname());
				if (!StringUtils.isEmpty(branch.getBranchwavfile())) {
					explinkResponse.addLastWav(request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile());
				} else {
					explinkResponse.addLongWav(wavPath);
				}
			} else {
				explinkResponse.addLongWav(wavPath);
				obj.put("cwbdeliverybranchname", "");
			}

			// 如果扫描的是封在某个包里面的快递单，则将该包设为不可用
			this.setExpressPackageUnable(cwbOrder);
			// add by bruce shangguan 20160923  如果是首次扫描订单号/运单号,就直接更新订单重量；否则就在订单原重量的基础上累加，再更新订单重量
			if(carrealweight != null && carrealweight.compareTo(BigDecimal.ZERO) > 0){
				if(cwbOrder.getScannum() == 1){
					this.cwbDAO.saveCwbWeight(carrealweight, cwb);
				}else{
					BigDecimal totalWeight = cwbOrder.getCarrealweight() == null ? BigDecimal.ZERO : cwbOrder.getCarrealweight() ;
					totalWeight = totalWeight.add(carrealweight);
					this.cwbDAO.saveCwbWeight(totalWeight, cwb);
				}
				// 如果扫描运单号，就更新当前运单的货物重量
			    if(customer.getIsUsetranscwb() != 2){
				   this.mpsOptStateService.updateTransCwbDetailWeight(cwb, scancwb, carrealweight);
				}
				obj.put("newCarrealWeight", carrealweight);
			}
			// end 20160923
			this.logger.info("分拣库入库扫描的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
			return explinkResponse;
		} else {
			this.logger.info("分拣库入库扫描的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
			return new ExplinkResponse("0001", msg, "");
		}

	}
	

	/**
	 * 中转站出库扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/cwbchangeoutwarhouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbchangeoutwarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag,
			@RequestParam(value = "requestbatchno", required = true, defaultValue = "") String requestbatchno, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno,
			@RequestParam(value = "comment", required = false, defaultValue = "") String comment, @RequestParam(value = "reasonid", required = false, defaultValue = "0") long reasonid) {
		return this._cwbchangeexportwarhouse(model, request, response, cwb, branchid, driverid, truckid, confirmflag, requestbatchno, baleno, comment, reasonid, true);
	}

	/**
	 * 中转出站功能
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param requestbatchno
	 * @param baleno
	 * @param comment
	 * @param reasonid
	 * @param deliverybranchid
	 * @return
	 */
	@RequestMapping("/cwbchangeexportwarhouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbchangeexportwarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag,
			@RequestParam(value = "requestbatchno", required = true, defaultValue = "") String requestbatchno, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno,
			@RequestParam(value = "comment", required = false, defaultValue = "") String comment, @RequestParam(value = "reasonid", required = false, defaultValue = "0") long reasonid,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long deliverybranchid) {
		JSONObject obj = new JSONObject();

		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);

		long successCount = request.getSession().getAttribute(baleno + "-successCount") == null ? 0 : Long.parseLong(request.getSession().getAttribute(baleno + "-successCount").toString());
		String scancwb = cwb;
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrdercheck = this.cwbDAO.getCwbByCwb(cwb);
		if (cwbOrdercheck != null) {
			if ((cwbOrdercheck.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (cwbOrdercheck.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue())) {
				explinkResponse.setStatuscode(ExceptionCwbErrorTypeEnum.Fenzhanzhiliustatenotzhongzhanchuzhan.getValue() + "");
				explinkResponse.setErrorinfo(ExceptionCwbErrorTypeEnum.Fenzhanzhiliustatenotzhongzhanchuzhan.getText());
				return explinkResponse;
			}
			if ((cwbOrdercheck.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (cwbOrdercheck.getDeliverystate() == DeliveryStateEnum.JuShou.getValue())) {
				explinkResponse.setStatuscode(ExceptionCwbErrorTypeEnum.ShenheweijushouCannotZhongzhuanchuzhan.getValue() + "");
				explinkResponse.setErrorinfo(ExceptionCwbErrorTypeEnum.ShenheweijushouCannotZhongzhuanchuzhan.getText());
				return explinkResponse;
			}
			// 揽件入站状态不允许中转出站
			if ((cwbOrdercheck.getFlowordertype() == FlowOrderTypeEnum.LanJianRuZhan.getValue())) {
				explinkResponse.setStatuscode(ExceptionCwbErrorTypeEnum.ExpressInstationNotAllowZHONGZHUANCHUZHAN.getValue() + "");
				explinkResponse.setErrorinfo(ExceptionCwbErrorTypeEnum.ExpressInstationNotAllowZHONGZHUANCHUZHAN.getText());
				return explinkResponse;
			}
			// 揽件录入状态不允许中转出站
			if ((cwbOrdercheck.getFlowordertype() == FlowOrderTypeEnum.YunDanLuRu.getValue())) {
				explinkResponse.setStatuscode(ExceptionCwbErrorTypeEnum.ExpressLuruNotAllowZHONGZHUANCHUZHAN.getValue() + "");
				explinkResponse.setErrorinfo(ExceptionCwbErrorTypeEnum.ExpressLuruNotAllowZHONGZHUANCHUZHAN.getText());
				return explinkResponse;
			}
			
			//add by neo01.huang,2016-5-4
			//校验上门退成功订单
			cwbOrderTypeService.validateShangMenTuiSuccess(cwbOrdercheck, FlowOrderTypeEnum.ZhongZhuanZhanChuKu);
			
		}
		CwbOrder cwbOrder = this.cwbOrderService.outWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid,
				requestbatchno == null ? 0 : requestbatchno.length() == 0 ? 0 : Long.parseLong(requestbatchno), confirmflag == 1, comment, baleno, reasonid, true, false);

		obj.put("packageCode", baleno);
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
		if (cwbOrder.getNextbranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
			obj.put("cwbbranchname", branch.getBranchname());
			obj.put("cwbbranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbbranchname", "");
			obj.put("cwbbranchnamewav", "");
		}
		if (cwbOrder.getDeliverybranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
			obj.put("cwbdeliverybranchname", branch.getBranchname());
			obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbdeliverybranchname", "");
			obj.put("cwbdeliverybranchnamewav", "");
		}
		if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			obj.put("cwbgaojia", "true");
			explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GJ));
		} else {
			obj.put("cwbgaojia", "");
		}

		if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
			if (this.isPlayYPDJSound()) {
				explinkResponse.addShortWav(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
			}
		}
		// 添加贵品声音.
		this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
		String wavPath = null;
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			if ((deliverybranchid > 0) && (branchid != deliverybranchid)) {
				try {
					try {
						Branch deliverybranch = this.branchDAO.getBranchByBranchid(deliverybranchid);
						CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
						if ((cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue())
								|| (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
																																// 都将匹配状态变更为修改
							addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
						} else if ((cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue())
								|| (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是为匹配的
																																// 或者是人工匹配的
																																// 都将匹配状态变更为人工修改
							addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
						}

						this.cwbOrderService.updateDeliveryOutBranch(this.getSessionUser(), cwbOrder, deliverybranch, addressCodeEditType, branchid);

						obj.put("cwbdeliverybranchname", deliverybranch.getBranchname());
						obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (deliverybranch.getBranchwavfile() == null ? "" : deliverybranch.getBranchwavfile()));
					} catch (CwbException ce) {
						explinkResponse.setStatuscode(ce.getError().getValue() + "");
						explinkResponse.setErrorinfo(ce.getMessage());
					}
				} catch (Exception e) {
					this.logger.error("", e);
					this.logger.info("中转出站，cwb：{},deliverybranchid:{}", cwbOrder.getCwb(), deliverybranchid);
				}
			}
			if (!baleno.equals("") && !baleno.equals("0")) {
				successCount++;
				request.getSession().setAttribute(baleno + "-successCount", successCount);
				explinkResponse.setErrorinfo("\n按包出库成功，已出库" + successCount + "件");
				obj.put("successCount", successCount);
			}
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
		} else {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}
		explinkResponse.addShortWav(wavPath);

		return explinkResponse;
	}

	/**
	 * 站点出站
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param branchid
	 * @param driverid
	 * @param confirmflag
	 * @return
	 */
	@RequestMapping("/cwbbranchexportwarhouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbbranchexportwarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag) {
		JSONObject obj = new JSONObject();

		String scancwb = cwb;
		ExplinkResponse resp = null;
		JSONArray promt = null;
		String msg = null;
		boolean isPackage = false;
		// 如果扫描的是封在某个包里面的快递单，则将该包设为不可用
		// CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		// this.baleDAO.updateBalesate(cwb, BaleStateEnum.BuKeYong.getValue());
		
		//add by neo01.huang,2016-5-4
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		//校验上门退成功订单
		cwbOrderTypeService.validateShangMenTuiSuccess(co, FlowOrderTypeEnum.ChuKuSaoMiao);
		
		Bale isbale = this.baleDAO.getBaleOnway(cwb);
		List<String> cwbList = null;
		if(isbale==null){
			msg = "（异常扫描）无此包号:" + cwb;
		}else{
			cwbList = this.baleCwbDao.getCwbsByBale(""+isbale.getId());
		}	
		if (isbale!=null) {
			// 这里cwb是包号
			if ((cwbList != null) && !cwbList.isEmpty()) {
				isPackage = true;
				if (isbale.getBalestate() == BaleStateEnum.BuKeYong.getValue()) {
					msg = cwb + "已被拆包";
				} else {

					BatchCount batchCount = new BatchCount(0, 0, 0);

					List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取供货商列表

					List<JSONObject> objList = new ArrayList<JSONObject>();
					promt = new JSONArray();

					this.doCwbbranchexportwarhouse(cwbList, batchCount, customerList, objList, branchid, driverid, confirmflag);

					String errorcode = objList.get(0).get("errorcode").toString();

					if (!errorcode.equals("000000")) {
						msg = objList.get(0).get("errorinfo").toString();
					} else {
						msg = cwb + " 成功扫描" + batchCount.getThissuccess() + "单";
					}
				}
			}
		}
		if (!isPackage) {
			ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
			cwb = this.cwbOrderService.translateCwb(cwb);
			CwbOrder cwbOrder = this.cwbOrderService.outWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, branchid, 0, confirmflag == 1, "", "", 0, false, false);
			obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
			obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
			if (cwbOrder.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				obj.put("cwbbranchname", branch.getBranchname());
				if ((branch.getBranchwavfile() != null) || (branch.getBranchwavfile().length() > 0)) {
					obj.put("cwbbranchnamewav", request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile());
				} else {
					obj.put("cwbbranchnamewav", "");
				}
			} else {
				obj.put("cwbbranchname", "");
				obj.put("cwbbranchnamewav", "");
			}
			if (cwbOrder.getDeliverybranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
				obj.put("cwbdeliverybranchname", branch.getBranchname());
				obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
			} else {
				obj.put("cwbdeliverybranchname", "");
				obj.put("cwbdeliverybranchnamewav", "");
			}
			if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				obj.put("cwbgaojia", "true");
				explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GJ));
			} else {
				obj.put("cwbgaojia", "");
			}
			// 加入货物类型声音.
			this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
			String wavPath = null;
			if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
				if (this.isPlayYPDJSound()) {
					wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl();
				}
			}
			if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
			} else {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
			}
			explinkResponse.addLastWav(wavPath);

			return explinkResponse;
		} else {
			return new ExplinkResponse("000001", msg, promt);

		}
	}

	/**
	 * 站点出站批量
	 *
	 * @param model
	 * @param cwbs
	 * @param branchid
	 * @param driverid
	 * @param confirmflag
	 * @return
	 */
	@RequestMapping("/cwbbranchexportwarhouseBatch")
	public String cwbbranchexportwarhouseBatch(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag) {
		long thissuccess = 0;
		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表
		List<User> uList = this.userDAO.getUserByRole(3);
		List<JSONObject> objList = new ArrayList<JSONObject>();
		long allcwbnum = 0;

		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			allcwbnum++;
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.outWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, branchid, 0, confirmflag == 1, "", "", 0, false, false);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				for (Customer c : cList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
				thissuccess++;
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);

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

			}
			objList.add(obj);
		}
		model.addAttribute("objList", objList);

		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());
		List<Branch> lastList = new ArrayList<Branch>();

		for (Branch b : bList) {
			if (b.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				lastList.add(b);
			}
		}

		model.addAttribute("customerlist", cList);

		model.addAttribute("branchlist", lastList);
		model.addAttribute("userList", uList);

		long chushibranchid = branchid;
		if (chushibranchid < 1) {
			chushibranchid = lastList.size() == 0 ? 0 : lastList.get(0).getBranchid();
		}
		long weicount = this.cwbDAO.getZhanDianChuZhanbyBranchid(this.getSessionUser().getBranchid(), chushibranchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		long yicount = this.cwbDAO.getZhanDianYiChuZhanbyBranchid(this.getSessionUser().getBranchid(), chushibranchid, chushibranchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());

		model.addAttribute("weicount", weicount);// 待出站总数
		model.addAttribute("yicount", yicount);// 已出站总数
		// ==================
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未出库==========================
		List<CwbDetailView> todayweilingViewlist = this.getcwbDetail(
				this.cwbDAO.getZhanDianChuZhanbyBranchidList(this.getSessionUser().getBranchid(), chushibranchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()), cList, showCustomerjSONArray,
				null, 0);

		// 已出库======================================
		List<String> cwborders = this.operationTimeDAO.getchaoqi(this.getSessionUser().getBranchid(), chushibranchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		List<CwbDetailView> yilinghuoViewList = this.getcwbDetail(this.cwbDAO.getZhanDianYiChuZhanbyBranchidList(cwborders), cList, showCustomerjSONArray, null, 0);

		model.addAttribute("weichukulist", todayweilingViewlist);
		model.addAttribute("yichukulist", yilinghuoViewList);
		model.addAttribute("showCustomerSign", showCustomerSign);
		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + thissuccess + "单，异常" + (allcwbnum - thissuccess) + "单";
		}
		model.addAttribute("msg", msg);

		return "pda/branchexportwarhouseBatch";
	}

	/**
	 * 库对库出库扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/cwbkdkexportwarhouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbkdkexportwarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag,
			@RequestParam(value = "requestbatchno", required = true, defaultValue = "") String requestbatchno, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno,
			@RequestParam(value = "comment", required = false, defaultValue = "") String comment) {
		JSONObject obj = new JSONObject();

		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);

		// long successCount =
		// request.getSession().getAttribute(baleno+"-successCount")==null?0:Long.parseLong(request.getSession().getAttribute(baleno+"-successCount").toString());
		String scancwb = cwb;
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = this.cwbOrderService.kdkoutWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid,
				requestbatchno == null ? 0 : requestbatchno.length() == 0 ? 0 : Long.parseLong(requestbatchno), confirmflag == 1, comment, baleno, 0);

		obj.put("packageCode", baleno);
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
		if (cwbOrder.getNextbranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
			obj.put("cwbbranchname", branch.getBranchname());
			obj.put("cwbbranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbbranchname", "");
			obj.put("cwbbranchnamewav", "");
		}
		if (cwbOrder.getDeliverybranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
			obj.put("cwbdeliverybranchname", branch.getBranchname());
			obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbdeliverybranchname", "");
			obj.put("cwbdeliverybranchnamewav", "");
		}
		if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			obj.put("cwbgaojia", "true");
			explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GJ));
		} else {
			obj.put("cwbgaojia", "");
		}
		// 加入货物类型声音.
		this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
		String wavPath = null;
		if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
			if (this.isPlayYPDJSound()) {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl();
			}
		}

		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			/*
			 * if(!baleno.equals("")&&!baleno.equals("0")){ successCount++;
			 * request.getSession().setAttribute(baleno+"-successCount",
			 * successCount);
			 * explinkResponse.setErrorinfo("\n按包出库成功，已出库"+successCount+"件"); }
			 */
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
		} else {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}
		explinkResponse.addLastWav(wavPath);

		return explinkResponse;
	}

	/**
	 * 库对库出库扫描====================================
	 *
	 * @param model
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param cwbs
	 * @param request
	 * @return
	 */
	@RequestMapping("/cwbkdkexportwarhouseBatch")
	public String cwbkdkexportwarhouseBatch(Model model, @RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid,
			@RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid, @RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid,
			@RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag, @RequestParam(value = "scancwbs", required = false, defaultValue = "") String cwbs,
			HttpServletRequest request) {
		long SuccessCount = 0;
		long alloutnum = 0;

		List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			alloutnum++;
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.kdkoutWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid, 0, confirmflag == 1, "", "", 0);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				SuccessCount++;
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);

				obj.put("cwbOrder", cwbOrder);
				obj.put("errorcode", ce.getError().getValue());
				obj.put("errorinfo", ce.getMessage());
				if (cwbOrder == null) {// 如果无此订单
					obj.put("customername", "");
				} else {
					for (Customer c : customerList) {
						if (c.getCustomerid() == cwbOrder.getCustomerid()) {
							obj.put("customername", c.getCustomername());
							break;
						}
					}
				}
			}
			objList.add(obj);
		}

		List<Branch> bList = this.cwbOrderService.getNextPossibleKuFangBranches(this.getSessionUser());

		List<CwbOrder> weichukulist = this.cwbDAO.getKDKChukuForCwbOrder(this.getSessionUser().getBranchid(), branchid == 0 ? (bList.size() > 0 ? bList.get(0).getBranchid() : 0) : branchid, -1);
		List<String> cwbyichukuList = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(this.getSessionUser().getBranchid(), branchid == 0 ? (bList.size() > 0 ? bList.get(0)
				.getBranchid() : 0) : branchid, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue());
		String yicwbs = "";
		if (cwbyichukuList.size() > 0) {
			yicwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbyichukuList);
		} else {
			yicwbs = "'--'";
		}
		List<CwbOrder> yichukulist = this.cwbDAO.getCwbByCwbsPage(1, yicwbs, Page.DETAIL_PAGE_NUMBER);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未出库明细
		List<CwbDetailView> weichukuViewlist = this.getcwbDetail(weichukulist, customerList, showCustomerjSONArray, null, 0);
		// 已出库明细
		List<CwbDetailView> yichukuViewlist = this.getcwbDetail(yichukulist, customerList, showCustomerjSONArray, null, 0);

		model.addAttribute("branchlist", bList);
		model.addAttribute("weichukulist", weichukuViewlist);
		model.addAttribute("yichukulist", yichukuViewlist);
		model.addAttribute("weichukucount", this.cwbDAO.getChukubyBranchid(this.getSessionUser().getBranchid(), branchid == 0 ? (bList.size() > 0 ? bList.get(0).getBranchid() : 0) : branchid, -1)
				.get(0).get("count"));
		model.addAttribute("yichukucount", cwbyichukuList.size());
		model.addAttribute("objList", objList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("showCustomerSign", showCustomerSign);

		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + SuccessCount + "单，异常" + (alloutnum - SuccessCount) + "单";
		}
		model.addAttribute("msg", msg);
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		return "pda/kdkexportwarhousebatch";
	}

	/**
	 * 退货出站扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/cwbexportUntreadWarhouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbexportUntreadWarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag,
			@RequestParam(value = "requestbatchno", required = true, defaultValue = "0") long requestbatchno, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno,
			@RequestParam(value = "comment", required = false, defaultValue = "") String comment) {
		String scancwb = cwb;
		long SuccessCount = request.getSession().getAttribute(baleno + "-successCount") == null ? 0 : Long.parseLong(request.getSession().getAttribute(baleno + "-successCount").toString());
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = this.cwbOrderService.outUntreadWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid, requestbatchno, confirmflag == 1, comment, baleno, false);

		JSONObject obj = new JSONObject();
		obj.put("packageCode", baleno);
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
		if (cwbOrder.getNextbranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
			obj.put("cwbbranchname", branch.getBranchname());
			obj.put("cwbbranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbbranchname", "");
			obj.put("cwbbranchnamewav", "");
		}
		if (cwbOrder.getDeliverybranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
			obj.put("cwbdeliverybranchname", branch.getBranchname());
			obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbdeliverybranchname", "");
			obj.put("cwbdeliverybranchnamewav", "");
		}
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			obj.put("cwbgaojia", "true");
			explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GJ));
		} else {
			obj.put("cwbgaojia", "");
		}
		// 加入货物类型声音.
		this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
		String wavPath = null;
		if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
			if (this.isPlayYPDJSound()) {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl();
			}
		}
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			if (!baleno.equals("") && !baleno.equals("0")) {// 为包号修改
				SuccessCount++;
				request.getSession().setAttribute(baleno + "-successCount", SuccessCount);
				explinkResponse.setErrorinfo("\n按包出库成功，已出库" + SuccessCount + "件");
				obj.put("successCount", SuccessCount);
			}
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
		} else {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}
		explinkResponse.addLastWav(wavPath);

		return explinkResponse;
	}

	/**
	 * 退货出站（批量）
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwbs
	 * @param branchid
	 * @param driverid
	 * @param confirmflag
	 * @return
	 */
	@RequestMapping("/cwbbranchbackexportBatch")
	public String cwbbranchbackexportBatch(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag) {
		long thissuccess = 0;
		List<Customer> customerList = this.customerDAO.getAllCustomers();

		List<JSONObject> objList = new ArrayList<JSONObject>();
		long allcwbnum = 0;
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			allcwbnum++;
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.outUntreadWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, branchid, 0, confirmflag == 1, "", "", false);// 为包号修改
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				for (Customer c : customerList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
				thissuccess++;
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);

				obj.put("cwbOrder", cwbOrder);
				obj.put("errorcode", ce.getError().getValue());
				obj.put("errorinfo", ce.getMessage());
				if (cwbOrder == null) {// 如果无此订单
					obj.put("customername", "");
				} else {
					for (Customer c : customerList) {
						if (c.getCustomerid() == cwbOrder.getCustomerid()) {
							obj.put("customername", c.getCustomername());
							break;
						}
					}
				}

			}
			objList.add(obj);
		}
		model.addAttribute("objList", objList);

		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + thissuccess + "单，异常" + (allcwbnum - thissuccess) + "单";
		}
		model.addAttribute("msg", msg);

		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser(), BranchEnum.TuiHuo);

		List<CwbOrder> weichuzhanlist = this.getAuditTuiHuo();
		List<CwbOrder> yichuzhanlist = this.cwbDAO.getCwbByFlowOrderTypeAndNextbranchidAndStartbranchidList(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), this.getSessionUser().getBranchid(), branchid);

		List<User> uList = this.userDAO.getUserByRole(3);

		long weiCount = weichuzhanlist.size();
		long yiCount = this.cwbDAO.getCwbByFlowOrderTypeAndNextbranchidAndStartbranchid(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), this.getSessionUser().getBranchid(), branchid);
		// ================
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未入库==========================
		List<CwbDetailView> weirukuViewlist = this.getcwbDetail(weichuzhanlist, customerList, showCustomerjSONArray, null, 0);

		// 已入库======================================
		List<CwbDetailView> yirukuViewlist = this.getcwbDetail(yichuzhanlist, customerList, showCustomerjSONArray, null, 0);

		model.addAttribute("weiCount", weiCount);
		model.addAttribute("yiCount", yiCount);
		model.addAttribute("weichuzhanlist", weirukuViewlist);
		model.addAttribute("yichuzhanlist", yirukuViewlist);
		model.addAttribute("customerlist", customerList);
		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("showCustomerSign", showCustomerSign);
		return "pda/branchbackexportBatch";
	}

	/**
	 * 退货站退货出站扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/cwbbackexportUntreadWarhouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbbackexportUntreadWarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag,
			@RequestParam(value = "requestbatchno", required = true, defaultValue = "0") long requestbatchno, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno,
			@RequestParam(value = "comment", required = false, defaultValue = "") String comment) {
		String scancwb = cwb;
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = this.cwbOrderService.outUntreadWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid, requestbatchno, confirmflag == 1, comment, baleno, false);// 为包号修改

		JSONObject obj = new JSONObject();
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
		if (cwbOrder.getNextbranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
			obj.put("cwbbranchname", branch.getBranchname());
			obj.put("cwbbranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbbranchname", "");
			obj.put("cwbbranchnamewav", "");
		}
		if (cwbOrder.getDeliverybranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
			obj.put("cwbdeliverybranchname", branch.getBranchname());
			obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbdeliverybranchname", "");
			obj.put("cwbdeliverybranchnamewav", "");
		}
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			obj.put("cwbgaojia", "true");
			explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GJ));
		} else {
			obj.put("cwbgaojia", "");
		}
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		if (((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) && this.isPlayYPDJSound()) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
		}
		return explinkResponse;
	}

	/**
	 * 小件员领货扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 *            订单
	 * @param deliverid
	 *            快递员
	 * @param requestbatchno
	 * @return
	 * @throws ParseException
	 *
	 *
	 */
	@RequestMapping("/cwbbranchdeliver/{cwb}")
	public @ResponseBody ExplinkResponse cwbbranchdeliver(Model model, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "deliverid", required = true, defaultValue = "0") long deliverid, boolean isChaoqu)
			throws ParseException {
		String scancwb = cwb;
		cwb = this.cwbOrderService.translateCwb(cwb);
		
		//校验是否存在退货出站审核中的记录
		orderBackCheckService.validateCheckStateAuditing(cwb, FlowOrderTypeEnum.FenZhanLingHuo);
		
		// json对象
		JSONObject obj = new JSONObject();
		// 判断当前流程是否为今日，供上门退订单分派使用.(包括重复扫描)
		this.addSmtDeliverPickingExtraMsg(obj, cwb);

		User deliveryUser = this.userDAO.getUserByUserid(deliverid);
		CwbOrder cwbOrder = this.cwbOrderService.receiveGoodsByDeliver(this.getSessionUser(), deliveryUser, cwb, scancwb, isChaoqu,true);
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));

		String searchCwb = "'" + cwb + "'";
		Map<String, List<String>> editCwbMap = this.cwbOrderService.getEditCwb(searchCwb);
		if ((editCwbMap != null) && !editCwbMap.isEmpty()) {
			String alertInfo = "";
			List<String> editCwbInfoList = editCwbMap.get("cwbInfo");
			List<String> editCwbPayList = editCwbMap.get("cwbPay");
			if (((editCwbPayList != null) && !editCwbPayList.isEmpty()) && ((editCwbInfoList != null) && !editCwbInfoList.isEmpty())) {
				alertInfo = "订单信息和订单支付信息存在修改，请及时核对！";
			} else if ((editCwbPayList != null) && !editCwbPayList.isEmpty()) {
				alertInfo = "订单涉及财务修改，请及时核对！";
			} else if ((editCwbInfoList != null) && !editCwbInfoList.isEmpty()) {
				alertInfo = "订单信息存在修改，请及时核对！";
			} else {// 没有修改订单
				// do nothing
			}
			obj.put("editCwb", alertInfo);
		} else {
			obj.put("editCwb", "");
		}

		if (cwbOrder.getDeliverid() != 0) {
			User user = this.userDAO.getUserByUserid(cwbOrder.getDeliverid());

			obj.put("cwbdelivername", user.getRealname());
			obj.put("cwbdelivernamewav", request.getContextPath() + ServiceUtil.wavPath + (user.getUserwavfile() == null ? "" : user.getUserwavfile()));

		} else {
			obj.put("cwbdelivername", "");
			obj.put("cwbdelivernamewav", "");
		}
		obj.put("guanlianlantuidan", cwbOrderService.haveRelatedShangMenTuiCwb(cwbOrder));
		
		// 查询系统设置，得到name=showCustomer的express_set_system_install表中的value,加入到obj中
		String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
		String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
		String[] showcustomer = list.get(0).getCustomerid().split(",");
		for (String s : showcustomer) {
			if (s.equals(cwbcustomerid)) {
				CwbOrder order = this.cwbDAO.getCwbByCwb(cwb);
				Object a;
				try {
					a = order.getClass().getMethod("get" + list.get(0).getRemark()).invoke(order);
					obj.put("showRemark", a);
				} catch (Exception e) {
					this.logger.error("", e);
					obj.put("showRemark", "Erro");
				}
			}
		}
		obj.put("isChaoqu", isChaoqu);
		if(isChaoqu) {
			String matchDeliver = "";
			if (cwbOrder.getExceldeliverid() > 0) {
				User matchDeliverUser = this.userDAO.getUserByUserid(cwbOrder.getExceldeliverid());
				if (matchDeliverUser != null) {
					matchDeliver = matchDeliverUser.getRealname();
				}
			}
			obj.put("matchDeliver", matchDeliver); //匹配到的小件员
			obj.put("receiveDeliver", deliveryUser.getRealname());
		}
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		// 加入货物类型声音.
		this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
		String wavPath = null;
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
		} else {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}

		explinkResponse.addLastWav(wavPath);

		if (cwbOrder.getSendcarnum() > 1) {
			if (cwbOrder.getScannum() == cwbOrder.getSendcarnum()) {
				this.cwbOrderService.delYpdjFlowordertypeMethod(cwb);
			}
		}

		return explinkResponse;
	}

	private void addSmtDeliverPickingExtraMsg(JSONObject obj, String cwb) throws ParseException {
		Map<String, Object> credateAndFlowTypeMap = this.queryCredateAndFlowTypeMap(cwb);
		String credate = (String) credateAndFlowTypeMap.get("credate");
		Integer flowType = (Integer) credateAndFlowTypeMap.get("flowType");
		if (credate == null) {
			return;
		}
		Date flowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(credate);
		obj.put("isTodayFlow", this.getTodayZeroDate().compareTo(flowTime) < 0);
		// 重复领货.
		obj.put("isRepeatPicking", FlowOrderTypeEnum.FenZhanLingHuo.getValue() == flowType.intValue());
	}

	private Date getTodayZeroDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}

	/**
	 * 订单最后流程时间，供上门退订单分派区分.
	 *
	 * @param cwb
	 * @return
	 */
	private Map<String, Object> queryCredateAndFlowTypeMap(String cwb) {
		return this.orderFlowDAO.queryOrderFlowAndCredate(cwb);
	}

	/**
	 * 小件员批量反馈扫描
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param deliverid
	 * @param requestbatchno
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/cwbdeliverpod/{cwbs}")
	public @ResponseBody ExplinkResponse cwbdeliverpod(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwbs") String cwbs,
			@RequestParam(value = "deliverid", required = true, defaultValue = "0") long deliverid, @RequestParam(value = "podresultid", required = true, defaultValue = "0") long podresultid,
			@RequestParam(value = "paywayid", required = false, defaultValue = "0") long paywayid, @RequestParam(value = "backreasonid", required = false, defaultValue = "0") long backreasonid,
			@RequestParam(defaultValue = "0", required = false, value = "firstlevelreasonid") long firstlevelreasonid,
			@RequestParam(defaultValue = "", required = false, value = "deliverstateremark") String deliverstateremark,
			@RequestParam(value = "leavedreasonid", required = false, defaultValue = "0") long leavedreasonid) throws UnsupportedEncodingException {
		String statuscode = CwbOrderPDAEnum.OK.getCode();
		StringBuilder errorMsg = new StringBuilder();
		for (String cwb : cwbs.split(",")) {
			this.logger.info("web-cwbdeliverpod小件员批量反馈扫描-进入单票反馈,cwb={}", cwb);
			String scancwb = cwb;
			try {
				cwb = this.cwbOrderService.translateCwb(cwb);
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("deliverid", deliverid);
				parameters.put("podresultid", podresultid);
				parameters.put("backreasonid", backreasonid);
				parameters.put("firstlevelreasonid", firstlevelreasonid);
				parameters.put("leavedreasonid", leavedreasonid);
				parameters.put("paywayid", paywayid);
				parameters.put("podremarkid", 0l);
				parameters.put("posremark", "");
				parameters.put("checkremark", "");
				parameters.put("deliverstateremark", URLDecoder.decode(deliverstateremark, "utf-8"));
				parameters.put("owgid", 0);
				parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
				parameters.put("sessionuserid", this.getSessionUser().getUserid());
				parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
				parameters.put("sign_time", DateTimeUtil.getNowTime());
				parameters.put("isbatch", true);
				this.cwbOrderService.deliverStatePod(this.getSessionUser(), cwb, scancwb, parameters);
			} catch (CwbException e) {
				this.logger.error("cwb="+cwb,e);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);

				statuscode = CwbOrderPDAEnum.SYS_ERROR.getCode();
				errorMsg = errorMsg.append(cwb).append("@").append(e.getMessage()).append(";");
				this.logger.error("归班反馈异常" + cwb, e);
			} catch (Exception e) {
				statuscode = CwbOrderPDAEnum.SYS_ERROR.getCode();
				errorMsg = errorMsg.append(cwb).append("@").append(e.getMessage());
				this.logger.error("归班反馈异常" + cwb, e);
			}
		}

		return new ExplinkResponse(statuscode, errorMsg.toString(), "");
	}

	/**
	 * 退货站入库扫描（退货中转入库扫描）
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param customerid
	 * @param driverid
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/cwbbackintowarhouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbbackintowarhouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid, @RequestParam(value = "comment", required = true, defaultValue = "") String comment,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "checktype", required = false, defaultValue = "0") int checktype,
			@RequestParam(value = "thzzrkFlag", required = false, defaultValue = "0") int thzzrkFlag) {

		String scancwb = cwb;
		// 根据运单找到订单
		cwb = this.cwbOrderService.translateCwb(cwb);
		// 订单详细信息
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		if (thzzrkFlag == 1) {// 这个参数为1，那么就是从退货中转入库进来的
			// 一票多件不支持退货中转入库的校验
			this.cwbOrderService.nonSupportMpsValid(co);
		}

		CwbOrder cwbOrder = null;
		if (checktype == 1) {
			if ((customerid > 0) && (co.getCustomerid() != customerid)) {
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.GongHuoShang_Bufu);
			}
			OperationTime op = this.operationTimeDAO.getObjectBycwb(cwb);
			if (op == null) {
				if (co.getFlowordertype() != FlowOrderTypeEnum.DingDanLanJie.getValue()) {
					throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
				} else {
					cwbOrder = this.cwbOrderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, comment, false, 1, co.getNextbranchid());
				}

			} else {
				if ((op.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) || (op.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())) {
					long branchid = 0;
					if (op.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
						branchid = op.getBranchid();
					} else {
						branchid = op.getNextbranchid();
					}
					cwbOrder = this.cwbOrderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, comment, false, 1, branchid);
				} else if (op.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
					// modified by songkaojun 去掉op.getFlowordertype() ==
					// FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()
					// 中转订单执行退货库入库的时候还调用中转入库服务，会提示重复入库
					if (op.getFlowordertype() != FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
						if ((op.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (co.getCwbstate() != CwbStateEnum.TuiHuo.getValue())) {
							Branch branch = this.branchDAO.getBranchByBranchid(op.getNextbranchid());
							if ((branch == null) || (branch.getSitetype() != BranchEnum.ZhongZhuan.getValue())) {
								throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.Fei_ZhongZhuan_Tuihuo);
							}
						}
						long branchid = 0;
						if (op.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
							branchid = op.getBranchid();
						} else {
							branchid = op.getNextbranchid();
						}
						cwbOrder = this.cwbOrderService.changeintoWarehous(this.getSessionUser(), cwb, scancwb, customerid, driverid, 0, comment, "", false, 1, branchid);
					}
				} else if ((co.getFlowordertype() == FlowOrderTypeEnum.DingDanLanJie.getValue())
						|| (co.getCwbstate() == CwbStateEnum.TuiGongYingShang.getValue() && co.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue())) {
					cwbOrder = this.cwbOrderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, comment, false, 1, 0);
				} else if (((co.getCwbstate() == CwbStateEnum.BUFENDIUSHI.getValue()) || (co.getCwbstate() == CwbStateEnum.BUFENPOSUN.getValue())
						|| (co.getCwbstate() == CwbStateEnum.WANQUANPOSUN.getValue()) || (co.getCwbstate() == CwbStateEnum.DiuShi.getValue()) || (co.getCwbstate() == CwbStateEnum.TuiHuo.getValue()))
						&& (co.getIsmpsflag() == 1) && (co.getNextbranchid() == this.getSessionUser().getBranchid())) {
					this.cwbOrderService.updateCwbFlowOrderType(cwb);
					cwbOrder = this.cwbOrderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, comment, false, 1, 0);
				} else {
					throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.Fei_ZhongZhuan_Tuihuo);
				}
			}
		} else {
			if (checktype == 2) {
				if (co.getCustomerid() != customerid) {
					throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.GongHuoShang_Bufu);
				}
			}
			cwbOrder = this.cwbOrderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, comment, false, 0, 0);
		}
		this.baleService.disableBale(scancwb);
		JSONObject obj = new JSONObject();
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		if (co.getFlowordertype() == FlowOrderTypeEnum.DingDanLanJie.getValue()) {
			obj.put("cwbcustomername", this.customerDAO.getCustomerById(co.getCustomerid()).getCustomername());
		} else {
			obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());

		}
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText(), obj);
		if (cwbOrder.getNextbranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
			obj.put("cwbbranchname", branch.getBranchname());
			obj.put("cwbbranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			obj.put("cwbgaojia", "true");
			explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GJ));
		} else {
			obj.put("cwbbranchname", "");
			obj.put("cwbbranchnamewav", "");
			obj.put("cwbgaojia", "");
		}

		if (cwbOrder.getDeliverybranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
			obj.put("cwbdeliverybranchname", branch.getBranchname());
			obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbdeliverybranchname", "");
			obj.put("cwbdeliverybranchnamewav", "");
		}
		// 加入供货商名称.
		this.addCustomerWav(request, explinkResponse, cwbOrder);
		// 加入货物类型声音.
		this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);

		String wavPath = null;
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			// wavPath = request.getContextPath() + ServiceUtil.waverrorPath +
			// CwbOrderPDAEnum.OK.getVediourl();
			if (cwbOrder.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
				// TODO 退货站入库 声音
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.TUI_HUO_RU_KU.getVediourl();
			} else if (cwbOrder.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
				// TODO 中转站入库 声音
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.ZHONG_ZHUAN_RU_KU.getVediourl();
			}
		} else {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}

		if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
			explinkResponse.setErrorinfo(explinkResponse.getErrorinfo() + "\n一票多件");
			if (this.isPlayYPDJSound()) {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl();
			}
		}
		explinkResponse.addLastWav(wavPath);

		return explinkResponse;
	}

	@RequestMapping("/cwbbackintowarhouseBatch")
	public String cwbbackintowarhouseBatch(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid, @RequestParam(value = "comment", required = true, defaultValue = "") String comment) {
		long allcwbnum = 0;
		long thissuccess = 0;
		List<Reason> backreasonList = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.TuiHuoZhanRuKuBeiZhu.getValue());

		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			allcwbnum++;
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, comment, false, 0, 0);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				for (Customer c : cList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
				thissuccess++;
				this.baleService.disableBale(scancwb);
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);

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

			}
			objList.add(obj);
		}
		model.addAttribute("objList", objList);

		List<User> uList = this.userDAO.getUserByRole(3);

		long branchid = this.getSessionUser().getBranchid();
		model.addAttribute("weirukucount", this.cwbDAO.getBackRukubyBranchid(branchid).get(0).get("count"));
		model.addAttribute("yirukucount", this.cwbDAO.getBackYiRukubyBranchid(branchid));

		model.addAttribute("weituihuorukuList", this.cwbDAO.getBackRukuByBranchidForList(this.getSessionUser().getBranchid(), 1));

		model.addAttribute("yituihuorukuList", this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), 1, String.valueOf(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())));

		model.addAttribute("userList", uList);
		model.addAttribute("customerlist", cList);
		model.addAttribute("backreasonList", backreasonList);
		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + thissuccess + "单，异常" + (allcwbnum - thissuccess) + "单";
		}
		model.addAttribute("msg", msg);

		return "pda/backimportbatch";
	}

	/*
	 * 退货站入库 批量 list 未入库
	 */
	@RequestMapping("/getbackintowarehouseweirukulist")
	public @ResponseBody List<CwbDetailView> getbackintowarehouseweirukulist(@RequestParam(value = "page", defaultValue = "1") long page) {
		List<CwbOrder> weirukulist = this.cwbDAO.getBackRukuByBranchidForList(this.getSessionUser().getBranchid(), page);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(weirukulist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;

	}

	/**
	 * 退货站入库 批量 list 已入库
	 */
	@RequestMapping("/getbackintowarehouseyirukulist")
	public @ResponseBody List<CwbDetailView> getbackintowarehouseyirukulist(@RequestParam(value = "page", defaultValue = "1") long page) {
		//
		List<CwbOrder> weirukulist = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), page, String.valueOf(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()));

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(weirukulist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;

	}

	/**
	 * 理货
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/cwbscancwbbranch/{cwb}")
	public @ResponseBody ExplinkResponse cwbbranchfinishchangeexport(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb) {
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);

		JSONObject obj = new JSONObject();
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));

		if (cwbOrder.getNextbranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
			obj.put("cwbbranchname", branch.getBranchname());
			obj.put("cwbbranchcode", branch.getBranchcode());
		} else {
			obj.put("cwbbranchcode", "");
			obj.put("cwbbranchname", "");
		}
		obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());

		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		return explinkResponse;
	}

	@RequestMapping("/cwbscancwbbranchnew/{cwb}")
	public @ResponseBody ExplinkResponse cwbbranchfinishchangeexportnew(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb) {
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);

		JSONObject obj = new JSONObject();
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		PrintStyle print = new PrintStyle();

		String str = this.systemInstallDAO.getSystemInstall("cqhy_print").getValue();
		try {
			print = JacksonMapper.getInstance().readValue(str, PrintStyle.class);
		} catch (Exception e) {
			this.logger.error("", e);
		}
		obj.put("print", print);
		Branch branch = this.branchDAO.getBranchByBranchname(cwbOrder.getExcelbranch());
		if (branch != null) {
			obj.put("branchcode", branch.getBranchcode());
		}
		obj.put("username", cwbOrder.getExceldeliver());
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		return explinkResponse;
	}
	
	
	@RequestMapping("/querycwbbranchcode")
	public @ResponseBody String querycwbbranchcode(HttpServletRequest request, @RequestParam(value = "cwb", required=true) String cwb) 
			throws JsonGenerationException, JsonMappingException, IOException {
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);

		JSONObject obj = new JSONObject();
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		PrintStyle print = new PrintStyle();

		String str = this.systemInstallDAO.getSystemInstall("cqhy_print").getValue();

		print = JacksonMapper.getInstance().readValue(str, PrintStyle.class);
		obj.put("print", print);
		Branch branch = this.branchDAO.getBranchById(cwbOrder.getDeliverybranchid());
		obj.put("branch", JSONObject.fromObject(branch));
		
		String value = this.systemInstallDAO.getSystemInstall("printdeliveryuser").getValue();
		if ("1".equals(value)) {
			User user = this.userDAO.getUserByUserid(cwbOrder.getExceldeliverid());
			obj.put("deliveryuser", user.getRealname() == null ? "" : user.getRealname());
		} else {
			obj.put("deliveryuser", "");
		}
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		String result = JacksonMapper.getInstance().writeValueAsString(explinkResponse); 
		return result;
	}

	@RequestMapping("/cwbscancwbbranchnew1")
	public @ResponseBody String cwbbranchfinishchangeexportnew1(HttpServletRequest request, @RequestParam(value = "cwb", required=true) String cwb) 
			throws JsonGenerationException, JsonMappingException, IOException {
//		String cwb = request.getParameter("cwb");
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);

		JSONObject obj = new JSONObject();
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		PrintStyle print = new PrintStyle();

		String str = this.systemInstallDAO.getSystemInstall("cqhy_print").getValue();

		print = JacksonMapper.getInstance().readValue(str, PrintStyle.class);
		obj.put("print", print);
		Branch branch = this.branchDAO.getBranchByBranchname(cwbOrder.getExcelbranch());
		if (branch != null) {
			obj.put("branchcode", branch.getBranchcode());
		}
		obj.put("username", cwbOrder.getExceldeliver());
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		String result = JacksonMapper.getInstance().writeValueAsString(explinkResponse); 
		return result;
	}

	@RequestMapping("/cwbscancwbbranchruku/{cwb}")
	public @ResponseBody ExplinkResponse cwbbranchfinishchangeexportruku(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb) {
		cwb = this.cwbOrderService.translateCwb(cwb);// 获取到运单号
		// 获取到该订单的对应所有数据
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		String code = cwbOrder != null ? "000000" : "111111";
		JSONObject obj = new JSONObject();
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));

		PrintStyle print = new PrintStyle();

		String str = this.systemInstallDAO.getSystemInstall("cqhy_print").getValue();
		try {
			print = JacksonMapper.getInstance().readValue(str, PrintStyle.class);
		} catch (Exception e) {
			this.logger.error("", e);
		}
		obj.put("print", print);
		Branch branch = this.branchDAO.getBranchByBranchname(cwbOrder.getExcelbranch());
		if (branch != null) {
			obj.put("branchcode", branch.getBranchcode());
		}
		obj.put("username", cwbOrder.getExceldeliver());
		ExplinkResponse explinkResponse = new ExplinkResponse(code, "", obj);
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		return explinkResponse;
	}

	/**
	 * 退供货商出库
	 *
	 * @param model
	 * @param request
	 * @param respStore
	 * @param cwb
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/cwbbacktocustomer/{cwb}")
	public @ResponseBody ExplinkResponse cwbbacktocustomer(HttpServletRequest request, @PathVariable("cwb") String cwb,
			@RequestParam(value = "baleno", required = false, defaultValue = "") String baleno, @RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid) {// 为包号修改
		String scancwb = cwb.trim();

		long successCount = request.getSession().getAttribute(baleno + "-TuigonghuoshangsuccessCount") == null ? 0 : Long.parseLong(request.getSession()
				.getAttribute(baleno + "-TuigonghuoshangsuccessCount").toString());
		cwb = this.cwbOrderService.translateCwb(cwb);

		CwbOrder co = this.cwbOrderService.backtocustom(this.getSessionUser(), cwb, scancwb, 0, baleno, false, customerid);
		co.setPackagecode(baleno);
		JSONObject obj = new JSONObject();
		obj.put("cwbOrder", JSONObject.fromObject(co));
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		// 加入货物类型声音.
		this.addGoodsTypeWaveJSON(request, co, explinkResponse);
		String wavPath = null;
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			if (!baleno.equals("") && !baleno.equals("0")) {
				successCount++;
				request.getSession().setAttribute(baleno + "-TuigonghuoshangsuccessCount", successCount);
				explinkResponse.setErrorinfo("\n按包出库成功，已出库" + successCount + "件");
				obj.put("successCount", successCount);
			}
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
		} else {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}
		explinkResponse.addLastWav(wavPath);

		return explinkResponse;
	}

	/**
	 * 退供货商出库批量
	 *
	 * @param model
	 * @param request
	 * @param respStore
	 * @param cwb
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/cwbbacktocustomerBatch")
	public String cwbbacktocustomerBatch(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid) {
		long thissuccess = 0;
		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();
		long allcwbnum = 0;
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			allcwbnum++;
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.backtocustom(this.getSessionUser(), cwb, scancwb, 0, "", false, customerid);// ""为包号修改
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				for (Customer c : cList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
				thissuccess++;
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
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

			}
			objList.add(obj);
		}
		model.addAttribute("objList", objList);

		// List<CwbOrder> weiChuKuList =
		// this.cwbDAO.getTGYSCKListbyBranchid(this.getSessionUser().getBranchid(),
		// 1);

		String flowordertypes = FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "," + FlowOrderTypeEnum.DingDanLanJie.getValue();
		List<CwbOrder> weiChuKuList = this.cwbDAO.getBackYiRukuListbyBranchid(this.getSessionUser().getBranchid(), 1, flowordertypes);

		model.addAttribute("customerlist", cList);
		model.addAttribute("weitghsckList", weiChuKuList);// 待出库数据
		model.addAttribute("yitghsckList", this.cwbDAO.getTuiGongHuoShangYiChuKu(this.getSessionUser().getBranchid(), 1));
		// model.addAttribute("count",
		// this.cwbDAO.getTGYSCKbyBranchid(this.getSessionUser().getBranchid()));//
		// 待出库总数
		Smtcount smtcount = this.cwbDAO.getBackYiRukubyBranchidsmt(this.getSessionUser().getBranchid(), flowordertypes);
		smtcount = smtcount == null ? new Smtcount() : smtcount;
		model.addAttribute("count", smtcount.getCount());
		model.addAttribute("yichukucount", this.cwbDAO.getTGYSYCK(this.getSessionUser().getBranchid()));// 已出库总数
		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + thissuccess + "单，异常" + (allcwbnum - thissuccess) + "单";
		}
		model.addAttribute("msg", msg);

		return "/pda/backtocustomerBatch";
	}

	/**
	 * 供货商拒收返库
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param remarkcontent备注内容
	 * @return
	 */
	@RequestMapping("/cwbcustomerrefuseback/{cwb}")
	public @ResponseBody ExplinkResponse cwbcustomerrefuseback(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "remarkcontent", required = false, defaultValue = "") String remarkcontent) {
		String scancwb = cwb;
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = this.cwbOrderService.customrefusebackToStorehouse(this.getSessionUser(), cwb, scancwb, 0, remarkcontent);
		JSONObject obj = new JSONObject();
		String customername = this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername();
		obj.put("customername", customername);
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		// 加入货物类型声音.
		this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
		String wavPath = null;
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
		} else {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}
		explinkResponse.addLastWav(wavPath);

		return explinkResponse;
	}

	/**
	 * 获得供货商拒收返库的数量
	 *
	 * @param model
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getRefusedSum")
	public @ResponseBody JSONObject getRefusedSum(Model model) {
		JSONObject obj = new JSONObject();
		long branchid = this.getSessionUser().getBranchid();
		obj.put("count", this.cwbDAO.getCustomerRefusedCount(branchid));
		obj.put("success", this.cwbDAO.getCustomerRefusedForScan(branchid));
		return obj;
	}

	/**
	 * 到货/入库 包扫描
	 *
	 * @param baleno
	 * @param driverid
	 * @param sysintowarhouse
	 * @return
	 */
	@RequestMapping("/getcwbbybaleno/{baleno}")
	public @ResponseBody JSONObject getcwbbybaleno(@PathVariable("baleno") String baleno, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "sysintowarhouse", required = true, defaultValue = "0") long sysintowarhouse) {
		JSONObject obj = new JSONObject();

		//List<Bale> balesinglelist = this.baleDAO.getBaleByBalestate(baleno, BaleStateEnum.WeiDaoZhan.getValue());

		Bale isbale = this.baleDAO.getBaleOnway(baleno);

		List<GroupDetail> gdcwblist = null;
		String cwbs = "";
		if (isbale==null) {// 该包不存在
			obj.put("errorinfo", "1");
			//} else if (balesinglelist.size() == 0) {// 未到站状态的该包不存在
			//obj.put("errorinfo", "1");
		} else {
			if (isbale.getBranchid() == this.getSessionUser().getBranchid()) {
				logger.info("包到货扫描，已出库，包号:" + isbale.getBaleno()) ;
				this.baleDAO.updateBalesate(isbale.getId(), BaleStateEnum.YiRuKu.getValue());

				gdcwblist = this.groupDetailDAO.getBroupDetailForBale(isbale.getId(), driverid, BaleStateEnum.YiRuKu.getValue(), this.getSessionUser().getBranchid());

			} else {// 非本站包
				logger.info("包到货扫描，非本站包，包号:" + isbale.getBaleno()) ;
				this.baleDAO.saveForBranchidAndState(isbale.getId(), this.getSessionUser().getBranchid(), BaleStateEnum.FeiBenZhanBao.getValue());
				gdcwblist = this.groupDetailDAO.getBroupDetailForBale(isbale.getId(), driverid, BaleStateEnum.FeiBenZhanBao.getValue(), this.getSessionUser().getBranchid());

			}
			if (this.groupDetailDAO.getBroupDetailForBale(isbale.getId(), driverid, BaleStateEnum.WeiDaoZhan.getValue(), this.getSessionUser().getBranchid()).size() > 0) {
				long groupid = gdcwblist.get(0).getGroupid();
				logger.info("包到货扫描，包号:" + isbale.getBaleno()) ;
				this.baleDAO.saveForBranchidAndGroupid(this.getSessionUser().getBranchid(), BaleStateEnum.YiDaoHuo.getValue(), groupid);
			}

			if (sysintowarhouse == 1) {

				for (GroupDetail gd : gdcwblist) {
					cwbs += gd.getCwb() + ",";
				}
				if (cwbs.length() > 0) {
					cwbs = cwbs.substring(0, cwbs.length() - 1);
				}
			}
			obj.put("cwbs", cwbs);
			obj.put("errorinfo", "0");
		}

		return obj;
	}

	/**
	 * 得到该供货商的订单总数（如果扫描后得到该批次的订单数，针对入库环节）
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getcwbsdataForCustomer")
	public @ResponseBody JSONArray getcwbsdataForCustomer(@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid) {
		JSONObject obj = new JSONObject();
		JSONArray objarr = new JSONArray();
		if (cwb.length() > 0) {
			CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
			obj.put("customername", this.customerDAO.getCustomerById(co.getCustomerid()).getCustomername());
			obj.put("size", this.cwbDAO.getCwbByCustomerid(co.getCustomerid(), this.getSessionUser().getBranchid()));
			objarr.add(obj);
		} else {

			if ((customerid != -1) && (customerid != 0)) {
				obj.put("customername", this.customerDAO.getCustomerById(customerid).getCustomername());
				obj.put("size", this.cwbDAO.getCwbByCustomerid(customerid, this.getSessionUser().getBranchid()));
			} else {
				obj.put("customername", "");
				obj.put("size", this.cwbDAO.getCwbByCustomerid(0, this.getSessionUser().getBranchid()));
			}

			objarr.add(obj);
		}
		return objarr;
	}

	/**
	 * 得到待退供应商出库的订单总数
	 *
	 * @param model
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getTGYSCKSum")
	public @ResponseBody JSONObject getTGYSCKSum() {
		JSONObject obj = new JSONObject();
		long branchid = this.getSessionUser().getBranchid();
		// Smtcount wsmtcount = this.cwbDAO.getTGYSCKbyBranchidsmt(branchid);
		String flowordertypes = FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "," + FlowOrderTypeEnum.DingDanLanJie.getValue();
		Smtcount wsmtcount = this.cwbDAO.getBackYiRukubyBranchidsmt(branchid, flowordertypes);// 于退客户入库已入库保持一致
		Smtcount ysmtcount = this.cwbDAO.getTGYSYCKsmt(branchid);
		obj.put("yps", ysmtcount.getPscount());
		obj.put("ysmh", ysmtcount.getSmhcount());
		obj.put("ysmt", ysmtcount.getSmtcount());
		obj.put("wps", wsmtcount.getPscount());
		obj.put("wsmh", wsmtcount.getSmhcount());
		obj.put("wsmt", wsmtcount.getSmtcount());
		/*
		 * long tuicunlist =
		 * this.cwbDAO.getTGYSCKbyBranchid(this.getSessionUser().getBranchid());
		 * obj.put("weirukucount", tuicunlist); obj.put("yichukucount",
		 * this.cwbDAO.getTGYSYCK(this.getSessionUser().getBranchid()));
		 */
		return obj;
	}

	/**
	 * 得到待领货的订单总数
	 *
	 * @param model
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getWeiLingHuoSum")
	public @ResponseBody JSONObject getWeiLingHuoSum(@RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid) {
		JSONObject obj = new JSONObject();
		List<CwbOrder> todayweilinghuolist = new ArrayList<CwbOrder>();// 今日待领货list
		List<CwbOrder> historyweilinghuolist = new ArrayList<CwbOrder>();// 历史待领货list
		List<CwbOrder> historyzhiliulist = new ArrayList<CwbOrder>();// 历史待领货list
		List<CwbOrder> historydaohuolist = new ArrayList<CwbOrder>();

		// 今日到货订单数
		// List<String> todaydaohuocwbs =
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime(), "");
		// List<String> todaydaohuocwbs =
		// this.operationTimeDAO.getOrderFlowLingHuoList(this.getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
		// + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime());

		// OXO: 过滤JIT类型的订单统计 by jinghui.pan on 20150804
		List<String> todaydaohuocwbs = this.operationTimeDAO.getOrderFlowLingHuoList(this.getSessionUser().getBranchid(), this.getCwbOrderTypeIdsWithoutJIT(),
				FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime());

		// 今日滞留订单数
		// List<String> todayzhiliucwbs =
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.YiShenHe.getValue()+"",
		// DateTimeUtil.getCurrentDayZeroTime(), "");
		List<String> todayzhiliucwbs = this.operationTimeDAO.getjinrizhiliu(this.getSessionUser().getBranchid(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(),
				DateTimeUtil.getCurrentDayZeroTime());
		this.logger.info("zhiliucwbs:" + todayzhiliucwbs.size());

		// 今日到货订单
		List<CwbOrder> todaydaohuolist = new ArrayList<CwbOrder>();
		if (todaydaohuocwbs.size() > 0) {
			todaydaohuolist = this.cwbDAO.getTodayWeiLingDaohuobyBranchid(this.getSessionUser().getBranchid(), this.getStrings(todaydaohuocwbs));
		}
		// 历史到货订单
		// List<CwbOrder> historydaohuolist =
		// cwbDAO.getHistoryyWeiLingDaohuobyBranchid(getSessionUser().getBranchid(),getStrings(todaydaohuocwbs));
		// List<String> historycwbs =
		// this.operationTimeDAO.getlishidaohuo(this.getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
		// + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime());

		// OXO: 过滤JIT类型的订单统计 by jinghui.pan on 20150804
		List<String> historycwbs = this.operationTimeDAO.getlishidaohuo(this.getSessionUser().getBranchid(), this.getCwbOrderTypeIdsWithoutJIT(),
				FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime());

		if (historycwbs.size() > 0) {
			historydaohuolist = this.cwbDAO.getCwbOrderByFlowordertypeAndCwbs(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), this.getStrings(historycwbs));
		}
		// 今日滞留订单
		List<CwbOrder> todayzhiliulist = new ArrayList<CwbOrder>();
		if (todayzhiliucwbs.size() > 0) {
			todayzhiliulist = this.cwbDAO.getTodayWeiLingZhiliuByWhereListformingxi(DeliveryStateEnum.FenZhanZhiLiu.getValue(), this.getSessionUser().getBranchid(), this.getStrings(todayzhiliucwbs),
					deliverid);
			this.logger.info("todayzhiliulist:" + todayzhiliulist.size());
		}
		// 历史滞留订单
		// List<CwbOrder> historyzhiliulist =
		// cwbDAO.getHistoryWeiLingZhiliuByWhereListformingxi(DeliveryStateEnum.FenZhanZhiLiu.getValue(),getSessionUser().getBranchid(),getStrings(todayzhiliucwbs),deliverid);
		List<String> historyzhiliucwbs = this.operationTimeDAO.getlishizhiliu(this.getSessionUser().getBranchid(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(),
				DateTimeUtil.getCurrentDayZeroTime());

		if (historyzhiliucwbs.size() > 0) {
			historyzhiliulist = this.cwbDAO.getHistoryWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(), this.getSessionUser().getBranchid(), this.getStrings(historyzhiliucwbs),
					deliverid);
		}

		// 今日反馈待中转失败订单-20150629新增---------------------------------------------
		List<CwbOrder> historydaizhongzhuanlist = new ArrayList<CwbOrder>();// 历史待领货list
		List<CwbOrder> todaydaizhongzhuanlist = new ArrayList<CwbOrder>();

		List<String> todayAppZhongZhuanList = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanList(DateTimeUtil.getCurrentDayZeroTime(), "", 2, this.getSessionUser().getBranchid());

		if (todayAppZhongZhuanList.size() > 0) {
			todaydaizhongzhuanlist = this.cwbDAO.getTodayWeiLingZhiliuByWhereListformingxi(DeliveryStateEnum.DaiZhongZhuan.getValue(), this.getSessionUser().getBranchid(),
					this.getStrings(todayAppZhongZhuanList), deliverid);
			this.logger.info("todaydaizhongzhuanlist:" + todaydaizhongzhuanlist.size());
		}
		// 历史待中转订单
		List<String> historyAppZhongZhuanList = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanList("", DateTimeUtil.getCurrentDayZeroTime(), 2, this.getSessionUser().getBranchid());

		if (historyAppZhongZhuanList.size() > 0) {
			historydaizhongzhuanlist = this.cwbDAO.getHistoryWeiLingZhiliuByWhereList(DeliveryStateEnum.DaiZhongZhuan.getValue(), this.getSessionUser().getBranchid(),
					this.getStrings(historyAppZhongZhuanList), deliverid);
		}

		// 今日反馈待中转失败订单----------------------------------------------

		// 今日反馈拒收审核不通过失败订单-20150629新增---------------------------------------------
		List<CwbOrder> historyjushoulist = new ArrayList<CwbOrder>();// 历史待领货list
		List<CwbOrder> todayjushoulist = new ArrayList<CwbOrder>();

		List<String> todayJuShouCwbs = this.orderBackCheckDAO.getOrderBackChecksCwbs(this.getSessionUser().getBranchid(), DateTimeUtil.getCurrentDayZeroTime(), "");

		String deliverystates = DeliveryStateEnum.JuShou.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.ShangMenJuTui.getValue();

		if (todayJuShouCwbs.size() > 0) {
			todayjushoulist = this.cwbDAO.getTodayWeiLingJuShouByWhereListformingxi(deliverystates, this.getSessionUser().getBranchid(), this.getStrings(todayJuShouCwbs), deliverid);
		}
		// 历史拒收订单
		List<String> historyJuShouCwbs = this.orderBackCheckDAO.getOrderBackChecksCwbs(this.getSessionUser().getBranchid(), "", DateTimeUtil.getCurrentDayZeroTime());

		if (historyJuShouCwbs.size() > 0) {
			historyjushoulist = this.cwbDAO.getHistoryWeiLingJuShouByWhereList(deliverystates, this.getSessionUser().getBranchid(), this.getStrings(historyJuShouCwbs), deliverid);
		}

		// 今日反馈拒收审核不通过----------------------------------------------

		List<String> linghuocwbs = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchid(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		String yilinghuocwbs = "";
		if (linghuocwbs.size() > 0) {
			yilinghuocwbs = this.getStrings(linghuocwbs);
		} else {
			yilinghuocwbs = "'--'";
		}

		todayweilinghuolist.addAll(todaydaohuolist);
		todayweilinghuolist.addAll(todayzhiliulist);
		todayweilinghuolist.addAll(todaydaizhongzhuanlist);
		todayweilinghuolist.addAll(todayjushoulist);

		historyweilinghuolist.addAll(historydaohuolist);
		historyweilinghuolist.addAll(historyzhiliulist);
		historyweilinghuolist.addAll(historydaizhongzhuanlist);
		historyweilinghuolist.addAll(historyjushoulist);

		todayweilinghuolist = this.cwbOrderService.filterCwbOrderByDeliver(todayweilinghuolist, deliverid);
		historyweilinghuolist = this.cwbOrderService.filterCwbOrderByDeliver(historyweilinghuolist, deliverid);
		obj.put("todayweilinghuocount", todayweilinghuolist.size());
		obj.put("historyweilinghuocount", historyweilinghuolist.size());
		obj.put("yilinghuo", this.cwbDAO.getYiLingHuoCountbyBranchid(yilinghuocwbs, this.getSessionUser().getBranchid(), deliverid));
		return obj;
	}

	/**
	 * 得到出库的订单总数
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getOutSum")
	public @ResponseBody JSONObject getOutSum(@RequestParam(value = "nextbranchid", required = false, defaultValue = "0") long nextbranchid,
			@RequestParam(value = "cwbstate", required = false, defaultValue = "1") int cwbstate, Model model) {
		JSONObject obj = new JSONObject();
		long branchid = this.getSessionUser().getBranchid();
		Branch b = this.branchDAO.getBranchById(branchid);
		/*
		 * if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) { cwbstate =
		 * CwbStateEnum.TuiHuo.getValue(); }
		 */
		long daizhongzhuan = 0;
		obj.put("branch", b);
		daizhongzhuan = this.cwbDAO.getDaizhongzhuanNum(branchid);
		List<Map<String, Object>> weichukudata = this.cwbDAO.getChukubyBranchid(branchid, nextbranchid, cwbstate);
		obj.put("weichukucount", weichukudata.get(0).get("count"));
		obj.put("weichukusum", weichukudata.get(0).get("sum"));
		obj.put("daizhongzhuan", daizhongzhuan);

		obj.put("yichukucount", this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNextCount(branchid, nextbranchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue()));
		List<String> cwbList = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(branchid, nextbranchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		List<CwbOrder> yisaomiaocwOrders = new ArrayList<CwbOrder>();
		if ((null != cwbList) && (cwbList.size() > 0)) {
			StringBuffer cwbs = null;
			final int batchSize = 1000;
			List<String> sublist = new ArrayList<String>();
			
			long begin = System.currentTimeMillis();
			this.logger.info("getOutSum循环开始");
			for(int i=0; i<(cwbList.size()+batchSize-1)/batchSize; i++){ // 自己分批，解决CPU过高
				cwbs = new StringBuffer();
				int upperBound = (i+1)*batchSize;
				if(upperBound > cwbList.size()) upperBound = cwbList.size();
				sublist = cwbList.subList(i*batchSize, upperBound);
				for (String cwb : sublist) {
					cwbs.append( "'" + cwb + "',");
				}
				if (cwbs.length()>0) {
//					cwbs = cwbs.substring(0, cwbs.lastIndexOf(","));
					cwbs.deleteCharAt(cwbs.length()-1);
				}
				if (cwbs.length() > 0) {
					yisaomiaocwOrders.addAll( this.cwbDAO.getCwbByCwbsByPage(cwbs.toString(), 0, Page.EXCEL_PAGE_NUMBER));
				}
			}
			this.logger.info("getOutSum循环结束，耗时 in ms："+(System.currentTimeMillis()-begin));
			obj.put("yisaomiaocwOrders", yisaomiaocwOrders);
			obj.put("customerList", this.customerDAO.getAllCustomers());
		}
		return obj;
	}

	/**
	 * 得到中转站出库的订单总数
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getChangeOutSum")
	public @ResponseBody JSONObject getChangeOutSum(@RequestParam(value = "nextbranchid", required = false, defaultValue = "0") long nextbranchid) {
		JSONObject obj = new JSONObject();
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());

		obj.put("branch", b);
		List<Map<String, Object>> weichukudata = this.cwbDAO.getZhongZhuanZhanChukubyBranchid(this.getSessionUser().getBranchid(), nextbranchid);
		obj.put("weichukucount", weichukudata.get(0).get("count"));
		obj.put("weichukusum", weichukudata.get(0).get("sum"));

		obj.put("yichukucount",
				this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNextCount(this.getSessionUser().getBranchid(), nextbranchid, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()));

		return obj;
	}

	/**
	 * 库对库出库未出库数据
	 *
	 * @param nextbranchid
	 * @param cwbstate
	 * @return
	 */
	@RequestMapping("/getkdkOutSum")
	public @ResponseBody JSONObject getkdkOutSum(@RequestParam(value = "nextbranchid", required = false, defaultValue = "0") long nextbranchid,
			@RequestParam(value = "cwbstate", required = false, defaultValue = "1") int cwbstate) {
		JSONObject obj = new JSONObject();
		long branchid = this.getSessionUser().getBranchid();
		Branch b = this.branchDAO.getBranchById(branchid);

		obj.put("branch", b);
		List<Map<String, Object>> weichukudata = this.cwbDAO.getChukubyBranchid(branchid, nextbranchid, -1);
		obj.put("weichukucount", weichukudata.get(0).get("count"));
		obj.put("weichukusum", weichukudata.get(0).get("sum"));

		obj.put("yichukucount", this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNextCount(branchid, nextbranchid, FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()));

		return obj;
	}

	/**
	 * 退货站退货出站统计数据
	 */
	@RequestMapping("/getBackBranchBackOutSum")
	public @ResponseBody JSONObject getBackBranchBackOutSum(@RequestParam(value = "nextbranchid", required = false, defaultValue = "0") long nextbranchid) {
		JSONObject obj = new JSONObject();
		long branchid = this.getSessionUser().getBranchid();

		obj.put("weichukucount", this.cwbDAO.getChukubyBranchid(branchid, nextbranchid, CwbStateEnum.TuiHuo.getValue()).get(0).get("count"));
		obj.put("yichukucount", this.cwbDAO.getYiChuKubyBranchid(branchid, nextbranchid, FlowOrderTypeEnum.TuiHuoChuZhan.getValue()));

		return obj;
	}

	/**
	 * 得到出库缺货件数的统计
	 *
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/getOutQueSum")
	public @ResponseBody JSONObject getOutQueSum(@RequestParam(value = "nextbranchid", required = false, defaultValue = "-1") long nextbranchid) {
		JSONObject obj = new JSONObject();
		obj.put("lesscwbnum", this.ypdjHandleRecordDAO.getChukuQuejianbyBranchid(this.getSessionUser().getBranchid(), nextbranchid));

		return obj;
	}

	/**
	 *
	 * @Title: getZhongZhuanOutQueSum
	 * @description 中转出库的时候，用来获取一票多件缺件数的方法
	 * @author 刘武强
	 * @date 2016年1月31日上午11:52:21
	 * @param @param nextbranchid
	 * @param @return
	 * @return JSONObject
	 * @throws
	 */
	@RequestMapping("/getZhongZhuanOutQueSum")
	public @ResponseBody JSONObject getZhongZhuanOutQueSum(@RequestParam(value = "nextbranchid", required = false, defaultValue = "-1") long nextbranchid) {
		JSONObject obj = new JSONObject();
		obj.put("lesscwbnum", this.ypdjHandleRecordDAO.getZhongZhuanChukuQuejianbyBranchid(this.getSessionUser().getBranchid(), nextbranchid));

		return obj;
	}

	/**
	 * 得到出库缺货件数的list列表
	 *
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/getOutQueList")
	public String getOutQueList(Model model, @RequestParam(value = "nextbranchid", required = false, defaultValue = "-1") long nextbranchid) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<JSONObject> quejianList = this.ypdjHandleRecordDAO.getChukuQuejianbyBranchidList(this.getSessionUser().getBranchid(), nextbranchid, 1, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		for (JSONObject obj : quejianList) {
			String transcwb = "";
			String explink = "explink";
			if (obj.getString("transcwb").indexOf("explink") > -1) {
				transcwb = obj.getString("transcwb").substring(explink.length() + 1, obj.getString("transcwb").length());
			} else if (obj.getString("transcwb").indexOf("havetranscwb") > -1) {
				transcwb = obj.getString("transcwb").split("_")[1];
			} else {
				transcwb = obj.getString("transcwb");
			}
			obj.put("transcwb", transcwb);
			obj.put("customername", this.dataStatisticsService.getQueryCustomerName(customerList, obj.getLong("customerid")));
		}
		model.addAttribute("quejianList", quejianList);
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		model.addAttribute("nextbranchid", nextbranchid);
		model.addAttribute("page", 1);

		return "pda/quejianlist";
	}

	/**
	 * 得到中转站出库缺货件数的list列表
	 *
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/getChangeOutQueList")
	public String getChangeOutQueList(Model model, @RequestParam(value = "nextbranchid", required = false, defaultValue = "-1") long nextbranchid) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<JSONObject> quejianList = this.ypdjHandleRecordDAO.getChukuQuejianbyBranchidList(this.getSessionUser().getBranchid(), nextbranchid, 1, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
		for (JSONObject obj : quejianList) {
			String transcwb = "";
			String explink = "explink";
			if (obj.getString("transcwb").indexOf("explink") > -1) {
				transcwb = obj.getString("transcwb").substring(explink.length() + 1, obj.getString("transcwb").length());
			} else if (obj.getString("transcwb").indexOf("havetranscwb") > -1) {
				transcwb = obj.getString("transcwb").split("_")[1];
			} else {
				transcwb = obj.getString("transcwb");
			}
			obj.put("transcwb", transcwb);
			obj.put("customername", this.dataStatisticsService.getQueryCustomerName(customerList, obj.getLong("customerid")));
		}
		model.addAttribute("quejianList", quejianList);
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
		model.addAttribute("nextbranchid", nextbranchid);
		model.addAttribute("page", 1);

		return "pda/quejianlist";
	}

	@RequestMapping("/getOutQueListPage")
	public @ResponseBody List<JSONObject> getOutQueListPage(Model model, @RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "nextbranchid", required = false, defaultValue = "-1") long nextbranchid) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<JSONObject> quejianList = this.ypdjHandleRecordDAO.getChukuQuejianbyBranchidList(this.getSessionUser().getBranchid(), nextbranchid, page, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		for (JSONObject obj : quejianList) {
			String transcwb = "";
			String explink = "explink";
			if (obj.getString("transcwb").indexOf("explink") > -1) {

			} else if (obj.getString("transcwb").indexOf("havetranscwb") > -1) {
				transcwb = obj.getString("transcwb").split("_")[1];
			} else {
				transcwb = obj.getString("transcwb");
			}
			obj.put("transcwb", transcwb);
			obj.put("customername", this.dataStatisticsService.getQueryCustomerName(customerList, obj.getLong("customerid")));
		}

		return quejianList;
	}

	/**
	 * 站点出站待出站数据
	 *
	 * @param deliverybranchid
	 * @return
	 */
	@RequestMapping("/getZhanDianChuZhanSum")
	public @ResponseBody JSONObject getZhanDianChuZhanSum(@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long deliverybranchid) {
		JSONObject obj = new JSONObject();
		long branchid = this.getSessionUser().getBranchid();
		obj.put("size", this.cwbDAO.getZhanDianChuZhanbyBranchid(branchid, deliverybranchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()));
		return obj;
	}

	/**
	 * 站点出站已出站数据
	 *
	 * @param deliverybranchid
	 * @return
	 */
	@RequestMapping("/getZhanDianYiChuZhanSum")
	public @ResponseBody JSONObject getZhanDianYiChuZhanSum(@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long deliverybranchid) {
		JSONObject obj = new JSONObject();
		obj.put("size", this.cwbDAO.getZhanDianYiChuZhanbyBranchid(this.getSessionUser().getBranchid(), deliverybranchid, deliverybranchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue()));
		return obj;
	}

	/**
	 * 得到待退货的订单总数
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getTuiHuoOutSum")
	public @ResponseBody JSONObject getTuiHuoOutSum(@RequestParam(value = "nextbranchid", required = false, defaultValue = "0") long nextbranchid) {
		JSONObject obj = new JSONObject();
		long branchid = this.getSessionUser().getBranchid();
		Branch b = this.branchDAO.getBranchById(branchid);
		long size = 0l;
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		if (isUseAuditTuiHuo.equals("yes")) {
			size = this.cwbDAO.getCwbOrderByFlowOrderTypeAndCurrentbranchidCount(FlowOrderTypeEnum.DingDanLanJie.getValue(), this.getSessionUser().getBranchid(), nextbranchid);
		} else {
			size += this.cwbDAO.getCwbByFlowOrderTypeAndDeliveryStateAndCurrentbranchid(FlowOrderTypeEnum.YiShenHe, DeliveryStateEnum.JuShou, this.getSessionUser().getBranchid(), nextbranchid);
			size += this.cwbDAO.getCwbByFlowOrderTypeAndDeliveryStateAndCurrentbranchid(FlowOrderTypeEnum.YiShenHe, DeliveryStateEnum.BuFenTuiHuo, this.getSessionUser().getBranchid(), nextbranchid);
			size += this.cwbDAO.getCwbByFlowOrderTypeAndDeliveryStateAndCurrentbranchid(FlowOrderTypeEnum.YiShenHe, DeliveryStateEnum.ShangMenHuanChengGong, this.getSessionUser().getBranchid(),
					nextbranchid);
			size += this.cwbDAO.getCwbByFlowOrderTypeAndDeliveryStateAndCurrentbranchid(FlowOrderTypeEnum.YiShenHe, DeliveryStateEnum.ShangMenTuiChengGong, this.getSessionUser().getBranchid(),
					nextbranchid);
			String cwbStates = CwbStateEnum.BUFENDIUSHI.getValue() + "," + CwbStateEnum.BUFENPOSUN.getValue() + "," + CwbStateEnum.WANQUANPOSUN.getValue() + "," + CwbStateEnum.TuiHuo.getValue();
			String flowordertypes = FlowOrderTypeEnum.DingDanLanJie.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
			size += this.cwbDAO.getCwbOrderByFlowOrderTypeAndDeliveryStateAndIsmpsflagCount(cwbStates, flowordertypes, this.getSessionUser().getBranchid());
		}

		obj.put("branch", b);
		obj.put("size", size);
		return obj;
	}

	/**
	 * 退货出站已出站数据
	 *
	 * @param nextbranchid
	 * @return
	 */
	@RequestMapping("/getTuiHuoYiOutSum")
	public @ResponseBody JSONObject getTuiHuoYiOutSum(@RequestParam(value = "nextbranchid", required = false, defaultValue = "0") long nextbranchid) {
		JSONObject obj = new JSONObject();
		long size = this.cwbDAO.getCwbByFlowOrderTypeAndNextbranchidAndStartbranchid(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), this.getSessionUser().getBranchid(), nextbranchid);

		obj.put("size", size);
		return obj;
	}

	/**
	 * 得到入库的订单总数
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getDaoRuSum")
	public @ResponseBody JSONObject getDaoRuSum(@RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid) {
		JSONObject obj = new JSONObject();
		long branchid = this.getSessionUser().getBranchid();
		obj.put("daiTiCount", this.cwbDAO.getDaoRubyBranchid(branchid, customerid).getOpscwbid());
		obj.put("yiTiCount", this.cwbDAO.getTiHuobyBranchid(branchid, customerid).getOpscwbid());
		return obj;
	}

	/**
	 * 得到入库的订单总数
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getInSum")
	public @ResponseBody JSONObject getInSum(@RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid,
			@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb, @RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		cwb = this.cwbOrderService.translateCwb(cwb);
		JSONObject obj = new JSONObject();
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		Map<String, Object> rukuMap = this.cwbDAO.getRukubyBranchid(this.getSessionUser().getBranchid(), b.getSitetype(), customerid, emaildate).get(0);
		obj.put("weirukucount", rukuMap.get("count"));
		obj.put("weirukusum", rukuMap.get("sum"));
		obj.put("yirukunum", this.cwbDAO.getYiRukubyBranchid(this.getSessionUser().getBranchid(), customerid, emaildate).getOpscwbid());
		return obj;
	}

	/**
	 * 得到中转站入库的订单总数
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getZhongZhuanZhanInSum")
	public @ResponseBody JSONObject getZhongZhuanZhanInSum(@RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid,
			@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb) {
		cwb = this.cwbOrderService.translateCwb(cwb);
		JSONObject obj = new JSONObject();
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		Map<String, Object> rukuMap = this.cwbDAO.getZhongZhuanZhanRukubyBranchid(this.getSessionUser().getBranchid(), b.getSitetype(), customerid).get(0);
		obj.put("weirukucount", rukuMap.get("count"));
		obj.put("weirukusum", rukuMap.get("sum"));
		obj.put("yirukunum", this.cwbDAO.getZhongZhuanZhanYiRukubyBranchid(this.getSessionUser().getBranchid(), customerid).getOpscwbid());
		return obj;
	}

	/**
	 * 得到入库缺货件数的统计
	 *
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/getInQueSum")
	public @ResponseBody JSONObject getInQueSum(@RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid,
			@RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		JSONObject obj = new JSONObject();
		obj.put("lesscwbnum", this.ypdjHandleRecordDAO.getRukuQuejianbyBranchid(this.getSessionUser().getBranchid(), customerid, emaildate));

		return obj;
	}

	/**
	 * 得到中转站入库缺货件数的统计
	 *
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/getZhongZhuanZhanInQueSum")
	public @ResponseBody JSONObject getZhongZhuanZhanInQueSum(@RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid) {
		JSONObject obj = new JSONObject();
		obj.put("lesscwbnum", this.ypdjHandleRecordDAO.getZhongZhuanZhanRukuQuejianbyBranchid(this.getSessionUser().getBranchid(), customerid));

		return obj;
	}

	/**
	 * 得到入库缺货件数的list列表
	 *
	 * @param customerid
	 * @return
	 *
	 *         1104 运单号不显示 shf 2016-01-15
	 */
	@RequestMapping("/getInQueList")
	public String getInQueList(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid,
			@RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<JSONObject> quejianList = this.ypdjHandleRecordDAO.getRukuQuejianbyBranchidList(this.getSessionUser().getBranchid(), customerid, 1, emaildate);
		for (JSONObject obj : quejianList) {
			//Hps_Concerto create  2016年6月29日 10:14:43
			String cwbstatetext="";
			String cwbordertypetext="";
			String flowordertypetext="";
			String cwb = (String)(obj.get("cwb")==null?"":obj.get("cwb"));
			if(!cwb.equals("")){
				CwbOrder cwborder = this.cwbDAO.getCwbByCwb(cwb);
				if(cwborder!=null){
			/*<td width="80" align="center" bgcolor="#f1f1f1"><%=obj.get("cwbordertype") %></td>
			<td width="80" align="center" bgcolor="#f1f1f1"><%=obj.get("cwbstatetext") %></td>
			<td width="120" align="center" bgcolor="#f1f1f1"><%=obj.get("flowordertypetext") %></td>*/
			for (CwbStateEnum  c   : CwbStateEnum.values()){
				 if(c.getValue()==cwborder.getCwbstate()){
					 cwbstatetext=c.getText();
				 }
			 } 
			long flowordertype=cwborder.getFlowordertype();
			int deliverystate = cwborder.getDeliverystate();
			try{
				if(CwbFlowOrderTypeEnum.getText(flowordertype)!=null){
					if(CwbFlowOrderTypeEnum.getText(flowordertype).getText()=="已审核"){
							flowordertypetext="审核为："+DeliveryStateEnum.getByValue(deliverystate).getText();
					 }else if(CwbFlowOrderTypeEnum.getText(flowordertype).getText()=="已反馈") {
						 flowordertypetext="反馈为："+DeliveryStateEnum.getByValue(deliverystate).getText();
						/*hps*/ }else{
							 flowordertypetext=CwbFlowOrderTypeEnum.getText(flowordertype).getText();
					 }
				}else{
					flowordertypetext="";
				}
			}catch(Exception e){
				flowordertypetext="";
			}
			cwbordertypetext = CwbOrderTypeIdEnum.getTextByValue(cwborder.getCwbordertypeid());
				}
			}
			obj.put("cwbstatetext", cwbstatetext);
			obj.put("cwbordertype", cwbordertypetext);
			obj.put("flowordertypetext", flowordertypetext);
			
			//*************
			String transcwb = "";
			String explink = "explink";
			if (obj.getString("transcwb").indexOf(explink) > -1) {
				// 2016-1-18 申鸿飞
				transcwb = obj.getString("transcwb").substring(explink.length() + 1, obj.getString("transcwb").length());
			} else if (obj.getString("transcwb").indexOf("havetranscwb") > -1) {
				transcwb = obj.getString("transcwb").split("_")[1];
			} else {
				transcwb = obj.getString("transcwb");
			}
			obj.put("transcwb", transcwb);
			obj.put("customername", this.dataStatisticsService.getQueryCustomerName(customerList, obj.getLong("customerid")));
		}
		model.addAttribute("quejianList", quejianList);
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("flowordertype", FlowOrderTypeEnum.RuKu.getValue());
		model.addAttribute("customerid", customerid);
		model.addAttribute("page", 1);
		return "pda/quejianlist";
	}

	/**
	 * 得到中转站入库缺货件数的list列表
	 *
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/getZhongZhuanZhanInQueList")
	public String getZhongZhuanZhanInQueList(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<JSONObject> quejianList = this.ypdjHandleRecordDAO.getZhongZhuanZhanRukuQuejianbyBranchidList(this.getSessionUser().getBranchid(), customerid, 1);
		for (JSONObject obj : quejianList) {
			String transcwb = "";
			String explink = "explink";
			if (obj.getString("transcwb").indexOf("explink") > -1) {
				transcwb = obj.getString("transcwb").substring(explink.length() + 1, obj.getString("transcwb").length());
			} else if (obj.getString("transcwb").indexOf("havetranscwb") > -1) {
				transcwb = obj.getString("transcwb").split("_")[1];
			} else {
				transcwb = obj.getString("transcwb");
			}
			obj.put("transcwb", transcwb);
			obj.put("customername", this.dataStatisticsService.getQueryCustomerName(customerList, obj.getLong("customerid")));
		}
		model.addAttribute("quejianList", quejianList);
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("flowordertype", FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue());
		model.addAttribute("customerid", customerid);
		model.addAttribute("page", 1);
		return "pda/quejianlist";
	}

	@RequestMapping("/getDaoHuoQueSum")
	public @ResponseBody JSONObject getDaoHuoQueSum() {
		JSONObject obj = new JSONObject();
		obj.put("lesscwbnum", this.ypdjHandleRecordDAO.getDaoHuoQuejianCount(this.getSessionUser().getBranchid()));

		return obj;
	}

	@RequestMapping("/getDaoHuoQueList")
	public @ResponseBody List<JSONObject> getDaoHuoQueList(@RequestParam(value = "page", defaultValue = "1") long page) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<JSONObject> quejianList = this.ypdjHandleRecordDAO.getDaoHuoQuejianList(this.getSessionUser().getBranchid(), 1);
		for (JSONObject obj : quejianList) {
			String transcwb = "";
			String explink = "explink";
			if (obj.getString("transcwb").indexOf("explink") > -1) {
				transcwb = obj.getString("transcwb").substring(explink.length() + 1, obj.getString("transcwb").length());
			} else if (obj.getString("transcwb").indexOf("havetranscwb") > -1) {
				transcwb = obj.getString("transcwb").split("_")[1];
			} else {
				transcwb = obj.getString("transcwb");
			}
			obj.put("transcwb", transcwb);
			obj.put("customername", this.dataStatisticsService.getQueryCustomerName(customerList, obj.getLong("customerid")));
		}
		return quejianList;
	}

	@RequestMapping("/getInQueListPage")
	public @ResponseBody List<JSONObject> getInQueListPage(Model model, @RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid, @RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<JSONObject> quejianList = this.ypdjHandleRecordDAO.getRukuQuejianbyBranchidList(this.getSessionUser().getBranchid(), customerid, page, emaildate);
		for (JSONObject obj : quejianList) {
			//Hps_Concerto create  2016年6月29日 10:14:43
			String cwbstatetext="";
			String cwbordertypetext="";
			String flowordertypetext="";
			String cwb = (String)(obj.get("cwb")==null?"":obj.get("cwb"));
			if(!cwb.equals("")){
				CwbOrder cwborder = this.cwbDAO.getCwbByCwb(cwb);
				if(cwborder!=null){
			/*<td width="80" align="center" bgcolor="#f1f1f1"><%=obj.get("cwbordertype") %></td>
			<td width="80" align="center" bgcolor="#f1f1f1"><%=obj.get("cwbstatetext") %></td>
			<td width="120" align="center" bgcolor="#f1f1f1"><%=obj.get("flowordertypetext") %></td>*/
			for (CwbStateEnum  c   : CwbStateEnum.values()){
				 if(c.getValue()==cwborder.getCwbstate()){
					 cwbstatetext=c.getText();
				 }
			 } 
			long flowordertype=cwborder.getFlowordertype();
			int deliverystate = cwborder.getDeliverystate();
			try{
				if(CwbFlowOrderTypeEnum.getText(flowordertype)!=null){
					if(CwbFlowOrderTypeEnum.getText(flowordertype).getText()=="已审核"){
							flowordertypetext="审核为："+DeliveryStateEnum.getByValue(deliverystate).getText();
					 }else if(CwbFlowOrderTypeEnum.getText(flowordertype).getText()=="已反馈") {
							flowordertypetext="反馈为："+DeliveryStateEnum.getByValue(deliverystate).getText(); 
						/*hps*/ }else{
							 flowordertypetext=CwbFlowOrderTypeEnum.getText(flowordertype).getText();
					 }
				}else{
					flowordertypetext="";
				}
			}catch(Exception e){
				flowordertypetext="";
			}
			cwbordertypetext = CwbOrderTypeIdEnum.getTextByValue(cwborder.getCwbordertypeid());
				}
			}
			obj.put("cwbstatetext", cwbstatetext);
			obj.put("cwbordertype", cwbordertypetext);
			obj.put("flowordertypetext", flowordertypetext);
			
			//*************
			String transcwb = "";
			String explink = "explink";
			if (obj.getString("transcwb").indexOf("explink") > -1) {
				transcwb = obj.getString("transcwb").substring(explink.length() + 1, obj.getString("transcwb").length());
			} else if (obj.getString("transcwb").indexOf("havetranscwb") > -1) {
				transcwb = obj.getString("transcwb").split("_")[1];
			} else {
				transcwb = obj.getString("transcwb");
			}
			obj.put("transcwb", transcwb);
			obj.put("customername", this.dataStatisticsService.getQueryCustomerName(customerList, obj.getLong("customerid")));
		}
		return quejianList;
	}

	/**
	 * 站点到货统计
	 *
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getZhanDianInSum")
	public @ResponseBody JSONObject getZhanDianInSum(@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb) {
		cwb = this.cwbOrderService.translateCwb(cwb);
		JSONObject obj = new JSONObject();
		long branchid = this.getSessionUser().getBranchid();
		Branch b = this.branchDAO.getBranchById(branchid);
		String showintowarehousedata = "no";
		try {
			showintowarehousedata = this.systemInstallDAO.getSystemInstallByName("showintowarehousedata").getValue();
		} catch (Exception e) {
			this.logger.error("分站到货时，”分站到货未到货数据是否显示库房入库的数据“系统配置获取失败");
		}

		// add by 王志宇-------未到货统计二级站出站给一级站
		String flowordertypes = FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()+","
                + FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue() + ","
				+ FlowOrderTypeEnum.DingDanLanJie.getValue();
		
		if (showintowarehousedata.equals("yes")) {
			flowordertypes += "," + FlowOrderTypeEnum.RuKu.getValue();
		}

		// 今日出库(未到货)订单数
		List<String> jinriweidaohuocwbslist = this.operationTimeDAO.getOrderFlowJinRiChuKuORRuKuListAll(this.getSessionUser().getBranchid(), flowordertypes, DateTimeUtil.getCurrentDayZeroTime());
		List<String> lishiweidaohuocwbslist = this.operationTimeDAO.getlishiweidaohuoAll(this.getSessionUser().getBranchid(), flowordertypes, DateTimeUtil.getCurrentDayZeroTime());
		// 已到货list
		List<String> yidaohuocwbs = this.operationTimeDAO.getyidaohuoByBranchid(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
				
		// List<String> yidaohuocwbs =
		// this.operationTimeDAO.getyidaohuoByBranchid(this.getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		String jinriweidaohuocwbs = "";
		if (jinriweidaohuocwbslist.size() > 0) {
			jinriweidaohuocwbs = this.getStrings(jinriweidaohuocwbslist);
		} else {
			jinriweidaohuocwbs = "'--'";
		}
		String lishiweidaohuocwbs = "";
		if (lishiweidaohuocwbslist.size() > 0) {
			lishiweidaohuocwbs = this.getStrings(lishiweidaohuocwbslist);
		} else {
			lishiweidaohuocwbs = "'--'";
		}
		String yidaohuo = "";
		if (yidaohuocwbs.size() > 0) {
			yidaohuo = this.getStrings(yidaohuocwbs);
		}else{
			yidaohuo = "'--'";
		}
		/*
		 * String yidaohuoorder = ""; if (yidaohuocwbs.size() > 0) {
		 * yidaohuoorder = this.getStrings(yidaohuocwbs); } else { yidaohuoorder
		 * = "'--'"; }
		 */

		long jinriweidaohuocount = this.cwbDAO.getJinRiWeiDaoHuoCount(flowordertypes, jinriweidaohuocwbs);
		long historyweidaohuocount = this.cwbDAO.getJinRiWeiDaoHuoCount(flowordertypes, lishiweidaohuocwbs);
		// long yidaohuocount =
		// this.cwbDAO.getJinRiWeiDaoHuoCount(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()
		// + "", this.getSessionUser().getBranchid(), yidaohuoorder);

		// long historyweidaohuocount =
		// cwbDAO.getHistoryWeiDaoHuoCount(b.getBranchid(),flowordertypes,jinriweidaohuocwbs);

		// 揽件未到站数量统计
		long lanjianweidaozhancount = this.cwbDAO.countLanJianWeiDaoZhanByBranch(branchid);// 揽件未到站
		
		//将已到货的数量统计逻辑改为和已到货明细查询的逻辑一致，防止数量和明细对应不上---刘武强
		long yidaohuocount = this.cwbDAO.getHistoryDaoHuoByBranchidForCount(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","+ FlowOrderTypeEnum.LanJianRuZhan.getValue(), this.getSessionUser().getBranchid(), yidaohuo);

																					// 货物数量统计

		obj.put("branch", b);
		obj.put("jinriweidaohuocount", jinriweidaohuocount);
		obj.put("historyweidaohuocount", historyweidaohuocount);
		obj.put("lanjianweidaozhancount", lanjianweidaozhancount);
		//obj.put("yidaohuonum", this.cwbDAO.getYiDaohuobyBranchid(this.getSessionUser().getBranchid()).getOpscwbid());
		obj.put("yidaohuonum", yidaohuocount);
		return obj;
	}

	/**
	 * 得到退货站入库的订单总数
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getBackInSum")
	public @ResponseBody JSONObject getBackInSum(@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb) {
		cwb = this.cwbOrderService.translateCwb(cwb);
		JSONObject obj = new JSONObject();
		long branchid = this.getSessionUser().getBranchid();
		/*
		 * obj.put("weirukucount",
		 * this.cwbDAO.getBackRukubyBranchid(branchid).get(0).get("count"));
		 * obj.put("yirukucount",
		 * this.cwbDAO.getBackYiRukubyBranchid(branchid));
		 */
		// Smtcount wsmtcount = this.cwbDAO.getBackRukubyBranchidsmt(branchid);
		// 增加订单拦截后的统计数目===LX
		Smtcount wsmtcount = this.cwbDAO.getBackRukubyBranchidsmtAll(branchid);
		Smtcount ysmtcount = this.cwbDAO.getBackYiRukubyBranchidsmt(branchid, String.valueOf(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()));
		Smtcount wpscount = this.cwbDAO.getBackRukubyBranchidAndwpssmtAll(branchid);

		obj.put("yps", ysmtcount.getPscount());
		obj.put("ysmh", ysmtcount.getSmhcount());
		obj.put("ysmt", ysmtcount.getSmtcount());
		// 新增一票多件处理时【订单拦截】订单统计到退后站待入库（未入库）明细
		/*
		 * Smtcount wsmtInterceptcount =
		 * this.cwbDAO.getBackRukubyInterceptsmt(branchid); long pscount =
		 * wsmtcount.getPscount() + wsmtInterceptcount.getPscount();
		 */
		obj.put("wps", wpscount.getPscount());
		obj.put("wsmh", wsmtcount.getSmhcount());
		obj.put("wsmt", wsmtcount.getSmtcount());
		return obj;
	}

	/**
	 * 得到退货站入库的订单总数
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getBackAndChangeInSum")
	public @ResponseBody JSONObject getBackAndChangeInSum(@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb) {
		cwb = this.cwbOrderService.translateCwb(cwb);
		JSONObject obj = new JSONObject();
		String tbranchids = "-1";
		String zbranchids = "-1";
		List<Branch> tbranchlist = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.TuiHuo.getValue());
		List<Branch> zbranchlist = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhongZhuan.getValue());
		if ((tbranchlist != null) && (tbranchlist.size() > 0)) {
			for (Branch branch : tbranchlist) {
				tbranchids += "," + branch.getBranchid();
			}
		}
		if ((zbranchlist != null) && (zbranchlist.size() > 0)) {
			for (Branch branch : zbranchlist) {
				zbranchids += "," + branch.getBranchid();
			}
		}

		Smtcount wsmtcount = this.cwbDAO.getBackAndChangeRukubyBranchids(tbranchids, FlowOrderTypeEnum.TuiHuoChuZhan.getValue());
		Smtcount ysmtcount = this.cwbDAO.getBackAndChangeYiRukubyBranchids(tbranchids, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue());

		Smtcount zwsmtcount = this.cwbDAO.getBackAndChangeRukubyBranchids(zbranchids, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		Smtcount zysmtcount = this.cwbDAO.getBackAndChangeYiRukubyBranchids(zbranchids, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue());

		obj.put("yps", ysmtcount.getPscount());
		obj.put("ysmh", ysmtcount.getSmhcount());
		obj.put("ysmt", ysmtcount.getSmtcount());
		obj.put("wps", wsmtcount.getPscount());
		obj.put("wsmh", wsmtcount.getSmhcount());
		obj.put("wsmt", wsmtcount.getSmtcount());
		obj.put("zyps", zysmtcount.getPscount());
		obj.put("zysmh", zysmtcount.getSmhcount());
		obj.put("zysmt", zysmtcount.getSmtcount());
		obj.put("zwps", zwsmtcount.getPscount());
		obj.put("zwsmh", zwsmtcount.getSmhcount());
		obj.put("zwsmt", zwsmtcount.getSmtcount());
		return obj;
	}

	/**
	 * 得到入库的订单总数
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	/*
	 * @RequestMapping("/getInSumDetail") public @ResponseBody JSONObject
	 * getInSumDetail
	 * (@RequestParam(value="startbranchid",required=false,defaultValue="0")long
	 * startbranchid){ JSONObject obj = new JSONObject(); long branchid =
	 * getSessionUser().getBranchid(); Branch b =
	 * branchDAO.getBranchById(branchid); obj.put("branch", b); obj.put("size",
	 * cwbDAO.getRukubyBranchid(branchid, startbranchid,-1)); return obj; }
	 */

	@RequestMapping("/getswitchbyparam/{parm}")
	public @ResponseBody JSONObject getswitchbyparam(Model model, @PathVariable("parm") String parm) {
		JSONObject obj = new JSONObject();
		if (parm.equals("fzdh")) {
			obj.put("switchstate", this.switchDAO.getSwitchBySwitchname(SwitchEnum.DaoHuoFengBao.getText()).getState());
		} else if (parm.equals("plfk")) {
			obj.put("switchstate", this.switchDAO.getSwitchBySwitchname(SwitchEnum.PiLiangFanKui.getText()).getState());
		}

		return obj;
	}

	/**
	 * 供货商拒收返库
	 *
	 * @param model
	 * @param request
	 * @param respStore
	 * @param cwb
	 * @param deliverid
	 * @param requestbatchno
	 * @return
	 */
	@RequestMapping("/supplierbacksuccess/{cwb}")
	public @ResponseBody ExplinkResponse supplierbacksuccess(@PathVariable("cwb") String cwb, HttpServletRequest request) {
		String scancwb = cwb;
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = this.cwbOrderService.supplierBackSuccess(this.getSessionUser(), cwb, scancwb, this.getSessionUser().getUserid());
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", cwbOrder);
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		return explinkResponse;
	}

	/**
	 * 退货出库批量功能======================================
	 *
	 * @param model
	 * @param cwbs
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param SuccessCount
	 * @return
	 */
	@RequestMapping("/cwbexportwarhouseBatch")
	public String cwbexportwarhouseBatch(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag) {
		BatchCount batchCount = new BatchCount(0, 0, 0);
		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		List<JSONObject> objList = new ArrayList<JSONObject>();
		this.processBatchExportHouse(cwbs, branchid, driverid, truckid, confirmflag, batchCount, cList, objList);
		model.addAttribute("objList", objList);

		Branch localbranch = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		int cwbstate = CwbStateEnum.PeiShong.getValue();
		//if (localbranch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
		//	cwbstate = CwbStateEnum.TuiHuo.getValue();
		//}

		List<CwbOrder> weiChuKuList = this.cwbDAO.getChukuForCwbOrderByBranchid(localbranch.getBranchid(), cwbstate, 1, branchid);
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		//if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
		//	cwbstate = CwbStateEnum.TuiHuo.getValue();
		//}
		List<Map<String, Object>> cwbObj = this.cwbDAO.getChukubyBranchid(this.getSessionUser().getBranchid(), branchid, cwbstate);

		List<String> cwbyichukuList = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(b.getBranchid(), branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		String yicwbs = "";
		if (cwbyichukuList.size() > 0) {
			yicwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbyichukuList);
		} else {
			yicwbs = "'--'";
		}
		List<CwbOrder> yiChuKuList = this.cwbDAO.getCwbByCwbsPage(1, yicwbs, Page.DETAIL_PAGE_NUMBER);

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未出库明细
		List<CwbDetailView> weichukuViewlist = this.getcwbDetail(weiChuKuList, cList, showCustomerjSONArray, null, 0);
		// 已出库明细
		List<CwbDetailView> yichukuViewlist = this.getcwbDetail(yiChuKuList, cList, showCustomerjSONArray, null, 0);

		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser(), BranchEnum.TuiHuo);
		model.addAttribute("branchList", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("customerList", cList);
		model.addAttribute("weiChuKuList", weichukuViewlist);// 待出库数据
		model.addAttribute("yiChuKuList", yichukuViewlist);
		model.addAttribute("count", cwbObj.get(0).get("count"));// 待出库总数
		model.addAttribute("sum", cwbObj.get(0).get("sum"));// 待出库件数总数
		model.addAttribute("yichukucount", cwbyichukuList.size());
		model.addAttribute("lesscwbnum", this.ypdjHandleRecordDAO.getChukuQuejianbyBranchid(this.getSessionUser().getBranchid(), branchid));// 缺货件数

		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + batchCount.getThissuccess() + "单，异常" + (batchCount.getAllcwbnum() - batchCount.getThissuccess()) + "单";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("showCustomerSign", showCustomerSign);
		return "pda/exportwarehouseBatch";
	}
	
	/**
	 * 分拣库出库出库批量功能======================================
	 *
	 * @param model
	 * @param cwbs
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param SuccessCount
	 * @return
	 */
	@RequestMapping("/exportwarhouseBatch")
	public String exportwarhouseBatch(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag) {
		BatchCount batchCount = new BatchCount(0, 0, 0);
		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		List<JSONObject> objList = new ArrayList<JSONObject>();
		this.processBatchExportHouse(cwbs, branchid, driverid, truckid, confirmflag, batchCount, cList, objList);
		model.addAttribute("objList", objList);

		Branch localbranch = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		int cwbstate = CwbStateEnum.PeiShong.getValue();

		List<CwbOrder> weiChuKuList = this.cwbDAO.getChukuForCwbOrderByBranchid(localbranch.getBranchid(), cwbstate, 1, branchid);
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		List<Map<String, Object>> cwbObj = this.cwbDAO.getChukubyBranchid(this.getSessionUser().getBranchid(), branchid, cwbstate);

		List<String> cwbyichukuList = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(b.getBranchid(), branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		String yicwbs = "";
		if (cwbyichukuList.size() > 0) {
			yicwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbyichukuList);
		} else {
			yicwbs = "'--'";
		}
		List<CwbOrder> yiChuKuList = this.cwbDAO.getCwbByCwbsPage(1, yicwbs, Page.DETAIL_PAGE_NUMBER);

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未出库明细
		List<CwbDetailView> weichukuViewlist = this.getcwbDetail(weiChuKuList, cList, showCustomerjSONArray, null, 0);
		// 已出库明细
		List<CwbDetailView> yichukuViewlist = this.getcwbDetail(yiChuKuList, cList, showCustomerjSONArray, null, 0);
		
		//Modified by leoliao at 2016-07-11 把已停用的站点过滤
		List<Branch> listNextBranch = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());
		List<Branch> bList = this.filterNoEffectBranch(listNextBranch);
		//Modified end
		
		model.addAttribute("branchList", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("customerList", cList);
		model.addAttribute("weiChuKuList", weichukuViewlist);// 待出库数据
		model.addAttribute("yiChuKuList", yichukuViewlist);
		model.addAttribute("count", cwbObj.get(0).get("count"));// 待出库总数
		model.addAttribute("sum", cwbObj.get(0).get("sum"));// 待出库件数总数
		model.addAttribute("yichukucount", cwbyichukuList.size());
		model.addAttribute("lesscwbnum", this.ypdjHandleRecordDAO.getChukuQuejianbyBranchid(this.getSessionUser().getBranchid(), branchid));// 缺货件数

		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + batchCount.getThissuccess() + "单，异常" + (batchCount.getAllcwbnum() - batchCount.getThissuccess()) + "单";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("showCustomerSign", showCustomerSign);
		return "pda/exportwarhouseBatch";
	}
	

	private void processBatchExportHouse(String cwbs, long branchid, long driverid, long truckid, long confirmflag, BatchCount batchCount, List<Customer> cList, List<JSONObject> objList) {
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			batchCount.setAllcwbnum(batchCount.getAllcwbnum() + 1);
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.outWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid, 0, confirmflag == 1, "", "", 0, false, false);
				this.tpsCwbFlowService.save(cwbOrder, scancwb, FlowOrderTypeEnum.ChuKuSaoMiao,this.getSessionUser().getBranchid(),null,true,null,null);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				for (Customer c : cList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
				batchCount.setThissuccess(batchCount.getThissuccess() + 1);
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);

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

			}
			objList.add(obj);
		}
	}

	/**
	 * 中转站出库批量功能======================================
	 *
	 * @param model
	 * @param cwbs
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param SuccessCount
	 * @return
	 */
	@RequestMapping("/cwbchangeoutwarhouseBatch")
	public String cwbchangeoutwarhouseBatch(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag) {
		long thissuccess = 0;
		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		List<JSONObject> objList = new ArrayList<JSONObject>();
		long allcwbnum = 0;
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			allcwbnum++;
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.changeoutWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid, 0, confirmflag == 1, "", "", 0, false, false);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				for (Customer c : cList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
				thissuccess++;
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);

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

			}
			objList.add(obj);
		}
		model.addAttribute("objList", objList);

		List<CwbOrder> weiChuKuList = this.cwbDAO.getZhongZhuanZhanChukuForCwbOrderByBranchid(this.getSessionUser().getBranchid(), 1, branchid);
		List<Map<String, Object>> cwbObj = this.cwbDAO.getZhongZhuanZhanChukubyBranchid(this.getSessionUser().getBranchid(), branchid);

		List<String> cwbyichukuList = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(this.getSessionUser().getBranchid(), branchid,
				FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
		String yicwbs = "";
		if (cwbyichukuList.size() > 0) {
			yicwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbyichukuList);
		} else {
			yicwbs = "'--'";
		}
		List<CwbOrder> yiChuKuList = this.cwbDAO.getCwbByCwbsPage(1, yicwbs, Page.DETAIL_PAGE_NUMBER);

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		// 未出库明细
		List<CwbDetailView> weichukuViewlist = this.getcwbDetail(weiChuKuList, cList, showCustomerjSONArray, null, 0);
		// 已出库明细
		List<CwbDetailView> yichukuViewlist = this.getcwbDetail(yiChuKuList, cList, showCustomerjSONArray, null, 0);

		//Modified by leoliao at 2016-07-11 把已停用的站点过滤
		List<Branch> listNextBranch = this.cwbOrderService.getNextPossibleBranches(this.getSessionUser());
		List<Branch> bList = this.filterNoEffectBranch(listNextBranch);
		//Modified end
		
		model.addAttribute("branchList", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("customerList", cList);
		model.addAttribute("weiChuKuList", weichukuViewlist);// 待出库数据
		model.addAttribute("yiChuKuList", yichukuViewlist);
		model.addAttribute("count", cwbObj.get(0).get("count"));// 待出库总数
		model.addAttribute("sum", cwbObj.get(0).get("sum"));// 待出库件数总数
		model.addAttribute("yichukucount", cwbyichukuList.size());
		model.addAttribute("lesscwbnum", this.ypdjHandleRecordDAO.getChukuQuejianbyBranchid(this.getSessionUser().getBranchid(), branchid));// 缺货件数

		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + thissuccess + "单，异常" + (allcwbnum - thissuccess) + "单";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("showCustomerSign", showCustomerSign);
		return "pda/changeexportwarehouseBatch";
	}

	/**
	 * 库房出库 批量 得到未出库 list
	 */
	@RequestMapping("/getexportwarehousebatchweirukulist")
	public @ResponseBody List<CwbDetailView> getexportwarehousebatchweirukulist(@RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "branchid", defaultValue = "0") long branchid) {
		Branch localbranch = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		int cwbstate = CwbStateEnum.PeiShong.getValue();
		if (localbranch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
			cwbstate = CwbStateEnum.TuiHuo.getValue();
		}
		List<CwbOrder> weirukulist = this.cwbDAO.getChukuForCwbOrderByBranchid(localbranch.getBranchid(), cwbstate, page, branchid);

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(weirukulist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;
	}

	/**
	 * 库房出库 批量 得到已出库 list
	 */
	@RequestMapping("/getexportwarehousebatchyirukulist")
	public @ResponseBody List<CwbDetailView> getexportwarehousebatchyirukulist(@RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "branchid", defaultValue = "0") long branchid) {
		List<CwbOrder> yirukulist = this.cwbDAO.getYiChuKubyBranchidList(this.getSessionUser().getBranchid(), branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), page);
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 退货站出站
		List<CwbDetailView> weidaohuoViewlist = this.getcwbDetail(yirukulist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, null, 0);
		return weidaohuoViewlist;

	}

	/**
	 * 标签打印功能列表
	 *
	 * @param model
	 * @param cwbs
	 * @param emaildateid
	 * @return
	 */
	@RequestMapping("/cwblableprint")
	public String cwblableprint(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "emaildateid", required = true, defaultValue = "0") long emaildateid) {
		model.addAttribute("customerList", this.customerDAO.getAllIsAutoProductcwbCustomers());
		model.addAttribute("branchList", this.branchDAO.getAllBranches());
		List<CwbOrder> clist = new ArrayList<CwbOrder>();
		if ((cwbs.length() > 0) || (emaildateid != 0)) {
			if (cwbs.length() > 0) {
				String quot = "'", quotAndComma = "',";
				StringBuffer cwbstr = new StringBuffer();
				for (String cwbStr : cwbs.split("\r\n")) {
					if (cwbStr.trim().length() == 0) {
						continue;
					}
					cwbStr = cwbStr.trim();
					cwbstr = cwbstr.append(quot).append(cwbStr).append(quotAndComma);
				}
				clist = this.cwbDAO.getCwbByCwbs(cwbstr.substring(0, cwbstr.length() - 1));
			}
			if (emaildateid != 0) {
				clist = this.cwbDAO.getCwbsByEmailDateId(emaildateid);
			}
		}
		model.addAttribute("clist", clist);
		return "cwblableprint/cwblableprintlist";
	}

	@RequestMapping("/cwblableprint_xhm")
	public String cwblableprint_xhm(Model model, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}

		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}
		List<CwbOrder> cwbList = this.cwbDAO.getCwbByCwbs(cwbs);

		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("branchlist", this.branchDAO.getAllBranches());
		model.addAttribute("userlist", this.userDAO.getAllUser());

		model.addAttribute("cwbList", cwbList);
		return "cwblableprint/cwblableprint_xhm";
	}

	/**
	 * 根据供订单查询对应的发货批次
	 *
	 * @param model
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/getEmaildateid/{cwb}")
	public @ResponseBody JSONObject getEmaildateid(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb) {
		String scancwb = cwb;
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		EmailDate e = new EmailDate();
		if (null == co) {
			e.setEmaildatetime("正确单号");
		} else {
			e = this.emaildateDAO.getEmailDateById(co.getEmaildateid());
		}
		JSONObject obj = new JSONObject();
		obj.put("cwb", cwb);
		// data[i].emaildatetime+(data[i].state==0?"（未到货）":"")+" "+
		// data[i].customername+"_"+data[i].warehousename+"_"+data[i].areaname
		obj.put("emaildatename", e.getEmaildatetime());
		return obj;
	}

	/**
	 * 根据供货商切换供货商对应的发货批次
	 *
	 * @param model
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/updateEmaildateid")
	public @ResponseBody List<EmailDate> updateEmaildateid(Model model, @RequestParam(value = "customerid", defaultValue = "0") long customerid) {
		return this.emaildateDAO.getEmailDateByCustomerid(customerid);
	}

	@ExceptionHandler(CwbException.class)
	public @ResponseBody ExplinkResponse handleCwbException(CwbException ex, HttpServletRequest request) {
		this.logger.error("系统异常", ex);
		CwbOrder co = this.cwbDAO.getCwbByCwb(ex.getCwb());
		this.exceptionCwbDAO.createExceptionCwbScan(ex.getCwb(), ex.getFlowordertye(), ex.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
				co == null ? 0 : co.getCustomerid(), 0, 0, 0, "", ex.getCwb());
		ExplinkResponse explinkResponse = new ExplinkResponse(ex.getError().getValue() + "", ex.getMessage(), null);
		// 添加异常报声.
		boolean existExceptionWav = this.addExceptionWav(request, ex, explinkResponse, co);
		String wavPath = null;
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
		} else {
			if (!existExceptionWav) {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
			}
		}
		explinkResponse.addShortWav(wavPath);

		return explinkResponse;
	}

	private boolean addExceptionWav(HttpServletRequest request, CwbException ex, ExplinkResponse explinkResponse, CwbOrder co) {
		boolean result = false;
		// 添加无此单号声音.
		result |= this.addNoOrderWav(request, ex, explinkResponse);
		// 添加货物类型声音.
		this.addGoodsTypeWaveJSON(request, co, explinkResponse);
		// 添加重复入库声音.
		result |= this.addRepeatInputWav(request, explinkResponse, ex);
		// 添加重复出库声音.
		result |= this.addRepeatOutputWav(request, explinkResponse, ex);
		// 添加到错货声音.
		result |= this.addArrivedWrongGoodsWav(request, ex, explinkResponse, co);

		return result;
	}

	private boolean addArrivedWrongGoodsWav(HttpServletRequest request, CwbException ex, ExplinkResponse explinkResponse, CwbOrder co) {
		CwbFlowOrderTypeEnum fromstate = CwbFlowOrderTypeEnum.getText(ex.getFlowordertye());
		boolean cond1 = CwbFlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.equals(fromstate);
		boolean cond2 = ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR.equals(ex.getError());
		if (cond1 && cond2) {
			explinkResponse.addLongWav(this.getErrorWavFullPath(request, WavFileName.DCH));
			return true;
		}
		return false;
	}

	private boolean addRepeatOutputWav(HttpServletRequest request, ExplinkResponse explinkResponse, CwbException ex) {
		ExceptionCwbErrorTypeEnum error = ex.getError();
		if (ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU.equals(error)) {
			explinkResponse.addLongWav(this.getErrorWavFullPath(request, CwbOrderPDAEnum.CHONG_FU_CHU_KU.getVediourl()));
			return true;
		}
		return false;
	}

	private boolean addRepeatInputWav(HttpServletRequest request, ExplinkResponse explinkResponse, CwbException ex) {
		ExceptionCwbErrorTypeEnum error = ex.getError();
		if (ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU.equals(error)) {
			explinkResponse.addLongWav(this.getErrorWavFullPath(request, CwbOrderPDAEnum.CHONG_FU_RU_KU.getVediourl()));
			return true;
		}
		return false;
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody ExplinkResponse handleException(Exception ex, HttpServletRequest request) {
		this.logger.error("系统异常", ex);
		ExplinkResponse explinkResponse = new ExplinkResponse("000001", ex.getMessage(), null);
		String wavPath = null;
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
		} else {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}
		explinkResponse.addShortWav(wavPath);

		return explinkResponse;
	}

	/**
	 * 入库、到货（明细）、领货（明细）功能的导出数据功能
	 *
	 * @param model
	 * @param response
	 * @param request
	 * @param cwbs
	 */

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") final String cwbs) {

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据

			final String sql = this.cwbDAO.getSQLExportKeFu(cwbs);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = PDAController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = PDAController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = PDAController.this.branchDAO.getAllBranches();
					final List<Common> commonList = PDAController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = PDAController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = PDAController.this.remarkDAO.getRemarkByCwbs(cwbs);
					final Map<String, Map<String, String>> remarkMap = PDAController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = PDAController.this.reasonDAO.getAllReason();
					PDAController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								// sheet.setColumnWidth(i, (short)
								// (5000));
								// //设置列宽
								Object a = PDAController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = PDAController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : PDAController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : PDAController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : PDAController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
							 * gotoClassAuditingDAO .getGotoClassAuditingByGcaid
							 * (deliveryState .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0) {
							 * ispayup = "是"; } cwbspayupidMap.put(deliveryState
							 * .getCwb(), ispayup); }
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
					 * Object a = exportService.setObjectA(cloumnName5, rs, i ,
					 * uList
					 * ,cMap,bList,commonList,ds,allTime,cWList,remarkMap,reasonList
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

	/**
	 * 查询订单的配送结果
	 *
	 * @param cwb
	 * @return
	 */
	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = this.deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	public String getStrings(List<String> strArr) {
		String strs = "";
		if (strArr.size() > 0) {
			for (String str : strArr) {
				strs += "'" + str + "',";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	// 2013-10-09 5196版本临时去掉到货明细、到货批量页面中的“出库时间”
	/*
	 * public List<CwbOrderView> getCwbOrderView(List<CwbOrder>
	 * clist,List<Customer> customerList,List<CustomWareHouse>
	 * customerWareHouseList, List<Branch> branchList,List<User>
	 * userList,List<Reason> reasonList,List<Remark> remarkList){
	 * List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * if(clist.size()>0){ for(CwbOrder c: clist){ CwbOrderView cwbOrderView =
	 * new CwbOrderView();
	 *
	 * cwbOrderView.setCwb(c.getCwb());
	 * cwbOrderView.setEmaildate(c.getEmaildate());
	 * cwbOrderView.setCarrealweight(c.getCarrealweight());
	 * cwbOrderView.setCarsize(c.getCarsize());
	 * cwbOrderView.setSendcarnum(c.getSendcarnum());
	 * cwbOrderView.setCwbprovince(c.getCwbprovince());
	 * cwbOrderView.setCwbcity(c.getCwbcity());
	 * cwbOrderView.setCwbcounty(c.getCwbcounty());
	 * cwbOrderView.setConsigneeaddress(c.getConsigneeaddress());
	 * cwbOrderView.setConsigneename(c.getConsigneename());
	 * cwbOrderView.setConsigneemobile(c.getConsigneemobile());
	 * cwbOrderView.setConsigneephone(c.getConsigneephone());
	 * cwbOrderView.setConsigneepostcode(c.getConsigneepostcode());
	 * cwbOrderView.setResendtime(c.getResendtime()==null?"":c.getResendtime());
	 *
	 * cwbOrderView.setCustomerid(c.getCustomerid());
	 * cwbOrderView.setCustomername
	 * (dataStatisticsService.getQueryCustomerName(customerList,
	 * c.getCustomerid()));//供货商的名称 String customwarehouse =
	 * dataStatisticsService
	 * .getQueryCustomWareHouse(customerWareHouseList,Long.parseLong
	 * (c.getCustomerwarehouseid()));
	 * cwbOrderView.setCustomerwarehousename(customwarehouse);
	 * cwbOrderView.setInhouse
	 * (dataStatisticsService.getQueryBranchName(branchList,
	 * Integer.parseInt(c.getCarwarehouse
	 * ()==""?"0":c.getCarwarehouse())));//入库仓库
	 * cwbOrderView.setCurrentbranchname
	 * (dataStatisticsService.getQueryBranchName(branchList,
	 * c.getCurrentbranchid()));//当前所在机构名称
	 * cwbOrderView.setStartbranchname(dataStatisticsService
	 * .getQueryBranchName(branchList, c.getStartbranchid()));//上一站机构名称
	 * cwbOrderView
	 * .setNextbranchname(dataStatisticsService.getQueryBranchName(branchList,
	 * c.getNextbranchid()));//下一站机构名称
	 * cwbOrderView.setDeliverybranch(dataStatisticsService
	 * .getQueryBranchName(branchList, c.getDeliverybranchid()));//配送站点
	 * cwbOrderView
	 * .setDelivername(dataStatisticsService.getQueryUserName(userList,
	 * c.getDeliverid())); cwbOrderView.setRealweight(c.getCarrealweight());
	 * cwbOrderView.setCwbremark(c.getCwbremark());
	 * cwbOrderView.setReceivablefee(c.getReceivablefee());
	 * cwbOrderView.setCaramount(c.getCaramount());
	 * cwbOrderView.setPaybackfee(c.getPaybackfee());
	 *
	 * DeliveryState deliverystate =
	 * dataStatisticsService.getDeliveryByCwb(c.getCwb());
	 * cwbOrderView.setPaytype
	 * (dataStatisticsService.getPayWayType(c.getCwb(),deliverystate));//新支付方式
	 * cwbOrderView
	 * .setPaytype_old(dataStatisticsService.getOldPayWayType(c.getPaywayid
	 * ()));//原支付方式 cwbOrderView.setRemark1(c.getRemark1());
	 * cwbOrderView.setRemark2(c.getRemark2());
	 * cwbOrderView.setRemark3(c.getRemark3());
	 * cwbOrderView.setRemark4(c.getRemark4());
	 * cwbOrderView.setRemark5(c.getRemark5());
	 * cwbOrderView.setPackagecode(c.getPackagecode());
	 * cwbOrderView.setFlowordertype(c.getFlowordertype());
	 * cwbOrderView.setReturngoodsremark
	 * (orderFlowDAO.getOrderFlowByCwbAndFlowordertype(c.getCwb(),
	 * FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()).getComment()); String
	 * currentBranch =dataStatisticsService.getQueryBranchName(branchList,
	 * c.getCurrentbranchid());
	 * cwbOrderView.setCurrentbranchname(currentBranch);
	 *
	 * Date ruku =orderFlowDAO.getOrderFlowByCwbAndFlowordertype(c.getCwb(),
	 * FlowOrderTypeEnum.RuKu.getValue()).getCredate(); Date chukusaomiao
	 * =orderFlowDAO.getOrderFlowByCwbAndFlowordertype(c.getCwb(),
	 * FlowOrderTypeEnum.ChuKuSaoMiao.getValue()).getCredate(); //到货扫描 OrderFlow
	 * daohuosaomiao =
	 * orderFlowDAO.getOrderFlowByCwbAndFlowordertype(c.getCwb(),
	 * FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
	 * if(daohuosaomiao.getCwb()==null){ daohuosaomiao =
	 * orderFlowDAO.getOrderFlowByCwbAndFlowordertype(c.getCwb(),
	 * FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()); } Date
	 * fenzhanlinghuo
	 * =orderFlowDAO.getOrderFlowByCwbAndFlowordertype(c.getCwb(),
	 * FlowOrderTypeEnum.FenZhanLingHuo.getValue()).getCredate(); Date yifankui
	 * =orderFlowDAO.getOrderFlowByCwbAndFlowordertype(c.getCwb(),
	 * FlowOrderTypeEnum.YiFanKui.getValue()).getCredate(); Date zuixinxiugai
	 * =dataStatisticsService.getOrderFlowByCwb(c.getCwb()).getCredate(); Date
	 * yishenhe=orderFlowDAO.getOrderFlowByCwbAndFlowordertype(c.getCwb(),
	 * FlowOrderTypeEnum.YiShenHe.getValue()).getCredate();
	 * cwbOrderView.setAuditstate(yishenhe==null?0:1);//审核状态
	 * cwbOrderView.setInstoreroomtime(ruku!=null?sdf.format(ruku):"");//入库时间
	 * cwbOrderView
	 * .setOutstoreroomtime(chukusaomiao!=null?sdf.format(chukusaomiao
	 * ):"");//出库时间
	 * cwbOrderView.setInSitetime(daohuosaomiao.getCredate()!=null?sdf
	 * .format(daohuosaomiao.getCredate()):"");//到站时间 long currentbranchid =
	 * daohuosaomiao
	 * .getFloworderdetail()!=null?JSONObject.fromObject(JSONObject.
	 * fromObject(daohuosaomiao
	 * .getFloworderdetail()).getString("cwbOrder")).getLong
	 * ("currentbranchid"):0; Branch thisbranch =
	 * branchDAO.getBranchByBranchid(currentbranchid); String branchname =
	 * thisbranch!=null?thisbranch.getBranchname():"";
	 * cwbOrderView.setInSiteBranchname(branchname);
	 * cwbOrderView.setPickGoodstime
	 * (fenzhanlinghuo!=null?sdf.format(fenzhanlinghuo):"");//小件员领货时间
	 * cwbOrderView.setGobacktime(yifankui!=null?sdf.format(yifankui):"");//反馈时间
	 * cwbOrderView
	 * .setGoclasstime(yishenhe==null?"":sdf.format(yishenhe));//归班时间
	 * cwbOrderView
	 * .setNowtime(zuixinxiugai!=null?sdf.format(zuixinxiugai):"");//最新修改时间
	 * cwbOrderView.setBackreason(c.getBackreason());
	 * cwbOrderView.setLeavedreasonStr
	 * (dataStatisticsService.getQueryReason(reasonList,
	 * c.getLeavedreasonid()));//滞留原因
	 * cwbOrderView.setOrderResultType(c.getDeliverid());
	 * cwbOrderView.setPodremarkStr(
	 * dataStatisticsService.getQueryReason(reasonList,
	 * dataStatisticsService.getDeliveryStateByCwb
	 * (c.getCwb()).getPodremarkid()));// 配送结果备注
	 * cwbOrderView.setCartype(c.getCartype());
	 * cwbOrderView.setCwbdelivertypeid(c.getCwbdelivertypeid());
	 * cwbOrderView.setInwarhouseremark
	 * (exportService.getInwarhouseRemarks(remarkList
	 * ).get(c.getCwb())==null?"":exportService
	 * .getInwarhouseRemarks(remarkList).
	 * get(c.getCwb()).get(ReasonTypeEnum.RuKuBeiZhu.getText()));
	 * cwbOrderView.setCwbordertypeid(c.getCwbordertypeid()+"");//订单类型
	 *
	 * cwbOrderViewList.add(cwbOrderView);
	 *
	 * } } return cwbOrderViewList; }
	 */

	/**
	 * 进入库存盘点功能，开始往库存明细表产生数据
	 *
	 * @param model
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/takestock")
	public String takestock(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb) {
		List<Customer> cList = this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist", cList);
		this.cwbOrderService.creStock(this.getSessionUser());
		List<CwbOrder> kucunlist = this.cwbOrderService.getkucunList(this.getSessionUser());
		List<CwbOrder> linghuokucunlist = this.cwbOrderService.getlinghuokucunlist(this.getSessionUser());

		model.addAttribute("kucunlist", kucunlist);
		model.addAttribute("linghuokucunlist", linghuokucunlist);
		return "pda/takestock";
	}

	/**
	 * 得到盘点数
	 *
	 * @return
	 */
	@RequestMapping("/getStockSum")
	public @ResponseBody JSONObject getStockSum() {
		JSONObject obj = new JSONObject();
		long kucunnum = this.cwbOrderService.getkucunList(this.getSessionUser()).size();
		long linghuokucunnum = this.cwbOrderService.getlinghuokucunlist(this.getSessionUser()).size();

		obj.put("kucunnum", kucunnum);
		obj.put("linghuokucunnum", linghuokucunnum);
		return obj;
	}

	/**
	 * 库存盘点功能
	 *
	 * @param model
	 * @param cwbs
	 * @return
	 */
	@RequestMapping("/cwbtakestock")
	public @ResponseBody JSONObject cwbtakestock(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb) {
		JSONObject obj = new JSONObject();
		try {
			StockResult stockResult = this.cwbOrderService.stock(this.getSessionUser(), cwb);
			if (stockResult == null) {
				obj.put("error", "本次已盘点完成，若想重新盘点请从菜单再次进入功能开始盘点");
			}
		} catch (Exception ce) {
			this.logger.error(ce.getMessage());
		}
		obj.put("cwb", cwb);
		return obj;
	}

	@RequestMapping("/takestockfinish")
	public @ResponseBody JSONObject takestockfinish(Model model) {
		JSONObject obj = this.cwbOrderService.StockFinish(this.getSessionUser());
		JSONObject finishobj = new JSONObject();

		List<JSONObject> winlist = JSONArray.toList(obj.getJSONArray("winlist"), JSONObject.class);
		List<JSONObject> kuilist = JSONArray.toList(obj.getJSONArray("kuilist"), JSONObject.class);

		finishobj.put("winlist", winlist);
		finishobj.put("kuilist", kuilist);
		finishobj.put("winnum", obj.getInt("winnum"));
		finishobj.put("kuinum", obj.getInt("kuinum"));

		return finishobj;
	}

	/**
	 * 导出
	 *
	 * @param customerid
	 *            供应商
	 * @type 未出库 已出库 一票多件
	 *
	 *
	 */
	@RequestMapping("/exportByCustomerid")
	public void exportByCustomerid(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "customerid", defaultValue = "0") long customerid,
			@RequestParam(value = "type", defaultValue = "") String type, @RequestParam(value = "emaildate", defaultValue = "0") long emaildate) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据
			String sqlstr = "";
			Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
			if (type.length() > 0) {
				if (type.equals("weiruku")) {
					sqlstr = this.cwbDAO.getSqlExportByCusromeridweiruku(customerid, b);
				}
				if (type.equals("yiruku")) {
					sqlstr = this.cwbDAO.getSqlExportByCusromeridyiruku(customerid, b);
				}
				if (emaildate > 0) {
					sqlstr += " and emaildateid=" + emaildate;
				}
				if (type.equals("ypdj")) {
					List<String> ypdjCwbs = new ArrayList<String>();
					Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
					int flowordertype = FlowOrderTypeEnum.RuKu.getValue();
					if (BranchEnum.KuFang.getValue() == b.getSitetype()) {
						flowordertype = FlowOrderTypeEnum.RuKu.getValue();
						// 中转库出库
					} else if (BranchEnum.ZhongZhuan.getValue() == b.getSitetype()) {
						flowordertype = FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue();
					}

					if ((branch != null) && (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
						ypdjCwbs = this.ypdjHandleRecordDAO.getSQLExportforypdjZhongzhuan(this.getSessionUser().getBranchid(), customerid, emaildate);
					} else {
						ypdjCwbs = this.ypdjHandleRecordDAO.getSQLExportforypdj(this.getSessionUser().getBranchid(), customerid, emaildate);
					}
					String orderflowcwbs = "";
					if (ypdjCwbs.size() > 0) {
						orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(ypdjCwbs);
					} else {
						orderflowcwbs = "'--'";
					}
					List<Integer> orderflowtypeList = new ArrayList<Integer>();
					orderflowtypeList.add(flowordertype);
					sqlstr = this.cwbDAO.getSqlByCwbForLack(orderflowcwbs, orderflowtypeList, branch.getBranchid());
				}
			}
			if (sqlstr.length() == 0) {
				return;
			}
			final String sql = sqlstr;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = PDAController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = PDAController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = PDAController.this.branchDAO.getAllBranches();
					final List<Common> commonList = PDAController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = PDAController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = PDAController.this.remarkDAO.getRemarkByCwbs("");
					final Map<String, Map<String, String>> remarkMap = PDAController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = PDAController.this.reasonDAO.getAllReason();
					PDAController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								// sheet.setColumnWidth(i, (short)
								// (5000));
								// //设置列宽
								Object a = PDAController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = PDAController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : PDAController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : PDAController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : PDAController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
							 * gotoClassAuditingDAO .getGotoClassAuditingByGcaid
							 * (deliveryState .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0) {
							 * ispayup = "是"; } cwbspayupidMap.put(deliveryState
							 * .getCwb(), ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	/**
	 * 导出 出库
	 *
	 * @param customerid
	 *            供应商
	 * @type 未出库 已出库 一票多件
	 *
	 *
	 */
	@RequestMapping("/exportBybranchid")
	public void exportByBranchid(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "branchid", defaultValue = "0") long branchid,
			@RequestParam(value = "type", defaultValue = "") String type) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据
			String sqlstr = "";
			Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
			if (type.length() > 0) {
				// 待出库明细
				if (type.equals("weichuku")) {
					int cwbstate = 1;
					// if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
					// cwbstate = CwbStateEnum.PeiShong.getValue();
					// }
					if (b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
						cwbstate = CwbStateEnum.ZhongZhuan.getValue();
					}
					sqlstr = this.cwbDAO.getSqlExportByBranchidweichuku(branchid, b, cwbstate);
				}
				// 已出库明细
				if (type.equals("yichuku")) {
					int flowordertypeid = 0;
					// 分拣库出库
					if (BranchEnum.KuFang.getValue() == b.getSitetype()) {
						flowordertypeid = FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
						// 中转库出库
					} else if (BranchEnum.ZhongZhuan.getValue() == b.getSitetype()) {
						flowordertypeid = FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue();
						// (退货再投)退货库
					} else if (BranchEnum.TuiHuo.getValue() == b.getSitetype()) {
						flowordertypeid = FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
					}
					sqlstr = this.cwbDAO.getSqlExportByBranchidyichuku(b.getBranchid(), branchid, flowordertypeid);
				}
				// 一票多件缺件明细
				if (type.equals("ypdj")) {
					int flowordertypeid = 0;
					// 分拣库出库
					if (BranchEnum.KuFang.getValue() == b.getSitetype()) {
						flowordertypeid = FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
						// 中转库出库
					} else if (BranchEnum.ZhongZhuan.getValue() == b.getSitetype()) {
						flowordertypeid = FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue();
						// (退货再投)退货库
					} else if (BranchEnum.TuiHuo.getValue() == b.getSitetype()) {
						flowordertypeid = FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
					}
					List<String> ypdjCwbs = this.ypdjHandleRecordDAO.getSQLExportforchukuypdj(this.getSessionUser().getBranchid(), branchid, flowordertypeid);
					String orderflowcwbs = "";
					if (ypdjCwbs.size() > 0) {
						orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(ypdjCwbs);
					} else {
						orderflowcwbs = "'--'";
					}
					List<Integer> orderflowtypeList = new ArrayList<Integer>();
					orderflowtypeList.add(flowordertypeid);
					sqlstr = this.cwbDAO.getSqlByCwbForLack(orderflowcwbs, orderflowtypeList, this.getSessionUser().getBranchid());
				}
			}
			if (sqlstr.length() == 0) {
				return;
			}
			final String sql = sqlstr;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = PDAController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = PDAController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = PDAController.this.branchDAO.getAllBranches();
					final List<Common> commonList = PDAController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = PDAController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = PDAController.this.remarkDAO.getRemarkByCwbs("");
					final Map<String, Map<String, String>> remarkMap = PDAController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = PDAController.this.reasonDAO.getAllReason();
					PDAController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								// sheet.setColumnWidth(i, (short)
								// (5000));
								// //设置列宽
								Object a = PDAController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = PDAController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : PDAController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : PDAController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : PDAController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
							 * gotoClassAuditingDAO .getGotoClassAuditingByGcaid
							 * (deliveryState .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0) {
							 * ispayup = "是"; } cwbspayupidMap.put(deliveryState
							 * .getCwb(), ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	/**
	 * 退货站入库 导出
	 *
	 * @param customerid
	 *            供应商
	 * @type 未出库 已出库 一票多件
	 *
	 *
	 */
	@RequestMapping("/backimportexport")
	public void exportByBranchid(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "type", defaultValue = "") String type,
			@RequestParam(value = "extype", defaultValue = "") String extype) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据
			String sqlstr = "";
			if (type.length() > 0) {
				// List<String> cwbList = new ArrayList<String>();
				if (type.equals("weiruku")) {
					if (extype.equals("wall") || extype.isEmpty()) {
						// cwbList = this.cwbDAO.getWeirukuCwbs(0,
						// FlowOrderTypeEnum.TuiHuoChuZhan.getValue(),
						// this.getSessionUser().getBranchid());
						sqlstr = this.cwbDAO.getWeirukuCwbsToSQL(0, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), this.getSessionUser().getBranchid());
					}
					if (extype.equals("wshangmengtui")) {
						// cwbList =
						// this.cwbDAO.getWeirukuCwbs(CwbOrderTypeIdEnum.Shangmentui.getValue(),
						// FlowOrderTypeEnum.TuiHuoChuZhan.getValue(),
						// this.getSessionUser().getBranchid());
						sqlstr = this.cwbDAO.getWeirukuCwbsToSQL(CwbOrderTypeIdEnum.Shangmentui.getValue(), FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), this.getSessionUser().getBranchid());
					}
					if (extype.equals("wshangmenghuan")) {
						// cwbList =
						// this.cwbDAO.getWeirukuCwbs(CwbOrderTypeIdEnum.Shangmenhuan.getValue(),
						// FlowOrderTypeEnum.TuiHuoChuZhan.getValue(),
						// this.getSessionUser().getBranchid());
						sqlstr = this.cwbDAO.getWeirukuCwbsToSQL(CwbOrderTypeIdEnum.Shangmenhuan.getValue(), FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), this.getSessionUser().getBranchid());
					}
					if (extype.equals("wpeisong")) {
						// cwbList =
						// this.cwbDAO.getWeirukuCwbs(CwbOrderTypeIdEnum.Peisong.getValue(),
						// FlowOrderTypeEnum.TuiHuoChuZhan.getValue(),
						// this.getSessionUser().getBranchid());
						sqlstr = this.cwbDAO.getWeirukuCwbsToSQL(CwbOrderTypeIdEnum.Peisong.getValue(), FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), this.getSessionUser().getBranchid());
					}
					// cwbList =
					// this.operationTimeDAO.getOperationTimeTuiHuoChuZhan(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(),
					// this.getSessionUser().getBranchid());
				}
				if (type.equals("yiruku")) {
					if (extype.equals("yall") || extype.isEmpty()) {
						// cwbList = this.cwbDAO.getYirukuCwbs(0,
						// FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(),
						// this.getSessionUser().getBranchid());
						sqlstr = this.cwbDAO.getYirukuCwbsToSQL(0, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), this.getSessionUser().getBranchid());
					}
					if (extype.equals("yshangmengtui")) {
						// cwbList =
						// this.cwbDAO.getYirukuCwbs(CwbOrderTypeIdEnum.Shangmentui.getValue(),
						// FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(),
						// this.getSessionUser().getBranchid());
						sqlstr = this.cwbDAO.getYirukuCwbsToSQL(CwbOrderTypeIdEnum.Shangmentui.getValue(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), this.getSessionUser().getBranchid());
					}
					if (extype.equals("yshangmenghuan")) {
						// cwbList =
						// this.cwbDAO.getYirukuCwbs(CwbOrderTypeIdEnum.Shangmenhuan.getValue(),
						// FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(),
						// this.getSessionUser().getBranchid());
						sqlstr = this.cwbDAO.getYirukuCwbsToSQL(CwbOrderTypeIdEnum.Shangmenhuan.getValue(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), this.getSessionUser().getBranchid());
					}
					if (extype.equals("ypeisong")) {
						// cwbList =
						// this.cwbDAO.getYirukuCwbs(CwbOrderTypeIdEnum.Peisong.getValue(),
						// FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(),
						// this.getSessionUser().getBranchid());
						sqlstr = this.cwbDAO.getYirukuCwbsToSQL(CwbOrderTypeIdEnum.Peisong.getValue(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), this.getSessionUser().getBranchid());
					}
					// cwbList =
					// this.operationTimeDAO.getOperationTimeTuiHuoZhanRuKu(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(),
					// this.getSessionUser().getBranchid());
				}
				/*
				 * String cwbs = ""; if (cwbList.size() > 0) { cwbs =
				 * this.dataStatisticsService.getStrings(cwbList); } else { cwbs
				 * = "'---'"; } sqlstr = this.cwbDAO.getSqlByCwb(cwbs);
				 */
			}
			if (sqlstr.length() == 0) {
				return;
			}
			final String sql = sqlstr;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = PDAController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = PDAController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = PDAController.this.branchDAO.getAllBranches();
					final List<Common> commonList = PDAController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = PDAController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = PDAController.this.remarkDAO.getRemarkByCwbs("");
					final Map<String, Map<String, String>> remarkMap = PDAController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = PDAController.this.reasonDAO.getAllReason();
					PDAController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								// sheet.setColumnWidth(i, (short)
								// (5000));
								// //设置列宽
								Object a = PDAController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = PDAController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : PDAController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : PDAController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : PDAController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
							 * gotoClassAuditingDAO .getGotoClassAuditingByGcaid
							 * (deliveryState .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0) {
							 * ispayup = "是"; } cwbspayupidMap.put(deliveryState
							 * .getCwb(), ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	@RequestMapping("/backandchangeimportexport")
	public void backandchangeimportexport(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "extype", defaultValue = "") String type) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据
			String sqlstr = "";
			String tbranchids = "-1";
			String zbranchids = "-1";
			List<Branch> tbranchlist = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.TuiHuo.getValue());
			List<Branch> zbranchlist = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhongZhuan.getValue());
			if ((tbranchlist != null) && (tbranchlist.size() > 0)) {
				for (Branch branch : tbranchlist) {
					tbranchids += "," + branch.getBranchid();
				}
			}
			if ((zbranchlist != null) && (zbranchlist.size() > 0)) {
				for (Branch branch : zbranchlist) {
					zbranchids += "," + branch.getBranchid();
				}
			}
			if (type.length() > 0) {
				if (type.equals("wall")) {
					sqlstr = this.cwbDAO.getWeirukuToSQL(0, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), tbranchids);
				}
				if (type.equals("yall")) {
					sqlstr = this.cwbDAO.getYirukuToSQL(0, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), tbranchids);
				}
				if (type.equals("wallz")) {
					sqlstr = this.cwbDAO.getWeirukuToSQL(0, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), zbranchids);
				}
				if (type.equals("yallz")) {
					sqlstr = this.cwbDAO.getYirukuToSQL(0, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), zbranchids);
				}
				if (type.equals("ypeisong")) {
					sqlstr = this.cwbDAO.getYirukuToSQL(CwbOrderTypeIdEnum.Peisong.getValue(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), tbranchids);
				}
				if (type.equals("yshangmengtui")) {
					sqlstr = this.cwbDAO.getYirukuToSQL(CwbOrderTypeIdEnum.Shangmentui.getValue(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), tbranchids);
				}
				if (type.equals("yshangmenghuan")) {
					sqlstr = this.cwbDAO.getYirukuToSQL(CwbOrderTypeIdEnum.Shangmenhuan.getValue(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), tbranchids);
				}
				if (type.equals("wpeisong")) {
					sqlstr = this.cwbDAO.getWeirukuToSQL(CwbOrderTypeIdEnum.Peisong.getValue(), FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), tbranchids);
				}
				if (type.equals("wshangmengtui")) {
					sqlstr = this.cwbDAO.getWeirukuToSQL(CwbOrderTypeIdEnum.Shangmentui.getValue(), FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), tbranchids);
				}
				if (type.equals("wshangmenghuan")) {
					sqlstr = this.cwbDAO.getWeirukuToSQL(CwbOrderTypeIdEnum.Shangmenhuan.getValue(), FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), tbranchids);
				}
				if (type.equals("wpeisongz")) {
					sqlstr = this.cwbDAO.getWeirukuToSQL(CwbOrderTypeIdEnum.Peisong.getValue(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), zbranchids);
				}
				if (type.equals("wshangmengtuiz")) {
					sqlstr = this.cwbDAO.getWeirukuToSQL(CwbOrderTypeIdEnum.Shangmentui.getValue(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), zbranchids);
				}
				if (type.equals("wshangmenghuanz")) {
					sqlstr = this.cwbDAO.getWeirukuToSQL(CwbOrderTypeIdEnum.Shangmenhuan.getValue(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), zbranchids);
				}

				if (type.equals("ypeisongz")) {
					sqlstr = this.cwbDAO.getYirukuToSQL(CwbOrderTypeIdEnum.Peisong.getValue(), FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), zbranchids);
				}
				if (type.equals("yshangmengtuiz")) {
					sqlstr = this.cwbDAO.getYirukuToSQL(CwbOrderTypeIdEnum.Shangmentui.getValue(), FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), zbranchids);
				}
				if (type.equals("yshangmenghuanz")) {
					sqlstr = this.cwbDAO.getYirukuToSQL(CwbOrderTypeIdEnum.Shangmenhuan.getValue(), FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), zbranchids);
				}
				// cwbList =
				// this.operationTimeDAO.getOperationTimeTuiHuoZhanRuKu(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(),
				// this.getSessionUser().getBranchid());

			}
			if (sqlstr.length() == 0) {
				return;
			}
			final String sql = sqlstr;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = PDAController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = PDAController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = PDAController.this.branchDAO.getAllBranches();
					final List<Common> commonList = PDAController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = PDAController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = PDAController.this.remarkDAO.getRemarkByCwbs("");
					final Map<String, Map<String, String>> remarkMap = PDAController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = PDAController.this.reasonDAO.getAllReason();
					PDAController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								// sheet.setColumnWidth(i, (short)
								// (5000));
								// //设置列宽
								Object a = PDAController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = PDAController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : PDAController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : PDAController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : PDAController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
							 * gotoClassAuditingDAO .getGotoClassAuditingByGcaid
							 * (deliveryState .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0) {
							 * ispayup = "是"; } cwbspayupidMap.put(deliveryState
							 * .getCwb(), ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	/**
	 * 退供货商 导出
	 *
	 * @param response
	 * @param request
	 * @param type
	 */

	@RequestMapping("/exportExcleForBackToCustomer")
	public void exportExcleForBackToCustomer(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "type", defaultValue = "") String type,
			@RequestParam(value = "extype", defaultValue = "") String extype) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据
			String sqlstr = "";
			if (type.length() > 0) {

				if (type.equals("weichuku")) {
					if (extype.equals("wall") || extype.isEmpty()) {
						sqlstr = this.cwbDAO.getBackYiRukuListbyBranchidSQL(this.getSessionUser().getBranchid(), 0);
					}
					if (extype.equals("wshangmengtui")) {
						sqlstr = this.cwbDAO.getBackYiRukuListbyBranchidSQL(this.getSessionUser().getBranchid(), CwbOrderTypeIdEnum.Shangmentui.getValue());

						// sqlstr =
						// this.cwbDAO.getSqlExportBackToCustomerWeichukuOfcwbtype(this.getSessionUser().getBranchid(),
						// CwbOrderTypeIdEnum.Shangmentui.getValue());
					}
					if (extype.equals("wshangmenghuan")) {
						// sqlstr =
						// this.cwbDAO.getSqlExportBackToCustomerWeichukuOfcwbtype(this.getSessionUser().getBranchid(),
						// CwbOrderTypeIdEnum.Shangmenhuan.getValue());
						sqlstr = this.cwbDAO.getBackYiRukuListbyBranchidSQL(this.getSessionUser().getBranchid(), CwbOrderTypeIdEnum.Shangmenhuan.getValue());

					}
					if (extype.equals("wpeisong")) {
						// sqlstr =
						// this.cwbDAO.getSqlExportBackToCustomerWeichukuOfcwbtype(this.getSessionUser().getBranchid(),
						// CwbOrderTypeIdEnum.Peisong.getValue());
						sqlstr = this.cwbDAO.getBackYiRukuListbyBranchidSQL(this.getSessionUser().getBranchid(), CwbOrderTypeIdEnum.Peisong.getValue());

					}
				}
				if (type.equals("yichuku")) {
					if (extype.equals("yall") || extype.isEmpty()) {
						sqlstr = this.cwbDAO.getTuiGongHuoShangYiChuKuSql(this.getSessionUser().getBranchid(), 0);
						// sqlstr =
						// this.cwbDAO.getSqlExportBackToCustomerYichukuOfcwbtype(this.getSessionUser().getBranchid(),
						// 0);
					}
					if (extype.equals("yshangmengtui")) {
						sqlstr = this.cwbDAO.getTuiGongHuoShangYiChuKuSql(this.getSessionUser().getBranchid(), CwbOrderTypeIdEnum.Shangmentui.getValue());
						// sqlstr =
						// this.cwbDAO.getSqlExportBackToCustomerYichukuOfcwbtype(this.getSessionUser().getBranchid(),
						// CwbOrderTypeIdEnum.Shangmentui.getValue());
					}
					if (extype.equals("yshangmenghuan")) {
						sqlstr = this.cwbDAO.getTuiGongHuoShangYiChuKuSql(this.getSessionUser().getBranchid(), CwbOrderTypeIdEnum.Shangmenhuan.getValue());
						// sqlstr =
						// this.cwbDAO.getSqlExportBackToCustomerYichukuOfcwbtype(this.getSessionUser().getBranchid(),
						// CwbOrderTypeIdEnum.Shangmenhuan.getValue());
					}
					if (extype.equals("ypeisong")) {
						sqlstr = this.cwbDAO.getTuiGongHuoShangYiChuKuSql(this.getSessionUser().getBranchid(), CwbOrderTypeIdEnum.Peisong.getValue());
						// sqlstr =
						// this.cwbDAO.getSqlExportBackToCustomerYichukuOfcwbtype(this.getSessionUser().getBranchid(),
						// CwbOrderTypeIdEnum.Peisong.getValue());
					}
				}
			}
			if (sqlstr.length() == 0) {
				return;
			}
			final String sql = sqlstr;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = PDAController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = PDAController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = PDAController.this.branchDAO.getAllBranches();
					final List<Common> commonList = PDAController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = PDAController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = PDAController.this.remarkDAO.getRemarkByCwbs("");
					final Map<String, Map<String, String>> remarkMap = PDAController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = PDAController.this.reasonDAO.getAllReason();
					PDAController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								// sheet.setColumnWidth(i, (short)
								// (5000));
								// //设置列宽
								Object a = PDAController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = PDAController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : PDAController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : PDAController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : PDAController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
							 * gotoClassAuditingDAO .getGotoClassAuditingByGcaid
							 * (deliveryState .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0) {
							 * ispayup = "是"; } cwbspayupidMap.put(deliveryState
							 * .getCwb(), ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	/**
	 * 领货够功能页面中的导出
	 *
	 * @param response
	 * @param request
	 * @param deliverid
	 * @param type
	 */
	@RequestMapping("/exportByDeliverid")
	public void exportByDeliverid(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "deliverid", defaultValue = "0") long deliverid,
			@RequestParam(value = "type", defaultValue = "") String type) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据
			String sqlstr = "";
			if (type.length() > 0) {
				if (type.equals("1")) {// 今日未领货数据
					// 今日到货订单数
					// List<String> todaydaohuocwbs =
					// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
					// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
					// DateTimeUtil.getCurrentDayZeroTime(), "");
					List<String> todaydaohuocwbs = this.operationTimeDAO.getOrderFlowLingHuoList(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
							+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime());
					// 今日滞留订单数
					// List<String> todayzhiliucwbs =
					// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
					// FlowOrderTypeEnum.YiShenHe.getValue()+"",
					// DateTimeUtil.getCurrentDayZeroTime(), "");
					List<String> todayzhiliucwbs = this.operationTimeDAO.getjinrizhiliu(this.getSessionUser().getBranchid(), DeliveryStateEnum.FenZhanZhiLiu.getValue(),
							FlowOrderTypeEnum.YiShenHe.getValue(), DateTimeUtil.getCurrentDayZeroTime());
					// 今日到货订单
					List<String> todaydaohuolist = new ArrayList<String>();
					if (todaydaohuocwbs.size() > 0) {
						todaydaohuolist = this.cwbDAO.getTodayWeiLingDaohuoCwbsbyBranchid(this.getSessionUser().getBranchid(), this.getStrings(todaydaohuocwbs));
					}
					// 今日滞留订单
					List<String> todayzhiliulist = new ArrayList<String>();
					if (todayzhiliucwbs.size() > 0) {
						todayzhiliulist = this.cwbDAO.getTodayWeiLingZhiliuCwbsByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(), this.getSessionUser().getBranchid(),
								this.getStrings(todayzhiliucwbs), deliverid);
					}

					// 今日待中转
					List<String> todaydaizhongzhuanlist = new ArrayList<String>();
					List<String> todayAppZhongZhuanList = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanList(DateTimeUtil.getCurrentDayZeroTime(), "", 2, this.getSessionUser().getBranchid());
					if (todayAppZhongZhuanList.size() > 0) {
						todaydaizhongzhuanlist = this.cwbDAO.getTodayWeiLingZhiliuCwbsByWhereList(DeliveryStateEnum.DaiZhongZhuan.getValue(), this.getSessionUser().getBranchid(),
								this.getStrings(todayAppZhongZhuanList), deliverid);
					}

					// 今日退货审核不通过统计
					List<String> todayjushoulist = new ArrayList<String>();
					List<String> todayJuShouCwbs = this.orderBackCheckDAO.getOrderBackChecksCwbs(this.getSessionUser().getBranchid(), DateTimeUtil.getCurrentDayZeroTime(), "");
					String deliverystates = DeliveryStateEnum.JuShou.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + "," + DeliveryStateEnum.ShangMenJuTui.getValue();
					if (todayJuShouCwbs.size() > 0) {
						todayjushoulist = this.cwbDAO.getTodayWeiLingJuShouByWhereList(deliverystates, this.getSessionUser().getBranchid(), this.getStrings(todayJuShouCwbs), deliverid);
					}

					// //////////////////////////////////////////////

					List<String> todaycwbslist = new ArrayList<String>();
					todaycwbslist.addAll(todaydaohuolist);
					todaycwbslist.addAll(todayzhiliulist);
					todaycwbslist.addAll(todaydaizhongzhuanlist);
					todaycwbslist.addAll(todayjushoulist);

					StringBuffer str = new StringBuffer();
					String cwbs = "";
					if ((todaycwbslist != null) && (todaycwbslist.size() > 0)) {
						for (String cwb : todaycwbslist) {
							str.append("'").append(cwb).append("',");
						}
						cwbs = str.substring(0, str.length() - 1);
					} else {
						cwbs = "'--'";
					}
					sqlstr = this.cwbDAO.getSqlByCwb(cwbs);

				}
				if (type.equals("2")) {// 已领货数据
					List<String> linghuocwbs = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchid(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanLingHuo.getValue());
					String yilinghuocwbs = "";
					if (linghuocwbs.size() > 0) {
						yilinghuocwbs = this.getStrings(linghuocwbs);
					} else {
						yilinghuocwbs = "'--'";
					}

					sqlstr = this.cwbDAO.getYiLingHuoListbyBranchidformingxiSql(yilinghuocwbs, this.getSessionUser().getBranchid(), deliverid);
					sqlstr += " and flowordertype in (" + FlowOrderTypeEnum.FenZhanLingHuo.getValue() + ")";
				}
				if (type.equals("4")) {// 历史未领货数据
					// 今日到货订单数
					// List<String> todaydaohuocwbs =
					// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
					// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
					// DateTimeUtil.getCurrentDayZeroTime(), "");
					// List<String> todaydaohuocwbs =
					// operationTimeDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
					// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
					// DateTimeUtil.getCurrentDayZeroTime());
					// 今日滞留订单数
					// List<String> todayzhiliucwbs =
					// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
					// FlowOrderTypeEnum.YiShenHe.getValue()+"",
					// DateTimeUtil.getCurrentDayZeroTime(), "");
					// List<String> todayzhiliucwbs =
					// operationTimeDAO.getjinrizhiliu(getSessionUser().getBranchid(),
					// DeliveryStateEnum.FenZhanZhiLiu.getValue(),FlowOrderTypeEnum.YiShenHe.getValue(),
					// DateTimeUtil.getCurrentDayZeroTime());
					List<String> historycwbslist = new ArrayList<String>();
					// 历史到货订单
					// List<String> historydaohuolist =
					// cwbDAO.getHistoryyWeiLingDaohuocwbsbyBranchid(getSessionUser().getBranchid(),getStrings(todaydaohuocwbs));
					List<String> historydaohuolist = this.operationTimeDAO.getlishidaohuo(this.getSessionUser().getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
							+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), DateTimeUtil.getCurrentDayZeroTime());
					if (historydaohuolist.size() > 0) {
						historydaohuolist = this.cwbDAO.getTodayWeiLingDaohuoCwbsbyBranchid(this.getSessionUser().getBranchid(), this.getStrings(historydaohuolist));
					}
					// 历史滞留订单

					// List<String> histo-+ryzhiliulist =
					// cwbDAO.getHistoryWeiLingZhiliuCwbsByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(),getSessionUser().getBranchid(),getStrings(todayzhiliucwbs),deliverid);
					List<String> historyzhiliulist = this.operationTimeDAO.getlishizhiliu(this.getSessionUser().getBranchid(), DeliveryStateEnum.FenZhanZhiLiu.getValue(),
							FlowOrderTypeEnum.YiShenHe.getValue(), DateTimeUtil.getCurrentDayZeroTime());
					if (historyzhiliulist.size() > 0) {
						historyzhiliulist = this.cwbDAO.getHistoryWeiLingZhiliuCwbsByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(), this.getSessionUser().getBranchid(),
								this.getStrings(historyzhiliulist), deliverid);
					}

					// 历史待中转订单
					List<String> historyAppZhongZhuanList = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanList("", DateTimeUtil.getCurrentDayZeroTime(), 2, this.getSessionUser().getBranchid());

					if (historyAppZhongZhuanList.size() > 0) {
						historycwbslist.addAll(historyAppZhongZhuanList);
					}
					// 历史拒收订单
					// List<CwbOrder> historyjushoulist = new
					// ArrayList<CwbOrder>();// 历史待领货list
					List<String> historyJuShouCwbs = this.orderBackCheckDAO.getOrderBackChecksCwbs(this.getSessionUser().getBranchid(), "", DateTimeUtil.getCurrentDayZeroTime());
					if (historyJuShouCwbs.size() > 0) {
						// historyjushoulist =
						// this.cwbDAO.getHistoryWeiLingJuShouByWhereList(deliverystates,
						// this.getSessionUser().getBranchid(),
						// this.getStrings(historyJuShouCwbs),
						historycwbslist.addAll(historyJuShouCwbs);
					}
					historycwbslist.addAll(historydaohuolist);
					historycwbslist.addAll(historyzhiliulist);

					StringBuffer str = new StringBuffer();
					String cwbs = "";
					if ((historycwbslist != null) && (historycwbslist.size() > 0)) {
						for (String cwb : historycwbslist) {
							str.append("'").append(cwb).append("',");
						}
						cwbs = str.substring(0, str.length() - 1);
					} else {
						cwbs = "'--'";
					}

					sqlstr = this.cwbDAO.getSqlByCwb(cwbs);

				}
			}
			if (sqlstr.length() == 0) {
				return;
			}
			final String sql = sqlstr;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = PDAController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = PDAController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = PDAController.this.branchDAO.getAllBranches();
					final List<Common> commonList = PDAController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = PDAController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = PDAController.this.remarkDAO.getRemarkByCwbs("");
					final Map<String, Map<String, String>> remarkMap = PDAController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = PDAController.this.reasonDAO.getAllReason();
					PDAController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								// sheet.setColumnWidth(i, (short)
								// (5000));
								// //设置列宽
								Object a = PDAController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = PDAController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : PDAController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : PDAController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : PDAController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
							 * gotoClassAuditingDAO .getGotoClassAuditingByGcaid
							 * (deliveryState .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0) {
							 * ispayup = "是"; } cwbspayupidMap.put(deliveryState
							 * .getCwb(), ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	@RequestMapping("/branchimportexport")
	public void branchimportexport(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "type", defaultValue = "") String type) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据
			String sqlstr = "";
			if (type.length() > 0) {
				String showintowarehousedata = "no";
				try {
					showintowarehousedata = this.systemInstallDAO.getSystemInstallByName("showintowarehousedata").getValue();
				} catch (Exception e) {
					this.logger.error("分站到货时，”分站到货未到货数据是否显示库房入库的数据“系统配置获取失败");
				}

				String flowordertypes = FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue() + "," + FlowOrderTypeEnum.LanJianChuZhan.getValue()+","
                        + FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue() + ","
						+ FlowOrderTypeEnum.DingDanLanJie.getValue();
				if (showintowarehousedata.equals("yes")) {
					flowordertypes += "," + FlowOrderTypeEnum.RuKu.getValue();
				}
				// 今日出库(未到货)订单数
				List<String> jinriweidaohuocwbslist = this.operationTimeDAO.getOrderFlowJinRiChuKuORRuKuListAll(this.getSessionUser().getBranchid(), flowordertypes,
						DateTimeUtil.getCurrentDayZeroTime());

				String jinriweidaohuocwbs = "";
				if (jinriweidaohuocwbslist.size() > 0) {
					jinriweidaohuocwbs = this.getStrings(jinriweidaohuocwbslist);
				} else {
					jinriweidaohuocwbs = "'--'";
				}

				if (type.equals("1")) {// 今日未到货

					List<String> jinriweidaohuolist = this.cwbDAO.getJinRiDaoHuoByBranchidForListNoPage(flowordertypes, jinriweidaohuocwbs);
					StringBuffer str = new StringBuffer();
					String cwbs = "";
					if ((jinriweidaohuolist != null) && (jinriweidaohuolist.size() > 0)) {
						for (String cwb : jinriweidaohuolist) {
							str.append("'").append(cwb).append("',");
						}
						cwbs = str.substring(0, str.length() - 1);
					} else {
						cwbs = "'--'";
					}
					sqlstr = this.cwbDAO.getSqlByCwb(cwbs);
				}
				if (type.equals("5")) {// 历史未到货
					// 历史未到货订单数
					//List<String> historyweidaohuocwblist = this.cwbDAO.getHistoryDaoHuoByBranchidForListNoPage(this.getSessionUser().getBranchid(), flowordertypes, jinriweidaohuocwbs);
					List<String> historyweidaohuocwblist = this.operationTimeDAO.getlishiweidaohuoAll(this.getSessionUser().getBranchid(), flowordertypes, DateTimeUtil.getCurrentDayZeroTime());
					
					StringBuffer str = new StringBuffer();
					String cwbs = "";
					if ((historyweidaohuocwblist != null) && (historyweidaohuocwblist.size() > 0)) {
						for (String cwb : historyweidaohuocwblist) {
							str.append("'").append(cwb).append("',");
						}
						cwbs = str.substring(0, str.length() - 1);
					} else {
						cwbs = "'--'";
					}
					sqlstr = this.cwbDAO.getSqlByCwb(cwbs);
				}
				if (type.equals("2")) {// 已到货数据
					sqlstr = this.cwbDAO.getYiDaohuobyBranchidListSql(this.getSessionUser().getBranchid());
				}
				if (type.equals("ypdj")) {
					List<String> ypdjCwbs = this.ypdjHandleRecordDAO.getDaoHuoSQLExportforypdj(this.getSessionUser().getBranchid());
					String orderflowcwbs = "";
					if (ypdjCwbs.size() > 0) {
						orderflowcwbs = this.dataStatisticsService.getOrderFlowCwbs(ypdjCwbs);
					} else {
						orderflowcwbs = "'--'";
					}
					List<Integer> orderflowtypeList = new ArrayList<Integer>();
					orderflowtypeList.add(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
					orderflowtypeList.add(FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
					sqlstr = this.cwbDAO.getSqlByCwbForLack(orderflowcwbs, orderflowtypeList, this.getSessionUser().getBranchid());
				}
				if (type.equals("lanjianwdz")) {// 揽件未到站sql
					sqlstr = this.cwbDAO.getLanJianWeiDaoZhanByBranchidListSql(this.getSessionUser().getBranchid());
				}
			}
			if (sqlstr.length() == 0) {
				return;
			}
			final String sql = sqlstr;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = PDAController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = PDAController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = PDAController.this.branchDAO.getAllBranches();
					final List<Common> commonList = PDAController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = PDAController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = PDAController.this.remarkDAO.getRemarkByCwbs("");
					final Map<String, Map<String, String>> remarkMap = PDAController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = PDAController.this.reasonDAO.getAllReason();
					PDAController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								// sheet.setColumnWidth(i, (short)
								// (5000));
								// //设置列宽
								Object a = PDAController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = PDAController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : PDAController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : PDAController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : PDAController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
							 * gotoClassAuditingDAO .getGotoClassAuditingByGcaid
							 * (deliveryState .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0) {
							 * ispayup = "是"; } cwbspayupidMap.put(deliveryState
							 * .getCwb(), ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	/**
	 * 通用方法，json变为list
	 *
	 * @param s
	 *            json
	 * @param clazz
	 *            公用
	 * @return
	 */
	public static List test(String s, Class clazz) {
		JSONArray jarr = JSONArray.fromObject(s);
		return (List) JSONArray.toCollection(jarr, clazz);
	}

	public Object getShowCustomer(JSONArray jSONArray, CwbOrder co) {
		Object remark = "";
		try {
			for (int i = 0; i < jSONArray.size(); i++) {
				String a = jSONArray.getJSONObject(i).getString("customerid");
				String b[] = a.split(",");
				for (String s : b) {
					if (String.valueOf(co.getCustomerid()).equals(s)) {
						remark = co.getClass().getMethod("get" + jSONArray.getJSONObject(i).getString("remark")).invoke(co);
						break;
					}
				}
			}
		} catch (Exception e) {
			this.logger.error("", e);
		}
		return remark;
	}

	public List<CwbDetailView> getcwbDetail(List<CwbOrder> cwbList, List<Customer> customerList, JSONArray showCustomerjSONArray, List<Branch> branchList, long sign) {
		// Map<String, Map<String, String>> allTime =
		// this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(this.getcwbs(cwbList),
		// branchList);
		// List<CwbDetailView> cwbViewlist = new ArrayList<CwbDetailView>();
		// if (cwbList.size() > 0) {
		// for (CwbOrder wco : cwbList) {
		// CwbDetailView view = new CwbDetailView();
		// Map<String, String> cwbMap = allTime.isEmpty() ? new HashMap<String,
		// String>() : (allTime.get(wco.getCwb()));
		//
		// view.setOpscwbid(wco.getOpscwbid());
		// view.setCwb(wco.getCwb());
		// view.setCwbordertypeid(wco.getCwbordertypeid());
		// view.setPackagecode(wco.getPackagecode());
		// view.setConsigneeaddress(wco.getConsigneeaddress());
		// view.setConsigneename(wco.getConsigneename());
		// view.setReceivablefee(wco.getReceivablefee());
		// view.setEmaildate(wco.getEmaildate());
		// view.setTranscwb(wco.getTranscwb());
		// view.setCustomerid(wco.getCustomerid());
		// view.setNextbranchid(wco.getNextbranchid());
		//
		// view.setRemarkView(this.getShowCustomer(showCustomerjSONArray, wco));
		// view.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList,
		// wco.getCustomerid()));
		// view.setInSitetime(cwbMap == null ? "" : (cwbMap.get("InSitetime") ==
		// null ? "" : cwbMap.get("InSitetime")));
		// view.setPickGoodstime(cwbMap == null ? "" :
		// (cwbMap.get("PickGoodstime") == null ? "" :
		// cwbMap.get("PickGoodstime")));
		// view.setOutstoreroomtime(cwbMap == null ? "" :
		// (cwbMap.get("Outstoreroomtime") == null ? "" :
		// cwbMap.get("Outstoreroomtime")));
		//
		// view.setCwbordertype(CwbOrderTypeIdEnum.getTextByValue(wco.getCwbordertypeid()));
		// view.setPickaddress(wco.getRemark4());
		//
		// cwbViewlist.add(view);
		// }
		// }
		//
		// List<CwbDetailView> views = new ArrayList<CwbDetailView>();
		// Map<String, CwbDetailView> map = new HashMap<String,
		// CwbDetailView>();
		// for (CwbDetailView weirukuView : cwbViewlist) {
		// if (sign == 1) {// 按出库时间排序
		// map.put(weirukuView.getOutstoreroomtime() + weirukuView.getOpscwbid()
		// + "_" + cwbViewlist.indexOf(weirukuView), weirukuView);
		// } else if (sign == 2) {// 按到货时间排序
		// map.put(weirukuView.getInSitetime() + weirukuView.getOpscwbid() + "_"
		// + cwbViewlist.indexOf(weirukuView), weirukuView);
		// } else if (sign == 3) {// 按领货时间排序
		// map.put(weirukuView.getPickGoodstime() + weirukuView.getOpscwbid() +
		// "_" + cwbViewlist.indexOf(weirukuView), weirukuView);
		// }
		// }
		// List<String> keys = new ArrayList<String>(map.keySet());
		// Collections.sort(keys, Collections.reverseOrder());
		// for (int i = 0; i < keys.size(); i++) {
		// views.add(map.get(keys.get(i)));
		// }
		// return sign == 0 ? cwbViewlist : views;

		return this.kfzdOrderService.getcwbDetail(cwbList, customerList, showCustomerjSONArray, branchList, sign);
	}

	public List<String> getcwbs(List<CwbOrder> cwbList) {
		List<String> cwbsList = new ArrayList<String>();
		if (cwbList.size() > 0) {
			for (CwbOrder wco : cwbList) {
				cwbsList.add(wco.getCwb());
			}
		}
		return cwbsList;
	}

	@RequestMapping("/cwblanshoudaohuoBatch")
	public String cwblanshoudaohuoBatch(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "deliverid", required = true, defaultValue = "0") long deliverid) {
		long allcwbnum = 0;
		long thissuccess = 0;
		String msg = "";
		String reg = "^[a-zA-Z0-9-_*]+$";
		Pattern pattern = Pattern.compile(reg);

		List<JSONObject> objList = new ArrayList<JSONObject>();
		List<JSONObject> falList = new ArrayList<JSONObject>();
		if ((deliverid > 0) && (cwbs.trim().length() > 0)) {
			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				}
				cwb = cwb.trim();
				allcwbnum++;
				JSONObject obj = new JSONObject();
				String scancwb = cwb;
				cwb = this.cwbOrderService.translateCwb(cwb);
				obj.put("cwb", cwb);
				try {// 成功订单
					Matcher matcher = pattern.matcher(cwb);
					if (!matcher.matches()) {
						throw new CwbException(cwb, FlowOrderTypeEnum.LanShouDaoHuo.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
					}
					CwbOrder cwbOrder = this.cwbOrderService.lanShouDaoHuo(this.getSessionUser(), cwb, scancwb, deliverid);
					obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
					obj.put("errorcode", "000000");
					thissuccess++;
				} catch (CwbException ce) {// 出现验证错误
					this.logger.error("cwb="+cwb,ce);
					CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
					this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
							cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
					obj.put("cwbOrder", cwbOrder);
					obj.put("errorcode", ce.getError().getValue());
					obj.put("errorinfo", ce.getMessage());
					falList.add(obj);
				}
				objList.add(obj);
			}
		}
		model.addAttribute("objList", objList);
		model.addAttribute("falList", falList);

		if (cwbs.length() > 0) {
			msg = "成功扫描" + thissuccess + "单，异常" + (allcwbnum - thissuccess) + "单";
		}
		model.addAttribute("msg", msg);

		String roleids = "2,4";
		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());
		model.addAttribute("userList", uList);

		return "pda/lanshoudaohuoBatch";
	}

	private boolean isPlayGPSound() {
		SystemInstall gpParam = this.systemInstallDAO.getSystemInstall(PDAController.PLAY_GP_SOUND);
		if ((gpParam == null) || "no".equals(gpParam.getValue())) {
			return false;
		}
		return true;
	}

	private boolean isPlayYPDJSound() {
		SystemInstall gpParam = this.systemInstallDAO.getSystemInstall(PDAController.PLAY_YPDJ_SOUND);
		if ((gpParam == null) || "no".equals(gpParam.getValue())) {
			return false;
		}
		return true;
	}

	@RequestMapping("/showgoodsdetail/{cwb}")
	public @ResponseBody ExplinkResponse showgoodsdetail(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid) {
		JSONObject obj = new JSONObject();
		String code = "111111";
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);

		if (cwbOrder != null) {
			List<OrderGoods> orderGoodsList = this.orderGoodsDAO.getOrderGoodsList(cwb);
			if (orderGoodsList.size() > 0) {
				code = "000000";
				obj.put("orderGoodsList", orderGoodsList);
			} else if (cwbOrder.getCustomerid() != customerid) {
				code = "333";
			}
		} else {
			code = "222222";
		}
		ExplinkResponse explinkResponse = new ExplinkResponse(code, "商品明细", obj);

		return explinkResponse;
	}

	@RequestMapping("/updategoodthzrkcount")
	public @ResponseBody ExplinkResponse updategoodthzrkcount(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid, @RequestParam(value = "comment", required = true, defaultValue = "") String comment,
			@RequestParam(value = "cwb", required = true, defaultValue = "") String cwb, @RequestParam(value = "jasonval", required = true, defaultValue = "0") String jasonval,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, @RequestParam(value = "checktype", required = false, defaultValue = "0") int checktype) {
		JSONArray ids = (JSONArray) JSONObject.fromObject(jasonval).get("id");
		JSONArray thzrkcounts = (JSONArray) JSONObject.fromObject(jasonval).get("thzrkcount");

		String scancwb = cwb;
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder cwbOrder = null;
		if (checktype == 1) {
			OperationTime op = this.operationTimeDAO.getObjectBycwb(cwb);
			if (op == null) {
				throw new CwbException(cwb, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}
			if ((op.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) || (op.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())) {
				long branchid = 0;
				if (op.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
					branchid = op.getBranchid();
				} else {
					branchid = op.getNextbranchid();
				}
				cwbOrder = this.cwbOrderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, comment, false, 1, branchid);
			} else {
				cwbOrder = this.cwbOrderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, comment, false, 0, 0);

			}
		} else {
			cwbOrder = this.cwbOrderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, comment, false, 0, 0);
		}
		JSONObject obj = new JSONObject();
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
		for (int i = 0; i < ids.size(); i++) {
			this.orderGoodsDAO.updateThzrkcount(ids.getLong(i), thzrkcounts.getLong(i));
		}
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText(), obj);
		if (cwbOrder.getNextbranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
			obj.put("cwbbranchname", branch.getBranchname());
			obj.put("cwbbranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			obj.put("cwbgaojia", "true");
			explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GJ));
		} else {
			obj.put("cwbbranchname", "");
			obj.put("cwbbranchnamewav", "");
			obj.put("cwbgaojia", "");
		}

		if (cwbOrder.getDeliverybranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
			obj.put("cwbdeliverybranchname", branch.getBranchname());
			obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbdeliverybranchname", "");
			obj.put("cwbdeliverybranchnamewav", "");
		}
		// 加入供货商名称.
		this.addCustomerWav(request, explinkResponse, cwbOrder);
		// 加入货物类型声音.
		this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);

		String wavPath = null;
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
		} else {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}

		if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
			explinkResponse.setErrorinfo(explinkResponse.getErrorinfo() + "\n一票多件");
			if (this.isPlayYPDJSound()) {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl();
			}
		}
		explinkResponse.addLastWav(wavPath);

		return explinkResponse;
	}

	// ------------------------入库排序-----------------
	@RequestMapping("/orderbyweiruku")
	public @ResponseBody List<CwbDetailView> orderbyweiruku(@RequestParam(value = "orderby", defaultValue = "") String orderby,
			@RequestParam(value = "customerid", defaultValue = "0") long customerid, @RequestParam(value = "emaildate", defaultValue = "0") long emaildate,
			@RequestParam(value = "asc", defaultValue = "0") long asc) {
		Branch b = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 供货商
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<CwbOrder> weirukulist = this.cwbDAO.getRukuByBranchidForList(b.getBranchid(), b.getSitetype(), orderby, customerid, emaildate, asc);
		List<CwbDetailView> weirukuVeiwList = this.getcwbDetail(weirukulist, customerList, showCustomerjSONArray, null, 0);
		return weirukuVeiwList;

	}

	@RequestMapping("/orderbyyiruku")
	public @ResponseBody List<CwbDetailView> orderbyyiruku(@RequestParam(value = "orderby", defaultValue = "") String orderby, @RequestParam(value = "customerid", defaultValue = "0") long customerid,
			@RequestParam(value = "emaildate", defaultValue = "0") long emaildate, @RequestParam(value = "asc", defaultValue = "0") long asc) {
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 供货商
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<CwbOrder> yiruku = this.cwbDAO.getYiRukubyBranchidList(this.getSessionUser().getBranchid(), customerid, orderby, emaildate, asc);
		List<CwbDetailView> yirukuVeiwList = this.getcwbDetail(yiruku, customerList, showCustomerjSONArray, null, 0);
		return yirukuVeiwList;

	}

	@RequestMapping("/orderbygetrukucwbquejiandataList")
	public String orderbygetrukucwbquejiandataList(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid,
			@RequestParam(value = "emaildate", defaultValue = "0") long emaildate, @RequestParam(value = "orderby", defaultValue = "") String orderby,
			@RequestParam(value = "asc", defaultValue = "0") long asc) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<JSONObject> quejianList = this.ypdjHandleRecordDAO.getRukuQuejianbyBranchidList(this.getSessionUser().getBranchid(), customerid, orderby, emaildate, asc);
		for (JSONObject obj : quejianList) {
			//Hps_Concerto create  2016年6月29日 10:14:43
			String cwbstatetext="";
			String cwbordertypetext="";
			String flowordertypetext="";
			String cwb = (String)(obj.get("cwb")==null?"":obj.get("cwb"));
			if(!cwb.equals("")){
				CwbOrder cwborder = this.cwbDAO.getCwbByCwb(cwb);
				if(cwborder!=null){
			/*<td width="80" align="center" bgcolor="#f1f1f1"><%=obj.get("cwbordertype") %></td>
			<td width="80" align="center" bgcolor="#f1f1f1"><%=obj.get("cwbstatetext") %></td>
			<td width="120" align="center" bgcolor="#f1f1f1"><%=obj.get("flowordertypetext") %></td>*/
			for (CwbStateEnum  c   : CwbStateEnum.values()){
				 if(c.getValue()==cwborder.getCwbstate()){
					 cwbstatetext=c.getText();
				 }
			 } 
			long flowordertype=cwborder.getFlowordertype();
			int deliverystate = cwborder.getDeliverystate();
			try{
				if(CwbFlowOrderTypeEnum.getText(flowordertype)!=null){
					if(CwbFlowOrderTypeEnum.getText(flowordertype).getText()=="已审核"){
							flowordertypetext="审核为："+DeliveryStateEnum.getByValue(deliverystate).getText();
					 }else if(CwbFlowOrderTypeEnum.getText(flowordertype).getText()=="已反馈") {
							flowordertypetext="反馈为："+DeliveryStateEnum.getByValue(deliverystate).getText(); 
						/*hps*/ }else{
							 flowordertypetext=CwbFlowOrderTypeEnum.getText(flowordertype).getText();
					 }
				}else{
					flowordertypetext="";
				}
			}catch(Exception e){
				flowordertypetext="";
			}
			cwbordertypetext = CwbOrderTypeIdEnum.getTextByValue(cwborder.getCwbordertypeid());
				}
			}
			obj.put("cwbstatetext", cwbstatetext);
			obj.put("cwbordertype", cwbordertypetext);
			obj.put("flowordertypetext", flowordertypetext);
			
			//*************
			String transcwb = "";
			String explink = "explink";
			if (obj.getString("transcwb").indexOf("explink") > -1) {
				transcwb = obj.getString("transcwb").substring(explink.length() + 1, obj.getString("transcwb").length());
			} else if (obj.getString("transcwb").indexOf("havetranscwb") > -1) {
				transcwb = obj.getString("transcwb").split("_")[1];
			} else {
				transcwb = obj.getString("transcwb");
			}
			obj.put("transcwb", transcwb);
			obj.put("customername", this.dataStatisticsService.getQueryCustomerName(customerList, obj.getLong("customerid")));
		}
		model.addAttribute("quejianList", quejianList);
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("flowordertype", FlowOrderTypeEnum.RuKu.getValue());
		model.addAttribute("customerid", customerid);
		model.addAttribute("page", 1);
		return "pda/quejianlist";
	}

	// ------------------------出库排序-----------------
	@RequestMapping("/orderbyweichuku")
	public @ResponseBody List<CwbDetailView> orderbyweichuku(@RequestParam(value = "branchid", defaultValue = "0") long branchid, @RequestParam(value = "orderby", defaultValue = "") String orderby,
			@RequestParam(value = "asc", defaultValue = "0") long asc) {
		Branch localbranch = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
		int cwbstate = CwbStateEnum.PeiShong.getValue();
		if (localbranch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
			cwbstate = CwbStateEnum.TuiHuo.getValue();
		}

		List<CwbOrder> cList = this.cwbDAO.getChukuForCwbOrderByBranchid(localbranch.getBranchid(), cwbstate, orderby, branchid, asc);

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 供货商
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<CwbDetailView> weichukuVeiwList = this.getcwbDetail(cList, customerList, showCustomerjSONArray, null, 0);

		return weichukuVeiwList;

	}

	@RequestMapping("/orderbyyichuku")
	public @ResponseBody List<CwbDetailView> orderbyyichuku(@RequestParam(value = "branchid", defaultValue = "0") long branchid, @RequestParam(value = "orderby", defaultValue = "0") String orderby,
			@RequestParam(value = "flowordertype", defaultValue = "6") long flowordertype, @RequestParam(value = "asc", defaultValue = "0") long asc) {
		List<String> cwbyichukuList = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(this.getSessionUser().getBranchid(), branchid, flowordertype);

		String cwbs = "";
		if (cwbyichukuList.size() > 0) {
			cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbyichukuList);
		} else {
			cwbs = "'--'";
		}

		List<CwbOrder> cList = this.cwbDAO.getCwbByCwbsPage(orderby, cwbs, asc);

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		// 供货商
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<CwbDetailView> yichukuVeiwList = this.getcwbDetail(cList, customerList, showCustomerjSONArray, null, 0);

		return yichukuVeiwList;

	}

	/**
	 *
	 * @param cwb
	 * @param customerid
	 * @return
	 */
	public String checkyouhuowudan(User user, String cwb, long customerid, long currentbranchid) {
		cwb = this.cwbOrderService.translateCwb(cwb);
		FlowOrderTypeEnum flowOrderTypeEnum = FlowOrderTypeEnum.RuKu;

		if (this.jdbcTemplate.queryForInt("select count(1) from express_sys_on_off where type='SYSTEM_ON_OFF' and on_off='on' ") == 0) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.SYS_SCAN_ERROR);
		}

		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		if ((customerid > 0) && (co != null)) {
			// TODO 因为客户的货物会混着扫描
			customerid = co.getCustomerid();

		}

		Branch userbranch = this.branchDAO.getBranchById(currentbranchid);

		if (co == null) {
			if (customerid < 1) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.Y_H_W_D_WEI_XUAN_GONG_HUO_SHANG);
			}

			if ((userbranch.getSitetype() == BranchEnum.KuFang.getValue())
					&& (this.cwbALLStateControlDAO.getCwbAllStateControlByParam(CwbStateEnum.WUShuju.getValue(), FlowOrderTypeEnum.RuKu.getValue()) != null)) {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.WuShuJuYouHuoWuDanError);
			} else {
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}
		}
		return "";
	}

	/**
	 * 分站到货揽件未到货list
	 *
	 * @param page
	 * @return
	 */
	@RequestMapping("/getbranchimportlanjianweidaolist")
	public @ResponseBody List<CwbDetailView> getbranchimportlanjianweidaolist(@RequestParam(value = "page", defaultValue = "1") long page) {
		List<Branch> branchList = this.branchDAO.getAllBranches();
		List<CwbOrder> lanjianweidaohuolist = new ArrayList<CwbOrder>();
		lanjianweidaohuolist = this.cwbDAO.getLanJianWeiDaoZhanByBranchidForList(this.getSessionUser().getBranchid(), page);

		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");

		// 历史未到货
		List<CwbDetailView> lanjianweidaohuoViewlist = this.getcwbDetail(lanjianweidaohuolist, this.customerDAO.getAllCustomers(), showCustomerjSONArray, branchList, 1);

		return lanjianweidaohuoViewlist;

	}

	private String getCwbOrderTypeIdsWithoutJIT() {
		String typdIds = "";

		for (CwbOrderTypeIdEnum cwbOrderTypeIdEnum : CwbOrderTypeIdEnum.values()) {
			if (!cwbOrderTypeIdEnum.equals(CwbOrderTypeIdEnum.OXO_JIT)) {
				typdIds += "," + cwbOrderTypeIdEnum.getValue();
			}
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(typdIds)) {
			typdIds = typdIds.substring(1); // remove the last comma
		}
		return typdIds;
	}

	@RequestMapping("/daizhongzhuanExport")
	public void daizhongzhuanExport(HttpServletResponse response, HttpServletRequest request) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据
			String sqlstr = this.cwbDAO.getDaizhongzhuanSql(this.getSessionUser().getBranchid());

			if (sqlstr.length() == 0) {
				return;
			}
			final String sql = sqlstr;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = PDAController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = PDAController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = PDAController.this.branchDAO.getAllBranches();
					final List<Common> commonList = PDAController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = PDAController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = PDAController.this.remarkDAO.getRemarkByCwbs("");
					final Map<String, Map<String, String>> remarkMap = PDAController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = PDAController.this.reasonDAO.getAllReason();
					PDAController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								// sheet.setColumnWidth(i, (short)
								// (5000));
								// //设置列宽
								Object a = PDAController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = PDAController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : PDAController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : PDAController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : PDAController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
							 * gotoClassAuditingDAO .getGotoClassAuditingByGcaid
							 * (deliveryState .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0) {
							 * ispayup = "是"; } cwbspayupidMap.put(deliveryState
							 * .getCwb(), ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	@RequestMapping("/daizhongzhuanysmExport")
	public void daizhongzhuanysmExport(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "nextbranchid", defaultValue = "0") long nextbranchid) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			String sqlstr = "";
			List<String> cwbList = this.operationTimeDAO
					.getOperationTimeByFlowordertypeAndBranchidAndNext(this.getSessionUser().getBranchid(), nextbranchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
			if ((null != cwbList) && (cwbList.size() > 0)) {
				String cwbs = "";
				for (String cwb : cwbList) {
					cwbs += "'" + cwb + "',";
				}
				if (cwbs.contains(",")) {
					cwbs = cwbs.substring(0, cwbs.lastIndexOf(","));
				}
				if (cwbs.length() > 0) {
					sqlstr = "SELECT * from express_ops_cwb_detail where cwb in(" + cwbs + ") and state=1 ";
				}
			}
			// 查询出数据

			if (sqlstr.length() == 0) {
				return;
			}
			final String sql = sqlstr;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = PDAController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = PDAController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = PDAController.this.branchDAO.getAllBranches();
					final List<Common> commonList = PDAController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = PDAController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = PDAController.this.remarkDAO.getRemarkByCwbs("");
					final Map<String, Map<String, String>> remarkMap = PDAController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = PDAController.this.reasonDAO.getAllReason();
					PDAController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								Object a = PDAController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = PDAController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : PDAController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : PDAController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : PDAController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
							 * gotoClassAuditingDAO .getGotoClassAuditingByGcaid
							 * (deliveryState .getGcaid());
							 *
							 * if(goclass!=null&&goclass.getPayupid()!=0) {
							 * ispayup = "是"; } cwbspayupidMap.put(deliveryState
							 * .getCwb(), ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	/**
	 * ============================================== 代码块：分拣中转功能
	 *
	 * @since DMP v4.2.3
	 * @author jinghui.pan@pjbest.com
	 *         ===============================================
	 */

	/**
	 * 进入分拣中转出库的功能页面（明细）
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/sortingAndChangeExportWarehouse")
	public String sortingAndChangeExportWarehouse(Model model, @RequestParam(value = "branchid", defaultValue = "0") long nextbranchid,
			@RequestParam(value = "isscanbaleTag", defaultValue = "0") long isscanbaleTag) {

		// List<Branch> bList =
		// this.cwborderService.getNextPossibleBranches(this.getSessionUser());

		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		List<Customer> cList = this.customerDAO.getAllCustomers();

		long kfBranchid = this.getSessionUser().getBranchid();
		long zzBranchid = this.kfzdOrderService.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);

		// 获取下一站的列表哦
		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(kfBranchid, zzBranchid);

		// 系统设置是否显示订单备注
		boolean showCustomerSign = this.kfzdOrderService.isShowCustomerSign();

		// List<CwbDetailView> weichukuViewList =
		// kfzdOrderService.getSortAndChangeExportWeiChuKuCwbViewList(1,
		// nextbranchid);
		// List<CwbDetailView> yichukuViewList =
		// kfzdOrderService.getSortAndChangeExportYiChuKuCwbViewList(1,
		// nextbranchid);
		//
		// model.addAttribute("weichukulist", weichukuViewList);
		// model.addAttribute("yichukulist", yichukuViewList);
		//
		String isOpenDialog = this.systemInstallDAO.getSystemInstallByName("Dialog").getValue();// 是否显示提示框
		String isConfigZhongZhuan = zzBranchid != 0 ? "true" : "false";

		/***************add****************/
		// add by bruce shangguan 20160908  获取电子秤称重时长
		SystemInstall systemInstall = this.systemInstallDAO.getSystemInstall("weightTime") ;
		String weightTime = "0" ; // 电子秤称重时长默认为10秒
		if(systemInstall != null && !StringUtils.isEmpty(systemInstall.getValue()) && systemInstall.getValue().trim().matches("^0|([1-9][0-9]*)$")){
			weightTime = systemInstall.getValue() ;
		}
		model.addAttribute("weightTime", weightTime);
		// end 20160908
		/**************end****************/
		model.addAttribute("customerlist", cList);
		model.addAttribute("isConfigZhongZhuan", isConfigZhongZhuan);// “区域权限设置”是否配置了中转站
		model.addAttribute("isOpenDialog", isOpenDialog);
		model.addAttribute("branchlist", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("ck_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuBuYunXu.getText()));
		model.addAttribute("ckfb_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.ChuKuFengBao.getText()));
		model.addAttribute("showCustomerSign", showCustomerSign);
		model.addAttribute("isscanbaleTag", isscanbaleTag);
		return "pda/sortingAndChangeExportWarehouse";
	}

	/**
	 * 得到中转分拣出库的订单总数
	 *
	 * @param model
	 * @param customerid
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/getSortAndChangeOutSum")
	public @ResponseBody JSONObject getSortAndChangeOutSum(@RequestParam(value = "nextbranchid", required = false, defaultValue = "0") long nextbranchid,
			@RequestParam(value = "cwbstate", required = false, defaultValue = "1") int cwbstate, Model model) {
		JSONObject obj = new JSONObject();

		long kfBranchid = this.getSessionUser().getBranchid();
		long zzBranchId = this.kfzdOrderService.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);

		// 统计分拣库出库扫描的数据
		List<Map<String, Object>> weichukudata = this.cwbDAO.getChukubyBranchid(kfBranchid, nextbranchid, CwbStateEnum.PeiShong.getValue());
		obj.put("weichukucount_fj", weichukudata.get(0).get("count"));
		obj.put("weichukusum_fj", weichukudata.get(0).get("sum"));

		// 统计中转出库扫描的数据
		weichukudata = this.cwbDAO.getZhongZhuanZhanChukubyBranchid(zzBranchId, nextbranchid);
		obj.put("weichukucount_zz", weichukudata.get(0).get("count"));
		obj.put("weichukusum_zz", weichukudata.get(0).get("sum"));

		// 已出库总数
		long yichukucount = this.kfzdOrderService.getSortAndChangeYiChuKuCount(nextbranchid);

		obj.put("yichukucount", yichukucount);

		return obj;
	}

	/**
	 * 判断订单是否为中转，通过订单操作环节是否为中转入库或中转出库或者订单的下站点是中转
	 */
	private boolean isZhongzhuanOrder(CwbOrder co) {
		long nextbranchid = co.getNextbranchid();
		Branch nextbranch = this.branchDAO.getBranchById(nextbranchid);

		return ((FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue() == co.getFlowordertype()) || (FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue() == co.getFlowordertype())) || (nextbranch == null) ? true
				: nextbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue();

	}

	/**
	 * 分拣中转出库批量功能======================================
	 *
	 */
	@RequestMapping("/cwbSortingAndChangeExportWarehouseBatch")
	public String cwbSortingAndChangeExportWarehouseBatch(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long nextbranchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag) {
		long thissuccess = 0;
		List<Customer> cList = this.customerDAO.getAllCustomers();// 获取供货商列表
		List<User> uList = this.userDAO.getUserByRole(3);
		List<Truck> tlist = this.truckDAO.getAllTruck();

		List<JSONObject> objList = new ArrayList<JSONObject>();
		long allcwbnum = 0;
		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			allcwbnum++;
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单

				CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
				if (co == null) {
					throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
				}

				CwbOrder cwbOrder = null;

				// 判断订单状态是否为中转
				if (this.isZhongzhuanOrder(co)) {

					cwbOrder = this.cwbOrderService.sortAndChangeOutWarehouse(this.getSessionUser(), cwb, scancwb, driverid, truckid, nextbranchid, 0, confirmflag == 1, "", "", 0, false, false);

				} else {
					cwbOrder = this.cwbOrderService.outWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, nextbranchid, 0, confirmflag == 1, "", "", 0, false, false);
					this.tpsCwbFlowService.save(cwbOrder, scancwb, FlowOrderTypeEnum.ChuKuSaoMiao,this.getSessionUser().getBranchid(),null,true,null,null);
				}

				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				for (Customer c : cList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
				thissuccess++;
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);

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

			}
			objList.add(obj);
		}
		model.addAttribute("objList", objList);

		long kfBranchid = this.getSessionUser().getBranchid();
		long zzBranchid = this.kfzdOrderService.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);

		// 系统设置是否显示订单备注
		boolean showCustomerSign = this.kfzdOrderService.isShowCustomerSign();

		/*
		 * 计算中转出库明细 start
		 */
		// 统计中转出库扫描的数据
		List<Map<String, Object>> weichukudata = this.cwbDAO.getZhongZhuanZhanChukubyBranchid(zzBranchid, nextbranchid);

		model.addAttribute("weichukucount_zz", weichukudata.get(0).get("count"));
		model.addAttribute("weichukusum_zz", weichukudata.get(0).get("sum"));
		/* * end * */

		/* * 计算分拣库出库明细 start */
		int cwbstate = CwbStateEnum.PeiShong.getValue();

		// 统计分拣库出库扫描的数据
		weichukudata = this.cwbDAO.getChukubyBranchid(kfBranchid, nextbranchid, cwbstate);

		model.addAttribute("weichukucount_fj", weichukudata.get(0).get("count"));
		model.addAttribute("weichukusum_fj", weichukudata.get(0).get("sum"));
		/* * end * */

		// 未出库分页列表
		List<CwbDetailView> weichukuViewList = this.kfzdOrderService.getSortAndChangeExportWeiChuKuCwbViewList(1, nextbranchid);

		// 已出库分页列表
		List<CwbDetailView> yichukuViewList = this.kfzdOrderService.getSortAndChangeExportYiChuKuCwbViewList(1, nextbranchid);

		// 一票多件缺货数量
		long lesscwbnum = this.kfzdOrderService.getSortAndChangeYpdjLessCwbNum(nextbranchid);

		// 已出库总数
		long yichukucount = this.kfzdOrderService.getSortAndChangeYiChuKuCount(nextbranchid);

		model.addAttribute("weiChuKuList", weichukuViewList);
		model.addAttribute("yiChuKuList", yichukuViewList);
		model.addAttribute("yichukucount", yichukucount);
		String isConfigZhongZhuan = zzBranchid != 0 ? "true" : "false";

		// List<Branch> bList =
		// this.cwborderService.getNextPossibleBranches(this.getSessionUser());
		// 获取下一站的列表哦
		List<Branch> bList = this.cwbOrderService.getNextPossibleBranches(kfBranchid, zzBranchid);

		model.addAttribute("isConfigZhongZhuan", isConfigZhongZhuan);// “区域权限设置”是否配置了中转站
		model.addAttribute("branchList", bList);
		model.addAttribute("userList", uList);
		model.addAttribute("truckList", tlist);
		model.addAttribute("customerList", cList);
		// model.addAttribute("weiChuKuList", weichukuViewlist);// 待出库数据
		// model.addAttribute("yiChuKuList", yichukuViewlist);
		// model.addAttribute("count", cwbObj.get(0).get("count"));// 待出库总数
		// model.addAttribute("sum", cwbObj.get(0).get("sum"));// 待出库件数总数
		// model.addAttribute("yichukucount", cwbyichukuList.size());
		model.addAttribute("lesscwbnum", lesscwbnum);// 缺货件数

		String msg = "";
		if (cwbs.length() > 0) {
			msg = "成功扫描" + thissuccess + "单，异常" + (allcwbnum - thissuccess) + "单";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("showCustomerSign", showCustomerSign);
		return "pda/sortingAndChangeExportWarehouseBatch";
	}
	
	/**
	 * 分拣中转出站扫描
	 *
	 */
	@RequestMapping("/cwbSortingAndChangeExportWarehouse/{cwb}")
	public @ResponseBody ExplinkResponse cwbSortingAndChangeExportWarehouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag,
			@RequestParam(value = "requestbatchno", required = true, defaultValue = "") String requestbatchno, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno,
			@RequestParam(value = "comment", required = false, defaultValue = "") String comment, @RequestParam(value = "reasonid", required = false, defaultValue = "0") long reasonid,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long deliverybranchid,
			@RequestParam(value = "carrealweight", required = false, defaultValue = "0") BigDecimal carrealweight) {

		ExplinkResponse explinkResponse = null;
		String translated = this.cwbOrderService.translateCwb(cwb);
		CwbOrder co = this.cwbDAO.getCwbByCwb(translated);
		if (co == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		// 判断订单状态是否为中转
		if (this.isZhongzhuanOrder(co)) {
			// 调用中转出库扫描逻辑
			explinkResponse = this._cwbchangeexportwarhouse(model, request, response, cwb, branchid, driverid, truckid, confirmflag, requestbatchno, baleno, comment, reasonid, false);
		} else {
			// 调用分拣出库扫描逻辑
			explinkResponse = this.cwbexportwarhouse(model, request, response, cwb, branchid, driverid, truckid, confirmflag, requestbatchno, baleno, comment, reasonid,carrealweight);
		}

		return explinkResponse;
	}

	/**
	 * 得到分拣中转出库一票多件缺货件数的统计
	 *
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/getSortAndChangeOutQueSum")
	public @ResponseBody JSONObject getSortAndChangeOutQueSum(@RequestParam(value = "nextbranchid", required = false, defaultValue = "-1") long nextbranchid) {
		JSONObject obj = new JSONObject();
		obj.put("lesscwbnum", this.kfzdOrderService.getSortAndChangeYpdjLessCwbNum(nextbranchid));

		return obj;
	}

	/**
	 * 得到 分拣和中转出库 一票多件缺件的分页
	 *
	 * @return
	 */
	@RequestMapping("/getSortAndChangeOutQueListPage")
	public @ResponseBody List<JSONObject> getSortAndChangeOutQueListPage(Model model, @RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "nextbranchid", required = false, defaultValue = "-1") long nextbranchid) {
		List<JSONObject> quejianList = this.kfzdOrderService.getSortAndChangeYpdjLessCwbList(nextbranchid, page);
		return quejianList;
	}

	/**
	 * 得到 分拣和中转出库明细 未入库 list
	 *
	 * @return
	 */
	@RequestMapping("/getSortAndChangeExportWeiChuKuList")
	public @ResponseBody List<CwbDetailView> getSortAndChangeExportWeiChuKuList(@RequestParam(value = "branchid", defaultValue = "0") long nextbranchid,
			@RequestParam(value = "page", defaultValue = "1") long page) {
		// 未出库明细
		List<CwbDetailView> weichukuViewList = this.kfzdOrderService.getSortAndChangeExportWeiChuKuCwbViewList(page, nextbranchid);
		return weichukuViewList;
	}

	/**
	 * 得到 分拣和中转出库明细 已入库 list
	 *
	 * @return
	 */
	@RequestMapping("/getSortAndChangeExportYiChuKuList")
	public @ResponseBody List<CwbDetailView> getSortAndChangeExportYiChuKuList(@RequestParam(value = "branchid", defaultValue = "0") long nextbranchid,
			@RequestParam(value = "page", defaultValue = "1") long page) {
		// 已出库明细
		List<CwbDetailView> yichukuViewList = this.kfzdOrderService.getSortAndChangeExportYiChuKuCwbViewList(page, nextbranchid);
		return yichukuViewList;
	}

	@RequestMapping("/connect")
	public  void connectMiddleWare(@RequestParam(value = "entranceno") String entranceno) {
		if(!entranceno.isEmpty() && !entranceno.equals("-1")) {
			String entranceIP=this.entranceDAO.getEntranceByNo(entranceno).getEntranceip();
			Map<String, SocketClient> socketMap=this.autoAllocationService.getSocketMap();
			SocketClient sc=socketMap.get(entranceIP);
			logger.info("中间件[" + entranceIP + "]连接状态：" + (sc==null? "" : sc.Clientstate.State) + ".（注：0没任何登录，1已连接未登录，2已登录，3已触发关闭引擎。）");
			if(null==sc||sc.Clientstate.State!=2){
				logger.info("尝试连接中间件 ："+entranceIP);
				sc=this.autoAllocationService.startConnect(entranceIP);
				socketMap.put(entranceIP, sc);
			}
			
			sleepAfterConnect();		// 连接后线程睡眠一段时间，以获得准确的连接状态
			this.autoAllocationService.init(entranceIP);	//初始化连接
			
			socketMap=this.autoAllocationService.getSocketMap();
			sc=socketMap.get(entranceIP);
			logger.info("中间件[" + entranceIP + "]连接状态：" + (sc==null? "" : sc.Clientstate.State) + ".（注：0没任何登录，1已连接未登录，2已登录，3已触发关闭引擎。）");
			if(null==sc||sc.Clientstate.State!=2){
				throw new CwbException("", FlowOrderTypeEnum.RuKu.getValue(), ExceptionCwbErrorTypeEnum.AUTO_ALLOCATING_BUT_DISCOUNTED_PLEASE_HANDLE);
			}
		}
	}
	
	@RequestMapping("/autoConnectAll")
	public void autoConnectAllMiddleWare() {
		List<Entrance> eList=this.entranceDAO.getAllEnableEntrances();//分拨入口查询
		//是否开启自动分拣设置
		String autoAllocatingSwitch = this.systemInstallDAO.getSystemInstall("AutoAllocating").getValue();
		/**
		 * 对接自动分拨的中间件,放在进入页面的时机是因为在系统启动时很有可能没有打开中间件客户端
		 */
		if(autoAllocatingSwitch.equals("1")){
			this.createSocketConnectMap(eList);
		}
		
	}
	
	
	@RequestMapping("/flush")
	public  void flushQueue(@RequestParam(value = "entranceno") String entranceno) {
		if(!entranceno.isEmpty() && !entranceno.equals("-1")) {
			String entranceIP=this.entranceDAO.getEntranceByNo(entranceno).getEntranceip();
			Map<String, SocketClient> socketMap=this.autoAllocationService.getSocketMap();
			SocketClient sc=socketMap.get(entranceIP);
			if(null==sc||sc.Clientstate.State!=2){
				return;
			}else{
				AutoAllocationParam param=new AutoAllocationParam();
				this.autoAllocationService.flushQueue(entranceIP, param);
			}
		}
		
	}
	/**
	 * ============================================== 代码块：分拣中转功能 end
	 * ===============================================
	 */
	public void doCwbbranchexportwarhouse(List<String> strList, BatchCount batchCount, List<Customer> customerList, List<JSONObject> objList, long branchid, long driverid, long confirmflag) {
		for (String cwb : strList) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			batchCount.setAllcwbnum(batchCount.getAllcwbnum() + 1);
			JSONObject obj = new JSONObject();
			String scancwb = cwb;
			cwb = this.cwbOrderService.translateCwb(cwb);
			obj.put("cwb", cwb);
			try {// 成功订单
				CwbOrder cwbOrder = this.cwbOrderService.outWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, branchid, 0, confirmflag == 1, "", "", 0, false, false);
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "000000");
				for (Customer c : customerList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customername", c.getCustomername());
						break;
					}
				}
				/* thissuccess++; */
				batchCount.setThissuccess(batchCount.getThissuccess() + 1);
			} catch (CwbException ce) {// 出现验证错误
				this.logger.error("cwb="+cwb,ce);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
					List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
					String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
					String[] showcustomer = list.get(0).getCustomerid().split(",");
					Object a = "";
					for (String s : showcustomer) {
						if (s.equals(cwbcustomerid)) {
							if (s.equals(cwbcustomerid)) {
								try {
									a = cwbOrder.getClass().getMethod("get" + list.get(0).getRemark()).invoke(cwbOrder);
								} catch (Exception e) {
									this.logger.error("", e);
									a = "Erro";
								}
							}
						}
					}
					obj.put("showRemark", a);
				}
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);

				obj.put("cwbOrder", cwbOrder);
				obj.put("errorcode", ce.getError().getValue());
				obj.put("errorinfo", ce.getMessage());
				if (cwbOrder == null) {// 如果无此订单
					obj.put("customername", "");
				} else {
					for (Customer c : customerList) {
						if (c.getCustomerid() == cwbOrder.getCustomerid()) {
							obj.put("customername", c.getCustomername());
							break;
						}
					}
				}

			}
			objList.add(obj);
		}
	}
	
	/**
	 * 中转库出库扫描公共逻辑
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 * @param branchid
	 * @param driverid
	 * @param truckid
	 * @param confirmflag
	 * @param requestbatchno
	 * @param baleno
	 * @param comment
	 * @param reasonid
	 * @param deliverybranchid
	 * @param isSingleZhongzhuan
	 *            是否当前页面只有中转库出库扫描
	 * @return
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	private ExplinkResponse _cwbchangeexportwarhouse(Model model, HttpServletRequest request, HttpServletResponse response, String cwb, long branchid, long driverid, long truckid, long confirmflag,
			String requestbatchno, String baleno, String comment, long reasonid, boolean isSingleZhongzhuan) {

		JSONObject obj = new JSONObject();

		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);

		long successCount = request.getSession().getAttribute(baleno + "-successCount") == null ? 0 : Long.parseLong(request.getSession().getAttribute(baleno + "-successCount").toString());
		String scancwb = cwb;
		cwb = this.cwbOrderService.translateCwb(cwb);

		CwbOrder cwbOrder = null;

		if (isSingleZhongzhuan) {
			cwbOrder = this.cwbOrderService.changeoutWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid,
					requestbatchno == null ? 0 : requestbatchno.length() == 0 ? 0 : Long.parseLong(requestbatchno), confirmflag == 1, comment, baleno, reasonid, true, false);  //把iszhongzhuanout参数改为true，防止未中转入库直接中转出库 -----刘武强20161103
		} else {
			cwbOrder = this.cwbOrderService.sortAndChangeOutWarehouse(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid, requestbatchno == null ? 0 : requestbatchno.length() == 0 ? 0
					: Long.parseLong(requestbatchno), confirmflag == 1, comment, baleno, reasonid, false, false);
		}

		obj.put("packageCode", baleno);
		obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
		if (cwbOrder.getNextbranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
			obj.put("cwbbranchname", branch.getBranchname());
			obj.put("cwbbranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbbranchname", "");

		}
		if (cwbOrder.getDeliverybranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
			obj.put("cwbdeliverybranchname", branch.getBranchname());
			obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
		} else {
			obj.put("cwbdeliverybranchname", "");
			obj.put("cwbdeliverybranchnamewav", "");
		}
		if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			obj.put("cwbgaojia", "true");
			explinkResponse.addShortWav(this.getErrorWavFullPath(request, WavFileName.GJ));
		} else {
			obj.put("cwbgaojia", "");
		}
		// 查询系统设置，得到name=showCustomer的express_set_system_install表中的value,加入到obj中
		String jyp = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		List<JsonContext> list = PDAController.test("[" + jyp + "]", JsonContext.class);// 把json转换成list
		String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
		String[] showcustomer = list.get(0).getCustomerid().split(",");
		for (String s : showcustomer) {
			if (s.equals(cwbcustomerid)) {
				CwbOrder order = this.cwbDAO.getCwbByCwb(cwb);
				Object a;
				try {
					a = order.getClass().getMethod("get" + list.get(0).getRemark()).invoke(order);
					obj.put("showRemark", a);
				} catch (Exception e) {
					this.logger.error("", e);
					obj.put("showRemark", "Erro");
				}
			}
		}
		// 加入货物类型音频文件.
		this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
		String wavPath = null;
		if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
			if (this.isPlayYPDJSound()) {
				wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl();
			}
		}

		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			if (!baleno.equals("") && !baleno.equals("0")) {
				successCount++;
				request.getSession().setAttribute(baleno + "-successCount", successCount);
				explinkResponse.setErrorinfo("\n按包出库成功，已出库" + successCount + "件");
				obj.put("successCount", successCount);
			}
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
		} else {
			wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}
		explinkResponse.addLastWav(wavPath);

		return explinkResponse;
	}

    @RequestMapping("/isExtractInput/{cwb}")
    public @ResponseBody ExplinkResponse isExtractInput(HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb){
        JSONObject obj = new JSONObject();

        ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);

        CwbOrder cwbOrder = cwbOrderService.getCwbByCwb(cwb);

        if (cwbOrder != null){
            if(0 == cwbOrder.getIsadditionflag() && CwbOrderTypeIdEnum.Express.getValue() == cwbOrder.getCwbordertypeid()){
                obj.put("result", true);
            }else {
                obj.put("result", false);
            }
        }
        return explinkResponse;

    }
	/**
	 * ============================================== 代码块：分拣中转功能 end
	 * ===============================================
	 */
	/**
	 * 建立系统中的各个中间件socket连接
	 * @param eList
	 */
	private void createSocketConnectMap(List<Entrance> eList) {
		logger.info("初始化自动连接所有中间件");
		for(Entrance e:eList){
			Map<String, SocketClient> socketMap=this.autoAllocationService.getSocketMap();
			SocketClient sc=socketMap.get(e.getEntranceip());
			if(null==sc||sc.Clientstate.State!=2){
				logger.info("尝试连接中间件："+e.getEntranceip());
				sc=this.autoAllocationService.startConnect(e.getEntranceip());
				socketMap.put(e.getEntranceip(), sc);
			}
		}
		
		sleepAfterConnect();		// 连接后线程睡眠一段时间，以获得准确的连接状态
		
		this.autoAllocationService.init(eList);	//初始化连接
		
		for(Entrance e:eList){
			Map<String, SocketClient> socketMap=this.autoAllocationService.getSocketMap();
			SocketClient sc=socketMap.get(e.getEntranceip());
			logger.info("中间件[" + e.getEntranceip() + "]连接状态：" + (sc==null? "" : sc.Clientstate.State) + ".（注：0没任何登录，1已连接未登录，2已登录，3已触发关闭引擎。）");
		}
		
		
	}
	
	/**
	 * 对接自动分拨的中间件,往队列中添加一个包裹
	 */
	private void addQueue(String outputNo,String entranceno,String cwb,String branchName, String scancwb, byte intowarehouseType){
		if(!entranceno.isEmpty() && !entranceno.equals("-1")) {
			String autoAllocatingSwitch=this.systemInstallDAO.getSystemInstallByName("AutoAllocating").getValue();
			//启用了自动分拨功能
			if(autoAllocatingSwitch.equals("1")){
				if(null!=outputNo && outputNo.length()==4 && outputNo.matches("^[0-9]{3}[01]{1}$")){		// 并设置了机构的对应滑槽口号为“0或1结尾的4位数字”
					String export = outputNo.substring(0, 3);	//滑槽口编号前3位为出货口
					String direction = outputNo.substring(3);	//滑槽口编号第4位定义出货为正向/反向
					String entranceIP=this.entranceDAO.getEntranceByNo(entranceno).getEntranceip();
					AutoAllocationParam param=new AutoAllocationParam(cwb,export,direction,branchName);
					this.autoAllocationService.createEmptyMsgLog(param, cwb, scancwb, intowarehouseType, entranceIP);
					//发送消息
					this.autoAllocationService.addQueue(entranceIP, param);
				}
				
			}
		}
	}
	
	/*
	 * 检查是否：使用了自动分拨功能，却未能连上分拨机
	 */
	private boolean isAutoAllocatingButDisconnected(String entranceno) {
		boolean isAutoAllocatingButDisconnected = false;
		if(!entranceno.isEmpty() && !entranceno.equals("-1")) {
			String autoAllocatingSwitch=this.systemInstallDAO.getSystemInstallByName("AutoAllocating").getValue();
			//启用自动分拨并设置了出货口
			if(autoAllocatingSwitch.equals("1")){
				String entranceIP=this.entranceDAO.getEntranceByNo(entranceno).getEntranceip();
				
				//检查是否连接断开，若未连上先尝试重连一次，并休眠一段时间以等待状态异步更新
				Map<String, SocketClient> socketMap=this.autoAllocationService.getSocketMap();
				SocketClient sc=socketMap.get(entranceIP);
				logger.info("中间件[" + entranceIP + "]连接状态：" + (sc==null? "" : sc.Clientstate.State) + ".（注：0没任何登录，1已连接未登录，2已登录，3已触发关闭引擎。）");
				if(null==sc||sc.Clientstate.State!=2){
					logger.info("尝试连接中间件："+entranceIP);
					sc=this.autoAllocationService.startConnect(entranceIP);
					socketMap.put(entranceIP, sc);
					
					sleepAfterConnect();		// 连接后线程睡眠一段时间，以获得准确的连接状态
					
					this.autoAllocationService.init(entranceIP);	//初始化连接
				}
				
				//再次检查是否连接断开，若仍然未连上则抛出异常，不能入库
				socketMap=this.autoAllocationService.getSocketMap();
				sc=socketMap.get(entranceIP);
				logger.info("中间件[" + entranceIP + "]连接状态：" + (sc==null? "" : sc.Clientstate.State) + ".（注：0没任何登录，1已连接未登录，2已登录，3已触发关闭引擎。）");
				if(null==sc||sc.Clientstate.State!=2){
					isAutoAllocatingButDisconnected = true;
				}
			}
		}
		return isAutoAllocatingButDisconnected;
	}
	
	/*
	 * 连接后线程睡眠一段时间，以获得准确的连接状态
	 */
	private void sleepAfterConnect() {
		//武汉自动化连接中间件等候返回状态时间间隔（毫秒）
		int intervalInMs = 200;
		SystemInstall intervalInMsSystemInstall = this.systemInstallDAO.getSystemInstall("autoAllocatingConnecteWaitIntervalInMs");
		if(intervalInMsSystemInstall != null){
			intervalInMs = Integer.parseInt(intervalInMsSystemInstall.getValue());
		}
		try {
			Thread.sleep(intervalInMs);
			logger.info("连接中间件后等候"+intervalInMs+"毫秒");
		} catch (InterruptedException e) {
			// do nothing
		}
	}
	
	/**
	 * 过滤停用的站点
	 * @author leo01.liao
	 * @param listBranchBeFiltered
	 * @return
	 */
	private List<Branch> filterNoEffectBranch(List<Branch> listBranchBeFiltered){
		List<Branch> listBranch = new ArrayList<Branch>();
		if(listBranchBeFiltered == null || listBranchBeFiltered.isEmpty()){
			return listBranch;
		}
		
		for(Branch branch : listBranchBeFiltered){
			if(branch.getBrancheffectflag() == null || branch.getBrancheffectflag().trim().equals("0")){
				continue;
			}
			
			listBranch.add(branch);
		}
		
		return listBranch;
	}
	
	/**
	 * 二次分拨（不入库，只上自动分拨）
	 * @param model
	 * @param request
	 * @param response
	 * @param cwb
	 */
	@RequestMapping("/autoAllocateAgainWithoutIntoWarehouse/{cwb}")
	public @ResponseBody ExplinkResponse autoAllocateAgainWithoutIntoWarehouse(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("cwb") String cwb,
			@RequestParam(value = "autoallocatid", defaultValue = "-1") String entranceno){
		if(isAutoAllocatingButDisconnected(entranceno)){
			throw new CwbException(cwb, FlowOrderTypeEnum.RuKu.getValue(), ExceptionCwbErrorTypeEnum.AUTO_ALLOCATING_BUT_DISCOUNTED_PLEASE_HANDLE);
		}
		
		long startTime = System.currentTimeMillis();
		ExplinkResponse resp = null;
		String scancwb = cwb;
		CwbOrder cwbOrder = new CwbOrder();

		String branchName="";//配送站点名称
		String outputNo="";//出货口编号
		
		cwb = this.cwbOrderService.translateCwb(cwb);
		cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		
		if (cwbOrder == null) {
			throw new CwbException(cwb, FlowOrderTypeEnum.RuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		
		try{
			JSONObject obj = new JSONObject();
			resp = new ExplinkResponse("000000", CwbFlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText(), obj);
			
			obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
			obj.put("cwbcustomername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());

			if (cwbOrder.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				obj.put("cwbbranchname", branch.getBranchname());
			} else {
				obj.put("cwbbranchname", "");
			}
			
			/*
			 * 单票成功订单，有站点提示语音的，忽略通用提示音
			 */
			// 通用提示音
			String wavPath = null;
			// 一票多件提示音乐
			String multiTipPath = null;
			if (resp.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
				wavPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.OK.getVediourl());
			} else {
				wavPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.SYS_ERROR.getVediourl());
			}
			if ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1)) {
				resp.setErrorinfo(resp.getErrorinfo() + "\n一票多件");
				if (this.isPlayYPDJSound()) {
					multiTipPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
					resp.addLongWav(multiTipPath);
				}
			}
			if (cwbOrder.getDeliverybranchid() != 0) {
				// 如果存在站点声音，忽略通用提示音
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
				branchName=branch.getBranchname();//站点中文名
				outputNo=branch.getOutputno();//站点出货口
				obj.put("cwbdeliverybranchname", branch.getBranchname());
				if (!this.isStringEmpty(branch.getBranchwavfile())) {
					String fullPath = this.getWavFullPath(request, branch.getBranchwavfile());
					resp.addLastWav(fullPath);
					obj.put("cwbdeliverybranchnamewav", fullPath);
				} else {
					// 存在站点,但未设置声音，也使用通用提示音
					resp.addLongWav(wavPath);
				}
			} else {
				// 如果不存在站点声音，使用通用提示音
				resp.addLongWav(wavPath);
				obj.put("cwbdeliverybranchname", "");
				obj.put("cwbdeliverybranchnamewav", "");
			}
			/**
			 * 对接自动分拨的中间件
			 */
			this.addQueue(outputNo, entranceno, cwb, branchName, scancwb, Constants.INTOWAHOUSE_TYPE_AUTO_ALLOCATE_AGAIN);		
		
			this.logger.info("二次分拣扫描的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
			return resp;
		} catch (CwbException e) {
			this.logger.error("cwb="+cwb,e);
			ExplinkResponse explinkResponse = new ExplinkResponse(e.getError().getValue() + "", e.getMessage(), null);
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
			// 添加货物类型声音.
			this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
			// 单号不存在异常.
			this.addNoOrderWav(request, e, explinkResponse);
			explinkResponse.addShortWav(this.getErrorWavFullPath(request, CwbOrderPDAEnum.SYS_ERROR.getVediourl()));
			return explinkResponse;
		}
	}
}
