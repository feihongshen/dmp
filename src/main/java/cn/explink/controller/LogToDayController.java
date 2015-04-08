package cn.explink.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.LogTodayDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchTodayLog;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.logdto.TodayArrivalDTO;
import cn.explink.domain.logdto.TodayDeliveryDTO;
import cn.explink.domain.logdto.TodayFundsDTO;
import cn.explink.domain.logdto.TodayStockDTO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.LogToDayExportService;
import cn.explink.service.LogToDayService;
import cn.explink.service.SystemInstallService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

@RequestMapping("/logtoday")
@Controller
public class LogToDayController {

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	LogToDayService logToDayService;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	@Autowired
	LogTodayDAO logTodayDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	LogToDayExportService logToDayExportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	SystemInstallService systemInstallService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	ExportService exportService;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/carwarehouse")
	public String list(Model model, @RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid) {
		// 获取当前要查询的机构id
		Branch b = branchDAO.getBranchByBranchid(branchid == 0 ? getSessionUser().getBranchid() : branchid);
		// 判断当前机构是否为库房
		if (b.getSitetype() != BranchEnum.KuFang.getValue()) {
			model.addAttribute("isKuFang", false);
			return "logtoday/kufang";
		}

		// 获得所有的供货商
		List<Customer> cList = customerDAO.getAllCustomers();
		model.addAttribute("customerList", cList);
		// 获得所有供货商对应的应入库批次与应入库合计单数
		model.addAttribute("reNeedEmaildateGroupByCustomer", logToDayService.getNeedEmaildateDetailGroupByCustomer(b.getBranchid(), cList));

		// 获得所有供货商对应的已入库单数与金额
		// model.addAttribute("toDayIntoWarehousGroupByCustomer",logToDayService.getToDayIntoWarehousGroupByCustomer(branchid,
		// cList));

		return "logtoday/kufang";
	}

	@RequestMapping("/simpleSupervisory")
	public String simpleSupervisory(Model model, @RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid) {

		Branch b = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		if (b.getSitetype() == BranchEnum.ZhanDian.getValue() || b.getSitetype() == BranchEnum.KuFang.getValue() || b.getSitetype() == BranchEnum.ZhongZhuan.getValue()
				|| b.getSitetype() == BranchEnum.TuiHuo.getValue()) {// 如果当前用户不是
																		// 库房 站点
																		// 中转站
																		// 退货站的用户

			List<Branch> bList = new ArrayList<Branch>();
			bList.add(b);
			model.addAttribute("branchList", bList);
		} else {
			model.addAttribute("branchList", branchDAO.getAllEffectBranches());
		}
		branchid = branchid == 0 ? getSessionUser().getBranchid() : branchid;
		model.addAttribute("branchid", branchid);

		// 在途nextbranchid为当前站点，并且flowordertype为出库的
		model.addAttribute(
				"zaitu",
				cwbDAO.getCwbByNextbranchidAndFlowordertypeToJson(branchid,
						FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.TuiHuoChuZhan.getValue()).get(0));
		model.addAttribute("kucun", cwbDAO.getCwbByCurrentbranchidAndFlowordertypeToJson(branchid).get(0));
		// 派送中currentbranchid为当前站flowordertype为领货
		model.addAttribute("paisongzhong", deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateToJson(branchid, DeliveryStateEnum.WeiFanKui.getValue()).get(0));

		return "logtoday/simpleSupervisory";
	}

