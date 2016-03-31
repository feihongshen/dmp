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
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbDiuShiDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserBranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbDiuShi;
import cn.explink.domain.CwbDiuShiView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.CwbDiuShiService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

@RequestMapping("/cwbdiushi")
@Controller
public class CwbDiuShiController {

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	CwbDiuShiService cwbDiuShiService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	CwbDiuShiDAO cwbDiuShiDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	RoleDAO roleDAO;

	@Autowired
	UserBranchDAO userBranchDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 货物丢失处理查询功能
	 * 
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param ishandle
	 * @param customerid
	 * @param branchid
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/diushilist/{page}")
	public String diushilist(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchid, @RequestParam(value = "ishandle", required = false, defaultValue = "-1") long ishandle,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbDiuShi sum = new CwbDiuShi();
		List<CwbDiuShi> cwbDiuShilist = new ArrayList<CwbDiuShi>();
		List<CwbDiuShiView> cwbDiuShiView = new ArrayList<CwbDiuShiView>();
		// 保存供货商的选择
		List<String> customeridList = new ArrayList<String>();
		// 保存站点的选择
		List<String> branchidList = new ArrayList<String>();

		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		// 根据用户区域权限设置得到相应的机构列表，分别是库房、站点、中转、退货类型的
		List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + ","
				+ BranchEnum.ZhongZhuan.getValue() + "," + BranchEnum.KuFang.getValue());

