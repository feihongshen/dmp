package cn.explink.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.FnCustomerBillDetailDAO;
import cn.explink.dao.FnOrgBillDetailDAO;
import cn.explink.dao.OrgBillAdjustmentRecordDao;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.FnOrgBillDetail;
import cn.explink.domain.OrgBillAdjustmentRecord;
import cn.explink.domain.User;
import cn.explink.enumutil.AdjustWayEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PayMethodSwitchEnum;
import cn.explink.enumutil.PaytypeEnum;

/**
 * 站点站内调整单
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
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
	/**
	 * 站内站点调整单  修改金额创建调整单
	 * @param cwbOrder
	 * @param user
	 * @param receivablefee
	 * @param paybackfee
	 * @param flag   true是修改金额，false是修改支付方式
	 */
	public void createOrgBillAdjustRecord(CwbOrder order, User user,BigDecimal modifyFeeReceiveFee, BigDecimal modifyPaybackfee) {
		
		List<FnOrgBillDetail> fnOrgBillDetails = new ArrayList<FnOrgBillDetail>();
		
		List<OrgBillAdjustmentRecord> adjustRecord = new ArrayList<OrgBillAdjustmentRecord>();
		
		OrgBillAdjustmentRecord record = new OrgBillAdjustmentRecord();
		//通过订单号查询出站点账单的记录
		fnOrgBillDetails = fnOrgBillDetailDAO.getFnOrgBillDetailByCwb(order.getCwb());
		//订单的类型
		Integer orderType = order.getCwbordertypeid();
		if(null!=fnOrgBillDetails&&fnOrgBillDetails.size()>0){//该订单已经生成过账单
			adjustRecord = orgBillAdjustmentRecordDao.getAdjustmentRecordByCwb(order.getCwb());
			DeliveryState deliveryState = deliverStateDao.getDeliverSignTime(order.getCwb());
			if(adjustRecord.size()<=0){//没有生成过调整单记录
				//根据不同的订单类型
				//配送
				record.setOrderNo(order.getCwb());
				record.setBillNo("");
				record.setBillId(0L);
				record.setAdjustBillNo("");
				record.setCustomerId(order.getCustomerid());
				record.setReceiveFee(order.getReceivablefee());
				record.setRefundFee(order.getPaybackfee());
				//是否修改过支付方式的标识
				record.setPayWayChangeFlag(PayMethodSwitchEnum.No.getValue());
				
				if(CwbOrderTypeIdEnum.Peisong.getValue()==orderType.intValue()){
					record.setModifyFee(modifyFeeReceiveFee);
					record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
					record.setRemark(order.getReceivablefee()+"元修改成"+modifyFeeReceiveFee+"元");
					
				}else if(CwbOrderTypeIdEnum.Shangmentui.getValue()==orderType.intValue()){
					//上门退
//					record.setReceiveFee(order.getReceivablefee());
//					record.setRefundFee(order.getPaybackfee());
					record.setModifyFee(modifyPaybackfee);
//					record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
					record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
					record.setRemark(order.getPaybackfee()+"元修改成"+modifyPaybackfee+"元");
					
				}else if(CwbOrderTypeIdEnum.Shangmenhuan.getValue()==orderType.intValue()){
					//判断
					//上门退
//					record.setReceiveFee(order.getReceivablefee());
//					record.setRefundFee(order.getPaybackfee());
					//修改的是应退金额
					if(modifyPaybackfee.doubleValue()>0&&modifyFeeReceiveFee.doubleValue()<=0){
						record.setModifyFee(modifyPaybackfee);
//						record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
						record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
						record.setRemark(order.getPaybackfee( )+"元修改成"+modifyPaybackfee+"元");
					}else if(modifyFeeReceiveFee.doubleValue()>0&&modifyPaybackfee.doubleValue()<=0){
						record.setModifyFee(modifyFeeReceiveFee);
						record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
						record.setRemark(order.getReceivablefee()+"元修改成"+modifyFeeReceiveFee+"元");
					}
					
				}
				record.setDeliverId(order.getDeliverid());
				record.setCreator(getSessionUser().getUsername());
				record.setCreateTime(new Date());
				record.setOrderType(orderType);
				record.setPayMethod(Long.valueOf(order.getPaywayid()).intValue());
				record.setDeliverybranchid(order.getDeliverybranchid());
				try {
					record.setSignTime(sdf.parse(deliveryState.getSign_time()));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				orgBillAdjustmentRecordDao.creAdjustmentRecord(record);
			}else{//该订单已经生成过调整单记录  不让其修改
				//提示信息
			}
		}else{
			//该订单还没有生成过账单记录，不能生成调整单记录
		}
	}
	/**
	 * 修改支付方式
	 * @param cwb
	 * @param paywayid
	 * @param newpaywayid
	 */
	public void createAdjustmentRecordByPayType(String cwb, int payWayId,int newPayWayId) {
		
		List<FnOrgBillDetail> fnOrgBillDetails = new ArrayList<FnOrgBillDetail>();
		
		List<OrgBillAdjustmentRecord> adjustRecord = new ArrayList<OrgBillAdjustmentRecord>();
		
		CwbOrder order = cwbDao.getCwbByCwb(cwb);
		
		//通过订单号查询出站点账单的记录
		fnOrgBillDetails = fnOrgBillDetailDAO.getFnOrgBillDetailByCwb(order.getCwb());
		
		if(null!=fnOrgBillDetails&&fnOrgBillDetails.size()>0){//该订单已经生成过账单
			adjustRecord = orgBillAdjustmentRecordDao.getAdjustmentRecordByCwb(order.getCwb());
			if(adjustRecord.size()<=0){//没有生成过调整单记录
				DeliveryState deliveryState = deliverStateDao.getDeliverSignTime(order.getCwb());
				//正向
				this.createAdjustRecordByModifyPayMethod(order,deliveryState,payWayId,newPayWayId,AdjustWayEnum.Forward);
				//逆向
				this.createAdjustRecordByModifyPayMethod(order,deliveryState,payWayId,newPayWayId,AdjustWayEnum.Reverse);
			}
		}
	}
	
	/**
	 * 
	 * @param order
	 * @param payWayId
	 * @param newPayWayId
	 * @param adjustWay  枚举
	 */
	private void createAdjustRecordByModifyPayMethod(CwbOrder order,DeliveryState deliveryState,int payWayId,int newPayWayId,AdjustWayEnum adjustWay){
		
		OrgBillAdjustmentRecord record = new OrgBillAdjustmentRecord();
		//订单的类型
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
		
		if(AdjustWayEnum.Forward.getValue().equals(adjustWay.getValue())){//正向--负的记录
			//是否修改过支付方式的标识
			record.setPayWayChangeFlag(PayMethodSwitchEnum.No.getValue());
			if(CwbOrderTypeIdEnum.Peisong.getValue()==orderType.intValue()){
				record.setModifyFee(modifyFeeReceiveFee);
				record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
				
			}else if(CwbOrderTypeIdEnum.Shangmentui.getValue()==orderType.intValue()){
				//上门退
				record.setModifyFee(modifyPaybackfee);
//				record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
				record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
				
			}else if(CwbOrderTypeIdEnum.Shangmenhuan.getValue()==orderType.intValue()){
				//上门退
				//修改的是应退金额
				if(modifyPaybackfee.doubleValue()>0&&modifyFeeReceiveFee.doubleValue()<=0){
					record.setModifyFee(modifyPaybackfee);
//					record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
					record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
					
				}else if(modifyFeeReceiveFee.doubleValue()>0&&modifyPaybackfee.doubleValue()<=0){
					
					record.setModifyFee(modifyFeeReceiveFee);
					record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
				}else{
					record.setModifyFee(modifyFeeReceiveFee);
					record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
				}
			}
			//原先的支付方式
			record.setPayMethod(Long.valueOf(payWayId).intValue());
		}else {//逆向 -- 正的记录
			//是否修改过支付方式的标识
			record.setPayWayChangeFlag(PayMethodSwitchEnum.Yes.getValue());
			if(CwbOrderTypeIdEnum.Peisong.getValue()==orderType.intValue()){
				record.setModifyFee(modifyFeeReceiveFee);
				record.setAdjustAmount(order.getReceivablefee().subtract(modifyFeeReceiveFee));
//				record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
				
			}else if(CwbOrderTypeIdEnum.Shangmentui.getValue()==orderType.intValue()){
				//上门退
				record.setModifyFee(modifyPaybackfee);
				record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
//				record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
			}else if(CwbOrderTypeIdEnum.Shangmenhuan.getValue()==orderType.intValue()){
				//上门退
				//修改的是应退金额
				if(modifyPaybackfee.doubleValue()>0&&modifyFeeReceiveFee.doubleValue()<=0){
					record.setModifyFee(modifyPaybackfee);
					record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
//					record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
				}else if(modifyFeeReceiveFee.doubleValue()>0&&modifyPaybackfee.doubleValue()<=0){
					record.setModifyFee(modifyFeeReceiveFee);
					record.setAdjustAmount(order.getReceivablefee().subtract(modifyFeeReceiveFee));
				}else {
					record.setModifyFee(modifyFeeReceiveFee);
					record.setAdjustAmount(order.getReceivablefee().subtract(modifyFeeReceiveFee));
				}
			}
			//新的支付方式
			record.setPayMethod(Long.valueOf(newPayWayId).intValue());
		}
		
		record.setRemark(PaytypeEnum.getByValue(payWayId).getText()+"修改成"+PaytypeEnum.getByValue(newPayWayId).getText());
		
		record.setDeliverId(order.getDeliverid());
		record.setCreator(getSessionUser().getUsername());
		record.setCreateTime(new Date());
		record.setOrderType(orderType);
		record.setDeliverybranchid(order.getDeliverybranchid());
		try {
			record.setSignTime(sdf.parse(deliveryState.getSign_time()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		orgBillAdjustmentRecordDao.creAdjustmentRecord(record);
	}
	
	
	public void test(){
		/*DeliveryState deliveryState = deliverStateDao.getDeliverSignTime(order.getCwb());
		OrgBillAdjustmentRecord record = new OrgBillAdjustmentRecord();
		//订单的类型
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
		
		if(CwbOrderTypeIdEnum.Peisong.getValue()==orderType.intValue()){
			record.setModifyFee(modifyFeeReceiveFee);
			record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
		}else if(CwbOrderTypeIdEnum.Shangmentui.getValue()==orderType.intValue()){
			//上门退
			record.setModifyFee(modifyPaybackfee);
//			record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
			record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
			
		}else if(CwbOrderTypeIdEnum.Shangmenhuan.getValue()==orderType.intValue()){
			//上门退
			//修改的是应退金额
			if(modifyPaybackfee.doubleValue()>0&&modifyFeeReceiveFee.doubleValue()<=0){
				record.setModifyFee(modifyPaybackfee);
//				record.setAdjustAmount(modifyPaybackfee.subtract(order.getPaybackfee()));
				record.setAdjustAmount(order.getPaybackfee().subtract(modifyPaybackfee));
			}else if(modifyFeeReceiveFee.doubleValue()>0&&modifyPaybackfee.doubleValue()<=0){
				record.setModifyFee(modifyFeeReceiveFee);
				record.setAdjustAmount(modifyFeeReceiveFee.subtract(order.getReceivablefee()));
			}
			
		}
		
		record.setRemark(PaytypeEnum.getByValue(payWayId).getText()+"修改成"+PaytypeEnum.getByValue(newPayWayId).getText());
		
		record.setDeliverId(order.getDeliverid());
		record.setCreator(getSessionUser().getUsername());
		record.setCreateTime(new Date());
		record.setOrderType(orderType);
		record.setPayMethod(Long.valueOf(newPayWayId).intValue());
		record.setDeliverybranchid(order.getDeliverybranchid());
		try {
			record.setSignTime(sdf.parse(deliveryState.getSign_time()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		orgBillAdjustmentRecordDao.creAdjustmentRecord(record);
	*/
	}
}
