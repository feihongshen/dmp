package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryPercentDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.LogTodayLogDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchTodayLog;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryPercent;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryPercentTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.DeliverypercentService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.LogToDayService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@RequestMapping("/percent")
@Controller
public class DeliveryPercentController {

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	LogToDayService logToDayService;
	@Autowired
	LogTodayLogDAO logTodayDAO;
	@Autowired
	DeliveryPercentDAO deliveryPercentDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	DeliverypercentService deliverypercentService;

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 今天的投递率
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toDaypercent")
	public String toDaypercent(Model model) {
		String toDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); // 获得遗留订单统计的时间
		String toDayTime = toDay;// 用于订单统计时间 与日志生成时间一致
		List<DeliveryPercent> dpList = deliveryPercentDAO.getDeliveryPercentEnd();
		if (dpList.size() > 0) {// 获取最后一个插入数据库的遗留订单记录，以这个订单的插入时间为查询条件
			toDay = dpList.get(0).getCredate();
		}
		BranchTodayLog btl = logTodayDAO.getLastBranchTodayLogByBranchid();
		if (btl != null) {
			toDayTime = btl.getCreatedate();
		}
		// 应投 =到货+昨日滞留+昨日到站未领+昨日未归班
		Map<Long, Map<Long, Long>> yingtouMap = new HashMap<Long, Map<Long, Long>>();
		List<JSONObject> today_daohuoList = deliveryPercentDAO.getCwbByFlowOrderTypeAndTodayForCount(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao, toDayTime);// 今日到站

		yingtouMap = getJsonForMap(today_daohuoList, yingtouMap);

		List<JSONObject> yestoday_zhiliuList = deliveryPercentDAO.getCwbByFlowOrderTypeForCountJson(DeliveryPercentTypeEnum.ZhiLiu, toDay);// 昨日滞留
		yingtouMap = getJsonForMap(yestoday_zhiliuList, yingtouMap);

		List<JSONObject> yestoday_daohuoList = deliveryPercentDAO.getCwbByFlowOrderTypeForCountJson(DeliveryPercentTypeEnum.DaoZhanWeiLing, toDay);// 昨日到站未领
		yingtouMap = getJsonForMap(yestoday_daohuoList, yingtouMap);

		List<JSONObject> yestoday_weiguibanList = deliveryPercentDAO.getCwbByFlowOrderTypeForCountJson(DeliveryPercentTypeEnum.WeiGuiBan, toDay);// 昨日未归班
		yingtouMap = getJsonForMap(yestoday_weiguibanList, yingtouMap);
		model.addAttribute("yingtouMap", yingtouMap);
		// 实投 =昨日未归班+今日领货
		Map<Long, Map<Long, Long>> shitouMap = new HashMap<Long, Map<Long, Long>>();
		shitouMap = getJsonForMap(yestoday_weiguibanList, shitouMap);

		List<JSONObject> today_linghuoList = deliveryPercentDAO.getCwbByTodayForCountJsonGroupDeliverbranchid(toDayTime);// 今日领货
		shitouMap = getJsonForMap(today_linghuoList, shitouMap);
		model.addAttribute("shitouMap", shitouMap);
		// 妥投 = 配送成功+上门换成功+上门退成功
		Map<Long, Map<Long, Long>> tuotouMap = new HashMap<Long, Map<Long, Long>>();
		List<JSONObject> today_tuotouList = deliveryPercentDAO.getCwbByTodaySuccessForCountJsonGroupDeliverbranchid(toDayTime);// 配送成功+上门换成功+上门退成功
		tuotouMap = getJsonForMap(today_tuotouList, tuotouMap);
		model.addAttribute("tuotouMap", tuotouMap);

