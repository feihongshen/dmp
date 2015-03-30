package cn.explink.service;

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
import cn.explink.domain.FnOrgBillDetail;
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
	 * 根据cwb值,判断是否存在和是否已经核销,来创建调整单记录
	 *
	 */
	public void createAdjustmentRecode(String cwb,long customerid,BigDecimal receive_fee,BigDecimal refund_fee,BigDecimal modify_fee,String remark,String creator,int cwbordertypeid) {
		
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
			BigDecimal payBackFee=cwbOrder.getPaybackfee();
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
							//未核销,并且存在调整单信息,则需要修改该信息
							//上门退订单
							if(cwbOrderTypeId==CwbOrderTypeIdEnum.Shangmentui.getValue()){
								aRecord.setReceive_fee(BigDecimal.ZERO);//修改调整单记录,没有原始应收金额
								aRecord.setRefund_fee(payBackFee);
								aRecord.setModify_fee(refund_fee);
								aRecord.setAdjust_amount(refund_fee.subtract(payBackFee));//通过原始金额减去调整后金额产生调整差额
								aRecord.setRemark(payBackFee+"元修改成"+refund_fee+"元");
								aRecord.setCreator(getSessionUser().getUsername());
								aRecord.setCreate_time(DateTimeUtil.getNowTime());
								adjustmentRecordDAO.updateAdjustmentRecord(aRecord, adjustmentRecord.getId());
							}else if(cwbOrderTypeId==CwbOrderTypeIdEnum.Peisong.getValue()){
								//配送订单
								//修改调整单记录
								aRecord.setReceive_fee(receive_fee);//修改调整单记录,原始金额为原有数据中的原始金额
								aRecord.setRefund_fee(refund_fee);
								aRecord.setModify_fee(modify_fee);
								aRecord.setAdjust_amount(modify_fee.subtract(receive_fee));//通过原始金额减去调整后金额产生调整差额
								aRecord.setRemark(receive_fee+"元修改成"+modify_fee+"元");
								aRecord.setCreator(getSessionUser().getUsername());
								aRecord.setCreate_time(DateTimeUtil.getNowTime());
								adjustmentRecordDAO.updateAdjustmentRecord(aRecord, adjustmentRecord.getId());
							}	
						}else if(adjust_bill_id>0&&adjust_status==VerificationEnum.YiHeXiao.getValue()){
							//表示存在该订单信息,并且已经核销,需要生成新的调整单信息
							if(cwbOrderTypeId==CwbOrderTypeIdEnum.Shangmentui.getValue()){
								//上门退订单
								aRecord.setOrder_no(cwb);
								aRecord.setBill_no(adjustmentRecord.getBill_no());//不允许为空
								aRecord.setCustomer_id(customerid);
								aRecord.setReceive_fee(receive_fee);
								aRecord.setRefund_fee(payBackFee);
								aRecord.setModify_fee(refund_fee);
								aRecord.setAdjust_amount(refund_fee.subtract(payBackFee));//通过原始金额减去调整后金额产生调整差额
								aRecord.setRemark(payBackFee+"元修改成"+refund_fee+"元");
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
								aRecord.setReceive_fee(receive_fee);
								aRecord.setRefund_fee(refund_fee);
								aRecord.setModify_fee(modify_fee);
								aRecord.setAdjust_amount(modify_fee.subtract(receive_fee));//通过原始金额减去调整后金额产生调整差额
								aRecord.setRemark(receive_fee+"元修改成"+modify_fee+"元");
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
						aRecord.setReceive_fee(receive_fee);
						aRecord.setRefund_fee(payBackFee);
						aRecord.setModify_fee(refund_fee);
						aRecord.setAdjust_amount(refund_fee.subtract(payBackFee));//通过原始金额减去调整后金额产生调整差额
						aRecord.setRemark(payBackFee+"元修改成"+refund_fee+"元");
						aRecord.setCreator(getSessionUser().getUsername());
						aRecord.setCreate_time(DateTimeUtil.getNowTime());
						aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
						aRecord.setOrder_type(cwbordertypeid);
						adjustmentRecordDAO.creAdjustmentRecord(aRecord);
					}else if(cwbOrderTypeId==CwbOrderTypeIdEnum.Peisong.getValue()){
						//没有生成过调整单新加一个记录
						aRecord.setOrder_no(cwb);
						aRecord.setBill_no("");//不允许为空
						aRecord.setAdjust_bill_no("");
						aRecord.setCustomer_id(customerid);
						aRecord.setReceive_fee(receive_fee);
						aRecord.setRefund_fee(refund_fee);
						aRecord.setModify_fee(modify_fee);
						aRecord.setAdjust_amount(modify_fee.subtract(receive_fee));//通过原始金额减去调整后金额产生调整差额
						aRecord.setRemark(receive_fee+"元修改成"+modify_fee+"元");
						aRecord.setCreator(getSessionUser().getUsername());
						aRecord.setCreate_time(DateTimeUtil.getNowTime());
						aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
						aRecord.setOrder_type(cwbordertypeid);
						adjustmentRecordDAO.creAdjustmentRecord(aRecord);
					}
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
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
						//已核销过,重新生成一个新的调整单记录
						aRecord.setOrder_no(cwb);
						aRecord.setBill_no("");//不允许为空
						aRecord.setAdjust_bill_no("");
						aRecord.setCustomer_id(cwbOrder.getCustomerid());
						aRecord.setReceive_fee(cwbOrder.getReceivablefee());
						aRecord.setRefund_fee(cwbOrder.getPaybackfee());
						aRecord.setModify_fee(BigDecimal.ZERO);
						aRecord.setAdjust_amount(BigDecimal.ZERO);//通过原始金额减去调整后金额产生调整差额
						aRecord.setRemark(PaytypeEnum.getByValue(payWayId).getText()+"修改成"+PaytypeEnum.getByValue(newPayWayId).getText());
						aRecord.setCreator(getSessionUser().getUsername());
						aRecord.setCreate_time(DateTimeUtil.getNowTime());
						aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
						aRecord.setOrder_type(cwbOrder.getCwbordertypeid());
						adjustmentRecordDAO.creAdjustmentRecord(aRecord);
					}
				}
			}else{
				//没有生成过调整单新加一个记录
				aRecord.setOrder_no(cwb);
				aRecord.setBill_no("");//不允许为空
				aRecord.setAdjust_bill_no("");
				aRecord.setCustomer_id(cwbOrder.getCustomerid());
				aRecord.setReceive_fee(cwbOrder.getReceivablefee());
				aRecord.setRefund_fee(cwbOrder.getPaybackfee());
				aRecord.setModify_fee(BigDecimal.ZERO);
				aRecord.setAdjust_amount(BigDecimal.ZERO);//通过原始金额减去调整后金额产生调整差额
				aRecord.setRemark(PaytypeEnum.getByValue(payWayId).getText()+"修改成"+PaytypeEnum.getByValue(newPayWayId).getText());
				aRecord.setCreator(getSessionUser().getUsername());
				aRecord.setCreate_time(DateTimeUtil.getNowTime());
				aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
				aRecord.setOrder_type(cwbOrder.getCwbordertypeid());
				adjustmentRecordDAO.creAdjustmentRecord(aRecord);
			}
		}
	}
}