		// 如果用户没有设置该站或者没有设置任何站将本站加入到机构列表中
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue() || branch.getSitetype() == BranchEnum.KuFang.getValue() || branch.getSitetype() == BranchEnum.ZhongZhuan.getValue()
				|| branch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
			if (branchnameList.size() == 0) {
				branchnameList.add(branch);
			} else {
				if (!dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
					branchnameList.add(branch);
				}
			}
		}
		if (isshow != 0) {
			logger.info("货物丢失处理，操作人{}，选择条件begindate:" + begindate + ",enddate:" + enddate + ",branchid:" + branchid + ",customerid:" + dataStatisticsService.getStrings(customerid) + ",branchid:"
					+ dataStatisticsService.getStrings(branchid) + ",isshow:" + isshow + ",page:" + page + "", getSessionUser().getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;

			// 定义参数
			List<CwbOrder> clist = new ArrayList<CwbOrder>();

			// 保存供货商的选择
			customeridList = dataStatisticsService.getList(customerid);
			// 保存站点的选择
			branchidList = dataStatisticsService.getList(branchid);

			String customeridStr = dataStatisticsService.getStrings(customerid);
			String branchidStr = dataStatisticsService.getStrings(branchid);

			count = cwbDiuShiDAO.getCwbDiuShiCount(begindate, enddate, customeridStr, branchidStr, ishandle);

			cwbDiuShilist = cwbDiuShiDAO.getCwbDiuShiList(page, begindate, enddate, customeridStr, branchidStr, ishandle);

			sum = cwbDiuShiDAO.getCwbDiuShiSum(begindate, enddate, customeridStr, branchidStr, ishandle);

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			List<Customer> customerList = customerDAO.getAllCustomersNew();
			List<User> userList = userDAO.getAllUser();
			if (cwbDiuShilist != null && cwbDiuShilist.size() > 0) {
				String cwbs = "";
				for (CwbDiuShi cwbDiuShi : cwbDiuShilist) {
					cwbs += "'" + cwbDiuShi.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					clist = cwbDAO.getCwbOrderByCwbs(cwbs);
					// 赋值显示对象
					cwbDiuShiView = cwbDiuShiService.getCwbDiuShiViewCount10(clist, cwbDiuShilist, customerList, userList);
				}
			}
		}

		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("roleList", roleDAO.getRoles());
		model.addAttribute("branchidStr", branchidList);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("count", count);
		model.addAttribute("sum", sum);
		model.addAttribute("cwbDiuShiViewList", cwbDiuShiView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		logger.info("货物丢失处理，当前操作人{},条数{}", getSessionUser().getRealname(), count);
		return "cwbdiushi/diushilist";
	}

	/**
	 * 有处理功能的查询功能
	 * 
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param customerid
	 * @param branchid
	 * @param ishandle
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/diushihaverightlist/{page}")
	public String diushihaverightlist(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchid, @RequestParam(value = "ishandle", required = false, defaultValue = "-1") long ishandle,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbDiuShi sum = new CwbDiuShi();
		List<CwbDiuShi> cwbDiuShilist = new ArrayList<CwbDiuShi>();
		List<CwbDiuShiView> cwbDiuShiView = new ArrayList<CwbDiuShiView>();
		// 保存供货商的选择
		List<String> customeridList = new ArrayList<String>();
		// 保存站点的选择
		List<String> branchidList = new ArrayList<String>();

		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		// 根据用户区域权限设置得到相应的机构列表，分别是库房、站点、中转、退货类型的
		List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + ","
				+ BranchEnum.ZhongZhuan.getValue() + "," + BranchEnum.KuFang.getValue());

		// 如果用户没有设置该站或者没有设置任何站将本站加入到机构列表中
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue() || branch.getSitetype() == BranchEnum.KuFang.getValue() || branch.getSitetype() == BranchEnum.ZhongZhuan.getValue()
				|| branch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
			if (branchnameList.size() == 0) {
				branchnameList.add(branch);
			} else {
				if (!dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
					branchnameList.add(branch);
				}
			}
		}
		if (isshow != 0) {
			logger.info("货物丢失处理，操作人{}，选择条件begindate:" + begindate + ",enddate:" + enddate + ",branchid:" + branchid + ",customerid:" + dataStatisticsService.getStrings(customerid) + ",branchid:"
					+ dataStatisticsService.getStrings(branchid) + ",isshow:" + isshow + ",page:" + page + "", getSessionUser().getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;

			// 定义参数
			List<CwbOrder> clist = new ArrayList<CwbOrder>();

			// 保存供货商的选择
			customeridList = dataStatisticsService.getList(customerid);
			// 保存站点的选择
			branchidList = dataStatisticsService.getList(branchid);

			String customeridStr = dataStatisticsService.getStrings(customerid);
			String branchidStr = dataStatisticsService.getStrings(branchid);

			count = cwbDiuShiDAO.getCwbDiuShiCount(begindate, enddate, customeridStr, branchidStr, ishandle);

			cwbDiuShilist = cwbDiuShiDAO.getCwbDiuShiList(page, begindate, enddate, customeridStr, branchidStr, ishandle);

			sum = cwbDiuShiDAO.getCwbDiuShiSum(begindate, enddate, customeridStr, branchidStr, ishandle);

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			List<Customer> customerList = customerDAO.getAllCustomersNew();
			List<User> userList = userDAO.getAllUser();
			if (cwbDiuShilist != null && cwbDiuShilist.size() > 0) {
				String cwbs = "";
				for (CwbDiuShi cwbDiuShi : cwbDiuShilist) {
					cwbs += "'" + cwbDiuShi.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					clist = cwbDAO.getCwbOrderByCwbs(cwbs);
					// 赋值显示对象
					cwbDiuShiView = cwbDiuShiService.getCwbDiuShiViewCount10(clist, cwbDiuShilist, customerList, userList);
				}
			}
		}

		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("roleList", roleDAO.getRoles());
		model.addAttribute("branchidStr", branchidList);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("count", count);
		model.addAttribute("sum", sum);
		model.addAttribute("cwbDiuShiViewList", cwbDiuShiView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		logger.info("货物丢失处理，当前操作人{},条数{}", getSessionUser().getRealname(), count);
		return "cwbdiushi/diushihaverightlist";
	}

	/**
	 * 根据订单查看某单详情
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/diushishow/{id}")
	public String diushishow(Model model, @PathVariable("id") long id) {
		CwbDiuShi cwbDiuShi = cwbDiuShiDAO.getCwbDiuShiById(id);
		CwbOrder co = cwbDAO.getCwbByCwb(cwbDiuShi.getCwb());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		List<User> userlist = userDAO.getAllUser();
		model.addAttribute("userlist", userlist);
		model.addAttribute("datetime", datetime);
		model.addAttribute("cwborder", co);
		model.addAttribute("cwbDiuShi", cwbDiuShi);
		return "cwbdiushi/diushishowdetail";
	}

	/**
	 * 货物丢失处理
	 * 
	 * @param model
	 * @param id
	 * @param payamount
	 * @return
	 */
	@RequestMapping("/diushihandle/{id}")
	public @ResponseBody String diushihandle(Model model, @PathVariable("id") long id, @RequestParam(value = "payamount", required = false, defaultValue = "0") BigDecimal payamount) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String datetime = df.format(date);
			cwbDiuShiDAO.saveCwbDiuShiById(datetime, getSessionUser().getUserid(), payamount, id);
			return "{\"errorCode\":0,\"error\":\"处理成功\"}";
		} catch (Exception e) {
			logger.info("货物丢失处理，id:" + id + ",payamount:" + payamount + ",handleuserid:" + getSessionUser().getRealname(), e);
			return "{\"errorCode\":1,\"error\":\"处理失败\"}";
		}
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {

		String[] cloumnName1 = new String[12]; // 导出的列名
		String[] cloumnName2 = new String[12]; // 导出的英文列名

		exportService.SetDiuShiFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "货物丢失订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "CwbDiuShi" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据

			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] branchid = request.getParameterValues("branchid1") == null ? new String[] {} : request.getParameterValues("branchid1");
			long ishandle = request.getParameter("ishandle1") == null ? -1 : Long.parseLong(request.getParameter("ishandle1").toString());

			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			List<CwbDiuShi> cwbDiuShilist = cwbDiuShiDAO
					.getCwbDiuShiListNOPage(begindate, enddate, dataStatisticsService.getStrings(customerids), dataStatisticsService.getStrings(branchid), ishandle);
			List<Customer> customerList = customerDAO.getAllCustomers();
			List<User> userList = userDAO.getAllUser();
			final List<Role> roleList = roleDAO.getRoles();
			List<CwbDiuShiView> cwbDiuShiView = new ArrayList<CwbDiuShiView>();
			if (cwbDiuShilist != null && cwbDiuShilist.size() > 0) {
				String cwbs = "";
				for (CwbDiuShi cwbDiuShi : cwbDiuShilist) {
					cwbs += "'" + cwbDiuShi.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					clist = cwbDAO.getCwbOrderByCwbs(cwbs);
					// 赋值显示对象
					cwbDiuShiView = cwbDiuShiService.getCwbDiuShiViewCount10(clist, cwbDiuShilist, customerList, userList);
				}
			}

			final List<CwbDiuShiView> cViewlist = cwbDiuShiView;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < cViewlist.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setDiuShiObject(cloumnName3, cViewlist, request1, a, i, k, roleList);
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

}
