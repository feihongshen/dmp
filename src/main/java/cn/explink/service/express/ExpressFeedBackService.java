package cn.explink.service.express;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.PreOrderDao;
import cn.explink.domain.Customer;
import cn.explink.domain.Reason;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExpressFeedBackDTO;
import cn.explink.domain.VO.express.ExpressFeedBackParamsVO;
import cn.explink.domain.VO.express.ExpressFeedBackView;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.domain.express.ExpressPreOrder;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.express.ExcuteStateEnum;
import cn.explink.enumutil.express.ExpressFeedBackTongjiEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;
import cn.explink.util.Tools;

import com.pjbest.deliveryorder.service.PjSaleOrderFeedbackRequest;

/**
 * 揽件反馈业务逻辑处理
 * @author jiangyu 2015年8月7日
 *
 */
@Service
@Transactional
public class ExpressFeedBackService {
	private Logger logger = LoggerFactory.getLogger(ExpressFeedBackService.class);

	@Autowired
	PreOrderDao preOrderDao;

	@Autowired
	ReasonDao reasonDao;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	SystemInstallDAO systemInstallDAO;

	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	/**
	 * 反馈操作的主要逻辑
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */

	public ExpressOpeAjaxResult executeFeedBackOperate(ExpressFeedBackParamsVO params, User user) throws Exception {
		ExpressOpeAjaxResult result = new ExpressOpeAjaxResult();
		String preOrderNo = params.getPreOrderNoEdit();
		Integer executeState = params.getPickResultId();
		//查询预订单记录 进行校验
		ExpressPreOrder preOrder = this.preOrderDao.getPreOrderByOrderNo(preOrderNo);
		if (null == preOrder) {
			this.logger.warn("preOrder {} not exist" + preOrderNo);
			throw new Exception("预订单号不存在或者已经失效");
		} else {
			if (ExcuteStateEnum.AllocatedDeliveryman.getValue() > preOrder.getExcuteState()) {//还没有分配小件员不能够进行反馈
				throw new Exception("预订单没有分配小件员，不能进行反馈操作");
			}
		}
		//TPS接口参数 --- for pjInterfaceParams

		PjSaleOrderFeedbackRequest request = new PjSaleOrderFeedbackRequest();
		request.setOperateOrg(this.branchDAO.getBranchByBranchid(user.getBranchid()).getTpsbranchcode());//机构编码
		request.setReserveOrderNo(preOrderNo);//预订单编号
		request.setOperater(user.getRealname());//操作人
		request.setOperateTime(new Date().getTime());//操作时间
		Integer operationType = 0;

		if (ExcuteStateEnum.DelayedEmbrace.getValue() == executeState.intValue()) {
			operationType = FeedbackOperateTypeEnum.TicklingDelay.getValue();
		} else if (ExcuteStateEnum.fail.getValue() == executeState.intValue()) {
			operationType = FeedbackOperateTypeEnum.ReceiveFail.getValue();
		} else if (ExcuteStateEnum.EmbraceSuperzone.getValue() == executeState.intValue()) {
			operationType = FeedbackOperateTypeEnum.ReceiveSurpass.getValue();
		} else if (ExcuteStateEnum.StationSuperzone.getValue() == executeState.intValue()) {
			operationType = FeedbackOperateTypeEnum.SiteSurpass.getValue();
		} else if (ExcuteStateEnum.Succeed.getValue() == executeState.intValue()) {
			operationType = FeedbackOperateTypeEnum.ReceiveSuccess.getValue();
		}
		request.setOperateType(operationType);//执行状态

		//一级原因
		Long firstLevelReasonId = 0L;
		//二级原因
		Long secondLevelReasonId = 0L;

		if (ExcuteStateEnum.Succeed.getValue() == executeState.intValue()) {//成功
			String transNo = params.getTransNo();
			if (Tools.isEmpty(transNo)) {
				result.setStatus(false);
				throw new Exception("反馈为成功时，运单号不能为空");
			}
			//运单号 --- for pjInterfaceParams
			request.setTransportNo(transNo);
		} else if (ExcuteStateEnum.fail.getValue() == executeState.intValue()) {//失败
			firstLevelReasonId = params.getPickFailedFirstLevel();
			secondLevelReasonId = params.getPickFailedSecondLevel();
		} else if (ExcuteStateEnum.DelayedEmbrace.getValue() == executeState.intValue()) {//延迟
			firstLevelReasonId = params.getPickDelayFirstLevel();
			secondLevelReasonId = params.getPickDelaySecondLevel();
		} else if (ExcuteStateEnum.EmbraceSuperzone.getValue() == executeState.intValue()) {//揽件超区
			firstLevelReasonId = params.getPickWrongFirstLevel();
			secondLevelReasonId = params.getPickWrongSecondLevel();
		} else if (ExcuteStateEnum.StationSuperzone.getValue() == executeState.intValue()) {//站点超区
			firstLevelReasonId = params.getAreaWrongFirstLevel();
			secondLevelReasonId = params.getAreaWrongSecondLevel();
		}

		//查询原因内容
		Reason firstReason = new Reason(0L, "");
		Reason secondReason = new Reason(0L, "");
		if ((firstLevelReasonId.longValue() != 0) && (secondLevelReasonId.longValue() > 0)) {
			firstReason = this.reasonDao.getRcontentByReasonid(firstLevelReasonId.intValue());
			secondReason = this.reasonDao.getRcontentByReasonid(secondLevelReasonId.intValue());
			//原因  --- for pjInterfaceParams
			request.setReason("一级原因：" + firstReason.getReasoncontent() + ";二级原因：" + secondReason.getReasoncontent());
		}
		/*//拼接描述  暂时不加
		JoinMessageVO contextVar = new JoinMessageVO();
		contextVar.setOperationType(TpsOperationEnum.EmbracedScan.getValue());//揽件反馈对应揽件扫描
		contextVar.setStation(this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());//站点名称
		contextVar.setOperator(user.getRealname());
		contextVar.connectMessage();
		*/
		//1.反馈操作【反馈操作要发短信】
		this.preOrderDao.updatePreOrderExecuteState(user, params, firstReason, secondReason);

		//2.将反馈状态通知到TPS
		ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.PreOrderFeedBack);
		//设置预订单号
		paramObj.setReserveOrderNo(request.getReserveOrderNo());
		paramObj.setPreOrderfeedBack(request);
		//发送JMS消息
		this.tpsInterfaceExecutor.executTpsInterface(paramObj);

