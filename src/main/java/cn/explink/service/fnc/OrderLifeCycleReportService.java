package cn.explink.service.fnc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.fnc.OrderDetailsSnapshotDao;
import cn.explink.dao.fnc.OrderLifeCycleReportDao;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderSnapshot;
import cn.explink.domain.OrderLifeCycleReportVO;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbOrderLifeCycleTypeIdEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.util.JsonUtil;

/**
 * @author jinghui.pan
 *
 */
@Service
public class OrderLifeCycleReportService {

	private Logger logger = LoggerFactory
			.getLogger(OrderLifeCycleReportService.class);

	@Autowired
	private CustomerDAO customerDao;
	@Autowired
	private OrderLifeCycleReportDao orderLifeCycleReportDao;
	@Autowired
	private OrderFlowDAO orderFlowDAO;
	@Autowired
	private OrderDetailsSnapshotDao orderDetailsSnapshotDao;
	
	@Autowired
	private OrderLifeCycleFeeNotReturnFeeHandler feeNotReturnFeeHandler;
	
	@Autowired
	private OrderLifeCycleTuiKeHuWeiShouKuanHandler tuiKeHuWeiShouKuanHandler;

	/**
	 * 获取订单生命周期列表和汇总数据
	 * 
	 * @param selectedCustomers selected customer string like '1,2,3'
	 * @param queryDate date string in format - `yyy-MM-dd`
	 * 
	 * @return 列表和汇总数据
	 *
	 */
	public DataGridReturn getOrderLifecycleReportData(String selectedCustomers,String queryDate) {
		DataGridReturn dg = new DataGridReturn();

		int reportdate = transalateReportDate2Int(queryDate);
		
		List<OrderLifeCycleReportVO> rows = orderLifeCycleReportDao.getListByCustomers(selectedCustomers,reportdate);

		dg.setRows(rows);
		dg.setFooter(getSummaryFooter(rows));

		return dg;
	}

	/**
	 * 获取订单详情列表的数据
	 * 
	 * @param reportVO
	 *            生命周期报表id
	 * @param page
	 *            当前页
	 * @param pageSize
	 *            页数
	 * @return
	 *
	 */
	public DataGridReturn getCwbOrderDetail(OrderLifeCycleReportVO reportVO, int page, int pageSize) {

		int reportdate = reportVO.getReportdate();
		
		//retrieve the report id 
		long reportid = this.orderLifeCycleReportDao.getReportIdByCustomerAndTypeidAndReportdate(reportVO.getCustomerid(), reportVO.getTypeid(),reportdate);
		 
		//count the total row in order snapshot table by reportid
		int total = this.orderDetailsSnapshotDao.countByFnrptlifecycleid(reportid);

		//query list in order snapshot table by reportid and page
		List<CwbOrderSnapshot> rows = orderDetailsSnapshotDao.listByFnrptlifecycleid(reportid, page, pageSize);
		
		
		DataGridReturn dg = new DataGridReturn();
		dg.setRows(rows);
		dg.setTotal(total);
		return dg;
	}
	