		model.addAttribute("branchList", branchDAO.getAllBranchBySiteType(BranchEnum.ZhanDian.getValue()));
		List<Customer> customerList = customerDAO.getAllCustomers();
		model.addAttribute("customerList", customerList);
		int width = 1400;
		if (customerList.size() > 3) {
			width = width + (300 * (customerList.size() - 3));
		}
		model.addAttribute("width", width);
		return "logtoday/deliverypercent/todaypercent";
	}

	public Map<Long, Map<Long, Long>> getJsonForMap(List<JSONObject> list, Map<Long, Map<Long, Long>> percentMap) {
		for (JSONObject obj : list) {
			if (percentMap.get(obj.getLong("branchid")) == null) {// 如果不存在这个站点的统计信息则创建一个
				Map<Long, Long> map = new HashMap<Long, Long>();
				map.put(obj.getLong("customerid"), obj.getLong("num"));
				percentMap.put(obj.getLong("branchid"), map);
			} else {// 如果存在
				Map<Long, Long> map = percentMap.get(obj.getLong("branchid"));
				if (map.get(obj.getLong("customerid")) == null) {// 如果供货商不存在这个站点下，则创建
					map.put(obj.getLong("customerid"), obj.getLong("num"));
				} else {// 如果供货商已经存在这个站点下，则直接累加单数
					map.put(obj.getLong("customerid"), (map.get(obj.getLong("customerid")) + obj.getLong("num")));
				}
			}
		}
		return percentMap;
	}

	/**
	 * 各站历史投递率
	 * 
	 * @param model
	 * @param page
	 *            分页 备用
	 * @param startdate
	 *            开始时间
	 * @param enddate
	 *            结束时间
	 * @return
	 */
	@RequestMapping("/history/{page}")
	public String allhistorylog(Model model, @PathVariable("page") long page, @RequestParam(value = "startdate", required = false, defaultValue = "") String startdate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate) {

		Map<Long, Map<String, BranchTodayLog>> branchMap = new HashMap<Long, Map<String, BranchTodayLog>>();
		List<Branch> branchnameList = branchDAO.getBranchAllzhandian(BranchEnum.ZhanDian.getValue() + "");

		List<BranchTodayLog> todayLogList = logTodayDAO.getBranchTodayLogByBranchidAndDateAllList("".equals(startdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00"
				: startdate + " 00:00:00", "".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : enddate + " 23:59:59");
		if (branchnameList != null && branchnameList.size() > 0) {
			for (Branch branch : branchnameList) {
				Map<String, BranchTodayLog> logMap = new HashMap<String, BranchTodayLog>();
				if (todayLogList != null && todayLogList.size() > 0) {
					for (BranchTodayLog branchTodayLog : todayLogList) {
						if (branchTodayLog.getBranchid() == branch.getBranchid()) {
							logMap.put(branchTodayLog.getCreatedate().substring(0, 10), branchTodayLog);
						}
					}
				}
				branchMap.put(branch.getBranchid(), logMap);
			}

		}

		List<String> dateList = getDateLsit("".equals(startdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : startdate + " 00:00:00",
				"".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : enddate + " 23:59:59");
		model.addAttribute("startdate", "".equals(startdate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : startdate);
		model.addAttribute("enddate", "".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : enddate);
		model.addAttribute("dateList", dateList);
		model.addAttribute("branchnameList", branchnameList);
		model.addAttribute("branchMap", branchMap);
		int width = 1400;
		if (dateList.size() > 3) {
			width = width + (300 * (dateList.size() - 3));
		}
		model.addAttribute("width", width);
		return "logtoday/deliverypercent/historypercent";
	}

	/**
	 * 获取两个时间之间的日期list
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	private List<String> getDateLsit(String startdate, String enddate) {
		if (DateDayUtil.getDaycha(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), enddate) > 0) {
			enddate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		List<String> list = new ArrayList<String>();
		long dayCha = DateDayUtil.getDaycha(startdate, enddate);// 获取两个时间的时间差（天数）
		if (dayCha > -1) {
			for (int i = 0; i < dayCha + 1; i++) {
				list.add(DateDayUtil.getDayCum(startdate, i));
			}
		}

		return list;
	}

	/**
	 * 发货批次投递率统计
	 * 
	 * @param model
	 * @param page
	 *            分页 备用
	 * @param startdate
	 *            开始时间
	 * @param enddate
	 *            结束时间
	 * @return
	 */
	@RequestMapping("/emaildateDeliveryPercent")
	public String emaildateDeliveryPercent(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "emaildateid", required = false, defaultValue = "") String[] emaildateid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") int flowordertype) {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		// 获取两个月内的批次
		model.addAttribute("emaildateList", emailDateDAO.getEmailDateByEmaildatetimeAndCustomerid(df.format(new Date((System.currentTimeMillis() / 1000) - (60 * 60 * 24 * 61) * 1000)), customerid));
		model.addAttribute("emaildateidStr", getList(emaildateid));

		if (emaildateid.length == 0) {
			return "logtoday/deliverypercent/emaildatepercent";
		}
		List<CwbOrder> cList = cwbDAO.getCwbsByEmailDateIds(getStrings(emaildateid));
		List<DeliveryPercentForEmaildateDTO> dpfeList = deliveryPercentDAO.getDeliveryPercentForEmaildate(getStrings(emaildateid), flowordertype);
		long hour = 1000 * 60 * 60;
		long dp24, dp36, dp48, dpall;
		dp24 = dp36 = dp48 = dpall = 0L;
		StringBuffer dp24cwbs, dp36cwbs, dp48cwbs, dpallcwbs;
		dp24cwbs = dp36cwbs = dp48cwbs = dpallcwbs = new StringBuffer();
		String separtor0 = "'";
		String separtor1 = "',";

		for (DeliveryPercentForEmaildateDTO dpfe : dpfeList) {
			if (dpfe.getDeliverytime().getTime() <= (dpfe.getStarttime().getTime() + hour * 24)) {// 24小时投递率
				dp24++;
				dp24cwbs.append(separtor0).append(dpfe.getCwb()).append(separtor1);
			}
			if (dpfe.getDeliverytime().getTime() <= (dpfe.getStarttime().getTime() + hour * 36)) {// 36小时投递率
				dp36++;
				dp36cwbs.append(separtor0).append(dpfe.getCwb()).append(separtor1);
			}
			if (dpfe.getDeliverytime().getTime() <= (dpfe.getStarttime().getTime() + hour * 48)) {// 48小时投递率
				dp48++;
				dp48cwbs.append(separtor0).append(dpfe.getCwb()).append(separtor1);
			}
			// 超过48小时投递率
			dpall++;
			dpallcwbs.append(separtor0).append(dpfe.getCwb()).append(separtor1);
		}
		model.addAttribute("dp24", dp24);
		model.addAttribute("dp24cwbs", dp24cwbs.length() > 0 ? dp24cwbs.substring(0, dp24cwbs.length() - 1) : "");
		model.addAttribute("dp36", dp36);
		model.addAttribute("dp36cwbs", dp36cwbs.length() > 0 ? dp36cwbs.substring(0, dp36cwbs.length() - 1) : "");
		model.addAttribute("dp48", dp48);
		model.addAttribute("dp48cwbs", dp48cwbs.length() > 0 ? dp48cwbs.substring(0, dp48cwbs.length() - 1) : "");
		model.addAttribute("dpall", dpall);
		model.addAttribute("dpallcwbs", dpallcwbs.length() > 0 ? dpallcwbs.substring(0, dpallcwbs.length() - 1) : "");
		model.addAttribute("allNum", cList.size());

		return "logtoday/deliverypercent/emaildatepercent";

	}

	/**
	 * 按发货时间查看投递率
	 * 
	 * @param model
	 * @param customerid
	 * @param emaildateid
	 * @param flowordertype
	 * @return
	 */
	@RequestMapping("/deliveryPercentByemaildate")
	public String deliveryPercentByEmaildate(Model model, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") int flowordertype, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		List<String> customeridList = dataStatisticsService.getList(customerid);
		model.addAttribute("customeridStr", customeridList);
		if (begindate.length() == 0 || enddate.length() == 0) {
			model.addAttribute("page_obj", new Page(0, 1, 5));
			model.addAttribute("page", 1);
			return "logtoday/deliverypercent/percentbyemaildate";
		}

		final Map<Long, Long> dp4Map = new HashMap<Long, Long>();
		final Map<Long, Long> dp12Map = new HashMap<Long, Long>();
		final Map<Long, Long> dp24Map = new HashMap<Long, Long>();
		final Map<Long, Long> dp36Map = new HashMap<Long, Long>();
		final Map<Long, Long> dp72Map = new HashMap<Long, Long>();
		final Map<Long, Long> dpallMap = new HashMap<Long, Long>();

		String customerids = getStrings(customerid);
		List<JSONObject> cList = cwbDAO.getCwbsByEmailDate(customerids, begindate, enddate);

		final StringBuffer cwbs = new StringBuffer();
		final long hour = 1000 * 60 * 60;
		// final StringBuffer
		// dp4cwbs,dp12cwbs,dp24cwbs,dp36cwbs,dp48cwbs,dp72cwbs,dpallcwbs;
		// dp4cwbs=dp12cwbs=dp24cwbs=dp36cwbs=dp48cwbs=dpallcwbs=dp72cwbs=new
		// StringBuffer();
		// final String separtor0 = "'";
		// final String separtor1 = "',";
		String sql = "SELECT cd.customerid,cd.cwb,cd.emaildateid,ds.deliverytime,of.credate FROM `express_ops_cwb_detail` cd " + "LEFT JOIN `express_ops_delivery_state` ds ON cd.`cwb`=ds.`cwb` "
				+ "LEFT JOIN `express_ops_order_flow` of ON cd.`cwb`=of.`cwb` " + "WHERE cd.emaildate>='" + begindate + "' AND cd.emaildate<='" + enddate + "'  AND cd.state=1 AND ds.`state`=1 "
				+ "AND ds.`deliverystate`>0 AND ds.`deliverystate`<4 AND ds.gcaid>0 " + "AND of.flowordertype=" + flowordertype + " ORDER BY of.credate ASC ";
		jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					String separtor = "#";
					if (cwbs.indexOf(rs.getString("cwb") + separtor) == -1) {
						cwbs.append(rs.getString("cwb") + separtor);
						if (rs.getDate("deliverytime").getTime() <= (rs.getDate("credate").getTime() + hour * 4)) {// 4小时投递率
							// dp4cwbs.append(separtor0).append(rs.getString("cwb")).append(separtor1);
							if (dp4Map.get(rs.getLong("customerid")) == null) {
								dp4Map.put(rs.getLong("customerid"), 0l);
							}
							dp4Map.put(rs.getLong("customerid"), dp4Map.get(rs.getLong("customerid")) + 1l);
						}
						if (rs.getDate("deliverytime").getTime() <= (rs.getDate("credate").getTime() + hour * 4)) {// 12小时投递率
							// dp12cwbs.append(separtor0).append(rs.getString("cwb")).append(separtor1);
							if (dp12Map.get(rs.getLong("customerid")) == null) {
								dp12Map.put(rs.getLong("customerid"), 0l);
							}
							dp12Map.put(rs.getLong("customerid"), dp12Map.get(rs.getLong("customerid")) + 1l);
						}
						if (rs.getDate("deliverytime").getTime() <= (rs.getDate("credate").getTime() + hour * 4)) {// 24小时投递率
							// dp24cwbs.append(separtor0).append(rs.getString("cwb")).append(separtor1);
							if (dp24Map.get(rs.getLong("customerid")) == null) {
								dp24Map.put(rs.getLong("customerid"), 0l);
							}
							dp24Map.put(rs.getLong("customerid"), dp24Map.get(rs.getLong("customerid")) + 1l);
						}
						if (rs.getDate("deliverytime").getTime() <= (rs.getDate("credate").getTime() + hour * 4)) {// 36小时投递率
							// dp36cwbs.append(separtor0).append(rs.getString("cwb")).append(separtor1);
							if (dp36Map.get(rs.getLong("customerid")) == null) {
								dp36Map.put(rs.getLong("customerid"), 0l);
							}
							dp36Map.put(rs.getLong("customerid"), dp36Map.get(rs.getLong("customerid")) + 1l);
						}
						if (rs.getDate("deliverytime").getTime() <= (rs.getDate("credate").getTime() + hour * 4)) {// 72小时投递率
							// dp72cwbs.append(separtor0).append(rs.getString("cwb")).append(separtor1);
							if (dp72Map.get(rs.getLong("customerid")) == null) {
								dp72Map.put(rs.getLong("customerid"), 0l);
							}
							dp72Map.put(rs.getLong("customerid"), dp72Map.get(rs.getLong("customerid")) + 1l);
						}
						// 超过72小时投递率
						// dpallcwbs.append(separtor0).append(rs.getString("cwb")).append(separtor1);
						if (dpallMap.get(rs.getLong("customerid")) == null) {
							dpallMap.put(rs.getLong("customerid"), 0l);
						}
						dpallMap.put(rs.getLong("customerid"), dpallMap.get(rs.getLong("customerid")) + 1l);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		});

		// model.addAttribute("dp24cwbs",dp24cwbs.length()>0?dp24cwbs.substring(0,
		// dp24cwbs.length()-1):"");
		// model.addAttribute("dp36cwbs",dp36cwbs.length()>0?dp36cwbs.substring(0,
		// dp36cwbs.length()-1):"");
		// model.addAttribute("dp48cwbs",dp48cwbs.length()>0?dp48cwbs.substring(0,
		// dp48cwbs.length()-1):"");
		// model.addAttribute("dpallcwbs",dpallcwbs.length()>0?dpallcwbs.substring(0,
		// dpallcwbs.length()-1):"");
		model.addAttribute("cList", cList);
		model.addAttribute("custMap", customerDAO.getAllCustomersToMap());

		model.addAttribute("dp4Map", dp4Map);
		model.addAttribute("dp12Map", dp12Map);
		model.addAttribute("dp24Map", dp24Map);
		model.addAttribute("dp36Map", dp36Map);
		model.addAttribute("dp72Map", dp72Map);
		model.addAttribute("dpallMap", dpallMap);

		model.addAttribute("page_obj", new Page(cList.size(), 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);

		return "logtoday/deliverypercent/percentbyemaildate";

	}

	@RequestMapping("/show/{type}/{begindate}/{enddate}/{customerid}/{flowordertype}/{page}")
	public String show(Model model, @PathVariable("type") String type, @PathVariable("begindate") String begindate, @PathVariable("enddate") String enddate,
			@PathVariable("customerid") long customerid, @PathVariable("flowordertype") int flowordertype, @PathVariable("page") long page) {

		if ("all".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getCwbOrderByEmailDate(customerid + "", begindate, enddate, page);
			long cwborderCount = cwbDAO.getCwbOrderByEmailDateCount(customerid + "", begindate, enddate);
			model.addAttribute("cwborderList", cwborderList);
			model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		} else if ("tuotou".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getCwbsByEmailDateAndTuotou(customerid + "", begindate, enddate, page);
			long cwborderCount = cwbDAO.getCwbsByEmailDateAndTuotouCount(customerid + "", begindate, enddate);
			model.addAttribute("cwborderList", cwborderList);
			model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		} else if ("jushou".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getCwbsByEmailDateAndJushou(customerid + "", begindate, enddate, page);
			long cwborderCount = cwbDAO.getCwbsByEmailDateAndJushouCount(customerid + "", begindate, enddate);
			model.addAttribute("cwborderList", cwborderList);
			model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		} else if ("zhiliu".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getCwbsByEmailDateAndZhiliu(customerid + "", begindate, enddate, page);
			long cwborderCount = cwbDAO.getCwbsByEmailDateAndZhiliuCount(customerid + "", begindate, enddate);
			model.addAttribute("cwborderList", cwborderList);
			model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		} else if ("diushi".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getCwbsByEmailDateAndDiushi(customerid + "", begindate, enddate, page);
			long cwborderCount = cwbDAO.getCwbsByEmailDateAndDiushiCount(customerid + "", begindate, enddate);
			model.addAttribute("cwborderList", cwborderList);
			model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		} else if ("wujieguo".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getCwbsByEmailDateAndWujieguo(customerid + "", begindate, enddate, page);
			long cwborderCount = cwbDAO.getCwbsByEmailDateAndWujieguoCount(customerid + "", begindate, enddate);
			model.addAttribute("cwborderList", cwborderList);
			model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		} else {
			List<CwbOrder> cwborderList = new ArrayList<CwbOrder>();
			long cwborderCount = 0l;
			model.addAttribute("cwborderList", cwborderList);
			model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
		}
		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : branchDAO.getAllBranches()) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("branchMap", branchMap);
		model.addAttribute("customerMap", customerDAO.getAllCustomersToMap());
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));

		model.addAttribute("type", type);
		model.addAttribute("begindate", begindate);
		model.addAttribute("enddate", enddate);
		model.addAttribute("customerid", customerid);
		model.addAttribute("flowordertype", flowordertype);

		return "logtoday/deliverypercent/show";
	}

	@RequestMapping("/exportExcel")
	public void exportExcel(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "page", required = false, defaultValue = "0") long page, @RequestParam(value = "type", required = false, defaultValue = "") String type) {

		deliverypercentService.exportExcelMethod(response, request, customerid + "", begindate, enddate, page, type);

	}

	/**
	 * 按站点查看投递率
	 * 
	 * @param model
	 * @param customerid
	 * @param emaildateid
	 * @param flowordertype
	 * @return
	 */
	@RequestMapping("/deliveryPercentBybranch")
	public String deliveryPercentBybranch(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchid,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {

		Branch branch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		List<Branch> branchnameList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + ","
				+ BranchEnum.ZhongZhuan.getValue());
		List<Branch> kufangList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + ","
				+ BranchEnum.ZhongZhuan.getValue());

		if (branch.getSitetype() == BranchEnum.KuFang.getValue() || branch.getSitetype() == BranchEnum.TuiHuo.getValue() || branch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			if (kufangList.size() == 0) {
				kufangList.add(branch);
			} else {
				if (!dataStatisticsService.checkBranchRepeat(kufangList, branch)) {
					kufangList.add(branch);
				}
			}
		} else if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			if (branchnameList.size() == 0) {
				branchnameList.add(branch);
			} else {
				if (!dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
					branchnameList.add(branch);
				}
			}
		}
		List<String> currentBranchidlist = dataStatisticsService.getList(branchid);
		model.addAttribute("branchList", branchnameList);
		model.addAttribute("currentBranchidStr", currentBranchidlist);
		Map<Long, String> branchNameMap = new HashMap<Long, String>();
		String branchids = "";

		if (branchnameList != null && branchnameList.size() > 0) {
			for (Branch bran : branchnameList) {
				branchNameMap.put(bran.getBranchid(), bran.getBranchname());
				if (branchid.length == 0) {
					branchids += bran.getBranchid() + ",";
				}
			}
		} else {
			model.addAttribute("page_obj", new Page(0, 1, 5));
			model.addAttribute("page", 1);
			return "logtoday/deliverypercent/percentbybranch";
		}
		if (branchid.length > 0) {
			branchids = getStrings(branchid);
		} else {
			branchids = branchids.length() > 0 ? branchids.substring(0, branchids.length() - 1) : "";
		}
		if (begindate.length() == 0 || enddate.length() == 0) {
			model.addAttribute("page_obj", new Page(0, 1, 5));
			model.addAttribute("page", 1);
			return "logtoday/deliverypercent/percentbybranch";
		}

		// 以下是核心代码:
		// 发货数量、妥投、拒收、滞留、丢失、无结果的存储
		final Map<Long, Long> allMap = new HashMap<Long, Long>();
		final Map<Long, BigDecimal> allMyMap = new HashMap<Long, BigDecimal>();
		final Map<Long, Long> tuotouMap = new HashMap<Long, Long>();
		final Map<Long, BigDecimal> tuotouMyMap = new HashMap<Long, BigDecimal>();
		final Map<Long, Long> jushouMap = new HashMap<Long, Long>();
		final Map<Long, BigDecimal> jushouMyMap = new HashMap<Long, BigDecimal>();
		final Map<Long, Long> zhiliuMap = new HashMap<Long, Long>();
		final Map<Long, BigDecimal> zhiliuMyMap = new HashMap<Long, BigDecimal>();
		final Map<Long, Long> diushiMap = new HashMap<Long, Long>();
		final Map<Long, BigDecimal> diushiMyMap = new HashMap<Long, BigDecimal>();
		final Map<Long, Long> wujieguoMap = new HashMap<Long, Long>();
		final Map<Long, BigDecimal> wujieguoMyMap = new HashMap<Long, BigDecimal>();

		// 4、12、24、36、72、超过72小时的投递率存储
		final Map<Long, Long> dp4Map = new HashMap<Long, Long>();
		final Map<Long, Long> dp12Map = new HashMap<Long, Long>();
		final Map<Long, Long> dp24Map = new HashMap<Long, Long>();
		final Map<Long, Long> dp36Map = new HashMap<Long, Long>();
		final Map<Long, Long> dp72Map = new HashMap<Long, Long>();
		final Map<Long, Long> dpallMap = new HashMap<Long, Long>();

		// 用于滤重的订单号存储
		final StringBuffer cwbs = new StringBuffer();
		final StringBuffer cwbs2 = new StringBuffer();
		final long hour = 1000 * 60 * 60;
		// final StringBuffer
		// dp4cwbs,dp12cwbs,dp24cwbs,dp36cwbs,dp48cwbs,dp72cwbs,dpallcwbs;
		// dp4cwbs=dp12cwbs=dp24cwbs=dp36cwbs=dp48cwbs=dpallcwbs=dp72cwbs=new
		// StringBuffer();
		// final String separtor0 = "'";
		// final String separtor1 = "',";

		// 这个sql是按到站时间查询：到站、妥投、拒收、滞留、丢失、无结果的数据，用数据库执行的循环遍历出结果
		String sql1 = "SELECT cd.cwb,cd.deliverybranchid,cd.deliverystate,of.credate,cd.flowordertype,of.branchid,cd.receivablefee FROM `express_ops_cwb_detail` cd "
				+ " LEFT JOIN `express_ops_order_flow` of ON cd.`cwb`=of.`cwb` " + " WHERE of.credate>='" + begindate + "' AND of.credate<='" + enddate + "' AND cd.state=1 "
				+ " AND  of.flowordertype in(7,8) AND of.branchid in(" + branchids + ") ORDER BY of.credate ASC ";
		jdbcTemplate.query(new StreamingStatementCreator(sql1), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					String separtor = "#";
					if (cwbs.indexOf(rs.getString("cwb") + separtor) == -1) {
						cwbs.append(rs.getString("cwb") + separtor);
						// 全部
						if (allMap.get(rs.getLong("branchid")) == null) {
							allMap.put(rs.getLong("branchid"), 0l);
							allMyMap.put(rs.getLong("branchid"), BigDecimal.ZERO);
						}
						allMap.put(rs.getLong("branchid"), allMap.get(rs.getLong("branchid")) + 1l);
						allMyMap.put(rs.getLong("branchid"), allMyMap.get(rs.getLong("branchid")).add(rs.getBigDecimal("receivablefee")));

						if ((rs.getLong("deliverystate") == 1 || rs.getLong("deliverystate") == 2 || rs.getLong("deliverystate") == 3)
								&& (rs.getLong("flowordertype") == 35 || rs.getLong("flowordertype") == 36)) {// 妥投
							if (tuotouMap.get(rs.getLong("deliverybranchid")) == null) {
								tuotouMap.put(rs.getLong("deliverybranchid"), 0l);
								tuotouMyMap.put(rs.getLong("deliverybranchid"), BigDecimal.ZERO);
							}
							tuotouMap.put(rs.getLong("deliverybranchid"), tuotouMap.get(rs.getLong("deliverybranchid")) + 1l);
							tuotouMyMap.put(rs.getLong("deliverybranchid"), tuotouMyMap.get(rs.getLong("deliverybranchid")).add(rs.getBigDecimal("receivablefee")));
						}
						if ((rs.getLong("deliverystate") == 4 || rs.getLong("deliverystate") == 5 || rs.getLong("deliverystate") == 7)
								&& (rs.getLong("flowordertype") == 35 || rs.getLong("flowordertype") == 36)) {// 拒收
							if (jushouMap.get(rs.getLong("deliverybranchid")) == null) {
								jushouMap.put(rs.getLong("deliverybranchid"), 0l);
								jushouMyMap.put(rs.getLong("deliverybranchid"), BigDecimal.ZERO);
							}
							jushouMap.put(rs.getLong("deliverybranchid"), jushouMap.get(rs.getLong("deliverybranchid")) + 1l);
							jushouMyMap.put(rs.getLong("deliverybranchid"), jushouMyMap.get(rs.getLong("deliverybranchid")).add(rs.getBigDecimal("receivablefee")));
						}
						if ((rs.getLong("deliverystate") == 6) && (rs.getLong("flowordertype") == 35 || rs.getLong("flowordertype") == 36)) {// 滞留
							if (zhiliuMap.get(rs.getLong("deliverybranchid")) == null) {
								zhiliuMap.put(rs.getLong("deliverybranchid"), 0l);
								zhiliuMyMap.put(rs.getLong("deliverybranchid"), BigDecimal.ZERO);
							}
							zhiliuMap.put(rs.getLong("deliverybranchid"), zhiliuMap.get(rs.getLong("deliverybranchid")) + 1l);
							zhiliuMyMap.put(rs.getLong("deliverybranchid"), zhiliuMyMap.get(rs.getLong("deliverybranchid")).add(rs.getBigDecimal("receivablefee")));
						}
						if ((rs.getLong("deliverystate") == 8) && (rs.getLong("flowordertype") == 35 || rs.getLong("flowordertype") == 36)) {// 丢失
							if (diushiMap.get(rs.getLong("deliverybranchid")) == null) {
								diushiMap.put(rs.getLong("deliverybranchid"), 0l);
								diushiMyMap.put(rs.getLong("deliverybranchid"), BigDecimal.ZERO);
							}
							diushiMap.put(rs.getLong("deliverybranchid"), diushiMap.get(rs.getLong("deliverybranchid")) + 1l);
							diushiMyMap.put(rs.getLong("deliverybranchid"), diushiMyMap.get(rs.getLong("deliverybranchid")).add(rs.getBigDecimal("receivablefee")));
						}
						if (rs.getLong("flowordertype") != 35 && rs.getLong("flowordertype") != 36) {// 无结果
							if (wujieguoMap.get(rs.getLong("branchid")) == null) {
								wujieguoMap.put(rs.getLong("branchid"), 0l);
								wujieguoMyMap.put(rs.getLong("branchid"), BigDecimal.ZERO);
							}
							wujieguoMap.put(rs.getLong("branchid"), wujieguoMap.get(rs.getLong("branchid")) + 1l);
							wujieguoMyMap.put(rs.getLong("branchid"), wujieguoMyMap.get(rs.getLong("branchid")).add(rs.getBigDecimal("receivablefee")));
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		});
		// 这个sql是查询：4、12、24、36、72、超过72小时的投递率
		String sql = "SELECT cd.cwb,ds.deliverybranchid,ds.deliverytime,of.credate FROM `express_ops_cwb_detail` cd " + "LEFT JOIN `express_ops_delivery_state` ds ON cd.`cwb`=ds.`cwb` "
				+ "LEFT JOIN `express_ops_order_flow` of ON cd.`cwb`=of.`cwb` " + "WHERE of.credate>='" + begindate + "' AND of.credate<='" + enddate + "'  AND cd.state=1 AND ds.`state`=1 "
				+ "AND ds.`deliverystate`>0 AND ds.`deliverystate`<4 AND ds.gcaid>0 " + "AND of.flowordertype in(7,8) AND of.branchid in(" + branchids + ") ORDER BY of.credate ASC ";
		jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					String separtor = "#";
					if (cwbs2.indexOf(rs.getString("cwb") + separtor) == -1) {
						cwbs2.append(rs.getString("cwb") + separtor);
						if (rs.getDate("deliverytime").getTime() <= (rs.getDate("credate").getTime() + hour * 4)) {// 4小时投递率
							if (dp4Map.get(rs.getLong("deliverybranchid")) == null) {
								dp4Map.put(rs.getLong("deliverybranchid"), 0l);
							}
							dp4Map.put(rs.getLong("deliverybranchid"), dp4Map.get(rs.getLong("deliverybranchid")) + 1l);
						}
						if (rs.getDate("deliverytime").getTime() <= (rs.getDate("credate").getTime() + hour * 4)) {// 12小时投递率
							if (dp12Map.get(rs.getLong("deliverybranchid")) == null) {
								dp12Map.put(rs.getLong("deliverybranchid"), 0l);
							}
							dp12Map.put(rs.getLong("deliverybranchid"), dp12Map.get(rs.getLong("deliverybranchid")) + 1l);
						}
						if (rs.getDate("deliverytime").getTime() <= (rs.getDate("credate").getTime() + hour * 4)) {// 24小时投递率
							if (dp24Map.get(rs.getLong("deliverybranchid")) == null) {
								dp24Map.put(rs.getLong("deliverybranchid"), 0l);
							}
							dp24Map.put(rs.getLong("deliverybranchid"), dp24Map.get(rs.getLong("deliverybranchid")) + 1l);
						}
						if (rs.getDate("deliverytime").getTime() <= (rs.getDate("credate").getTime() + hour * 4)) {// 36小时投递率
							if (dp36Map.get(rs.getLong("deliverybranchid")) == null) {
								dp36Map.put(rs.getLong("deliverybranchid"), 0l);
							}
							dp36Map.put(rs.getLong("deliverybranchid"), dp36Map.get(rs.getLong("deliverybranchid")) + 1l);
						}
						if (rs.getDate("deliverytime").getTime() <= (rs.getDate("credate").getTime() + hour * 4)) {// 72小时投递率
							if (dp72Map.get(rs.getLong("deliverybranchid")) == null) {
								dp72Map.put(rs.getLong("deliverybranchid"), 0l);
							}
							dp72Map.put(rs.getLong("deliverybranchid"), dp72Map.get(rs.getLong("deliverybranchid")) + 1l);
						}
						if (dpallMap.get(rs.getLong("deliverybranchid")) == null) {
							dpallMap.put(rs.getLong("deliverybranchid"), 0l);
						}
						dpallMap.put(rs.getLong("deliverybranchid"), dpallMap.get(rs.getLong("deliverybranchid")) + 1l);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		});

		model.addAttribute("allMap", allMap);
		model.addAttribute("allMyMap", allMyMap);
		model.addAttribute("tuotouMap", tuotouMap);
		model.addAttribute("tuotouMyMap", tuotouMyMap);
		model.addAttribute("jushouMap", jushouMap);
		model.addAttribute("jushouMyMap", jushouMyMap);
		model.addAttribute("zhiliuMap", zhiliuMap);
		model.addAttribute("zhiliuMyMap", zhiliuMyMap);
		model.addAttribute("diushiMap", diushiMap);
		model.addAttribute("diushiMyMap", diushiMyMap);
		model.addAttribute("wujieguoMap", wujieguoMap);
		model.addAttribute("wujieguoMyMap", wujieguoMyMap);
		model.addAttribute("branchNameMap", branchNameMap);

		model.addAttribute("dp4Map", dp4Map);
		model.addAttribute("dp12Map", dp12Map);
		model.addAttribute("dp24Map", dp24Map);
		model.addAttribute("dp36Map", dp36Map);
		model.addAttribute("dp72Map", dp72Map);
		model.addAttribute("dpallMap", dpallMap);

		model.addAttribute("page_obj", new Page(allMap.size(), 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);

		return "logtoday/deliverypercent/percentbybranch";

	}

	public String getStrings(String[] strArr) {
		String strs = "";
		if (strArr.length > 0) {
			for (String str : strArr) {
				strs += str + ",";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public List<String> getList(String[] strArr) {
		List<String> strList = new ArrayList<String>();
		if (strArr != null && strArr.length > 0) {
			for (String str : strArr) {
				strList.add(str);
			}
		}
		return strList;
	}
	//
	// public static void main(String[] args) {
	// int a =0;
	// System.out.println("开始:"+System.currentTimeMillis());
	// for (int i = 0; i < 1000; i++) {
	// for(int j = 0; j< 10000; j++){
	// if(j%2==0){
	//
	// a++;
	// }
	// }
	// }
	// System.out.println(a);
	// System.out.println("结束:"+System.currentTimeMillis());
	// }
}
