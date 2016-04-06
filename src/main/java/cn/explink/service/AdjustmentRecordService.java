package cn.explink.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.AdjustmentRecordDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.FnCustomerBillDetailDAO;
import cn.explink.dao.FnOrgBillDetailDAO;
import cn.explink.domain.AdjustmentRecord;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.FnCustomerBill;
import cn.explink.domain.User;
import cn.explink.enumutil.CustomerBillDateTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.VerificationEnum;
import cn.explink.util.DateTimeUtil;

/**
 * 调整单服务类
 *
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

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	// ===================================================Refactored by jiangyu
	// 2015-04-03================================================================================

	/**
	 * 创建调整单的记录 --added by jiangyu
	 *
	 * @param cwbOrder
	 * @param page_payback_fee
	 *            页面修改金额后的应退金额
	 * @param page_receive_fee
	 *            页面修改金额后的应收金额
	 * @param remark
	 * @param username
	 */
	public void processAdjusRecordByMoney(CwbOrder cwbOrder, BigDecimal page_payback_fee, BigDecimal page_receive_fee, String remark, String username) {
		/**
		 * 2015/11/25 added by zhouguoting 用来代替上面注释掉的代码
		 *
		 */
		int cwbOrderTypeId = cwbOrder.getCwbordertypeid();
		AdjustmentRecord aRecord = null;
		if ((cwbOrderTypeId == CwbOrderTypeIdEnum.Peisong.getValue()) || (cwbOrderTypeId == CwbOrderTypeIdEnum.OXO.getValue())) {
			/*
			 * Customer cust =
			 * customerdao.getCustomerById(cwbOrder.getCustomerid()); if(cust ==
			 * null || cust.getPaytype() == 0){
			 * logger.error("订单【"+cwbOrder.getCwb
			 * ()+"】没有客户信息，或未配置客户的结算类型,订单支付信息修改审核生成调整记录失败"); throw new
			 * RuntimeException
			 * ("订单【"+cwbOrder.getCwb()+"】没有客户信息,订单支付信息修改审核生成调整记录失败"); }
			 */// deleted by zhouguoting 2015/12/2
			/**
			 * 买单结算类型的客户的订单,且已经生成过“应付配送货款账单”,在修改支付金额的时候才需要生成调整记录
			 */
			if (cwbOrder.getFncustomerpayablebillid() != 0) {
				FnCustomerBill custBill = this.FnCustomerBillDetaildao.getFnCustomerBillById(cwbOrder.getFncustomerpayablebillid());
				if ((custBill != null) && (CustomerBillDateTypeEnum.audit.getValue().intValue() != (int) custBill.getDateType())) {
					aRecord = new AdjustmentRecord();
					this.buildAdjustRecord4ModifyMoney(cwbOrder, page_payback_fee, page_receive_fee, aRecord);
				}
			}

		} else if (cwbOrderTypeId == CwbOrderTypeIdEnum.Shangmentui.getValue()) {

			// TODO 修改上门退类型订单支付金额时 生成调整记录的逻辑

		} else if (cwbOrderTypeId == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {

			// TODO 修改上门换类型订单支付金额时 生成调整记录的逻辑
		}

		if (aRecord != null) {
			this.adjustmentRecordDAO.creAdjustmentRecord(aRecord);
		}
	}

	/**
	 * 重置反馈状态，对应生成调整单记录
	 *
	 * @param cwb
	 * @param payWayId
	 * @param newPayWayId
	 *            update express_ops_applyeditdeliverystate set
	 *            shenhestate=3,edituserid=?,edittime=?,ishandle=1 where cwb=?
	 *            order by id desc limit 1
	 */
	public void createAdjustment4ReFeedBack(String cwb) {
		/**
		 * 2015/11/25 added by zhougtuoing 以下代码代替上方注释掉的代码
		 */

		CwbOrder cwbOrder = this.cwbDao.getCwbByCwb(cwb);

		/*
		 * Customer cust =
		 * customerdao.getCustomerById(cwbOrder.getCustomerid()); if(cust ==
		 * null || cust.getPaytype() == 0){
		 * logger.error("订单【"+cwbOrder.getCwb()+
		 * "】没有客户信息，或未配置客户的结算类型,订单重置反馈审核生成调整记录失败"); throw new
		 * RuntimeException("订单【"
		 * +cwbOrder.getCwb()+"】没有客户信息，或未配置客户的结算类型,订单重置反馈审核生成调整记录失败"); }
		 */// deletedy by zhouguoting 2015/12/2

		/**
		 * //结算类型为“返款结算”的客户的订单重置反馈的时候,如果之前已经生成过“应付配送货款账单”需要生成调整单
		 */
		if (cwbOrder.getFncustomerpayablebillid() != 0) {
			FnCustomerBill custBill = this.FnCustomerBillDetaildao.getFnCustomerBillById(cwbOrder.getFncustomerpayablebillid());
			if ((custBill != null) && (CustomerBillDateTypeEnum.audit.getValue().intValue() == (int) custBill.getDateType())) {
				AdjustmentRecord aRecord = new AdjustmentRecord();
				aRecord.setOrder_no(cwbOrder.getCwb());
				aRecord.setOrder_id(cwbOrder.getOpscwbid());
				// 不允许为空
				aRecord.setBill_no("");
				aRecord.setAdjust_bill_no("");
				aRecord.setCustomer_id(cwbOrder.getCustomerid());
				aRecord.setReceive_fee(cwbOrder.getReceivablefee());
				aRecord.setRefund_fee(cwbOrder.getPaybackfee());
				aRecord.setModify_fee(BigDecimal.ZERO);
				BigDecimal adjustAmount = cwbOrder.getReceivablefee() == null ? BigDecimal.ZERO : BigDecimal.ZERO.subtract(cwbOrder.getReceivablefee());
				aRecord.setAdjust_amount(adjustAmount);
				aRecord.setRemark("已审核账单重置反馈状态!");
				aRecord.setCreator(this.getSessionUser().getUsername());
				aRecord.setCreate_time(DateTimeUtil.getNowTime());
				aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
				aRecord.setOrder_type(cwbOrder.getCwbordertypeid());
				this.adjustmentRecordDAO.creAdjustmentRecord(aRecord);
			}
		}

	}

	/**
	 * 修改支付方式生成调整单的记录
	 *
	 * @param cwb
	 * @param payWayId
	 * @param newPayWayId
	 */
	public void createAdjustmentRecordByPayType(String cwb, int payWayId, int newPayWayId) {
		// 只知道订单号
		List<FnCustomerBill> fnCustomerBillDetails = new ArrayList<FnCustomerBill>();
		List<AdjustmentRecord> adjustmentRecords = new ArrayList<AdjustmentRecord>();
		// 创建一个新的调整单记录对象,用于保存调整单记录
		AdjustmentRecord aRecord = new AdjustmentRecord();
		// 查询出对应订单号的账单详细信息
		CwbOrder cwbOrder = this.cwbDao.getCwbByCwb(cwb);
		fnCustomerBillDetails = this.FnCustomerBillDetaildao.getFnCustomerBillDetailByCwb(cwb);
		if ((fnCustomerBillDetails != null) && (fnCustomerBillDetails.size() > 0)) {
			FnCustomerBill targetBill = fnCustomerBillDetails.get(0);
			// 表示存在该订单的账单信息
			// 表示有对应订单信息,下面判断是否生成过调整单,查询调整单表 fn_adjustment_record表
			adjustmentRecords = this.adjustmentRecordDAO.getAdjustmentRecordByCwb(cwb);
			this.modifyPayWay2CreateRecord(payWayId, newPayWayId, aRecord, cwbOrder, targetBill);
		}
	}

	/**
	 * 修改支付方式创建新的记录 --modified by jiangyu
	 *
	 * @param payWayId
	 * @param newPayWayId
	 * @param aRecord
	 * @param cwbOrder
	 * @param targetBill
	 */
	private void modifyPayWay2CreateRecord(int payWayId, int newPayWayId, AdjustmentRecord aRecord, CwbOrder cwbOrder, FnCustomerBill targetBill) {
		aRecord.setOrder_no(cwbOrder.getCwb());
		// 不允许为空
		aRecord.setBill_no(targetBill.getBillNo());
		aRecord.setAdjust_bill_no("");
		aRecord.setCustomer_id(cwbOrder.getCustomerid());

		aRecord.setReceive_fee(cwbOrder.getReceivablefee());
		aRecord.setRefund_fee(cwbOrder.getPaybackfee());
		aRecord.setModify_fee(BigDecimal.ZERO);
		aRecord.setAdjust_amount(BigDecimal.ZERO);
		aRecord.setRemark(PaytypeEnum.getByValue(payWayId).getText() + "修改成" + PaytypeEnum.getByValue(newPayWayId).getText());

		aRecord.setCreator(this.getSessionUser().getUsername());
		aRecord.setCreate_time(DateTimeUtil.getNowTime());
		aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
		aRecord.setOrder_type(cwbOrder.getCwbordertypeid());

		this.adjustmentRecordDAO.creAdjustmentRecord(aRecord);
	}

	/**
	 * 重置审核状态-创建调整单记录
	 *
	 * @param payWayId
	 * @param newPayWayId
	 * @param aRecord
	 * @param cwbOrder
	 *            TODO
	 * @param targetBill
	 */
	private void reFeedBack2CreateRecord(AdjustmentRecord aRecord, CwbOrder cwbOrder, FnCustomerBill targetBill) {
		aRecord.setOrder_no(cwbOrder.getCwb());
		// 不允许为空
		aRecord.setBill_no(targetBill.getBillNo());
		// aRecord.setBill_id(targetBill.getId());
		aRecord.setAdjust_bill_no("");
		aRecord.setCustomer_id(cwbOrder.getCustomerid());

		aRecord.setReceive_fee(cwbOrder.getReceivablefee());
		aRecord.setRefund_fee(cwbOrder.getPaybackfee());
		aRecord.setModify_fee(BigDecimal.ZERO);

		BigDecimal adjustAmount = ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) > 0)) ? BigDecimal.ZERO.subtract(cwbOrder.getReceivablefee())
				: cwbOrder.getPaybackfee();
		aRecord.setAdjust_amount(adjustAmount);
		aRecord.setRemark("已审核账单重置反馈状态!");

		aRecord.setCreator(this.getSessionUser().getUsername());
		aRecord.setCreate_time(DateTimeUtil.getNowTime());
		aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
		aRecord.setOrder_type(cwbOrder.getCwbordertypeid());

		this.adjustmentRecordDAO.creAdjustmentRecord(aRecord);
	}

	/**
	 * 订单失效-创建调整单记录
	 */
	private void createRecordFowLosecwbBatch(CwbOrder cwbOrder) {
		AdjustmentRecord aRecord = new AdjustmentRecord();
		aRecord.setOrder_no(cwbOrder.getCwb());
		aRecord.setOrder_id(cwbOrder.getOpscwbid());
		// 不允许为空
		aRecord.setBill_no("");
		aRecord.setAdjust_bill_no("");
		aRecord.setCustomer_id(cwbOrder.getCustomerid());

		aRecord.setReceive_fee(cwbOrder.getReceivablefee());
		aRecord.setRefund_fee(cwbOrder.getPaybackfee());
		aRecord.setModify_fee(BigDecimal.ZERO);

		BigDecimal adjustAmount = ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) > 0)) ? BigDecimal.ZERO.subtract(cwbOrder.getReceivablefee())
				: cwbOrder.getPaybackfee();
		aRecord.setAdjust_amount(adjustAmount);
		aRecord.setRemark("订单状态失效!");

		aRecord.setCreator(this.getSessionUser().getUsername());
		aRecord.setCreate_time(DateTimeUtil.getNowTime());
		aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
		aRecord.setOrder_type(cwbOrder.getCwbordertypeid());

		this.adjustmentRecordDAO.creAdjustmentRecord(aRecord);
	}

	/**
	 * 创建一个新的记录 --added by jiangyu
	 *
	 * @param order
	 * @param page_payback_fee
	 *            页面返回修改后的应退金额
	 * @param page_receive_fee
	 *            页面返回修改后的应收金额
	 * @param aRecord
	 * @param targetBill
	 */
	private void createNewAdjustRecordByMoney(CwbOrder order, BigDecimal page_payback_fee, BigDecimal page_receive_fee, AdjustmentRecord aRecord, FnCustomerBill targetBill) {
		// 订单类型
		int orderType = order.getCwbordertypeid();
		aRecord.setOrder_no(order.getCwb());
		// 不允许为空
		aRecord.setBill_no(targetBill.getBillNo());
		aRecord.setAdjust_bill_no("");
		aRecord.setCustomer_id(order.getCustomerid());

		aRecord.setReceive_fee(order.getReceivablefee());
		aRecord.setRefund_fee(order.getPaybackfee());

		if (orderType == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			// 没有生成过调整单新加一个记录
			aRecord.setModify_fee(page_payback_fee);
			aRecord.setAdjust_amount(order.getPaybackfee().subtract(page_payback_fee));
			aRecord.setRemark(order.getPaybackfee() + "元修改成" + page_payback_fee + "元");
		} else if (orderType == CwbOrderTypeIdEnum.Peisong.getValue()) {
			// 没有生成过调整单新加一个记录
			// aRecord.setRefund_fee(page_payback_fee);
			aRecord.setModify_fee(page_receive_fee);
			aRecord.setAdjust_amount(page_receive_fee.subtract(order.getReceivablefee()));// 通过原始金额减去调整后金额产生调整差额
			aRecord.setRemark(order.getReceivablefee() + "元修改成" + page_receive_fee + "元");

		} else {// 上门换的记录 TODO
			if ((page_payback_fee.doubleValue() > 0) && (page_receive_fee.doubleValue() <= 0)) {
				// 没有生成过调整单新加一个记录
				aRecord.setModify_fee(page_payback_fee);
				aRecord.setAdjust_amount(order.getPaybackfee().subtract(page_payback_fee));
				aRecord.setRemark(order.getPaybackfee() + "元修改成" + page_payback_fee + "元");
			} else if ((page_receive_fee.doubleValue() > 0) && (page_payback_fee.doubleValue() <= 0)) {
				aRecord.setModify_fee(page_receive_fee);
				aRecord.setAdjust_amount(page_receive_fee.subtract(order.getReceivablefee()));// 通过原始金额减去调整后金额产生调整差额
				aRecord.setRemark(order.getReceivablefee() + "元修改成" + page_receive_fee + "元");

			}
		}

		aRecord.setCreator(this.getSessionUser().getUsername());
		aRecord.setCreate_time(DateTimeUtil.getNowTime());
		aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
		aRecord.setOrder_type(orderType);

		this.adjustmentRecordDAO.creAdjustmentRecord(aRecord);
	}

	/**
	 * 修改订单支付金额创建客户调整记录
	 *
	 * @param order
	 * @param page_payback_fee
	 * @param page_receive_fee
	 * @param aRecord
	 */
	private void buildAdjustRecord4ModifyMoney(CwbOrder order, BigDecimal page_payback_fee, BigDecimal page_receive_fee, AdjustmentRecord aRecord) {
		// 订单类型
		int orderType = order.getCwbordertypeid();

		if (orderType == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			/**
			 * TODO 上门退类型的订单先不做调整单 aRecord.setModify_fee(page_payback_fee);
			 * aRecord.setAdjust_amount(order.getPaybackfee().subtract(
			 * page_payback_fee));
			 * aRecord.setRemark(order.getPaybackfee()+"元修改成"
			 * +page_payback_fee+"元");
			 **/
		} else if (orderType == CwbOrderTypeIdEnum.Peisong.getValue()) {
			aRecord.setOrder_no(order.getCwb());
			aRecord.setOrder_id(order.getOpscwbid());
			// 不允许为空
			aRecord.setBill_no("");
			aRecord.setAdjust_bill_no("");
			aRecord.setCustomer_id(order.getCustomerid());
			aRecord.setReceive_fee(order.getReceivablefee());
			aRecord.setRefund_fee(order.getPaybackfee());
			aRecord.setModify_fee(page_receive_fee);
			aRecord.setAdjust_amount(page_receive_fee.subtract(order.getReceivablefee()));// 通过原始金额减去调整后金额产生调整差额
			aRecord.setRemark(order.getReceivablefee() + "元修改成" + page_receive_fee + "元");
			aRecord.setCreator(this.getSessionUser().getUsername());
			aRecord.setCreate_time(DateTimeUtil.getNowTime());
			aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
			aRecord.setOrder_type(orderType);

		} else if (orderType == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {// 上门换的记录
																				// TODO
			/**
			 * if(page_payback_fee.doubleValue()>0&&page_receive_fee.doubleValue
			 * ()<=0){ //没有生成过调整单新加一个记录 aRecord.setModify_fee(page_payback_fee);
			 * aRecord.setAdjust_amount(order.getPaybackfee().subtract(
			 * page_payback_fee));
			 * aRecord.setRemark(order.getPaybackfee()+"元修改成"
			 * +page_payback_fee+"元"); }else
			 * if(page_receive_fee.doubleValue()>0&&
			 * page_payback_fee.doubleValue()<=0){
			 * aRecord.setModify_fee(page_receive_fee);
			 * aRecord.setAdjust_amount(
			 * page_receive_fee.subtract(order.getReceivablefee
			 * ()));//通过原始金额减去调整后金额产生调整差额
			 * aRecord.setRemark(order.getReceivablefee
			 * ()+"元修改成"+page_receive_fee+"元");
			 *
			 * }
			 **/
		}

	}

	/**
	 *
	 * added by zhouguoting 2015/11/25
	 * 如果审核状态为“配送成功”，如果结算类型是“返款结算”的客户的订单，且之前已经生成过
	 * “应付配送货款账单”,需要生成配送货款调整记录，配送货款调整金额= 应收金额。
	 *
	 * @param cwbOrder
	 * @param deliverystate
	 */
	public void createAdjustment4GoToClassConfirm(CwbOrder cwbOrder, DeliveryState deliverystate) {
		/*
		 * Customer cust =
		 * customerdao.getCustomerById(cwbOrder.getCustomerid()); if(cust ==
		 * null || cust.getPaytype() == 0){
		 * logger.error("订单【"+cwbOrder.getCwb()+
		 * "】没有客户信息，或未配置客户的结算类型,订单反馈审核时无法判断是否要生成调整记录"); return; }
		 */// deleted by zhouguoting 2015/12/2

		/**
		 * 如果之前已经生成过“应付配送货款账单”,且结算类型是“返款结算”的客户的订单（如果之前生成过的账单的时间类型是归班审核时间即认为是返款结算
		 * ）,需要生成配送货款调整记录，配送货款调整金额= 应收金额。
		 */
		long payableBillId = cwbOrder.getFncustomerpayablebillid();
		if (payableBillId != 0) {
			FnCustomerBill custBill = null;
			try{
				custBill = this.FnCustomerBillDetaildao.getFnCustomerBillById(payableBillId);
			}catch(EmptyResultDataAccessException e){
				//如果没找到，可以忽略，后面覆盖即可
			}
			if ((custBill != null) && (CustomerBillDateTypeEnum.audit.getValue().intValue() == (int) custBill.getDateType())) {
				AdjustmentRecord aRecord = new AdjustmentRecord();
				aRecord.setOrder_no(cwbOrder.getCwb());
				aRecord.setOrder_id(cwbOrder.getOpscwbid());
				// 不允许为空
				aRecord.setBill_no("");
				aRecord.setAdjust_bill_no("");
				aRecord.setCustomer_id(cwbOrder.getCustomerid());

				aRecord.setReceive_fee(cwbOrder.getReceivablefee());
				aRecord.setRefund_fee(cwbOrder.getPaybackfee());
				aRecord.setModify_fee(BigDecimal.ZERO);
				aRecord.setAdjust_amount(cwbOrder.getReceivablefee());

				aRecord.setRemark("再次归班审核生成调整记录");

				aRecord.setCreator(this.getSessionUser().getUsername());
				aRecord.setCreate_time(DateTimeUtil.getNowTime());
				aRecord.setStatus(VerificationEnum.WeiHeXiao.getValue());
				aRecord.setOrder_type(cwbOrder.getCwbordertypeid());
				this.adjustmentRecordDAO.creAdjustmentRecord(aRecord);
			}
		}

	}

	/**
	 * 订单失效，对应生成调整单记录
	 */
	public void createAdjustmentForLosecwbBatch(CwbOrder cwbOrder) {
		Long customerBillId = cwbOrder.getFncustomerpayablebillid(); // 生成过应付客户账单的订单失效生成调整单
		if (customerBillId != 0) {
			FnCustomerBill customerBill = this.FnCustomerBillDetaildao.getFnCustomerBillById(customerBillId);
			if (customerBill != null) {
				if (CustomerBillDateTypeEnum.audit.getValue().intValue() != (int) customerBill.getDateType()) {
					this.createRecordFowLosecwbBatch(cwbOrder);
				}
			}
		}
	}
}