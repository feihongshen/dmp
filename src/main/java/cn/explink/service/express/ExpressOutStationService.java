package cn.explink.service.express;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchRouteDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbStateControlDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OrderFlowLogDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.ExpressOutStationInfoDao;
import cn.explink.domain.Bale;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchRoute;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.VO.express.ExpressOutStationParamsVO;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.BranchTypeEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.enumutil.express.ExpressOutStationFlagEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbRouteService;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;

import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;

/**
 * 揽件出站
 *
 * @author jiangyu 2015年8月4日
 *
 */
@Service
@Transactional
public class ExpressOutStationService {

	private Logger logger = LoggerFactory.getLogger(ExpressOutStationService.class);

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BaleDao baleDAO;
	@Autowired
	BranchRouteDAO branchRouteDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	CwbStateControlDAO cwbStateControlDAO;
	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	OrderFlowLogDAO orderFlowLogDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;

	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	ExpressOutStationInfoDao expressOutStationInfoDao;

	/**
	 * 判断是订单号操作还是包号操作
	 *
	 * @param sessionUser
	 * @param scanNo
	 * @param params
	 */
	public Map<String, Object> checkIsOrderOrBaleOperation(String scanNo, ExpressOutStationParamsVO params) {
		Map<String, Object> checkMap = new HashMap<String, Object>();

		// 通过扫描的号码查询订单表和包号表
		CwbOrder order = this.cwbDAO.getCwbByCwb(scanNo);
		//根据包号获取是否为合包
		Bale bale = this.baleDAO.getBaleOnway(scanNo);
		// 标识包号是否存在
		Boolean baleIsExist = false;
		if (bale!=null) {
			baleIsExist = true;
		}
		// 查询的时候要加上订单类型为快递
		if ((order == null) && !baleIsExist) {
			checkMap.put("opeFlag", ExpressOutStationFlagEnum.OrderNo.getValue());
			throw new CwbException(scanNo, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		} else if ((order != null) && !baleIsExist) {
			checkMap.put("opeFlag", ExpressOutStationFlagEnum.OrderNo.getValue());
			checkMap.put("order", order);
		} else if ((order == null) && baleIsExist) {
			checkMap.put("opeFlag", ExpressOutStationFlagEnum.BaleNo.getValue());
			checkMap.put("bale", bale);
		}
		return checkMap;
	}

	/**
	 * 订单执行揽件入站操作
	 *
	 * @param user
	 * @param scanNo
	 * @param params
	 * @return
	 */
	public ExpressOpeAjaxResult executeOutStationOpeOrderNo(User user, String scanNo, ExpressOutStationParamsVO params) {
		//ajax返回信息
		ExpressOpeAjaxResult res = new ExpressOpeAjaxResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			this.logger.info("开始出库处理,cwb:{}", scanNo);
			// 获取当前机构
			Long currentBranchId = user.getBranchid();
			//获取下一站
			Long nextBranchId = params.getNextBranch();
			//根据下一站获取机构
			Branch nextBranch = this.branchDAO.getBranchByBranchid(nextBranchId);
			//获取订单信息
			CwbOrder order = this.cwbDAO.getCwbByCwbLock(scanNo);
			// 1.操作订单号的标识
			resultMap.put("opeFlag", ExpressOutStationFlagEnum.OrderNo.getValue());
			// 2.订单操作出站
			CwbOrder opeOrder = this.expressOrderIntoStationOperation(scanNo, order, currentBranchId, nextBranchId, params, user, false);
			// jms调用接口
			this.executeTpsInterface(opeOrder, user, nextBranch == null ? "" : nextBranch.getTpsbranchcode() + "");
			// 3.下一站转换
			resultMap = this.orgnizeAttributes(resultMap, "nextBranchName", opeOrder.getNextbranchid(), params.getContextPath());
			// 4.目的站转换
			resultMap = this.orgnizeAttributes(resultMap, "deliveryBranchName", opeOrder.getDeliverybranchid(), params.getContextPath());
			// 5.结果的封装
			res.setObj(opeOrder);
			// 6.操作成功或失败的标识
			res.setStatus(true);
		} catch (CwbException e) {
			res.setStatus(false);
			res.setMsg(e.getMessage());
		}
		res.setAttributes(resultMap);
		return res;
	}

