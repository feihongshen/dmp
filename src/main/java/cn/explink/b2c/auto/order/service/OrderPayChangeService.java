package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ZhiFuApplyDao;
import cn.explink.dao.interfac.IBranceReportAdjustDao;
import cn.explink.dao.interfac.ICustomerAccountDao;
import cn.explink.domain.BranceReportAdjustPO;
import cn.explink.domain.CustomerAccountPO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.ZhiFuApplyView;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.SettleTimeTypeEnum;
import cn.explink.enumutil.SettleTypeEnum;
import cn.explink.util.DateTimeUtil;
@Service("orderPayChangeService")
@Transactional
public class OrderPayChangeService {
	private Logger logger = LoggerFactory.getLogger(OrderPayChangeService.class);
	@Autowired
	@Qualifier("branceReportAdjustDao")
	private IBranceReportAdjustDao branceReportAdjustDao;
	
	@Autowired
	@Qualifier("customerAccountDao")
	private ICustomerAccountDao customerAccountDao;
	
	@Autowired
	private CwbDAO orderDao ;
	
	@Autowired
	private OrderFlowDAO orderFlowDao ;
	
	@Autowired
	private ZhiFuApplyDao zhiFuApplyDao ;
	
	
	/**
	 * 确认通过修改订单金额
	 * @param applyid
	 * @param confirmname
	 * @param confirmtime
	 */
	public void updateStateConfirmPass(String applyid, String confirmname, String confirmtime){
		ZhiFuApplyView  payView =  this.zhiFuApplyDao.findZFAByApplyId(applyid) ;
		if(payView == null){
			return ;
		}
		CustomerAccountPO customerAccountPO = this.customerAccountDao.findCustomer(String.valueOf(payView.getCustomerid())) ;
		if(customerAccountPO != null){
			payView.setConfirmtime(confirmtime);
			this.addBranceReportAdjustForChange(customerAccountPO , payView);
		}
	}
	
	/**
	 * 修改订单金额时，添加应付甲方调整记录
	 * @param customerAccountPO
	 */
	private void addBranceReportAdjustForChange(CustomerAccountPO customerAccountPO , ZhiFuApplyView  payView){
		// 买单结算类型客户
	   if(SettleTypeEnum.pay.getValue().equals(customerAccountPO.getSettleType())){
		   BigDecimal applyreceivablefee = payView.getApplyreceivablefee() ;
		   BigDecimal receivablefee = payView.getReceivablefee() ;
		   BigDecimal adjustAmount = BigDecimal.ZERO ;
		   if(applyreceivablefee != null && receivablefee != null){
			   adjustAmount = applyreceivablefee.subtract(receivablefee) ;
		   }
		   if(adjustAmount == null || adjustAmount.compareTo(BigDecimal.ZERO) == 0){
				return ;
		   }
		   CwbOrder cwb =  this.orderDao.getCwbByCwb(payView.getCwb()) ;
		   if(cwb == null){
			  return ; 
		   }
		  if(SettleTimeTypeEnum.customerDeliver.getValue().equals(customerAccountPO.getSettleTimeType())){
			 // 订单发货客户为买单结算且按客户发货时间
			  String emailDate = cwb.getEmaildate() ;
			  if(!StringUtils.isEmpty(emailDate) && !StringUtils.isEmpty(payView.getConfirmtime())
					  && DateTimeUtil.dateDiff("day", DateTimeUtil.StringToDate(emailDate), DateTimeUtil.StringToDate(payView.getConfirmtime())) != 0)
			   this.addBranceReportAdjust(cwb, adjustAmount);
		   }else if(SettleTimeTypeEnum.sortingInventory.getValue().equals(customerAccountPO.getSettleTimeType())){
			  // 订单发货客户为买单结算且按分拣入库时间时
			   OrderFlow orderFlow = this.orderFlowDao.findOrderFlow(payView.getCwb(), 4) ;
			   if(orderFlow == null){
				   return ;
			   }
			   String warehouseDate = orderFlow.getCredate() == null ? "" : DateTimeUtil.formatDate(orderFlow.getCredate())  ;
			   if(!StringUtils.isEmpty(warehouseDate) && !StringUtils.isEmpty(payView.getConfirmtime())
						  && DateTimeUtil.dateDiff("day", DateTimeUtil.StringToDate(warehouseDate), DateTimeUtil.StringToDate(payView.getConfirmtime())) != 0)
				   this.addBranceReportAdjust(cwb, adjustAmount);
		   }
		}
	}
	
