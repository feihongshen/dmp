package cn.explink.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.FnOrgOrderAdjustRecordDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.OrgOrderAdjustmentRecord;
import cn.explink.enumutil.BillAdjustTypeEnum;
import cn.explink.enumutil.FnCwbStatusEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class OrgOrderAdjustRecordService {
	 @Autowired
	 FnOrgOrderAdjustRecordDAO orgOrderAdjustRecordDao;
	 
		private static Logger logger = LoggerFactory.getLogger(OrgOrderAdjustRecordService.class);

	 
	 /**
	  * add by bruce shangguan 20160831
	  * 处理上门退订单取消后，添加相应的站点签收调整记录
	  * @param cwb
	  */
	public void handleAdjustRecordForShangMenTuiSuccess(CwbOrder cwbOrder , DeliveryState deliverState){
		String signTime = deliverState.getSign_time() ;
		long dateDiff = DateTimeUtil.dateDiff("day", DateTimeUtil.getNowTime(), signTime) ;
		logger.info("取消上门退，订单[{}]最后一次反馈为上门退成功的时间是[{}]，此刻和该订单最后一次反馈为上门退成功的时间间隔[{}]天，该订单应退金额:{},该订单应收运费是:{}", cwbOrder.getCwb(),signTime, dateDiff,cwbOrder.getPaybackfee(),cwbOrder.getShouldfare());
		if(dateDiff == 0){
			return ;
		}
		BigDecimal paybackFee = cwbOrder.getPaybackfee() ;
		BigDecimal shouldfare = cwbOrder.getShouldfare() ;
		// 判断订单应退金额是否大于0，若是则生成一条订单货款调整记录，调整金额=-货款金额
		if(paybackFee != null && paybackFee.compareTo(BigDecimal.ZERO) == 1){
			OrgOrderAdjustmentRecord record = new OrgOrderAdjustmentRecord() ;
			record.setModifyFee(paybackFee);
			record.setAdjustAmount(paybackFee.negate());
			record.setAdjustType(BillAdjustTypeEnum.OrderFee.getValue());
			record.setStatus(FnCwbStatusEnum.Received.getIndex());
			this.createAdjustRecordForCancelShangMenTuiSuccess(record, cwbOrder, signTime);
		}
		//判断订单应收运费是否大于0，若是则生成一条订单运费调整记录，调整金额=-应收运费
		if(shouldfare != null &&  shouldfare.compareTo(BigDecimal.ZERO) == 1){
			OrgOrderAdjustmentRecord record = new OrgOrderAdjustmentRecord() ;
			record.setModifyFee(shouldfare);
			record.setAdjustAmount(shouldfare.negate());
			// 调整金额为运费调整
			record.setAdjustType(BillAdjustTypeEnum.ExpressFee.getValue());
			record.setStatus(FnCwbStatusEnum.Received.getIndex());
			this.createAdjustRecordForCancelShangMenTuiSuccess(record, cwbOrder, signTime);
		}
	}
	
	
	/**
	 *add by bruce shangguan 20160831
	 * 处理上门退订单取消后，添加相应的账单调整记录
	 * @param record
	 * @param cwbOrder
	 * @param signTime
	 */
	public void createAdjustRecordForCancelShangMenTuiSuccess(OrgOrderAdjustmentRecord record, CwbOrder cwbOrder , String signTime){
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
		this.orgOrderAdjustRecordDao.creOrderAdjustmentRecord(record);
	}

}
