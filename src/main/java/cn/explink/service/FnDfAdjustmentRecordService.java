package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.FnDfAdjustmentRecordDAO;
import cn.explink.dao.FnDfBillDetailDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.FnDfAdjustmentRecord;
import cn.explink.domain.FnDfBillDetail;
import cn.explink.domain.User;
import cn.explink.enumutil.FnDfAdjustStateEnum;
import cn.explink.util.DateTimeUtil;
/** 
 *  派费调整记录
 */
@Service
public class FnDfAdjustmentRecordService {
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	FnDfAdjustmentRecordDAO fnDfAdjustmentRecordDAO;
	@Autowired
	FnDfBillDetailDAO fnDfBillDetailDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 *
	 * 再次归班审核为“配送成功”、“上门退成功”、“上门换成功”、“揽收成功”时，重新获取原计费金额生成调整记录，调整金额=原派费金额
	 */
	public void createAdjustment4GoToClassConfirm(CwbOrder order){
		List<FnDfBillDetail> billDetails= fnDfBillDetailDAO.queryFnDfBillDetailByCwb(order.getCwb());
		if (billDetails != null && !billDetails.isEmpty()) {//已经生成过派费账单
			FnDfBillDetail detail = billDetails.get(0);
			FnDfAdjustmentRecord aRecord = new FnDfAdjustmentRecord();
			aRecord.setOrder_no(order.getCwb());
			aRecord.setBill_no("");
			aRecord.setAdjust_bill_no("");
			aRecord.setOrg_id(order.getDeliverybranchid());
			aRecord.setCustomer_id(order.getCustomerid());
			aRecord.setAdjust_amount(detail.getDeliveryAmount());//派费金额 为正数
			aRecord.setRemark("再次归班审核生成调整记录");
			aRecord.setCreator(getSessionUser().getUsername());
			aRecord.setCreate_time(DateTimeUtil.getNowTime());
			aRecord.setAdj_state(FnDfAdjustStateEnum.WeiShengCheng.getValue());
			aRecord.setOrderType(order.getCwbordertypeid());
			fnDfAdjustmentRecordDAO.creAdjustmentRecord(aRecord);
		}
	}
	
	/**
	 * 重置反馈状态，对应生成调整单记录
	 */
	public void createAdjustment4ReFeedBack(String cwb){
		List<FnDfBillDetail> billDetails= fnDfBillDetailDAO.queryFnDfBillDetailByCwb(cwb);
		if (billDetails != null && !billDetails.isEmpty()) {//已经生成过派费账单
			CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
			FnDfBillDetail detail = billDetails.get(0);
			FnDfAdjustmentRecord aRecord = new FnDfAdjustmentRecord();
			aRecord.setOrder_no(cwb);
			aRecord.setBill_no("");
			aRecord.setAdjust_bill_no("");
			aRecord.setCustomer_id(order.getCustomerid());
			aRecord.setOrg_id(order.getDeliverybranchid());
			aRecord.setAdjust_amount(detail.getDeliveryAmount().negate());//派费金额 为负数
			aRecord.setRemark("重置反馈生成调整记录");
			aRecord.setCreator(getSessionUser().getUsername());
			aRecord.setCreate_time(DateTimeUtil.getNowTime());
			aRecord.setAdj_state(FnDfAdjustStateEnum.WeiShengCheng.getValue());
			aRecord.setOrderType(order.getCwbordertypeid());
			fnDfAdjustmentRecordDAO.creAdjustmentRecord(aRecord);
		}
	}
}