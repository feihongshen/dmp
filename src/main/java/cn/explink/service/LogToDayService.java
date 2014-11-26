package cn.explink.service;

import java.math.BigDecimal;
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
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryPercentDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.LogTodayDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchTodayLog;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryPercent;
import cn.explink.domain.EmailDate;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.logdto.TodayArrivalDTO;
import cn.explink.domain.logdto.TodayDeliveryDTO;
import cn.explink.domain.logdto.TodayFundsDTO;
import cn.explink.domain.logdto.TodayHuiZongDTO;
import cn.explink.domain.logdto.TodayStockDTO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryPercentTypeEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateDayUtil;
import cn.explink.util.Page;

@Component
@DependsOn({ "systemInstallDAO" })
public class LogToDayService implements SystemConfigChangeListner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	LogTodayDAO logTodayDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	DeliveryPercentDAO deliveryPercentDAO;
	@Autowired
	LogTodayDAO todayDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	SystemInstallDAO systemInstallDAO;

	@Autowired
	CamelContext camelContext;

	@PostConstruct
	public void init() throws Exception {
		final SystemInstall siteDayLogTime = systemInstallDAO.getSystemInstallByName("siteDayLogTime");
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
				from("timer://foo?fixedRate=true&period=" + period + "&delay=" + delay).to("bean:logToDayService?method=dailyDayLogGenerate").routeId("站点定时任务生成");
			}
		});
	}

	/**
	 * 库房应入库的批次数 需求 查看当前所有未到货的批次
	 * 
	 * @param branchid
	 *            是当前所属机构的id ，如果不传则获得所有的 为0则获取所有库房的批次
	 * @return
	 */
	public List<EmailDate> getNeedEmaildate(long branchid) {
		if (branchid == 0) {
			return emailDateDAO.getEmailDateAllForState(0);
		}
		return emailDateDAO.getEmailDateByBranchidForState(branchid, 0);
	}

	/**
	 * 工具方法，筛选出当前批次集合中的这个供货商的所有批次
	 * 
	 * @param edList
	 * @param customer
	 * @return
	 */
	public List<EmailDate> getEmailDateListByCustomer(List<EmailDate> edList, Customer customer) {
		List<EmailDate> edListByCustomer = new ArrayList<EmailDate>();
		for (EmailDate ed : edList) {
			if (ed.getCustomerid() == customer.getCustomerid()) {
				edListByCustomer.add(ed);
			}
		}

		return edListByCustomer;
	}

	/**
	 * 获得所有供货商对应的应入库批次与应入库合计单数
	 * 
	 * @param branchid
	 *            指定的库房
	 * @param customerList
	 *            供货商列表
	 * @return Map<供货商id,Map<标识,数量与金额>>
	 */
	public Map<Long, Map<String, BigDecimal>> getNeedEmaildateDetailGroupByCustomer(long branchid, List<Customer> customerList) {

		Map<Long, Map<String, BigDecimal>> reNeedEmaildateGroupByCustomer = new HashMap<Long, Map<String, BigDecimal>>();
		List<EmailDate> edList = this.getNeedEmaildate(branchid);
		for (Customer customer : customerList) {

			List<EmailDate> edListByCustomer = getEmailDateListByCustomer(edList, customer);

			BigDecimal num = BigDecimal.ZERO;
			BigDecimal receivablefee = BigDecimal.ZERO;
			BigDecimal paybackfee = BigDecimal.ZERO;
			for (EmailDate ed : edListByCustomer) {
				BigDecimal[] cbe = cwbDAO.getCwbByEmailDateId(ed.getEmaildateid());
				num = num.add(cbe[0]);
				receivablefee = receivablefee.add(cbe[1]);
				paybackfee = paybackfee.add(cbe[2]);
			}

			Map<String, BigDecimal> reNeedEmaildate = new HashMap<String, BigDecimal>();
			reNeedEmaildate.put("emaildatenum", BigDecimal.valueOf(edListByCustomer.size()));
			reNeedEmaildate.put("num", num);
			reNeedEmaildate.put("receivablefee", receivablefee);
			reNeedEmaildate.put("paybackfee", paybackfee);

			reNeedEmaildateGroupByCustomer.put(customer.getCustomerid(), reNeedEmaildate);
		}

		return reNeedEmaildateGroupByCustomer;

	}

	// =======================站点日报方法============begin===========
	public TodayArrivalDTO getTodayArrivalDTO(Branch branch, Date startDate, Date endDate) {
		TodayArrivalDTO todayArrivalDTO = new TodayArrivalDTO();
		// 今日已出库：今日库房已经进行出库扫描到本站的数据；
		// （此数据里应该指今日新从库房扫描出库的订单，不包含他站到货后转给库房再次扫描给本站的，不包含昨日少货今日扫描给本站的）
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		todayArrivalDTO.setJinriyichuku(logTodayDAO.getTodayWeidaohuobyNextbranchid(branch.getBranchid(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate)));
		long zongdaozhan = logTodayDAO
				.getTodaybyBranchid(branch.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));

		long lousaoDaohuo = logTodayDAO.getLousaoyidaozhanCount(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branch.getBranchid(), simpleDateFormat.format(startDate));
		// 今日到货
		todayArrivalDTO.setYidaohuo(zongdaozhan - lousaoDaohuo);
		// 漏扫到站
		todayArrivalDTO.setJinri_lousaodaozhan(lousaoDaohuo);

		// 到错货：非本站货，但是本站扫描到货了
		todayArrivalDTO.setDaocuohuo(logTodayDAO.getTodaybyBranchid(branch.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate)));

		// 他站到货：今日该到本站，但是在其他站扫描到货了?
		todayArrivalDTO.setTazhandaohuo(logTodayDAO.getTodaybyOrtherBranchid(branch.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate)));

		// 少货：今日批次到本站，但是批次里未到本站的 白天暂时不统计，都设为0
		todayArrivalDTO.setShaohuo(0);
		// 未到货：今日批次到本站，但是批次里未到本站的
		todayArrivalDTO.setWeidaohuo(logTodayDAO.getTodayWeidaohuobyNextbranchidIsnowCount(branch.getBranchid(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue()));
		return todayArrivalDTO;
	}

	public TodayDeliveryDTO getTodayDeliveryDTO(Branch branch, List<JSONObject> peisongchenggongList, Date startDate, Date endDate) {
		TodayDeliveryDTO todayDeliveryDTO = new TodayDeliveryDTO();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 到站未领
		todayDeliveryDTO.setToudi_daozhanweiling(logTodayDAO.getCountbyDedailt(branch.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
				FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()));
		// 今日领货
		todayDeliveryDTO.setToday_fankui_linghuo(logTodayDAO.getTodaybyBranchid(branch.getBranchid(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate)));
		// 昨日未归班
		// todayDeliveryDTO.setToday_fankui_zuoriweiguiban(deliveryStateDAO.getCountByBranchidAndGcaidAndCredate(branch.getBranchid(),
		// yesterday+" 00:00:00",
		// yesterday+" 23:59:59" ));

		// 配送成功单数、应收金额、应退金额
		if (peisongchenggongList != null) {
			// 配送成功单数
			todayDeliveryDTO.setToday_fankui_peisongchenggong_count(peisongchenggongList.get(0).getLong("num"));
			// 配送成功金额
			todayDeliveryDTO.setToday_fankui_peisongchenggong_money(new BigDecimal(peisongchenggongList.get(0).getString("receivablefee")));
		}
		// 拒收
		todayDeliveryDTO.setToday_fankui_jushou(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateAndOrderType(branch.getBranchid(), DeliveryStateEnum.JuShou.getValue(),
				CwbOrderTypeIdEnum.Peisong.getValue(), simpleDateFormat.format(startDate), simpleDateFormat.format(endDate)));
		// 部分拒收
		todayDeliveryDTO.setToday_fankui_bufenjushou(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branch.getBranchid(), DeliveryStateEnum.BuFenTuiHuo.getValue(),
				simpleDateFormat.format(startDate), simpleDateFormat.format(endDate)));
		// 今日滞留
		todayDeliveryDTO.setToday_fankui_leijizhiliu(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateByJinri(FlowOrderTypeEnum.YiShenHe.getValue(), branch.getBranchid(),
				DeliveryStateEnum.FenZhanZhiLiu.getValue(), simpleDateFormat.format(startDate), simpleDateFormat.format(endDate)));

		// 中转
		todayDeliveryDTO.setToday_fankui_zhobgzhuan(logTodayDAO.getTodaybyBranchid(branch.getBranchid(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate)));
		// 上门退成功单数、应退金额
		List<JSONObject> shangMenTuiChengGongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(),
				DeliveryStateEnum.ShangMenTuiChengGong.getValue(), simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
		if (shangMenTuiChengGongList != null) {
			// 上门退成功单数
			todayDeliveryDTO.setToday_fankui_shangmentuichenggong_count(shangMenTuiChengGongList.get(0).getLong("num"));
			// 上门退成功应退金额
			todayDeliveryDTO.setToday_fankui_shangmentuichenggong_money(new BigDecimal(shangMenTuiChengGongList.get(0).getString("paybackfee")));
		}
		// 上门退拒退
		todayDeliveryDTO.setToday_fankui_shangmentuijutui(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branch.getBranchid(), DeliveryStateEnum.ShangMenJuTui.getValue(),
				simpleDateFormat.format(startDate), simpleDateFormat.format(endDate)));
		// 上门换成功单数、应收金额、应退金额
		List<JSONObject> shangmenhuanchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(),
				DeliveryStateEnum.ShangMenHuanChengGong.getValue(), simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
		if (shangmenhuanchenggongList != null) {
			// 上门换成功单数
			todayDeliveryDTO.setToday_fankui_shangmenhuanchenggong_count(shangmenhuanchenggongList.get(0).getLong("num"));
			// 上门换成功应收金额
			todayDeliveryDTO.setToday_fankui_shangmenhuanchenggong_yingshou_money(new BigDecimal(shangmenhuanchenggongList.get(0).getString("receivablefee")));
			// 上门换成功应退金额
			todayDeliveryDTO.setToday_fankui_shangmenhuanchenggong_yingtui_money(new BigDecimal(shangmenhuanchenggongList.get(0).getString("paybackfee")));
		}
		// 上门换拒换
		todayDeliveryDTO.setToday_fankui_shangmenhuanjuhuan(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateAndOrderType(branch.getBranchid(),
				DeliveryStateEnum.JuShou.getValue(), CwbOrderTypeIdEnum.Shangmenhuan.getValue(), simpleDateFormat.format(startDate), simpleDateFormat.format(endDate)));
		// 丢失破损
		todayDeliveryDTO.setToday_fankui_diushi(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branch.getBranchid(), DeliveryStateEnum.HuoWuDiuShi.getValue(),
				simpleDateFormat.format(startDate), simpleDateFormat.format(endDate)));
		// 反馈合计单数、应收金额、应退金额
		List<JSONObject> fankuilist = deliveryStateDAO.getIsFankuiByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate));
		if (fankuilist != null) {
			// 反馈合计单数
			todayDeliveryDTO.setToday_fankui_heji_count(fankuilist.get(0).getLong("num"));
			// 反馈合计应收金额
			todayDeliveryDTO.setToday_fankui_heji_yingshou_money(new BigDecimal(fankuilist.get(0).getString("receivablefee")));
			// 反馈合计应退金额
			todayDeliveryDTO.setToday_fankui_heji_yingtui_money(new BigDecimal(fankuilist.get(0).getString("paybackfee")));
		}

		// 未反馈合计单数、应收金额、应退金额
		List<JSONObject> notfankuilist = deliveryStateDAO.getIsNotFankuiByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate), FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		if (notfankuilist != null) {
			// 未反馈合计单数
			todayDeliveryDTO.setToday_weifankui_heji_count(notfankuilist.get(0).getLong("num"));
			// 未反馈合计应收金额
			todayDeliveryDTO.setToday_weifankui_heji_yingshou_money(new BigDecimal(notfankuilist.get(0).getString("receivablefee")));
			// 未反馈合计应应退金额
			todayDeliveryDTO.setToday_weifankui_heji_yingtui_money(new BigDecimal(notfankuilist.get(0).getString("paybackfee")));

		}
		// 反馈.未归班的订单
		List<JSONObject> fankuiweiguibanlist = deliveryStateDAO.getFankuiNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate));
		if (fankuiweiguibanlist != null) {
			// 反馈.未归班合计单数
			todayDeliveryDTO.setToday_fankuiweiguiban_heji_count(fankuiweiguibanlist.get(0).getLong("num"));
			// 反馈.未归班合计应收金额
			todayDeliveryDTO.setToday_fankuiweiguiban_heji_yingshou_money(new BigDecimal(fankuiweiguibanlist.get(0).getString("receivablefee")));
			// 反馈.未归班合计应应退金额
			todayDeliveryDTO.setToday_fankuiweiguiban_heji_yingtui_money(new BigDecimal(fankuiweiguibanlist.get(0).getString("paybackfee")));

		}

		// 昨日滞留，数据时动态的
		todayDeliveryDTO.setZuori_fankui_leijizhiliu(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateByZuori(FlowOrderTypeEnum.YiShenHe.getValue(), branch.getBranchid(),
				DeliveryStateEnum.FenZhanZhiLiu.getValue(), simpleDateFormat.format(startDate)));

		return todayDeliveryDTO;
	}

	public TodayFundsDTO getTodayFundsDTO(Branch branch, Date startDate, Date endDate) {
		TodayFundsDTO todayFundsDTO = new TodayFundsDTO();
		// 配送成功单数、现金、pos、支票
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<JSONObject> peisongchenggongFunsList = deliveryStateDAO.getFunsByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(), DeliveryStateEnum.PeiSongChengGong.getValue(),
				simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
		if (peisongchenggongFunsList != null) {
			// 配送成功单数
			todayFundsDTO.setPeisongchenggong(peisongchenggongFunsList.get(0).getLong("num"));
			// 配送成功中现金所占的总金额
			todayFundsDTO.setPeisongchenggong_cash_amount(new BigDecimal(peisongchenggongFunsList.get(0).getString("cash")));
			// 所有货物中pos刷卡所占的总金额
			todayFundsDTO.setPeisongchenggong_pos_amount(new BigDecimal(peisongchenggongFunsList.get(0).getString("pos")));
			// 所有货物中支票所占的总金额
			todayFundsDTO.setPeisongchenggong_checkfee_amount(new BigDecimal(peisongchenggongFunsList.get(0).getString("checkfee")));
		}
		// 上门退成功票数、应付金额
		List<JSONObject> shangmentuichenggongList = deliveryStateDAO.getFunsShangmentuiByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(),
				DeliveryStateEnum.ShangMenTuiChengGong.getValue(), simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
		if (shangmentuichenggongList != null) {
			// 上门退成功
			todayFundsDTO.setShangmentuichenggong(shangmentuichenggongList.get(0).getLong("num"));
			// 所有货物中现金所占的总金额
			todayFundsDTO.setShangmentuichenggong_cash_amount(new BigDecimal(shangmentuichenggongList.get(0).getString("paybackfee")));

		}
		// 上门换成功单数、应收金额、应退金额
		List<JSONObject> shangmenhuanchenggongList = deliveryStateDAO.getFunsShangmentuiByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(),
				DeliveryStateEnum.ShangMenHuanChengGong.getValue(), simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
		if (shangmenhuanchenggongList != null) {
			// 上门换成功单数
			todayFundsDTO.setShangmenhuanchenggong(shangmenhuanchenggongList.get(0).getLong("num"));
			// 上门换成功应收金额
			todayFundsDTO.setShangmenhuanchenggong_yingshou_amount(new BigDecimal(shangmenhuanchenggongList.get(0).getString("receivablefee")));
			// 上门换成功应退金额
			todayFundsDTO.setShangmenhuanchenggong_yingtui_amount(new BigDecimal(shangmenhuanchenggongList.get(0).getString("paybackfee")));
		}
		// 已上缴中现金、pos、支票
		List<JSONObject> yishangjiaoList = deliveryStateDAO.getPayUpByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate));
		if (yishangjiaoList != null) {
			// 已上缴中现金所占的总金额
			todayFundsDTO.setShijiaokuan_cash_amount(new BigDecimal(yishangjiaoList.get(0).getString("cash")));
			// 已上缴中pos刷卡所占的总金额
			todayFundsDTO.setShijiaokuan_pos_amount(new BigDecimal(yishangjiaoList.get(0).getString("pos")));
			// 已上缴中支票所占的总金额
			todayFundsDTO.setShijiaokuan_checkfee_amount(new BigDecimal(yishangjiaoList.get(0).getString("checkfee")));
		}
		return todayFundsDTO;
	}

	public TodayStockDTO getTodayStockDTO(Branch branch, Date startDate, Date endDate) {
		TodayStockDTO todayStockDTO = new TodayStockDTO();

		// 今日到货
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long zongdaozhan = logTodayDAO
				.getTodaybyBranchid(branch.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
		todayStockDTO.setJinridaohuo(zongdaozhan);

		// 今日妥投 在外层封装
		// todayStockDTO.setJinrituodou(peisongchenggongList.get(0).getLong("num"));
		// 今日中转出库

		long jinrizhongzhuangchuku = logTodayDAO.getTodaybyBranchid(branch.getBranchid(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate));
		// 今日中转出库
		todayStockDTO.setJinrizhongzhuanchuku(jinrizhongzhuangchuku);
		// 今日退货出库
		long jinrituihuochuku = logTodayDAO.getTodaybyBranchid(branch.getBranchid(), FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
		// 今日退货出库
		todayStockDTO.setJinrituihuochuku(jinrituihuochuku);
		// 库存 = 今日到站扫描+今日到错货+上门退成功+上门换成功+滞留 +拒收+部分拒收(当前状态)
		// 今日库存 = 昨日库存 + 今日到站扫描+今日到错货
		// List<JSONObject> jinrukucunList =
		// deliveryStateDAO.getCountbyKucun(branch.getBranchid());
		// if(jinrukucunList != null){
		// todayStockDTO.setJinrikucun(jinrukucunList.get(0).getLong("num"));
		// }
		// 拒收的库存
		todayStockDTO.setJushou_kuicun(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredate(FlowOrderTypeEnum.YiShenHe.getValue(), branch.getBranchid(), DeliveryStateEnum.JuShou.getValue()));
		// 滞留的库存
		todayStockDTO.setZhiliu_kuicun(deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredate(FlowOrderTypeEnum.YiShenHe.getValue(), branch.getBranchid(),
				DeliveryStateEnum.FenZhanZhiLiu.getValue()));
		// 未归班的库存
		todayStockDTO.setWeiguiban_kuicun(deliveryStateDAO.getIsNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid()));
		return todayStockDTO;
	}

	public TodayHuiZongDTO getTodayHuiZongDTO(Branch branch, String yesterday, List<JSONObject> peisongchenggongList, Date startDate, Date endDate) {
		TodayHuiZongDTO todayHuiZongDTO = new TodayHuiZongDTO();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> jinridaohuolist = logTodayDAO.getOrderBybranchid(branch.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate));
		if (jinridaohuolist != null) {
			// 今日到货
			long daohuo = jinridaohuolist.size();

			BigDecimal receivablefee = BigDecimal.ZERO;
			BigDecimal paybackfee = BigDecimal.ZERO;
			for (String cwb : jinridaohuolist) {
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					receivablefee = receivablefee.add(cwbOrder.getReceivablefee());
					paybackfee = paybackfee.add(cwbOrder.getPaybackfee());
				} else {
					daohuo = daohuo - 1;
				}
			}
			todayHuiZongDTO.setJinridaohuo_count(daohuo);
			todayHuiZongDTO.setJinridaohuo_money(receivablefee.subtract(paybackfee));
		}

		// 今日应投= 今日到站 + 昨日库存
		List<String> kufangchukuList = logTodayDAO.getOrderBybranchid(branch.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate));
		if (kufangchukuList != null) {
			BigDecimal receivablefee = BigDecimal.ZERO;
			BigDecimal paybackfee = BigDecimal.ZERO;
			long yingtoucount = kufangchukuList.size();
			for (String cwb : kufangchukuList) {
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					receivablefee = receivablefee.add(cwbOrder == null ? BigDecimal.ZERO : cwbOrder.getReceivablefee());
					paybackfee = paybackfee.add(cwbOrder == null ? BigDecimal.ZERO : cwbOrder.getPaybackfee());
				} else {
					yingtoucount = yingtoucount - 1;
				}
			}
			todayHuiZongDTO.setJinriyingtou_count(yingtoucount);
			todayHuiZongDTO.setJinriyingtou_money(receivablefee.subtract(paybackfee));
		} else {
			todayHuiZongDTO.setJinriyingtou_count(0);
			todayHuiZongDTO.setJinriyingtou_money(BigDecimal.ZERO);
		}

		// 今日妥投 在外层封装
		// todayHuiZongDTO.setJinrishoukuan_count(jinrishoukuan_count);
		// todayHuiZongDTO.setJinrishoukuan_money(jinrishoukuan_money);

		// 今日出库 中转+退货
		List<String> zhandianchukuList = logTodayDAO.getOrderBybranchid(branch.getBranchid(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate));
		long chukuCount = 0;
		BigDecimal chukuMoney = BigDecimal.ZERO;
		if (zhandianchukuList != null) {
			// 今日到货
			BigDecimal receivablefee = BigDecimal.ZERO;
			BigDecimal paybackfee = BigDecimal.ZERO;
			long zhandianchuku = zhandianchukuList.size();
			for (String cwb : zhandianchukuList) {
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					receivablefee = receivablefee.add(cwbOrder.getReceivablefee());
					paybackfee = paybackfee.add(cwbOrder.getPaybackfee());
				} else {
					zhandianchuku = zhandianchuku - 1;
				}
			}
			chukuCount = chukuCount + zhandianchuku;
			chukuMoney = chukuMoney.add(receivablefee.subtract(paybackfee));
		}
		List<String> zhandiantuihuochukuList = logTodayDAO.getOrderBybranchid(branch.getBranchid(), FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate));
		if (zhandiantuihuochukuList != null) {
			// 今日到货
			BigDecimal receivablefee = BigDecimal.ZERO;
			BigDecimal paybackfee = BigDecimal.ZERO;
			long zhandiantuihuochuku = zhandiantuihuochukuList.size();
			for (String cwb : zhandiantuihuochukuList) {
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					receivablefee = receivablefee.add(cwbOrder.getReceivablefee());
					paybackfee = paybackfee.add(cwbOrder.getPaybackfee());
				} else {
					zhandiantuihuochuku = zhandiantuihuochuku - 1;
				}
			}
			chukuCount = chukuCount + zhandiantuihuochuku;
			chukuMoney = chukuMoney.add(receivablefee.subtract(paybackfee));

		}
		todayHuiZongDTO.setJinrichuku_count(chukuCount);
		todayHuiZongDTO.setJinrichuku_money(chukuMoney);

		// 拒收的库存
		List<JSONObject> jushoukucunList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateTojson(FlowOrderTypeEnum.YiShenHe.getValue(), branch.getBranchid(),
				DeliveryStateEnum.JuShou.getValue());

		long jushoukucun = 0;
		BigDecimal jushoukucunMoney = BigDecimal.ZERO;
		if (jushoukucunList != null) {
			jushoukucun = jushoukucunList.get(0).getLong("num");
			jushoukucunMoney = new BigDecimal(deliveryStateDAO
					.getCountByBranchidAndDeliveryStateAndCredateTojson(FlowOrderTypeEnum.YiShenHe.getValue(), branch.getBranchid(), DeliveryStateEnum.JuShou.getValue()).get(0)
					.getString("paybackfee"));
		}
		// 滞留的库存
		List<JSONObject> zhiliukuncunList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateTojson(FlowOrderTypeEnum.YiShenHe.getValue(), branch.getBranchid(),
				DeliveryStateEnum.FenZhanZhiLiu.getValue());
		long zhiliukuncun = 0;
		BigDecimal zhiliukuncunMoney = BigDecimal.ZERO;
		if (zhiliukuncunList != null) {
			zhiliukuncun = zhiliukuncunList.get(0).getLong("num");
			zhiliukuncunMoney = new BigDecimal(zhiliukuncunList.get(0).getString("receivablefee")).subtract(new BigDecimal(zhiliukuncunList.get(0).getString("paybackfee")));
		}

		List<JSONObject> weiguibankuncunList = deliveryStateDAO.getIsNotGuibanByDeliverybranchidAndDeliverystateAndCredate(branch.getBranchid());
		// 未归班的库存
		long weiguibankuncun = 0;
		BigDecimal weiguibanMoney = BigDecimal.ZERO;
		if (weiguibankuncunList != null) {
			weiguibankuncun = weiguibankuncunList.get(0).getLong("num");
			weiguibanMoney = new BigDecimal(weiguibankuncunList.get(0).getString("receivablefee")).subtract(new BigDecimal(weiguibankuncunList.get(0).getString("paybackfee")));
		}

		// 到站未领
		List<String> daozhuanweilingList = logTodayDAO.getOrderByDeliverystate(branch.getBranchid(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), simpleDateFormat.format(startDate),
				simpleDateFormat.format(endDate));
		long daozhuanweiling = 0;
		BigDecimal daozhanweilingMoney = BigDecimal.ZERO;
		if (daozhuanweilingList != null) {
			// 到站未领
			BigDecimal receivablefee = BigDecimal.ZERO;
			BigDecimal paybackfee = BigDecimal.ZERO;
			long daozhuanweiling1 = daozhuanweilingList.size();
			for (String cwb : daozhuanweilingList) {
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					receivablefee = receivablefee.add(cwbOrder.getReceivablefee());
					paybackfee = paybackfee.add(cwbOrder.getPaybackfee());
				} else {
					daozhuanweiling1 = daozhuanweiling1 - 1;
				}
			}
			daozhanweilingMoney = daozhanweilingMoney.add(receivablefee.subtract(paybackfee));
			daozhuanweiling = daozhuanweiling1;
		}
		todayHuiZongDTO.setJinrikucun_count(jushoukucun + zhiliukuncun + weiguibankuncun + daozhuanweiling);
		BigDecimal jinrikucun_money = jushoukucunMoney.add(zhiliukuncunMoney).add(weiguibanMoney).add(daozhanweilingMoney);
		todayHuiZongDTO.setJinrikucun_money(jinrikucun_money);

		// 投递率 在外层封装
		// todayHuiZongDTO.setDeliveryRate(deliveryRate);

		return todayHuiZongDTO;
	}

	// =======================站点日报方法============end===========
	// 已出库
	private Map<Integer, List<String>> getCwbByflowOrderYichuku(long branchid, String flowordertype, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = new ArrayList<String>();
		String[] flowordertypeList = flowordertype.split(",");
		for (String flowtype : flowordertypeList) {
			List<String> daocuohuoList = logTodayDAO.getTodayWeidaohuoCwbbyChukuNextbranchid(branchid, Long.parseLong(flowtype), startTime, endTime);
			cwbList.addAll(daocuohuoList);
		}
		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	private Map<Integer, List<String>> getCwbByflowOrder(long branchid, String flowordertype, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = new ArrayList<String>();
		String[] flowordertypeList = flowordertype.split(",");
		for (String flowtype : flowordertypeList) {
			List<String> daocuohuoList = logTodayDAO.getOrderBybranchid(branchid, Long.parseLong(flowtype), startTime, endTime);
			cwbList.addAll(daocuohuoList);
		}
		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// 库房出库站点未到站
	private Map<Integer, List<String>> getWeidaohuoCwb(long branchid, long flowordertype, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = logTodayDAO.getTodayWeidaohuoCwbbyChukuNextbranchidCwb(branchid, flowordertype);

		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	private Map<Integer, List<String>> getCwbByflowOrderByTazhan(long branchid, String flowordertype, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = new ArrayList<String>();
		String[] flowordertypeList = flowordertype.split(",");
		for (String flowtype : flowordertypeList) {
			List<String> daocuohuoList = logTodayDAO.getCwbTodaybyOrtherBranchid(branchid, Long.parseLong(flowtype), startTime, endTime);
			cwbList.addAll(daocuohuoList);
		}
		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// 到站未领
	private Map<Integer, List<String>> getCwbdaozhanweiling(long flowordertype, long branchid, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> daocuohuoList = logTodayDAO.getCountbyDedailtCwb(branchid, flowordertype, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
				+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		for (int i = 0, page = 1; i < daocuohuoList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > daocuohuoList.size() ? daocuohuoList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, daocuohuoList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// ====投递中订单号查询 除了拒收 外 所有已归班的 ==========
	private Map<Integer, List<String>> getCwbByflowOrderByToudi(long branchid, long deliverystate, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> daocuohuoList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, deliverystate, startTime, endTime);
		for (int i = 0, page = 1; i < daocuohuoList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > daocuohuoList.size() ? daocuohuoList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, daocuohuoList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// ====投递反馈 拒收 （包括拒换）==========
	private Map<Integer, List<String>> getCwbByflowOrderByToudiJuhuan(long branchid, long deliveryState, long cwbordertypeid, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateAndOrderTypeCwb(branchid, deliveryState, cwbordertypeid, startTime, endTime);
		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// =====反馈合计===========
	private Map<Integer, List<String>> getCwbByflowOrderByToudiHeji(long branchid, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = deliveryStateDAO.getIsFankuiByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(branchid, startTime, endTime);

		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// ========= 未反馈合计=======
	private Map<Integer, List<String>> getWeifankui(long branchid, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = deliveryStateDAO.getIsNotFankuiByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(branchid, startTime, endTime, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// ========= 反馈 未归班合计=======
	private Map<Integer, List<String>> getfankuiWeiguiban(long branchid, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = deliveryStateDAO.getFankuiNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(branchid, startTime, endTime);
		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// ===投递 妥投============
	private Map<Integer, List<String>> getCwbByflowOrderByToudiHejiAndFlow(long branchid, String deliverystate, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = new ArrayList<String>();
		String[] deliverystateList = deliverystate.split(",");
		for (String deliveryst : deliverystateList) {
			List<String> daocuohuoList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateCwb(branchid, Long.parseLong(deliveryst), startTime, endTime);

			cwbList.addAll(daocuohuoList);
		}
		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// ===库存 滞留、退货============
	private Map<Integer, List<String>> getCwbBykucun(long flowordertype, long branchid, long deliverystate, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateCwb(flowordertype, branchid, deliverystate);

		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// ===今日 滞留============
	private Map<Integer, List<String>> getCwbBykucunByjinri(long flowordertype, long branchid, long deliverystate, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = deliveryStateDAO.getCwbByBranchidAndDeliveryStateAndCredateByJinri(flowordertype, branchid, deliverystate, startTime, endTime);

		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// ===昨日 滞留============
	private Map<Integer, List<String>> getCwbBykucunByZuori(long flowordertype, long branchid, long deliverystate, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = deliveryStateDAO.getCwbByBranchidAndDeliveryStateAndCredateByZuori(flowordertype, branchid, deliverystate, startTime);

		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	// ===库存 未归班============
	private Map<Integer, List<String>> getCwbBykucunWeiguiban(long branchid, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = deliveryStateDAO.getIsNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJsonCwb(branchid);

		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	private Map<Integer, List<String>> getShijiaoKuan(long branchid, String startTime, String endTime) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		List<String> cwbList = new ArrayList<String>();
		List<String> daocuohuoList = logTodayDAO.getPayUpByDeliverybranchidAndDeliverystateAndCredateTo(branchid, startTime, endTime);
		cwbList.addAll(daocuohuoList);
		for (int i = 0, page = 1; i < cwbList.size(); page++) {
			int pageMax = page * Page.ONE_PAGE_NUMBER > cwbList.size() ? cwbList.size() : page * Page.ONE_PAGE_NUMBER;
			map.put(page, cwbList.subList(i, pageMax));
			i = page * Page.ONE_PAGE_NUMBER;
		}
		return map;
	}

	public List<CwbOrder> getOrderListByBranchidAndType(long branchid, String type, long page, String startTime, String endTime) {

		List<CwbOrder> list = new ArrayList<CwbOrder>();
		if ("daohuo_jinriyichuku".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderYichuku(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "", startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("daohuo_weidaohuo".equals(type)) {
			Map<Integer, List<String>> map = getWeidaohuoCwb(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("daohuo_yidaohuo".equals(type)) {
			List<CwbOrder> cwborderList = cwbDAO.getLousaodaozhanByZhandianCwb(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid, startTime);
			String cwbs = "'--'";
			if (cwborderList != null && cwborderList.size() > 0) {
				cwbs = "";
				for (CwbOrder cwbOrder : cwborderList) {
					cwbs += "'" + cwbOrder.getCwb() + "',";
				}
				cwbs = cwbs.substring(0, cwbs.length() - 1);
			}
			return cwbDAO.getYichukudaozhanByZhandian(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "", startTime, cwbs, branchid, page);
		}
		if ("daohuo_lousaoyidaohuo".equals(type)) {
			return cwbDAO.getLousaodaozhanByZhandian(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid, startTime, page);
		}
		if ("daohuo_tazhandaohuo".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByTazhan(branchid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "", startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("daohuo_shaohuo".equals(type)) {

			return list;
		}
		if ("daohuo_daocuohuo".equals(type)) {

			Map<Integer, List<String>> map = getCwbByflowOrder(branchid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "", startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_daohuo".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrder(branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "", startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_zuorizhiliu".equals(type)) {// 页面却掉显示

			return list;
		}
		if ("toudi_daozhanweiling".equals(type)) {
			Map<Integer, List<String>> map = getCwbdaozhanweiling(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), branchid, startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_linghuo".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrder(branchid, FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "", startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_zuoriweiguiban".equals(type)) {

			return list;
		}
		if ("toudi_peisongcheng".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudi(branchid, DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_jushou".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudi(branchid, DeliveryStateEnum.JuShou.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_jinrizhiliu".equals(type)) {
			Map<Integer, List<String>> map = getCwbBykucunByjinri(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.FenZhanZhiLiu.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_bufenjushou".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudi(branchid, DeliveryStateEnum.BuFenTuiHuo.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_zhongzhuan".equals(type)) {

			Map<Integer, List<String>> map = getCwbByflowOrder(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "", startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_shangmentuichenggong".equals(type)) {

			Map<Integer, List<String>> map = getCwbByflowOrderByToudi(branchid, DeliveryStateEnum.ShangMenTuiChengGong.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}

		if ("toudi_shangmentuijutui".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudi(branchid, DeliveryStateEnum.ShangMenJuTui.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_shangmenhuanchenggong".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudi(branchid, DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}

		if ("toudi_shangmenhuanjuhuan".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudiJuhuan(branchid, DeliveryStateEnum.JuShou.getValue(), CwbOrderTypeIdEnum.Shangmenhuan.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_diushi".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudi(branchid, DeliveryStateEnum.HuoWuDiuShi.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_fankuiheji".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudiHeji(branchid, startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;

		}
		if ("toudi_weifankuiheji".equals(type)) {
			Map<Integer, List<String>> map = getWeifankui(branchid, startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}

		if ("kuanxiang_peisongchenggong".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudi(branchid, DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("kuanxiang_shangmentui".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudi(branchid, DeliveryStateEnum.ShangMenTuiChengGong.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("kuanxiang_shangmenhuan".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudi(branchid, DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("kuanxiang_shijiaokuan".equals(type)) {
			Map<Integer, List<String>> map = getShijiaoKuan(branchid, startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("kucun_zuorikucun".equals(type)) {

			return list;
		}
		if ("kucun_daohuo".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrder(branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "", startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("kucun_tuotou".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrderByToudiHejiAndFlow(branchid, DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue()
					+ "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("kucun_tuihuochuku".equals(type)) {
			Map<Integer, List<String>> map = getCwbByflowOrder(branchid, FlowOrderTypeEnum.TuiHuoChuZhan.getValue() + "", startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("kucun_zhongzhuanchuku".equals(type)) {

			Map<Integer, List<String>> map = getCwbByflowOrder(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "", startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("kucun_jinrikuncun".equals(type)) {

			return cwbDAO.getCwbOrderByCurrentbranchidAndFlowordertypeToPage(page, branchid);
		}

		if ("jushou_kuicun".equals(type)) {
			Map<Integer, List<String>> map = getCwbBykucun(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.JuShou.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("zhiliu_kuicun".equals(type)) {
			Map<Integer, List<String>> map = getCwbBykucun(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.FenZhanZhiLiu.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("weiguiban_kuicun".equals(type)) {
			Map<Integer, List<String>> map = getCwbBykucunWeiguiban(branchid, startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("toudi_fankuiweiguiban_heji".equals(type)) {
			Map<Integer, List<String>> map = getfankuiWeiguiban(branchid, startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		if ("zuori_fankui_leijizhiliu".equals(type)) {
			Map<Integer, List<String>> map = getCwbBykucunByZuori(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.FenZhanZhiLiu.getValue(), startTime, endTime);
			List<String> cwblist = (List<String>) map.get((int) page);
			if (cwblist != null) {
				for (String cwb : cwblist) {
					if (cwbDAO.getCwbByCwb(cwb) != null) {
						list.add(cwbDAO.getCwbByCwb(cwb));
					}
				}
			}
			return list;
		}
		return list;
	}

	public long getOrderListByBranchidAndTypeCount(long branchid, String type, String startTime, String endTime) {

		if ("daohuo_jinriyichuku".equals(type)) {
			return logTodayDAO.getTodayWeidaohuobyNextbranchid(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), startTime, endTime);
		}
		if ("daohuo_weidaohuo".equals(type)) {

			return logTodayDAO.getTodayWeidaohuobyNextbranchidIsnowCount(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		}
		if ("daohuo_yidaohuo".equals(type)) {
			return logTodayDAO.getTodaybyBranchid(branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), startTime, endTime)
					- logTodayDAO.getLousaoyidaozhanCount(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid, startTime);
		}
		if ("daohuo_lousaoyidaohuo".equals(type)) {
			return logTodayDAO.getLousaoyidaozhanCount(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), branchid, startTime);
		}
		if ("daohuo_tazhandaohuo".equals(type)) {

			return logTodayDAO.getTodaybyOrtherBranchid(branchid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), startTime, endTime);
		}
		if ("daohuo_shaohuo".equals(type)) {

			return 0;
		}
		if ("daohuo_daocuohuo".equals(type)) {

			return logTodayDAO.getTodaybyBranchid(branchid, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), startTime, endTime);
		}
		if ("toudi_daohuo".equals(type)) {

			return logTodayDAO.getTodaybyBranchid(branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), startTime, endTime);
		}
		if ("toudi_zuorizhiliu".equals(type)) {

			return 0;
		}
		if ("toudi_daozhanweiling".equals(type)) {
			return logTodayDAO.getCountbyDedailt(branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		}
		if ("toudi_linghuo".equals(type)) {
			return logTodayDAO.getTodaybyBranchid(branchid, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), startTime, endTime);
		}
		if ("toudi_zuoriweiguiban".equals(type)) {

			return 0;
		}

		if ("toudi_peisongcheng".equals(type)) {
			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime);
		}
		if ("toudi_jushou".equals(type)) {

			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.JuShou.getValue(), startTime, endTime);
		}
		if ("toudi_jinrizhiliu".equals(type)) {

			return deliveryStateDAO
					.getCountByBranchidAndDeliveryStateAndCredateByJinri(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.FenZhanZhiLiu.getValue(), startTime, endTime);
		}
		if ("toudi_bufenjushou".equals(type)) {

			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.BuFenTuiHuo.getValue(), startTime, endTime);
		}
		if ("toudi_zhongzhuan".equals(type)) {

			return logTodayDAO.getTodaybyBranchid(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), startTime, endTime);
		}
		if ("toudi_shangmentuichenggong".equals(type)) {

			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.ShangMenTuiChengGong.getValue(), startTime, endTime);
		}

		if ("toudi_shangmentuijutui".equals(type)) {

			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.ShangMenJuTui.getValue(), startTime, endTime);
		}
		if ("toudi_shangmenhuanchenggong".equals(type)) {
			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startTime, endTime);
		}

		if ("toudi_shangmenhuanjuhuan".equals(type)) {

			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndStateAndOrderType(branchid, DeliveryStateEnum.JuShou.getValue(), CwbOrderTypeIdEnum.Shangmenhuan.getValue(),
					startTime, endTime);
		}
		if ("toudi_diushi".equals(type)) {

			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.HuoWuDiuShi.getValue(), startTime, endTime);
		}
		if ("toudi_fankuiheji".equals(type)) {

			return deliveryStateDAO.getIsFankuiByDeliverybranchidAndDeliverystateAndCredateToJson(branchid, startTime, endTime).get(0).getLong("num");
		}
		if ("toudi_weifankuiheji".equals(type)) {

			return deliveryStateDAO.getIsNotFankuiByDeliverybranchidAndDeliverystateAndCredateToJson(branchid, startTime, endTime, FlowOrderTypeEnum.FenZhanLingHuo.getValue()).get(0).getLong("num");
		}
		if ("kuanxiang_peisongchenggong".equals(type)) {

			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime);
		}
		if ("kuanxiang_shangmentui".equals(type)) {

			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.ShangMenTuiChengGong.getValue(), startTime, endTime);
		}
		if ("kuanxiang_shangmenhuan".equals(type)) {

			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startTime, endTime);
		}
		if ("kuanxiang_shijiaokuan".equals(type)) {

			return deliveryStateDAO.getPayUpByDeliverybranchidAndDeliverystateAndCredateToJson(branchid, startTime, endTime).get(0).getLong("num");
		}
		if ("kucun_zuorikucun".equals(type)) {

			return 0;
		}
		if ("kucun_daohuo".equals(type)) {
			return logTodayDAO.getTodaybyBranchid(branchid, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), startTime, endTime);
		}
		if ("kucun_tuotou".equals(type)) {

			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.PeiSongChengGong.getValue(), startTime, endTime)
					+ deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.ShangMenTuiChengGong.getValue(), startTime, endTime)
					+ deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateAndState(branchid, DeliveryStateEnum.ShangMenHuanChengGong.getValue(), startTime, endTime);
		}
		if ("kucun_tuihuochuku".equals(type)) {

			return logTodayDAO.getTodaybyBranchid(branchid, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), startTime, endTime);
		}
		if ("kucun_zhongzhuanchuku".equals(type)) {

			return logTodayDAO.getTodaybyBranchid(branchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), startTime, endTime);
		}
		if ("jushou_kuicun".equals(type)) {
			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredate(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.JuShou.getValue());
		}
		if ("zhiliu_kuicun".equals(type)) {
			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredate(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.FenZhanZhiLiu.getValue());
		}
		if ("weiguiban_kuicun".equals(type)) {
			return deliveryStateDAO.getIsNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJson(branchid);
		}
		if ("toudi_fankuiweiguiban_heji".equals(type)) {
			return deliveryStateDAO.getFankuiNotGuibanByDeliverybranchidAndDeliverystateAndCredateToJson(branchid, startTime, endTime).get(0).getLong("num");
		}
		if ("zuori_fankui_leijizhiliu".equals(type)) {
			return deliveryStateDAO.getCountByBranchidAndDeliveryStateAndCredateByZuori(FlowOrderTypeEnum.YiShenHe.getValue(), branchid, DeliveryStateEnum.FenZhanZhiLiu.getValue(), startTime);
		}
		return 0;
	}

	public BranchTodayLog getBranchTodayLog(long userid, long branchid, BranchTodayLog yesterdayLog, TodayArrivalDTO todayArrivalDTO, TodayDeliveryDTO todayDeliveryDTO, TodayFundsDTO todayFundsDTO,
			TodayStockDTO todayStockDTO, TodayHuiZongDTO todayHuiZongDTO) {
		BranchTodayLog branchTodayLog = new BranchTodayLog();
		branchTodayLog.setBranchid(branchid);
		branchTodayLog.setCreatedate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		branchTodayLog.setUserid(userid);
		branchTodayLog.setYesterday_stortage(todayArrivalDTO.getShaohuo());
		branchTodayLog.setYesterday_not_arrive(0);
		branchTodayLog.setYesterday_other_site_arrive(0);
		branchTodayLog.setToday_out_storehouse(todayArrivalDTO.getJinriyichuku());
		branchTodayLog.setToday_not_arrive(0);
		branchTodayLog.setToday_arrive(todayArrivalDTO.getYidaohuo());
		branchTodayLog.setToday_other_site_arrive(todayArrivalDTO.getTazhandaohuo());
		branchTodayLog.setToday_stortage(todayArrivalDTO.getWeidaohuo());
		branchTodayLog.setToday_wrong_arrive(todayArrivalDTO.getDaocuohuo());
		branchTodayLog.setToday_fankui_daohuo(todayDeliveryDTO.getToday_fankui_daohuo());
		branchTodayLog.setToday_fankui_zhiliu(todayDeliveryDTO.getToday_fankui_zhiliu());
		branchTodayLog.setToday_fankui_linghuo(todayDeliveryDTO.getToday_fankui_linghuo());
		branchTodayLog.setToday_fankui_zuoriweiguiban(todayDeliveryDTO.getToday_fankui_zuoriweiguiban());
		branchTodayLog.setToday_fankui_peisongchenggong_count(todayDeliveryDTO.getToday_fankui_peisongchenggong_count());
		branchTodayLog.setToday_fankui_peisongchenggong_money(todayDeliveryDTO.getToday_fankui_peisongchenggong_money());
		branchTodayLog.setToday_fankui_jushou(todayDeliveryDTO.getToday_fankui_jushou());
		branchTodayLog.setToday_fankui_leijizhiliu(todayDeliveryDTO.getToday_fankui_leijizhiliu());
		branchTodayLog.setToday_fankui_bufenjushou(todayDeliveryDTO.getToday_fankui_bufenjushou());
		branchTodayLog.setToday_fankui_zhobgzhuan(todayDeliveryDTO.getToday_fankui_zhobgzhuan());
		branchTodayLog.setToday_fankui_shangmentuichenggong_count(todayDeliveryDTO.getToday_fankui_shangmentuichenggong_count());
		branchTodayLog.setToday_fankui_shangmentuichenggong_money(todayDeliveryDTO.getToday_fankui_shangmentuichenggong_money());
		branchTodayLog.setToday_fankui_shangmentuijutui(todayDeliveryDTO.getToday_fankui_shangmentuijutui());
		branchTodayLog.setToday_fankui_shangmenhuanchenggong_count(todayDeliveryDTO.getToday_fankui_shangmenhuanchenggong_count());
		branchTodayLog.setToday_fankui_shangmenhuanchenggong_yingshou_money(todayDeliveryDTO.getToday_fankui_shangmenhuanchenggong_yingshou_money());
		branchTodayLog.setToday_fankui_shangmenhuanchenggong_yingtui_money(todayDeliveryDTO.getToday_fankui_shangmenhuanchenggong_yingtui_money());
		branchTodayLog.setToday_fankui_shangmenhuanjuhuan(todayDeliveryDTO.getToday_fankui_shangmenhuanjuhuan());
		branchTodayLog.setToday_fankui_diushi(todayDeliveryDTO.getToday_fankui_diushi());
		branchTodayLog.setToday_fankui_heji_count(todayDeliveryDTO.getToday_fankui_heji_count());
		branchTodayLog.setToday_fankui_heji_yingshou_money(todayDeliveryDTO.getToday_fankui_heji_yingshou_money());
		branchTodayLog.setToday_fankui_heji_yingtui_money(todayDeliveryDTO.getToday_fankui_heji_yingtui_money());
		branchTodayLog.setToday_weifankui_heji_count(todayDeliveryDTO.getToday_weifankui_heji_count());
		branchTodayLog.setToday_weifankui_heji_yingshou_money(todayDeliveryDTO.getToday_weifankui_heji_yingshou_money());
		branchTodayLog.setToday_weifankui_heji_yingtui_money(todayDeliveryDTO.getToday_weifankui_heji_yingtui_money());
		branchTodayLog.setToday_funds_peisongchenggong_count(todayFundsDTO.getPeisongchenggong());
		branchTodayLog.setToday_funds_peisongchenggong_cash(todayFundsDTO.getPeisongchenggong_cash_amount());
		branchTodayLog.setToday_funds_peisongchenggong_pos(todayFundsDTO.getPeisongchenggong_pos_amount());
		branchTodayLog.setToday_funds_peisongchenggong_checkfee(todayFundsDTO.getPeisongchenggong_checkfee_amount());
		branchTodayLog.setToday_funds_shangmentuichenggong_count(todayFundsDTO.getShangmentuichenggong());
		branchTodayLog.setToday_funds_shangmentuichenggong_money(todayFundsDTO.getShangmentuichenggong_cash_amount());
		branchTodayLog.setToday_funds_shangmentuichenggong_cash(BigDecimal.ZERO);
		branchTodayLog.setToday_funds_shangmentuichenggong_pos(BigDecimal.ZERO);
		branchTodayLog.setToday_funds_shangmentuichenggong_checkfee(BigDecimal.ZERO);
		branchTodayLog.setYesterday_kucun_count(todayStockDTO.getZuorikucun());
		branchTodayLog.setToday_kucun_arrive(todayStockDTO.getJinridaohuo());
		branchTodayLog.setToday_kucun_sucess(todayStockDTO.getJinrituodou());
		branchTodayLog.setToday_kucun_tuihuochuku(todayStockDTO.getJinrituihuochuku());
		branchTodayLog.setToday_kucun_zhongzhuanchuku(todayStockDTO.getJinrizhongzhuanchuku());
		branchTodayLog.setToday_kucun_count(todayStockDTO.getJinrikucun());
		branchTodayLog.setJobRemark("系统自动生成");
		branchTodayLog.setShangmenhuanchenggong(todayFundsDTO.getShangmenhuanchenggong());
		branchTodayLog.setShangmenhuanchenggong_yingshou_amount(todayFundsDTO.getShangmenhuanchenggong_yingshou_amount());
		branchTodayLog.setShangmenhuanchenggong_yingtui_amount(todayFundsDTO.getShangmenhuanchenggong_yingtui_amount());
		branchTodayLog.setShishou_sum_amount(BigDecimal.ZERO);

		branchTodayLog.setDeliveryRate(todayHuiZongDTO.getDeliveryRate());

		branchTodayLog.setJushou_kuicun(todayStockDTO.getJushou_kuicun());
		branchTodayLog.setZhiliu_kuicun(todayStockDTO.getZhiliu_kuicun());
		branchTodayLog.setWeiguiban_kuicun(todayStockDTO.getWeiguiban_kuicun());
		branchTodayLog.setToudi_daozhanweiling(todayDeliveryDTO.getToudi_daozhanweiling());// 今日投递日报,未领货-今日未领货
		branchTodayLog.setZuori_jushou_kuicun(todayStockDTO.getZuori_jushou_kuicun());// 今日库存日报，昨日库存-退货未出站
		branchTodayLog.setZuori_zhiliu_kuicun(todayStockDTO.getZuori_zhiliu_kuicun());// 今日投递日报,今日应投-昨日滞留
																						// |
																						// 今日库存日报，昨日库存-昨日滞留
		branchTodayLog.setZuori_weiguiban_kuicun(todayStockDTO.getZuori_weiguiban_kuicun());// 今日投递日报,今日应投-昨日未归班
																							// |
																							// 今日库存日报，昨日库存-小件员未归班
		branchTodayLog.setZuori_toudi_daozhanweiling(todayStockDTO.getZuori_toudi_daozhanweiling());// 今日投递日报,今日应投-昨日到站未领
																									// |
																									// 今日库存日报，昨日库存-到站未领

		branchTodayLog.setShijiaokuan_cash_amount(todayFundsDTO.getShijiaokuan_cash_amount());
		branchTodayLog.setShijiaokuan_pos_amount(todayFundsDTO.getShijiaokuan_pos_amount());
		branchTodayLog.setShijiaokuan_checkfee_amount(todayFundsDTO.getShijiaokuan_checkfee_amount());

		branchTodayLog.setToday_fankuiweiguiban_heji_count(todayDeliveryDTO.getToday_fankuiweiguiban_heji_count());
		branchTodayLog.setToday_fankuiweiguiban_heji_yingshou_money(todayDeliveryDTO.getToday_fankuiweiguiban_heji_yingshou_money());
		branchTodayLog.setToday_fankuiweiguiban_heji_yingtui_money(todayDeliveryDTO.getToday_fankuiweiguiban_heji_yingtui_money());
		branchTodayLog.setZuori_fankui_leijizhiliu(todayDeliveryDTO.getZuori_fankui_leijizhiliu());// 今日投递日报,未领货-昨日滞留

		branchTodayLog.setJinri_lousaodaozhan(todayArrivalDTO.getJinri_lousaodaozhan());

		// ======合计=============
		branchTodayLog.setJinridaohuo_count(todayDeliveryDTO.getToday_fankui_daohuo());// 今日到货
		if (yesterdayLog != null) {
			branchTodayLog.setZuorikucun_count(yesterdayLog.getJushou_kuicun() + yesterdayLog.getZhiliu_kuicun() + yesterdayLog.getWeiguiban_kuicun() + yesterdayLog.getToudi_daozhanweiling());// 获取昨天的今日库存
			branchTodayLog.setJinriyingtou_count(yesterdayLog.getZhiliu_kuicun() + yesterdayLog.getWeiguiban_kuicun() + yesterdayLog.getToudi_daozhanweiling()
					+ todayDeliveryDTO.getToday_fankui_daohuo());// 今日应投
			branchTodayLog.setZuorikucun_money(todayHuiZongDTO.getZuorikucun_money());// 昨日库存
			branchTodayLog.setJinridaohuo_money(todayHuiZongDTO.getJinridaohuo_money());// 已到货
			branchTodayLog.setJinriyingtou_money(todayHuiZongDTO.getZuorikucun_money().add(todayHuiZongDTO.getJinridaohuo_money()));// 应投金额
		} else {
			branchTodayLog.setJinriyingtou_count(todayDeliveryDTO.getToday_fankui_daohuo());// 今日应投
			branchTodayLog.setJinridaohuo_money(todayHuiZongDTO.getJinridaohuo_money());// 已到货
			branchTodayLog.setJinriyingtou_money(todayHuiZongDTO.getJinridaohuo_money());// 应投金额
		}
		branchTodayLog.setJinrishoukuan_count(todayHuiZongDTO.getJinrishoukuan_count());// 今日妥投
		branchTodayLog.setJinrichuku_count(todayStockDTO.getJinrituihuochuku() + todayStockDTO.getJinrizhongzhuanchuku());// 今日出库
		branchTodayLog.setJinrikucun_count(todayStockDTO.getJushou_kuicun() + todayStockDTO.getZhiliu_kuicun() + todayStockDTO.getWeiguiban_kuicun() + todayDeliveryDTO.getToudi_daozhanweiling());// 今日库存

		branchTodayLog.setJinrishoukuan_money(todayHuiZongDTO.getJinrishoukuan_money());// 妥投金额
		branchTodayLog.setJinrichuku_money(todayHuiZongDTO.getJinrichuku_money());// 退货出库+中转出库
																					// 金额
		branchTodayLog.setJinrikucun_money(todayHuiZongDTO.getJinrikucun_money());// 今日库存金额

		// ======合计=============

		return branchTodayLog;
	}

	public BranchTodayLog generateTodaylog(long userid, long branchid, Date startDate, Date endDate) {
		Branch branch = branchDAO.getBranchByBranchid(branchid);
		// 获取昨日日期
		String yesterday = DateDayUtil.getDateBefore("", -1);
		if (branchid == 224) {
			System.out.println(branchid);
		}
		BranchTodayLog yesterdayLog = logTodayDAO.getBranchTodayLogByBranchidAndDate(branchid, new Date());
		// 已到货（单）
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 配送成功单数、应收金额、应退金额
		List<JSONObject> peisongchenggongList = deliveryStateDAO.getDeliveryStateByDeliverybranchidAndDeliverystateAndCredateToJson(branch.getBranchid(),
				DeliveryStateEnum.PeiSongChengGong.getValue(), (simpleDateFormat.format(startDate)), (simpleDateFormat.format(endDate)));
		// ===========今日到货日报==========
		TodayArrivalDTO todayArrivalDTO = this.getTodayArrivalDTO(branch, startDate, endDate);
		// =======今日投递日报=============
		TodayDeliveryDTO todayDeliveryDTO = this.getTodayDeliveryDTO(branch, peisongchenggongList, startDate, endDate);
		// ============今日款项日报=================
		TodayFundsDTO todayFundsDTO = this.getTodayFundsDTO(branch, startDate, endDate);
		// ==============今日库存日报==========================
		TodayStockDTO todayStockDTO = this.getTodayStockDTO(branch, startDate, endDate);
		// =====汇总=======

		TodayHuiZongDTO todayHuiZongDTO = this.getTodayHuiZongDTO(branch, yesterday, peisongchenggongList, startDate, endDate);

		long tuotou = todayDeliveryDTO.getToday_fankui_peisongchenggong_count() + todayDeliveryDTO.getToday_fankui_shangmenhuanchenggong_count()
				+ todayDeliveryDTO.getToday_fankui_shangmentuichenggong_count();

		BigDecimal tuotouMoney = todayDeliveryDTO.getToday_fankui_peisongchenggong_money().add(todayDeliveryDTO.getToday_fankui_shangmenhuanchenggong_yingshou_money())
				.subtract(todayDeliveryDTO.getToday_fankui_shangmenhuanchenggong_yingtui_money()).subtract(todayDeliveryDTO.getToday_fankui_shangmentuichenggong_money());

		todayHuiZongDTO.setJinrishoukuan_count(tuotou);
		todayHuiZongDTO.setJinrishoukuan_money(tuotouMoney);

		// 存储站点到站和漏扫到站
		todayDeliveryDTO.setToday_fankui_daohuo(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());
		todayDeliveryDTO.setJinri_lousaodaozhan(todayArrivalDTO.getJinri_lousaodaozhan());
		todayStockDTO.setJinridaohuo(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());
		todayHuiZongDTO.setJinridaohuo_count(todayArrivalDTO.getYidaohuo() + todayArrivalDTO.getJinri_lousaodaozhan());

		long yingtou = todayHuiZongDTO.getJinriyingtou_count();
		if (yesterdayLog != null) {
			yingtou = yingtou + yesterdayLog.getWeiguiban_kuicun() + yesterdayLog.getZhiliu_kuicun() + yesterdayLog.getToudi_daozhanweiling();
			todayHuiZongDTO.setZuorikucun_count(yesterdayLog.getJinrikucun_count());
			todayHuiZongDTO.setZuorikucun_money(yesterdayLog.getJinrikucun_money());
			todayStockDTO.setZuori_jushou_kuicun(yesterdayLog.getJushou_kuicun());
			todayStockDTO.setZuori_zhiliu_kuicun(yesterdayLog.getZhiliu_kuicun());
			todayStockDTO.setZuori_weiguiban_kuicun(yesterdayLog.getWeiguiban_kuicun());
			todayStockDTO.setZuori_toudi_daozhanweiling(yesterdayLog.getToudi_daozhanweiling());// 昨日到站未领
			todayDeliveryDTO.setToday_fankui_zuoriweiguiban(yesterdayLog.getWeiguiban_kuicun());
			todayHuiZongDTO.setJinriyingtou_count(yingtou);
		}
		todayStockDTO.setJinrituodou(tuotou);
		todayHuiZongDTO.setDeliveryRate(new BigDecimal(yingtou == 0 ? 0 : tuotou * 100 / yingtou));
		return this.getBranchTodayLog(userid, branchid, yesterdayLog, todayArrivalDTO, todayDeliveryDTO, todayFundsDTO, todayStockDTO, todayHuiZongDTO);
	}

	public void dailyDayLogGenerate() {
		SystemInstall remoteInvoke = systemInstallDAO.getSystemInstallByName("isOpenJobHand");// 判断是否开启远程调用
		String value = remoteInvoke.getValue();
		if (value != null && value.equals("yes")) {
			logger.warn("站点生成日志任务-远程调用已开启，本地定时任务不执行dailylogs");
			return;
		}

		logger.info("start to generate dailylogs");
		User user = userDAO.getUserByUsername("admin");
		dailyDayLogGenerate(user);
		logger.info("generate dailylogs ends");
	}

	/**
	 * 远程调用
	 */
	public long dailyDayLogGenerate_RemoteInvoke() {
		long calcCount = 0;
		SystemInstall remoteInvoke = systemInstallDAO.getSystemInstallByName("isOpenJobHand");// 判断是否开启远程调用
		String value = remoteInvoke.getValue();
		if (value != null && value.equals("yes")) {
			logger.info("start to generate dailylogs");
			User user = userDAO.getUserByUsername("admin");
			calcCount = dailyDayLogGenerate(user);
			logger.info("generate dailylogs ends");
		} else {
			logger.warn("站点生成日志任务-未开启远程调用dailylogs");
		}
		return calcCount;

	}

	private long dailyDayLogGenerate(User user) {
		long calcCount = 0;
		List<Branch> allBranches = branchDAO.getAllBranches();
		for (Branch branch : allBranches) {
			Date endDate = new Date();
			dailyDayLogGenerate(user, endDate, branch);
			calcCount++;
		}
		saveDayLogDetail();
		return calcCount;
	}

	private void dailyDayLogGenerate(User user, Date endDate, Branch branch) {
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			try {
				Date startDate;
				BranchTodayLog lastBranchTodayLog = todayDAO.getLastBranchTodayLogByBranchid(branch.getBranchid());
				if (lastBranchTodayLog != null) {
					startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastBranchTodayLog.getCreatedate());
				} else {
					startDate = DateDayUtil.getTimeByDay(-1);
				}
				BranchTodayLog generateTodaylog = generateTodaylog(user.getUserid(), branch.getBranchid(), startDate, endDate);
				todayDAO.creBranchTodayLog(generateTodaylog);
			} catch (Exception e) {
				logger.error("error while generating dailylog for branchid: " + branch.getBranchname(), e);
			}
		}
	}

	/**
	 * 存储日志产生的库存订单明细，目前只存储 滞留 到站未领 未归班
	 */
	private void saveDayLogDetail() {
		String toDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		DeliveryPercent dp = new DeliveryPercent();
		// 获取到站未领
		List<CwbOrder> daohuoList = cwbDAO.getCwbByFlowOrderType(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao);
		for (CwbOrder co : daohuoList) {// 当前状态为分站到货的订单 进行插入当日遗留明细表
			deliveryPercentDAO.cre(co.getCwb(), co.getCurrentbranchid(), co.getCustomerid(), DeliveryPercentTypeEnum.DaoZhanWeiLing, toDay);
		}
		List<CwbOrder> daocuohuo_daohuoList = cwbDAO.getCwbByFlowOrderType(FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao);
		for (CwbOrder co : daocuohuo_daohuoList) {// 当前状态为分站 到错货 的订单 进行插入当日遗留明细表
			deliveryPercentDAO.cre(co.getCwb(), co.getCurrentbranchid(), co.getCustomerid(), DeliveryPercentTypeEnum.DaoZhanWeiLing, toDay);
		}
		List<CwbOrder> daocuohuochuli_daohuoList = cwbDAO.getCwbByFlowOrderType(FlowOrderTypeEnum.DaoCuoHuoChuLi);
		for (CwbOrder co : daocuohuochuli_daohuoList) {// 当前状态为分站 到错货处理 的订单
														// 进行插入当日遗留明细表
			deliveryPercentDAO.cre(co.getCwb(), co.getCurrentbranchid(), co.getCustomerid(), DeliveryPercentTypeEnum.DaoZhanWeiLing, toDay);
		}
		// 获取滞留
		List<CwbOrder> fankui_zhiliuList = cwbDAO.getCwbByFlowOrderTypeAndDeliveryState(FlowOrderTypeEnum.YiFanKui, DeliveryStateEnum.FenZhanZhiLiu);
		for (CwbOrder co : fankui_zhiliuList) {// 当前状态为分站 滞留 反馈 的订单 进行插入当日遗留明细表
			deliveryPercentDAO.cre(co.getCwb(), co.getDeliverybranchid(), co.getCustomerid(), DeliveryPercentTypeEnum.ZhiLiu, toDay);
		}
		List<CwbOrder> shenhe_zhiliuList = cwbDAO.getCwbByFlowOrderTypeAndDeliveryState(FlowOrderTypeEnum.YiShenHe, DeliveryStateEnum.FenZhanZhiLiu);
		for (CwbOrder co : shenhe_zhiliuList) {// 当前状态为分站 滞留 审核 的订单 进行插入当日遗留明细表
			deliveryPercentDAO.cre(co.getCwb(), co.getDeliverybranchid(), co.getCustomerid(), DeliveryPercentTypeEnum.ZhiLiu, toDay);
		}
		// 获取未归班
		List<CwbOrder> linghuoList = cwbDAO.getCwbByFlowOrderType(FlowOrderTypeEnum.FenZhanLingHuo);
		for (CwbOrder co : linghuoList) {// 当前状态为 领货的 进行插入当日遗留明细表
			deliveryPercentDAO.cre(co.getCwb(), co.getDeliverybranchid(), co.getCustomerid(), DeliveryPercentTypeEnum.WeiGuiBan, toDay);
		}
		List<CwbOrder> poszhifuList = cwbDAO.getCwbByFlowOrderType(FlowOrderTypeEnum.PosZhiFu);
		for (CwbOrder co : poszhifuList) {// 当前状态为 领货的 进行插入当日遗留明细表
			deliveryPercentDAO.cre(co.getCwb(), co.getCurrentbranchid(), co.getCustomerid(), DeliveryPercentTypeEnum.WeiGuiBan, toDay);
		}
		List<CwbOrder> chexiaofankuiList = cwbDAO.getCwbByFlowOrderType(FlowOrderTypeEnum.CheXiaoFanKui);
		for (CwbOrder co : chexiaofankuiList) {// 当前状态为 领货的 进行插入当日遗留明细表
			deliveryPercentDAO.cre(co.getCwb(), co.getCurrentbranchid(), co.getCustomerid(), DeliveryPercentTypeEnum.WeiGuiBan, toDay);
		}

	}

	@Override
	public void onChange(Map<String, String> parameters) {
		if (parameters.keySet().contains("siteDayLogTime")) {
			try {
				this.init();
			} catch (Exception e) {
				logger.error("error while reloading logtoDay camle routes", e);
			}
		}
	}

}