	/**
	 * 站点未返代收货款
	 * 
	 * @param batchSize
	 *            每批次处理多少行数据在.如果传入少于等于0，则默认是1000行/批次
	 * @param date
	 *            生成的日期字符串，格式是'yyyy-MM-dd'
	 */
	public void handleNotReturnedRecivableFeeOrder(int batchSize,String date) {
		
//		if (batchSize <= 0) {
//			batchSize = 1000;
//		}
//
//		int reportdate = transalateReportDate2Int(date);
//		
//		// 订单类型
//		CwbOrderTypeIdEnum cwbOrderTypeIdEnum = CwbOrderTypeIdEnum.Peisong;
//		
//		// 获取总的记录数
//		long countOfRecord = this.orderDetailsSnapshotDao.countFeeNotReturnedFromCwbDetail(cwbOrderTypeIdEnum.getValue());
//
//		// 计算批次
//		int batch = getBatch(countOfRecord, batchSize);
//
//		logger.info(
//				"[handleNotReturnedRecivableFeeOrder][total record:{}, batch size : {}]", countOfRecord, batch);
//
//		long st, et;
//		List<CwbOrderSnapshot> cwbOrderListForBatchSave = null;
//		String cwbs = null;
//		long lastopscwbid = 0L;
//		
//		for (int i = 1; i <= batch; i++) {
//			// 记录开始时间
//			st = System.currentTimeMillis();
//			
//			cwbOrderListForBatchSave = this.orderDetailsSnapshotDao
//					.getListFeeNotReturnedFromCwbDetailByPage(cwbOrderTypeIdEnum.getValue(),i, batchSize,lastopscwbid);
//			
//			if(CollectionUtils.isNotEmpty(cwbOrderListForBatchSave)){
//				
//				cwbs = getCwbsFromCwbOrderSnapshotList(cwbOrderListForBatchSave,reportdate);
//				
//				this.orderDetailsSnapshotDao.disableRowByNotReturnedFeeByReportDate(cwbOrderTypeIdEnum.getValue(), reportdate);
//				
//				orderDetailsSnapshotDao.batchInsertOrderDetailSnapshot(cwbOrderListForBatchSave);
//				
//				//Retrieve the last opscwbid for next iterator
//				lastopscwbid = cwbOrderListForBatchSave.get(cwbOrderListForBatchSave.size() - 1).getOpscwbid();
//				
//			}
//
//			et = (System.currentTimeMillis() - st);
//
//			logger.info(
//					"[handleNotReturnedRecivableFeeOrder][batch {} procceed {} rows，spend time={} ms]",
//					new Object[] { i, cwbOrderListForBatchSave.size(), et });
//		}
		
	}
	
	
	/**
	 * “退供货商成功”操作状态但未做应收款账单核销的配送类型
	 * 
	 * @param batchSize
	 *            每批次处理多少行数据在.如果传入少于等于0，则默认是1000行/批次
	 * @param date
	 *            生成的日期字符串，格式是'yyyy-MM-dd'
	 */
	public void handleTuiKeHuWeiShouKuanOrder(int batchSize,String date) {
		
//		if (batchSize <= 0) {
//			batchSize = 1000;
//		}
//
//		int reportdate = transalateReportDate2Int(date);
//		
//		// 订单类型
//		CwbOrderTypeIdEnum cwbOrderTypeIdEnum = CwbOrderTypeIdEnum.Peisong;
//		
//		// 获取总的记录数
//		long countOfRecord = this.orderDetailsSnapshotDao.countFeeNotReturnedFromCwbDetail(cwbOrderTypeIdEnum.getValue());
//
//		// 计算批次
//		int batch = getBatch(countOfRecord, batchSize);
//
//		logger.info(
//				"[handleTuiKeHuWeiShouKuanOrder][total record:{}, batch size : {}]", countOfRecord, batch);
//
//		long st, et;
//		List<CwbOrderSnapshot> cwbOrderListForBatchSave = null;
//		String cwbs = null;
//		long lastopscwbid = 0L;
//		
//		for (int i = 1; i <= batch; i++) {
//			// 记录开始时间
//			st = System.currentTimeMillis();
//			
//			cwbOrderListForBatchSave = this.orderDetailsSnapshotDao
//					.getListTuiKeHuNotReturnedFromCwbDetailByPage(cwbOrderTypeIdEnum.getValue(),i, batchSize,lastopscwbid);
//			
//			if(CollectionUtils.isNotEmpty(cwbOrderListForBatchSave)){
//				
//				cwbs = getCwbsFromCwbOrderSnapshotList(cwbOrderListForBatchSave,reportdate);
//				
//				this.orderDetailsSnapshotDao.disableRowByCwbAndReportDate(cwbs, reportdate);
//				
//				orderDetailsSnapshotDao.batchInsertOrderDetailSnapshot(cwbOrderListForBatchSave);
//				
//				//Retrieve the last opscwbid for next iterator
//				lastopscwbid = cwbOrderListForBatchSave.get(cwbOrderListForBatchSave.size() - 1).getOpscwbid();
//				
//			}
//
//			et = (System.currentTimeMillis() - st);
//
//			logger.info(
//					"[handleTuiKeHuWeiShouKuanOrder][batch {} procceed {} rows，spend time={} ms]",
//					new Object[] { i, cwbOrderListForBatchSave.size(), et });
//		}
	}
	
	
	
	
	/**
	 * Get the cwb in format '1,3,2,3' from list;
	 */
	private String getCwbsFromCwbOrderSnapshotList(List<CwbOrderSnapshot> cwbOrderListForBatchSave, int reportdate){
		
		String cwbs = null;
		
		if(CollectionUtils.isNotEmpty(cwbOrderListForBatchSave)){
			List<String> cwbList = new ArrayList<String>();
			for (CwbOrderSnapshot cwbOrderSnapshot : cwbOrderListForBatchSave) {
				
				cwbOrderSnapshot.setReportdate(reportdate);
				cwbList.add(cwbOrderSnapshot.getCwb());
				
			}
			cwbs = StringUtils.join(cwbList, "','");
			cwbs = "'" + cwbs + "'";
		}
		
		return cwbs;
	}

