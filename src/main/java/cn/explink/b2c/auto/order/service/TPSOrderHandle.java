package cn.explink.b2c.auto.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.auto.order.vo.TPSOrder;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;

@Service
public class TPSOrderHandle {
	@Autowired
	TPSGetOrderDataService tPSGetOrderDataService;
	@Autowired
	AutoExceptionService autoExceptionService;
	
	protected Logger logger = LoggerFactory.getLogger(TPSOrderHandle.class);
	
	 //处理业务逻辑
	@Transactional
	public void handleOrderData(
    		TPSOrder order,VipShop vipshop,int vipshop_key,String msg,int mpsswitch){
    	//flagOrder = order.getCustOrderNo();
    	try{
    		if(order.getBusinessType()==20 || order.getBusinessType()==40){
    			tPSGetOrderDataService.extractedOXODataImport(order,vipshop);
    		}else{
    			//普通接口数据导入
    			if(null!=order){
    				if(order.getBusinessType()!=60 && order.getAddTime()==null){
    					this.logger.info("没有出仓时间");
    					//throw new CwbException(order.getCustOrderNo(),FlowOrderTypeEnum.DaoRuShuJu.getValue(),"没有出仓时间");
    				}
    				//返回的报文订单信息解析
    				CwbOrderDTO cwbOrder = tPSGetOrderDataService.parseXmlDetailInfo(vipshop,order,mpsswitch);
    				if (cwbOrder != null) {
    					//是否开启托运单模式，生成多个批次 0 不开启
        				if (vipshop.getIsTuoYunDanFlag() == 0) {
        					//普通单在没有开启托运单模式下，数据插入临时表
        					tPSGetOrderDataService.extractedDataImport(vipshop_key, vipshop, cwbOrder);
        				} else {
        					//普通单在开启托运单模式下，数据插入临时表
        					tPSGetOrderDataService.extractedDataImportByEmaildate(vipshop_key, vipshop, cwbOrder);
        				}
    				}
    			}
    		}
    	}catch(Exception e){
			e.printStackTrace();
			this.logger.error("TPS自动化数据转到临时表业务之前异常,cwb=" + order.getCustOrderNo() + "message:", e);
			CwbException ex=null;
			long flowordertye=FlowOrderTypeEnum.DaoRuShuJu.getValue();
			if(e instanceof CwbException){
				flowordertye=((CwbException)e).getFlowordertye();
				ex=(CwbException)e;
			}else{
				ex=new CwbException(order.getCustOrderNo(),flowordertye,e.getMessage());
			}
			throw ex;
    	}
    }
}
