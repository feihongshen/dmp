package main.java.cn.explink.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.AdjustmentRecordDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.FnCustomerBillDetailDAO;
import cn.explink.dao.FnOrgBillDetailDAO;
import cn.explink.domain.AdjustmentRecord;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.FnCustomerBillDetail;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.VerificationEnum;
import cn.explink.util.DateTimeUtil;
/**
 * 调整单服务类
 * @author 
 *
 */
@Service
public class AdjustmentRecordService {
	@Autowired
	AdjustmentRecordDAO adjustmentRecordDAO;
	@Autowired
	FnOrgBillDetailDAO fnOrgBillDetailDAO;
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	FnCustomerBillDetailDAO FnCustomerBillDetaildao;
	
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * sunyun
	 * 根据cwb值,判断是否存在和是否已经核销,来创建调整单记录
	 *
	 */
	public void createAdjustmentRecode(String cwb,long customerid,BigDecimal order_receive_fee,BigDecimal page_payback_fee,BigDecimal page_receive_fee,String remark,String creator,int cwbordertypeid) {
		
		try {
			//cwbDao.getcwbbycwb
			//只知道订单号
			List<FnCustomerBillDetail> fnCustomerBillDetails=new ArrayList<FnCustomerBillDetail>();
			List<AdjustmentRecord> adjustmentRecords=new ArrayList<AdjustmentRecord>();
			//创建一个新的调整单记录对象,用于保存调整单记录
			AdjustmentRecord aRecord=new AdjustmentRecord();
			//查询出对应订单号的账单详细信息
			fnCustomerBillDetails=FnCustomerBillDetaildao.getFnCustomerBillDetailByCwb(cwb);
			//查询出订单类型状态用于判断订单类型是配送订单还是上门退订单
			CwbOrder cwbOrder=cwbDao.getCwbByCwb(cwb);
			int cwbOrderTypeId=cwbOrder.getCwbordertypeid();
			//获取原始应退金额
			BigDecimal orderPayBackFee=cwbOrder.getPaybackfee();
			//
			if(fnCustomerBillDetails!=null&&fnCustomerBillDetails.size()>0){
				//表示有对应订单信息,下面判断是否生成过调整单,查询调整单表 fn_adjustment_record表
				adjustmentRecords=adjustmentRecordDAO.getAdjustmentRecordByCwb(cwb);
				if(adjustmentRecords.size()>0){
					//表示表中存在该订单的调整单记录,查询是否存在调整账单号
					for (AdjustmentRecord adjustmentRecord : adjustmentRecords) {
						long adjust_bill_id=adjustmentRecord.getBill_id();
						int adjust_status=adjustmentRecord.getStatus();
						if(adjust_bill_id>0&&adjust_status==VerificationEnum.WeiHeXiao.getValue()){
							//未核销,并且存在调整账单信息,则需要修改该信息
							//上门退订单
							if(cwbOrderTypeId==CwbOrderTypeIdEnum.Shangmentui.getValue()){
								aRecord.setReceive_fee(BigDecimal.ZERO);//修改调整单记录,没有原始应收金额
								aRecord.setRefund_fee(orderPayBackFee);
								aRecord.setModify_fee(page_payback_fee);
//								aRecord.setAdjust_amount(refund_fee.subtract(payBackFee));//通过原始金额减去调整后金额产生调整差额
								aRecord.setAdjust_amount(orderPayBackFee.subtract(page_payback_fee));
								aRecord.setRemark(orderPayBackFee+"元修改成"+page_payback_fee+"元");
								aRecord.setCreator(getSessionUser().getUsername());
								aRecord.setCreate_time(DateTimeUtil.getNowTime());
//								adjustmentRecordDAO.updateAdjustmentRecord(aRecord, adjustmentRecord.getId());
								
							}else if(cwbOrderTypeId==CwbOrderTypeIdEnum.Peisong.getValue()){
								//配送订单
								//修改调整单记录
								aRecord.setReceive_fee(order_receive_fee);//修改调整单记录,原始金额为原有数据中的原始金额
								aRecord.setRefund_fee(page_payback_fee);
								aRecord.setModify_fee(page_receive_fee);
								aRecord.setAdjust_amount(page_receive_fee.subtract(order_receive_fee));//通过原始金额减去调整后金额产生调整差额
								aRecord.setRemark(order_receive_fee+"元修改成"+page_receive_fee+"元");
								aRecord.setCreator(getSessionUser().getUsername());
								aRecord.setCreate_time(DateTimeUtil.getNowTime());
							}
							
							adjustmentRecordDAO.updateAdjustmentRecord(aRecord, adjustmentRecord.getId());
						}else if(adjust_bill_id>0&&adjust_status==VerificationEnum.YiHeXiao.getValue()){
							//表示存在该订单信息,并且已经核销,需要生成新的调整单信息
							if(cwbOrderTypeId==CwbOrderTypeIdEnum.Shangmentui.getValue()){
								//上门退订单
								aRecord.setOrder_no(cwb);
								aRecord.setBill_no(adjustmentRecord.getBill_no());//不允许为空
								aRecord.setCustomer_id(customerid);
								aRecord.setReceive_fee(order_receive_fee);
								aRecord.setRefund_fee(orderPayBackFee);
								aRecord.setModify_fee(page_payback_fee);
//								aRecord.setAdjust_amount(refund_fee.subtract(payBackFee));//通过原始金额减去调整后金额产生调整差额
								aRecord.setAdjust_amount(orderPayBackFee.subtract(page_payback_fee));
								aRecord.setRemark(orderPayBackFee+"元修改成"+page_payback_fee+"元");
								aRecord.setCreator(getSessionUser().getUsername());
								aRecord.setCreate_time(DateTimeUtil.getNowTime());
								aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
								aRecord.setOrder_type(cwbordertypeid);
								adjustmentRecordDAO.creAdjustmentRecord(aRecord);
							}else if(cwbOrderTypeId==CwbOrderTypeIdEnum.Peisong.getValue()){
								//配送订单
								aRecord.setOrder_no(cwb);
								aRecord.setBill_no(adjustmentRecord.getBill_no());//不允许为空
								aRecord.setCustomer_id(customerid);
								aRecord.setReceive_fee(order_receive_fee);
								aRecord.setRefund_fee(page_payback_fee);
								aRecord.setModify_fee(page_receive_fee);
								aRecord.setAdjust_amount(page_receive_fee.subtract(order_receive_fee));//通过原始金额减去调整后金额产生调整差额
								aRecord.setRemark(order_receive_fee+"元修改成"+page_receive_fee+"元");
								aRecord.setCreator(getSessionUser().getUsername());
								aRecord.setCreate_time(DateTimeUtil.getNowTime());
								aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
								aRecord.setOrder_type(cwbordertypeid);
								adjustmentRecordDAO.creAdjustmentRecord(aRecord);
							}
						}
					}	
				}else{
					if(cwbOrderTypeId==CwbOrderTypeIdEnum.Shangmentui.getValue()){
						//没有生成过调整单新加一个记录
						aRecord.setOrder_no(cwb);
						aRecord.setBill_no("");//不允许为空
						aRecord.setAdjust_bill_no("");
						aRecord.setCustomer_id(customerid);
						aRecord.setReceive_fee(order_receive_fee);
						aRecord.setRefund_fee(orderPayBackFee);
						aRecord.setModify_fee(page_payback_fee);
						aRecord.setAdjust_amount(page_payback_fee.subtract(orderPayBackFee));//通过原始金额减去调整后金额产生调整差额
						aRecord.setRemark(orderPayBackFee+"元修改成"+page_payback_fee+"元");
						aRecord.setCreator(getSessionUser().getUsername());
						aRecord.setCreate_time(DateTimeUtil.getNowTime());
						aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
						aRecord.setOrder_type(cwbordertypeid);
//									adjustmentRecordDAO.creAdjustmentRecord(aRecord);
					}else if(cwbOrderTypeId==CwbOrderTypeIdEnum.Peisong.getValue()){
						//没有生成过调整单新加一个记录
						aRecord.setOrder_no(cwb);
						aRecord.setBill_no("");//不允许为空
						aRecord.setAdjust_bill_no("");
						aRecord.setCustomer_id(customerid);
						aRecord.setReceive_fee(order_receive_fee);
						aRecord.setRefund_fee(page_payback_fee);
						aRecord.setModify_fee(page_receive_fee);
						aRecord.setAdjust_amount(page_receive_fee.subtract(order_receive_fee));//通过原始金额减去调整后金额产生调整差额
						aRecord.setRemark(order_receive_fee+"元修改成"+page_receive_fee+"元");
						aRecord.setCreator(getSessionUser().getUsername());
						aRecord.setCreate_time(DateTimeUtil.getNowTime());
						aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
						aRecord.setOrder_type(cwbordertypeid);
					}
					adjustmentRecordDAO.creAdjustmentRecord(aRecord);
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	//===================================================Refactored by jiangyu 2015-04-03================================================================================
	
	/**
	 * 创建调整单的记录  --added by jiangyu
	 * @param cwbOrder
	 * @param page_payback_fee  页面修改金额后的应退金额
	 * @param page_receive_fee  页面修改金额后的应收金额
	 * @param remark
	 * @param username
	 */
	public void processAdjusRecordByMoney(CwbOrder cwbOrder,BigDecimal page_payback_fee, BigDecimal page_receive_fee, String remark,String username) {

		try {
			List<FnCustomerBillDetail> fnCustomerBillDetails=new ArrayList<FnCustomerBillDetail>();
			
			List<AdjustmentRecord> adjustmentRecords=new ArrayList<AdjustmentRecord>();
			
			//创建一个新的调整单记录对象,用于保存调整单记录
			AdjustmentRecord aRecord=new AdjustmentRecord();
			
			//查询出对应订单号的账单详细信息
			fnCustomerBillDetails=FnCustomerBillDetaildao.getFnCustomerBillDetailByCwb(cwbOrder.getCwb());
			
			//查询出订单类型状态用于判断订单类型是配送订单还是上门退订单
			int cwbOrderTypeId=cwbOrder.getCwbordertypeid();
			
			if(fnCustomerBillDetails!=null&&fnCustomerBillDetails.size()>0){
				//表示有对应订单信息,下面判断是否生成过调整单,查询调整单表 fn_adjustment_record表
				adjustmentRecords=adjustmentRecordDAO.getAdjustmentRecordByCwb(cwbOrder.getCwb());
				
				if(adjustmentRecords.size()>0){
					//表示表中存在该订单的调整单记录,查询是否存在调整账单号
					for (AdjustmentRecord adjustmentRecord : adjustmentRecords) {
						
						long adjust_bill_id=adjustmentRecord.getBill_id();
						
						int adjust_status=adjustmentRecord.getStatus();
						
						if(adjust_bill_id>0&&adjust_status==VerificationEnum.WeiHeXiao.getValue()){
							//未核销,并且存在调整账单信息,则需要修改该信息
							//上门退订单
							if(cwbOrderTypeId==CwbOrderTypeIdEnum.Shangmentui.getValue()){
//								aRecord.setReceive_fee(BigDecimal.ZERO);//修改调整单记录,没有原始应收金额
//								aRecord.setRefund_fee(cwbOrder.getPaybackfee());//应退金额
								aRecord.setModify_fee(page_payback_fee);
//								aRecord.setAdjust_amount(cwbOrder.getPaybackfee().subtract(page_payback_fee));
								aRecord.setAdjust_amount(aRecord.getRefund_fee().subtract(page_payback_fee));
								aRecord.setRemark(aRecord.getRefund_fee()+"元修改成"+page_payback_fee+"元");
							}else if(cwbOrderTypeId==CwbOrderTypeIdEnum.Peisong.getValue()){
								//配送订单
//								aRecord.setReceive_fee(cwbOrder.getReceivablefee());//修改调整单记录,原始金额为原有数据中的原始金额
//								aRecord.setRefund_fee(page_payback_fee);
								aRecord.setModify_fee(page_receive_fee);
//								aRecord.setAdjust_amount(page_receive_fee.subtract(cwbOrder.getReceivablefee()));//通过原始金额减去调整后金额产生调整差额
								aRecord.setAdjust_amount(page_receive_fee.subtract(aRecord.getReceive_fee()));//通过原始订单的金额减去调整后金额产生调整差额
								aRecord.setRemark(aRecord.getReceive_fee()+"元修改成"+page_receive_fee+"元");
							}
							aRecord.setCreator(getSessionUser().getUsername());
							aRecord.setCreate_time(DateTimeUtil.getNowTime());
							
							adjustmentRecordDAO.updateAdjustmentRecord(aRecord, adjustmentRecord.getId());
							
						}else if(adjust_bill_id>0&&adjust_status==VerificationEnum.YiHeXiao.getValue()){
							//表示存在该订单信息,并且已经核销,需要生成新的调整单信息
							this.createNewAdjustRecordByMoney(cwbOrder, page_payback_fee, page_receive_fee, aRecord);
							
						}else if(adjust_bill_id==0&&adjust_status==VerificationEnum.WeiHeXiao.getValue()){
							//没有生成过调整账单&&是未核销的状态  则创建新的记录
							this.createNewAdjustRecordByMoney(cwbOrder, page_payback_fee, page_receive_fee, aRecord);
						}
					}	
				}else{
					//创建新的记录
					this.createNewAdjustRecordByMoney(cwbOrder, page_payback_fee, page_receive_fee, aRecord);
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	/**
	 * 修改支付方式生成调整单的记录 
	 * @param cwb
	 * @param payWayId
	 * @param newPayWayId
	 */
	public void createAdjustmentRecordByPayType(String cwb,int payWayId,int newPayWayId){
		//只知道订单号
		List<FnCustomerBillDetail> fnCustomerBillDetails=new ArrayList<FnCustomerBillDetail>();
		List<AdjustmentRecord> adjustmentRecords=new ArrayList<AdjustmentRecord>();
		//创建一个新的调整单记录对象,用于保存调整单记录
		AdjustmentRecord aRecord=new AdjustmentRecord();
		//查询出对应订单号的账单详细信息
		CwbOrder cwbOrder=cwbDao.getCwbByCwb(cwb);
		fnCustomerBillDetails=FnCustomerBillDetaildao.getFnCustomerBillDetailByCwb(cwb);
		if(fnCustomerBillDetails!=null&&fnCustomerBillDetails.size()>0){
			//表示存在该订单的账单信息
			//表示有对应订单信息,下面判断是否生成过调整单,查询调整单表 fn_adjustment_record表
			adjustmentRecords=adjustmentRecordDAO.getAdjustmentRecordByCwb(cwb);
			if(adjustmentRecords.size()>0){
				//存在订单的调整单信息,
				for (AdjustmentRecord adjustmentRecord : adjustmentRecords) {
					long adjust_bill_id=adjustmentRecord.getBill_id();
					int adjust_status=adjustmentRecord.getStatus();
					if(adjust_bill_id>0&&adjust_status==VerificationEnum.WeiHeXiao.getValue()){
						//需要修改订单信息
						aRecord.setReceive_fee(cwbOrder.getReceivablefee());
						aRecord.setRefund_fee(cwbOrder.getPaybackfee());
						aRecord.setModify_fee(BigDecimal.ZERO);
						aRecord.setAdjust_amount(BigDecimal.ZERO);
						aRecord.setRemark(PaytypeEnum.getByValue(payWayId).getText()+"修改成"+PaytypeEnum.getByValue(newPayWayId).getText());
						aRecord.setCreator(getSessionUser().getUsername());
						aRecord.setCreate_time(DateTimeUtil.getNowTime());
						adjustmentRecordDAO.updateAdjustmentRecord(aRecord, adjustmentRecord.getId());
					}else if(adjust_bill_id>0&&adjust_status==VerificationEnum.YiHeXiao.getValue()){
						//已核销过,重新生成一个新的调整单记录  modified by jiangyu
						this.modifyPayWay2CreateRecord(payWayId, newPayWayId,aRecord, cwbOrder);
					}
				}
			}else{
				//创建新的记录   modified by jiangyu
				this.modifyPayWay2CreateRecord(payWayId, newPayWayId, aRecord,cwbOrder);
			}
		}
	}
	
	/**
	 * 修改支付方式创建新的记录 --modified by jiangyu
	 * @param payWayId
	 * @param newPayWayId
	 * @param aRecord
	 * @param cwbOrder
	 */
	private void modifyPayWay2CreateRecord(int payWayId, int newPayWayId,AdjustmentRecord aRecord, CwbOrder cwbOrder) {
		aRecord.setOrder_no(cwbOrder.getCwb());
		//不允许为空
		aRecord.setBill_no("");
		aRecord.setAdjust_bill_no("");
		aRecord.setCustomer_id(cwbOrder.getCustomerid());
		
		aRecord.setReceive_fee(cwbOrder.getReceivablefee());
		aRecord.setRefund_fee(cwbOrder.getPaybackfee());
		aRecord.setModify_fee(BigDecimal.ZERO);
		aRecord.setAdjust_amount(BigDecimal.ZERO);
		aRecord.setRemark(PaytypeEnum.getByValue(payWayId).getText()+"修改成"+PaytypeEnum.getByValue(newPayWayId).getText());
		
		aRecord.setCreator(getSessionUser().getUsername());
		aRecord.setCreate_time(DateTimeUtil.getNowTime());
		aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
		aRecord.setOrder_type(cwbOrder.getCwbordertypeid());
		
		adjustmentRecordDAO.creAdjustmentRecord(aRecord);
	}
	
	
	/**
	 * 创建一个新的记录  --added by jiangyu
	 * @param order
	 * @param page_payback_fee  页面返回修改后的应退金额
	 * @param page_receive_fee  页面返回修改后的应收金额
	 * @param aRecord
	 */
	private void createNewAdjustRecordByMoney(CwbOrder order, BigDecimal page_payback_fee,BigDecimal page_receive_fee,AdjustmentRecord aRecord) {
		//订单类型
		int orderType = order.getCwbordertypeid();
		aRecord.setOrder_no(order.getCwb());
		//不允许为空
		aRecord.setBill_no("");
		aRecord.setAdjust_bill_no("");
		aRecord.setCustomer_id(order.getCustomerid());
		
		aRecord.setReceive_fee(order.getReceivablefee());
		aRecord.setRefund_fee(order.getPaybackfee());
		
		if(orderType==CwbOrderTypeIdEnum.Shangmentui.getValue()){
			//没有生成过调整单新加一个记录
			aRecord.setModify_fee(page_payback_fee);
			aRecord.setAdjust_amount(order.getPaybackfee().subtract(page_payback_fee));
			aRecord.setRemark(order.getPaybackfee()+"元修改成"+page_payback_fee+"元");
		}else if(orderType==CwbOrderTypeIdEnum.Peisong.getValue()){
			//没有生成过调整单新加一个记录
//			aRecord.setRefund_fee(page_payback_fee);
			aRecord.setModify_fee(page_receive_fee);
			aRecord.setAdjust_amount(page_receive_fee.subtract(order.getReceivablefee()));//通过原始金额减去调整后金额产生调整差额
			aRecord.setRemark(order.getReceivablefee()+"元修改成"+page_receive_fee+"元");
			
		}
		
		aRecord.setCreator(getSessionUser().getUsername());
		aRecord.setCreate_time(DateTimeUtil.getNowTime());
		aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
		aRecord.setOrder_type(orderType);
		
		adjustmentRecordDAO.creAdjustmentRecord(aRecord);
	}
}