	/**
	 * 生成给定日期的订单详细信息的快照，
	 * 
	 * @param batchSize
	 *            每批次处理多少行数据在.如果传入少于等于0，则默认是1000行/批次
	 * @param date
	 *            生成的日期字符串，格式是'yyyy-MM-dd'
	 */
	public void genLifeCycleOrderDetail(int batchSize, String date) {

		if (batchSize <= 0) {
			batchSize = 1000;
		}
		// 计算当前日期的前一天的
		String beginDate = date + " 00:00:00";
		String endDate = date + " 23:59:59";

		int reportdate = transalateReportDate2Int(date);
		
		
		logger.info(
				"[genLifeCycleOrderDetail][start to generate order snapshot on report date {}]", reportdate);
		
		// 获取总的记录数，时间范围是一天
		long countOfRecord = orderFlowDAO.getOrderFlowCountByCredate(beginDate,
				endDate);

		// 计算批次
		int batch = getBatch(countOfRecord, batchSize);

		logger.info(
				"total record:{}, batch size : {}",
				countOfRecord, batch);

		CwbOrder cwbOrder = null;
		CwbOrderSnapshot cwbOrderSnapshot = null;
		long st, et;
		List<CwbOrderSnapshot> cwbOrderListForBatchSave = null;
		long lastFloworderid = 0L;
		
		for (int i = 1; i <= batch; i++) {
			// 记录开始时间
			st = System.currentTimeMillis();

			// 分批从《订单操作流程表》中获取记录，记录已将按倒序排列
			List<OrderFlow> orderFlows = orderFlowDAO.getOrderFlowByCredateAndPage(beginDate, endDate, i, batchSize,lastFloworderid);

			if (logger.isDebugEnabled()) {
				logger.debug(
						"[Before remove duplicate cwb, orderFlows size = {}]",
						orderFlows.size());
			}
			orderFlows = removeDuplicatedRecordWithCwb(orderFlows);

			if (logger.isDebugEnabled()) {
				logger.debug(
						"[After remove duplicate cwb, orderFlows size = {}]",
						orderFlows.size());
			}

			if(CollectionUtils.isNotEmpty(orderFlows)){
				
				cwbOrderListForBatchSave = new ArrayList<CwbOrderSnapshot>();
				
				//Recurse the order flow 
				for (OrderFlow orderFlow : orderFlows) {
					
					cwbOrder = getCwbOrderFromOrderFlow(orderFlow);

					if (cwbOrder != null) {
						boolean existed = orderDetailsSnapshotDao
								.isExistByCwbAndReportdate(orderFlow.getCwb(),reportdate);

						// 如果 《订单快照表》中不存在记录，则插入
						if (!existed) {
							if (logger.isDebugEnabled()) {
								logger.debug("[cwb {} add to batch {} ]", cwbOrder.getCwb(), i);
							}
							cwbOrderSnapshot = copyCwbOrderToSnapshot(cwbOrder);
							cwbOrderSnapshot.setReportdate(reportdate);//set report date
							cwbOrderListForBatchSave.add(cwbOrderSnapshot);
						}
					}
				}
				
				lastFloworderid = orderFlows.get(orderFlows.size() - 1).getFloworderid();
				
				
				if (!cwbOrderListForBatchSave.isEmpty()) {
					// 批量插入到《订单快照表》
					orderDetailsSnapshotDao
							.batchInsertOrderDetailSnapshot(cwbOrderListForBatchSave);
				}
			}
			

			et = (System.currentTimeMillis() - st);

			logger.info(
					"[genLifeCycleOrderDetail][batch {} procceed {} rows，spend time={} ms]",
					new Object[] { i, cwbOrderListForBatchSave.size(), et });

		}
		//处理退供应商为收款逻辑
		this.tuiKeHuWeiShouKuanHandler.batchHandle(batchSize, reportdate);
		
		//handle未返代收货款
		this.feeNotReturnFeeHandler.batchHandle(batchSize, reportdate);
	}