		return result;
	}

	/**
	 * 分组进入页面需要的内容
	 * @param user
	 * @param deliveryId
	 * @return
	 */
	public Map<String, Object> orgnizeInfo2PageView(User user, long deliveryId) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		List<JSONObject> deliverList_1 = null;
		// 如果当前登录用户为小件员[查询总的记录数]
		if (2 == user.getRoleid()) {
			// 自己的逻辑查询出来数据
			deliverList_1 = this.preOrderDao.getpreOrderGroupByUserId(user.getBranchid(), user.getUserid());
			SystemInstall deliveryxiaojianyuan = this.systemInstallDAO.getSystemInstall("deliveryxiaojianyuan");// 系统设置：归班审核是否允许小件员确认审核
			resMap.put("deliveryxiaojianyuan", deliveryxiaojianyuan == null ? "no" : deliveryxiaojianyuan.getValue());
		} else {
			deliverList_1 = this.preOrderDao.getpreOrderGroupByUserId(user.getBranchid(), user.getUserid());
		}
		List<JSONObject> deliverList = new ArrayList<JSONObject>();
		if (deliverList != null) {
			for (JSONObject json : deliverList_1) {
				if (!Tools.isEmpty(json.getString("delivername"))) {
					deliverList.add(json);
				}
			}
		}
		resMap.put("deliverList", deliverList);
		// 根据小件员查询记录数据
		if (deliveryId > 0) {
			ExpressFeedBackDTO feedBackDTO = new ExpressFeedBackDTO();
			List<ExpressPreOrder> expressPreOrders = this.preOrderDao.getExpressFeedBackStateByDeliver(deliveryId, user);
			// 获取每个表格的数据
			List<ExpressFeedBackView> preOrderAndFeedBackState = this.getFeedBackStateViews(expressPreOrders, user);
			feedBackDTO.analysisFeedBackStateList(preOrderAndFeedBackState);
			resMap.put("feedBackDTO", feedBackDTO);
		} else {// 没有选小件员的情况下 显示所有小件员当天的对应数据数据的
			// 逻辑：已揽收 = 今日未反馈+ 今日已反馈 + 昨日未反馈
			Map<Long, Map<Long, Long>> deliveryInCountMap = new HashMap<Long, Map<Long, Long>>();// <小件员,<类型，单数>>
			// 查询数据
			deliveryInCountMap = this.getAllDeliverSummaryData(deliverList, user.getBranchid());
			resMap.put("deliveryInCountMap", deliveryInCountMap);
			// 汇总的数据
			Map<Long, Long> summaryMap = new HashMap<Long, Long>();
			if ((deliverList != null) && (deliverList.size() > 0)) {
				long alreadyFeedBack = 0;// 已反馈
				long todayNotFeedBack = 0;// 今日未反馈
				long yestodayNotFeedBack = 0;// 历史未反馈
				long alreadyPicked = 0;// 已揽收
				// <类型，单数>
				for (JSONObject deliver : deliverList) {
					Map<Long, Long> map = deliveryInCountMap.get(deliver.getLong("deliveryid"));
					alreadyFeedBack += map.get(ExpressFeedBackTongjiEnum.FeedBacked.getValue());
					todayNotFeedBack += map.get(ExpressFeedBackTongjiEnum.TodayNotFeedBack.getValue());
					yestodayNotFeedBack += map.get(ExpressFeedBackTongjiEnum.YestodayNotFeedBack.getValue());
					alreadyPicked += map.get(ExpressFeedBackTongjiEnum.Picked.getValue());
				}
				summaryMap.put(ExpressFeedBackTongjiEnum.Picked.getValue(), alreadyPicked);
				summaryMap.put(ExpressFeedBackTongjiEnum.FeedBacked.getValue(), alreadyFeedBack);
				summaryMap.put(ExpressFeedBackTongjiEnum.TodayNotFeedBack.getValue(), todayNotFeedBack);
				summaryMap.put(ExpressFeedBackTongjiEnum.YestodayNotFeedBack.getValue(), yestodayNotFeedBack);
			}
			resMap.put("summaryMap", summaryMap);
		}
		SystemInstall siteDayLogTime = this.systemInstallDAO.getSystemInstallByName("useAudit");
		SystemInstall isGuiBanUseZanBuChuLi = this.systemInstallDAO.getSystemInstall("isGuiBanUseZanBuChuLi");
		//是否重新分配[针对的是揽件延迟预订单]
		SystemInstall isReAllocateExpress = this.systemInstallDAO.getSystemInstall("isReAllocateExpress");

		if (siteDayLogTime != null) {
			resMap.put("useAudit", siteDayLogTime.getValue());
		}

		resMap.put("isGuiBanUseZanBuChuLi", isGuiBanUseZanBuChuLi == null ? "no" : isGuiBanUseZanBuChuLi.getValue());
		resMap.put("isReAllocateExpress", isReAllocateExpress == null ? "no" : isReAllocateExpress.getValue());

		return resMap;
	}

	/**
	 * 分别获取每个小件员的数据分组[new]
	 *
	 * @param dList //小件员
	 * @param branchid //机构
	 * @return
	 */
	private Map<Long, Map<Long, Long>> getAllDeliverSummaryData(List<JSONObject> deliverList, long branchId) {
		Map<Long, Map<Long, Long>> deliveryInCountMap = new HashMap<Long, Map<Long, Long>>();
		// 获取该站点所有的未归班的订单
		List<ExpressPreOrder> preOrderList = this.preOrderDao.getPreOrderByBranchId(branchId);

		if ((deliverList != null) && (deliverList.size() > 0)) {
			String nowtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			for (JSONObject deliverObj : deliverList) {
				if ((preOrderList != null) && (preOrderList.size() > 0)) {
					Map<Long, Long> typeMap = new HashMap<Long, Long>();
					long alreadyFeedBack = 0;
					long todayNotFeedBack = 0;
					long yestodayNotFeedBack = 0;
					long alreadyPicked = 0;
					for (ExpressPreOrder preOrder : preOrderList) {
						if (preOrder.getDelivermanId() != deliverObj.getLong("deliveryid")) {
							continue;
						}

						long crateTimeOfMiSeconds = 0;// 分配小件员时间
						long nowTimeOfMiSeconds = 0;// 当天开始时间

						try {
							if (preOrder.getDistributeDelivermanTime() != null) {
								crateTimeOfMiSeconds = preOrder.getDistributeDelivermanTime().getTime();
							}
							nowTimeOfMiSeconds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nowtime).getTime();
						} catch (ParseException e) {
							e.printStackTrace();
						}

						// 已反馈
						if (preOrder.getExcuteState() > ExcuteStateEnum.AllocatedDeliveryman.getValue()) {
							alreadyFeedBack++;
							alreadyPicked++;
							continue;
						}

						// 今日未反馈
						if ((preOrder.getExcuteState() <= ExcuteStateEnum.AllocatedDeliveryman.getValue()) && (crateTimeOfMiSeconds > nowTimeOfMiSeconds)) {
							todayNotFeedBack++;
							alreadyPicked++;
							continue;
						}
						// 昨日未反馈
						if ((preOrder.getExcuteState() <= ExcuteStateEnum.AllocatedDeliveryman.getValue()) && (crateTimeOfMiSeconds <= nowTimeOfMiSeconds)) {
							yestodayNotFeedBack++;
							alreadyPicked++;
							continue;
						}

					}
					typeMap.put(ExpressFeedBackTongjiEnum.Picked.getValue(), alreadyPicked);
					typeMap.put(ExpressFeedBackTongjiEnum.FeedBacked.getValue(), alreadyFeedBack);
					typeMap.put(ExpressFeedBackTongjiEnum.TodayNotFeedBack.getValue(), todayNotFeedBack);
					typeMap.put(ExpressFeedBackTongjiEnum.YestodayNotFeedBack.getValue(), yestodayNotFeedBack);

					deliveryInCountMap.put(deliverObj.getLong("deliveryid"), typeMap);
				}
			}

		}
		return deliveryInCountMap;

	}

	/**
	 *
	 * @param expressPreOrders
	 * @return
	 */
	private List<ExpressFeedBackView> getFeedBackStateViews(List<ExpressPreOrder> expressPreOrders, User user) {
		String preOrderNos = null;
		List<ExpressFeedBackView> expressFeedBackViewList = new ArrayList<ExpressFeedBackView>();
		List<Customer> customerList = this.customerDAO.getAllCustomersNew();
		List<User> userList = this.userDAO.getAllUser();
		if (expressPreOrders.size() > 0) {
			if ((preOrderNos == null) || preOrderNos.equals("")) {
				StringBuffer preOrderBuffer = new StringBuffer();
				for (ExpressPreOrder epo : expressPreOrders) {
					preOrderBuffer = preOrderBuffer.append("'").append(epo.getPreOrderNo()).append("',");
				}
				preOrderNos = preOrderBuffer.substring(0, preOrderBuffer.length() - 1);
			}

			List<ExpressPreOrder> preOrderList = this.preOrderDao.getPreOrderByPreOrderNos(preOrderNos, user);

			for (ExpressPreOrder epo : expressPreOrders) {
				ExpressFeedBackView efbv = this.getExpressFeedBackView(epo, customerList, userList, preOrderList);
				if (efbv != null) { // 数据不正确时会返回null
					expressFeedBackViewList.add(efbv);
				}
			}
		}
		return expressFeedBackViewList;
	}

	/**
	 * 新增
	 *
	 * @param epo
	 * @param customerList
	 * @param userList
	 * @param preOrderList
	 * @return
	 */
	private ExpressFeedBackView getExpressFeedBackView(ExpressPreOrder epo, List<Customer> customerList, List<User> userList, List<ExpressPreOrder> preOrderList) {
		ExpressFeedBackView efb = new ExpressFeedBackView();
		// 属性的转换 实体转为页面要显示的内容 TODO
		BeanUtils.copyProperties(epo, efb);
		ExpressPreOrder preOrder = null;
		if (preOrderList == null) {
			preOrder = this.preOrderDao.getPreOrderByOrderNo(epo.getOrderNo());
		} else {
			for (ExpressPreOrder c : preOrderList) {
				if (c.getPreOrderNo().equals(epo.getPreOrderNo())) {
					preOrder = c;
					break;
				}
			}
		}
		if (preOrder == null) {
			this.logger.warn("cwborder {} not exist" + epo.getPreOrderNo());
			return null;
		}

		return efb;
	}

	/**
	 * 查询预订单记录
	 * @param preOrderNo
	 * @return
	 */
	public ExpressPreOrder getPreOrderByOrderNo(String preOrderNo) {
		return this.preOrderDao.getPreOrderByOrderNo(preOrderNo);
	}

	/**
	 * 获取反馈的信息
	 * @param preOrderNo
	 * @return
	 */
	public Map<String, Object> getFeedBackInfo(String preOrderNo) {
		Map<String, Object> infoMap = new HashMap<String, Object>();
		// 揽件失败原因
		List<Reason> pickFailedReason = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.PickFailed.getValue());
		// 站点超区原因
		List<Reason> areaWrongReason = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.WrongArea.getValue());
		// 揽件超区原因
		List<Reason> pickWrongReason = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.PickWrong.getValue());
		// 揽件延迟原因
		List<Reason> pickDelayReason = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.PickDelay.getValue());

		// 2.查询要反馈的预订单
		ExpressPreOrder preOrder = this.getPreOrderByOrderNo(preOrderNo);
		// 3.组装成页面显示内容
		ExpressFeedBackView feedBackView = ExpressFeedBackView.copyValue(preOrder);

		infoMap.put("pickFailedReason", pickFailedReason);
		infoMap.put("areaWrongReason", areaWrongReason);
		infoMap.put("pickWrongReason", pickWrongReason);
		infoMap.put("pickDelayReason", pickDelayReason);
		infoMap.put("feedBackView", feedBackView);
		infoMap.put("preOrder", preOrder);

		return infoMap;
	}

	/**
	 * 查询统计数据
	 * @param deliveryId
	 */
	public Map<String, Object> queryFeedBackDTOData(long deliveryId, User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		ExpressFeedBackDTO feedBackDTO = new ExpressFeedBackDTO();
		List<ExpressPreOrder> expressPreOrders = this.preOrderDao.getExpressFeedBackStateByDeliver(deliveryId, user);
		// 获取每个表格的数据
		List<ExpressFeedBackView> preOrderAndFeedBackState = this.getFeedBackStateViews(expressPreOrders, null);
		feedBackDTO.analysisFeedBackStateList(preOrderAndFeedBackState);
		map.put("feedBackDTO", feedBackDTO);
		map.put("preOrderAndFeedBackState", preOrderAndFeedBackState);
		return map;
	}

	/**
	 * 校验运单号是否存在
	 * @param transNo
	 * @return
	 */
	public ExpressOpeAjaxResult checkTransNoIsRepeat(String transNo) {
		ExpressOpeAjaxResult result = new ExpressOpeAjaxResult();
		if (!Tools.isEmpty(transNo)) {
			Integer count = this.preOrderDao.checkTransNoIsRepeat(transNo);
			if ((count != null) && (count.intValue() > 0)) {
				result.setStatus(true);
			} else {
				result.setStatus(false);
			}
		}
		return result;
	}

	/**
	 *
	 * @Title: getSessionUser
	 * @description 获取登录用户
	 * @author 刘武强
	 * @date  2015年10月19日上午10:18:46
	 * @param  @return
	 * @return  User
	 * @throws
	 */
	public User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

}
