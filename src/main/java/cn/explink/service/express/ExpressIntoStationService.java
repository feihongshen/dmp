package cn.explink.service.express;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.ExpressIntoStationDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.VO.express.ExpressIntoStationCountVO;
import cn.explink.domain.VO.express.ExpressIntoStationVO;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.VO.express.ExpressParamsVO;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.enumutil.express.ExpressSettleWayEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;

import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;

@Service
public class ExpressIntoStationService {
	private static Logger logger = LoggerFactory.getLogger(ExpressIntoStationService.class);

	@Autowired
	private ExpressIntoStationDAO expressIntoStationDAO;

	@Autowired
	ProvinceDAO provinceDAO;

	@Autowired
	CityDAO cityDAO;

	@Autowired
	CwbOrderService cwbOrderService;

	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	/**
	 * 揽件入站查询记录
	 * @param page
	 * @param params
	 * @param uList
	 * @return
	 */
	public Map<String, Object> queryExpressListByPage(long page, ExpressParamsVO params, List<User> uList) {
		Long deliveryId = params.getDeliveryId();
		Integer processState = params.getProcessState();
		Integer payType = params.getPayType();
		Long branchId = params.getBranchId();
		Map<Long, String> deliverManMap = new HashMap<Long, String>();
		if ((uList != null) && (uList.size() > 0)) {
			for (User user : uList) {
				deliverManMap.put(user.getUserid(), user.getRealname());
			}
		}
		Map<String, Object> addressInfo = this.getProvinceAndCityInfo();
		Map<String, Object> map = new HashMap<String, Object>();
		List<ExpressIntoStationVO> orderList = this.expressIntoStationDAO.getRecordListByPage(page, deliveryId, processState, payType, branchId, deliverManMap, addressInfo);
		Long recordCount = this.expressIntoStationDAO.getExpressRecordCount(deliveryId, processState, payType, branchId);
		map.put("orders", orderList);
		map.put("recordCount", recordCount);
		return map;
	}

	/**
	 * 查询汇总记录数
	 * @param ids
	 * @return
	 */
	public Map<String, Object> getSummaryRecord(String ids) {
		//分别记录结果数
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("nowPayMap", new ExpressIntoStationCountVO());
		resultMap.put("monthPayMap", new ExpressIntoStationCountVO());
		resultMap.put("arrivePayMap", new ExpressIntoStationCountVO());
		List<ExpressIntoStationCountVO> recordList = this.expressIntoStationDAO.getExpressOrderSummaryCountBefore(ids);
		if ((recordList != null) && (recordList.size() > 0)) {
			for (ExpressIntoStationCountVO record : recordList) {
				if (ExpressSettleWayEnum.NowPay.getValue().equals(record.getPayType())) {
					resultMap.put("nowPayMap", record);
				} else if (ExpressSettleWayEnum.MonthPay.getValue().equals(record.getPayType())) {
					resultMap.put("monthPayMap", record);
				} else if (ExpressSettleWayEnum.ArrivePay.getValue().equals(record.getPayType())) {
					resultMap.put("arrivePayMap", record);
				}
			}
		}

		Long totalCount = 0L;
		BigDecimal totalFee = BigDecimal.ZERO;
		ExpressIntoStationCountVO nowPay = (ExpressIntoStationCountVO) resultMap.get("nowPayMap");
		ExpressIntoStationCountVO monthPay = (ExpressIntoStationCountVO) resultMap.get("monthPayMap");
		ExpressIntoStationCountVO arrivePay = (ExpressIntoStationCountVO) resultMap.get("arrivePayMap");
		totalCount = nowPay.getCount() + monthPay.getCount() + arrivePay.getCount();
		totalFee = nowPay.getSumFee();

		List<String> collectorNames = this.expressIntoStationDAO.deliveryManList(ids);
		String deliveryMan = "";
		if ((collectorNames != null) && (collectorNames.size() > 0)) {
			for (int i = 0; i < collectorNames.size(); i++) {
				if ("".equals(collectorNames.get(i))) {
					continue;
				}
				if (i >= 2) {
					deliveryMan = (deliveryMan.substring(0, deliveryMan.length() - 1)) + "...";
					break;
				} else {
					deliveryMan += collectorNames.get(i) + ",";
				}
			}
			resultMap.put("totalRecord", new ExpressIntoStationCountVO());
		}
		//		ExpressIntoStationCountVO record = expressIntoStationDAO.getExpressOrderSummaryCount(ids);
		//汇总记录
		ExpressIntoStationCountVO record = new ExpressIntoStationCountVO();
		record.setCount(totalCount);
		record.setSumFee(totalFee);
		if (record != null) {
			resultMap.put("totalRecord", record);
			resultMap.put("deliverNameStr", deliveryMan);
		}

		return resultMap;
	}