	/**
	 * Translate date from string to int format.
	 * 
	 * @param string date in format `yyyy-MM-dd`
	 * @return date in Integer format like  `yyyyMMdd`, will return 0 if  any exception.  
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	private int transalateReportDate2Int(String date) {
		SimpleDateFormat fromeSdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat toSdf = new java.text.SimpleDateFormat("yyyyMMdd");
		int ret = 0;
		try {
			Date parsed  = fromeSdf.parse(date);
			String formated = toSdf.format(parsed);
			if(formated!= null){
				ret = Integer.parseInt(formated);
			}
			
		} catch (Exception e) {
			this.logger.error("[formateReportDate][parse date {} error = {}]" ,date, e.getMessage());
		} 
		
		return ret;
	}

	/**
	 * CwbOrder to CwbOrderSnapshots
	 */
	private CwbOrderSnapshot copyCwbOrderToSnapshot(CwbOrder cwbOrder) {
		CwbOrderSnapshot cwbOrderSnapshots = new CwbOrderSnapshot();
		
		try{
			BeanUtils.copyProperties(cwbOrder, cwbOrderSnapshots);
			
		}catch(BeansException beansException){
			this.logger.error("[copyCwbOrderToSnapshot][cwb = {}, error= {}]" ,cwbOrder.getCwb(),  beansException.getMessage());
		}
		
		return cwbOrderSnapshots;
	}

