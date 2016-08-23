/**
 *
 */
package cn.explink.service.mps.release;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.core.utils.JsonUtil;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.mps.MPSCommonService;

/**
 *
 * 小件员领货对于一票多件放行的处理
 *
 * @author songkaojun 2016年1月8日
 */
@Component("deliverTakeGoodsMPSReleaseService")
public final class DeliverTakeGoodsMPSReleaseService extends AbstractMPSReleaseService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TransCwbDetailDAO transCwbDetailDAO;
	@Autowired
	private TranscwbOrderFlowDAO transcwbOrderFlowDAO;
	@Autowired
	private SystemInstallDAO systemInstallDAO;
	@Autowired
	private OrderBackCheckDAO orderBackCheckDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private MPSCommonService mpsCommonService;
	
	@Override
	public void validateReleaseCondition(String cwbOrTransCwb) throws CwbException {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwbConsideringMPSSwitchType(cwbOrTransCwb, AbstractMPSReleaseService.VALIDATE_RELEASE_CONDITION);
		if (cwbOrder == null) {
			cwbOrder = this.getMPSCwbOrderByCwbConsideringMPSSwitchType(cwbOrTransCwb, AbstractMPSReleaseService.VALIDATE_RELEASE_CONDITION);
			if (cwbOrder == null) {
				return;
			}
		}
		// 不管是库房集单还是站点集单，都须要进行校验
		CwbException exception = new CwbException(cwbOrder.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.DELIVERTAKEGOODS_MPS_NOT_ALL_ARRIVED);
		this.validateMPS(cwbOrTransCwb, cwbOrder, exception, AbstractMPSReleaseService.BEFORE_SUBSTATION_GOODS_ARRIVED_STATE);
	}
	
	/**
	 * 领货校验到齐、同站领货
	 * @param cwbOrder 订单
	 * @param scancwb 扫描的单号(订单号 or 运单号)
	 * @param currentUserBranchid 当前用户所在站点id
	 * @throws CwbException 如果校验不通过，则会抛出异常
	 * @author neo01.huang 2016-6-28
	 */
	public void validateReveiveGoodsAllArrivedAndSameBranch(CwbOrder cwbOrder,
			String scancwb, long currentUserBranchid) {
		final String logPrefix = "领货校验到齐、同站领货->";
		long flowordertype = cwbOrder.getFlowordertype(); //操作类型
		int deliverystate = cwbOrder.getDeliverystate(); //配送状态
		int ismpsflag = cwbOrder.getIsmpsflag(); //集单标识
		long sendcarnum = cwbOrder.getSendcarnum(); //发货数量
		int cwbordertypeid = cwbOrder.getCwbordertypeid(); //订单类型
		long cwbstate = cwbOrder.getCwbstate(); //订单状态
		long currentBranchid = cwbOrder.getCurrentbranchid(); //当前站点id
		String transcwb = StringUtils.trim(cwbOrder.getTranscwb()); //运单号
		long customerId = cwbOrder.getCustomerid(); //客户id
		logger.info("{}cwb:{}, scancwb:{}, currentUserBranchid:{}, flowordertype:{}, deliverystate:{}, \n"
				+ "ismpsflag:{}, sendcarnum:{}, cwbordertypeid:{}, cwbstate:{}, transcwb:{}, \n"
				+ "currentBranchid:{}, customerId:{}", 
				logPrefix, cwbOrder.getCwb(), scancwb, currentUserBranchid, flowordertype, deliverystate, 
				ismpsflag, sendcarnum, cwbordertypeid, cwbstate, transcwb,
				currentBranchid, customerId);
		
		if (cwbordertypeid != CwbOrderTypeIdEnum.Peisong.getValue()) {
			logger.info("{}{}不是配送订单，无需校验，跳过", logPrefix, cwbOrder.getCwb());
			return;
		}
		
		if (sendcarnum <= 1) {
			logger.info("{}{}非一票多件，无需校验，跳过", logPrefix, cwbOrder.getCwb());
			return;
		}
		
		Long isYpdjusetranscwb = null; //一票多件是否用运单号
		//获取客户信息
		Customer customer = customerDAO.getCustomerById(customerId);
		logger.info("{}{}，客户信息:{}", logPrefix, cwbOrder.getCwb(), JsonUtil.translateToJson(customer));
		if (customer != null) {
			isYpdjusetranscwb = customer.getIsypdjusetranscwb();
		}
		if (isYpdjusetranscwb == null) {
			logger.info("{}{}isYpdjusetranscwb为空", logPrefix, cwbOrder.getCwb());
			return;
		}
		
		if (StringUtils.isEmpty(transcwb)) {
			logger.info("{}{}运单号为空，无需校验，跳过", logPrefix, cwbOrder.getCwb());
			return;
		}
		
		String[] transcwbs = transcwb.split(",");
		if (transcwbs == null || transcwbs.length != sendcarnum) {
			logger.info("{}{}发货数量与运单号数量不一致，无需校验，跳过", logPrefix, cwbOrder.getCwb());
			return;
		}
		
		/* ***************modify begin*********************/
		//commented by neo01.huang，2016-7-26，放开对express_ops_transcwb的校验，express_ops_transcwb的记录条数有可能跟sendcarnum不一致
		/*
		List<TranscwbView> transcwbViewList = transCwbDao.getTransCwbByCwb(cwbOrder.getCwb());
		if (CollectionUtils.isEmpty(transcwbViewList)) {
			logger.info("{}{}订单号没有绑定运单号，无需校验，跳过", logPrefix, cwbOrder.getCwb());
			return;
		}
		logger.info("{}transcwbViewList.size:{}", logPrefix, transcwbViewList.size());
		if (transcwbViewList.size() != sendcarnum) {
			logger.info("{}{}运单号数量与发货数量不一致，无需校验，跳过", logPrefix, cwbOrder.getCwb());
			return;
		}
		*/
		/* ***************modify end*********************/
		
		/* ***************modify begin*********************/
		//modify by neo01.huang,2016-8-19,加入判断第一件已经领货，其他件做了其他操作的场景
		//获取运单领货的轨迹
		Map<String, Object> firstReceiveGoodsParamMap = new HashMap<String, Object>();
		firstReceiveGoodsParamMap.put("flowordertype", FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		firstReceiveGoodsParamMap.put("isnow", 1);
		firstReceiveGoodsParamMap.put("cwb", cwbOrder.getCwb());
		firstReceiveGoodsParamMap.put("branchid", currentUserBranchid);
		//运单领货的轨迹
		/*
		 * SELECT * FROM express_ops_transcwb_orderflow t  WHERE t.flowordertype=9 AND t.isnow=1  AND t.cwb=?  AND t.branchid=?
		 */
		List<TranscwbOrderFlow> firstReceiveGoodsFlowList = transcwbOrderFlowDAO.queryTranscwbOrderFlow(firstReceiveGoodsParamMap, null);
		//运单已领货的轨迹数量
		int firstReceiveGoodsFlowListSize = 0;
		if (firstReceiveGoodsFlowList != null) {
			firstReceiveGoodsFlowListSize = firstReceiveGoodsFlowList.size();
 		}
		logger.info("{}{},firstReceiveGoodsFlowListSize:{}", logPrefix, cwbOrder.getCwb(), firstReceiveGoodsFlowListSize);
		
		//是否属于到站集齐领货
		boolean isAllArrived = (FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() == flowordertype ||
				FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() == flowordertype) && firstReceiveGoodsFlowListSize == 0;
		logger.info("{}是否属于到站集齐领货:{}", logPrefix, isAllArrived);
		
		//是否已经进行第一次领货
		boolean isFirstReceiveGoodsFinished = FlowOrderTypeEnum.FenZhanLingHuo.getValue() == flowordertype ||
				firstReceiveGoodsFlowListSize != 0;
		logger.info("{}是否已经进行第一次领货:{}", logPrefix, isFirstReceiveGoodsFinished);
		/* ***************modify end*********************/
		
		//是否为分站滞留
		boolean isBranchLeft = FlowOrderTypeEnum.YiShenHe.getValue() == flowordertype &&
				DeliveryStateEnum.FenZhanZhiLiu.getValue() == deliverystate;
		logger.info("{}是否为分站滞留:{}", logPrefix, isBranchLeft);
		
		//是否拒收后退货出站审核为站点配送的情况
		boolean isRejectAfterAudit = flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() &&
				deliverystate == DeliveryStateEnum.JuShou.getValue() &&
				cwbstate == CwbStateEnum.PeiShong.getValue() &&
				currentBranchid == currentUserBranchid;
		logger.info("{}是否拒收后退货出站审核为站点配送的情况:{}", logPrefix, isRejectAfterAudit);
		
		if (isAllArrived && isYpdjusetranscwb.longValue() == 1) { //属于到站集齐领货 && 一票多件使用运单号
			//校验是否所有运单都到齐同一个站点
			validateOrderAllArrived(cwbOrder, currentUserBranchid);
		} 
		
		if (isFirstReceiveGoodsFinished && isYpdjusetranscwb.longValue() == 1) { //已经进行第一次领货 && 一票多件使用运单号
			//校验同一个站点领货（针对第一次已经领货）
			validateSameBranchReceiveGood(cwbOrder, scancwb, currentUserBranchid);
		}
		
		if (isBranchLeft) {
			//校验分站滞留后的领货
			validateBranchLeft(cwbOrder, currentUserBranchid);
		}
		
		if (isRejectAfterAudit) {
			//校验拒收后退货出站审核为站点配送的情况
			validateRejectAfterAudit(cwbOrder);
		}
		
	}
	
	/**
	 * 校验拒收后退货出站审核为站点配送的情况
	 * @param cwbOrder 订单
	 * @throws CwbException 如果校验不通过，则会抛出异常
	 */
	public void validateRejectAfterAudit(CwbOrder cwbOrder) {
		final String logPrefix = "校验拒收后退货出站审核为站点配送的情况->";
		
		//通过订单号获取退货出站审核记录
		List<OrderBackCheck> orderBackCheckList = orderBackCheckDAO.getOrderBackChecks("'" + cwbOrder.getCwb() + "'", -1, -1, -1, -1, -1, "", "");
		if (CollectionUtils.isEmpty(orderBackCheckList)) {
			logger.info("{}orderBackCheckList is null or empty", logPrefix);
			throw new CwbException(cwbOrder.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.BU_CUN_ZAI_TUI_HUO_CHU_ZHAN_SHEN_HE_JI_LU);
		}
		
		//获取最后1条
		OrderBackCheck orderBackCheck = orderBackCheckList.get(orderBackCheckList.size() - 1);
		logger.info("{}checkstate:{}, checkresult:{}", logPrefix, orderBackCheck.getCheckstate(), orderBackCheck.getCheckresult());
		boolean isOrderBackCheckPass = orderBackCheck.getCheckstate() == 2 &&
				orderBackCheck.getCheckresult() == 2;
		if (!isOrderBackCheckPass) {
			throw new CwbException(cwbOrder.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.BU_CUN_ZAI_TUI_HUO_CHU_ZHAN_SHEN_HE_WEI_ZHAN_DIAN_PEI_SONG);
		}
	}
	
	/**
	 * 校验是否所有运单都到齐同一个站点
	 * @param cwbOrder 订单
	 * @param scancwb 扫描的单号(订单号 or 运单号)
	 * @param currentUserBranchid 当前用户所在站点id
	 * @throws CwbException 如果校验不通过，则会抛出异常
	 * @author neo01.huang 2016-6-24
	 */
	public void validateOrderAllArrived(CwbOrder cwbOrder, long currentUserBranchid) {
		final String logPrefix = "校验是否所有运单都到齐同一个站点->";
		String cwb = cwbOrder.getCwb(); //订单号
		long flowordertype = cwbOrder.getFlowordertype(); //操作类型
		int ismpsflag = cwbOrder.getIsmpsflag(); //集单标识
		logger.info("{}cwb:{}, ismpsflag:{}, flowordertype:{}", logPrefix, cwb, ismpsflag, flowordertype);
		logger.info("{}currentUserBranchid:{}", logPrefix, currentUserBranchid);
		
		if (ismpsflag == IsmpsflagEnum.no.getValue()) { //非集单
			SystemInstall ypdjpathtongParam  = systemInstallDAO.getSystemInstallByName("ypdjpathtong");
			//一票多件操作流程件数限制:一票多件全部流程不对件数限制:(2),
			//缺件扫描下站，站点能扫描入库，但不能领货,但不能操作退货甲方:(1),
			//正常每个环节都会对件数验证限制:(0)
			if (ypdjpathtongParam != null && "2".equals(ypdjpathtongParam.getValue())) {
				logger.info("{}{}为非集单，系统参数ypdjpathtong为2，跳过", logPrefix, cwb);
				return ;
			}
		}
		
		List<TransCwbDetail> transCwbDetailList = transCwbDetailDAO.getTransCwbDetailListByCwb(cwb);
		if (CollectionUtils.isEmpty(transCwbDetailList)) {
			logger.info("{}通过订单查询的运单list为空，为非集单模式，通过运单轨迹表校验", logPrefix);
			long sendcarnum = cwbOrder.getSendcarnum(); //发货数量
			logger.info("{}sendcarnum:{}", logPrefix, sendcarnum);
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("flowordertypeIn", FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "," + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
			paramMap.put("isnow", 1);
			paramMap.put("cwb", cwb);
			paramMap.put("branchid", currentUserBranchid);
			List<TranscwbOrderFlow> transcwbOrderFlowList = transcwbOrderFlowDAO.queryTranscwbOrderFlow(paramMap, "t.credate DESC");
			if (CollectionUtils.isEmpty(transcwbOrderFlowList)) {
				transcwbOrderFlowList = Collections.emptyList();
			}
			logger.info("{}transcwbOrderFlowList.size:{}", logPrefix, transcwbOrderFlowList.size());
			if (sendcarnum != transcwbOrderFlowList.size()) {
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.DELIVERTAKEGOODS_MPS_NOT_ALL_ARRIVED);
			}
			logger.info("{}通过订单查询的运单list为空，为非集单模式，校验通过", logPrefix);
			return;
		}
		
		//往下是集单模式，通过运单表判断
		
		Set<Long> currentbranchidSet = new HashSet<Long>(); //当前站点id Set
		
		//校验站点id
		for (TransCwbDetail transCwbDetail : transCwbDetailList) {
			if (transCwbDetail == null) {
				continue;
			}
			if (transCwbDetail.getCurrentbranchid() == 0) { //有其中一个站点id为0，则说明还没到齐
				logger.info("{}transCwbDetail({})的currentbranchid为0", logPrefix, transCwbDetail.getTranscwb());
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.DELIVERTAKEGOODS_MPS_NOT_ALL_ARRIVED);
			}
			
			currentbranchidSet.add(transCwbDetail.getCurrentbranchid());
		}
		
		if (currentbranchidSet.size() > 1) { //当前站点id Set的size大于1，说明运单不在同一个站点
			logger.info("{}当前站点id Set的size大于1，说明运单不在同一个站点,currentbranchidSet:{}", logPrefix, JsonUtil.translateToJson(currentbranchidSet));
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.DELIVERTAKEGOODS_MPS_NOT_ALL_ARRIVED);
		} 
		
		//校验运单flowordertype是否为到货、到错货
		for (TransCwbDetail transCwbDetail : transCwbDetailList) {
			if (transCwbDetail == null) {
				continue;
			}
			if (FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() != transCwbDetail.getTranscwboptstate() &&
					FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() != transCwbDetail.getTranscwboptstate()) {
				logger.info("{}运单号：{},操作类型不是7or8,flowordertype:{}", logPrefix, 
						transCwbDetail.getTranscwb(), transCwbDetail.getTranscwboptstate());
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.DELIVERTAKEGOODS_MPS_NOT_ALL_ARRIVED);
			}
		}
		
		Long currentbranchid = currentbranchidSet.iterator().next(); //唯一的当前站点id
		logger.info("{}所有运单都在同一站点,{}", logPrefix, currentbranchid);
		
		if (currentbranchid.longValue() != currentUserBranchid) {
			logger.info("{}运单所在站点与当前用户所在站点不一致", logPrefix);
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.YUN_DAN_ZHAN_DIAN_YU_YONG_HU_ZHAN_DIAN_BU_YI_ZHI);
		}
	}
	
	/**
	 * 校验同一个站点领货（针对第一次已经领货）
	 * @param cwbOrder 订单
	 * @param scancwb 扫描的单号(订单号 or 运单号)
	 * @param currentUserBranchid 当前用户所在站点id
	 * @throws CwbException 如果校验不通过，则会抛出异常
	 * @author neo01.huang 2016-6-24
	 */
	public void validateSameBranchReceiveGood(CwbOrder cwbOrder, String scancwb, long currentUserBranchid) {
		final String logPrefix = "校验同一个站点领货->";
		String cwb = StringUtils.trimToEmpty(cwbOrder.getCwb());
		scancwb = StringUtils.trimToEmpty(scancwb);
		long flowordertype = cwbOrder.getFlowordertype(); //操作类型
		if (StringUtils.isEmpty(cwb)) {
			logger.info("{}cwb is empty", logPrefix);
			return;
		}
		if (StringUtils.isEmpty(scancwb)) {
			logger.info("{}scancwb is empty", logPrefix);
			return;
		}
		logger.info("{}cwb:{}, scancwb:{}, currentUserBranchid:{}, flowordertype:{}", 
				logPrefix, cwb, scancwb, currentUserBranchid, flowordertype);
		if (StringUtils.equals(cwb, scancwb)) {
			logger.info("{}扫描的是订单号，无需校验", logPrefix);
			return;
		}
		
		//获取兄弟运单的领货站点id
		Long siblingReceiveGoodsBranchid = getSiblingReceiveGoodsBranchid(cwb, scancwb);
		logger.info("{}兄弟运单的领货站点id:{}", logPrefix, siblingReceiveGoodsBranchid);
		if (siblingReceiveGoodsBranchid != null && 
				siblingReceiveGoodsBranchid.longValue() != currentUserBranchid) {
			logger.info("{}不在同一站领货", logPrefix);
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.DELIVERTAKEGOODS_MPS_NOT_ALL_ARRIVED);
		}
		logger.info("{}通过!", logPrefix);
	}
	
	/**
	 * 获取兄弟运单的领货站点id
	 * @param cwb 订单号
	 * @param scancwb 扫描的运单号
	 * @return
	 * @author neo01.huang 2016-6-28
	 */
	private Long getSiblingReceiveGoodsBranchid(String cwb, String scancwb) {
		final String logPrefix = "获取兄弟运单的领货站点id->";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("flowordertype", FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		paramMap.put("isnow", 1);
		paramMap.put("cwb", cwb);
		//当前领货操作记录List
		List<TranscwbOrderFlow> currentReceiveGoodsList = transcwbOrderFlowDAO.queryTranscwbOrderFlow(paramMap, "t.credate DESC");
		if (CollectionUtils.isEmpty(currentReceiveGoodsList)) {
			logger.info("{}当前领货操作记录List为空", logPrefix);
			return null;
		}
		
		boolean isExistScancwb = false;
		//遍历当前领货操作记录List，找出是否已存在扫描的运单号
		for (TranscwbOrderFlow transcwbOrderFlow : currentReceiveGoodsList) {
			if (StringUtils.equals(transcwbOrderFlow.getScancwb(), scancwb)) {
				isExistScancwb = true;
				break;
			}
		}
		
		if (isExistScancwb) { //已存在扫描的运单号
			logger.info("{}已存在扫描的运单号", logPrefix);
			return null;
		}
		return currentReceiveGoodsList.get(0).getBranchid();
	}
	
	/**
	 * 校验分站滞留后的领货
	 * @param cwbOrder 订单
	 * @param currentUserBranchid 当前用户所在站点id
	 * @author neo01.huang 2016-6-28
	 */
	public void validateBranchLeft(CwbOrder cwbOrder, long currentUserBranchid) {
		final String logPrefix = "校验分站滞留后的领货->";
		String cwb = cwbOrder.getCwb(); //订单号
		long currentBranchid = cwbOrder.getCurrentbranchid(); //当前站点id
		logger.info("{}cwb:{}, currentBranchid:{}", logPrefix, cwb, currentBranchid);
		if (currentBranchid != currentUserBranchid) {
			logger.info("{}订单所在站点与当前用户所在站点不一致", logPrefix);
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.DING_DAN_ZHAN_DIAN_YU_YONG_HU_ZHAN_DIAN_BU_YI_ZHI);
		}
		
		List<TransCwbDetail> transCwbDetailList = transCwbDetailDAO.getTransCwbDetailListByCwb(cwb);
		if (CollectionUtils.isEmpty(transCwbDetailList)) {
			logger.info("{}通过订单查询的运单list为空", logPrefix);
			return;
		}
		
		//校验所有运单是否为配送状态
		List<String> errorTranscwbStateList = new ArrayList<String>();
		for (TransCwbDetail transCwbDetail : transCwbDetailList) {
			logger.info("{}transcwb:{}, transcwbstate:{}", logPrefix, transCwbDetail.getTranscwb(), transCwbDetail.getTranscwbstate());
			if (TransCwbStateEnum.PEISONG.getValue() != transCwbDetail.getTranscwbstate()) {
				errorTranscwbStateList.add(transCwbDetail.getTranscwb());
			}
		}
		if (errorTranscwbStateList.size() > 0) {
			logger.info("{}运单{}状态不是配送，不允许领货", logPrefix, JsonUtil.translateToJson(errorTranscwbStateList));
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.YUN_DAN_ZHUANG_TAI_BU_SHI_PEI_SONG, JsonUtil.translateToJson(errorTranscwbStateList));
		}
		
		//校验所有运单的currentbranchid
		List<String> errorCurrentbranchidList = new ArrayList<String>();
		for (TransCwbDetail transCwbDetail : transCwbDetailList) {
			logger.info("{}transcwb:{}, currentbranchid:{}", logPrefix, transCwbDetail.getTranscwb(), transCwbDetail.getCurrentbranchid());
			if (currentUserBranchid != transCwbDetail.getCurrentbranchid()) {
				errorCurrentbranchidList.add(transCwbDetail.getTranscwb());
			}
		}
		if (errorCurrentbranchidList.size() > 0) {
			logger.info("{}运单所在站点与当前用户所在站点不一致,{}", logPrefix, JsonUtil.translateToJson(errorCurrentbranchidList));
			throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.YUN_DAN_ZHAN_DIAN_YU_YONG_HU_ZHAN_DIAN_BU_YI_ZHI.getText() + "," + JsonUtil.translateToJson(errorCurrentbranchidList));
		}
		
		logger.info("{}通过！", logPrefix);
	}

	/**
	 * 反馈时校验是否已存在领货记录
	 * @param cwbOrder 订单
	 * @throws CwbException 如果不存在领货记录，则会抛出异常
	 */
	public void validateExistPickingForFeedBack(CwbOrder cwbOrder) {
		final String logPrefix = "反馈时校验是否已存在领货记录->";
		String cwb = cwbOrder.getCwb(); //订单号
		long flowordertype = cwbOrder.getFlowordertype(); //订单操作类型
		logger.info("{}cwb:{}, flowordertype:{}", logPrefix, cwb, flowordertype);
		if (flowordertype != FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || 
					flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				logger.info("{}cwb:{}, 请先做到货扫描", logPrefix, cwb);
				throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.PLEASE_RECEIVE_GOODS_FIRST, FlowOrderTypeEnum.FenZhanLingHuo.getText());				
			}
		}
	}
	
}