	/**
	 * 
	 * @param order
	 * @param disableDate
	 */
	public void disabledOrder(CwbOrder order , Date disableDate){
		CustomerAccountPO customerAccountPO = this.customerAccountDao.findCustomer(String.valueOf(order.getCustomerid())) ;
		if(customerAccountPO == null){
			return ;
		}
		// 买单结算类型客户
	    if(SettleTypeEnum.pay.getValue().equals(customerAccountPO.getSettleType())){
			BigDecimal adjustAmount = BigDecimal.ZERO ;
			if(order.getReceivablefee() != null){
				adjustAmount = adjustAmount.subtract(order.getReceivablefee()) ;
			}
			if(adjustAmount == null || adjustAmount.compareTo(BigDecimal.ZERO) == 0){
				return ;
			}
			String disableOrderCreateDate = disableDate == null ?"":DateTimeUtil.formatDate(disableDate) ;
	    	if(SettleTimeTypeEnum.customerDeliver.getValue().equals(customerAccountPO.getSettleTimeType())){
	    		// 订单发货客户为买单结算且按客户发货时间
	    		String emailDate = order.getEmaildate() ;
				  if(!StringUtils.isEmpty(emailDate) && !StringUtils.isEmpty(disableOrderCreateDate)
						  && DateTimeUtil.dateDiff("day", DateTimeUtil.StringToDate(emailDate), DateTimeUtil.StringToDate(disableOrderCreateDate)) != 0){
					  this.addBranceReportAdjust(order, adjustAmount);
				  }
	    	}else if(SettleTimeTypeEnum.sortingInventory.getValue().equals(customerAccountPO.getSettleTimeType())){
	    		// 订单发货客户为买单结算且按分拣入库时间时
				   OrderFlow orderFlow = this.orderFlowDao.findOrderFlow(order.getCwb(), 4) ;
				   if(orderFlow == null){
					   return ;
				   }
				   String warehouseDate = orderFlow.getCredate() == null ? "" : DateTimeUtil.formatDate(orderFlow.getCredate())  ;
				   if(!StringUtils.isEmpty(warehouseDate) && !StringUtils.isEmpty(disableOrderCreateDate)
							  && DateTimeUtil.dateDiff("day", DateTimeUtil.StringToDate(warehouseDate), DateTimeUtil.StringToDate(disableOrderCreateDate)) != 0)
					   this.addBranceReportAdjust(order, adjustAmount);
	    	}
	    }
	}
	
	/**
	 * 新增应付甲方调整记录表
	 * @param order
	 * @param adjustAmount
	 */
	private void addBranceReportAdjust(CwbOrder order , BigDecimal adjustAmount){
		BranceReportAdjustPO adjustPO = new BranceReportAdjustPO() ;
		adjustPO.setCustomerId(order.getCustomerid());
		adjustPO.setCreateTime(DateTimeUtil.getNowDate());
		adjustPO.setOrderNumber(order.getCwb());
		adjustPO.setOrderType(order.getCwbordertypeid()); 
		adjustPO.setOrderStatus(order.getCwbstate());
		adjustPO.setOperateStatus(order.getFlowordertype());
		adjustPO.setReceivableAmount(order.getReceivablefee());
		adjustPO.setPayAmount(order.getPaybackfee());
		adjustPO.setAdjustAmount(adjustAmount);
		this.branceReportAdjustDao.addBranceReportAdjust(adjustPO);
	}
}
