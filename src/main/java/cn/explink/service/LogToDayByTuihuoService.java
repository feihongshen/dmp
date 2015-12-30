package cn.explink.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.LogTodayByTuihuoDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TuihuoSiteTodaylog;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateDayUtil;

@Component
@DependsOn({ "systemInstallDAO" })
public class LogToDayByTuihuoService implements SystemConfigChangeListner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	LogTodayByTuihuoDAO logTodayByTuihuoDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;

	@Autowired
	CamelContext camelContext;

	/*
	 * 改为quartz实现
	@PostConstruct
	public void init() throws Exception {
		final SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("tuiHuoDayLogTime");
		if (siteDayLogTime == null || !StringUtils.hasLength(siteDayLogTime.getValue())) {
			camelContext.stopRoute("退货定时任务生成");
			logger.warn("自动站点日志未启用，没有找到参数{}", siteDayLogTime);
			return;
		}
		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " " + siteDayLogTime.getValue();
				Date nextExecuteTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
				long period = 86400000;
				long delay = nextExecuteTime.getTime() - new Date().getTime();
				if (delay < 0) {
					delay = period + delay % period;
				}
				from("timer://foo?fixedRate=true&period=" + period + "&delay=" + delay).to("bean:logToDayByTuihuoService?method=dailyDayLogByTuihuoGenerate").routeId("退货定时任务生成");
			}
		});
	}
	*/
	public List<CwbOrder> getOrderListByBranchidAndType(List<Branch> kufangList, long branchid, String type, String startTime, long page) {
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		if ("zhandianyingtuihuo".equals(type)) {
			return cwbDAO.getZhanDianYingtuiList(FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.JuShou.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + ","
					+ DeliveryStateEnum.ShangMenTuiChengGong.getValue(), page);
		}
		if ("zhandiantuihuochukuzaitu".equals(type)) {
			List<String> cwblist = logTodayByTuihuoDAO.getFlowTypeCwbs(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), startTime, page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					list.add(cwbDAO.getCwbByCwb(cwb));
				}
			}
			return list;
		}
		if ("tuihuozhanruku".equals(type)) {
			List<String> cwblist = logTodayByTuihuoDAO.getFlowTypeCwbs(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), startTime, page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					list.add(cwbDAO.getCwbByCwb(cwb));
				}
			}
			return list;
		}
		if ("tuihuozhantuihuochukuzaitu".equals(type)) {
			String branchids = "";
			for (Branch branch : kufangList) {
				branchids += branch.getBranchid() + ",";
			}
			branchids = branchids.length() > 0 ? branchids.substring(0, branchids.length() - 1) : "0";
			List<String> cwblist = logTodayByTuihuoDAO.getFlowTypeCwbsByTuihuozhan(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchids, startTime, page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					list.add(cwbDAO.getCwbByCwb(cwb));
				}
			}
			return list;
		}
		if ("tuigonghuoshangchuku".equals(type)) {
			List<String> cwblist = logTodayByTuihuoDAO.getFlowTypeCwbs(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), startTime, page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					list.add(cwbDAO.getCwbByCwb(cwb));
				}
			}
			return list;
		}
		if ("gonghuoshangshouhuo".equals(type)) {
			List<String> cwblist = logTodayByTuihuoDAO.getFlowTypeCwbs(FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), startTime, page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					list.add(cwbDAO.getCwbByCwb(cwb));
				}
			}
			return list;
		}
		if ("gonghuoshangjushoufanku".equals(type)) {
			List<String> cwblist = logTodayByTuihuoDAO.getFlowTypeCwbs(FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), startTime, page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					list.add(cwbDAO.getCwbByCwb(cwb));
				}
			}
			return list;
		}

		return list;
	}

	public long getOrderListByBranchidAndTypeConut(List<Branch> kufangList, long branchid, String type, String startTime) {
		if ("zhandianyingtuihuo".equals(type)) {
			// 站点应退
			long zhandianyingtui = logTodayByTuihuoDAO.getZhanDianYingtuiCount(FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.JuShou.getValue() + ","
					+ DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue());
			return zhandianyingtui;
		}
		if ("zhandiantuihuochukuzaitu".equals(type)) {
			return logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), startTime);
		}
		if ("tuihuozhanruku".equals(type)) {
			return logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), startTime);
		}
		if ("tuihuozhantuihuochukuzaitu".equals(type)) {
			String branchids = "";
			for (Branch branch : kufangList) {
				branchids += branch.getBranchid() + ",";
			}
			branchids = branchids.length() > 0 ? branchids.substring(0, branchids.length() - 1) : "0";
			return logTodayByTuihuoDAO.getFlowTypeCountByTuihuozhan(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchids, startTime);
		}
		if ("tuigonghuoshangchuku".equals(type)) {
			return logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), startTime);
		}
		if ("gonghuoshangshouhuo".equals(type)) {
			return logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), startTime);
		}
		if ("gonghuoshangjushoufanku".equals(type)) {
			return logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), startTime);
		}
		return 0;
	}

	public TuihuoSiteTodaylog generateTodaylog(long branchid, long userid, String startTime, String endTime) {
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
		long zhandianyingtui = logTodayByTuihuoDAO.getZhanDianYingtuiCount(FlowOrderTypeEnum.YiShenHe.getValue(),
				DeliveryStateEnum.JuShou.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue());
		// 退供货商出库
		long tuigonghuoshangchuku = logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), startTime);
		// 退货站入库
		long tuihuozhanruku = logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), startTime);
		// 退货站退货出库在途
		long tuihuozhantuihuochukuzaitu = logTodayByTuihuoDAO.getFlowTypeCountByTuihuozhan(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid + "", startTime);
		// 站点退货出站
		long zhandiantuihuochukuzaitu = logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), startTime);
		// 供货商拒收返库
		long gonghuoshangjushoufanku = logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), startTime);
		// 供货商退货成功
		long gonghuoshangshouhuo = logTodayByTuihuoDAO.getFlowTypeCount(FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), startTime);

		TuihuoSiteTodaylog tview = new TuihuoSiteTodaylog();
		tview.setZhandianyingtui(zhandianyingtui);
		tview.setTuigonghuoshangchuku(tuigonghuoshangchuku);
		tview.setTuihuozhanruku(tuihuozhanruku);
		tview.setTuihuozhantuihuochukuzaitu(tuihuozhantuihuochukuzaitu);
		tview.setZhandiantuihuochukuzaitu(zhandiantuihuochukuzaitu);
		tview.setGonghuoshangjushoufanku(gonghuoshangjushoufanku);
		tview.setGonghuoshangshouhuo(gonghuoshangshouhuo);
		tview.setCteatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		tview.setStarttime(startTime);
		tview.setEndtime(endTime);
		tview.setTuihuoid(branchid);
		return tview;
	}

	public void dailyDayLogByTuihuoGenerate() {

		SystemInstall remoteInvoke = systemInstallDAO.getSystemInstallByName("isOpenJobHand");// 判断是否开启远程调用
		String value = remoteInvoke.getValue();
		if (value != null && value.equals("yes")) {
			logger.warn("站点退货日志任务-远程调用已开启，本地定时任务不执行dailylogs");
			return;
		}

		logger.info("start to generate warehouse dailylogs");
		User user = userDAO.getUserByUsername("admin");
		Date endDate = new Date();
		dailyDayLogByTuihuoGenerate(user, endDate);
		logger.info("generate dailylogs warehouse  ends");
	}

	/**
	 * 远程调用
	 */
	public long dailyDayLogByTuihuoGenerate_RemoteInvoke() {
		long calcCount = 0;
		SystemInstall remoteInvoke = systemInstallDAO.getSystemInstallByName("isOpenJobHand");// 判断是否开启远程调用
		String value = remoteInvoke.getValue();
		if (value != null && value.equals("yes")) {
			logger.info("start to generate warehouse dailylogs");
			User user = userDAO.getUserByUsername("admin");
			Date endDate = new Date();
			calcCount = dailyDayLogByTuihuoGenerate(user, endDate);
			logger.info("generate dailylogs warehouse  ends");
		} else {
			logger.warn("站点退货日志任务-未开启远程调用dailylogs");
		}
		return calcCount;

	}

	private long dailyDayLogByTuihuoGenerate(User user, Date endDate) {
		int calcCount = 0;
		List<Branch> tuihuoList = branchDAO.getAllBranchBySiteType(BranchEnum.TuiHuo.getValue());
		if (tuihuoList != null && tuihuoList.size() > 0) {
			for (Branch branch : tuihuoList) {
				long branchid = branch.getBranchid();
				calcCount++;
				try {
					Date startDate;
					TuihuoSiteTodaylog warehouseTodaylog = logTodayByTuihuoDAO.getLastBranchTodayLogByWarehouseid(branchid);
					if (warehouseTodaylog != null) {
						startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(warehouseTodaylog.getCteatetime());
					} else {
						startDate = DateDayUtil.getTimeByDay(-1);
					}
					TuihuoSiteTodaylog tuihuoSiteTodaylog = generateTodaylog(branchid, user.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate), new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").format(endDate));
					if (tuihuoSiteTodaylog != null) {
						logTodayByTuihuoDAO.creBranchTodayLog(tuihuoSiteTodaylog);
					}
				} catch (ParseException e) {
					logger.error("error while generating dailylog for branchid: " + tuihuoList.get(0).getBranchname(), e);
				}
			}
		}
		return calcCount;

	}

	@Override
	public void onChange(Map<String, String> parameters) {
		/*
		 * 改为quartz实现
		if (parameters.keySet().contains("tuiHuoDayLogTime")) {
			try {
				this.init();
			} catch (Exception e) {
				logger.error("error while reloading logtoDay camle routes", e);
			}
		}
		*/
	}
}