	/**
	 * 生成订单生命周期报表的数据
	 *
	 */
	@Transactional
	public void genLifeCycleReport(String date) {
		
		int reportdate = transalateReportDate2Int(date);

		this.logger.info("[genLifeCycleReport][start to gen: reportdate = {}]" , reportdate);
		
		// 订单类型
		CwbOrderTypeIdEnum cwbOrderTypeIdEnum = CwbOrderTypeIdEnum.Peisong;

		// 订单流程
		FlowOrderTypeEnum flowOrderTypeEnum = null;

		// 订单状态
		CwbStateEnum cwbStateEnum = null;

		List<CwbOrderSnapshot> orderSnapshots = null;

		// 软删除之前生成的记录
//		this.orderLifeCycleReportDao.updateStateToZero();
		this.orderLifeCycleReportDao.updateStateToZeroByReportDate(reportdate);

		// 导入未收货
		// flowordertype=1 and cwbordertypeid = 1
		flowOrderTypeEnum = FlowOrderTypeEnum.DaoRuShuJu;
		orderSnapshots = this.orderDetailsSnapshotDao
				.listByFlowordertypeAndOrdertypeid(
			flowOrderTypeEnum.getValue(),
						cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.Import,reportdate);

		// 提货未入库
		// flowordertype=2 and cwbordertypeid = 1
		flowOrderTypeEnum = FlowOrderTypeEnum.TiHuo;
		orderSnapshots = this.orderDetailsSnapshotDao
				.listByFlowordertypeAndOrdertypeid(
						flowOrderTypeEnum.getValue(),
						cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.TiHuo,reportdate);

		// 分拣入库未出库
		// flowordertype=4 and cwbordertypeid = 1
		flowOrderTypeEnum = FlowOrderTypeEnum.RuKu;
		orderSnapshots = this.orderDetailsSnapshotDao
				.listByFlowordertypeAndOrdertypeid(
						flowOrderTypeEnum.getValue(),
						cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.FengBoRuku,reportdate);

		// 分拣出库未到站 “出库扫描” ,配送状态
		// flowordertype=6 and cwbstate = 1 and cwbordertypeid = 1
//		cwbStateEnum = CwbStateEnum.PeiShong;
//		flowOrderTypeEnum = FlowOrderTypeEnum.ChuKuSaoMiao;
//		orderSnapshots = this.orderDetailsSnapshotDao
//				.listByFlowordertypeAndOrdertypeidAndCwbstate(
//						flowOrderTypeEnum.getValue(),
//						cwbOrderTypeIdEnum.getValue(), cwbStateEnum.getValue(),reportdate);
		orderSnapshots = this.orderDetailsSnapshotDao.getFengBoChukuWeiDaoZhan(cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.FengBoChuku,reportdate);

		// 站点在站货物 -
		// “分站到货扫描”“到错货”“到错货处理”“分站领货”“反馈为分站滞留”“反馈为小件员滞留”“反馈为待中转”“反馈为拒收”“反馈为货物丢失”
		// flowordertype in(7,8,9,38) or (flowordertype in (35,36) and
		// deliverystate in(6,10,4,8)) and cwbordertypeid = 1
		orderSnapshots = this.orderDetailsSnapshotDao
				.getListStockInStation(cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.ZhanDianZaiZhan,reportdate);

		// 站点未返代收货款 - 全部反馈为“配送成功”且结算状态为“未收款”
		// deliverystate=1 and cwbordertypeid = 1 and fnorgoffsetflag = 0 ;

		// TODO: 这里的付款标志fnorgoffsetflag =
		// 0，由于从操作环节中的json去解释的，那就会产生一直都是0的情况，这里需要进一步处理
		orderSnapshots = this.orderDetailsSnapshotDao
				.getListFeeNotReturned(cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.ZhanDianWeiFanKuan,reportdate);

		// 退货出站在途 - “退货出站”
		flowOrderTypeEnum = FlowOrderTypeEnum.TuiHuoChuZhan;
		orderSnapshots = this.orderDetailsSnapshotDao
				.listByFlowordertypeAndOrdertypeid(
						flowOrderTypeEnum.getValue(),
						cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.TuiHuoChuZhanZaiTu,reportdate);

		// 中转出站在途 - “中转出站”
		// flowordertype=6 and cwbstate = 6 and cwbordertypeid = 1;
		cwbStateEnum = CwbStateEnum.ZhongZhuan;
		flowOrderTypeEnum = FlowOrderTypeEnum.ChuKuSaoMiao;
		orderSnapshots = this.orderDetailsSnapshotDao
				.listByFlowordertypeAndOrdertypeidAndCwbstate(
						flowOrderTypeEnum.getValue(),
						cwbOrderTypeIdEnum.getValue(), cwbStateEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.ZhongZhuanChuZhanZaiTu,reportdate);

		// 中转库入库未出库 - “中转站入库”
		// flowordertype=12 and cwbordertypeid = 1
		flowOrderTypeEnum = FlowOrderTypeEnum.ZhongZhuanZhanRuKu;
		orderSnapshots = this.orderDetailsSnapshotDao
				.listByFlowordertypeAndOrdertypeid(
						flowOrderTypeEnum.getValue(),
						cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.ZhongZhuanKuRuKu,reportdate);

		// 中转出库未到站 - “中转站出库”
		// flowordertype=14 and cwbordertypeid = 1 ;
		flowOrderTypeEnum = FlowOrderTypeEnum.ZhongZhuanZhanChuKu;
		orderSnapshots = this.orderDetailsSnapshotDao
				.listByFlowordertypeAndOrdertypeid(
						flowOrderTypeEnum.getValue(),
						cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.ZhongZhuanKuChuKu,reportdate);

		// 退货入库未出库 - “退货站入库”&“供货商拒收返库”& "审核为退货再投"
		// flowordertype in(15,28,45) and cwbordertypeid = 1 and state =1;

		String flowordertypes = StringUtils
				.join(new Object[] {
						FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(),
						FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue(),
						FlowOrderTypeEnum.GongYingShangJuShouFanKu
								.getValue() }, ",");
		orderSnapshots = this.orderDetailsSnapshotDao
				.listByFlowordertypesAndOrdertypeid(flowordertypes,
						cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.TuiHuoKuRuKu,reportdate);

		// 退货再投未到站 - “退货再投”
		// fn_order_details_snapshot s left join express_set_branch b on s.startbranchid = b.branchid  
		// where flowordertype=6 and cwbstate = 1 and cwbordertypeid = 1 and sitetype = 3 and state =1;
		orderSnapshots = this.orderDetailsSnapshotDao.getTuiHuoZaiTouWeiDaoZhan(cwbOrderTypeIdEnum.getValue(),reportdate);
		
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.TuiHuoZaiTou,reportdate);

		// 退客户在途 - “退供货商出库”
		// flowordertype=27 and cwbordertypeid = 1 ;
		flowOrderTypeEnum = FlowOrderTypeEnum.TuiGongYingShangChuKu;
		orderSnapshots = this.orderDetailsSnapshotDao
				.listByFlowordertypeAndOrdertypeid(
						flowOrderTypeEnum.getValue(),
						cwbOrderTypeIdEnum.getValue(),reportdate);
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.TuiKeHuZaiTu,reportdate);

		// 退客户未收款 - “退供货商成功”
		// flowordertype=34 and cwbordertypeid = 1 ;
