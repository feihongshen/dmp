package cn.explink.b2c.auto.order.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.auto.order.service.MQGetOrderDataService;
import cn.explink.b2c.auto.order.service.PeisongOrderService;
import cn.explink.b2c.auto.order.vo.InfDmpOrderSendVO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.controller.MQCwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Customer;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;

/**
 * 配送单订单处理
 */
@Component("peisongOrderHandler")
public class PeisongOrderHandler implements IOrderHandler{
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	MQGetOrderDataService mQGetOrderDataService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	PeisongOrderService peisongOrderService;
	protected Logger logger = LoggerFactory.getLogger(PeisongOrderHandler.class);

	@Override
	@Transactional
	public void dealWith(InfDmpOrderSendVO order,VipShop vipshop){
		/*****************************add start ********************************/
		//add by 周欢     根据承运商编码和客户id筛选订单    2016-07-15
		JointEntity jointEntityByShipper = this.jiontDAO.getDetialJointEntityByShipperNoForUse("\""+order.getCustCode()+"\"",vipshop.getCustomerids());
		if(jointEntityByShipper == null){
			this.logger.info("tps订单下发接口，承运商对应的配置与接口设置客户id不符,承运商号：{},客户id:{}", order.getCustCode(),vipshop.getCustomerids());
			throw new CwbException("",FlowOrderTypeEnum.DaoRuShuJu.getValue(),"TPS接口未开启接收配送单开关");
		}
		/*****************************add end ********************************/
		//配送单接口数据导入
		if(vipshop.getIsGetPeisongFlag()==1){
			Customer customer=customerDAO.getCustomerById(Long.valueOf(vipshop.getCustomerids()));
			//返回的报文订单信息解析
			MQCwbOrderDTO cwbOrder = peisongOrderService.peisongJsonDetailInfo(vipshop,order,customer.getMpsswitch());
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
			this.logger.info("TPS接口未开启接收配送单开关");
	    	throw new CwbException("",FlowOrderTypeEnum.DaoRuShuJu.getValue(),"TPS接口未开启接收配送单开关");
		}
		
	}
	

}