	/**
	 * 确认入站操作
	 * @param user
	 * @param params
	 * @return
	 */
	@Transactional
	public ExpressOpeAjaxResult executeIntoStationOpe(User user, ExpressParamsVO params) {
		ExpressOpeAjaxResult result = new ExpressOpeAjaxResult();
		try {
			ExpressIntoStationService.logger.info("揽件入站：确认入站操作开始……");
			//当前站点
			Long currentBranchId = user.getBranchid();
			//下一站 [如何确定] TODO
			Long nextBranchId = 0L;
			String intoStationName = this.branchDAO.getBranchName(currentBranchId);//入站站点名称
			if (null == intoStationName) {
				intoStationName = "";
			}
			//执行入站的订单id
			String ids = params.getIds();
			String idsBefore = "";
			String idsAfter = "";
			String idsOther = "";
			//收件员
			//Long deliverId = params.getDeliveryId();
			//交件时间
			String pickExpressTime = params.getPickExpressTime();
			
			//将状态为揽件入站和运单录入的订单id分别存储起来
			List<CwbOrder> ordersBefore = this.cwbDAO.getCwbsBycwbIds(ids);
			for(CwbOrder orderBefore : ordersBefore){
				if(orderBefore.getFlowordertype()==FlowOrderTypeEnum.YunDanLuRu.getValue()){
					idsBefore += ","+orderBefore.getOpscwbid();
				}else if(orderBefore.getFlowordertype()==FlowOrderTypeEnum.LanJianRuZhan.getValue()){
					idsAfter += ","+orderBefore.getOpscwbid();
				}else{
					idsOther += ","+orderBefore.getOpscwbid();
				}
			}
			Integer countTotal = 0;
			if(!"".equals(idsBefore)){
				idsBefore = idsBefore.substring(1,idsBefore.length());
				//更新订单的相关信息
				Integer count = this.expressIntoStationDAO.executeIntoStationOpe(idsBefore, currentBranchId, nextBranchId, pickExpressTime, intoStationName);
				//1.创建流程跟踪记录等
				List<CwbOrder> orders = this.cwbDAO.getCwbsBycwbIds(idsBefore);
				if ((orders != null) && (orders.size() > 0)) {
					for (CwbOrder order : orders) {
						this.cwbOrderService.createFloworder(user, currentBranchId, order, FlowOrderTypeEnum.LanJianRuZhan, "", System.currentTimeMillis());
					}
				}
				//调用tps接口将反馈结果回传给tps
				this.executeTpsTransInterface(orders, user);
				countTotal += count;
			}
			if(!"".equals(idsAfter)){
				idsAfter = idsAfter.substring(1,idsAfter.length());
				Integer count = this.expressIntoStationDAO.executeIntoStationOpe(idsAfter, currentBranchId, nextBranchId, pickExpressTime, intoStationName);
				//1.创建流程跟踪记录等
				List<CwbOrder> orders = this.cwbDAO.getCwbsBycwbIds(idsAfter);
				if ((orders != null) && (orders.size() > 0)) {
					for (CwbOrder order : orders) {
						this.cwbOrderService.createFloworder(user, currentBranchId, order, FlowOrderTypeEnum.LanJianQueRen, "", System.currentTimeMillis());
					}
				}
				countTotal += count;
			}
			if(!"".equals(idsOther)){
				idsOther = idsOther.substring(1,idsOther.length());
				Integer count = this.expressIntoStationDAO.executeIntoStationForOth(idsOther,currentBranchId,pickExpressTime,intoStationName);
				countTotal += count;
			}
			result.setRecordCount(countTotal);
			result.setStatus(true);
		} catch (Exception e) {
			result.setStatus(false);
			result.setMsg("确认入站操作失败！");
			ExpressIntoStationService.logger.info("确认入站操作失败，失败原因是：" + e.getMessage());
		}
		return result;
	}

	/**
	 * 调用tps运单反馈接口
	 * @param orders
	 */
	private void executeTpsTransInterface(List<CwbOrder> orders, User user) {
		if ((orders != null) && (orders.size() > 0)) {
			for (CwbOrder order : orders) {
				ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
				PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
				Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
				transNoFeedBack.setTransportNo(order.getCwb());
				transNoFeedBack.setOperateOrg(branch.getTpsbranchcode());
				transNoFeedBack.setOperater(user.getRealname());
				transNoFeedBack.setOperateTime(System.currentTimeMillis());
				transNoFeedBack.setOperateType(FeedbackOperateTypeEnum.InboundScan.getValue());
				transNoFeedBack.setReason("");

				/*//拼接描述
				JoinMessageVO contextVar = new JoinMessageVO();
				contextVar.setOperationType(TpsOperationEnum.ArrivalScan.getValue());//揽件入站对应入站扫描
				contextVar.setStation(branch.getBranchname());//站点名称
				contextVar.setOperator(user.getRealname());
				contextVar.connectMessage();
				transNoFeedBack.setTransportDetail(contextVar.getTrackMessage());*/
				paramObj.setTransNoFeedBack(transNoFeedBack);
				//发送JMS消息
				this.tpsInterfaceExecutor.executTpsInterface(paramObj);
			}
		}
	}

	/**
	 * 查询省市信息
	 * @return
	 */
	private Map<String, Object> getProvinceAndCityInfo() {
		Map<String, Object> addressInfo = new HashMap<String, Object>();
		Map<Integer, String> provinceMap = new HashMap<Integer, String>();
		Map<Integer, String> cityMap = new HashMap<Integer, String>();
		List<AdressVO> provinces = this.provinceDAO.getAllProvince();
		List<AdressVO> cities = this.cityDAO.getAllCity();
		if ((provinces != null) && (provinces.size() > 0)) {
			for (AdressVO adressVO : provinces) {
				provinceMap.put(adressVO.getId(), adressVO.getName());
			}
		}
		if ((cities != null) && (cities.size() > 0)) {
			for (AdressVO adressVO : cities) {
				cityMap.put(adressVO.getId(), adressVO.getName());
			}
		}
		addressInfo.put("province", provinceMap);
		addressInfo.put("city", cityMap);
		return addressInfo;
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
