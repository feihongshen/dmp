package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.Exportmould;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.DeliveryService;
import cn.explink.service.ExportService;
import cn.explink.service.MonitorSiteExportService;
import cn.explink.service.MonitorSiteService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.Page;

@Controller
@RequestMapping("/monitorsite")
public class MonitorSiteController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	MonitorSiteService monitorService;
	@Autowired
	DeliveryService deliveryService;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	BranchDAO branchDao;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ExportService exportService;
	@Autowired
	MonitorSiteExportService monitorSiteExportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;

	// 第一页
	@RequestMapping("/date")
	public String list(Model model, @RequestParam(value = "crateStartdate", required = false, defaultValue = "") String crateStartdate,
			@RequestParam(value = "crateEnddate", required = false, defaultValue = "") String crateEnddate,
			@RequestParam(value = "startinSitetime", required = false, defaultValue = "") String startinSitetime,
			@RequestParam(value = "endinSitetime", required = false, defaultValue = "") String endinSitetime, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "controlStrCustomer", required = false, defaultValue = "") String controlStrCustomer, HttpServletResponse response, HttpServletRequest request) {

		if (!"1".equals(request.getParameter("isshow"))) {// 搜索的条件
			crateStartdate = (crateStartdate == null || crateStartdate.equals("")) ? (DateDayUtil.getDateBefore("", -1) + " 00:00:00") : crateStartdate;
			crateEnddate = (crateEnddate == null || crateEnddate.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 23:59:59") : crateEnddate;
			startinSitetime = (startinSitetime == null || startinSitetime.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 00:00:00") : crateStartdate;
			endinSitetime = (endinSitetime == null || endinSitetime.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 23:59:59") : crateEnddate;
		}

		model.addAttribute("crateStartdate", crateStartdate);
		model.addAttribute("crateEnddate", crateEnddate);
		model.addAttribute("startinSitetime", startinSitetime);
		model.addAttribute("endinSitetime", endinSitetime);

		// 查询所有的站点
		List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
		model.addAttribute("branchnameList", branchnameList);
		// 查询当前站点
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		long branchidPram = branchid;
		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		Branch nowbranch = getDmpDAO.getNowBranch(branchidSession);
		if (nowbranch != null) {
			if (nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				model.addAttribute("nowBranch", nowbranch);
				branchidPram = branchidSession;
			}
		}

		model.addAttribute("branchidSession", branchidPram);
		String customerid = "".equals(controlStrCustomer) ? "" : controlStrCustomer.substring(0, controlStrCustomer.length() - 1);
		// 将条件保存session中
		setSetions(request, crateStartdate, crateEnddate, customerid, branchidPram, branchidSession, startinSitetime, endinSitetime);
		setCustomerlist(model, customerid, branchidPram, dmpid);
		setSiteModels(model, crateStartdate, crateEnddate, customerid, branchidPram, startinSitetime, endinSitetime);
		return "/monitor/site/siteMonitor";

	}

	// 第一页导出
	@RequestMapping("/dateExport")
	public String dateExp(Model model, HttpServletResponse response, HttpServletRequest request) {
		String crateStartdate = request.getSession().getAttribute("crateStartdate").toString();
		String crateEnddate = request.getSession().getAttribute("crateEnddate").toString();
		String startinSitetime = request.getSession().getAttribute("startinSitetime").toString();
		String endinSitetime = request.getSession().getAttribute("endinSitetime").toString();
		String customerid = request.getSession().getAttribute("customerid").toString();
		long branchid = new Long(request.getSession().getAttribute("branchid").toString());
		Branch branch = getDmpDAO.getNowBranch(branchid);
		model.addAttribute("nowbranchName", branch == null ? "全部" : branch.getBranchname());

		setSiteModels(model, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return "/monitor/site/siteExpo";

	}

	// 查看详细页
	@RequestMapping("/dateshow/{flowType}/{page}")
	public String dateShow(Model model, @PathVariable("page") long page, @PathVariable("flowType") String flowType, HttpServletResponse response, HttpServletRequest request) {
		String crateStartdate = request.getSession().getAttribute("crateStartdate").toString();
		String crateEnddate = request.getSession().getAttribute("crateEnddate").toString();
		String startinSitetime = request.getSession().getAttribute("startinSitetime").toString();
		String endinSitetime = request.getSession().getAttribute("endinSitetime").toString();
		String customerid = request.getSession().getAttribute("customerid").toString();
		long branchid = new Long(request.getSession().getAttribute("branchid").toString());

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);

		User user = getDmpDAO.getLogUser(dmpid);
		List<Exportmould> exportmouldlist = exportmouldDAO.getAllExportmouldByUser(user.getRoleid());
		model.addAttribute("exportmouldlist", exportmouldlist);
		if ("".equals(flowType)) {
			flowType = (String) request.getSession().getAttribute("flowType");
		} else {
			request.getSession().setAttribute("flowType", flowType);
		}
		if ("rkzs".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 系统信息匹配订单");
		} else if ("weidaohuo".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 实际应到货量订单");
		} else if ("rukuweiling".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 入库未领订单");
		} else if ("youhuowudan".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 到错货的订单，注：此处统计货物分错到该站的订单");
		} else if ("youdanwuhuo".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 分站少货的订单");
		} else if ("yilinghuo".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 小件员领货订单，注：此处统计小件员领货扫描的数据");
		} else if ("yichangdan".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 异常单订单");
		} else if ("yiliudan".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 库存遗留订单，注：此处统计超过一天的遗留货物");
		} else if ("kucuntuihuo".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 库存退货订单");
		} else if ("kucunzhiliu".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 库存滞留订单");
		} else if ("zaituzhongzhuan".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 中转在途订单");
		} else if ("zaitutuihuo".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 退货在途订单");
		} else if ("weijiaokuan".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 未交款订单，注：此统计的是已归班但未向财务或总部交款的订单");
		} else if ("qiankuan".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 已交款订单，注：此统计的是配送成功已向财务或总部交款的订单");
		} else if ("tuotou".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 妥投订单，注：此统计的是反馈配送成功的订单");
		} else if ("weituotou".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 未妥投订单，注：此统计的反馈时不是配送成功的订单");
		} else if ("qita".equals(flowType)) {
			model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate + "到" + crateEnddate + "): 其他订单，注：此统计包括退供货商出库、供货商拒收返库、撤销反馈的订单");
		}
		showDate(model, crateStartdate, crateEnddate, customerid, branchid, page, flowType, startinSitetime, endinSitetime);

		return "/monitor/site/siteShow";

	}

	// 详细页导出
	@RequestMapping("/dateShowExport")
	public void dateShowExp(Model model, HttpServletResponse response, HttpServletRequest request) {
		String flowType = request.getSession().getAttribute("flowType").toString();
		String crateStartdate = request.getSession().getAttribute("crateStartdate").toString();
		String crateEnddate = request.getSession().getAttribute("crateEnddate").toString();
		String customerid = request.getSession().getAttribute("customerid").toString();
		long branchid = new Long(request.getSession().getAttribute("branchid").toString());
		String startinSitetime = request.getSession().getAttribute("startinSitetime").toString();
		String endinSitetime = request.getSession().getAttribute("endinSitetime").toString();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		String exportmould2 = request.getParameter("exportmould2");
		model.addAttribute("usershowphoneflag", showphoneflag);

		monitorSiteExportService.MonitorSitExport(crateStartdate, crateEnddate, customerid, branchid, flowType, startinSitetime, endinSitetime, exportmould2, response);

	}

	// 返回第一页面
	@RequestMapping("/back")
	public String goback(Model model, HttpServletResponse response, HttpServletRequest request) {
		String crateStartdate = request.getSession().getAttribute("crateStartdate").toString();
		String crateEnddate = request.getSession().getAttribute("crateEnddate").toString();
		String customerid = request.getSession().getAttribute("customerid").toString();
		long branchid = new Long(request.getSession().getAttribute("branchid").toString());
		String startinSitetime = request.getSession().getAttribute("startinSitetime").toString();
		String endinSitetime = request.getSession().getAttribute("endinSitetime").toString();
		model.addAttribute("crateStartdate", crateStartdate);
		model.addAttribute("crateEnddate", crateEnddate);
		model.addAttribute("startinSitetime", startinSitetime);
		model.addAttribute("endinSitetime", endinSitetime);

		// 查询所有的站点
		List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
		model.addAttribute("branchnameList", branchnameList);
		// 查询当前站点
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		long branchidPram = branchid;
		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		Branch nowbranch = getDmpDAO.getNowBranch(branchidSession);
		if (nowbranch != null) {
			if (nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				model.addAttribute("nowBranch", nowbranch);
				branchidPram = branchidSession;
			}
		}

		model.addAttribute("branchidSession", branchidPram);
		setCustomerlist(model, customerid, branchid, dmpid);
		setSiteModels(model, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		model.addAttribute("crateStartdate", crateStartdate);
		model.addAttribute("crateEnddate", crateEnddate);
		return "/monitor/site/siteMonitor";

	}

	// ======以下都是封装方法=============/////

	// 存储库房信息
	private void setSiteModels(Model model, String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		// 实际应到货量 已到货 已领货 库存 在途 未交款 欠款
		// 实际应到货量 入库未领 有货无单 异常单 有单无货 已领货 遗留单 退货 滞留 中转 退货 未交款 欠款

		// 系统信息匹配量
		model.addAttribute("daoruDate", monitorService.getSiteDate(crateStartdate, crateEnddate, customerid, branchid, "rkzs", "", ""));
		model.addAttribute("daoruDateType", "rkzs");
		// 实际应到货量
		model.addAttribute("weidaohuoDate", monitorService.getSiteDate(crateStartdate, crateEnddate, customerid, branchid, "weidaohuo", "", ""));
		model.addAttribute("weidaohuoDateType", "weidaohuo");
		// 入库未领
		model.addAttribute("rukuweilingDate", monitorService.getSiteDate("", "", customerid, branchid, "rukuweiling", startinSitetime, endinSitetime));
		model.addAttribute("rukuweilingDateType", "rukuweiling");
		// 有货无单
		model.addAttribute("youhuowudanDate", monitorService.getSiteDate("", "", customerid, branchid, "youhuowudan", startinSitetime, endinSitetime));
		model.addAttribute("youhuowudanDateType", "youhuowudan");
		// 有单无货
		model.addAttribute("youdanwuhuoDate", monitorService.getSiteDate("", "", customerid, branchid, "youdanwuhuo", startinSitetime, endinSitetime));
		model.addAttribute("youdanwuhuoDateType", "youdanwuhuo");
		// 异常单
		model.addAttribute("yichangdanDate", monitorService.getSiteDate("", "", customerid, branchid, "yichangdan", startinSitetime, endinSitetime));
		model.addAttribute("yichangdanDateType", "yichangdan");
		// 已领货
		model.addAttribute("yilinghuoDate", monitorService.getSiteDate("", "", customerid, branchid, "yilinghuo", startinSitetime, endinSitetime));
		model.addAttribute("yilinghuoDateType", "yilinghuo");
		// 遗留库存
		model.addAttribute("yiliudanDate", monitorService.getSiteDate("", "", customerid, branchid, "yiliudan", startinSitetime, endinSitetime));
		model.addAttribute("yiliudanDateType", "yiliudan");
		// 退货库存
		model.addAttribute("kucuntuihuoDate", monitorService.getSiteDate("", "", customerid, branchid, "kucuntuihuo", startinSitetime, endinSitetime));
		model.addAttribute("kucuntuihuoDateType", "kuncuntuihuo");
		// 滞留库存
		model.addAttribute("kucunzhiliuDate", monitorService.getSiteDate("", "", customerid, branchid, "kucunzhiliu", startinSitetime, endinSitetime));
		model.addAttribute("kucunzhiliuDateType", "kucunzhiliu");
		// 中转在途
		model.addAttribute("zaituzhongzhuanDate", monitorService.getSiteDate("", "", customerid, branchid, "zaituzhongzhuan", startinSitetime, endinSitetime));
		model.addAttribute("zaituzhongzhuanDateType", "zaituzhongzhuan");
		// 退货在途
		model.addAttribute("zaitutuihuoDate", monitorService.getSiteDate("", "", customerid, branchid, "zaitutuihuo", startinSitetime, endinSitetime));
		model.addAttribute("zaitutuihuoDateType", "zaitutuihuo");
		// 未交款
		model.addAttribute("weijiaokuanDate", monitorService.getSiteDate("", "", customerid, branchid, "weijiaokuan", startinSitetime, endinSitetime));
		model.addAttribute("weijiaokuanDateType", "weijiaokuan");
		// 欠款
		model.addAttribute("qiankuanDate", monitorService.getSiteDate("", "", customerid, branchid, "qiankuan", startinSitetime, endinSitetime));
		model.addAttribute("qiankuanDateType", "qiankuan");
		// 妥投
		model.addAttribute("tuotouDate", monitorService.getSiteDate("", "", customerid, branchid, "tuotou", startinSitetime, endinSitetime));
		model.addAttribute("tuotouDateType", "tuotou");
		// 为妥投
		model.addAttribute("weituotouDate", monitorService.getSiteDate("", "", customerid, branchid, "weituotou", startinSitetime, endinSitetime));
		model.addAttribute("weituotouDateType", "weituotou");
		// 其他
		model.addAttribute("qitaDate", monitorService.getSiteDate("", "", customerid, branchid, "qita", startinSitetime, endinSitetime));
		model.addAttribute("qitaDateType", "qita");

	}

	// 查询具体订单数据
	private void showDate(Model model, String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String type, String startinSitetime, String endinSitetime) {
		model.addAttribute("showDateList", monitorService.getSiteDateList(crateStartdate, crateEnddate, customerid, branchid, type, page, startinSitetime, endinSitetime));
		model.addAttribute("page_obj", new Page(monitorService.getSiteDateCount(crateStartdate, crateEnddate, customerid, branchid, type, startinSitetime, endinSitetime), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
	}

	// 保存基本查询条件的session
	private void setSetions(HttpServletRequest request, String crateStartdate, String crateEnddate, String customerid, long branchid, long branchidSession, String startinSitetime, String endinSitetime) {
		request.getSession().setAttribute("crateStartdate", crateStartdate);
		request.getSession().setAttribute("crateEnddate", crateEnddate);
		request.getSession().setAttribute("customerid", customerid);
		request.getSession().setAttribute("branchid", branchid);
		request.getSession().setAttribute("branchid_Session", branchidSession);
		request.getSession().setAttribute("startinSitetime", startinSitetime);
		request.getSession().setAttribute("endinSitetime", endinSitetime);
	}

	// 保存前台传递过来的供货商解析后存储到列表中
	private void setCustomerlist(Model model, String customerid, long branchid, String dmpid) {
		List<Customer> cumstrListAll = getDmpDAO.getAllCustomers();
		// 保存前台传递过来的供货商解析后存储到列表中
		List cumstrList1 = new ArrayList();
		if (customerid != null && customerid.length() > 0) {
			String[] cStr = customerid.split(",");
			for (int i = 0; i < cStr.length; i++) {
				cumstrList1.add(cStr[i]);
			}
		}
		model.addAttribute("cumstrList1", cumstrList1);
		model.addAttribute("cumstrListAll", cumstrListAll);
		// 查询所有的站点
		List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
		model.addAttribute("branchnameList", branchnameList);
		// 查询当前站点

		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		long branchidPram = branchid == -1 ? branchidSession : branchid;
		model.addAttribute("branchidSession", branchidPram);

	}

}