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
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.MonitorDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Exportmould;
import cn.explink.domain.User;
import cn.explink.service.DeliveryService;
import cn.explink.service.ExportService;
import cn.explink.service.MonitorHouseExportService;
import cn.explink.service.MonitorHouseService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/monitorhouse")
public class MonitorHouseController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	MonitorHouseService monitorService;
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
	MonitorDAO monitorDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	MonitorHouseExportService monitorHouseExportService;
	@Autowired
	CwbDAO cwbDAO;

	// 第一页
	@RequestMapping("/date")
	public String list(Model model, HttpServletResponse response, HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		Branch branch = getDmpDAO.getNowBranch(user.getBranchid());
		List<Branch> kufangListView = getDmpDAO.getBranchByKufang();
		List<Branch> kufangList = new ArrayList<Branch>();
		if (branch != null && branch.getSitetype() == 1) {
			kufangList.add(branch);
			model.addAttribute("houseviewlist", monitorService.getHouseViewByNowBranchList(kufangList));
		} else {
			kufangList = getDmpDAO.getBranchByKufang();
			model.addAttribute("houseviewlist", monitorService.getHouseViewList(kufangList));
		}
		model.addAttribute("kufangList", kufangListView);
		return "/monitor/house/houseMonitor";

	}

	// 查看详细页
	@RequestMapping("/dateshow/{page}")
	public String dateShow(Model model, @PathVariable("page") long page, @RequestParam(value = "datetype", required = false, defaultValue = "") String datetype,
			@RequestParam(value = "carwarehouse", required = false, defaultValue = "") long carwarehouse, HttpServletRequest request) {
		List<CwbOrder> cwborderList = new ArrayList<CwbOrder>();
		long flowordertype = 0;
		long startbranchid = 0;
		long currentbranchid = 0;
		String datetypeValue = "";
		String valueSql = "";
		Page pageparm = new Page();
		if ("yingruku".equals(datetype)) {
			cwborderList = cwbDAO.getCwbByTypeAndCarwarehouse(page, carwarehouse, flowordertype, startbranchid, currentbranchid);
			pageparm = new Page(cwbDAO.getCwbByTypeAndCarwarehouseCount(carwarehouse, flowordertype, startbranchid, currentbranchid), page, Page.ONE_PAGE_NUMBER);
			valueSql = cwbDAO.getCwbByTypeAndCarwarehouseSQL(carwarehouse, flowordertype, startbranchid, currentbranchid);
			datetypeValue = "yingruku";
		}
		if ("weiruku".equals(datetype)) {
			cwborderList = cwbDAO.getCwbByTypeAndCarwarehouse(page, carwarehouse, 3, startbranchid, currentbranchid);
			pageparm = new Page(cwbDAO.getCwbByTypeAndCarwarehouseCount(carwarehouse, 3, startbranchid, currentbranchid), page, Page.ONE_PAGE_NUMBER);
			valueSql = cwbDAO.getCwbByTypeAndCarwarehouseSQL(carwarehouse, 3, startbranchid, currentbranchid);
			datetypeValue = "weiruku";
		}
		if ("chukuzaitu".equals(datetype)) {
			cwborderList = cwbDAO.getCwbByTypeAndCarwarehouse(page, carwarehouse, 6, carwarehouse, currentbranchid);
			pageparm = new Page(cwbDAO.getCwbByTypeAndCarwarehouseCount(carwarehouse, 6, startbranchid, currentbranchid), page, Page.ONE_PAGE_NUMBER);
			valueSql = cwbDAO.getCwbByTypeAndCarwarehouseSQL(carwarehouse, 6, startbranchid, currentbranchid);
			datetypeValue = "chukuzaitu";
		}
		if ("kucun".equals(datetype)) {
			cwborderList = cwbDAO.getCwbByTypeAndCarwarehouse(page, carwarehouse, 4, startbranchid, carwarehouse);
			pageparm = new Page(cwbDAO.getCwbByTypeAndCarwarehouseCount(carwarehouse, 4, startbranchid, currentbranchid), page, Page.ONE_PAGE_NUMBER);
			valueSql = cwbDAO.getCwbByTypeAndCarwarehouseSQL(carwarehouse, 4, startbranchid, currentbranchid);
			datetypeValue = "kucun";
		}
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		List<Exportmould> exportmouldlist = getDmpDAO.getExportmoulds(user, dmpid);
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		request.getSession().setAttribute("valueSql", valueSql);
		model.addAttribute("datetype", datetypeValue);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("carwarehouse", carwarehouse);
		model.addAttribute("usershowphoneflag", showphoneflag);
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("cwborderList", cwborderList);

		return "/monitor/house/houseShow";
	}

	// 详细页导出
	@RequestMapping("/dateShowExport")
	public void dateShowExp(Model model, HttpServletResponse response, HttpServletRequest request) {

		String valueSql = request.getSession().getAttribute("valueSql").toString();
		String exportmould2 = request.getParameter("exportmould2");
		monitorHouseExportService.MonitorHouseExport(valueSql, exportmould2, response);

	}

	/*
	 * // 第一页导出
	 * 
	 * @RequestMapping("/dateExport") public String dateExp(Model model,
	 * HttpServletResponse response, HttpServletRequest request) { String
	 * crateStartdate =
	 * request.getSession().getAttribute("crateStartdate").toString(); String
	 * crateEnddate =
	 * request.getSession().getAttribute("crateEnddate").toString(); String
	 * customerid = request.getSession().getAttribute("customerid").toString();
	 * long branchid = new
	 * Long(request.getSession().getAttribute("branchid").toString()); Branch
	 * branch = getDmpDAO.getNowBranch(branchid);
	 * model.addAttribute("nowbranchName", branch == null ? "全部" :
	 * branch.getBranchname()); //setHouseModels(model, crateStartdate,
	 * crateEnddate, customerid, branchid); return "/monitor/house/houseExpo";
	 * 
	 * }
	 */
	/*
	 * @RequestMapping("/dateshow/{flowType}/{page}") public String
	 * dateShow(Model model, @PathVariable("page") long page,
	 * @PathVariable("flowType") String flowType, HttpServletResponse response,
	 * HttpServletRequest request) { String crateStartdate =
	 * request.getSession().getAttribute("crateStartdate").toString(); String
	 * crateEnddate =
	 * request.getSession().getAttribute("crateEnddate").toString();
	 * 
	 * String customerid =
	 * request.getSession().getAttribute("customerid").toString(); long branchid
	 * = new Long(request.getSession().getAttribute("branchid").toString());
	 * 
	 * String dmpid = request.getSession().getAttribute("dmpid") == null ? "" :
	 * request.getSession().getAttribute("dmpid").toString(); int showphoneflag
	 * = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
	 * model.addAttribute("usershowphoneflag", showphoneflag); User user =
	 * getDmpDAO.getLogUser(dmpid); List<Exportmould> exportmouldlist =
	 * getDmpDAO.getExportmoulds(user, dmpid);
	 * model.addAttribute("exportmouldlist", exportmouldlist); if
	 * ("".equals(flowType)) { flowType = (String)
	 * request.getSession().getAttribute("flowType"); } else {
	 * request.getSession().setAttribute("flowType", flowType); } if
	 * ("rkzs".equals(flowType)) { model.addAttribute("flowTypeStr", "当前查看的是(" +
	 * crateStartdate + "到" + crateEnddate + "): 应入库订单"); } else if
	 * ("wrk".equals(flowType)) { model.addAttribute("flowTypeStr", "当前查看的是(" +
	 * crateStartdate + "到" + crateEnddate + ")：整批未入库订单"); } else if
	 * ("yrk".equals(flowType)) { model.addAttribute("flowTypeStr", "当前查看的是(" +
	 * crateStartdate + "到" + crateEnddate + ")：已入库订单，注：统计的是已做过入库扫描的订单"); } else
	 * if ("yhwd".equals(flowType)) { model.addAttribute("flowTypeStr",
	 * "当前查看的是(" + crateStartdate + "到" + crateEnddate +
	 * ")：到错货订单，注：统计的是入库扫描时，没有匹配到该库房导入的单号"); } else if ("ydwh".equals(flowType))
	 * {// 库房少货 model.addAttribute("flowTypeStr", "当前查看的是(" + crateStartdate +
	 * "到" + crateEnddate + ")：库房少货订单，注：多批次订单的库房少货按批累加"); } else if
	 * ("ckzt".equals(flowType)) { model.addAttribute("flowTypeStr", "当前查看的是(" +
	 * crateStartdate + "到" + crateEnddate + ")：出库在途订单，注：只要是做过出库扫描，均为出库在途订单"); }
	 * else if ("kc".equals(flowType)) { model.addAttribute("flowTypeStr",
	 * "当前查看的是(" + crateStartdate + "到" + crateEnddate +
	 * ")：库存订单，注：此处统计的是所查询条件范围内库存的货物，不包括历史遗留的货物"); } Branch prambranch =
	 * getDmpDAO.getNowBranch(branchid);// 页面传过来的参数 if (prambranch != null &&
	 * prambranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) { //
	 * 此处是退货中转组的统计 showzhongzhuanDate(model, crateStartdate, crateEnddate,
	 * customerid, branchid, page, flowType); return "/monitor/house/houseShow";
	 * } if (prambranch != null && prambranch.getSitetype() ==
	 * BranchEnum.TuiHuo.getValue()) { // 此处是退货中转组的统计 showtuihuoDate(model,
	 * crateStartdate, crateEnddate, customerid, branchid, page, flowType);
	 * return "/monitor/house/houseShow"; }
	 * 
	 * showDate(model, crateStartdate, crateEnddate, customerid, branchid, page,
	 * flowType); return "/monitor/house/houseShow";
	 * 
	 * }
	 */

	// 返回第一页面
	/*
	 * @RequestMapping("/back") public String goback(Model model,
	 * HttpServletResponse response, HttpServletRequest request) { String
	 * crateStartdate =
	 * request.getSession().getAttribute("crateStartdate").toString(); String
	 * crateEnddate =
	 * request.getSession().getAttribute("crateEnddate").toString(); String
	 * customerid = request.getSession().getAttribute("customerid").toString();
	 * long branchid = new
	 * Long(request.getSession().getAttribute("branchid").toString()); //
	 * 查询所有的库房 List<Branch> kufangList = getDmpDAO.getBranchByKufang();
	 * model.addAttribute("kufangList", kufangList); // 查询当前库房 String dmpid =
	 * request.getSession().getAttribute("dmpid") == null ? "" :
	 * request.getSession().getAttribute("dmpid").toString(); long branchidPram
	 * = branchid; long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
	 * Branch nowbranch = getDmpDAO.getNowBranch(branchidSession); Branch
	 * prambranch = getDmpDAO.getNowBranch(branchidPram);// 页面传过来的参数 if
	 * (nowbranch != null) { if (nowbranch.getSitetype() ==
	 * BranchEnum.KuFang.getValue()) { model.addAttribute("nowBranch",
	 * nowbranch); branchidPram = branchidSession; } } if (prambranch != null &&
	 * prambranch.getSitetype() == BranchEnum.TuiHuo.getValue() ||
	 * prambranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) { //
	 * 此处是退货中转组的统计 model.addAttribute("branchidSession", branchidPram);
	 * setCustomerlist(model, customerid, branchidPram, dmpid); if
	 * (prambranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
	 * 
	 * setZhongzhuanModels(model, crateStartdate, crateEnddate, customerid,
	 * branchidPram); } else { setTuihuoModels(model, crateStartdate,
	 * crateEnddate, customerid, branchidPram); }
	 * model.addAttribute("crateStartdate", crateStartdate);
	 * model.addAttribute("crateEnddate", crateEnddate); return
	 * "/monitor/house/houseMonitor";
	 * 
	 * } model.addAttribute("branchidSession", branchidPram);
	 * setCustomerlist(model, customerid, branchid, dmpid);
	 * //setHouseModels(model, crateStartdate, crateEnddate, customerid,
	 * branchid); model.addAttribute("crateStartdate", crateStartdate);
	 * model.addAttribute("crateEnddate", crateEnddate); return
	 * "/monitor/house/houseMonitor";
	 * 
	 * }
	 */

	// ======以下都是封装方法=============/////

	// 存储库房信息
	/*
	 * private void setHouseModels(Model model) { // 入库总数
	 * model.addAttribute("rukuzongliangDate",
	 * monitorService.getHouseDate("rkzs"));
	 * model.addAttribute("rukuzongliangDateType", "rkzs"); // // 已入库 //
	 * model.addAttribute("yirukuDate",
	 * monitorService.getHouseDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "yrk")); // model.addAttribute("yirukuDateType", "yrk"); // //
	 * 整批未入库 // model.addAttribute("weirukuDate",
	 * monitorService.getHouseDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "wrk")); // model.addAttribute("weirukuDateType", "wrk"); // //
	 * 到错货 // model.addAttribute("youhuowudanDate",
	 * monitorService.getHouseDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "yhwd")); // model.addAttribute("youhuowudanDateType", "yhwd");
	 * // // 出库在途 // model.addAttribute("chukuzaituDate",
	 * monitorService.getHouseDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "ckzt")); // model.addAttribute("chukuzaituDateType", "ckzt");
	 * // // 库存 // model.addAttribute("kucunDate",
	 * monitorService.getHouseDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "kc")); // model.addAttribute("kucunDateType", "kc"); // //
	 * 库房少货 // model.addAttribute("youdanwuhuoDate",
	 * monitorService.getHouseDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "ydwh")); // model.addAttribute("youdanwuhuoDateType", "ydwh");
	 * }
	 * 
	 * // 存储中转的信息 private void setZhongzhuanModels(Model model, String
	 * crateStartdate, String crateEnddate, String customerid, long branchid) {
	 * // 入库总数 model.addAttribute("rukuzongliangDate",
	 * monitorService.getZhongzhuanDate(crateStartdate, crateEnddate,
	 * customerid,branchid, "rkzs"));
	 * model.addAttribute("rukuzongliangDateType", "rkzs"); // 已入库
	 * model.addAttribute("yirukuDate",
	 * monitorService.getZhongzhuanDate(crateStartdate, crateEnddate,
	 * customerid, branchid, "yrk")); model.addAttribute("yirukuDateType",
	 * "yrk"); // 整批未入库 model.addAttribute("weirukuDate",
	 * monitorService.getZhongzhuanDate(crateStartdate, crateEnddate,
	 * customerid, branchid, "wrk")); model.addAttribute("weirukuDateType",
	 * "wrk"); // 到错货 model.addAttribute("youhuowudanDate",
	 * monitorService.getZhongzhuanDate(crateStartdate, crateEnddate,
	 * customerid, branchid, "yhwd")); model.addAttribute("youhuowudanDateType",
	 * "yhwd"); // 出库在途 model.addAttribute("chukuzaituDate",
	 * monitorService.getZhongzhuanDate(crateStartdate, crateEnddate,
	 * customerid, branchid, "ckzt")); model.addAttribute("chukuzaituDateType",
	 * "ckzt"); // 库存 model.addAttribute("kucunDate",
	 * monitorService.getZhongzhuanDate(crateStartdate, crateEnddate,
	 * customerid, branchid, "kc")); model.addAttribute("kucunDateType", "kc");
	 * // 库房少货 model.addAttribute("youdanwuhuoDate",
	 * monitorService.getZhongzhuanDate(crateStartdate, crateEnddate,
	 * customerid, branchid, "ydwh")); model.addAttribute("youdanwuhuoDateType",
	 * "ydwh"); }
	 * 
	 * // 存储退货的信息 private void setTuihuoModels(Model model, String
	 * crateStartdate, String crateEnddate, String customerid, long branchid) {
	 * // 入库总数 model.addAttribute("rukuzongliangDate",
	 * monitorService.getTuihuoDate(crateStartdate, crateEnddate, customerid,
	 * branchid,"rkzs")); model.addAttribute("rukuzongliangDateType", "rkzs");
	 * // 已入库 model.addAttribute("yirukuDate",
	 * monitorService.getTuihuoDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "yrk")); model.addAttribute("yirukuDateType", "yrk"); // 整批未入库
	 * model.addAttribute("weirukuDate",
	 * monitorService.getTuihuoDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "wrk")); model.addAttribute("weirukuDateType", "wrk"); // 到错货
	 * model.addAttribute("youhuowudanDate",
	 * monitorService.getTuihuoDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "yhwd")); model.addAttribute("youhuowudanDateType", "yhwd"); //
	 * 出库在途 model.addAttribute("chukuzaituDate",
	 * monitorService.getTuihuoDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "ckzt")); model.addAttribute("chukuzaituDateType", "ckzt"); //
	 * 库存 model.addAttribute("kucunDate",
	 * monitorService.getTuihuoDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "kc")); model.addAttribute("kucunDateType", "kc"); // 库房少货
	 * model.addAttribute("youdanwuhuoDate",
	 * monitorService.getTuihuoDate(crateStartdate, crateEnddate, customerid,
	 * branchid, "ydwh")); model.addAttribute("youdanwuhuoDateType", "ydwh"); }
	 * 
	 * // 查询具体订单数据 private void showDate(Model model, String crateStartdate,
	 * String crateEnddate, String customerid, long branchid, long page, String
	 * type) { model.addAttribute("showDateList",
	 * monitorService.getHouseDateList(crateStartdate, crateEnddate, customerid,
	 * branchid, type, page)); model.addAttribute("page_obj", new
	 * Page(monitorService.getHouseDateCount(crateStartdate, crateEnddate,
	 * customerid, branchid, type), page, Page.ONE_PAGE_NUMBER));
	 * model.addAttribute("page", page); }
	 * 
	 * // 查询中转具体订单数据 private void showzhongzhuanDate(Model model, String
	 * crateStartdate, String crateEnddate, String customerid, long branchid,
	 * long page, String type) { model.addAttribute("showDateList",
	 * monitorService.getzhongzhuanDateList(crateStartdate, crateEnddate,
	 * customerid, branchid,type, page)); model.addAttribute("page_obj", new
	 * Page(monitorService.getzhongzhuanDateCount(crateStartdate, crateEnddate,
	 * customerid, branchid, type), page, Page.ONE_PAGE_NUMBER));
	 * model.addAttribute("page", page); }
	 * 
	 * // 查询退货具体订单数据 private void showtuihuoDate(Model model, String
	 * crateStartdate, String crateEnddate, String customerid, long branchid,
	 * long page, String type) { model.addAttribute("showDateList",
	 * monitorService.gettuihuoDateList(crateStartdate, crateEnddate,
	 * customerid, branchid, type, page)); model.addAttribute("page_obj", new
	 * Page(monitorService.gettuihuoDateCount(crateStartdate, crateEnddate,
	 * customerid, branchid, type), page, Page.ONE_PAGE_NUMBER));
	 * model.addAttribute("page", page); }
	 * 
	 * // 保存基本查询条件的session private void setSetions(HttpServletRequest request,
	 * String crateStartdate, String crateEnddate, String customerid, long
	 * branchid, long branchidSession) {
	 * request.getSession().setAttribute("crateStartdate", crateStartdate);
	 * request.getSession().setAttribute("crateEnddate", crateEnddate);
	 * request.getSession().setAttribute("customerid", customerid);
	 * request.getSession().setAttribute("branchid", branchid);
	 * request.getSession().setAttribute("branchid_Session", branchidSession); }
	 */

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