//		flowOrderTypeEnum = FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong;
//		orderSnapshots = this.orderDetailsSnapshotDao
//				.listByFlowordertypeAndOrdertypeid(
//						flowOrderTypeEnum.getValue(),
//						cwbOrderTypeIdEnum.getValue(),reportdate);
		
		orderSnapshots = this.orderDetailsSnapshotDao.getListTuiKeHuNotReturned(cwbOrderTypeIdEnum.getValue(),reportdate);
		
		_genLifeCycleReportByCwbOrderSnapshotList(orderSnapshots,
				CwbOrderLifeCycleTypeIdEnum.TuiKeHuWeiShouKuan,reportdate);


		logger.info("[OrderLifeCycleReportService][genLifeCycleReport][success]");

	}

	/**
	 * 根据customer_id汇总票数和金额数据到数据库
	 * 
	 * @param orderSnapshots
	 *            订单快照信息列表
	 * @param orderLifeCycleTypeIdEnum
	 *            订单生命周期类型枚举
	 *
	 */
	private void _genLifeCycleReportByCwbOrderSnapshotList(
			List<CwbOrderSnapshot> orderSnapshots,
			CwbOrderLifeCycleTypeIdEnum orderLifeCycleTypeIdEnum,int reportdate) {

		Map<Long, OrderLifeCycleReportVO> customer2ReportMap = new HashMap<Long, OrderLifeCycleReportVO>();

		long customerid = 0L;
		BigDecimal tmpAmount = null;
		int tmpCount = 0;

		if (CollectionUtils.isNotEmpty(orderSnapshots)) {
			OrderLifeCycleReportVO tmpOrderLifeCycleReportVO = null;

			// 遍历，以customerid做聚合
			for (CwbOrderSnapshot cwbOrderSnapshot : orderSnapshots) {
				// 以customerid做聚合
				customerid = cwbOrderSnapshot.getCustomerid();

				tmpOrderLifeCycleReportVO = customer2ReportMap.get(customerid);
				// 如果map中不存在，则新建
				if (tmpOrderLifeCycleReportVO == null) {
					tmpOrderLifeCycleReportVO = new OrderLifeCycleReportVO();
					tmpOrderLifeCycleReportVO.setCustomerid(customerid);
					tmpOrderLifeCycleReportVO.setAmount(cwbOrderSnapshot
							.getReceivablefee());
					tmpOrderLifeCycleReportVO.setCount(1);// 新建的时候，默认是1
					tmpOrderLifeCycleReportVO
							.setTypeid(orderLifeCycleTypeIdEnum.getValue());

					customer2ReportMap.put(customerid,
							tmpOrderLifeCycleReportVO);

				} else {// 如果map中存在，则在对象中取出相应的值做计算
					tmpAmount = tmpOrderLifeCycleReportVO.getAmount();
					tmpCount = tmpOrderLifeCycleReportVO.getCount();

					if (tmpAmount != null) {
						tmpAmount = tmpAmount.add(cwbOrderSnapshot
								.getReceivablefee());
					} else {
						tmpAmount = BigDecimal.ZERO;
					}

					tmpCount += 1;

					tmpOrderLifeCycleReportVO.setAmount(tmpAmount);
					tmpOrderLifeCycleReportVO.setCount(tmpCount);
				}

				// 保存关联的订单号
				tmpOrderLifeCycleReportVO.addOrderSnapshotId(String.valueOf(cwbOrderSnapshot.getId()));
				tmpOrderLifeCycleReportVO.setReportdate(reportdate);
			}

		}

		// 获取聚合后的报表列表
		Collection<OrderLifeCycleReportVO> orderLifeCycleReportVOs = customer2ReportMap
				.values();

		for (OrderLifeCycleReportVO orderLifeCycleReportVO : orderLifeCycleReportVOs) {

			// 插入报表
			this.orderLifeCycleReportDao
					.insertOrderDetailSnapshot(orderLifeCycleReportVO);

			// 创建订单生命周期报表与订单详情快照的关联关系
			_createMappingBtwOrderSnapShotAndLifeCycle(orderLifeCycleReportVO);

		}

	}

	/**
	 * 创建订单生命周期报表与订单详情快照的关联关系
	 * 
	 * @param orderSnapshots
	 *            订单快照信息列表
	 * @param orderLifeCycleTypeIdEnum
	 *            订单生命周期类型枚举
	 *
	 */
	private void _createMappingBtwOrderSnapShotAndLifeCycle(
			OrderLifeCycleReportVO orderLifeCycleReportVO) {
		List<String> orderSnapshotIdList = orderLifeCycleReportVO.getOrderSnapshotIdList();

		int total = orderSnapshotIdList.size();
		final int maxPageSiz = 1000;
		long lifecycleid = orderLifeCycleReportVO.getId();

		logger.info(
				"[OrderLifeCycleReportService][_createMappingBtwOrderSnapShotAndLifeCycle][total {} cwbs in report {}，]",
				total, orderLifeCycleReportVO.getTypeid());

		if(total > 0){
			// 如果，大于最好处理数，则分批次处理
			if (total >= maxPageSiz) {

				int batch = getBatch(total, maxPageSiz);
				List<String> batchCwbList = null;

				for (int i = 0, startIdx = 0, endIdx = 0; i < batch; i++) {

					startIdx = maxPageSiz * i;
					endIdx = maxPageSiz * (i + 1);
					endIdx = endIdx >= total ? total : endIdx;

					logger.debug(
							"[_createMappingBtwOrderSnapShotAndLifeCycle][startIdx= {}, endIdx = {}，]",
							startIdx, endIdx);

					batchCwbList = orderSnapshotIdList.subList(startIdx, endIdx);

					updateLifecycleReportIdByCwbList(lifecycleid, batchCwbList);
				}

			} else {

				updateLifecycleReportIdByCwbList(lifecycleid, orderSnapshotIdList);
			}
		}
		

	}

	/**
	 * Remove the duplicate row with key - cwb.
	 * 
	 * @param orderFlows
	 * @return
	 *
	 */
	private List<OrderFlow> removeDuplicatedRecordWithCwb(
			List<OrderFlow> orderFlows) {

		List<OrderFlow> resultList = new ArrayList<OrderFlow>();
		Set<String> matchCache = new HashSet<String>();
		String cwb = null;

		for (OrderFlow orderFlow : orderFlows) {

			cwb = orderFlow.getCwb();
			if (!matchCache.contains(cwb)) {

				resultList.add(orderFlow);

				matchCache.add(cwb);
			}
		}

		return resultList;
	}

	/**
	 * 更新订单详情快照表的fnrptlifecycleid
	 */
	private void updateLifecycleReportIdByCwbList(long lifecycleid,
			List<String> cwbList) {
		String orderSnapshotIds = StringUtils.join(cwbList, ",");
		logger.debug("[updateLifecycleReportIdByCwbList][lifecycleid {},orderSnapshotId {}，]",
				lifecycleid, orderSnapshotIds);

		this.orderDetailsSnapshotDao.updateLifecycleReportIdByKeys(lifecycleid,
				orderSnapshotIds);
	}

	/**
	 * 从<code>OrderFlow</code>的json串解释出订单详情对象中, 如果json反序列化失败，则返回null
	 * 
	 * @param orderFlow
	 *            订单操作流程
	 * @return CwbOrder 订单详情,如果json反序列化失败，则返回null
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	private CwbOrder getCwbOrderFromOrderFlow(OrderFlow orderFlow) {
		CwbOrder cwbOrder = null;

		String json = orderFlow.getFloworderdetail();

		if (StringUtils.isNotBlank(json)) {
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState;
			try {
				cwbOrderWithDeliveryState = JsonUtil.readValue(json,
						CwbOrderWithDeliveryState.class);
				if (cwbOrderWithDeliveryState != null) {
					cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
				}
			} catch (Exception e) {
				logger.error(
						"[getCwbOrderFromOrderFlow][parse cwb {} 's order flow json error: {}",
						orderFlow.getCwb(), e.getMessage());
			}

		}

		return cwbOrder;
	}

	/**
	 * 获取汇总的数据信息
	 */
	private List<LinkedHashMap<String, Object>> getSummaryFooter(
			List<OrderLifeCycleReportVO> rows) {

		List<LinkedHashMap<String, Object>> footer = new ArrayList<LinkedHashMap<String, Object>>();

		LinkedHashMap<String, Object> foot = new LinkedHashMap<String, Object>();

		BigDecimal amount1 = BigDecimal.ZERO;
		BigDecimal amount2 = BigDecimal.ZERO;
		BigDecimal amount3 = BigDecimal.ZERO;
		BigDecimal amount4 = BigDecimal.ZERO;
		BigDecimal amount5 = BigDecimal.ZERO;
		BigDecimal amount6 = BigDecimal.ZERO;
		BigDecimal amount7 = BigDecimal.ZERO;
		BigDecimal amount8 = BigDecimal.ZERO;
		BigDecimal amount9 = BigDecimal.ZERO;
		BigDecimal amount10 = BigDecimal.ZERO;
		BigDecimal amount11 = BigDecimal.ZERO;
		BigDecimal amount12 = BigDecimal.ZERO;
		BigDecimal amount13 = BigDecimal.ZERO;
		BigDecimal amount14 = BigDecimal.ZERO;

		Integer count1 = 0;
		Integer count2 = 0;
		Integer count3 = 0;
		Integer count4 = 0;
		Integer count5 = 0;
		Integer count6 = 0;
		Integer count7 = 0;
		Integer count8 = 0;
		Integer count9 = 0;
		Integer count10 = 0;
		Integer count11 = 0;
		Integer count12 = 0;
		Integer count13 = 0;
		Integer count14 = 0;

		for (OrderLifeCycleReportVO row : rows) {

			amount1 = bigDecimalAdd(amount1, row.getAmount1());
			amount2 = bigDecimalAdd(amount2, row.getAmount2());
			amount3 = bigDecimalAdd(amount3, row.getAmount3());
			amount4 = bigDecimalAdd(amount4, row.getAmount4());
			amount5 = bigDecimalAdd(amount5, row.getAmount5());
			amount6 = bigDecimalAdd(amount6, row.getAmount6());
			amount7 = bigDecimalAdd(amount7, row.getAmount7());
			amount8 = bigDecimalAdd(amount8, row.getAmount8());
			amount9 = bigDecimalAdd(amount9, row.getAmount9());
			amount10 = bigDecimalAdd(amount10, row.getAmount10());
			amount11 = bigDecimalAdd(amount11, row.getAmount11());
			amount12 = bigDecimalAdd(amount12, row.getAmount12());
			amount13 = bigDecimalAdd(amount13, row.getAmount13());
			amount14 = bigDecimalAdd(amount14, row.getAmount14());
			count1 = count1 + row.getCount1();
			count2 = count2 + row.getCount2();
			count3 = count3 + row.getCount3();
			count4 = count4 + row.getCount4();
			count5 = count5 + row.getCount5();
			count6 = count6 + row.getCount6();
			count7 = count7 + row.getCount7();
			count8 = count8 + row.getCount8();
			count9 = count9 + row.getCount9();
			count10 = count10 + row.getCount10();
			count11 = count11 + row.getCount11();
			count12 = count12 + row.getCount12();
			count13 = count13 + row.getCount13();
			count14 = count14 + row.getCount14();
		}

		foot.put("amount1", amount1);
		foot.put("amount2", amount2);
		foot.put("amount3", amount3);
		foot.put("amount4", amount4);
		foot.put("amount5", amount5);
		foot.put("amount6", amount6);
		foot.put("amount7", amount7);
		foot.put("amount8", amount8);
		foot.put("amount9", amount9);
		foot.put("amount10", amount10);
		foot.put("amount11", amount11);
		foot.put("amount12", amount12);
		foot.put("amount13", amount13);
		foot.put("amount14", amount14);

		foot.put("count1", count1);
		foot.put("count2", count2);
		foot.put("count3", count3);
		foot.put("count4", count4);
		foot.put("count5", count5);
		foot.put("count6", count6);
		foot.put("count7", count7);
		foot.put("count8", count8);
		foot.put("count9", count9);
		foot.put("count10", count10);
		foot.put("count11", count11);
		foot.put("count12", count12);
		foot.put("count13", count13);
		foot.put("count14", count14);
		foot.put("customername", "汇总");

		footer.add(foot);

		return footer;
	}

	private BigDecimal bigDecimalAdd(BigDecimal a, BigDecimal b) {
		if (b != null) {
			return a.add(b);
		} else {
			return a;
		}
	}

	private int getBatch(long countOfRecord, int pageSize) {
		// 计算批次
		int batch = 0;
		if ((countOfRecord % pageSize) == 0) {
			batch = (int) (countOfRecord / pageSize);
		} else {
			batch = (int) (countOfRecord / pageSize + 1);
		}
		return batch;

	}
}
