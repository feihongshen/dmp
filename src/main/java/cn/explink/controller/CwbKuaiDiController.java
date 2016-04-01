package cn.explink.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbApplyTuiHuoDAO;
import cn.explink.dao.CwbApplyZhongZhuanDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbKuaiDiDAO;
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
import cn.explink.domain.CwbKuaiDi;
import cn.explink.domain.CwbKuaiDiView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.express.ExpressSettleWayEnum;
import cn.explink.service.CwbKuaiDiService;
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
import cn.explink.util.StringUtil;

@RequestMapping("/cwbkuaidi")
@Controller
public class CwbKuaiDiController {
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
	@Autowired
	CwbKuaiDiDAO cwbKuaiDiDAO;
	@Autowired
	CwbKuaiDiService cwbKuaiDiService;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
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

		return "cwbkuaidi/kuaidilist";
	}

	/**
	 * 补录功能
	 *
	 * @param model
	 * @param cwbs
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/buludetail")
	public String buludetail(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs) {
		if (cwbs.trim().length() > 0) {
			List<User> users = this.userDAO.getAllUser();
			List<Branch> branchs = this.branchDAO.getAllBranches();
			String[] cwb = cwbs.split("\r\n");
			List<String> cwbList = new ArrayList<String>();
			for (int i = cwb.length - 1; i >= 0; i--) {
				cwbList.add(cwb[i].trim());
			}
			for (int i = cwb.length - 1; i >= 0; i--) {
				cwbList.remove(cwb[i]);// 去掉 正在处理的订单号
				CwbOrder order = this.cwbDAO.getCwbByCwb(cwb[i].trim());
				if (order != null) {
					CwbKuaiDi kd = this.cwbKuaiDiDAO.getCwbKuaiDiByCwb(cwb[i].trim());
					if (kd != null) {
						CwbKuaiDiView ckv = this.cwbKuaiDiService.getKuaiDiView(order, kd, users, branchs);
						if (ckv.getConsigneenamekf().length() == 0) {// 判断一下该订单有没有做过补录信息
																		// 说明没有补录
							model.addAttribute("error", "订单" + ckv.getCwb() + "没有做过补录信息");
						}
						model.addAttribute("ckv", ckv);
						break;
					} else {
						continue;
					}
				}
			}
			if (cwbList.size() > 0) {// 如果 还有订单就 把剩下的订单 封装成一个新的cwbs
				StringBuffer cwbBuffer = new StringBuffer();
				for (int i = cwbList.size(); i > 0; i--) {
					cwbBuffer.append(cwbList.get(i - 1) + "\r\n");
				}
				String newcwbs = cwbBuffer.toString().substring(0, cwbBuffer.toString().length() - 2);
				model.addAttribute("newcwbs", newcwbs);
			}
		}
		return "cwbkuaidi/buludetail";
	}

	@RequestMapping("/savebulu")
	public @ResponseBody String saveBuLu(HttpServletResponse response, HttpServletRequest request) {
		this.cwbKuaiDiService.savebulu(request, response, this.getSessionUser());
		return "{\"erroeCode\":\"0\"}";

	}

	@RequestMapping("/savequickbulu")
	public @ResponseBody String savequickbulu(HttpServletResponse response, HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String signman = request.getParameter("signman");
		String signstate = request.getParameter("signstate");
		String signtime = request.getParameter("signtime");
		String remark = request.getParameter("remark");
		User user = this.getSessionUser();
		this.cwbKuaiDiDAO.updateKuDiQuick(cwb, signman, Long.parseLong(signstate), signtime, remark, user.getUserid(), DateTimeUtil.getNowTime());
		return "{\"erroeCode\":\"0\"}";
	}

	/**
	 * 快速补录功能
	 *
	 * @param model
	 * @param cwbs
	 * @param response
	 * @param request
	 * @return
	 */

	@RequestMapping("/quickbuludetail")
	public String quickbuludetail(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs) {
		if (cwbs.trim().length() > 0) {
			String[] cwb = cwbs.split("\r\n");
			List<String> cwbList = new ArrayList<String>();
			for (int i = cwb.length - 1; i >= 0; i--) {
				cwbList.add(cwb[i].trim());
			}
			for (int i = cwb.length - 1; i >= 0; i--) {
				cwbList.remove(cwb[i]);// 去掉 正在处理的订单号
				CwbOrder order = this.cwbDAO.getCwbByCwb(cwb[i].trim());
				if (order != null) {
					CwbKuaiDi kd = this.cwbKuaiDiDAO.getCwbKuaiDiByCwb(cwb[i].trim());
					if ((kd != null) && (kd.getPaytype() > 0)) {
						model.addAttribute("kd", kd);
						break;
					} else {
						continue;
					}
				}
			}
			if (cwbList.size() > 0) {// 如果 还有订单就 把剩下的订单 封装成一个新的cwbs
				StringBuffer cwbBuffer = new StringBuffer();
				for (int i = cwbList.size(); i > 0; i--) {
					cwbBuffer.append(cwbList.get(i - 1) + "\r\n");
				}
				String newcwbs = cwbBuffer.toString().substring(0, cwbBuffer.toString().length() - 2);
				model.addAttribute("newcwbs", newcwbs);
			}
		}
		return "cwbkuaidi/quickbuludetail";
	}

	@RequestMapping("/editbulu")
	public String editbulu(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs) {
		if (cwbs.trim().length() > 0) {
			List<User> users = this.userDAO.getAllUser();
			List<Branch> branchs = this.branchDAO.getAllBranches();
			String[] cwb = cwbs.split("\r\n");
			List<String> cwbList = new ArrayList<String>();
			for (int i = cwb.length - 1; i >= 0; i--) {
				cwbList.add(cwb[i].trim());
			}
			for (int i = cwb.length - 1; i >= 0; i--) {
				cwbList.remove(cwb[i]);// 去掉 正在处理的订单号
				CwbOrder order = this.cwbDAO.getCwbByCwb(cwb[i].trim());
				if (order != null) {
					CwbKuaiDi kd = this.cwbKuaiDiDAO.getCwbKuaiDiByCwb(cwb[i].trim());
					if ((kd != null) && (kd.getPaytype() > 0)) {// 说明存在这一单
																// 并且已经补录过了
						CwbKuaiDiView ckv = this.cwbKuaiDiService.getKuaiDiView(order, kd, users, branchs);
						model.addAttribute("ckv", ckv);
						break;
					} else {
						continue;
					}
				}
			}
			if (cwbList.size() > 0) {// 如果 还有订单就 把剩下的订单 封装成一个新的cwbs
				StringBuffer cwbBuffer = new StringBuffer();
				for (int i = cwbList.size(); i > 0; i--) {
					cwbBuffer.append(cwbList.get(i - 1) + "\r\n");
				}
				String newcwbs = cwbBuffer.toString().substring(0, cwbBuffer.toString().length() - 2);
				model.addAttribute("newcwbs", newcwbs);
			}
		}
		return "cwbkuaidi/editbulu";

	}

	@RequestMapping("/exportExcle")
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
							a = CwbKuaiDiController.this.exportService.setCwbKuaiDiObject(cloumnName3, request1, cwbOrderViewList, a, i, k);
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
						cwbKuaiDiView.setCustomername(StringUtil.nullConvertToEmptyString(c.getSendername()));
						cwbKuaiDiView.setCustomercode(StringUtil.nullConvertToEmptyString(c.getSendercustomcode()));

						cwbOrderViewList.add(cwbKuaiDiView);
					}
				}
			}
		}
		return cwbOrderViewList;
	}
}
