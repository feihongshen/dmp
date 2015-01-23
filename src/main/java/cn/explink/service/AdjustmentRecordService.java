package cn.explink.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.AdjustmentRecordDAO;
import cn.explink.dao.FnOrgBillDetailDAO;
import cn.explink.domain.AdjustmentRecord;
import cn.explink.domain.FnOrgBillDetail;
import cn.explink.domain.User;
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
			//只知道订单号
			List<FnOrgBillDetail> fnOrgBillDetails=new ArrayList<FnOrgBillDetail>();
			List<AdjustmentRecord> adjustmentRecords=new ArrayList<AdjustmentRecord>();
			//创建一个新的调整单记录对象,用于保存调整单记录
			AdjustmentRecord aRecord=new AdjustmentRecord();
			//查询出对应订单号的账单详细信息
			fnOrgBillDetails=fnOrgBillDetailDAO.getFnOrgBillDetailByCwb(cwb);
			
			if(fnOrgBillDetails.size()>0){
				//表示有对应订单信息,下面判断是否生成过调整单,查询调整单表 fn_adjustment_record表
				adjustmentRecords=adjustmentRecordDAO.getAdjustmentRecordByCwb(cwb);
				if(adjustmentRecords.size()>0){
					//表示表中存在该订单的调整单记录,查询是否存在调整账单号
					for (AdjustmentRecord adjustmentRecord : adjustmentRecords) {
						//定义一个字符串,避免空指针
						String adjustBillNoString="";
						adjustBillNoString= adjustmentRecord.getAdjust_bill_no();
						if(adjustmentRecord!=null&&adjustBillNoString!=null&&adjustBillNoString.equals("")){
							//修改调整单记录
							
							aRecord.setReceive_fee(receive_fee);//修改调整单记录,原始金额为原有数据中的原始金额
							aRecord.setRefund_fee(refund_fee);
							aRecord.setModify_fee(modify_fee);
							aRecord.setAdjust_amount(receive_fee.subtract(modify_fee));//通过原始金额减去调整后金额产生调整差额
							aRecord.setRemark(receive_fee+"元修改成"+modify_fee+"元");
							aRecord.setCreator(getSessionUser().getUsername());
							aRecord.setCreate_time(DateTimeUtil.getNowTime());
							adjustmentRecordDAO.updateAdjustmentRecord(aRecord, adjustmentRecord.getId());
							
							
						}else{
							if(adjustBillNoString==null){
								continue;
							}else {
								//表示没有生成账单,生成新的调整单记录,调整金额为上次调整后的金额,上次调整后金额为传入的金额
								aRecord.setOrder_no(cwb);
								aRecord.setBill_no(adjustmentRecord.getBill_no());//不允许为空
								aRecord.setCustomer_id(customerid);
								aRecord.setReceive_fee(receive_fee);
								aRecord.setRefund_fee(refund_fee);
								aRecord.setModify_fee(modify_fee);
								aRecord.setAdjust_amount(receive_fee.subtract(modify_fee));//通过原始金额减去调整后金额产生调整差额
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
					//没有生成过调整单新加一个记录
					aRecord.setOrder_no(cwb);
					aRecord.setBill_no("");//不允许为空
					aRecord.setAdjust_bill_no("");
					aRecord.setCustomer_id(customerid);
					aRecord.setReceive_fee(receive_fee);
					aRecord.setRefund_fee(refund_fee);
					aRecord.setModify_fee(modify_fee);
					aRecord.setAdjust_amount(receive_fee.subtract(modify_fee));//通过原始金额减去调整后金额产生调整差额
					aRecord.setRemark(receive_fee+"元修改成"+modify_fee+"元");
					aRecord.setCreator(getSessionUser().getUsername());
					aRecord.setCreate_time(DateTimeUtil.getNowTime());
					aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
					aRecord.setOrder_type(cwbordertypeid);
					adjustmentRecordDAO.creAdjustmentRecord(aRecord);
				}	
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			/*adjustmentRecords=adjustmentRecordDAO.getAdjustmentRecordByCwb(cwb);		
			if(adjustmentRecords.size()>0){
				//表示调整单表中存在有关订单的记录,需要判断他是否是核销状态
				//将原始金额查询出来保存,在后面计算中使用
				BigDecimal oldReceive_fee=BigDecimal.ZERO;
				for (AdjustmentRecord adjustmentRecord : adjustmentRecords) {
					oldReceive_fee=adjustmentRecord.getReceive_fee();
					if(adjustmentRecord.getStatus()==VerificationEnum.WeiHeXiao.getValue()){
						adjustmentRecordDAO.delAdjustmentRecord(cwb);
					}
				}
				AdjustmentRecord aRecord=new AdjustmentRecord();
				aRecord.setOrder_no(cwb);
				aRecord.setBill_no("");//不允许为空
				aRecord.setCustomer_id(customerid);
				aRecord.setReceive_fee(oldReceive_fee);
				aRecord.setRefund_fee(refund_fee);
				aRecord.setModify_fee(modify_fee);
				aRecord.setAdjust_amount(oldReceive_fee.subtract(modify_fee));//通过原始金额减去调整后金额产生调整差额
				aRecord.setRemark(remark);
				aRecord.setCreator(creator);
				aRecord.setCreate_time(DateTimeUtil.getNowTime());
				aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
				aRecord.setOrder_type(cwbordertypeid);
				adjustmentRecordDAO.creAdjustmentRecord(aRecord);
			}else {
				AdjustmentRecord aRecord=new AdjustmentRecord();
				aRecord.setOrder_no(cwb);
				aRecord.setBill_no("");//不允许为空
				aRecord.setCustomer_id(customerid);
				aRecord.setReceive_fee(receive_fee);
				aRecord.setRefund_fee(refund_fee);
				aRecord.setModify_fee(modify_fee);
				aRecord.setAdjust_amount(receive_fee.subtract(modify_fee));//通过原始金额减去调整后金额产生调整差额
				aRecord.setRemark(remark);
				aRecord.setCreator(creator);
				aRecord.setCreate_time(DateTimeUtil.getNowTime());
				aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
				aRecord.setOrder_type(cwbordertypeid);
				adjustmentRecordDAO.creAdjustmentRecord(aRecord);
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
