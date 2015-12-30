package cn.explink.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.sf.json.JSONObject;

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
import cn.explink.dao.LogTodayByWarehouseDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.WarehouseTodaylog;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateDayUtil;

@Component
@DependsOn({ "systemInstallDAO" })
public class LogToDayByWarehouseService implements SystemConfigChangeListner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	LogTodayByWarehouseDAO logTodayByWarehouseDAO;
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
		final SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("wareHouseDayLogTime");
		if (siteDayLogTime == null || !StringUtils.hasLength(siteDayLogTime.getValue())) {
			camelContext.stopRoute("站点定时任务生成");
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
				from("timer://foo?fixedRate=true&period=" + period + "&delay=" + delay).to("bean:logToDayByWarehouseService?method=dailyDayLogByWareHouseGenerate").routeId("库房定时任务生成");
			}
		});
	}
	*/
	
	// 未入库
	public Map<Long, Long> getWeirukuMap(long branchid, String flowordertypes) {

		List<JSONObject> list = logTodayByWarehouseDAO.getWeirukuCount(branchid, flowordertypes);

		Map<Long, Long> weirukuMap = new HashMap<Long, Long>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				weirukuMap.put(list.get(i).getLong("customerid"), list.get(i).getLong("num"));
			}
		}

		return weirukuMap;
	}

	// 已入库
	public Map<Long, Long> getYirukuMap(long branchid, long flowordertype, String startTime, String endTime) {

		List<JSONObject> list = logTodayByWarehouseDAO.getYirukuCount(branchid, flowordertype, startTime, endTime);

		Map<Long, Long> yirukuMap = new HashMap<Long, Long>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				yirukuMap.put(list.get(i).getLong("customerid"), list.get(i).getLong("num"));
			}
		}

		return yirukuMap;
	}

	// 到错货
	public Map<Long, Long> getDaocuohuoMap(long branchid, long flowordertype) {

		List<JSONObject> list = logTodayByWarehouseDAO.getDaocuohuoCount(branchid, flowordertype);

		Map<Long, Long> daocuohuoMap = new HashMap<Long, Long>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				daocuohuoMap.put(list.get(i).getLong("customerid"), list.get(i).getLong("num"));
			}
		}

		return daocuohuoMap;
	}

	// 昨日出库在途
	public Map<Long, Long> getZuorichukuzaituMap(long branchid, long flowordertype, String startTime) {

		List<JSONObject> list = logTodayByWarehouseDAO.getZuorichukuzaituCount(branchid, flowordertype, startTime);

		Map<Long, Long> zuorichukuzaituMap = new HashMap<Long, Long>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				zuorichukuzaituMap.put(list.get(i).getLong("customerid"), list.get(i).getLong("num"));
			}
		}

		return zuorichukuzaituMap;
	}

	// 今日出库
	public Map<Long, Long> getJinrichukuMap(long branchid, String flowordertypes, String startTime) {

		List<JSONObject> list = logTodayByWarehouseDAO.getJinrichukuCount(branchid, flowordertypes, startTime);

		Map<Long, Long> jinrichukuzaituMap = new HashMap<Long, Long>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				jinrichukuzaituMap.put(list.get(i).getLong("customerid"), list.get(i).getLong("num"));
			}
		}

		return jinrichukuzaituMap;
	}

	// 今日出库在途
	public Map<Long, Long> getJinrichukuzaituMap(long branchid, long flowordertype, String startTime) {

		List<JSONObject> list = logTodayByWarehouseDAO.getJinrichukuzaituCount(branchid, flowordertype, startTime);

		Map<Long, Long> jinrichukuzaituMap = new HashMap<Long, Long>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				jinrichukuzaituMap.put(list.get(i).getLong("customerid"), list.get(i).getLong("num"));
			}
		}

		return jinrichukuzaituMap;
	}

	// 今日库存
	public Map<Long, Long> getJinrikucunMap(long branchid) {

		List<JSONObject> list = logTodayByWarehouseDAO.getJinrikucunCount(branchid);

		Map<Long, Long> jinrikucunMap = new HashMap<Long, Long>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				jinrikucunMap.put(list.get(i).getLong("customerid"), list.get(i).getLong("num"));
			}
		}

		return jinrikucunMap;
	}

	/**
	 * 今日出库已到站 = 所有已到站 - 漏扫到站
	 * 
	 * @param flowordertypeStr
	 *            7,8
	 * @param flowordertype
	 *            6
	 * @param branchid
	 * @param startTime
	 * @return
	 */
	public Map<Long, Long> getChukuyidaozhanMap(String flowordertypeStr, long flowordertype, long branchid, String startTime) {
		List<JSONObject> yidaozhanlist = logTodayByWarehouseDAO.getYidaozhanCount(flowordertypeStr, startTime);
		List<JSONObject> lousaodaozhanlist = logTodayByWarehouseDAO.getLousaoyidaozhanCount(flowordertype, branchid, startTime);

		Map<Long, Long> chukuyidaozhanMap = new HashMap<Long, Long>();
		Map<Long, Long> lousaodaozhanMap = new HashMap<Long, Long>();
		if (lousaodaozhanlist != null) {
			for (int i = 0; i < lousaodaozhanlist.size(); i++) {
				lousaodaozhanMap.put(lousaodaozhanlist.get(i).getLong("customerid"), lousaodaozhanlist.get(i).getLong("num"));
			}
		}
		if (yidaozhanlist != null) {
			for (int i = 0; i < yidaozhanlist.size(); i++) {
				chukuyidaozhanMap.put(yidaozhanlist.get(i).getLong("customerid"), yidaozhanlist.get(i).getLong("num")
						- (lousaodaozhanMap.get(yidaozhanlist.get(i).getLong("customerid")) == null ? 0 : lousaodaozhanMap.get(yidaozhanlist.get(i).getLong("customerid"))));
			}
		}

		return chukuyidaozhanMap;
	}

	// 未出库到站
	public Map<Long, Long> getWeichukudaozhanMap(long flowordertype, long branchid, String startTime) {

		List<JSONObject> lousaodaozhanlist = logTodayByWarehouseDAO.getLousaoyidaozhanCount(flowordertype, branchid, startTime);
		Map<Long, Long> lousaodaozhanMap = new HashMap<Long, Long>();
		if (lousaodaozhanlist != null) {
			for (int i = 0; i < lousaodaozhanlist.size(); i++) {
				lousaodaozhanMap.put(lousaodaozhanlist.get(i).getLong("customerid"), lousaodaozhanlist.get(i).getLong("num"));
			}
		}
		return lousaodaozhanMap;
	}

	// ========================================================查看明细=======begin===================

	/**
	 * 获取库房今日日志明细
	 * 
	 * @param branchid
	 * @param customerid
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getOrderListByCustomeridAndType(long branchid, long customerid, String type, String startTime, String endTime, long page) {

		List<CwbOrder> list = new ArrayList<CwbOrder>();
		if ("weiruku".equals(type)) {
			return cwbDAO.getWeirukuList(branchid, customerid,
					FlowOrderTypeEnum.UpdateDeliveryBranch.getValue() + "," + FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + ","
							+ FlowOrderTypeEnum.TuiHuoChuZhan.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), page);
		}
		if ("yiruku".equals(type)) {
			List<String> cwblist = logTodayByWarehouseDAO.getYirukuCwb(customerid, branchid, FlowOrderTypeEnum.RuKu.getValue(), startTime, endTime, page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					list.add(cwbDAO.getCwbByCwb(cwb));
				}
			}
			return list;
		}
		if ("daocuohuo".equals(type)) {
			return cwbDAO.getDaocuohuoList(branchid, customerid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), page);
		}
		// if("zuorichukuzaitu".equals(type)){
		// return cwbDAO.getZuorichukuzaituList(branchid,
		// FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), customerid, startTime,
		// page);
		// }
		if ("jinrichukuzaitu".equals(type)) {
			return cwbDAO.getJinrichukuList(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), customerid, startTime, page);
		}
		if ("chukuyidaozhan".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getLousaodaozhanCwb(FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), branchid, customerid,
					startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (CwbOrder cwbOrder : cwborderList) {
					cwbs += "'" + cwbOrder.getCwb() + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}

			return cwbDAO.getYichukudaozhan(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), customerid, startTime, cwbs, page);
		}
		if ("weichukuyidaozhan".equals(type)) {
			return cwbDAO.getLousaodaozhan(FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), branchid, customerid, startTime, page);
		}
		if ("kucun".equals(type)) {
			return cwbDAO.getJinriKucun(branchid, customerid, page);
		}
		return list;
	}

	/**
	 * 获取 库房今日日志明细 不限制供货商
	 * 
	 * @param branchid
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getOrderListByType(long branchid, String type, String startTime, String endTime, long page) {

		List<CwbOrder> list = new ArrayList<CwbOrder>();
		if ("weiruku".equals(type)) {
			return cwbDAO.getWeirukuList(branchid, FlowOrderTypeEnum.UpdateDeliveryBranch.getValue() + "," + FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.ChuKuSaoMiao.getValue()
					+ "," + FlowOrderTypeEnum.TuiHuoChuZhan.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), page);
		}
		if ("yiruku".equals(type)) {
			List<String> cwblist = logTodayByWarehouseDAO.getYirukuCwb(branchid, FlowOrderTypeEnum.RuKu.getValue(), startTime, endTime, page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					list.add(cwbDAO.getCwbByCwb(cwb));
				}
			}
			return list;
		}
		if ("daocuohuo".equals(type)) {
			return cwbDAO.getDaocuohuoList(branchid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), page);
		}
		// if("zuorichukuzaitu".equals(type)){
		// return cwbDAO.getZuorichukuzaituList(branchid,
		// FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), customerid, startTime,
		// page);
		// }
		if ("jinrichukuzaitu".equals(type)) {
			return cwbDAO.getJinrichukuList(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), startTime, page);
		}
		if ("chukuyidaozhan".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getLousaodaozhanCwb(FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), branchid, startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (CwbOrder cwbOrder : cwborderList) {
					cwbs += "'" + cwbOrder.getCwb() + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}

			return cwbDAO.getYichukudaozhan(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), startTime, cwbs, page);
		}
		if ("weichukuyidaozhan".equals(type)) {
			return cwbDAO.getLousaodaozhan(FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), branchid, startTime, page);
		}
		if ("kucun".equals(type)) {
			return cwbDAO.getJinriKucun(branchid, page);
		}
		return list;
	}

	// ========================================================查看明细===end=======================

	/**
	 * 查询到的所有统计的map 转换成库房日志的对象
	 * 
	 * @param weirukuMap
	 * @param yirukuMap
	 * @param daocuohuoMap
	 * @param zuorichukuzaituMap
	 * @param jinrichukuzaituMap
	 * @param chukuyidaozhanMap
	 * @param lousaodaozhanMap
	 * @param jinrikucunMap
	 * @return
	 */
	public List<WarehouseTodaylog> getWarehouseTodaylog(long branchid, List<Customer> customerList, long userid, String startDate, String endDate, Map<Long, Long> weirukuMap,
			Map<Long, Long> yirukuMap, Map<Long, Long> daocuohuoMap, Map<Long, Long> zuorichukuzaituMap, Map<Long, Long> jinrichukuzaituMap, Map<Long, Long> chukuyidaozhanMap,
			Map<Long, Long> lousaodaozhanMap, Map<Long, Long> jinrikucunMap, Map<Long, Long> zuirikucunMap) {

		List<WarehouseTodaylog> list = new ArrayList<WarehouseTodaylog>();
		if (customerList != null) {
			for (Customer customer : customerList) {
				long customerid = customer.getCustomerid();
				WarehouseTodaylog warehouseTodaylog = new WarehouseTodaylog();
				warehouseTodaylog.setUserid(userid);
				warehouseTodaylog.setStarttime(startDate);
				warehouseTodaylog.setEndtime(endDate);
				warehouseTodaylog.setCteatetime(endDate);
				warehouseTodaylog.setWarehouseid(branchid);
				warehouseTodaylog.setCustomerid(customerid);
				warehouseTodaylog.setJinri_weiruku(weirukuMap.get(customerid) == null ? 0 : weirukuMap.get(customerid));
				warehouseTodaylog.setJinri_yiruku(yirukuMap.get(customerid) == null ? 0 : yirukuMap.get(customerid));
				warehouseTodaylog.setJinri_daocuohuo(daocuohuoMap.get(customerid) == null ? 0 : daocuohuoMap.get(customerid));
				warehouseTodaylog.setZuori_chukuzaitu(zuorichukuzaituMap.get(customerid) == null ? 0 : zuorichukuzaituMap.get(customerid));
				warehouseTodaylog.setJinri_chukuzaitu(jinrichukuzaituMap.get(customerid) == null ? 0 : jinrichukuzaituMap.get(customerid));
				warehouseTodaylog.setJinri_chukuyidaozhan(chukuyidaozhanMap.get(customerid) == null ? 0 : chukuyidaozhanMap.get(customerid));
				warehouseTodaylog.setJinri_weichukuyidaozhan(lousaodaozhanMap.get(customerid) == null ? 0 : lousaodaozhanMap.get(customerid));
				warehouseTodaylog.setJinri_kucun(jinrikucunMap.get(customerid) == null ? 0 : jinrikucunMap.get(customerid));
				warehouseTodaylog.setZuori_kucun(zuirikucunMap.get(customerid) == null ? 0 : zuirikucunMap.get(customerid));
				list.add(warehouseTodaylog);
			}
		}

		return list;
	}

	public List<WarehouseTodaylog> generateTodaylog(long branchid, long userid, String startTime, String endTime) {
		List<Customer> customerList = customerDAO.getAllCustomers();
		// 未入库
		Map<Long, Long> weirukuMap = this.getWeirukuMap(branchid, FlowOrderTypeEnum.UpdateDeliveryBranch.getValue() + "" + FlowOrderTypeEnum.DaoRuShuJu.getValue() + ","
				+ FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.TuiHuoChuZhan.getValue());
		// 已入库
		Map<Long, Long> yirukuMap = this.getYirukuMap(branchid, FlowOrderTypeEnum.RuKu.getValue(), startTime, endTime);
		// 到错货
		Map<Long, Long> daocuohuoMap = this.getDaocuohuoMap(branchid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		// 昨日出库在途
		Map<Long, Long> zuorichukuzaituMap = this.getZuorichukuzaituMap(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), startTime);
		// 今日出库
		Map<Long, Long> jinrichukuzaituMap = this.getJinrichukuMap(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "," + FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), startTime);
		// 出库已到站
		Map<Long, Long> chukuyidaozhanMap = this.getChukuyidaozhanMap(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
				FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid, startTime);
		// 未出库已到站
		Map<Long, Long> lousaodaozhanMap = this.getWeichukudaozhanMap(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid, startTime);
		// 今日库存
		Map<Long, Long> jinrikucunMap = this.getJinrikucunMap(branchid);

		List<WarehouseTodaylog> warehouseLogList = logTodayByWarehouseDAO.getLogByWarehouseidAndDate(branchid, startTime, startTime);

		Map<Long, Long> zuirikucunMap = new HashMap<Long, Long>();
		if (warehouseLogList != null) {
			for (WarehouseTodaylog warehouseTodaylog : warehouseLogList) {
				zuirikucunMap.put(warehouseTodaylog.getCustomerid(), warehouseTodaylog.getJinri_kucun());
			}
		}
		return this.getWarehouseTodaylog(branchid, customerList, userid, startTime, endTime, weirukuMap, yirukuMap, daocuohuoMap, zuorichukuzaituMap, jinrichukuzaituMap, chukuyidaozhanMap,
				lousaodaozhanMap, jinrikucunMap, zuirikucunMap);
	}

	public void dailyDayLogByWareHouseGenerate() {
		SystemInstall remoteInvoke = systemInstallDAO.getSystemInstallByName("isOpenJobHand");// 判断是否开启远程调用
		String value = remoteInvoke.getValue();
		if (value != null && value.equals("yes")) {
			logger.warn("库房日志任务-远程调用已开启，本地定时任务不执行dailylogs");
			return;
		}
		logger.info("start to generate warehouse dailylogs");
		User user = userDAO.getUserByUsername("admin");
		Date endDate = new Date();
		dailyDayLogGenerate(user, endDate);
		logger.info("generate dailylogs warehouse  ends");
	}

	/**
	 * 远程调用
	 */
	public long dailyDayLogByWareHouseGenerate_remoteInvoke() {
		long calcCount = 0;
		SystemInstall remoteInvoke = systemInstallDAO.getSystemInstallByName("isOpenJobHand");// 判断是否开启远程调用
		String value = remoteInvoke.getValue();
		if (value != null && value.equals("yes")) {
			logger.info("start to generate warehouse dailylogs");
			User user = userDAO.getUserByUsername("admin");
			Date endDate = new Date();
			calcCount = dailyDayLogGenerate(user, endDate);
			logger.info("generate dailylogs warehouse  ends");
		} else {
			logger.warn("库房日志任务-未开启远程调用dailylogs");
		}
		return calcCount;
	}

	private long dailyDayLogGenerate(User user, Date endDate) {
		long calcCount = 0;
		List<Branch> kufangList = branchDAO.getAllBranchBySiteType(BranchEnum.KuFang.getValue());
		if (kufangList != null && kufangList.size() > 0) {
			for (Branch branch : kufangList) {
				long branchid = branch.getBranchid();
				calcCount++;
				try {
					Date startDate;
					WarehouseTodaylog warehouseTodaylog = logTodayByWarehouseDAO.getLastBranchTodayLogByWarehouseid(branchid);
					if (warehouseTodaylog != null) {
						startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(warehouseTodaylog.getCteatetime());
					} else {
						startDate = DateDayUtil.getTimeByDay(-1);
					}
					List<WarehouseTodaylog> list = generateTodaylog(branchid, user.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate), new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").format(endDate));
					if (list != null) {
						for (WarehouseTodaylog warehouselog : list) {
							logTodayByWarehouseDAO.creBranchTodayLog(warehouselog);
						}
					}
				} catch (ParseException e) {
					logger.error("error while generating dailylog for branchid: " + kufangList.get(0).getBranchname(), e);
				}
			}
		}
		return calcCount;
	}

	@Override
	public void onChange(Map<String, String> parameters) {
		/*
		 * 改为quartz实现
		if (parameters.keySet().contains("wareHouseDayLogTime")) {
			try {
				this.init();
			} catch (Exception e) {
				logger.error("error while reloading logtoDay camle routes", e);
			}
		}
		*/
	}
}