	/**
	 * 调用Tps接口
	 *
	 * @param transNo
	 * @param user
	 */
	private void executeTpsInterface(CwbOrder opeOrder, User user, String nextBranchCode) {
		// this.executeTpsInterface4TransNoTrace(transNo);这个说是不要用的说 10.19
		// 如果不是快递单，则不需要此操作--刘武强10.19
		if (opeOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			this.executeTpsTransInterface4TransFeedBack(opeOrder.getCwb(), user, nextBranchCode);
		}
	}

	/**
	 * 调用Tps接口 运单轨迹查询
	 */
	private void executeTpsInterface4TransNoTrace(String transNo) {
		// 运单轨迹查询接口
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOTraceQuery);
		// 设置预订单号
		paramObj.setTransNo(transNo);
		// 发送JMS消息
		this.tpsInterfaceExecutor.executTpsInterface(paramObj);

	}

	/**
	 * 调用tps运单反馈接口
	 *
	 * @param orders
	 */
	public void executeTpsTransInterface4TransFeedBack(String transNo, User user, String nextBranchCode) {
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
		PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		transNoFeedBack.setTransportNo(transNo);
		transNoFeedBack.setOperateOrg(branch.getTpsbranchcode());
		// add by wangzy 新增加下一站机构的参数
		transNoFeedBack.setNextOrg(nextBranchCode);
		transNoFeedBack.setOperater(user.getRealname());
		transNoFeedBack.setOperateTime(System.currentTimeMillis());
		transNoFeedBack.setOperateType(FeedbackOperateTypeEnum.OutboundScan.getValue());// 对应接口文档【枚举对应】
		transNoFeedBack.setReason("");
		/*
		 * //拼接描述 JoinMessageVO contextVar = new JoinMessageVO();
		 * contextVar.setOperationType
		 * (TpsOperationEnum.ArrivalScan.getValue());//揽件入站对应入站扫描
		 * contextVar.setStation(branch.getBranchname());//站点名称
		 * contextVar.setOperator(user.getRealname());
		 * contextVar.setNextStation(
		 * this.branchDAO.getBranchByBranchid(nextBranchId).getBranchname());
		 * contextVar.connectMessage();
		 * transNoFeedBack.setTransportDetail(contextVar.getTrackMessage());
		 */
		paramObj.setTransNoFeedBack(transNoFeedBack);
		// 发送JMS消息
		this.tpsInterfaceExecutor.executTpsInterface(paramObj);
	}

	/**
	 * 执行包号操作的揽件出站[特别声明，由于合包操作的的时候，没有校验订单的信息，所以这边出站的话会出现错误]
	 *
	 * @param user
	 * @param scanNo
	 * @param params
	 * @return
	 */
	public ExpressOpeAjaxResult executeOutStationOpeBaleNo(User user, String scanNo, ExpressOutStationParamsVO params) {
		ExpressOpeAjaxResult result = new ExpressOpeAjaxResult();
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("opeFlag", ExpressOutStationFlagEnum.BaleNo.getValue());
		// 站点信息
		long currentBranchId = user.getBranchid();
		long nextBranchId = params.getNextBranch();
		// 1.获取包
		Bale bale = this.baleDAO.getBaleOnway(scanNo);
		if (bale == null) {
			// add by wangzhiyu 当包号不可用时进行错误提示
			throw new CwbException(scanNo, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.BaleCannotUse);
		}else{
			Long successCount = 0L;
			Long failCount = 0L;
			// 2.获取包实体
			// 3.包的信息校验
			this.baleOpeIntoStationValidation(params, currentBranchId, bale);
			// 4.查询包号中的订单循环执行订单的操作出站
			List<CwbOrder> cwbOrderList = this.cwborderService.getListByBale(bale.getId(),0);
			List<CwbOrder> errorList = new ArrayList<CwbOrder>();
			List<CwbOrder> succList = new ArrayList<CwbOrder>();
			String tempOrderNo = "";// 缓存订单号
			// try {
			successCount = this.expressOrderBatchIntoStation(user, params, currentBranchId, nextBranchId, successCount, bale, cwbOrderList, succList, tempOrderNo);
			if (bale.getNextbranchid() != 0) {
				baleDAO.updateBranchIdAndNextBranchId(bale.getId(), params.getNextBranch(), 0);
				Branch branch = this.branchDAO.getBranchByBranchid(bale.getNextbranchid());
				resMap.put("nextBranchName", branch.getBranchname());
			//add by 王志宇
			}else{
				//将下一站更新到包表中
				baleDAO.updateBranchIdAndNextBranchId(bale.getId(), params.getNextBranch(), 0);
				resMap.put("nextBranchName", this.branchDAO.getBranchByBranchid(params.getNextBranch()).getBranchname());
			}
			if (bale.getBranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(bale.getBranchid());
				resMap.put("deliveryBranchName", branch.getBranchname());
			}
			resMap.put("successCount", successCount);
			resMap.put("failCount", failCount);
			resMap.put("successList", succList);
			resMap.put("errorList", errorList);
			result.setObj(bale);
			result.setStatus(true);

			// modified by songkaojun 2015-10-30 为了实现成功一起成功失败一起失败，异常不在这里捕获
			// } catch (CwbException e) {
			// resMap.put("opeFlag",
			// ExpressOutStationFlagEnum.OrderNo.getValue());
			// result.setStatus(false);
			// result.setMsg("订单：" + tempOrderNo +
			// (e.getMessage().replace(bale.getBaleno(), "").replace("/包号",
			// "")));
			// result.setStatus(false);
			// }
		}
		result.setAttributes(resMap);
		return result;
	}

	@Transactional
	private Long expressOrderBatchIntoStation(User user, ExpressOutStationParamsVO params, long currentBranchId, long nextBranchId, Long successCount, Bale bale, List<CwbOrder> cwbOrderList,
			List<CwbOrder> succList, String tempOrderNo) {
		if ((cwbOrderList != null) && (cwbOrderList.size() > 0)) {
			for (CwbOrder order : cwbOrderList) {
				tempOrderNo = order.getCwb();
				CwbOrder opeOrder = this.expressOrderIntoStationOperation(order.getCwb(), order, currentBranchId, nextBranchId, params, user, true);
				succList.add(opeOrder);
				Branch nextBranch = this.branchDAO.getBranchByBranchid(nextBranchId);
				String nextBranchCode = nextBranch.getTpsbranchcode();
				// 运单状态反馈接口
				this.executeTpsInterface(order, user, nextBranchCode);
				successCount++;
			}
			// 更新包的状态以及定相关信息的修改
			if (successCount > 0) {
				// 更改包的状态
				logger.info("expressOrderBatchIntoStation,更改包的状态,包号："+bale.getBaleno() + " , 当前包状态：" + bale.getBalestate() + " , 更改后的包状态："+BaleStateEnum.YiFengBaoChuKu.getValue());
				this.baleDAO.updateBalesateAndNextBranchId(bale.getId(), BaleStateEnum.YiFengBaoChuKu.getValue(), nextBranchId, currentBranchId);
			}

		}
		return successCount;
	}

	/**
	 * 订单的入站操作
	 *
	 * @param scanNo
	 * @param order
	 * @param currentBranchId
	 * @param pageNextBranchId
	 * @param params
	 * @param user
	 * @param isBale
	 * @return
	 */
	private CwbOrder expressOrderIntoStationOperation(String scanNo, CwbOrder order, long currentBranchId, long pageNextBranchId, ExpressOutStationParamsVO params, User user, Boolean isBale) {
		// 1.操作之前的校验
		this.orderIntoStationValidation(scanNo, params, currentBranchId, pageNextBranchId, order, isBale);

		// 2.揽件出站的逻辑操作
		Branch ifBranch = this.branchDAO.getQueryBranchByBranchid(currentBranchId);
		//货物流向确认
		boolean routeFlag = false;
		if ((ifBranch != null) && (ifBranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {// 当前站不为空&&当前机构为站点
			//获取当前站点的货物流向
			List<BranchRoute> routelist = this.branchRouteDAO.getBranchRouteByWheresql(currentBranchId, pageNextBranchId, 2);
			for (BranchRoute r : routelist) {
				//下一站点==站点的货物流向
				if (pageNextBranchId == r.getToBranchId()) {
					routeFlag = true;
				}
			}
			//订单操作状态不是运单录入&&订单的下一站不为0&&没有下一站点的货物流向&&下一站>0
			if ((order.getFlowordertype() != FlowOrderTypeEnum.YunDanLuRu.getValue()) && (order.getNextbranchid() != 0) && !routeFlag && (pageNextBranchId > 0)) {
				throw new CwbException(scanNo, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(order.getNextbranchid())
						.getBranchname());
			}
			//订单操作状态不是运单录入&&订单下一站不为0&&订单的下一站不等于货物流向的下一站&& 货物流向的下一站不为0
		} else if ((order.getFlowordertype() != FlowOrderTypeEnum.YunDanLuRu.getValue()) && (order.getNextbranchid() != 0) && (order.getNextbranchid() != pageNextBranchId) && (pageNextBranchId > 0)) {
			throw new CwbException(scanNo, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(order.getNextbranchid())
					.getBranchname());
		}
		//订单操作状态等于揽件入站||订单状态为到错货||订单状态为已审核||订单状态为分站滞留
		if (((order.getFlowordertype() == FlowOrderTypeEnum.LanJianRuZhan.getValue()) || (order.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || ((order
				.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) && (order.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue())))
				&& (order.getCurrentbranchid() != currentBranchId)) {
			throw new CwbException(scanNo, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.FEI_BEN_ZHAN_HUO);
		}
		//获取当前站点的信息
		Branch userbranch = this.branchDAO.getBranchById(currentBranchId);
		//订单的当前站点
		Branch cwbBranch = this.branchDAO.getBranchByBranchid(order.getCurrentbranchid() == 0 ? order.getNextbranchid() : order.getCurrentbranchid());
		if ((cwbBranch.getBranchid() != pageNextBranchId) && (userbranch.getSitetype() != BranchEnum.ZhongZhuan.getValue()) && (cwbBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			throw new CwbException(scanNo, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.ZHONG_ZHUAN_HUO);
		}

		// 出站的逻辑处理
		this.handleOutStation(user, scanNo, currentBranchId, pageNextBranchId, order, FlowOrderTypeEnum.ChuKuSaoMiao, false, routeFlag);

		return this.cwbDAO.getCwbByCwb(scanNo);
	}

	/**
	 * 订单操作揽件出站前的校验
	 *
	 * @param scanNo
	 *            订单号
	 * @param params
	 *            参数
	 * @param currentBranchId
	 *            当前站
	 * @param nextBranchId
	 *            下一站
	 * @param order
	 *            订单
	 * @param isBale
	 */
	private void orderIntoStationValidation(String scanNo, ExpressOutStationParamsVO params, Long currentBranchId, Long nextBranchId, CwbOrder order, Boolean isBale) {
		Branch nextBranch = this.branchDAO.getBranchByBranchid(order.getNextbranchid());
		Branch pageBranch = this.branchDAO.getBranchByBranchid(params.getNextBranch());
		// 订单号操作 ---订单是否在包中
		if (!Tools.isEmpty(order.getPackagecode()) && !isBale) {// 包号是否为空
			// 将包号置为不可用
			Bale bale=this.baleDAO.getBaleOnway(order.getPackagecode());
			if(bale!=null){
				logger.info("orderIntoStationValidation，订单操作揽件出站前的校验，包号：" + bale.getBaleno());
				this.baleDAO.updateBalesate(bale.getId(), BaleStateEnum.BuKeYong.getValue());
			}
			// throw new CwbException(scanNo,
			// FlowOrderTypeEnum.FenZhanLingHuo.getValue(),
			// ExceptionCwbErrorTypeEnum.Bale_Error1, scanNo,
			// order.getPackagecode(), ",请扫描包号");
		}

		// 重复出站的校验
		if (// 上一站==当前站&&(下一站==下一站||下一站==当前站||下一站不存在)&&订单的操作状态为：领货
		(order.getStartbranchid() == currentBranchId.longValue())
				&& ((order.getNextbranchid() == nextBranchId.longValue()) || (nextBranchId.longValue() == -1) || (nextBranchId.longValue() == 0) || (order.getNextbranchid() == currentBranchId
						.longValue())) && (order.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue())) {

			throw new CwbException(scanNo, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.REPEATED_OUT_STATION, StringUtil.nullOrEmptyStringConvertToEmpty(nextBranch
					.getBranchname()));// ,StringUtil.nullOrEmptyStringConvertToEmpty(nextBranch.getBranchname())

		} else if (// 上一站==当前站&&订单的操作状态：领货
		(order.getStartbranchid() == currentBranchId.longValue()) && (order.getFlowordertype() == FlowOrderTypeEnum.LanJianChuZhan.getValue())) {
			throw new CwbException(scanNo, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.REPEATED_OUT_STATION, StringUtil.nullOrEmptyStringConvertToEmpty(nextBranch
					.getBranchname()));// ,StringUtil.nullOrEmptyStringConvertToEmpty(nextBranch.getBranchname())
		}

		// 当前站校验
		if (order.getCurrentbranchid() != currentBranchId) {// 非本站订单
			throw new CwbException(scanNo, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.CURRENT_BRANCH_NOT_MATCH, StringUtil.nullOrEmptyStringConvertToEmpty(nextBranch
					.getBranchname()));
		}

		// 非快递单不允许揽件出站
		if (!this.validateIsExpressOrder(order)) {// 校验是否是快递类型的订单
			throw new CwbException(scanNo, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.NOT_EXPRESS_CWB, scanNo);
		}

		//如果快递单的上上一个状态是揽件出站，上一个状态站点到货，那么现在做揽件出站就不校验下一站和状态了，因为允许快递单在站点之间转移--刘武强20160706
		boolean alowFlag =  this.getAlowFlag(scanNo);
		
		if(!alowFlag){
			// 下一站校验
			if ((order.getNextbranchid() != 0) && (order.getNextbranchid() != params.getNextBranch().longValue())) {// 页面下一站和订单的下一站是否一致
				throw new CwbException(scanNo, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.NEXT_BRANCH_NOT_MATCH, StringUtil.nullOrEmptyStringConvertToEmpty(pageBranch
						.getBranchname()), StringUtil.nullOrEmptyStringConvertToEmpty(nextBranch.getBranchname()));
			}
			// 没有做揽件入站的快递单不能操作出站
			if (!this.validateIsIntoStation(order)) {
				throw new CwbException(scanNo, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.NotIntoStation);
			}
		}
	}

	/**
	 * 操作状态的校验
	 *
	 * @param order
	 * @return
	 */
	private boolean validateIsIntoStation(CwbOrder order) {
		if (FlowOrderTypeEnum.LanJianRuZhan.getValue() != order.getFlowordertype()) {
			return false;
		}
		return true;
	}

	/**
	 * 包号单独的校验
	 *
	 * @param params
	 * @param currentBranchId
	 * @param bale
	 */
	private void baleOpeIntoStationValidation(ExpressOutStationParamsVO params, Long currentBranchId, Bale bale) {
		Branch nextBranch = this.branchDAO.getBranchByBranchid(bale.getNextbranchid());
		Branch pageBranch = this.branchDAO.getBranchByBranchid(params.getNextBranch());
//		if ((bale.getBranchid() == currentBranchId.longValue()) && (bale.getBalestate() == BaleStateEnum.YiFengBaoChuKu.getValue())) {
		if(bale.getBranchid()==0 && bale.getNextbranchid()==params.getNextBranch()){
			// 包重复出站操作异常[下一站]
			throw new CwbException(params.getScanNo(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.REPEATED_OUT_STATION,
					StringUtil.nullOrEmptyStringConvertToEmpty(nextBranch.getBranchname()));// ,StringUtil.nullOrEmptyStringConvertToEmpty(nextBranch.getBranchname())
		}

		// 非本站的校验
		if (bale.getBranchid() != currentBranchId.longValue()) {
			// 非本站的包，下一站为
			throw new CwbException(params.getScanNo(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.CURRENT_BRANCH_NOT_MATCH,
					StringUtil.nullOrEmptyStringConvertToEmpty(nextBranch.getBranchname()));
		}

		// 下一站的校验
		if ((bale.getNextbranchid() != 0) && bale.getNextbranchid() != params.getNextBranch().longValue()) {
			// 包的下一站不匹配
			throw new CwbException(params.getScanNo(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.NEXT_BRANCH_NOT_MATCH,
					StringUtil.nullOrEmptyStringConvertToEmpty(pageBranch.getBranchname()), StringUtil.nullOrEmptyStringConvertToEmpty(nextBranch.getBranchname()));
		}
	}

	/**
	 * 处理揽件出站
	 */
	private void handleOutStation(User user, String scanNo, long currentBranchId, long pageNextBranchId, CwbOrder order, FlowOrderTypeEnum flowOrderTypeEnum, boolean isBale, boolean routeFlag) {
		this.cwbOrderService.validateCwbState(order, flowOrderTypeEnum);
		if ((order.getFlowordertype() != FlowOrderTypeEnum.LanJianChuZhan.getValue()) || (order.getStartbranchid() != currentBranchId)) {
			this.validateStateTransfer(order, FlowOrderTypeEnum.LanJianChuZhan);// FenZhanLingHuo
		}
		// 重新计算下一站
		if (!isBale) {
			pageNextBranchId = this.getNextBranchidExecute(pageNextBranchId, false, order, user, routeFlag);
		}

		this.cwbDAO.updateCwbByExpressOutStation(order.getCwb(), order.getSendcarnum(), currentBranchId, pageNextBranchId, user);
		// 创建流程跟踪信息表
		this.cwbOrderService.createFloworder(user, currentBranchId, order, FlowOrderTypeEnum.LanJianChuZhan, "", System.currentTimeMillis());
		// 创建揽件出站信息表
		Branch branchNext = this.branchDAO.getBranchById(currentBranchId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String str = sdf.format(date);
		// List<ExpressOutStationInfo> aaa =
		// this.expressOutStationInfoDao.getAllOutStationInfo();
		this.expressOutStationInfoDao.creatOutStationInfo(order.getOpscwbid(), order.getCwb(), currentBranchId, branchNext.getBranchname(), str, user.getUserid(), user.getRealname(),
				order.getPaywayid(), order.getCollectorid());
	}

	/**
	 * 组织结果的属性[下一站和目的站]
	 *
	 * @param resultMap
	 * @param flag
	 * @param branchId
	 * @param contextPath
	 * @return
	 */
	private Map<String, Object> orgnizeAttributes(Map<String, Object> resultMap, String flag, Long branchId, String contextPath) {
		if (branchId.longValue() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(branchId);
			resultMap.put(flag, branch.getBranchname());
			resultMap.put(flag + "Wav", contextPath + ServiceUtil.wavPath + branch.getBranchwavfile());
		} else {
			resultMap.put(flag, "");
			resultMap.put(flag + "Wav", "");
		}
		return resultMap;
	}

	/**
	 * 校验是否是快递单
	 *
	 * @param order
	 * @return
	 */
	private boolean validateIsExpressOrder(CwbOrder order) {
		return (CwbOrderTypeIdEnum.Express.getValue() == order.getCwbordertypeid());
	}

	private void validateStateTransfer(CwbOrder co, FlowOrderTypeEnum nextState) {
		CwbFlowOrderTypeEnum fromstate = CwbFlowOrderTypeEnum.getText((int) co.getFlowordertype());
		if ((fromstate != null) && (this.cwbStateControlDAO.getCwbStateControlByParam((int) co.getFlowordertype(), nextState.getValue()) == null)) {
			throw new CwbException(co.getCwb(), nextState.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, fromstate.getText(), nextState.getText());
		}
	}
	/**
	 * 
	 * @param branchid 下一站
	 * @param forceOut
	 * @param co 订单
	 * @param user 
	 * @param aflag
	 * @return
	 */
	private long getNextBranchidExecute(long branchid, boolean forceOut, CwbOrder co, User user, boolean aflag) {
		long nextBranchid = this.getNextBranchId(co, user);
		long currentbranchtype = this.branchDAO.getBranchByBranchid(user.getBranchid()).getSitetype();
		long type = this.branchDAO.getBranchByBranchid(branchid).getSitetype();

		if (branchid <= 0) {
			if (nextBranchid <= 0) {
				throw new CwbException(co.getCwb(), co.getFlowordertype(), ExceptionCwbErrorTypeEnum.MU_DI_ZHAN_WEI_ZHI);
			} else {
				branchid = nextBranchid;
			}
		}

		if ((branchid > 0) && (nextBranchid == 0)) {
			return branchid;
		}
		if ((currentbranchtype == 2) && aflag) {
			if ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) && (co.getCurrentbranchid() == user.getBranchid())
					&& (currentbranchtype == BranchEnum.ZhanDian.getValue()) && ((type == BranchEnum.ZhongZhuan.getValue()) || (type == BranchEnum.KuFang.getValue()))) {
				this.logger.info("站点做中转出站的时候，若设置了到错货允许中转出站，那么当前站的到错货订单可以直接出库 ，currentbranchid: {} branchid {}", user.getBranchid(), branchid);
			}
		} else if ((branchid > 0) && (branchid != nextBranchid) && !forceOut) {
			if ((co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) && (co.getCurrentbranchid() == user.getBranchid())
					&& (currentbranchtype == BranchEnum.ZhanDian.getValue()) && ((type == BranchEnum.ZhongZhuan.getValue()) || (type == BranchEnum.KuFang.getValue()))) {
				this.logger.info("站点做中转出站的时候，若设置了到错货允许中转出站，那么当前站的到错货订单可以直接出库 ，currentbranchid: {} branchid {}", user.getBranchid(), branchid);
			} else {
				throw new CwbException(co.getCwb(), co.getFlowordertype(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid()).getBranchname());
			}
		}
		return branchid;
	}

	private long getNextBranchId(CwbOrder co, User user) {
		long nextbranchid = co.getNextbranchid();

		if (nextbranchid > 0) {
			long deliverystate = this.deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).size() == 0 ? 0 : this.deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).get(0).getDeliverystate();
			if ((nextbranchid == user.getBranchid())
					&& ((deliverystate != DeliveryStateEnum.JuShou.getValue()) && (deliverystate != DeliveryStateEnum.BuFenTuiHuo.getValue())
							&& (deliverystate != DeliveryStateEnum.ShangMenHuanChengGong.getValue()) && (deliverystate != DeliveryStateEnum.ShangMenTuiChengGong.getValue()) && (deliverystate != DeliveryStateEnum.FenZhanZhiLiu
							.getValue())) && ((co.getFlowordertype() == CwbFlowOrderTypeEnum.FenZhanLingHuo.getValue()) || (co.getFlowordertype() == CwbFlowOrderTypeEnum.YiFanKui.getValue()))) {
				throw new CwbException(co.getCwb(), co.getFlowordertype(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, this.branchDAO.getBranchByBranchid(co.getNextbranchid()).getBranchname());
			} else {
				return nextbranchid;
			}

		}
		if (co.getCwbstate() == CwbStateEnum.PeiShong.getValue()) {
			return this.cwbRouteService.getNextBranch(co.getCurrentbranchid(), co.getDeliverybranchid());
		} else if (co.getCwbstate() == CwbStateEnum.TuiHuo.getValue()) {
			return this.cwbRouteService.getPreviousBranch(co.getCurrentbranchid(), co.getTuihuoid());
		}
		return 0;
	}

	// 获取下一站集合 add by 王志宇
	public List<Branch> getNextBranchList(Long branchId, User user) {
		List<Branch> branchList = this.cwborderService.getNextPossibleBranches(user);
		List<Branch> nextBranchList = new ArrayList<Branch>();
		Branch currentBranch = this.branchDAO.getBranchByBranchid(branchId);
		//判空处理
		if(currentBranch.getContractflag()!=null&&!"".equals(currentBranch.getContractflag())){
			// 如果是一级站，则只能在货物流向中获取机构类型为库房的机构    加盟站点也需要能够揽件出站给分拣库---刘武强20161115
			if (Integer.parseInt(currentBranch.getContractflag()) == BranchTypeEnum.ZhiYing.getValue() || Integer.parseInt(currentBranch.getContractflag()) == BranchTypeEnum.JiaMeng.getValue()) {
				for (Branch branch : branchList) {
					if (branch.getSitetype() == BranchEnum.KuFang.getValue()) {// 站点改为库房
						nextBranchList.add(branch);
					}
				}
				// 如果是二级站，则只能在货物流向中获取机构类型为一级站的站点
			} else if (Integer.parseInt(currentBranch.getContractflag()) == BranchTypeEnum.ErJiZhan.getValue()) {
				for (Branch branch : branchList) {
					// 取出类型为站点，并且是一级站的机构
					if ((branch.getSitetype() == BranchEnum.ZhanDian.getValue()) && (Integer.parseInt(branch.getContractflag()) == BranchTypeEnum.ZhiYing.getValue())) {
						nextBranchList.add(branch);
					}
				}
			}
		}
		return nextBranchList;
	}

	public List<Branch> getNextBranchList4Combine(Long branchId, User user) {
		List<Branch> branchList = this.cwborderService.getNextPossibleBranches(user);
		List<Branch> nextBranchList = new ArrayList<Branch>();
		Branch currentBranch = this.branchDAO.getBranchByBranchid(branchId);
		//判空处理
		if(currentBranch.getContractflag()!=null&&!"".equals(currentBranch.getContractflag())){
		// 如果是一级站，则只能在货物流向中获取机构类型为库房的机构
		if (Integer.parseInt(currentBranch.getContractflag()) == BranchTypeEnum.ZhiYing.getValue()) {
			for (Branch branch : branchList) {
				// 排除退货站
				if (branch.getSitetype() != BranchEnum.TuiHuo.getValue()) {
					nextBranchList.add(branch);
				}
			}
			// 如果是二级站，则只能在货物流向中获取机构类型为一级站的站点
		} else if (Integer.parseInt(currentBranch.getContractflag()) == BranchTypeEnum.ErJiZhan.getValue()) {
			for (Branch branch : branchList) {
				if (branch.getSitetype() != BranchEnum.TuiHuo.getValue()) {
					// 取出类型为站点，并且是一级站的机构
					if ((branch.getSitetype() == BranchEnum.ZhanDian.getValue()) && (Integer.parseInt(branch.getContractflag()) == BranchTypeEnum.ZhiYing.getValue())) {
						nextBranchList.add(branch);
					}
				}
			}
		}}
		return nextBranchList;
	}
	/**
		 * 
		 * 判断订单上一个状态是否为站点到货，并且上上一个状态是揽件出站 
		 * @author 刘武强
		 * @date:2016年7月6日 上午9:23:15 
		 * @params:@param scanNo
		 * @params:@return
	*/
	private boolean getAlowFlag (String scanNo){
		List<OrderFlow> of = this.orderFlowDAO.getOrderFlowByCwb(scanNo);//查询出轨迹list.并且按时间顺序排序
		boolean flag = false;
		//如果上一个状态站点到货，上上一个状态是揽件出站，那么返回true
		if(of != null && of.size() >= 2 && of.get(of.size()-1).getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() && of.get(of.size()-2).getFlowordertype() == FlowOrderTypeEnum.LanJianChuZhan.getValue() ){
			flag = true;
		}
		return flag;
	}
}
