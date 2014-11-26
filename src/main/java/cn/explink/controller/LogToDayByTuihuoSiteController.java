package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.LogTodayByTuihuoDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TuihuoSiteTodaylog;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.LogToDayByTuihuoService;
import cn.explink.service.LogToDayExportService;
import cn.explink.service.SystemInstallService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.Page;

@RequestMapping("/tuihuoLog")
@Controller
public class LogToDayByTuihuoSiteController {

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	OrderFlowDAO orderFlowDAO;

	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	@Autowired
	LogTodayByTuihuoDAO logTodayByTuihuoDAO;
	@Autowired
	LogToDayByTuihuoService logToDayByTuihuoService;
	@Autowired
	LogToDayExportService logToDayExportService;

	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	SystemInstallService systemInstallService;
	@Autowired
	SystemInstallDAO systemInstallDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/nowlog")
	public String nowlog(Model model, @RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid) {
		List<Branch> tuihuoList = branchDAO.getBranchBySiteType(BranchEnum.TuiHuo.getValue());
		if (branchid == 0) {
			branchid = (tuihuoList != null && tuihuoList.size() > 0) ? tuihuoList.get(0).getBranchid() : 0;
		}
		String startTime = "";
		TuihuoSiteTodaylog tuihuoSiteTodaylog = logTodayByTuihuoDAO.getLastBranchTodayLogByWarehouseid(branchid);
		if (tuihuoSiteTodaylog != null) {
			startTime = tuihuoSiteTodaylog.getCteatetime();
		} else {
			SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("tuiHuoDayLogTime");
			if (siteDayLogTime == null || !StringUtils.hasLength(siteDayLogTime.getValue())) {
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			} else {
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1)) + " " + siteDayLogTime.getValue();
			}
		}
		// 站点应退
		// long zhandianyingtui =
		// logTodayByTuihuoDAO.getZhanDianYingtuiCount(FlowOrderTypeEnum.YiShenHe.getValue(),
		// DeliveryStateEnum.JuShou.getValue()+","+DeliveryStateEnum.ShangMenHuanChengGong.getValue()+","+DeliveryStateEnum.ShangMenTuiChengGong.getValue());
		// 退供货商出库
		long tuigonghuoshangchuku = logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), startTime);
		// 退货站入库
		long tuihuozhanruku = logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), startTime);
		// 退货站退货出库在途

		String branchids = "";
		for (Branch branch : tuihuoList) {
			branchids += branch.getBranchid() + ",";
		}
		branchids = branchids.length() > 0 ? branchids.substring(0, branchids.length() - 1) : "0";
		long tuihuozhantuihuochukuzaitu = logTodayByTuihuoDAO.getFlowTypeCountByTuihuozhan(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchids, startTime);
		// 站点退货出站
		long zhandiantuihuochukuzaitu = logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), startTime);
		// 供货商拒收返库
		long gonghuoshangjushoufanku = logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), startTime);
		// 供货商退货成功
		long gonghuoshangshouhuo = logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), startTime);

		TuihuoSiteTodaylog tview = new TuihuoSiteTodaylog();
		tview.setZhandianyingtui(0);
		tview.setTuigonghuoshangchuku(tuigonghuoshangchuku);
		tview.setTuihuozhanruku(tuihuozhanruku);
		tview.setTuihuozhantuihuochukuzaitu(tuihuozhantuihuochukuzaitu);
		tview.setZhandiantuihuochukuzaitu(zhandiantuihuochukuzaitu);
		tview.setGonghuoshangjushoufanku(gonghuoshangjushoufanku);
		tview.setGonghuoshangshouhuo(gonghuoshangshouhuo);

		model.addAttribute("tuihuoSiteTodaylogview", tview);
		model.addAttribute("startTime", startTime);
		model.addAttribute("tuihuoList", tuihuoList);
		model.addAttribute("branchid", branchid);

		return "logtoday/tuihuo/todaylog";
	}

	@RequestMapping("/getYingtuiCount")
	public @ResponseBody JSONObject getYingtuiCount() {
		JSONObject obj = new JSONObject();
		// 站点应退
		long zhandianyingtui = logTodayByTuihuoDAO.getZhanDianYingtuiCount(FlowOrderTypeEnum.YiShenHe.getValue(),
				DeliveryStateEnum.JuShou.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue());
		obj.put("yingtuiCount", zhandianyingtui);
		return obj;
	}

	@RequestMapping("/historylog")
	public String historylog(Model model, @RequestParam(value = "createdate", required = false, defaultValue = "") String createdate) {
		List<Branch> kufangList = branchDAO.getBranchBySiteType(BranchEnum.TuiHuo.getValue());
		long branchid = (kufangList != null && kufangList.size() > 0) ? kufangList.get(0).getBranchid() : 0;
		String statetime = "".equals(createdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : createdate + " 00:00:00";
		String endtime = "".equals(createdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : createdate + " 23:59:59";
		List<TuihuoSiteTodaylog> tuihuoSiteTodaylogList = logTodayByTuihuoDAO.getHistoryLog(branchid, statetime, endtime);
		model.addAttribute("tuihuoSiteTodaylogList", tuihuoSiteTodaylogList);
		model.addAttribute("createdate", createdate);
		return "logtoday/tuihuo/historylog";
	}

	@RequestMapping("/show/{type}/{page}")
	public String show(Model model, @PathVariable("type") String type, @PathVariable("page") long page) {
		List<Branch> kufangList = branchDAO.getBranchBySiteType(BranchEnum.TuiHuo.getValue());
		long branchid = (kufangList != null && kufangList.size() > 0) ? kufangList.get(0).getBranchid() : 0;
		String startTime = "";
		TuihuoSiteTodaylog tuihuoSiteTodaylog = logTodayByTuihuoDAO.getLastBranchTodayLogByWarehouseid(branchid);
		if (tuihuoSiteTodaylog != null) {
			startTime = tuihuoSiteTodaylog.getCteatetime();
		} else {
			SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("tuiHuoDayLogTime");
			if (siteDayLogTime == null || !StringUtils.hasLength(siteDayLogTime.getValue())) {
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			} else {
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1)) + " " + siteDayLogTime.getValue();
			}
		}
		List<CwbOrder> cwborderList = logToDayByTuihuoService.getOrderListByBranchidAndType(kufangList, branchid, type, startTime, page);
		long count = logToDayByTuihuoService.getOrderListByBranchidAndTypeConut(kufangList, branchid, type, startTime);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("customerMap", customerDAO.getAllCustomersToMap());
		Map<Long, CustomWareHouse> customerWarehouseMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse customWareHouse : customWareHouseDAO.getAllCustomWareHouse()) {
			customerWarehouseMap.put(customWareHouse.getWarehouseid(), customWareHouse);
		}
		model.addAttribute("customerWarehouseMap", customerWarehouseMap);
		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : branchDAO.getAllBranches()) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("cwborderList", cwborderList);
		model.addAttribute("branchMap", branchMap);
		model.addAttribute("branchid", branchid);
		model.addAttribute("typeStr", getTypeStr(type));
		model.addAttribute("type", type);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		return "logtoday/tuihuo/show";
	}

	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletResponse response, @RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid, @RequestParam(value = "exportmould", required = false, defaultValue = "") String exportmould

	) {
		// List<Branch> kufangList=
		// branchDAO.getBranchBySiteType(BranchEnum.TuiHuo.getValue());
		// long branchid =(kufangList!=null && kufangList.size()>0) ?
		// kufangList.get(0).getBranchid() : 0;
		//
		String startTime = "";
		TuihuoSiteTodaylog tuihuoSiteTodaylog = logTodayByTuihuoDAO.getLastBranchTodayLogByWarehouseid(branchid);
		if (tuihuoSiteTodaylog != null) {
			startTime = tuihuoSiteTodaylog.getCteatetime();
		} else {
			SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("tuiHuoDayLogTime");
			if (siteDayLogTime == null || !StringUtils.hasLength(siteDayLogTime.getValue())) {
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			} else {
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1)) + " " + siteDayLogTime.getValue();
			}
		}
		List<Branch> tuihuoList = branchDAO.getBranchBySiteType(BranchEnum.TuiHuo.getValue());
		String branchids = "";
		for (Branch branch : tuihuoList) {
			branchids += branch.getBranchid() + ",";
		}
		branchids = branchids.length() > 0 ? branchids.substring(0, branchids.length() - 1) : "0";
		logToDayExportService.excelPublicMethodByTuihuo(response, branchids, type, startTime, exportmould);

	}

	private String getTypeStr(String type) {
		String typeStr = "当前查看的是：";
		if ("zhandianyingtuihuo".equals(type)) {
			return typeStr + "站点已审核为拒收、上门退成功、上门换成功，且还未退货出站的订单";
		}
		if ("zhandiantuihuochukuzaitu".equals(type)) {
			return typeStr + "今日站点正退货出站的订单";
		}
		if ("tuihuozhanruku".equals(type)) {
			return typeStr + "今日退货站正退货站入库的订单";
		}
		if ("tuihuozhantuihuochukuzaitu".equals(type)) {
			return typeStr + "今日退货站正退货再投出库的订单";
		}
		if ("tuigonghuoshangchuku".equals(type)) {
			return typeStr + "今日退货站正退供货商出库的订单";
		}
		if ("gonghuoshangshouhuo".equals(type)) {
			return typeStr + "今日退货站退供货商成功的订单";
		}
		if ("gonghuoshangjushoufanku".equals(type)) {
			return typeStr + "今日退货站退供货商拒收返库的订单";
		}

		return "";
	}

}
