package cn.explink.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.FnCustomerBillDetailDAO;
import cn.explink.dao.FnOrgBillDetailDAO;
import cn.explink.dao.OrgBillAdjustmentRecordDao;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EdtiCwb_DeliveryStateDetail;
import cn.explink.domain.FnOrgBillDetail;
import cn.explink.domain.OrgBillAdjustmentRecord;
import cn.explink.domain.User;
import cn.explink.enumutil.AdjustWayEnum;
import cn.explink.enumutil.BillAdjustTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.PayMethodSwitchEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.util.DateTimeUtil;

/**
 * 站点站内调整单
 * 
 * @author jiangyu 2015年3月30日
 *
 */
@Service
public class OrgBillAdjustmentRecordService {
	@Autowired
	OrgBillAdjustmentRecordDao orgBillAdjustmentRecordDao;

	@Autowired
	FnOrgBillDetailDAO fnOrgBillDetailDAO;

	@Autowired
	CwbDAO cwbDao;

	@Autowired
	DeliveryStateDAO deliverStateDao;

	@Autowired
	FnCustomerBillDetailDAO FnCustomerBillDetaildao;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	
	private Logger logger = LoggerFactory.getLogger(OrgBillAdjustmentRecordService.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 站内站点调整单 修改金额创建调整单
	 * 
	 * @param cwbOrder
	 * @param user
	 * @param receivablefee
	 * @param paybackfee
	 * @param flag
	 *            true是修改金额，false是修改支付方式
	 */
	public void createOrgBillAdjustRecord(CwbOrder order, User user, BigDecimal modifyFeeReceiveFee, BigDecimal modifyPaybackfee) {

		List<FnOrgBillDetail> fnOrgBillDetails = new ArrayList<FnOrgBillDetail>();

		List<OrgBillAdjustmentRecord> adjustRecord = new ArrayList<OrgBillAdjustmentRecord>();

		OrgBillAdjustmentRecord record = new OrgBillAdjustmentRecord();
		// 通过订单号查询出站点账单的记录
		fnOrgBillDetails = this.fnOrgBillDetailDAO.getFnOrgBillDetailByCwb(order.getCwb());
		// 订单的类型
		Integer orderType = order.getCwbordertypeid();
		if ((null != fnOrgBillDetails) && (fnOrgBillDetails.size() > 0)) {// 该订单已经生成过账单
			adjustRecord = this.orgBillAdjustmentRecordDao.getAdjustmentRecordByCwb(order.getCwb());
			DeliveryState deliveryState = this.deliverStateDao.getDeliverSignTime(order.getCwb());
			// if(adjustRecord.size()<=0){//没有生成过调整单记录
			// 根据不同的订单类型
			// 配送
			record.setOrderNo(order.getCwb());
			record.setBillNo("");
			record.setBillId(0L);
			record.setAdjustBillNo("");
			record.setCustomerId(order.getCustomerid());
			record.setReceiveFee(order.getReceivablefee());
			record.setRefundFee(order.getPaybackfee());
			// 是否修改过支付方式的标识PayMethodSwitchEnum.No.getValue()
			record.setPayWayChangeFlag(PayMethodSwitchEnum.No.getValue());

			if (CwbOrderTypeIdEnum.Peisong.getValue() == orderType.intValue()) {
				record.setModifyFee(modifyFeeReceiveFee);
				record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
				record.setRemark(order.getReceivablefee() + "元修改成" + modifyFeeReceiveFee + "元");

			} else if (CwbOrderTypeIdEnum.Shangmentui.getValue() == orderType.intValue()) {
				// 上门退
				// record.setReceiveFee(order.getReceivablefee());
				// record.setRefundFee(order.getPaybackfee());
				record.setModifyFee(modifyPaybackfee);
				// record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
				record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
				record.setRemark(order.getPaybackfee() + "元修改成" + modifyPaybackfee + "元");

			} else if (CwbOrderTypeIdEnum.Shangmenhuan.getValue() == orderType.intValue()) {
				// 获取订单的应收和应退金额
				BigDecimal orderReceiveFee = order.getReceivablefee() == null ? BigDecimal.ZERO : order.getReceivablefee();
				BigDecimal orderPayBackFee = order.getPaybackfee() == null ? BigDecimal.ZERO : order.getPaybackfee();
				// 计算是否修改过金额
				BigDecimal changeReceiveableFee = orderReceiveFee.subtract(modifyFeeReceiveFee == null ? BigDecimal.ZERO : modifyFeeReceiveFee);
				BigDecimal changePayBackFee = orderPayBackFee.subtract(modifyPaybackfee == null ? BigDecimal.ZERO : modifyPaybackfee);
				// 判断
				if ((modifyPaybackfee.doubleValue() > 0) && (modifyFeeReceiveFee.doubleValue() <= 0)) {
					record.setModifyFee(modifyPaybackfee);
					// record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
					record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
					record.setRemark(order.getPaybackfee() + "元修改成" + modifyPaybackfee + "元");
				} else if ((modifyFeeReceiveFee.doubleValue() > 0) && (modifyPaybackfee.doubleValue() <= 0)) {
					record.setModifyFee(modifyFeeReceiveFee);
					record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
					record.setRemark(order.getReceivablefee() + "元修改成" + modifyFeeReceiveFee + "元");
				}

			}
			record.setDeliverId(order.getDeliverid());
			record.setCreator(this.getSessionUser().getUsername());
			record.setCreateTime(new Date());
			record.setOrderType(orderType);
			// 订单的支付方式可能是新的支付方式
			Long oldPayWay = Long.valueOf(order.getPaywayid()) == null ? 1L : Long.valueOf(order.getPaywayid());
			Long newPayWay = order.getNewpaywayid() == null ? 0L : Long.valueOf(order.getNewpaywayid());
			if (oldPayWay.intValue() == newPayWay.intValue()) {
				record.setPayMethod(oldPayWay.intValue());
			} else {
				record.setPayMethod(newPayWay.intValue());
			}
			record.setDeliverybranchid(order.getDeliverybranchid());
			try {
				record.setSignTime(this.sdf.parse(deliveryState.getSign_time()));
			} catch (Exception e) {
				logger.error("", e);
			}
			record.setPayWayChangeFlag(0);
			this.orgBillAdjustmentRecordDao.creAdjustmentRecord(record);
			// }else{//该订单已经生成过调整单记录 不让其修改
			// //提示信息
			// }
		} else {
			// 该订单还没有生成过账单记录，不能生成调整单记录
		}
	}

	/**
	 * 站内站点调整单 重置审核状态
	 * 
	 * @param ec_dsd
	 */
	public void createAdjustment4ReFeedBack(String cwb, EdtiCwb_DeliveryStateDetail ec_dsd) {

		// List<FnOrgBillDetail> fnOrgBillDetails = new
		// ArrayList<FnOrgBillDetail>();

		OrgBillAdjustmentRecord record = new OrgBillAdjustmentRecord();
		CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
		// 订单的类型
		Integer orderType = order.getCwbordertypeid();
		if (order.getFnorgbillid() > 0) {
			DeliveryState deliveryState = ec_dsd.getDs();
			// 根据不同的订单类型
			record.setOrderNo(order.getCwb());
			record.setBillNo("");
			record.setBillId(0L);
			record.setAdjustBillNo("");
			record.setCustomerId(order.getCustomerid());
			record.setReceiveFee(order.getReceivablefee());
			record.setRefundFee(order.getPaybackfee());
			// 是否修改过支付方式的标识PayMethodSwitchEnum.No.getValue()
			record.setPayWayChangeFlag(PayMethodSwitchEnum.No.getValue());

			record.setDeliverId(order.getDeliverid());
			record.setCreator(this.getSessionUser().getUsername());
			record.setCreateTime(new Date());
			record.setOrderType(orderType);
			// 订单的支付方式可能是新的支付方式
			Long oldPayWay = Long.valueOf(order.getPaywayid()) == null ? 1L : Long.valueOf(order.getPaywayid());
			Long newPayWay = order.getNewpaywayid() == null ? 0L : Long.valueOf(order.getNewpaywayid());
			if (oldPayWay.intValue() == newPayWay.intValue()) {
				record.setPayMethod(oldPayWay.intValue());
			} else {
				record.setPayMethod(newPayWay.intValue());
			}
			record.setDeliverybranchid(order.getDeliverybranchid());
			if ((order.getPaybackfee() != null) && (order.getPaybackfee().compareTo(BigDecimal.ZERO) > 0)) {
				record.setModifyFee(BigDecimal.ZERO);
				record.setAdjustAmount(order.getPaybackfee());
			} else {
				record.setModifyFee(order.getReceivablefee());
				record.setAdjustAmount(BigDecimal.ZERO.subtract(order.getReceivablefee()));
			}
			try {
				record.setSignTime(this.sdf.parse(deliveryState.getSign_time()));
			} catch (Exception e) {
				logger.error("", e);
			}
			record.setPayWayChangeFlag(0);
			// 调整金额为货款调整
			record.setAdjustType(BillAdjustTypeEnum.OrderFee.getValue());
			// 记录运费
			record.setFreightAmount(ec_dsd.getOriInfactfare());
			this.orgBillAdjustmentRecordDao.creAdjustmentRecord(record);

		}
		if ((CwbOrderTypeIdEnum.Shangmentui.getValue() == order.getCwbordertypeid()) && (order.getFnorgfreightbillid() > 0)) {
			DeliveryState deliveryState = ec_dsd.getDs();
			// 根据不同的订单类型
			record.setOrderNo(order.getCwb());
			record.setBillNo("");
			record.setBillId(0L);
			record.setAdjustBillNo("");
			record.setCustomerId(order.getCustomerid());
			record.setReceiveFee(order.getReceivablefee());
			record.setRefundFee(order.getPaybackfee());
			// 是否修改过支付方式的标识PayMethodSwitchEnum.No.getValue()
			record.setPayWayChangeFlag(PayMethodSwitchEnum.No.getValue());

			record.setDeliverId(order.getDeliverid());
			record.setCreator(this.getSessionUser().getUsername());
			record.setCreateTime(new Date());
			record.setOrderType(orderType);
			// 订单的支付方式可能是新的支付方式
			Long oldPayWay = Long.valueOf(order.getPaywayid()) == null ? 1L : Long.valueOf(order.getPaywayid());
			Long newPayWay = order.getNewpaywayid() == null ? 0L : Long.valueOf(order.getNewpaywayid());
			if (oldPayWay.intValue() == newPayWay.intValue()) {
				record.setPayMethod(oldPayWay.intValue());
			} else {
				record.setPayMethod(newPayWay.intValue());
			}
			record.setDeliverybranchid(order.getDeliverybranchid());
			try {
				record.setSignTime(this.sdf.parse(deliveryState.getSign_time()));
			} catch (Exception e) {
				logger.error("", e);
			}
			record.setPayWayChangeFlag(0);
			record.setModifyFee(ec_dsd.getOriInfactfare());
			record.setAdjustAmount(BigDecimal.ZERO.subtract(ec_dsd.getOriInfactfare()));
			// 调整金额为运费调整
			record.setAdjustType(BillAdjustTypeEnum.ExpressFee.getValue());
			this.orgBillAdjustmentRecordDao.creAdjustmentRecord(record);

		}
	}

	/**
	 * 站内站点调整单 归班审核
	 * 
	 * @param order
	 * @param deliverystate
	 */
	public void createAdjustment4GoToClassConfirm(CwbOrder order, DeliveryState deliverystate) {
		if (order.getFnorgbillid() > 0) {
			OrgBillAdjustmentRecord record = new OrgBillAdjustmentRecord();
			// 订单的类型
			// 根据不同的订单类型
			record.setOrderNo(order.getCwb());
			record.setBillNo("");
			record.setBillId(0L);
			record.setAdjustBillNo("");
			record.setCustomerId(order.getCustomerid());
			record.setReceiveFee(order.getReceivablefee());
			record.setRefundFee(order.getPaybackfee());
			// 是否修改过支付方式的标识PayMethodSwitchEnum.No.getValue()
			record.setPayWayChangeFlag(PayMethodSwitchEnum.No.getValue());

			record.setDeliverId(order.getDeliverid());
			record.setCreator(this.getSessionUser().getUsername());
			record.setCreateTime(new Date());
			record.setOrderType(order.getCwbordertypeid());
			// 订单的支付方式可能是新的支付方式
			Long oldPayWay = Long.valueOf(order.getPaywayid()) == null ? 1L : Long.valueOf(order.getPaywayid());
			Long newPayWay = order.getNewpaywayid() == null ? 0L : Long.valueOf(order.getNewpaywayid());
			if (oldPayWay.intValue() == newPayWay.intValue()) {
				record.setPayMethod(oldPayWay.intValue());
			} else {
				record.setPayMethod(newPayWay.intValue());
			}
			record.setDeliverybranchid(order.getDeliverybranchid());

			record.setModifyFee(order.getReceivablefee().subtract(order.getPaybackfee()));
			record.setAdjustAmount(order.getReceivablefee().subtract(order.getPaybackfee()));

			try {
				record.setSignTime(this.sdf.parse(deliverystate.getSign_time()));
			} catch (Exception e) {
				logger.error("", e);
			}
			record.setPayWayChangeFlag(0);
			// 调整金额为货款调整
			record.setAdjustType(BillAdjustTypeEnum.OrderFee.getValue());
			// 记录运费
			record.setFreightAmount(order.getInfactfare());
			this.orgBillAdjustmentRecordDao.creAdjustmentRecord(record);

		}
		if ((CwbOrderTypeIdEnum.Shangmentui.getValue() == order.getCwbordertypeid()) && (order.getFnorgfreightbillid() > 0)) {
			OrgBillAdjustmentRecord record = new OrgBillAdjustmentRecord();
			// 根据不同的订单类型
			record.setOrderNo(order.getCwb());
			record.setBillNo("");
			record.setBillId(0L);
			record.setAdjustBillNo("");
			record.setCustomerId(order.getCustomerid());
			record.setReceiveFee(order.getReceivablefee());
			record.setRefundFee(order.getPaybackfee());
			// 是否修改过支付方式的标识PayMethodSwitchEnum.No.getValue()
			record.setPayWayChangeFlag(PayMethodSwitchEnum.No.getValue());

			record.setDeliverId(order.getDeliverid());
			record.setCreator(this.getSessionUser().getUsername());
			record.setCreateTime(new Date());
			record.setOrderType(order.getCwbordertypeid());
			// 订单的支付方式可能是新的支付方式
			Long oldPayWay = Long.valueOf(order.getPaywayid()) == null ? 1L : Long.valueOf(order.getPaywayid());
			Long newPayWay = order.getNewpaywayid() == null ? 0L : Long.valueOf(order.getNewpaywayid());
			if (oldPayWay.intValue() == newPayWay.intValue()) {
				record.setPayMethod(oldPayWay.intValue());
			} else {
				record.setPayMethod(newPayWay.intValue());
			}
			record.setDeliverybranchid(order.getDeliverybranchid());
			try {
				record.setSignTime(this.sdf.parse(deliverystate.getSign_time()));
			} catch (Exception e) {
				logger.error("", e);
			}
			record.setPayWayChangeFlag(0);
			// 调整金额为运费调整
			record.setAdjustType(BillAdjustTypeEnum.ExpressFee.getValue());
			record.setModifyFee(order.getInfactfare());
			record.setAdjustAmount(order.getInfactfare());
			this.orgBillAdjustmentRecordDao.creAdjustmentRecord(record);

		}
	}

	/**
	 * 修改支付方式
	 * 
	 * @param cwb
	 * @param paywayid
	 * @param newpaywayid
	 */
	public void createAdjustmentRecordByPayType(String cwb, int payWayId, int newPayWayId) {

		List<FnOrgBillDetail> fnOrgBillDetails = new ArrayList<FnOrgBillDetail>();

		List<OrgBillAdjustmentRecord> adjustRecord = new ArrayList<OrgBillAdjustmentRecord>();

		CwbOrder order = this.cwbDao.getCwbByCwb(cwb);

		// 通过订单号查询出站点账单的记录
		fnOrgBillDetails = this.fnOrgBillDetailDAO.getFnOrgBillDetailByCwb(order.getCwb());

		if ((null != fnOrgBillDetails) && (fnOrgBillDetails.size() > 0)) {// 该订单已经生成过账单
			adjustRecord = this.orgBillAdjustmentRecordDao.getAdjustmentRecordByCwb(order.getCwb());
			// if(adjustRecord.size()<=0){//没有生成过调整单记录
			DeliveryState deliveryState = this.deliverStateDao.getDeliverSignTime(order.getCwb());
			// 正向
			this.createAdjustRecordByModifyPayMethod(order, deliveryState, payWayId, newPayWayId, AdjustWayEnum.Forward);
			// 逆向
			this.createAdjustRecordByModifyPayMethod(order, deliveryState, payWayId, newPayWayId, AdjustWayEnum.Reverse);
			// }
		}
	}

	/**
	 *
	 * @param order
	 * @param payWayId
	 * @param newPayWayId
	 * @param adjustWay
	 *            枚举
	 */
	private void createAdjustRecordByModifyPayMethod(CwbOrder order, DeliveryState deliveryState, int payWayId, int newPayWayId, AdjustWayEnum adjustWay) {

		OrgBillAdjustmentRecord record = new OrgBillAdjustmentRecord();
		// 订单的类型
		Integer orderType = order.getCwbordertypeid();

		record.setOrderNo(order.getCwb());
		record.setBillNo("");
		record.setBillId(0L);
		record.setAdjustBillNo("");
		record.setCustomerId(order.getCustomerid());

		record.setReceiveFee(order.getReceivablefee());
		record.setRefundFee(order.getPaybackfee());

		BigDecimal modifyFeeReceiveFee = BigDecimal.ZERO;
		BigDecimal modifyPaybackfee = BigDecimal.ZERO;

		if (AdjustWayEnum.Forward.getValue().equals(adjustWay.getValue())) {// 正向--负的记录
			// 是否修改过支付方式的标识
			record.setPayWayChangeFlag(PayMethodSwitchEnum.No.getValue());
			if (CwbOrderTypeIdEnum.Peisong.getValue() == orderType.intValue()) {
				record.setModifyFee(modifyFeeReceiveFee);
				record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));

			} else if (CwbOrderTypeIdEnum.Shangmentui.getValue() == orderType.intValue()) {
				// 上门退
				record.setModifyFee(modifyPaybackfee);
				// record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
				record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));

			} else if (CwbOrderTypeIdEnum.Shangmenhuan.getValue() == orderType.intValue()) {
				// 上门退
				// 修改的是应退金额
				if ((modifyPaybackfee.doubleValue() > 0) && (modifyFeeReceiveFee.doubleValue() <= 0)) {
					record.setModifyFee(modifyPaybackfee);
					// record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
					record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));

				} else if ((modifyFeeReceiveFee.doubleValue() > 0) && (modifyPaybackfee.doubleValue() <= 0)) {

					record.setModifyFee(modifyFeeReceiveFee);
					record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
				} else {
					record.setModifyFee(modifyFeeReceiveFee);
					record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
				}
			}
			// 原先的支付方式
			record.setPayMethod(Long.valueOf(payWayId).intValue());
		} else {// 逆向 -- 正的记录
				// 是否修改过支付方式的标识
			record.setPayWayChangeFlag(PayMethodSwitchEnum.Yes.getValue());
			if (CwbOrderTypeIdEnum.Peisong.getValue() == orderType.intValue()) {
				record.setModifyFee(modifyFeeReceiveFee);
				record.setAdjustAmount(order.getReceivablefee().subtract(modifyFeeReceiveFee));
				// record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));

			} else if (CwbOrderTypeIdEnum.Shangmentui.getValue() == orderType.intValue()) {
				// 上门退
				record.setModifyFee(modifyPaybackfee);
				record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
				// record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
			} else if (CwbOrderTypeIdEnum.Shangmenhuan.getValue() == orderType.intValue()) {
				// 上门退
				// 修改的是应退金额
				if ((modifyPaybackfee.doubleValue() > 0) && (modifyFeeReceiveFee.doubleValue() <= 0)) {
					record.setModifyFee(modifyPaybackfee);
					record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
					// record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
				} else if ((modifyFeeReceiveFee.doubleValue() > 0) && (modifyPaybackfee.doubleValue() <= 0)) {
					record.setModifyFee(modifyFeeReceiveFee);
					record.setAdjustAmount(order.getReceivablefee().subtract(modifyFeeReceiveFee));
				} else {
					record.setModifyFee(modifyFeeReceiveFee);
					record.setAdjustAmount(order.getReceivablefee().subtract(modifyFeeReceiveFee));
				}
			}
			// 新的支付方式
			record.setPayMethod(Long.valueOf(newPayWayId).intValue());
		}

		record.setRemark(PaytypeEnum.getByValue(payWayId).getText() + "修改成" + PaytypeEnum.getByValue(newPayWayId).getText());

		record.setDeliverId(order.getDeliverid());
		record.setCreator(this.getSessionUser().getUsername());
		record.setCreateTime(new Date());
		record.setOrderType(orderType);
		record.setDeliverybranchid(order.getDeliverybranchid());
		try {
			record.setSignTime(this.sdf.parse(deliveryState.getSign_time()));
		} catch (ParseException e) {
			logger.error("", e);
		}
		this.orgBillAdjustmentRecordDao.creAdjustmentRecord(record);
	}
	
	/**
	 * add by bruce shangguan 20160831
	 * 处理上门退订单取消后，添加相应的账单调整记录
	 * @param cwb
	 */
	public void handleAdjustRecordForShangMenTuiSuccess(CwbOrder cwbOrder , DeliveryState deliverState){
		long orgFreightBillId = cwbOrder.getFnorgfreightbillid() ;
		long orgBillId = cwbOrder.getFnorgbillid() ;
		String signTime = deliverState.getSign_time() ;
		logger.info("接口取消上门退，订单[{}]对应的站点账单id[{}] , 对应的运费账单id:[{}]", cwbOrder.getCwb() , orgBillId,orgFreightBillId);
		if(orgFreightBillId > 0){
			OrgBillAdjustmentRecord record = new OrgBillAdjustmentRecord();
			// 调整金额为运费调整
			record.setAdjustType(BillAdjustTypeEnum.ExpressFee.getValue());
		    // 记录运费
			record.setModifyFee(cwbOrder.getInfactfare());
			record.setAdjustAmount(BigDecimal.ZERO.subtract(cwbOrder.getInfactfare()));
			this.createAdjustRecordForCancelShangMenTuiSuccess(record, cwbOrder,signTime);
		}
		if(orgBillId > 0){
			OrgBillAdjustmentRecord record = new OrgBillAdjustmentRecord();
			// 调整金额为货款调整
			record.setAdjustType(BillAdjustTypeEnum.OrderFee.getValue());
		    // 记录货款金额
			if ((cwbOrder.getPaybackfee() != null) && (cwbOrder.getPaybackfee().compareTo(BigDecimal.ZERO) > 0)) {
				record.setModifyFee(BigDecimal.ZERO);
				record.setAdjustAmount(cwbOrder.getPaybackfee());
			} else {
				record.setModifyFee(cwbOrder.getReceivablefee());
				record.setAdjustAmount(BigDecimal.ZERO.subtract(cwbOrder.getReceivablefee()));
			}
			record.setGoodsAmount(cwbOrder.getReceivablefee());
			this.createAdjustRecordForCancelShangMenTuiSuccess(record, cwbOrder,signTime);
		}
	}
	
	
	/**
	 *add by bruce shangguan 20160831
	 * 处理上门退订单取消后，添加相应的账单调整记录
	 * @param record
	 * @param cwbOrder
	 * @param signTime
	 */
	public void createAdjustRecordForCancelShangMenTuiSuccess(OrgBillAdjustmentRecord record , CwbOrder cwbOrder , String signTime){
		record.setOrderNo(cwbOrder.getCwb());
		record.setOrderType(cwbOrder.getCwbordertypeid());
		record.setBillNo("");
		record.setBillId(0L);
		record.setAdjustBillNo("");
		record.setPayWayChangeFlag(0);
		record.setReceiveFee(cwbOrder.getReceivablefee());
		record.setRefundFee(cwbOrder.getPaybackfee());
		BigDecimal infactFare = cwbOrder.getInfactfare();
		BigDecimal shouldFare = cwbOrder.getShouldfare() ;
		if(infactFare != null && infactFare.compareTo(BigDecimal.ZERO) != 0){
			record.setFreightAmount(infactFare);
		}else{
			record.setFreightAmount(shouldFare);
		}
		// 订单的支付方式可能是新的支付方式
		Long oldPayWay = Long.valueOf(cwbOrder.getPaywayid()) == null ? 1L : Long.valueOf(cwbOrder.getPaywayid());
		Long newPayWay = cwbOrder.getNewpaywayid() == null ? 0L : Long.valueOf(cwbOrder.getNewpaywayid());
		if (oldPayWay.intValue() == newPayWay.intValue()) {
			record.setPayMethod(oldPayWay.intValue());
		} else {
			record.setPayMethod(newPayWay.intValue());
		}
		record.setCustomerId(cwbOrder.getCustomerid());
		record.setCreator("接口对接取消");
		record.setDeliverId(cwbOrder.getDeliverid());
		record.setCreateTime(DateTimeUtil.nowDate());
		record.setDeliverybranchid(cwbOrder.getDeliverybranchid());
		record.setSignTime(!StringUtils.isEmpty(signTime)?DateTimeUtil.parseStringToDate(signTime):null);
		this.orgBillAdjustmentRecordDao.creAdjustmentRecord(record);
	}

}