	/**
	 * 站点今日日志 显示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/todayArrival")
	public String todayArrival(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid) {
		// 获取所有站点
		BranchTodayLog yesterdayLog = new BranchTodayLog();
		List<JSONObject> peisongchenggongList = new ArrayList<JSONObject>();
		TodayArrivalDTO todayArrivalDTO = new TodayArrivalDTO();
		TodayDeliveryDTO todayDeliveryDTO = new TodayDeliveryDTO();
		TodayFundsDTO todayFundsDTO = new TodayFundsDTO();
		TodayStockDTO todayStockDTO = new TodayStockDTO();

		List<Branch> branchnameList = new ArrayList<Branch>();
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			// 获取昨日的日志
			yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(
					branch.getBranchid(), new Date());
			if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
				startTime = yesterdayLog.getCreatedate();
			}/*
			 * else{ SystemInstall siteDayLogTime =
			 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
			 * if(siteDayLogTime
			 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
			 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
			 * Date())+" 00:00:00"; }else{ startTime = new
			 * SimpleDateFormat("yyyy-MM-dd"
			 * ).format(DateDayUtil.getTimeByDay(-1))+" "
			 * +siteDayLogTime.getValue(); } }
			 */
			String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			// 已到货（单）
			// 配送成功单数、应收金额、应退金额
			peisongchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(), DeliveryStateEnum.PeiSongChengGong.getValue(), startTime,
					endTime);
			Date startTimeToDate = new Date();
			try {
				startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			} catch (ParseException e) {

			}
			todayArrivalDTO = logToDayService.getTodayArrivalDTO(branch, startTimeToDate, new Date());
			// =======今日投递日报=============
			todayDeliveryDTO = logToDayService.getTodayDeliveryDTO(branch, peisongchenggongList, startTimeToDate, new Date());
			// ============今日款项日报=================
			todayFundsDTO = logToDayService.getTodayFundsDTO(branch, startTimeToDate, new Date());
			// ==============今日库存日报==========================
			todayStockDTO = logToDayService.getTodayStockDTO(branch, startTimeToDate, new Date());
			long tuotou = todayDeliveryDTO.getToday_fankui_peisongchenggong_count() + todayDeliveryDTO.getToday_fankui_shangmenhuanchenggong_count()
					+ todayDeliveryDTO.getToday_fankui_shangmentuichenggong_count();

			// 存储站点到站和漏扫到站
			todayDeliveryDTO.setToday_fankui_daohuo(todayArrivalDTO.getYidaohuo());
			todayDeliveryDTO.setJinri_lousaodaozhan(todayArrivalDTO.getJinri_lousaodaozhan());

			todayDeliveryDTO.setToday_fankui_daohuo(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());
			todayStockDTO.setJinridaohuo(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());

			todayStockDTO.setJinrituodou(tuotou);
			if (yesterdayLog != null) {
				todayDeliveryDTO.setToday_fankui_zhiliu(yesterdayLog.getZhiliu_kuicun());
				todayDeliveryDTO.setToday_fankui_zuoriweiguiban(yesterdayLog.getWeiguiban_kuicun());

				todayStockDTO.setZuori_jushou_kuicun(yesterdayLog.getJushou_kuicun());
				todayStockDTO.setZuori_zhiliu_kuicun(yesterdayLog.getZhiliu_kuicun());
				todayStockDTO.setZuori_weiguiban_kuicun(yesterdayLog.getWeiguiban_kuicun());
				todayDeliveryDTO.setZuori_toudi_daozhanweiling(yesterdayLog.getToudi_daozhanweiling());
			}

			branchnameList.add(branch);
		} else {
			if (branchid > 0) {
				Branch branchPram = branchDAO.getBranchByBranchid(branchid);
				if (branchPram.getSitetype() == BranchEnum.ZhanDian.getValue()) {

					// 获取昨日的日志
					yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
							new Date());
					if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
						startTime = yesterdayLog.getCreatedate();
					}/*
					 * else{ SystemInstall siteDayLogTime =
					 * systemInstallDAO.getSystemInstallByName
					 * ("siteDayLogTime");
					 * if(siteDayLogTime==null||!StringUtils.
					 * hasLength(siteDayLogTime.getValue())){ startTime= new
					 * SimpleDateFormat("yyyy-MM-dd").format(new
					 * Date())+" 00:00:00"; }else{ startTime = new
					 * SimpleDateFormat
					 * ("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1))+" "
					 * +siteDayLogTime.getValue(); } }
					 */
					String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());// 配送成功单数、应收金额、应退金额
					peisongchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branchid, DeliveryStateEnum.PeiSongChengGong.getValue(), startTime,
							endTime);
					Date startTimeToDate = new Date();
					try {
						startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
					} catch (ParseException e) {

					}
					todayArrivalDTO = logToDayService.getTodayArrivalDTO(branchPram, startTimeToDate, new Date());
					// =======今日投递日报=============
					todayDeliveryDTO = logToDayService.getTodayDeliveryDTO(branchPram, peisongchenggongList, startTimeToDate, new Date());
					// ============今日款项日报=================
					todayFundsDTO = logToDayService.getTodayFundsDTO(branchPram, startTimeToDate, new Date());
					// ==============今日库存日报==========================
					todayStockDTO = logToDayService.getTodayStockDTO(branchPram, startTimeToDate, new Date());
					long tuotou = todayDeliveryDTO.getToday_fankui_peisongchenggong_count() + todayDeliveryDTO.getToday_fankui_shangmenhuanchenggong_count()
							+ todayDeliveryDTO.getToday_fankui_shangmentuichenggong_count();

					todayStockDTO.setJinrituodou(tuotou);

					// 存储站点到站和漏扫到站
					todayDeliveryDTO.setToday_fankui_daohuo(todayArrivalDTO.getYidaohuo());
					todayDeliveryDTO.setJinri_lousaodaozhan(todayArrivalDTO.getJinri_lousaodaozhan());

					todayDeliveryDTO.setToday_fankui_daohuo(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());
					todayStockDTO.setJinridaohuo(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());

					if (yesterdayLog != null) {

						todayDeliveryDTO.setToday_fankui_zhiliu(yesterdayLog.getZhiliu_kuicun());
						todayDeliveryDTO.setToday_fankui_zuoriweiguiban(yesterdayLog.getWeiguiban_kuicun());

						todayStockDTO.setZuori_jushou_kuicun(yesterdayLog.getJushou_kuicun());
						todayStockDTO.setZuori_zhiliu_kuicun(yesterdayLog.getZhiliu_kuicun());
						todayStockDTO.setZuori_weiguiban_kuicun(yesterdayLog.getWeiguiban_kuicun());

						todayDeliveryDTO.setZuori_toudi_daozhanweiling(yesterdayLog.getToudi_daozhanweiling());
					}
					branch = branchPram;
				}
			}

			branchnameList = branchDAO.getBranchEffectAllzhandian(BranchEnum.ZhanDian.getValue() + "");
			if (branch.getSitetype() != BranchEnum.ZhanDian.getValue()) {
				branchnameList.add(branch);
			}
		}
		// 查询今日的所有反馈

		// ===========今日到货日报==========
		String showWeidaohuo = systemInstallService.getParameter("showWeidaohuo");
		model.addAttribute("showWeidaohuo", showWeidaohuo);
		model.addAttribute("nowbranch", branch);
		model.addAttribute("branchidSession", branch.getBranchid());
		model.addAttribute("branchnameList", branchnameList);
		model.addAttribute("todayArrival", todayArrivalDTO);
		model.addAttribute("todayDelivery", todayDeliveryDTO);
		model.addAttribute("todayFunds", todayFundsDTO);
		model.addAttribute("todayStock", todayStockDTO);
		model.addAttribute("startTime", startTime);
		return "logtoday/nowlog/todayarrival";
	}

	@RequestMapping("/todayArrivalAll")
	public String todayArrivalAll(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid) {
		// 获取所有站点
		BranchTodayLog yesterdayLog = new BranchTodayLog();
		List<JSONObject> peisongchenggongList = new ArrayList<JSONObject>();
		TodayArrivalDTO todayArrivalDTO = new TodayArrivalDTO();
		TodayDeliveryDTO todayDeliveryDTO = new TodayDeliveryDTO();
		TodayFundsDTO todayFundsDTO = new TodayFundsDTO();
		TodayStockDTO todayStockDTO = new TodayStockDTO();

		List<Branch> branchnameList = new ArrayList<Branch>();
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			// 获取昨日的日志
			yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(
					branch.getBranchid(), new Date());
			if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
				startTime = yesterdayLog.getCreatedate();
			}/*
			 * else{ SystemInstall siteDayLogTime =
			 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
			 * if(siteDayLogTime
			 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
			 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
			 * Date())+" 00:00:00"; }else{ startTime = new
			 * SimpleDateFormat("yyyy-MM-dd"
			 * ).format(DateDayUtil.getTimeByDay(-1))+" "
			 * +siteDayLogTime.getValue(); } }
			 */
			String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			// 已到货（单）
			// 配送成功单数、应收金额、应退金额
			peisongchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(), DeliveryStateEnum.PeiSongChengGong.getValue(), startTime,
					endTime);
			Date startTimeToDate = new Date();
			try {
				startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			} catch (ParseException e) {

			}
			todayArrivalDTO = logToDayService.getTodayArrivalDTO(branch, startTimeToDate, new Date());
			// =======今日投递日报=============
			todayDeliveryDTO = logToDayService.getTodayDeliveryDTO(branch, peisongchenggongList, startTimeToDate, new Date());
			// ============今日款项日报=================
			todayFundsDTO = logToDayService.getTodayFundsDTO(branch, startTimeToDate, new Date());
			// ==============今日库存日报==========================
			todayStockDTO = logToDayService.getTodayStockDTO(branch, startTimeToDate, new Date());

			long tuotou = todayDeliveryDTO.getToday_fankui_peisongchenggong_count() + todayDeliveryDTO.getToday_fankui_shangmenhuanchenggong_count()
					+ todayDeliveryDTO.getToday_fankui_shangmentuichenggong_count();

			// 存储站点到站和漏扫到站
			todayDeliveryDTO.setToday_fankui_daohuo(todayArrivalDTO.getYidaohuo());
			todayDeliveryDTO.setJinri_lousaodaozhan(todayArrivalDTO.getJinri_lousaodaozhan());

			todayDeliveryDTO.setToday_fankui_daohuo(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());
			todayStockDTO.setJinridaohuo(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());

			todayStockDTO.setJinrituodou(tuotou);
			if (yesterdayLog != null) {
				todayDeliveryDTO.setToday_fankui_zhiliu(yesterdayLog.getZhiliu_kuicun());
				todayDeliveryDTO.setToday_fankui_zuoriweiguiban(yesterdayLog.getWeiguiban_kuicun());

				todayStockDTO.setZuori_jushou_kuicun(yesterdayLog.getJushou_kuicun());
				todayStockDTO.setZuori_zhiliu_kuicun(yesterdayLog.getZhiliu_kuicun());
				todayStockDTO.setZuori_weiguiban_kuicun(yesterdayLog.getWeiguiban_kuicun());
				todayDeliveryDTO.setZuori_toudi_daozhanweiling(yesterdayLog.getToudi_daozhanweiling());
			}

			branchnameList.add(branch);
		} else {
			if (branchid > 0) {
				Branch branchPram = branchDAO.getBranchByBranchid(branchid);
				if (branchPram.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
					// 获取昨日的日志
					yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
							new Date());
					if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
						startTime = yesterdayLog.getCreatedate();
					}/*
					 * else{ SystemInstall siteDayLogTime =
					 * systemInstallDAO.getSystemInstallByName
					 * ("siteDayLogTime");
					 * if(siteDayLogTime==null||!StringUtils.
					 * hasLength(siteDayLogTime.getValue())){ startTime= new
					 * SimpleDateFormat("yyyy-MM-dd").format(new
					 * Date())+" 00:00:00"; }else{ startTime = new
					 * SimpleDateFormat
					 * ("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1))+" "
					 * +siteDayLogTime.getValue(); } }
					 */
					String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());// 配送成功单数、应收金额、应退金额
					peisongchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branchid, DeliveryStateEnum.PeiSongChengGong.getValue(), startTime,
							endTime);
					Date startTimeToDate = new Date();
					try {
						startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
					} catch (ParseException e) {

					}
					todayArrivalDTO = logToDayService.getTodayArrivalDTO(branchPram, startTimeToDate, new Date());
					// =======今日投递日报=============
					todayDeliveryDTO = logToDayService.getTodayDeliveryDTO(branchPram, peisongchenggongList, startTimeToDate, new Date());
					// ============今日款项日报=================
					todayFundsDTO = logToDayService.getTodayFundsDTO(branchPram, startTimeToDate, new Date());
					// ==============今日库存日报==========================
					todayStockDTO = logToDayService.getTodayStockDTO(branchPram, startTimeToDate, new Date());
					long tuotou = todayDeliveryDTO.getToday_fankui_peisongchenggong_count() + todayDeliveryDTO.getToday_fankui_shangmenhuanchenggong_count()
							+ todayDeliveryDTO.getToday_fankui_shangmentuichenggong_count();

					todayStockDTO.setJinrituodou(tuotou);

					// 存储站点到站和漏扫到站
					todayDeliveryDTO.setToday_fankui_daohuo(todayArrivalDTO.getYidaohuo());
					todayDeliveryDTO.setJinri_lousaodaozhan(todayArrivalDTO.getJinri_lousaodaozhan());

					todayDeliveryDTO.setToday_fankui_daohuo(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());
					todayStockDTO.setJinridaohuo(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());

					if (yesterdayLog != null) {

						todayDeliveryDTO.setToday_fankui_zhiliu(yesterdayLog.getZhiliu_kuicun());
						todayDeliveryDTO.setToday_fankui_zuoriweiguiban(yesterdayLog.getWeiguiban_kuicun());

						todayStockDTO.setZuori_jushou_kuicun(yesterdayLog.getJushou_kuicun());
						todayStockDTO.setZuori_zhiliu_kuicun(yesterdayLog.getZhiliu_kuicun());
						todayStockDTO.setZuori_weiguiban_kuicun(yesterdayLog.getWeiguiban_kuicun());

						todayDeliveryDTO.setZuori_toudi_daozhanweiling(yesterdayLog.getToudi_daozhanweiling());
					}
					branch = branchPram;
				}
			}

			branchnameList = branchDAO.getBranchEffectAllzhandian(BranchEnum.ZhanDian.getValue() + "");
			if (branch.getSitetype() != BranchEnum.ZhanDian.getValue()) {
				branchnameList.add(branch);
			}
		}
		// 查询今日的所有反馈

		// ===========今日到货日报==========
		String showWeidaohuo = systemInstallService.getParameter("showWeidaohuo");
		model.addAttribute("showWeidaohuo", showWeidaohuo);
		model.addAttribute("nowbranch", branch);
		model.addAttribute("branchidSession", branch.getBranchid());
		model.addAttribute("branchnameList", branchnameList);
		model.addAttribute("todayArrival", todayArrivalDTO);
		model.addAttribute("todayDelivery", todayDeliveryDTO);
		model.addAttribute("todayFunds", todayFundsDTO);
		model.addAttribute("todayStock", todayStockDTO);

		return "logtoday/nowlog/todayarrivalAll";
	}

	/**
	 * 站点今日日志 显示 ajax
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/todayArrivalAjax")
	public String todayArrivalAjax(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid) {
		// 获取所有站点
		List<Branch> branchnameList = new ArrayList<Branch>();
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			// 获取昨日的日志
			BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date()) == null ? new BranchTodayLog() : logTodayDAO
					.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date());
			if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
				startTime = yesterdayLog.getCreatedate();
			}/*
			 * else{ SystemInstall siteDayLogTime =
			 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
			 * if(siteDayLogTime
			 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
			 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
			 * Date())+" 00:00:00"; }else{ startTime = new
			 * SimpleDateFormat("yyyy-MM-dd"
			 * ).format(DateDayUtil.getTimeByDay(-1))+" "
			 * +siteDayLogTime.getValue(); } }
			 */
			branchnameList.add(branch);
			model.addAttribute("sitetype", BranchEnum.ZhanDian.getValue() + "");
		} else {
			if (branchid > 0) {
				Branch branchPram = branchDAO.getBranchByBranchid(branchid);
				if (branchPram.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					// 获取昨日的日志
					BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(
							branchid, new Date());
					if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
						startTime = yesterdayLog.getCreatedate();
					}/*
					 * else{ SystemInstall siteDayLogTime =
					 * systemInstallDAO.getSystemInstallByName
					 * ("siteDayLogTime");
					 * if(siteDayLogTime==null||!StringUtils.
					 * hasLength(siteDayLogTime.getValue())){ startTime= new
					 * SimpleDateFormat("yyyy-MM-dd").format(new
					 * Date())+" 00:00:00"; }else{ startTime = new
					 * SimpleDateFormat
					 * ("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1))+" "
					 * +siteDayLogTime.getValue(); } }
					 */
					branch = branchPram;
					model.addAttribute("sitetype", BranchEnum.ZhanDian.getValue() + "");
				}
			}
			branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
			if (branch.getSitetype() != BranchEnum.ZhanDian.getValue()) {
				branchnameList.add(branch);
			}
		}
		// 查询今日的所有反馈
		// ===========今日到货日报==========
		String showWeidaohuo = systemInstallService.getParameter("showWeidaohuo");
		model.addAttribute("showWeidaohuo", showWeidaohuo);
		model.addAttribute("nowbranch", branch);
		model.addAttribute("branchidSession", branch.getBranchid());
		model.addAttribute("branchnameList", branchnameList);
		model.addAttribute("startTime", startTime);

		return "logtoday/nowlog/todayarrivalAjax";
	}

	/**
	 * 站点今日日志 显示 ajax
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/todayArrivalAllAjax")
	public String todayArrivalAllAjax(Model model, @RequestParam(value = "flag", required = false, defaultValue = "1") long flag,// 第几个页卡
			@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid) {
		// 获取所有站点
		List<Branch> branchnameList = new ArrayList<Branch>();
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			// 获取昨日的日志
			BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date()) == null ? new BranchTodayLog() : logTodayDAO
					.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date());
			if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
				startTime = yesterdayLog.getCreatedate();
			}/*
			 * else{ SystemInstall siteDayLogTime =
			 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
			 * if(siteDayLogTime
			 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
			 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
			 * Date())+" 00:00:00"; }else{ startTime = new
			 * SimpleDateFormat("yyyy-MM-dd"
			 * ).format(DateDayUtil.getTimeByDay(-1))+" "
			 * +siteDayLogTime.getValue(); } }
			 */
			branchnameList.add(branch);
			model.addAttribute("sitetype", BranchEnum.ZhanDian.getValue() + "");
		} else {
			if (branchid > 0) {
				Branch branchPram = branchDAO.getBranchByBranchid(branchid);
				if (branchPram.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					// 获取昨日的日志
					BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(
							branchid, new Date());
					if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
						startTime = yesterdayLog.getCreatedate();
					}/*
					 * else{ SystemInstall siteDayLogTime =
					 * systemInstallDAO.getSystemInstallByName
					 * ("siteDayLogTime");
					 * if(siteDayLogTime==null||!StringUtils.
					 * hasLength(siteDayLogTime.getValue())){ startTime= new
					 * SimpleDateFormat("yyyy-MM-dd").format(new
					 * Date())+" 00:00:00"; }else{ startTime = new
					 * SimpleDateFormat
					 * ("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1))+" "
					 * +siteDayLogTime.getValue(); } }
					 */
					branch = branchPram;
					model.addAttribute("sitetype", BranchEnum.ZhanDian.getValue() + "");
				}
			}
			branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
			if (branch.getSitetype() != BranchEnum.ZhanDian.getValue()) {
				branchnameList.add(branch);
			}
		}
		// 查询今日的所有反馈
		// ===========今日到货日报==========
		model.addAttribute("nowbranch", branch);
		model.addAttribute("branchidSession", branch.getBranchid());
		model.addAttribute("branchnameList", branchnameList);
		model.addAttribute("startTime", startTime);
		return "logtoday/nowlog/todayarrivalAllAjax";
	}

	/**
	 * 异步调用到货日报的统计
	 * 
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/getTodayArrivalDTO")
	public @ResponseBody JSONObject getTodayArrivalDTO(@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "startTime", required = false, defaultValue = "") String startTime) {
		JSONObject obj = new JSONObject();
		// 获取所有站点
		TodayArrivalDTO todayArrivalDTO = new TodayArrivalDTO();
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {

			Date startTimeToDate = new Date();
			try {
				startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			} catch (ParseException e) {

			}
			todayArrivalDTO = logToDayService.getTodayArrivalDTO(branch, startTimeToDate, new Date());

		} else {
			if (branchid > 0) {
				Branch branchPram = branchDAO.getBranchByBranchid(branchid);
				if (branchPram.getSitetype() == BranchEnum.ZhanDian.getValue()) {

					Date startTimeToDate = new Date();
					try {
						startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
					} catch (ParseException e) {

					}
					todayArrivalDTO = logToDayService.getTodayArrivalDTO(branchPram, startTimeToDate, new Date());
				}
			}
		}
		obj = JSONObject.fromObject(todayArrivalDTO);
		return obj;
	}

	/**
	 * 异步调用投递日报的统计
	 * 
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/getTodayDeliveryDTO")
	public @ResponseBody JSONObject getTodayDeliveryDTO(@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid) {
		JSONObject obj = new JSONObject();
		// 获取所有站点
		TodayDeliveryDTO todayDeliveryDTO = new TodayDeliveryDTO();
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());

		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			// 获取昨日的日志
			BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date()) == null ? new BranchTodayLog() : logTodayDAO
					.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date());
			if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
				startTime = yesterdayLog.getCreatedate();
			}/*
			 * else{ SystemInstall siteDayLogTime =
			 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
			 * if(siteDayLogTime
			 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
			 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
			 * Date())+" 00:00:00"; }else{ startTime = new
			 * SimpleDateFormat("yyyy-MM-dd"
			 * ).format(DateDayUtil.getTimeByDay(-1))+" "
			 * +siteDayLogTime.getValue(); } }
			 */
			String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			Date startTimeToDate = new Date();
			try {
				startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			} catch (ParseException e) {

			}
			List<JSONObject> peisongchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(),
					DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime);
			todayDeliveryDTO = logToDayService.getTodayDeliveryDTO(branch, peisongchenggongList, startTimeToDate, new Date());
			long zongdaozhan = logTodayDAO.getTodaybyBranchid(branch.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), startTime, endTime);
			todayDeliveryDTO.setToday_fankui_daohuo(zongdaozhan);
			BigDecimal b1 = new BigDecimal(
					(todayDeliveryDTO.getToday_fankui_peisongchenggong_count() + todayDeliveryDTO.getToday_fankui_shangmentuichenggong_count() + todayDeliveryDTO
							.getToday_fankui_shangmenhuanchenggong_count()) * 100);
			yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
					new Date());
			BigDecimal b2 = new BigDecimal(todayDeliveryDTO.getToday_fankui_daohuo()
					+ (yesterdayLog == null ? 0 : yesterdayLog.getZhiliu_kuicun() + yesterdayLog.getToudi_daozhanweiling() + yesterdayLog.getWeiguiban_kuicun()));
			BigDecimal toudilv = b2.intValue() == 0 ? new BigDecimal("0") : b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
			todayDeliveryDTO.setTuotoulv(toudilv);
			if (yesterdayLog != null) {
				todayDeliveryDTO.setZuori_zhiliu_kuicun(yesterdayLog.getZhiliu_kuicun());
				todayDeliveryDTO.setZuori_weiguiban_kuicun(yesterdayLog.getWeiguiban_kuicun());
				todayDeliveryDTO.setToday_fankui_zhiliu(yesterdayLog.getZhiliu_kuicun());
				todayDeliveryDTO.setToday_fankui_zuoriweiguiban(yesterdayLog.getWeiguiban_kuicun());
				todayDeliveryDTO.setZuori_toudi_daozhanweiling(yesterdayLog.getToudi_daozhanweiling());
			}
		} else {
			if (branchid > 0) {
				Branch branchPram = branchDAO.getBranchByBranchid(branchid);

				if (branchPram.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
					// 获取昨日的日志
					BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(
							branchid, new Date());
					if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
						startTime = yesterdayLog.getCreatedate();
					}/*
					 * else{ SystemInstall siteDayLogTime =
					 * systemInstallDAO.getSystemInstallByName
					 * ("siteDayLogTime");
					 * if(siteDayLogTime==null||!StringUtils.
					 * hasLength(siteDayLogTime.getValue())){ startTime= new
					 * SimpleDateFormat("yyyy-MM-dd").format(new
					 * Date())+" 00:00:00"; }else{ startTime = new
					 * SimpleDateFormat
					 * ("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1))+" "
					 * +siteDayLogTime.getValue(); } }
					 */
					String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					Date startTimeToDate = new Date();
					try {
						startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
					} catch (ParseException e) {

					}
					List<JSONObject> peisongchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branchPram.getBranchid(),
							DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime);
					todayDeliveryDTO = logToDayService.getTodayDeliveryDTO(branchPram, peisongchenggongList, startTimeToDate, new Date());
					long zongdaozhan = logTodayDAO.getTodaybyBranchid(branchPram.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), startTime, endTime);
					todayDeliveryDTO.setToday_fankui_daohuo(zongdaozhan);
					BigDecimal b1 = new BigDecimal(
							(todayDeliveryDTO.getToday_fankui_peisongchenggong_count() + todayDeliveryDTO.getToday_fankui_shangmentuichenggong_count() + todayDeliveryDTO
									.getToday_fankui_shangmenhuanchenggong_count()) * 100);
					yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
							new Date());
					BigDecimal b2 = new BigDecimal(todayDeliveryDTO.getToday_fankui_daohuo()
							+ (yesterdayLog == null ? 0 : yesterdayLog.getZhiliu_kuicun() + yesterdayLog.getToudi_daozhanweiling() + yesterdayLog.getWeiguiban_kuicun()));
					BigDecimal toudilv = b2.intValue() == 0 ? new BigDecimal("0") : b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
					todayDeliveryDTO.setTuotoulv(toudilv);
					if (yesterdayLog != null) {
						todayDeliveryDTO.setZuori_zhiliu_kuicun(yesterdayLog.getZhiliu_kuicun());
						todayDeliveryDTO.setZuori_weiguiban_kuicun(yesterdayLog.getWeiguiban_kuicun());
						todayDeliveryDTO.setToday_fankui_zhiliu(yesterdayLog.getZhiliu_kuicun());
						todayDeliveryDTO.setToday_fankui_zuoriweiguiban(yesterdayLog.getWeiguiban_kuicun());
						todayDeliveryDTO.setZuori_toudi_daozhanweiling(yesterdayLog.getToudi_daozhanweiling());
					}
				}
			}
		}
		obj = JSONObject.fromObject(todayDeliveryDTO);
		return obj;
	}

	/**
	 * 异步调用款项日报的统计
	 * 
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/getTodayFundsDTO")
	public @ResponseBody JSONObject getTodayFundsDTO(@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "startTime", required = false, defaultValue = "") String startTime) {
		JSONObject obj = new JSONObject();
		// 获取所有站点
		TodayFundsDTO todayFundsDTO = new TodayFundsDTO();
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			Date startTimeToDate = new Date();
			try {
				startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			} catch (ParseException e) {

			}
			todayFundsDTO = logToDayService.getTodayFundsDTO(branch, startTimeToDate, new Date());
		} else {
			if (branchid > 0) {
				Branch branchPram = branchDAO.getBranchByBranchid(branchid);
				if (branchPram.getSitetype() == BranchEnum.ZhanDian.getValue()) {

					Date startTimeToDate = new Date();
					try {
						startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
					} catch (ParseException e) {

					}
					todayFundsDTO = logToDayService.getTodayFundsDTO(branchPram, startTimeToDate, new Date());
				}
			}
		}
		obj = JSONObject.fromObject(todayFundsDTO);
		return obj;
	}

	/**
	 * 异步调用库存日报的统计
	 * 
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/getTodayStockDTO")
	public @ResponseBody JSONObject getTodayStockDTO(@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid) {
		JSONObject obj = new JSONObject();
		// 获取所有站点
		TodayStockDTO todayStockDTO = new TodayStockDTO();
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			// 获取昨日的日志
			BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date()) == null ? new BranchTodayLog() : logTodayDAO
					.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date());
			if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
				startTime = yesterdayLog.getCreatedate();
			}/*
			 * else{ SystemInstall siteDayLogTime =
			 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
			 * if(siteDayLogTime
			 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
			 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
			 * Date())+" 00:00:00"; }else{ startTime = new
			 * SimpleDateFormat("yyyy-MM-dd"
			 * ).format(DateDayUtil.getTimeByDay(-1))+" "
			 * +siteDayLogTime.getValue(); } }
			 */
			String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			Date startTimeToDate = new Date();
			try {
				startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			} catch (ParseException e) {

			}
			todayStockDTO = logToDayService.getTodayStockDTO(branch, startTimeToDate, new Date());
			List<JSONObject> peisongchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(),
					DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime);
			long peisongchenggong = (peisongchenggongList == null || peisongchenggongList.size() == 0) ? 0 : peisongchenggongList.get(0).getLong("num");
			// 上门退成功单数、应退金额
			List<JSONObject> shangMenTuiChengGongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(),
					DeliveryStateEnum.ShangMenTuiChengGong.getValue(), startTime, endTime);
			long shangMenTuiChengGong = (shangMenTuiChengGongList == null || shangMenTuiChengGongList.size() == 0) ? 0 : shangMenTuiChengGongList.get(0).getLong("num");
			List<JSONObject> shangmenhuanchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(),
					DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startTime, endTime);
			long shangmenhuanchenggong = (shangmenhuanchenggongList == null || shangmenhuanchenggongList.size() == 0) ? 0 : shangmenhuanchenggongList.get(0).getLong("num");
			long tuotou = peisongchenggong + shangMenTuiChengGong + shangmenhuanchenggong;
			todayStockDTO.setJinrituodou(tuotou);
			yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branch.getBranchid(), new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
					new Date());
			if (yesterdayLog != null) {
				todayStockDTO.setZuori_jushou_kuicun(yesterdayLog.getJushou_kuicun());
				todayStockDTO.setZuori_zhiliu_kuicun(yesterdayLog.getZhiliu_kuicun());
				todayStockDTO.setZuori_weiguiban_kuicun(yesterdayLog.getWeiguiban_kuicun());
				todayStockDTO.setZuori_toudi_daozhanweiling(yesterdayLog.getToudi_daozhanweiling());// 昨日到站未领
			}
			todayStockDTO.setToudi_daozhanweiling(logTodayDAO.getCountbyDedailt(branch.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
					FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()));// 今日到站未领
			todayStockDTO.setToday_fankui_diushi(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branch.getBranchid(), DeliveryStateEnum.HuoWuDiuShi.getValue(), startTime,
					endTime));// 货物丢失

		} else {
			if (branchid > 0) {
				Branch branchPram = branchDAO.getBranchByBranchid(branchid);
				if (branchPram.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
					// 获取昨日的日志
					BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(
							branchid, new Date());
					if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
						startTime = yesterdayLog.getCreatedate();
					}/*
					 * else{ SystemInstall siteDayLogTime =
					 * systemInstallDAO.getSystemInstallByName
					 * ("siteDayLogTime");
					 * if(siteDayLogTime==null||!StringUtils.
					 * hasLength(siteDayLogTime.getValue())){ startTime= new
					 * SimpleDateFormat("yyyy-MM-dd").format(new
					 * Date())+" 00:00:00"; }else{ startTime = new
					 * SimpleDateFormat
					 * ("yyyy-MM-dd").format(DateDayUtil.getTimeByDay(-1))+" "
					 * +siteDayLogTime.getValue(); } }
					 */
					String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					Date startTimeToDate = new Date();
					try {
						startTimeToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
					} catch (ParseException e) {

					}
					todayStockDTO = logToDayService.getTodayStockDTO(branchPram, startTimeToDate, new Date());
					List<JSONObject> peisongchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branchid,
							DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime);
					long peisongchenggong = (peisongchenggongList == null || peisongchenggongList.size() == 0) ? 0 : peisongchenggongList.get(0).getLong("num");
					// 上门退成功单数、应退金额
					List<JSONObject> shangMenTuiChengGongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branchid,
							DeliveryStateEnum.ShangMenTuiChengGong.getValue(), startTime, endTime);
					long shangMenTuiChengGong = (shangMenTuiChengGongList == null || shangMenTuiChengGongList.size() == 0) ? 0 : shangMenTuiChengGongList.get(0).getLong("num");
					List<JSONObject> shangmenhuanchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branchid,
							DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startTime, endTime);
					long shangmenhuanchenggong = (shangmenhuanchenggongList == null || shangmenhuanchenggongList.size() == 0) ? 0 : shangmenhuanchenggongList.get(0).getLong("num");
					long tuotou = peisongchenggong + shangMenTuiChengGong + shangmenhuanchenggong;
					todayStockDTO.setJinrituodou(tuotou);
					yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
							new Date());
					if (yesterdayLog != null) {
						todayStockDTO.setZuori_jushou_kuicun(yesterdayLog.getJushou_kuicun());
						todayStockDTO.setZuori_zhiliu_kuicun(yesterdayLog.getZhiliu_kuicun());
						todayStockDTO.setZuori_weiguiban_kuicun(yesterdayLog.getWeiguiban_kuicun());
						todayStockDTO.setZuori_toudi_daozhanweiling(yesterdayLog.getToudi_daozhanweiling());// 昨日到站未领
					}
					todayStockDTO.setToudi_daozhanweiling(logTodayDAO.getCountbyDedailt(branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()
							+ "," + FlowOrderTypeEnum.BeiZhu.getValue()));// 今日到站未领
					todayStockDTO.setToday_fankui_diushi(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.HuoWuDiuShi.getValue(), startTime, endTime));// 今日到站未领
				}
			}
		}
		obj = JSONObject.fromObject(todayStockDTO);
		return obj;
	}

	// ================站点日报 明细begin=============
	@RequestMapping("/show/{branchid}/{type}/{page}")
	public String show(Model model, @PathVariable("branchid") long branchid, @PathVariable("type") String type, @PathVariable("page") long page) {
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		// 获取昨日的日志
		BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
				new Date());
		if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
			startTime = yesterdayLog.getCreatedate();
		}/*
		 * else{ SystemInstall siteDayLogTime =
		 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
		 * if(siteDayLogTime
		 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
		 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
		 * Date())+" 00:00:00"; }else{ startTime = new
		 * SimpleDateFormat("yyyy-MM-dd"
		 * ).format(DateDayUtil.getTimeByDay(-1))+" "
		 * +siteDayLogTime.getValue(); } }
		 */
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<CwbOrder> cwborderList = logToDayService.getOrderListByBranchidAndType(branchid, type, page, startTime, endTime);
		long cwborderCount = logToDayService.getOrderListByBranchidAndTypeCount(branchid, type, startTime, endTime);
		model.addAttribute("cwborderList", cwborderList);
		model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
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
		String typeStr = getTypeStr(type);
		model.addAttribute("typeStr", typeStr);
		model.addAttribute("type", type);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		return "logtoday/nowlog/show";
	}

	// ================站点日报 明细begin=============
	@RequestMapping("/showAll/{branchid}/{type}/{page}")
	public String showAll(Model model, @PathVariable("branchid") long branchid, @PathVariable("type") String type, @PathVariable("page") long page) {
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		// 获取昨日的日志
		BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
				new Date());
		if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
			startTime = yesterdayLog.getCreatedate();
		}/*
		 * else{ SystemInstall siteDayLogTime =
		 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
		 * if(siteDayLogTime
		 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
		 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
		 * Date())+" 00:00:00"; }else{ startTime = new
		 * SimpleDateFormat("yyyy-MM-dd"
		 * ).format(DateDayUtil.getTimeByDay(-1))+" "
		 * +siteDayLogTime.getValue(); } }
		 */
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<CwbOrder> cwborderList = logToDayService.getOrderListByBranchidAndType(branchid, type, page, startTime, endTime);
		long cwborderCount = logToDayService.getOrderListByBranchidAndTypeCount(branchid, type, startTime, endTime);
		model.addAttribute("cwborderList", cwborderList);
		model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
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
		String typeStr = getTypeStr(type);
		model.addAttribute("typeStr", typeStr);
		model.addAttribute("type", type);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		return "logtoday/nowlog/showAll";
	}

	@RequestMapping("/showAjax/{branchid}/{type}/{page}")
	public String showAjax(Model model, @PathVariable("branchid") long branchid, @PathVariable("type") String type, @PathVariable("page") long page) {
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		// 获取昨日的日志
		BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
				new Date());
		if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
			startTime = yesterdayLog.getCreatedate();
		}/*
		 * else{ SystemInstall siteDayLogTime =
		 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
		 * if(siteDayLogTime
		 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
		 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
		 * Date())+" 00:00:00"; }else{ startTime = new
		 * SimpleDateFormat("yyyy-MM-dd"
		 * ).format(DateDayUtil.getTimeByDay(-1))+" "
		 * +siteDayLogTime.getValue(); } }
		 */
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<CwbOrder> cwborderList = logToDayService.getOrderListByBranchidAndType(branchid, type, page, startTime, endTime);
		long cwborderCount = logToDayService.getOrderListByBranchidAndTypeCount(branchid, type, startTime, endTime);
		model.addAttribute("cwborderList", cwborderList);
		model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
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
		String typeStr = getTypeStr(type);
		model.addAttribute("typeStr", typeStr);
		model.addAttribute("type", type);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		return "logtoday/nowlog/showAjax";
	}

	@RequestMapping("/showAllAjax/{branchid}/{type}/{page}")
	public String showAllAjax(Model model, @PathVariable("branchid") long branchid, @PathVariable("type") String type, @PathVariable("page") long page,
			@RequestParam(value = "flag", required = false, defaultValue = "1") long flag// 第几个页卡
	) {
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		// 获取昨日的日志
		BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
				new Date());
		if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
			startTime = yesterdayLog.getCreatedate();
		}/*
		 * else{ SystemInstall siteDayLogTime =
		 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
		 * if(siteDayLogTime
		 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
		 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
		 * Date())+" 00:00:00"; }else{ startTime = new
		 * SimpleDateFormat("yyyy-MM-dd"
		 * ).format(DateDayUtil.getTimeByDay(-1))+" "
		 * +siteDayLogTime.getValue(); } }
		 */
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<CwbOrder> cwborderList = logToDayService.getOrderListByBranchidAndType(branchid, type, page, startTime, endTime);
		long cwborderCount = logToDayService.getOrderListByBranchidAndTypeCount(branchid, type, startTime, endTime);
		model.addAttribute("cwborderList", cwborderList);
		model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
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
		String typeStr = getTypeStr(type);
		model.addAttribute("typeStr", typeStr);
		model.addAttribute("type", type);
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		return "logtoday/nowlog/showAllAjax";
	}

	// ================站点日报 明细end=============

	@RequestMapping("/show/{id}")
	public String showHistorey(Model model, @PathVariable("id") long id) {
		// 获取昨日的日志
		BranchTodayLog branchLog = logTodayDAO.getBranchTodayLogById(id);
		Branch branch = branchDAO.getBranchByBranchid(branchLog.getBranchid());
		model.addAttribute("nowbranch", branch);
		model.addAttribute("branchLog", branchLog);
		return "logtoday/allhistorylog/show";
	}

	/**
	 * 今日站点日志第一个书签
	 * 
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/todayArrivalLog/{page}")
	public String todayArrivalLog(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-2") long branchid, HttpServletRequest request) {
		setModel(model, branchid, page, request);
		return "logtoday/alllogmanage/allarrivallogmanage";
	}

	/**
	 * 今日站点日志第二个书签
	 * 
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/alldeliverlogmanage/{page}")
	public String alldeliverlogmanage(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-2") long branchid, HttpServletRequest request) {
		setModel(model, branchid, page, request);
		return "logtoday/alllogmanage/alldeliverlogmanage";
	}

	/**
	 * 今日站点日志第三个书签
	 * 
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/allslowlogmanage/{page}")
	public String allslowlogmanage(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-2") long branchid, HttpServletRequest request) {
		setModel(model, branchid, page, request);
		return "logtoday/alllogmanage/allslowlogmanage";
	}

	/**
	 * 今日站点日志第四个书签
	 * 
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/allstocklogmanage/{page}")
	public String allstocklogmanage(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "-2") long branchid, HttpServletRequest request) {
		setModel(model, branchid, page, request);
		return "logtoday/alllogmanage/allstocklogmanage";
	}

	/**
	 * 站点历史日志
	 * 
	 * @param model
	 * @param page
	 * @param createdate
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/allhistorylog/{page}")
	public String allhistorylog(Model model, @PathVariable("page") long page, @RequestParam(value = "createdate", required = false, defaultValue = "") String createdate,
			@RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid) {

		setHejiModel(model, branchid, createdate, page);
		return "logtoday/allhistorylog/allhistorylog";
	}

	private void setModel(Model model, long branchid, long page, HttpServletRequest request) {
		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		Map branchMap = new HashMap();
		if (branchid >= -1) {
			request.getSession().setAttribute("branchidPublicSession", branchid);
		}
		if (request.getSession().getAttribute("branchidPublicSession") != null) {
			branchid = Long.valueOf(request.getSession().getAttribute("branchidPublicSession").toString());
		} else {
			request.getSession().setAttribute("branchidPublicSession", branchid);
		}
		if (branchid < 0) {
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {// 站点角色
				List<BranchTodayLog> todayLogList = logTodayDAO.getBranchTodayLogByBranchidAndDateList(branch.getBranchid(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00",
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59");
				model.addAttribute("todayLogList", todayLogList);
				model.addAttribute("page_obj", new Page(1, page, Page.ONE_PAGE_NUMBER));
				model.addAttribute("page", 1);
				branchMap.put(branch.getBranchid(), branch.getBranchname());
				model.addAttribute("branchMap", branchMap);
				branchid = getSessionUser().getBranchid();
			} else {// 获取所有的
				List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
				String branchids = "-1";
				if (branchnameList != null && branchnameList.size() > 0) {
					for (Branch branch2 : branchnameList) {
						branchids += "," + branch2.getBranchid() + "";
					}
				}
				List<BranchTodayLog> todayLogList = logTodayDAO.getBranchTodayLogByDateList(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00",
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59", page, Page.ONE_PAGE_NUMBER, branchids);
				model.addAttribute("todayLogList", todayLogList);
				if (todayLogList != null && todayLogList.size() > 0) {
					for (BranchTodayLog branchTodayLog : todayLogList) {
						branchMap.put(branchTodayLog.getBranchid(), branchDAO.getBranchByBranchid(branchTodayLog.getBranchid()).getBranchname());
					}
				}
				model.addAttribute(
						"page_obj",
						new Page(logTodayDAO.getBranchTodayLogByDateCount(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00", new SimpleDateFormat("yyyy-MM-dd").format(new Date())
								+ " 23:59:59", page), page, Page.ONE_PAGE_NUMBER));
				model.addAttribute("page", page);
				model.addAttribute("branchMap", branchMap);
			}
		} else {
			List<BranchTodayLog> todayLogList = logTodayDAO.getBranchTodayLogByBranchidAndDateList(branchid, new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00", new SimpleDateFormat(
					"yyyy-MM-dd").format(new Date()) + " 23:59:59");
			model.addAttribute("todayLogList", todayLogList);
			model.addAttribute("page_obj", new Page(1, page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", 1);
			Branch branchPram = branchDAO.getBranchByBranchid(branchid);
			branchMap.put(branchid, branchPram.getBranchname());
			model.addAttribute("branchMap", branchMap);
		}

		request.getSession().setAttribute("branchidPublicSession", branchid);
		model.addAttribute("branchidSession", branchid);
		// 查询所有的站点
		List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
		model.addAttribute("branchnameList", branchnameList);
		if (branch != null) {
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				model.addAttribute("nowBranch", branch);
			}
		}

	}

	private void setHejiModel(Model model, long branchid, String createdate, long page) {
		Branch nowbranch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		Map branchMap = new HashMap();
		Map<Long, BranchTodayLog> branchAndTomorrow = new HashMap<Long, BranchTodayLog>();
		// 时间处理
		String dateFirStr = "".equals(createdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : createdate + " 00:00:00";
		String dateFirEnd = "".equals(createdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : createdate + " 23:59:59";
		String dateSecStr;
		String dateSecEnd;
		try {
			dateSecStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateFirStr).getTime() + 24 * 60 * 60 * 1000)).toString();
			dateSecEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateFirEnd).getTime() + 24 * 60 * 60 * 1000)).toString();
		} catch (ParseException e) {
			// 错误 默认查系统日期 明天的
			dateSecStr = DateTimeUtil.getDateAfter(new Date().getTime(), 1);
			dateSecEnd = DateTimeUtil.getDateAfter(new Date().getTime(), 1);
		}

		if (branchid < 0) {
			if (nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {// 站点角色
				List<BranchTodayLog> todayLogList = logTodayDAO.getBranchTodayLogByBranchidAndDateList(nowbranch.getBranchid(), dateFirStr, dateFirEnd);
				List<BranchTodayLog> tomorrowLogList = logTodayDAO.getBranchTodayLogByBranchidAndDateList(branchid, dateSecStr, dateSecEnd);
				if (tomorrowLogList != null && tomorrowLogList.size() > 0) {
					for (BranchTodayLog tomorrowlog : tomorrowLogList) {
						branchAndTomorrow.put(tomorrowlog.getBranchid(), tomorrowlog);
					}
				}
				model.addAttribute("branchAndTomorrow", branchAndTomorrow);
				model.addAttribute("todayLogList", todayLogList);
				model.addAttribute("page_obj", new Page(1, page, Page.ONE_PAGE_NUMBER / 2));
				model.addAttribute("page", 1);
				branchMap.put(nowbranch.getBranchid(), nowbranch.getBranchname());
				model.addAttribute("branchMap", branchMap);
			} else {// 获取所有的
				List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
				String branchids = "-1";
				if (branchnameList != null && branchnameList.size() > 0) {
					for (Branch branch2 : branchnameList) {
						branchids += "," + branch2.getBranchid() + "";
					}
				}
				List<BranchTodayLog> todayLogList = logTodayDAO.getBranchTodayLogByDateList(dateFirStr, dateFirEnd, page, Page.ONE_PAGE_NUMBER / 2, branchids);
				model.addAttribute("todayLogList", todayLogList);
				// 获取次日应投
				List<BranchTodayLog> tomorrowLogList = logTodayDAO.getBranchTodayLogByDateList(dateSecStr, dateSecEnd, page, Page.ONE_PAGE_NUMBER / 2, branchids);
				if (tomorrowLogList != null && tomorrowLogList.size() > 0) {
					for (BranchTodayLog tomorrowlog : tomorrowLogList) {
						branchAndTomorrow.put(tomorrowlog.getBranchid(), tomorrowlog);
					}
				}

				if (todayLogList != null && todayLogList.size() > 0) {
					for (BranchTodayLog branchTodayLog : todayLogList) {
						branchMap.put(branchTodayLog.getBranchid(), branchDAO.getBranchByBranchid(branchTodayLog.getBranchid()).getBranchname());
					}
				}
				model.addAttribute("page_obj", new Page(logTodayDAO.getBranchTodayLogByDateCount(dateFirStr, dateFirEnd, page), page, Page.ONE_PAGE_NUMBER / 2));
				model.addAttribute("page", page);
				model.addAttribute("branchMap", branchMap);
				model.addAttribute("branchAndTomorrow", branchAndTomorrow);
			}
		} else {
			List<BranchTodayLog> todayLogList = logTodayDAO.getBranchTodayLogByBranchidAndDateList(branchid, dateFirStr, dateFirEnd);
			List<BranchTodayLog> tomorrowLogList = logTodayDAO.getBranchTodayLogByBranchidAndDateList(branchid, dateSecStr, dateSecEnd);
			if (tomorrowLogList != null && tomorrowLogList.size() > 0) {
				for (BranchTodayLog tomorrowlog : tomorrowLogList) {
					branchAndTomorrow.put(tomorrowlog.getBranchid(), tomorrowlog);
				}
			}
			model.addAttribute("branchAndTomorrow", branchAndTomorrow);
			model.addAttribute("todayLogList", todayLogList);
			model.addAttribute("page_obj", new Page(1, page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", 1);
			Branch branchPram = branchDAO.getBranchByBranchid(branchid);
			branchMap.put(branchPram.getBranchid(), branchPram.getBranchname());
			model.addAttribute("branchMap", branchMap);
		}
		model.addAttribute("createdate", createdate);
		model.addAttribute("branchidSession", branchid);
		// 查询所有的站点
		List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
		model.addAttribute("branchnameList", branchnameList);
		if (nowbranch != null) {
			if (nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				model.addAttribute("nowBranch", nowbranch);
			}
		}
	}

	// -------------------------站点日报（本站）----------------------

	private String getTypeStr(String type) {
		String str = "当前查看的是：";
		if ("daohuo_jinriyichuku".equals(type)) {
			return str + "今日库房已出库给本站的订单数";
		}
		if ("daohuo_weidaohuo".equals(type)) {

			return str + "今日站点未到货，即当前状态还是库房出库扫描";
		}
		if ("daohuo_yidaohuo".equals(type)) {
			return str + "库房做了出库扫描，站点已做了到站扫描";
		}
		if ("daohuo_tazhandaohuo".equals(type)) {

			return str + "今日应到本站的货在他站操作到站扫描";
		}
		if ("daohuo_shaohuo".equals(type)) {

			return str + "今日站点少货，此项统计在系统截止提交日志前不会统计，提交后会在第二天日志的昨日少货体现";
		}
		if ("daohuo_daocuohuo".equals(type)) {

			return str + "今日站点扫到别的站的货";
		}
		if ("toudi_daohuo".equals(type)) {

			return str + "今日站点已做了到站扫描";
		}
		if ("toudi_zuorizhiliu".equals(type)) {

			return str + "昨日滞留的订单，即系统提交日志后，统计出昨日所有滞留的订单";
		}
		if ("toudi_linghuo".equals(type)) {
			return str + "今日做领货操作的订单";
		}
		if ("toudi_zuoriweiguiban".equals(type)) {

			return str + "昨日未归班的订单";
		}

		if ("toudi_peisongcheng".equals(type)) {
			return str + "今日审核为配送成功的订单";
		}
		if ("toudi_jushou".equals(type)) {

			return str + "今日审核为拒收的订单";
		}
		if ("toudi_jinrizhiliu".equals(type)) {

			return str + "今日所有审核为滞留，而且未做退货出站、中转出站的订单";
		}
		if ("toudi_bufenjushou".equals(type)) {

			return str + "今日所有审核为部分拒收";
		}
		if ("toudi_zhongzhuan".equals(type)) {

			return str + "今日站点做过中转出站扫描的订单";
		}
		if ("toudi_shangmentuichenggong".equals(type)) {

			return str + "今日所有审核为上门退成功的订单";
		}

		if ("toudi_shangmentuijutui".equals(type)) {

			return str + "今日所有审核为上门退拒退的订单";
		}
		if ("toudi_shangmenhuanchenggong".equals(type)) {
			return str + "今日所有审核为上门换成功的订单";
		}

		if ("toudi_shangmenhuanjuhuan".equals(type)) {

			return str + "今日所有审核为上门换拒换的订单";
		}
		if ("toudi_diushi".equals(type)) {

			return str + "今日所有审核为货物丢失的订单";
		}
		if ("toudi_fankuiheji".equals(type)) {

			return str + "今日所有归班合计的订单，即所有做了审核的订单";
		}
		if ("toudi_weifankuiheji".equals(type)) {

			return str + "今日所有未反馈的订单";
		}
		if ("kuanxiang_peisongchenggong".equals(type)) {

			return str + "今日审核为配送成功的订单";
		}
		if ("kuanxiang_shangmentui".equals(type)) {

			return str + "今日审核为上门退成功的订单";
		}
		if ("kuanxiang_shangmenhuan".equals(type)) {

			return str + "今日审核为上门换成功的订单";
		}
		if ("kuanxiang_shijiaokuan".equals(type)) {

			return str + "今日已向财务上交款的订单";
		}
		if ("kucun_zuorikucun".equals(type)) {

			return str + "昨日库存";
		}
		if ("kucun_daohuo".equals(type)) {
			return str + "今日做过到站扫描的订单";
		}
		if ("kucun_tuotou".equals(type)) {

			return str + "今日审核为配送成功、上门换成功、上门退成功的总和";
		}
		if ("kucun_tuihuochuku".equals(type)) {

			return str + "今日站点做退货出库的订单";
		}
		if ("kucun_zhongzhuanchuku".equals(type)) {

			return str + "今日站点做中转出库的订单";
		}
		if ("kucun_jinrikuncun".equals(type)) {

			return str + "今日站点的所有库存";
		}

		if ("jushou_kuicun".equals(type)) {

			return str + "站点的所有当前状态未已审核的拒收订单";
		}

		if ("zhiliu_kuicun".equals(type)) {

			return str + "站点的所有当前状态未已审核的滞留订单";
		}

		if ("weiguiban_kuicun".equals(type)) {

			return str + "小件员领货后所有未归班的订单";
		}
		if ("toudi_daozhanweiling".equals(type)) {

			return str + "今日到站的货物，小件员未领货的订单";
		}
		if ("daohuo_lousaoyidaohuo".equals(type)) {

			return str + "库房未做出库，站点做到货的订单";
		}
		if ("zuori_fankui_leijizhiliu".equals(type)) {

			return str + "昨日滞留的订单，今日还是滞留状态的";
		}

		return "";
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletResponse response, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "type", required = false, defaultValue = "") String type, @RequestParam(value = "exportmould", required = false, defaultValue = "") String exportmould

	) {
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		// 获取昨日的日志
		BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date()) == null ? new BranchTodayLog() : logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid,
				new Date());
		if (yesterdayLog != null && yesterdayLog.getCreatedate() != null && !"".equals(yesterdayLog.getCreatedate())) {
			startTime = yesterdayLog.getCreatedate();
		}/*
		 * else{ SystemInstall siteDayLogTime =
		 * systemInstallDAO.getSystemInstallByName("siteDayLogTime");
		 * if(siteDayLogTime
		 * ==null||!StringUtils.hasLength(siteDayLogTime.getValue())){
		 * startTime= new SimpleDateFormat("yyyy-MM-dd").format(new
		 * Date())+" 00:00:00"; }else{ startTime = new
		 * SimpleDateFormat("yyyy-MM-dd"
		 * ).format(DateDayUtil.getTimeByDay(-1))+" "
		 * +siteDayLogTime.getValue(); } }
		 */
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		logToDayExportService.excelPublicByZhandianMethod(response, branchid, type, exportmould, startTime, endTime);

	}

	@RequestMapping("/exporttable")
	public void exporttable(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "createdate", required = false, defaultValue = "") String createdate) {
		Branch nowbranch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		Map<Long, BranchTodayLog> branchAndTomorrow = new HashMap<Long, BranchTodayLog>();
		// 时间处理
		String dateFirStr = "".equals(createdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : createdate + " 00:00:00";
		String dateFirEnd = "".equals(createdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : createdate + " 23:59:59";
		String dateSecStr;
		String dateSecEnd;
		try {
			dateSecStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateFirStr).getTime() + 24 * 60 * 60 * 1000)).toString();
			dateSecEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateFirEnd).getTime() + 24 * 60 * 60 * 1000)).toString();
		} catch (ParseException e) {
			// 错误 默认查系统日期 明天的
			dateSecStr = DateTimeUtil.getDateAfter(new Date().getTime(), 1);
			dateSecEnd = DateTimeUtil.getDateAfter(new Date().getTime(), 1);
		}
		List<BranchTodayLog> todayLogList = new ArrayList<BranchTodayLog>();
		List<BranchTodayLog> tomorrowLogList = new ArrayList<BranchTodayLog>();

		if (nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {// 站点角色
			todayLogList = logTodayDAO.getBranchTodayLogByBranchidAndDateList(nowbranch.getBranchid(), dateFirStr, dateFirEnd);
			tomorrowLogList = logTodayDAO.getBranchTodayLogByBranchidAndDateList(nowbranch.getBranchid(), dateSecStr, dateSecEnd);
		} else {// 获取所有的
			List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "");
			String branchids = "-1";
			if (branchnameList != null && branchnameList.size() > 0) {
				for (Branch branch2 : branchnameList) {
					branchids += "," + branch2.getBranchid() + "";
				}
			}
			todayLogList = logTodayDAO.getBranchTodayLogByDateListExport(dateFirStr, dateFirEnd, branchids);
			// 获取次日应投
			tomorrowLogList = logTodayDAO.getBranchTodayLogByDateListExport(dateSecStr, dateSecEnd, branchids);

		}
		if (tomorrowLogList != null && tomorrowLogList.size() > 0) {
			for (BranchTodayLog tomorrowlog : tomorrowLogList) {
				branchAndTomorrow.put(tomorrowlog.getBranchid(), tomorrowlog);
			}
		}
		// 站点的map
		Map<Long, String> branchMap = new HashMap<Long, String>();
		List<Branch> branchs = branchDAO.getAllBranches();
		for (Branch branch : branchs) {
			branchMap.put(branch.getBranchid(), branch.getBranchname());
		}

		final List<BranchTodayLog> todayLogs = todayLogList;
		final Map<Long, BranchTodayLog> tomorrwMap = branchAndTomorrow;
		final Map<Long, String> branchnameMap = branchMap;

		String[] cloumnName1 = new String[11]; // 导出的列名
		String[] cloumnName2 = new String[11]; // 导出的英文列名

		exportService.SetBranchLogFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sheetName = "站点日志" + dateFirStr.substring(0, 10); // sheet的名称
		String fileName = sheetName + ".xlsx"; // 文件名
		try {
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					style.setAlignment(style.ALIGN_JUSTIFY);
					for (int k = 0; k < todayLogs.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setBranchLogObject(cloumnName3, todayLogs, tomorrwMap, branchnameMap, a, i, k);
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

}
