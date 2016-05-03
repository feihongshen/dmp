package cn.explink.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.LogTodayByWarehouseDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.WarehouseTodaylog;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.LogToDayByWarehouseService;
import cn.explink.service.LogToDayExportService;
import cn.explink.service.SystemInstallService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.Page;

@RequestMapping("/warehouseLog")
@Controller
public class LogToDayByWareHouseController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	LogTodayByWarehouseDAO logTodayByWarehouseDAO;

	@Autowired
	OrderFlowDAO orderFlowDAO;

	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	@Autowired
	LogToDayByWarehouseService logToDayByWarehouseService;

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
	
		List<Branch> kufangList = branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		
		if (branchid == 0) {
			//获取第一个库房id
			branchid = (kufangList != null && kufangList.size() > 0) ? kufangList.get(0).getBranchid() : 0;
		}
		
		//update by neo01.huang, 2016-4-5
		String startTime = "";
		//获取系统参数
		SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("wareHouseDayLogTime");
		//系统参数不为空
		if (siteDayLogTime != null && StringUtils.hasLength(siteDayLogTime.getValue())) {
			
			/*
			逻辑：
			假设当前时间是2016-4-20 10:52:16
			系统参数设置了： 23:00:00，则startTime就是2016-4-19 23:00:00
			假设当前时间是2016-4-20 23:01:00
			系统参数设置了： 23:00:00，则startTime就是2016-4-20 23:00:00
			update by neo01.huang，2016-4-28
			*/
			Date now = new Date();
			startTime = new SimpleDateFormat("yyyy-MM-dd").format(now) + " " + siteDayLogTime.getValue();
			
			Date startTimeDate = null;
			try {
				startTimeDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				logger.info("nowlog->startTime格式不正确，startTime:{}", startTime);
			}
			
			if (startTimeDate != null) {
				if (now.getTime() < startTimeDate.getTime()) { //如果当前时间还没到startTime，则startTime取昨天的时间
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					Date yesterday = calendar.getTime(); //昨天
					startTime = new SimpleDateFormat("yyyy-MM-dd").format(yesterday) + " " + siteDayLogTime.getValue();
				}
			}
			
		} else { //系统参数为空
			long randomBranchid = 0;
			if (branchid == 0) {
				//获取第一个库房id
				randomBranchid = (kufangList != null && kufangList.size() > 0) ? kufangList.get(0).getBranchid() : 0;
			}
			//获取最新一条统计log
			WarehouseTodaylog warehouseTodaylog = logTodayByWarehouseDAO.getLastBranchTodayLogByWarehouseid(randomBranchid);
			if (warehouseTodaylog != null) {
				startTime = warehouseTodaylog.getCteatetime();
				
			} else { //找不到最新一条统计log
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
				
			}
		}
		
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		// 未入库
		model.addAttribute(
				"weirukuMap",
				logToDayByWarehouseService.getWeirukuMap(branchid, FlowOrderTypeEnum.UpdateDeliveryBranch.getValue() + "," + FlowOrderTypeEnum.DaoRuShuJu.getValue() + ","
						+ FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.TuiHuoChuZhan.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()));
		// 已入库
		model.addAttribute("yirukuMap", logToDayByWarehouseService.getYirukuMap(branchid, FlowOrderTypeEnum.RuKu.getValue(), startTime, endTime));
		// 到错货
		model.addAttribute("dacuohuoMap", logToDayByWarehouseService.getDaocuohuoMap(branchid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()));

		// 今日出库
		model.addAttribute(
				"jinrichukuMap",
				logToDayByWarehouseService.getJinrichukuMap(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.TuiHuoChuZhan.getValue() + ","
						+ FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), startTime));

		// ===合并为出库===
		/*
		 * //昨日出库在途
		 * model.addAttribute("zuorichukuzaituMap",logToDayByWarehouseService
		 * .getZuorichukuzaituMap(branchid,
		 * FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),startTime)); //今日出库在途
		 * model.addAttribute("jinrichukuzaituMap",logToDayByWarehouseService.
		 * getJinrichukuzaituMap(branchid,
		 * FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),startTime)); //出库已到站
		 * model.addAttribute
		 * ("chukuyidaozhanMap",logToDayByWarehouseService.getChukuyidaozhanMap(
		 * FlowOrderTypeEnum
		 * .FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum
		 * .FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		 * FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid, startTime));
		 * //未出库已到站
		 * model.addAttribute("weichukuyidaozhanMap",logToDayByWarehouseService
		 * .getWeichukudaozhanMap(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),
		 * branchid, startTime));
		 */
		// ======合并为出库===

		// 今日库存
		model.addAttribute("kucunMap", logToDayByWarehouseService.getJinrikucunMap(branchid));

		List<WarehouseTodaylog> warehouseLogList = logTodayByWarehouseDAO.getLogByWarehouseidAndDate(branchid, startTime, startTime);

		Map zuirikucunMap = new HashMap();
		if (warehouseLogList != null) {
			for (WarehouseTodaylog warehouselog : warehouseLogList) {
				zuirikucunMap.put(warehouselog.getCustomerid(), warehouselog.getJinri_kucun());
			}
		}
		List<Customer> customerList = customerDAO.getAllCustomers();
		model.addAttribute("kufangList", kufangList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("zuirikucunMap", zuirikucunMap);
		model.addAttribute("startTime", startTime);
		model.addAttribute("branchid", branchid);

		return "logtoday/warehouse/todaylog";
	}

	@RequestMapping("/historylog")
	public String historylog(Model model, @RequestParam(value = "createdate", required = false, defaultValue = "") String createdate,
			@RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid) {

		List<Branch> kufangList = branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		if (branchid == 0) {
			branchid = (kufangList != null && kufangList.size() > 0) ? kufangList.get(0).getBranchid() : 0;
		}
		String statetime = "".equals(createdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : createdate + " 00:00:00";
		String endtime = "".equals(createdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : createdate + " 23:59:59";
		List<WarehouseTodaylog> warehouseLogList = logTodayByWarehouseDAO.getLogByWarehouseidAndDate(branchid, statetime, endtime);

		List<Customer> customerList = customerDAO.getAllCustomers();
		Map customerMap = new HashMap();
		for (Customer customer : customerList) {
			customerMap.put(customer.getCustomerid(), customer.getCustomername());
		}
		model.addAttribute("warehouseLogList", warehouseLogList);
		model.addAttribute("customerMap", customerMap);
		model.addAttribute("createdate", createdate);
		model.addAttribute("kufangList", kufangList);
		return "logtoday/warehouse/historylog";
	}

	@RequestMapping("/show/{customerid}/{type}/{count}/{branchid}/{page}")
	public String show(Model model, @PathVariable("customerid") long customerid, @PathVariable("type") String type, @PathVariable("count") long count, @PathVariable("branchid") long branchid,
			@PathVariable("page") long page) {
		// List<Branch> kufangList=
		// branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		// long branchid =(kufangList!=null && kufangList.size()>0) ?
		// kufangList.get(0).getBranchid() : 0;
		String startTime = "";
		WarehouseTodaylog warehouseTodaylog = logTodayByWarehouseDAO.getLastBranchTodayLogByWarehouseid(branchid);
		if (warehouseTodaylog != null) {
			startTime = warehouseTodaylog.getCteatetime();
		} else {
			SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("wareHouseDayLogTime");
			if (siteDayLogTime == null || !StringUtils.hasLength(siteDayLogTime.getValue())) {
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			} else {
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1)) + " " + siteDayLogTime.getValue();
			}
		}
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<CwbOrder> cwborderList = null;
		if (customerid > 0) {// 如果供货商id大于0
			cwborderList = logToDayByWarehouseService.getOrderListByCustomeridAndType(branchid, customerid, type, startTime, endTime, page);
		} else {
			cwborderList = logToDayByWarehouseService.getOrderListByType(branchid, type, startTime, endTime, page);
		}

		model.addAttribute("cwborderList", cwborderList);
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
		model.addAttribute("branchMap", branchMap);
		model.addAttribute("branchid", branchid);
		model.addAttribute("typeStr", getTypeStr(type));
		model.addAttribute("type", type);
		model.addAttribute("customerid", customerid);
		model.addAttribute("count", count);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		return "logtoday/warehouse/show";
	}

	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletResponse response, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "type", required = false, defaultValue = "") String type, @RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid,
			@RequestParam(value = "exportmould", required = false, defaultValue = "") String exportmould

	) {
		// List<Branch> kufangList=
		// branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		// long branchid =(kufangList!=null && kufangList.size()>0) ?
		// kufangList.get(0).getBranchid() : 0;
		String startTime = "";
		WarehouseTodaylog warehouseTodaylog = logTodayByWarehouseDAO.getLastBranchTodayLogByWarehouseid(branchid);
		if (warehouseTodaylog != null) {
			startTime = warehouseTodaylog.getCteatetime();
		} else {
			SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("wareHouseDayLogTime");
			if (siteDayLogTime == null || !StringUtils.hasLength(siteDayLogTime.getValue())) {
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			} else {
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1)) + " " + siteDayLogTime.getValue();
			}
		}
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		if (customerid > 0) {// 如果供货商不为0 则只导出对应供货商的数据
			logToDayExportService.excelPublicMethod(response, branchid, customerid, type, startTime, endTime, exportmould);
		} else {
			logToDayExportService.excelPublicByAllCustomeridMethod(response, branchid, type, startTime, endTime, exportmould);
		}

	}

	private String getTypeStr(String type) {
		String typeStr = "当前查看的是：";
		if ("weiruku".equals(type)) {
			return typeStr + "未入库的订单";
		}
		if ("yiruku".equals(type)) {
			return typeStr + "已入库的订单";
		}
		if ("daocuohuo".equals(type)) {
			return typeStr + "到错货的订单";
		}
		if ("zuorichukuzaitu".equals(type)) {
			return typeStr + "历史出库未到站的订单";
		}
		if ("jinrichukuzaitu".equals(type)) {
			return typeStr + "今日已出库的订单";
		}
		if ("chukuyidaozhan".equals(type)) {

			return typeStr + "出库已经到站的订单";
		}
		if ("weichukuyidaozhan".equals(type)) {
			return typeStr + "未做出库扫描，已到站的订单";
		}
		if ("kucun".equals(type)) {
			return typeStr + "库房当前库存的订单";
		}

		return "";
	}

}
