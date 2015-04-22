package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbApplyTuiHuoDAO;
import cn.explink.dao.CwbApplyZhongZhuanDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TruckDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbApplyTuiHuo;
import cn.explink.domain.CwbApplyZhongZhuan;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbRouteService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.LogToDayService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@RequestMapping("/cwbapply")
@Controller
public class CwbApplyController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	EmailDateDAO emaildateDao;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	TruckDAO truckDAO;
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
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	OrderFlowDAO orderFlowDAO;

	@Autowired
	ExportService exportService;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	CwbApplyTuiHuoDAO cwbApplyTuiHuoDAO;
	@Autowired
	CwbApplyZhongZhuanDAO cwbApplyZhongZhuanDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	JointService jointService;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 申请为退货
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/applytoTuiHuo")
	public String applytoTuiHuo(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		String quot = "'", quotAndComma = "',";
		if (cwb.length() > 0) {
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				String lastcwb = this.cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = this.cwbDAO.getCwbByCwb(lastcwb);
				if (co != null) {
					cwborderlist.add(co);
				}
			}
			List<Customer> customerList = this.customerDao.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllEffectBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDAO.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			Map<String, CwbApplyTuiHuo> cwbapplyList = this.cwbApplyTuiHuoDAO.getCwbApplyTuiHuoByCwbs(cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("cwbList", this.getTuiHuoCwbOrderView(cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList, cwbapplyList));
			model.addAttribute("branchList", branchList);
			model.addAttribute("customerList", this.customerDao.getAllCustomers());
			model.addAttribute("userList", this.userDAO.getAllUser());
			List<Branch> tuihuobranchList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.TuiHuo.getValue() + "");
			model.addAttribute("tuihuobranchList", tuihuobranchList);
		}

		return "cwbapply/applytoTuiHuo";
	}

	/**
	 * 申请为中转
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/applytoZhongZhuan")
	public String applytoZhongZhuan(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		String quot = "'", quotAndComma = "',";
		if (cwb.length() > 0) {
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				String lastcwb = this.cwborderService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = this.cwbDAO.getCwbByCwb(lastcwb);
				if (co != null) {
					cwborderlist.add(co);
				}
			}
			List<Customer> customerList = this.customerDao.getAllCustomers();
			List<CustomWareHouse> customerWareHouseList = this.customWareHouseDAO.getAllCustomWareHouse();
			List<Branch> branchList = this.branchDAO.getAllEffectBranches();
			List<User> userList = this.userDAO.getAllUser();
			List<Reason> reasonList = this.reasonDAO.getAllReason();
			List<Remark> remarkList = this.remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			Map<String, CwbApplyZhongZhuan> cwbapplyList = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanByCwbs(cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("cwbList", this.getZhongZhuanCwbOrderView(cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList, cwbapplyList));
			model.addAttribute("branchList", branchList);
			model.addAttribute("customerList", this.customerDao.getAllCustomers());
			model.addAttribute("userList", this.userDAO.getAllUser());
			List<Branch> zhongzhuanbranchList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhongZhuan.getValue() + "");
			model.addAttribute("zhongzhuanbranchList", zhongzhuanbranchList);
		}

		return "cwbapply/applytoZhongZhuan";
	}

	/**
	 * 提交申请为退货
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/auditApplyTuiHuo")
	public @ResponseBody
	String auditApplyTuiHuo(Model model, HttpServletRequest request) {
		this.logger.info("--申请为退货 开始--");
		String branchidsAndRemarks = request.getParameter("branchidsAndRemarks");
		if (branchidsAndRemarks == null) {
			return 0 + "_s_" + 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(branchidsAndRemarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String content = rJson.getString(i);
			if (content.equals("") || (content.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_content = content.split("_s_");
			if (cwb_content.length == 3) {
				try {
					if (!cwb_content[1].equals("0") && (cwb_content[2].trim().length() > 0)) {
						CwbOrder co = this.cwbDAO.getCwbByCwb(this.cwborderService.translateCwb(cwb_content[0]));

						if (this.cwbApplyTuiHuoDAO.getCwbApplyTuiHuoByCwbCount(co.getCwb()) == 0) {
							CwbApplyTuiHuo cwbApplyTuiHuo = new CwbApplyTuiHuo();
							cwbApplyTuiHuo.setApplybranchid(this.getSessionUser().getBranchid());
							cwbApplyTuiHuo.setApplytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							cwbApplyTuiHuo.setApplytuihuobranchid(Long.valueOf(cwb_content[1]));
							cwbApplyTuiHuo.setApplyuserid(this.getSessionUser().getUserid());
							cwbApplyTuiHuo.setCustomerid(co.getCustomerid());
							cwbApplyTuiHuo.setCwbordertypeid(co.getCwbordertypeid());
							cwbApplyTuiHuo.setPaybackfee(co.getPaybackfee());
							cwbApplyTuiHuo.setReceivablefee(co.getReceivablefee());
							cwbApplyTuiHuo.setCwb(co.getCwb());
							cwbApplyTuiHuo.setApplytuihuoremark(cwb_content[2]);

							this.cwbApplyTuiHuoDAO.creAndUpdateCwbApplyTuiHuo(cwbApplyTuiHuo);
							successCount++;
							failureCount--;
							this.logger.info("{} 申请退货成功", content);
						} else {
							this.logger.error("{} 申请退货失败,订单未处理不允许重复申请", content);
						}
					} else {
						this.logger.error("{} 申请退货失败,没有选择退货站", content);
					}
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.error("{} 申请退货失败,{}", content, e.getMessage());
				}
			} else {
				this.logger.info("{} 申请退货失败，格式不正确");
			}
		}

		return successCount + "_s_" + failureCount;
	}

	/**
	 * 提交申请为中转
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/auditApplyZhongZhuan")
	public @ResponseBody
	String auditApplyZhongZhuan(Model model, HttpServletRequest request) {
		this.logger.info("--申请为中转 开始--");
		String branchidsAndRemarks = request.getParameter("branchidsAndRemarks");
		if (branchidsAndRemarks == null) {
			return 0 + "_s_" + 0 + "_s_" + 0;
		}
		JSONArray rJson = JSONArray.fromObject(branchidsAndRemarks);
		long successCount = 0;
		long failureCount = rJson.size();
		for (int i = 0; i < rJson.size(); i++) {
			String content = rJson.getString(i);
			if (content.equals("") || (content.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_content = content.split("_s_");
			if (cwb_content.length == 3) {
				try {
					if (!cwb_content[1].equals("0") && (cwb_content[2].trim().length() > 0)) {
						CwbOrder co = this.cwbDAO.getCwbByCwb(this.cwborderService.translateCwb(cwb_content[0]));

						if (this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanByCwbCount(co.getCwb()) == 0) {
							CwbApplyZhongZhuan cwbApplyZhongZhuan = new CwbApplyZhongZhuan();
							cwbApplyZhongZhuan.setApplybranchid(this.getSessionUser().getBranchid());
							cwbApplyZhongZhuan.setApplytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							cwbApplyZhongZhuan.setApplyzhongzhuanbranchid(Long.valueOf(cwb_content[1]));
							cwbApplyZhongZhuan.setApplyuserid(this.getSessionUser().getUserid());
							cwbApplyZhongZhuan.setCustomerid(co.getCustomerid());
							cwbApplyZhongZhuan.setCwbordertypeid(co.getCwbordertypeid());
							cwbApplyZhongZhuan.setPaybackfee(co.getPaybackfee());
							cwbApplyZhongZhuan.setReceivablefee(co.getReceivablefee());
							cwbApplyZhongZhuan.setCwb(co.getCwb());
							cwbApplyZhongZhuan.setApplyzhongzhuanremark(cwb_content[2]);

							this.cwbApplyZhongZhuanDAO.creAndUpdateCwbApplyZhongZhuan(cwbApplyZhongZhuan);
							successCount++;
							failureCount--;
							this.logger.info("{} 申请中转成功", content);
						} else {
							this.logger.error("{} 申请中转失败,订单未处理不允许重复申请", content);
						}
					} else {
						this.logger.error("{} 申请中转失败,没有选择中转站", content);
					}
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.error("{} 申请中转失败,{}", content, e.getMessage());
				}
			} else {
				this.logger.info("{} 申请中转失败，格式不正确");
			}
		}

		return successCount + "_s_" + failureCount;
	}

	/**
	 * 历史退货订单list
	 * 
	 * @param model
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param ishandle
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/applytoTuiHuolist/{page}")
	public String applytoTuiHuolist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "ishandle", required = false, defaultValue = "0") long ishandle,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, HttpServletResponse response, HttpServletRequest request) {
		Page pageparm = new Page();
		List<CwbApplyTuiHuo> cwbApplyTuiHuolist = new ArrayList<CwbApplyTuiHuo>();
		List<CwbOrderView> cwbViewList = new ArrayList<CwbOrderView>();
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		if (isshow != 0) {
			cwbApplyTuiHuolist = this.cwbApplyTuiHuoDAO.getCwbApplyTuiHuoPage(page, begindate, enddate, ishandle);
			pageparm = new Page(this.cwbApplyTuiHuoDAO.getCwbApplyTuiHuoCount(begindate, enddate, ishandle), page, Page.ONE_PAGE_NUMBER);

			if ((cwbApplyTuiHuolist != null) && (cwbApplyTuiHuolist.size() > 0)) {
				String cwbs = "";
				for (CwbApplyTuiHuo cwbApplyTuiHuo : cwbApplyTuiHuolist) {
					cwbs += "'" + cwbApplyTuiHuo.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					List<Branch> branchList = this.branchDAO.getAllBranches();
					List<Customer> customerList = this.customerDao.getAllCustomers();
					List<User> userList = this.userDAO.getAllUser();
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					cwbViewList = this.getTuiHuoCwbOrderViewCount10(orderlist, cwbApplyTuiHuolist, customerList, branchList, userList);
				}
			}
		}
		model.addAttribute("cwbViewList", cwbViewList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		return "cwbapply/applytoTuiHuolist";
	}

	@RequestMapping("/applytoZhongZhuanlist/{page}")
	public String applytoZhongZhuanlist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "ishandle", required = false, defaultValue = "0") long ishandle,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, HttpServletResponse response, HttpServletRequest request) {
		Page pageparm = new Page();
		List<CwbApplyZhongZhuan> cwbApplyZhongZhuanlist = new ArrayList<CwbApplyZhongZhuan>();
		List<CwbOrderView> cwbViewList = new ArrayList<CwbOrderView>();
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		if (isshow != 0) {
			cwbApplyZhongZhuanlist = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanPage(page, begindate, enddate, ishandle);
			pageparm = new Page(this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanCount(begindate, enddate, ishandle), page, Page.ONE_PAGE_NUMBER);

			if ((cwbApplyZhongZhuanlist != null) && (cwbApplyZhongZhuanlist.size() > 0)) {
				String cwbs = "";
				for (CwbApplyZhongZhuan cwbApplyZhongZhuan : cwbApplyZhongZhuanlist) {
					cwbs += "'" + cwbApplyZhongZhuan.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					List<Branch> branchList = this.branchDAO.getAllBranches();
					List<Customer> customerList = this.customerDao.getAllCustomers();
					List<User> userList = this.userDAO.getAllUser();
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					cwbViewList = this.getZhongZhuanCwbOrderViewCount10(orderlist, cwbApplyZhongZhuanlist, customerList, branchList, userList);
				}
			}
		}
		model.addAttribute("cwbViewList", cwbViewList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		return "cwbapply/applytoZhongZhuanlist";
	}

	/**
	 * 客服审核为退货页面
	 * 
	 * @param model
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param ishandle
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 */

	@RequestMapping("/kefuuserapplytoTuiHuolist/{page}")
	public String kefuuserapplytoTuiHuolist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "ishandle", required = false, defaultValue = "0") long ishandle,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, HttpServletResponse response, HttpServletRequest request) {
		Page pageparm = new Page();
		List<CwbApplyTuiHuo> cwbApplyTuiHuolist = new ArrayList<CwbApplyTuiHuo>();
		List<CwbOrderView> cwbViewList = new ArrayList<CwbOrderView>();
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		if (isshow != 0) {
			cwbApplyTuiHuolist = this.cwbApplyTuiHuoDAO.getCwbApplyTuiHuoPage(page, begindate, enddate, ishandle);
			pageparm = new Page(this.cwbApplyTuiHuoDAO.getCwbApplyTuiHuoCount(begindate, enddate, ishandle), page, Page.ONE_PAGE_NUMBER);

			if ((cwbApplyTuiHuolist != null) && (cwbApplyTuiHuolist.size() > 0)) {
				String cwbs = "";
				for (CwbApplyTuiHuo cwbApplyTuiHuo : cwbApplyTuiHuolist) {
					cwbs += "'" + cwbApplyTuiHuo.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					List<Branch> branchList = this.branchDAO.getAllBranches();
					List<Customer> customerList = this.customerDao.getAllCustomers();
					List<User> userList = this.userDAO.getAllUser();
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					cwbViewList = this.getTuiHuoCwbOrderViewCount10(orderlist, cwbApplyTuiHuolist, customerList, branchList, userList);
				}
			}
		}
		model.addAttribute("cwbViewList", cwbViewList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		List<Branch> tuihuobranchList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.TuiHuo.getValue() + "");
		model.addAttribute("tuihuobranchList", tuihuobranchList);
		String isUseAuditZhongZhuan = this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		return "cwbapply/kefuuserapplytoTuiHuolist";
	}

	/**
	 * 退货申请订单确认审核
	 * 
	 * @param model
	 * @param cwb
	 * @param applytuihuobranchid
	 * @param handleremark
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/trueAuditByCwb")
	public @ResponseBody
	String trueAuditByCwb(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "applytuihuobranchid", required = false, defaultValue = "0") long applytuihuobranchid,
			@RequestParam(value = "handleremark", required = false, defaultValue = "") String handleremark, HttpServletResponse response, HttpServletRequest request) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String datetime = df.format(date);
			// CwbOrder co =
			// this.cwborderService.auditToTuihuo(this.getSessionUser(), cwb,
			// cwb, FlowOrderTypeEnum.DingDanLanJie.getValue(), 0);
			this.cwbApplyTuiHuoDAO.updateCwbApplyTuiHuoForHandle(datetime, this.getSessionUser().getUserid(), handleremark, 1, applytuihuobranchid, cwb);
			this.cwbDAO.updateNextBranchid(cwb, applytuihuobranchid);
			this.cwbDAO.updateCwbState(cwb, CwbStateEnum.TuiHuo);
			// if (co.getFlowordertype() ==
			// FlowOrderTypeEnum.DingDanLanJie.getValue()) {
			return "{\"errorCode\":0,\"error\":\"审核成功\"}";
			// } else {
			// return "{\"errorCode\":1,\"error\":\"审核失败，不符合\"}";
			// }
		} catch (CwbException ce) {
			return "{\"errorCode\":1,\"error\":\"审核失败，" + ce.getMessage() + "\"}";
		}
	}

	/**
	 * 退货申请订单客服拒审
	 * 
	 * @param model
	 * @param cwb
	 * @param applytuihuobranchid
	 * @param handleremark
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/refuseAuditByCwb")
	public @ResponseBody
	String refuseAuditByCwb(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "applytuihuobranchid", required = false, defaultValue = "0") long applytuihuobranchid,
			@RequestParam(value = "handleremark", required = false, defaultValue = "") String handleremark, HttpServletResponse response, HttpServletRequest request) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String datetime = df.format(date);

			this.cwbApplyTuiHuoDAO.updateCwbApplyTuiHuoForHandle(datetime, this.getSessionUser().getUserid(), handleremark, 2, applytuihuobranchid, cwb);
			return "{\"errorCode\":0,\"error\":\"客户拒审成功\"}";
		} catch (CwbException ce) {
			return "{\"errorCode\":1,\"error\":\"客服拒审失败，" + ce.getMessage() + "\",\"type\":\"add\"}";
		}
	}

	/**
	 * 客服审核为中转页面
	 * 
	 * @param model
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param ishandle
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 */

	@RequestMapping("/kefuuserapplytoZhongZhuanlist/{page}")
	public String kefuuserapplytoZhongZhuanlist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "ishandle", required = false, defaultValue = "0") long ishandle,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, HttpServletResponse response, HttpServletRequest request) {
		Page pageparm = new Page();
		List<CwbApplyZhongZhuan> cwbApplyZhongZhuanlist = new ArrayList<CwbApplyZhongZhuan>();
		List<CwbOrderView> cwbViewList = new ArrayList<CwbOrderView>();
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		if (isshow != 0) {
			cwbApplyZhongZhuanlist = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanPage(page, begindate, enddate, ishandle);
			pageparm = new Page(this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanCount(begindate, enddate, ishandle), page, Page.ONE_PAGE_NUMBER);

			if ((cwbApplyZhongZhuanlist != null) && (cwbApplyZhongZhuanlist.size() > 0)) {
				String cwbs = "";
				for (CwbApplyZhongZhuan cwbApplyZhongZhuan : cwbApplyZhongZhuanlist) {
					cwbs += "'" + cwbApplyZhongZhuan.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					List<Branch> branchList = this.branchDAO.getAllBranches();
					List<Customer> customerList = this.customerDao.getAllCustomers();
					List<User> userList = this.userDAO.getAllUser();
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					cwbViewList = this.getZhongZhuanCwbOrderViewCount10(orderlist, cwbApplyZhongZhuanlist, customerList, branchList, userList);
				}
			}
		}
		model.addAttribute("cwbViewList", cwbViewList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		List<Branch> tuihuobranchList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhongZhuan.getValue() + "");
		model.addAttribute("zhongzhuanbranchList", tuihuobranchList);
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		return "cwbapply/kefuuserapplytoZhongZhuanlist";
	}

	/**
	 * 中转申请订单确认审核
	 * 
	 * @param model
	 * @param cwb
	 * @param applytuihuobranchid
	 * @param handleremark
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/zhongZhuantrueAuditByCwb")
	public @ResponseBody
	String zhongZhuantrueAuditByCwb(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "applyzhongzhuanbranchid", required = false, defaultValue = "0") long applyzhongzhuanbranchid,
			@RequestParam(value = "handleremark", required = false, defaultValue = "") String handleremark, HttpServletResponse response, HttpServletRequest request) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String datetime = df.format(date);

			this.cwbApplyZhongZhuanDAO.updateCwbApplyZhongZhuanForHandle(datetime, this.getSessionUser().getUserid(), handleremark, 1, applyzhongzhuanbranchid, cwb);
			return "{\"errorCode\":0,\"error\":\"审核成功\"}";
		} catch (CwbException ce) {
			return "{\"errorCode\":1,\"error\":\"审核失败，" + ce.getMessage() + "\",\"type\":\"add\"}";
		}
	}

	/**
	 * 中转申请订单客服拒审
	 * 
	 * @param model
	 * @param cwb
	 * @param applytuihuobranchid
	 * @param handleremark
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/zhongZhuanrefuseAuditByCwb")
	public @ResponseBody
	String zhongZhuanrefuseAuditByCwb(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "applyzhongzhuanbranchid", required = false, defaultValue = "0") long applyzhongzhuanbranchid,
			@RequestParam(value = "handleremark", required = false, defaultValue = "") String handleremark, HttpServletResponse response, HttpServletRequest request) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String datetime = df.format(date);

			this.cwbApplyZhongZhuanDAO.updateCwbApplyZhongZhuanForHandle(datetime, this.getSessionUser().getUserid(), handleremark, 2, applyzhongzhuanbranchid, cwb);
			return "{\"errorCode\":0,\"error\":\"客户拒审成功\"}";
		} catch (CwbException ce) {
			return "{\"errorCode\":1,\"error\":\"客服拒审失败，" + ce.getMessage() + "\",\"type\":\"add\"}";
		}
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {
		String[] cloumnName1 = new String[12]; // 导出的列名
		String[] cloumnName2 = new String[12]; // 导出的英文列名

		this.exportService.SetCwbApplyTuiHuoFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "申请退货订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Cwbapplytuihuo_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			long ishandle = request.getParameter("ishandle1") == null ? -1 : Long.parseLong(request.getParameter("ishandle1").toString());
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();

			List<CwbApplyTuiHuo> cwbApplyTuiHuolist = this.cwbApplyTuiHuoDAO.getCwbApplyTuiHuoNoPage(begindate, enddate, ishandle);
			List<Customer> customerList = this.customerDao.getAllCustomers();
			List<User> userList = this.userDAO.getAllUser();
			List<Branch> branchList = this.branchDAO.getAllBranches();
			List<CwbOrderView> cwbViewList = new ArrayList<CwbOrderView>();
			if ((cwbApplyTuiHuolist != null) && (cwbApplyTuiHuolist.size() > 0)) {
				String cwbs = "";
				for (CwbApplyTuiHuo cwbApplyTuiHuo : cwbApplyTuiHuolist) {
					cwbs += "'" + cwbApplyTuiHuo.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					List<CwbOrder> orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					cwbViewList = this.getTuiHuoCwbOrderViewCount10(orderlist, cwbApplyTuiHuolist, customerList, branchList, userList);
				}
			}
			final List<CwbOrderView> cwbOrderViewList = cwbViewList;
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
							a = CwbApplyController.this.exportService.setAllpyTuiHuoObject(cloumnName3, request1, cwbOrderViewList, a, i, k);
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

	@RequestMapping("/exportZhongZhuanExcle")
	public void exportZhongZhuanExcle(Model model, HttpServletResponse response, HttpServletRequest request) {
		String[] cloumnName1 = new String[12]; // 导出的列名
		String[] cloumnName2 = new String[12]; // 导出的英文列名

		this.exportService.SetCwbApplyZhongZhuanFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "申请中转订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Cwbapplyzhongzhuan_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			long ishandle = request.getParameter("ishandle1") == null ? -1 : Long.parseLong(request.getParameter("ishandle1").toString());
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();

			List<CwbApplyZhongZhuan> cwbApplyZhongZhuanlist = this.cwbApplyZhongZhuanDAO.getCwbApplyZhongZhuanNoPage(begindate, enddate, ishandle);
			List<CwbOrderView> cwbViewList = new ArrayList<CwbOrderView>();

			if ((cwbApplyZhongZhuanlist != null) && (cwbApplyZhongZhuanlist.size() > 0)) {
				String cwbs = "";
				for (CwbApplyZhongZhuan cwbApplyZhongZhuan : cwbApplyZhongZhuanlist) {
					cwbs += "'" + cwbApplyZhongZhuan.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					List<Branch> branchList = this.branchDAO.getAllBranches();
					List<Customer> customerList = this.customerDao.getAllCustomers();
					List<User> userList = this.userDAO.getAllUser();
					List<CwbOrder> orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					cwbViewList = this.getZhongZhuanCwbOrderViewCount10(orderlist, cwbApplyZhongZhuanlist, customerList, branchList, userList);
				}
			}

			final List<CwbOrderView> cwbOrderViewList = cwbViewList;
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
							a = CwbApplyController.this.exportService.setAllpyZhongZhuanObject(cloumnName3, request1, cwbOrderViewList, a, i, k);
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

	// 给CwbOrderView赋值
	public List<CwbOrderView> getTuiHuoCwbOrderView(List<CwbOrder> clist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList, List<Branch> branchList, List<User> userList,
			List<Reason> reasonList, List<Remark> remarkList, Map<String, CwbApplyTuiHuo> cwbapplylist) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if (clist.size() > 0) {
			for (CwbOrder c : clist) {
				CwbOrderView cwbOrderView = new CwbOrderView();

				cwbOrderView.setCarwarehouse(c.getCarwarehouse());
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

				cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
				cwbOrderView.setInhouse(this.dataStatisticsService.getQueryBranchName(branchList, Integer.parseInt(c.getCarwarehouse() == "" ? "0" : c.getCarwarehouse())));// 入库仓库
				cwbOrderView.setCurrentbranchname(this.dataStatisticsService.getQueryBranchName(branchList, c.getCurrentbranchid()));// 当前所在机构名称
				cwbOrderView.setStartbranchname(this.dataStatisticsService.getQueryBranchName(branchList, c.getStartbranchid()));// 上一站机构名称
				cwbOrderView.setNextbranchname(this.dataStatisticsService.getQueryBranchName(branchList, c.getNextbranchid()));// 下一站机构名称
				cwbOrderView.setDeliverybranch(this.dataStatisticsService.getQueryBranchName(branchList, c.getDeliverybranchid()));// 配送站点
				cwbOrderView.setDelivername(this.dataStatisticsService.getQueryUserName(userList, c.getDeliverid()));
				cwbOrderView.setRealweight(c.getCarrealweight());
				cwbOrderView.setCwbremark(c.getCwbremark());
				cwbOrderView.setReceivablefee(c.getReceivablefee());
				cwbOrderView.setCaramount(c.getCaramount());
				cwbOrderView.setPaybackfee(c.getPaybackfee());

				DeliveryState deliverystate = this.dataStatisticsService.getDeliveryByCwb(c.getCwb());
				cwbOrderView.setPaytype(this.dataStatisticsService.getPayWayType(c.getCwb(), deliverystate));// 支付方式
				cwbOrderView.setRemark1(c.getRemark1());
				cwbOrderView.setRemark2(c.getRemark2());
				cwbOrderView.setRemark3(c.getRemark3());
				cwbOrderView.setRemark4(c.getRemark4());
				cwbOrderView.setRemark5(c.getRemark5());
				cwbOrderView.setFlowordertype(c.getFlowordertype());
				cwbOrderView.setCwbstate(c.getCwbstate());
				if (c.getCurrentbranchid() == 0) {
					cwbOrderView.setCurrentbranchid(c.getStartbranchid());
				} else {
					cwbOrderView.setCurrentbranchid(c.getCurrentbranchid());
				}
				cwbOrderView.setOrderResultType(c.getDeliverid());
				cwbOrderView.setCartype(c.getCartype());
				cwbOrderView.setCwbdelivertypeid(c.getCwbdelivertypeid());
				cwbOrderView.setCwbordertypeid(CwbOrderTypeIdEnum.getByValue(c.getCwbordertypeid()).getText());// 订单类型
				cwbOrderView.setCurrentsitetype(this.getQueryBranchSitetype(branchList, c.getCurrentbranchid()));

				CwbApplyTuiHuo cwbTuiHuo = cwbapplylist.isEmpty() ? new CwbApplyTuiHuo() : (cwbapplylist.get(c.getCwb()) == null ? new CwbApplyTuiHuo() : cwbapplylist.get(c.getCwb()));

				cwbOrderView.setApplytuihuobranchid(cwbTuiHuo.getApplytuihuobranchid());
				cwbOrderView.setApplytuihuobranchname(this.dataStatisticsService.getQueryBranchName(branchList, cwbTuiHuo.getApplytuihuobranchid()));
				cwbOrderView.setApplytuihuoremark(StringUtil.nullConvertToEmptyString(cwbTuiHuo.getApplytuihuoremark()));
				cwbOrderView.setApplyhandleremark(StringUtil.nullConvertToEmptyString(cwbTuiHuo.getHandleremark()));
				cwbOrderView.setApplyhandletime(cwbTuiHuo.getHandletime());
				cwbOrderView.setApplyhandleusername(this.dataStatisticsService.getQueryUserName(userList, cwbTuiHuo.getHandleuserid()));

				cwbOrderView.setDeliverystate(c.getDeliverystate());
				cwbOrderViewList.add(cwbOrderView);

			}
		}
		return cwbOrderViewList;
	}

	public List<CwbOrderView> getTuiHuoCwbOrderViewCount10(List<CwbOrder> orderlist, List<CwbApplyTuiHuo> applyTuiHuolist, List<Customer> customerList, List<Branch> branchList, List<User> userList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((applyTuiHuolist.size() > 0) && (orderlist.size() > 0)) {
			for (CwbApplyTuiHuo ct : applyTuiHuolist) {
				for (CwbOrder c : orderlist) {
					if (ct.getCwb().equals(c.getCwb())) {
						CwbOrderView cwbOrderView = new CwbOrderView();

						cwbOrderView.setCwb(c.getCwb());
						cwbOrderView.setDeliverystate(c.getDeliverystate());
						cwbOrderView.setCwbordertypeid(CwbOrderTypeIdEnum.getByValue(c.getCwbordertypeid()).getText());// 订单类型
						cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
						cwbOrderView.setDelivername(this.dataStatisticsService.getQueryUserName(userList, c.getDeliverid()));

						if (c.getCurrentbranchid() == 0) {
							cwbOrderView.setCurrentbranchid(c.getStartbranchid());
						} else {
							cwbOrderView.setCurrentbranchid(c.getCurrentbranchid());
						}
						cwbOrderView.setCurrentbranchname(this.dataStatisticsService.getQueryBranchName(branchList, cwbOrderView.getCurrentbranchid()));// 当前所在机构名称
						cwbOrderView.setApplytuihuobranchid(ct.getApplytuihuobranchid());
						cwbOrderView.setApplytuihuobranchname(this.dataStatisticsService.getQueryBranchName(branchList, ct.getApplytuihuobranchid()));
						cwbOrderView.setApplytuihuoremark(ct.getApplytuihuoremark());
						cwbOrderView.setApplyishandle(ct.getIshandle());
						cwbOrderView.setApplyhandleremark(ct.getHandleremark());
						cwbOrderView.setApplyhandletime(ct.getHandletime());
						cwbOrderView.setApplyhandleusername(this.dataStatisticsService.getQueryUserName(userList, ct.getHandleuserid()));
						cwbOrderViewList.add(cwbOrderView);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	public List<CwbOrderView> getZhongZhuanCwbOrderView(List<CwbOrder> clist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList, List<Branch> branchList, List<User> userList,
			List<Reason> reasonList, List<Remark> remarkList, Map<String, CwbApplyZhongZhuan> cwbapplylist) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if (clist.size() > 0) {
			for (CwbOrder c : clist) {
				CwbOrderView cwbOrderView = new CwbOrderView();

				cwbOrderView.setCarwarehouse(c.getCarwarehouse());
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

				cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
				cwbOrderView.setInhouse(this.dataStatisticsService.getQueryBranchName(branchList, Integer.parseInt(c.getCarwarehouse() == "" ? "0" : c.getCarwarehouse())));// 入库仓库
				cwbOrderView.setCurrentbranchname(this.dataStatisticsService.getQueryBranchName(branchList, c.getCurrentbranchid()));// 当前所在机构名称
				cwbOrderView.setStartbranchname(this.dataStatisticsService.getQueryBranchName(branchList, c.getStartbranchid()));// 上一站机构名称
				cwbOrderView.setNextbranchname(this.dataStatisticsService.getQueryBranchName(branchList, c.getNextbranchid()));// 下一站机构名称
				cwbOrderView.setDeliverybranch(this.dataStatisticsService.getQueryBranchName(branchList, c.getDeliverybranchid()));// 配送站点
				cwbOrderView.setDelivername(this.dataStatisticsService.getQueryUserName(userList, c.getDeliverid()));
				cwbOrderView.setRealweight(c.getCarrealweight());
				cwbOrderView.setCwbremark(c.getCwbremark());
				cwbOrderView.setReceivablefee(c.getReceivablefee());
				cwbOrderView.setCaramount(c.getCaramount());
				cwbOrderView.setPaybackfee(c.getPaybackfee());

				DeliveryState deliverystate = this.dataStatisticsService.getDeliveryByCwb(c.getCwb());
				cwbOrderView.setPaytype(this.dataStatisticsService.getPayWayType(c.getCwb(), deliverystate));// 支付方式
				cwbOrderView.setRemark1(c.getRemark1());
				cwbOrderView.setRemark2(c.getRemark2());
				cwbOrderView.setRemark3(c.getRemark3());
				cwbOrderView.setRemark4(c.getRemark4());
				cwbOrderView.setRemark5(c.getRemark5());
				cwbOrderView.setFlowordertype(c.getFlowordertype());
				cwbOrderView.setCwbstate(c.getCwbstate());
				if (c.getCurrentbranchid() == 0) {
					cwbOrderView.setCurrentbranchid(c.getStartbranchid());
				} else {
					cwbOrderView.setCurrentbranchid(c.getCurrentbranchid());
				}
				cwbOrderView.setOrderResultType(c.getDeliverid());
				cwbOrderView.setCartype(c.getCartype());
				cwbOrderView.setCwbdelivertypeid(c.getCwbdelivertypeid());
				cwbOrderView.setCwbordertypeid(CwbOrderTypeIdEnum.getByValue(c.getCwbordertypeid()).getText());// 订单类型
				cwbOrderView.setCurrentsitetype(this.getQueryBranchSitetype(branchList, c.getCurrentbranchid()));

				CwbApplyZhongZhuan cwbZhongZhuan = cwbapplylist.isEmpty() ? new CwbApplyZhongZhuan() : (cwbapplylist.get(c.getCwb()) == null ? new CwbApplyZhongZhuan() : cwbapplylist.get(c.getCwb()));

				cwbOrderView.setApplyzhongzhuanbranchid(cwbZhongZhuan.getApplyzhongzhuanbranchid());
				cwbOrderView.setApplyzhongzhuanbranchname(this.dataStatisticsService.getQueryBranchName(branchList, cwbZhongZhuan.getApplyzhongzhuanbranchid()));
				cwbOrderView.setApplyzhongzhuanremark(StringUtil.nullConvertToEmptyString(cwbZhongZhuan.getApplyzhongzhuanremark()));
				cwbOrderView.setApplyzhongzhuanhandleremark(StringUtil.nullConvertToEmptyString(cwbZhongZhuan.getHandleremark()));
				cwbOrderView.setApplyzhongzhuanhandletime(cwbZhongZhuan.getHandletime());
				cwbOrderView.setApplyzhongzhuanhandleusername(this.dataStatisticsService.getQueryUserName(userList, cwbZhongZhuan.getHandleuserid()));

				cwbOrderView.setDeliverystate(c.getDeliverystate());
				cwbOrderViewList.add(cwbOrderView);

			}
		}
		return cwbOrderViewList;
	}

	public List<CwbOrderView> getZhongZhuanCwbOrderViewCount10(List<CwbOrder> orderlist, List<CwbApplyZhongZhuan> applyZhongZhuanlist, List<Customer> customerList, List<Branch> branchList,
			List<User> userList) {
		List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
		if ((applyZhongZhuanlist.size() > 0) && (orderlist.size() > 0)) {
			for (CwbApplyZhongZhuan ct : applyZhongZhuanlist) {
				for (CwbOrder c : orderlist) {
					if (ct.getCwb().equals(c.getCwb())) {
						CwbOrderView cwbOrderView = new CwbOrderView();

						cwbOrderView.setCwb(c.getCwb());
						cwbOrderView.setDeliverystate(c.getDeliverystate());
						cwbOrderView.setCwbordertypeid(CwbOrderTypeIdEnum.getByValue(c.getCwbordertypeid()).getText());// 订单类型
						cwbOrderView.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
						cwbOrderView.setDelivername(this.dataStatisticsService.getQueryUserName(userList, c.getDeliverid()));

						if (c.getCurrentbranchid() == 0) {
							cwbOrderView.setCurrentbranchid(c.getStartbranchid());
						} else {
							cwbOrderView.setCurrentbranchid(c.getCurrentbranchid());
						}
						cwbOrderView.setCurrentbranchname(this.dataStatisticsService.getQueryBranchName(branchList, cwbOrderView.getCurrentbranchid()));// 当前所在机构名称
						cwbOrderView.setApplyzhongzhuanbranchid(ct.getApplyzhongzhuanbranchid());
						cwbOrderView.setApplyzhongzhuanbranchname(this.dataStatisticsService.getQueryBranchName(branchList, ct.getApplyzhongzhuanbranchid()));
						cwbOrderView.setApplyzhongzhuanremark(ct.getApplyzhongzhuanremark());
						cwbOrderView.setApplyzhongzhuanishandle(ct.getIshandle());
						cwbOrderView.setApplyzhongzhuanhandleremark(ct.getHandleremark());
						cwbOrderView.setApplyzhongzhuanhandletime(ct.getHandletime());
						cwbOrderView.setApplyzhongzhuanhandleusername(this.dataStatisticsService.getQueryUserName(userList, ct.getHandleuserid()));
						cwbOrderViewList.add(cwbOrderView);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	public long getQueryBranchSitetype(List<Branch> branchList, long branchid) {
		long branchsitetype = 0;
		for (Branch b : branchList) {
			if (b.getBranchid() == branchid) {
				branchsitetype = b.getSitetype();
				break;
			}
		}
		return branchsitetype;
	}
}
