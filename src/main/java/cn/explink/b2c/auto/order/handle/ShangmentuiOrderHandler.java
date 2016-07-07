package cn.explink.b2c.auto.order.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.b2c.auto.order.service.MQGetOrderDataService;
import cn.explink.b2c.auto.order.service.ShangmentuiOrderService;
import cn.explink.b2c.auto.order.vo.InfDmpOrderSendVO;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.controller.MQCwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Customer;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;

/**
 * 上门退订单处理
 * 注意揽退单的新增、修改、取消顺序
 */
@Component("shangmentuiOrderHandler")
public class ShangmentuiOrderHandler implements IOrderHandler{
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	MQGetOrderDataService mQGetOrderDataService;
	@Autowired
	ShangmentuiOrderService shangmentuiService;
	
	protected Logger logger = LoggerFactory.getLogger(ShangmentuiOrderHandler.class);

	@Override
	public void dealWith(InfDmpOrderSendVO order,VipShop vipshop) {
		//上门退订单接口数据导入
		if(vipshop.getIsGetShangmentuiFlag()==1){
			Customer customer=customerDAO.getCustomerById(Long.valueOf(vipshop.getCustomerids()));
			//返回的报文订单信息解析
			MQCwbOrderDTO cwbOrder = shangmentuiService.ShangmentuiJsonDetailInfo(vipshop,order,customer.getMpsswitch());
			if (cwbOrder != null) {
				//是否开启托运单模式，生成多个批次 0 不开启
				if (vipshop.getIsTuoYunDanFlag() == 0) {
					//普通单在没有开启托运单模式下，数据插入临时表
					mQGetOrderDataService.insertTempOnAttemperOpen(vipshop, cwbOrder);
				} else {
					//普通单在开启托运单模式下，数据插入临时表
					mQGetOrderDataService.insertTempOnAttemperClose(vipshop, cwbOrder);
				}
			}
		}else{
			this.logger.info("TPS接口未开启接收上门退订单开关");
	    	throw new CwbException("",FlowOrderTypeEnum.DaoRuShuJu.getValue(),"TPS接口未开启接收上门退订单开关");
		}
		
	}

}
